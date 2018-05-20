package com.noscale.bos.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import com.noscale.bos.R;
import com.noscale.bos.utils.AppGlobal;
import com.noscale.bos.utils.tools.AlertBuilder;

/**
 * Created by kurniawanrizzki on 20/05/18.
 */
public class MainController extends BaseController {

    public static final String TAG = "MainController";
    private AlertDialog.Builder notificationDialogBuilder;

    public MainController(Context context) {
        super(context);
    }

    public MainController(Activity activity, String tag) {
        super(activity, tag);
    }

    @Override
    public void initData() {
        notificationDialogBuilder = getNotificationDialogBuilder().build(
                activity.getString(R.string.app_service),
                activity.getString(R.string.app_service_dialog_content),
                R.string.app_yes_button_label,
                R.string.app_no_button_label
        );

        if (!isNotificationServiceAllowed()) {
            notificationDialogBuilder.show();
        }

    }

    private AlertBuilder getNotificationDialogBuilder () {
        AlertBuilder builder = new AlertBuilder(activity) {
            @Override
            protected void onPositiveButtonEvent(DialogInterface dialog, int id) {
                activity.startActivity(new Intent(AppGlobal.ENABLE_NOTIFICATION_LISTENERS_ACTION_SETTING_TAG));
            }

            @Override
            protected void onNegativeButtonEvent(DialogInterface dialog, int id) {

            }
        };
        return builder;
    }

    private boolean isNotificationServiceAllowed () {
        String packageName = activity.getPackageName();
        final String flat = Settings.Secure.getString(activity.getContentResolver(), AppGlobal.ENABLE_NOTIFICATION_LISTENERS_TAG);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name:names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (null != cn) {
                    if (TextUtils.equals(packageName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
