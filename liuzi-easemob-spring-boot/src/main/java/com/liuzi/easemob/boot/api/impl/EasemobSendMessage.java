package com.liuzi.easemob.boot.api.impl;

import com.liuzi.easemob.boot.api.SendMessageAPI;
import com.liuzi.easemob.boot.comm.ResponseHandler;
import com.liuzi.easemob.boot.comm.EasemobAPI;
import com.liuzi.easemob.boot.EasemobConfig;
import io.swagger.client.ApiException;
import io.swagger.client.api.MessagesApi;
import io.swagger.client.model.Msg;

public class EasemobSendMessage extends EasemobConfig implements SendMessageAPI {
    private ResponseHandler responseHandler = new ResponseHandler();
    private MessagesApi msg_api = new MessagesApi();
    @Override
    public Object sendMessage(final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return msg_api.orgNameAppNameMessagesPost( ORG_NAME, APP_NAME, getAccessToken(), (Msg) payload);
            }
        });
    }
}
