package com.noscale.bos.controllers;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.service.notification.StatusBarNotification;
import com.noscale.bos.models.RequestMessage;
import com.noscale.bos.models.ResponseMessage;
import com.noscale.bos.utils.AppGlobal;
import com.noscale.bos.utils.Instance;
import com.noscale.bos.utils.databases.BosDatabase;
import com.noscale.bos.utils.preferences.Configuration;
import com.noscale.bos.utils.receivers.ScheduleReceiver;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by kurniawanrizzki on 20/05/18.
 */

public class MessageController extends BaseController {

    public static final String TAG = "MessageController";
    private ResponseListener responseListener;

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

    public void schedule (List<RequestMessage> requestMessages) {
        long nextScheduleRunning = Calendar.getInstance(Locale.getDefault()).getTimeInMillis() + ((long) Configuration.configurationMap.get(Configuration.USED_INTERVAL_MESSAGE_FORWARDED));
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Bundle bundle = new Bundle();
        bundle.putString(Intent.EXTRA_SUBJECT, ScheduleReceiver.NEW_SCHEDULE_SUBJECT);
        bundle.putParcelableArrayList(ScheduleReceiver.MESSAGES_SCHEDULE_DATA, (ArrayList<? extends Parcelable>) requestMessages);

        Intent scheduleIntent = new Intent(ScheduleReceiver.ACTION);
        scheduleIntent.putExtra(ScheduleReceiver.NEW_SCHEDULE_BUNDLE, bundle);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, ScheduleReceiver.REQUEST_ID, scheduleIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarm.setExact(AlarmManager.RTC_WAKEUP, nextScheduleRunning, pIntent);
        } else {
            alarm.set(AlarmManager.RTC_WAKEUP, nextScheduleRunning, pIntent);
        }
        AppGlobal.isScheduleRunning = true;
    }

    public void store (StatusBarNotification sbn) {
        try {
            RequestMessage message;
            BosDatabase database = (BosDatabase) getInstanceManager().get(Instance.DB_INSTANCE);
            String src = sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE).toString();

            CharSequence[] textLines = sbn.getNotification().extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);

            if (null != textLines) {
                for (CharSequence content : textLines) {
                    message = buildRequestMessage(src, content.toString());
                    if (null != message) {
                        database.getRequestMessageTable().insert(message);
                    }
                }
                return;
            }

            Pattern pattern = Pattern.compile(RequestMessage.MESSAGE_FILTERED_REGEX);
            CharSequence content = sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT);

            if (null == pattern.matcher(content)) {
                message = buildRequestMessage(src, content.toString());
                if (null != message) {
                    database.getRequestMessageTable().insert(message);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private RequestMessage buildRequestMessage (String src, String content) {
        RequestMessage message = null;
        try {

            long currentTime = Calendar.getInstance(Locale.getDefault()).getTimeInMillis();
            BosDatabase database = (BosDatabase) getInstanceManager().get(Instance.DB_INSTANCE);
            boolean isAllowtoForwaded = database.getRequestMessageTable().isRequestMessageAllowToForwarded(src, content.toString());

            if (isAllowtoForwaded) {
                message = new RequestMessage();
                message.setSrc(src);
                message.setContent(content.toString());
                message.setReceivedTime(currentTime);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return message;
    }

    public void sendMessage (RequestMessage req, ResponseMessage res, Enum status) {
        //TODO sending message through whatsapp; broadcast ();
        //TODO update status and forwarded message in request Message table;
    }

    public void sendMessage (RequestMessage req, Enum status) {
        //TODO sending message through whatsapp; broadcast () if it's already reach max attempt;
        //TODO update last attempt in request message table;
    }

    public void broadcast (String src, String content) {
        for (String item:AppGlobal.ALLOWED_FORWARDING_APPLICATION_PACKAGE) {
            if (item.equals("com.whatsapp")) {
                //TODO sending message through allowed package in background;
            }
        }
    }

    public void setResponseListener (ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public interface ResponseListener {
        void onResponse ();
    }

}
