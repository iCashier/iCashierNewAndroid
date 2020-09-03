package com.icashier.app.adapter;

import android.content.ClipData;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.icashier.app.R;
import com.icashier.app.databinding.ItemSearchCategoryBinding;
import com.icashier.app.listener.CatSelectedListener;
import com.icashier.app.model.OrderListResponse;
import com.icashier.app.model.PredefinedCategoriesResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchCategoryAdapter extends RecyclerView.Adapter<SearchCategoryAdapter.ViewHolder> implements Filterable {

    LayoutInflater inflater;
    Context context;
    List<PredefinedCategoriesResponse.ResultBean> list,tempList;
    String search="";
    CatSelectedListener listener;


    public SearchCategoryAdapter(Context context,List<PredefinedCategoriesResponse.ResultBean> list,CatSelectedListener listener){
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);
        this.listener=listener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                //here constraint charSequence which is type in search bar
                FilterResults results = new FilterResults();
                List<PredefinedCategoriesResponse.ResultBean> FilteredArrList = new ArrayList<>();
                if (tempList == null) {
                    tempList = new ArrayList<>(list);
                    // saves the original data in contactDataListOriginal

                }
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = tempList.size();
                    results.values = tempList;
                } else {
                    search= (String) constraint;
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < tempList.size(); i++) {
                        String data;
                        if (Locale.getDefault().equals(new Locale("ar","MA"))) {
                            data=tempList.get(i).getArabic_name();
                        }else{
                            data=tempList.get(i).getEnglish_name();
                        }


                        if (data.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(tempList.get(i));
                        }

                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }


            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (ArrayList<PredefinedCategoriesResponse.ResultBean>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchCategoryBinding binding=DataBindingUtil.inflate(inflater, R.layout.item_search_category,parent,false);
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

        ItemSearchCategoryBinding binding;
        public ViewHolder(ItemSearchCategoryBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position){
            if (Locale.getDefault().equals(new Locale("ar","MA"))) {
                binding.tvCat.setText(list.get(position).getArabic_name());
            }else{
                binding.tvCat.setText(list.get(position).getEnglish_name());

            }


            binding.tvCat.setOnClickListener(V->{
                listener.onCatSelected(list.get(position));
            });
        }


    }

}
