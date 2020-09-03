package com.icashier.app.model;

import java.io.Serializable;
import java.util.List;

public class ExtrasListResponse implements Serializable {

    /**
     * code : 200
     * message : Success.
     * result : [{"id":5,"uid":"HCN2HR33S8MYG5V2UNVTEWTMC","title":"Whipped Cream","type":"radiobutton","price":"150"},{"id":8,"uid":"HCN2HR33S8MYG5V2UNVTEWTMC","title":"Cheese","type":"radiobutton","price":"56"}]
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

    public static class ResultBean implements Serializable{
        /**
         * id : 5
         * uid : HCN2HR33S8MYG5V2UNVTEWTMC
         * title : Whipped Cream
         * type : radiobutton
         * price : 150
         */

        private int id;
        private String uid;
        private String title;
        private String type;
        private String price;
        private boolean isChecked;

        public ResultBean(int id, String uid, String title, String type, String price, boolean isChecked) {
            this.id = id;
            this.uid = uid;
            this.title = title;
            this.type = type;
            this.price = price;
            this.isChecked = isChecked;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
