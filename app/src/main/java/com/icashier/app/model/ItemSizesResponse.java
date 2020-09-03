package com.icashier.app.model;

import java.io.Serializable;
import java.util.List;

public class ItemSizesResponse implements Serializable {
    /**
     * statusCode : 200
     * statusMessage : Success.
     * result : [{"id":1,"size":"XS"},{"id":2,"size":"S"},{"id":3,"size":"M"},{"id":4,"size":"L"},{"id":5,"size":"XL"}]
     */

    private int statusCode;
    private String statusMessage;
    private List<ResultBean> result;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 1
         * size : XS
         */

        private int id;
        private String size;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }


    }
}
