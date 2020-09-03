package com.icashier.app.adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.icashier.app.R;
import com.icashier.app.databinding.ItemCouponBinding;
import com.icashier.app.databinding.ItemHoursBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.ExistingItemListener;
import com.icashier.app.model.GetCouponsResponse;
import com.icashier.app.model.GetHoursResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    public static List<GetHoursResponse.ResultBean> timingsList = new ArrayList<>();

    public HoursAdapter(Context context, List<GetHoursResponse.ResultBean> timingsList) {
        this.context = context;
        HoursAdapter.timingsList = timingsList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HoursAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHoursBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_hours, parent, false);
        return new HoursAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HoursAdapter.ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemHoursBinding binding;

        public ViewHolder(ItemHoursBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindData(int position) {
            if (position == 0)
                binding.tvDay.setText(context.getString(R.string.sunday));
            else if (position == 1)
                binding.tvDay.setText(context.getString(R.string.monday));
            else if (position == 2)
                binding.tvDay.setText(context.getString(R.string.tuesday));
            else if (position == 3)
                binding.tvDay.setText(context.getString(R.string.wednesday));
            else if (position == 4)
                binding.tvDay.setText(context.getString(R.string.thursday));
            else if (position == 5)
                binding.tvDay.setText(context.getString(R.string.friday));
            else if (position == 6)
                binding.tvDay.setText(context.getString(R.string.saturday));

            if (timingsList.get(position).getM_from().equals(""))
                binding.tvMorningFrom.setText("00:00"+" "+context.getString(R.string.am));
            else binding.tvMorningFrom.setText(timingsList.get(position).getM_from());
            if (timingsList.get(position).getM_to().equals(""))
                binding.tvMorningTo.setText("00:00"+" "+context.getString(R.string.am));
            else binding.tvMorningTo.setText(timingsList.get(position).getM_to());
            if (timingsList.get(position).getN_from().equals(""))
                binding.tvNightFrom.setText("00:00"+" "+context.getString(R.string.am));
            else binding.tvNightFrom.setText(timingsList.get(position).getN_from());
            if (timingsList.get(position).getN_to().equals(""))
                binding.tvNightTo.setText("00:00"+" "+context.getString(R.string.am));
            else binding.tvNightTo.setText(timingsList.get(position).getN_to());

            binding.tvMorningFrom.setOnClickListener(view -> openTimePicker(binding.tvMorningFrom, position, "mf"));
            binding.tvMorningTo.setOnClickListener(view -> openTimePicker(binding.tvMorningTo, position, "mt"));
            binding.tvNightFrom.setOnClickListener(view -> openTimePicker(binding.tvNightFrom, position, "nf"));
            binding.tvNightTo.setOnClickListener(view -> openTimePicker(binding.tvNightTo, position, "nt"));
        }
    }

    private void openTimePicker(TextView textView, int position, String type) {
        // TODO Auto-generated method stub
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                int hour = selectedHour;
                int minutes = selectedMinute;
                String timeSet = "";
                if (hour > 12) {
                    hour -= 12;
                    timeSet = context.getString(R.string.pm);
                } else if (hour == 0) {
                    hour += 12;
                    timeSet =  context.getString(R.string.am);
                } else if (hour == 12) {
                    timeSet =  context.getString(R.string.pm);
                } else {
                    timeSet =  context.getString(R.string.am);
                }

                String min = "";
                if (minutes < 10)
                    min = "0" + minutes;
                else
                    min = String.valueOf(minutes);

                // Append in a StringBuilder
                String aTime = new StringBuilder().append(hour).append(':')
                        .append(min).append(" ").append(timeSet).toString();
                textView.setText(aTime);
                if (type.equals("mf"))
                    timingsList.get(position).setM_from(aTime);
                else if (type.equals("mt"))
                    timingsList.get(position).setM_to(aTime);
                else if (type.equals("nf"))
                    timingsList.get(position).setN_from(aTime);
                else if (type.equals("nt"))
                    timingsList.get(position).setN_to(aTime);

            }
        }, hour, minute, false);
        mTimePicker.setTitle(context.getString(R.string.select_time));
        mTimePicker.show();
    }

}
