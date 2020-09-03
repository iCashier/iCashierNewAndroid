package com.icashier.app.model;


import java.io.Serializable;
import java.util.List;

public class OrderListResponse implements Serializable {

    /**
     * code : 200
     * message : Success.
     * result : [{"id":1,"status":"pending","customerName":"Love","mid":95,"pdf":"users/C614CH39M5CWPK6JIMYCY1QHX/1542980845532.pdf","items":[{"about":"","addedToCart":true,"category":88,"extras":[{"id":27,"isChecked":false,"price":"25","title":"Full Cream","type":"radiobutton"},{"id":28,"isChecked":false,"price":"2","title":"Halfnhalf","type":"radiobutton"},{"id":29,"isChecked":false,"price":"25","title":"Whipped Cream","type":"checkbox"},{"id":30,"isChecked":false,"price":"8","title":"Caramel","type":"checkbox"},{"id":31,"isChecked":false,"price":"9","title":"Chocolate","type":"checkbox"},{"id":32,"isChecked":true,"price":"2","title":"A","type":"checkbox"},{"id":34,"isChecked":true,"price":"5","title":"B","type":"checkbox"}],"extrasAddedToCart":[{"id":32,"isChecked":true,"price":"2","title":"A","type":"checkbox"},{"id":34,"isChecked":true,"price":"5","title":"B","type":"checkbox"}],"finalPrice":0,"id":18,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/c0d723e0a29de03c5a41f06861378e2f.png","isCartPopupShowing":false,"isExpanded":false,"isPopupShowing":false,"isSelected":true,"like":0,"mid":95,"name":"Red Velvet Rose","numbers":1,"priceForItem":"61.0","qty":"10","qtyAddedToCart":"1","sale_percent":"46","sale_price":"","selectedPrice":"100","sizeAddedToCart":"XS","size_price":[{"price":"100","size":"XS"},{"price":"200","size":"S"}],"sold":0,"specialitem":1,"sub_category":17,"sub_category_name":"Cakes","totalPrice":"107.0","type":"item"},{"about":"","addedToCart":true,"category":88,"extras":[],"extrasAddedToCart":[{"id":32,"isChecked":true,"price":"2","title":"A","type":"checkbox"},{"id":34,"isChecked":true,"price":"5","title":"B","type":"checkbox"}],"finalPrice":0,"id":19,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/7c651c7b959f985cea0d986916b98456.png","isCartPopupShowing":false,"isExpanded":false,"isPopupShowing":false,"isSelected":true,"like":0,"mid":95,"name":"Black Forest","numbers":0,"priceForItem":"138.0","qty":"25","qtyAddedToCart":"1","sale_percent":"","sale_price":"112","selectedPrice":"250","sizeAddedToCart":"XS","size_price":[{"price":"250","size":"XS"},{"price":"350","size":"S"}],"sold":0,"specialitem":0,"sub_category":17,"sub_category_name":"Cakes","totalPrice":"250.0","type":"item"}],"meal":[{"addedToCart":true,"category":0,"finalPrice":0,"id":10,"isCartPopupShowing":false,"isExpanded":false,"isPopupShowing":false,"isSelected":true,"items":[{"about":"","addedToCart":false,"category":88,"count":1,"extras":[{"id":27,"isChecked":false,"price":"25","title":"Full Cream","type":"radiobutton"},{"id":28,"isChecked":false,"price":"2","title":"Halfnhalf","type":"radiobutton"},{"id":29,"isChecked":false,"price":"25","title":"Whipped Cream","type":"checkbox"},{"id":30,"isChecked":false,"price":"8","title":"Caramel","type":"checkbox"},{"id":31,"isChecked":false,"price":"9","title":"Chocolate","type":"checkbox"},{"id":32,"isChecked":false,"price":"2","title":"A","type":"checkbox"},{"id":34,"isChecked":false,"price":"5","title":"B","type":"checkbox"}],"id":18,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/c0d723e0a29de03c5a41f06861378e2f.png","isExpanded":false,"isPopupShowing":false,"isSelected":false,"like":0,"mid":95,"name":"Red Velvet Rose","numbers":0,"price":"100,200","qty":"10","sale_percent":"46","sale_price":"","selectedPrice":"","sold":0,"specialitem":1,"sub_category":17,"totalPrice":"","type":"item"},{"about":"","addedToCart":false,"category":102,"count":1,"extras":[],"id":40,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/a9ff1001dbed5eaf92e532f60c5664cf.png","isExpanded":false,"isPopupShowing":false,"isSelected":false,"like":0,"mid":95,"name":"Lemon Tea","numbers":0,"price":"40","qty":"6","sale_percent":"1","sale_price":"1","selectedPrice":"","sold":0,"specialitem":0,"sub_category":45,"totalPrice":"","type":"item"},{"about":"","addedToCart":false,"category":109,"count":1,"extras":[],"id":44,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/48bee3348d4522b649036c58b4d35a8a.png","isExpanded":false,"isPopupShowing":false,"isSelected":false,"like":0,"mid":95,"name":"Saloon Prantha","numbers":0,"price":"20,40","qty":"5","sale_percent":"","sale_price":"","selectedPrice":"","sold":0,"specialitem":0,"sub_category":51,"totalPrice":"","type":"item"},{"about":"","addedToCart":false,"category":88,"count":1,"extras":[],"id":31,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/4e41961e40473a585301a26de61cbaf3.png","isExpanded":false,"isPopupShowing":false,"isSelected":false,"like":0,"mid":95,"name":"Chicken","numbers":0,"price":"120","qty":"12","sale_percent":"55","sale_price":"31","selectedPrice":"","sold":0,"specialitem":0,"sub_category":17,"totalPrice":"","type":"item"},{"about":"","addedToCart":false,"category":105,"count":1,"extras":[],"id":38,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/c1775b945ceb565edb8c58a2fb54bf8f.png","isExpanded":false,"isPopupShowing":false,"isSelected":false,"like":0,"mid":95,"name":"Noodels","numbers":0,"price":"50","qty":"8","sale_percent":"11","sale_price":"","selectedPrice":"","sold":0,"specialitem":0,"sub_category":47,"totalPrice":"","type":"item"},{"about":"","addedToCart":false,"category":103,"count":1,"extras":[],"id":41,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/2561b0525cb13efb17bfba4f680a7330.png","isExpanded":false,"isPopupShowing":false,"isSelected":false,"like":0,"mid":95,"name":"Pizza","numbers":0,"price":"255","qty":"26","sale_percent":"1","sale_price":"1","selectedPrice":"","sold":0,"specialitem":0,"sub_category":46,"totalPrice":"","type":"item"}],"like":0,"mid":95,"name":"Lunch","numbers":0,"priceForItem":"340.0","qty":"20","qtyAddedToCart":"1","selectedPrice":"340","sizeAddedToCart":"XS","sold":0,"specialitem":0,"sub_category":0,"title":"Lunch","totalPrice":"340.0","type":"meal"},{"addedToCart":true,"category":0,"finalPrice":0,"id":9,"isCartPopupShowing":false,"isExpanded":false,"isPopupShowing":false,"isSelected":true,"items":[{"about":"","addedToCart":false,"category":88,"count":1,"extras":[{"id":27,"isChecked":false,"price":"25","title":"Full Cream","type":"radiobutton"},{"id":28,"isChecked":false,"price":"2","title":"Halfnhalf","type":"radiobutton"},{"id":29,"isChecked":false,"price":"25","title":"Whipped Cream","type":"checkbox"},{"id":30,"isChecked":false,"price":"8","title":"Caramel","type":"checkbox"},{"id":31,"isChecked":false,"price":"9","title":"Chocolate","type":"checkbox"},{"id":32,"isChecked":false,"price":"2","title":"A","type":"checkbox"},{"id":34,"isChecked":false,"price":"5","title":"B","type":"checkbox"}],"id":18,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/c0d723e0a29de03c5a41f06861378e2f.png","isExpanded":false,"isPopupShowing":false,"isSelected":false,"like":0,"mid":95,"name":"Red Velvet Rose","numbers":0,"price":"100,200","qty":"10","sale_percent":"46","sale_price":"","selectedPrice":"","sold":0,"specialitem":1,"sub_category":17,"totalPrice":"","type":"item"},{"about":"","addedToCart":false,"category":102,"count":1,"extras":[],"id":40,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/a9ff1001dbed5eaf92e532f60c5664cf.png","isExpanded":false,"isPopupShowing":false,"isSelected":false,"like":0,"mid":95,"name":"Lemon Tea","numbers":0,"price":"40","qty":"6","sale_percent":"1","sale_price":"1","selectedPrice":"","sold":0,"specialitem":0,"sub_category":45,"totalPrice":"","type":"item"},{"about":"","addedToCart":false,"category":109,"count":1,"extras":[],"id":44,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/48bee3348d4522b649036c58b4d35a8a.png","isExpanded":false,"isPopupShowing":false,"isSelected":false,"like":0,"mid":95,"name":"Saloon Prantha","numbers":0,"price":"20,40","qty":"5","sale_percent":"","sale_price":"","selectedPrice":"","sold":0,"specialitem":0,"sub_category":51,"totalPrice":"","type":"item"}],"like":0,"mid":95,"name":"Breakfast","numbers":0,"priceForItem":"250.0","qty":"20","qtyAddedToCart":"1","selectedPrice":"250","sizeAddedToCart":"XS","sold":0,"specialitem":0,"sub_category":0,"title":"Breakfast","totalPrice":"250.0","type":"meal"}],"delivery":"Dinein","payment":"Cash","cashierName":"Jonathan Doe","address":"","deliveryCharges":"0","subtotal":"789.0","total":"789.0","date":"2018-11-23T13:47:25.000Z"}]
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
         * id : 1
         * status : pending
         * customerName : Love
         * mid : 95
         * pdf : users/C614CH39M5CWPK6JIMYCY1QHX/1542980845532.pdf
         * items : [{"about":"","addedToCart":true,"category":88,"extras":[{"id":27,"isChecked":false,"price":"25","title":"Full Cream","type":"radiobutton"},{"id":28,"isChecked":false,"price":"2","title":"Halfnhalf","type":"radiobutton"},{"id":29,"isChecked":false,"price":"25","title":"Whipped Cream","type":"checkbox"},{"id":30,"isChecked":false,"price":"8","title":"Caramel","type":"checkbox"},{"id":31,"isChecked":false,"price":"9","title":"Chocolate","type":"checkbox"},{"id":32,"isChecked":true,"price":"2","title":"A","type":"checkbox"},{"id":34,"isChecked":true,"price":"5","title":"B","type":"checkbox"}],"extrasAddedToCart":[{"id":32,"isChecked":true,"price":"2","title":"A","type":"checkbox"},{"id":34,"isChecked":true,"price":"5","title":"B","type":"checkbox"}],"finalPrice":0,"id":18,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/c0d723e0a29de03c5a41f06861378e2f.png","isCartPopupShowing":false,"isExpanded":false,"isPopupShowing":false,"isSelected":true,"like":0,"mid":95,"name":"Red Velvet Rose","numbers":1,"priceForItem":"61.0","qty":"10","qtyAddedToCart":"1","sale_percent":"46","sale_price":"","selectedPrice":"100","sizeAddedToCart":"XS","size_price":[{"price":"100","size":"XS"},{"price":"200","size":"S"}],"sold":0,"specialitem":1,"sub_category":17,"sub_category_name":"Cakes","totalPrice":"107.0","type":"item"},{"about":"","addedToCart":true,"category":88,"extras":[],"extrasAddedToCart":[{"id":32,"isChecked":true,"price":"2","title":"A","type":"checkbox"},{"id":34,"isChecked":true,"price":"5","title":"B","type":"checkbox"}],"finalPrice":0,"id":19,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/7c651c7b959f985cea0d986916b98456.png","isCartPopupShowing":false,"isExpanded":false,"isPopupShowing":false,"isSelected":true,"like":0,"mid":95,"name":"Black Forest","numbers":0,"priceForItem":"138.0","qty":"25","qtyAddedToCart":"1","sale_percent":"","sale_price":"112","selectedPrice":"250","sizeAddedToCart":"XS","size_price":[{"price":"250","size":"XS"},{"price":"350","size":"S"}],"sold":0,"specialitem":0,"sub_category":17,"sub_category_name":"Cakes","totalPrice":"250.0","type":"item"}]
         * meal : [{"addedToCart":true,"category":0,"finalPrice":0,"id":10,"isCartPopupShowing":false,"isExpanded":false,"isPopupShowing":false,"isSelected":true,"items":[{"about":"","addedToCart":false,"category":88,"count":1,"extras":[{"id":27,"isChecked":false,"price":"25","title":"Full Cream","type":"radiobutton"},{"id":28,"isChecked":false,"price":"2","title":"Halfnhalf","type":"radiobutton"},{"id":29,"isChecked":false,"price":"25","title":"Whipped Cream","type":"checkbox"},{"id":30,"isChecked":false,"price":"8","title":"Caramel","type":"checkbox"},{"id":31,"isChecked":false,"price":"9","title":"Chocolate","type":"checkbox"},{"id":32,"isChecked":false,"price":"2","title":"A","type":"checkbox"},{"id":34,"isChecked":false,"price":"5","title":"B","type":"checkbox"}],"id":18,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/c0d723e0a29de03c5a41f06861378e2f.png","isExpanded":false,"isPopupShowing":false,"isSelected":false,"like":0,"mid":95,"name":"Red Velvet Rose","numbers":0,"price":"100,200","qty":"10","sale_percent":"46","sale_price":"","selectedPrice":"","sold":0,"specialitem":1,"sub_category":17,"totalPrice":"","type":"item"},{"about":"","addedToCart":false,"category":102,"count":1,"extras":[],"id":40,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/a9ff1001dbed5eaf92e532f60c5664cf.png","isExpanded":false,"isPopupShowing":false,"isSelected":false,"like":0,"mid":95,"name":"Lemon Tea","numbers":0,"price":"40","qty":"6","sale_percent":"1","sale_price":"1","selectedPrice":"","sold":0,"specialitem":0,"sub_category":45,"totalPrice":"","type":"item"},{"about":"","addedToCart":false,"category":109,"count":1,"extras":[],"id":44,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/48bee3348d4522b649036c58b4d35a8a.png","isExpanded":false,"isPopupShowing":false,"isSelected":false,"like":0,"mid":95,"name":"Saloon Prantha","numbers":0,"price":"20,40","qty":"5","sale_percent":"","sale_price":"","selectedPrice":"","sold":0,"specialitem":0,"sub_category":51,"totalPrice":"","type":"item"},{"about":"","addedToCart":false,"category":88,"count":1,"extras":[],"id":31,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/4e41961e40473a585301a26de61cbaf3.png","isExpanded":false,"isPopupShowing":false,"isSelected":false,"like":0,"mid":95,"name":"Chicken","numbers":0,"price":"120","qty":"12","sale_percent":"55","sale_price":"31","selectedPrice":"","sold":0,"specialitem":0,"sub_category":17,"totalPrice":"","type":"item"},{"about":"","addedToCart":false,"category":105,"count":1,"extras":[],"id":38,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/c1775b945ceb565edb8c58a2fb54bf8f.png","isExpanded":false,"isPopupShowing":false,"isSelected":false,"like":0,"mid":95,"name":"Noodels","numbers":0,"price":"50","qty":"8","sale_percent":"11","sale_price":"","selectedPrice":"","sold":0,"specialitem":0,"sub_category":47,"totalPrice":"","type":"item"},{"about":"","addedToCart":false,"category":103,"count":1,"extras":[],"id":41,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/2561b0525cb13efb17bfba4f680a7330.png","isExpanded":false,"isPopupShowing":false,"isSelected":false,"like":0,"mid":95,"name":"Pizza","numbers":0,"price":"255","qty":"26","sale_percent":"1","sale_price":"1","selectedPrice":"","sold":0,"specialitem":0,"sub_category":46,"totalPrice":"","type":"item"}],"like":0,"mid":95,"name":"Lunch","numbers":0,"priceForItem":"340.0","qty":"20","qtyAddedToCart":"1","selectedPrice":"340","sizeAddedToCart":"XS","sold":0,"specialitem":0,"sub_category":0,"title":"Lunch","totalPrice":"340.0","type":"meal"},{"addedToCart":true,"category":0,"finalPrice":0,"id":9,"isCartPopupShowing":false,"isExpanded":false,"isPopupShowing":false,"isSelected":true,"items":[{"about":"","addedToCart":false,"category":88,"count":1,"extras":[{"id":27,"isChecked":false,"price":"25","title":"Full Cream","type":"radiobutton"},{"id":28,"isChecked":false,"price":"2","title":"Halfnhalf","type":"radiobutton"},{"id":29,"isChecked":false,"price":"25","title":"Whipped Cream","type":"checkbox"},{"id":30,"isChecked":false,"price":"8","title":"Caramel","type":"checkbox"},{"id":31,"isChecked":false,"price":"9","title":"Chocolate","type":"checkbox"},{"id":32,"isChecked":false,"price":"2","title":"A","type":"checkbox"},{"id":34,"isChecked":false,"price":"5","title":"B","type":"checkbox"}],"id":18,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/c0d723e0a29de03c5a41f06861378e2f.png","isExpanded":false,"isPopupShowing":false,"isSelected":false,"like":0,"mid":95,"name":"Red Velvet Rose","numbers":0,"price":"100,200","qty":"10","sale_percent":"46","sale_price":"","selectedPrice":"","sold":0,"specialitem":1,"sub_category":17,"totalPrice":"","type":"item"},{"about":"","addedToCart":false,"category":102,"count":1,"extras":[],"id":40,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/a9ff1001dbed5eaf92e532f60c5664cf.png","isExpanded":false,"isPopupShowing":false,"isSelected":false,"like":0,"mid":95,"name":"Lemon Tea","numbers":0,"price":"40","qty":"6","sale_percent":"1","sale_price":"1","selectedPrice":"","sold":0,"specialitem":0,"sub_category":45,"totalPrice":"","type":"item"},{"about":"","addedToCart":false,"category":109,"count":1,"extras":[],"id":44,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/48bee3348d4522b649036c58b4d35a8a.png","isExpanded":false,"isPopupShowing":false,"isSelected":false,"like":0,"mid":95,"name":"Saloon Prantha","numbers":0,"price":"20,40","qty":"5","sale_percent":"","sale_price":"","selectedPrice":"","sold":0,"specialitem":0,"sub_category":51,"totalPrice":"","type":"item"}],"like":0,"mid":95,"name":"Breakfast","numbers":0,"priceForItem":"250.0","qty":"20","qtyAddedToCart":"1","selectedPrice":"250","sizeAddedToCart":"XS","sold":0,"specialitem":0,"sub_category":0,"title":"Breakfast","totalPrice":"250.0","type":"meal"}]
         * delivery : Dinein
         * payment : Cash
         * cashierName : Jonathan Doe
         * address :
         * deliveryCharges : 0
         * subtotal : 789.0
         * total : 789.0
         * date : 2018-11-23T13:47:25.000Z
         */

        private int id;
        private String status;
        private String customerName;
        private int mid;
        private String pdf;
        private String delivery;
        private String payment;
        private String cashierName;
        private String address;
        private String deliveryCharges;
        private String subtotal;
        private String total;
        private String date;
        private float customAmount;
        private List<ItemsBean> items;
        private List<ItemsBean> meal;
        private List<ItemsBean> deals;

        private boolean isExpanded;
        private boolean refund;
        private String tax;
        private int paymentStatus;
        private int payOptions;
        private int waiterCall;
        private int isReordered;
        private String tableName;
        private String amtTaken;
        private String changeGiven;
        private String addressText;
        private String mobileNo;
        private String lat;
        private String lng;
        private int sequence_no;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        private String location;
        private String title;
        private String logo;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public String getPdf() {
            return pdf;
        }

        public void setPdf(String pdf) {
            this.pdf = pdf;
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

        public String getCashierName() {
            return cashierName;
        }

        public void setCashierName(String cashierName) {
            this.cashierName = cashierName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDeliveryCharges() {
            return deliveryCharges;
        }

        public void setDeliveryCharges(String deliveryCharges) {
            this.deliveryCharges = deliveryCharges;
        }

        public String getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(String subtotal) {
            this.subtotal = subtotal;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public List<ItemsBean> getMeal() {
            return meal;
        }

        public void setMeal(List<ItemsBean> meal) {
            this.meal = meal;
        }

        public boolean isExpanded() {
            return isExpanded;
        }

        public void setExpanded(boolean expanded) {
            isExpanded = expanded;
        }

        public boolean isRefund() {
            return refund;
        }

        public void setRefund(boolean refund) {
            this.refund = refund;
        }

        public String getTax() {
            return tax;
        }

        public void setTax(String tax) {
            this.tax = tax;
        }

        public List<ItemsBean> getDeals() {
            return deals;
        }

        public void setDeal(List<ItemsBean> deals) {
            this.deals = deals;
        }

        public float getCustomAmount() {
            return customAmount;
        }

        public void setCustomAmount(float customAmount) {
            this.customAmount = customAmount;
        }

        public int getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(int paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public int getPayOptions() {
            return payOptions;
        }

        public void setPayOptions(int payOptions) {
            this.payOptions = payOptions;
        }

        public int getWaiterCall() {
            return waiterCall;
        }

        public void setWaiterCall(int waiterCall) {
            this.waiterCall = waiterCall;
        }

        public int getIsReordered() {
            return isReordered;
        }

        public void setIsReordered(int isReordered) {
            this.isReordered = isReordered;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public String getAmtTaken() {
            return amtTaken;
        }

        public void setAmtTaken(String amtTaken) {
            this.amtTaken = amtTaken;
        }

        public String getChangeGiven() {
            return changeGiven;
        }

        public void setChangeGiven(String changeGiven) {
            this.changeGiven = changeGiven;
        }

        public String getAddressText() {
            return addressText;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public void setAddressText(String addressText) {
            this.addressText = addressText;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public int getSequence_no() {
            return sequence_no;
        }

        public void setSequence_no(int sequence_no) {
            this.sequence_no = sequence_no;
        }


        public static class ItemsBean implements Serializable{
            /**
             * about :
             * addedToCart : true
             * category : 88
             * extras : [{"id":27,"isChecked":false,"price":"25","title":"Full Cream","type":"radiobutton"},{"id":28,"isChecked":false,"price":"2","title":"Halfnhalf","type":"radiobutton"},{"id":29,"isChecked":false,"price":"25","title":"Whipped Cream","type":"checkbox"},{"id":30,"isChecked":false,"price":"8","title":"Caramel","type":"checkbox"},{"id":31,"isChecked":false,"price":"9","title":"Chocolate","type":"checkbox"},{"id":32,"isChecked":true,"price":"2","title":"A","type":"checkbox"},{"id":34,"isChecked":true,"price":"5","title":"B","type":"checkbox"}]
             * extrasAddedToCart : [{"id":32,"isChecked":true,"price":"2","title":"A","type":"checkbox"},{"id":34,"isChecked":true,"price":"5","title":"B","type":"checkbox"}]
             * finalPrice : 0
             * id : 18
             * image : http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/c0d723e0a29de03c5a41f06861378e2f.png
             * isCartPopupShowing : false
             * isExpanded : false
             * isPopupShowing : false
             * isSelected : true
             * like : 0
             * mid : 95
             * name : Red Velvet Rose
             * numbers : 1
             * priceForItem : 61.0
             * qty : 10
             * qtyAddedToCart : 1
             * sale_percent : 46
             * sale_price :
             * selectedPrice : 100
             * sizeAddedToCart : XS
             * size_price : [{"price":"100","size":"XS"},{"price":"200","size":"S"}]
             * sold : 0
             * specialitem : 1
             * sub_category : 17
             * sub_category_name : Cakes
             * totalPrice : 107.0
             * type : item
             */

            private String about;
            private boolean addedToCart;
            private int finalPrice;
            private int id;
            private String image;
            private boolean isCartPopupShowing;
            private boolean isExpanded;
            private boolean isPopupShowing;
            private boolean isSelected;
            private int like;
            private int mid;
            private String name;
            private int numbers;
            private String priceForItem;
            private String qty;
            private String qtyAddedToCart;
            private String sale_percent;
            private String sale_price;
            private String selectedPrice;
            private String sizeAddedToCart;
            private int sold;
            private int specialitem;
            private String sub_category_name;
            private String totalPrice;
            private String type;
            private List<ExtrasBean> extras;
            private List<ExtrasAddedToCartBean> extrasAddedToCart;
            private List<SizePriceBean> size_price;
            private String title;
            private List<ItemsBean> items;
            private int reorder_count;
            private List<ItemsBean> itemsDetails;
            private List<ItemsBean> withItemsDetails;




            public String getAbout() {
                return about;
            }

            public void setAbout(String about) {
                this.about = about;
            }

            public boolean isAddedToCart() {
                return addedToCart;
            }

            public void setAddedToCart(boolean addedToCart) {
                this.addedToCart = addedToCart;
            }



            public int getFinalPrice() {
                return finalPrice;
            }

            public void setFinalPrice(int finalPrice) {
                this.finalPrice = finalPrice;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public boolean isIsCartPopupShowing() {
                return isCartPopupShowing;
            }

            public void setIsCartPopupShowing(boolean isCartPopupShowing) {
                this.isCartPopupShowing = isCartPopupShowing;
            }

            public boolean isIsExpanded() {
                return isExpanded;
            }

            public void setIsExpanded(boolean isExpanded) {
                this.isExpanded = isExpanded;
            }

            public boolean isIsPopupShowing() {
                return isPopupShowing;
            }

            public void setIsPopupShowing(boolean isPopupShowing) {
                this.isPopupShowing = isPopupShowing;
            }

            public boolean isIsSelected() {
                return isSelected;
            }

            public void setIsSelected(boolean isSelected) {
                this.isSelected = isSelected;
            }

            public int getLike() {
                return like;
            }

            public void setLike(int like) {
                this.like = like;
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

            public int getNumbers() {
                return numbers;
            }

            public void setNumbers(int numbers) {
                this.numbers = numbers;
            }

            public String getPriceForItem() {
                return priceForItem;
            }

            public void setPriceForItem(String priceForItem) {
                this.priceForItem = priceForItem;
            }

            public String getQty() {
                return qty;
            }

            public void setQty(String qty) {
                this.qty = qty;
            }

            public String getQtyAddedToCart() {
                return qtyAddedToCart;
            }

            public void setQtyAddedToCart(String qtyAddedToCart) {
                this.qtyAddedToCart = qtyAddedToCart;
            }

            public String getSale_percent() {
                return sale_percent;
            }

            public void setSale_percent(String sale_percent) {
                this.sale_percent = sale_percent;
            }

            public String getSale_price() {
                return sale_price;
            }

            public void setSale_price(String sale_price) {
                this.sale_price = sale_price;
            }

            public String getSelectedPrice() {
                return selectedPrice;
            }

            public void setSelectedPrice(String selectedPrice) {
                this.selectedPrice = selectedPrice;
            }

            public String getSizeAddedToCart() {
                return sizeAddedToCart;
            }

            public void setSizeAddedToCart(String sizeAddedToCart) {
                this.sizeAddedToCart = sizeAddedToCart;
            }

            public int getSold() {
                return sold;
            }

            public void setSold(int sold) {
                this.sold = sold;
            }

            public int getSpecialitem() {
                return specialitem;
            }

            public void setSpecialitem(int specialitem) {
                this.specialitem = specialitem;
            }



            public String getSub_category_name() {
                return sub_category_name;
            }

            public void setSub_category_name(String sub_category_name) {
                this.sub_category_name = sub_category_name;
            }

            public String getTotalPrice() {
                return totalPrice;
            }

            public void setTotalPrice(String totalPrice) {
                this.totalPrice = totalPrice;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public List<ExtrasBean> getExtras() {
                return extras;
            }

            public void setExtras(List<ExtrasBean> extras) {
                this.extras = extras;
            }

            public List<ExtrasAddedToCartBean> getExtrasAddedToCart() {
                return extrasAddedToCart;
            }

            public void setExtrasAddedToCart(List<ExtrasAddedToCartBean> extrasAddedToCart) {
                this.extrasAddedToCart = extrasAddedToCart;
            }

            public List<SizePriceBean> getSize_price() {
                return size_price;
            }

            public void setSize_price(List<SizePriceBean> size_price) {
                this.size_price = size_price;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<ItemsBean> getItems() {
                return items;
            }

            public void setItems(List<ItemsBean> items) {
                this.items = items;
            }

            public int getReorder_count() {
                return reorder_count;
            }

            public void setReorder_count(int reorder_count) {
                this.reorder_count = reorder_count;
            }

            public List<ItemsBean> getItemsDetails() {
                return itemsDetails;
            }

            public void setItemsDetails(List<ItemsBean> itemsDetails) {
                this.itemsDetails = itemsDetails;
            }

            public List<ItemsBean> getWithItemsDetails() {
                return withItemsDetails;
            }

            public void setWithItemsDetails(List<ItemsBean> withItemsDetails) {
                this.withItemsDetails = withItemsDetails;
            }

            public static class ExtrasBean implements Serializable{
                /**
                 * id : 27
                 * isChecked : false
                 * price : 25
                 * title : Full Cream
                 * type : radiobutton
                 */

                private int id;
                private boolean isChecked;
                private String price;
                private String title;
                private String type;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public boolean isIsChecked() {
                    return isChecked;
                }

                public void setIsChecked(boolean isChecked) {
                    this.isChecked = isChecked;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
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
            }

            public static class ExtrasAddedToCartBean implements Serializable{
                /**
                 * id : 32
                 * isChecked : true
                 * price : 2
                 * title : A
                 * type : checkbox
                 */

                private int id;
                private boolean isChecked;
                private String price;
                private String title;
                private String type;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public boolean isIsChecked() {
                    return isChecked;
                }

                public void setIsChecked(boolean isChecked) {
                    this.isChecked = isChecked;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
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
            }

            public static class SizePriceBean implements Serializable{
                /**
                 * price : 100
                 * size : XS
                 */

                private String price;
                private String size;

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }
            }
        }

    }

    @Override
    public String toString() {
        return "OrderListResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
