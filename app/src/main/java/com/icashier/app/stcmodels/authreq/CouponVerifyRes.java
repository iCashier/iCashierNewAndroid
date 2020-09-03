
package com.icashier.app.stcmodels.authreq;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponVerifyRes {

    @SerializedName("MobilePaymentAuthorizeRequestMessage")
    @Expose
    private MobilePaymentAuthorizeRequestMessage mobilePaymentAuthorizeRequestMessage;

    public MobilePaymentAuthorizeRequestMessage getMobilePaymentAuthorizeRequestMessage() {
        return mobilePaymentAuthorizeRequestMessage;
    }

    public void setMobilePaymentAuthorizeRequestMessage(MobilePaymentAuthorizeRequestMessage mobilePaymentAuthorizeRequestMessage) {
        this.mobilePaymentAuthorizeRequestMessage = mobilePaymentAuthorizeRequestMessage;
    }


}
