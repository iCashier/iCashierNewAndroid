package com.icashier.app.fragment;


import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.CategoryExpandableAdapter;
import com.icashier.app.adapter.CategoryIconsAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentCategoriesBinding;
import com.icashier.app.dialog.EditCategoryDialog;
import com.icashier.app.dialog.EditPrimaryCategoryDialog;
import com.icashier.app.dialog.SearchCategoryDialog;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.listener.CatSelectedListener;
import com.icashier.app.listener.EditCategoryDialogListener;
import com.icashier.app.listener.PrimaryCategoryListener;
import com.icashier.app.listener.SelectIconListener;
import com.icashier.app.model.CategoryImagesResponse;
import com.icashier.app.model.CategoryListReponse;
import com.icashier.app.model.DeleteCatResponse;
import com.icashier.app.model.PredefinedCategoriesResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class CategoriesFragment extends Fragment {

    FragmentCategoriesBinding binding;
    HomeActivity context;
    CategoryExpandableAdapter categoryExpandableAdapter;
    List<String> namePrimaryList=new ArrayList<>();
    List<String> predefinedList=new ArrayList<>();

    RestClient.ApiRequest apiRequest;
    SearchCategoryDialog searchCategoryDialog;
    List<CategoryImagesResponse.ResultBean> imagesList=new ArrayList<>();
    List<CategoryListReponse.ResultBean> categoryList=new ArrayList<>();
    ArrayAdapter listPopupAdapter,listPopupAdapter2;
    CategoryIconsAdapter categoryIconsAdapter;
    String iconId="",categoryId="",name="",arabicName="";
    EditCategoryDialog editCategoryDialog;
    EditPrimaryCategoryDialog editPrimaryCategoryDialog;
    List<PredefinedCategoriesResponse.ResultBean> predefinedCategories=new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity)getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_categories,container,false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING|WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        listPopupAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item,namePrimaryList);
        listPopupAdapter2 = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item,predefinedList);

        setOnClickListeners();
        callGetPredefinedCategoryApi();
        setCategoriesAdapter();
        setCategoryImagesAdapter();
        callGetCategoryListApi();
        setRadioButton();
        return binding.getRoot();
    }

    //================Perform action of radio button change============//
    private void setRadioButton() {
        binding.rbPrimary.setOnCheckedChangeListener((rb,isChecked)->{
            if(isChecked){
                binding.clDropDown.setVisibility(View.GONE);
                binding.tvPrimaryCategroy.setVisibility(View.VISIBLE);
                binding.tvTitle.setVisibility(View.VISIBLE);
                binding.etTittle.setVisibility(View.GONE);
                binding.tvSubTitle.setVisibility(View.GONE);
                categoryId="";
            }
        });

        binding.rbSecondary.setOnCheckedChangeListener((rb,isChecked)->{
            if(isChecked){
                binding.clDropDown.setVisibility(View.VISIBLE);
                binding.tvPrimaryCategroy.setVisibility(View.GONE);
                binding.tvTitle.setVisibility(View.GONE);
                binding.etTittle.setVisibility(View.VISIBLE);
                binding.tvSubTitle.setVisibility(View.VISIBLE);
            }
        });
    }


    //=============Mehtod to set onClick listeners=================//
    private void setOnClickListeners() {

        binding.tvDropDown.setOnClickListener(V->{
            showPrimaryCategoryPopup();
        });

        binding.flSaveCategory.setOnClickListener(V->{
            if(isCategoryDataValid())
                callAddCategoryApi();
        });

        binding.llParentLayout.setOnClickListener(V->{
            Utilities.hideSoftKeyboard(context);
            context.closeDrawer();
        });

        binding.tvPrimaryCategroy.setOnClickListener(V->{
            searchCategoryDialog= new SearchCategoryDialog(context, predefinedCategories, new CatSelectedListener() {
                @Override
                public void onCatSelected(PredefinedCategoriesResponse.ResultBean cat) {
                    arabicName=cat.getArabic_name();
                    name=cat.getEnglish_name();
                    if (Locale.getDefault().equals(new Locale("ar","MA"))) {
                        binding.tvPrimaryCategroy.setText(arabicName);

                    }else{
                        binding.tvPrimaryCategroy.setText(name);

                    }
                    searchCategoryDialog.dismiss();
                }

                @Override
                public void onCatSelected(String cat) {



                }
            });
           searchCategoryDialog.show();
        });
    }
    //===========Method to set adapter for category icons====================//
    private void setCategoryImagesAdapter(){
        categoryIconsAdapter=new CategoryIconsAdapter(context, imagesList, new SelectIconListener() {
            @Override
            public void onIconSelected(int position) {
                iconId=""+imagesList.get(position).getId();
            }
        });
        binding.rvCategoryImages.setLayoutManager(new GridLayoutManager(context,15));
        binding.rvCategoryImages.setAdapter(categoryIconsAdapter);

        callGetIconsListApi();
    }
    //===========Method to set adapter for categories====================//
    private void setCategoriesAdapter() {

        categoryExpandableAdapter=new CategoryExpandableAdapter(context, categoryList, new PrimaryCategoryListener() {
            @Override
            public void onDeleteClick(int position) {

                AlertUtil.showAlertWindow(context, getString(R.string.delete_category_alert), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callDeleteCategoryApi(""+categoryList.get(position).getId(),"",position,0);
                    }
                });
            }

            @Override
            public void onEditClick(int position) {


                editPrimaryCategoryDialog =new EditPrimaryCategoryDialog(context,categoryList.get(position).getName(),categoryList.get(position).getArabic_name(),predefinedList, new EditPrimaryCategoryDialog.UpdateCategory(){

                    @Override
                    public void onUpdateClick(int index) {
                        callUpdateCategoryApi(predefinedCategories.get(index).getEnglish_name(),predefinedCategories.get(index).getArabic_name(),""+categoryList.get(position).getId(),"");
                        editPrimaryCategoryDialog.dismiss();
                    }
                });

                editPrimaryCategoryDialog.show();
            }

            @Override
            public void onEditClick(int groupPosition, int childPosition) {
                editCategoryDialog=new EditCategoryDialog(context,categoryList.get(groupPosition).getSubCategories().get(childPosition).getName(), new EditCategoryDialogListener() {
                    @Override
                    public void onUpdateClick(String tittle) {
                        callUpdateCategoryApi(tittle,"",""+categoryList.get(groupPosition).getId(),""+categoryList.get(groupPosition).getSubCategories().get(childPosition).getId());
                        editCategoryDialog.dismiss();
                    }
                });
                editCategoryDialog.show();
            }

            @Override
            public void onDeleteClick(int groupPositon, int childPosition) {

                AlertUtil.showAlertWindow(context, getString(R.string.delete_subcat_alert), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callDeleteCategoryApi(""+categoryList.get(groupPositon).getId(),""+categoryList.get(groupPositon).getSubCategories().get(childPosition).getId(),groupPositon,childPosition);
                    }
                });

            }

            @Override
            public void onExpanded(int groupPosition) {
                setListViewHeight(binding.lvExp, groupPosition);
            }
        });
        binding.lvExp.setAdapter(categoryExpandableAdapter);
        binding.lvExp.setGroupIndicator(null);
        binding.lvExp.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }

    //======================Mehtod to show primary category list popup=======================//
    private void showPrimaryCategoryPopup() {

        ListPopupWindow popupWindow=new ListPopupWindow(context);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int index, long id) {

                binding.tvDropDown.setText(namePrimaryList.get(index));
                categoryId=""+categoryList.get(index).getId();
                popupWindow.dismiss();
            }
        });

        popupWindow.setContentWidth(binding.tvDropDown.getWidth());
        popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setAnchorView(binding.tvDropDown);
        popupWindow.setAdapter(listPopupAdapter);
        popupWindow.show();

    }

    //======================Mehtod to show primary category list popup=======================//
    private void showPredefinedCategoryPopup() {

        ListPopupWindow popupWindow=new ListPopupWindow(context);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int index, long id) {

                binding.tvPrimaryCategroy.setText(predefinedList.get(index));
                popupWindow.dismiss();
            }
        });

        popupWindow.setContentWidth(binding.tvPrimaryCategroy.getWidth());
        popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setAnchorView(binding.tvPrimaryCategroy);
        popupWindow.setAdapter(listPopupAdapter2);
        popupWindow.show();

    }
    //==========================Method to call get category icons api=====================//
    private void callAddCategoryApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("icon_id",iconId);

            if(!categoryId.equals("")){
                params.put("cid",categoryId);
                params.put("name",binding.etTittle.getText().toString().trim());
            }else{
                params.put("name",name);
                params.put("arabic_name",arabicName);

            }


            HashMap<String,String> headers=new HashMap<>();
            headers.put("token",SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));
            headers.put("type",SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE,AppConstant.TYPE_MERCHANT));
            headers.put("cashiertoken",SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN,""));

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.ADD_CATEGORY)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeaders(headers)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    CategoryListReponse categoryListReponse= new Gson().fromJson(response, CategoryListReponse.class);
                                    if (categoryListReponse != null) {
                                        AlertUtil.hideProgressDialog();
                                        if (categoryListReponse.getCode()==200 ) {

                                            categoryList.clear();
                                            categoryList.addAll(categoryListReponse.getResult());

                                            if(categoryList.size()>0){
                                                binding.tvExistingCategories.setVisibility(View.VISIBLE);
                                            }
                                            namePrimaryList.clear();
                                            for(int i=0;i<categoryList.size();i++){
                                                if (Locale.getDefault().equals(new Locale("ar","MA"))) {
                                                    namePrimaryList.add(categoryList.get(i).getArabic_name());
                                                }else{
                                                    namePrimaryList.add(categoryList.get(i).getName());
                                                }
                                            }
                                            listPopupAdapter.notifyDataSetChanged();
                                            categoryExpandableAdapter.notifyDataSetChanged();
                                            if(categoryList.size()>0){
                                                setListViewHeight(binding.lvExp, 0);
                                            }


                                            binding.etTittle.setText("");
                                            binding.tvPrimaryCategroy.setText("");

                                            iconId="";

                                            for(int i=0;i<imagesList.size();i++){
                                                imagesList.get(i).setSelected(false);
                                            }
                                            categoryIconsAdapter.notifyDataSetChanged();
                                        } else if(categoryListReponse.getCode()==301)
                                        {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                    callAddCategoryApi();
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
            AlertUtil.toastMsg(context,getString(R.string.network_error));
        }
    }


    //==========================Method to call get category icons api=====================//
    private void callDeleteCategoryApi(String cid,String scId,int groupPosition,int childPosition) {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("id",cid);

            if(!scId.equals("")){
                params.put("scid",scId);
            }else{
              params.put("otherid",""+categoryList.get(groupPosition).getSubCategories().get(childPosition).getId());
            }


            HashMap<String,String> headers=new HashMap<>();
            headers.put("token",SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));
            headers.put("type",SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE,AppConstant.TYPE_MERCHANT));
            headers.put("cashiertoken",SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN,""));

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.DELETE_CATEGORY)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeaders(headers)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                DeleteCatResponse genericResponse= new Gson().fromJson(response, DeleteCatResponse.class);
                                if (genericResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (genericResponse.getCode()==200 ) {
                                        if(!scId.equals("")){
                                            categoryList.get(groupPosition).getSubCategories().remove(childPosition);
                                        } else{
                                            categoryList.remove(groupPosition);
                                            namePrimaryList.remove(groupPosition);
                                        }


                                        categoryIconsAdapter.notifyDataSetChanged();
                                        categoryExpandableAdapter.notifyDataSetChanged();
                                        if(categoryList.size()>0){
                                            setListViewHeight(binding.lvExp, 0);
                                        }
                                    }else if(genericResponse.getCode()==301)
                                    {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callDeleteCategoryApi(cid,scId,groupPosition,childPosition);
                                            }
                                        });
                                    }
                                    AlertUtil.toastMsgLong(context, genericResponse.getMessage());
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
            AlertUtil.toastMsg(context,getString(R.string.network_error));
        }
    }

    //==========================Method to call get category icons api=====================//
    private void callGetIconsListApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            //AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            apiRequest = new RestClient.ApiRequest(context);

            HashMap<String,String> headers=new HashMap<>();
            headers.put("token",SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));
            headers.put("type",SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE,AppConstant.TYPE_MERCHANT));
            headers.put("cashiertoken",SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN,""));

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_CATEGORY_ICONS)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeaders(headers)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                CategoryImagesResponse categoryImagesResponse= new Gson().fromJson(response, CategoryImagesResponse.class);
                                if (categoryImagesResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (categoryImagesResponse.getStatusCode()==200 ) {

                                        imagesList.addAll(categoryImagesResponse.getResult());

                                        categoryIconsAdapter.notifyDataSetChanged();
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
            AlertUtil.toastMsg(context,getString(R.string.network_error));
        }
    }

    //==========================Method to call get category icons api=====================//
    private void callGetCategoryListApi() {

        if(Utilities.isNetworkAvailable(context)) {
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
                                CategoryListReponse categoryListReponse= new Gson().fromJson(response, CategoryListReponse.class);
                                if (categoryListReponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (categoryListReponse.getCode()==200 ) {
                                        categoryList.clear();
                                        categoryList.addAll(categoryListReponse.getResult());
                                        if(categoryList.size()>0){
                                            binding.tvExistingCategories.setVisibility(View.VISIBLE);
                                        }
                                        for(int i=0;i<categoryList.size();i++){
                                            if (Locale.getDefault().equals(new Locale("ar","MA"))) {
                                                namePrimaryList.add(categoryList.get(i).getArabic_name());
                                            }else{
                                                namePrimaryList.add(categoryList.get(i).getName());
                                            }                                        }
                                        listPopupAdapter.notifyDataSetChanged();
                                        categoryExpandableAdapter.notifyDataSetChanged();
                                        if(categoryList.size()>0){
                                            setListViewHeight(binding.lvExp, 0);
                                        }
                                    }else if(categoryListReponse.getCode()==301)
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
        }else
        {
            AlertUtil.toastMsg(context,getString(R.string.network_error));
        }
    }

    //==========================Method to call get category icons api=====================//
    private void callUpdateCategoryApi(String catName,String arabicName,String cId,String scId) {

        Utilities.hideSoftKeyboard(context);
        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("name",catName);
            params.put("cid",cId);
            if(!scId.equals("")) {
                params.put("scid", scId);
            }else{
                params.put("arabic_name",arabicName);
            }


            HashMap<String,String> headers=new HashMap<>();
            headers.put("token",SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));
            headers.put("type",SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE,AppConstant.TYPE_MERCHANT));
            headers.put("cashiertoken",SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN,""));
            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.UPDATE_CATEGORY)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeaders(headers)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    CategoryListReponse categoryListReponse= new Gson().fromJson(response, CategoryListReponse.class);
                                    if (categoryListReponse != null) {
                                        AlertUtil.hideProgressDialog();
                                        if (categoryListReponse.getCode()==200 ) {
                                            categoryList.clear();
                                            categoryList.addAll(categoryListReponse.getResult());
                                            if(categoryList.size()>0){
                                                binding.tvExistingCategories.setVisibility(View.VISIBLE);
                                            }
                                            namePrimaryList.clear();
                                            for(int i=0;i<categoryList.size();i++){
                                                if (Locale.getDefault().equals(new Locale("ar","MA"))) {
                                                    namePrimaryList.add(categoryList.get(i).getArabic_name());
                                                }else{
                                                    namePrimaryList.add(categoryList.get(i).getName());
                                                }
                                            }
                                            listPopupAdapter.notifyDataSetChanged();
                                            categoryExpandableAdapter.notifyDataSetChanged();
                                            if(categoryList.size()>0){
                                                setListViewHeight(binding.lvExp, 0);
                                            }
                                        }else if(categoryListReponse.getCode()==301)
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

    //==================Method to validate category data===================//
    private boolean isCategoryDataValid(){



            if(binding.rbPrimary.isChecked())
            {
                if(!binding.tvTitle.getText().toString().trim().equals("")) {
                    if (!iconId.equals("")) {
                        return true;
                    } else {
                        AlertUtil.toastMsg(context, getString(R.string.empty_icon));
                    }
                }
                else
                {
                    AlertUtil.toastMsg(context,getString(R.string.empty_title));
                }
            }
            else if(binding.rbSecondary.isChecked())
            {
                if(!binding.etTittle.getText().toString().trim().equals("")) {
                    if(!binding.tvDropDown.getText().toString().trim().equals("")){
                    if(!iconId.equals(""))
                    {
                        return true;
                    }else
                    {
                        AlertUtil.toastMsg(context,getString(R.string.empty_icon));
                    }
                }else{
                    AlertUtil.toastMsg(context,getString(R.string.empty_primary_category));
                }
                }
                else
                {
                    AlertUtil.toastMsg(context,getString(R.string.empty_title));
                }

            }



        return false;
    }

    //==========================Method to call get category icons api=====================//
    private void callGetPredefinedCategoryApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            //AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            apiRequest = new RestClient.ApiRequest(context);


            HashMap<String,String> headers=new HashMap<>();
            headers.put("token",SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));
            headers.put("type",SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE,AppConstant.TYPE_MERCHANT));
            headers.put("cashiertoken",SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN,""));

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_PREDEFINED_CATEGORIES)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeaders(headers)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                PredefinedCategoriesResponse categoryListReponse= new Gson().fromJson(response, PredefinedCategoriesResponse.class);
                                if (categoryListReponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (categoryListReponse.getCode()==200 ) {

                                        predefinedCategories.addAll(categoryListReponse.getResult());
                                        if (Locale.getDefault().equals(new Locale("ar","MA"))) {
                                            for(int i=0;i<categoryListReponse.getResult().size();i++){
                                                predefinedList.add(categoryListReponse.getResult().get(i).getArabic_name());
                                            }
                                        }else{
                                            for(int i=0;i<categoryListReponse.getResult().size();i++){
                                                predefinedList.add(categoryListReponse.getResult().get(i).getEnglish_name());
                                            }
                                        }
                                        listPopupAdapter2.notifyDataSetChanged();
                                    }else if(categoryListReponse.getCode()==301)
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
        }else
        {
            AlertUtil.toastMsg(context,getString(R.string.network_error));
        }
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

}
