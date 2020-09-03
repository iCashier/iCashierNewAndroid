package com.icashier.app.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.PrinterListAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentSettingsBinding;
import com.icashier.app.dialog.ChangeLangDialog;
import com.icashier.app.dialog.SelectPrinterDialog;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.PermissionsUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.GenericResponse;

import java.util.HashMap;


public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;
    HomeActivity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = (HomeActivity) getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);

        if (SharedPrefManager.getInstance(context).getInt(AppConstant.IS_PARENT, 1) == 0) {
            //binding.llChangePass.setVisibility(View.GONE);
        }
        setOnClickListener();
        callChangeStatusApi("0");
        setCheckChangeListener();
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(context);
        if (sharedPrefManager.getInt(AppConstant.PRINTER_AUTO_ON, 0) == 1) {
            binding.switchPrint.setChecked(true);
            binding.tvOnOffTwo.setText(context.getString(R.string.on));
        } else {
            binding.switchPrint.setChecked(false);
            binding.tvOnOffTwo.setText(context.getString(R.string.off));
        }
        return binding.getRoot();
    }

    //==================MEthod to set onClick listener===================//
    private void setOnClickListener() {
        binding.llChangeLang.setOnClickListener(V -> {
            context.closeDrawer();
            new ChangeLangDialog(context, binding.clParentLayout).show();
        });

        binding.llChangePass.setOnClickListener(V -> {
            context.closeDrawer();
            context.openResetPassword();
        });

        binding.getRoot().setOnClickListener(V -> {
            context.closeDrawer();
        });

        /*devices Selection*/
        binding.llSetPrinter.setOnClickListener(V -> {
            startDevicesSelection();
        });

        binding.llSetPrinterAuto.setOnClickListener(V -> {
            startDevicesSelectionTwo();
        });

        binding.switchPrint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(context);
                    sharedPrefManager.saveInt(AppConstant.PRINTER_AUTO_ON, 1);
                    binding.tvOnOffTwo.setText(context.getString(R.string.on));
                } else {
                    SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(context);
                    sharedPrefManager.saveInt(AppConstant.PRINTER_AUTO_ON, 0);
                    binding.tvOnOffTwo.setText(context.getString(R.string.off));
                }

            }
        });
    }

    // for auto printer register
    private void startDevicesSelectionTwo() {

        PermissionsUtil.askPermissions(context, PermissionsUtil.STORAGE, PermissionsUtil.LOCATION, new PermissionsUtil.PermissionListener() {
            @Override
            public void onPermissionResult(boolean isGranted) {
                if (isGranted) {
                    new SelectPrinterDialog(context, new PrinterListAdapter.SelectPrinterListener() {
                        @Override
                        public void onClick(String name, String target) {
                            Log.e(name, target);
                            SharedPrefManager myPref = SharedPrefManager.getInstance(context);
                            myPref.saveString(AppConstant.PRINTER_DEVICES_TARGET_AUTO, target);

                            //targetPrinter = target;
                            //runPrintReceiptSequence();
                        }
                    }).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(context);
        if (sharedPrefManager.getInt(AppConstant.PRINTER_AUTO_ON, 0) == 1) {
            binding.switchPrint.setChecked(true);
            binding.tvOnOffTwo.setText(context.getString(R.string.on));
        } else {
            binding.switchPrint.setChecked(false);
            binding.tvOnOffTwo.setText(context.getString(R.string.off));
        }


    }

    // for manual printer
    private void startDevicesSelection() {

        PermissionsUtil.askPermissions(context, PermissionsUtil.STORAGE, PermissionsUtil.LOCATION, new PermissionsUtil.PermissionListener() {
            @Override
            public void onPermissionResult(boolean isGranted) {
                if (isGranted) {
                    new SelectPrinterDialog(context, new PrinterListAdapter.SelectPrinterListener() {
                        @Override
                        public void onClick(String name, String target) {
                            Log.e(name, target);
                            SharedPrefManager myPref = SharedPrefManager.getInstance(context);
                            myPref.saveString(AppConstant.PRINTER_DEVICES_TARGET, target);

                            //targetPrinter = target;
                            //runPrintReceiptSequence();
                        }
                    }).show();
                }
            }
        });
    }

    private void setCheckChangeListener() {

        binding.switchNoti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                callChangeStatusApi("1");

            }
        });

    }

    //==========================Method to call change online status api=====================//
    private void callChangeStatusApi(String type) {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);

            HashMap<String, String> params = new HashMap<>();
            params.put("status", binding.switchNoti.isChecked() ? "1" : "0");
            params.put("type", type);

            RestClient.ApiRequest apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.CHANGE_NOTIFICATION_SETTING)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                                if (genericResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (genericResponse.getCode() == 200) {
                                        binding.switchNoti.setOnCheckedChangeListener(null);
                                        if (genericResponse.getResult().getStatus() == 1) {
                                            binding.switchNoti.setChecked(true);
                                            binding.tvOnOff.setText(getString(R.string.on));
                                        } else {
                                            binding.switchNoti.setChecked(false);
                                            binding.tvOnOff.setText(getString(R.string.off));
                                        }


                                        setCheckChangeListener();

                                    } else {

                                        AlertUtil.toastMsg(context, genericResponse.getMessage());
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


}
