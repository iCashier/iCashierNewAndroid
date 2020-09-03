package com.icashier.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.google.gson.reflect.TypeToken;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.CashierListAdapter;
import com.icashier.app.adapter.ExistingItemAdapter;
import com.icashier.app.adapter.MealItemChipAdapter;
import com.icashier.app.adapter.TodaysSpecialAdapter;
import com.icashier.app.adapter.WeekDaysAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.DialogCasheirLoginBinding;
import com.icashier.app.databinding.DialogEditDealBinding;
import com.icashier.app.databinding.DialogTodaySpecialBinding;
import com.icashier.app.databinding.DialogViewDealBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.DateUtils;
import com.icashier.app.helper.DecimalDigitsInputFilter;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.listener.DialogDismissListener;
import com.icashier.app.listener.ExistingItemListener;
import com.icashier.app.listener.GetDob;
import com.icashier.app.listener.MealChipListener;
import com.icashier.app.listener.SelectCashierListener;
import com.icashier.app.listener.SelectItemListener;
import com.icashier.app.model.CashierLoginResponse;
import com.icashier.app.model.ExistingDealsModel;
import com.icashier.app.model.ExistingItemList;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.GetCashiersResponse;
import com.icashier.app.model.MenuResponse;
import com.icashier.app.model.WeekDaysModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ViewDealDialog  extends Dialog {

    Context context;
    private LayoutInflater inflater;
    DialogViewDealBinding binding;

    List<WeekDaysModel> list=new ArrayList<>();
    RestClient.ApiRequest apiRequest;
    MealItemChipAdapter chipAdapter,chipAdapter2;
    int day,month,year;
    String fromDate,toDate,hour,amPm,fromTime,toTime;
    boolean firstDate;
    MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean data;
    List<ExistingItemList.ResultBean> itemsList=new ArrayList<>();
    List<ExistingItemList.ResultBean> withItemsList=new ArrayList<>();
    WeekDaysAdapter adapter;


    public ViewDealDialog(Context context, MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean data) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_view_deal, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);
        setAdapter();
        setData();
       // callGetItemListApi();
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



        binding.tvSelectDate.setOnClickListener(V->{
            getDate();
        });
        binding.disableClick.setOnClickListener(V->{});

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
                binding.tvTime.setText(""+time+" "+context.getString(R.string.hours));

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
        String[] days={"Su","Mo","Tu","We","Th","Fr","Sa"};
        for(int i=0;i<days.length;i++){
            WeekDaysModel weekDaysModel=new WeekDaysModel(days[i],false);
            list.add(weekDaysModel);
        }
    }

    //=================MEhtod to set data================//
    private void setData() {

        binding.etTitle.setText(data.getTitle());
        binding.etPrice.setText(data.getPrice());


        for(int i=0;i<data.getWithItemsDetails().size();i++){
            data.getWithItemsDetails().get(i).setSelected(true);
        }

        for(int i=0;i<data.getItemsDetails().size();i++){
            data.getItemsDetails().get(i).setSelected(true);
        }

        itemsList=new Gson().fromJson(new Gson().toJson(data.getItemsDetails()).toString(),new TypeToken<List<ExistingItemList.ResultBean>>() {
        }.getType());

        withItemsList=new Gson().fromJson(new Gson().toJson(data.getWithItemsDetails()).toString(),new TypeToken<List<ExistingItemList.ResultBean>>() {
        }.getType());
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
   /* private void callGetItemListApi() {

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
    }*/



    //================Mehtod to set chip items adapter==============//
    private void setWithItemChipAdapter() {
        chipAdapter2=new MealItemChipAdapter(context, withItemsList, new MealChipListener() {
            @Override
            public void onDeleteClick(int position) {
            }
        },false);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        binding.rvItems2.setLayoutManager(layoutManager);
        binding.rvItems2.setAdapter(chipAdapter2);
    }

    //================Mehtod to set chip items adapter==============//
    private void setItemChipAdapter() {
        chipAdapter=new MealItemChipAdapter(context,itemsList , new MealChipListener() {
            @Override
            public void onDeleteClick(int position) {
            }
        },false);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        binding.rvItems.setLayoutManager(layoutManager);
        binding.rvItems.setAdapter(chipAdapter);
    }
}