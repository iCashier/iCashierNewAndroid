package com.icashier.app.model;

import java.io.Serializable;

public class GetTaxResponse implements Serializable{

    /**
     * code : 200
     * message : Success.
     * result : {"totalTax":"50","amount":"192.0","taxAmount":"96.00"}
     */

    private int code;
    private String message;
    private ResultBean result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable{
        /**
         * totalTax : 50
         * amount : 192.0
         * taxAmount : 96.00
         */

        private String totalTax;
        private String amount;
        private String taxAmount;

        public String getTotalTax() {
            return totalTax;
        }

        public void setTotalTax(String totalTax) {
            this.totalTax = totalTax;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getTaxAmount() {
            return taxAmount;
        }

        public void setTaxAmount(String taxAmount) {
            this.taxAmount = taxAmount;
        }
    }
}
