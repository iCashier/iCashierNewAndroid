package com.icashier.app.model;

import java.io.Serializable;

public class CashierLoginResponse implements Serializable {
    /**
     * code : 201
     * message : Success.
     * result : {"name":"Love","token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE1NDMzMTE1NzIsImV4cCI6MTU0MzkxNjM3Mn0.aUraO5tm5djsyGZHgeQEeKizZ747nCcct9xqF1lLj5I","account":"","planid":3,"document":"","country_code":"","mobile":"","email":"","image":"users/C614CH39M5CWPK6JIMYCY1QHX/6298e70a48e8107ff5b6955f490a9e9e.png","signup":"2018-11-26T13:17:32.000Z","login":"2018-11-26T13:17:32.000Z"}
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
         * name : Love
         * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE1NDMzMTE1NzIsImV4cCI6MTU0MzkxNjM3Mn0.aUraO5tm5djsyGZHgeQEeKizZ747nCcct9xqF1lLj5I
         * account :
         * planid : 3
         * document :
         * country_code :
         * mobile :
         * email :
         * image : users/C614CH39M5CWPK6JIMYCY1QHX/6298e70a48e8107ff5b6955f490a9e9e.png
         * signup : 2018-11-26T13:17:32.000Z
         * login : 2018-11-26T13:17:32.000Z
         */

        private String name;
        private String token;
        private String account;
        private int planid;
        private String document;
        private String country_code;
        private String mobile;
        private String email;
        private String image;
        private String signup;
        private String login;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public int getPlanid() {
            return planid;
        }

        public void setPlanid(int planid) {
            this.planid = planid;
        }

        public String getDocument() {
            return document;
        }

        public void setDocument(String document) {
            this.document = document;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getSignup() {
            return signup;
        }

        public void setSignup(String signup) {
            this.signup = signup;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }
    }
}
