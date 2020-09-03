package com.icashier.app.fragment;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.CouponsAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentCouponsBinding;
import com.icashier.app.dialog.EditCouponDialog;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.PermissionsUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.ExistingItemListener;
import com.icashier.app.model.CreateCouponResponse;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.GetCouponsResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class CouponsFragment extends Fragment {

    HomeActivity context;
    FragmentCouponsBinding binding;
    String discountType = "%";
    RestClient.ApiRequest apiRequest;
    String selectedDate = "";
    Calendar myCalendar;
    List<GetCouponsResponse.ResultBean> couponsList = new ArrayList<>();
    CouponsAdapter couponsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = (HomeActivity) getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_coupons, container, false);

        myCalendar = Calendar.getInstance();
        binding.tvPercent.setSelected(true);
        setOnClickListeners();
        setAdapter();
        callGetCoupons();
        updateLabel();
        return binding.getRoot();
    }

    private void setAdapter() {
        binding.rvCoupons.setLayoutManager(new LinearLayoutManager(context));
        couponsAdapter = new CouponsAdapter(context, couponsList, new ExistingItemListener() {
            @Override
            public void onDeleteClick(int position) {
                AlertUtil.showAlertWindow(context, getString(R.string.delete_coupon_alert), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCoupon(position);
                    }
                });
            }

            @Override
            public void onEditClick(int position) {
                EditCouponDialog editCouponDialog = new EditCouponDialog(context, couponsList.get(position), () -> callGetCoupons());
                editCouponDialog.show();
            }
        },false);
        binding.rvCoupons.setAdapter(couponsAdapter);
    }

    private void deleteCoupon(int position) {
        if (Utilities.isNetworkAvailable(context)) {
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(couponsList.get(position).getId()));
            apiRequest = new RestClient.ApiRequest(context);
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.DELETE_COUPON)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setParams(map)
                    .setResponseListener((tag, response) -> {
                        AlertUtil.hideProgressDialog();
                        if (response != null) {
                            GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                            if (genericResponse.getCode() == 200) {
//                                cashierList.remove(position);
                                callGetCoupons();
                            }
                        }
                    }).setErrorListener((tag, errorMsg) -> {
                AlertUtil.hideProgressDialog();
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
            }).execute();
        } else
            Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
    }

    private void callGetCoupons() {
        //AlertUtil.showProgressDialog(context);
        HashMap<String, String> map = new HashMap<>();
        apiRequest = new RestClient.ApiRequest(context);
        apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.COUPON_LIST)
                .setMethod(RestClient.ApiRequest.METHOD_POST)
                .setParams(map)
                .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                .setResponseListener((tag, response) -> {
                    AlertUtil.hideProgressDialog();
                    if (response != null) {
                        GetCouponsResponse getCouponsResponse = new Gson().fromJson(response, GetCouponsResponse.class);
                        if (getCouponsResponse.getCode() == 200) {
                            couponsList.clear();
                            couponsList.addAll(getCouponsResponse.getResult());
                            couponsAdapter.notifyDataSetChanged();
                        } else
                            Toast.makeText(context, getCouponsResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).setErrorListener((tag, errorMsg) -> {
            AlertUtil.hideProgressDialog();
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
        }).execute();
    }

    private void setOnClickListeners() {
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
        binding.tvPercent.setOnClickListener(V -> {
            binding.tvPercent.setSelected(true);
            binding.tvDollar.setSelected(false);
            discountType = "%";
            binding.tvValue.setText(getString(R.string.value_percent));
        });
        binding.tvDollar.setOnClickListener(V -> {
            binding.tvDollar.setSelected(true);
            binding.tvPercent.setSelected(false);
            discountType = "$";
            binding.tvValue.setText(getString(R.string.value_sr));

        });
        binding.flSave.setOnClickListener(V -> {
            if (isInputValid())
                callCreateCoupon();
        });
        binding.tvDate.setOnClickListener(V -> {
           DatePickerDialog dialog= new DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
           dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
           dialog.show();
        });
    }

    private void updateLabel() {
        String myFormat = "MMM dd, yyyy"; //In which you need put here
        String serverFormat = "yyyy-MM-dd"; // format that we need to send to server
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdfServer = new SimpleDateFormat(serverFormat, Locale.US);
        binding.tvDate.setText(sdf.format(myCalendar.getTime()));
        selectedDate = sdfServer.format(myCalendar.getTime());
    }

    private void callCreateCoupon() {
        Utilities.hideSoftKeyboard(context);
        if (Utilities.isNetworkAvailable(context)) {
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> map = new HashMap<>();
            map.put("title", binding.etTitle.getText().toString().trim());
            map.put("type", discountType);
            map.put("value", binding.etValue.getText().toString().trim());
            map.put("expiry_date", binding.tvDate.getText().toString().trim());
            apiRequest = new RestClient.ApiRequest(context);
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.CREATE_COUPON)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(map)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (response != null) {
                                CreateCouponResponse createCouponResponse = new Gson().fromJson(response, CreateCouponResponse.class);
                                if (createCouponResponse.getCode() == 200) {
                                    clearFields();
                                    myCalendar = Calendar.getInstance();
                                    updateLabel();
                                    callGetCoupons();
                                }
                            }
                        }
                    }).setErrorListener(new RestClient.ErrorListener() {
                @Override
                public void onError(String tag, String errorMsg) {
                    AlertUtil.hideProgressDialog();
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }).execute();
        } else
            AlertUtil.toastMsg(context, getString(R.string.network_error));
    }

    private void clearFields() {
        binding.etValue.setText("");
        binding.etTitle.setText("");
        binding.tvPercent.setSelected(true);
        binding.tvDollar.setSelected(false);
        binding.etTitle.requestFocus();
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtil.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
