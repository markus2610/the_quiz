package com.thilek.android.qleneagles_quiz.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.text.Spanned;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class ConnectivityCheck {

    private final static String TAG = "ConnectivityCheck";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static boolean isHostAvailable(String host, int timeOut) {

        try {
            return InetAddress.getByName(host).isReachable(timeOut);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            Logs.e(TAG, e.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Logs.e(TAG, e.toString());
        }
        return false;
    }

    public static String getipAddress() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        Spanned ip = Html.fromHtml(inetAddress.getHostAddress().toString());
                        Logs.e("IP : " + ip);
                        return ip.toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Logs.e("Socket exception in GetIP Address of Utilities", ex.toString());
        }
        return null;
    }


}