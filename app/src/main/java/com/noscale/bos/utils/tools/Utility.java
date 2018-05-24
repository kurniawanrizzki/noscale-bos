package com.noscale.bos.utils.tools;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.noscale.bos.models.HttpMessage;

import java.util.ArrayList;

import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kurniawanrizzki on 21/05/18.
 */

public final class Utility {

    public static final int STORAGE_PERMISSION_REQUEST_ID = 0;

    @TargetApi(Build.VERSION_CODES.M)
    public static ArrayList<String> getDeniedPermissionList (Context context, String[] permissionList) {
        ArrayList<String> deniedPermissionList = new ArrayList<>();

        for(String permission:permissionList){
            if(context.checkSelfPermission(permission) ==  PackageManager.PERMISSION_DENIED){
                deniedPermissionList.add(permission);
            }
        }

        return deniedPermissionList;
    }

    public static Retrofit getClient (String URL) {

        return new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static HttpMessage.Request getRequestBody (RequestBody body) {

        HttpMessage.Request requestBody = null;
        Buffer buffer = new Buffer();

        if (null != body) {
            try {
                body.writeTo(buffer);
                requestBody = new Gson().fromJson(buffer.readUtf8(), HttpMessage.Request.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return requestBody;
    }

    public static String getFixedNumber (String src) {
        String fixedNumber = src;
        if (fixedNumber.matches(".*\\WhatsApp\\b.*")) {
            fixedNumber = src.substring(0, 11);
        }

        fixedNumber = fixedNumber.replaceAll("\\D+","");
        return fixedNumber;
    }

}
