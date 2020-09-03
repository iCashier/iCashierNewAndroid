package com.icashier.app.dialog;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;


import com.icashier.app.R;
import com.icashier.app.databinding.DialogCalculatorBinding;
import com.icashier.app.listener.CalculatorListener;





public class CalculatorDialog extends Dialog {

    Context context;
    LayoutInflater inflater;
    DialogCalculatorBinding binding;
    private String  stringSpecial;
    private boolean numberClicked=false;
    private int  dotCount=0;

    CalculatorListener listener;


    public CalculatorDialog( Context context,CalculatorListener listener) {
        super(context);
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.listener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_calculator, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);

        setCancelable(true);
        
        setOnClickListeners();
    }

   

    private void setOnClickListeners() {
        binding.buttonCE.setOnClickListener(V->{
            binding.displayNumber.setText("0");
        });
        
        binding.button0.setOnClickListener(V->{
            binding.displayNumber.setText(binding.displayNumber.getText()+"0");
            numberClicked=true;
        });
        binding.button00.setOnClickListener(V->{
            binding.displayNumber.setText(binding.displayNumber.getText()+"00");
            numberClicked=true;
        });

        binding.button1.setOnClickListener(V->{
            binding.displayNumber.setText(binding.displayNumber.getText()+"1");
            numberClicked=true;
        });

        binding.button2.setOnClickListener(V->{
            binding.displayNumber.setText(binding.displayNumber.getText()+"2");
            numberClicked=true;
        });

        binding.button3.setOnClickListener(V->{
            binding.displayNumber.setText(binding.displayNumber.getText()+"3");
            numberClicked=true;
        });

        binding.button4.setOnClickListener(V->{
            binding.displayNumber.setText(binding.displayNumber.getText()+"4");
            numberClicked=true;
        });

        binding.button5.setOnClickListener(V->{
            binding.displayNumber.setText(binding.displayNumber.getText()+"5");
            numberClicked=true;
        });

        binding.button6.setOnClickListener(V->{
            binding.displayNumber.setText(binding.displayNumber.getText()+"6");
            numberClicked=true;
        });

        binding.button7.setOnClickListener(V->{
            binding.displayNumber.setText(binding.displayNumber.getText()+"7");
            numberClicked=true;
        });

        binding.button8.setOnClickListener(V->{
            binding.displayNumber.setText(binding.displayNumber.getText()+"8");
            numberClicked=true;
        });

        binding.button9.setOnClickListener(V->{
            binding.displayNumber.setText(binding.displayNumber.getText()+"9");
            numberClicked=true;
        });

        binding.buttonDot.setOnClickListener(V->{
            clickButtonDot();
        });



        binding.flClose.setOnClickListener(V->{
            dismiss();
        });

        binding.buttonSum.setOnClickListener(V->{
            if(!binding.displayNumber.getText().toString().equals("")){
                listener.onSaveClick(Float.parseFloat(binding.displayNumber.getText().toString().trim()));
                dismiss();
            }
        });


    }



    private void clickButtonDot() {

                stringSpecial = binding.displayNumber.getText().toString();
                if(!stringSpecial.contains(".")) {
                    binding.displayNumber.setText(binding.displayNumber.getText() + ".");
                }


    }













}
