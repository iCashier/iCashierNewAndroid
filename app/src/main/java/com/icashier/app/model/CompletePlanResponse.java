package com.icashier.app.model;

public class CompletePlanResponse {
    /**
     * code : 200
     * message : Success.
     * result : {"uid":"OXDFB6OJRB4N2BV39FLE8X4SN","name":"Yui","account":"1234567898765432","planid":"2","document":"OXDFB6OJRB4N2BV39FLE8X4SN/151a1953af776e3c863f1e8c7c103294.png","country_code":"+91","mobile":"5676543456","email":"you@gmail.com","image":"","signup":"2018-08-24T12:07:48.000Z","login":"2018-08-24T12:07:48.000Z"}
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

    public static class ResultBean {
        /**
         * uid : OXDFB6OJRB4N2BV39FLE8X4SN
         * name : Yui
         * account : 1234567898765432
         * planid : 2
         * document : OXDFB6OJRB4N2BV39FLE8X4SN/151a1953af776e3c863f1e8c7c103294.png
         * country_code : +91
         * mobile : 5676543456
         * email : you@gmail.com
         * image :
         * signup : 2018-08-24T12:07:48.000Z
         * login : 2018-08-24T12:07:48.000Z
         */

        private String uid;
        private String name;
        private String account;
        private String planid;
        private String document;
        private String country_code;
        private String mobile;
        private String email;
        private String image;
        private String signup;
        private String login;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getPlanid() {
            return planid;
        }

        public void setPlanid(String planid) {
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
