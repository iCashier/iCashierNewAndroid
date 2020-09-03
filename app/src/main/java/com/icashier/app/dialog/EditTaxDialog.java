package com.icashier.app.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.DialogEditCouponBinding;
import com.icashier.app.databinding.DialogEditTaxBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.CreateCouponResponse;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.GetCouponsResponse;
import com.icashier.app.model.GetTaxListResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class EditTaxDialog extends Dialog {

    Activity context;
    private LayoutInflater inflater;
    DialogEditTaxBinding binding;
    GetTaxListResponse.ResultBean tax;
    RestClient.ApiRequest apiRequest;
    String discountType = "";
    TaxUpdateListener taxUpdateListener;

    public EditTaxDialog(Activity context, GetTaxListResponse.ResultBean tax, TaxUpdateListener taxUpdateListener) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.tax = tax;
        this.taxUpdateListener = taxUpdateListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_edit_tax, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);

        binding.etTitle.setText(tax.getTitle());
        binding.etValue.setText(tax.getValue());
        if (tax.getType().equals("%")) {
            binding.tvPercent.setSelected(true);
            binding.tvDollar.setSelected(false);
            discountType = "%";
        } else if (tax.getType().equals("$")) {
            binding.tvDollar.setSelected(true);
            binding.tvPercent.setSelected(false);
            discountType = "$";
        }

        binding.tvUpdate.setOnClickListener(V -> {
            Utilities.hideSoftKeyboard(context);
            if (isInputValid()) {
                callUpdateTax();
            }
        });

        binding.tvCancel.setOnClickListener(V -> {
            Utilities.hideSoftKeyboard(context);
            dismiss();
        });

        binding.tvPercent.setOnClickListener(V -> {
            binding.tvPercent.setSelected(true);
            binding.tvDollar.setSelected(false);
            discountType = "%";
        });
        binding.tvDollar.setOnClickListener(V -> {
            binding.tvDollar.setSelected(true);
            binding.tvPercent.setSelected(false);
            discountType = "$";
        });

    }

    private void callUpdateTax() {
        if (Utilities.isNetworkAvailable(context)) {
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> map = new HashMap<>();
            map.put("title", binding.etTitle.getText().toString().trim());
            map.put("type", discountType);
            map.put("value", binding.etValue.getText().toString());
            map.put("id", String.valueOf(tax.getId()));
            apiRequest = new RestClient.ApiRequest(context);
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.TAX_API)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(map)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setResponseListener((tag, response) -> {
                        AlertUtil.hideProgressDialog();
                        if (response != null) {
                            GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                            if (genericResponse.getCode() == 200) {
                                taxUpdateListener.onTaxUpdated();
                                Toast.makeText(context, genericResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        }
                    }).setErrorListener((tag, errorMsg) -> {
                AlertUtil.hideProgressDialog();
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
            }).execute();
        }
    }

    private boolean isInputValid() {
        if (binding.etTitle.getText().toString().trim().length() == 0) {
            Toast.makeText(context, context.getString(R.string.empty_title), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etValue.getText().toString().trim().length() == 0) {
            Toast.makeText(context, context.getString(R.string.empty_value), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.tvPercent.isSelected()) {
            if (Integer.valueOf(binding.etValue.getText().toString().trim()) < 1 || Integer.valueOf(binding.etValue.getText().toString().trim()) > 99) {
                Toast.makeText(context, context.getString(R.string.error_percent), Toast.LENGTH_SHORT).show();
                return false;
            } else return true;
        } else return true;
    }

    public interface TaxUpdateListener {
        void onTaxUpdated();
    }

}
