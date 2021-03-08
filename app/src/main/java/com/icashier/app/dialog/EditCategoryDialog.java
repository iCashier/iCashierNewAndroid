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
import com.icashier.app.databinding.DialogEditCategoryBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.EditCategoryDialogListener;

public class EditCategoryDialog extends Dialog {

    HomeActivity context;
    private LayoutInflater inflater;
    DialogEditCategoryBinding binding;
    EditCategoryDialogListener listener;
    String catName;


    public EditCategoryDialog(HomeActivity context, String catName, EditCategoryDialogListener listener) {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_edit_category, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);
        binding.etTittle.setText(catName);
        binding.etTittle.requestFocus(binding.etTittle.getText().length() - 1);

        binding.tvUpdate.setOnClickListener(V -> {
            Utilities.hideSoftKeyboard(context);
            if (isInputValid()) {
                listener.onUpdateClick(binding.etTittle.getText().toString().trim());
            }
        });

        binding.tvCancel.setOnClickListener(V -> {
            Utilities.hideSoftKeyboard(context);
            dismiss();
        });
    }

    private boolean isInputValid() {
        if (!binding.etTittle.getText().toString().trim().equals("")) {
            return true;
        } else {
            AlertUtil.toastMsg(context, context.getString(R.string.empty_title));
        }

        return false;
    }

}
