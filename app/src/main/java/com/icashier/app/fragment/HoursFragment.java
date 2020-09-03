package com.icashier.app.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.HoursAdapter;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentHoursBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.GetHoursResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HoursFragment extends Fragment {

    HomeActivity context;
    FragmentHoursBinding binding;
    String mFrom = "", mTo = "", nFrom = "", nTo = "";
    RestClient.ApiRequest apiRequest;
    List<GetHoursResponse.ResultBean> timingsList = new ArrayList<>();
    HoursAdapter hoursAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (HomeActivity) getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hours, container, false);

        setupScreen();
        return binding.getRoot();
    }

    private void setupScreen() {
        for (int i = 0; i < 7; i++) {
            GetHoursResponse.ResultBean getHoursResponse = new GetHoursResponse.ResultBean();
            getHoursResponse.setM_from("00:00 AM");
            getHoursResponse.setM_to("00:00 AM");
            getHoursResponse.setN_from("00:00 AM");
            getHoursResponse.setN_to("00:00 AM");
            timingsList.add(getHoursResponse);
        }
        setAdapter(timingsList);
        clickListener();
        getTimings();
    }

    private void getTimings() {
        if (Utilities.isNetworkAvailable(context)) {
            //AlertUtil.showProgressDialog(context);

            apiRequest = new RestClient.ApiRequest(context);
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.TIMINGS)
                    .setMethod(RestClient.ApiRequest.METHOD_GET)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (response != null && !response.equals("")) {
                                GetHoursResponse getHoursResponse = new Gson().fromJson(response, GetHoursResponse.class);
                                if (getHoursResponse.getCode() == 200) {
                                    if (getHoursResponse.getResult().size() == 0) {
                                        for (int i = 0; i < 7; i++) {
                                            GetHoursResponse.ResultBean getHoursResponse1 = new GetHoursResponse.ResultBean();
                                            getHoursResponse1.setM_from("00:00 AM");
                                            getHoursResponse1.setM_to("00:00 AM");
                                            getHoursResponse1.setN_from("00:00 AM");
                                            getHoursResponse1.setN_to("00:00 AM");
                                            timingsList.add(getHoursResponse1);
                                        }
                                        hoursAdapter.notifyDataSetChanged();
                                    } else {
                                        timingsList.clear();
                                        timingsList.addAll(getHoursResponse.getResult());
                                        hoursAdapter.notifyDataSetChanged();
                                    }
                                } else
                                    Toast.makeText(context, getHoursResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setErrorListener(new RestClient.ErrorListener() {
                @Override
                public void onError(String tag, String errorMsg) {
                    AlertUtil.hideProgressDialog();
                }
            }).execute();
        } else
            Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
    }

    private void clickListener() {
        binding.flSave.setOnClickListener(V -> {
            try {
                if (checkTimings()) {
                    callSaveTimings();
                } else
                    Toast.makeText(context, context.getString(R.string.error_timings), Toast.LENGTH_SHORT).show();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    private void callSaveTimings() {
        if (Utilities.isNetworkAvailable(context)) {
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> map = new HashMap<>();
            String days = "Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday";
            map.put("day", days);
            map.put("m_from", mFrom);
            map.put("m_to", mTo);
            map.put("n_from", nFrom);
            map.put("n_to", nTo);
            apiRequest = new RestClient.ApiRequest(context);
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.SAVE_TIMINGS)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(map)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                            Toast.makeText(context, genericResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).setErrorListener(new RestClient.ErrorListener() {
                @Override
                public void onError(String tag, String errorMsg) {
                    AlertUtil.hideProgressDialog();
                }
            }).execute();
        } else
            Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
    }

    private boolean checkTimings() throws ParseException {
        boolean flag = true;
        int i = 0;
        while (i < HoursAdapter.timingsList.size()) {
            if (mFrom.equals(""))
                mFrom = HoursAdapter.timingsList.get(i).getM_from();
            else mFrom = mFrom + "," + HoursAdapter.timingsList.get(i).getM_from();
            if (mTo.equals(""))
                mTo = HoursAdapter.timingsList.get(i).getM_to();
            else mTo = mTo + "," + HoursAdapter.timingsList.get(i).getM_to();
            if (nFrom.equals(""))
                nFrom = HoursAdapter.timingsList.get(i).getN_from();
            else nFrom = nFrom + "," + HoursAdapter.timingsList.get(i).getN_from();
            if (nTo.equals(""))
                nTo = HoursAdapter.timingsList.get(i).getN_to();
            else nTo = nTo + "," + HoursAdapter.timingsList.get(i).getN_to();
            String time1 = HoursAdapter.timingsList.get(i).getM_from().toLowerCase();
            String time2 = HoursAdapter.timingsList.get(i).getM_to().toLowerCase();
            String time3 = HoursAdapter.timingsList.get(i).getN_from().toLowerCase();
            String time4 = HoursAdapter.timingsList.get(i).getN_to().toLowerCase();
            SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
            Date date1 = format.parse(time1);
            Date date2 = format.parse(time2);
            Date date3 = format.parse(time3);
            Date date4 = format.parse(time4);
            if (date2.getTime() - date1.getTime() < 0) {
                flag = false;
            }
            if (date4.getTime() - date3.getTime() < 0) {
                flag = false;
            }
            i++;
        }
        return flag;
    }

    private void setAdapter(List<GetHoursResponse.ResultBean> timingsList) {
        binding.rvDays.setLayoutManager(new LinearLayoutManager(context));
        hoursAdapter = new HoursAdapter(context, timingsList);
        binding.rvDays.setAdapter(hoursAdapter);
    }

}
