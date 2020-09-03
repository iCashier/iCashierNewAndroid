package com.icashier.app.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.icashier.app.R;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.databinding.DialogPaymentCardBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.Utilities;
import com.spectratech.lib.Callback;

import java.util.Timer;
import java.util.TimerTask;

import geidea.net.spectratechlib_api.ApiAppMain;
import geidea.net.spectratechlib_api.BuildConfig;
import geidea.net.spectratechlib_api.CONNECTIONSTATUS;
import geidea.net.spectratechlib_api.Tools;

import static geidea.net.spectratechlib_api.ApiAppMain.SP530API_ACTION_ACL_CONNECTED;
import static geidea.net.spectratechlib_api.ApiAppMain.SP530API_ACTION_ACL_DISCONNECTED;
import static java.lang.Double.parseDouble;

public class PaymentCardDialog extends Activity {

    public static final String HARDCODED_TERMINAL_ID = "1234567812345678";
    private static final String TAG = PaymentCardDialog.class.getSimpleName();
    public static boolean IS_TERMINAL_VERIFIED = false;
    protected final int REQUEST_PERMISSIONS = 1;
    private final int MSG_HANDLER_REFRESHUI_BLUETOOTHROW = 1100;
    private final int MSG_HANDLER_REFRESHUI_BLUETOOTHROW_TV = 1101;//JUST REFRESH
    public boolean bluetoothTransInProgress = false;
    protected String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    protected ProgressDialog m_progressDialog;
    DialogPaymentCardBinding binding;
    PaymentCardDialog context;
    Dialog dialog;
    Callback<Object> cb_finish_tran;
    Callback<Object> cb_finish_recon;
    Callback<Object> cb_finish_ter_val;
    Callback<Object> cb_finish_ter_info;
    String amount;
    boolean isSuccess;
    private Handler m_handler;
    private Object mLock;
    private boolean mFlagCreate;
    private BroadcastReceiver m_broadcastReceiver;

    private boolean mFlagOnPause;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.setContentView(context, R.layout.dialog_payment_card);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        amount = Utilities.formatPrice(getIntent().getFloatExtra(AppConstant.TOTAL_AMOUNT, 0));
        binding.tvAmount.setText("SR " + amount);
        startIntialization();
        setOnClickListener();

        registerBroadcastReceiver();

        initHandler();

        boolean bProcessRuntimePermission = processRuntimeCheckPermission();
        if (!bProcessRuntimePermission) {
            /*refreshBluetoothIconByPostMessage(MSG_HANDLER_REFRESHUI_BLUETOOTHROW_TV);*/
            onRefreshApiAppMainBTConnection();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFlagOnPause = false;
        refreshBluetoothIconByPostMessage(MSG_HANDLER_REFRESHUI_BLUETOOTHROW_TV);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFlagOnPause = true;
    }

    private void onRefreshApiAppMainBTConnection() {
        Log.i(TAG, "onRefreshApiAppMainBTConnection");
        ApiAppMain.refreshBTConnection();
        refreshBluetoothIconByPostMessage(MSG_HANDLER_REFRESHUI_BLUETOOTHROW_TV);
    }

    private void initHandler() {
        m_handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_HANDLER_REFRESHUI_BLUETOOTHROW: {
                        refreshBluetoothIcon(MSG_HANDLER_REFRESHUI_BLUETOOTHROW);
                    }
                    break;
                    case MSG_HANDLER_REFRESHUI_BLUETOOTHROW_TV: {
                        refreshBluetoothIcon(MSG_HANDLER_REFRESHUI_BLUETOOTHROW_TV);
                    }
                    break;
                }
            }
        };
    }

    private void refreshBluetoothIcon(int messageHandler) {
        if (binding.rowBluetoothIv == null) {
            return;
        }
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return;
        }

        int id = R.drawable.bluetooth_disabled_128px;
        if (bluetoothAdapter.isEnabled()) {
            id = R.drawable.bluetooth_enabled_128px;
            CONNECTIONSTATUS connectionStatus = ApiAppMain.terminalAndMobileConnectionStatus();
            if (connectionStatus == CONNECTIONSTATUS.CONNECTED || connectionStatus == CONNECTIONSTATUS.BONDED) {
                id = R.drawable.bluetooth_connected_128px;
                if (!bluetoothTransInProgress) {
                    if (messageHandler == 1100) {
                        onBluetoothConnected();
                    }
                }
            } else {
                IS_TERMINAL_VERIFIED = false;
            }
        }
        binding.rowBluetoothIv.setImageDrawable(getResources().getDrawable(id));
    }


    public void onBluetoothConnected() {

        //AlertUtil.showToastShort(context,"Bluetooth connected");

        final Timer t = new Timer();
        bluetoothTransInProgress = true;
        t.schedule(new TimerTask() {
            @Override
            public void run() {

                PaymentCardDialog.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressDialog();
                    }
                });

                start_terminalVerification();

                t.cancel();
            }
        }, 5000, 1000000);

    }


    private void setOnClickListener() {

        binding.rowBluetoothIv.setOnClickListener(V -> {
            if (!isOnPause()) {
                ApiAppMain.launchBluetoothScreen();
            }
        });

        binding.btnStart.setOnClickListener(v -> {
            startTransaction();
        });

        binding.btnCancel.setOnClickListener(V -> {
            finish();
        });

    }

    public boolean isOnPause() {
        return mFlagOnPause;
    }

    private void startTransaction() {

        if (isConnected()) {
            if (IS_TERMINAL_VERIFIED) {
                if (validateAmount()) {
                    // AlertUtil.showToastShort(context,"Transaction Started");
                    byte[] amountBytes = Tools.getAmountByteArray(amount);
                    // AlertUtil.showToastShort(context,amountBytes.length+"");

                    final ApiAppMain apiAppMain = ApiAppMain.getInstance();
                    apiAppMain.getTransactionReceipt(amountBytes, cb_finish_tran);
                }
            } else {
                if (!bluetoothTransInProgress) {
                    AlertUtil.showToastShort(context, getString(R.string.terminal_yet_to_be_verified));
                    start_terminalVerification();
                } else {
                    AlertUtil.showToastShort(context, getString(R.string.terminal_yet_to_be_verified));
                }
            }
        } else {
            AlertUtil.showToastShort(context, getString(R.string.please_connect_to_terminal));
        }
    }

    public void start_terminalVerification() {
        //AlertUtil.showToastShort(context,"Terminal Verification started");
        byte[] amountBytes = Tools.getAmountByteArray("0.00");
        final ApiAppMain apiAppMain = ApiAppMain.getInstance();
        apiAppMain.getTerminalValidationInfo(amountBytes, cb_finish_ter_val, HARDCODED_TERMINAL_ID);
    }


    public boolean isConnected() {
        CONNECTIONSTATUS connectionStatus = ApiAppMain.terminalAndMobileConnectionStatus();
        return connectionStatus != CONNECTIONSTATUS.DISCONNECTED;
    }


    private void startIntialization() {

        if (BuildConfig.DEBUG) {
            ApiAppMain.setEnableLogging(true);
        } else {
            ApiAppMain.setEnableLogging(false);
        }
        initCallBack();
        mLock = new Object();
        mFlagCreate = true;
        mFlagOnPause = false;

        ApiAppMain.onCreate(context);

        // disable use of SSL in local channel
        ApiAppMain.setLocalChannelUseSSL(false);
    }

    public void initCallBack() {
        cb_finish_tran = new Callback<Object>() {
            @Override
            public Object call() {
                final String[] resultBytes = (String[]) this.getParameter();
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultBytes[0].equals("1")) {
                          /*  SwipeCardResponse transactionResult=new Gson().fromJson(resultBytes[2],SwipeCardResponse.class);
                            if(transactionResult.getTransactionStatusCode()==0){
                                //transaction declined
                                isSuccess=false;
                            }else{
                                //transaction approved
                                isSuccess=true;
                            }*/
                            if (resultBytes[1].toLowerCase().contains("approved")) {
                                isSuccess = true;
                            }
                            showDialog(true, false, getString(R.string.receipt_label), resultBytes[1]);
                        } else {
                            isSuccess = false;
                            //AlertUtil.showToastShort(context,"Trancation callback:Error");
                            showDialog(true, true, getString(R.string.error), resultBytes[1]);
                        }
                    }
                });

                return null;
            }
        };

        cb_finish_recon = new Callback<Object>() {
            @Override
            public Object call() {
                final String[] resultBytes = (String[]) this.getParameter();
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultBytes[0].equals("1")) {
                            //showDialog(true, false, "Receipt", resultBytes[1]);
                        } else {
                            //showDialog(true, true, "Error", resultBytes[1]);
                        }
                    }
                });
                return null;
            }
        };

        cb_finish_ter_val = new Callback<Object>() {
            @Override
            public Object call() {
                final String[] resultBytes = (String[]) this.getParameter();
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                        //AlertUtil.showToastShort(context,"Progress dialog dismissed");
                        bluetoothTransInProgress = false;
                        refreshBluetoothIconByPostMessage(MSG_HANDLER_REFRESHUI_BLUETOOTHROW_TV);
                        if (resultBytes[0].equals("1")) {
                            IS_TERMINAL_VERIFIED = true;
                            // AlertUtil.showToastShort(context,"Terminal Verification successfull");
                            startTransaction();
                            //showDialog(true, false, "Receipt", resultBytes[1]);
                        } else {
                            AlertUtil.showToastShort(context, getString(R.string.teminal_ver_failed));
                            //showDialog(true, true, "Error", resultBytes[1]);
                        }
                    }
                });
                return null;
            }
        };

       /* cb_finish_ter_info= new Callback<Object>() {
            @Override
            public Object call() {

                final String[] resultBytes = (String[])this.getParameter();

                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(resultBytes[0].equals("1")) {
                            showDialog(true, false, "Receipt", resultBytes[1]);
                        }else{
                            showDialog(true, true, "Error", resultBytes[1]);
                        }
                    }
                });
                return null;
            }
        };*/

    }

    public void showDialog(boolean isShow, boolean isError, String title, String html_content) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        dialog.setCancelable(false);
        if (dialog != null && isShow) {
            ((TextView) dialog.findViewById(R.id.title)).setText(title);

            if (!isError) {
                dialog.findViewById(R.id.error_text).setVisibility(View.GONE);
                dialog.findViewById(R.id.webview_sroll).setVisibility(View.VISIBLE);

                WebView webView = dialog.findViewById(R.id.webView);
                webView.setWebViewClient(new MyBrowser());

                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webView.loadData(html_content, "text/html", "UTF-8");

            } else {
                dialog.findViewById(R.id.webview_sroll).setVisibility(View.GONE);
                ((TextView) dialog.findViewById(R.id.error_text)).setText(Utilities.getStringResourceByName(context, "err_" + html_content));
                dialog.findViewById(R.id.error_text).setVisibility(View.VISIBLE);
                isError = true;

            }
            dialog.findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                    if (isSuccess) {
                        Intent intent = new Intent(context, getCallingActivity().getClass());
                        setResult(AppConstant.CARD_PAYMENT, intent);
                        finish();
                    }
                }
            });

            dialog.show();

        } else if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public boolean validateAmount() {
        boolean isValidAmount = true;
        if (amount.length() <= 0 || amount.trim().equals(".") || parseDouble(amount.trim()) <= 0.0f) {
            AlertUtil.showToastShort(context, getString(R.string.invalid_amount));
            isValidAmount = false;
        }
        //AlertUtil.showToastShort(context,"Amount Validated");
        return isValidAmount;
    }

    protected void refreshBluetoothIconByPostMessage(int messageHandler) {
        m_handler.sendEmptyMessage(messageHandler);
    }

    @Override
    protected void onDestroy() {
        unregisterBroadcastReceiver();
        ApiAppMain.onDestroy(context);
        showDialog(false, false, null, null);
        super.onDestroy();
    }

    public boolean hasPermissions(String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            boolean bHasPermissions = hasPermissions(PERMISSIONS);
            if (bHasPermissions) {
                onRefreshApiAppMainBTConnection();
                /*refreshBluetoothIconByPostMessage(MSG_HANDLER_REFRESHUI_BLUETOOTHROW_TV);*/
            }
        }
    }

    public void showProgressDialog() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    public void dismissProgressDialog() {
        binding.progressBar.setVisibility(View.GONE);
    }

    protected void registerBroadcastReceiver() {
        if (m_broadcastReceiver == null) {
            m_broadcastReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {

                    String action = intent.getAction();
                    if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
//                        Logger.i(m_className, "registerBroadcastReceiver, ACTION_STATE_CHANGED");
                        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                        switch (state) {
                            case BluetoothAdapter.STATE_OFF:
                            case BluetoothAdapter.STATE_TURNING_OFF:
                            case BluetoothAdapter.STATE_ON:
                            case BluetoothAdapter.STATE_TURNING_ON:
                            default:
                                refreshBluetoothIconByPostMessage(1101);
                                break;
                        }
                    } else if (SP530API_ACTION_ACL_CONNECTED.equals(action)) {
                        //SP530 is now connected
                        Log.i(TAG, "registerBroadcastReceiver, SP530API_ACTION_ACL_CONNECTED");
                        refreshBluetoothIconByPostMessage(1100);
                    } else if (SP530API_ACTION_ACL_DISCONNECTED.equals(action)) {
                        //SP530 has disconnected
                        Log.i(TAG, "registerBroadcastReceiver, SP530API_ACTION_ACL_DISCONNECTED");
                        refreshBluetoothIconByPostMessage(1101);
                    }
                }
            };
            Activity act = context;
            IntentFilter filter = new IntentFilter();
            filter.addAction(SP530API_ACTION_ACL_CONNECTED);
            filter.addAction(SP530API_ACTION_ACL_DISCONNECTED);
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            act.registerReceiver(m_broadcastReceiver, filter);
        }
    }

    protected void unregisterBroadcastReceiver() {
        if (m_broadcastReceiver != null) {
            Activity act = context;
            act.unregisterReceiver(m_broadcastReceiver);
            m_broadcastReceiver = null;
        }
    }

    protected boolean processRuntimeCheckPermission() {
        boolean bHasPermissions = hasPermissions(PERMISSIONS);
        if (!bHasPermissions) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSIONS);
        }
        boolean bProcess = (!bHasPermissions);
        return bProcess;
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
