package com.noscale.bos.utils.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.gson.Gson;
import com.noscale.bos.controllers.MainController;
import com.noscale.bos.controllers.MessageController;
import com.noscale.bos.models.RequestMessage;
import com.noscale.bos.models.HttpMessage;
import com.noscale.bos.utils.AppGlobal;
import com.noscale.bos.utils.Instance;
import com.noscale.bos.utils.databases.BosDatabase;
import com.noscale.bos.utils.loggers.BosLogger;
import com.noscale.bos.utils.managers.InstanceManager;
import com.noscale.bos.utils.preferences.Configuration;
import com.noscale.bos.utils.tools.APIInterface;
import com.noscale.bos.utils.tools.Utility;

import java.util.Arrays;
import java.util.List;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kurniawanrizzki on 21/05/18.
 */

public class ScheduleReceiver extends BroadcastReceiver {

    private BosLogger LOGGER;
    private BosDatabase database;
    private static final String TAG = BosLogger.getLogPrefix(ScheduleReceiver.class);

    public static final int REQUEST_ID = 22;
    public static final String ACTION = "com.noscale.bos.utils.receivers.ScheduleReceiver";
    public static final String NEW_SCHEDULE_BUNDLE = "bos.schedule.bundle";
    public static final String NEW_SCHEDULE_SUBJECT = "bos.schedule.new_schedule";
    public static final String MESSAGES_SCHEDULE_DATA = "bos.schedule.messages";

    @Override
    public void onReceive(Context context, Intent intent) {

        try {

            LOGGER = (BosLogger) InstanceManager.getInstanceManager(context).get(Instance.LOGGER_INSTANCE);
            database = ((BosDatabase) InstanceManager.getInstanceManager(context).get(Instance.DB_INSTANCE));

            String URL = Configuration.configurationMap.get(Configuration.USED_BASE_URL)+":"+Configuration.configurationMap.get(Configuration.USED_PORT);
            APIInterface anInterface = Utility.getClient(URL).create(APIInterface.class);

            Bundle bundle = intent.getBundleExtra(NEW_SCHEDULE_BUNDLE);
            String subject = bundle.getString(Intent.EXTRA_SUBJECT);

            if (null != subject && subject.equals(NEW_SCHEDULE_SUBJECT)) {
                final MessageController controller = (MessageController) AppGlobal.controllerMap.get(MessageController.TAG);

                List<RequestMessage> requestMessagesExtra = bundle.getParcelableArrayList(MESSAGES_SCHEDULE_DATA);

                if (null != requestMessagesExtra) {
                    for (RequestMessage requestMessage:requestMessagesExtra) {

                        //change the token with registered IMEI;
                        Call<HttpMessage.Response> responseMessageCall = anInterface.sendingRequestMessage(
                                new HttpMessage()
                                        .new Request(
                                        "093f8b8afbe3",
                                        requestMessage.getSrc(),
                                        requestMessage.getContent(),
                                        requestMessage.getId(),
                                        requestMessage.getLastAttempt()
                                )
                        );

                        database.getRequestMessageTable().updateRequestMessage(
                                requestMessage.getStatus(),
                                RequestMessage.Status.QUEUE,
                                requestMessage.getId()
                        );

                        responseMessageCall.enqueue(new Callback<HttpMessage.Response>() {
                            @Override
                            public void onResponse(Call<HttpMessage.Response> call, final Response<HttpMessage.Response> response) {

                                final HttpMessage.Response responseBody = response.body();
                                final HttpMessage.Request requestBody = Utility.getRequestBody(call.request().body());

                                if (null != requestBody) {
                                    controller.sendMessage(
                                            requestBody.id,
                                            requestBody.phone,
                                            responseBody.message
                                    );
                                    LOGGER.debug(TAG, "onResponse : "+requestBody.toString()+" "+responseBody.toString());
                                    return;
                                }

                                LOGGER.error(TAG, "doesn't get a request body, message is not able to forwarded");

                            }

                            @Override
                            public void onFailure(Call<HttpMessage.Response> call, Throwable t) {

                                HttpMessage.Request requestBody = Utility.getRequestBody(call.request().body());
                                database.getRequestMessageTable().updateRequestMessage(
                                        requestBody.lastAttempt,
                                        RequestMessage.Status.WAITING,
                                        requestBody.id
                                );

                                LOGGER.info(TAG, "onFailure : "+t.getMessage()+" "+t.getCause());

                                if (null != requestBody) {
                                    int lastAttempt = requestBody.lastAttempt + 1;
                                    controller.sendMessage(
                                            lastAttempt,
                                            requestBody.id,
                                            requestBody.phone
                                    );
                                    LOGGER.debug(TAG, requestBody.toString());
                                    return;
                                }

                                LOGGER.error(TAG, "doesn't get a request body, message is not able to forwarded");

                            }
                        });
                    }
                }

                int limit = (int) Configuration.configurationMap.get(Configuration.USED_QUERY_LIMIT_PER_INTERVAL_MESSAGE_FORWARDED);
                List<RequestMessage> scheduledData = database.getRequestMessageTable().getMessageRequestScheduledData(limit);
                controller.schedule(scheduledData);

                if (null != scheduledData) {
                    LOGGER.info(TAG, "Preparing Data in Next 10s : "+ Arrays.toString(scheduledData.toArray()));
                    return;
                }

                LOGGER.info(TAG, "Preparing Data in Next 10s : null");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
