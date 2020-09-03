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
import com.icashier.app.databinding.FragmentAppSettingsBinding;


public class AppSettingsFragment extends Fragment {


    FragmentAppSettingsBinding binding;
    HomeActivity context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity)getActivity();
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_app_settings, container, false);
        setTab(binding.tabGeneral);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(binding.container.getId(), new GeneralFragment()
                ,GeneralFragment.class.getSimpleName()).commit();
        setOnClickListeners();
        return binding.getRoot();
    }

    //===================Mehtod to set onClick Listeners===============//
    private void setOnClickListeners() {

        binding.tabGeneral.setOnClickListener(V->{
            setTab(binding.tabGeneral);
        });
        binding.tabCoupon.setOnClickListener(V->{
            setTab(binding.tabCoupon);
        });
        binding.tabCashier.setOnClickListener(V->{
            setTab(binding.tabCashier);
        });
        binding.tabHours.setOnClickListener(V->{
            setTab(binding.tabHours);
        });
        binding.tabTaxes.setOnClickListener(V->{
            setTab(binding.tabTaxes);
        });

        binding.tabSetting.setOnClickListener(V->{
            setTab(binding.tabSetting);
        });
    }

    //=================Method to change tab selection=============//
    private void setTab(View view)
    {
        if(view==binding.tabGeneral)
        {
            binding.tabGeneral.setSelected(true);
            binding.tabCoupon.setSelected(false);
            binding.tabCashier.setSelected(false);
            binding.tabHours.setSelected(false);
            binding.tabTaxes.setSelected(false);
            binding.tabSetting.setSelected(false);
            replaceFragment(new GeneralFragment());
        }
        else if(view==binding.tabCoupon)
        {
            binding.tabGeneral.setSelected(false);
            binding.tabCoupon.setSelected(true);
            binding.tabCashier.setSelected(false);
            binding.tabHours.setSelected(false);
            binding.tabTaxes.setSelected(false);
            binding.tabSetting.setSelected(false);
            replaceFragment(new CouponsFragment());
        }
        else if(view==binding.tabCashier)
        {
            binding.tabGeneral.setSelected(false);
            binding.tabCoupon.setSelected(false);
            binding.tabCashier.setSelected(true);
            binding.tabHours.setSelected(false);
            binding.tabTaxes.setSelected(false);
            binding.tabSetting.setSelected(false);
            replaceFragment(new CashierFragment());
        }
        else if(view==binding.tabHours)
        {
            binding.tabGeneral.setSelected(false);
            binding.tabCoupon.setSelected(false);
            binding.tabCashier.setSelected(false);
            binding.tabHours.setSelected(true);
            binding.tabTaxes.setSelected(false);
            binding.tabSetting.setSelected(false);
            replaceFragment(new HoursFragment());
        }
        else if(view==binding.tabTaxes)
        {
            binding.tabGeneral.setSelected(false);
            binding.tabCoupon.setSelected(false);
            binding.tabCashier.setSelected(false);
            binding.tabHours.setSelected(false);
            binding.tabTaxes.setSelected(true);
            binding.tabSetting.setSelected(false);
            replaceFragment(new TaxesFragment());
        }else if(view==binding.tabSetting){
            binding.tabGeneral.setSelected(false);
            binding.tabCoupon.setSelected(false);
            binding.tabCashier.setSelected(false);
            binding.tabHours.setSelected(false);
            binding.tabTaxes.setSelected(false);
            binding.tabSetting.setSelected(true);
            replaceFragment(new SettingsFragment());
        }

    }

    //================Method to change fragments =======================//
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(binding.container.getId(), fragment, fragment.getClass().getSimpleName()).commit();
    }

}
