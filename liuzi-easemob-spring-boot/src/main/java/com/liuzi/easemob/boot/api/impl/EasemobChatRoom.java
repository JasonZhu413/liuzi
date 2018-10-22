package com.liuzi.easemob.boot.api.impl;

import com.liuzi.easemob.boot.api.ChatRoomAPI;
import com.liuzi.easemob.boot.comm.ResponseHandler;
import com.liuzi.easemob.boot.comm.EasemobAPI;
import com.liuzi.easemob.boot.EasemobConfig;
import io.swagger.client.ApiException;
import io.swagger.client.StringUtil;
import io.swagger.client.api.ChatRoomsApi;
import io.swagger.client.model.Chatroom;
import io.swagger.client.model.ModifyChatroom;
import io.swagger.client.model.UserNames;

public class EasemobChatRoom extends EasemobConfig implements ChatRoomAPI {
    private ResponseHandler responseHandler = new ResponseHandler();
    private ChatRoomsApi rooms_api = new ChatRoomsApi();

    @Override
    public Object createChatRoom(final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsPost( ORG_NAME, APP_NAME, getAccessToken(), (Chatroom) payload);
            }
        });
    }

    @Override
    public Object modifyChatRoom(final String roomId,final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdPut( ORG_NAME, APP_NAME, getAccessToken(),roomId, (ModifyChatroom) payload);
            }
        });
    }

    @Override
    public Object deleteChatRoom(final String roomId) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdDelete( ORG_NAME, APP_NAME, getAccessToken(),roomId);
            }
        });
    }

    @Override
    public Object getAllChatRooms() {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsGet( ORG_NAME, APP_NAME, getAccessToken());
            }
        });
    }

    @Override
    public Object getChatRoomDetail(final String roomId) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdGet( ORG_NAME, APP_NAME, getAccessToken(),roomId);
            }
        });
    }

    @Override
    public Object addSingleUserToChatRoom(final String roomId,final String userName) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdUsersUsernamePost( ORG_NAME, APP_NAME, getAccessToken(),roomId,userName);
            }
        });
    }

    @Override
    public Object addBatchUsersToChatRoom(final String roomId,final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdUsersPost( ORG_NAME, APP_NAME, getAccessToken(),roomId, (UserNames) payload);
            }
        });
    }

    @Override
    public Object removeSingleUserFromChatRoom(final String roomId,final String userName) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdUsersUsernameDelete( ORG_NAME, APP_NAME, getAccessToken(),roomId,userName);
            }
        });
    }

    @Override
    public Object removeBatchUsersFromChatRoom(final String roomId,final String[] userNames) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdUsersUsernamesDelete( ORG_NAME, APP_NAME, getAccessToken(),roomId, StringUtil.join(userNames,","));
            }
        });
    }
}
