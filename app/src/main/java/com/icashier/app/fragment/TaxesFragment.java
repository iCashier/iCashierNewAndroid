package com.icashier.app.fragment;


import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.TaxesAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentTaxesBinding;
import com.icashier.app.dialog.EditTaxDialog;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.ExistingItemListener;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.GetTaxListResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TaxesFragment extends Fragment {

    HomeActivity context;
    FragmentTaxesBinding binding;
    String discountType = "%";
    RestClient.ApiRequest apiRequest;
    TaxesAdapter taxesAdapter;
    List<GetTaxListResponse.ResultBean> taxList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = (HomeActivity) getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_taxes, container, false);
        binding.tvPercent.setSelected(true);
        setAdapter();
        callGetTax();
        setupScreen();
        return binding.getRoot();
    }

    private void setAdapter() {
        binding.rvTaxes.setLayoutManager(new LinearLayoutManager(context));
        taxesAdapter = new TaxesAdapter(context, taxList, new ExistingItemListener() {
            @Override
            public void onDeleteClick(int position)
            {
                AlertUtil.showAlertWindow(context, getString(R.string.delete_tax_alert), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTax(position);
                    }
                });
            }

            @Override
            public void onEditClick(int position) {
                EditTaxDialog editTaxDialog = new EditTaxDialog(context, taxList.get(position), () -> callGetTax());
                editTaxDialog.show();
            }
        },true);
        binding.rvTaxes.setAdapter(taxesAdapter);
    }

    private void deleteTax(int position) {
        if (Utilities.isNetworkAvailable(context)) {
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(taxList.get(position).getId()));
            apiRequest = new RestClient.ApiRequest(context);
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.DELETE_TAX)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setParams(map)
                    .setResponseListener((tag, response) -> {
                        AlertUtil.hideProgressDialog();
                        if (response != null) {
                            GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                            if (genericResponse.getCode() == 200) {
//                                cashierList.remove(position);
                                callGetTax();
                            }
                        }
                    }).setErrorListener((tag, errorMsg) -> {
                AlertUtil.hideProgressDialog();
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
            }).execute();
        } else
            Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
    }

    private void setupScreen() {
        onViewClickListeners();
    }

    private void onViewClickListeners() {
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
        binding.flSave.setOnClickListener(V -> {
            if (isInputValid())
                callAddTax();
        });

    }

    private void callAddTax() {
        Utilities.hideSoftKeyboard(context);
        if (Utilities.isNetworkAvailable(context)) {
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> map = new HashMap<>();
            map.put("title", binding.etTitle.getText().toString().trim());
            map.put("type", discountType);
            map.put("value", binding.etValue.getText().toString().trim());
            apiRequest = new RestClient.ApiRequest(context);
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.TAX_API)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(map)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (response != null) {
                                GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                                if (genericResponse.getCode() == 200) {
                                    clearFields();
                                    callGetTax();
                                }else{
                                    AlertUtil.showToastShort(context,genericResponse.getMessage());
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

    private void callGetTax() {
        Utilities.hideSoftKeyboard(context);
        if (Utilities.isNetworkAvailable(context)) {
           // AlertUtil.showProgressDialog(context);
            apiRequest = new RestClient.ApiRequest(context);
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.TAX_API)
                    .setMethod(RestClient.ApiRequest.METHOD_GET)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (response != null) {
                                GetTaxListResponse getTaxListResponse = new Gson().fromJson(response, GetTaxListResponse.class);
                                if (getTaxListResponse.getCode() == 200) {
                                    taxList.clear();
                                    taxList.addAll(getTaxListResponse.getResult());
                                    taxesAdapter.notifyDataSetChanged();
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

}
