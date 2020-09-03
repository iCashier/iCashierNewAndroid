package com.icashier.app.model;

import java.util.List;

public class GetHoursResponse {

    /**
     * code : 200
     * message : Success.
     * result : [{"id":3,"mid":201,"day":"monday","m_from":"10:00 AM","m_to":"00:00 AM","n_from":"00:00 AM","n_to":"00:00 AM"},{"id":5,"mid":201,"day":"sunday","m_from":"5:08 PM","m_to":"6:08 PM","n_from":"00:00 AM","n_to":"00:00 AM"},{"id":7,"mid":201,"day":"tuesday","m_from":"3:00 PM","m_to":"00:00 AM","n_from":"00:00 AM","n_to":"00:00 AM"},{"id":8,"mid":201,"day":"wednesday","m_from":"7:00 PM","m_to":"00:00 AM","n_from":"00:00 AM","n_to":"00:00 AM"},{"id":9,"mid":201,"day":"","m_from":"04:00 AM","m_to":"00:00 AM","n_from":"00:00 AM","n_to":"00:00 AM"},{"id":10,"mid":201,"day":"friday","m_from":"11:00 PM","m_to":"00:00 AM","n_from":"00:00 AM","n_to":"00:00 AM"},{"id":11,"mid":201,"day":"saturday","m_from":"01:00 AM","m_to":"00:00 AM","n_from":"00:00 AM","n_to":"00:00 AM"}]
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
         * id : 3
         * mid : 201
         * day : monday
         * m_from : 10:00 AM
         * m_to : 00:00 AM
         * n_from : 00:00 AM
         * n_to : 00:00 AM
         */

        private int id;
        private int mid;
        private String day;
        private String m_from;
        private String m_to;
        private String n_from;
        private String n_to;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getM_from() {
            return m_from;
        }

        public void setM_from(String m_from) {
            this.m_from = m_from;
        }

        public String getM_to() {
            return m_to;
        }

        public void setM_to(String m_to) {
            this.m_to = m_to;
        }

        public String getN_from() {
            return n_from;
        }

        public void setN_from(String n_from) {
            this.n_from = n_from;
        }

        public String getN_to() {
            return n_to;
        }

        public void setN_to(String n_to) {
            this.n_to = n_to;
        }
    }
}
