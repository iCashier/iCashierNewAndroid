package com.icashier.app.model;

import java.io.Serializable;
import java.util.List;

public class InvoiceListResponse implements Serializable {
    /**
     * code : 200
     * message : Success.
     * result : [{"id":19,"invoiceId":"27","customerName":"Love","title":"New Invoice","notes":"fcvvvvv","email":"loveinside2012@gmail.com","cc":"loveinside2012@gmail.com","mobile":"9855989824","recept":"users/C614CH39M5CWPK6JIMYCY1QHX/1543564673773.pdf","type":"send","date":"2018-11-30T08:01:56.000Z"}]
     */

    private int code;
    private String message;
    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        /**
         * id : 19
         * invoiceId : 27
         * customerName : Love
         * title : New Invoice
         * notes : fcvvvvv
         * email : loveinside2012@gmail.com
         * cc : loveinside2012@gmail.com
         * mobile : 9855989824
         * recept : users/C614CH39M5CWPK6JIMYCY1QHX/1543564673773.pdf
         * type : send
         * date : 2018-11-30T08:01:56.000Z
         */

        private int id;
        private String invoiceId;
        private String customerName;
        private String title;
        private String notes;
        private String email;
        private String cc;
        private String mobile;
        private String recept;
        private String type;
        private String date;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCc() {
            return cc;
        }

        public void setCc(String cc) {
            this.cc = cc;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getRecept() {
            return recept;
        }

        public void setRecept(String recept) {
            this.recept = recept;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
