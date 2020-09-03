package com.icashier.app.model;

import java.io.Serializable;
import java.util.List;

public class PredefinedCategoriesResponse implements Serializable{
    /**
     * code : 200
     * message : Success.
     * result : [{"id":1,"english_name":"Sandwiches","arabic_name":"السندويشات"},{"id":2,"english_name":"Juice Bars","arabic_name":"العصيرات"},{"id":3,"english_name":"Breakfast & Brunch","arabic_name":"الإفطار والغداء"},{"id":4,"english_name":"Chinese","arabic_name":"مأكولات صينية"},{"id":5,"english_name":"American","arabic_name":"مأكولات أمريكية"},{"id":6,"english_name":"Mexican","arabic_name":"مأكولات مكسيكية"},{"id":7,"english_name":"Japanese","arabic_name":"مأكولات يابانية"},{"id":8,"english_name":"Coffee & Tea","arabic_name":"شاي وقهوة"},{"id":9,"english_name":"Pizza","arabic_name":"بيتزا"},{"id":10,"english_name":"Shawrma","arabic_name":"شاورما"},{"id":11,"english_name":"Cafes","arabic_name":"كوفي شوب"},{"id":12,"english_name":"Local Food","arabic_name":"مأكولات شعبية"},{"id":13,"english_name":"Italian","arabic_name":"مأكولات إيطالية"},{"id":14,"english_name":"Seafood","arabic_name":"مأكولات بحرية"},{"id":15,"english_name":"Sushi","arabic_name":"سوشي"},{"id":16,"english_name":"Sashimi","arabic_name":"ساشيمي"},{"id":17,"english_name":"Salad","arabic_name":"سلطات"},{"id":18,"english_name":"Delis","arabic_name":"الوجبات الجاهزة"},{"id":19,"english_name":"Burgers","arabic_name":"برجر"},{"id":20,"english_name":"Fast Food","arabic_name":"وجبات سريعة"},{"id":21,"english_name":"Vietnamese","arabic_name":"مأكولات فيتنامية"},{"id":22,"english_name":"Thai","arabic_name":"مأكولات تايلاندية"},{"id":23,"english_name":"Asian Fusion","arabic_name":"مأكولات آسيوية"},{"id":24,"english_name":"Noodles","arabic_name":"نودولز"},{"id":25,"english_name":"Bakeries","arabic_name":"المخابز"},{"id":26,"english_name":"Indian","arabic_name":"مأكولات هندية"},{"id":27,"english_name":"Desserts","arabic_name":"الحلويات"},{"id":28,"english_name":"Korean","arabic_name":"ماكولات كورية"},{"id":29,"english_name":"Barbeque","arabic_name":"مشاوي"},{"id":30,"english_name":"Dim Sum","arabic_name":"ديم سوم"},{"id":31,"english_name":"French","arabic_name":"مأكولات فرنسية"},{"id":32,"english_name":"Vegetarian","arabic_name":"نباتي"},{"id":33,"english_name":"Chicken Wings","arabic_name":"أجنحة دجاج"},{"id":34,"english_name":"Ramen","arabic_name":"رامن"},{"id":35,"english_name":"Juice Bars & Smoothies","arabic_name":"محلات العصائر"},{"id":36,"english_name":"Soup","arabic_name":"الشوربة"},{"id":37,"english_name":"Hot Dogs","arabic_name":"نقانق"},{"id":38,"english_name":"Tapas/Small Plates","arabic_name":"وجبات صغيرة"},{"id":39,"english_name":"Diners","arabic_name":"مطعم محلي"},{"id":40,"english_name":"Grocery","arabic_name":"بقالة"},{"id":41,"english_name":"Middle Eastern","arabic_name":"مأكولات عربية"},{"id":42,"english_name":"Gluten-Free","arabic_name":"خالي من الجلوتين"},{"id":43,"english_name":"Specialty Food","arabic_name":"تخصص غذائي"},{"id":44,"english_name":"Bagels","arabic_name":"مخبوزات بيغل"},{"id":45,"english_name":"Hot Pot","arabic_name":"فخارية"},{"id":46,"english_name":"Steaks","arabic_name":"ستيك و لحم"},{"id":47,"english_name":"Comfort Food","arabic_name":"وجبات راحة"},{"id":48,"english_name":"Greek","arabic_name":"مأكولات إغريقية"},{"id":49,"english_name":"Pakistani","arabic_name":"مأكولات باكستانية"},{"id":50,"english_name":"Spanish","arabic_name":"مأكولات إسبانية"},{"id":51,"english_name":"Tacos","arabic_name":"التاكو المكسيكي"},{"id":52,"english_name":"Cheesesteaks","arabic_name":"ستيك تشيز"},{"id":53,"english_name":"Turkish","arabic_name":"مأكولات تركية"},{"id":54,"english_name":"German","arabic_name":"مأكولات ألمانية"},{"id":55,"english_name":"Ethiopian","arabic_name":"مأكولات أثيوبي"},{"id":56,"english_name":"Taiwanese","arabic_name":"مأكولات تايوانية"},{"id":57,"english_name":"Brazilian","arabic_name":"مأكولات برازيلية"},{"id":58,"english_name":"Buffets","arabic_name":"البوفيهات"},{"id":59,"english_name":"Waffles","arabic_name":"وافل و بانكيك"},{"id":60,"english_name":"Fol Joint","arabic_name":"فوال"},{"id":61,"english_name":"Shiha Cafe","arabic_name":"مقهى شيشة"}]
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
         * english_name : Sandwiches
         * arabic_name : السندويشات
         */

        private int id;
        private String english_name;
        private String arabic_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEnglish_name() {
            return english_name;
        }

        public void setEnglish_name(String english_name) {
            this.english_name = english_name;
        }

        public String getArabic_name() {
            return arabic_name;
        }

        public void setArabic_name(String arabic_name) {
            this.arabic_name = arabic_name;
        }
    }
}
