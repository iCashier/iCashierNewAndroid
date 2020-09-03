package com.icashier.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;

import com.icashier.app.R;
import com.icashier.app.adapter.TableListAdapter;
import com.icashier.app.databinding.DialogPaymentSelectionBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.model.TableListResponse;

import java.util.ArrayList;
import java.util.List;

public class PaymentSelctionOnlineDialog extends Dialog {

    Context context;
    RestClient.ApiRequest apiRequest;
    List<TableListResponse.ResultBean> tableList = new ArrayList<>();
    TableListAdapter adapter;
    DialogPaymentSelectionBinding binding;
    int mobNo = 0;
    PaySelectionCallBack listener;
    private LayoutInflater inflater;
    private String oldNo=" ";


    public PaymentSelctionOnlineDialog(Context context, PaySelectionCallBack listener,String no) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.oldNo=no;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_payment_selection, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(false);
        setOnClickListener();
        binding.edtPaymentTyeMobile.setText(oldNo);
    }

    //====================Mehtod to set onClick Listener=================//
    private void setOnClickListener() {

        binding.rdGroupSelecction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.rdPaytab) {
                    binding.edtPaymentTyeMobile.setVisibility(View.GONE);
                } else if (checkedId == R.id.rdStc) {
                    binding.edtPaymentTyeMobile.setVisibility(View.VISIBLE);

                }
            }

        });

        binding.btnPaymentTyeOk.setOnClickListener(view -> {
            if (binding.edtPaymentTyeMobile.getVisibility() == View.VISIBLE) {
                if (binding.edtPaymentTyeMobile.getText().toString().trim().length() > 5 && binding.edtPaymentTyeMobile
                        .getText().toString().trim().length() < 13) {
                   // int n=Integer.valueOf(binding.edtPaymentTyeMobile.getText().toString());
                    listener.payMentSelectionWith(binding.edtPaymentTyeMobile.getText().toString());
                 dismiss();
                } else {
                    binding.edtPaymentTyeMobile.setFocusable(true);
                    AlertUtil.showToastLong(context, context.getString(R.string.valid_mobile));
                }
            }else{
                listener.payMentSelectionWith("1");
                dismiss();
            }
        });

        binding.btnPaymentTyeCancel.setOnClickListener(view -> {
            dismiss();
        });
    }

    public interface PaySelectionCallBack {
        void payMentSelectionWith(String num);
    }


}
