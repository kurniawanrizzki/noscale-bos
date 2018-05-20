package com.noscale.bos.controllers;

import android.app.Activity;
import android.content.Context;
import com.noscale.bos.utils.AppGlobal;
import com.noscale.bos.utils.Instance;
import com.noscale.bos.utils.loggers.BosLogger;
import com.noscale.bos.utils.managers.InstanceManager;

/**
 * Created by kurniawanrizzki on 20/05/18.
 */
public abstract class BaseController {

    protected Activity activity;
    protected Context context;
    private String tag;

    BosLogger LOG;

    public BaseController (Activity activity, String tag) {
        this.activity = activity;
        this.context = activity;
        this.tag = tag;
        AppGlobal.controllerMap.put(tag, this);
        initialize();
    }

    public BaseController (Context context) {
        this.context = context;
        initialize();
    }

    private void initialize () {
        initData();
        initLayout();
        initEvent();
    }

    public void initData () {
        try {
            LOG = (BosLogger) InstanceManager.getInstanceManager(activity).get(Instance.LOGGER_INSTANCE);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void initLayout () {

    }

    protected void initEvent () {

    }

    public String getTag () {
        return tag;
    }

    public BaseController setTag (String tag) {
        this.tag = tag;
        AppGlobal.controllerMap.put(tag, this);
        return this;
    }

    public InstanceManager getInstanceManager () {
        return InstanceManager.getInstanceManager(context);
    }

}
