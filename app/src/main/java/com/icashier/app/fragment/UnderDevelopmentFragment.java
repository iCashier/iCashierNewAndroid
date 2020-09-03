package com.icashier.app.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;


public class UnderDevelopmentFragment extends Fragment {

    HomeActivity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity)getActivity();
        container.setOnClickListener(V->{
            context.closeDrawer();
        });
        return inflater.inflate(R.layout.fragment_under_development, container, false);
    }

}
