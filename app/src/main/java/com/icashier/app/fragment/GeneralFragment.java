package com.icashier.app.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentGeneralBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.GetGeneralSettingsResponse;


import java.util.HashMap;


public class GeneralFragment extends Fragment {


    HomeActivity context;
    FragmentGeneralBinding binding;
    RestClient.ApiRequest apiRequest;
    int min=5;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = (HomeActivity) getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_general, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupScreen();
    }

    private void setupScreen() {
        callGetSettings();
        onViewClickListeners();
        setSeekListener();
    }

    private void setSeekListener() {
        binding.seekbarDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i=i+min;
                binding.tvProgess.setText(getTime(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void callGetSettings() {
        if (Utilities.isNetworkAvailable(context)) {
            //AlertUtil.showProgressDialog(context);

            apiRequest = new RestClient.ApiRequest(context);
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.SAVE_GENERAL_SETTING)
                    .setMethod(RestClient.ApiRequest.METHOD_GET)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (response != null) {
                                GetGeneralSettingsResponse getGeneralSettingsResponse = new Gson().fromJson(response, GetGeneralSettingsResponse.class);
                                if (getGeneralSettingsResponse.getCode() == 200) {
                                    if(getGeneralSettingsResponse.getResult()!=null){
                                        updateUI(getGeneralSettingsResponse);

                                    }
                                }
                            }
                        }
                    }).setErrorListener(new RestClient.ErrorListener() {
                @Override
                public void onError(String tag, String errorMsg) {
                    AlertUtil.hideProgressDialog();
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }).execute();

        } else
            Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
    }

    private void updateUI(GetGeneralSettingsResponse getGeneralSettingsResponse) {
        if (getGeneralSettingsResponse.getResult().getNotify() == 0) {
            binding.switchNotification.setChecked(false);
        } else binding.switchNotification.setChecked(true);
        if (getGeneralSettingsResponse.getResult().getCOD() == 0) {
            binding.switchCod.setChecked(false);
            SharedPrefManager.getInstance(context).saveBoolean(AppConstant.COD_ENABLED,false);
        } else{
            binding.switchCod.setChecked(true);
            SharedPrefManager.getInstance(context).saveBoolean(AppConstant.COD_ENABLED,true);
        }
        if (getGeneralSettingsResponse.getResult().getDeliveryService() == 0) {
            binding.switchDelServices.setChecked(false);
        } else binding.switchDelServices.setChecked(true);
        if (getGeneralSettingsResponse.getResult().getAutoCashier() == 0) {
            binding.switchAutoCashier.setChecked(false);
        } else binding.switchAutoCashier.setChecked(true);
        binding.seekbarDuration.setOnSeekBarChangeListener(null);
        binding.tvProgess.setText(getTime(getGeneralSettingsResponse.getResult().getDeliveryDuration()));
        binding.seekbarDuration.setProgress(getGeneralSettingsResponse.getResult().getDeliveryDuration());
        setSeekListener();

    }

    private void onViewClickListeners() {
        binding.flSave.setOnClickListener(V -> {
            callSaveSettings();
        });

    }

    private void callSaveSettings() {
        if (Utilities.isNetworkAvailable(context)) {
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> map = new HashMap<>();
            if (binding.switchNotification.isChecked())
                map.put("notify", "1");
            else map.put("notify", "0");
            if (binding.switchCod.isChecked())
                map.put("COD", "1");
            else map.put("COD", "0");
            if (binding.switchAutoCashier.isChecked())
                map.put("autoCashier", "1");
            else map.put("autoCashier", "0");
            if (binding.switchDelServices.isChecked())
                map.put("deliveryService", "1");
            else map.put("deliveryService", "0");
            map.put("deliveryDuration", String.valueOf(binding.seekbarDuration.getProgress()+5));
            apiRequest = new RestClient.ApiRequest(context);
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.SAVE_GENERAL_SETTING)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(map)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (response != null) {
                                GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                                Toast.makeText(context, genericResponse.getMessage(), Toast.LENGTH_SHORT).show();

                                callGetSettings();
                            }
                        }
                    }).setErrorListener(new RestClient.ErrorListener() {
                @Override
                public void onError(String tag, String errorMsg) {
                    AlertUtil.hideProgressDialog();
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }).execute();

        } else
            Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
    }

    public  String getTime(int text)
    {
        String time="";
        if(text<60){
            time=text+ " "+context.getString(R.string.mins);
        }else{
            int hr=(text / 60)/60;
            int min=(text / 60)%60;//text%60;

            if(hr>0){
                if(min>0){
                    if(hr>1){
                        if(min>1){
                            time=""+hr+" "+context.getString(R.string._hours)+" "+min+ " "+getString(R.string.mins);
                        }else{
                            time=""+hr+" "+context.getString(R.string._hours)+" "+min+" "+context.getString(R.string.min);
                        }

                    }else{
                        if(min>1){
                            time=""+hr+" "+context.getString(R.string.hour)+" "+min+ " "+context.getString(R.string.mins);;

                        }else{
                            time=""+hr+" "+context.getString(R.string.hour)+" "+min+ " "+context.getString(R.string.min);;

                        }
                    }

                }else{
                    if(hr>1){
                        time=""+hr+" "+context.getString(R.string._hours);

                    }else{
                        time=""+hr+" "+context.getString(R.string.hour);
                    }
                }
            }else{
                time=""+min+ " "+getString(R.string.mins);
            }
        }
        return time;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(apiRequest!=null)
        apiRequest.cancelRequest();
    }
}
