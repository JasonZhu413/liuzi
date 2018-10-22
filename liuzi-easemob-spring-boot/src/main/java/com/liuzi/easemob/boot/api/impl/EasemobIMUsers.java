package com.liuzi.easemob.boot.api.impl;


import com.liuzi.easemob.boot.api.IMUserAPI;
import com.liuzi.easemob.boot.comm.EasemobAPI;
import com.liuzi.easemob.boot.comm.ResponseHandler;
import com.liuzi.easemob.boot.EasemobConfig;
import io.swagger.client.ApiException;
import io.swagger.client.api.UsersApi;
import io.swagger.client.model.NewPassword;
import io.swagger.client.model.Nickname;
import io.swagger.client.model.RegisterUsers;
import io.swagger.client.model.UserNames;


public class EasemobIMUsers extends EasemobConfig implements IMUserAPI {

	private UsersApi users_api = new UsersApi();
	private ResponseHandler responseHandler = new ResponseHandler();
	@Override
	public Object createNewIMUserSingle(final Object payload) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersPost( ORG_NAME, APP_NAME, (RegisterUsers) payload, getAccessToken());
			}
		});
	}

	@Override
	public Object createNewIMUserBatch(final Object payload) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersPost( ORG_NAME, APP_NAME, (RegisterUsers) payload, getAccessToken());
			}
		});
	}

	@Override
	public Object getIMUserByUserName(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameGet( ORG_NAME, APP_NAME, getAccessToken(),userName);
		}
		});
	}

	@Override
	public Object getIMUsersBatch(final Long limit,final String cursor) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersGet( ORG_NAME, APP_NAME, getAccessToken(),limit+"",cursor);
			}
		});
	}

	@Override
	public Object deleteIMUserByUserName(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameDelete( ORG_NAME, APP_NAME, getAccessToken(),userName);
			}
		});
	}

	@Override
	public Object deleteIMUserBatch(final Long limit,final String cursor) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersDelete( ORG_NAME, APP_NAME, getAccessToken(),limit+"",cursor);
			}
		});
	}

	@Override
	public Object modifyIMUserPasswordWithAdminToken(final String userName, final Object payload) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernamePasswordPut( ORG_NAME, APP_NAME,userName, (NewPassword) payload, getAccessToken());
			}
		});
	}

	@Override
	public Object modifyIMUserNickNameWithAdminToken(final String userName,final Object payload) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernamePut( ORG_NAME, APP_NAME,userName, (Nickname) payload, getAccessToken());
			}
		});
	}

	@Override
	public Object addFriendSingle(final String userName,final String friendName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameContactsUsersFriendUsernamePost( ORG_NAME, APP_NAME, getAccessToken(),userName,friendName);
			}
		});
	}

	@Override
	public Object deleteFriendSingle(final String userName,final String friendName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameContactsUsersFriendUsernameDelete( ORG_NAME, APP_NAME, getAccessToken(),userName,friendName);
			}
		});
	}

	@Override
	public Object getFriends(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameContactsUsersGet( ORG_NAME, APP_NAME, getAccessToken(),userName);
			}
		});
	}

	@Override
	public Object getBlackList(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameBlocksUsersGet( ORG_NAME, APP_NAME, getAccessToken(),userName);
			}
		});
	}

	@Override
	public Object addToBlackList(final String userName,final Object payload) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameBlocksUsersPost( ORG_NAME, APP_NAME, getAccessToken(),userName, (UserNames) payload);
			}
		});
	}

	@Override
	public Object removeFromBlackList(final String userName,final String blackListName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameBlocksUsersBlockedUsernameDelete( ORG_NAME, APP_NAME, getAccessToken(),userName,blackListName);
			}
		});
	}

	@Override
	public Object getIMUserStatus(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameStatusGet( ORG_NAME, APP_NAME, getAccessToken(),userName);
			}
		});
	}

	@Override
	public Object getOfflineMsgCount(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameOfflineMsgCountGet( ORG_NAME, APP_NAME, getAccessToken(),userName);
			}
		});
	}

	@Override
	public Object getSpecifiedOfflineMsgStatus(final String userName,final String msgId) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameOfflineMsgStatusMsgIdGet( ORG_NAME, APP_NAME, getAccessToken(),userName,msgId);
			}
		});
	}

	@Override
	public Object deactivateIMUser(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameDeactivatePost( ORG_NAME, APP_NAME, getAccessToken(),userName);
			}
		});
	}

	@Override
	public Object activateIMUser(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameActivatePost( ORG_NAME, APP_NAME, getAccessToken(),userName);
			}
		});
	}

	@Override
	public Object disconnectIMUser(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameDisconnectGet( ORG_NAME, APP_NAME, getAccessToken(),userName);
			}
		});
	}

	@Override
	public Object getIMUserAllChatGroups(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameJoinedChatgroupsGet( ORG_NAME, APP_NAME, getAccessToken(),userName);
			}
		});
	}

	@Override
	public Object getIMUserAllChatRooms(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameJoinedChatroomsGet( ORG_NAME, APP_NAME, getAccessToken(),userName);
			}
		});
	}
}
