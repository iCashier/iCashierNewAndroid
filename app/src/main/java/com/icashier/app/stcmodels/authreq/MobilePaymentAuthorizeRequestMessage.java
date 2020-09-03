
package com.icashier.app.stcmodels.authreq;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobilePaymentAuthorizeRequestMessage {

    @SerializedName("BranchID")
    @Expose
    private String branchID;
    @SerializedName("TellerID")
    @Expose
    private String tellerID;
    @SerializedName("DeviceID")
    @Expose
    private String deviceID;
    @SerializedName("RefNum")
    @Expose
    private String refNum;
    @SerializedName("BillNumber")
    @Expose
    private String billNumber;
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("Amount")
    @Expose
    private Integer amount;
    @SerializedName("MerchantNote")
    @Expose
    private String merchantNote;
    @SerializedName("ExpiryPeriodType")
    @Expose
    private Integer expiryPeriodType;
    @SerializedName("ExpiryPeriod")
    @Expose
    private Integer expiryPeriod;

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public String getTellerID() {
        return tellerID;
    }

    public void setTellerID(String tellerID) {
        this.tellerID = tellerID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getRefNum() {
        return refNum;
    }

    public void setRefNum(String refNum) {
        this.refNum = refNum;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getMerchantNote() {
        return merchantNote;
    }

    public void setMerchantNote(String merchantNote) {
        this.merchantNote = merchantNote;
    }

    public Integer getExpiryPeriodType() {
        return expiryPeriodType;
    }

    public void setExpiryPeriodType(Integer expiryPeriodType) {
        this.expiryPeriodType = expiryPeriodType;
    }

    public Integer getExpiryPeriod() {
        return expiryPeriod;
    }

    public void setExpiryPeriod(Integer expiryPeriod) {
        this.expiryPeriod = expiryPeriod;
    }

    @Override
    public String toString() {
        return "MobilePaymentAuthorizeRequestMessage{" +
                "branchID='" + branchID + '\'' +
                ", tellerID='" + tellerID + '\'' +
                ", deviceID='" + deviceID + '\'' +
                ", refNum='" + refNum + '\'' +
                ", billNumber='" + billNumber + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", amount=" + amount +
                ", merchantNote='" + merchantNote + '\'' +
                ", expiryPeriodType=" + expiryPeriodType +
                ", expiryPeriod=" + expiryPeriod +
                '}';
    }
}
