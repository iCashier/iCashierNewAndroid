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
import com.icashier.app.databinding.FragmentDealsBinding;


public class DealsFragment extends Fragment {

    HomeActivity context;
    FragmentDealsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity)getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_deals, container, false);
        setTab(binding.tabNewItem);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(binding.container.getId(), new NewDealsFragment()
                ,NewDealsFragment.class.getSimpleName()).commit();
        setOnClickListener();
        return binding.getRoot();
    }

    //=========Mehtod to set onClick listener=========//
    private void setOnClickListener() {
        binding.tabNewItem.setOnClickListener(V->{
            setTab(binding.tabNewItem);
        });

        binding.tabExistingItems.setOnClickListener(V->{
            setTab(binding.tabExistingItems);
        });
    }

    //=================Method to change tab selection=============//
    private void setTab(View view)
    {
        if(view==binding.tabNewItem)
        {
            binding.tabNewItem.setSelected(true);
            binding.tabExistingItems.setSelected(false);

            replaceFragment(new NewDealsFragment());
        }
        else if(view==binding.tabExistingItems)
        {
            binding.tabNewItem.setSelected(false);
            binding.tabExistingItems.setSelected(true);

            replaceFragment(new ExistingDealsFragment());
        }
    }

    //================Method to change fragments =======================//
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(binding.container.getId(), fragment, fragment.getClass().getSimpleName()).commit();
    }



}
