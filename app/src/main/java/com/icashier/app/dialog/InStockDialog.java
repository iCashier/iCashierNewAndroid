package com.icashier.app.dialog;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.databinding.DialogInStockBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.EditCategoryDialogListener;

public class InStockDialog extends Dialog {

    HomeActivity context;
    private LayoutInflater inflater;
    DialogInStockBinding binding;
    EditCategoryDialogListener listener;
    String catName;


    public InStockDialog(HomeActivity context, String catName, EditCategoryDialogListener listener) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.catName = catName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_in_stock, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);
        if(!catName.equals("")&&Integer.parseInt(catName)>0){
            binding.etTittle.setText(catName);
        }
        binding.etTittle.requestFocus(binding.etTittle.getText().length() - 1);


        binding.tvUpdate.setOnClickListener(V -> {
            
            Utilities.hideSoftKeyboard(context);
            if(binding.etTittle.getText().toString().trim().equals("")||Float.parseFloat(binding.etTittle.getText().toString().trim())!=0) {
                Utilities.hideKeyboardOnPopup(context,binding.etTittle);
                dismiss();
                listener.onUpdateClick(binding.etTittle.getText().toString().trim());
            }else{
                Utilities.hideKeyboardOnPopup(context,binding.etTittle);
                AlertUtil.toastMsg(context,context.getString(R.string.invalid_qty));
            }

        });

        binding.tvCancel.setOnClickListener(V -> {
            Utilities.hideKeyboardOnPopup(context,binding.etTittle);

            dismiss();
        });
    }



}
