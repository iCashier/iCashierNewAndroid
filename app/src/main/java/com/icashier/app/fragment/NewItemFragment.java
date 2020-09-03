package com.icashier.app.fragment;


import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.AllergiesAdapter;
import com.icashier.app.adapter.CheckBoxItemAdapter;
import com.icashier.app.adapter.ItemSizeAdapter;
import com.icashier.app.adapter.RadioItemAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentNewItemBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.ImagePickerUtil;
import com.icashier.app.helper.PermissionsUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.listener.ItemSizeListener;
import com.icashier.app.model.AllergiesModel;
import com.icashier.app.model.CategoryListReponse;
import com.icashier.app.model.ExtrasListResponse;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.ItemPriceModel;
import com.icashier.app.model.ItemSizesResponse;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class NewItemFragment extends Fragment {

    HomeActivity context;
    FragmentNewItemBinding binding;
    RestClient.ApiRequest apiRequest;
    List<ExtrasListResponse.ResultBean> extrasList = new ArrayList<>();
    List<ExtrasListResponse.ResultBean> checkboxList = new ArrayList<>();
    List<ExtrasListResponse.ResultBean> radioList = new ArrayList<>();
    List<AllergiesModel.Result> allergiesList=new ArrayList<>();
    ItemSizeAdapter itemSizeAdapter;
    List<CategoryListReponse.ResultBean> categoryList = new ArrayList<>();
    List<String> primaryList = new ArrayList<>();
    List<String> secondaryList = new ArrayList<>();
    List<String> sizesNameList = new ArrayList<>();
    List<String> addedSizeList = new ArrayList<>();
    List<ItemPriceModel> sizeData = new ArrayList<>();
    String percentDiscount = "", priceDiscount = "";
    String primaryCategory = "", secondaryCategory = "", itemPrice = "", itemSize = "";
    List<ItemPriceModel> itemPriceList = new ArrayList<>();
    int position;
    File imgItem = null;
    Double minPrice=0.00;
    boolean priceSelected, percentSelected;
    AllergiesAdapter allergiesAdapter;
    private static DecimalFormat df2 = new DecimalFormat("#.##");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        context=(HomeActivity)getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_item, container, false);

        callGetExtrasListApi();
        callGetItemSizeApi();
        callGetCategoryListApi();
        setOnClickListener();
        setAllergiesAdapter();
        callGetAllergiesListApi();
        binding.tvPercent.setSelected(true);
        setSeekBarListener();
        return binding.getRoot();
    }

    /*
    Mehthod to allergies list adapter
     */
    private void setAllergiesAdapter() {
        allergiesAdapter=new AllergiesAdapter(context,allergiesList);
        binding.rvAllergies.setAdapter(allergiesAdapter);
        binding.rvAllergies.setLayoutManager(new GridLayoutManager(context,5));
    }

    private void setSeekBarListener() {
        binding.seekbarPercent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.tvProgess.setText(""+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.seekbarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.tvProgess.setText(""+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //==============Method to get max price==============//
    public void getMinPrice() {
        EditText etPrice;
        int childCount = binding.rvSizes.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (binding.rvSizes.findViewHolderForLayoutPosition(i) instanceof ItemSizeAdapter.ViewHolder) {
                ItemSizeAdapter.ViewHolder childHolder = (ItemSizeAdapter.ViewHolder) binding.rvSizes.findViewHolderForLayoutPosition(i);

                etPrice = childHolder.itemView.findViewById(R.id.etPrice);
                if (!etPrice.getText().toString().trim().equals("")) {
                    Double price = Double.valueOf(etPrice.getText().toString().trim());
                    if (i == 0) {
                        minPrice = price;
                    }
                    if (minPrice > price) {
                        minPrice = price;
                    }
                }
            }
        }


    }

    //===================Method to set size list adapter================//
    private void setSizeAdapter() {

        if(sizesNameList.size()>0){
            sizeData.add(new ItemPriceModel(sizesNameList.get(0), ""));
            if(sizesNameList.get(0).equalsIgnoreCase(AppConstant.DEFAULT_SIZE)&&Utilities.isArabic()){
                addedSizeList.add(getString(R.string.default_size));

            }else{
                addedSizeList.add(sizesNameList.get(0));
            }
        }else{
            sizeData.add(new ItemPriceModel("", ""));

        }
        itemSizeAdapter = new ItemSizeAdapter(context, sizesNameList, addedSizeList, sizeData, binding.clParentLayout, new ItemSizeListener() {
            @Override
            public void onAddClick(int position,String size) {
                addSizeToList(-1);
                sizeData.add(new ItemPriceModel(size, ""));
                itemSizeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDeleteClick(int position) {
                sizeData.remove(position);
                itemSizeAdapter.notifyDataSetChanged();
                //sizeData.remove(position);
                addSizeToList(position);
            }
        });
        binding.rvSizes.setLayoutManager(new LinearLayoutManager(context));
        binding.rvSizes.setAdapter(itemSizeAdapter);
    }

    //==============Method to validate user input=============//
    private boolean isInputValid() {
        if (!binding.etName.getText().toString().trim().equals("")) {
            if (binding.etQuantity.getText().toString().trim().equals("")||Float.parseFloat(binding.etQuantity.getText().toString().trim())!=0) {
                if(!binding.etCalories.getText().toString().trim().equals("")) {
                    if (!binding.tvPrimary.getText().toString().equals("")) {
                        if (!binding.tvSecondary.getText().toString().equals("")||true) {
                            if (imgItem != null) {
                                getAllEditTextValues();
                            } else {
                                AlertUtil.toastMsg(context, context.getString(R.string.empty_item_image));
                            }
                        } else {
                            AlertUtil.toastMsg(context, context.getString(R.string.empty_secondary_category));
                        }
                    } else {
                        AlertUtil.toastMsg(context, context.getString(R.string.empty_primary_category));
                    }
                }else{
                    AlertUtil.toastMsg(context, getString(R.string.empty_calories));
                }
            } else {
                AlertUtil.toastMsg(context, context.getString(R.string.invalid_qty));

            }
        } else {
            AlertUtil.toastMsg(context, context.getString(R.string.empty_name));
        }
        return false;
    }


    //=========Mehtod to get data from recycler view views==========//
    public void addSizeToList(int position) {
        sizeData.clear();
        TextView tvSize;
        EditText etPrice;
        int childCount = binding.rvSizes.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (binding.rvSizes.findViewHolderForLayoutPosition(i) instanceof ItemSizeAdapter.ViewHolder) {
                ItemSizeAdapter.ViewHolder childHolder = (ItemSizeAdapter.ViewHolder) binding.rvSizes.findViewHolderForLayoutPosition(i);

                tvSize = childHolder.itemView.findViewById(R.id.tvSize);
                etPrice = childHolder.itemView.findViewById(R.id.etPrice);

                if (position != i) {

                    if (!tvSize.getText().toString().equals("")) {
                        if (!etPrice.getText().toString().equals("")) {
                            sizeData.add(new ItemPriceModel(tvSize.getText().toString(), etPrice.getText().toString()));
                        } else {
                            sizeData.add(new ItemPriceModel(tvSize.getText().toString(), ""));

                        }
                    } else {
                        sizeData.add(new ItemPriceModel("", ""));
                    }
                }
            }
        }

        itemSizeAdapter.notifyDataSetChanged();

    }


    //=========Mehtod to get data from recycler view views==========//
    public void getAllEditTextValues() {
        itemPriceList.clear();
        TextView tvSize;
        EditText etPrice;
        boolean isValid = false;
        int childCount = binding.rvSizes.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (binding.rvSizes.findViewHolderForLayoutPosition(i) instanceof ItemSizeAdapter.ViewHolder) {
                ItemSizeAdapter.ViewHolder childHolder = (ItemSizeAdapter.ViewHolder) binding.rvSizes.findViewHolderForLayoutPosition(i);

                tvSize = childHolder.itemView.findViewById(R.id.tvSize);
                etPrice = childHolder.itemView.findViewById(R.id.etPrice);

                if (!tvSize.getText().toString().equals("")) {
                    if (!etPrice.getText().toString().equals("")) {
                        if(tvSize.getText().toString().equalsIgnoreCase(getString(R.string.default_size))){
                            itemPriceList.add(new ItemPriceModel(AppConstant.DEFAULT_SIZE, etPrice.getText().toString()));

                        }else{
                            itemPriceList.add(new ItemPriceModel(tvSize.getText().toString(), etPrice.getText().toString()));
                        }
                        isValid = true;
                    } else {
                        AlertUtil.toastMsg(context, context.getString(R.string.empty_price));
                        isValid = false;
                        break;
                    }
                }
            }
        }
        if (isValid) {
            Collections.sort(itemPriceList);
            callSaveItemApi();
        }

    }

    //===================Method to set onClick listener================//
    private void setOnClickListener() {
        binding.imgCamera1.setOnClickListener(V -> {
            openImagePicker();
        });

        binding.imgLogo.setOnClickListener(V->{
            openImagePicker();
        });

        binding.clPrimary.setOnClickListener(V -> {
            showPrimaryCategoryPopup();
        });

        binding.clSecondary.setOnClickListener(V -> {
            showSecondaryCategoryPopup();
        });

        binding.flSave.setOnClickListener(V -> {
            isInputValid();
        });

        binding.tvPercent.setOnClickListener(V -> {
            binding.tvPercent.setSelected(true);
            binding.tvDollar.setSelected(false);
            binding.tvMax.setText("100");
            priceDiscount = "";
            priceSelected=false;
            percentSelected=true;
            binding.seekbarPrice.setVisibility(View.GONE);
            binding.seekbarPrice.setProgress(0);
            binding.seekbarPercent.setVisibility(View.VISIBLE);

        });
        binding.tvDollar.setOnClickListener(V -> {
            getMinPrice();
            binding.tvDollar.setSelected(true);
            binding.tvPercent.setSelected(false);
            percentDiscount = "";
            priceSelected=true;
            percentSelected=false;
            binding.seekbarPrice.setVisibility(View.VISIBLE);
            binding.seekbarPercent.setVisibility(View.GONE);
            binding.seekbarPercent.setProgress(0);
            binding.tvMax.setText("" + df2.format(minPrice));
            double temp=Double.parseDouble(df2.format(minPrice));
            //String split[]=df2.format(minPrice).split(".");
            binding.seekbarPrice.setMax((int)Math.round(temp));

        });

        binding.scrollView.setOnClickListener(V -> {
            Utilities.hideSoftKeyboard(context);
        });
    }

    //==========================Method to call get extras  api=====================//
    private void callGetExtrasListApi() {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            //AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();


            HashMap<String,String> headers=new HashMap<>();
            headers.put("token",SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));
            headers.put("type",SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE,AppConstant.TYPE_MERCHANT));
            headers.put("cashiertoken",SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN,""));
            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_EXTRAS_LIST)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeaders(headers)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    ExtrasListResponse extrasListResponse = new Gson().fromJson(response, ExtrasListResponse.class);
                                    if (extrasListResponse != null) {
                                        AlertUtil.hideProgressDialog();
                                        if (extrasListResponse.getCode() == 200) {

                                            extrasList.clear();
                                            extrasList.addAll(extrasListResponse.getResult());
                                           /* if (extrasList.size() > 0) {
                                                binding.clExtras.setVisibility(View.VISIBLE);
                                            } else {
                                                binding.clExtras.setVisibility(View.GONE);
                                            }*/
                                            filterExtrasList();
                                        } else if(extrasListResponse.getCode()==301)
                                        {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                    callGetExtrasListApi();
                                                }
                                            });
                                        }

                                    } else {
                                        AlertUtil.hideProgressDialog();
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }
                                }
                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            AlertUtil.hideProgressDialog();
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        } else {
            AlertUtil.toastMsg(context, getString(R.string.network_error));
        }
    }

    //==========================Method to call get sizes  api=====================//
    private void callGetItemSizeApi() {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            //AlertUtil.showProgressDialog(context);

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_ITEM_SIZE)
                    .setMethod(RestClient.ApiRequest.METHOD_GET)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    ItemSizesResponse itemSizesResponse = new Gson().fromJson(response, ItemSizesResponse.class);
                                    if (itemSizesResponse != null) {
                                        AlertUtil.hideProgressDialog();
                                        if (itemSizesResponse.getStatusCode() == 200) {
                                            for (int i = 0; i < itemSizesResponse.getResult().size(); i++) {
                                                sizesNameList.add(itemSizesResponse.getResult().get(i).getSize());
                                            }
                                            setSizeAdapter();
                                        } else if(itemSizesResponse.getStatusCode()==301)
                                        {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                    callGetItemSizeApi();
                                                }
                                            });
                                        }
                                    } else {
                                        AlertUtil.hideProgressDialog();
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }
                                }
                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            AlertUtil.hideProgressDialog();
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        } else {
            AlertUtil.toastMsg(context, getString(R.string.network_error));
        }


    }


    //==========================Method to call get allergies  api=====================//
    private void callGetAllergiesListApi() {

        if (Utilities.isNetworkAvailable(context)) {


            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_ALLEGIES)
                    .setMethod(RestClient.ApiRequest.METHOD_GET)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    AllergiesModel allergiesModel = new Gson().fromJson(response, AllergiesModel.class);
                                    if (allergiesModel != null) {
                                        if (allergiesModel.getCode() == 200) {

                                            allergiesList.clear();
                                            allergiesList.addAll(allergiesModel.getResult());
                                            allergiesAdapter.notifyDataSetChanged();
                                        } else if(allergiesModel.getCode()==301)
                                        {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                    callGetExtrasListApi();
                                                }
                                            });
                                        }

                                    } else {
                                        AlertUtil.hideProgressDialog();
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }
                                }
                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            AlertUtil.hideProgressDialog();
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        } else {
            AlertUtil.toastMsg(context, getString(R.string.network_error));
        }
    }

    //==================Mehtod to divide radio item and checkbox item ==================//
    private void filterExtrasList() {

        radioList.clear();
        checkboxList.clear();
        for (ExtrasListResponse.ResultBean data : extrasList) {
            data.setChecked(false);
            if (data.getType().equals(AppConstant.RADIO_BUTTON)) {
                radioList.add(data);
            } else if (data.getType().equals(AppConstant.CHECK_BOX)) {
                checkboxList.add(data);
            }
        }
        setCheckboxAdapter();
        setRadioAdapter();
    }

    //===============Mehtod to set adapter for checkbox extras==============//
    private void setCheckboxAdapter() {
        CheckBoxItemAdapter adapter = new CheckBoxItemAdapter(context, checkboxList);
        binding.rvCheckbox.setAdapter(adapter);
        binding.rvCheckbox.setLayoutManager(new LinearLayoutManager(context));
    }

    //===============Mehtod to set adapter for radio button extras==============//
    private void setRadioAdapter() {
        RadioItemAdapter adapter = new RadioItemAdapter(context, radioList);
        binding.rvRadio.setAdapter(adapter);
        binding.rvRadio.setLayoutManager(new LinearLayoutManager(context));
    }

    //============Method to pick image from camera or gallery========================//
    private void openImagePicker() {
        //to ask permissions at runtime
        PermissionsUtil.askPermissions(context, PermissionsUtil.CAMERA, PermissionsUtil.STORAGE, new PermissionsUtil.PermissionListener() {
            @Override
            public void onPermissionResult(boolean isGranted) {
                if (isGranted) {
                    ImagePickerUtil.selectImage(context, new ImagePickerUtil.ImagePickerListener() {
                        @Override
                        public void onImagePicked(File imageFile, String tag) {
                            binding.imgLogo.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
                            imgItem = imageFile;
                        }
                    }, "img" + new Random().nextInt(), false);
                }

            }
        });

    }

    //==========================Method to call get category icons api=====================//
    private void callGetCategoryListApi() {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            //AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            apiRequest = new RestClient.ApiRequest(context);


            HashMap<String,String> headers=new HashMap<>();
            headers.put("token",SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));
            headers.put("type",SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE,AppConstant.TYPE_MERCHANT));
            headers.put("cashiertoken",SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN,""));

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_CATEGORY_LIST)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeaders(headers)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                CategoryListReponse categoryListReponse = new Gson().fromJson(response, CategoryListReponse.class);
                                if (categoryListReponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (categoryListReponse.getCode() == 200) {
                                        categoryList.clear();
                                        categoryList.addAll(categoryListReponse.getResult());
                                        for (int i = 0; i < categoryList.size(); i++) {
                                            if (Locale.getDefault().equals(new Locale("ar","MA"))) {
                                                if(categoryList.get(i).getArabic_name()!=null&&!categoryList.get(i).getArabic_name().equals("")){
                                                    primaryList.add(categoryList.get(i).getArabic_name());
                                                }else{
                                                    primaryList.add(categoryList.get(i).getName());

                                                }
                                            }else{
                                                primaryList.add(categoryList.get(i).getName());

                                            }
                                        }

                                    } else if(categoryListReponse.getCode()==301)
                                    {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callGetCategoryListApi();
                                            }
                                        });
                                    }
                                } else {
                                    AlertUtil.hideProgressDialog();
                                    AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                }
                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            AlertUtil.hideProgressDialog();
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        } else {
            AlertUtil.toastMsg(context, getString(R.string.network_error));
        }
    }

    //======================Mehtod to show primary category list popup=======================//
    private void showPrimaryCategoryPopup() {

        ArrayAdapter listPopupAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, primaryList);

        ListPopupWindow popupWindow = new ListPopupWindow(context);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int index, long id) {

                binding.tvPrimary.setText(primaryList.get(index));
                primaryCategory = "" + categoryList.get(index).getId();
                binding.tvSecondary.setText("");
                secondaryCategory = "";
                position = index;
                secondaryList.clear();
                for (int i = 1; i < categoryList.get(index).getSubCategories().size(); i++) {
                    secondaryList.add(categoryList.get(index).getSubCategories().get(i).getName());
                }
                popupWindow.dismiss();
            }
        });

        popupWindow.setContentWidth(binding.clPrimary.getWidth());
        popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setAnchorView(binding.clPrimary);
        popupWindow.setAdapter(listPopupAdapter);
        popupWindow.show();

    }

    //======================Mehtod to show primary category list popup=======================//
    private void showSecondaryCategoryPopup() {

        ArrayAdapter listPopupAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, secondaryList);

        ListPopupWindow popupWindow = new ListPopupWindow(context);

        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int index, long id) {

                binding.tvSecondary.setText(secondaryList.get(index));
                secondaryCategory = "" + categoryList.get(position).getSubCategories().get(index).getId();
                popupWindow.dismiss();
            }
        });

        popupWindow.setContentWidth(binding.clSecondary.getWidth());
        popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setAnchorView(binding.clSecondary);
        popupWindow.setAdapter(listPopupAdapter);
        popupWindow.show();

    }

    //==========================Method to call get category icons api=====================//
    private void callSaveItemApi() {


        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            AlertUtil.showProgressDialog(context);

            String allergies="";
            for(int i=0;i<allergiesList.size();i++){

                if(allergiesList.get(i).isSelected()){
                    if(allergies.equals("")){
                        allergies+=allergiesList.get(i).getId();
                    }else{
                        allergies+=","+allergiesList.get(i).getId();

                    }
                }
            }

            if(!priceSelected){
                if(binding.seekbarPercent.getProgress()>0){
                    percentSelected=true;
                }
            }

            if(priceSelected){
                priceDiscount=""+binding.seekbarPrice.getProgress();
                percentDiscount="";
            }else if(percentSelected){
                percentDiscount=""+binding.seekbarPercent.getProgress();
                priceDiscount="";
            }
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < radioList.size(); i++) {
                if (radioList.get(i).isChecked()) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.accumulate("id", radioList.get(i).getId());
                        jsonObject.accumulate("title", radioList.get(i).getTitle());
                        jsonObject.accumulate("type", radioList.get(i).getType());
                        jsonObject.accumulate("price", radioList.get(i).getPrice());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
            }
            for (int i = 0; i < checkboxList.size(); i++) {
                if (checkboxList.get(i).isChecked()) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.accumulate("id", checkboxList.get(i).getId());
                        jsonObject.accumulate("title", checkboxList.get(i).getTitle());
                        jsonObject.accumulate("type", checkboxList.get(i).getType());
                        jsonObject.accumulate("price", checkboxList.get(i).getPrice());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
            }

            itemPrice = "";
            itemSize = "";
            for (ItemPriceModel itemPriceModel : itemPriceList) {
                if (itemPrice.equals("")) {
                    itemPrice = itemPriceModel.getPrice();
                } else {
                    itemPrice = itemPrice + "," + itemPriceModel.getPrice();
                }

                if (itemSize.equals("")) {
                    itemSize = itemPriceModel.getSize();
                } else {
                    itemSize = itemSize + "," + itemPriceModel.getSize();
                }
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("name", binding.etName.getText().toString().trim());
            params.put("price", itemPrice);
            params.put("size", itemSize);
            params.put("sale_price", priceDiscount);
            params.put("sale_percent", percentDiscount);
            if(binding.etQuantity.getText().toString().trim().equals("")){
                params.put("qty","-1");

            }else{
                params.put("qty", binding.etQuantity.getText().toString().trim());

            }
            params.put("extras", jsonArray.toString());
            params.put("about", binding.etAbout.getText().toString());
            params.put("category", primaryCategory);
            params.put("calories",binding.etCalories.getText().toString().trim());

            if(secondaryCategory.equals("")){
                secondaryCategory=""+categoryList.get(position).getSubCategories().get(0).getId();
            }
            params.put("sub_category", secondaryCategory);
            params.put("allergies",allergies);


            HashMap<String, File> fileHashMap = new HashMap<>();
            if (imgItem != null)
                fileHashMap.put("image", imgItem);


            HashMap<String,String> headers=new HashMap<>();
            headers.put("token",SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));
            headers.put("type",SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE,AppConstant.TYPE_MERCHANT));
            headers.put("cashiertoken",SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN,""));

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.SAVE_ITEM)
                    .setMethod(RestClient.ApiRequest.METHOD_MULTIPART)
                    .setParams(params)
                    .setFileParams(fileHashMap)
                    .setHeaders(headers)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                                if (genericResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (genericResponse.getCode() == 200) {
                                        AlertUtil.toastMsg(context, genericResponse.getMessage());
                                        resetUi();
                                    } else if(genericResponse.getCode()==301)
                                    {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callSaveItemApi();
                                            }
                                        });
                                    }
                                } else {
                                    AlertUtil.hideProgressDialog();
                                    AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                }
                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            AlertUtil.hideProgressDialog();
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        } else {
            AlertUtil.toastMsg(context, getString(R.string.network_error));
        }
    }

    private void resetUi() {
        itemPrice = "";
        itemSize = "";
        position = 0;
        imgItem = null;
        sizeData.clear();
        sizeData.add(new ItemPriceModel("", ""));
        itemSizeAdapter.notifyDataSetChanged();
        binding.imgLogo.setImageResource(R.drawable.placeholder_logo);
        binding.seekbarPercent.setProgress(0);
        binding.seekbarPrice.setProgress(0);
        filterExtrasList();
        binding.etAbout.setText("");
        binding.etQuantity.setText("");
        binding.etName.setText("");
        addedSizeList.clear();
        binding.etCalories.setText("");
        binding.tvPrimary.setText("");
        binding.tvSecondary.setText("");
        primaryCategory="";
        secondaryCategory="";
        for(AllergiesModel.Result allergy:allergiesList){
            allergy.setSelected(false);
        }
        allergiesAdapter.notifyDataSetChanged();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtil.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}


