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
import com.noscale.bos.utils.Instance;
import com.noscale.bos.utils.loggers.BosLogger;
import com.noscale.bos.utils.managers.InstanceManager;
import com.noscale.bos.utils.receivers.ScheduleReceiver;

/**
 * Created by kurniawanrizzki on 20/05/18.
*/
@SuppressLint("OverrideAbstract")
public class BosNotificationService extends NotificationListenerService {
    ScheduleReceiver receiver;
    BosLogger LOGGER;
    static final String TAG = BosLogger.getLogPrefix(BosNotificationService.class);

    @Override
    public void onCreate() {
        try {
            LOGGER = (BosLogger) InstanceManager.getInstanceManager(this).get(Instance.LOGGER_INSTANCE);

            receiver = new ScheduleReceiver();
            registerReceiver(receiver, new IntentFilter(ScheduleReceiver.ACTION));
            LOGGER.info(TAG, "register receiver");
            if (!AppGlobal.isScheduleRunning) {
                Bundle bundle = new Bundle();
                Intent scheduleIntent = new Intent(ScheduleReceiver.ACTION);
                bundle.putString(Intent.EXTRA_SUBJECT, ScheduleReceiver.NEW_SCHEDULE_SUBJECT);
                scheduleIntent.putExtra(ScheduleReceiver.NEW_SCHEDULE_BUNDLE, bundle);
                sendBroadcast(scheduleIntent);
                LOGGER.info(TAG, "running schedule");
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
            LOGGER.info(TAG, "unregister receiver");
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
