package com.noscale.bos.utils;

import android.content.Context;

/**
 * Created by kurniawanrizzki on 21/05/18.
 */

public abstract class Manager {
    protected Context context;
    protected void setup (Context context) {
        this.context = context;
    }
    public abstract Instance get (String tag) throws ClassNotFoundException;
}
