package com.icashier.app.constants;

/**
 * Created by love on 8/6/18.
 */

public class ServerConstants {

    //production url
    public static String BASE_URl_SOCKET = ":6262/";
    public static String BASE_URL = "https://www.icashierapp.com" + BASE_URl_SOCKET;
//    public static String BASE_URL = "https://devnode.devtechnosys.tech:17335/";
//    public static String BASE_URL = "http://18.196.177.79:6262/";
    // public static String BASE_URL = "http://52.57.61.227:6262/";


    //development url
    // public static String BASE_URL = "http://52.27.53.102:3434/";
    public static String SOCKET_URL = "http://52.27.53.102:3434/";
    public static String IMAGE_BASE_URL = "";
    public static String IMENU_BASE_URL = "http://52.27.53.102/imenu/uploads/";
    public static String SIGN_UP = "api/icuser/signup";
    public static String SIGN_IN = "api/icuser/login";
    public static String VERIFY_OTP = "api/icuser/verify/otp";
    public static String RESEND_OTP = "api/icuser/resend/otp";
    public static String RESET_PASSWORD = "api/icuser/resetpassword";
    public static String FORGOT_PASSWORD = "api/icuser/forgotpassword";
    public static String GET_PLANS = "api/icuser/get/plans";
    public static String SAVE_PLANS = "api/icuser/save/plan";
    public static String SAVE_MERCHANT = "api/icuser/save/merchant";
    public static String GET_CATEGORY_ICONS = "api/restaurant/categories/get/icons";
    public static String ADD_CATEGORY = "api/restaurant/category/add";
    public static String GET_CATEGORY_LIST = "api/restaurant/categories/get/list";
    public static String DELETE_IMAGE = "api/icuser/delete/merchant/image";
    public static String UPDATE_CATEGORY = "api/restaurant/category/update";
    public static String DELETE_CATEGORY = "api/restaurant/category/delete";
    public static String ADD_EXTRAS = "api/restaurant/extras/add";
    public static String GET_EXTRAS_LIST = "api/restaurant/extras/list";
    public static String DELETE_EXTRAS = "api/restaurant/extras/delete";
    public static String EDIT_EXTRAS = "api/restaurant/extras/edit";
    public static String GET_ITEM_SIZE = "api/restaurant/item/sizes";
    public static String SAVE_ITEM = "api/restaurant/item/add";
    public static String GET_ITEM_LIST = "api/restaurant/item/list";
    public static String DELETE_ITEM = "api/restaurant/item/delete";
    public static String EDIT_ITEM = "api/restaurant/item/edit";
    public static String ADD_MEAL = "api/restaurant/meal/add";
    public static String EDIT_MEAL = "api/restaurant/meal/edit";
    public static String DELETE_MEAL = "api/restaurant/meal/delete";
    public static String GET_MEAL_LIST = "api/restaurant/meal/list";
    public static String ADD_TABLE = "api/restaurant/table/add";
    public static String GET_TABLE_LIST = "api/restaurant/table/list";
    public static String DELETE_TABLE = "api/restaurant/table/delete";
    public static String EDIT_TABLE = "api/restaurant/table/edit";
    public static String CREATE_QR = "api/restaurant/qr/create";
    public static String GET_QR_LIST = "api/restaurant/qr/list";
    public static String EDIT_QR = "api/restaurant/qr/edit";
    public static String DELETE_QR = "api/restaurant/qr/delete";
    public static String GET_MENU = "api/icuser/get/menu";
    public static String CHANGE_STOCK_ITEM = "api/icuser/mark/item";
    public static String CHANGE_STOCK_MEAL = "api/icuser/mark/meal";
    public static String CHANGE_STOCK_DEAL = "api/icuser/mark/deal";

    public static String CHECKOUT = "api/icuser/checkout";
    public static String GET_ORDER_LIST = "api/icuser/order/list";

    public static String CREATE_COUPON = "api/icuser/coupon/create";
    public static String COUPON_LIST = "api/icuser/coupon/detail";
    public static String SAVE_GENERAL_SETTING = "api/icuser/setting";
    public static String SIGNUP_CASHIER = "api/icuser/signup/icashier";
    public static String GET_CASHIER_LIST = "api/icuser/icashier/list";
    public static String DELETE_CASHIER = "api/icuser/icashier/delete";
    public static String DELETE_COUPON = "api/icuser/coupon/delete";
    public static String LOGIN_CASHIER = "api/icuser/icashier/login";
    public static String TAX_API = "api/icuser/tax";
    public static String DELETE_TAX = "api/icuser/tax/delete";
    public static String CHANGE_ORDER_STATUS = "api/icuser/order/status";
    public static String SAVE_TIMINGS = "api/icuser/save/timings";
    public static String TIMINGS = "api/icuser/timings";

    public static String ADD_INVOICE = "api/icuser/invoice";
    public static String DELETE_INVOICE = "api/icuser/invoice/delete";
    public static String CALCULATE_TAX = "api/icuser/taxAmount";

    public static String DEALS = "api/restaurant/deals";
    public static String DELETE_DEAL = "api/restaurant/delete/deals";
    public static String GET_ITEM_COUNT = "api/restaurant/get/itemCount";
    public static String GET_PREDEFINED_CATEGORIES = "api/restaurant/predefined/categories";
    public static String MAKE_DEAL_SPECIAL = "api/restaurant/makeDealSpecial";
    public static String SALES_DASHBOARD = "api/restaurant/get/dashboard";
    public static String PAYMENT_CONFIRMATION = "api/icuser/orderAck";
    public static String ACK_WAITER_CALL = "api/icuser/ackwaitercall";
    public static String SIGNOUT = "api/icuser/merchantSignout";
    public static String CHANGE_ONLINE_STATUS = "api/icuser/onlineStatus";
    public static String GET_ONLINE_STATUS = "api/icuser/getOnlineStatus";
    public static String CREATE_NEW_OUTLET = "api/icuser/createnewoutlet";
    public static String UPDATE_DEVICE_TOKEN = "api/icuser/updateDeviceToken";
    public static String CHANGE_NOTIFICATION_SETTING = "api/icuser/updateNotificationStatus";
    public static String CHANGE_LANG_API = "api/user/changeStatusLang";
    public static String GET_ALLEGIES = "api/icuser/getallergies";
    public static String STC_TRANSACTION = "api/icuser/stcTransactions";
    //LIVE STC
    public static String STC_PAYINQUARY = "https://b2b.stcpay.com.sa/B2B.Merchant.WebApi/Merchant/v3/PaymentInquiry";
    public static String STC_PAYAUTORISE = "https://b2b.stcpay.com.sa/B2B.Merchant.WebApi/Merchant/v3/MobilePaymentAuthorize";
    public static String STC_CLIENT_CODE = "72289245413";
    public static String STC_USER_NAME = "AZOUewcBCrLl70ks";
    public static String STC_USER_PASS = "9QWsRHwF0ypLefiO";
    public static String GENERATE_NEW_PDF = "api/icuser/getPdf";

    // DEMO_STC rest of related content on imenu code
    // public static String STC_PAYINQUARY="https://b2btest.stcpay.com.sa/B2B.MerchantWebApi/Merchant/v3/PaymentInquiry";
    // public static String STC_PAYAUTORISE="https://b2btest.stcpay.com.sa/B2B.MerchantWebApi/Merchant/v3/MobilePaymentAuthorize";


}
