package com.noscale.bos.utils.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import com.noscale.bos.utils.AppGlobal;
import com.noscale.bos.utils.Instance;

/**
 * Created by kurniawanrizzki on 21/05/18.
 */

public class BosPreference extends Instance implements Preference, Configuration {

    private static final String PREFERENCE_NAME_CONFIG = "bos_preference";
    private static final int PREFERENCE_MODE_CONFIG = Context.MODE_PRIVATE;
    private SharedPreferences preferences;

    public BosPreference(Context context, String tag) {
        super(context, tag);
        buildConfiguration();
    }

    @Override
    protected void setup() {
        preferences = context.getSharedPreferences(PREFERENCE_NAME_CONFIG, PREFERENCE_MODE_CONFIG);
    }

    @Override
    public void setString(String key, String value) {
        preferences.edit().putString(key, value).commit();
        configurationMap.put(key, value);
    }

    @Override
    public String getString(String key, String def) {
        return preferences.getString(key, def);
    }

    @Override
    public void setInt(String key, int value) {
        preferences.edit().putInt(key, value).commit();
        configurationMap.put(key, value);
    }

    @Override
    public int getInt(String key, int def) {
        return preferences.getInt(key, def);
    }

    @Override
    public void setLong(String key, long value) {
        preferences.edit().putLong(key, value).commit();
        configurationMap.put(key, value);
    }

    @Override
    public long getLong(String key, long def) {
        return preferences.getLong(key, def);
    }

    @Override
    public void setBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value);
        configurationMap.put(key, value);
    }

    @Override
    public boolean getBoolean(String key, boolean def) {
        return preferences.getBoolean(key, def);
    }

    @Override
    public void buildConfiguration() {
        configurationMap.put(USED_BASE_URL,
                getString(USED_BASE_URL, AppGlobal.DEFAULT_USED_BASE_URL));
        configurationMap.put(USED_PORT,
                getInt(USED_PORT, AppGlobal.DEFAULT_USED_PORT)
        );
        configurationMap.put(USED_LANGUAGE,
                getInt(USED_LANGUAGE, AppGlobal.DEFAULT_USED_LANGUAGE)
        );
        configurationMap.put(USED_IMEI,
                getString(USED_IMEI, AppGlobal.DEFAULT_USED_IMEI)
        );
        configurationMap.put(IS_SERVICE_ENABLE,
                getBoolean(IS_SERVICE_ENABLE, AppGlobal.DEFAULT_IS_SERVICE_ENABLE)
        );
        configurationMap.put(USED_INTERVAL_MESSAGE_FORWARDED,
                getLong(USED_INTERVAL_MESSAGE_FORWARDED, AppGlobal.DEFAULT_USED_INTERVAL_MESSAGE_FORWARDED)
        );
        configurationMap.put(USED_QUERY_LIMIT_PER_INTERVAL_MESSAGE_FORWARDED,
                getInt(USED_QUERY_LIMIT_PER_INTERVAL_MESSAGE_FORWARDED, AppGlobal.DEFAULT_USED_QUERY_LIMIT_PER_INTERVAL_MESSAGE_FORWARDED)
        );
    }

}
