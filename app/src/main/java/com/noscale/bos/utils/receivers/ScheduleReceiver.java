package com.noscale.bos.utils.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.noscale.bos.controllers.MainController;
import com.noscale.bos.controllers.MessageController;
import com.noscale.bos.models.RequestMessage;
import com.noscale.bos.models.ResponseMessage;
import com.noscale.bos.utils.AppGlobal;
import com.noscale.bos.utils.Instance;
import com.noscale.bos.utils.databases.BosDatabase;
import com.noscale.bos.utils.managers.InstanceManager;
import com.noscale.bos.utils.preferences.Configuration;
import com.noscale.bos.utils.tools.APIInterface;
import com.noscale.bos.utils.tools.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kurniawanrizzki on 21/05/18.
 */

public class ScheduleReceiver extends BroadcastReceiver {

    private APIInterface anInterface;

    public static final int REQUEST_ID = 22;
    public static final String ACTION = "com.noscale.bos.utils.receivers.ScheduleReceiver";
    public static final String NEW_SCHEDULE_BUNDLE = "bos.schedule.bundle";
    public static final String NEW_SCHEDULE_SUBJECT = "bos.schedule.new_schedule";
    public static final String MESSAGES_SCHEDULE_DATA = "bos.schedule.messages";

    @Override
    public void onReceive(Context context, Intent intent) {

        anInterface = Utility.getClient().create(APIInterface.class);

        Bundle bundle = intent.getBundleExtra(NEW_SCHEDULE_BUNDLE);
        String subject = bundle.getString(Intent.EXTRA_SUBJECT);

        if (null != subject && subject.equals(NEW_SCHEDULE_SUBJECT)) {
            MessageController controller = (MessageController) AppGlobal.controllerMap.get(MessageController.TAG);

            if (null == controller) {
                controller = (MessageController) new MessageController(context).setTag(MainController.TAG);
            }

            List<RequestMessage> requestMessagesExtra = bundle.getParcelableArrayList(MESSAGES_SCHEDULE_DATA);

            if (null != requestMessagesExtra) {
                for (final RequestMessage requestMessage:requestMessagesExtra) {
                    //change the token with registered IMEI;
                    Call<ResponseMessage> responseMessageCall = anInterface.sendingRequestMessage(
                            requestMessage.getSrc(), "093f8b8afbe3", requestMessage.getContent()
                    );

                    final MessageController finalController = controller;
                    responseMessageCall.enqueue(new Callback<ResponseMessage>() {
                        @Override
                        public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                            final ResponseMessage responseMessage = response.body();
                            finalController.setResponseListener(new MessageController.ResponseListener() {
                                @Override
                                public void onResponse() {
                                    finalController.sendMessage(
                                            requestMessage,
                                            responseMessage,
                                            ResponseMessage.Status.SUCCESS_STATUS
                                    );
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<ResponseMessage> call, Throwable t) {
                            finalController.setResponseListener(new MessageController.ResponseListener() {
                                @Override
                                public void onResponse() {
                                    finalController.sendMessage(
                                            requestMessage,
                                            ResponseMessage.Status.FAILED_STATUS
                                    );
                                }
                            });
                        }
                    });
                }
            }

            try {
                BosDatabase database = ((BosDatabase) InstanceManager.getInstanceManager(context).get(Instance.DB_INSTANCE));
                int limit = (int) Configuration.configurationMap.get(Configuration.USED_QUERY_LIMIT_PER_INTERVAL_MESSAGE_FORWARDED);
                List<RequestMessage> scheduledData = database.getRequestMessageTable().getMessageRequestScheduledData(limit);
                controller.schedule(scheduledData);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

}
