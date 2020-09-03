package com.icashier.app.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.SearchCategoryAdapter;
import com.icashier.app.adapter.TransactionsAdapter;
import com.icashier.app.databinding.DialogEditCategoryBinding;
import com.icashier.app.databinding.DialogInStockBinding;
import com.icashier.app.databinding.DialogSearchCategoryBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CatSelectedListener;
import com.icashier.app.listener.EditCategoryDialogListener;
import com.icashier.app.listener.RefundStatusListener;
import com.icashier.app.model.PredefinedCategoriesResponse;

import java.util.List;

public class SearchCategoryDialog extends Dialog {

    HomeActivity context;
    private LayoutInflater inflater;
    DialogSearchCategoryBinding binding;
    CatSelectedListener listener;
    SearchCategoryAdapter adapter;
    List<PredefinedCategoriesResponse.ResultBean> list;


    public SearchCategoryDialog(HomeActivity context, List<PredefinedCategoriesResponse.ResultBean> list, CatSelectedListener listener) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.list=list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_search_category, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);
        binding.etTittle.requestFocus(binding.etTittle.getText().length() - 1);
        binding.flClose.setOnClickListener(V->{
            dismiss();
        });
        setAdapter();

        binding.etTittle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.getFilter().filter(editable.toString());
            }
        });
    }

    private void setAdapter() {
        adapter=new SearchCategoryAdapter(context, list, listener);
        binding.rvCatgory.setLayoutManager(new LinearLayoutManager(context));
        binding.rvCatgory.setAdapter(adapter);
    }

}
