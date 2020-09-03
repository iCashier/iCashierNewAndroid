package com.icashier.app.model;

public class CreateCouponResponse {

    /**
     * code : 200
     * message : Success.
     * insertId : 4
     */

    private int code;
    private String message;
    private int insertId;

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

    public int getInsertId() {
        return insertId;
    }

    public void setInsertId(int insertId) {
        this.insertId = insertId;
    }
}
