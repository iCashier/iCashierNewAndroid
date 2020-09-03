package com.icashier.app.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.ItemsCodeAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentAllCodesBinding;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.listener.DialogDismissListener;
import com.icashier.app.dialog.EditDineInCodeDialog;
import com.icashier.app.dialog.EditItemCodeDialog;
import com.icashier.app.dialog.EditTakeoutCodeDialog;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CodeListItemListener;
import com.icashier.app.model.AllCodesListResponse;
import com.icashier.app.model.GenericResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllCodesFragment extends Fragment {


    HomeActivity context;
    FragmentAllCodesBinding binding;
    RestClient.ApiRequest apiRequest;
    List<AllCodesListResponse.ResultBean> allCodesList=new ArrayList<>();
    ItemsCodeAdapter adapter;
    boolean isDine=true,isTakeout=true,isItems=true;
    List<AllCodesListResponse.ResultBean> filteredList=new ArrayList<>();
    String encodedImage="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity)getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_all_codes, container, false);
        setOnClickListener();
        setAdapter();
        callGetCodestApi();
        return binding.getRoot();
    }

    //============Mehtod to set onClick listener================//
    private void setOnClickListener() {
        binding.clDinein.setOnClickListener(V->{
            if(isDine){
                isDine=false;
                binding.cbDinein.setChecked(false);
            }else{
                isDine=true;
                binding.cbDinein.setChecked(true);
            }
            filterList();
        });

        binding.clTakeout.setOnClickListener(V->{
            if(isTakeout){
                isTakeout=false;
                binding.cbTakeout.setChecked(false);
            }else{
                isTakeout=true;
                binding.cbTakeout.setChecked(true);
            }
            filterList();
        });

        binding.clItems.setOnClickListener(V->{
            if(isItems){
                isItems=false;
                binding.cbItems.setChecked(false);
            }else{
                isItems=true;
                binding.cbItems.setChecked(true);
            }
            filterList();
        });

        binding.imgShare.setOnClickListener(V->{
            shareQr();
        });
    }

    //=================Mehtod to set adapter===========//
    private void setAdapter() {
        adapter=new ItemsCodeAdapter(context, filteredList, new CodeListItemListener() {
            @Override
            public void onEdit(int position) {

                if(filteredList.get(position).getType().equalsIgnoreCase(AppConstant.TYPE_DINE_IN))
                {
                    new EditDineInCodeDialog(context, filteredList.get(position), new DialogDismissListener() {
                        @Override
                        public void onDismiss() {
                            callGetCodestApi();
                        }
                    }).show();
                }
                else if(filteredList.get(position).getType().equalsIgnoreCase(AppConstant.TYPE_PICKUP))
                {
                    new EditTakeoutCodeDialog(context,filteredList.get(position), new DialogDismissListener() {
                        @Override
                        public void onDismiss() {
                            callGetCodestApi();
                        }
                    }).show();
                }
                else if(filteredList.get(position).getType().equalsIgnoreCase(AppConstant.TYPE_ITEM)
                        ||filteredList.get(position).getType().equalsIgnoreCase(AppConstant.TYPE_MEALS)
                        ||filteredList.get(position).getType().equalsIgnoreCase(AppConstant.TYPE_DEALS)
                        ||filteredList.get(position).getType().equalsIgnoreCase(AppConstant.TYPE_COUPON))
                {
                    new EditItemCodeDialog(context,filteredList.get(position), new DialogDismissListener() {
                        @Override
                        public void onDismiss() {
                            callGetCodestApi();
                        }
                    }).show();
                }
            }

            @Override
            public void onDelete(int position) {
                AlertUtil.showAlertWindow(context, getString(R.string.delete_qr_alert), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callDeleteCodeApi(position);
                    }
                });
            }

            @Override
            public void onSelected(int position) {
                encodedImage=filteredList.get(position).getQrcode().split(",")[1];
                binding.imgQr.setImageBitmap(Utilities.getImageFromBase64(filteredList.get(position).getQrcode().split(",")[1]));
                binding.tvOrderNo.setText(getString(R.string.order)+" #"+filteredList.get(position).getId());
            }
        });
        binding.rvAll.setLayoutManager(new LinearLayoutManager(context));
        binding.rvAll.setAdapter(adapter);
    }

    //==========================Method to call get qr codes  api=====================//
    private void callGetCodestApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_QR_LIST)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                    AllCodesListResponse allCodesListResponse= new Gson().fromJson(response, AllCodesListResponse.class);
                                    if (allCodesListResponse != null) {
                                        AlertUtil.hideProgressDialog();
                                        if (allCodesListResponse.getCode()==200 ) {
                                            allCodesList.clear();
                                            allCodesList.addAll(allCodesListResponse.getResult());
                                            if(allCodesList.size()>0){
                                                filterList();

                                            }else{
                                                binding.tvEmptyView.setVisibility(View.VISIBLE);
                                                binding.llCode.setVisibility(View.GONE);
                                            }

                                        }else if(allCodesListResponse.getCode()==301)
                                        {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                    callGetCodestApi();
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

    //==========================Method to call delete qr code api=====================//
    private void callDeleteCodeApi(int position) {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));
            params.put("id",""+filteredList.get(position).getId());

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.DELETE_QR)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                GenericResponse genericResponse= new Gson().fromJson(response, GenericResponse.class);
                                if (genericResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (genericResponse.getCode()==200 ) {

                                        for(int i=0;i<allCodesList.size();i++){
                                            if(filteredList.get(position).getId()==allCodesList.get(i).getId()){
                                                allCodesList.remove(i);
                                                filteredList.remove(position);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                        if(allCodesList.size()>0){
                                            filterList();

                                        }else{

                                            binding.tvEmptyView.setVisibility(View.VISIBLE);
                                            binding.llCode.setVisibility(View.GONE);
                                            adapter.notifyDataSetChanged();
                                        }


                                    } else if(genericResponse.getCode()==301)
                                    {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callDeleteCodeApi(position);
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

    //=============Mehtod to filter list=============//
    private void filterList() {
        filteredList.clear();
        if(isDine){
            for(int i=0;i<allCodesList.size();i++){
                if(allCodesList.get(i).getType().equalsIgnoreCase(AppConstant.TYPE_DINE_IN)){
                    filteredList.add(allCodesList.get(i));
                }
            }
        }

        if(isTakeout){
            for(int i=0;i<allCodesList.size();i++){
                if(allCodesList.get(i).getType().equalsIgnoreCase(AppConstant.TYPE_PICKUP)){
                    filteredList.add(allCodesList.get(i));
                }
            }
        }

        if(isItems){
            for(int i=0;i<allCodesList.size();i++){
                if(allCodesList.get(i).getType().equalsIgnoreCase(AppConstant.TYPE_ITEM)
                        ||allCodesList.get(i).getType().equalsIgnoreCase(AppConstant.TYPE_DEALS)
                        ||allCodesList.get(i).getType().equalsIgnoreCase(AppConstant.TYPE_MEALS)
                        ||allCodesList.get(i).getType().equalsIgnoreCase(AppConstant.TYPE_COUPON)){
                    filteredList.add(allCodesList.get(i));

                }
            }
        }
        for(int i=0;i<filteredList.size();i++){
            filteredList.get(i).setSelected(false);
        }

        if(filteredList.size()>0) {
            encodedImage=filteredList.get(0).getQrcode().split(",")[1];
            binding.tvEmptyView.setVisibility(View.GONE);
            binding.llCode.setVisibility(View.VISIBLE);
            filteredList.get(0).setSelected(true);
            binding.imgQr.setImageBitmap(Utilities.getImageFromBase64(filteredList.get(0).getQrcode().split(",")[1]));
            binding.tvOrderNo.setText(context.getString(R.string.order) + " #" + filteredList.get(0).getId());
        }
        adapter.notifyDataSetChanged();
    }

    private void shareQr(){
        Bitmap bitmap =Utilities.getImageFromBase64(encodedImage);
        try {
            File file = new File(context.getExternalCacheDir(),"qr.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Share qr via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
