package com.icashier.app.model;

import java.io.Serializable;
import java.util.List;

public class ExistingDealsModel implements Serializable {
    /**
     * code : 200
     * message : Success.
     * result : [{"id":8,"mid":95,"title":"New Deal","price":"250","items":"19,31","fromDate":"14-12-2018","toDate":"25-1-2019","days":"1,2,3","fromTime":"04:00:00","toTime":"18:00:00","withItems":"40,41","moreThan":"350","createdOn":"2018-12-14T11:18:11.000Z","itemsDetails":[{"id":19,"specialitem":1,"mid":95,"category":{"id":88,"mid":95,"name":"Bakery"},"sub_category":{"id":17,"name":"Cakes"},"name":"Black Forest","price":"250,350","qty":"25","size":"XS,S","sale_percent":"","sale_price":"112","extras":[],"about":"","image":"users/C614CH39M5CWPK6JIMYCY1QHX/7c651c7b959f985cea0d986916b98456.png","sold":0,"numbers":0,"like":0},{"id":31,"specialitem":1,"mid":95,"category":{"id":88,"mid":95,"name":"Bakery"},"sub_category":{"id":17,"name":"Cakes"},"name":"Chicken","price":"120","qty":"12","size":"XS","sale_percent":"63","sale_price":"31","extras":[{"id":30,"title":"Caramel","type":"checkbox","price":"8"}],"about":"","image":"users/C614CH39M5CWPK6JIMYCY1QHX/4e41961e40473a585301a26de61cbaf3.png","sold":0,"numbers":0,"like":0}],"withItemsDetails":[{"id":40,"specialitem":1,"mid":95,"category":{"id":102,"mid":95,"name":"Beverage"},"sub_category":{"id":45,"name":"Cold Beverges"},"name":"Lemon Tea","price":"40","qty":"6","size":"XS","sale_percent":"1","sale_price":"13","extras":[],"about":"","image":"users/C614CH39M5CWPK6JIMYCY1QHX/1ac8ae8cdc5b7cde4bac7b5b5b6465cc.png","sold":0,"numbers":0,"like":0},{"id":41,"specialitem":1,"mid":95,"category":{"id":103,"mid":95,"name":"Main cours"},"sub_category":{"id":46,"name":"Chicken"},"name":"Pizza","price":"255","qty":"26","size":"M","sale_percent":"1","sale_price":"1","extras":[],"about":"","image":"users/C614CH39M5CWPK6JIMYCY1QHX/2561b0525cb13efb17bfba4f680a7330.png","sold":0,"numbers":0,"like":0}]}]
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
         * id : 8
         * mid : 95
         * title : New Deal
         * price : 250
         * items : 19,31
         * fromDate : 14-12-2018
         * toDate : 25-1-2019
         * days : 1,2,3
         * fromTime : 04:00:00
         * toTime : 18:00:00
         * withItems : 40,41
         * moreThan : 350
         * createdOn : 2018-12-14T11:18:11.000Z
         * itemsDetails : [{"id":19,"specialitem":1,"mid":95,"category":{"id":88,"mid":95,"name":"Bakery"},"sub_category":{"id":17,"name":"Cakes"},"name":"Black Forest","price":"250,350","qty":"25","size":"XS,S","sale_percent":"","sale_price":"112","extras":[],"about":"","image":"users/C614CH39M5CWPK6JIMYCY1QHX/7c651c7b959f985cea0d986916b98456.png","sold":0,"numbers":0,"like":0},{"id":31,"specialitem":1,"mid":95,"category":{"id":88,"mid":95,"name":"Bakery"},"sub_category":{"id":17,"name":"Cakes"},"name":"Chicken","price":"120","qty":"12","size":"XS","sale_percent":"63","sale_price":"31","extras":[{"id":30,"title":"Caramel","type":"checkbox","price":"8"}],"about":"","image":"users/C614CH39M5CWPK6JIMYCY1QHX/4e41961e40473a585301a26de61cbaf3.png","sold":0,"numbers":0,"like":0}]
         * withItemsDetails : [{"id":40,"specialitem":1,"mid":95,"category":{"id":102,"mid":95,"name":"Beverage"},"sub_category":{"id":45,"name":"Cold Beverges"},"name":"Lemon Tea","price":"40","qty":"6","size":"XS","sale_percent":"1","sale_price":"13","extras":[],"about":"","image":"users/C614CH39M5CWPK6JIMYCY1QHX/1ac8ae8cdc5b7cde4bac7b5b5b6465cc.png","sold":0,"numbers":0,"like":0},{"id":41,"specialitem":1,"mid":95,"category":{"id":103,"mid":95,"name":"Main cours"},"sub_category":{"id":46,"name":"Chicken"},"name":"Pizza","price":"255","qty":"26","size":"M","sale_percent":"1","sale_price":"1","extras":[],"about":"","image":"users/C614CH39M5CWPK6JIMYCY1QHX/2561b0525cb13efb17bfba4f680a7330.png","sold":0,"numbers":0,"like":0}]
         */

        private int id;
        private int mid;
        private String title;
        private String price;
        private String items;
        private String fromDate;
        private String toDate;
        private String days;
        private String fromTime;
        private String toTime;
        private String withItems;
        private String moreThan;
        private String createdOn;
        private String image;
        private List<ExistingItemList.ResultBean> itemsDetails;
        private List<ExistingItemList.ResultBean> withItemsDetails;
        private boolean isSelected;
        private int specialitem;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getItems() {
            return items;
        }

        public void setItems(String items) {
            this.items = items;
        }

        public String getFromDate() {
            return fromDate;
        }

        public void setFromDate(String fromDate) {
            this.fromDate = fromDate;
        }

        public String getToDate() {
            return toDate;
        }

        public void setToDate(String toDate) {
            this.toDate = toDate;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public String getFromTime() {
            return fromTime;
        }

        public void setFromTime(String fromTime) {
            this.fromTime = fromTime;
        }

        public String getToTime() {
            return toTime;
        }

        public void setToTime(String toTime) {
            this.toTime = toTime;
        }

        public String getWithItems() {
            return withItems;
        }

        public void setWithItems(String withItems) {
            this.withItems = withItems;
        }

        public String getMoreThan() {
            return moreThan;
        }

        public void setMoreThan(String moreThan) {
            this.moreThan = moreThan;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public List<ExistingItemList.ResultBean> getItemsDetails() {
            return itemsDetails;
        }

        public void setItemsDetails(List<ExistingItemList.ResultBean> itemsDetails) {
            this.itemsDetails = itemsDetails;
        }

        public List<ExistingItemList.ResultBean> getWithItemsDetails() {
            return withItemsDetails;
        }

        public void setWithItemsDetails(List<ExistingItemList.ResultBean> withItemsDetails) {
            this.withItemsDetails = withItemsDetails;
        }


        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getSpecialitem() {
            return specialitem;
        }

        public void setSpecialitem(int specialitem) {
            this.specialitem = specialitem;
        }
    }
}
