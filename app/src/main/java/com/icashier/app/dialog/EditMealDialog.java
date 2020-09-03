package com.icashier.app.dialog;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.MealItemChipAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.DialogEditMealBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.DecimalDigitsInputFilter;
import com.icashier.app.helper.ImagePickerUtil;
import com.icashier.app.helper.PermissionsUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.listener.EditMealDialogListener;
import com.icashier.app.listener.MealChipListener;
import com.icashier.app.listener.SelectItemListener;
import com.icashier.app.model.ExistingItemList;
import com.icashier.app.model.MealListResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class EditMealDialog extends Dialog {

    private HomeActivity context;
    private LayoutInflater inflater;
    private DialogEditMealBinding binding;
    RestClient.ApiRequest apiRequest;
    List<ExistingItemList.ResultBean> itemsList=new ArrayList<>();
    MealListResponse.ResultBean mealData;
    MealItemChipAdapter chipAdapter;
    String itemIds="",itemPrices="",itemSizes="";
    List<MealListResponse.ResultBean> mealList=new ArrayList<>();
    EditMealDialogListener listener;
    File imgMeal;



    public EditMealDialog(HomeActivity context,MealListResponse.ResultBean mealData,EditMealDialogListener listener) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.mealData=mealData;
        this.listener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_edit_meal, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);
        callGetItemListApi();

        binding.etPrice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});



        binding.imgLogo.setOnClickListener(V->{
            openImagePicker();
        });
        binding.imgCamera1.setOnClickListener(V->{
            openImagePicker();
        });
        binding.imgAddItem.setOnClickListener(V->{
            new SelectItemsDialog(context, itemsList, new SelectItemListener() {
                @Override
                public void onAddClicked() {

                    chipAdapter.notifyDataSetChanged();
                    setPrice();
                }

            }).show();
        });
        binding.tvUpdate.setOnClickListener(V->{
            Utilities.hideKeyboardOnPopup(context,binding.etPrice);
            if(isValidInput()) {
                callEditMealApi();
            }
        });

        binding.tvCancel.setOnClickListener(V->{
            Utilities.hideKeyboardOnPopup(context,binding.etPrice);
            dismiss();
        });


        binding.getRoot().setOnClickListener(V->{
            Utilities.hideKeyboardOnPopup(context,binding.etPrice);
        });
    }

    private void setData()
    {
        binding.etTittle.setText(mealData.getTitle());
        binding.etPrice.setText(mealData.getPrice());
        for(int i=0;i<itemsList.size();i++){
            for(int j=0;j<mealData.getItems().size();j++) {
                if(mealData.getItems().get(j).getId()==itemsList.get(i).getId()){
                    itemsList.get(i).setSelected(true);
                    itemsList.get(i).setSelectedPrice(mealData.getItems().get(j).getSelected_price());
                    itemsList.get(i).setSelectedSize(mealData.getItems().get(j).getSelected_size());
                    break;

                }
            }
        }
        if(mealData.getImage()!=null||!mealData.getImage().equals("")){
            Utilities.setCategoryImage(context,binding.imgLogo,mealData.getImage());
        }

        setChipAdapter();
        setPrice();

    }
    //===============Method to set total price===========//
    private void setPrice()
    {
        float totalPrice=0;
        for(int i=0;i<itemsList.size();i++){
            if(itemsList.get(i).isSelected()){

                 totalPrice=totalPrice+ Float.parseFloat(itemsList.get(i).getSelectedPrice());
            }
        }
        binding.tvTotalPrice.setVisibility(View.VISIBLE);
        binding.tvTotalPrice.setText(context.getString(R.string.total_price_of_selected_items)+" "+totalPrice);
    }

    private void setChipAdapter() {
        chipAdapter=new MealItemChipAdapter(context, itemsList, new MealChipListener() {
            @Override
            public void onDeleteClick(int position) {
                setPrice();
            }
        },true);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        binding.rvItems.setLayoutManager(layoutManager);
        binding.rvItems.setAdapter(chipAdapter);
    }
    //=============Mehtod to validate user input===========//
    private boolean isValidInput(){
        if(!binding.etTittle.getText().toString().equals("")){
            if(!binding.etPrice.getText().toString().equals("")){
                boolean isSelected=false;
                for(int i=0;i<itemsList.size();i++){
                    if(itemsList.get(i).isSelected()){
                        isSelected=true;
                    }
                }
                if(isSelected){
                    return true;
                }else{
                    AlertUtil.toastMsg(context,context.getString(R.string.empty_meal_items));
                }

            }else{
                AlertUtil.toastMsg(context,context.getString(R.string.empty_price));
            }
        }else{
            AlertUtil.toastMsg(context,context.getString(R.string.empty_title));
        }
        return false;
    }

    //==========================Method to call get existing items  api=====================//
    private void callGetItemListApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboardOnPopup(context,binding.etPrice);
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> params = new HashMap<>();
            params.put("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));


            HashMap<String,String> headers=new HashMap<>();
            headers.put("token",SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));
            headers.put("type",SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE,AppConstant.TYPE_MERCHANT));
            headers.put("cashiertoken",SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN,""));
            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_ITEM_LIST)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeaders(headers)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    ExistingItemList existingItemList= new Gson().fromJson(response, ExistingItemList.class);
                                    if (existingItemList != null) {
                                        if (existingItemList.getCode()==200 ) {
                                            itemsList.addAll(existingItemList.getResult());
                                            setData();
                                        }else if(existingItemList.getCode()==301)
                                        {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                    callGetItemListApi();
                                                }
                                            });
                                        }

                                    } else {
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }
                                }
                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            binding.progressBar.setVisibility(View.GONE);
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        }else
        {
            AlertUtil.toastMsg(context,context.getString(R.string.network_error));
        }
    }

    //==========================Method to call edit_yellow meal api=====================//
    private void callEditMealApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboardOnPopup(context,binding.etPrice);
            binding.progressBar.setVisibility(View.VISIBLE);
            for(int i=0;i<itemsList.size();i++){
                if(itemsList.get(i).isSelected()){
                    if(itemIds.equals("")){
                        itemIds=""+itemsList.get(i).getId();
                        itemPrices=itemsList.get(i).getSelectedPrice();
                        itemSizes=itemsList.get(i).getSelectedSize();
                    }else{
                        itemIds=itemIds+","+itemsList.get(i).getId();
                        itemPrices=itemPrices+","+itemsList.get(i).getSelectedPrice();
                        itemSizes=itemSizes+","+itemsList.get(i).getSelectedSize();
                    }
                }
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("id",""+mealData.getId());
            params.put("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));
            params.put("title",binding.etTittle.getText().toString().trim());
            params.put("price",binding.etPrice.getText().toString().trim());
            params.put("items",itemIds);
            params.put("selected_size",itemSizes);
            params.put("selected_price",itemPrices);

            HashMap<String, File> fileHashMap = new HashMap<>();
            if (imgMeal != null)
                fileHashMap.put("image", imgMeal);
            apiRequest = new RestClient.ApiRequest(context);

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.EDIT_MEAL)
                    .setMethod(RestClient.ApiRequest.METHOD_MULTIPART)
                    .setFileParams(fileHashMap)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {
                                MealListResponse mealListResponse= new Gson().fromJson(response, MealListResponse.class);
                                if (mealListResponse != null) {
                                    if (mealListResponse.getCode()==200 ) {
                                        mealList.clear();
                                        mealList.addAll(mealListResponse.getResult());
                                        listener.onUpdate(mealList);
                                        dismiss();
                                    } else if(mealListResponse.getCode()==301)
                                    {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callEditMealApi();
                                            }
                                        });
                                    }

                                } else {
                                    binding.progressBar.setVisibility(View.GONE);
                                    AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                }

                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            binding.progressBar.setVisibility(View.GONE);
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        }else
        {
            AlertUtil.toastMsg(context,context.getString(R.string.network_error));
        }
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
                            imgMeal = imageFile;
                        }
                    }, "img" + new Random().nextInt(), false);
                }

            }
        });

    }
}
