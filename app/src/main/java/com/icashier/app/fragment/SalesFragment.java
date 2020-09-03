package com.icashier.app.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.SalesFilterAdapter;
import com.icashier.app.adapter.TopItemsAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentSalesBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.DateUtils;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.GetDob;
import com.icashier.app.listener.SalesFilterListener;
import com.icashier.app.model.SalesDataModel;
import com.icashier.app.model.WeekDaysModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SalesFragment extends Fragment {


    HomeActivity context;
    FragmentSalesBinding binding;
    RestClient.ApiRequest apiRequest;
    int day,month,year;
    String fromDate="",toDate="";
    boolean firstDate;
    List<WeekDaysModel> filterList=new ArrayList<>();
    SalesFilterAdapter salesFilterAdapter;
    TopItemsAdapter topItemsAdapter;
    List<SalesDataModel.ResultBeanX.TopItemsBean> itemsList=new ArrayList<>();
    List<String> xLabels=new ArrayList<>();
    List<Integer> yLabels=new ArrayList<>();
    double range=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity)getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_sales, container, false);

        setOnClickListener();
        setFilter();
        setAdapter();
        setItemsAdapter();
        callGetSalesDataApi();

        return binding.getRoot();
    }

    private void setItemsAdapter() {
        topItemsAdapter=new TopItemsAdapter(context,itemsList);
        binding.rvTopItems.setAdapter(topItemsAdapter);
        binding.rvTopItems.setLayoutManager(new LinearLayoutManager(context));
    }

    private void setAdapter() {
        salesFilterAdapter=new SalesFilterAdapter(context, filterList, new SalesFilterListener() {
            @Override
            public void onFiltered() {
                binding.tvSelectDate.setText("");
                fromDate="";
                toDate="";
                callGetSalesDataApi();
            }
        });
        binding.rvFilter.setAdapter(salesFilterAdapter);
        LinearLayoutManager mLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        binding.rvFilter.setLayoutManager(mLayoutManager );
    }

    private void setFilter() {
        filterList.add(new WeekDaysModel(context.getString(R.string.today),false));
        filterList.add(new WeekDaysModel(context.getString(R.string.week),true));
        filterList.add(new WeekDaysModel(context.getString(R.string.month),false));
        filterList.add(new WeekDaysModel(context.getString(R.string.months_3),false));

    }

    private void setOnClickListener() {

        binding.tvSelectDate.setOnClickListener(V->{
            getDate();
        });
    }
    private void getDate() {
        DateUtils.openDatePicker3(context, day, month, year, new GetDob() {
            @Override
            public void getTimeSuccess(int hours, int minutes) {

            }

            @Override
            public void getDateSuccess(int _day, int _month, int _year) {
                day=_day;
                month=_month;
                year=_year;
                if(!firstDate){
                    fromDate=""+year+"-"+month+"-"+day;
                    firstDate=true;
                    getDate();
                }else{
                    toDate=""+year+"-"+month+"-"+day;
                    firstDate=false;
                    binding.tvSelectDate.setText(DateUtils.formatDate(fromDate,"yyyy-MM-dd","dd MMM,yyyy")+" to "+DateUtils.formatDate(toDate,"yyyy-MM-dd","dd MMM,yyyy"));
                    for(int i=0;i<filterList.size();i++){
                        filterList.get(i).setSelected(false);
                    }

                    salesFilterAdapter.notifyDataSetChanged();
                    callGetSalesDataApi();
                }
            }
        });
    }

    private void setBarChart(){
        binding.chart.setDrawBarShadow(false);
        binding.chart.setDrawValueAboveBar(true);

        binding.chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the binding.chart, no values will be
        // drawn
        binding.chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        binding.chart.setPinchZoom(false);

        binding.chart.setDrawGridBackground(false);
        // binding.chart.setDrawYLabels(false);

       // ValueFormatter xAxisFormatter = new DayAxisValueFormatter(binding.chart);

        XAxis xAxis = binding.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
       // xAxis.setValueFormatter(xAxisFormatter);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value<xLabels.size()) {
                    return xLabels.get((int) value);
                }else{
                    return "";
                }
            }
        });






        YAxis leftAxis = binding.chart.getAxisLeft();
        //leftAxis.setTypeface(tfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(false);
        // this replaces setStartAtZero(true)

        YAxis rightAxis = binding.chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        //rightAxis.setTypeface(tfLight);
        rightAxis.setLabelCount(4, true);
       // rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value<999){
                    return ""+(int)Math.ceil(value) ;
                }else{
                    return formatYAxis((double)value);
                }

            }
        });

        Legend l = binding.chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        l.setEnabled(false);


        setData(xLabels.size(),range);
    }


    private void setData(int count, double range) {


        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float val = yLabels.get(i);

            values.add(new BarEntry(i, val));

        }

        BarDataSet set1;

        if (binding.chart.getData() != null &&
                binding.chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) binding.chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            binding.chart.invalidate();
            binding.chart.getData().notifyDataChanged();
            binding.chart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "The year 2017");

            set1.setDrawIcons(false);

//            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            /*int startColor = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor = ContextCompat.getColor(this, android.R.color.holo_blue_bright);
            set1.setGradientColor(startColor, endColor);*/


            set1.setColor(ContextCompat.getColor(context, R.color.turkishGreen));

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setDrawValues(false);

            data.setBarWidth(0.5f);


            binding.chart.setData(data);
            binding.chart.setVisibleXRangeMaximum(10); // allow 20 values to be displayed at once on the x-axis, not more
            binding.chart.moveViewToX(10);
            binding.chart.notifyDataSetChanged();
            binding.chart.invalidate();
        }
    }

    //===========Mehtods to call api get sales data===========//
    private void callGetSalesDataApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();

            for(int i=0;i<filterList.size();i++){
                if(filterList.get(i).isSelected()) {
                    int sum=i+1;
                    fromDate = "" + sum;
                    toDate = "";
                    break;
                }
            }
            params.put("fromDate",fromDate);
            params.put("toDate",toDate);


            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.SALES_DASHBOARD)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setHeader2("type",SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE,AppConstant.TYPE_MERCHANT))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {

                                SalesDataModel salesDataModel= new Gson().fromJson(response, SalesDataModel.class);
                                if (salesDataModel != null) {
                                    if(salesDataModel.getCode()==200){
                                        binding.salesLayout.setVisibility(View.VISIBLE);
                                        itemsList.clear();
                                        itemsList.addAll(salesDataModel.getResult().getTopItems());
                                        topItemsAdapter.notifyDataSetChanged();
                                        setSalesData(salesDataModel.getResult());

                                        xLabels.clear();
                                        yLabels.clear();
                                        for(int i=0;i<salesDataModel.getResult().getSalesReport().getResult().size();i++){
                                            xLabels.add(Utilities.formatDate(salesDataModel.getResult().getSalesReport().getResult().get(i).getDateFormatted(), "yyyy-MM-dd", "dd MMM"));
                                            yLabels.add((int)salesDataModel.getResult().getSalesReport().getResult().get(i).getSales());

                                        }
                                        range=salesDataModel.getResult().getSalesReport().getMaxSale();
                                        setBarChart();
                                    }else{
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
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

    private void setSalesData(SalesDataModel.ResultBeanX result) {
        binding.tvOrders.setText(""+result.getInsight().getOrders());
        binding.tvCoupons.setText(""+result.getInsight().getCoupons());
        binding.tvLikes.setText(""+result.getInsight().getLikes());

        binding.tvCards.setText(""+result.getPayments().get_Credit_Card());
        binding.tvCash.setText(""+result.getPayments().getCash());
        binding.tvOnline.setText(""+result.getPayments().getOnline());
        if(result.getVisitors().getVisitors()>999){
            binding.tvVisitors.setText(formatValue((double)result.getVisitors().getVisitors()));
        }else{
            binding.tvVisitors.setText(""+result.getVisitors().getVisitors());
        }
        if(result.getSales().getSales()>999){
            binding.tvSales.setText(formatValue(result.getSales().getSales()));
        }else{
            binding.tvSales.setText(""+result.getSales().getSales());
        }
        if(result.getSales().getType()!=null) {
            if (result.getSales().getType().equals("1")) {
                binding.vsDays.setVisibility(View.VISIBLE);
                binding.tvSalesPercent.setVisibility(View.VISIBLE);
                binding.tvSalesPercent.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.green, 0, 0, 0);
                binding.tvSalesPercent.setText(result.getSales().getPercent() + "%");
                binding.tvSalesPercent.setTextColor(context.getResources().getColor(R.color.lightGreen));

                setVsDays();
            } else if (result.getSales().getType().equals("-1")) {
                binding.vsDays.setVisibility(View.VISIBLE);
                binding.tvSalesPercent.setVisibility(View.VISIBLE);
                binding.tvSalesPercent.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.red, 0, 0, 0);
                binding.tvSalesPercent.setText(result.getSales().getPercent() + "%");
                binding.tvSalesPercent.setTextColor(context.getResources().getColor(R.color.colorRed));
                setVsDays();
            } else {
                binding.vsDays.setVisibility(View.GONE);
                binding.tvSalesPercent.setVisibility(View.GONE);
            }
        }else{
            binding.vsDays.setVisibility(View.GONE);
            binding.tvSalesPercent.setVisibility(View.GONE);
        }

        if(result.getVisitors().getType().equals("1")){
            binding.vsDaysVisitors.setVisibility(View.VISIBLE);
            binding.tvVisitorsPercent.setVisibility(View.VISIBLE);
            binding.tvVisitorsPercent.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.green,0,0,0);
            binding.tvVisitorsPercent.setText(result.getVisitors().getPercent()+"%");
            binding.tvVisitorsPercent.setTextColor(context.getResources().getColor(R.color.lightGreen));

            setVsDays();
        }else if(result.getVisitors().getType().equals("-1")){
            binding.vsDaysVisitors.setVisibility(View.VISIBLE);
            binding.tvVisitorsPercent.setVisibility(View.VISIBLE);
            binding.tvVisitorsPercent.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.red,0,0,0);
            binding.tvVisitorsPercent.setText(result.getVisitors().getPercent()+"%");
            binding.tvVisitorsPercent.setTextColor(context.getResources().getColor(R.color.colorRed));
            setVsDays();
        }else{
            binding.vsDaysVisitors.setVisibility(View.GONE);
            binding.tvVisitorsPercent.setVisibility(View.GONE);
        }
    }

    public  String formatValue(Double val){

        double shortForm= (Math.floor(val/1000 * 10) / 10);
        if(shortForm==(long)shortForm){
            return ""+(long)shortForm+" K";
        }else{
            return  ""+(double) (Math.floor(val/1000 * 10) / 10)+" K";
        }

    }

    private void setVsDays(){

        boolean isDateFlter=true;
        for(int i=0;i<filterList.size();i++)
            if(filterList.get(i).isSelected()){
                isDateFlter=false;
                switch (filterList.get(i).getDay()) {
                    case "Today":
                        binding.vsDays.setText(getString(R.string.vs_yesterday));
                        binding.vsDaysVisitors.setText(getString(R.string.vs_yesterday));
                        break;
                    case "Week":
                        binding.vsDays.setText(getString(R.string.vs_last_7_days));
                        binding.vsDaysVisitors.setText(getString(R.string.vs_last_7_days));

                        break;
                    case "Month":
                        binding.vsDays.setText(getString(R.string.vs_last_1_month));
                        binding.vsDaysVisitors.setText(getString(R.string.vs_last_1_month));

                        break;
                    case "3 Months":
                        binding.vsDays.setText(getString(R.string.vs_last_3_months));
                        binding.vsDaysVisitors.setText(getString(R.string.vs_last_3_months));
                        break;


                }


        }

        if(isDateFlter){
            binding.vsDays.setText(getString(R.string.vs_last)+" "+DateUtils.getDaysDiff(fromDate,toDate)+" "+getString(R.string.days));
            binding.vsDaysVisitors.setText(getString(R.string.vs_last)+" "+DateUtils.getDaysDiff(fromDate,toDate)+" "+getString(R.string.days));
        }

    }

    public  String formatYAxis(Double val){

        double shortForm= (Math.floor(val/1000 * 10) / 10);
        if(shortForm==(long)shortForm){
            return ""+(int)Math.ceil((long)shortForm)+" K";
        }else{
            return  ""+ (int)(Math.ceil(val/1000 * 10) / 10)+" K";
        }

    }

}
