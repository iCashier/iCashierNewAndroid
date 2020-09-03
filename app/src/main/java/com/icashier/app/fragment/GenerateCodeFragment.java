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
import com.icashier.app.databinding.FragmentGenerateCodeBinding;
import com.icashier.app.helper.Utilities;


public class GenerateCodeFragment extends Fragment {


    HomeActivity context;
    FragmentGenerateCodeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity)getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_generate_code, container, false);
        setTab(binding.tabDinein);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(binding.container.getId(), new DineinCodeFragment(),DineinCodeFragment.class.getSimpleName()).commit();
        setOnClickListeners();
        return binding.getRoot();
    }

    //======================Method to set onClick listeners================//
    private void setOnClickListeners()
    {
        binding.tabDinein.setOnClickListener(V->{
            setTab(binding.tabDinein);
        });

        binding.tabTakeOut.setOnClickListener(V->{
            setTab(binding.tabTakeOut);
        });

        binding.tabItems.setOnClickListener(V->{
            setTab(binding.tabItems);
        });

        binding.tabCodes.setOnClickListener(V->{
            setTab(binding.tabCodes);
        });

        binding.clParentLayout.setOnClickListener(V->{
            context.closeDrawer();
            Utilities.hideSoftKeyboard(context);
        });

    }


    //=================Method to change tab selection=============//
    private void setTab(View view)
    {
        if(view==binding.tabDinein)
        {
            binding.tabDinein.setSelected(true);
            binding.tabTakeOut.setSelected(false);
            binding.tabItems.setSelected(false);
            binding.tabCodes.setSelected(false);
            replaceFragment(new DineinCodeFragment());
        }
        else if(view==binding.tabTakeOut)
        {
            binding.tabDinein.setSelected(false);
            binding.tabTakeOut.setSelected(true);
            binding.tabItems.setSelected(false);
            binding.tabCodes.setSelected(false);
            replaceFragment(new TakeOutCodeFragment());
        }
        else if(view==binding.tabItems)
        {
            binding.tabDinein.setSelected(false);
            binding.tabTakeOut.setSelected(false);
            binding.tabItems.setSelected(true);
            binding.tabCodes.setSelected(false);
            replaceFragment(new ItemsCodeFragment());
        }
        else if(view==binding.tabCodes)
        {
            binding.tabDinein.setSelected(false);
            binding.tabTakeOut.setSelected(false);
            binding.tabItems.setSelected(false);
            binding.tabCodes.setSelected(true);
            replaceFragment(new AllCodesFragment());
        }

    }

    //================Method to change fragments =======================//
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(binding.container.getId(), fragment, fragment.getClass().getSimpleName()).commit();
    }
}
