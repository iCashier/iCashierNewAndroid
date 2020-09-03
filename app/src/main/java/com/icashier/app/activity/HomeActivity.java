package com.icashier.app.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icashier.app.R;
import com.icashier.app.autoservices.BackgroundService;
import com.icashier.app.autoservices.ResponseBroadcastReceiver;
import com.icashier.app.autoservices.ToastBroadcastReceiver;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.ActivityHomeBinding;
import com.icashier.app.dialog.AutomaticOrderDetailDialog;
import com.icashier.app.dialog.CalculatorDialog;
import com.icashier.app.dialog.CashierLoginDialog;
import com.icashier.app.fragment.AddCategoriesFragment;
import com.icashier.app.fragment.AddMealFragment;
import com.icashier.app.fragment.AppSettingsFragment;
import com.icashier.app.fragment.CashierFragment;
import com.icashier.app.fragment.CheckoutFragment;
import com.icashier.app.fragment.DealsFragment;
import com.icashier.app.fragment.GenerateCodeFragment;
import com.icashier.app.fragment.InvoiceFragment;
import com.icashier.app.fragment.ItemsFragment;
import com.icashier.app.fragment.MerchantHomeFragment;
import com.icashier.app.fragment.NewDealsFragment;
import com.icashier.app.fragment.NewItemFragment;
import com.icashier.app.fragment.OrdersFragment;
import com.icashier.app.fragment.ReportsFragment;
import com.icashier.app.fragment.ResetPasswordFragment;
import com.icashier.app.fragment.TransactionsFragment;
import com.icashier.app.fragment.UnderDevelopmentFragment;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.DateUtils;
import com.icashier.app.helper.ImagePickerUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CalculatorListener;
import com.icashier.app.listener.DialogDismissListener;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.OnlineStatusResponse;
import com.icashier.app.model.OrderListResponse;
import com.icashier.app.model.SigninResponse;
import com.icashier.app.printer.AutoPrinterFuntionality;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.callback.Callback;

import static com.icashier.app.helper.ViewAnimationUtils.collapse;
import static com.icashier.app.helper.ViewAnimationUtils.expand;

public class HomeActivity extends FragmentActivity implements View.OnClickListener {

    public ActivityHomeBinding binding;
    public HomeActivity context;
    boolean showing = false;
    boolean isArabic;
    SigninResponse.ResultBean userData;
    RestClient.ApiRequest apiRequest;
    private long starttime = 0;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            if (intent.hasExtra(AppConstant.PUSH_TYPE)) {
                if (intent.getStringExtra(AppConstant.PUSH_TYPE).equals(AppConstant.NOTI_MERCHANT_STATUS)) {
                    // start automatic printer
                    SharedPrefManager sharedPrefManager=SharedPrefManager.getInstance(HomeActivity.this);
                    if (sharedPrefManager.getInt(AppConstant.PRINTER_AUTO_ON, 0) == 1) {
                        SharedPrefManager tempPrefenceManager = SharedPrefManager.getInstance(context);
                        List<OrderListResponse.ResultBean> tempPrintList = loadSharedPreferencesLogList(tempPrefenceManager);
                        if (tempPrintList.size() > 0) {
                            autoPrint();
                        }
                    }
                    callGetStatusApi();

                }
            }
        }
    };

    // automatic printer timer task
    /*
    * **********************************start
    * */
  //  ResponseBroadcastReceiver broadcastReceiver;




    // *******************************************************
    public static void saveSharedPreferencesLogList(SharedPrefManager pref, List<OrderListResponse.ResultBean> printLog) {
        Gson gson = new Gson();
        String json = gson.toJson(printLog);
        pref.saveString(AppConstant.PRINTER_PUSH_NOTIFICATION, json);
    }

    public static List<OrderListResponse.ResultBean> loadSharedPreferencesLogList(SharedPrefManager pref) {
        List<OrderListResponse.ResultBean> callLog = new ArrayList<OrderListResponse.ResultBean>();
        // SharedPreferences mPrefs = context.getSharedPreferences(Constant.CALL_HISTORY_RC, context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString(AppConstant.PRINTER_PUSH_NOTIFICATION, "");
        if (json.isEmpty()) {
            callLog = new ArrayList<OrderListResponse.ResultBean>();
        } else {
            Type type = new TypeToken<List<OrderListResponse.ResultBean>>() {
            }.getType();
            callLog = gson.fromJson(json, type);
        }
        return callLog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        binding = DataBindingUtil.setContentView(context, R.layout.activity_home);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frame, new MerchantHomeFragment(), MerchantHomeFragment.class.getSimpleName()).commit();
        setSelection(binding.llMHome, binding.imgHome, binding.tvMHome);

        setOnClickListeners();
        //to rotate views for arabic
        Utilities.rotateViews(binding.btnDrawer, binding.flLogout);
        getExtras();
        setData();
        setCheckChangeListener();
        updateDeviceTokenApi();
        callGetStatusApi();

      /*  broadcastReceiver= new ResponseBroadcastReceiver();
        IntentFilter intentFilter= new IntentFilter();
        intentFilter.addAction(BackgroundService.ACTION);
        registerReceiver(broadcastReceiver,intentFilter);*/

        /*scheduleAlarm();*/
    }

    private void scheduleAlarm() {
        Intent toastIntent= new Intent(getApplicationContext(), ToastBroadcastReceiver.class);
        PendingIntent toastAlarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, toastIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        long startTime=System.currentTimeMillis(); //alarm starts immediately
        AlarmManager backupAlarmMgr=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        backupAlarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,startTime,1000,toastAlarmIntent); // alarm will repeat after every 15 minutes
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter(AppConstant.NOTI_MERCHANT_STATUS));
        // start automatic printer
        SharedPrefManager sharedPrefManager=SharedPrefManager.getInstance(HomeActivity.this);
        if (sharedPrefManager.getInt(AppConstant.PRINTER_AUTO_ON, 0) == 1) {
            SharedPrefManager tempPrefenceManager = SharedPrefManager.getInstance(context);
            List<OrderListResponse.ResultBean> tempPrintList = loadSharedPreferencesLogList(tempPrefenceManager);
            if (tempPrintList.size() > 0) {
                autoPrint();
            }
        }
      //  autoPrint();

    }

    @Override
    protected void onPause() {
        super.onPause();
       // unregisterReceiver(broadcastReceiver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void autoPrint() {
        // this is for automatic print from notification
        HomeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!context.isFinishing()) {
                    try {
                        SharedPrefManager tempPrefenceManager = SharedPrefManager.getInstance(HomeActivity.this);
                        List<OrderListResponse.ResultBean> tempPrintList = loadSharedPreferencesLogList(tempPrefenceManager);
                        if (tempPrintList.size() > 0) {
                            for (int i = 0; i < tempPrintList.size(); i++) {
                                AutoPrinterFuntionality orderDetailDialog = new AutoPrinterFuntionality(HomeActivity.this, tempPrintList.get(i), i);
                              /*  orderDetailDialog.show();
                                orderDetailDialog.setCancelable(true);
                                orderDetailDialog.setOnDismissListener(D -> {
                                    orderDetailDialog.dismiss();
                                });*/
                                orderDetailDialog.startPrintingReceipt();
                            }
                        }
                    } catch (WindowManager.BadTokenException e) {
                        Log.e("WindowManagerBad ", e.toString());
                    }
                }
            }
        });
    }

    private void setCheckChangeListener() {
        binding.switchOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                callChangeStatusApi();
                if (isChecked) {
                    binding.viewOnlineCollapsed.setSelected(true);
                    binding.viewOnlineExpanded.setSelected(true);
                    binding.tvOnline.setText(getString(R.string.online));
                } else {
                    binding.viewOnlineCollapsed.setSelected(false);
                    binding.viewOnlineExpanded.setSelected(false);
                    binding.tvOnline.setText(getString(R.string.offline));

                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // start automatic printer
        SharedPrefManager sharedPrefManager=SharedPrefManager.getInstance(HomeActivity.this);
        if (sharedPrefManager.getInt(AppConstant.PRINTER_AUTO_ON, 0) == 1) {
            SharedPrefManager tempPrefenceManager = SharedPrefManager.getInstance(context);
            List<OrderListResponse.ResultBean> tempPrintList = loadSharedPreferencesLogList(tempPrefenceManager);
            if (tempPrintList.size() > 0) {
                autoPrint();
            }
        }
        if (intent.hasExtra(AppConstant.FROM_PUSH) && intent.getBooleanExtra(AppConstant.FROM_PUSH, false)) {
            if (getSupportFragmentManager().findFragmentByTag(OrdersFragment.class.getSimpleName()) == null) {
                setSelection(binding.llCashier, binding.imgCashier, binding.tvCashier);
                replaceFragment(new OrdersFragment());
            } else if (!getSupportFragmentManager().findFragmentByTag(OrdersFragment.class.getSimpleName()).isVisible()) {
                setSelection(binding.llCashier, binding.imgCashier, binding.tvCashier);
                replaceFragment(new OrdersFragment());
                getSupportFragmentManager().popBackStack();

            } else if (getSupportFragmentManager().findFragmentByTag(OrdersFragment.class.getSimpleName()).isVisible()) {
                ((OrdersFragment) getSupportFragmentManager().findFragmentByTag(OrdersFragment.class.getSimpleName())).callGetOrdersApi();
            }
        }

    }

    private void getExtras() {
        // start automatic printer
        SharedPrefManager sharedPrefManager=SharedPrefManager.getInstance(HomeActivity.this);
        if (sharedPrefManager.getInt(AppConstant.PRINTER_AUTO_ON, 0) == 1) {
            SharedPrefManager tempPrefenceManager = SharedPrefManager.getInstance(context);
            List<OrderListResponse.ResultBean> tempPrintList = loadSharedPreferencesLogList(tempPrefenceManager);
            if (tempPrintList.size() > 0) {
                autoPrint();
            }
        }
        if (getIntent().hasExtra(AppConstant.FROM_PUSH) && getIntent().getBooleanExtra(AppConstant.FROM_PUSH, false)) {
            if (!getSupportFragmentManager().findFragmentByTag(OrdersFragment.class.getSimpleName()).isVisible()) {
                setSelection(binding.llCashier, binding.imgCashier, binding.tvCashier);
                replaceFragment(new OrdersFragment());
            }
        }

    }


    //=========================Mehtod to set data====================//
    private void setData() {

       /*binding.viewOnlineCollapsed.setSelected(true);
        binding.viewOnlineExpanded.setSelected(true);*/
        String userInfo = SharedPrefManager.getInstance(context).getString(AppConstant.USER_INFO, "");
        userData = new Gson().fromJson(userInfo, SigninResponse.ResultBean.class);
        if (SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE, "").equals(AppConstant.TYPE_ICASHEIR)) {
            binding.tvUserNameCollapse.setText(SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_NAME, ""));
            binding.tvUserNameExpand.setText(SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_NAME, ""));
            Utilities.setImageCasheir(context, binding.imgUserCollapse, ServerConstants.IMAGE_BASE_URL + SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_IMAGE, ""));
            Utilities.setImageCasheir(context, binding.imgUserExpand, ServerConstants.IMAGE_BASE_URL + SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_IMAGE, ""));
            binding.switchOnline.setVisibility(View.GONE);
        } else {
            binding.tvUserNameCollapse.setText(userData.getName());
            binding.tvUserNameExpand.setText(userData.getName());
            binding.imgUserCollapse.setImageDrawable(getResources().getDrawable(R.drawable.icon_placeholder));
            binding.imgUserExpand.setImageDrawable(getResources().getDrawable(R.drawable.icon_placeholder));
            binding.switchOnline.setVisibility(View.VISIBLE);
            SharedPrefManager.getInstance(context).saveString(AppConstant.MERCHANT_NAME, userData.getName());


        }


        setDateTime();


    }

    //========MEhtod to set and update date and time===========//
    private void setDateTime() {

        Thread time = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String currentTime = DateUtils.getCurrentTime();//2018-Aug-14 16:41:51
                                String[] cureentTimeArray = currentTime.split(" ");
                                String[] dateArray = cureentTimeArray[0].split("-");
                                String[] timeArray = cureentTimeArray[1].split(":");
                                binding.tvDateCollapse.setText(dateArray[2] + "  " + dateArray[1].toUpperCase());
                                getAmPm(Integer.valueOf(timeArray[0]), Integer.valueOf(timeArray[1]), binding.tvTimeCollapse, binding.tvAmPmCollapse);


                                binding.tvDayExpand.setText(dateArray[2]);
                                binding.tvMonthExpand.setText(dateArray[1].toUpperCase());
                                binding.tvYearExpand.setText(dateArray[0]);
                                binding.tvTimeExpand.setText(timeArray[0] + ":" + timeArray[1]);
                                getAmPm(Integer.valueOf(timeArray[0]), Integer.valueOf(timeArray[1]), binding.tvTimeExpand, binding.tvAmPmExpand);
                            }
                        });
                        Thread.sleep(12000);
                    }
                } catch (InterruptedException e) {

                }
            }
        };

        time.start();
    }

    //=================Method to open or close drawer==================//
    private void toggleDrawer() {
        if (showing) {
            collapse(binding.tvMHome);
            collapse(binding.tvCashier);
            collapse(binding.tvGenCode);
            collapse(binding.tvItems);
            collapse(binding.tvDeals);
            collapse(binding.tvCategories);
            collapse(binding.tvTransactions);
            collapse(binding.tvInvoices);
            collapse(binding.tvSettings);
            collapse(binding.tvReports);
            collapse(binding.tvHelp);
            binding.clTimeExpand.setVisibility(View.GONE);
            binding.clUserExpand.setVisibility(View.GONE);
            binding.clTimeCollapse.setVisibility(View.VISIBLE);
            binding.llUserInfoCollapse.setVisibility(View.VISIBLE);
            binding.imgCalc.setVisibility(View.VISIBLE);
            binding.llMenu.setPaddingRelative(0, 0, 0, 0);
            binding.imgArrow.setRotation(0);
            showing = false;

        } else {
            expand(binding.tvMHome);
            expand(binding.tvCashier);
            expand(binding.tvGenCode);
            expand(binding.tvItems);
            expand(binding.tvDeals);
            expand(binding.tvCategories);
            expand(binding.tvTransactions);
            expand(binding.tvInvoices);
            expand(binding.tvSettings);
            expand(binding.tvReports);
            expand(binding.tvHelp);
            binding.clTimeExpand.setVisibility(View.VISIBLE);
            binding.clUserExpand.setVisibility(View.VISIBLE);
            binding.clTimeCollapse.setVisibility(View.GONE);
            binding.llUserInfoCollapse.setVisibility(View.GONE);
            binding.imgCalc.setVisibility(View.GONE);
            binding.llMenu.setPaddingRelative(0, 0, 5, 0);
            binding.imgArrow.setRotation(180);
            showing = true;
        }
    }

    //=================Method to  or close drawer==================//
    public void closeDrawer() {

        collapse(binding.tvMHome);
        collapse(binding.tvCashier);
        collapse(binding.tvGenCode);
        collapse(binding.tvItems);
        collapse(binding.tvDeals);
        collapse(binding.tvCategories);
        collapse(binding.tvTransactions);
        collapse(binding.tvInvoices);
        collapse(binding.tvSettings);
        collapse(binding.tvReports);
        collapse(binding.tvHelp);
        binding.clTimeExpand.setVisibility(View.GONE);
        binding.clUserExpand.setVisibility(View.GONE);
        binding.clTimeCollapse.setVisibility(View.VISIBLE);
        binding.llUserInfoCollapse.setVisibility(View.VISIBLE);
        binding.imgCalc.setVisibility(View.VISIBLE);
        binding.llMenu.setPaddingRelative(0, 0, 0, 0);
        binding.imgArrow.setRotation(0);

    }

    //==================Method to set onClick listener====================//
    private void setOnClickListeners() {

        binding.llMHome.setOnClickListener(context);
        binding.llCashier.setOnClickListener(context);
        binding.llGenCode.setOnClickListener(context);
        binding.llItems.setOnClickListener(context);
        binding.llDeals.setOnClickListener(context);
        binding.llCategories.setOnClickListener(context);
        binding.llTransactions.setOnClickListener(context);
        binding.llInvoices.setOnClickListener(context);
        binding.llSettings.setOnClickListener(context);
        binding.llReports.setOnClickListener(context);
        binding.llHelp.setOnClickListener(context);
        binding.btnDrawer.setOnClickListener(context);
        binding.flLogout.setOnClickListener(context);
        binding.frame.setOnClickListener(context);
        binding.llDrawer.setOnClickListener(context);
        binding.imgUserCollapse.setOnClickListener(context);
        binding.imgUserExpand.setOnClickListener(context);
        binding.imgCalc.setOnClickListener(context);


    }


    @Override
    public void onClick(View v) {
        /*SharedPrefManager sharedPrefManager=SharedPrefManager.getInstance(HomeActivity.this);
        if (sharedPrefManager.getInt(AppConstant.PRINTER_AUTO_ON, 0) == 1) {
            IntentFilter intentFilter= new IntentFilter();
            intentFilter.addAction(BackgroundService.ACTION);
            registerReceiver(broadcastReceiver,intentFilter);
        }*/
        if (v == binding.llMHome) {
            setSelection(binding.llMHome, binding.imgHome, binding.tvMHome);
            replaceFragment(new MerchantHomeFragment());

        } else if (v == binding.llCashier) {
            setSelection(binding.llCashier, binding.imgCashier, binding.tvCashier);
            replaceFragment(new OrdersFragment());
        } else if (v == binding.llGenCode) {
            setSelection(binding.llGenCode, binding.imgCode, binding.tvGenCode);
            replaceFragment(new GenerateCodeFragment());
        } else if (v == binding.llItems) {
            setSelection(binding.llItems, binding.imgItems, binding.tvItems);
            replaceFragment(new ItemsFragment());

        } else if (v == binding.llDeals) {
            setSelection(binding.llDeals, binding.imgDeals, binding.tvDeals);
            replaceFragment(new DealsFragment());
        } else if (v == binding.llCategories) {
            setSelection(binding.llCategories, binding.imgCategories, binding.tvCategories);
            replaceFragment(new AddCategoriesFragment());

        } else if (v == binding.llTransactions) {
            setSelection(binding.llTransactions, binding.imgTransactions, binding.tvTransactions);
            replaceFragment(new TransactionsFragment());

        } else if (v == binding.llInvoices) {
            setSelection(binding.llInvoices, binding.imgInvoices, binding.tvInvoices);
            replaceFragment(new InvoiceFragment());
        } else if (v == binding.llSettings) {
            setSelection(binding.llSettings, binding.imgSettings, binding.tvSettings);
            replaceFragment(new AppSettingsFragment());
        } else if (v == binding.llReports) {
            setSelection(binding.llReports, binding.imgReports, binding.tvReports);
            replaceFragment(new ReportsFragment());
        } else if (v == binding.llHelp) {
            setSelection(binding.llHelp, binding.imgHelp, binding.tvHelp);
            replaceFragment(new UnderDevelopmentFragment());
        } else if (v == binding.btnDrawer) {
            Utilities.hideSoftKeyboard(context);
            toggleDrawer();
        } else if (v == binding.flLogout) {
            logoutUser();
        } else if (v == binding.frame) {
            Utilities.hideSoftKeyboard(context);
        } else if (v == binding.imgUserCollapse || v == binding.imgUserExpand) {
            new CashierLoginDialog(context, new DialogDismissListener() {
                @Override
                public void onDismiss() {
                    setData();
                    callGetStatusApi();

                }
            }).show();
        } else if (v == binding.imgCalc) {
            if (getSupportFragmentManager().findFragmentByTag(CheckoutFragment.class.getSimpleName()) != null) {
                new CalculatorDialog(context, new CalculatorListener() {
                    @Override
                    public void onSaveClick(float val) {
                        if (val > 0) {

                            ((CheckoutFragment) getSupportFragmentManager().findFragmentByTag(CheckoutFragment.class.getSimpleName())).setCustomAmount(val);
                        }
                    }
                }).show();
            } else if (getSupportFragmentManager().findFragmentByTag(OrdersFragment.class.getSimpleName()) != null && getSupportFragmentManager().findFragmentByTag(OrdersFragment.class.getSimpleName()).isVisible() &&
                    ((OrdersFragment) getSupportFragmentManager().findFragmentByTag(OrdersFragment.class.getSimpleName())).cartItems.size() > 0) {
                SharedPrefManager sharedPrefManager=SharedPrefManager.getInstance(HomeActivity.this);
                if (sharedPrefManager.getInt(AppConstant.PRINTER_AUTO_ON, 0) == 1) {
                    SharedPrefManager tempPrefenceManager = SharedPrefManager.getInstance(context);
                    List<OrderListResponse.ResultBean> tempPrintList = loadSharedPreferencesLogList(tempPrefenceManager);
                    if (tempPrintList.size() > 0) {
                        autoPrint();
                    }
                }
                new CalculatorDialog(context, new CalculatorListener() {
                    @Override
                    public void onSaveClick(float val) {
                        if (val > 0) {
                            ((OrdersFragment) getSupportFragmentManager().findFragmentByTag(OrdersFragment.class.getSimpleName())).setCustomAmount(val);

                        }
                    }
                }).show();
            }
        }
    }

    public void showAddDeal() {
        setSelection(binding.llDeals, binding.imgDeals, binding.tvDeals);
        replaceFragment(new DealsFragment());
    }

    //================Method to show signout alert===================//
    private void logoutUser() {
        AlertUtil.showAlertWindow(context, getString(R.string.signout_confirmation), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callLogoutApi();
            }
        });
    }

    //================Method to change fragments =======================//
    public void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).commitAllowingStateLoss();
        // start automatic printer
        SharedPrefManager sharedPrefManager=SharedPrefManager.getInstance(HomeActivity.this);
        if (sharedPrefManager.getInt(AppConstant.PRINTER_AUTO_ON, 0) == 1) {
            SharedPrefManager tempPrefenceManager = SharedPrefManager.getInstance(context);
            List<OrderListResponse.ResultBean> tempPrintList = loadSharedPreferencesLogList(tempPrefenceManager);
            if (tempPrintList.size() > 0) {
                autoPrint();
            }
        }
        if (getSupportFragmentManager().findFragmentByTag(OrdersFragment.class.getSimpleName()) != null) {
            getSupportFragmentManager().popBackStack();
        }

    }

    //==================Mehtod to change selected item=================//
    public void setSelection(View view, ImageView imageView, TextView tv) {
        binding.llMHome.setSelected(false);
        binding.llCashier.setSelected(false);
        binding.llGenCode.setSelected(false);
        binding.llItems.setSelected(false);
        binding.llDeals.setSelected(false);
        binding.llCategories.setSelected(false);
        binding.llTransactions.setSelected(false);
        binding.llInvoices.setSelected(false);
        binding.llSettings.setSelected(false);
        binding.llReports.setSelected(false);
        binding.llHelp.setSelected(false);

        binding.imgHome.setSelected(false);
        binding.imgCashier.setSelected(false);
        binding.imgCode.setSelected(false);
        binding.imgItems.setSelected(false);
        binding.imgDeals.setSelected(false);
        binding.imgCategories.setSelected(false);
        binding.imgTransactions.setSelected(false);
        binding.imgInvoices.setSelected(false);
        binding.imgSettings.setSelected(false);
        binding.imgReports.setSelected(false);
        binding.imgHelp.setSelected(false);

        binding.tvMHome.setSelected(false);
        binding.tvCashier.setSelected(false);
        binding.tvGenCode.setSelected(false);
        binding.tvItems.setSelected(false);
        binding.tvDeals.setSelected(false);
        binding.tvCategories.setSelected(false);
        binding.tvTransactions.setSelected(false);
        binding.tvInvoices.setSelected(false);
        binding.tvSettings.setSelected(false);
        binding.tvReports.setSelected(false);
        binding.tvHelp.setSelected(false);

        if (SharedPrefManager.getInstance(context).getString(AppConstant.APP_LANGUAGE, "en").equals("ar"))
            view.setBackground(getResources().getDrawable(R.drawable.bg_menu_selector_rotate));

        view.setSelected(true);
        imageView.setSelected(true);
        tv.setSelected(true);
    }


    //=======================Method to format time====================//
    private void getAmPm(int hours, int minutes, TextView tvDateTime, TextView tvAmPm) {
        String hour, amPm, minute;
        /*check AM/PM*/
        if (hours == 0) {
            if (minutes == 0) {
                hour = String.valueOf(hours + 12);
                amPm = getString(R.string.am);
            } else {
                hour = String.valueOf(hours + 12);
                amPm = getString(R.string.am);
            }

        } else if (hours > 12) {

            if ((hours - 12) < 10) {
                hour = "0" + (hours - 12);
            } else {
                hour = String.valueOf(hours - 12);
            }
            amPm = getString(R.string.pm);
        } else if (hours == 12) {
            if (minutes == 0) {
                hour = String.valueOf(hours);
                amPm = getString(R.string.pm);
            } else {
                hour = String.valueOf(hours);
                amPm = getString(R.string.pm);
            }
        } else {
            amPm = getString(R.string.am);
            if (hours < 10) {
                hour = "0" + hours;
            } else {
                hour = String.valueOf(hours);
            }
        }
        if (minutes < 10) {
            minute = "0" + minutes;
        } else {
            minute = String.valueOf(minutes);
        }

        tvDateTime.setText(hour + ":" + minute);
        tvAmPm.setText(amPm);


    }

    public void openResetPassword() {


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.add(R.id.frame, new ResetPasswordFragment())
                .addToBackStack(AppConstant.SETTING_FRAGMENT).commit();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImagePickerUtil.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(MerchantHomeFragment.class.getSimpleName());
        if (fragment != null && fragment.isVisible()) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        Fragment itemFragment = getSupportFragmentManager().findFragmentByTag(NewItemFragment.class.getSimpleName());
        if (itemFragment != null && itemFragment.isVisible()) {
            itemFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        Fragment cashierFragment = getSupportFragmentManager().findFragmentByTag(CashierFragment.class.getSimpleName());
        if (cashierFragment != null && cashierFragment.isVisible()) {
            cashierFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        Fragment mealFragment = getSupportFragmentManager().findFragmentByTag(AddMealFragment.class.getSimpleName());
        if (mealFragment != null && mealFragment.isVisible()) {
            mealFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        Fragment dealFragment = getSupportFragmentManager().findFragmentByTag(NewDealsFragment.class.getSimpleName());
        if (dealFragment != null && dealFragment.isVisible()) {
            dealFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        Fragment checkoutFragment = getSupportFragmentManager().findFragmentByTag(CheckoutFragment.class.getSimpleName());
        if (checkoutFragment != null && checkoutFragment.isVisible()) {
            checkoutFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //==========================Method to call logout api=====================//
    private void callLogoutApi() {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);

            HashMap<String, String> params = new HashMap<>();
            params.put("device_id", SharedPrefManager.getInstance(context).getString(AppConstant.KEY_DEVICE_TOKEN, ""));
            params.put("flag", "1");


            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.SIGNOUT)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                                if (genericResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (genericResponse.getCode() == 200) {

                                        SharedPrefManager.getInstance(context).removeData(AppConstant.USER_INFO);
                                        SharedPrefManager.getInstance(context).removeData(AppConstant.KEY_LOGIN_USER_ID);
                                        SharedPrefManager.getInstance(context).removeData(AppConstant.ACCESS_TOKEN);
                                        SharedPrefManager.getInstance(context).removeData(AppConstant.CASHIER_TOKEN);
                                        SharedPrefManager.getInstance(context).removeData(AppConstant.CASHIER_NAME);
                                        SharedPrefManager.getInstance(context).removeData(AppConstant.USER_TYPE);
                                        SharedPrefManager.getInstance(context).removeData(AppConstant.IS_PARENT);
                                        SharedPrefManager.getInstance(context).removeData(AppConstant.CART_HISTORY);

                                        Utilities.clearAllgoToActivity(context, LoginActivity.class);
                                        Utilities.clearNotifications(context);
                                        AlertUtil.toastMsg(context, genericResponse.getMessage());


                                    } else {

                                        AlertUtil.toastMsg(context, genericResponse.getMessage());
                                    }
                                } else {
                                    AlertUtil.hideProgressDialog();
                                    AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                }
                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            AlertUtil.hideProgressDialog();
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        } else {
            AlertUtil.toastMsg(context, getString(R.string.network_error));
        }
    }


    //==========================Method to call change online status api=====================//
    private void callChangeStatusApi() {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);

            HashMap<String, String> params = new HashMap<>();
            params.put("status", binding.switchOnline.isChecked() ? "1" : "0");
            params.put("device_id", SharedPrefManager.getInstance(context).getString(AppConstant.KEY_DEVICE_TOKEN, ""));

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.CHANGE_ONLINE_STATUS)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                                if (genericResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (genericResponse.getCode() == 200) {
                                        AlertUtil.toastMsg(context, genericResponse.getMessage());

                                    } else {

                                        AlertUtil.toastMsg(context, genericResponse.getMessage());
                                    }
                                } else {
                                    AlertUtil.hideProgressDialog();
                                    AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                }
                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            AlertUtil.hideProgressDialog();
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        } else {
            AlertUtil.toastMsg(context, getString(R.string.network_error));
        }
    }

    //==========================Method to call change online status api=====================//
    private void callGetStatusApi() {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);

            HashMap<String, String> params = new HashMap<>();

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_ONLINE_STATUS)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            if (Utilities.isValidJson(response)) {
                                OnlineStatusResponse onlineStatusResponse = new Gson().fromJson(response, OnlineStatusResponse.class);
                                if (onlineStatusResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (onlineStatusResponse.getCode() == 200) {
                                        binding.switchOnline.setOnCheckedChangeListener(null);

                                        if (onlineStatusResponse.getResult() != null && onlineStatusResponse.getResult().getOnline_status() == 1) {
                                            binding.switchOnline.setChecked(true);
                                            binding.tvOnline.setText(getString(R.string.online));
                                            binding.viewOnlineCollapsed.setSelected(true);
                                            binding.viewOnlineExpanded.setSelected(true);
                                        } else {
                                            binding.switchOnline.setChecked(false);
                                            binding.tvOnline.setText(getString(R.string.offline));
                                            binding.viewOnlineCollapsed.setSelected(false);
                                            binding.viewOnlineExpanded.setSelected(false);
                                        }
                                        setCheckChangeListener();
                                        //AlertUtil.toastMsg(context, onlineStatusResponse.getMessage());

                                    } else {

                                        //AlertUtil.toastMsg(context, onlineStatusResponse.getMessage());
                                    }
                                } else {
                                    // AlertUtil.hideProgressDialog();
                                    // AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                }
                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            //AlertUtil.hideProgressDialog();
                            //AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);

    }

    //==========================Method to call change online status api=====================//
    private void updateDeviceTokenApi() {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            //AlertUtil.showProgressDialog(context);

            HashMap<String, String> params = new HashMap<>();
            params.put("device_id", SharedPrefManager.getInstance(context).getString(AppConstant.KEY_DEVICE_TOKEN, ""));
            params.put("device_type", "android");
            params.put("lang", Utilities.isArabic() ? "1" : "0");  //1 for arabic and 0 for english
            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.UPDATE_DEVICE_TOKEN)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            // AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                                if (genericResponse != null) {
                                    //AlertUtil.hideProgressDialog();
                                    if (genericResponse.getCode() == 200) {
                                        //AlertUtil.toastMsg(context, genericResponse.getMessage());

                                    } else {

                                        //AlertUtil.toastMsg(context, genericResponse.getMessage());
                                    }
                                } else {
                                    //AlertUtil.hideProgressDialog();
                                    //AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                }
                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            // AlertUtil.hideProgressDialog();
                            // AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        } else {
            //AlertUtil.toastMsg(context,getString(R.string.network_error));
        }
    }
}
