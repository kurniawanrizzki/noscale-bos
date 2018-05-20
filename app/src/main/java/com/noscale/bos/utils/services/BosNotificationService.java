package com.noscale.bos.utils.services;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.noscale.bos.controllers.MainController;
import com.noscale.bos.controllers.MessageController;
import com.noscale.bos.utils.AppGlobal;
import com.noscale.bos.utils.receivers.ScheduleReceiver;

/**
 * Created by kurniawanrizzki on 20/05/18.
*/
@SuppressLint("OverrideAbstract")
public class BosNotificationService extends NotificationListenerService {
    ScheduleReceiver receiver;

    @Override
    public void onCreate() {
        receiver = new ScheduleReceiver();
        registerReceiver(receiver, new IntentFilter(ScheduleReceiver.ACTION));
        try {
            if (!AppGlobal.isScheduleRunning) {
                Bundle bundle = new Bundle();
                Intent scheduleIntent = new Intent(ScheduleReceiver.ACTION);
                bundle.putString(Intent.EXTRA_SUBJECT, ScheduleReceiver.NEW_SCHEDULE_SUBJECT);
                scheduleIntent.putExtra(ScheduleReceiver.NEW_SCHEDULE_BUNDLE, bundle);
                sendBroadcast(scheduleIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != receiver) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        MessageController controller = (MessageController) AppGlobal.controllerMap.get(MessageController.TAG);

        if (null == controller) {
            controller = (MessageController) new MessageController(this).setTag(MainController.TAG);
        }

        if (!controller.isAllowedPackage(sbn.getPackageName())) {
            return;
        }

        controller.store(sbn);

    }
}
