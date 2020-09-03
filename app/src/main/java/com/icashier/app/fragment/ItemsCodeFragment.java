package com.icashier.app.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentItemsCodeBinding;
import com.icashier.app.dialog.SelectCouponDialog;
import com.icashier.app.dialog.SelectDealDialog;
import com.icashier.app.dialog.SelectItemDialog;
import com.icashier.app.dialog.SelectMealDialog;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.listener.DialogDismissListener;
import com.icashier.app.model.ExistingDealsModel;
import com.icashier.app.model.ExistingItemList;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.GetCouponsResponse;
import com.icashier.app.model.MealListResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ItemsCodeFragment extends Fragment {


    HomeActivity context;
    FragmentItemsCodeBinding binding;
    RestClient.ApiRequest apiRequest;
    List<ExistingItemList.ResultBean> itemList=new ArrayList<>();
    List<MealListResponse.ResultBean> mealList=new ArrayList<>();
    List<ExistingDealsModel.ResultBean> dealList=new ArrayList<>();
    List<GetCouponsResponse.ResultBean> couponsList = new ArrayList<>();

    List<String> itemNamesList=new ArrayList<>();
    ExistingItemList.ResultBean item;
    MealListResponse.ResultBean meal;
    ExistingDealsModel.ResultBean deal;
    GetCouponsResponse.ResultBean coupon;
    String paymentMode="",deliveryMode="";
    boolean isCreditCard,isCash,isPaypal;
    boolean isDelivery,isPickup,isInstaDelivery,isSwiftDelivery;
    String msg="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity)getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_items_code, container, false);
        setOnClickListeners();
        callGetItemListApi();
        callGetMealListApi();
        callGetDealsApi();
        callGetCoupons();
        setRadioButton();
        return binding.getRoot();
    }

    //===============Mehtod to implememnt radio button listener=========//
    private void setRadioButton() {

        binding.rbDeal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    meal = null;
                    deal = null;
                    item = null;
                    binding.tvItem.setText("");
                    binding.flItemImg.setVisibility(View.GONE);
                    msg = getString(R.string.empty_deal_qr);
                }
            }
        });

        binding.rbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    meal = null;
                    deal = null;
                    item = null;
                    coupon = null;
                    binding.tvItem.setText("");
                    binding.flItemImg.setVisibility(View.GONE);
                    msg = getString(R.string.empty_item_qr);
                }
            }
        });

        binding.rbMeal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    meal = null;
                    deal = null;
                    item = null;
                    coupon = null;
                    binding.tvItem.setText("");
                    binding.flItemImg.setVisibility(View.GONE);
                    msg = getString(R.string.empty_meal_qr);
                }
            }
        });

        binding.rbCoupon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    meal = null;
                    deal = null;
                    item = null;
                    coupon = null;
                    binding.tvItem.setText("");
                    binding.flItemImg.setVisibility(View.GONE);
                    msg = getString(R.string.empty_coupon_qr);
                }
            }
        });
    }

    //======================Method to set onClick listeners================//
    private void setOnClickListeners() {

        binding.clParentLayout.setOnClickListener(V->{
            context.closeDrawer();
            Utilities.hideSoftKeyboard(context);
        });

        binding.llCash.setOnClickListener(V->{
            setPaymentSelector(binding.imgCash);
        });

        binding.llCreditCard.setOnClickListener(V->{
            setPaymentSelector(binding.imgCreditCard);
        });

        binding.llPaypal.setOnClickListener(V->{
            setPaymentSelector(binding.imgPaypal);
        });

        binding.tvItem.setOnClickListener(V->{
            showDialog();
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

        binding.flSave.setOnClickListener(V->{
            if(isValidInput()){
                callGenerateCodeApi();
            }
        });
    }

    //================Method to show dialog to select item ,meal or deal=========//
    private void showDialog() {

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
                        }
                        binding.flItemImg.setVisibility(View.VISIBLE);
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

    //============Method to call generate qr api===========//
    private void callGenerateCodeApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            AlertUtil.showProgressDialog(context);

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

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.CREATE_QR)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
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
                                            isCreditCard=false;
                                            isCash=false;
                                            isPaypal=false;
                                            isDelivery=false;
                                            isPickup=false;
                                            isInstaDelivery=false;
                                            isSwiftDelivery=false;
                                            paymentMode="";
                                            deliveryMode="";
                                            binding.flItemImg.setVisibility(View.GONE);
                                            item=null;
                                            meal=null;
                                            deal=null;
                                            coupon=null;

                                            AlertUtil.toastMsg(context,genericResponse.getMessage());

                                        } else if(genericResponse.getCode()==301)
                                        {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                    callGenerateCodeApi();
                                                }
                                            });
                                        }else{
                                            AlertUtil.toastMsg(context, genericResponse.getMessage());
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
            AlertUtil.toastMsg(context,getString(R.string.network_error));
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
                    AlertUtil.toastMsg(context,getString(R.string.empty_delivery_mode));
                }
            }else{
                AlertUtil.toastMsg(context,getString(R.string.empty_payment_mode));
            }
        }else{
            AlertUtil.toastMsg(context,msg);
        }
        return false;
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
                                            itemList.addAll(existingItemList.getResult());
                                            for(int i=0;i<itemList.size();i++){
                                                itemNamesList.add(itemList.get(i).getName());
                                            }
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
            //AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.DEALS)
                    .setMethod(RestClient.ApiRequest.METHOD_GET)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                ExistingDealsModel existingDealsModel= new Gson().fromJson(response, ExistingDealsModel.class);
                                if (existingDealsModel != null) {
                                    if(existingDealsModel.getCode()==200){
                                        if(existingDealsModel.getResult().size()>0){
                                            dealList.clear();
                                            dealList.addAll(existingDealsModel.getResult());
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
            AlertUtil.toastMsg(context,getString(R.string.network_error));
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
                        } else
                            Toast.makeText(context, getCouponsResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).setErrorListener((tag, errorMsg) -> {
            AlertUtil.hideProgressDialog();
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
        }).execute();
    }
}
