package com.icashier.app.fragment;


import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;

import com.github.guilhe.rangeseekbar.SeekBarRangedView;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.MealItemChipAdapter;
import com.icashier.app.adapter.WeekDaysAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentNewDealsBinding;
import com.icashier.app.dialog.SelectItemsDialog;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.DateUtils;
import com.icashier.app.helper.DecimalDigitsInputFilter;
import com.icashier.app.helper.ImagePickerUtil;
import com.icashier.app.helper.PermissionsUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.listener.GetDob;
import com.icashier.app.listener.MealChipListener;
import com.icashier.app.listener.SelectItemListener;
import com.icashier.app.model.ExistingItemList;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.WeekDaysModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewDealsFragment extends Fragment {

    HomeActivity context;
    FragmentNewDealsBinding binding;
    List<ExistingItemList.ResultBean> itemsList=new ArrayList<>();
    List<ExistingItemList.ResultBean> withItemsList=new ArrayList<>();
    List<WeekDaysModel> list=new ArrayList<>();
    RestClient.ApiRequest apiRequest;
    MealItemChipAdapter chipAdapter,chipAdapter2;
    int day,month,year;
    String fromDate="",toDate="",hour="",amPm="",fromTime="00:00",toTime="24:00";
    boolean firstDate;
    WeekDaysAdapter adapter;
    File imgDeal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity)getActivity();

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_new_deals,container,false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setAdapter();
        setOnClickListener();
        callGetItemListApi();
        setItemChipAdapter();
        setWithItemChipAdapter();
        setSeekbar();
        setCheckChangeListener();
        binding.etPrice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
        return binding.getRoot();
    }

    private void setCheckChangeListener() {

        binding.cbDateRange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    binding.tvSelectDate.setEnabled(true);
                }else{
                    binding.tvSelectDate.setEnabled(false);
                    binding.tvSelectDate.setText("");
                    fromDate="";
                    toDate="";
                }
            }
        });

        binding.cbSelectDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    for(int i=0;i<list.size();i++){
                        list.get(i).setSelected(false);
                    }
                    adapter.notifyDataSetChanged();
                    binding.llOverlay.setVisibility(View.VISIBLE);
                }else{
                    binding.llOverlay.setVisibility(View.GONE);
                }
            }
        });
    }


    private boolean isInputValid(){

        if(!binding.etTittle.getText().toString().trim().equals("")){
            if(!binding.etPrice.getText().toString().trim().equals("")) {
                if (imgDeal != null) {
                    if (isItemsSelected()) {

                        if (binding.cbDateRange.isChecked()) {
                            if (!binding.tvSelectDate.getText().toString().equals("")) {
                                if (binding.cbSelectDay.isChecked()) {
                                    if (isDaySelected()) {
                                        return isInputValid2();
                                    } else {
                                        AlertUtil.toastMsg(context, getString(R.string.empty_days));
                                    }
                                } else {
                                    return isInputValid2();
                                }
                            } else {
                                AlertUtil.toastMsg(context, getString(R.string.empty_date_range));

                            }
                        } else {
                            if (binding.cbSelectDay.isChecked()) {
                                if (isDaySelected()) {
                                    return isInputValid2();
                                } else {
                                    AlertUtil.toastMsg(context, getString(R.string.empty_days));
                                }
                            } else {
                                return isInputValid2();
                            }
                        }

                    } else {
                        AlertUtil.toastMsg(context, getString(R.string.select_one_item));
                    }
                } else {
                    AlertUtil.toastMsg(context, getString(R.string.empty_deal_image));
                }
            }
            else{
                AlertUtil.toastMsg(context,getString(R.string.empty_price));
            }


        }else {
            AlertUtil.toastMsg(context,getString(R.string.empty_title));
        }
        return false;
    }

    private boolean isInputValid2(){

        /*if(!binding.etMoreThan.getText().toString().trim().equals("")){
            if(isWithItemsSelected()){
                return true;
            }else{
                AlertUtil.toastMsg(context,getString(R.string.empty_with_items));
            }
        }else{
            AlertUtil.toastMsg(context,getString(R.string.empty_more_price));
        }*/
        return true;
    }

    private boolean isDaySelected() {
        boolean isSelected=false;
        for(int i=0;i<list.size();i++){
            if(list.get(i).isSelected()){
                isSelected=true;
                break;
            }
        }
        return isSelected;
    }

    private boolean isItemsSelected() {
        boolean isSelected=false;
        for(int i=0;i<itemsList.size();i++){
           if(itemsList.get(i).isSelected()) {
               isSelected=true;
               break;
           }
        }
        return isSelected;
    }

    private boolean isWithItemsSelected() {
        boolean isSelected=false;
        for(int i=0;i<withItemsList.size();i++){
            if(withItemsList.get(i).isSelected()) {
                isSelected=true;
                break;
            }
        }
        return isSelected;
    }


    private void setSeekbar() {

        if (Locale.getDefault().equals(new Locale("ar"))) {
            binding.rangeSeekbar.setScaleX(-1);
        }
        binding.rangeSeekbar.setScaleX(-1);
        binding.rangeSeekbar.setOnSeekBarRangedChangeListener(new SeekBarRangedView.OnSeekBarRangedChangeListener() {
            @Override
            public void onChanged(SeekBarRangedView view, double minValue, double maxValue) {
                int min= (int)minValue;
                int max=(int)maxValue;
                binding.tvMin.setText(getTime(min));
                binding.tvMax.setText(getTime(max));
                int time=max-min;
                binding.tvTime.setText(""+time+" "+getString(R.string.hours_label));

                fromTime=min+":00";
                toTime=max+":00";
            }

            @Override
            public void onChanging(SeekBarRangedView view, double minValue, double maxValue) {

            }
        });

    }

    private void setOnClickListener() {
        binding.imgAddItem.setOnClickListener(V->{
            new SelectItemsDialog(context, itemsList, new SelectItemListener() {
                @Override
                public void onAddClicked() {

                    chipAdapter.notifyDataSetChanged();
                }

            }).show();
        });

        binding.llOverlay.setOnClickListener(V->{});
        binding.imgAddItem2.setOnClickListener(V->{
            new SelectItemsDialog(context, withItemsList, new SelectItemListener() {
                @Override
                public void onAddClicked() {

                    chipAdapter2.notifyDataSetChanged();
                }

            }).show();
        });

        binding.tvSelectDate.setOnClickListener(V->{
            getDate();
        });

        binding.flSave.setOnClickListener(V->{
            if(isInputValid()){
                callSaveDealApi();
            }
        });

        binding.imgCamera1.setOnClickListener(V->{
            openImagePicker();
        });

        binding.imgLogo.setOnClickListener(V->{
            openImagePicker();
        });

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
                            imgDeal = imageFile;
                        }
                    }, "img" + new Random().nextInt(), false);
                }

            }
        });

    }

    private void getDate()
    {
        DateUtils.openDatePicker2(context, day, month, year, new GetDob() {
            @Override
            public void getTimeSuccess(int hours, int minutes) {

            }

            @Override
            public void getDateSuccess(int _day, int _month, int _year) {
                day=_day;
                month=_month;
                year=_year;
                if(!firstDate){
                    fromDate=""+day+"-"+month+"-"+year;
                    firstDate=true;
                    getDate();
                }else{
                    toDate=""+day+"-"+month+"-"+year;
                    firstDate=false;
                    binding.tvSelectDate.setText(fromDate+" to "+toDate);
                }
            }
        });
    }

    //======Method to set weekdays adapter=====//
    private void setAdapter() {

        createArray();
         adapter=new WeekDaysAdapter(context,list);
        binding.rvDays.setAdapter(adapter);
        binding.rvDays.setLayoutManager(new GridLayoutManager(context,7));
    }

    //================Mehtod to set chip items adapter==============//
    private void setWithItemChipAdapter() {
        chipAdapter2=new MealItemChipAdapter(context, withItemsList, new MealChipListener() {
            @Override
            public void onDeleteClick(int position) {
            }
        },true);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        binding.rvItems2.setLayoutManager(layoutManager);
        binding.rvItems2.setAdapter(chipAdapter2);
    }

    //================Mehtod to set chip items adapter==============//
    private void setItemChipAdapter() {
        chipAdapter=new MealItemChipAdapter(context, itemsList, new MealChipListener() {
            @Override
            public void onDeleteClick(int position) {
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
                                    ExistingItemList existingItemList2= new Gson().fromJson(response, ExistingItemList.class);

                                    if (existingItemList != null) {
                                        AlertUtil.hideProgressDialog();
                                        if (existingItemList.getCode()==200 ) {
                                            itemsList.addAll(existingItemList.getResult());
                                            withItemsList.addAll(existingItemList2.getResult());
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

    private String getTime(int hours) {
        /*check AM/PM*/
        if (hours == 0) {

                hour = String.valueOf(hours + 12);
                amPm = getString(R.string.am);


        } else if (hours > 12) {

            if(hours==24){

                hour = String.valueOf(hours - 12);
                amPm = getString(R.string.am);
            }
            else{
                if ((hours - 12) < 10) {
                    hour = "0" + String.valueOf(hours - 12);
                } else {
                    hour = String.valueOf(hours - 12);
                }
                amPm = getString(R.string.pm);
            }

        } else if (hours == 12) {

                hour = String.valueOf(hours);
                amPm = getString(R.string.pm);

        } else {
            amPm = getString(R.string.am);
            if (hours < 10) {
                hour = "0" + hours;
            } else {
                hour = String.valueOf(hours);
            }
        }

       return hour+":00 "+amPm;
    }

    //==========================Method to call get category icons api=====================//
    private void callSaveDealApi() {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            AlertUtil.showProgressDialog(context);

            String itemsId="",withItemsId="",days="";
            for(int i=0;i<itemsList.size();i++){
                if(itemsList.get(i).isSelected()){
                    if(itemsId.equals("")){
                        itemsId=""+itemsList.get(i).getId();
                    }else{
                        itemsId=itemsId+","+itemsList.get(i).getId();
                    }
                }

            }

            for(int i=0;i<withItemsList.size();i++){
                if(withItemsList.get(i).isSelected()){
                    if(withItemsId.equals("")){
                        withItemsId=""+withItemsList.get(i).getId();
                    }else{
                        withItemsId=withItemsId+","+withItemsList.get(i).getId();
                    }
                }
            }

            for(int i=0;i<list.size();i++){
                if(list.get(i).isSelected()){
                    if(days.equals("")){
                        int day=i+1;
                        days=""+day;
                    }else{
                        int day=i+1;
                        days=days+","+day;
                    }
                }
            }



            HashMap<String, String> params = new HashMap<>();
            params.put("title",binding.etTittle.getText().toString().trim());
            params.put("price",binding.etPrice.getText().toString().trim());
            params.put("items",itemsId);
            params.put("fromDate",fromDate);
            params.put("toDate",toDate);
            params.put("days",days);
            params.put("fromTime",fromTime);
            params.put("toTime",toTime);
            params.put("withItems",withItemsId);
            params.put("moreThan",binding.etMoreThan.getText().toString().trim());

            HashMap<String, File> fileHashMap = new HashMap<>();
            if (imgDeal != null)
                fileHashMap.put("image", imgDeal);

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.DEALS)
                    .setMethod(RestClient.ApiRequest.METHOD_MULTIPART)
                    .setParams(params)
                    .setFileParams(fileHashMap)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                                if (genericResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (genericResponse.getCode() == 200) {
                                        clearfields();
                                        AlertUtil.toastMsg(context, genericResponse.getMessage());
                                    } else if(genericResponse.getCode()==301)
                                    {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callSaveDealApi();
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

    public  void createArray(){
        String[] days={getString(R.string.su),getString(R.string.mo),getString(R.string.tu),getString(R.string.we),getString(R.string.th),getString(R.string.fr),getString(R.string.sa)};
        for(int i=0;i<days.length;i++){
            WeekDaysModel weekDaysModel=new WeekDaysModel(days[i],false);
            list.add(weekDaysModel);
        }
    }

    private void clearfields(){
        binding.etTittle.setText("");
        binding.etPrice.setText("");
        binding.etMoreThan.setText("");
        binding.tvSelectDate.setText("");
        binding.cbSelectDay.setChecked(false);
        binding.cbDateRange.setChecked(false);
        fromDate="";
        toDate="";
        fromTime="";
        toTime="";

        imgDeal=null;
        binding.imgLogo.setImageResource(R.drawable.placeholder_logo);

        for(int i=0;i<itemsList.size();i++){
            itemsList.get(i).setSelected(false);
        }

        for(int i=0;i<withItemsList.size();i++){
            withItemsList.get(i).setSelected(false);
        }

        chipAdapter.notifyDataSetChanged();
        chipAdapter2.notifyDataSetChanged();
        binding.rangeSeekbar.setSelectedMaxValue(24);
        binding.rangeSeekbar.setSelectedMinValue(0);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtil.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
