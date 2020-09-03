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

import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.CartItemExtrasAdapter;
import com.icashier.app.adapter.ItemExtrasAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.databinding.DialogChooseExtrasBinding;
import com.icashier.app.model.CartItemModel;
import com.icashier.app.model.MenuResponse;

import java.util.List;

public class ChooseExtrasCartDialog extends Dialog {

    HomeActivity context;
    private LayoutInflater inflater;
    DialogChooseExtrasBinding binding;
    List<CartItemModel.ExtrasBean> list;



    public ChooseExtrasCartDialog(HomeActivity context, List<CartItemModel.ExtrasBean> list) {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_choose_extras, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(false);

        if(list.size()==0){
            binding.tvEmptyView.setVisibility(View.VISIBLE);
        }else{
            binding.tvEmptyView.setVisibility(View.GONE);
        }
        //to set adapter
        setAdapter();

        binding.btnDone.setOnClickListener(V->{
            dismiss();
        });
    }

    //========mehtod to set extras adapter==========//
    private void setAdapter() {
        CartItemExtrasAdapter radioAdapter=new CartItemExtrasAdapter(context,list, AppConstant.RADIO_BUTTON);
        binding.rvRadio.setAdapter(radioAdapter);
        binding.rvRadio.setLayoutManager(new LinearLayoutManager(context));

        CartItemExtrasAdapter checkBoxAdapter=new CartItemExtrasAdapter(context,list, AppConstant.CHECK_BOX);
        binding.rvCheckbox.setAdapter(checkBoxAdapter);
        binding.rvCheckbox.setLayoutManager(new LinearLayoutManager(context));

    }


}

