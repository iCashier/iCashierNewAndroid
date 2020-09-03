package com.icashier.app.model;

import java.util.List;

public class PlanListResponse {
    /**
     * code : 200
     * message : Success.
     * result : [{"id":1,"plan":"free","services":"takeaway, dinein, pickup, delivery"},{"id":2,"plan":"verified","services":"takeaway, dinein, pickup, delivery"},{"id":3,"plan":"not-verified","services":"takeaway, pickup, delivery"}]
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

    public static class ResultBean {
        /**
         * id : 1
         * plan : free
         * services : takeaway, dinein, pickup, delivery
         */

        private int id;
        private String plan;
        private String services;
        private double price;

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPlan() {
            return plan;
        }

        public void setPlan(String plan) {
            this.plan = plan;
        }

        public String getServices() {
            return services;
        }

        public void setServices(String services) {
            this.services = services;
        }
    }
}
