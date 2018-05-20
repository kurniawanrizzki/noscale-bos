package com.noscale.bos.utils.services;

import android.annotation.SuppressLint;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.noscale.bos.controllers.BaseController;
import com.noscale.bos.controllers.MainController;
import com.noscale.bos.controllers.MessageController;
import com.noscale.bos.utils.AppGlobal;

/**
 * Created by kurniawanrizzki on 20/05/18.

*/
@SuppressLint("OverrideAbstract")
public class BosNotificationService extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        MessageController controller = (MessageController) AppGlobal.controllerMap.get(MessageController.TAG);

        if (null == controller) {
            controller = (MessageController) new MessageController(getBaseContext()).setTag(MainController.TAG);
        }

        if (!controller.isAllowedPackage(sbn.getPackageName())) {
            return;
        }

        controller.store(sbn);

    }
}
