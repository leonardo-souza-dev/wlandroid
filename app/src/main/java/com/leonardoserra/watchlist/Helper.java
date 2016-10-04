package com.leonardoserra.watchlist;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

public class Helper {

    private static Context context;
    private String basePosterUrl;

    public Helper(Context pContext){
        context = pContext;
    }

    public String obterUrlBase(){
        String urlBase;

        String testarApiLocal = context.getResources().getString(R.string.testarApiLocal);

        if (testarApiLocal.equals("true")) {
            if (isEmulator()) {
                urlBase = context.getResources().getString(R.string.baseUrlLocalEmulator);
            }else{
                urlBase = context.getResources().getString(R.string.baseUrlLocalDevice);
            }
        } else {
            urlBase = context.getResources().getString(R.string.baseUrlNuvem);
        }

        return urlBase;
    }

    public String obterUrlBaseApi(){
        basePosterUrl = obterUrlBase() + "/api";
        return basePosterUrl;
    }

    public boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    public void enviarLogException(Exception e){
        ApiHelper apiHelper = new ApiHelper();
        apiHelper.enviarLog(TextUtils.join("\r\n", e.getStackTrace()));
    }
}
