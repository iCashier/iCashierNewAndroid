package com.icashier.app.model;

import com.icashier.app.constants.ServerConstants;

import java.io.Serializable;

public class CheckoutResponse implements Serializable {
    /**
     * code : 200
     * message : Success.
     * insertId : 63
     * result : {"pdf":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/1542965044599.pdf"}
     */

    private int code;
    private String message;
    private int insertId;
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

    public int getInsertId() {
        return insertId;
    }

    public void setInsertId(int insertId) {
        this.insertId = insertId;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable{
        /**
         * pdf : http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/1542965044599.pdf
         */

        private String pdf;
        private String location;
        private String title;
        private String logo;


        public String getPdf() {
            return pdf;
        }

        public void setPdf(String pdf) {
            this.pdf = pdf;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
    }
}
