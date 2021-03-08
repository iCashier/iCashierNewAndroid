package com.icashier.app.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.ActivityOtpBinding;
import com.icashier.app.dialog.PaymentSelctionOnlineDialog;
import com.icashier.app.dialog.PaymentStcFinalDialog;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.SigninResponse;
import com.icashier.app.stcmodels.authreq.CouponVerifyRes;
import com.icashier.app.stcmodels.authreq.MobilePaymentAuthorizeRequestMessage;
import com.icashier.app.stcmodels.authreq.PaymentEnquary;
import com.icashier.app.stcmodels.authreq.PaymentInquiryRequestMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    ActivityOtpBinding binding;
    OtpActivity context;
    CountDownTimer countDownTimer, countDownTimer2;
    RestClient.ApiRequest apiRequest;
    String Otp = "", phoneNo = "", countryCode = "";
    private int previousLength;
    private boolean backSpace;
    //private Handler handler;
    /*Stc Pay inquiry through Runnable*/
    private MobilePaymentAuthorizeRequestMessage model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(context, R.layout.activity_otp);
        setTimer();

        moveCursor();
        setOnClickListener();
        setData();
    }

    //===============Method to set data=================//
    private void setData() {
        phoneNo = getIntent().getStringExtra(AppConstant.PHONE_NO);
        countryCode = getIntent().getStringExtra(AppConstant.COUNTRY_CODE);
        binding.tvNumber.setText(countryCode + " " + phoneNo);

    }

    //to block back press
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

    //=====================Method to set on click listener=======================//
    private void setOnClickListener() {
        binding.btnVerify.setOnClickListener(V -> {
            if (isValidOTP()) {
                callValidOTPApi();
         }
           /* new PaymentSelctionOnlineDialog(context, new PaymentSelctionOnlineDialog.PaySelectionCallBack() {
                @Override
                public void payMentSelectionWith(String num) {
                    long nums = Long.valueOf(num);
                    if (nums == 1) {
                        countDownTimer2.cancel();
                        AlertUtil.hideProgressDialog();
                    } else if (nums != 0 && nums != 1) {
                        if (Utilities.isNetworkAvailable(OtpActivity.this)) {
                            stcPayTabData(String.valueOf(nums));
                        } else {
                            AlertUtil.toastMsgLong(OtpActivity.this, getString(R.string.network_error));
                        }

                    }
                    //callUpdateTableApi(tableData.getName(),tableData.getId());
                }
            }).show();*/
        });

        binding.tvResend.setOnClickListener(V -> {
            binding.etOtp1.setEnabled(true);
            binding.etOtp2.setEnabled(true);
            binding.etOtp3.setEnabled(true);
            binding.etOtp4.setEnabled(true);
            binding.btnVerify.setClickable(true);
            callResendOTPApi();

        });

        binding.rlParentLayout.setOnClickListener(V -> {
            Utilities.hideSoftKeyboard(context);
        });

        binding.flBack.setOnClickListener(V -> {
            finish();
        });
    }

    private void stcPayTabData(String num) {
        Log.e("STCMOB_NU", "stcPayTabData: " + num);
        //AlertUtil.showProgressDialog(context);
        apiRequest = new RestClient.ApiRequest(OtpActivity.this);
        HashMap<String, String> headersMap = new HashMap<>();
        headersMap.put("X-ClientCode", ServerConstants.STC_CLIENT_CODE);
        headersMap.put("X-Username",  ServerConstants.STC_USER_NAME);
        headersMap.put("X-Password",  ServerConstants.STC_USER_PASS);
        headersMap.put("Content-Type", "application/json");
        /*String dummyData = "{\n" +
                "\n" +
                "  \"MobilePaymentAuthorizeRequestMessage\": {\n" +
                "\n" +
                "    \"BranchID\": \"string\",\n" +
                "\n" +
                "    \"TellerID\": \"string\",\n" +
                "\n" +
                "    \"DeviceID\": \"string\",\n" +
                "\n" +
                "    \"RefNum\": \"techugo123\",\n" +
                "\n" +
                "    \"BillNumber\": \"string\",\n" +
                "\n" +
                "    \"MobileNo\": \"0539396141\",\n" +
                "\n" +
                "    \"Amount\": 2,\n" +
                "\n" +
                "    \"MerchantNote\": \"string\",\n" +
                "\n" +
                "    \"ExpiryPeriodType\": 1,\n" +
                "\n" +
                "    \"ExpiryPeriod\": 2" +
                "\n" +
                "  }\n" +
                "\n" +
                "}";*/

        model = new MobilePaymentAuthorizeRequestMessage();
        model.setBranchID("1");
        model.setTellerID("1");
        model.setDeviceID("string");
        model.setBillNumber(phoneNo);
        model.setRefNum("techugo123");
        model.setMobileNo("0539396141");
        model.setAmount(2);
        model.setMerchantNote("Test purpose");
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
                            AlertUtil.toastMsgLong(context, getString(R.string.payment_process_message));
                            if (Utilities.isNetworkAvailable(context)) {
                                AlertUtil.showProgressDialog(context, getString(R.string.payment_process_message));
                                stcpayInquary(response);
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
        apiRequest = new RestClient.ApiRequest(OtpActivity.this);
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
                                                    // countDownTimer2.cancel();
                                                } else if (paymentStatus == 4) {
                                                    countDownTimer2.onFinish();
                                                   // AlertUtil.toastMsgLong(OtpActivity.this, "Cancel by user");
                                                } else if (paymentStatus == 2) {
                                                    countDownTimer2.onFinish();
                                                   // AlertUtil.toastMsgLong(OtpActivity.this, "Paid by user");
                                                } else if (paymentStatus == 1) {
                                                    AlertUtil.toastMsgLong(OtpActivity.this, "Pending");
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
                                                } else if (paymentStatus == 4) {
                                                    countDownTimer2.onFinish();
                                                  //  AlertUtil.toastMsgLong(OtpActivity.this, "Cancel by user");
                                                } else if (paymentStatus == 2) {
                                                    countDownTimer2.onFinish();
                                                  //  AlertUtil.toastMsgLong(OtpActivity.this, "Paid by user");
                                                } else if (paymentStatus == 1) {
                                                    AlertUtil.toastMsgLong(OtpActivity.this, "Pending");
                                                }
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    AlertUtil.hideProgressDialog();
                                }
                                   if (flag==8){
                                       //
                                       new PaymentStcFinalDialog(context, new PaymentStcFinalDialog.PayStcFinalCallBack() {
                                           @Override
                                           public void payFinal(String response) {
                                               AlertUtil.toastMsg(context,response.toString());
                                           }
                                       },response).show();

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
            AlertUtil.toastMsg(OtpActivity.this, context.getString(R.string.error_generic));
        }

        //return finalRes;
    }


    //===============Method to set timer===================//
    private void setTimer() {
        binding.tvResend.setClickable(false);
        int noOfMinutes = Integer.parseInt("2") * 60 * 1000;//Convert minutes into milliseconds
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // long count=millisUntilFinished/1000;
                long millis = millisUntilFinished;

                String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                binding.tvTimer.setText(hms);

                /*count=count-1;
                if(count<10) {
                    binding.tvTimer.setText("00:0" + count);
                }else
                {

                    binding.tvTimer.setText("00:" + count);
                }
                if(count==0){
                    AlertUtil.toastMsgLong(context,getString(R.string.resend_otp_msg));
                }*/
            }

            @Override
            public void onFinish() {
                binding.tvTimer.setText("00:00");
                binding.tvResend.setClickable(true);
                binding.etOtp1.setEnabled(false);
                binding.etOtp2.setEnabled(false);
                binding.etOtp3.setEnabled(false);
                binding.etOtp4.setEnabled(false);
                binding.btnVerify.setClickable(false);
                AlertUtil.toastMsgLong(context, getString(R.string.resend_otp_msg));
                onStop();
            }
        };
        countDownTimer.start();
    }


    //==========To move to next edittext==================//
    private void moveCursor() {
        binding.etOtp1.requestFocus();
        binding.etOtp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                previousLength = charSequence.length();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                binding.etOtp2.requestFocus();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                backSpace = previousLength > editable.length();

                if (backSpace) {
                    binding.etOtp1.requestFocus();

                }
            }
        });

        binding.etOtp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                previousLength = charSequence.length();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                binding.etOtp3.requestFocus();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                backSpace = previousLength > editable.length();

                if (backSpace) {
                    binding.etOtp1.requestFocus();
                    binding.etOtp1.setSelection(binding.etOtp1.getText().length());


                }
            }
        });

        binding.etOtp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                previousLength = charSequence.length();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                binding.etOtp4.requestFocus();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                backSpace = previousLength > editable.length();

                if (backSpace) {
                    binding.etOtp2.requestFocus();
                    binding.etOtp2.setSelection(binding.etOtp2.getText().length());


                }
            }
        });

        binding.etOtp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                previousLength = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    Utilities.hideSoftKeyboard(context);
                    if (isValidOTP()) {
                        callValidOTPApi();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                backSpace = previousLength > editable.length();

                if (backSpace) {
                    binding.etOtp3.requestFocus();
                    binding.etOtp3.setSelection(binding.etOtp3.getText().length());

                }
            }
        });
    }

    //==================Mehtod to validate OTP================//
    private boolean isValidOTP() {
        if (!binding.etOtp1.getText().toString().equals("") || !binding.etOtp1.getText().toString().equals("") ||
                !binding.etOtp1.getText().toString().equals("") || !binding.etOtp1.getText().toString().equals("")) {
            Otp = binding.etOtp1.getText().toString() + binding.etOtp2.getText().toString() + binding.etOtp3.getText().toString()
                    + binding.etOtp4.getText().toString();
            return true;
        } else {
            AlertUtil.toastMsg(context, getString(R.string.valid_otp));
        }
        return false;
    }

    //==========================Method to call validate OTP api=====================//
    private void callValidOTPApi() {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);

            HashMap<String, String> params = new HashMap<>();
            params.put("otp", Otp);
            params.put("lang", Utilities.isArabic() ? "1" : "0");  //1 for arabic and 0 for english


            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.VERIFY_OTP)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                SigninResponse otpResponse = new Gson().fromJson(response, SigninResponse.class);
                                if (otpResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (otpResponse.getCode() == 201) {
                                        Log.e("TAG", otpResponse.getResult().toString());
                                        AlertUtil.toastMsg(context, otpResponse.getMessage());
                                        countDownTimer.cancel();
                                        /*Intent intent = new Intent(context, CompletePlansActivity.class);
                                        intent.putExtra(AppConstant.SIGNIN_RESPONSE, otpResponse.getResult());
                                        intent.putExtra("PNO", phoneNo);
                                        startActivity(intent);*/
                                        SharedPrefManager.getInstance(context).saveString(AppConstant.KEY_LOGIN_USER_ID, otpResponse.getResult().getUid());
                                        SharedPrefManager.getInstance(context).saveString(AppConstant.USER_INFO, new Gson().toJson(otpResponse.getResult()));
                                        Intent intent = new Intent(context, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        intent.putExtra(AppConstant.SIGNIN_RESPONSE, otpResponse.getResult());
//                                        intent.putExtra("PNO", phoneNo);
                                        startActivity(intent);
                                        finish();
                                    } else {

                                        AlertUtil.toastMsg(context, otpResponse.getMessage());
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

    //==========================Method to call resend OTP api=====================//
    private void callResendOTPApi() {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);

            HashMap<String, String> params = new HashMap<>();
            params.put("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""));
            params.put("lang", Utilities.isArabic() ? "1" : "0");//1 for arabic and 0 for english
            params.put("mobile", phoneNo);
            params.put("country_code", countryCode);
            apiRequest = new RestClient.ApiRequest(context);
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.RESEND_OTP)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setParams(params)
                    .setResponseListener((tag, response) -> {
                        AlertUtil.hideProgressDialog();
                        if (Utilities.isValidJson(response)) {
                            GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                            if (genericResponse != null) {
                                AlertUtil.hideProgressDialog();
                                if (genericResponse.getCode() == 201) {
                                    AlertUtil.toastMsg(context, genericResponse.getMessage());
                                    countDownTimer.cancel();
                                    setTimer();
                                } else {
                                    AlertUtil.toastMsg(context, genericResponse.getMessage());
                                }
                            } else {
                                AlertUtil.hideProgressDialog();
                                AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
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
    protected void onStop() {
        super.onStop();
        countDownTimer.cancel();
    }
}
