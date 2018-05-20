package com.noscale.bos.utils.preferences;

import java.util.HashMap;

/**
 * Created by kurniawanrizzki on 21/05/18.
 */

public interface Configuration {
    HashMap<String, Object> configurationMap = new HashMap<>();

    String USED_BASE_URL = "bos.configuration.url";
    String USED_PORT = "bos.configuration.port";
    String USED_LANGUAGE = "bos.configuration.language";
    String USED_IMEI = "bos.configuration.imei";
    String IS_SERVICE_ENABLE = "bos.configuration.enabled_service";
    String USED_INTERVAL_MESSAGE_FORWARDED = "bos.configuration.interval";
    String USED_QUERY_LIMIT_PER_INTERVAL_MESSAGE_FORWARDED = "bos.configuration.limit_interval";

    void buildConfiguration ();
}
