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
import com.icashier.app.databinding.DialogEditTableBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.EditTableDialogListener;
import com.icashier.app.model.TableListResponse;

public class EditTableDialog extends Dialog {

    private HomeActivity context;
    private LayoutInflater inflater;
    private DialogEditTableBinding binding;
    TableListResponse.ResultBean tableData;
    EditTableDialogListener listener;


    public EditTableDialog(HomeActivity context, TableListResponse.ResultBean tableData , EditTableDialogListener listener) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.tableData=tableData;
        this.listener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_edit_table, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);

        binding.etTittle.setText(tableData.getName());
        binding.etTittle.requestFocus(binding.etTittle.getText().length()-1);;

        binding.tvUpdate.setOnClickListener(V->{
            Utilities.hideKeyboardOnPopup(context,binding.etTittle);
            if(isInputValid()) {
                listener.onUpdateClick(tableData);
                dismiss();
            }
        });

        binding.tvCancel.setOnClickListener(V->{
            Utilities.hideKeyboardOnPopup(context,binding.etTittle);
            dismiss();
        });


        binding.getRoot().setOnClickListener(V->{
            Utilities.hideKeyboardOnPopup(context,binding.etTittle);
        });
    }


    //=================Mehtod to validate user input==========//
    private boolean isInputValid()
    {
        if(!binding.etTittle.getText().toString().trim().equals(""))
        {
            tableData.setName(binding.etTittle.getText().toString().trim());
            return true;
        }else
        {
            AlertUtil.toastMsg(context,context.getString(R.string.empty_table_name));
        }
        return false;
    }
}