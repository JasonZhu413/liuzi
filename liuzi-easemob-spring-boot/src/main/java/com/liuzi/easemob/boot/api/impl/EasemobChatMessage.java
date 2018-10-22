package com.liuzi.easemob.boot.api.impl;

import com.liuzi.easemob.boot.api.ChatMessageAPI;
import com.liuzi.easemob.boot.comm.ResponseHandler;
import com.liuzi.easemob.boot.comm.EasemobAPI;
import com.liuzi.easemob.boot.EasemobConfig;
import io.swagger.client.ApiException;
import io.swagger.client.api.ChatHistoryApi;


public class EasemobChatMessage extends EasemobConfig implements ChatMessageAPI {

    private ResponseHandler responseHandler = new ResponseHandler();
    private ChatHistoryApi history_api = new ChatHistoryApi();

    @Override
    public Object exportChatMessages(final Long limit,final String cursor,final String query) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return history_api.orgNameAppNameChatmessagesGet( ORG_NAME, APP_NAME, getAccessToken(),query,limit+"",cursor);
            }
        });
    }

    @Override
    public Object exportChatMessages(final String timeStr) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return history_api.orgNameAppNameChatmessagesTimeGet( ORG_NAME, APP_NAME, getAccessToken(),timeStr);
            }
        });
    }
}
