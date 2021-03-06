package com.liuzi.easemob.api.impl;

import com.liuzi.easemob.api.ChatMessageAPI;
import com.liuzi.easemob.comm.ResponseHandler;
import com.liuzi.easemob.comm.EasemobAPI;
import com.liuzi.easemob.EasemobConfig;
import io.swagger.client.ApiException;
import io.swagger.client.api.ChatHistoryApi;


public class EasemobChatMessage  implements ChatMessageAPI {

    private ResponseHandler responseHandler = new ResponseHandler();
    private ChatHistoryApi history_api = new ChatHistoryApi();

    @Override
    public Object exportChatMessages(final Long limit,final String cursor,final String query) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return history_api.orgNameAppNameChatmessagesGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),query,limit+"",cursor);
            }
        });
    }

    @Override
    public Object exportChatMessages(final String timeStr) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return history_api.orgNameAppNameChatmessagesTimeGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),timeStr);
            }
        });
    }
}
