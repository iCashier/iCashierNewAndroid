package com.icashier.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.icashier.app.R;
import com.icashier.app.adapter.TableListAdapter;
import com.icashier.app.databinding.DialogStcFinalPageBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.TableListResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaymentStcFinalDialog extends Dialog {

    Context context;
    RestClient.ApiRequest apiRequest;
    List<TableListResponse.ResultBean> tableList = new ArrayList<>();
    TableListAdapter adapter;
    DialogStcFinalPageBinding binding;
    int mobNo = 0;
    PayStcFinalCallBack listener;
    String response;
    private LayoutInflater inflater;


    public PaymentStcFinalDialog(Context context, PayStcFinalCallBack listener, String responses) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.response = responses;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_stc_final_page, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(false);
        setData();
        setOnClickListener();
    }

    private void setData() {
        if (response != null) {
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
                                    binding.dimgTick.setVisibility(View.GONE);
                                    binding.dtxtPaySuccess.setText(R.string.payment_unsecess);
                                    binding.dtxtBillNo.setText(context.getString(R.string.bill_nos) + ": " + mobNo);
                                    binding.dtxtRefNo.setText(context.getString(R.string.ref_nos) + ": " + refrenceNumber);
                                    binding.dtxtMsg.setText(context.getString(R.string.merchant_is) + " " + branchId);
                                    binding.stxtAmount.setText(String.valueOf(amount));  //stxt_amount
                                } else if (paymentStatus == 4) {
                                    binding.dimgTick.setVisibility(View.GONE);
                                    binding.stxtAmount.setText(String.valueOf(amount));
                                    binding.dtxtPaySuccess.setText(R.string.payment_unsecess);
                                    binding.dtxtBillNo.setText(context.getString(R.string.bill_nos) + ": " + mobNo);
                                    binding.dtxtRefNo.setText(context.getString(R.string.ref_nos) + ": " + refrenceNumber);
                                    binding.dtxtMsg.setText(context.getString(R.string.merchant_is) + " " + branchId);
                                    //AlertUtil.toastMsgLong(OtpActivity.this, "Cancel by user");
                                } else if (paymentStatus == 2) {
                                    binding.dimgTick.setVisibility(View.VISIBLE);
                                    binding.stxtAmount.setText(String.valueOf(amount));
                                    binding.dtxtPaySuccess.setText(R.string.payment_received);
                                    binding.dtxtBillNo.setText(context.getString(R.string.bill_nos) + ": " + mobNo);
                                    binding.dtxtRefNo.setText(context.getString(R.string.ref_nos) + ": " + refrenceNumber);
                                    binding.dtxtMsg.setText(context.getString(R.string.merchant_is) + " " + branchId);
                                    binding.dllImgIndicator.setBackground(context.getResources().getDrawable(R.drawable.bg_circle_green));
                                    //AlertUtil.toastMsgLong(OtpActivity.this, "Paid by user");
                                } else if (paymentStatus == 1) {
                                    binding.dimgTick.setVisibility(View.GONE);
                                    binding.dtxtBillNo.setText(context.getString(R.string.bill_nos) + ": " + mobNo);
                                    binding.dtxtRefNo.setText(context.getString(R.string.ref_nos) + ": " + refrenceNumber);
                                    binding.dtxtMsg.setText(context.getString(R.string.merchant_is) + " " + branchId);
                                    binding.stxtAmount.setText(String.valueOf(amount));
                                    binding.dtxtPaySuccess.setText(R.string.payment_unsecess);
                                    // AlertUtil.toastMsgLong(OtpActivity.this, "Pending");
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
                                    // AlertUtil.toastMsgLong(OtpActivity.this, "Expire Status");
                                    binding.dimgTick.setVisibility(View.GONE);
                                    binding.dtxtPaySuccess.setText(R.string.payment_unsecess);
                                    binding.dtxtBillNo.setText(context.getString(R.string.bill_nos) + ": " + mobNo);
                                    binding.dtxtRefNo.setText(context.getString(R.string.ref_nos) + ": " + stcPayRefNum);
                                    binding.dtxtMsg.setText(context.getString(R.string.merchant_is) + " " + branchId);
                                    binding.stxtAmount.setText(String.valueOf(amount));  //stxt_amount
                                } else if (paymentStatus == 4) {
                                    binding.dimgTick.setVisibility(View.GONE);
                                    binding.stxtAmount.setText(String.valueOf(amount));
                                    binding.dtxtPaySuccess.setText(R.string.payment_unsecess);
                                    binding.dtxtBillNo.setText(context.getString(R.string.bill_nos) + ": " + mobNo);
                                    binding.dtxtRefNo.setText(context.getString(R.string.ref_nos) + ": " + stcPayRefNum);
                                    binding.dtxtMsg.setText(context.getString(R.string.merchant_is) + " " + branchId);
                                    //AlertUtil.toastMsgLong(OtpActivity.this, "Cancel by user");
                                } else if (paymentStatus == 2) {
                                    binding.dimgTick.setVisibility(View.VISIBLE);
                                    binding.stxtAmount.setText(String.valueOf(amount));
                                    binding.dtxtPaySuccess.setText(R.string.payment_received);
                                    binding.dtxtBillNo.setText(context.getString(R.string.bill_nos) + ": " + mobNo);
                                    binding.dtxtRefNo.setText(context.getString(R.string.ref_nos) + ": " + stcPayRefNum);
                                    binding.dtxtMsg.setText(context.getString(R.string.merchant_is) + " " + branchId);
                                    binding.dllImgIndicator.setBackground(context.getResources().getDrawable(R.drawable.bg_circle_green));
                                    //AlertUtil.toastMsgLong(OtpActivity.this, "Paid by user");
                                } else if (paymentStatus == 1) {
                                    binding.dimgTick.setVisibility(View.GONE);
                                    binding.dtxtBillNo.setText(context.getString(R.string.bill_nos) + ": " + mobNo);
                                    binding.dtxtRefNo.setText(context.getString(R.string.ref_nos) + ": " + stcPayRefNum);
                                    binding.dtxtMsg.setText(context.getString(R.string.merchant_is) + " " + branchId);
                                    binding.stxtAmount.setText(String.valueOf(amount));
                                    binding.dtxtPaySuccess.setText(R.string.payment_unsecess);
                                    // AlertUtil.toastMsgLong(OtpActivity.this, "Pending");
                                }


                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    AlertUtil.hideProgressDialog();
                }

            }
        }
    }


    //====================Mehtod to set onClick Listener=================//
    private void setOnClickListener() {
        binding.dbtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.payFinal(response);
                dismiss();
            }
        });

    }

    public interface PayStcFinalCallBack {
        void payFinal(String response);
    }


}
