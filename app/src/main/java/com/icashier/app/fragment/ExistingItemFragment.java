package com.icashier.app.fragment;


import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.ExistingItemAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentExistingItemBinding;
import com.icashier.app.dialog.EditItemDialog;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.listener.EditItemListener;
import com.icashier.app.listener.ExistingItemListener;
import com.icashier.app.model.ExistingItemList;
import com.icashier.app.model.GenericResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExistingItemFragment extends Fragment implements EditItemListener{

    FragmentExistingItemBinding binding;
    HomeActivity context;
    RestClient.ApiRequest apiRequest;
    List<ExistingItemList.ResultBean> list=new ArrayList<>();
    ExistingItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        context=(HomeActivity)getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_existing_item, container, false);
        setAdapter();
        return binding.getRoot();
    }

    //===============Method to set Adapter============//
    private void setAdapter() {
        adapter=new ExistingItemAdapter(context, list, new ExistingItemListener() {
            @Override
            public void onDeleteClick(int position) {

                AlertUtil.showAlertWindow(context, getString(R.string.delete_item_alert), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callDeleteItemApi(position);
                    }
                });
            }

            @Override
            public void onEditClick(int position) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog1");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

               DialogFragment dialogFragment1=new EditItemDialog();
               Bundle bundle=new Bundle();
               bundle.putSerializable(AppConstant.EXISTING_ITEM,list.get(position));
               dialogFragment1.setArguments(bundle);
               dialogFragment1.setTargetFragment(ExistingItemFragment.this,1);
               dialogFragment1.show(ft,"dialog");
            }
        });
        binding.rvExistingItems.setAdapter(adapter);
        binding.rvExistingItems.setLayoutManager(new LinearLayoutManager(context));
        callGetItemListApi();

    }

    //==========================Method to call get existing items  api=====================//
    private void callGetItemListApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            binding.progressBar.setVisibility(View.VISIBLE);
           // AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("token",SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));


            HashMap<String,String> headers=new HashMap<>();
            headers.put("token",SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));
            headers.put("type",SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE,AppConstant.TYPE_MERCHANT));
            headers.put("cashiertoken",SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN,""));
            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_ITEM_LIST)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeaders(headers)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    ExistingItemList existingItemList= new Gson().fromJson(response, ExistingItemList.class);
                                    if (existingItemList != null) {
                                        AlertUtil.hideProgressDialog();
                                        if (existingItemList.getCode()==200 ) {
                                            list.clear();
                                            list.addAll(existingItemList.getResult());
                                            if(list.size()>0){
                                                binding.tvEmptyView.setVisibility(View.GONE);
                                            }else{
                                                binding.tvEmptyView.setVisibility(View.VISIBLE);
                                            }
                                            adapter.notifyDataSetChanged();
                                        } else if(existingItemList.getCode()==301)
                                        {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                    callGetItemListApi();
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

    //==========================Method to call delete items  api=====================//
    private void callDeleteItemApi(int position) {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("token",SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));
            params.put("id",""+list.get(position).getId());

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.DELETE_ITEM)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    GenericResponse genericResponse= new Gson().fromJson(response, GenericResponse.class);
                                    if (genericResponse != null) {
                                        AlertUtil.hideProgressDialog();
                                        if (genericResponse.getCode()==200 ) {
                                            list.remove(position);
                                            adapter.notifyDataSetChanged();
                                        } else if(genericResponse.getCode()==301)
                                        {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                    callDeleteItemApi(position);
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
                            AlertUtil.hideProgressDialog();
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        }else
        {
            AlertUtil.toastMsg(context,getString(R.string.network_error));
        }
    }

    @Override
    public void onUpdateSuccess() {
        callGetItemListApi();
    }
}
