package com.thilek.android.qleneagles_quiz.util;

public interface IProgressReceiver {
	
	void setProgress(String message, int progress, boolean isCancelEnabled);
	
	void setProgress(String message, int progress, boolean isCancelEnabled, String progressText);
	
	void setMax(long max);
	
	long getMax();
	
	boolean isTaskToBeCanceled();
	

	


}
