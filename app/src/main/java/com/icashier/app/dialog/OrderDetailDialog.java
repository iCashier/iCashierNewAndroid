package com.icashier.app.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.epson.epos2.Epos2CallbackCode;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.OrderItemsAdapter;
import com.icashier.app.adapter.PrinterListAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.DialogOrderDetailBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.DateUtils;
import com.icashier.app.helper.PermissionsUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.GenreatePDFModer;
import com.icashier.app.model.OrderListResponse;
import com.icashier.app.printer.ShowMsg;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.icashier.app.fragment.MerchantHomeFragment.createDrawableFromView;

public class OrderDetailDialog extends Dialog implements ReceiveListener {

    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public Printer mPrinter = null;
    Context context;
    DialogOrderDetailBinding binding;
    RestClient.ApiRequest apiRequest;
    double lat, lng;
    GoogleMap googleMap;
    MarkerOptions markerOptions;
    LayoutInflater inflater;
    String targetPrinter = "";
    private OrderListResponse.ResultBean orderData;
    private List<OrderListResponse.ResultBean.ItemsBean> itemList = new ArrayList<>();
    private SupportMapFragment mapFragment;


    public OrderDetailDialog(HomeActivity context, OrderListResponse.ResultBean orderData) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.orderData = orderData;

    }

    private void getExtras() {
        // orderData=(OrderListResponse.ResultBean)getIntent().getSerializableExtra(AppConstant.ORDER_DETAIL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        // binding = DataBindingUtil.setContentView(context, R.layout.dialog_order_detail);
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_order_detail, null, false);
        setContentView(binding.getRoot());

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        binding.statusPending.setVisibility(View.GONE);
        getExtras();
        setData();

        binding.imgShare.setOnClickListener(V -> {
            String uri = "http://maps.google.com/maps?daddr=" + lat + "," + lng;

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String ShareSub = "Location";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, ShareSub);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri);
            context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
        });

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
        });

        binding.flReceipt.setOnClickListener(V -> {

            generateNewPdf();

        });

        binding.statusPending.setOnClickListener(V -> {
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
        });
    }

    private void generateNewPdf() {
        binding.progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(orderData.getId()));
        if (SharedPrefManager.getInstance(context).getString(AppConstant.APP_LANGUAGE, "").equals("ar")) {
            params.put("lang", "1");
        } else {
            params.put("lang", "0");
        }

        apiRequest = new RestClient.ApiRequest(context);

        apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GENERATE_NEW_PDF)
                .setMethod(RestClient.ApiRequest.METHOD_POST)
                .setParams(params)
                .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                .setHeader2("type",SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE,AppConstant.TYPE_MERCHANT))
                .setResponseListener(new RestClient.ResponseListener() {
                    @Override
                    public void onResponse(String tag, String response) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (Utilities.isValidJson(response)) {
                            Log.e("RESULTpdf",response);
                            Log.e("typeaaya",SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE,AppConstant.TYPE_MERCHANT));
                            GenreatePDFModer pdfModer= new Gson().fromJson(response, GenreatePDFModer.class);
                            if (pdfModer != null) {
                                new PaymentReceiptDialog(context, pdfModer.getResult().getPdf(), new PaymentReceiptDialog.PrintReceipt() {
                                    @Override
                                    public void onPrint() {
                                        startPrintingReceipt();
                                    }
                                }).show();
                            } else {
                                AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                            }

                        }
                    }
                })
                .setErrorListener(new RestClient.ErrorListener() {
                    @Override
                    public void onError(String tag, String errorMsg) {
                        binding.progressBar.setVisibility(View.GONE);
                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        Log.e("RESULTpdf",errorMsg);
                    }
                })
                .execute();
    }

    //=================Method to set data===================//
    private void setData() {
        binding.tvCashierName.setText(orderData.getCashierName());

        if (orderData.getCashierName() != null & !orderData.getCashierName().equals("")) {
            binding.llCashierName.setVisibility(View.VISIBLE);
        } else {
            binding.llCashierName.setVisibility(View.GONE);
        }
        if (!orderData.getCustomerName().equals("")) {
            binding.llCustomerName.setVisibility(View.VISIBLE);
            binding.tvCustomerName.setText(orderData.getCustomerName());
        }
        if (orderData.getPaymentStatus() == 0 && !orderData.getStatus().equalsIgnoreCase(AppConstant.STATUS_PENDING)) {
            binding.btnPayment.setVisibility(View.VISIBLE);
        } else {
            binding.btnPayment.setVisibility(View.GONE);
        }
        if (orderData.getDelivery().equals(AppConstant.TYPE_DINE_IN)) {
            binding.tvDelivery.setText(context.getString(R.string.dine_in_tab));
            binding.delCharges.setVisibility(View.GONE);

        } else if (orderData.getDelivery().equals(AppConstant.TYPE_PICKUP)) {
            binding.tvDelivery.setText(context.getString(R.string.pickup));
            binding.delCharges.setVisibility(View.GONE);

        } else if (orderData.getDelivery().equals(AppConstant.TYPE_DELIVERY)) {
            binding.tvDelivery.setText(context.getString(R.string.delivery));
            setAddress();

        } else if (orderData.getDelivery().equals(AppConstant.TYPE_INSTA_DELIVERY)) {
            binding.tvDelivery.setText(context.getString(R.string.insta_delivery));
            setAddress();

        } else if (orderData.getDelivery().equals(AppConstant.TYPE_SWIFT_DELIVERY)) {
            binding.tvDelivery.setText(context.getString(R.string.swift_delivery));
            setAddress();
        } else if (orderData.getDelivery().equals(AppConstant.CASHIER)) {
            binding.tvDelivery.setText(context.getString(R.string.dine_in_tab));
            binding.llStatus.setVisibility(View.GONE);
            binding.delCharges.setVisibility(View.GONE);
        }

        if (orderData.getPayment().equalsIgnoreCase(AppConstant.CASH)) {
            binding.tvPayment.setText(context.getString(R.string.cash));
        } else if (orderData.getPayment().equalsIgnoreCase(AppConstant.CREDIT_CARD)) {
            binding.tvPayment.setText(context.getString(R.string.credit_card));
            binding.btnPayment.setVisibility(View.GONE);
        } else {
            binding.tvPayment.setText(context.getString(R.string.online));
            binding.btnPayment.setVisibility(View.GONE);
        }

        if (!orderData.getTableName().equalsIgnoreCase("null") && !orderData.getTableName().equals("")) {
            // binding.tvSeqNo.setText("#" + orderData.getSequence_no() + " - " + orderData.getTableName());
            // binding.tvOrderId.setText("#" + orderData.getId() );
            binding.tvOrderId.setText("#" + orderData.getSequence_no() + " - " + orderData.getTableName());

        } else {
            // binding.tvSeqNo.setText("#" + orderData.getSequence_no() );
            //binding.tvOrderId.setText("#" + orderData.getId());

            binding.tvOrderId.setText("#" + orderData.getSequence_no());
        }

        binding.tvSubTotal.setText("SR " + Utilities.formatPrice(Float.parseFloat(orderData.getSubtotal())));
        binding.tvTax.setText("SR " + Utilities.formatPrice(Float.parseFloat(orderData.getTax())));

        float totalPrice = Float.parseFloat(orderData.getTotal());
        totalPrice = (float) (Math.round(totalPrice * 100.0) / 100.0);

        binding.tvTotal.setText("SR " + Utilities.formatPrice(totalPrice));

        if (orderData.getCustomAmount() > 0) {
            binding.clCustomAmount.setVisibility(View.VISIBLE);
            binding.tvCustomAmount.setText("SR " + Utilities.formatPrice(orderData.getCustomAmount()));
        } else {
            binding.clCustomAmount.setVisibility(View.GONE);
        }
        if (orderData.getChangeGiven() != null && !orderData.getChangeGiven().equals("NULL")
                && Double.parseDouble(orderData.getChangeGiven()) > 0) {
            binding.clChange.setVisibility(View.VISIBLE);
            binding.tvChange.setText("SR " + Utilities.formatPrice(Float.parseFloat(orderData.getChangeGiven())));
        }
        itemList.addAll(orderData.getItems());
        itemList.addAll(orderData.getMeal());
        itemList.addAll(orderData.getDeals());
        try {
            binding.tvDate.setText(DateUtils.convertUTCtoLocalTime2(orderData.getDate(), "dd MMM yyyy hh:mm:ss"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        OrderItemsAdapter adapter = new OrderItemsAdapter(context, itemList);
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
        }

    }

    //=========Mehtod to show current porder status===========//
    private void setSelection(View view) {
        binding.statusPending.setSelected(false);
        binding.statusProcessing.setSelected(false);
        binding.statusDelivered.setSelected(false);
        view.setSelected(true);

    }


    //=======method to chnage order status api==============//
    private void callChangeStatusApi(String status) {
        if (Utilities.isNetworkAvailable(context)) {
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> params = new HashMap<>();
            params.put("id", "" + orderData.getId());
            params.put("status", status);
            params.put("device_id", SharedPrefManager.getInstance(context).getString(AppConstant.KEY_DEVICE_TOKEN, ""));

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.CHANGE_ORDER_STATUS)
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
                                            orderData.setStatus(status);
                                            if (status.equalsIgnoreCase(AppConstant.STATUS_PENDING)) {
                                                setSelection(binding.statusPending);
                                            } else if (status.equalsIgnoreCase(AppConstant.STATUS_PROCESSING)) {
                                                setSelection(binding.statusProcessing);
                                                orderData.setIsReordered(0);

                                            } else if (status.equalsIgnoreCase(AppConstant.STATUS_DELIVERED)) {
                                                setSelection(binding.statusDelivered);
                                            } else if (status.equalsIgnoreCase(AppConstant.STATUS_READY)) {
                                                setSelection(binding.statusReady);
                                            }

                                            if (orderData.getPaymentStatus() == 0 && !orderData.getStatus().equalsIgnoreCase(AppConstant.STATUS_PENDING)) {
                                                binding.btnPayment.setVisibility(View.VISIBLE);
                                            } else {
                                                binding.btnPayment.setVisibility(View.GONE);
                                            }
                                        } else if (genericResponse.getCode() == 301) {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                   /* AlertUtil.showAlertWindow(context, context.getString(R.string.still_want_to_proceed), new OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            callChangeStatusApi(status);
                                                        }
                                                    });*/
                                                }
                                            });
                                        }

                                    } else {
                                        binding.progressBar.setVisibility(View.VISIBLE);
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }
                                }
                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            binding.progressBar.setVisibility(View.VISIBLE);
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        } else {
            AlertUtil.toastMsg(context, context.getString(R.string.network_error));
        }
    }

    //===========Mehhod to call api to acknwoledge payment==========//
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
                                                  /*  AlertUtil.showAlertWindow(context, context.getString(R.string.still_want_to_proceed), new OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                        }
                                                    });*/
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
    }

    public void setAddress() {
        binding.llAddress.setVisibility(View.VISIBLE);
        binding.tvAddress.setText(orderData.getAddressText());
        binding.tvPhone.setText(orderData.getMobileNo());
        if (orderData.getLat() != null && orderData.getLng() != null) {
            lat = Double.parseDouble(orderData.getLat());
            lng = Double.parseDouble(orderData.getLng());
            setMap();
        } else {
            binding.llMap.setVisibility(View.GONE);
        }

    }

    //===============Method to set map=====================//
    private void setMap() {
        MapsInitializer.initialize(context);
        binding.mapView.onCreate(onSaveInstanceState());
        binding.mapView.onResume();
        binding.mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng location = new LatLng(lat, lng);

                markerOptions = new MarkerOptions();
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context)));
                markerOptions.position(location);

                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17.0f));
                googleMap.addMarker(markerOptions);
            }
        });

    }

    //=============Method to print receipt==============//
    private void startPrintingReceipt() {
        PermissionsUtil.askPermissions((Activity) context, PermissionsUtil.STORAGE, PermissionsUtil.LOCATION, new PermissionsUtil.PermissionListener() {
            @Override
            public void onPermissionResult(boolean isGranted) {
                if (isGranted) {
                    if (SharedPrefManager.getInstance(context).getString(AppConstant.PRINTER_DEVICES_TARGET, "") != null
                            && !SharedPrefManager.getInstance(context).getString(AppConstant.PRINTER_DEVICES_TARGET, "").trim().isEmpty()) {
                        //binding.llChangePass.setVisibility(View.GONE);
                        targetPrinter = SharedPrefManager.getInstance(context).getString(AppConstant.PRINTER_DEVICES_TARGET, "");
                        runPrintReceiptSequence();
                    } else {
                        new SelectPrinterDialog(context, new PrinterListAdapter.SelectPrinterListener() {
                            @Override
                            public void onClick(String name, String target) {
                                Log.e(name, target);
                                targetPrinter = target;
                                SharedPrefManager myPref = SharedPrefManager.getInstance(context);
                                myPref.saveString(AppConstant.PRINTER_DEVICES_TARGET, target);
                                runPrintReceiptSequence();
                            }
                        }).show();
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
        }

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
            ShowMsg.showException(e, "Printer", context);
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
            ShowMsg.showMsg(makeErrorMessage(status), context);
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
            ShowMsg.showException(e, "sendData", context);

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
            Log.e("connect", "error");
            ShowMsg.showException(e, "connect", context);

            return false;
        }

        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        } catch (Exception e) {
            Log.e("beginTransaction", "err");
            ShowMsg.showException(e, "beginTransaction", context);

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
    public void onPtrReceive(Printer printer, int i, PrinterStatusInfo printerStatusInfo, String s) {


        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ShowMsg.showResult(i, makeErrorMessage(printerStatusInfo), context);
                        disconnectPrinter();

                        if (i == Epos2CallbackCode.CODE_SUCCESS) {

                        }


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
                    ShowMsg.showException(e, "endTransaction", context);
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
                    ShowMsg.showException(e, "disconnect", context);
                }
            });
        }

        finalizeObject();
    }


    private boolean formatPrintData() {
        Utilities.setLanguage(context);

        StringBuilder textData = new StringBuilder();
        Bitmap logoData = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_logo_login);
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
           /* if(isArabic) {
                mPrinter.addTextFont(30);
            }*/
            mPrinter.addFeedLine(1);

            mPrinter.addTextSmooth(Printer.TRUE);

            mPrinter.addTextSize(2, 2);
            mPrinter.addText(orderData.getTitle().trim());
            mPrinter.addTextSize(1, 1);
            mPrinter.addFeedLine(1);
            String tempAddress = orderData.getLocation().trim();//printerLocationPrintArabicBase(orderData.getLocation().trim());
            Log.e("TAG", "formatPrintData: " + tempAddress);
            mPrinter.addText(tempAddress);
            mPrinter.addTextAlign(textAlignment);
            mPrinter.addTextSmooth(Printer.PARAM_DEFAULT);
            mPrinter.addFeedLine(1);


            mPrinter.addText(binding.tvDate.getText().toString());
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
                mPrinter.addText(tempVarItem/*context.getString(R.string.items)*/.trim());
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
            price = binding.tvSubTotal.getText().toString();
            spaces = 48 - name.length() - price.length();
            space = "";
            for (int i = 0; i < spaces; i++) {
                space += " ";
            }

            mPrinter.addTextAlign(Printer.ALIGN_RIGHT);
            mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
            mPrinter.addFeedLine(1);


            name = !isArabic ? context.getString(R.string.tax) : reverseString(context.getString(R.string.tax));
            price = binding.tvTax.getText().toString();
            spaces = 48 - name.length() - price.length();
            space = "";
            for (int i = 0; i < spaces; i++) {
                space += " ";
            }

            mPrinter.addText((!isArabic ? (name + space + price) : (price + space + name)).trim());
            mPrinter.addFeedLine(1);


            if (orderData.getCustomAmount() > 0) {

                name = !isArabic ? context.getString(R.string.custom_amount) : reverseString(context.getString(R.string.custom_amount));
                price = binding.tvCustomAmount.getText().toString();
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
            price = binding.tvTotal.getText().toString();
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
                    price = binding.tvChange.getText().toString();
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

