package com.liuzi.easemob.api.impl;


import com.liuzi.easemob.api.IMUserAPI;
import com.liuzi.easemob.comm.EasemobAPI;
import com.liuzi.easemob.comm.ResponseHandler;
import com.liuzi.easemob.EasemobConfig;
import io.swagger.client.ApiException;
import io.swagger.client.api.UsersApi;
import io.swagger.client.model.NewPassword;
import io.swagger.client.model.Nickname;
import io.swagger.client.model.RegisterUsers;
import io.swagger.client.model.UserNames;


public class EasemobIMUsers  implements IMUserAPI {

	private UsersApi users_api = new UsersApi();
	private ResponseHandler responseHandler = new ResponseHandler();
	@Override
	public Object createNewIMUserSingle(final Object payload) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME, (RegisterUsers) payload,EasemobConfig.getAccessToken());
			}
		});
	}

	@Override
	public Object createNewIMUserBatch(final Object payload) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME, (RegisterUsers) payload,EasemobConfig.getAccessToken());
			}
		});
	}

	@Override
	public Object getIMUserByUserName(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
		}
		});
	}

	@Override
	public Object getIMUsersBatch(final Long limit,final String cursor) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),limit+"",cursor);
			}
		});
	}

	@Override
	public Object deleteIMUserByUserName(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
	}

	@Override
	public Object deleteIMUserBatch(final Long limit,final String cursor) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),limit+"",cursor);
			}
		});
	}

	@Override
	public Object modifyIMUserPasswordWithAdminToken(final String userName, final Object payload) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernamePasswordPut(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,userName, (NewPassword) payload,EasemobConfig.getAccessToken());
			}
		});
	}

	@Override
	public Object modifyIMUserNickNameWithAdminToken(final String userName,final Object payload) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernamePut(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,userName, (Nickname) payload,EasemobConfig.getAccessToken());
			}
		});
	}

	@Override
	public Object addFriendSingle(final String userName,final String friendName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameContactsUsersFriendUsernamePost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName,friendName);
			}
		});
	}

	@Override
	public Object deleteFriendSingle(final String userName,final String friendName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameContactsUsersFriendUsernameDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName,friendName);
			}
		});
	}

	@Override
	public Object getFriends(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameContactsUsersGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
	}

	@Override
	public Object getBlackList(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameBlocksUsersGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
	}

	@Override
	public Object addToBlackList(final String userName,final Object payload) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameBlocksUsersPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName, (UserNames) payload);
			}
		});
	}

	@Override
	public Object removeFromBlackList(final String userName,final String blackListName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameBlocksUsersBlockedUsernameDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName,blackListName);
			}
		});
	}

	@Override
	public Object getIMUserStatus(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameStatusGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
	}

	@Override
	public Object getOfflineMsgCount(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameOfflineMsgCountGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
	}

	@Override
	public Object getSpecifiedOfflineMsgStatus(final String userName,final String msgId) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameOfflineMsgStatusMsgIdGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName,msgId);
			}
		});
	}

	@Override
	public Object deactivateIMUser(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameDeactivatePost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
	}

	@Override
	public Object activateIMUser(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameActivatePost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
	}

	@Override
	public Object disconnectIMUser(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameDisconnectGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
	}

	@Override
	public Object getIMUserAllChatGroups(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameJoinedChatgroupsGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
	}

	@Override
	public Object getIMUserAllChatRooms(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameJoinedChatroomsGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
	}
}
