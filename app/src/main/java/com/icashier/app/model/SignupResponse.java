package com.icashier.app.model;

import java.io.Serializable;

public class SignupResponse implements Serializable{
    /**
     * code : 201
     * result : {"uid":"OB3ATBYQW52Y4170QMXTTZRNC","name":"Love Mahajan","country_code":"+91","token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Ik9CM0FUQllRVzUyWTQxNzBRTVhUVFpSTkMiLCJpYXQiOjE1MzUwMjM5NDMsImV4cCI6MTUzNTYyODc0M30.z5vyiMCCZBSBUt659bV12_q60nxV1wV_YlcXEiOooBs","mobile":"1234567890","email":"abcd@gmail.com","image":"","signup":"2018-08-23T11:32:23.561Z","login":"2018-08-23T11:32:23.561Z"}
     */

    private int code;


    private String message;
    private ResultBean result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * uid : OB3ATBYQW52Y4170QMXTTZRNC
         * name : Love Mahajan
         * country_code : +91
         * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Ik9CM0FUQllRVzUyWTQxNzBRTVhUVFpSTkMiLCJpYXQiOjE1MzUwMjM5NDMsImV4cCI6MTUzNTYyODc0M30.z5vyiMCCZBSBUt659bV12_q60nxV1wV_YlcXEiOooBs
         * mobile : 1234567890
         * email : abcd@gmail.com
         * image :
         * signup : 2018-08-23T11:32:23.561Z
         * login : 2018-08-23T11:32:23.561Z
         */

        private String uid;
        private String name;
        private String country_code;
        private String token;
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

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
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
