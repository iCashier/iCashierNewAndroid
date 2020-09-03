package com.icashier.app.autoservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.model.OrderListResponse;
import com.icashier.app.printer.AutoPrinterFuntionality;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ResponseBroadcastReceiver extends BroadcastReceiver {
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        //get the broadcast message
       this.context=context;
        int resultCode=intent.getIntExtra("resultCode",RESULT_CANCELED);
        if (resultCode==RESULT_OK){
            String message=intent.getStringExtra("toastMessage");
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            autoPrint();
        }
    }


        public void autoPrint() {
        // this is for automatic print from notification
        try {
            SharedPrefManager tempPrefenceManager = SharedPrefManager.getInstance(context);
            List<OrderListResponse.ResultBean> tempPrintList = loadSharedPreferencesLogList(tempPrefenceManager);
            if (tempPrintList.size() > 0) {
                for (int i = 0; i < tempPrintList.size(); i++) {
                    AutoPrinterFuntionality orderDetailDialog = new AutoPrinterFuntionality(context, tempPrintList.get(i), i);
                               /* orderDetailDialog.show();
                                orderDetailDialog.setCancelable(true);
                                orderDetailDialog.setOnDismissListener(D -> {
                                    orderDetailDialog.dismiss();
                                });*/
                    orderDetailDialog.startPrintingReceipt();

                }
            }
        } catch (WindowManager.BadTokenException e) {
            Log.e("WindowManagerBad ", e.toString());
        }

    }



    public  void saveSharedPreferencesLogList(SharedPrefManager pref, List<OrderListResponse.ResultBean> printLog) {
        Gson gson = new Gson();
        String json = gson.toJson(printLog);
        pref.saveString(AppConstant.PRINTER_PUSH_NOTIFICATION, json);
    }

    public  List<OrderListResponse.ResultBean> loadSharedPreferencesLogList(SharedPrefManager pref) {
        List<OrderListResponse.ResultBean> callLog = new ArrayList<OrderListResponse.ResultBean>();
        // SharedPreferences mPrefs = context.getSharedPreferences(Constant.CALL_HISTORY_RC, context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString(AppConstant.PRINTER_PUSH_NOTIFICATION, "");
        if (json.isEmpty()) {
            callLog = new ArrayList<OrderListResponse.ResultBean>();
        } else {
            Type type = new TypeToken<List<OrderListResponse.ResultBean>>() {
            }.getType();
            callLog = gson.fromJson(json, type);
        }
        return callLog;
    }
}
