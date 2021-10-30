package com.mtvn.enums.error;

import lombok.Getter;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

public enum ErrorCode {
    INVALID_JSON									(100001, "INVALID_JSON"),
    //LMS
    LMS_UNIQUE_RECORD_NOT_FOUND						(100002, "LMS_UNIQUE_RECORD_NOT_FOUND"),
    LMS_INVALID_WITHDRAWAL_AMOUNT					(100003, "LMS_INVALID_WITHDRAWAL_AMOUNT");

    @Getter
    private int code;
    private String message;

    private Locale locale =  new Locale("vi_VN");
    private ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle", locale);

    public String getMessage() {
        String msg =  this.messages.getString(this.toString());

        try {
            return new String(msg.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //do nothing
        }
        return msg;
    }


    private ErrorCode(int code, String message) {
        this.message = message;
        this.code = code;
    }

}
