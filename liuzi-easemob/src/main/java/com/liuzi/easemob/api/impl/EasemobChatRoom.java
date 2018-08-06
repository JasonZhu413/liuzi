package com.liuzi.easemob.api.impl;

import com.liuzi.easemob.api.ChatRoomAPI;
import com.liuzi.easemob.comm.ResponseHandler;
import com.liuzi.easemob.comm.EasemobAPI;
import com.liuzi.easemob.EasemobConfig;
import io.swagger.client.ApiException;
import io.swagger.client.StringUtil;
import io.swagger.client.api.ChatRoomsApi;
import io.swagger.client.model.Chatroom;
import io.swagger.client.model.ModifyChatroom;
import io.swagger.client.model.UserNames;

public class EasemobChatRoom implements ChatRoomAPI {
    private ResponseHandler responseHandler = new ResponseHandler();
    private ChatRoomsApi rooms_api = new ChatRoomsApi();

    @Override
    public Object createChatRoom(final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(), (Chatroom) payload);
            }
        });
    }

    @Override
    public Object modifyChatRoom(final String roomId,final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdPut(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),roomId, (ModifyChatroom) payload);
            }
        });
    }

    @Override
    public Object deleteChatRoom(final String roomId) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),roomId);
            }
        });
    }

    @Override
    public Object getAllChatRooms() {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken());
            }
        });
    }

    @Override
    public Object getChatRoomDetail(final String roomId) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),roomId);
            }
        });
    }

    @Override
    public Object addSingleUserToChatRoom(final String roomId,final String userName) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdUsersUsernamePost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),roomId,userName);
            }
        });
    }

    @Override
    public Object addBatchUsersToChatRoom(final String roomId,final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdUsersPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),roomId, (UserNames) payload);
            }
        });
    }

    @Override
    public Object removeSingleUserFromChatRoom(final String roomId,final String userName) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdUsersUsernameDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),roomId,userName);
            }
        });
    }

    @Override
    public Object removeBatchUsersFromChatRoom(final String roomId,final String[] userNames) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdUsersUsernamesDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),roomId, StringUtil.join(userNames,","));
            }
        });
    }
}
