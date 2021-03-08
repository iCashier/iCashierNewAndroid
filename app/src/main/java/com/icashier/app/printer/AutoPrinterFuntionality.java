package com.icashier.app.printer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.epson.epos2.Epos2CallbackCode;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.PrinterListAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.dialog.SelectPrinterDialog;
import com.icashier.app.helper.DateUtils;
import com.icashier.app.helper.PermissionsUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.OrderListResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AutoPrinterFuntionality implements ReceiveListener {

    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public Printer mPrinter = null;
    Context context;
    RestClient.ApiRequest apiRequest;
    double lat, lng;
    GoogleMap googleMap;
    MarkerOptions markerOptions;
    LayoutInflater inflater;
    String targetPrinter = "";
    private OrderListResponse.ResultBean orderData;
    private List<OrderListResponse.ResultBean.ItemsBean> itemList = new ArrayList<>();
    private SupportMapFragment mapFragment;
    private int position;
    private String orderCashierName;
    private String CustomerName = "", tvOrderId, tvSubTotal, tvTax, tvTotal, tvCustomAmount;
    private String tvChange, tvDate;


    public AutoPrinterFuntionality(Context context, OrderListResponse.ResultBean orderData, int pos) {
        //super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.orderData = orderData;
        this.position = pos;

        onCreate();

    }

    public void saveSharedPreferencesLogList(SharedPrefManager pref, List<OrderListResponse.ResultBean> printLog) {
        Gson gson = new Gson();
        String json = gson.toJson(printLog);
        pref.saveString(AppConstant.PRINTER_PUSH_NOTIFICATION, json);
    }

    public List<OrderListResponse.ResultBean> loadSharedPreferencesLogList(SharedPrefManager pref) {
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

    private void getExtras() {
        // orderData=(OrderListResponse.ResultBean)getIntent().getSerializableExtra(AppConstant.ORDER_DETAIL);
    }

    protected void onCreate() {
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getExtras();
        setData();


/*
        binding.btnPayment.setOnClickListener(V -> {
            callPaymentRecievedApi();

        });
        binding.progressBar.setOnClickListener(V -> {

        });
        binding.btnClose.setOnClickListener(V -> {
            dismiss();

        });

        binding.tvViewTax.setOnClickListener(V -> {
            new TaxesListDialog(context).show();
        });*/
        // start Printing
        // startPrintingReceipt();


        /*binding.statusPending.setOnClickListener(V -> {
            if (!orderData.getStatus().equalsIgnoreCase(AppConstant.STATUS_DELIVERED)) {
                callChangeStatusApi(AppConstant.STATUS_PENDING);
            }
        });

        binding.statusProcessing.setOnClickListener(V -> {
            if (!orderData.getStatus().equalsIgnoreCase(AppConstant.STATUS_DELIVERED)) {
                callChangeStatusApi(AppConstant.STATUS_PROCESSING);
            }
        });

        binding.statusDelivered.setOnClickListener(V -> {
            if (!orderData.getStatus().equalsIgnoreCase(AppConstant.STATUS_DELIVERED)) {
                callChangeStatusApi(AppConstant.STATUS_DELIVERED);
            }

        });

        binding.statusReady.setOnClickListener(V -> {
            callChangeStatusApi(AppConstant.STATUS_READY);
        });*/
    }

    //=================Method to set data===================//
    private void setData() {

        if (!orderData.getCustomerName().equals("")) {
            CustomerName = orderData.getCustomerName();
        }
        setAddress();

        if (!orderData.getTableName().equalsIgnoreCase("null") && !orderData.getTableName().equals("")) {
            tvOrderId = "#" + orderData.getSequence_no() + " - " + orderData.getTableName();
        } else {
            tvOrderId = "#" + orderData.getSequence_no();
        }
        tvSubTotal = "SR " + Utilities.formatPrice(Float.parseFloat(orderData.getSubtotal()));
        tvTax = "SR " + Utilities.formatPrice(Float.parseFloat(orderData.getTax()));

        float totalPrice = Float.parseFloat(orderData.getTotal());
        totalPrice = (float) (Math.round(totalPrice * 100.0) / 100.0);

        tvTotal = "SR " + Utilities.formatPrice(totalPrice);

        if (orderData.getCustomAmount() > 0) {
            tvCustomAmount = "SR " + Utilities.formatPrice(orderData.getCustomAmount());
        }
        if (orderData.getChangeGiven() != null && !orderData.getChangeGiven().equals("NULL")
                && Double.parseDouble(orderData.getChangeGiven()) > 0) {
            tvChange = "SR " + Utilities.formatPrice(Float.parseFloat(orderData.getChangeGiven()));
        }
        itemList.addAll(orderData.getItems());
        itemList.addAll(orderData.getMeal());
        itemList.addAll(orderData.getDeals());
        try {
            tvDate = DateUtils.convertUTCtoLocalTime2(orderData.getDate(), "dd MMM yyyy hh:mm:ss");
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*OrderItemsAdapter adapter = new OrderItemsAdapter(context, itemList);
        binding.rvItems.setLayoutManager(new LinearLayoutManager(context));
        binding.rvItems.setAdapter(adapter);
        if (orderData.getStatus().equalsIgnoreCase(AppConstant.STATUS_PENDING)) {
            setSelection(binding.statusPending);
            callChangeStatusApi(AppConstant.STATUS_PROCESSING);
        }
        if (orderData.getStatus().equalsIgnoreCase(AppConstant.STATUS_PROCESSING)) {
            setSelection(binding.statusProcessing);
        }
        if (orderData.getStatus().equalsIgnoreCase(AppConstant.STATUS_DELIVERED)) {
            setSelection(binding.statusDelivered);
        }
        if (orderData.getStatus().equalsIgnoreCase(AppConstant.STATUS_READY)) {
            setSelection(binding.statusReady);
        }*/

    }

    //=========Mehtod to show current porder status===========//
/*    private void setSelection(View view) {
        binding.statusPending.setSelected(false);
        binding.statusProcessing.setSelected(false);
        binding.statusDelivered.setSelected(false);
        view.setSelected(true);

    }*/


    /*//===========Mehhod to call api to acknwoledge payment==========//
    private void callPaymentRecievedApi() {
        if (Utilities.isNetworkAvailable(context)) {
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> params = new HashMap<>();
            params.put("oid", "" + orderData.getId());
            params.put("device_id", SharedPrefManager.getInstance(context).getString(AppConstant.KEY_DEVICE_TOKEN, ""));


            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.PAYMENT_CONFIRMATION)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                                    if (genericResponse != null) {
                                        if (genericResponse.getCode() == 200) {

                                            binding.btnPayment.setVisibility(View.GONE);
                                            binding.btnClose.setVisibility(View.VISIBLE);
                                            orderData.setPaymentStatus(1);
                                            orderData.setStatus(AppConstant.STATUS_DELIVERED);
                                            orderData.setPayOptions(1);
                                            setSelection(binding.statusDelivered);
                                            for (int i = 0; i < itemList.size(); i++) {
                                                itemList.get(i).setReorder_count(0);
                                            }
                                            orderData.setIsReordered(0);
                                            dismiss();


                                        } else if (genericResponse.getCode() == 301) {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                  *//*  AlertUtil.showAlertWindow(context, context.getString(R.string.still_want_to_proceed), new OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                        }
                                                    });*//*
                                                }
                                            });
                                        }

                                    } else {
                                        binding.progressBar.setVisibility(View.GONE);
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }
                                }
                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            binding.progressBar.setVisibility(View.GONE);
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        } else {
            AlertUtil.toastMsg(context, context.getString(R.string.network_error));
        }
    }*/

    public void setAddress() {
        String tvAddress, tvPhone;
        //binding.llAddress.setVisibility(View.VISIBLE);
        tvAddress = orderData.getAddressText();
        tvPhone = orderData.getMobileNo();
        if (orderData.getLat() != null && orderData.getLng() != null) {
            lat = Double.parseDouble(orderData.getLat());
            lng = Double.parseDouble(orderData.getLng());

        }

    }

    //=============Method to print receipt==============//
    public void startPrintingReceipt() {
        PermissionsUtil.askPermissions((Activity) context, PermissionsUtil.STORAGE, PermissionsUtil.LOCATION, new PermissionsUtil.PermissionListener() {
            @Override
            public void onPermissionResult(boolean isGranted) {
                if (isGranted) {
                    if (SharedPrefManager.getInstance(context).getString(AppConstant.PRINTER_DEVICES_TARGET_AUTO, "") != null
                            && !SharedPrefManager.getInstance(context).getString(AppConstant.PRINTER_DEVICES_TARGET_AUTO, "").trim().isEmpty()) {
                        //binding.llChangePass.setVisibility(View.GONE);
                        targetPrinter = SharedPrefManager.getInstance(context).getString(AppConstant.PRINTER_DEVICES_TARGET_AUTO, "");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                runPrintReceiptSequence();
                            }
                        }, 2000);

                    } else {
                        try {
                            new SelectPrinterDialog(context, new PrinterListAdapter.SelectPrinterListener() {
                                @Override
                                public void onClick(String name, String target) {
                                    Log.e(name, target);
                                    targetPrinter = target;
                                    SharedPrefManager myPref = SharedPrefManager.getInstance(context);
                                    myPref.saveString(AppConstant.PRINTER_DEVICES_TARGET_AUTO, target);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            runPrintReceiptSequence();
                                        }
                                    }, 2000);
                                }
                            }).show();
                        }
                        catch (WindowManager.BadTokenException e) {
                            //use a log message
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
    }

    private boolean runPrintReceiptSequence() {
        if (!initializeObject()) {
            return false;
        }

        if (!formatPrintData()) {
            finalizeObject();
            return false;
        }/*else{
            SharedPrefManager pref=SharedPrefManager.getInstance(context);
            pref.saveString(AppConstant.PRINTER_PUSH_NOTIFICATION,"");
        }*/

        if (!printData()) {
            finalizeObject();
            return false;
        }

        return true;
    }

    private boolean initializeObject() {
        try {
            mPrinter = new Printer(Printer.TM_T20, Printer.MODEL_ANK,
                    context);

        } catch (Exception e) {
            Log.e("Printer", "error printing");
            // ShowMsg.showException(e, "Printer", context);
            return false;
        }

        mPrinter.setReceiveEventListener(this);

        return true;
    }

    private void finalizeObject() {
        if (mPrinter == null) {
            return;
        }

        mPrinter.clearCommandBuffer();

        mPrinter.setReceiveEventListener(null);

        mPrinter = null;
    }

    private boolean printData() {
        if (mPrinter == null) {
            return false;
        }

        if (!connectPrinter()) {
            return false;
        }

        PrinterStatusInfo status = mPrinter.getStatus();

        dispPrinterWarnings(status);

        if (!isPrintable(status)) {
            // ShowMsg.showMsg(makeErrorMessage(status), context);
            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
                // Do nothing
                //ex.printStackTrace();
            }
            return false;
        }

        try {
            mPrinter.sendData(Printer.PARAM_DEFAULT);
        } catch (Exception e) {
            Log.e("sendData", "err");
            // ShowMsg.showException(e, "sendData", context);

            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        return true;
    }

    private boolean connectPrinter() {
        boolean isBeginTransaction = false;

        if (mPrinter == null) {
            return false;
        }

        try {
            mPrinter.connect(targetPrinter, Printer.PARAM_DEFAULT);
        } catch (Exception e) {

            Log.e("connect", "error" + e.getMessage());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    startPrintingReceipt();
                }
            }, 1500);

            //ShowMsg.showException(e, "connect", context);

            return false;
        }

        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        } catch (Exception e) {
            Log.e("beginTransaction", "err");
            // ShowMsg.showException(e, "beginTransaction", context);
        }

        if (isBeginTransaction == false) {
            try {
                mPrinter.disconnect();
            } catch (Epos2Exception e) {
                // Do nothing
                return false;
            }
        }

        return true;
    }

    private void dispPrinterWarnings(PrinterStatusInfo status) {
        String warningsMsg = "";

        if (status == null) {
            return;
        }

        if (status.getPaper() == Printer.PAPER_NEAR_END) {
            warningsMsg += context.getString(R.string.handlingmsg_warn_receipt_near_end);
        }

        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_1) {
            warningsMsg += context.getString(R.string.handlingmsg_warn_battery_near_end);
        }


        Log.e("Printer Warnings", warningsMsg);
    }

    private boolean isPrintable(PrinterStatusInfo status) {
        if (status == null) {
            return false;
        }

        if (status.getConnection() == Printer.FALSE) {
            return false;
        } else if (status.getOnline() == Printer.FALSE) {
            return false;
        } else {
            //print available
        }

        return true;
    }

    private String makeErrorMessage(PrinterStatusInfo status) {
        String msg = "";
        if (status.getOnline() == Printer.FALSE) {
            msg += context.getString(R.string.handlingmsg_err_offline);
        }
        if (status.getConnection() == Printer.FALSE) {
            msg += context.getString(R.string.handlingmsg_err_no_response);
        }
        if (status.getCoverOpen() == Printer.TRUE) {
            msg += context.getString(R.string.handlingmsg_err_cover_open);
        }
        if (status.getPaper() == Printer.PAPER_EMPTY) {
            msg += context.getString(R.string.handlingmsg_err_receipt_end);
        }
        if (status.getPaperFeed() == Printer.TRUE || status.getPanelSwitch() == Printer.SWITCH_ON) {
            msg += context.getString(R.string.handlingmsg_err_paper_feed);
        }
        if (status.getErrorStatus() == Printer.MECHANICAL_ERR || status.getErrorStatus() == Printer.AUTOCUTTER_ERR) {
            msg += context.getString(R.string.handlingmsg_err_autocutter);
            msg += context.getString(R.string.handlingmsg_err_need_recover);
        }
        if (status.getErrorStatus() == Printer.UNRECOVER_ERR) {
            msg += context.getString(R.string.handlingmsg_err_unrecover);
        }
        if (status.getErrorStatus() == Printer.AUTORECOVER_ERR) {
            if (status.getAutoRecoverError() == Printer.HEAD_OVERHEAT) {
                msg += context.getString(R.string.handlingmsg_err_overheat);
                msg += context.getString(R.string.handlingmsg_err_head);
            }
            if (status.getAutoRecoverError() == Printer.MOTOR_OVERHEAT) {
                msg += context.getString(R.string.handlingmsg_err_overheat);
                msg += context.getString(R.string.handlingmsg_err_motor);
            }
            if (status.getAutoRecoverError() == Printer.BATTERY_OVERHEAT) {
                msg += context.getString(R.string.handlingmsg_err_overheat);
                msg += context.getString(R.string.handlingmsg_err_battery);
            }
            if (status.getAutoRecoverError() == Printer.WRONG_PAPER) {
                msg += context.getString(R.string.handlingmsg_err_wrong_paper);
            }
        }
        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_0) {
            msg += context.getString(R.string.handlingmsg_err_battery_real_end);
        }

        return msg;
    }

    @Override
    public void onPtrReceive(Printer printer, int ii, PrinterStatusInfo printerStatusInfo, String s) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("AUTOPRINTER_STATUS", "***********" + ii);
                        if (ii == Epos2CallbackCode.CODE_SUCCESS) {
                            SharedPrefManager pref = SharedPrefManager.getInstance(context);
                            List<OrderListResponse.ResultBean> tempOrderList = loadSharedPreferencesLogList(pref);
                            //pref.saveString(AppConstant.PRINTER_PUSH_NOTIFICATION,"");
                            if (tempOrderList.size() > 0)
                                for (int i = 0; i < tempOrderList.size(); i++) {
                                    if (tempOrderList.get(i).getSequence_no() == orderData.getSequence_no()) {
                                        tempOrderList.remove(i);
                                        Log.e("AUTOPRINTER", "Done Printing" + i);
                                    }
                                }
                            // if(orderData.getSequence_no())
                            //tempOrderList.remove(position);
                            saveSharedPreferencesLogList(pref, tempOrderList);

                        }/*else{
                            startPrintingReceipt();
                        }*/
                        disconnectPrinter();
                    }
                });


            }
        });

    }

    private void disconnectPrinter() {
        if (mPrinter == null) {
            return;
        }

        try {
            mPrinter.endTransaction();
        } catch (final Exception e) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    //ShowMsg.showException(e, "endTransaction", context);
                }
            });
        }

        try {
            mPrinter.disconnect();
        } catch (final Exception e) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    Log.e("disconnect", "disconnect");
                    //  ShowMsg.showException(e, "disconnect", context);
                }
            });
        }

        finalizeObject();
    }

    private boolean formatPrintData() {
        Utilities.setLanguage(context);

        StringBuilder textData = new StringBuilder();
        Bitmap logoData = BitmapFactory.decodeResource(context.getResources(), R.drawable.imenunewprint);
        boolean isArabic = Utilities.isArabic();
        int count = 0;
        int spaces = 0;
        String space = "";
        Bitmap logo = null;
        String name = "";
        String price = "";
        int textAlignment = isArabic ? Printer.ALIGN_RIGHT : Printer.ALIGN_LEFT;
       /* try {
            logo = BitmapFactory.decodeStream(new URL(orderData.getLogo()).openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        try {

//            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            mPrinter.addTextAlign(Printer.ALIGN_RIGHT);


            mPrinter.addImage(logoData, 0, 0,
                    logoData.getWidth(),
                    logoData.getHeight(),
                    Printer.COLOR_1,
                    Printer.MODE_MONO,
                    Printer.HALFTONE_DITHER,
                    Printer.PARAM_DEFAULT,
                    Printer.COMPRESS_AUTO);
           /* if(isArabic) {
                mPrinter.addTextFont(30);
            }*/
            mPrinter.addFeedLine(1);

            mPrinter.addText(orderData.getDate());
            mPrinter.addFeedLine(1);
            mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.TRUE, Printer.TRUE, Printer.COLOR_4);
//            String tempVarOrderId = !isArabic ? context.getString(R.string.order_id).trim() + "#" + orderData.getSequence_no() :
//                    "#" + orderData.getSequence_no() + reverseString(context.getString(R.string.order_id).trim());
            String tempVarOrderId = context.getString(R.string.order_id).trim() + "#" + orderData.getSequence_no();
            mPrinter.addText(tempVarOrderId.trim());
            mPrinter.addFeedLine(2);

            mPrinter.addTextSmooth(Printer.TRUE);

            mPrinter.addTextSize(2, 2);
            mPrinter.addText(orderData.getTitle().trim());
            mPrinter.addTextSize(1, 1);
            mPrinter.addFeedLine(2);
            String tempAddress = orderData.getLocation().trim();//printerLocationPrintArabicBase(orderData.getLocation().trim());
            Log.e("TAG", "formatPrintData: " + tempAddress);
            mPrinter.addText(tempAddress);
            mPrinter.addFeedLine(2);
            mPrinter.addText(ServerConstants.vatUser +" : ضريبة القيمة المضافة ");

//            mPrinter.addTextAlign(textAlignment);
            mPrinter.addTextAlign(Printer.ALIGN_RIGHT);
            mPrinter.addTextSmooth(Printer.PARAM_DEFAULT);
            mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT);
            mPrinter.addFeedLine(2);
            mPrinter.addText(orderData.getDelivery()+ " : نوع التوصيل ");
            mPrinter.addFeedLine(1);
            mPrinter.addText(orderData.getCustomerName()+ " : اسم الزبون ");
            mPrinter.addFeedLine(1);

            mPrinter.addText(orderData.getPayment()+ " : نوع الدفع ");
            mPrinter.addFeedLine(1);

            mPrinter.addText(orderData.getAmtTaken()+ " : المبلغ المأخو ");
            mPrinter.addFeedLine(1);
            mPrinter.addText(orderData.getChangeGiven()+ " : التغييرات المقدمة ");
            mPrinter.addFeedLine(2);
            textData.append("------------------------------------------------");
            mPrinter.addText(textData.toString().trim());
            textData.delete(0, textData.length());
            mPrinter.addFeedLine(1);

            if (orderData.getItems().size() > 0) {
//                mPrinter.addTextAlign(textAlignment);
                mPrinter.addTextAlign(Printer.ALIGN_RIGHT);
                mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.TRUE, Printer.TRUE, Printer.COLOR_4);
                String tempVarItem = "المنتجات";
                mPrinter.addText(tempVarItem/*context.getString(R.string.items)*/.trim());
                mPrinter.addFeedLine(1);
                mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT);
                for (int i = 0; i < orderData.getItems().size(); i++) {
                    count++;

                    if (isArabic) {
                        name = orderData.getItems().get(i).getQtyAddedToCart() + "x " + orderData.getItems().get(i).getName() + " ." + count;
                    } else {
                        name = count + ") " + orderData.getItems().get(i).getName() + " x" + orderData.getItems().get(i).getQtyAddedToCart();
                    }

                    if (orderData.getItems().get(i).getTitleAr()!=null && !orderData.getItems().get(i).getTitleAr().isEmpty()){
                        name = orderData.getItems().get(i).getQtyAddedToCart() + "x " + orderData.getItems().get(i).getTitleAr() + " ." + count;
                    }

                    price = "SR " + orderData.getItems().get(i).getPriceForItem();
                    if (name.length() > 25) {
                        name = name.substring(0, 24) + "..";
                    }
                    spaces = 48 - name.length() - price.length();
                    space = "";
                    for (int j = 0; j < spaces; j++) {
                        space += " ";
                    }


//                    mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
                    mPrinter.addText(price + space + name);
                    mPrinter.addFeedLine(1);
                }

            }

            if (orderData.getMeal().size() > 0) {
                mPrinter.addFeedLine(1);
//                mPrinter.addTextAlign(textAlignment);
                mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.TRUE, Printer.TRUE, Printer.COLOR_4);
                mPrinter.addText(context.getString(R.string.meals).trim());
                mPrinter.addFeedLine(1);
                mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT);

                for (int i = 0; i < orderData.getMeal().size(); i++) {
                    count++;
                    if (isArabic) {
                        name = orderData.getMeal().get(i).getQtyAddedToCart() + "x " + orderData.getMeal().get(i).getTitle() + " (" + count;
                    } else {
                        name = count + ") " + orderData.getMeal().get(i).getTitle() + " x" + orderData.getMeal().get(i).getQtyAddedToCart();
                    }
                    price = "SR " + orderData.getMeal().get(i).getPriceForItem();
                    if (name.length() > 25) {
                        name = name.substring(0, 24) + "..";
                    }
                    spaces = 48 - name.length() - price.length();
                    space = "";
                    for (int j = 0; j < spaces; j++) {
                        space += " ";
                    }

//                    mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
                    mPrinter.addText(price + space + name);
                    mPrinter.addFeedLine(1);
                }

            }

            if (orderData.getDeals().size() > 0) {
                mPrinter.addFeedLine(1);
//                mPrinter.addTextAlign(textAlignment);
                mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.TRUE, Printer.TRUE, Printer.COLOR_4);
                mPrinter.addText(context.getString(R.string.deals).trim());
                mPrinter.addFeedLine(1);
                mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT);

                for (int i = 0; i < orderData.getDeals().size(); i++) {
                    count++;
                    if (isArabic) {
                        name = orderData.getDeals().get(i).getQtyAddedToCart() + "x " + orderData.getDeals().get(i).getTitle() + " (" + count;
                    } else {
                        name = count + ") " + orderData.getDeals().get(i).getTitle() + " x" + orderData.getDeals().get(i).getQtyAddedToCart();
                    }
                    price = "SR " + orderData.getDeals().get(i).getPriceForItem();
                    if (name.length() > 25) {
                        name = name.substring(0, 24) + "..";
                    }
                    spaces = 48 - name.length() - price.length();
                    space = "";
                    for (int j = 0; j < spaces; j++) {
                        space += " ";
                    }
//                    mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
                    mPrinter.addText(price + space + name);
                    mPrinter.addFeedLine(1);
                }

            }

            textData.append("------------------------------------------------");
            mPrinter.addText(textData.toString().trim());
            textData.delete(0, textData.length());
            mPrinter.addFeedLine(1);


//            name = !isArabic ? context.getString(R.string.subtotal) : reverseString(context.getString(R.string.subtotal));
            name = "المجموع الفرعي ";

            price = "SR "+orderData.getSubtotal();

            spaces = 48 - name.length() - price.length();
            space = "";
            for (int i = 0; i < spaces; i++) {
                space += " ";
            }

            mPrinter.addTextAlign(Printer.ALIGN_RIGHT);
//            mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
            mPrinter.addText(price + space + name);
            mPrinter.addFeedLine(1);


//            name = !isArabic ? context.getString(R.string.tax) : reverseString(context.getString(R.string.tax));
            name ="القيمة المضافة ";
            price = "SR "+orderData.getTax();
            spaces = 48 - name.length() - price.length();
            space = "";
            for (int i = 0; i < spaces; i++) {
                space += " ";
            }

//            mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
            mPrinter.addText(price + space + name);
            mPrinter.addFeedLine(1);


            name ="رسوم التوصيل ";
            price = "SR "+orderData.getDeliveryCharges();
            spaces = 48 - name.length() - price.length();
            space = "";
            for (int i = 0; i < spaces; i++) {
                space += " ";
            }


//            mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
            mPrinter.addText(price + space + name);
            mPrinter.addFeedLine(1);


            name ="المبلغ المخصص ";
            price = "SR "+orderData.getCustomAmount();
            spaces = 48 - name.length() - price.length();
            space = "";
            for (int i = 0; i < spaces; i++) {
                space += " ";
            }


//            mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
            mPrinter.addText(price + space + name);
            mPrinter.addFeedLine(1);


            if (orderData.getCustomAmount() > 0) {

                name = !isArabic ? context.getString(R.string.custom_amount) : reverseString(context.getString(R.string.custom_amount));
                price = "SR "+orderData.getCustomAmount();
                spaces = 48 - name.length() - price.length();
                space = "";
                for (int i = 0; i < spaces; i++) {
                    space += " ";
                }
//                mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
                mPrinter.addText(price + space + name);
                mPrinter.addFeedLine(1);

            }

            mPrinter.addText("------------------------------------------------");
            mPrinter.addFeedLine(1);

            mPrinter.addTextSize(2, 2);
            mPrinter.addTextAlign(Printer.ALIGN_RIGHT);

            name = "المجموع";
            price = "SR "+orderData.getTotal();
            spaces = 24 - name.length() - price.length();
            space = "";
            for (int i = 0; i < spaces; i++) {
                space += " ";
            }

//            mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
            mPrinter.addText(price + space + name);
            mPrinter.addFeedLine(1);


            mPrinter.addTextSize(1, 1);
            mPrinter.addTextAlign(Printer.ALIGN_RIGHT);
            mPrinter.addText("------------------------------------------------");


            if (orderData.getChangeGiven() != null && !orderData.getChangeGiven().equals("NULL") && !orderData.getChangeGiven().equals("")) {
                mPrinter.addFeedLine(1);
                if (Float.parseFloat(orderData.getChangeGiven()) > 0) {
                    name = !isArabic ? context.getString(R.string.change).trim() : reverseString(context.getString(R.string.change)).trim();
                    price = "SR "+orderData.getChangeGiven();
                    spaces = 48 - name.length() - price.length();
                    space = "";
                    for (int i = 0; i < spaces; i++) {
                        space += " ";
                    }

//                    mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
                    mPrinter.addText(price + space + name);
                }
            }
            mPrinter.addFeedLine(2);
            mPrinter.addCut(Printer.CUT_FEED);
            mPrinter.addPulse(Printer.DRAWER_2PIN, Printer.PULSE_100);
        } catch (Exception e) {
            //ShowMsg.showException(e, "Print data", context);
            return false;
        }
        return true;
    }

/*
    private boolean formatPrintData() {
        Utilities.setLanguage(context);

        StringBuilder textData = new StringBuilder();
        Bitmap logoData = BitmapFactory.decodeResource(context.getResources(), R.drawable.imenunewprint);
        boolean isArabic = Utilities.isArabic();
        int count = 0;
        int spaces = 0;
        String space = "";
        Bitmap logo = null;
        String name = "";
        String price = "";
        int textAlignment = isArabic ? Printer.ALIGN_RIGHT : Printer.ALIGN_LEFT;
        try {
            logo = BitmapFactory.decodeStream(new URL(orderData.getLogo()).openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {

            mPrinter.addTextAlign(Printer.ALIGN_CENTER);


            mPrinter.addImage(logo, 0, 0,
                    logo.getWidth(),
                    logo.getHeight(),
                    Printer.COLOR_1,
                    Printer.MODE_MONO,
                    Printer.HALFTONE_DITHER,
                    Printer.PARAM_DEFAULT,
                    Printer.COMPRESS_AUTO);
           */
/* if(isArabic) {
                mPrinter.addTextFont(30);
            }*//*

            mPrinter.addFeedLine(1);

            mPrinter.addTextSmooth(Printer.TRUE);

            mPrinter.addTextSize(2, 2);
            mPrinter.addText(orderData.getTitle().trim());
            mPrinter.addTextSize(1, 1);
            mPrinter.addFeedLine(1);
            String tempAddress = orderData.getLocation().trim();//printerLocationPrintArabicBase(orderData.getLocation().trim());
            Log.e("TAG", "formatPrintData: " + tempAddress + " ---" + orderData.getSequence_no());
            mPrinter.addText(tempAddress);
            mPrinter.addTextAlign(textAlignment);
            mPrinter.addTextSmooth(Printer.PARAM_DEFAULT);
            mPrinter.addFeedLine(1);


            mPrinter.addText(tvDate);
            mPrinter.addFeedLine(1);
            mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.TRUE, Printer.TRUE, Printer.COLOR_4);
            String tempVarOrderId = !isArabic ? context.getString(R.string.order_id).trim() + "#" + orderData.getSequence_no() :
                    "#" + orderData.getSequence_no() + reverseString(context.getString(R.string.order_id).trim());
            mPrinter.addText(tempVarOrderId.trim());

            mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT);
            mPrinter.addFeedLine(2);

            textData.append("------------------------------------------------");
            mPrinter.addText(textData.toString().trim());
            textData.delete(0, textData.length());
            mPrinter.addFeedLine(1);


            if (orderData.getItems().size() > 0) {
                mPrinter.addTextAlign(textAlignment);
                mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.TRUE, Printer.TRUE, Printer.COLOR_4);
                String tempVarItem = !isArabic ? context.getString(R.string.items) : reverseString(context.getString(R.string.items));
                mPrinter.addText(tempVarItem*/
/*context.getString(R.string.items)*//*
.trim());
                mPrinter.addFeedLine(1);
                mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT);
                for (int i = 0; i < orderData.getItems().size(); i++) {
                    count++;

                    if (isArabic) {
                        name = orderData.getItems().get(i).getQtyAddedToCart() + "x " + orderData.getItems().get(i).getName() + " (" + count;
                    } else {
                        name = count + ") " + orderData.getItems().get(i).getName() + " x" + orderData.getItems().get(i).getQtyAddedToCart();
                    }
                    price = "SR " + orderData.getItems().get(i).getPriceForItem();
                    if (name.length() > 25) {
                        name = name.substring(0, 24) + "..";
                    }
                    spaces = 48 - name.length() - price.length();
                    space = "";
                    for (int j = 0; j < spaces; j++) {
                        space += " ";
                    }


                    mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
                    mPrinter.addFeedLine(1);
                }

            }

            if (orderData.getMeal().size() > 0) {
                mPrinter.addFeedLine(1);
                mPrinter.addTextAlign(textAlignment);
                mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.TRUE, Printer.TRUE, Printer.COLOR_4);
                mPrinter.addText(context.getString(R.string.meals).trim());
                mPrinter.addFeedLine(1);
                mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT);
                for (int i = 0; i < orderData.getMeal().size(); i++) {
                    count++;
                    if (isArabic) {
                        name = orderData.getMeal().get(i).getQtyAddedToCart() + "x " + orderData.getMeal().get(i).getTitle() + " (" + count;
                    } else {
                        name = count + ") " + orderData.getMeal().get(i).getTitle() + " x" + orderData.getMeal().get(i).getQtyAddedToCart();
                    }
                    price = "SR " + orderData.getMeal().get(i).getPriceForItem();
                    if (name.length() > 25) {
                        name = name.substring(0, 24) + "..";
                    }
                    spaces = 48 - name.length() - price.length();
                    space = "";
                    for (int j = 0; j < spaces; j++) {
                        space += " ";
                    }

                    mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
                    mPrinter.addFeedLine(1);
                }

            }

            if (orderData.getDeals().size() > 0) {
                mPrinter.addFeedLine(1);
                mPrinter.addTextAlign(textAlignment);
                mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.TRUE, Printer.TRUE, Printer.COLOR_4);
                mPrinter.addText(context.getString(R.string.deals).trim());
                mPrinter.addFeedLine(1);
                mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT);
                for (int i = 0; i < orderData.getDeals().size(); i++) {
                    count++;
                    if (isArabic) {
                        name = orderData.getDeals().get(i).getQtyAddedToCart() + "x " + orderData.getDeals().get(i).getTitle() + " (" + count;
                    } else {
                        name = count + ") " + orderData.getDeals().get(i).getTitle() + " x" + orderData.getDeals().get(i).getQtyAddedToCart();
                    }
                    price = "SR " + orderData.getDeals().get(i).getPriceForItem();
                    if (name.length() > 25) {
                        name = name.substring(0, 24) + "..";
                    }
                    spaces = 48 - name.length() - price.length();
                    space = "";
                    for (int j = 0; j < spaces; j++) {
                        space += " ";
                    }
                    mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
                    mPrinter.addFeedLine(1);
                }

            }

            textData.append("------------------------------------------------");
            mPrinter.addText(textData.toString().trim());
            textData.delete(0, textData.length());
            mPrinter.addFeedLine(1);


            name = !isArabic ? context.getString(R.string.subtotal) : reverseString(context.getString(R.string.subtotal));
            price = tvSubTotal;
            spaces = 48 - name.length() - price.length();
            space = "";
            for (int i = 0; i < spaces; i++) {
                space += " ";
            }

            mPrinter.addTextAlign(Printer.ALIGN_RIGHT);
            mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
            mPrinter.addFeedLine(1);


            name = !isArabic ? context.getString(R.string.tax) : reverseString(context.getString(R.string.tax));
            price = tvTax;
            spaces = 48 - name.length() - price.length();
            space = "";
            for (int i = 0; i < spaces; i++) {
                space += " ";
            }

            mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
            mPrinter.addFeedLine(1);


            if (orderData.getCustomAmount() > 0) {

                name = !isArabic ? context.getString(R.string.custom_amount) : reverseString(context.getString(R.string.custom_amount));
                price = tvCustomAmount;
                spaces = 48 - name.length() - price.length();
                space = "";
                for (int i = 0; i < spaces; i++) {
                    space += " ";
                }
                mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
                mPrinter.addFeedLine(1);

            }

            mPrinter.addText("------------------------------------------------");
            mPrinter.addFeedLine(1);

            mPrinter.addTextSize(2, 2);
            mPrinter.addTextAlign(Printer.ALIGN_RIGHT);

            name = !isArabic ? context.getString(R.string.total) : reverseString(context.getString(R.string.total));
            price = tvTotal;
            spaces = 24 - name.length() - price.length();
            space = "";
            for (int i = 0; i < spaces; i++) {
                space += " ";
            }

            mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
            mPrinter.addFeedLine(1);


            mPrinter.addTextSize(1, 1);
            mPrinter.addTextAlign(Printer.ALIGN_RIGHT);
            mPrinter.addText("------------------------------------------------");


            if (orderData.getChangeGiven() != null && !orderData.getChangeGiven().equals("NULL") && !orderData.getChangeGiven().equals("")) {
                mPrinter.addFeedLine(1);
                if (Float.parseFloat(orderData.getChangeGiven()) > 0) {
                    name = !isArabic ? context.getString(R.string.change).trim() : reverseString(context.getString(R.string.change)).trim();
                    price = tvChange;
                    spaces = 48 - name.length() - price.length();
                    space = "";
                    for (int i = 0; i < spaces; i++) {
                        space += " ";
                    }

                    mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
                }
            }
            mPrinter.addFeedLine(2);
            mPrinter.addCut(Printer.CUT_FEED);
            mPrinter.addPulse(Printer.DRAWER_2PIN, Printer.PULSE_100);
        } catch (Exception e) {
            //ShowMsg.showException(e, "Print data", context);
            return false;
        }
        return true;
    }
*/

    private String printerLocationPrintArabicBase(String trimLoc) {
        StringBuilder finalString = new StringBuilder(" ");
        boolean isArabic = Utilities.isArabic();
        String[] tempLocs = trimLoc.split(",");
        if (isArabic) {
            String tempLastSenntence = tempLocs[tempLocs.length - 1];
            String[] tempSpace = tempLastSenntence.split(" ");
            for (int i = tempSpace.length - 1; i > 0; i--) {
                if (i == 1) {
                    finalString.append(tempSpace[i] + " ");
                } else {
                    finalString.append(reverseString(tempSpace[i]) + " ");
                }
            }
            String t = "";
            for (int i = tempLocs.length - 2; i > 0; i--) {
                t = finalString.append(tempLocs[i]).toString();
            }

            Log.e("final", t);
            return t;
        } else {

            String t = finalString.append(trimLoc).toString();
            Log.e("final", t);
            return t;
        }

    }

    private String reverseString(String str) {
        StringBuilder sb = new StringBuilder(str);
        return sb.reverse().toString();
    }

}