package com.icashier.app.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.discovery.DeviceInfo;
import com.epson.epos2.discovery.Discovery;
import com.epson.epos2.discovery.DiscoveryListener;
import com.epson.epos2.discovery.FilterOption;
import com.icashier.app.R;
import com.icashier.app.adapter.PrinterListAdapter;
import com.icashier.app.databinding.DialogSelectPrinterBinding;
import com.icashier.app.model.PrinterListModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectPrinterDialog extends Dialog {
    Context context;
    DialogSelectPrinterBinding binding;
    LayoutInflater inflater;
    private FilterOption mFilterOption = null;
    List<PrinterListModel> printerList=new ArrayList<>();
    PrinterListAdapter adapter;
    PrinterListAdapter.SelectPrinterListener listener;

    public SelectPrinterDialog(Context context,PrinterListAdapter.SelectPrinterListener listener){
        super(context);
        this.context=context;
        this.inflater= LayoutInflater.from(context);
        this.listener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_select_printer, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);

        setAdapter();
        startDiscovery();

        binding.flClose.setOnClickListener(V->{
            dismiss();
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                try {
                    Discovery.stop();

                }
                catch (Epos2Exception e) {
                    if (e.getErrorStatus() != Epos2Exception.ERR_PROCESSING) {
                    }
                }
            }
        });

    }

    //==========Methods to set adapter=========//
    private void setAdapter() {
        adapter=new PrinterListAdapter(context, printerList, new PrinterListAdapter.SelectPrinterListener() {
            @Override
            public void onClick(String name, String target) {
                dismiss();
                listener.onClick(name,target);

            }
        });
        binding.rvPrinters.setAdapter(adapter);
        binding.rvPrinters.setLayoutManager(new LinearLayoutManager(context));
    }

    private void startDiscovery() {
        mFilterOption = new FilterOption();
        mFilterOption.setDeviceType(Discovery.TYPE_PRINTER);
        mFilterOption.setEpsonFilter(Discovery.FILTER_NAME);
        try {
            Discovery.start(context, mFilterOption, mDiscoveryListener);
        }
        catch (Exception e) {
            Log.e( "start", "error");
        }
    }


    private DiscoveryListener mDiscoveryListener = new DiscoveryListener() {
        @Override
        public void onDiscovery(final DeviceInfo deviceInfo) {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    PrinterListModel printer=new PrinterListModel();
                    printer.setName(deviceInfo.getDeviceName());
                    printer.setTarget(deviceInfo.getTarget());
                    printerList.add(printer);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };

}
