package com.icashier.app.model;

public class CashierSignupResponse {

    /**
     * code : 200
     * message : User update successfully.
     * result : {"id":"2","mid":201,"name":"sumit","image":"asfsaf","signup":"2018-11-23T09:14:43.984Z","login":"2018-11-23T09:14:43.984Z"}
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
         * id : 2
         * mid : 201
         * name : sumit
         * image : asfsaf
         * signup : 2018-11-23T09:14:43.984Z
         * login : 2018-11-23T09:14:43.984Z
         */

        private String id;
        private int mid;
        private String name;
        private String image;
        private String signup;
        private String login;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
