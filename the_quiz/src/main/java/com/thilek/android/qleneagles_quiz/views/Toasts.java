package com.thilek.android.qleneagles_quiz.views;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.thilek.android.qleneagles_quiz.R;


public class Toasts {

	private static Handler handler;

	public final static void longToast(final Context context,
			final String message) {

		handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {

			public void run() {
				Toast.makeText(context.getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
			}
		});
	}

	public final static void shortToast(final Context context,
			final String message) {
		handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {

			public void run() {
				Toast.makeText(context.getApplicationContext(), message,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	public final static void shortToast(final Context context,
			final int stringID) {
		handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {

			public void run() {
				Toast.makeText(context.getApplicationContext(),
						context.getString(stringID), Toast.LENGTH_SHORT).show();
			}
		});
	}

	public final static void longToast(final Context context, final int stringID) {
		handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {

			public void run() {
				Toast.makeText(context.getApplicationContext(),
						context.getString(stringID), Toast.LENGTH_LONG).show();
			}
		});
	}

	public final static void customLongToast(final Activity context,
			final int stringID) {
		handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {

			public void run() {
				LayoutInflater inflater = context.getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast_layout,
						(ViewGroup) context
								.findViewById(R.id.toast_layout_root));

				TextView text = (TextView) layout.findViewById(R.id.text);
				text.setText(context.getString(stringID));

				Toast toast = new Toast(context);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();
			}
		});
	}
	
	public final static void customShortToast(final Activity context,
			final int stringID) {
		handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {

			public void run() {
				LayoutInflater inflater = context.getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast_layout,
						(ViewGroup) context
								.findViewById(R.id.toast_layout_root));

				TextView text = (TextView) layout.findViewById(R.id.text);
				text.setText(context.getString(stringID));

				Toast toast = new Toast(context);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.setDuration(Toast.LENGTH_SHORT);
				toast.setView(layout);
				toast.show();
			}
		});
	}

    public final static void customLongToast(final Activity context,
                                             final String string) {
        handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            public void run() {
                LayoutInflater inflater = context.getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast_layout,
                        (ViewGroup) context
                                .findViewById(R.id.toast_layout_root));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText(string);

                Toast toast = new Toast(context);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }
        });
    }

    public final static void customShortToast(final Activity context,
                                              final String string) {
        handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            public void run() {
                LayoutInflater inflater = context.getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast_layout,
                        (ViewGroup) context
                                .findViewById(R.id.toast_layout_root));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText(string);

                Toast toast = new Toast(context);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            }
        });
    }

}