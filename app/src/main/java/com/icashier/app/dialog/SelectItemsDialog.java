package com.icashier.app.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.SelectItemsAdapter;
import com.icashier.app.adapter.TodaysSpecialAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.DialogSelectItemsBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.SelectItemListener;
import com.icashier.app.model.ExistingItemList;

import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectItemsDialog extends Dialog {

    HomeActivity context;
    private LayoutInflater inflater;
    DialogSelectItemsBinding binding;
    List<ExistingItemList.ResultBean> list;
    SelectItemsAdapter adapter;
    SelectItemListener listener;


    public SelectItemsDialog(HomeActivity context,List<ExistingItemList.ResultBean> itemList,SelectItemListener listener) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list=itemList;
        this.listener=listener;

    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_select_items, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        setAdapter();

        binding.btnDone.setOnClickListener(V->{
                dismiss();
                listener.onAddClicked();
        });



    }

    //===============Method to set Adapter============//
    private void setAdapter() {
        if(list.size()>0){
            binding.tvEmptyView.setVisibility(View.GONE);
        }else{
            binding.tvEmptyView.setVisibility(View.VISIBLE);
        }
        adapter=new SelectItemsAdapter(context, list);
        binding.rvItems.setAdapter(adapter);
        binding.rvItems.setLayoutManager(new LinearLayoutManager(context));

    }




}