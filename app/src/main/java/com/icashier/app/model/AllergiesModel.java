
package com.icashier.app.model;
import java.io.Serializable;
import java.util.List;

public class AllergiesModel implements Serializable {

    private long code;
    private String message;
    private List<Result> result;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public static class Result implements Serializable{

        private String arabic_name;
        private String icon;
        private long id;
        private String name;
        private boolean isSelected;

        public String getarabic_name() {
            return arabic_name;
        }

        public void setarabic_name(String arabic_name) {
            this.arabic_name = arabic_name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }


}
