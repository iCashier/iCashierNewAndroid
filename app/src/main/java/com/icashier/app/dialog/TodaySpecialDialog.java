package com.icashier.app.dialog;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.ExistingDealsAdapter;
import com.icashier.app.adapter.SelectMealAdapter;
import com.icashier.app.adapter.TodaysSpecialAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.DialogTodaySpecialBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.listener.CheckBoxListener;
import com.icashier.app.listener.ExtraItemListener;
import com.icashier.app.model.ExistingDealsModel;
import com.icashier.app.model.ExistingItemList;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.MealListResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TodaySpecialDialog  extends Dialog {

    HomeActivity context;
    private LayoutInflater inflater;
    DialogTodaySpecialBinding binding;

    RestClient.ApiRequest apiRequest;
    SelectMealAdapter mealAdapter;
    List<ExistingItemList.ResultBean> list=new ArrayList<>();
    TodaysSpecialAdapter adapter;
    List<MealListResponse.ResultBean> mealList=new ArrayList<>();
    List<ExistingDealsModel.ResultBean> dealList=new ArrayList<>();
    ExistingDealsAdapter dealsAdapter;


    public TodaySpecialDialog(HomeActivity context) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_today_special, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);
        setAdapter();
        binding.btnCancel.setOnClickListener(V->{
            dismiss();
        });
        binding.tabItems.setSelected(true);

        setOnClickListener();

    }

    private void setOnClickListener() {
        binding.tabItems.setOnClickListener(V->{
            binding.tabMeals.setSelected(false);
            binding.tabDeals.setSelected(false);
            binding.tabItems.setSelected(true);
            binding.llItems.setVisibility(View.VISIBLE);
            binding.llMeals.setVisibility(View.GONE);
            binding.llDeals.setVisibility(View.GONE);

        });

        binding.tabDeals.setOnClickListener(V->{
            binding.tabMeals.setSelected(false);
            binding.tabDeals.setSelected(true);
            binding.tabItems.setSelected(false);
            binding.llItems.setVisibility(View.GONE);
            binding.llMeals.setVisibility(View.GONE);
            binding.llDeals.setVisibility(View.VISIBLE);
        });

        binding.tabMeals.setOnClickListener(V->{
            binding.tabMeals.setSelected(true);
            binding.tabDeals.setSelected(false);
            binding.tabItems.setSelected(false);
            binding.llItems.setVisibility(View.GONE);
            binding.llMeals.setVisibility(View.VISIBLE);
            binding.llDeals.setVisibility(View.GONE);

        });
    }

    //===============Method to set Adapter============//
    private void setAdapter() {
        adapter=new TodaysSpecialAdapter(context, list,binding);
        binding.rvItems.setAdapter(adapter);
        binding.rvItems.setLayoutManager(new LinearLayoutManager(context));
        callGetItemListApi();
        callGetMealListApi();
        callGetDealsApi();

    }

    //==========================Method to call get existing items  api=====================//
    private void callGetItemListApi() {
        if(Utilities.isNetworkAvailable(context)) {
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> params = new HashMap<>();
            params.put("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));


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
                                        binding.progressBar.setVisibility(View.VISIBLE);
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }
                                }
                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            binding.progressBar.setVisibility(View.VISIBLE);
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        }else
        {
            AlertUtil.toastMsg(context,context.getString(R.string.network_error));
        }
    }

    //==========================Method to call get existing meals  api=====================//
    private void callGetMealListApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            //AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_MEAL_LIST)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                MealListResponse mealListResponse= new Gson().fromJson(response, MealListResponse.class);
                                if (mealListResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (mealListResponse.getCode()==200 ) {
                                        mealList.clear();
                                        mealList.addAll(mealListResponse.getResult());
                                        setMealAdapter();
                                        /*if(mealList.size()>0){
                                            binding.tvExistingMeals.setVisibility(View.VISIBLE);
                                        }else{
                                            binding.tvExistingMeals.setVisibility(View.GONE);
                                        }*/
                                    } else if(mealListResponse.getCode()==301)
                                    {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callGetMealListApi();
                                            }
                                        });
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
        }else
        {
            AlertUtil.toastMsg(context,context.getString(R.string.network_error));
        }
    }


    //===============Method to set Adapter============//
    private void setMealAdapter() {
        if(list.size()>0){
            binding.tvEmptyView.setVisibility(View.GONE);
        }else{
            binding.tvEmptyView.setVisibility(View.VISIBLE);
        }

         mealAdapter=new SelectMealAdapter(context, mealList, true, new CheckBoxListener() {
            @Override
            public void onCheckChanged(int position) {
                callEditMealApi(position);
            }
        });
        binding.lvExpMeals.setAdapter(mealAdapter);
        binding.lvExpMeals.setGroupIndicator(null);
        binding.lvExpMeals.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

    }

    private void callGetDealsApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            // AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.DEALS)
                    .setMethod(RestClient.ApiRequest.METHOD_GET)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                ExistingDealsModel existingDealsModel= new Gson().fromJson(response, ExistingDealsModel.class);
                                if (existingDealsModel != null) {
                                    if(existingDealsModel.getCode()==200){
                                        if(existingDealsModel.getResult().size()>0){
                                            dealList.clear();
                                            dealList.addAll(existingDealsModel.getResult());
                                            setDealsAdapter();
                                        }
                                    }
                                } else {
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
        }else
        {
            AlertUtil.toastMsg(context,context.getString(R.string.network_error));
        }
    }
    private void setDealsAdapter() {
        if(list.size()>0){
            binding.tvEmptyView.setVisibility(View.GONE);
        }else{
            binding.tvEmptyView.setVisibility(View.VISIBLE);
        }


        dealsAdapter=new ExistingDealsAdapter(context, dealList, new ExtraItemListener() {
            @Override
            public void onDeleteClick(int position) {
            }

            @Override
            public void onEditClick(int position) {
                callEditDealApi(position);

            }
        },true,true);
        binding.rvDeals.setLayoutManager(new LinearLayoutManager(context));
        binding.rvDeals.setAdapter(dealsAdapter);

    }

    private void callEditMealApi(int position) {

        if (Utilities.isNetworkAvailable(context)) {
            binding.progressBar.setVisibility(View.VISIBLE);

            HashMap<String, String> params = new HashMap<>();
            params.put("id",""+mealList.get(position).getId());
            if(mealList.get(position).getSpecialitem()==1){
                params.put("specialitem", "0");

            }else{
                params.put("specialitem","1");
            }

            apiRequest=new RestClient.ApiRequest(context);
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.EDIT_MEAL)
                    .setMethod(RestClient.ApiRequest.METHOD_MULTIPART)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {
                                MealListResponse mealListResponse = new Gson().fromJson(response, MealListResponse.class);
                                if (mealListResponse != null) {
                                    if (mealListResponse.getCode() == 200) {
                                        mealList.clear();
                                        mealList.addAll(mealListResponse.getResult());
                                        mealAdapter.notifyDataSetChanged();
                                        AlertUtil.toastMsg(context, mealListResponse.getMessage());
                                    } else if(mealListResponse.getCode()==301)
                                    {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callEditMealApi(position);
                                            }
                                        });
                                    }
                                } else {
                                    AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
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
        } else {
            AlertUtil.toastMsg(context, context.getString(R.string.network_error));
        }
    }


    private void callEditDealApi(int position) {

        if (Utilities.isNetworkAvailable(context)) {
            binding.progressBar.setVisibility(View.VISIBLE);

            HashMap<String, String> params = new HashMap<>();
            params.put("id",""+dealList.get(position).getId());
            if(dealList.get(position).getSpecialitem()==1){
                params.put("specialitem", "0");
            }else{
                params.put("specialitem","1");
            }

            apiRequest=new RestClient.ApiRequest(context);
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.MAKE_DEAL_SPECIAL)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {
                                GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                                if (genericResponse != null) {
                                    if (genericResponse.getCode() == 200) {
                                        if(dealList.get(position).getSpecialitem()==1){
                                            dealList.get(position).setSpecialitem(0);
                                        }else if(dealList.get(position).getSpecialitem()==0){
                                            dealList.get(position).setSpecialitem(1);
                                        }
                                        dealsAdapter.notifyDataSetChanged();
                                        AlertUtil.toastMsg(context, genericResponse.getMessage());
                                    } else if(genericResponse.getCode()==301)
                                    {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callEditDealApi(position);
                                            }
                                        });
                                    }
                                } else {
                                    AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
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
        } else {
            AlertUtil.toastMsg(context, context.getString(R.string.network_error));
        }
    }
}