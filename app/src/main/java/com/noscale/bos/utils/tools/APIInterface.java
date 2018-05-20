package com.noscale.bos.utils.tools;

import com.noscale.bos.models.ResponseMessage;
import com.noscale.bos.utils.AppGlobal;
import com.noscale.bos.utils.preferences.Configuration;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by kurniawanrizzki on 21/05/18.
 */

public interface APIInterface {
    String URL = String.valueOf(AppGlobal.controllerMap.get(Configuration.USED_BASE_URL));
    @POST("request")
    Call<ResponseMessage> sendingRequestMessage (String phone, String token, String message);
}
