package com.icashier.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.icashier.app.R;
import com.icashier.app.databinding.DialogPaymentRecieptBinding;

import static android.content.Context.PRINT_SERVICE;

public class PaymentReceiptDialog  extends Dialog {

    Context context;
    private LayoutInflater inflater;
    DialogPaymentRecieptBinding binding;
    String url;
    WebView wv=null;
    PrintReceipt listener;



    public PaymentReceiptDialog(Context context, String url,PrintReceipt listener) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.url=url;
        this.listener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_payment_reciept, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        loadPage();

        binding.flClose.setOnClickListener(V->{
            dismiss();
        });

        binding.imgPrint.setOnClickListener(V->{
          listener.onPrint();
        });
    }






    private void loadPage() {
        binding.webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url="+url);
        binding.webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    public interface PrintReceipt{
        void onPrint();
    }
}

