package com.icashier.app.model;

import java.io.Serializable;
import java.util.List;

public class CategoryImagesResponse implements Serializable{
    /**
     * statusCode : 200
     * result : [{"id":1,"name":"xpm.png","image":"categories/92b3b7d0fddac8d905c5076a3c601122.jpeg"},{"id":2,"name":"dev.png","image":"categories/92b3b7d0fddac8d905c5076a3c601122.jpeg"}]
     */

    private int statusCode;
    private List<ResultBean> result;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
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
         * name : xpm.png
         * image : categories/92b3b7d0fddac8d905c5076a3c601122.jpeg
         */

        private int id;
        private String name;
        private String image;
        private boolean isSelected;


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

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
