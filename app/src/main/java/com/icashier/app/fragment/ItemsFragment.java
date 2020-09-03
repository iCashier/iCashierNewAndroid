package com.icashier.app.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.databinding.FragmentItemsBinding;
import com.icashier.app.helper.Utilities;


public class ItemsFragment extends Fragment {

    FragmentItemsBinding binding;
    HomeActivity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity)getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_items, container, false);

        binding.tabNewItem.setSelected(true);
        setOnClickListeners();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(binding.container.getId(), new NewItemFragment(),NewItemFragment.class.getSimpleName()).commit();
        return binding.getRoot();
    }

    //=============Mehtod to set onClick listeners=================//
    private void setOnClickListeners() {

        binding.tabNewItem.setOnClickListener(V->{
            setTab(binding.tabNewItem);
        });

        binding.tabExistingItems.setOnClickListener(V->{
            setTab(binding.tabExistingItems);
        });


        binding.clParentLayout.setOnClickListener(V->{
            Utilities.hideSoftKeyboard(context);
            context.closeDrawer();
        });
    }
    //=================Method to change tab selection=============//
    private void setTab(View view) {
        if (view == binding.tabNewItem) {
            binding.tabNewItem.setSelected(true);
            binding.tabExistingItems.setSelected(false);
            replaceFragment(new NewItemFragment());


        } else if (view == binding.tabExistingItems) {
            binding.tabNewItem.setSelected(false);
            binding.tabExistingItems.setSelected(true);
            replaceFragment(new ExistingItemFragment());

        }
    }

    //================Method to change fragments =======================//
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(binding.container.getId(), fragment, fragment.getClass().getSimpleName()).commit();
    }
}


