package com.noscale.bos.utils;

import android.content.Context;

/**
 * Created by kurniawanrizzki on 20/05/18.
 */

public abstract class Instance {

    public static final String DB_INSTANCE = "bos.instance.database";
    public static final String PREFERENCE_INSTANCE = "bos.instance.preference";
    public static final String LOGGER_INSTANCE = "bos.instance.logger";

    protected Context context;
    private String tag;

    public Instance (Context context, String tag) {
        this.context = context;
        this.tag = tag;
        setup();
    }

    protected abstract void setup ();

    public String getTag () {
        return tag;
    }

}
