package com.thilek.android.qleneagles_quiz.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.thilek.android.qleneagles_quiz.R;


public class LaunchActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launch_layout);


    }

    @Override
    protected void onResume() {
        new InitializeTask().execute();
        super.onResume();

    }

    static class MyHandler extends Handler {

        Activity context;

        public MyHandler(Activity context) {
            // TODO Auto-generated constructor stub
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            // InitResult result = (InitResult) msg.obj;


            context.startActivity(new Intent(context
                    .getApplicationContext(), HomeScreenActivity.class));
            context.finish();

        }
    }

    MyHandler MyHandler = new MyHandler(this);

    private class InitializeTask extends AsyncTask<Void, Void, InitResult> {

        private long DURATION = 2000;

        @Override
        protected void onPostExecute(InitResult result) {
            Message message = Message.obtain();
            message.obj = result;
            long delay = DURATION - result.elapsedTime;
            MyHandler.sendMessageDelayed(message, delay > 0 ? delay : 0);
        }

        @Override
        protected InitResult doInBackground(Void... params) {

            long start = System.currentTimeMillis();

            return new InitResult(System.currentTimeMillis() - start, false);

        }

    }

    private class InitResult {
        public InitResult(Long elapsedTime, boolean databasesPresent) {
            this.elapsedTime = elapsedTime;

        }

        public Long elapsedTime;
    }


}
