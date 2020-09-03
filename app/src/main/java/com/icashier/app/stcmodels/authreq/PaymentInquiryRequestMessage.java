
package com.icashier.app.stcmodels.authreq;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentInquiryRequestMessage {

    @SerializedName("RefNum")
    @Expose
    private String refNum;

    public String getRefNum() {
        return refNum;
    }

    public void setRefNum(String refNum) {
        this.refNum = refNum;
    }

}
