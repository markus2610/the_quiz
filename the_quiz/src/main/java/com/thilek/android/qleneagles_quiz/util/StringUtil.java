package com.thilek.android.qleneagles_quiz.util;

import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.*;

public class StringUtil {

	public static String checkForNull(String string) {
		if (string == null || string.length() < 1) {
			return "";
		}

		// string = checkForBR(string);
		return string;
	}

	public static boolean isStringNull(String string) {
		if (string == null || string.equals("null") ||string.length() < 1) {
			return true;
		}
		return false;
	}

	public final static String handleLineBreakAndNull(String old) {

		if (old == null) {
			return "";
		} else {
			Spanned spannedString = Html.fromHtml(old);
			return spannedString.toString();
		}
	}
	
	public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);
	}

	public static String padLeft(String s, int n) {
	    return String.format("%1$" + n + "s", s);
	}

	public static String checkForBR(String string) {
		string = string.replace("<BR>", "");
		string = string.replace("<br>", "");
		string = string.replace("\"\"", "");
		if (string.length() <= 2 && string.contains(",")) {
			string = "";
		}
		return string;
	}

	public static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder stringBuilder = new StringBuilder();

		String line = null;

		try {

			while ((line = reader.readLine()) != null) {

				stringBuilder.append(line + "\n");
			}
		} catch (IOException e) {

			e.printStackTrace();
		} finally {

			try {

				is.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		return stringBuilder.toString();
	}

	public static String ConvertDoubleToString(double aDouble) {

		String aString = Double.toString(aDouble);
		return aString;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem instanceof ViewGroup)
				listItem.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public static String formatPhone(String phone) {
		String phoneFormatted;

		phoneFormatted = phone.replaceAll("[^\\d]", "");

		if (!("").equals(phoneFormatted)) {
			if (phoneFormatted.startsWith("0049"))
				phoneFormatted = phoneFormatted.substring(4);
			if (phoneFormatted.startsWith("+49"))
				phoneFormatted = phoneFormatted.substring(3);
			if (phoneFormatted.startsWith("0"))
				phoneFormatted = phoneFormatted.substring(1);

			phoneFormatted = "+49" + phoneFormatted;
		}

		return phoneFormatted;
	}

	public final static String stringFormatter(String old) {

		if (old == null) {
			return "";
		} else {
			Spanned spannedString = Html.fromHtml(old);
			return spannedString.toString();
		}
	}

	public static String convertDoubleToString(double aDouble) {

		String aString = Double.toString(aDouble);
		return aString;
	}

	public static String fileToString(String file) {
		String result = null;
		DataInputStream in = null;

		try {
			File f = new File(file);
			byte[] buffer = new byte[(int) f.length()];
			in = new DataInputStream(new FileInputStream(f));
			in.readFully(buffer);
			result = new String(buffer);
		} catch (IOException e) {
			throw new RuntimeException("IO problem in fileToString", e);
		} finally {
			try {
				in.close();
			} catch (IOException e) { /* ignore it */
			}
		}
		return result;
	}

	public static String fileToString(File f) {
		String result = null;
		DataInputStream in = null;

		try {
			byte[] buffer = new byte[(int) f.length()];
			in = new DataInputStream(new FileInputStream(f));
			in.readFully(buffer);
			result = new String(buffer);
		} catch (IOException e) {
			throw new RuntimeException("IO problem in fileToString", e);
		} finally {
			try {
				in.close();
			} catch (IOException e) { /* ignore it */
			}
		}
		return result;
	}

	public static String[] convertArrayToStringArray(Object[] objects) {
		if (objects == null)
			return new String[0];
		String[] result = new String[objects.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = objects[i].toString();
		}
		return result;
	}
}
