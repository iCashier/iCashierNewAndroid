package com.icashier.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.OutletListAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.databinding.DialogSelectOutletBinding;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.SigninResponse;

import java.util.List;

public class SelectOutletDialog extends Dialog {
    Context context;
    List<SigninResponse.ResultBean> list;
    DialogSelectOutletBinding binding;
    LayoutInflater inflater;
    public SelectOutletDialog(Context context, List<SigninResponse.ResultBean> list){
        super(context);
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_select_outlet, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        setAdapter();

        binding.btnView.setOnClickListener(V->{
            for(int i=0;i<list.size();i++){
                if(list.get(i).isSelected()){
                    SharedPrefManager.getInstance(context).saveString(AppConstant.USER_INFO, new Gson().toJson(list.get(i)));
                    SharedPrefManager.getInstance(context).saveString(AppConstant.KEY_LOGIN_USER_ID, list.get(i).getUid());
                    SharedPrefManager.getInstance(context).saveString(AppConstant.ACCESS_TOKEN,list.get(i).getToken());
                    SharedPrefManager.getInstance(context).saveString(AppConstant.USER_TYPE, AppConstant.TYPE_MERCHANT);
                    SharedPrefManager.getInstance(context).saveInt(AppConstant.IS_PARENT,list.get(i).getIsParent());

                    //Clears previous activities
                    Utilities.clearAllgoToActivity(context, HomeActivity.class);
                }
            }
        });

        binding.btnCancel.setOnClickListener(V->{
            dismiss();
        });

    }

    private void setAdapter() {
        OutletListAdapter adapter=new OutletListAdapter(context,list);
        binding.rvOutlets.setAdapter(adapter);
        binding.rvOutlets.setLayoutManager(new LinearLayoutManager(context));
    }
}
