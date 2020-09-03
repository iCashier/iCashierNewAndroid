package com.icashier.app.dialog;

import android.app.Activity;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.DialogEditCashierBinding;
import com.icashier.app.databinding.DialogEditCategoryBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.ImagePickerUtil;
import com.icashier.app.helper.PermissionsUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.CashierSignupResponse;
import com.icashier.app.model.GetCashiersResponse;
import com.icashier.app.model.TableListResponse;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class EditCashierDialog extends Dialog {

    Activity context;
    private LayoutInflater inflater;
    DialogEditCashierBinding binding;
    GetCashiersResponse.CashiersBean cashier;
    File cashierImage;
    RestClient.ApiRequest apiRequest;
    CashierUpdateListener cashierUpdateListener;

    public EditCashierDialog(Activity context, GetCashiersResponse.CashiersBean cashier, CashierUpdateListener cashierUpdateListener) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.cashier = cashier;
        this.cashierUpdateListener = cashierUpdateListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_edit_cashier, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);
        binding.etName.setText(cashier.getName());
        Picasso.with(context).load(cashier.getImage()).placeholder(R.drawable.def_user).into(binding.imgCashier);

        binding.tvUpdate.setOnClickListener(V -> {
            Utilities.hideSoftKeyboard(context);
            if (isInputValid()) {
                if (cashierImage == null)
                    callUpdateCashier();
                else callUpdateCashierMultipart();
            }
        });
        binding.imgEdit.setOnClickListener(V -> {
            openImagePicker();
        });

        binding.tvCancel.setOnClickListener(V -> {
            Utilities.hideSoftKeyboard(context);
            dismiss();
        });

        binding.cbPass.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {

                binding.rlNewPassword.setVisibility(View.VISIBLE);
                binding.rlNewPasswordField.setVisibility(View.VISIBLE);
            } else {

                binding.rlNewPassword.setVisibility(View.GONE);
                binding.rlNewPasswordField.setVisibility(View.GONE);
            }
        });
    }

    private void callUpdateCashierMultipart() {
        if (Utilities.isNetworkAvailable(context)) {
            AlertUtil.showProgressDialog(context);

                HashMap<String, String> map = new HashMap<>();
                map.put("name", binding.etName.getText().toString().trim());
                map.put("passcode", binding.etPassword.getText().toString().trim());
                map.put("id", String.valueOf(cashier.getId()));
                if (binding.cbPass.isChecked())
                    map.put("newpasscode", binding.etNewPassword.getText().toString().trim());
                HashMap<String, File> fileMap = new HashMap<>();
                fileMap.put("image", cashierImage);
                apiRequest = new RestClient.ApiRequest(context);
                apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.SIGNUP_CASHIER)
                        .setMethod(RestClient.ApiRequest.METHOD_MULTIPART)
                        .setParams(map)
                        .setFileParams(fileMap)
                        .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                        .setResponseListener((tag, response) -> {
                            AlertUtil.hideProgressDialog();
                            CashierSignupResponse cashierSignupResponse = new Gson().fromJson(response, CashierSignupResponse.class);
                            if (cashierSignupResponse.getCode() == 200) {
                                cashierUpdateListener.onCashierUpdated();
                                Toast.makeText(context, cashierSignupResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        }).setErrorListener((tag, errorMsg) -> {
                    AlertUtil.hideProgressDialog();
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                }).execute();

        } else
            Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
    }

    private void callUpdateCashier() {
        if (Utilities.isNetworkAvailable(context)) {
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> map = new HashMap<>();
            HashMap<String, File> fileMap = new HashMap<>();
            map.put("name", binding.etName.getText().toString().trim());
            map.put("passcode", binding.etPassword.getText().toString().trim());
            map.put("id", String.valueOf(cashier.getId()));
            if (binding.cbPass.isChecked())
                map.put("newpasscode", binding.etNewPassword.getText().toString().trim());

            if (cashierImage == null) {

                map.put("image", cashier.getImage());
            }

            apiRequest = new RestClient.ApiRequest(context);
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.SIGNUP_CASHIER)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setParams(map)
                    .setResponseListener((tag, response) -> {
                        AlertUtil.hideProgressDialog();
                        CashierSignupResponse cashierSignupResponse = new Gson().fromJson(response, CashierSignupResponse.class);
                        if (cashierSignupResponse.getCode() == 200) {
                            cashierUpdateListener.onCashierUpdated();
                            Toast.makeText(context, cashierSignupResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            dismiss();
                        }else{
                            Toast.makeText(context, cashierSignupResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).setErrorListener((tag, errorMsg) -> {
                AlertUtil.hideProgressDialog();
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
            }).execute();
        } else
            Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
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

    private boolean isInputValid() {
        if (binding.etName.getText().toString().trim().length() < 1) {
            Toast.makeText(context, context.getString(R.string.empty_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etPassword.getText().toString().trim().length() < 1) {
            Toast.makeText(context, context.getString(R.string.empty_password), Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.etPassword.getText().toString().length() < 6) {
            Toast.makeText(context, context.getString(R.string.valid_password), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.cbPass.isChecked()) {


            if (binding.etNewPassword.getText().toString().trim().length() < 1) {
                Toast.makeText(context, context.getString(R.string.enter_new_password), Toast.LENGTH_SHORT).show();
                return false;
            } else if (binding.etNewPassword.getText().toString().trim().length() < 6) {
                Toast.makeText(context, context.getString(R.string.valid_new_password), Toast.LENGTH_SHORT).show();
                return false;
            } else if (binding.etPassword.getText().toString().trim().equals(binding.etNewPassword.getText().toString().trim())) {
                Toast.makeText(context, context.getString(R.string.same_old_new_pass), Toast.LENGTH_SHORT).show();
                return false;
            } else return true;
        } else return true;
    }

    public interface CashierUpdateListener {
        void onCashierUpdated();
    }

}
