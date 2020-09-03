package com.icashier.app.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epson.epos2.Epos2CallbackCode;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.CheckoutItemsAdapter;
import com.icashier.app.adapter.PrinterListAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentCheckoutBinding;
import com.icashier.app.dialog.AddAdressDialog;
import com.icashier.app.dialog.CalculatorDialog;
import com.icashier.app.dialog.PaymentCardDialog;
import com.icashier.app.dialog.PaymentReceiptDialog;
import com.icashier.app.dialog.SelectPrinterDialog;
import com.icashier.app.dialog.TaxesListDialog;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.DateUtils;
import com.icashier.app.helper.PermissionsUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CalculatorListener;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.model.AddressModel;
import com.icashier.app.model.CartItemModel;
import com.icashier.app.model.CheckoutResponse;
import com.icashier.app.model.GetTaxResponse;
import com.icashier.app.printer.ShowMsg;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;


public class CheckoutFragment extends Fragment implements ReceiveListener {

    public Printer mPrinter = null;
    HomeActivity context;
    FragmentCheckoutBinding binding;
    List<CartItemModel> itemList = new ArrayList<>();
    float cartPrice, totalPrice, customAmount;
    String deliveryMode, paymentMode;
    AddressModel addressModel;
    RestClient.ApiRequest apiRequest;
    String taxAmount = "";
    double amtTaken = 0, changeGiven = 0;
    List<Integer> arrAmt = new ArrayList<>();
    String targetPrinter = "";
    List<CartItemModel> selectedItems = new ArrayList<>();
    List<CartItemModel> selectedMeals = new ArrayList<>();
    List<CartItemModel> selectedDeals = new ArrayList<>();
    String orderId = "";
    CheckoutResponse checkoutResponse;
    FragmentTransaction ft;
    PaymentReceiptDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = (HomeActivity) getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkout, container, false);

        setData();
        Utilities.rotateViews(binding.flBack);
        callGetTaxApi();
        setOnClickListener();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void setCustomAmount(float amount) {

        if (amount > 0) {
            binding.tvCustomAmount.setText("SR " + Utilities.formatPrice(amount));
            totalPrice = cartPrice + amount + Float.parseFloat(taxAmount);
            totalPrice = (float) (Math.round(totalPrice * 100.0) / 100.0);
            binding.tvTotal.setText("SR " + Utilities.formatPrice(totalPrice));
            customAmount = amount;
            getRoundOffs(totalPrice);
            if (amtTaken != 0) {
                float change = (float) (Math.round((amtTaken - totalPrice) * 100.0) / 100.0);
                if (change > 0) {
                    binding.tvChange.setText("SR " + Utilities.formatPrice(change));
                } else {
                    binding.tvChange.setText("SR 0.00");
                    amtTaken = 0;
                    changeGiven = 0;
                    binding.tv1.setSelected(false);
                    binding.tv2.setSelected(false);
                    binding.tv3.setSelected(false);
                    binding.tv4.setSelected(false);
                }
            }
        }

    }


    //==================Method to set Data=========//
    private void setData() {
        setDeliverySelector(binding.imgDineIn);
        setPaymentSelector(binding.imgCash);

        deliveryMode = AppConstant.TYPE_DINE_IN;
        paymentMode = AppConstant.CASH;
        String items = getArguments().getString(AppConstant.CART_ITEMS);
        itemList = new Gson().fromJson(items, new TypeToken<List<CartItemModel>>() {
        }.getType());

        cartPrice = getArguments().getFloat(AppConstant.CART_TOTAL);
        customAmount = getArguments().getFloat(AppConstant.CUSTOM_AMOUNT);
        binding.tvSubTotal.setText("SR " + Utilities.formatPrice(cartPrice));

        binding.tvDate.setText(DateUtils.getCurrentDate("dd MMM yyyy hh:mm:ss"));
        if (!SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_NAME, "").equals("")) {
            binding.tvCashierName.setText(SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_NAME, ""));
        } else {
            binding.tvCashierName.setText(SharedPrefManager.getInstance(context).getString(AppConstant.MERCHANT_NAME, ""));

        }
        CheckoutItemsAdapter adapter = new CheckoutItemsAdapter(context, itemList);
        binding.rvItems.setLayoutManager(new LinearLayoutManager(context));
        binding.rvItems.setAdapter(adapter);

    }


    //=================Mehtod to set onClick listener=======//
    private void setOnClickListener() {
        binding.flBack.setOnClickListener(V -> {
            context.onBackPressed();
        });

        binding.imgDineIn.setOnClickListener(V -> {
            setDeliverySelector(binding.imgDineIn);
        });

        binding.imgPickup.setOnClickListener(V -> {
            setDeliverySelector(binding.imgPickup);
        });
        binding.imgDelivery.setOnClickListener(V -> {
            setDeliverySelector(binding.imgDelivery);
        });
        binding.imgSwiftDelivery.setOnClickListener(V -> {
            setDeliverySelector(binding.imgSwiftDelivery);
        });
        binding.imgInstaDelivery.setOnClickListener(V -> {
            setDeliverySelector(binding.imgInstaDelivery);
        });

        binding.imgCash.setOnClickListener(V -> {
            setPaymentSelector(binding.imgCash);
        });

        binding.imgCreditCard.setOnClickListener(V -> {
            setPaymentSelector(binding.imgCreditCard);
        });

        binding.imgEdit.setOnClickListener(V -> {
            Intent intent = new Intent(context, AddAdressDialog.class);
            intent.putExtra(AppConstant.FULL_ADDRESS, addressModel);
            startActivityForResult(intent, 6);
        });

        binding.btnPayment.setOnClickListener(V -> {
            if (paymentMode.equals(AppConstant.CREDIT_CARD)) {
                Intent intent = new Intent(context, PaymentCardDialog.class);
                intent.putExtra(AppConstant.TOTAL_AMOUNT, totalPrice);
                startActivityForResult(intent, 7);
            } else {
                callCheckoutApi();
            }

        });

        binding.tvViewTax.setOnClickListener(V -> {
            new TaxesListDialog(context).show();
        });

        binding.tv1.setOnClickListener(V -> {
            selectRecievedMoney(binding.tv1);
        });

        binding.tv2.setOnClickListener(V -> {
            selectRecievedMoney(binding.tv2);
        });

        binding.tv3.setOnClickListener(V -> {
            selectRecievedMoney(binding.tv3);
        });

        binding.tv4.setOnClickListener(V -> {
            selectRecievedMoney(binding.tv4);
        });

        binding.imgCustomAmount.setOnClickListener(V -> {
            new CalculatorDialog(context, new CalculatorListener() {
                @Override
                public void onSaveClick(float val) {
                    if (val > totalPrice) {
                        binding.tv1.setSelected(false);
                        binding.tv2.setSelected(false);
                        binding.tv3.setSelected(false);
                        binding.tv4.setSelected(false);

                        float change = (float) (Math.round((val - totalPrice) * 100.0) / 100.0);
                        binding.tvChange.setText("SR " + Utilities.formatPrice(change));
                    } else {
                        AlertUtil.toastMsg(context, getString(R.string.amount_must_be_greater));

                    }
                }
            }).show();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 6) {
            addressModel = (AddressModel) data.getSerializableExtra(AppConstant.FULL_ADDRESS);
            binding.tvUsername.setVisibility(View.VISIBLE);
            binding.tvUsername.setText(addressModel.getName());
            binding.tvAddress.setVisibility(View.VISIBLE);
            binding.tvAddress.setText(addressModel.getAddress() + ", " + addressModel.getCity() + ", " + addressModel.getCountry() + " | " +
                    addressModel.getCode() + " " + addressModel.getPhoneNumber());

        } else if (resultCode == AppConstant.CARD_PAYMENT) {
            callCheckoutApi();
        }
    }

    //=================Method to select delivery mode=============//
    private void setDeliverySelector(ImageView img) {
        if (img == binding.imgDineIn) {
            binding.imgDineIn.setSelected(true);
            binding.imgDelivery.setSelected(false);
            binding.imgPickup.setSelected(false);
            binding.imgInstaDelivery.setSelected(false);
            binding.imgSwiftDelivery.setSelected(false);
            deliveryMode = AppConstant.TYPE_DINE_IN;

        } else if (img == binding.imgDelivery) {
            binding.imgDineIn.setSelected(false);
            binding.imgDelivery.setSelected(true);
            binding.imgPickup.setSelected(false);
            binding.imgInstaDelivery.setSelected(false);
            binding.imgSwiftDelivery.setSelected(false);
            deliveryMode = AppConstant.TYPE_DELIVERY;

        } else if (img == binding.imgPickup) {
            binding.imgDineIn.setSelected(false);
            binding.imgDelivery.setSelected(false);
            binding.imgPickup.setSelected(true);
            binding.imgInstaDelivery.setSelected(false);
            binding.imgSwiftDelivery.setSelected(false);
            deliveryMode = AppConstant.TYPE_PICKUP;

        } else if (img == binding.imgSwiftDelivery) {
            binding.imgDineIn.setSelected(false);
            binding.imgDelivery.setSelected(false);
            binding.imgPickup.setSelected(false);
            binding.imgInstaDelivery.setSelected(false);
            binding.imgSwiftDelivery.setSelected(true);
            deliveryMode = AppConstant.TYPE_SWIFT_DELIVERY;

        } else if (img == binding.imgInstaDelivery) {
            binding.imgDineIn.setSelected(false);
            binding.imgDelivery.setSelected(false);
            binding.imgPickup.setSelected(false);
            binding.imgInstaDelivery.setSelected(true);
            binding.imgSwiftDelivery.setSelected(false);
            deliveryMode = AppConstant.TYPE_INSTA_DELIVERY;

        }

        if (img == binding.imgDineIn || img == binding.imgPickup) {
            binding.llAddress.setVisibility(View.GONE);
            binding.llCustomerName.setVisibility(View.VISIBLE);
        } else {
            binding.llAddress.setVisibility(View.VISIBLE);
            binding.llCustomerName.setVisibility(View.GONE);
        }
    }

    //=================Method to select payment mode=============//
    private void setPaymentSelector(ImageView img) {
        if (img == binding.imgCash) {
            binding.imgCash.setSelected(true);
            binding.imgCreditCard.setSelected(false);
            paymentMode = AppConstant.CASH;
        } else if (img == binding.imgCreditCard) {
            binding.imgCash.setSelected(false);
            binding.imgCreditCard.setSelected(true);
            paymentMode = AppConstant.CREDIT_CARD;
        }
    }

    private boolean isInputValid() {
        if (deliveryMode.equals(AppConstant.TYPE_DINE_IN) || deliveryMode.equals(AppConstant.TYPE_PICKUP)) {

        } else {
            if (addressModel != null) {
                return true;
            } else {
                AlertUtil.toastMsg(context, "Please enter address.");
            }
        }
        return false;
    }

    //==========================Method to call checkout api=====================//
    private void callGetTaxApi() {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);


            HashMap<String, String> params = new HashMap<>();

            params.put("amount", "" + cartPrice);

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.CALCULATE_TAX)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                GetTaxResponse taxResponse = new Gson().fromJson(response, GetTaxResponse.class);
                                if (taxResponse != null) {
                                    if (taxResponse.getCode() == 200) {
                                        binding.tvTax.setText("SR " + taxResponse.getResult().getTaxAmount());
                                        taxAmount = taxResponse.getResult().getTaxAmount();
                                        totalPrice = cartPrice + Float.parseFloat(taxAmount);
                                        totalPrice = (float) (Math.round(totalPrice * 100.0) / 100.0);
                                        binding.tvTotal.setText("SR " + Utilities.formatPrice(totalPrice));
                                        getRoundOffs(totalPrice);
                                        setCustomAmount(customAmount);


                                    }
                                } else {
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

    //==========================Method to call checkout api=====================//
    private void callCheckoutApi() {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);

            selectedItems.clear();
            selectedMeals.clear();
            selectedDeals.clear();

            for (int i = 0; i < itemList.size(); i++) {
                if (itemList.get(i).getType().equals("meal")) {
                    selectedMeals.add(itemList.get(i));
                } else if (itemList.get(i).getType().equals("deal")) {
                    selectedDeals.add(itemList.get(i));
                } else {
                    selectedItems.add(itemList.get(i));
                }
            }
            HashMap<String, String> params = new HashMap<>();

            params.put("items", new Gson().toJson(selectedItems));
            params.put("meal", new Gson().toJson(selectedMeals));
            params.put("deals", new Gson().toJson(selectedDeals));
            params.put("payment", paymentMode);
            params.put("delivery", "cashier");
            params.put("subtotal", "" + cartPrice);
            params.put("total", "" + totalPrice);
            params.put("deliveryCharges", "0");
            params.put("paymentStatus", "1");
            params.put("cashierName", binding.tvCashierName.getText().toString());
            params.put("tz_offset", "" + TimeZone.getDefault().getRawOffset());
            params.put("device_id", SharedPrefManager.getInstance(context).getString(AppConstant.KEY_DEVICE_TOKEN, ""));
            params.put("tax", taxAmount);
            params.put("tip", "");
            params.put("amtTaken", "" + amtTaken);
            params.put("changeGiven", "" + changeGiven);
            if (deliveryMode.equals(AppConstant.TYPE_DINE_IN) || deliveryMode.equalsIgnoreCase(AppConstant.TYPE_PICKUP)) {
                params.put("customerName", binding.etCustomerName.getText().toString().trim());
                params.put("address", "");
            } else {
                params.put("customerName", binding.tvUsername.getText().toString());
                params.put("address", binding.tvAddress.getText().toString());
            }

            if (customAmount != 0) {
                params.put("customAmount", "" + customAmount);
            }

            apiRequest = new RestClient.ApiRequest(context);


            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.CHECKOUT)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                checkoutResponse = new Gson().fromJson(response, CheckoutResponse.class);
                                if (checkoutResponse != null) {
                                    if (checkoutResponse.getCode() == 200) {

                                        ft = context.getSupportFragmentManager().beginTransaction();
                                        SharedPrefManager.getInstance(context).removeData(AppConstant.CART_HISTORY);
                                        SharedPrefManager.getInstance(context).removeData(AppConstant.CUSTOM_AMOUNT);
                                        orderId = "" + checkoutResponse.getInsertId();


                                        dialog = new PaymentReceiptDialog(context, checkoutResponse.getResult().getPdf(), new PaymentReceiptDialog.PrintReceipt() {
                                            @Override
                                            public void onPrint() {
                                                startPrintingReceipt();
                                            }
                                        });
//                                        dialog.show();
                                        startPrintingReceipt();
                                        ft.replace(R.id.frame, new OrdersFragment(), OrdersFragment.class.getSimpleName())
                                                .commit();
                                        context.getSupportFragmentManager().popBackStack();
                                        dialog.setOnDismissListener(dialog1 -> {
                                            ft.replace(R.id.frame, new OrdersFragment(), OrdersFragment.class.getSimpleName())
                                                    .commit();
                                            context.getSupportFragmentManager().popBackStack();
                                        });
                                    } else if (checkoutResponse.getCode() == 301) {
                                        binding.tvCashierName.setText("");
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                AlertUtil.showAlertWindow(context, getString(R.string.still_want_to_proceed), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        callCheckoutApi();
                                                    }
                                                });
                                            }
                                        });
                                    } else {
                                        AlertUtil.toastMsg(context, checkoutResponse.getMessage());

                                    }
                                } else {
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


    @Override
    public void onStop() {
        super.onStop();
//        getChildFragmentManager().popBackStack();
    }

    private void setNearestValues() {
        int nearestInt = (int) Math.ceil(totalPrice);
        binding.tv1.setText("" + nearestInt);

        int nearestTenth = (int) (Math.ceil((nearestInt + 1) / 10.0) * 10);
        if (nearestInt < nearestTenth - 5) {
            int nearestFive = nearestTenth - 5;
            binding.tv2.setText("" + nearestFive);

            binding.tv3.setText("" + nearestTenth);

            int nearestHundered = (int) (Math.ceil((nearestTenth + 5) / 100.0) * 100);
            if (nearestTenth < nearestHundered - 50) {
                int nearestFifty = nearestTenth - 50;
                binding.tv4.setText("" + nearestFifty);
            } else {
                binding.tv4.setText("" + nearestHundered);
            }


        } else {
            binding.tv2.setText("" + nearestTenth);

            int nearestToTenth = nearestTenth + 10;
            if (nearestTenth < nearestToTenth - 50) {
                int nearestFifty = nearestToTenth - 50;
                binding.tv3.setText("" + nearestFifty);
            } else {
                int nearestHundered = (int) (Math.ceil((nearestToTenth + 5) / 100.0) * 100);
                binding.tv3.setText("" + nearestHundered);
            }

            int nearestHundered = (int) (Math.ceil((nearestToTenth + 5) / 100.0) * 100);
            binding.tv4.setText("" + nearestHundered);
        }


    }

    private void selectRecievedMoney(View view) {
        binding.tv1.setSelected(false);
        binding.tv2.setSelected(false);
        binding.tv3.setSelected(false);
        binding.tv4.setSelected(false);

        view.setSelected(true);
        float change = (float) (Math.round((Integer.parseInt(((TextView) view).getText().toString()) - totalPrice) * 100.0) / 100.0);
        binding.tvChange.setText("SR " + Utilities.formatPrice(change));
        amtTaken = Double.parseDouble(((TextView) view).getText().toString());
        changeGiven = change;

    }


    private int roundUp(double value, double toNearest) {
        int rounded = (int) ((Math.ceil(value / toNearest)) * toNearest);
        return rounded == (int) (value) ? (rounded + (int) (toNearest)) : rounded;
    }

    void getRoundOffs(float num) {
        binding.tv1.setVisibility(View.GONE);
        binding.tv2.setVisibility(View.GONE);
        binding.tv3.setVisibility(View.GONE);
        binding.tv4.setVisibility(View.GONE);

        arrAmt.clear();
        int roundoff;
        if (num % Math.round(num) == 0) {
            roundoff = Math.round(num);
        } else {
            roundoff = (int) (num) + 1;
        }
        arrAmt.add(roundoff);
        if (!(roundoff % 10 == 0)) {
            int nextNearestFive = Math.round(num) + (5 - (Math.round(num) % 5));
            arrAmt.add(nextNearestFive);
            int nearestTen = ((roundoff / 10) + 1) * 10;
            arrAmt.add(nearestTen);
        }
        int nearestFifty = roundUp(roundoff, 50.0);
        arrAmt.add(nearestFifty);
        int nearestToHundred = roundUp(roundoff, 100.0);
        arrAmt.add(nearestToHundred);

        int nearestFiveHundred = roundUp(roundoff, 500.0);
        arrAmt.add(nearestFiveHundred);

        List<Integer> arr = new ArrayList<>();
        for (int i = 0; i < arrAmt.size(); i++) {
            if (!arr.contains(arrAmt.get(i)) && arr.size() < 4) {
                arr.add(arrAmt.get(i));
            }
        }

        for (int i = 0; i < arr.size(); i++) {
            if (i == 0) {
                binding.tv1.setVisibility(View.VISIBLE);
                binding.tv1.setText("" + arr.get(i));
            }

            if (i == 1) {
                binding.tv2.setVisibility(View.VISIBLE);
                binding.tv2.setText("" + arr.get(i));
            }

            if (i == 2) {
                binding.tv3.setVisibility(View.VISIBLE);
                binding.tv3.setText("" + arr.get(i));
            }

            if (i == 3) {
                binding.tv4.setVisibility(View.VISIBLE);
                binding.tv4.setText("" + arr.get(i));
            }
        }

        binding.imgCustomAmount.setVisibility(View.VISIBLE);
    }

    //=============Method to print receipt==============//
    private void startPrintingReceipt() {

        PermissionsUtil.askPermissions(context, PermissionsUtil.STORAGE, PermissionsUtil.LOCATION, new PermissionsUtil.PermissionListener() {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
            warningsMsg += getString(R.string.handlingmsg_warn_receipt_near_end);
        }

        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_1) {
            warningsMsg += getString(R.string.handlingmsg_warn_battery_near_end);
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
            msg += getString(R.string.handlingmsg_err_offline);
        }
        if (status.getConnection() == Printer.FALSE) {
            msg += getString(R.string.handlingmsg_err_no_response);
        }
        if (status.getCoverOpen() == Printer.TRUE) {
            msg += getString(R.string.handlingmsg_err_cover_open);
        }
        if (status.getPaper() == Printer.PAPER_EMPTY) {
            msg += getString(R.string.handlingmsg_err_receipt_end);
        }
        if (status.getPaperFeed() == Printer.TRUE || status.getPanelSwitch() == Printer.SWITCH_ON) {
            msg += getString(R.string.handlingmsg_err_paper_feed);
        }
        if (status.getErrorStatus() == Printer.MECHANICAL_ERR || status.getErrorStatus() == Printer.AUTOCUTTER_ERR) {
            msg += getString(R.string.handlingmsg_err_autocutter);
            msg += getString(R.string.handlingmsg_err_need_recover);
        }
        if (status.getErrorStatus() == Printer.UNRECOVER_ERR) {
            msg += getString(R.string.handlingmsg_err_unrecover);
        }
        if (status.getErrorStatus() == Printer.AUTORECOVER_ERR) {
            if (status.getAutoRecoverError() == Printer.HEAD_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_head);
            }
            if (status.getAutoRecoverError() == Printer.MOTOR_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_motor);
            }
            if (status.getAutoRecoverError() == Printer.BATTERY_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_battery);
            }
            if (status.getAutoRecoverError() == Printer.WRONG_PAPER) {
                msg += getString(R.string.handlingmsg_err_wrong_paper);
            }
        }
        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_0) {
            msg += getString(R.string.handlingmsg_err_battery_real_end);
        }

        return msg;
    }

    @Override
    public void onPtrReceive(Printer printer, int i, PrinterStatusInfo printerStatusInfo, String s) {


        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ShowMsg.showResult(i, makeErrorMessage(printerStatusInfo), context);
                        disconnectPrinter();

                        if (i == Epos2CallbackCode.CODE_SUCCESS) {
                            dialog.dismiss();
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
            context.runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    ShowMsg.showException(e, "endTransaction", context);
                }
            });
        }

        try {
            mPrinter.disconnect();
        } catch (final Exception e) {
            context.runOnUiThread(new Runnable() {
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
        Bitmap logoData = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo_login);
        boolean isArabic = Utilities.isArabic();
        int count = 0;
        int spaces = 0;
        String space = "";
        Bitmap logo = null;
        String name = "";
        String price = "";
        int textAlignment = isArabic ? Printer.ALIGN_RIGHT : Printer.ALIGN_LEFT;
        try {
            logo = BitmapFactory.decodeStream(new URL(checkoutResponse.getResult().getLogo()).openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {

            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            if (isArabic) {
                mPrinter.addTextFont(R.font.arabic_font);
            } else {
                mPrinter.addTextFont(R.font.cairo_semibold);
            }

            mPrinter.addImage(logo, 0, 0,
                    logo.getWidth(),
                    logo.getHeight(),
                    Printer.COLOR_1,
                    Printer.MODE_MONO,
                    Printer.HALFTONE_DITHER,
                    Printer.PARAM_DEFAULT,
                    Printer.COMPRESS_AUTO);
            mPrinter.addFeedLine(1);

            mPrinter.addTextSmooth(Printer.TRUE);

            mPrinter.addTextSize(2, 2);
            mPrinter.addText(checkoutResponse.getResult().getTitle());
            mPrinter.addTextSize(1, 1);
            mPrinter.addFeedLine(1);
            mPrinter.addText(checkoutResponse.getResult().getLocation());
            mPrinter.addTextSmooth(Printer.PARAM_DEFAULT);
            mPrinter.addFeedLine(1);


            mPrinter.addText(binding.tvDate.getText().toString());
            mPrinter.addFeedLine(1);
            mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.TRUE, Printer.TRUE, Printer.COLOR_4);
            mPrinter.addText(getString(R.string.order_id) + "#" + orderId);

            mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT);
            mPrinter.addFeedLine(2);

            textData.append("------------------------------------------------");
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());
            mPrinter.addFeedLine(1);


            if (selectedItems.size() > 0) {
                mPrinter.addTextAlign(textAlignment);
                mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.TRUE, Printer.TRUE, Printer.COLOR_4);
                mPrinter.addText(getString(R.string.items));
                mPrinter.addFeedLine(1);
                mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT);
                for (int i = 0; i < selectedItems.size(); i++) {
                    count++;

                    if (isArabic) {
                        name = selectedItems.get(i).getQtyAddedToCart() + "x " + selectedItems.get(i).getName() + " (" + count;
                    } else {
                        name = count + ") " + selectedItems.get(i).getName() + " x" + selectedItems.get(i).getQtyAddedToCart();
                    }
                    price = "SR " + selectedItems.get(i).getPriceForItem();
                    if (name.length() > 25) {
                        name = name.substring(0, 24) + "..";
                    }
                    spaces = 48 - name.length() - price.length();
                    space = "";
                    for (int j = 0; j < spaces; j++) {
                        space += " ";
                    }


                    mPrinter.addText(!isArabic ? (name + space + price) : (price + space + name));
                    mPrinter.addFeedLine(1);
                }

            }

            if (selectedMeals.size() > 0) {
                mPrinter.addFeedLine(1);
                mPrinter.addTextAlign(textAlignment);
                mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.TRUE, Printer.TRUE, Printer.COLOR_4);
                mPrinter.addText(getString(R.string.meals));
                mPrinter.addFeedLine(1);
                mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT);

                for (int i = 0; i < selectedMeals.size(); i++) {
                    count++;
                    if (isArabic) {
                        name = selectedMeals.get(i).getQtyAddedToCart() + "x " + selectedMeals.get(i).getTitle() + " (" + count;
                    } else {
                        name = count + ") " + selectedMeals.get(i).getTitle() + " x" + selectedMeals.get(i).getQtyAddedToCart();
                    }
                    price = "SR " + selectedMeals.get(i).getPriceForItem();
                    if (name.length() > 25) {
                        name = name.substring(0, 24) + "..";
                    }
                    spaces = 48 - name.length() - price.length();
                    space = "";
                    for (int j = 0; j < spaces; j++) {
                        space += " ";
                    }

                    mPrinter.addText(!isArabic ? (name + space + price) : (price + space + name));
                    mPrinter.addFeedLine(1);
                }

            }

            if (selectedDeals.size() > 0) {
                mPrinter.addFeedLine(1);
                mPrinter.addTextAlign(textAlignment);
                mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.TRUE, Printer.TRUE, Printer.COLOR_4);
                mPrinter.addText(getString(R.string.deals));
                mPrinter.addFeedLine(1);
                mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT);

                for (int i = 0; i < selectedDeals.size(); i++) {
                    count++;
                    if (isArabic) {
                        name = selectedDeals.get(i).getQtyAddedToCart() + "x " + selectedDeals.get(i).getTitle() + " (" + count;
                    } else {
                        name = count + ") " + selectedDeals.get(i).getTitle() + " x" + selectedDeals.get(i).getQtyAddedToCart();
                    }
                    price = "SR " + selectedDeals.get(i).getPriceForItem();
                    if (name.length() > 25) {
                        name = name.substring(0, 24) + "..";
                    }
                    spaces = 48 - name.length() - price.length();
                    space = "";
                    for (int j = 0; j < spaces; j++) {
                        space += " ";
                    }

                    mPrinter.addText(!isArabic ? (name + space + price) : (price + space + name));
                    mPrinter.addFeedLine(1);
                }

            }

            textData.append("------------------------------------------------");
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());
            mPrinter.addFeedLine(1);


            name = !isArabic ? getString(R.string.subtotal) : reverseString(getString(R.string.subtotal));
            price = binding.tvSubTotal.getText().toString();
            spaces = 48 - name.length() - price.length();
            space = "";
            for (int i = 0; i < spaces; i++) {
                space += " ";
            }

            mPrinter.addTextAlign(Printer.ALIGN_RIGHT);
            mPrinter.addText(!isArabic ? (name + space + price) : (price + space + name));
            mPrinter.addFeedLine(1);


            name = !isArabic ? getString(R.string.tax) : reverseString(getString(R.string.tax));
            price = binding.tvTax.getText().toString();
            spaces = 48 - name.length() - price.length();
            space = "";
            for (int i = 0; i < spaces; i++) {
                space += " ";
            }

            mPrinter.addText(!isArabic ? (name + space + price) : (price + space + name));
            mPrinter.addFeedLine(1);


            if (customAmount > 0) {

                name = !isArabic ? getString(R.string.custom_amount) : reverseString(getString(R.string.custom_amount));
                price = binding.tvCustomAmount.getText().toString();
                spaces = 48 - name.length() - price.length();
                space = "";
                for (int i = 0; i < spaces; i++) {
                    space += " ";
                }
                mPrinter.addText(!isArabic ? (name + space + price) : (price + space + name));
                mPrinter.addFeedLine(1);

            }

            mPrinter.addText("------------------------------------------------");
            mPrinter.addFeedLine(1);

            mPrinter.addTextSize(2, 2);
            mPrinter.addTextAlign(Printer.ALIGN_RIGHT);

            name = !isArabic ? getString(R.string.total) : reverseString(getString(R.string.total));
            price = binding.tvTotal.getText().toString();
            spaces = 24 - name.length() - price.length();
            space = "";
            for (int i = 0; i < spaces; i++) {
                space += " ";
            }

            mPrinter.addText(!isArabic ? (name + space + price) : (price + space + name));
            mPrinter.addFeedLine(1);


            mPrinter.addTextSize(1, 1);
            mPrinter.addTextAlign(Printer.ALIGN_RIGHT);
            mPrinter.addText("------------------------------------------------");
            mPrinter.addFeedLine(1);

            name = !isArabic ? getString(R.string.cash) : reverseString(getString(R.string.cash));
            price = "SR " + amtTaken;
            spaces = 48 - name.length() - price.length();
            space = "";
            for (int i = 0; i < spaces; i++) {
                space += " ";
            }

            mPrinter.addText(!isArabic ? (name + space + price) : (price + space + name));
            mPrinter.addFeedLine(1);

            if (changeGiven > 0) {
                name = !isArabic ? getString(R.string.change) : reverseString(getString(R.string.change));
                price = binding.tvChange.getText().toString();
                spaces = 48 - name.length() - price.length();
                space = "";
                for (int i = 0; i < spaces; i++) {
                    space += " ";
                }

                mPrinter.addText(!isArabic ? (name + space + price) : (price + space + name));
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

    private String reverseString(String str) {
        StringBuilder sb = new StringBuilder(str);
        return sb.reverse().toString();
    }

}

