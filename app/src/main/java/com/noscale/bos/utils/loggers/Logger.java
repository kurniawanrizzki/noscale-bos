package com.noscale.bos.utils.loggers;

/**
 * Created by kurniawanrizzki on 21/05/18.
 */

public interface Logger {
    void debug (String title, String content);
    void error (String title, String content);
    void info (String title, String content);
    void verbose (String title, String content);
    void warning (String title, String content);
    void write (String level, String title, String content);
}
