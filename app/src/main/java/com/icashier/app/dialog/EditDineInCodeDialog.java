package com.icashier.app.dialog;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.DialogEditDineinCodeBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.listener.DialogDismissListener;
import com.icashier.app.model.AllCodesListResponse;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.TableListResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditDineInCodeDialog extends Dialog{

    HomeActivity context;
    private LayoutInflater inflater;
    DialogEditDineinCodeBinding binding;
    AllCodesListResponse.ResultBean codeData;
    RestClient.ApiRequest apiRequest;
    List<String> tableNameList=new ArrayList<>();
    List<TableListResponse.ResultBean> tableList=new ArrayList<>();
    String paymentMode="",tableId="";
    DialogDismissListener listener;
    boolean isCreditCard,isCash,isPaypal;

    public EditDineInCodeDialog(HomeActivity context,AllCodesListResponse.ResultBean data,DialogDismissListener listener) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.codeData=data;
        this.listener=listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_edit_dinein_code, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);

        setOnClickListeners();
        setData();

    }

    //======================Method to set onClick listeners================//
    private void setOnClickListeners() {

        binding.clParentLayout.setOnClickListener(V -> {
            context.closeDrawer();
            Utilities.hideSoftKeyboard(context);
        });

        binding.llCash.setOnClickListener(V -> {
            setPaymentSelector(binding.imgCash);
        });

        binding.llCreditCard.setOnClickListener(V -> {
            setPaymentSelector(binding.imgCreditCard);
        });

        binding.llPaypal.setOnClickListener(V -> {
            setPaymentSelector(binding.imgPaypal);
        });


        binding.tvUpdate.setOnClickListener(V->{
            if(isValidInput()){
                callUpdateCodeApi();
            }
        });

        binding.tvCancel.setOnClickListener(V->{
            dismiss();
        });
    }

    private boolean isValidInput()
    {
        if(isPaypal||isCash||isCreditCard){
            if(!binding.etTable.getText().toString().trim().equals("")){
                return true;
            }else{
                AlertUtil.toastMsg(context,context.getString(R.string.empty_table));
            }
        }else{
            AlertUtil.toastMsg(context,context.getString(R.string.empty_payment_mode));
        }
        return false;
    }
    //=================MEhtod to select payment method=============//
    private void setPaymentSelector(ImageView img)
    {
        if(img==binding.imgPaypal)
        {
            if(!isPaypal){
                binding.imgPaypal.setSelected(true);
                isPaypal=true;
            }else{
                binding.imgPaypal.setSelected(false);
                isPaypal=false;
            }

        }
        else if(img==binding.imgCreditCard)
        {
            if(!isCreditCard){
                binding.imgCreditCard.setSelected(true);
                isCreditCard=true;
            }else{
                binding.imgCreditCard.setSelected(false);
                isCreditCard=false;
            }
        }
        else if(img==binding.imgCash)
        {
            if(!isCash){
                binding.imgCash.setSelected(true);
                isCash=true;
            }else{
                binding.imgCash.setSelected(false);
                isCash=false;
            }
        }
    }


    //==============Mehtod to set data===========//
    private void setData() {

        binding.etTable.setText(codeData.getTable_id());

        if(codeData.getPayment().contains(AppConstant.CASH)){
            setPaymentSelector(binding.imgCash);

        } if(codeData.getPayment().contains(AppConstant.CREDIT_CARD)){
            setPaymentSelector(binding.imgCreditCard);

        }  if(codeData.getPayment().contains(AppConstant.PAYPAL)){
            setPaymentSelector(binding.imgPaypal);

        }
        binding.imgCash.setSelected(true);
        isCash=true;
    }



    //============Method to call update qr api===========//
    private void callUpdateCodeApi() {

        if(Utilities.isNetworkAvailable(context)) {
            //Utilities.hideKeyboardOnPopup(context);
            binding.progressBar.setVisibility(View.VISIBLE);
            paymentMode="";
            if(isCreditCard){
                if(paymentMode.equals("")){
                    paymentMode=AppConstant.CREDIT_CARD;
                }else{
                    paymentMode=paymentMode+","+AppConstant.CREDIT_CARD;
                }
            }
            if(isCash){
                if(paymentMode.equals("")){
                    paymentMode=AppConstant.CASH;
                }else{
                    paymentMode=paymentMode+","+AppConstant.CASH;
                }
            }

            if(isPaypal){
                if(paymentMode.equals("")){
                    paymentMode=AppConstant.PAYPAL;
                }else{
                    paymentMode=paymentMode+","+AppConstant.PAYPAL;
                }
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("id",""+codeData.getId());
            params.put("type","dine_in");
            params.put("table_id",binding.etTable.getText().toString().trim());
            params.put("payment",paymentMode);

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.EDIT_QR)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {
                                GenericResponse genericResponse= new Gson().fromJson(response, GenericResponse.class);
                                if (genericResponse != null) {
                                    if (genericResponse.getCode()==200 ) {

                                        binding.imgPaypal.setSelected(false);
                                        binding.imgCash.setSelected(false);
                                        binding.imgCreditCard.setSelected(false);
                                        paymentMode="";
                                        tableId="";
                                        AlertUtil.toastMsg(context,genericResponse.getMessage());
                                        dismiss();
                                        listener.onDismiss();
                                    } else if(genericResponse.getCode()==301)
                                    {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callUpdateCodeApi();
                                            }
                                        });
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
                            binding.progressBar.setVisibility(View.GONE);
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        }else
        {
            AlertUtil.toastMsg(context,context.getString(R.string.network_error));
        }

    }
}
