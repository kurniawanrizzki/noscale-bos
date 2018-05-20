package com.noscale.bos.utils.tools;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kurniawanrizzki on 21/05/18.
 */

public final class Utility {

    private static Retrofit retrofit = null;

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

    public static Retrofit getClient () {
        if (null == retrofit) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(APIInterface.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
