package com.icashier.app.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.databinding.FragmentReportsBinding;
import com.icashier.app.helper.RestClient;


public class ReportsFragment extends Fragment {

    HomeActivity context;
    FragmentReportsBinding binding;
    RestClient.ApiRequest apiRequest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity)getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_reports, container, false);
        setTab(binding.tabCashDrawer);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(binding.container.getId(), new CashDrawerFragment()
                ,CashDrawerFragment.class.getSimpleName()).commit();
        setOnClickListener();
        return binding.getRoot();
    }

    //=========Mehtod to set onClick listener=========//
    private void setOnClickListener() {
        binding.tabCashDrawer.setOnClickListener(V->{
            setTab(binding.tabCashDrawer);
        });

        binding.tabSales.setOnClickListener(V->{
            setTab(binding.tabSales);
        });
    }

    //=================Method to change tab selection=============//
    private void setTab(View view)
    {
        if(view==binding.tabCashDrawer)
        {
            binding.tabCashDrawer.setSelected(true);
            binding.tabSales.setSelected(false);

            replaceFragment(new CashDrawerFragment());
        }
        else if(view==binding.tabSales)
        {
            binding.tabCashDrawer.setSelected(false);
            binding.tabSales.setSelected(true);

            replaceFragment(new SalesFragment());
        }
    }

    //================Method to change fragments =======================//
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(binding.container.getId(), fragment, fragment.getClass().getSimpleName()).commit();
    }


}
