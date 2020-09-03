package com.icashier.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SalesDataModel implements Serializable {


    /**
     * code : 200
     * message : Success.
     * result : {"topItems":[{"name":"Virgin Mojito","sale_count":1,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/70003e9cdc537b7c97aabcf15ac685b6.png"},{"name":"Ggh","sale_count":0,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/f13ab296b012494620e9a9eb78b9134d.png"},{"name":"Maggi","sale_count":0,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/d230a67919c32224f68a62d094b72e59.png"}],"payments":{"Online":0,"Credit Card":0,"Cash":14},"sales":{"sales":2444.17},"salesReport":{"maxSale":1834,"result":[{"sales":610,"dateStr":"2019-02-19T04:59:46.000Z","dateFormatted":"2019-02-19"},{"sales":1834,"dateStr":"2019-02-21T12:23:50.000Z","dateFormatted":"2019-02-21"}]},"visitors":{"visitors":1},"insight":{"likes":4,"orders":14,"coupons":3}}
     */

    private int code;
    private String message;
    private ResultBeanX result;

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

    public ResultBeanX getResult() {
        return result;
    }

    public void setResult(ResultBeanX result) {
        this.result = result;
    }

    public static class ResultBeanX implements Serializable {
        /**
         * topItems : [{"name":"Virgin Mojito","sale_count":1,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/70003e9cdc537b7c97aabcf15ac685b6.png"},{"name":"Ggh","sale_count":0,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/f13ab296b012494620e9a9eb78b9134d.png"},{"name":"Maggi","sale_count":0,"image":"http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/d230a67919c32224f68a62d094b72e59.png"}]
         * payments : {"Online":0,"Credit Card":0,"Cash":14}
         * sales : {"sales":2444.17}
         * salesReport : {"maxSale":1834,"result":[{"sales":610,"dateStr":"2019-02-19T04:59:46.000Z","dateFormatted":"2019-02-19"},{"sales":1834,"dateStr":"2019-02-21T12:23:50.000Z","dateFormatted":"2019-02-21"}]}
         * visitors : {"visitors":1}
         * insight : {"likes":4,"orders":14,"coupons":3}
         */

        private PaymentsBean payments;
        private SalesBean sales;
        private SalesReportBean salesReport;
        private VisitorsBean visitors;
        private InsightBean insight;
        private List<TopItemsBean> topItems;

        public PaymentsBean getPayments() {
            return payments;
        }

        public void setPayments(PaymentsBean payments) {
            this.payments = payments;
        }

        public SalesBean getSales() {
            return sales;
        }

        public void setSales(SalesBean sales) {
            this.sales = sales;
        }

        public SalesReportBean getSalesReport() {
            return salesReport;
        }

        public void setSalesReport(SalesReportBean salesReport) {
            this.salesReport = salesReport;
        }

        public VisitorsBean getVisitors() {
            return visitors;
        }

        public void setVisitors(VisitorsBean visitors) {
            this.visitors = visitors;
        }

        public InsightBean getInsight() {
            return insight;
        }

        public void setInsight(InsightBean insight) {
            this.insight = insight;
        }

        public List<TopItemsBean> getTopItems() {
            return topItems;
        }

        public void setTopItems(List<TopItemsBean> topItems) {
            this.topItems = topItems;
        }

        public static class PaymentsBean implements Serializable {
            /**
             * Online : 0
             * Credit Card : 0
             * Cash : 14
             */

            private int online;
            private int Credit_Card;
            private int Cash;

            public int getOnline() {
                return online;
            }

            public void setOnline(int Online) {
                this.online = Online;
            }

            public int get_Credit_Card() {
                return Credit_Card;
            }

            public void set_Credit_Card(int Credit_Card) {
                this.Credit_Card = Credit_Card;
            }

            public int getCash() {
                return Cash;
            }

            public void setCash(int Cash) {
                this.Cash = Cash;
            }
        }

        public static class SalesBean implements Serializable {
            /**
             * sales : 2444.17
             */

            private double sales;
            private String percent;
            private String type;

            public double getSales() {
                return sales;
            }

            public void setSales(double sales) {
                this.sales = sales;
            }

            public String getPercent() {
                return percent;
            }

            public void setPercent(String percent) {
                this.percent = percent;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class SalesReportBean implements Serializable {
            /**
             * maxSale : 1834
             * result : [{"sales":610,"dateStr":"2019-02-19T04:59:46.000Z","dateFormatted":"2019-02-19"},{"sales":1834,"dateStr":"2019-02-21T12:23:50.000Z","dateFormatted":"2019-02-21"}]
             */

            private double maxSale;
            private List<ResultBean> result;

            public double getMaxSale() {
                return maxSale;
            }

            public void setMaxSale(double maxSale) {
                this.maxSale = maxSale;
            }

            public List<ResultBean> getResult() {
                return result;
            }

            public void setResult(List<ResultBean> result) {
                this.result = result;
            }

            public static class ResultBean implements Serializable {
                /**
                 * sales : 610
                 * dateStr : 2019-02-19T04:59:46.000Z
                 * dateFormatted : 2019-02-19
                 */

                private double sales;
                private String dateStr;
                private String dateFormatted;

                public double getSales() {
                    return sales;
                }

                public void setSales(double sales) {
                    this.sales = sales;
                }

                public String getDateStr() {
                    return dateStr;
                }

                public void setDateStr(String dateStr) {
                    this.dateStr = dateStr;
                }

                public String getDateFormatted() {
                    return dateFormatted;
                }

                public void setDateFormatted(String dateFormatted) {
                    this.dateFormatted = dateFormatted;
                }
            }
        }

        public static class VisitorsBean implements Serializable {
            /**
             * visitors : 1
             */

            private int visitors;
            private String type;
            private String percent;

            public int getVisitors() {
                return visitors;
            }

            public void setVisitors(int visitors) {
                this.visitors = visitors;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getPercent() {
                return percent;
            }

            public void setPercent(String percent) {
                this.percent = percent;
            }
        }

        public static class InsightBean implements Serializable {
            /**
             * likes : 4
             * orders : 14
             * coupons : 3
             */

            private int likes;
            private int orders;
            private int coupons;

            public int getLikes() {
                return likes;
            }

            public void setLikes(int likes) {
                this.likes = likes;
            }

            public int getOrders() {
                return orders;
            }

            public void setOrders(int orders) {
                this.orders = orders;
            }

            public int getCoupons() {
                return coupons;
            }

            public void setCoupons(int coupons) {
                this.coupons = coupons;
            }
        }

        public static class TopItemsBean implements Serializable {
            /**
             * name : Virgin Mojito
             * sale_count : 1
             * image : http://52.27.53.102/imenu/uploads/users/C614CH39M5CWPK6JIMYCY1QHX/70003e9cdc537b7c97aabcf15ac685b6.png
             */

            private String name;
            private int sale_count;
            private String image;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getSale_count() {
                return sale_count;
            }

            public void setSale_count(int sale_count) {
                this.sale_count = sale_count;
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
