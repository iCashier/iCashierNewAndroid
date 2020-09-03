package com.icashier.app.dialog;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.TableItemListener;
import com.icashier.app.adapter.TableListAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.DialogAddTableBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.AddTableDialogListener;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.listener.EditTableDialogListener;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.TableListResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddTableDialog extends DialogFragment {

    HomeActivity context;
    DialogAddTableBinding binding;
    RestClient.ApiRequest apiRequest;
    List<TableListResponse.ResultBean> tableList=new ArrayList<>();
    TableListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        context=(HomeActivity)getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_table, container, false);
        setOnClickListener();
        setAdapter();
        return binding.getRoot();
    }

    //==============Mehtod to set table list adapter================//
    private void setAdapter() {
        adapter=new TableListAdapter(context, tableList, new TableItemListener() {
            @Override
            public void onDeleteClick(int position) {
                AlertUtil.showAlertWindow(context, getString(R.string.delete_table_alert), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callDeleteTableApi(position);
                    }
                });
            }

            @Override
            public void onEditClick(int position) {
                new EditTableDialog(context, tableList.get(position), new EditTableDialogListener() {
                    @Override
                    public void onUpdateClick(TableListResponse.ResultBean tableData) {
                        callUpdateTableApi(tableData.getName(),tableData.getId());
                    }
                }).show();
            }
        });

        binding.rvTables.setAdapter(adapter);
        binding.rvTables.setLayoutManager(new LinearLayoutManager(context));
        callGetTableListApi();
    }

    //====================Mehtod to set onClick Listener=================//
    private void setOnClickListener() {
        binding.flSave.setOnClickListener(V->{
            if(!binding.etName.getText().toString().trim().equals("")){
                callAddTableApi();
            }else{
                AlertUtil.toastMsg(context,context.getString(R.string.empty_table_name));
            }
        });
        binding.clParentLayout.setOnClickListener(V->{
            Utilities.hideKeyboardOnPopup(context,binding.etName);

        });

        binding.progressBar.setOnClickListener(V->{});
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        getDialog().setCancelable(true);
        getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                AddTableDialogListener listener=(AddTableDialogListener)getTargetFragment();
                listener.onDialogDismissed(tableList);
            }
        });
    }

    //============Mehtod to call add table api================//
    private void callDeleteTableApi(int position) {
        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> params = new HashMap<>();
            params.put("id",""+tableList.get(position).getId());


            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.DELETE_TABLE)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    GenericResponse genericResponse= new Gson().fromJson(response, GenericResponse.class);
                                    if (genericResponse != null) {
                                        if (genericResponse.getCode()==200 ) {
                                            tableList.remove(position);
                                            adapter.notifyDataSetChanged();
                                            if(tableList.size()>0){
                                                binding.tvExistingTables.setVisibility(View.VISIBLE);
                                            }else{
                                                binding.tvExistingTables.setVisibility(View.GONE);
                                            }
                                        } else if(genericResponse.getCode()==301)
                                        {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                    callDeleteTableApi(position);
                                                }
                                            });
                                        }
                                    } else {
                                        AlertUtil.hideProgressDialog();
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }
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
            AlertUtil.toastMsg(context,getString(R.string.network_error));
        }
    }

    //============Mehtod to call add table api================//
    private void callAddTableApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboardOnPopup(context,binding.etName);
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> params = new HashMap<>();
            params.put("name",binding.etName.getText().toString().trim());


            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.ADD_TABLE)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    TableListResponse tableListResponse= new Gson().fromJson(response, TableListResponse.class);
                                    if (tableListResponse != null) {
                                        if (tableListResponse.getCode()==200 ) {
                                            binding.etName.setText("");
                                            tableList.clear();
                                            tableList.addAll(tableListResponse.getResult());
                                            adapter.notifyDataSetChanged();
                                            if(tableList.size()>0){
                                                binding.tvExistingTables.setVisibility(View.VISIBLE);
                                            }else{
                                                binding.tvExistingTables.setVisibility(View.GONE);
                                            }
                                        }
                                    } else {
                                        AlertUtil.hideProgressDialog();
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }
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
            AlertUtil.toastMsg(context,getString(R.string.network_error));
        }
    }

    //============Mehtod to call add table api================//
    private void callUpdateTableApi(String name ,int id) {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboardOnPopup(context,binding.etName);
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> params = new HashMap<>();
            params.put("name",name);
            params.put("id",""+id);


            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.EDIT_TABLE)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    TableListResponse tableListResponse= new Gson().fromJson(response, TableListResponse.class);
                                    if (tableListResponse != null) {
                                        if (tableListResponse.getCode()==200 ) {
                                            tableList.clear();
                                            tableList.addAll(tableListResponse.getResult());
                                            adapter.notifyDataSetChanged();
                                            if(tableList.size()>0){
                                                binding.tvExistingTables.setVisibility(View.VISIBLE);
                                            }else{
                                                binding.tvExistingTables.setVisibility(View.GONE);
                                            }
                                        }
                                    } else {
                                        AlertUtil.hideProgressDialog();
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }
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
            AlertUtil.toastMsg(context,getString(R.string.network_error));
        }
    }

    //============Method to call get table list api===========//
    private void callGetTableListApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboardOnPopup(context,binding.etName);
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> params = new HashMap<>();
            params.put("name",binding.etName.getText().toString().trim());

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_TABLE_LIST)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    TableListResponse tableListResponse= new Gson().fromJson(response, TableListResponse.class);
                                    if (tableListResponse != null) {
                                        if (tableListResponse.getCode()==200 ) {
                                            tableList.clear();
                                            tableList.addAll(tableListResponse.getResult());
                                            adapter.notifyDataSetChanged();
                                            if(tableList.size()>0){
                                                binding.tvExistingTables.setVisibility(View.VISIBLE);
                                            }else{
                                                binding.tvExistingTables.setVisibility(View.GONE);
                                            }
                                        }
                                    } else {
                                        AlertUtil.hideProgressDialog();
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }
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
            AlertUtil.toastMsg(context,getString(R.string.network_error));
        }
    }

}
