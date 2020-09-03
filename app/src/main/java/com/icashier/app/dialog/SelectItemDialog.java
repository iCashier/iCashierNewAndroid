package com.icashier.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.SelectItemAdapter;
import com.icashier.app.adapter.SelectItemsAdapter;
import com.icashier.app.databinding.DialogSelectItemBinding;
import com.icashier.app.listener.DialogDismissListener;
import com.icashier.app.model.ExistingItemList;

import java.util.List;

public class SelectItemDialog extends Dialog {

    HomeActivity context;
    private LayoutInflater inflater;
    DialogSelectItemBinding binding;
    List<ExistingItemList.ResultBean> list;
    SelectItemAdapter adapter;
    DialogDismissListener dismissListener;

    public SelectItemDialog(HomeActivity context,List<ExistingItemList.ResultBean> itemList,DialogDismissListener dismissListener) {
        super(context);
        this.context=context;
        this.list=itemList;
        inflater=LayoutInflater.from(context);
        this.dismissListener=dismissListener;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_select_item, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        setAdapter();

        binding.btnCancel.setOnClickListener(V->{
            dismiss();
        });

        binding.btnOk.setOnClickListener(V->{
            dismiss();
            dismissListener.onDismiss();
        });


    }

    //===============Method to set Adapter============//
    private void setAdapter() {
        if(list.size()>0){
            binding.tvEmptyView.setVisibility(View.GONE);
        }else{
            binding.tvEmptyView.setVisibility(View.VISIBLE);
        }
        adapter=new SelectItemAdapter(context, list);
        binding.rvExistingItems.setAdapter(adapter);
        binding.rvExistingItems.setLayoutManager(new LinearLayoutManager(context));

    }




}
