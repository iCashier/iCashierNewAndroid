package com.icashier.app.model;

import com.icashier.app.constants.ServerConstants;

import java.io.Serializable;
import java.util.List;

public class MerchantDetailResponse implements Serializable{

    /**
     * code : 201
     * message : User profile update successfully.
     * result : {"title":"Starbucks","tagline":"Coffee","network":"Wifi","password":"123456","location":"Noida","lat":"28.535516100000002","lng":"77.3910265","logo":"","image":[{"id":0,"file":"users/H9MHZ0FPLMVPOYZ48R050VD6U/1f986424b6aa4850a140b970a51c6cca.png"},{"id":1,"file":"users/H9MHZ0FPLMVPOYZ48R050VD6U/65c073aad90d9d48a2cb50a22c506f44.png"},{"id":2,"file":"users/H9MHZ0FPLMVPOYZ48R050VD6U/c4f75907b54c3ec3af49a461928cdcba.png"},{"id":3,"file":"users/H9MHZ0FPLMVPOYZ48R050VD6U/d6595922ffe8893ded205eee7111cc7c.png"},{"id":4,"file":"users/H9MHZ0FPLMVPOYZ48R050VD6U/3e5d6214b90b63153c2f1562e8263712.png"},{"id":5,"file":"users/H9MHZ0FPLMVPOYZ48R050VD6U/52363fc7b6611d9a969bf531f310b82c.png"}],"services":"Dine In,Party","site":"www.gg.com","phone":"","whatsapp":"","facebook":"","twitter":"","instagram":""}
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

    public static class ResultBean implements Serializable {
        /**
         * title : Starbucks
         * tagline : Coffee
         * network : Wifi
         * password : 123456
         * location : Noida
         * lat : 28.535516100000002
         * lng : 77.3910265
         * logo :
         * image : [{"id":0,"file":"users/H9MHZ0FPLMVPOYZ48R050VD6U/1f986424b6aa4850a140b970a51c6cca.png"},{"id":1,"file":"users/H9MHZ0FPLMVPOYZ48R050VD6U/65c073aad90d9d48a2cb50a22c506f44.png"},{"id":2,"file":"users/H9MHZ0FPLMVPOYZ48R050VD6U/c4f75907b54c3ec3af49a461928cdcba.png"},{"id":3,"file":"users/H9MHZ0FPLMVPOYZ48R050VD6U/d6595922ffe8893ded205eee7111cc7c.png"},{"id":4,"file":"users/H9MHZ0FPLMVPOYZ48R050VD6U/3e5d6214b90b63153c2f1562e8263712.png"},{"id":5,"file":"users/H9MHZ0FPLMVPOYZ48R050VD6U/52363fc7b6611d9a969bf531f310b82c.png"}]
         * services : Dine In,Party
         * site : www.gg.com
         * phone :
         * whatsapp :
         * facebook :
         * twitter :
         * instagram :
         */

        private String title;
        private String tagline;
        private String network;
        private String password;
        private String location;
        private String lat;
        private String lng;
        private String logo;
        private String services;
        private String site;
        private String phone;
        private String whatsapp;
        private String facebook;
        private String twitter;
        private String instagram;
        private List<ImageBean> image;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTagline() {
            return tagline;
        }

        public void setTagline(String tagline) {
            this.tagline = tagline;
        }

        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
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

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getServices() {
            return services;
        }

        public void setServices(String services) {
            this.services = services;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getWhatsapp() {
            return whatsapp;
        }

        public void setWhatsapp(String whatsapp) {
            this.whatsapp = whatsapp;
        }

        public String getFacebook() {
            return facebook;
        }

        public void setFacebook(String facebook) {
            this.facebook = facebook;
        }

        public String getTwitter() {
            return twitter;
        }

        public void setTwitter(String twitter) {
            this.twitter = twitter;
        }

        public String getInstagram() {
            return instagram;
        }

        public void setInstagram(String instagram) {
            this.instagram = instagram;
        }

        public List<ImageBean> getImage() {
            return image;
        }

        public void setImage(List<ImageBean> image) {
            this.image = image;
        }

        public static class ImageBean {
            /**
             * id : 0
             * file : users/H9MHZ0FPLMVPOYZ48R050VD6U/1f986424b6aa4850a140b970a51c6cca.png
             */

            private int id;
            private String image;

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
        }
    }
}
