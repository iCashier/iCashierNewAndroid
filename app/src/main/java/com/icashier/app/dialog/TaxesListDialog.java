package com.icashier.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.MealItemsAdapter;
import com.icashier.app.adapter.TaxesAdapter;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.DialogTaxesListBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.ExistingItemListener;
import com.icashier.app.model.GetTaxListResponse;
import com.icashier.app.model.MenuResponse;

import java.util.ArrayList;
import java.util.List;

public class TaxesListDialog extends Dialog {

    Context context;
    private LayoutInflater inflater;
    DialogTaxesListBinding binding;
    RestClient.ApiRequest apiRequest;
    List<GetTaxListResponse.ResultBean> taxList = new ArrayList<>();
    TaxesAdapter taxesAdapter;
    List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean> list;
    MealItemsAdapter adapter;

    public TaxesListDialog(Context context) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list=list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_taxes_list, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);
        setAdapter();
        callGetTax();
        binding.btnCancel.setOnClickListener(V->{
            dismiss();
        });

    }

    //===============Method to set Adapter============//
    private void setAdapter() {
        binding.rvTaxes.setLayoutManager(new LinearLayoutManager(context));
        taxesAdapter = new TaxesAdapter(context, taxList, new ExistingItemListener() {
            @Override
            public void onDeleteClick(int position) {

            }

            @Override
            public void onEditClick(int position) {

            }
        },false);
        binding.rvTaxes.setAdapter(taxesAdapter);
    }

    private void callGetTax() {
        //Utilities.hideSoftKeyboard(context);
        if (Utilities.isNetworkAvailable(context)) {
            binding.progressBar.setVisibility(View.VISIBLE);
            apiRequest = new RestClient.ApiRequest(context);
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.TAX_API)
                    .setMethod(RestClient.ApiRequest.METHOD_GET)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
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
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }).execute();
        } else
            AlertUtil.toastMsg(context, context.getString(R.string.network_error));
    }


}
