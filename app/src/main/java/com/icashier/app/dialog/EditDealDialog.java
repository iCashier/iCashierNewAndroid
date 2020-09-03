package com.icashier.app.dialog;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.icashier.app.databinding.DialogEditDealBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.DateUtils;
import com.icashier.app.helper.DecimalDigitsInputFilter;
import com.icashier.app.helper.ImagePickerUtil;
import com.icashier.app.helper.PermissionsUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.listener.DialogDismissListener;
import com.icashier.app.listener.GetDob;
import com.icashier.app.listener.MealChipListener;
import com.icashier.app.listener.SelectItemListener;
import com.icashier.app.model.ExistingDealsModel;
import com.icashier.app.model.ExistingItemList;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.WeekDaysModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class EditDealDialog  extends Dialog {

    HomeActivity context;
    private LayoutInflater inflater;
    DialogEditDealBinding binding;

    List<WeekDaysModel> list=new ArrayList<>();
    RestClient.ApiRequest apiRequest;
    MealItemChipAdapter chipAdapter,chipAdapter2;
    int day,month,year;
    String fromDate="",toDate="",hour="",amPm="",fromTime="",toTime="";
    boolean firstDate;
    ExistingDealsModel.ResultBean data;
    List<ExistingItemList.ResultBean> itemsList=new ArrayList<>();
    List<ExistingItemList.ResultBean> withItemsList=new ArrayList<>();
    WeekDaysAdapter adapter;
    DialogDismissListener listener;
    File imgDeal;


    public EditDealDialog(HomeActivity context,ExistingDealsModel.ResultBean data,DialogDismissListener listener) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data=data;
        this.listener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_edit_deal, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);
        setAdapter();
        callGetItemListApi();
        setOnClickListener();
        setSeekbar();
        setCheckChangeListener();
        binding.etPrice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});

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
                }
            }
        });
    }

    private void setOnClickListener() {

        binding.flClose.setOnClickListener(V->{
            dismiss();
        });

        binding.imgAddItem.setOnClickListener(V->{
            new SelectItemsDialog(context, itemsList, new SelectItemListener() {
                @Override
                public void onAddClicked() {

                    chipAdapter.notifyDataSetChanged();
                }

            }).show();
        });

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
                callEditDealApi();
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

    //==========================Method to call get category icons api=====================//
    private void callEditDealApi() {

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
            params.put("id",""+data.getId());
            params.put("title",binding.etTitle.getText().toString().trim());
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
                                        dismiss();
                                        listener.onDismiss();
                                        AlertUtil.toastMsg(context, genericResponse.getMessage());
                                    } else if(genericResponse.getCode()==301)
                                    {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callEditDealApi();
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
            AlertUtil.toastMsg(context, context.getString(R.string.network_error));
        }
    }


    private boolean isInputValid(){

        if(!binding.etTitle.getText().toString().trim().equals("")){
            if(!binding.etPrice.getText().toString().trim().equals("")){
                if(isItemsSelected()){

                    if(binding.cbDateRange.isChecked()){
                        if(!binding.tvSelectDate.getText().toString().equals("")){
                            if(binding.cbSelectDay.isChecked()){
                                if(isDaySelected()){
                                    return isInputValid2();
                                }else{
                                    AlertUtil.toastMsg(context,context.getString(R.string.empty_days));
                                }
                            }else{
                                return isInputValid2();
                            }
                        }else{
                            AlertUtil.toastMsg(context,context.getString(R.string.empty_date_range));

                        }
                    }else{
                        if(binding.cbSelectDay.isChecked()){
                            if(isDaySelected()){
                                return isInputValid2();
                            }else{
                                AlertUtil.toastMsg(context,context.getString(R.string.empty_days));
                            }
                        }else{
                            return isInputValid2();
                        }
                    }

                }else{
                    AlertUtil.toastMsg(context,context.getString(R.string.select_one_item));
                }
            }else{
                AlertUtil.toastMsg(context,context.getString(R.string.empty_price));
            }


        }else {
            AlertUtil.toastMsg(context,context.getString(R.string.empty_title));
        }
        return false;
    }

    private boolean isInputValid2(){

       /* if(!binding.etMoreThan.getText().toString().trim().equals("")){
            if(isWithItemsSelected()){
                return true;
            }else{
                AlertUtil.toastMsg(context,"Please select items to provide deal with.");
            }
        }else{
            AlertUtil.toastMsg(context,"Please select more than price.");
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

    private String getTime(int hours) {
        /*check AM/PM*/
        if (hours == 0) {

            hour = String.valueOf(hours + 12);
            amPm = "AM";


        } else if (hours > 12) {

            if(hours==24){

                hour = String.valueOf(hours - 12);
                amPm = "AM";
            }
            else{
                if ((hours - 12) < 10) {
                    hour = "0" + String.valueOf(hours - 12);
                } else {
                    hour = String.valueOf(hours - 12);
                }
                amPm = "PM";
            }
        } else if (hours == 12) {

            hour = String.valueOf(hours);
            amPm = "PM";

        } else {
            amPm = "AM";
            if (hours < 10) {
                hour = "0" + hours;
            } else {
                hour = String.valueOf(hours);
            }
        }

        return hour+":00 "+amPm;
    }


    private void setSeekbar() {

        if (Locale.getDefault().equals(new Locale("ar"))) {
            binding.rangeSeekbar.setScaleX(-1);
        }
        binding.rangeSeekbar.setOnSeekBarRangedChangeListener(new SeekBarRangedView.OnSeekBarRangedChangeListener() {
            @Override
            public void onChanged(SeekBarRangedView view, double minValue, double maxValue) {
                int min= (int)minValue;
                int max=(int)maxValue;
                binding.tvMin.setText(getTime(min));
                binding.tvMax.setText(getTime(max));
                int time=max-min;
                binding.tvTime.setText(""+time+" hours");

                fromTime=min+":00";
                toTime=max+":00";
            }

            @Override
            public void onChanging(SeekBarRangedView view, double minValue, double maxValue) {

            }
        });

    }



    //=============Mehtpod to get date============//
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

    //===========MEthod to crate dates array=========//
    public  void createArray(){
        String[] days={context.getString(R.string.su),context.getString(R.string.mo),context.getString(R.string.tu),context.getString(R.string.we),context.getString(R.string.th),context.getString(R.string.fr),context.getString(R.string.sa)};
        for(int i=0;i<days.length;i++){
            WeekDaysModel weekDaysModel=new WeekDaysModel(days[i],false);
            list.add(weekDaysModel);
        }
    }

    //=================MEhtod to set data================//
    private void setData() {

        binding.etTitle.setText(data.getTitle());
        binding.etPrice.setText(data.getPrice());

        if(data.getImage()!=null&&!data.getImage().equals("")){
            Utilities.setCategoryImage(context,binding.imgLogo,data.getImage());
        }
        for(int i=0;i<data.getItemsDetails().size();i++){
            for(int j=0;j<itemsList.size();j++){
                if(itemsList.get(j).getId()==data.getItemsDetails().get(i).getId()){
                    itemsList.get(j).setSelected(true);
                }
            }
        }

        for(int i=0;i<data.getWithItemsDetails().size();i++){
            for(int j=0;j<withItemsList.size();j++){
                if(withItemsList.get(j).getId()==data.getWithItemsDetails().get(i).getId()){
                    withItemsList.get(j).setSelected(true);
                }
            }
        }

        setItemChipAdapter();
        setWithItemChipAdapter();

        if(!data.getDays().equals("")){
            binding.cbSelectDay.setChecked(true);
            String[] selectedDays=data.getDays().split(",");
            if(selectedDays.length>0){
                for(int i=0;i<selectedDays.length;i++){
                    list.get(Integer.parseInt(selectedDays[i])-1).setSelected(true);
                }
                adapter.notifyDataSetChanged();
            }else{
                list.get(Integer.parseInt(data.getDays())-1).setSelected(true);
            }
        }else{
            binding.cbSelectDay.setChecked(false);
        }

        if(!data.getFromDate().equals("")){
            binding.cbDateRange.setChecked(true);
            binding.tvSelectDate.setEnabled(true);
            binding.tvSelectDate.setText(data.getFromDate()+" - "+data.getToDate());
            fromDate=data.getFromDate();
            toDate=data.getToDate();
        }else{
            binding.cbDateRange.setChecked(false);
            binding.tvSelectDate.setEnabled(false);
        }

        binding.rangeSeekbar.setSelectedMinValue(Integer.parseInt(data.getFromTime().split(":")[0]));
        binding.rangeSeekbar.setSelectedMaxValue(Integer.parseInt(data.getToTime().split(":")[0]));

        binding.etMoreThan.setText(data.getMoreThan());



    }

    //=========Mehtod to call api to get item list===========//
    private void callGetItemListApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
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
                                    ExistingItemList existingItemList2= new Gson().fromJson(response, ExistingItemList.class);

                                    if (existingItemList != null) {
                                        AlertUtil.hideProgressDialog();
                                        if (existingItemList.getCode()==200 ) {
                                            itemsList.addAll(existingItemList.getResult());
                                            withItemsList.addAll(existingItemList2.getResult());
                                            setData();
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
                                        binding.progressBar.setVisibility(View.GONE);
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
}