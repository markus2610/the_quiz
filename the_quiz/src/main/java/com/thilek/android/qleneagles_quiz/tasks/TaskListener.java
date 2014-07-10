package com.thilek.android.qleneagles_quiz.tasks;

public interface TaskListener {

	public void onTaskStarted(int taskID, Object object);

	public void onTaskProgressUpdated(int taskID, Object object);

	public void onTaskCompleted(int taskID, Object object);

	public void onTaskCanceled(int taskID, Object object);

    public void onTaskError(int taskID, Object object);
}
