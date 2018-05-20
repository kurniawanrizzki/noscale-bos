package com.noscale.bos.utils.permissions;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.noscale.bos.utils.Instance;

/**
 * Created by kurniawanrizzki on 21/05/18.
 */
@TargetApi(Build.VERSION_CODES.M)
public class BosPermission extends Instance {

    public BosPermission(Context context, String tag) {
        super(context, tag);
    }

    @Override
    protected void setup() {

    }

    public String[] getDeniedPermissionList (String[] permissionList) {
        String[] deniedPermissionList = new String[permissionList.length];
        int count = 0;
        for (String permission:permissionList) {
            boolean isPermissionDenied = context.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED;
            if (isPermissionDenied) {
                deniedPermissionList[count] = permission;
            }
            count++;
        }
        return deniedPermissionList;
    }

}
