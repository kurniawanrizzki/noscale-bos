package com.noscale.bos.utils.managers;

import android.content.Context;

import com.noscale.bos.utils.Instance;
import com.noscale.bos.utils.Manager;
import com.noscale.bos.utils.databases.BosDatabase;
import com.noscale.bos.utils.loggers.BosLogger;
import com.noscale.bos.utils.permissions.BosPermission;
import com.noscale.bos.utils.preferences.BosPreference;

/**
 * Created by kurniawanrizzki on 20/05/18.
 */

public class InstanceManager extends Manager {

    private static InstanceManager instanceManager;
    private BosDatabase database;
    private BosPreference preference;
    private BosLogger logger;
    private BosPermission permission;

    public static InstanceManager getInstanceManager (Context context) {
        if (null == instanceManager) {
            instanceManager = new InstanceManager();
        }
        instanceManager.setup(context);
        return instanceManager;
    }

    @Override
    protected void setup(Context context) {
        super.setup(context);
        if (null == database) {
            database = new BosDatabase(context, Instance.DB_INSTANCE);
        }
        if (null == permission) {
            permission = new BosPermission(context, Instance.PERMISSION_INSTANCE);
        }
        if (null == preference) {
            preference = new BosPreference(context, Instance.PREFERENCE_INSTANCE);
        }
        if (null == logger) {
            logger = new BosLogger(context, Instance.LOGGER_INSTANCE);
        }
    }

    @Override
    public Instance get (String tag) throws ClassNotFoundException {
        if (tag.equals(Instance.DB_INSTANCE)) {
            return database;
        } else if (tag.equals(Instance.LOGGER_INSTANCE)) {
            return logger;
        } else if (tag.equals(Instance.PERMISSION_INSTANCE)) {
            return permission;
        } else if (tag.equals(Instance.PREFERENCE_INSTANCE)) {
            return preference;
        }

        throw new ClassNotFoundException("Not found class for tag : "+tag);
    }

}
