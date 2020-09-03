package com.icashier.app.fragment;


import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.MealItemChipAdapter;
import com.icashier.app.adapter.MealListAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentAddMealBinding;
import com.icashier.app.dialog.EditMealDialog;
import com.icashier.app.dialog.SelectItemsDialog;
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
import com.icashier.app.listener.MealListListener;
import com.icashier.app.listener.SelectItemListener;
import com.icashier.app.model.ExistingItemList;
import com.icashier.app.model.MealListResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class AddMealFragment extends Fragment {

    HomeActivity context;
    FragmentAddMealBinding binding;
    List<ExistingItemList.ResultBean> itemsList=new ArrayList<>();
    RestClient.ApiRequest apiRequest;
    MealItemChipAdapter chipAdapter;
    String itemIds="",itemPrices="",itemSizes="";
    List<MealListResponse.ResultBean> mealList=new ArrayList<>();
    MealListAdapter mealListAdapter;
    File imgMeal;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity)getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_add_meal, container, false);
        setOnClickListners();
        setMealListAdapter();
        callGetItemListApi();
        callGetMealListApi();
        setChipAdapter();
        binding.etPrice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});

        return binding.getRoot();
    }

    //=============Mehtod to set meal list adapter==========//
    private void setMealListAdapter()
    {
        mealListAdapter=new MealListAdapter(context, mealList, new MealListListener() {
            @Override
            public void onDeleteClick(int position) {
                AlertUtil.showAlertWindow(context, getString(R.string.delete_meal_alert), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callDeleteMealApi(position);
                    }
                });
            }

            @Override
            public void onEditClick(int position) {
                new EditMealDialog(context, mealList.get(position),  new EditMealDialogListener() {
                    @Override
                    public void onUpdate(List<MealListResponse.ResultBean> list) {
                        mealList.clear();
                        mealList.addAll(list);
                        mealListAdapter.notifyDataSetChanged();

                        if(mealList.size()>0){
                            setListViewHeight(binding.lvExpMeals,0);
                        }
                    }
                }).show();
            }

            @Override
            public void onExpanded(int position) {
                setListViewHeight(binding.lvExpMeals,position);
            }
        });
        binding.lvExpMeals.setAdapter(mealListAdapter);
        binding.lvExpMeals.setGroupIndicator(null);
        binding.lvExpMeals.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }
    //=================Mehtod to set onClick listeners============//
    private void setOnClickListners() {
        binding.llMeals.setOnClickListener(V->{
            Utilities.hideSoftKeyboard(context);
        });

        binding.flSave.setOnClickListener(V->{
            if(isValidInput()) {
                callAddMealApi();
            }
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

        binding.imgCamera1.setOnClickListener(V->{
            openImagePicker();
        });

        binding.imgLogo.setOnClickListener(V->{
            openImagePicker();
        });
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
        binding.tvTotalPrice.setText(getString(R.string.total_price_of_selected_items)+" "+totalPrice);
    }

    //=============Mehtod to validate user input===========//
    private boolean isValidInput(){
        if(!binding.etTittle.getText().toString().equals("")){
            if(imgMeal!=null) {
                if (!binding.etPrice.getText().toString().equals("")) {
                    boolean isSelected = false;
                    for (int i = 0; i < itemsList.size(); i++) {
                        if (itemsList.get(i).isSelected()) {
                            isSelected = true;
                        }

                    }
                    if (isSelected) {
                        return true;
                    } else {
                        AlertUtil.toastMsg(context,  getString(R.string.empty_meal_items));
                    }

                } else {
                    AlertUtil.toastMsg(context, getString(R.string.empty_price));
                }
            }else{
                AlertUtil.toastMsg(context, getString(R.string.empty_meal_image));

            }
        }else{
            AlertUtil.toastMsg(context,getString(R.string.empty_title));
        }
        return false;
    }
    //================Mehtod to set chip items adapter==============//
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
    //==========================Method to call get existing items  api=====================//
    private void callGetItemListApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            //AlertUtil.showProgressDialog(context);
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
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    ExistingItemList existingItemList= new Gson().fromJson(response, ExistingItemList.class);
                                    if (existingItemList != null) {
                                        AlertUtil.hideProgressDialog();
                                        if (existingItemList.getCode()==200 ) {
                                            itemsList.addAll(existingItemList.getResult());
                                        } else if(existingItemList.getCode()==301)
                                        {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                    callGetItemListApi();
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
        }else
        {
            AlertUtil.toastMsg(context,context.getString(R.string.network_error));
        }
    }

    //==========================Method to call add meal api=====================//
    private void callAddMealApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            AlertUtil.showProgressDialog(context);

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

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.ADD_MEAL)
                    .setMethod(RestClient.ApiRequest.METHOD_MULTIPART)
                    .setParams(params)
                    .setFileParams(fileHashMap)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                MealListResponse mealListResponse= new Gson().fromJson(response, MealListResponse.class);
                                if (mealListResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (mealListResponse.getCode()==200 ) {
                                        mealList.clear();
                                        mealList.addAll(mealListResponse.getResult());
                                        mealListAdapter.notifyDataSetChanged();
                                        if(mealList.size()>0){
                                            binding.tvExistingMeals.setVisibility(View.VISIBLE);
                                            setListViewHeight(binding.lvExpMeals,0);

                                        }else{
                                            binding.tvExistingMeals.setVisibility(View.GONE);
                                        }

                                        imgMeal=null;
                                        binding.imgLogo.setImageResource(R.drawable.placeholder_logo);
                                        binding.etTittle.setText("");
                                        binding.etPrice.setText("");
                                        binding.tvTotalPrice.setText("");
                                        for(int i=0;i<itemsList.size();i++){
                                            itemsList.get(i).setSelected(false);
                                        }
                                        chipAdapter.notifyDataSetChanged();

                                    } else if(mealListResponse.getCode()==301)
                                    {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callAddMealApi();
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
        }else
        {
            AlertUtil.toastMsg(context,context.getString(R.string.network_error));
        }
    }

    //==========================Method to call add meal api=====================//
    private void callDeleteMealApi(int position) {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            AlertUtil.showProgressDialog(context);

            HashMap<String, String> params = new HashMap<>();
            params.put("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));
            params.put("id",""+mealList.get(position).getId());


            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.DELETE_MEAL)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                MealListResponse mealListResponse= new Gson().fromJson(response, MealListResponse.class);
                                if (mealListResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (mealListResponse.getCode()==200 ) {
                                        mealList.clear();
                                        mealList.addAll(mealListResponse.getResult());
                                        mealListAdapter.notifyDataSetChanged();
                                        if(mealList.size()>0){
                                            binding.tvExistingMeals.setVisibility(View.VISIBLE);
                                            setListViewHeight(binding.lvExpMeals,0);
                                        }else{
                                            binding.tvExistingMeals.setVisibility(View.GONE);
                                        }
                                    } else if(mealListResponse.getCode()==301)
                                    {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callGetMealListApi();
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
        }else
        {
            AlertUtil.toastMsg(context,context.getString(R.string.network_error));
        }
    }

    //==========================Method to call get existing meals  api=====================//
    private void callGetMealListApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            //AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_MEAL_LIST)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                MealListResponse mealListResponse= new Gson().fromJson(response, MealListResponse.class);
                                if (mealListResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (mealListResponse.getCode()==200 ) {
                                        mealList.clear();
                                        mealList.addAll(mealListResponse.getResult());
                                        mealListAdapter.notifyDataSetChanged();
                                        if(mealList.size()>0){
                                            binding.tvExistingMeals.setVisibility(View.VISIBLE);
                                            setListViewHeight(binding.lvExpMeals,0);

                                        }else{
                                            binding.tvExistingMeals.setVisibility(View.GONE);
                                        }

                                    } else if(mealListResponse.getCode()==301)
                                    {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callGetMealListApi();
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


    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtil.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
