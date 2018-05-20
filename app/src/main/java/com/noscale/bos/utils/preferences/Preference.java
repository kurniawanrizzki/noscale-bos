package com.noscale.bos.utils.preferences;

/**
 * Created by kurniawanrizzki on 21/05/18.
 */

public interface Preference {
    void setString (String key, String value);
    String getString (String key, String def);
    void setInt (String key, int value);
    int getInt (String key, int def);
    void setLong (String key, long value);
    long getLong (String key, long def);
    void setBoolean (String key, boolean value);
    boolean getBoolean (String key, boolean def);
}
