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
import com.icashier.app.databinding.FragmentInvoiceBinding;


public class InvoiceFragment extends Fragment {


    HomeActivity context;
    FragmentInvoiceBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity)getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_invoice, container, false);
        setTab(binding.tabNewInvoice);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(binding.container.getId(), new NewInvoiceFragment()
                ,NewInvoiceFragment.class.getSimpleName()).commit();
        setOnClickListener();
        return binding.getRoot();
    }

    //==================Mehtod to set onClick listeners===============//
    private void setOnClickListener() {

        binding.tabNewInvoice.setOnClickListener(V->{
            setTab(binding.tabNewInvoice);
        });
        binding.tabAllItems.setOnClickListener(V->{
            setTab(binding.tabAllItems);
        });
    }

    //=================Method to change tab selection=============//
    private void setTab(View view)
    {
        if(view==binding.tabNewInvoice)
        {
            binding.tabNewInvoice.setSelected(true);
            binding.tabAllItems.setSelected(false);

            replaceFragment(new NewInvoiceFragment());
        }
        else if(view==binding.tabAllItems)
        {
            binding.tabNewInvoice.setSelected(false);
            binding.tabAllItems.setSelected(true);

            replaceFragment(new AllInvoicesFragment());
        }
    }

    //================Method to change fragments =======================//
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(binding.container.getId(), fragment, fragment.getClass().getSimpleName()).commit();
    }





}
