package com.icashier.app.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.ActivityCompletePlansBinding;
import com.icashier.app.dialog.PaymentSelctionOnlineDialog;
import com.icashier.app.dialog.PaymentStcFinalDialog;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.ImagePickerUtil;
import com.icashier.app.helper.PermissionsUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.PlanListResponse;
import com.icashier.app.model.SigninResponse;
import com.icashier.app.stcmodels.authreq.CouponVerifyRes;
import com.icashier.app.stcmodels.authreq.MobilePaymentAuthorizeRequestMessage;
import com.icashier.app.stcmodels.authreq.PaymentEnquary;
import com.icashier.app.stcmodels.authreq.PaymentInquiryRequestMessage;
import com.paytabs.paytabs_sdk.payment.ui.activities.PayTabActivity;
import com.paytabs.paytabs_sdk.utils.PaymentParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CompletePlansActivity extends AppCompatActivity {

    ActivityCompletePlansBinding binding;
    CompletePlansActivity context;
    File file;
    RestClient.ApiRequest apiRequest;
    List<PlanListResponse.ResultBean> planList = new ArrayList<>();
    SigninResponse.ResultBean signinData;
    String planId = "1";
    String accountNo = "", transactionId = "";
    double amount;
    String refNo;
    /*Stc Pay inquiry through Runnable*/
    private CountDownTimer countDownTimer2;
    private MobilePaymentAuthorizeRequestMessage model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(context, R.layout.activity_complete_plans);

        getExtras();
        setOnClickListeners();
        getPlanListApi();

        //to rotate views for arabic
        Utilities.rotateViews(binding.flBack);


    }
    /*STC
     * **************************
     *
     *
     * **********/

    private void stcPayTabData(String num) {
        Log.e("STCMOB_NU", "stcPayTabData: " + num);
        AlertUtil.showProgressDialog(context, getString(R.string.payment_process_message));
        apiRequest = new RestClient.ApiRequest(CompletePlansActivity.this);
        HashMap<String, String> headersMap = new HashMap<>();
        headersMap.put("X-ClientCode", ServerConstants.STC_CLIENT_CODE);
        headersMap.put("X-Username",  ServerConstants.STC_USER_NAME);
        headersMap.put("X-Password",  ServerConstants.STC_USER_PASS);
        headersMap.put("Content-Type", "application/json");
        model = new MobilePaymentAuthorizeRequestMessage();
        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
        model.setBranchID(signinData.getUid());
        model.setTellerID(planId);
        model.setDeviceID(signinData.getEmail());
        model.setBillNumber(getIntent().getStringExtra("PNO"));
        model.setRefNum(planId + signinData.getUid() + timeStamp);
        model.setMobileNo(num);// 0539396141 test purpose
        Double newData = new Double(amount);
        int value = newData.intValue();
        model.setAmount(value);
        model.setMerchantNote(getString(R.string.purchase_subscription));
        model.setExpiryPeriodType(1);
        model.setExpiryPeriod(2);
        CouponVerifyRes verifyRes = new CouponVerifyRes();
        verifyRes.setMobilePaymentAuthorizeRequestMessage(model);
        String dummData = new Gson().toJson(verifyRes);

        apiRequest.setUrl(ServerConstants.STC_PAYAUTORISE)
                .setMethod(RestClient.ApiRequest.METHOD_POST)
                .setHeaders(headersMap)
                .setParamsRow(dummData)
                .setResponseListener(new RestClient.ResponseListener() {
                    @Override
                    public void onResponse(String tag, String response) {
                        //AlertUtil.hideProgressDialog();
                        if (Utilities.isValidJson(response)) {
                            Log.e("STC", response);
                            // AlertUtil.hideProgressDialog();
                            AlertUtil.toastMsgLong(CompletePlansActivity.this, getString(R.string.payment_process_message));
                            if (Utilities.isNetworkAvailable(context)) {
                                if (response != null) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONObject jsonObject2 = jsonObject.optJSONObject("MobilePaymentAuthorizeResponseMessage");
                                        if (jsonObject2 != null && jsonObject2.has("AuthorizationReference")) {
                                            //     AlertUtil.showProgressDialog(context, getString(R.string.payment_process_message));
                                            stcpayInquary(response);
                                        } else {
                                            AlertUtil.toastMsgLong(context, response);
                                            AlertUtil.hideProgressDialog();
                                        }

                                    } catch (JSONException e) {
                                        //e.printStackTrace();
                                        AlertUtil.hideProgressDialog();
                                    }
                                }
                            } else {
                                AlertUtil.toastMsgLong(context, getString(R.string.network_error));
                                AlertUtil.hideProgressDialog();
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
                .executeStc(headersMap);
    }

    private void stcpayInquary(String responses) {
        int noOfMinutes = Integer.parseInt("2") * 60 * 1000;//Convert minutes into milliseconds
        countDownTimer2 = new CountDownTimer(noOfMinutes, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // long count=millisUntilFinished/1000;
                long millis = millisUntilFinished;
                String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                Log.e("TIMER", "onTick: " + hms);
                // binding.tvTimer.setText(hms);
                if (hms.equals("00:00")) {
                    countDownTimer2.cancel();
                    AlertUtil.hideProgressDialog();
                } else if (hms.equals("01:59")) {
                    // if(!hms.equals("00:00")) {
                    startEnquary(1);
                    //}
                } else if (hms.equals("01:40")) {
                    // if(!hms.equals("00:00")) {
                    startEnquary(2);
                    //}
                } else if (hms.equals("01:20")) {
                    // if(!hms.equals("00:00")) {
                    startEnquary(3);
                    //}
                } else if (hms.equals("01:00")) {
                    // if(!hms.equals("00:00")) {
                    startEnquary(4);
                    //}
                } else if (hms.equals("00:40")) {
                    // if(!hms.equals("00:00")) {
                    startEnquary(5);
                    //}
                } else if (hms.equals("00:20")) {
                    // if(!hms.equals("00:00")) {
                    startEnquary(6);
                    //}
                } else if (hms.equals("00:10")) {
                    // if(!hms.equals("00:00")) {
                    startEnquary(7);
                    //}
                } else if (hms.equals("00:05")) {
                    // if(!hms.equals("00:00")) {
                    startEnquary(8);
                    //}
                }
            }

            @Override
            public void onFinish() {
                // AlertUtil.toastMsgLong(context,getString(R.string.payment_verifys_stc));
                AlertUtil.hideProgressDialog();
                countDownTimer2.cancel();
                onStop();
            }
        };
        countDownTimer2.start();
    }

    private void startEnquary(int flag) {
        //    String finalRes=" ";
        apiRequest = new RestClient.ApiRequest(CompletePlansActivity.this);
        HashMap<String, String> headersMap = new HashMap<>();
        headersMap.put("X-ClientCode", ServerConstants.STC_CLIENT_CODE);
        headersMap.put("X-Username",  ServerConstants.STC_USER_NAME);
        headersMap.put("X-Password",  ServerConstants.STC_USER_PASS);
        headersMap.put("Content-Type", "application/json");
        if (model != null) {
            PaymentEnquary enquary = new PaymentEnquary();
            PaymentInquiryRequestMessage requestMessage = new PaymentInquiryRequestMessage();
            requestMessage.setRefNum(model.getRefNum());
            enquary.setPaymentInquiryRequestMessage(requestMessage);
            String requestDat = new Gson().toJson(enquary);
                /*"{\n" +
                                "\"PaymentInquiryRequestMessage\": {\n" +
                                "\"RefNum:"+model.getRefNum()+"\n" +
                                "}\n" +
                                "}"*/
            apiRequest.setUrl(ServerConstants.STC_PAYINQUARY)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setHeaders(headersMap)
                    .setParamsRow(requestDat)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            //  AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                Log.e("STC_AUTH", response);
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    if (jObj.has("PaymentInquiryResponseMessage")) {
                                        JSONObject PaymentInquiryResponseMessageJsonObject = jObj.optJSONObject("PaymentInquiryResponseMessage");
                                        if (!PaymentInquiryResponseMessageJsonObject.isNull("TransactionList")) {
                                            JSONArray TransactionListArray = PaymentInquiryResponseMessageJsonObject.optJSONArray("TransactionList");
                                            if (TransactionListArray.length() > 0) {
                                                JSONObject finalJsonObject = TransactionListArray.optJSONObject(TransactionListArray.length() - 1);
                                                /*String */
                                                String refrenceNumber = finalJsonObject.optString("RefNum");
                                                String merchantId = finalJsonObject.optString("MerchantID");
                                                String branchId = finalJsonObject.optString("BranchID");
                                                String TellerId = finalJsonObject.optString("TellerID");
                                                String deviceId = finalJsonObject.optString("DeviceID");
                                                String stcPayRefNum = finalJsonObject.optString("STCPayRefNum");
                                                double amount = finalJsonObject.optDouble("Amount");
                                                double amountReceived = finalJsonObject.optDouble("AmountReversed");
                                                String paymentDate = finalJsonObject.optString("PaymentDate");
                                                int paymentStatus = finalJsonObject.optInt("PaymentStatus");
                                                String paymentStatusDesc = finalJsonObject.optString("PaymentStatusDesc");
                                                if (paymentStatus == 5) {
                                                    // AlertUtil.toastMsgLong(OtpActivity.this, "Expire Status");
                                                    countDownTimer2.onFinish();
                                                    AlertUtil.hideProgressDialog();
                                                    stcFinalResponse(response);
                                                    // countDownTimer2.cancel();
                                                } else if (paymentStatus == 4) {
                                                    countDownTimer2.onFinish();
                                                    AlertUtil.hideProgressDialog();
                                                    stcFinalResponse(response);
                                                    // AlertUtil.toastMsgLong(OtpActivity.this, "Cancel by user");
                                                } else if (paymentStatus == 2) {
                                                    countDownTimer2.onFinish();
                                                    AlertUtil.hideProgressDialog();
                                                    stcFinalResponse(response);
                                                    // AlertUtil.toastMsgLong(OtpActivity.this, "Paid by user");
                                                } else if (paymentStatus == 1) {
                                                    AlertUtil.toastMsgLong(CompletePlansActivity.this, "Pending");
                                                }

                                            } else {
                                                JSONObject finalJsonObject = TransactionListArray.optJSONObject(0);
                                                /*String */
                                                String refrenceNumber = finalJsonObject.optString("RefNum");
                                                String merchantId = finalJsonObject.optString("MerchantID");
                                                String branchId = finalJsonObject.optString("BranchID");
                                                String TellerId = finalJsonObject.optString("TellerID");
                                                String deviceId = finalJsonObject.optString("DeviceID");
                                                String stcPayRefNum = finalJsonObject.optString("STCPayRefNum");
                                                double amount = finalJsonObject.optDouble("Amount");
                                                double amountReceived = finalJsonObject.optDouble("AmountReversed");
                                                String paymentDate = finalJsonObject.optString("PaymentDate");
                                                int paymentStatus = finalJsonObject.optInt("PaymentStatus");
                                                String paymentStatusDesc = finalJsonObject.optString("PaymentStatusDesc");
                                                if (paymentStatus == 5) {
                                                    //  AlertUtil.toastMsgLong(OtpActivity.this, "Expire Status");
                                                    countDownTimer2.onFinish();
                                                    AlertUtil.hideProgressDialog();
                                                    stcFinalResponse(response);
                                                } else if (paymentStatus == 4) {
                                                    countDownTimer2.onFinish();
                                                    AlertUtil.hideProgressDialog();
                                                    stcFinalResponse(response);
                                                    //  AlertUtil.toastMsgLong(OtpActivity.this, "Cancel by user");
                                                } else if (paymentStatus == 2) {
                                                    countDownTimer2.onFinish();
                                                    AlertUtil.hideProgressDialog();
                                                    stcFinalResponse(response);
                                                    //  AlertUtil.toastMsgLong(OtpActivity.this, "Paid by user");
                                                } else if (paymentStatus == 1) {
                                                    // AlertUtil.toastMsgLong(CompletePlansActivity.this, "Pending");
                                                }
                                            }
                                        }
                                        if (flag == 8) {
                                            countDownTimer2.onFinish();
                                            AlertUtil.hideProgressDialog();
                                            stcFinalResponse(response);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    AlertUtil.hideProgressDialog();
                                }

                                //  AlertUtil.hideProgressDialog();
                                // stcpayInquary(response);
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
                    .executeStc(headersMap);
        } else {
            countDownTimer2.cancel();
            AlertUtil.hideProgressDialog();
            AlertUtil.toastMsg(CompletePlansActivity.this, context.getString(R.string.error_generic));
        }

        //return finalRes;
    }

    // api hit
    private void stcFinalResponse(String response) {
        //
        new PaymentStcFinalDialog(context, new PaymentStcFinalDialog.PayStcFinalCallBack() {
            @Override
            public void payFinal(String response) {
                stcTransaction(response);
            }
        }, response).show();

    }
    /*
     * ********************************************/


    private void getExtras() {
        signinData = (SigninResponse.ResultBean) getIntent().getSerializableExtra(AppConstant.SIGNIN_RESPONSE);
    }

    //==============To block back press=================//
    @Override
    public void onBackPressed() {
    }

    //================Method to set onClick listners==================//
    private void setOnClickListeners() {
        binding.btnUpload.setOnClickListener(V -> {

            openImagePicker();
        });

        binding.flOk.setOnClickListener(V -> {
            if (isInputValid()) {
                if (!planId.equals("1")) {
                    new PaymentSelctionOnlineDialog(CompletePlansActivity.this, new PaymentSelctionOnlineDialog.PaySelectionCallBack() {
                        @Override
                        public void payMentSelectionWith(String num) {
                            long nums = Long.valueOf(num);
                            if (nums == 1) {
                                countDownTimer2.cancel();
                                openPaymentActivity();
                            } else if (nums != 0 && nums != 1) {
                                if (Utilities.isNetworkAvailable(CompletePlansActivity.this)) {
                                    stcPayTabData(String.valueOf(nums));
                                } else {
                                    AlertUtil.toastMsgLong(CompletePlansActivity.this, getString(R.string.network_error));
                                }
                            }
                            //callUpdateTableApi(tableData.getName(),tableData.getId());
                        }
                    }, getIntent().getStringExtra("PNO")).show();
                    //
                } else {
                    callSavePlanApi(1, "");
                }
            }
        });

        binding.clPlans.setOnClickListener(V -> {
            showDropDown();
        });

        binding.flBack.setOnClickListener(V -> {
            finish();
        });

        binding.btnRemove.setOnClickListener(V -> {
            if (file != null) {
                file = null;
                binding.imgDocument.setImageDrawable(getResources().getDrawable(R.drawable.placeholder1));
            }
        });

        binding.clParentLayout.setOnClickListener(V -> {
            Utilities.hideSoftKeyboard(context);
        });

    }

    private void openPaymentActivity() {
        Intent in = new Intent(getApplicationContext(), PayTabActivity.class);
        //Live
        in.putExtra(PaymentParams.MERCHANT_EMAIL, "maxceo.imenu@gmail.com");
        in.putExtra(PaymentParams.SECRET_KEY,"U3oPn65bQFwewXz4CbcGHCtI4WbQCD3wHrm4ysfo8MDEKdJmpV26yvPCs0t5EwBlYYpxrFWzrzebMXybTAir674y7xDM3f72exzF");
        // Demo
        /* in.putExtra(PaymentParams.MERCHANT_EMAIL, "sumit@techugo.com"); //this a demo account for testing the sdk
        in.putExtra(PaymentParams.SECRET_KEY, "jHNnwY2CJxvhXvDmIJXuksDQ3P6DxTIQBwe7uhKdOfvML98tS6YdzB4CqbgY4ZOFv3MZkxd8MIVxS41KbZYKB2m2dD0hCPX7SK7c");//Add your Secret Key Here*/
        if (!SharedPrefManager.getInstance(context).getString(AppConstant.APP_LANGUAGE, "").equals("ar")) {
            in.putExtra(PaymentParams.LANGUAGE, PaymentParams.ARABIC);
        } else {
            in.putExtra(PaymentParams.LANGUAGE, PaymentParams.ENGLISH);
        }
        // in.putExtra(PaymentParams.LANGUAGE,PaymentParams.ENGLISH);
        in.putExtra(PaymentParams.TRANSACTION_TITLE, getString(R.string.purchase_subscription));
        in.putExtra(PaymentParams.AMOUNT, amount);
        in.putExtra(PaymentParams.CURRENCY_CODE, "SAR");
        in.putExtra(PaymentParams.CUSTOMER_PHONE_NUMBER, signinData.getMobile());
        in.putExtra(PaymentParams.CUSTOMER_EMAIL, signinData.getEmail());
        in.putExtra(PaymentParams.ORDER_ID, signinData.getUid());
        in.putExtra(PaymentParams.PRODUCT_NAME, planId);
        //Billing Address
        in.putExtra(PaymentParams.ADDRESS_BILLING, "Flat 1,Building 123, Road 2345");
        in.putExtra(PaymentParams.CITY_BILLING, "Manama");
        in.putExtra(PaymentParams.STATE_BILLING, "Manama");
        in.putExtra(PaymentParams.COUNTRY_BILLING, "SAU");
        in.putExtra(PaymentParams.POSTAL_CODE_BILLING, "11564"); //Put Country Phone code if Postal code not available '00973'
        //Shipping Address
        in.putExtra(PaymentParams.ADDRESS_SHIPPING, "Flat 1,Building 123, Road 2345");
        in.putExtra(PaymentParams.CITY_SHIPPING, "Manama");
        in.putExtra(PaymentParams.STATE_SHIPPING, "Manama");
        in.putExtra(PaymentParams.COUNTRY_SHIPPING, "SAU");
        in.putExtra(PaymentParams.POSTAL_CODE_SHIPPING, "11564");
        //Payment Page Style
        in.putExtra(PaymentParams.PAY_BUTTON_COLOR, "#2474bc");
        //Tokenization
        in.putExtra(PaymentParams.IS_TOKENIZATION, true);
        in.putExtra(PaymentParams.TOKEN, signinData.getUid());
        startActivityForResult(in, PaymentParams.PAYMENT_REQUEST_CODE);
    }

    //====================Mehtod to validate user input==================//
    private boolean isInputValid() {
        if (planId.equals("2")) {
            if (!binding.etAccountNo.getText().toString().trim().equals("")) {
                if (binding.etAccountNo.getText().toString().trim().length() == 16) {
                    accountNo = binding.etAccountNo.getText().toString().trim();
                    if (file != null) {
                        return true;
                    } else {
                        AlertUtil.toastMsg(context, getString(R.string.empty_document));

                    }
                } else {
                    AlertUtil.toastMsg(context, getString(R.string.inavlid_account));

                }
            } else {
                AlertUtil.toastMsg(context, getString(R.string.empty_account_no));
            }
        } else {
            return true;
        }
        return false;
    }

    //==================Method to show dropdown========================//
    private void showDropDown() {
        ListPopupWindow popupWindow = new ListPopupWindow(context);
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, new String[]{getString(R.string.free), getString(R.string.verified), getString(R.string.non_verified)});
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int index, long id) {

                setServices(index);
                if (index == 0) {
                    binding.tvPlanName.setText(getString(R.string.free));
                } else if (index == 1) {
                    binding.tvPlanName.setText(getString(R.string.verified));
                } else {
                    binding.tvPlanName.setText(getString(R.string.non_verified));
                }
                popupWindow.dismiss();
            }
        });

        popupWindow.setContentWidth(binding.clPlans.getWidth());
        popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setAnchorView(binding.clPlans);
        popupWindow.setAdapter(adapter);
        popupWindow.show();
    }

    //==================Mehtod to set services available in a plan============//
    private void setServices(int index) {

        //1=free 2=verified 3=non-verified
        if (index == 0) {
            binding.imgDelivery.setSelected(true);
            binding.imgDineIn.setSelected(true);
            binding.imgTakeAway.setSelected(true);
            binding.imgPickup.setSelected(true);
            planId = "1";
            binding.llDocument.setVisibility(View.GONE);
            binding.llAccountNo.setVisibility(View.GONE);
            binding.tvPrice.setText("SR " + 0);
            binding.tvInfo.setText(getString(R.string.one_qr_per_service));

        } else if (index == 1) {
            binding.imgDelivery.setSelected(true);
            binding.imgDineIn.setSelected(true);
            binding.imgTakeAway.setSelected(true);
            binding.imgPickup.setSelected(true);
            planId = "2";
            binding.llDocument.setVisibility(View.VISIBLE);
            binding.llAccountNo.setVisibility(View.VISIBLE);
            binding.tvPrice.setText("SR " + planList.get(index).getPrice() + "0");
            binding.tvInfo.setText(getString(R.string.you_can_generate_unlimited_qr_code));
            amount = planList.get(index).getPrice();


        } else if (index == 2) {
            binding.imgDelivery.setSelected(true);
            binding.imgDineIn.setSelected(true);
            binding.imgTakeAway.setSelected(true);
            binding.imgPickup.setSelected(true);
            planId = "3";
            binding.llDocument.setVisibility(View.GONE);
            binding.llAccountNo.setVisibility(View.GONE);
            binding.tvPrice.setText("SR " + planList.get(index).getPrice() + "0");
            binding.tvInfo.setText(getString(R.string.you_can_generate_unlimited_qr_code));

            amount = planList.get(index).getPrice();

        }
    }

    //============Method to pick image from camera or gallery========================//
    private void openImagePicker() {
        //to ask permissions at runtime
        PermissionsUtil.askPermissions(context, PermissionsUtil.CAMERA, PermissionsUtil.STORAGE, new PermissionsUtil.PermissionListener() {
            @Override
            public void onPermissionResult(boolean isGranted) {
                if (isGranted) {
                    ImagePickerUtil.selectImage(context, new ImagePickerUtil.ImagePickerListener() {
                        @Override
                        public void onImagePicked(File imageFile, String tag) {
                            file = imageFile;
                            binding.imgDocument.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
                        }
                    }, "user", false);
                }

            }
        });

    }

    //==========================Method to call get plans api=====================//
    private void getPlanListApi() {
        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("lang", Utilities.isArabic() ? "1" : "0");  //1 for arabic and 0 for english

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_PLANS)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                PlanListResponse planListResponse = new Gson().fromJson(response, PlanListResponse.class);
                                if (planListResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (planListResponse.getCode() == 200) {
                                        planList.addAll(planListResponse.getResult());
                                        setServices(1);
                                    } else {

                                        AlertUtil.toastMsg(context, planListResponse.getMessage());
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


    // sct tranaction api
    private void stcTransaction(String res) {
        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);
            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.STC_TRANSACTION)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    // .setHeader("userId",signinData.getUid())
                    .setParamsRow(res)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.has("code")) {
                                        if (jsonObject.optInt("code") == 404) {
                                            AlertUtil.toastMsg(context, jsonObject.optString("message"));
                                        } else if (jsonObject.optInt("code") == 200) {
                                            if (jsonObject.has("result")) {
                                                JSONObject jsonObject1 = jsonObject.optJSONObject("result");
                                                String stcrefNo = jsonObject1.optString("STCPayRefNum");
                                                String refNo = jsonObject1.optString("RefNum");
                                                if (stcrefNo != null && stcrefNo.trim().length() > 0) {
                                                    callSavePlanApi(2, stcrefNo);
                                                } else {
                                                    callSavePlanApi(2, refNo);
                                                }
                                            }
                                        } else {
                                            AlertUtil.toastMsg(context, jsonObject.optString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
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

    //==========================Method to call save plan api=====================//
    private void callSavePlanApi(int flag, String ref) {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);

            HashMap<String, String> params = new HashMap<>();
            params.put("planid", planId);
            if (flag == 2) {
                //params.put("stc_refno",ref);
                params.put("transaction_id", ref);
            } else {
                params.put("transaction_id", transactionId);
            }
            HashMap<String, File> fileHashMap = new HashMap<>();


            if (!planId.equals("3")) {
                params.put("account", accountNo);
                if (file != null) {
                    fileHashMap.put("image", file);
                }
            }

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.SAVE_PLANS)
                    .setMethod(RestClient.ApiRequest.METHOD_MULTIPART)
                    .setFileParams(fileHashMap)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                SigninResponse completePlanResponse = new Gson().fromJson(response, SigninResponse.class);
                                if (completePlanResponse != null) {
                                    if (completePlanResponse.getCode() == 200) {

                                        AlertUtil.toastMsg(context, completePlanResponse.getMessage());
                                        SharedPrefManager.getInstance(context).saveString(AppConstant.KEY_LOGIN_USER_ID, completePlanResponse.getResult().getUid());
                                        SharedPrefManager.getInstance(context).saveString(AppConstant.USER_INFO, new Gson().toJson(completePlanResponse.getResult()));
                                        Utilities.clearAllgoToActivity(context, HomeActivity.class);

                                    } else {

                                        AlertUtil.toastMsg(context, completePlanResponse.getMessage());
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        setLanguage();
        if (resultCode == RESULT_OK && requestCode == PaymentParams.PAYMENT_REQUEST_CODE) {
            Log.e("Tag", data.getStringExtra(PaymentParams.RESPONSE_CODE));
            Log.e("Tag", data.getStringExtra(PaymentParams.TRANSACTION_ID));
            // transactionId = null;
            transactionId = data.getStringExtra(PaymentParams.TRANSACTION_ID);
           /* if(transactionId!=null && !transactionId.isEmpty()){
                callSavePlanApi();
            }*/
            // Toast.makeText(context, data.getStringExtra(PaymentParams.RESPONSE_CODE), Toast.LENGTH_LONG).show();
            // Toast.makeText(context, data.getStringExtra(PaymentParams.TRANSACTION_ID), Toast.LENGTH_LONG).show();

            if (data.hasExtra(PaymentParams.TOKEN) && !data.getStringExtra(PaymentParams.TOKEN).isEmpty()) {
                Log.e("Tag", data.getStringExtra(PaymentParams.TOKEN));
                Log.e("Tag", data.getStringExtra(PaymentParams.CUSTOMER_EMAIL));
                Log.e("Tag", data.getStringExtra(PaymentParams.CUSTOMER_PASSWORD));
                // Toast.makeText(context, data.getStringExtra(PaymentParams.TOKEN), Toast.LENGTH_LONG).show();
                // Toast.makeText(context, data.getStringExtra(PaymentParams.CUSTOMER_EMAIL), Toast.LENGTH_LONG).show();
                // Toast.makeText(context, data.getStringExtra(PaymentParams.CUSTOMER_PASSWORD), Toast.LENGTH_LONG).show();

                callSavePlanApi(1, "");
            }
        } else {
            ImagePickerUtil.onActivityResult(requestCode, resultCode, data);
        }

    }

    //==========================Mehtod to set app language============================//
    private void setLanguage() {

        String appLang = SharedPrefManager.getInstance(getBaseContext()).getString(AppConstant.APP_LANGUAGE, "en");

        Locale locale = new Locale("en");
        if (appLang.equals("en")) {
            locale = new Locale("en");
        } else if (appLang.equals("ar")) {
            locale = new Locale("ar", "MA");
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

}
