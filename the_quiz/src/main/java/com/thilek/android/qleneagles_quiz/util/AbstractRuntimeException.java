package com.thilek.android.qleneagles_quiz.util;

public abstract class AbstractRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 6827345050863463550L;

	public abstract int getErrorCode();

}
