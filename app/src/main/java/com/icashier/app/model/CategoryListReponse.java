package com.icashier.app.model;

import com.icashier.app.constants.ServerConstants;

import java.io.Serializable;
import java.util.List;

public class CategoryListReponse implements Serializable{

    /**
     * statusCode : 200
     * result : [{"id":12,"name":"Cold Beverages","image":"categories/coffee.png","subCategories":[{"id":3,"name":"Ice Tea","cid":12,"image":"categories/icon_cold_bevrages.png"}]},{"id":16,"name":"Chinese Cuisine","image":"categories/noddles.png","subCategories":[]},{"id":17,"name":"Refreshment","image":"categories/icon_cold_bevrages.png","subCategories":[]}]
     */

    private int code;
    private List<ResultBean> result;

    public int getCode() {
        return code;
    }

    public void setStatusCode(int statusCode) {
        this.code = code;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable{
        /**
         * id : 12
         * name : Cold Beverages
         * image : categories/coffee.png
         * subCategories : [{"id":3,"name":"Ice Tea","cid":12,"image":"categories/icon_cold_bevrages.png"}]
         */

        private int id;
        private String name;
        private String image;
        private boolean isExpanded;
        private String arabic_name;
        private List<SubCategoriesBean> subCategories;

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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public List<SubCategoriesBean> getSubCategories() {
            return subCategories;
        }

        public void setSubCategories(List<SubCategoriesBean> subCategories) {
            this.subCategories = subCategories;
        }

        public boolean isExpanded() {
            return isExpanded;
        }

        public void setExpanded(boolean expanded) {
            isExpanded = expanded;
        }

        public String getArabic_name() {
            return arabic_name;
        }

        public void setArabic_name(String arabic_name) {
            this.arabic_name = arabic_name;
        }


        public static class SubCategoriesBean {
            /**
             * id : 3
             * name : Ice Tea
             * cid : 12
             * image : categories/icon_cold_bevrages.png
             */

            private int id;
            private String name;
            private int cid;
            private String image;

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

            public int getCid() {
                return cid;
            }

            public void setCid(int cid) {
                this.cid = cid;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }
    }
}
