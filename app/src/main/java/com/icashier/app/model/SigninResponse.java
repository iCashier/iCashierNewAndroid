package com.icashier.app.model;

import java.io.Serializable;
import java.util.List;

public class SigninResponse implements Serializable{

    /**
     * code : 202
     * message : Please verify your mobile number before login.
     * result : {"uid":"MOBDQNPO9TJ0H7MLCP4ZCECPX","name":"For","token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Ik1PQkRRTlBPOVRKMEg3TUxDUDRaQ0VDUFgiLCJpYXQiOjE1MzUxMTM2MzIsImV4cCI6MTUzNTcxODQzMn0.8O54wiwgpT_1BsW9rejS6BLXU0bNkpZA1SFLQ-VYKWM","account":"","planid":"","document":"","country_code":"+91","mobile":"7645698765","email":"for@gmail.com","image":"","signup":"2018-08-24T12:26:25.000Z","login":"2018-08-24T12:26:25.000Z"}
     */

    private int code;
    private String message;
    private ResultBean result;
    private List<ResultBean> list;

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

    public List<ResultBean> getList() {
        return list;
    }

    public void setList(List<ResultBean> list) {
        this.list = list;
    }

    public static class ResultBean implements Serializable{
        /**
         * uid : MOBDQNPO9TJ0H7MLCP4ZCECPX
         * name : For
         * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Ik1PQkRRTlBPOVRKMEg3TUxDUDRaQ0VDUFgiLCJpYXQiOjE1MzUxMTM2MzIsImV4cCI6MTUzNTcxODQzMn0.8O54wiwgpT_1BsW9rejS6BLXU0bNkpZA1SFLQ-VYKWM
         * account :
         * planid :
         * document :
         * country_code : +91
         * mobile : 7645698765
         * email : for@gmail.com
         * image :
         * signup : 2018-08-24T12:26:25.000Z
         * login : 2018-08-24T12:26:25.000Z
         */

        private String uid;
        private String name;
        private String token;
        private String account;
        private String planid;
        private String document;
        private String country_code;
        private String mobile;
        private String email;
        private String image;
        private String signup;
        private String login;
        private int isParent;
        private String title;
        private String location;
        private String logo;
        private boolean isSelected;

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

        public int getIsParent() {
            return isParent;
        }

        public void setIsParent(int isParent) {
            this.isParent = isParent;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
