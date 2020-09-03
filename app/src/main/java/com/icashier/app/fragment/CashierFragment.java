package com.icashier.app.fragment;


import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.CashiersAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentCashierBinding;
import com.icashier.app.dialog.EditCashierDialog;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.ImagePickerUtil;
import com.icashier.app.helper.PermissionsUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.ExistingItemListener;
import com.icashier.app.model.CashierSignupResponse;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.GetCashiersResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class CashierFragment extends Fragment {

    HomeActivity context;
    FragmentCashierBinding binding;
    File cashierImage;
    RestClient.ApiRequest apiRequest;
    List<GetCashiersResponse.CashiersBean> cashierList = new ArrayList<>();
    CashiersAdapter cashiersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = (HomeActivity) getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cashier, container, false);

        setAdapter();
        getCashiersCall();
        onViewClickListener();
        return binding.getRoot();
    }

    private void onViewClickListener() {
        binding.imgEdit.setOnClickListener(V -> {
            openImagePicker();
        });
        binding.flSave.setOnClickListener(V -> {
            if (checkFields()) {
                callSignupCashier();
            }
        });
    }

    private void callSignupCashier() {
        if (Utilities.isNetworkAvailable(context)) {
            AlertUtil.showProgressDialog(context);

            HashMap<String, String> map = new HashMap<>();
            map.put("name", binding.etName.getText().toString().trim());
            map.put("passcode", binding.etPassword.getText().toString().trim());

            HashMap<String, File> fileMap = new HashMap<>();
            fileMap.put("image", cashierImage);
            apiRequest = new RestClient.ApiRequest(context);
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.SIGNUP_CASHIER)
                    .setMethod(RestClient.ApiRequest.METHOD_MULTIPART)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setParams(map)
                    .setFileParams(fileMap)
                    .setResponseListener((tag, response) -> {
                        AlertUtil.hideProgressDialog();
                        if (response != null) {
                            CashierSignupResponse cashierSignupResponse = new Gson().fromJson(response, CashierSignupResponse.class);
                            if (cashierSignupResponse.getCode() == 200) {
                                binding.etName.setText("");
                                binding.etPassword.setText("");
                                cashierImage=null;
                                binding.imgCashier.setImageDrawable(context.getResources().getDrawable(R.drawable.def_user));
                                GetCashiersResponse.CashiersBean cashier = new GetCashiersResponse.CashiersBean();
                                cashier.setId(cashierSignupResponse.getResult().getMid());
                                cashier.setImage(cashierSignupResponse.getResult().getImage());
                                cashier.setName(cashierSignupResponse.getResult().getName());
                                cashier.setSignup(cashierSignupResponse.getResult().getSignup());
                                cashierList.add(cashier);
                                cashiersAdapter.notifyDataSetChanged();
                            }else{
                                AlertUtil.showToastShort(context,cashierSignupResponse.getMessage());
                            }
                        }
                    }).setErrorListener((tag, errorMsg) -> {
                AlertUtil.hideProgressDialog();
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
            }).execute();

        } else
            Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
    }

    private boolean checkFields() {
        if (binding.etName.getText().toString().trim().length() < 1) {
            Toast.makeText(context, context.getString(R.string.empty_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etPassword.getText().toString().trim().length() < 1) {
            Toast.makeText(context, context.getString(R.string.empty_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cashierImage == null) {
            Toast.makeText(context, context.getString(R.string.error_cashier_image), Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }

    private void openImagePicker() {
        //to ask permissions at runtime
        PermissionsUtil.askPermissions(context, PermissionsUtil.CAMERA, PermissionsUtil.STORAGE, new PermissionsUtil.PermissionListener() {
            @Override
            public void onPermissionResult(boolean isGranted) {
                if (isGranted) {
                    ImagePickerUtil.selectImage(context, new ImagePickerUtil.ImagePickerListener() {
                        @Override
                        public void onImagePicked(File imageFile, String tag) {
                            binding.imgCashier.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
                            cashierImage = imageFile;
                        }
                    }, "img" + new Random().nextInt(), false);
                }

            }
        });

    }

    private void getCashiersCall() {
        if (Utilities.isNetworkAvailable(context)) {
            //AlertUtil.showProgressDialog(context);
            apiRequest = new RestClient.ApiRequest(context);
            HashMap<String, String> map = new HashMap<>();
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_CASHIER_LIST)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(map)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setResponseListener((tag, response) -> {
                        AlertUtil.hideProgressDialog();
                        if (response != null) {
                            GetCashiersResponse getCashiersResponse = new Gson().fromJson(response, GetCashiersResponse.class);
                            if (getCashiersResponse.getCode() == 200) {
                                cashierList.clear();
                                cashierList.addAll(getCashiersResponse.getCashiers());
                                cashiersAdapter.notifyDataSetChanged();
                            }
                        }
                    }).setErrorListener((tag, errorMsg) -> {
                AlertUtil.hideProgressDialog();
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
            }).execute();
        } else
            Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
    }

    private void setAdapter() {
        binding.rvCashier.setLayoutManager(new LinearLayoutManager(context));
        cashiersAdapter = new CashiersAdapter(context, cashierList, new ExistingItemListener() {
            @Override
            public void onDeleteClick(int position) {

                AlertUtil.showAlertWindow(context, getString(R.string.delete_casheir_alert), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCashier(position);
                    }
                });
            }

            @Override
            public void onEditClick(int position) {
                EditCashierDialog editCashierDialog = new EditCashierDialog(context, cashierList.get(position), () -> getCashiersCall());
                editCashierDialog.show();
            }
        });
        binding.rvCashier.setAdapter(cashiersAdapter);
    }

    private void deleteCashier(int position) {
        if (Utilities.isNetworkAvailable(context)) {
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(cashierList.get(position).getId()));
            apiRequest = new RestClient.ApiRequest(context);
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.DELETE_CASHIER)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setParams(map)
                    .setResponseListener((tag, response) -> {
                        AlertUtil.hideProgressDialog();
                        if (response != null) {
                            GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                            if (genericResponse.getCode() == 200) {
//                                cashierList.remove(position);
                                getCashiersCall();
                            }
                        }
                    }).setErrorListener((tag, errorMsg) -> {
                AlertUtil.hideProgressDialog();
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
            }).execute();
        } else
            Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtil.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
