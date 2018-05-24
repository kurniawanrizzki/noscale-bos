package com.noscale.bos.utils.tools;

import com.noscale.bos.models.HttpMessage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by kurniawanrizzki on 21/05/18.
 */

public interface APIInterface {

    @Headers({"Accept: application/json"})
    @POST("/request")
    Call<HttpMessage.Response> sendingRequestMessage (@Body HttpMessage.Request request);

    @Headers({"Accept: application/json"})
    @POST("/instance3327/message?token=ip3pzrfgrpd6j7em")
    Call<HttpMessage.WhatsappResponse> sendingResponse (@Body HttpMessage.WhatsappRequest request);

}
