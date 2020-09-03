package com.icashier.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.MealItemsAdapter;
import com.icashier.app.databinding.DialogMealItemsBinding;
import com.icashier.app.model.MenuResponse;

import java.util.ArrayList;
import java.util.List;

public class MealItemsDialog extends Dialog {

    Context context;
    private LayoutInflater inflater;
    DialogMealItemsBinding binding;

    List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean> list;
    MealItemsAdapter adapter;

    public MealItemsDialog(Context context, List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean> list) {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_meal_items, null, false);
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

    }

    //===============Method to set Adapter============//
    private void setAdapter() {
        adapter=new MealItemsAdapter(context, list,true);
        binding.rvItems.setAdapter(adapter);
        binding.rvItems.setLayoutManager(new LinearLayoutManager(context));

    }



}
