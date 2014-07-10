package com.thilek.android.qleneagles_quiz.util;


public class NetworkException extends AbstractRuntimeException {

	private static final long serialVersionUID = 1L;
	
	public static final int IO_EXCEPTION = 150;
	
	public static final int WRONG_HTTP_STATUS_CODE = 151;	
	
	public static final int URL_EXCEPTION = 152;	
	
	public static final int HTTP_SC_BAD_REQUEST = 100;

	public static final int HTTP_SC_UNAUTHORIZED = 101;

	public static final int HTTP_SC_FORBIDDEN = 103;
	
	public static final int HTTP_SC_NOT_FOUND= 104;
	
	public static final String WRONG_CREDENTIAL ="access_denied";
	
	private String message;
	
	private int errorCode;
	
	public NetworkException(int errorCode, String message) {
		this.message = message;
		this.errorCode = errorCode;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public int getErrorCode() {
		return errorCode;
	}

}
