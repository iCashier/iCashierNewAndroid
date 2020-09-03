package com.icashier.app.helper;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;


import com.icashier.app.R;

import java.io.File;


public class AppValidator {

    public static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    //    public static final String NAME_REGEX = "^[_A-Za-z0-9-\\+]";
    public static final String NAME_REGEX = "^[A-Za-z0-9\\s]{1,}[\\.]{0,1}[A-Za-z0-9\\s]{0,}$";
    public static final String CHAR_REGEX = ".*[a-zA-Z]+.*";
    public static final String ONLY_CHAR_REGEX = "^[a-zA-Z ]*$";
    public static final String MOBILE_REGEX = "\\d{10}";
    public static final String DIGIT_ONLY_REGEX="[0-9]+";
    public static final String MOBILE_REGEX_TEST = "^[0-9]{6,12}$";
    public static final String YEAR_REGEX = "\\d{4}";
    public static final String PINCODE_REGEX = "^([1-9])([0-9]){5}$";
    public static final String VEHICLE_REGEX = "^[A-Z]{2} [0-9]{2} [A-Z]{2} [0-9]{4}$";
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_])(?=\\S+$).{6,}$";//.{8,}
    public static final String[] IMAGE_EXTENSIONS = new String[]{"jpg", "jpeg", "png"};
    public static final String USERNAME_REGEX="^[a-z]+[a-z0-9._-]{3,14}$";
    public static final String URL_REGEX = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";



    public static boolean isValidURL(View view,Context context, EditText editText,String msg){
        if(editText.getText().toString().trim().equals("")){
            return true;
        }else{
            if(android.util.Patterns.WEB_URL.matcher(editText.getText().toString().trim()).matches()){
                return true;
            }else{
                AlertUtil.toastMsg(context, msg);
            }
        }

        return false;
    }

    public static boolean isValidUserName(View view,Context context, EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {

            // editText.setError(msg);
            AlertUtil.toastMsg(context, msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().matches(USERNAME_REGEX)) {
            return true;
        } else {
            AlertUtil.toastMsg(context,context.getString(R.string.valid_username));
            //editText.setError("Invalid Email");
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }
    }
    public static boolean isValidEmail(View view,Context context, EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {

            // editText.setError(msg);
            AlertUtil.toastMsg(context, msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().matches(EMAIL_REGEX)) {
            return true;
        } else {
            AlertUtil.toastMsg(context, context.getString(R.string.valid_email));
            //editText.setError("Invalid Email");
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }
    }

    public static boolean isValidEmailPhone(View view,Context context, EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {

            // editText.setError(msg);
            AlertUtil.toastMsg(context, msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().matches(EMAIL_REGEX)) {
            return true;
        } else {
            AlertUtil.toastMsg(context,"Please enter valid email or phone number");
            //editText.setError("Invalid Email");
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }
    }

    public static boolean isValidPhoneEmail(View view,Context context,EditText editText, String msg) {


        if (editText.getText().toString().trim().equals("")) {

            //editText.setError(msg);
            AlertUtil.toastMsg(context, msg);

            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().matches(MOBILE_REGEX_TEST))
            return true;
        else {
            //editText.setError("invalid mobile");
            AlertUtil.toastMsg(context,"Please enter valid email or phone number");

            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }

    }

    public static boolean isValidMobile(View view,Context context,EditText editText) {

        if (editText.getText().toString().trim().equals("")) {

            AlertUtil.toastMsg(context, context.getString(R.string.empty_mobile));
            return false;
        }
        else if (editText.getText().toString().matches(MOBILE_REGEX_TEST))
            return true;
        else {
            //editText.setError("invalid mobile");
            AlertUtil.toastMsg(context,context.getString(R.string.valid_mobile));

            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }

    }

    public static boolean isValidMobilePopup(View view,Context context,EditText editText) {

        if (editText.getText().toString().trim().equals("")) {

            AlertUtil.showToastShort(context, context.getString(R.string.empty_mobile));
            return false;
        }
        else if (editText.getText().toString().matches(MOBILE_REGEX_TEST))
            return true;
        else {
            //editText.setError("invalid mobile");
            AlertUtil.showToastShort(context,context.getString(R.string.valid_mobile));

            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }

    }

    public static boolean isValidPhone(View view,Context context,EditText editText,String msg) {

        if (editText.getText().toString().trim().equals("")) {

            return true;
        }
        else if (editText.getText().toString().matches(MOBILE_REGEX_TEST))
            return true;
        else {
            AlertUtil.toastMsg(context,msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }

    }


   /* public static boolean isValidMobile(View view,Context context,EditText editText, String msg) {


        if (editText.getText().toString().trim().equals("")) {

            //editText.setError(msg);
            AlertUtil.toastMsg(context, msg);

            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().matches(MOBILE_REGEX_TEST))
            return true;
        else {
            //editText.setError("invalid mobile");
            AlertUtil.toastMsg(context,"Invalid phone number");

            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }

    }
*/

  /*  public static boolean isValidPassword(EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {
            editText.setError(msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().trim().matches(PASSWORD_REGEX))
            return true;
        else {
            editText.setError("Password should contain  Alphanumeric.");
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }
    }*/
  public static boolean isValidPassword(View view,Context context, EditText editText, String msg) {
      if (editText.getText().toString().trim().equals("")) {
          AlertUtil.toastMsg(context, msg);
          //editText.setError(msg);
          editText.addTextChangedListener(new RemoveErrorEditText(editText));
          editText.requestFocus();
          return false;
      } else if (editText.getText().toString().length() >= 6)
          return true;
      else {
          AlertUtil.toastMsg(context, context.getString(R.string.valid_password));
          // editText.setError("Password should contain  alphanumeric with one caps and special character.");
          editText.addTextChangedListener(new RemoveErrorEditText(editText));
          editText.requestFocus();
          return false;
      }
  }

    public static boolean isValidPasswordEdit(View view,Context context, EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {

            return true;
        } else if (editText.getText().toString().length() >= 6)
            return true;
        else {
            AlertUtil.toastMsg(context, context.getString(R.string.valid_password));
            // editText.setError("Password should contain  alphanumeric with one caps and special character.");
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }
    }
   /* public static boolean isValidPassword(Context context, EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {
            AlertUtil.toastMsg(context, msg);
            //editText.setError(msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().length() >= 6)
            return true;
        else {
            AlertUtil.toastMsg(context, "Password must contain at least 6 characters.");
            // editText.setError("Password should contain  alphanumeric with one caps and special character.");
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }
    }*/

    public static boolean isValidName(View view,Context context, EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {

            // editText.setError(msg);
            AlertUtil.toastMsg(context, msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().matches(ONLY_CHAR_REGEX))
            return true;
        else {
            // editText.setError("invalid name");
            AlertUtil.toastMsg(context, "Invalid Name");
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }
    }

    public static boolean isOnlyDigits( EditText editText) {
       if (editText.getText().toString().matches(DIGIT_ONLY_REGEX))
            return true;
        else {
            return false;
        }
    }

    public static boolean isFirstValidName(View view,Context context, EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {

            // editText.setError(msg);
            AlertUtil.toastMsg(context, msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().matches(ONLY_CHAR_REGEX))
            return true;
        else {
            // editText.setError("invalid name");
            AlertUtil.toastMsg(context, "Invalid First name");
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }
    }
    public static boolean isLastValidName(View view,Context context, EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {

            // editText.setError(msg);
            AlertUtil.toastMsg(context,msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().matches(ONLY_CHAR_REGEX))
            return true;
        else {
            // editText.setError("invalid name");
            AlertUtil.toastMsg(context, "Invalid Last Name");
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }
    }
    public static boolean isAboutValid(View view,Context context, EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {

            // editText.setError(msg);
            AlertUtil.toastMsg(context, msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().matches(ONLY_CHAR_REGEX))
            return true;
        else {
            // editText.setError("invalid name");
            AlertUtil.toastMsg(context,"Invalid About Section");
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }
    }
    public static boolean isOccuValid(View view,Context context, EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {

            // editText.setError(msg);
            AlertUtil.toastMsg(context, msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().matches(ONLY_CHAR_REGEX))
            return true;
        else {
            // editText.setError("invalid name");
            AlertUtil.toastMsg(context,"Invalid Occupation Section");
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }
    }

    public static boolean isCityValid(View view,Context context, EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {

            // editText.setError(msg);
            AlertUtil.toastMsg(context, msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().matches(ONLY_CHAR_REGEX))
            return true;
        else {
            // editText.setError("invalid name");
            AlertUtil.toastMsg(context,"Invalid City Name");
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }
    }

    public static boolean isStateValid(View view,Context context, EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {

            // editText.setError(msg);
            AlertUtil.toastMsg(context, msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().matches(ONLY_CHAR_REGEX))
            return true;
        else {
            // editText.setError("invalid name");
            AlertUtil.toastMsg(context, "Invalid State Name");
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }
    }

    public static boolean isCountryValid(View view,Context context, EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {

            // editText.setError(msg);
            AlertUtil.toastMsg(context, msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().matches(ONLY_CHAR_REGEX))
            return true;
        else {
            // editText.setError("invalid name");
            AlertUtil.toastMsg(context, "Invalid Country Name");
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }
    }


    public static boolean isValidPincode(EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {

            editText.setError(msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().matches(PINCODE_REGEX))
            return true;
        else {
            editText.setError("invalid pincode");
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }
    }


    public static boolean isValidAddress(EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {

            editText.setError(msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().matches(NAME_REGEX))
            return true;
        else {
            editText.setError("invalid address");
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }
    }

    public static boolean isOnlyChars(EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {

            editText.setError(msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().matches(ONLY_CHAR_REGEX))
            return true;
        else {
            editText.setError("invalid format");
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }

    }


    public static boolean isValidVehicleNo(EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {

            editText.setError(msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().matches(VEHICLE_REGEX))
            return true;
        else {
            editText.setError("invalid vehicle no.");
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }
    }


    public static boolean isValidImage(File file) {
        for (String extensions : IMAGE_EXTENSIONS) {
            if (file.getName().toLowerCase().endsWith(extensions))
                return true;
        }
        return false;
    }

    public static boolean isValidYear(String data) {
        if (data.matches(YEAR_REGEX))
            return true;
        else
            return false;
    }


    public static boolean isValid(View view,Context context, EditText editText, String msg) {
        if (editText.getText().toString().trim().equals("")) {
            AlertUtil.toastMsg(context, msg);
            // editText.setError(msg);
            editText.addTextChangedListener(new RemoveErrorEditText(editText));
            editText.requestFocus();
            return false;
        }
        return true;
    }


    public static class RemoveErrorEditText implements TextWatcher {

        private EditText editText;


        public RemoveErrorEditText(EditText edittext) {
            this.editText = edittext;

        }

        @Override
        public void afterTextChanged(Editable s) {

            editText.setError(null);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    }


}
