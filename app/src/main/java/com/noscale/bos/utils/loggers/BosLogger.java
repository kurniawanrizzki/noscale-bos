package com.noscale.bos.utils.loggers;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.noscale.bos.utils.AppGlobal;
import com.noscale.bos.utils.Instance;
import com.noscale.bos.utils.tools.Utility;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by kurniawanrizzki on 21/05/18.
 */

public class BosLogger extends Instance implements Logger {

    private static final String LOG_PREFIX = getLogPrefix(BosLogger.class);
    private Writer fileWriter;
    private boolean isPermissionGranted;

    public BosLogger(Context context, String tag) {
        super(context, tag);
    }

    @Override
    public void setup() {

        if (AppGlobal.IS_FILE_LOGGED_VERSION) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                List<String> deniedPermissionList = Utility.getDeniedPermissionList(context, AppGlobal.FILE_PERMISSION);
                if (deniedPermissionList.size() > 0) {
                    warning(LOG_PREFIX, "The log is not able to write in the file, cause of permission not granted");
                    return;
                }
                isPermissionGranted = true;
            }

            File logFile;
            File dir = new File(AppGlobal.FILE_LOG_DIR);
            if (!dir.exists() || !dir.isDirectory()) {
                dir.mkdir();
            }

            logFile = new File(dir.getPath()+"/"+AppGlobal.FILE_LOG_NAME);

            try {

                if (!logFile.exists() || !logFile.isFile()) {
                    logFile.createNewFile();
                }

                fileWriter = new BufferedWriter(
                        new FileWriter(logFile)
                );

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        }

    }

    @Override
    public void debug(String title, String content) {
        write("debug", title, content);
        Log.d(title, content);
    }

    @Override
    public void error(String title, String content) {
        write("error", title, content);
        Log.e(title, content);
    }

    @Override
    public void info(String title, String content) {
        write("info", title, content);
        Log.i(title, content);
    }

    @Override
    public void verbose(String title, String content) {
        write("verbose", title, content);
        Log.v(title, content);
    }

    @Override
    public void warning(String title, String content) {
        write("warning", title, content);
        Log.w(title, content);
    }

    @Override
    public void write(String level, String title, String content) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar cal = Calendar.getInstance(Locale.getDefault());

            if (AppGlobal.IS_FILE_LOGGED_VERSION) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    List<String> deniedPermissionList = Utility.getDeniedPermissionList(context, AppGlobal.FILE_PERMISSION);
                    if (deniedPermissionList.size() > 0) {
                        isPermissionGranted = false;
                        warning(LOG_PREFIX, "The log is not able to write in in the file, permission not granted");
                        return;
                    }
                    isPermissionGranted = true;
                }

                if (null != fileWriter && isPermissionGranted) {
                    String time_str = dateFormat.format(cal.getTime());

                    fileWriter.append("[" + time_str + "]["+level+"] "+ title +" "+content+"\n");
                    fileWriter.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getLogPrefix (Class<?> className) {
        String LOG_PREFIX_IDENTIFIER = "bos_";
        String name = className.getName();
        final int maxLength = 23 - LOG_PREFIX_IDENTIFIER.length();
        if (name.length() > maxLength) {
            name = name.substring(name.length() - maxLength, name.length());
        }
        return LOG_PREFIX_IDENTIFIER + name;
    }
}
