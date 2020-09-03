package com.icashier.app.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.icashier.app.R;


/**
 * Created by Naresh on 10/4/2016.
 */

public class AlertUtil {
    private static ProgressDialog progressDialog;
    private static ProgressBar progressBar;


    public static void toastMsg(Context context,  String msg) {
        if (context != null&&!((Activity)context).isFinishing())
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackBarLong(Context context, View view, String msg, String buttonTitle, final View.OnClickListener onClickListener) {
        if (context != null) {
            Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
            snackbar.setAction(buttonTitle, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(view);
                }
            });
            snackbar.show();
        }
    }

    public static void toastMsgLong(Context context,  String msg) {
        if (context != null)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToastShort(Context context, String msg) {
        if (context != null)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(Context context, String msg) {
        if (context != null)
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showAlertDialog(final Context context, String title, String msg) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        alertDialog.setMessage(msg);
        alertDialog.setTitle(title);
        alertDialog.setPositiveButton(context.getResources().getString(R.string.ok),null);

        final android.app.AlertDialog alertDialogBuild=alertDialog.create();
        alertDialogBuild.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
               // alertDialogBuild.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.bgColor));

            }
        });
        alertDialogBuild.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        alertDialogBuild.show();
    }

    public static void showAlertDialogWithOK(final Context context, String msg) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(context.getResources().getString(R.string.ok),null);


        final android.app.AlertDialog alertDialogBuild=alertDialog.create();
        alertDialogBuild.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                //alertDialogBuild.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.bgColor));

            }
        });
        alertDialogBuild.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        alertDialogBuild.show();

    }

    public static void showAlertDialog(Context context, String title, String msg, String button1, String button2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(button1, null);
        builder.setNegativeButton(button2, null);
        builder.show();
    }

    public static void showAlertWindow(Context context,String msg,DialogInterface.OnClickListener okListener,DialogInterface.OnClickListener cancelListener)
    {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(context.getResources().getString(R.string.yes),okListener);

        alertDialog.setNegativeButton(context.getResources().getString(R.string.no),cancelListener);
        final android.app.AlertDialog alertDialogBuild=alertDialog.create();
        alertDialogBuild.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                //alertDialogBuild.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.bgColor));
                //alertDialogBuild.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.bgColor));

            }
        });
        alertDialogBuild.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        alertDialogBuild.show();
    }

    public static void showAlertWindow(Context context,String msg,DialogInterface.OnClickListener okListener)
    {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(context.getResources().getString(R.string.yes),okListener);

        alertDialog.setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final android.app.AlertDialog alertDialogBuild=alertDialog.create();
        alertDialogBuild.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                //alertDialogBuild.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.bgColor));
                //alertDialogBuild.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.bgColor));

            }
        });
        alertDialogBuild.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        alertDialogBuild.show();
    }

    public static void showAlertForSignout(Context context,String msg,DialogInterface.OnClickListener okListener)
    {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(context.getResources().getString(R.string.ok),okListener);


        final android.app.AlertDialog alertDialogBuild=alertDialog.create();
        alertDialogBuild.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialogBuild.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorBlack));

            }
        });
        alertDialogBuild.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        alertDialogBuild.show();
    }


    public static void showAlertDialog(Context context, String title, String msg, String button1, String button2, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(button1, okListener);
        builder.setNegativeButton(button2, cancelListener);
        builder.show();
    }

    public static void showAlertDialog(Context context, String title, String msg, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("Ok", okListener);
        builder.setNegativeButton("Cancel", cancelListener);
        builder.show();
    }

    public static void showProgressDialog(final Context context) {
        if (context != null) {
            if (progressDialog != null && progressDialog.isShowing()) {

            } else {
                progressDialog = new ProgressDialog(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                TextView titleView = (TextView) progressDialog.findViewById(R.id.title);
                progressBar = (ProgressBar) progressDialog.findViewById(R.id.marker_progress);
                titleView.setVisibility(View.GONE);
            }
        }
    }

    public static void setProgress(int progress) {
        if (progressDialog != null && progressBar != null && progressDialog.isShowing()) {
            progressBar.setProgress(progress);
        }
    }

    public static void showProgressDialogProgress(final Context context, final String title) {
        if (context != null) {
            if (progressDialog != null && progressDialog.isShowing()) {

            } else {
                progressDialog = new ProgressDialog(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                TextView titleView = (TextView) progressDialog.findViewById(R.id.title);
                titleView.setText(title);
            }
        }
    }

    public static void setProgress(Context context, final int progress) {
        if (progressBar != null) {
            progressBar.setProgress(progress);
        }
    }


    public static void showProgressDialog(final Context context, String title) {
        if (context != null) {
            if (progressDialog != null && progressDialog.isShowing()) {

            } else {
                progressDialog = new ProgressDialog(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                TextView titleView = (TextView) progressDialog.findViewById(R.id.title);
                titleView.setVisibility(View.VISIBLE);
                titleView.setText(title);
            }
        }
    }

    public static void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
