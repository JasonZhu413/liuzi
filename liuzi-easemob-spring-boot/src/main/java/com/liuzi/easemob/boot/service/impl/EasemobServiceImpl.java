package com.liuzi.easemob.boot.service.impl;

import java.io.File;

import io.swagger.client.ApiException;
import io.swagger.client.StringUtil;
import io.swagger.client.api.ChatHistoryApi;
import io.swagger.client.api.ChatRoomsApi;
import io.swagger.client.api.GroupsApi;
import io.swagger.client.api.MessagesApi;
import io.swagger.client.api.UploadAndDownloadFilesApi;
import io.swagger.client.api.UsersApi;
import io.swagger.client.model.Chatroom;
import io.swagger.client.model.Group;
import io.swagger.client.model.ModifyChatroom;
import io.swagger.client.model.ModifyGroup;
import io.swagger.client.model.Msg;
import io.swagger.client.model.NewOwner;
import io.swagger.client.model.NewPassword;
import io.swagger.client.model.Nickname;
import io.swagger.client.model.RegisterUsers;
import io.swagger.client.model.UserName;
import io.swagger.client.model.UserNames;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.liuzi.easemob.boot.service.EasemobService;
import com.liuzi.easemob.boot.comm.EasemobAPI;
import com.liuzi.easemob.boot.comm.Response;
import com.liuzi.easemob.boot.EasemobConfig;
import com.liuzi.easemob.boot.comm.ResponseHandler;



@Service("easemobService")
public class EasemobServiceImpl implements EasemobService{
	
	private static Logger logger = LoggerFactory.getLogger(EasemobServiceImpl.class);

	private ResponseHandler responseHandler = new ResponseHandler();
	private GroupsApi api = new GroupsApi();
	private ChatHistoryApi history_api = new ChatHistoryApi();
	private ChatRoomsApi rooms_api = new ChatRoomsApi();
	private UploadAndDownloadFilesApi u_d_api = new UploadAndDownloadFilesApi();
	private UsersApi users_api = new UsersApi();
	private MessagesApi msg_api = new MessagesApi();
	
    
	@Override
	public String getAuthToken(){
		return EasemobConfig.getAccessToken();
	}
	
	@Override
    public Response getChatGroups(final Long limit,final String cursor) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
            	logger.info("getChatGroups - params{limit: {}, cursor: {} }", limit, cursor);
            	String str = api.orgNameAppNameChatgroupsGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),limit+"",cursor);
            	logger.info("getChatGroups - return {}", str);
                return str;
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response getChatGroupDetails(final String[] groupIds) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdsGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),StringUtil.join(groupIds,","));
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }
    
    @Override
    public Response createChatGroup(final Object payload) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(), (Group) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response modifyChatGroup(final String groupId,final Object payload) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdPut(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),groupId, (ModifyGroup) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response deleteChatGroup(final String groupId) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),groupId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response getChatGroupUsers(final String groupId) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdUsersGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),groupId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response addSingleUserToChatGroup(final String groupId,final String userId) {
        final UserNames userNames = new UserNames();
        UserName userList = new UserName();
        userList.add(userId);
        userNames.usernames(userList);
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdUsersPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),groupId,userNames);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response addBatchUsersToChatGroup(final String groupId,final Object payload) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdUsersPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),groupId, (UserNames) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response removeSingleUserFromChatGroup(final String groupId,final String userId) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdUsersUsernameDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),groupId,userId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response removeBatchUsersFromChatGroup(final String groupId,final String[] userIds) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdUsersMembersDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),groupId,StringUtil.join(userIds,","));
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response transferChatGroupOwner(final String groupId,final Object payload) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupidPut(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),groupId, (NewOwner) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response getChatGroupBlockUsers(final String groupId) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdBlocksUsersGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),groupId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response addSingleBlockUserToChatGroup(final String groupId,final String userId) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernamePost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),groupId,userId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response addBatchBlockUsersToChatGroup(final String groupId,final Object payload) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdBlocksUsersPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),groupId, (UserNames) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response removeSingleBlockUserFromChatGroup(final String groupId,final String userId) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernameDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),groupId,userId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response removeBatchBlockUsersFromChatGroup(final String groupId,final String[] userIds) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernamesDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),groupId,StringUtil.join(userIds,","));
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }
    
    @Override
    public Response exportChatMessages(final Long limit,final String cursor,final String query) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return history_api.orgNameAppNameChatmessagesGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),query,limit+"",cursor);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response exportChatMessages(final String timeStr) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return history_api.orgNameAppNameChatmessagesTimeGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),timeStr);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }
    
    @Override
    public Response createChatRoom(final Object payload) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(), (Chatroom) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response modifyChatRoom(final String roomId,final Object payload) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdPut(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),roomId, (ModifyChatroom) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response deleteChatRoom(final String roomId) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),roomId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response getAllChatRooms() {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken());
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response getChatRoomDetail(final String roomId) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),roomId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response addSingleUserToChatRoom(final String roomId,final String userName) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdUsersUsernamePost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),roomId,userName);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response addBatchUsersToChatRoom(final String roomId,final Object payload) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdUsersPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),roomId, (UserNames) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response removeSingleUserFromChatRoom(final String roomId,final String userName) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdUsersUsernameDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),roomId,userName);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response removeBatchUsersFromChatRoom(final String roomId,final String[] userNames) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdUsersUsernamesDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),roomId, StringUtil.join(userNames,","));
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }
    
    @Override
    public Response uploadFile(final Object file) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return u_d_api.orgNameAppNameChatfilesPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),(File)file,true);
             }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    @Override
    public Response downloadFile(final String fileUUID,final  String shareSecret,final Boolean isThumbnail) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
               return u_d_api.orgNameAppNameChatfilesUuidGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),fileUUID,shareSecret,isThumbnail);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }
    
    @Override
	public Response createNewIMUserSingle(final Object payload) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME, (RegisterUsers) payload,EasemobConfig.getAccessToken());
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response createNewIMUserBatch(final Object payload) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME, (RegisterUsers) payload,EasemobConfig.getAccessToken());
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response getIMUserByUserName(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
		}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response getIMUsersBatch(final Long limit,final String cursor) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),limit+"",cursor);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response deleteIMUserByUserName(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response deleteIMUserBatch(final Long limit,final String cursor) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),limit+"",cursor);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response modifyIMUserPasswordWithAdminToken(final String userName, final Object payload) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernamePasswordPut(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,userName, (NewPassword) payload,EasemobConfig.getAccessToken());
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response modifyIMUserNickNameWithAdminToken(final String userName,final Object payload) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernamePut(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,userName, (Nickname) payload,EasemobConfig.getAccessToken());
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response addFriendSingle(final String userName,final String friendName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameContactsUsersFriendUsernamePost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName,friendName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response deleteFriendSingle(final String userName,final String friendName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameContactsUsersFriendUsernameDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName,friendName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response getFriends(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameContactsUsersGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response getBlackList(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameBlocksUsersGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response addToBlackList(final String userName,final Object payload) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameBlocksUsersPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName, (UserNames) payload);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response removeFromBlackList(final String userName,final String blackListName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameBlocksUsersBlockedUsernameDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName,blackListName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response getIMUserStatus(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameStatusGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response getOfflineMsgCount(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameOfflineMsgCountGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response getSpecifiedOfflineMsgStatus(final String userName,final String msgId) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameOfflineMsgStatusMsgIdGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName,msgId);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response deactivateIMUser(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameDeactivatePost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response activateIMUser(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameActivatePost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response disconnectIMUser(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameDisconnectGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response getIMUserAllChatGroups(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameJoinedChatgroupsGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	@Override
	public Response getIMUserAllChatRooms(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameJoinedChatroomsGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}
	
	@Override
    public Response sendMessage(final Object payload) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return msg_api.orgNameAppNameMessagesPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(), (Msg) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }
	
}
