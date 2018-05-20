package com.noscale.bos.utils.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by kurniawanrizzki on 20/05/18.
 */

public class ScheduleReceiver extends BroadcastReceiver {
    public static final String ACTION = "com.noscale.bos.receivers.schedule";
    public static final int REQUEST_ID = 22;
    public static final String SCHEDULE_TRIGGERED_SUBJECT = "bos.subject.schedule_triggered";
    public static final String SCHEDULE_MESSAGES_TAG = "bos.schedule.messages";
    @Override
    public void onReceive(Context context, Intent intent) {
        String subject = intent.getStringExtra(Intent.EXTRA_SUBJECT);

        if (null != subject && subject.equals(SCHEDULE_TRIGGERED_SUBJECT)) {

        }

    }
}
