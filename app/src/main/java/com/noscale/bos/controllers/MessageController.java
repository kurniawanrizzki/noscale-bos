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
import com.noscale.bos.R;
import com.noscale.bos.models.HttpMessage;
import com.noscale.bos.models.RequestMessage;
import com.noscale.bos.utils.AppGlobal;
import com.noscale.bos.utils.loggers.BosLogger;
import com.noscale.bos.utils.preferences.Configuration;
import com.noscale.bos.utils.receivers.ScheduleReceiver;
import com.noscale.bos.utils.tools.APIInterface;
import com.noscale.bos.utils.tools.Utility;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kurniawanrizzki on 20/05/18.
 */

public class MessageController extends BaseController {

    public static final String TAG = "MessageController";
    private static final String PREFIX = BosLogger.getLogPrefix(MessageController.class);

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
        RequestMessage message;
        String src = sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE).toString().replaceAll("\\D+","");
        CharSequence[] textLines = sbn.getNotification().extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);

        if (null != textLines) {
            CharSequence content = textLines[textLines.length - 1];
            message = buildRequestMessage(Utility.getFixedNumber(src), content.toString());
            if (null != message) {
                database.getRequestMessageTable().insert(message);
                LOGGER.info(PREFIX, "insert to db : "+message.toString());
            }
            return;
        }

//        Pattern pattern = Pattern.compile(RequestMessage.MESSAGE_FILTERED_REGEX);
//        CharSequence content = sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT);
//
//        if (null == pattern.matcher(content)) {
//            message = buildRequestMessage(src, content.toString());
//            if (null != message) {
//                database.getRequestMessageTable().insert(message);
//                LOGGER.info(PREFIX, "insert into db : "+message.toString());
//            }
//        }
    }

    private RequestMessage buildRequestMessage (String src, String content) {
        RequestMessage message = null;
        long currentTime = Calendar.getInstance(Locale.getDefault()).getTimeInMillis();
        boolean isAllowToForwarded = database.getRequestMessageTable()
                .isRequestMessageAllowToForwarded(src, content.toString());

        if (isAllowToForwarded) {
            message = new RequestMessage();
            message.setSrc(src);
            message.setContent(content.toString());
            message.setReceivedTime(currentTime);
        }
        return message;
    }

    public void sendMessage (long requestId, String dest, String response) {

        database.getRequestMessageTable().updateRequestMessage(requestId);
        broadcast(dest, response);

        LOGGER.info(PREFIX, "broadcasted and update database with parameters : "+requestId+" "+dest+" "+response);

    }

    public void sendMessage (int lastAttempt, long id, String dest) {

        int status = RequestMessage.Status.WAITING;
        if (lastAttempt > AppGlobal.MAX_ALLOWED_ATTEMPT) {
            status = RequestMessage.Status.UNDELIVERED;
            broadcast(dest, context.getString(R.string.app_failed_to_forwarded_label));
            LOGGER.info(PREFIX, "broadcasted because reached max allowed attempt");
        }

        database.getRequestMessageTable().updateRequestMessage(lastAttempt,status, id);
        LOGGER.info(PREFIX, "update database with parameters : "+lastAttempt+" "+dest+" "+dest);

    }

    public void broadcast (String dest, String response) {

        String URL = "https://eu5.chat-api.com";
        APIInterface anInterface = Utility.getClient(URL).create(APIInterface.class);

        Call<HttpMessage.WhatsappResponse> responseSending = anInterface.sendingResponse(new HttpMessage().new WhatsappRequest(
                dest,
                response
        ));

        responseSending.enqueue(new Callback<HttpMessage.WhatsappResponse>() {
            @Override
            public void onResponse(Call<HttpMessage.WhatsappResponse> call, Response<HttpMessage.WhatsappResponse> response) {
                HttpMessage.WhatsappResponse responseBody = response.body();
                LOGGER.info(PREFIX, "onResponse : "+responseBody.toString());
            }

            @Override
            public void onFailure(Call<HttpMessage.WhatsappResponse> call, Throwable t) {
                final HttpMessage.Request requestBody = Utility.getRequestBody(call.request().body());
                LOGGER.info(PREFIX, "onFailure : "+t.getMessage()+" "+t.getCause()+" for "+requestBody.toString());
            }
        });
    }
}
