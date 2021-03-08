package com.icashier.app.model;

import java.io.Serializable;
import java.util.List;

public class CartItemModel implements Serializable {
    private int id;
    private int specialitem;
    private int mid;
    private int category;
    private int sub_category;
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
    private boolean isSelected=true;
    private boolean isExpanded;
    private boolean isPopupShowing;
    private boolean isCartPopupShowing;
    private int sold;
    private String selectedPrice;
    private boolean addedToCart;
    private String totalPrice;
    private float finalPrice;
    private String type;
    private String qtyAddedToCart;
    private String sizeAddedToCart;
    private String title;
    private String price;
    private List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean> items;
    private List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean> itemsDetails;
    private List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean> withItemsDetails;
    private List<ExtrasBean> extrasAddedToCart;
    private String priceForItem;
    private int sale_count;
    private String titleAr;


    public String getTitleAr() {
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
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

    public List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean> items) {
        this.items = items;
    }





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

    public boolean isCartPopupShowing() {
        return isCartPopupShowing;
    }

    public void setCartPopupShowing(boolean cartPopupShowing) {
        isCartPopupShowing = cartPopupShowing;
    }


    public String getSelectedPrice() {
        return selectedPrice;
    }

    public void setSelectedPrice(String selectedPrice) {
        this.selectedPrice = selectedPrice;
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

    public float getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(float finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getQtyAddedToCart() {
        return qtyAddedToCart;
    }

    public void setQtyAddedToCart(String qtyAddedToCart) {
        this.qtyAddedToCart = qtyAddedToCart;
    }

    public String getSizeAddedToCart() {
        return sizeAddedToCart;
    }

    public void setSizeAddedToCart(String sizeAddedToCart) {
        this.sizeAddedToCart = sizeAddedToCart;
    }


    public String getPriceForItem() {
        return priceForItem;
    }

    public void setPriceForItem(String priceForItem) {
        this.priceForItem = priceForItem;
    }

    public List<ExtrasBean> getExtrasAddedToCart() {
        return extrasAddedToCart;
    }

    public void setExtrasAddedToCart(List<ExtrasBean> extrasAddedToCart) {
        this.extrasAddedToCart = extrasAddedToCart;
    }

    public int getSale_count() {
        return sale_count;
    }

    public void setSale_count(int sale_count) {
        this.sale_count = sale_count;
    }

    public List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean> getItemsDetails() {
        return itemsDetails;
    }

    public void setItemsDetails(List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean> itemsDetails) {
        this.itemsDetails = itemsDetails;
    }

    public List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean> getWithItemsDetails() {
        return withItemsDetails;
    }

    public void setWithItemsDetails(List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean> withItemsDetails) {
        this.withItemsDetails = withItemsDetails;
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


