package com.icashier.app.model;

import com.icashier.app.constants.ServerConstants;

import java.io.Serializable;
import java.util.List;

public class ExistingItemList implements Serializable{
    /**
     * code : 200
     * message : Success.
     * result : [{"id":17,"uid":"H9MHZ0FPLMVPOYZ48R050VD6U","category":{"id":48,"name":"Hot Beverages"},"sub_category":{"id":51,"name":"Coffee"},"name":"Coffee","price":"150","qty":"5","size":"M","sale_percent":"0","sale_price":"30","extras":[{"title":"Cheese","id":12,"type":"radiobutton","price":"50"},{"title":"Choco Syrup","id":10,"type":"radiobutton","price":"20"}],"about":"Hello","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/80968d7d560f4c5785cf270025e9668a.png"},{"id":18,"uid":"H9MHZ0FPLMVPOYZ48R050VD6U","category":{"id":20,"name":"Cold Beverages"},"sub_category":{"id":4,"name":"Chocolate Crusher"},"name":"Cold Coffee","price":"120","qty":"5","size":"M","sale_percent":"0","sale_price":"30","extras":[{"title":"Cheese","id":12,"type":"radiobutton","price":"50"},{"title":"Choco Syrup","id":10,"type":"radiobutton","price":"20"}],"about":"About","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/6afc1fb8be72af654884b314072fd3c1.png"},{"id":20,"uid":"H9MHZ0FPLMVPOYZ48R050VD6U","category":{"id":49,"name":"Bakery"},"sub_category":{"id":48,"name":"Cake"},"name":"Red Velvet Cake","price":"120","qty":"9","size":"M","sale_percent":"0","sale_price":"30","extras":[{"id":15,"title":"Half-n-half","type":"radiobutton","price":"0.50"},{"id":17,"title":"Soy Milk","type":"radiobutton","price":"0.75"},{"id":19,"title":"Whipped Cream","type":"checkbox","price":"1.50"},{"id":22,"title":"Hazelnut","type":"checkbox","price":"0.95"}],"about":"About","image":"users/H9MHZ0FPLMVPOYZ48R050VD6U/ebac12efdbc90109b7c853273ac2e53f.png"}]
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
         * id : 17
         * uid : H9MHZ0FPLMVPOYZ48R050VD6U
         * category : {"id":48,"name":"Hot Beverages"}
         * sub_category : {"id":51,"name":"Coffee"}
         * name : Coffee
         * price : 150
         * qty : 5
         * size : M
         * sale_percent : 0
         * sale_price : 30
         * extras : [{"title":"Cheese","id":12,"type":"radiobutton","price":"50"},{"title":"Choco Syrup","id":10,"type":"radiobutton","price":"20"}]
         * about : Hello
         * image : users/H9MHZ0FPLMVPOYZ48R050VD6U/80968d7d560f4c5785cf270025e9668a.png
         */

        private int id;
        private String uid;
        private CategoryBean category;
        private SubCategoryBean sub_category;
        private String name;
        private String price;
        private String qty;
        private String size;
        private String sale_percent;
        private String sale_price;
        private String about;
        private String image;
        private List<ExtrasBean> extras;
        private int specialitem;
        private boolean isSelected;
        String selectedSize;
        String selectedPrice;
        private String calories;
        private String allergies;

        public String getSelectedSize() {
            return selectedSize;
        }

        public void setSelectedSize(String selectedSize) {
            this.selectedSize = selectedSize;
        }

        public String getSelectedPrice() {
            return selectedPrice;
        }

        public void setSelectedPrice(String selectedPrice) {
            this.selectedPrice = selectedPrice;
        }


        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }


        public int getSpecialitem() {
            return specialitem;
        }

        public void setSpecialitem(int specialitem) {
            this.specialitem = specialitem;
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

        public CategoryBean getCategory() {
            return category;
        }

        public void setCategory(CategoryBean category) {
            this.category = category;
        }

        public SubCategoryBean getSub_category() {
            return sub_category;
        }

        public void setSub_category(SubCategoryBean sub_category) {
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

        public List<ExtrasBean> getExtras() {
            return extras;
        }

        public void setExtras(List<ExtrasBean> extras) {
            this.extras = extras;
        }

        public String getCalories() {
            return calories;
        }

        public void setCalories(String calories) {
            this.calories = calories;
        }

        public String getAllergies() {
            return allergies;
        }

        public void setAllergies(String allergies) {
            this.allergies = allergies;
        }

        public static class CategoryBean implements Serializable{
            /**
             * id : 48
             * name : Hot Beverages
             */

            private int id;
            private String name;
            private String arabic_name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getArabic_name() {
                return arabic_name;
            }

            public void setArabic_name(String arabic_name) {
                this.arabic_name = arabic_name;
            }
        }

        public static class SubCategoryBean implements Serializable{
            /**
             * id : 51
             * name : Coffee
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class ExtrasBean implements Serializable{
            /**
             * title : Cheese
             * id : 12
             * type : radiobutton
             * price : 50
             */

            private String title;
            private int id;
            private String type;
            private String price;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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
