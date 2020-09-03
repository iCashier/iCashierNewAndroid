package com.icashier.app.model;

public class GetGeneralSettingsResponse {

    /**
     * code : 200
     * message : Success.
     * result : {"notify":1,"deliveryDuration":1800,"COD":0,"deliveryService":1,"autoCashier":0}
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
         * notify : 1
         * deliveryDuration : 1800
         * COD : 0
         * deliveryService : 1
         * autoCashier : 0
         */

        private int notify;
        private int deliveryDuration;
        private int COD;
        private int deliveryService;
        private int autoCashier;

        public int getNotify() {
            return notify;
        }

        public void setNotify(int notify) {
            this.notify = notify;
        }

        public int getDeliveryDuration() {
            return deliveryDuration;
        }

        public void setDeliveryDuration(int deliveryDuration) {
            this.deliveryDuration = deliveryDuration;
        }

        public int getCOD() {
            return COD;
        }

        public void setCOD(int COD) {
            this.COD = COD;
        }

        public int getDeliveryService() {
            return deliveryService;
        }

        public void setDeliveryService(int deliveryService) {
            this.deliveryService = deliveryService;
        }

        public int getAutoCashier() {
            return autoCashier;
        }

        public void setAutoCashier(int autoCashier) {
            this.autoCashier = autoCashier;
        }
    }
}
