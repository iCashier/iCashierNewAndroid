
package com.icashier.app.stcmodels.authreq;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentEnquary {

    @SerializedName("PaymentInquiryRequestMessage")
    @Expose
    private PaymentInquiryRequestMessage paymentInquiryRequestMessage;

    public PaymentInquiryRequestMessage getPaymentInquiryRequestMessage() {
        return paymentInquiryRequestMessage;
    }

    public void setPaymentInquiryRequestMessage(PaymentInquiryRequestMessage paymentInquiryRequestMessage) {
        this.paymentInquiryRequestMessage = paymentInquiryRequestMessage;
    }

}
