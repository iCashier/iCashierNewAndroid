package com.icashier.app.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.DialogEditCouponBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.ImagePickerUtil;
import com.icashier.app.helper.PermissionsUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.CashierSignupResponse;
import com.icashier.app.model.CreateCouponResponse;
import com.icashier.app.model.GetCashiersResponse;
import com.icashier.app.model.GetCouponsResponse;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class EditCouponDialog extends Dialog {

    Activity context;
    private LayoutInflater inflater;
    DialogEditCouponBinding binding;
    GetCouponsResponse.ResultBean coupon;
    RestClient.ApiRequest apiRequest;
    String discountType = "";
    CouponUpdateListener couponUpdateListener;
    String selectedDate = "";
    Calendar myCalendar;

    public EditCouponDialog(Activity context, GetCouponsResponse.ResultBean coupon, CouponUpdateListener couponUpdateListener) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.coupon = coupon;
        this.couponUpdateListener = couponUpdateListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_edit_coupon, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);

        myCalendar = Calendar.getInstance();
        binding.etTitle.setText(coupon.getTitle());
        binding.etValue.setText(coupon.getValue());
        if (coupon.getType().equals("%")) {
            binding.tvPercent.setSelected(true);
            binding.tvDollar.setSelected(false);
            discountType = "%";
            binding.tvValue.setText(context.getString(R.string.value_percent));

        } else if (coupon.getType().equals("$")) {
            binding.tvDollar.setSelected(true);
            binding.tvPercent.setSelected(false);
            discountType = "$";
            binding.tvValue.setText(context.getString(R.string.value_sr));

        }
        selectedDate = coupon.getExpiry_date();
        //binding.tvDate.setText(Utilities.formatDate(coupon.getExpiry_date(), "yyyy-MM-dd", "MMM dd, yyyy"));
        binding.tvDate.setText(coupon.getExpiry_date());

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        binding.tvDate.setOnClickListener(V -> {
            new DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        binding.tvUpdate.setOnClickListener(V -> {
            Utilities.hideSoftKeyboard(context);
            if (isInputValid()) {
                callUpdateCoupon();
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
            binding.tvValue.setText(context.getString(R.string.value_percent));

        });
        binding.tvDollar.setOnClickListener(V -> {
            binding.tvDollar.setSelected(true);
            binding.tvPercent.setSelected(false);
            discountType = "$";
            binding.tvValue.setText(context.getString(R.string.value_sr));

        });

    }

    private void callUpdateCoupon() {
        if (Utilities.isNetworkAvailable(context)) {
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> map = new HashMap<>();
            map.put("title", binding.etTitle.getText().toString().trim());
            map.put("type", discountType);
            map.put("value", binding.etValue.getText().toString());
            map.put("expiry_date", selectedDate);
            map.put("id", String.valueOf(coupon.getId()));
            apiRequest = new RestClient.ApiRequest(context);
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.CREATE_COUPON)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(map)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setResponseListener((tag, response) -> {
                        AlertUtil.hideProgressDialog();
                        if (response != null) {
                            CreateCouponResponse createCouponResponse = new Gson().fromJson(response, CreateCouponResponse.class);
                            if (createCouponResponse.getCode() == 200) {
                                couponUpdateListener.onCouponUpdated();
                                Toast.makeText(context, createCouponResponse.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void updateLabel() {
        String myFormat = "MMM dd, yyyy"; //In which you need put here
        String serverFormat = "yyyy-MM-dd"; // format that we need to send to server
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdfServer = new SimpleDateFormat(serverFormat, Locale.US);
        binding.tvDate.setText(sdf.format(myCalendar.getTime()));
        selectedDate = sdfServer.format(myCalendar.getTime()) + " 00:00:00";
    }

    public interface CouponUpdateListener {
        void onCouponUpdated();
    }

}
