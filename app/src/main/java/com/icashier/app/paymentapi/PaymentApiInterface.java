package com.icashier.app.paymentapi;

import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PaymentApiInterface {

    String URL_BASE = "https://b2btest.stcpay.com.sa/B2B.MerchantWebApi/Merchant/v3/";

    @Headers({
            "X-ClientCode: 61275995135",
            "X-Username: gI8d8HWsHkGa7tbJ$eR",
            "X-Password: J5OD67qbC0BfGdSDP@$$w0rd",
            "Content-Type: application/json"
    })
    @POST("MobilePaymentAuthorize")
    Call<JSONObject> stcPaymentAuthorise(@Body RequestBody body);
}
