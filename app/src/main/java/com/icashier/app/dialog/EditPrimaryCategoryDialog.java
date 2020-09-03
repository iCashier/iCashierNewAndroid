package com.icashier.app.dialog;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.databinding.DialogEditPrimaryCatBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.EditCategoryDialogListener;

import java.util.List;
import java.util.Locale;

public class EditPrimaryCategoryDialog extends Dialog {

    HomeActivity context;
    private LayoutInflater inflater;
    DialogEditPrimaryCatBinding binding;
    UpdateCategory updateListener;
    String catName,arabicName;
    List<String> nameList;
    int position;



    public EditPrimaryCategoryDialog(HomeActivity context, String catName, String arabicName,List<String> nameList,EditPrimaryCategoryDialog.UpdateCategory updateListener) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.catName = catName;
        this.nameList=nameList;
        this.arabicName=arabicName;
        this.updateListener=updateListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_edit_primary_cat, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);

        if (Locale.getDefault().equals(new Locale("ar","MA"))) {
            binding.tvPrimaryCategroy.setText(arabicName);
        }else{
            binding.tvPrimaryCategroy.setText(catName);

        }


        binding.tvUpdate.setOnClickListener(V -> {
            Utilities.hideSoftKeyboard(context);
            if (isInputValid()) {
                updateListener.onUpdateClick(position);
            }
        });

        binding.tvCancel.setOnClickListener(V -> {
            Utilities.hideSoftKeyboard(context);
            dismiss();
        });

        binding.tvPrimaryCategroy.setOnClickListener(V->{
            showPredefinedCategoryPopup();
        });
    }

    private boolean isInputValid() {
        if (!binding.tvPrimaryCategroy.getText().toString().trim().equals("")) {
            return true;
        } else {
            AlertUtil.toastMsg(context, context.getString(R.string.empty_title));
        }

        return false;
    }

    //======================Mehtod to show primary category list popup=======================//
    private void showPredefinedCategoryPopup() {

        ArrayAdapter listPopupAdapter2 = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item,nameList);

        ListPopupWindow popupWindow=new ListPopupWindow(context);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int index, long id) {

                binding.tvPrimaryCategroy.setText(nameList.get(index));
                position=index;
                popupWindow.dismiss();
            }
        });

        popupWindow.setContentWidth(binding.tvPrimaryCategroy.getWidth());
        popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setAnchorView(binding.tvPrimaryCategroy);
        popupWindow.setAdapter(listPopupAdapter2);
        popupWindow.show();

    }

    public interface UpdateCategory{
        void onUpdateClick(int index);
    }
}
