package com.icashier.app.model;

import java.io.Serializable;
import java.util.List;

public class MealListResponse implements Serializable {

    /**
     * code : 200
     * message : Meal added Successfully.
     * result : [{"id":2,"mid":31,"title":"Breakfast","price":"500","items":[{"id":2,"specialitem":1,"mid":70,"category":70,"sub_category":1,"name":"Hot Coffee","price":"100,120,130,140,150","qty":"10","size":"XS,S,M,L,XL","sale_percent":"42","sale_price":"0","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"}],"about":"Hot Coffee ","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/75521fe0b670ee7fdcffc1444e870049.png","selected_size":"S","selected_price":"130"},{"id":6,"specialitem":1,"mid":31,"category":71,"sub_category":2,"name":"Cold Coffee","price":"100,120,130,140,150","qty":"6","size":"XS,S,M,L,XL","sale_percent":"35","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"About","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/dea1179049de40407f5ceb3cb3619eef.png","selected_size":"L","selected_price":"140"}]},{"id":6,"mid":31,"title":"Breakfast","price":"250","items":[{"id":6,"specialitem":1,"mid":31,"category":71,"sub_category":2,"name":"Cold Coffee","price":"100,120,130,140,150","qty":"6","size":"XS,S,M,L,XL","sale_percent":"35","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"About","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/dea1179049de40407f5ceb3cb3619eef.png","selected_size":"S","selected_price":"130"},{"id":11,"specialitem":0,"mid":31,"category":70,"sub_category":1,"name":"Hot Coffee","price":"120,130,140,200","qty":"6","size":"XS,S,L,XL","sale_percent":"24","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"Coffee","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/18c1503c18100c0d4a136bf5a219a66b.png","selected_size":"L","selected_price":"140"}]},{"id":7,"mid":31,"title":"Breakfast","price":"250","items":[{"id":6,"specialitem":1,"mid":31,"category":71,"sub_category":2,"name":"Cold Coffee","price":"100,120,130,140,150","qty":"6","size":"XS,S,M,L,XL","sale_percent":"35","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"About","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/dea1179049de40407f5ceb3cb3619eef.png","selected_size":"S","selected_price":"130"},{"id":11,"specialitem":0,"mid":31,"category":70,"sub_category":1,"name":"Hot Coffee","price":"120,130,140,200","qty":"6","size":"XS,S,L,XL","sale_percent":"24","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"Coffee","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/18c1503c18100c0d4a136bf5a219a66b.png","selected_size":"L","selected_price":"140"},{"id":6,"specialitem":1,"mid":31,"category":71,"sub_category":2,"name":"Cold Coffee","price":"100,120,130,140,150","qty":"6","size":"XS,S,M,L,XL","sale_percent":"35","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"About","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/dea1179049de40407f5ceb3cb3619eef.png","selected_size":"S","selected_price":"130"},{"id":11,"specialitem":0,"mid":31,"category":70,"sub_category":1,"name":"Hot Coffee","price":"120,130,140,200","qty":"6","size":"XS,S,L,XL","sale_percent":"24","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"Coffee","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/18c1503c18100c0d4a136bf5a219a66b.png","selected_size":"L","selected_price":"140"}]},{"id":8,"mid":31,"title":"Breakfast","price":"250","items":[{"id":6,"specialitem":1,"mid":31,"category":71,"sub_category":2,"name":"Cold Coffee","price":"100,120,130,140,150","qty":"6","size":"XS,S,M,L,XL","sale_percent":"35","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"About","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/dea1179049de40407f5ceb3cb3619eef.png","selected_size":"XS","selected_price":"100"},{"id":11,"specialitem":0,"mid":31,"category":70,"sub_category":1,"name":"Hot Coffee","price":"120,130,140,200","qty":"6","size":"XS,S,L,XL","sale_percent":"24","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"Coffee","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/18c1503c18100c0d4a136bf5a219a66b.png","selected_size":"XS","selected_price":"120"}]},{"id":9,"mid":31,"title":"Breakfast","price":"250","items":[{"id":6,"specialitem":1,"mid":31,"category":71,"sub_category":2,"name":"Cold Coffee","price":"100,120,130,140,150","qty":"6","size":"XS,S,M,L,XL","sale_percent":"35","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"About","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/dea1179049de40407f5ceb3cb3619eef.png","selected_size":"XS","selected_price":"100"},{"id":11,"specialitem":0,"mid":31,"category":70,"sub_category":1,"name":"Hot Coffee","price":"120,130,140,200","qty":"6","size":"XS,S,L,XL","sale_percent":"24","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"Coffee","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/18c1503c18100c0d4a136bf5a219a66b.png","selected_size":"XS","selected_price":"120"},{"id":6,"specialitem":1,"mid":31,"category":71,"sub_category":2,"name":"Cold Coffee","price":"100,120,130,140,150","qty":"6","size":"XS,S,M,L,XL","sale_percent":"35","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"About","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/dea1179049de40407f5ceb3cb3619eef.png","selected_size":"XS","selected_price":"100"},{"id":11,"specialitem":0,"mid":31,"category":70,"sub_category":1,"name":"Hot Coffee","price":"120,130,140,200","qty":"6","size":"XS,S,L,XL","sale_percent":"24","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"Coffee","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/18c1503c18100c0d4a136bf5a219a66b.png","selected_size":"XS","selected_price":"120"}]},{"id":10,"mid":31,"title":"Breakfast","price":"250","items":[{"id":6,"specialitem":1,"mid":31,"category":71,"sub_category":2,"name":"Cold Coffee","price":"100,120,130,140,150","qty":"6","size":"XS,S,M,L,XL","sale_percent":"35","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"About","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/dea1179049de40407f5ceb3cb3619eef.png","selected_size":"XS","selected_price":"100"},{"id":11,"specialitem":0,"mid":31,"category":70,"sub_category":1,"name":"Hot Coffee","price":"120,130,140,200","qty":"6","size":"XS,S,L,XL","sale_percent":"24","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"Coffee","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/18c1503c18100c0d4a136bf5a219a66b.png","selected_size":"XS","selected_price":"120"},{"id":6,"specialitem":1,"mid":31,"category":71,"sub_category":2,"name":"Cold Coffee","price":"100,120,130,140,150","qty":"6","size":"XS,S,M,L,XL","sale_percent":"35","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"About","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/dea1179049de40407f5ceb3cb3619eef.png","selected_size":"XS","selected_price":"100"},{"id":11,"specialitem":0,"mid":31,"category":70,"sub_category":1,"name":"Hot Coffee","price":"120,130,140,200","qty":"6","size":"XS,S,L,XL","sale_percent":"24","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"Coffee","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/18c1503c18100c0d4a136bf5a219a66b.png","selected_size":"XS","selected_price":"120"},{"id":6,"specialitem":1,"mid":31,"category":71,"sub_category":2,"name":"Cold Coffee","price":"100,120,130,140,150","qty":"6","size":"XS,S,M,L,XL","sale_percent":"35","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"About","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/dea1179049de40407f5ceb3cb3619eef.png","selected_size":"XS","selected_price":"100"},{"id":11,"specialitem":0,"mid":31,"category":70,"sub_category":1,"name":"Hot Coffee","price":"120,130,140,200","qty":"6","size":"XS,S,L,XL","sale_percent":"24","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"Coffee","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/18c1503c18100c0d4a136bf5a219a66b.png","selected_size":"XS","selected_price":"120"}]},{"id":12,"mid":31,"title":"Breakfast","price":"250","items":[{"id":6,"specialitem":1,"mid":31,"category":71,"sub_category":2,"name":"Cold Coffee","price":"100,120,130,140,150","qty":"6","size":"XS,S,M,L,XL","sale_percent":"35","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"About","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/dea1179049de40407f5ceb3cb3619eef.png","selected_size":"XS","selected_price":"100"},{"id":11,"specialitem":0,"mid":31,"category":70,"sub_category":1,"name":"Hot Coffee","price":"120,130,140,200","qty":"6","size":"XS,S,L,XL","sale_percent":"24","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"Coffee","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/18c1503c18100c0d4a136bf5a219a66b.png","selected_size":"XS","selected_price":"120"},{"id":6,"specialitem":1,"mid":31,"category":71,"sub_category":2,"name":"Cold Coffee","price":"100,120,130,140,150","qty":"6","size":"XS,S,M,L,XL","sale_percent":"35","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"About","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/dea1179049de40407f5ceb3cb3619eef.png","selected_size":"XS","selected_price":"100"},{"id":11,"specialitem":0,"mid":31,"category":70,"sub_category":1,"name":"Hot Coffee","price":"120,130,140,200","qty":"6","size":"XS,S,L,XL","sale_percent":"24","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"Coffee","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/18c1503c18100c0d4a136bf5a219a66b.png","selected_size":"XS","selected_price":"120"},{"id":6,"specialitem":1,"mid":31,"category":71,"sub_category":2,"name":"Cold Coffee","price":"100,120,130,140,150","qty":"6","size":"XS,S,M,L,XL","sale_percent":"35","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"About","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/dea1179049de40407f5ceb3cb3619eef.png","selected_size":"XS","selected_price":"100"},{"id":11,"specialitem":0,"mid":31,"category":70,"sub_category":1,"name":"Hot Coffee","price":"120,130,140,200","qty":"6","size":"XS,S,L,XL","sale_percent":"24","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"Coffee","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/18c1503c18100c0d4a136bf5a219a66b.png","selected_size":"XS","selected_price":"120"},{"id":6,"specialitem":1,"mid":31,"category":71,"sub_category":2,"name":"Cold Coffee","price":"100,120,130,140,150","qty":"6","size":"XS,S,M,L,XL","sale_percent":"35","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"About","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/dea1179049de40407f5ceb3cb3619eef.png","selected_size":"XS","selected_price":"100"},{"id":11,"specialitem":0,"mid":31,"category":70,"sub_category":1,"name":"Hot Coffee","price":"120,130,140,200","qty":"6","size":"XS,S,L,XL","sale_percent":"24","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"Coffee","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/18c1503c18100c0d4a136bf5a219a66b.png","selected_size":"XS","selected_price":"120"}]}]
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
         * id : 2
         * mid : 31
         * title : Breakfast
         * price : 500
         * items : [{"id":2,"specialitem":1,"mid":70,"category":70,"sub_category":1,"name":"Hot Coffee","price":"100,120,130,140,150","qty":"10","size":"XS,S,M,L,XL","sale_percent":"42","sale_price":"0","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"}],"about":"Hot Coffee ","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/75521fe0b670ee7fdcffc1444e870049.png","selected_size":"S","selected_price":"130"},{"id":6,"specialitem":1,"mid":31,"category":71,"sub_category":2,"name":"Cold Coffee","price":"100,120,130,140,150","qty":"6","size":"XS,S,M,L,XL","sale_percent":"35","sale_price":"","extras":[{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":3,"title":"So Milk","type":"radiobutton","price":"0.25"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"},{"id":6,"title":"Chocolate","type":"checkbox","price":"0.75"}],"about":"About","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/dea1179049de40407f5ceb3cb3619eef.png","selected_size":"L","selected_price":"140"}]
         */

        private int id;
        private int mid;
        private String title;
        private String price;
        private List<ItemsBean> items;
        private String image;
        boolean isExpanded;
        private boolean isSelected;
        private int specialitem;

        public boolean isExpanded() {
            return isExpanded;
        }

        public void setExpanded(boolean expanded) {
            isExpanded = expanded;
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

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
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

        public static class ItemsBean implements Serializable{
            /**
             * id : 2
             * specialitem : 1
             * mid : 70
             * category : 70
             * sub_category : 1
             * name : Hot Coffee
             * price : 100,120,130,140,150
             * qty : 10
             * size : XS,S,M,L,XL
             * sale_percent : 42
             * sale_price : 0
             * extras : [{"id":1,"title":"Full Cream","type":"radiobutton","price":"0.25"},{"id":2,"title":"Skim Milk","type":"radiobutton","price":"0.75"},{"id":4,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":5,"title":"Charamel","type":"checkbox","price":"0.50"}]
             * about : Hot Coffee
             * image : users/H9MHZ0FPLMVPOYZ48R050VD6U/75521fe0b670ee7fdcffc1444e870049.png
             * selected_size : S
             * selected_price : 130
             */

            private int id;
            private int specialitem;
            private int mid;
            private int category;
            private int sub_category;
            private String name;
            private String price;
            private String qty;
            private String size;
            private String sale_percent;
            private String sale_price;
            private String about;
            private String image;
            private String selected_size;
            private String selected_price;
            private List<ExtrasBean> extras;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getSpecialitem() {
                return specialitem;
            }

            public void setSpecialitem(int specialitem) {
                this.specialitem = specialitem;
            }

            public int getMid() {
                return mid;
            }

            public void setMid(int mid) {
                this.mid = mid;
            }

            public int getCategory() {
                return category;
            }

            public void setCategory(int category) {
                this.category = category;
            }

            public int getSub_category() {
                return sub_category;
            }

            public void setSub_category(int sub_category) {
                this.sub_category = sub_category;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getQty() {
                return qty;
            }

            public void setQty(String qty) {
                this.qty = qty;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
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

            public String getAbout() {
                return about;
            }

            public void setAbout(String about) {
                this.about = about;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getSelected_size() {
                return selected_size;
            }

            public void setSelected_size(String selected_size) {
                this.selected_size = selected_size;
            }

            public String getSelected_price() {
                return selected_price;
            }

            public void setSelected_price(String selected_price) {
                this.selected_price = selected_price;
            }

            public List<ExtrasBean> getExtras() {
                return extras;
            }

            public void setExtras(List<ExtrasBean> extras) {
                this.extras = extras;
            }

            public static class ExtrasBean {
                /**
                 * id : 1
                 * title : Full Cream
                 * type : radiobutton
                 * price : 0.25
                 */

                private int id;
                private String title;
                private String type;
                private String price;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
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
    }
}
