package com.icashier.app.model;

import java.io.Serializable;
import java.util.List;

public class AllCodesListResponse implements Serializable {
    /**
     * code : 200
     * message : Success.
     * result : [{"id":7,"mid":31,"item_id":null,"meal_id":null,"delivery":null,"payment":"asd, asd, asd","type":"dine_in","table_id":2,"qrcode":"data:image/png;base64,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"},{"id":8,"mid":31,"item_id":null,"meal_id":null,"delivery":null,"payment":"asd, asd, asd","type":"dine_in","table_id":2,"qrcode":"data:image/png;base64,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"},{"id":10,"mid":31,"item_id":null,"meal_id":null,"delivery":null,"payment":"Credit Card","type":"dine_in","table_id":12,"qrcode":"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALQAAAC0CAYAAAA9zQYyAAAAAklEQVR4AewaftIAAAd3SURBVO3BQY4cy5LAQDLQ978yR0tfJZCoaun9GDezP1jrEoe1LnJY6yKHtS5yWOsih7UucljrIoe1LnJY6yKHtS5yWOsih7UucljrIoe1LnJY6yKHtS7yw4dU/qaKJypPKp6oTBVvqDypmFSmijdUnlR8QuVvqvjEYa2LHNa6yGGti/zwZRXfpPJNKlPFVDGpvFHxRGWqeKIyVUwVk8qk8qTijYpvUvmmw1oXOax1kcNaF/nhl6m8UfGGylTxRGVSmSqmikllqnii8kTlmyqeqEwqU8UbKm9U/KbDWhc5rHWRw1oX+eEyKk8qnqg8qZhUPlHxRGVSmSomlaliqrjZYa2LHNa6yGGti/xwuYpJZap4UjGpTBVPVD5R8UbFGypTxf+yw1oXOax1kcNaF/nhl1X8l1RMKlPFGypTxZOKN1SmiicqU8Wk8k0V/yWHtS5yWOsih7Uu8sOXqfxLFZPKVPGGylQxqXxCZap4Q2WqmFSmiknlDZX/ssNaFzmsdZHDWhexP/gfpvJGxaQyVUwqb1Q8UZkq/iaVqeImh7UucljrIoe1LmJ/8AGVqWJS+aaKN1R+U8UTlaliUnmj4onKk4o3VL6p4jcd1rrIYa2LHNa6iP3BB1SmiicqTyqeqPyXVfwmlaniv0RlqphUpopvOqx1kcNaFzmsdZEffpnKVDGpTCr/JRVvqDypmFSeVEwVT1SmikllqniiMlW8ofJEZar4xGGtixzWushhrYv88I9VvKHyiYpJ5RMqU8Wk8qRiUplUpopJZap4UvFGxaQyVUwV/9JhrYsc1rrIYa2L/PBlKk9UpopJ5Y2Kb6r4hMpU8UbFpPKGylQxqUwVk8qTiknlScUTlaniE4e1LnJY6yKHtS7yw4cqJpWp4o2Kb1KZKv4llaliUpkqJpWpYlJ5UjGpTBVvVHyi4psOa13ksNZFDmtd5IcPqbyh8psq3qiYVN6omFSeVHyiYlKZKiaVT6hMFZPKk4onKlPFJw5rXeSw1kUOa13khw9VPFF5UvGGylQxqfymiknlicqTiicqU8VU8UbFpPJNFW9UfNNhrYsc1rrIYa2L/PBlKlPFpPJE5UnFpPKk4onKVDGpvFHxTRWTylQxqbxRMak8UXlDZar4TYe1LnJY6yKHtS7ywz+mMlU8UXlSMak8qZhUpopJZap4ojJVTCpPVJ6oPKl4o2JSeVIxqUwVf9NhrYsc1rrIYa2L2B98QOWNiknljYo3VN6omFTeqJhUnlRMKk8q3lB5UjGpvFHxRGWqmFSmik8c1rrIYa2LHNa6iP3B/xCVqeINlW+qeKLyiYpJ5UnFE5UnFZPKVDGpTBVPVKaKbzqsdZHDWhc5rHWRH36ZypOKSeVJxRsqU8UnVJ6oTBWTypOKJxWTyqQyVbyh8kTlDZWp4jcd1rrIYa2LHNa6yA8fUpkqpoonKlPFGypvqDypmFSmikllqphUnlS8oTJVTCqfqPiEyhOVqeKbDmtd5LDWRQ5rXeSHX6byhsobFZPKJ1SeqHyiYlKZKiaVqWJSmSq+SWWqmFSmijdUpopPHNa6yGGtixzWuoj9wS9SeVIxqUwVk8pU8UTlb6r4TSpPKiaVT1T8LzmsdZHDWhc5rHUR+4MPqDypeKIyVUwqb1Q8UZkqnqg8qXiiMlVMKm9UPFF5o2JSeVIxqUwVk8pUMalMFZ84rHWRw1oXOax1EfuDD6g8qfgmlTcq3lCZKiaVJxWfUJkqJpUnFW+oTBVPVKaKSWWqmFSmim86rHWRw1oXOax1EfuDD6hMFU9UnlRMKr+p4onKVPGGylQxqUwVb6g8qZhUpopvUvlExScOa13ksNZFDmtd5Id/rGJSmSqeqEwVn1CZKp6oTBVTxZOKSWWqmFSeVDypeKLyRsVU8URlqvimw1oXOax1kcNaF/nhl6lMFZPKVDGpTBVTxaTyiYpJZaqYKiaVqeKJylTxCZW/SeWNikllqvjEYa2LHNa6yGGti9gf/A9TmSomlaniicpU8UTlScWkMlVMKm9UPFF5o+INlScVf9NhrYsc1rrIYa2L/PAhlb+pYqqYVKaKJypTxROVT1S8UfFNFZPKE5Wp4ptUpopPHNa6yGGtixzWusgPX1bxTSpPVKaKNyomlW9SeVLxRGWqmFSmiqniExVvVPxLh7UucljrIoe1LvLDL1N5o+ITKlPFE5UnFU9UpopvqnhSMalMFZPKE5VPqPxLh7UucljrIoe1LvLD/zMqU8UbKlPFpPKkYlKZKiaVNyp+U8WkMlW8ofJNh7UucljrIoe1LvLD/zMVb6g8UZkqJpVJZap4UvFE5YnKVDGpTBVvVLyh8psOa13ksNZFDmtd5IdfVvGbKt5QeVLxRsWTikllUnlSMam8UfEJlScVb1RMKt90WOsih7UucljrIj98mcrfpDJVTCpTxRsVk8oTlaniScUbFZPKpPKkYqqYVD6h8i8d1rrIYa2LHNa6iP3BWpc4rHWRw1oXOax1kcNaFzmsdZHDWhc5rHWRw1oXOax1kcNaFzmsdZHDWhc5rHWRw1oXOax1kf8DOCXlbzlMSZEAAAAASUVORK5CYII="},{"id":11,"mid":31,"item_id":null,"meal_id":null,"delivery":null,"payment":"Cash","type":"dine_in","table_id":14,"qrcode":"data:image/png;base64,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"},{"id":12,"mid":31,"item_id":null,"meal_id":11,"delivery":null,"payment":"Cash","type":"take_out","table_id":null,"qrcode":"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAKQAAACkCAYAAAAZtYVBAAAAAklEQVR4AewaftIAAAY4SURBVO3BQY5Dx5LAQLKg+1+Z42WuHiBIapf/ZIT9g7UucVjrIoe1LnJY6yKHtS5yWOsih7UucljrIoe1LnJY6yKHtS5yWOsih7UucljrIoe1LnJY6yIvPqTylyomlaniicpU8URlqphUpopJZap4ojJVTCpTxROVv1TxicNaFzmsdZHDWhd58WUV36TyCZUnKlPFVPGk4knFpPKJik9UfJPKNx3WushhrYsc1rrIix9TeUfFJ1Smil9SmSreUTGpTCpTxTepvKPilw5rXeSw1kUOa13kxX9cxaQyqTypeIfKE5WpYqqYVD6hMlX8lx3WushhrYsc1rrIi/84laniHSpTxaTypGJSmVSeVDxR+f/ksNZFDmtd5LDWRV78WMUvVUwqTyqmikllqvimiicqU8Wk8k0VNzmsdZHDWhc5rHWRF1+m8pdUpopJ5YnKVDGpTBWTylQxqTxRmSomlaliUnmHys0Oa13ksNZFDmtd5MWHKv5NFZPKVPGkYlL5popfqnhS8V9yWOsih7UucljrIi8+pDJVPFH5pYonKlPFJyomlaliUnlHxROVJxVPVKaKSeUdFZ84rHWRw1oXOax1kRcfqvhExaQyVXxTxZOKSWVS+aaKSeUdFZPKL1X80mGtixzWushhrYu8+JDKVDGpTBVPKiaVX6r4RMWkMqlMFZPKVPFE5R0q71CZKiaVJxWfOKx1kcNaFzmsdZEXP1bxRGWqmComlaniicoTlaniHSpPKiaVqeKJylQxqTypeKIyVUwqf+mw1kUOa13ksNZFXnyZypOKqeKJyhOVqWKqmFSeqLyj4hMqU8UnKiaVJxVPKiaVXzqsdZHDWhc5rHWRF19WMalMKlPFk4onKpPKVPGOiicqk8o7KiaVSWWqmFSmikllqphUvqnimw5rXeSw1kUOa13E/sEfUvmmiicqU8UTlXdUPFF5UvFNKk8qJpWpYlJ5UvFNh7UucljrIoe1LvLij1VMKk8qJpVfqphUpopJZaqYKn5JZap4ojJV3OSw1kUOa13ksNZFXnxIZap4R8WkMqlMFZPKVPFEZap4h8pU8Q6VJxWTylTxDpUnKk8q/tJhrYsc1rrIYa2L2D/4gMqTiicqU8U3qUwVk8pU8UTlmyp+SWWqmFSmiknlHRWfOKx1kcNaFzmsdZEXX1YxqbxD5UnFpDJVvKNiUpkqPlExqUwqU8Wk8qRiUvmEypOKSeWbDmtd5LDWRQ5rXeTFhyomlScVT1SmiicVk8o7VJ6oTBWTylQxqTypeFLxRGWqmFQmlaliUvk3Hda6yGGtixzWusiLD6k8qZhUpoqpYlKZKiaVqeKJylTxRGVSmSomlScV71CZKt5RMam8o2JSmSq+6bDWRQ5rXeSw1kXsH3xA5R0Vk8pU8URlqvhLKr9U8URlqniHypOKSeVJxTcd1rrIYa2LHNa6yIsvq5hUJpWp4onKVPEOlaliUnlS8YmKJyqTyjepvENlqphUJpWp4hOHtS5yWOsih7Uu8uLHKp6oPKl4h8pUMak8qZhUvkllqvhLFZPKE5Wp4pcOa13ksNZFDmtd5MWXqUwVk8pU8URlqphU/k0Vn1CZKt6h8g6Vd1T8pcNaFzmsdZHDWhexf/AfpvKk4ptUnlRMKlPFpDJVTCpTxaQyVbxDZap4ojJVfNNhrYsc1rrIYa2LvPiQyl+q+CaVb1J5ojJVTCpPVN6hMlU8UXlS8UuHtS5yWOsih7Uu8uLLKr5J5R0V76iYVN5R8UTlicpU8UTlHRXfpDJVfNNhrYsc1rrIYa2LvPgxlXdUfEJlqvimikllqnhSMal8k8onKt6hMlV84rDWRQ5rXeSw1kVe/I+pmFSmiknlHSpPVKaKSWWq+KWKb1L5pcNaFzmsdZHDWhd58R9X8Q6Vm6lMFVPFpDJVvEPlJoe1LnJY6yKHtS7y4scq/pLKVDGpTBWTylQxqUwVn1CZKp6ofFPFE5W/dFjrIoe1LnJY6yIvvkzlL6m8o+IdKlPFpPKk4knFE5V3qEwVk8qTiqniLx3WushhrYsc1rqI/YO1LnFY6yKHtS5yWOsih7UucljrIoe1LnJY6yKHtS5yWOsih7UucljrIoe1LnJY6yKHtS5yWOsi/wcoXf6G/UxIZwAAAABJRU5ErkJggg=="},{"id":13,"mid":31,"item_id":11,"meal_id":null,"delivery":"Swift Delivery","payment":"PayPal","type":"item","table_id":null,"qrcode":"data:image/png;base64,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"}]
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
         * id : 7
         * mid : 31
         * item_id : null
         * meal_id : null
         * delivery : null
         * payment : asd, asd, asd
         * type : dine_in
         * table_id : 2
         * qrcode : data:image/png;base64,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
         */

        private int id;
        private int mid;
        private int item_id;
        private String meal_id;
        private int meals_id;

        private String delivery;
        private String payment;
        private String type;
        private String name;
        private String table_id;
        private String qrcode;
        private int deals_id;
        private int coupon_id;
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

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public int getItem_id() {
            return item_id;
        }

        public void setItem_id(int item_id) {
            this.item_id = item_id;
        }

        public String getMeal_id() {
            return meal_id;
        }

        public void setMeal_id(String meal_id) {
            this.meal_id = meal_id;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public String getPayment() {
            return payment;
        }

        public void setPayment(String payment) {
            this.payment = payment;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTable_id() {
            return table_id;
        }

        public void setTable_id(String table_id) {
            this.table_id = table_id;
        }

        public String getQrcode() {
            return qrcode;
        }

        public void setQrcode(String qrcode) {
            this.qrcode = qrcode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getDeals_id() {
            return deals_id;
        }

        public void setDeals_id(int deals_id) {
            this.deals_id = deals_id;
        }

        public int getMeals_id() {
            return meals_id;
        }

        public void setMeals_id(int meals_id) {
            this.meals_id = meals_id;
        }

        public int getCoupon_id() {
            return coupon_id;
        }

        public void setCoupon_id(int coupon_id) {
            this.coupon_id = coupon_id;
        }
    }
}
