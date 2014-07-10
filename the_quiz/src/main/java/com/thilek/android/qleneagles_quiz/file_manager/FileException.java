package com.thilek.android.qleneagles_quiz.file_manager;


import com.thilek.android.qleneagles_quiz.util.AbstractRuntimeException;

public class FileException extends AbstractRuntimeException {


    public static int EXTERNAL_STORAGE_NOT_MOUNTED_EXCEPTION = 200;

    public static int FILE_NOT_FOUND_EXCEPTION = 201;

    public static int IO_EXCEPTION = 202;

    public static int WRONG_LOCATION_EXCEPTION = 203;

    public static int FILE_OUTSIDE_ASSETS_NOT_FOUND_EXCEPTION = 204;

    private String message;

    private int errorCode;

    public FileException(int errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }


    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return "ErrorCode:" + errorCode + " " + message;
    }

}