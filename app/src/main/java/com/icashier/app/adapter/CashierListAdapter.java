package com.icashier.app.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.activity.LoginActivity;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.databinding.ItemSelectCasheirBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.DateUtils;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.listener.SelectCashierListener;
import com.icashier.app.model.GetCashiersResponse;

import java.util.List;

public class CashierListAdapter extends RecyclerView.Adapter<CashierListAdapter.ViewHolder>{

    Context context;
    List<GetCashiersResponse.CashiersBean> list;
    LayoutInflater inflater;
    SelectCashierListener listener;
    boolean isLoggedin;
    public CashierListAdapter(Context context,List<GetCashiersResponse.CashiersBean> list,SelectCashierListener listener){
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);
        this.listener=listener;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSelectCasheirBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_select_casheir,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ItemSelectCasheirBinding binding;

        public ViewHolder(ItemSelectCasheirBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position){
            Utilities.setImageCasheir(context,binding.imgItem,list.get(position).getImage());
            binding.tvName.setText(list.get(position).getName());
            try {
                binding.tvDate.setText(context.getString(R.string.joined)+" "+DateUtils.convertUTCtoLocalTime2(list.get(position).getSignup(),"dd MMM, yyyy"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(list.get(position).getName().equals(SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_NAME,""))){
                isLoggedin=true;
                list.get(position).setSelected(true);
                binding.tvLogout.setVisibility(View.VISIBLE);
                binding.tvLogout.setOnClickListener(V->{
                   listener.onLogoutClick(position);
                });
            }else{
                isLoggedin=false;
                binding.tvLogout.setVisibility(View.GONE);
            }

            if(list.get(position).isSelected()){
                binding.imgSelect.setSelected(true);
            }else{
                binding.imgSelect.setSelected(false);
            }


            if(!isLoggedin){
                binding.imgSelect.setOnClickListener(V->{
                    for(int i=0;i<list.size();i++){
                        if(i!=position){
                            list.get(i).setSelected(false);
                        }
                    }
                    if(list.get(position).isSelected()){
                        list.get(position).setSelected(false);
                        listener.onCasheirSelected("");
                    }else{
                        list.get(position).setSelected(true);
                        listener.onCasheirSelected(list.get(position).getName());
                    }
                    notifyDataSetChanged();
                });
            }else{
                binding.imgSelect.setOnClickListener(V->{});
            }





        }
    }
}
