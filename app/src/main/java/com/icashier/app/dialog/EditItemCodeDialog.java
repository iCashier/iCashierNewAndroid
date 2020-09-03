package com.icashier.app.dialog;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.DialogEditItemCodeBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.listener.DialogDismissListener;
import com.icashier.app.model.AllCodesListResponse;
import com.icashier.app.model.ExistingDealsModel;
import com.icashier.app.model.ExistingItemList;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.GetCouponsResponse;
import com.icashier.app.model.MealListResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditItemCodeDialog extends Dialog {

    HomeActivity context;
    private LayoutInflater inflater;
    DialogEditItemCodeBinding binding;
    AllCodesListResponse.ResultBean codeData;
    RestClient.ApiRequest apiRequest;
    List<ExistingItemList.ResultBean> itemList=new ArrayList<>();
    List<MealListResponse.ResultBean> mealList=new ArrayList<>();
    List<ExistingDealsModel.ResultBean> dealList=new ArrayList<>();
    List<GetCouponsResponse.ResultBean> couponsList = new ArrayList<>();

    List<String> itemNamesList=new ArrayList<>();
    ExistingItemList.ResultBean item;
    MealListResponse.ResultBean meal;
    GetCouponsResponse.ResultBean coupon;
    ExistingDealsModel.ResultBean deal;
    String paymentMode="",deliveryMode="";
    DialogDismissListener listener;
    boolean isCreditCard,isCash,isPaypal;
    boolean isDelivery,isPickup,isInstaDelivery,isSwiftDelivery;


    public EditItemCodeDialog(HomeActivity context,AllCodesListResponse.ResultBean data,DialogDismissListener listener) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.codeData=data;
        this.listener=listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_edit_item_code, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);

        setRadioButton();
        setOnClickListeners();
        if(codeData.getType().equals(AppConstant.TYPE_ITEM)){
            binding.rbItem.setChecked(true);
            binding.rbMeal.setVisibility(View.GONE);
            binding.rbDeal.setVisibility(View.GONE);
            binding.rbCoupon.setVisibility(View.GONE);

            callGetItemListApi();

        }else if(codeData.getType().equals(AppConstant.TYPE_MEALS)){
            binding.rbMeal.setChecked(true);
            binding.rbItem.setVisibility(View.GONE);
            binding.rbDeal.setVisibility(View.GONE);
            binding.rbCoupon.setVisibility(View.GONE);

            callGetMealListApi();
        }else if(codeData.getType().equals(AppConstant.TYPE_DEALS)){
            binding.rbDeal.setChecked(true);
            binding.rbItem.setVisibility(View.GONE);
            binding.rbMeal.setVisibility(View.GONE);
            binding.rbCoupon.setVisibility(View.GONE);

            callGetDealsApi();
        }else if(codeData.getType().equals(AppConstant.TYPE_COUPON)){
            binding.rbCoupon.setChecked(true);
            binding.rbItem.setVisibility(View.GONE);
            binding.rbMeal.setVisibility(View.GONE);
            binding.rbDeal.setVisibility(View.GONE);
            binding.rbCoupon.setVisibility(View.VISIBLE);
            callGetCoupons();
        }



    }
    //===============Mehtod to implememnt radio button listener=========//
    private void setRadioButton() {

        binding.rbDeal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                meal=null;
                deal=null;
                item=null;
                binding.tvItem.setText("");
                binding.flItemImg.setVisibility(View.GONE);
            }
        });

        binding.rbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                meal=null;
                deal=null;
                item=null;
                binding.tvItem.setText("");
                binding.flItemImg.setVisibility(View.GONE);
            }
        });

        binding.rbMeal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                meal=null;
                deal=null;
                item=null;
                binding.tvItem.setText("");
                binding.flItemImg.setVisibility(View.GONE);
            }
        });

        binding.rbCoupon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                meal=null;
                deal=null;
                item=null;
                coupon=null;
                binding.tvItem.setText("");
                binding.flItemImg.setVisibility(View.GONE);
            }
        });
    }

    //======================Method to set onClick listeners================//
    private void setOnClickListeners() {

        binding.clParentLayout.setOnClickListener(V -> {
            context.closeDrawer();

        });

        binding.llCash.setOnClickListener(V -> {
            setPaymentSelector(binding.imgCash);
        });

        binding.llCreditCard.setOnClickListener(V -> {
            setPaymentSelector(binding.imgCreditCard);
        });

        binding.llPaypal.setOnClickListener(V -> {
            setPaymentSelector(binding.imgPaypal);
        });

        binding.tvItem.setOnClickListener(V->{
            if(binding.rbItem.isChecked()){
                new SelectItemDialog(context, itemList, new DialogDismissListener() {
                    @Override
                    public void onDismiss() {
                        for(int i=0;i<itemList.size();i++){
                            if(itemList.get(i).isSelected()){
                                item=itemList.get(i);
                            }
                        }

                        if(item!=null) {
                            binding.flItemImg.setVisibility(View.VISIBLE);
                            Utilities.setCategoryImage(context, binding.imgItem, ServerConstants.IMAGE_BASE_URL + item.getImage());
                            binding.tvItem.setText(item.getName());
                        }
                    }
                }).show();
            }else if(binding.rbMeal.isChecked()){
                new SelectMealDialog(context,mealList, new DialogDismissListener() {
                    @Override
                    public void onDismiss() {
                        for(int i=0;i<mealList.size();i++){
                            if(mealList.get(i).isSelected()){
                                meal=mealList.get(i);
                            }
                        }
                        if(meal!=null) {
                            if(meal.getImage()!=null&&!meal.getImage().equals("")){
                                Utilities.setCategoryImage(context, binding.imgItem,  meal.getImage());
                            }else {
                                binding.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_meal_item));
                            }
                            binding.flItemImg.setVisibility(View.VISIBLE);
                            binding.tvItem.setText(meal.getTitle());
                        }
                    }
                }).show();
            }else if(binding.rbDeal.isChecked()){
                new SelectDealDialog(context,dealList, new DialogDismissListener() {
                    @Override
                    public void onDismiss() {
                        for(int i=0;i<dealList.size();i++){
                            if(dealList.get(i).isSelected()){
                                deal=dealList.get(i);
                            }
                        }
                        if(deal!=null) {
                            if(deal.getImage()!=null&&!deal.getImage().equals("")){
                                Utilities.setCategoryImage(context,binding.imgItem,deal.getImage());
                            }else{
                                binding.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.deals_icon));
                            }                            binding.flItemImg.setVisibility(View.VISIBLE);
                            binding.tvItem.setText(deal.getTitle());
                        }
                    }
                }).show();
            }else if(binding.rbCoupon.isChecked()){
                new SelectCouponDialog(context, couponsList, new DialogDismissListener() {
                    @Override
                    public void onDismiss() {
                        for(int i=0;i<couponsList.size();i++){
                            if(couponsList.get(i).isSelected()){
                                coupon=couponsList.get(i);
                            }
                        }

                        if(coupon!=null){
                            binding.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.deals_icon));
                            binding.flItemImg.setVisibility(View.VISIBLE);
                            binding.tvItem.setText(coupon.getTitle());
                        }
                    }
                }).show();
            }
        });

        binding.llPickup.setOnClickListener(V->{
            setDeliverySelector(binding.imgPickup);
        });

        binding.llDelivery.setOnClickListener(V->{
            setDeliverySelector(binding.imgDelivery);
        });

        binding.llInstaDelivery.setOnClickListener(V->{
            setDeliverySelector(binding.imgInstaDelivery);
        });

        binding.llSwiftDelivery.setOnClickListener(V->{
            setDeliverySelector(binding.imgSwiftDelivery);
        });

        binding.tvUpdate.setOnClickListener(V->{
            if(isValidInput()){
                callUpdateCodeApi();
            }
        });

        binding.tvCancel.setOnClickListener(V->{
            dismiss();
        });
    }


    //=================Method to select delivery mode=============//
    private void setDeliverySelector(ImageView img) {
        if(img==binding.imgDelivery)
        {
            if(!isDelivery) {
                binding.imgDelivery.setSelected(true);
                isDelivery = true;
            }else{
                binding.imgDelivery.setSelected(false);
                isDelivery = false;
            }

        }
        else if(img==binding.imgPickup)
        {
            if(!isPickup) {
                binding.imgPickup.setSelected(true);
                isPickup = true;
            }else{
                binding.imgPickup.setSelected(false);
                isPickup = false;
            }

        }
        else if(img==binding.imgSwiftDelivery)
        {
            if(!isSwiftDelivery) {
                binding.imgSwiftDelivery.setSelected(true);
                isSwiftDelivery = true;
            }else{
                binding.imgSwiftDelivery.setSelected(false);
                isSwiftDelivery = false;
            }
        }
        else if(img==binding.imgInstaDelivery)
        {
            if(!isInstaDelivery) {
                binding.imgInstaDelivery.setSelected(true);
                isInstaDelivery = true;
            }else{
                binding.imgInstaDelivery.setSelected(false);
                isInstaDelivery = false;
            }
        }
    }

    //==========Mehtod to validate user input===========//
    private boolean isValidInput() {
        if(item!=null||meal!=null||deal!=null||coupon!=null){
            if(isCreditCard||isCash||isPaypal){
                if(isPickup||isDelivery||isSwiftDelivery||isInstaDelivery){
                    return true;
                }else
                {
                    AlertUtil.toastMsg(context,context.getString(R.string.empty_delivery_mode));
                }
            }else{
                AlertUtil.toastMsg(context,context.getString(R.string.empty_payment_mode));
            }
        }else{
            AlertUtil.toastMsg(context,context.getString(R.string.empty_item));
        }
        return false;
    }

    //=================MEhtod to select payment method=============//
    private void setPaymentSelector(ImageView img) {
        if(img==binding.imgPaypal)
        {
            if(!isPaypal){
                binding.imgPaypal.setSelected(true);
                isPaypal=true;
            }else{
                binding.imgPaypal.setSelected(false);
                isPaypal=false;
            }

        }
        else if(img==binding.imgCreditCard)
        {
            if(!isCreditCard){
                binding.imgCreditCard.setSelected(true);
                isCreditCard=true;
            }else{
                binding.imgCreditCard.setSelected(false);
                isCreditCard=false;
            }
        }
        else if(img==binding.imgCash)
        {
            if(!isCash){
                binding.imgCash.setSelected(true);
                isCash=true;
            }else{
                binding.imgCash.setSelected(false);
                isCash=false;
            }
        }
    }


    //==========================Method to call get existing items  api=====================//
    private void callGetItemListApi() {

        if(Utilities.isNetworkAvailable(context)) {

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
                                ExistingItemList existingItemList= new Gson().fromJson(response, ExistingItemList.class);
                                if (existingItemList != null) {
                                    if (existingItemList.getCode()==200 ) {
                                        itemList.addAll(existingItemList.getResult());
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

    //==============Mehtod to set data===========//
    private void setData() {

        if(codeData.getType().equals(AppConstant.TYPE_ITEM)){

            for(int i=0;i<itemList.size();i++){
                if(itemList.get(i).getId()==codeData.getItem_id()){
                    binding.tvItem.setText(itemList.get(i).getName());
                    item=itemList.get(i);
                    itemList.get(i).setSelected(true);
                    Utilities.setCategoryImage(context,binding.imgItem,ServerConstants.IMAGE_BASE_URL+item.getImage());
                    binding.flItemImg.setVisibility(View.VISIBLE);
                }
            }
        }else if(codeData.getType().equals(AppConstant.TYPE_DEALS)){

            for(int i=0;i<dealList.size();i++){
                if(dealList.get(i).getId()==codeData.getDeals_id()){
                    binding.tvItem.setText(dealList.get(i).getTitle());
                    deal=dealList.get(i);
                    dealList.get(i).setSelected(true);
                    if(deal.getImage()!=null&&!deal.getImage().equals("")){
                        Utilities.setCategoryImage(context,binding.imgItem,deal.getImage());
                    }else{
                        binding.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.deals_icon));
                    }
                    binding.flItemImg.setVisibility(View.VISIBLE);
                }
            }
        }else if(codeData.getType().equals(AppConstant.TYPE_MEALS)){

            for(int i=0;i<mealList.size();i++){
                if(mealList.get(i).getId()==codeData.getMeals_id()){
                    binding.tvItem.setText(mealList.get(i).getTitle());
                    meal=mealList.get(i);
                    mealList.get(i).setSelected(true);
                    if(meal.getImage()!=null&&!meal.getImage().equals("")){
                        Utilities.setCategoryImage(context, binding.imgItem,  meal.getImage());
                    }else {
                        binding.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_meal_item));
                    }
                    binding.flItemImg.setVisibility(View.VISIBLE);
                }
            }
        }else if(codeData.getType().equals(AppConstant.TYPE_COUPON)){

            for(int i=0;i<couponsList.size();i++){
                if(couponsList.get(i).getId()==codeData.getCoupon_id()){
                    binding.tvItem.setText(couponsList.get(i).getTitle());
                    coupon=couponsList.get(i);
                    couponsList.get(i).setSelected(true);
                    binding.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_title));
                    binding.flItemImg.setVisibility(View.VISIBLE);
                }
            }
        }

        if(codeData.getPayment().contains(AppConstant.CASH)){
            setPaymentSelector(binding.imgCash);

        }  if(codeData.getPayment().contains(AppConstant.CREDIT_CARD)){
            setPaymentSelector(binding.imgCreditCard);

        } if(codeData.getPayment().contains(AppConstant.PAYPAL)){
            setPaymentSelector(binding.imgPaypal);

        }


        if(codeData.getDelivery().contains(AppConstant.TYPE_DELIVERY)){
            setDeliverySelector(binding.imgDelivery);
        }if(codeData.getDelivery().contains(AppConstant.TYPE_PICKUP)){
            setDeliverySelector(binding.imgPickup);
        }if(codeData.getDelivery().contains(AppConstant.TYPE_INSTA_DELIVERY)){
            setDeliverySelector(binding.imgInstaDelivery);
        }if(codeData.getDelivery().contains(AppConstant.TYPE_SWIFT_DELIVERY)){
            setDeliverySelector(binding.imgSwiftDelivery);
        }
    }


    //============Method to call update qr api===========//
    private void callUpdateCodeApi() {

        if(Utilities.isNetworkAvailable(context)) {

            binding.progressBar.setVisibility(View.VISIBLE);
            paymentMode="";
            if(isCreditCard){
                if(paymentMode.equals("")){
                    paymentMode=AppConstant.CREDIT_CARD;
                }else{
                    paymentMode=paymentMode+","+AppConstant.CREDIT_CARD;
                }
            }
            if(isCash){
                if(paymentMode.equals("")){
                    paymentMode=AppConstant.CASH;
                }else{
                    paymentMode=paymentMode+","+AppConstant.CASH;
                }
            }

            if(isPaypal){
                if(paymentMode.equals("")){
                    paymentMode=AppConstant.PAYPAL;
                }else{
                    paymentMode=paymentMode+","+AppConstant.PAYPAL;
                }
            }

            deliveryMode="";
            if(isDelivery){
                if(deliveryMode.equals("")){
                    deliveryMode=AppConstant.TYPE_DELIVERY;
                }else{
                    deliveryMode=deliveryMode+","+AppConstant.TYPE_DELIVERY;
                }
            }

            if(isPickup){
                if(deliveryMode.equals("")){
                    deliveryMode=AppConstant.TYPE_PICKUP;
                }else{
                    deliveryMode=deliveryMode+","+AppConstant.TYPE_PICKUP;
                }
            }

            if(isInstaDelivery){
                if(deliveryMode.equals("")){
                    deliveryMode=AppConstant.TYPE_INSTA_DELIVERY;
                }else{
                    deliveryMode=deliveryMode+","+AppConstant.TYPE_INSTA_DELIVERY;
                }
            }
            if(isSwiftDelivery){
                if(deliveryMode.equals("")){
                    deliveryMode=AppConstant.TYPE_SWIFT_DELIVERY;
                }else{
                    deliveryMode=deliveryMode+","+AppConstant.TYPE_SWIFT_DELIVERY;
                }
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("id",""+codeData.getId());
            params.put("payment",paymentMode);
            params.put("delivery",deliveryMode);

            if(binding.rbMeal.isChecked()){
                params.put("type","meals");
                params.put("meals_id",""+meal.getId());
                params.put("name",meal.getTitle());
            }else if(binding.rbItem.isChecked()){
                params.put("type","item");
                params.put("item_id",""+item.getId());
                params.put("name",item.getName());

            }else if(binding.rbDeal.isChecked()){
                params.put("type","deals");
                params.put("deals_id",""+deal.getId());
                params.put("name",deal.getTitle());

            }else if(binding.rbCoupon.isChecked()){
                params.put("type","coupon");
                params.put("coupon_id",""+coupon.getId());
                params.put("name",coupon.getTitle());

            }

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.EDIT_QR)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {
                                    GenericResponse genericResponse= new Gson().fromJson(response, GenericResponse.class);
                                    if (genericResponse != null) {
                                        if (genericResponse.getCode()==200 ) {

                                            binding.imgPaypal.setSelected(false);
                                            binding.imgCash.setSelected(false);
                                            binding.imgCreditCard.setSelected(false);
                                            binding.imgSwiftDelivery.setSelected(false);
                                            binding.imgDelivery.setSelected(false);
                                            binding.imgInstaDelivery.setSelected(false);
                                            binding.imgPickup.setSelected(false);
                                            binding.tvItem.setText("");
                                            paymentMode="";
                                            deliveryMode="";
                                            binding.flItemImg.setVisibility(View.GONE);
                                            item=null;
                                            AlertUtil.toastMsg(context,genericResponse.getMessage());
                                            dismiss();
                                            listener.onDismiss();
                                        }else if(genericResponse.getCode()==301)
                                        {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                    callUpdateCodeApi();
                                                }
                                            });
                                        }
                                    } else {
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

    //==========================Method to call get existing meals  api=====================//
    private void callGetMealListApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            binding.progressBar.setVisibility(View.VISIBLE);

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
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {
                                MealListResponse mealListResponse= new Gson().fromJson(response, MealListResponse.class);
                                if (mealListResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (mealListResponse.getCode()==200 ) {
                                        mealList.clear();
                                        mealList.addAll(mealListResponse.getResult());
                                        setData();


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

    private void callGetDealsApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> params = new HashMap<>();

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.DEALS)
                    .setMethod(RestClient.ApiRequest.METHOD_GET)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {
                                ExistingDealsModel existingDealsModel= new Gson().fromJson(response, ExistingDealsModel.class);
                                if (existingDealsModel != null) {
                                    if(existingDealsModel.getCode()==200){
                                        if(existingDealsModel.getResult().size()>0){

                                            dealList.clear();
                                            dealList.addAll(existingDealsModel.getResult());
                                            setData();
                                        }
                                    }
                                } else {
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

    private void callGetCoupons() {
        //AlertUtil.showProgressDialog(context);
        HashMap<String, String> map = new HashMap<>();
        apiRequest = new RestClient.ApiRequest(context);
        apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.COUPON_LIST)
                .setMethod(RestClient.ApiRequest.METHOD_POST)
                .setParams(map)
                .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                .setResponseListener((tag, response) -> {
                    AlertUtil.hideProgressDialog();
                    if (response != null) {
                        GetCouponsResponse getCouponsResponse = new Gson().fromJson(response, GetCouponsResponse.class);
                        if (getCouponsResponse.getCode() == 200) {
                            couponsList.clear();
                            couponsList.addAll(getCouponsResponse.getResult());
                            setData();
                        } else
                            Toast.makeText(context, getCouponsResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).setErrorListener((tag, errorMsg) -> {
            AlertUtil.hideProgressDialog();
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
        }).execute();
    }
}
