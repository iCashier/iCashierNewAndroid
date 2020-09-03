package com.icashier.app.dialog;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.databinding.DialogEditExtrasBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.DecimalDigitsInputFilter;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.EditExtrasDialogListener;
import com.icashier.app.model.ExtrasListResponse;

public class EditExtrasDialog extends Dialog {

    private HomeActivity context;
    private LayoutInflater inflater;
    private DialogEditExtrasBinding binding;
    private EditExtrasDialogListener listener;
    private ExtrasListResponse.ResultBean extrasData;


    public EditExtrasDialog(HomeActivity context, ExtrasListResponse.ResultBean extrasData, EditExtrasDialogListener listener) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener=listener;
        this.extrasData=extrasData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_edit_extras, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);

        binding.etPrice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});

        binding.etTittle.setText(extrasData.getTitle());
        binding.etTittle.requestFocus(binding.etTittle.getText().length()-1);;

        if(extrasData.getType().equals("radiobutton")){
            binding.tvType.setText(context.getString(R.string.radio_button));
        }else
        {
            binding.tvType.setText(context.getString(R.string.check_box));

        }

        binding.etPrice.setText(extrasData.getPrice());
        binding.tvUpdate.setOnClickListener(V->{
            Utilities.hideKeyboardOnPopup(context,binding.etPrice);
            if(isInputValid()) {
                listener.onUpdateClick(extrasData);
                dismiss();
            }
        });

        binding.tvCancel.setOnClickListener(V->{
            Utilities.hideSoftKeyboard(context);
            dismiss();
        });

        binding.tvType.setOnClickListener(V->{
            showDropDown();
        });
        binding.getRoot().setOnClickListener(V->{
            Utilities.hideSoftKeyboard(context);
        });
    }

    //================MEthod to show drop down===================//
    private void showDropDown() {
        Utilities.hideKeyboardOnPopup(context,binding.etPrice);
        String list[]=new String[]{context.getString(R.string.radio_button),context.getString(R.string.check_box)};
        ArrayAdapter listPopupAdapter=new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item,list);
        ListPopupWindow popupWindow=new ListPopupWindow(context);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int index, long id) {

                binding.tvType.setText(list[index]);
                if(index==0){
                    extrasData.setType("radiobutton");
                }else {
                    extrasData.setType("checkbox");
                }
                popupWindow.dismiss();
            }
        });

        popupWindow.setContentWidth(binding.tvType.getWidth());
        popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setAnchorView(binding.tvType);
        popupWindow.setAdapter(listPopupAdapter);
        popupWindow.show();

    }

    private boolean isInputValid()
    {
        if(!binding.etTittle.getText().toString().trim().equals(""))
        {
            extrasData.setTitle(binding.etTittle.getText().toString().trim());
            if(!binding.etPrice.getText().toString().trim().equals(""))
            {
                extrasData.setPrice(binding.etPrice.getText().toString().trim());
                return true;
            }
            else
            {
                AlertUtil.toastMsg(context,context.getString(R.string.empty_price));

            }
        }else
        {
            AlertUtil.toastMsg(context,context.getString(R.string.empty_title));
        }
        return false;
    }
}