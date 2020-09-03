package com.icashier.app.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.CategoryExpandableAdapter;
import com.icashier.app.adapter.CategoryIconsAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentAddCategoriesBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.SelectIconListener;
import com.icashier.app.model.CategoryImagesResponse;
import com.icashier.app.model.CategoryListReponse;
import com.icashier.app.model.GenericResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AddCategoriesFragment extends Fragment {


    FragmentAddCategoriesBinding binding;
    HomeActivity context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity)getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_add_categories, container, false);
        binding.tabCategories.setSelected(true);
        setOnClickListeners();

        //to add fragment
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(binding.container.getId(), new CategoriesFragment(),CategoriesFragment.class.getSimpleName()).commit();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    //=============Mehtod to set onClick listeners=================//
    private void setOnClickListeners() {

        binding.tabCategories.setOnClickListener(V->{
            setTab(binding.tabCategories);
        });

        binding.tabMeals.setOnClickListener(V->{
            setTab(binding.tabMeals);
        });

        binding.tabExtras.setOnClickListener(V->{
            setTab(binding.tabExtras);
        });


        binding.flParentLayout.setOnClickListener(V->{
            Utilities.hideSoftKeyboard(context);
            context.closeDrawer();
        });
    }



    //=================Method to change tab selection=============//
    private void setTab(View view)
    {
        if(view==binding.tabCategories)
        {
            binding.tabCategories.setSelected(true);
            binding.tabMeals.setSelected(false);
            binding.tabExtras.setSelected(false);
            replaceFragment(new CategoriesFragment());


        }
        else if(view==binding.tabMeals)
        {
            binding.tabCategories.setSelected(false);
            binding.tabMeals.setSelected(true);
            binding.tabExtras.setSelected(false);
            replaceFragment(new AddMealFragment());

        }
        else if(view==binding.tabExtras)
        {
            binding.tabCategories.setSelected(false);
            binding.tabMeals.setSelected(false);
            binding.tabExtras.setSelected(true);
            replaceFragment(new AddExtrasFragment());
        }
    }
    //================Method to change fragments =======================//
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(binding.container.getId(), fragment, fragment.getClass().getSimpleName()).commit();
    }
}
