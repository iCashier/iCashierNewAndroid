package com.icashier.app.model;

import java.io.Serializable;
import java.util.List;

public class MenuResponse implements Serializable {


    /**
     * code : 200
     * message : Success.
     * merchant : {"id":95,"title":"ساگر رتنا","tagline":"Mysterious Food","network":"Qwerty","password":"qwerty","location":"Al Badia Blvd - Dubai - United Arab Emirates","lat":25.21710021187992,"lng":55.36136336624622,"services":"Delivery,Reservation,Pickup","site":"www.vc.com","phone":"","whatsapp":"","facebook":"","twitter":"","instagram":"","image":[{"id":8,"file":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/8e2f571b7fb4c13d5dcc07e52d4120dd.png"}],"logo":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/9440527ab8ee17181d10e1f46f737732.png","Categories":[{"id":88,"name":"Bakery","image":"http://52.27.53.102/imenu/uploads/categories/cupcake.png","subCategories":[{"id":17,"name":"Cakes","cid":88,"image":"http://52.27.53.102/imenu/uploads/categories/cupcake.png","items":[{"id":18,"specialitem":1,"mid":95,"category":88,"sub_category":17,"name":"Red Velvet Rose","qty":"1","sale_percent":"46","sale_price":"","extras":[{"id":27,"title":"Full Cream","type":"radiobutton","price":"25"},{"id":28,"title":"Halfnhalf","type":"radiobutton","price":"2"},{"id":29,"title":"Whipped Cream","type":"checkbox","price":"25"},{"id":30,"title":"Caramel","type":"checkbox","price":"8"}],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/c0d723e0a29de03c5a41f06861378e2f.png","numbers":1,"like":0,"size_price":[{"size":"XS","price":"100"},{"size":"S","price":"200"}],"sub_category_name":"Cakes"},{"id":19,"specialitem":0,"mid":95,"category":88,"sub_category":17,"name":"Black Forest","qty":"25","sale_percent":"","sale_price":"112","extras":[],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/7c651c7b959f985cea0d986916b98456.png","numbers":0,"like":0,"size_price":[{"size":"XS","price":"250"},{"size":"S","price":"350"}],"sub_category_name":"Cakes"},{"id":31,"specialitem":0,"mid":95,"category":88,"sub_category":17,"name":"Chicken","qty":"12","sale_percent":"55","sale_price":"31","extras":[],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/4e41961e40473a585301a26de61cbaf3.png","numbers":0,"like":0,"size_price":[{"size":"XS","price":"120"}],"sub_category_name":"Cakes"},{"id":42,"specialitem":0,"mid":95,"category":88,"sub_category":17,"name":"Mocha","qty":"12","sale_percent":"33","sale_price":"","extras":[{"id":27,"title":"Full Cream","type":"radiobutton","price":"25"},{"id":29,"title":"Whipped Cream","type":"checkbox","price":"25"}],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/704b7f2102f9f01e414f0b5d735ee69a.png","numbers":0,"like":0,"size_price":[{"size":"M","price":"100"},{"size":"L","price":"120"}],"sub_category_name":"Cakes"}]}]},{"id":102,"name":"Beverages","image":"http://52.27.53.102/imenu/uploads/categories/icon_cold_bevrages.png","subCategories":[{"id":45,"name":"Cold Beverges","cid":102,"image":"http://52.27.53.102/imenu/uploads/categories/icon_cold_bevrages.png","items":[{"id":40,"specialitem":0,"mid":95,"category":102,"sub_category":45,"name":"Lemon Tea","qty":"6","sale_percent":"1","sale_price":"1","extras":[],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/a9ff1001dbed5eaf92e532f60c5664cf.png","numbers":0,"like":0,"size_price":[{"size":"XS","price":"40"}],"sub_category_name":"Cold Beverges"}]}]},{"id":103,"name":"Main course","image":"http://52.27.53.102/imenu/uploads/categories/noddles.png","subCategories":[{"id":46,"name":"Chicken","cid":103,"image":"http://52.27.53.102/imenu/uploads/categories/pizza.png","items":[{"id":41,"specialitem":0,"mid":95,"category":103,"sub_category":46,"name":"Pizza","qty":"26","sale_percent":"1","sale_price":"1","extras":[],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/2561b0525cb13efb17bfba4f680a7330.png","numbers":0,"like":0,"size_price":[{"size":"M","price":"255"}],"sub_category_name":"Chicken"}]}]},{"id":105,"name":"Chinese","image":"http://52.27.53.102/imenu/uploads/categories/icon_coffee.png","subCategories":[{"id":47,"name":"Noodles","cid":105,"image":"http://52.27.53.102/imenu/uploads/categories/noddles.png","items":[{"id":38,"specialitem":0,"mid":95,"category":105,"sub_category":47,"name":"Noodels","qty":"8","sale_percent":"11","sale_price":"","extras":[],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/c1775b945ceb565edb8c58a2fb54bf8f.png","numbers":0,"like":0,"size_price":[{"size":"XS","price":"50"}],"sub_category_name":"Noodles"},{"id":39,"specialitem":0,"mid":95,"category":105,"sub_category":47,"name":"Maggi","qty":"25","sale_percent":"1","sale_price":"1","extras":[{"id":30,"title":"Caramel","type":"checkbox","price":"8"}],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/0cd48fa955131b604a455910d74ab9fb.png","numbers":0,"like":0,"size_price":[{"size":"XL","price":"120"}],"sub_category_name":"Noodles"}]}]}],"mealsDetails":[]}
     */

    private int code;
    private String message;
    private MerchantBean merchant;

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

    public MerchantBean getMerchant() {
        return merchant;
    }

    public void setMerchant(MerchantBean merchant) {
        this.merchant = merchant;
    }

    public static class MerchantBean implements Serializable{
        /**
         * id : 95
         * title : ساگر رتنا
         * tagline : Mysterious Food
         * network : Qwerty
         * password : qwerty
         * location : Al Badia Blvd - Dubai - United Arab Emirates
         * lat : 25.21710021187992
         * lng : 55.36136336624622
         * services : Delivery,Reservation,Pickup
         * site : www.vc.com
         * phone :
         * whatsapp :
         * facebook :
         * twitter :
         * instagram :
         * image : [{"id":8,"file":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/8e2f571b7fb4c13d5dcc07e52d4120dd.png"}]
         * logo : http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/9440527ab8ee17181d10e1f46f737732.png
         * Categories : [{"id":88,"name":"Bakery","image":"http://52.27.53.102/imenu/uploads/categories/cupcake.png","subCategories":[{"id":17,"name":"Cakes","cid":88,"image":"http://52.27.53.102/imenu/uploads/categories/cupcake.png","items":[{"id":18,"specialitem":1,"mid":95,"category":88,"sub_category":17,"name":"Red Velvet Rose","qty":"1","sale_percent":"46","sale_price":"","extras":[{"id":27,"title":"Full Cream","type":"radiobutton","price":"25"},{"id":28,"title":"Halfnhalf","type":"radiobutton","price":"2"},{"id":29,"title":"Whipped Cream","type":"checkbox","price":"25"},{"id":30,"title":"Caramel","type":"checkbox","price":"8"}],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/c0d723e0a29de03c5a41f06861378e2f.png","numbers":1,"like":0,"size_price":[{"size":"XS","price":"100"},{"size":"S","price":"200"}],"sub_category_name":"Cakes"},{"id":19,"specialitem":0,"mid":95,"category":88,"sub_category":17,"name":"Black Forest","qty":"25","sale_percent":"","sale_price":"112","extras":[],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/7c651c7b959f985cea0d986916b98456.png","numbers":0,"like":0,"size_price":[{"size":"XS","price":"250"},{"size":"S","price":"350"}],"sub_category_name":"Cakes"},{"id":31,"specialitem":0,"mid":95,"category":88,"sub_category":17,"name":"Chicken","qty":"12","sale_percent":"55","sale_price":"31","extras":[],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/4e41961e40473a585301a26de61cbaf3.png","numbers":0,"like":0,"size_price":[{"size":"XS","price":"120"}],"sub_category_name":"Cakes"},{"id":42,"specialitem":0,"mid":95,"category":88,"sub_category":17,"name":"Mocha","qty":"12","sale_percent":"33","sale_price":"","extras":[{"id":27,"title":"Full Cream","type":"radiobutton","price":"25"},{"id":29,"title":"Whipped Cream","type":"checkbox","price":"25"}],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/704b7f2102f9f01e414f0b5d735ee69a.png","numbers":0,"like":0,"size_price":[{"size":"M","price":"100"},{"size":"L","price":"120"}],"sub_category_name":"Cakes"}]}]},{"id":102,"name":"Beverages","image":"http://52.27.53.102/imenu/uploads/categories/icon_cold_bevrages.png","subCategories":[{"id":45,"name":"Cold Beverges","cid":102,"image":"http://52.27.53.102/imenu/uploads/categories/icon_cold_bevrages.png","items":[{"id":40,"specialitem":0,"mid":95,"category":102,"sub_category":45,"name":"Lemon Tea","qty":"6","sale_percent":"1","sale_price":"1","extras":[],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/a9ff1001dbed5eaf92e532f60c5664cf.png","numbers":0,"like":0,"size_price":[{"size":"XS","price":"40"}],"sub_category_name":"Cold Beverges"}]}]},{"id":103,"name":"Main course","image":"http://52.27.53.102/imenu/uploads/categories/noddles.png","subCategories":[{"id":46,"name":"Chicken","cid":103,"image":"http://52.27.53.102/imenu/uploads/categories/pizza.png","items":[{"id":41,"specialitem":0,"mid":95,"category":103,"sub_category":46,"name":"Pizza","qty":"26","sale_percent":"1","sale_price":"1","extras":[],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/2561b0525cb13efb17bfba4f680a7330.png","numbers":0,"like":0,"size_price":[{"size":"M","price":"255"}],"sub_category_name":"Chicken"}]}]},{"id":105,"name":"Chinese","image":"http://52.27.53.102/imenu/uploads/categories/icon_coffee.png","subCategories":[{"id":47,"name":"Noodles","cid":105,"image":"http://52.27.53.102/imenu/uploads/categories/noddles.png","items":[{"id":38,"specialitem":0,"mid":95,"category":105,"sub_category":47,"name":"Noodels","qty":"8","sale_percent":"11","sale_price":"","extras":[],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/c1775b945ceb565edb8c58a2fb54bf8f.png","numbers":0,"like":0,"size_price":[{"size":"XS","price":"50"}],"sub_category_name":"Noodles"},{"id":39,"specialitem":0,"mid":95,"category":105,"sub_category":47,"name":"Maggi","qty":"25","sale_percent":"1","sale_price":"1","extras":[{"id":30,"title":"Caramel","type":"checkbox","price":"8"}],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/0cd48fa955131b604a455910d74ab9fb.png","numbers":0,"like":0,"size_price":[{"size":"XL","price":"120"}],"sub_category_name":"Noodles"}]}]}]
         * mealsDetails : []
         */

        private int id;
        private String title;
        private String tagline;
        private String network;
        private String password;
        private String location;
        private double lat;
        private double lng;
        private String services;
        private String site;
        private String phone;
        private String whatsapp;
        private String facebook;
        private String twitter;
        private String instagram;
        private String logo;
        private List<ImageBean> image;
        private List<CategoriesBean> Categories;
        private List<CategoriesBean.SubCategoriesBean.ItemsBean> mealsDetails;
        private List<CategoriesBean.SubCategoriesBean.ItemsBean> deals;


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

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
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

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public List<ImageBean> getImage() {
            return image;
        }

        public void setImage(List<ImageBean> image) {
            this.image = image;
        }

        public List<CategoriesBean> getCategories() {
            return Categories;
        }

        public void setCategories(List<CategoriesBean> Categories) {
            this.Categories = Categories;
        }

        public List<CategoriesBean.SubCategoriesBean.ItemsBean> getMealsDetails() {
            return mealsDetails;
        }

        public void setMealsDetails(List<CategoriesBean.SubCategoriesBean.ItemsBean> mealsDetails) {
            this.mealsDetails = mealsDetails;
        }

        public List<CategoriesBean.SubCategoriesBean.ItemsBean> getDeals() {
            return deals;
        }

        public void setDeals(List<CategoriesBean.SubCategoriesBean.ItemsBean> deals) {
            this.deals = deals;
        }

        public static class ImageBean implements Serializable{
            /**
             * id : 8
             * file : http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/8e2f571b7fb4c13d5dcc07e52d4120dd.png
             */

            private int id;
            private String file;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getFile() {
                return file;
            }

            public void setFile(String file) {
                this.file = file;
            }
        }

        public static class CategoriesBean implements Serializable{
            /**
             * id : 88
             * name : Bakery
             * image : http://52.27.53.102/imenu/uploads/categories/cupcake.png
             * subCategories : [{"id":17,"name":"Cakes","cid":88,"image":"http://52.27.53.102/imenu/uploads/categories/cupcake.png","items":[{"id":18,"specialitem":1,"mid":95,"category":88,"sub_category":17,"name":"Red Velvet Rose","qty":"1","sale_percent":"46","sale_price":"","extras":[{"id":27,"title":"Full Cream","type":"radiobutton","price":"25"},{"id":28,"title":"Halfnhalf","type":"radiobutton","price":"2"},{"id":29,"title":"Whipped Cream","type":"checkbox","price":"25"},{"id":30,"title":"Caramel","type":"checkbox","price":"8"}],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/c0d723e0a29de03c5a41f06861378e2f.png","numbers":1,"like":0,"size_price":[{"size":"XS","price":"100"},{"size":"S","price":"200"}],"sub_category_name":"Cakes"},{"id":19,"specialitem":0,"mid":95,"category":88,"sub_category":17,"name":"Black Forest","qty":"25","sale_percent":"","sale_price":"112","extras":[],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/7c651c7b959f985cea0d986916b98456.png","numbers":0,"like":0,"size_price":[{"size":"XS","price":"250"},{"size":"S","price":"350"}],"sub_category_name":"Cakes"},{"id":31,"specialitem":0,"mid":95,"category":88,"sub_category":17,"name":"Chicken","qty":"12","sale_percent":"55","sale_price":"31","extras":[],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/4e41961e40473a585301a26de61cbaf3.png","numbers":0,"like":0,"size_price":[{"size":"XS","price":"120"}],"sub_category_name":"Cakes"},{"id":42,"specialitem":0,"mid":95,"category":88,"sub_category":17,"name":"Mocha","qty":"12","sale_percent":"33","sale_price":"","extras":[{"id":27,"title":"Full Cream","type":"radiobutton","price":"25"},{"id":29,"title":"Whipped Cream","type":"checkbox","price":"25"}],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/704b7f2102f9f01e414f0b5d735ee69a.png","numbers":0,"like":0,"size_price":[{"size":"M","price":"100"},{"size":"L","price":"120"}],"sub_category_name":"Cakes"}]}]
             */

            private int id;
            private String name;
            private String image;
            private List<SubCategoriesBean> subCategories;
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

            public List<SubCategoriesBean> getSubCategories() {
                return subCategories;
            }

            public void setSubCategories(List<SubCategoriesBean> subCategories) {
                this.subCategories = subCategories;
            }

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            public static class SubCategoriesBean implements Serializable{
                /**
                 * id : 17
                 * name : Cakes
                 * cid : 88
                 * image : http://52.27.53.102/imenu/uploads/categories/cupcake.png
                 * items : [{"id":18,"specialitem":1,"mid":95,"category":88,"sub_category":17,"name":"Red Velvet Rose","qty":"1","sale_percent":"46","sale_price":"","extras":[{"id":27,"title":"Full Cream","type":"radiobutton","price":"25"},{"id":28,"title":"Halfnhalf","type":"radiobutton","price":"2"},{"id":29,"title":"Whipped Cream","type":"checkbox","price":"25"},{"id":30,"title":"Caramel","type":"checkbox","price":"8"}],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/c0d723e0a29de03c5a41f06861378e2f.png","numbers":1,"like":0,"size_price":[{"size":"XS","price":"100"},{"size":"S","price":"200"}],"sub_category_name":"Cakes"},{"id":19,"specialitem":0,"mid":95,"category":88,"sub_category":17,"name":"Black Forest","qty":"25","sale_percent":"","sale_price":"112","extras":[],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/7c651c7b959f985cea0d986916b98456.png","numbers":0,"like":0,"size_price":[{"size":"XS","price":"250"},{"size":"S","price":"350"}],"sub_category_name":"Cakes"},{"id":31,"specialitem":0,"mid":95,"category":88,"sub_category":17,"name":"Chicken","qty":"12","sale_percent":"55","sale_price":"31","extras":[],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/4e41961e40473a585301a26de61cbaf3.png","numbers":0,"like":0,"size_price":[{"size":"XS","price":"120"}],"sub_category_name":"Cakes"},{"id":42,"specialitem":0,"mid":95,"category":88,"sub_category":17,"name":"Mocha","qty":"12","sale_percent":"33","sale_price":"","extras":[{"id":27,"title":"Full Cream","type":"radiobutton","price":"25"},{"id":29,"title":"Whipped Cream","type":"checkbox","price":"25"}],"about":"","image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/704b7f2102f9f01e414f0b5d735ee69a.png","numbers":0,"like":0,"size_price":[{"size":"M","price":"100"},{"size":"L","price":"120"}],"sub_category_name":"Cakes"}]
                 */

                private int id;
                private String name;
                private int cid;
                private String image;
                private List<ItemsBean> items;

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

                public List<ItemsBean> getItems() {
                    return items;
                }

                public void setItems(List<ItemsBean> items) {
                    this.items = items;
                }

                public static class ItemsBean implements Serializable{
                    /**
                     * id : 18
                     * specialitem : 1
                     * mid : 95
                     * category : 88
                     * sub_category : 17
                     * name : Red Velvet Rose
                     * qty : 1
                     * sale_percent : 46
                     * sale_price :
                     * extras : [{"id":27,"title":"Full Cream","type":"radiobutton","price":"25"},{"id":28,"title":"Halfnhalf","type":"radiobutton","price":"2"},{"id":29,"title":"Whipped Cream","type":"checkbox","price":"25"},{"id":30,"title":"Caramel","type":"checkbox","price":"8"}]
                     * about :
                     * image : http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/c0d723e0a29de03c5a41f06861378e2f.png
                     * numbers : 1
                     * like : 0
                     * size_price : [{"size":"XS","price":"100"},{"size":"S","price":"200"}]
                     * sub_category_name : Cakes
                     */

                    private int id;
                    private int specialitem;
                    private int mid;
                   // private int category;
                   // private int sub_category;
                    private String name;
                    private String qty;
                    private String sale_percent;
                    private String sale_price;
                    private String about;
                    private String image;
                    private int numbers;
                    private int like;
                    private String sub_category_name;
                    private List<ExtrasBean> extras;
                    private List<SizePriceBean> size_price;
                    private boolean isSelected;
                    private boolean isExpanded;
                    private boolean isPopupShowing;
                    private int sold;
                    private boolean addedToCart;
                    private String totalPrice="";
                    private int count=1;
                    private String selectedPrice="";
                    private String type="item";
                    private String title;
                    private String price;
                    private List<ItemsBean> items;

                    private String fromDate;
                    private String toDate;
                    private String days;
                    private String fromTime;
                    private String toTime;
                    private String withItems;
                    private String moreThan;
                    private String createdOn;
                    private boolean isUnlimited;
                    private List<ItemsBean> itemsDetails;
                    private List<ItemsBean> withItemsDetails;
                    private int itemSold;
                    private int sale_count;
                    private String calories;
                    private List<AllergiesModel.Result> allergieDetail;





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

                    /*public int getCategory() {
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
                    }*/

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getQty() {
                        return qty;
                    }

                    public void setQty(String qty) {
                        this.qty = qty;
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

                    public int getNumbers() {
                        return numbers;
                    }

                    public void setNumbers(int numbers) {
                        this.numbers = numbers;
                    }

                    public int getLike() {
                        return like;
                    }

                    public void setLike(int like) {
                        this.like = like;
                    }

                    public String getSub_category_name() {
                        return sub_category_name;
                    }

                    public void setSub_category_name(String sub_category_name) {
                        this.sub_category_name = sub_category_name;
                    }

                    public List<ExtrasBean> getExtras() {
                        return extras;
                    }

                    public void setExtras(List<ExtrasBean> extras) {
                        this.extras = extras;
                    }

                    public List<SizePriceBean> getSize_price() {
                        return size_price;
                    }

                    public void setSize_price(List<SizePriceBean> size_price) {
                        this.size_price = size_price;
                    }

                    public boolean isSelected() {
                        return isSelected;
                    }

                    public void setSelected(boolean selected) {
                        isSelected = selected;
                    }

                    public boolean isExpanded() {
                        return isExpanded;
                    }

                    public void setExpanded(boolean expanded) {
                        isExpanded = expanded;
                    }

                    public boolean isPopupShowing() {
                        return isPopupShowing;
                    }

                    public void setPopupShowing(boolean popupShowing) {
                        isPopupShowing = popupShowing;
                    }

                    public int getSold() {
                        return sold;
                    }

                    public void setSold(int sold) {
                        this.sold = sold;
                    }



                    public boolean isAddedToCart() {
                        return addedToCart;
                    }

                    public void setAddedToCart(boolean addedToCart) {
                        this.addedToCart = addedToCart;
                    }

                    public String getTotalPrice() {
                        return totalPrice;
                    }

                    public void setTotalPrice(String totalPrice) {
                        this.totalPrice = totalPrice;
                    }

                    public int getCount() {
                        return count;
                    }

                    public void setCount(int count) {
                        this.count = count;
                    }

                    public String getSelectedPrice() {
                        return selectedPrice;
                    }

                    public void setSelectedPrice(String selectedPrice) {
                        this.selectedPrice = selectedPrice;
                    }


                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
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

                    public boolean isUnlimited() {
                        return isUnlimited;
                    }

                    public void setUnlimited(boolean unlimited) {
                        isUnlimited = unlimited;
                    }

                    public int getItemSold() {
                        return itemSold;
                    }

                    public void setItemSold(int itemSold) {
                        this.itemSold = itemSold;
                    }

                    public int getSale_count() {
                        return sale_count;
                    }

                    public void setSale_count(int sale_count) {
                        this.sale_count = sale_count;
                    }

                    public String getCalories() {
                        return calories;
                    }

                    public void setCalories(String calories) {
                        this.calories = calories;
                    }

                    public List<AllergiesModel.Result> getAllergieDetail() {
                        return allergieDetail;
                    }

                    public void setAllergieDetail(List<AllergiesModel.Result> allergieDetail) {
                        this.allergieDetail = allergieDetail;
                    }

                    public static class ExtrasBean implements Serializable{
                        /**
                         * id : 27
                         * title : Full Cream
                         * type : radiobutton
                         * price : 25
                         */

                        private int id;
                        private String title;
                        private String type;
                        private String price;
                        private boolean isChecked;

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

                        public boolean isChecked() {
                            return isChecked;
                        }

                        public void setChecked(boolean checked) {
                            isChecked = checked;
                        }
                    }

                    public static class SizePriceBean implements Serializable{
                        /**
                         * size : XS
                         * price : 100
                         */

                        private String size;
                        private String price;

                        public String getSize() {
                            return size;
                        }

                        public void setSize(String size) {
                            this.size = size;
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
    }
}
