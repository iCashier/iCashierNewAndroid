package com.icashier.app.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import com.icashier.app.adapter.MealItemsAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.countryPicker.Country;
import com.icashier.app.countryPicker.CountryPickerActivity;
import com.icashier.app.databinding.DialogAddAddressBinding;
import com.icashier.app.databinding.DialogMealItemsBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.AppValidator;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.AddressModel;
import com.icashier.app.model.MenuResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddAdressDialog extends Activity {

    DialogAddAddressBinding binding;
    int flag;
    List<String> countryNames=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.setContentView(this, R.layout.dialog_add_address);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        setData();
        binding.imgDropDown.setVisibility(View.GONE);
        binding.etCountry.setAdapter(getCountryListAdapter(this));
        binding.btnCancel.setOnClickListener(V->{
            finish();
        });


         binding.tvCode.setOnClickListener(V->{
             Intent intent=new Intent(this, CountryPickerActivity.class);
             startActivityForResult(intent,5);
         });

         binding.btnSubmit.setOnClickListener(V->{
             Utilities.hideSoftKeyboard(this);
             if(isInputValid()){

                 AddressModel addressModel=new AddressModel(binding.etName.getText().toString().trim(),
                         binding.etAddress.getText().toString().trim(),binding.etLandmark.getText().toString().trim(),
                         binding.etCity.getText().toString().trim(),binding.etCountry.getText().toString().trim(),
                         binding.etMobile.getText().toString().trim(),binding.tvCode.getText().toString(),flag);

                 Intent intent=new Intent(this,getCallingActivity().getClass());
                 intent.putExtra(AppConstant.FULL_ADDRESS,addressModel);
                 setResult(6,intent);
                 finish();
             }
         });


         binding.imgDropDown.setOnClickListener(V->{countryDropDown();});

    }

    //==============Method to set previous data===========//
    private void setData() {
        AddressModel addressModel=(AddressModel) getIntent().getSerializableExtra(AppConstant.FULL_ADDRESS);
        if(addressModel!=null) {
            binding.etName.setText(addressModel.getName());
            binding.etAddress.setText(addressModel.getAddress());
            binding.etLandmark.setText(addressModel.getLandmark());
            binding.etCity.setText(addressModel.getCity());
            binding.etCountry.setText(addressModel.getCountry());
            binding.tvCode.setText(addressModel.getCode());
            binding.imgFlag.setImageResource(addressModel.getFlag());
            binding.etMobile.setText(addressModel.getPhoneNumber());
        }
    }

    //======================Mehtod to show primary category list popup=======================//
    private void countryDropDown() {


        ListPopupWindow popupWindow = new ListPopupWindow(AddAdressDialog.this);
        ArrayAdapter listPopupAdapter = new ArrayAdapter(AddAdressDialog.this, android.R.layout.simple_spinner_dropdown_item, countryNames);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int index, long id) {

                binding.etCountry.setText(countryNames.get(index));

            }
        });

        popupWindow.setContentWidth(binding.etCountry.getWidth());
        popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setAnchorView(binding.etCountry);
        popupWindow.setAdapter(listPopupAdapter);
        popupWindow.show();

    }
    //=====================Mehtod to validate user input==============//
    private boolean isInputValid() {
        if(!binding.etName.getText().toString().trim().equals("")){
            if(!binding.etAddress.getText().toString().trim().equals("")){
                if(!binding.etLandmark.getText().toString().trim().equals("")){
                    if(!binding.etCity.getText().toString().trim().equals("")){
                        if(!binding.etCountry.getText().toString().trim().equals("")){
                            if(AppValidator.isValidMobilePopup(binding.flParentLayout,this,binding.etMobile))
                            {
                                return true;
                            }
                        }else{
                            AlertUtil.showToastLong(this,getString(R.string.empty_country_name));
                        }
                    }else{
                        AlertUtil.showToastLong(this,getString(R.string.empty_city_name));
                    }
                }else{
                    AlertUtil.showToastLong(this,getString(R.string.empty_landmark));
                }
            }else{
                AlertUtil.showToastLong(this,getString(R.string.empty_address));
            }
        }else{
            AlertUtil.showToastLong(this,getString(R.string.empty_user_name));
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==5){
            String code=data.getStringExtra("COUNTRY_CODE");
            binding.tvCode.setText(code);
            binding.imgFlag.setImageResource(CountryPickerActivity.getFlagByCountryCode(code));
            flag=CountryPickerActivity.getFlagByCountryCode(code);
        }
    }

    private ArrayAdapter<String> getCountryListAdapter(Context context) {
        Country[] country = CountryPickerActivity.COUNTRIES;

        for (int i = 0; i < country.length; i++) {
            countryNames.add((new Locale(context.getResources().getConfiguration().locale.getLanguage(),
                    country[i].getCountryCode()).getDisplayCountry()));
        }
        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, countryNames);
    }



}
