package com.noscale.bos.controllers;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.noscale.bos.models.RequestMessage;
import com.noscale.bos.utils.AppGlobal;
import java.util.regex.Pattern;

/**
 * Created by kurniawanrizzki on 20/05/18.
 */

public class MessageController extends BaseController {

    public static final String TAG = "MessageController";

    public MessageController(Context context) {
        super(context);
    }

    public MessageController(Activity activity, String tag) {
        super(activity, tag);
    }

    public boolean isAllowedPackage (String packageName) {
        for (String item: AppGlobal.ALLOWED_FORWARDING_APPLICATION_PACKAGE) {
            if (item.equals(packageName)) {
                return true;
            }
        }
        return false;
    }


    public void store (StatusBarNotification sbn) {
        String src = sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE).toString();

        CharSequence[] textLines = sbn.getNotification().extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);

        if (null != textLines) {
            for (CharSequence content : textLines) {
                Log.d("Notification Broadcast ", src+" "+content.toString());
                return;
            }
        }

        Pattern pattern = Pattern.compile(RequestMessage.MESSAGE_FILTERED_REGEX);
        CharSequence content = sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT);

        if (null != pattern.matcher(content)) {
            Log.d("Notification Broadcast ", src+" "+content.toString());
        }
    }

}
