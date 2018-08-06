package com.liuzi.easemob.api.impl;

import com.liuzi.easemob.api.SendMessageAPI;
import com.liuzi.easemob.comm.ResponseHandler;
import com.liuzi.easemob.comm.EasemobAPI;
import com.liuzi.easemob.EasemobConfig;
import io.swagger.client.ApiException;
import io.swagger.client.api.MessagesApi;
import io.swagger.client.model.Msg;

public class EasemobSendMessage implements SendMessageAPI {
    private ResponseHandler responseHandler = new ResponseHandler();
    private MessagesApi msg_api = new MessagesApi();
    @Override
    public Object sendMessage(final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return msg_api.orgNameAppNameMessagesPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(), (Msg) payload);
            }
        });
    }
}
