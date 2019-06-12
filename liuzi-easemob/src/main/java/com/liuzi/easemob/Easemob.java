package com.liuzi.easemob;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.liuzi.easemob.comm.EasemobAPI;
import com.liuzi.easemob.comm.Response;
import com.liuzi.easemob.comm.ResponseHandler;



@Service("easemob")
public class Easemob{
	
	private static Logger logger = LoggerFactory.getLogger(Easemob.class);
	@Autowired
	private ResponseHandler responseHandler;
	@Autowired
	private GroupsApi groupsApi;
	@Autowired
	private ChatHistoryApi chatHistoryApi;
	@Autowired
	private ChatRoomsApi chatRoomsApi;
	@Autowired
	private UploadAndDownloadFilesApi uploadAndDownloadFilesApi;
	@Autowired
	private UsersApi usersApi;
	@Autowired
	private MessagesApi messagesApi;

	public String getAccessToken(){
		return getAccessToken();
	}
	
    public Response getChatGroups(final Long limit,final String cursor) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
            	logger.info("getChatGroups - params{limit: {}, cursor: {} }", limit, cursor);
            	String str = groupsApi.orgNameAppNameChatgroupsGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),limit+"",cursor);
            	logger.info("getChatGroups - return {}", str);
                return str;
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response getChatGroupDetails(final String[] groupIds) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groupsApi.orgNameAppNameChatgroupsGroupIdsGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),StringUtil.join(groupIds,","));
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }
    
    
    public Response createChatGroup(final Object payload) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groupsApi.orgNameAppNameChatgroupsPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(), (Group) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response modifyChatGroup(final String groupId,final Object payload) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groupsApi.orgNameAppNameChatgroupsGroupIdPut(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),groupId, (ModifyGroup) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response deleteChatGroup(final String groupId) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groupsApi.orgNameAppNameChatgroupsGroupIdDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),groupId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response getChatGroupUsers(final String groupId) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groupsApi.orgNameAppNameChatgroupsGroupIdUsersGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),groupId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response addSingleUserToChatGroup(final String groupId,final String userId) {
        final UserNames userNames = new UserNames();
        UserName userList = new UserName();
        userList.add(userId);
        userNames.usernames(userList);
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groupsApi.orgNameAppNameChatgroupsGroupIdUsersPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),groupId,userNames);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response addBatchUsersToChatGroup(final String groupId,final Object payload) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groupsApi.orgNameAppNameChatgroupsGroupIdUsersPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),groupId, (UserNames) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response removeSingleUserFromChatGroup(final String groupId,final String userId) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groupsApi.orgNameAppNameChatgroupsGroupIdUsersUsernameDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),groupId,userId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response removeBatchUsersFromChatGroup(final String groupId,final String[] userIds) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groupsApi.orgNameAppNameChatgroupsGroupIdUsersMembersDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),groupId,StringUtil.join(userIds,","));
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response transferChatGroupOwner(final String groupId,final Object payload) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groupsApi.orgNameAppNameChatgroupsGroupidPut(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),groupId, (NewOwner) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response getChatGroupBlockUsers(final String groupId) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groupsApi.orgNameAppNameChatgroupsGroupIdBlocksUsersGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),groupId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response addSingleBlockUserToChatGroup(final String groupId,final String userId) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groupsApi.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernamePost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),groupId,userId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response addBatchBlockUsersToChatGroup(final String groupId,final Object payload) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groupsApi.orgNameAppNameChatgroupsGroupIdBlocksUsersPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),groupId, (UserNames) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response removeSingleBlockUserFromChatGroup(final String groupId,final String userId) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groupsApi.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernameDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),groupId,userId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response removeBatchBlockUsersFromChatGroup(final String groupId,final String[] userIds) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groupsApi.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernamesDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),groupId,StringUtil.join(userIds,","));
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }
    
    
    public Response exportChatMessages(final Long limit,final String cursor,final String query) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return chatHistoryApi.orgNameAppNameChatmessagesGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),query,limit+"",cursor);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response exportChatMessages(final String timeStr) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return chatHistoryApi.orgNameAppNameChatmessagesTimeGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),timeStr);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }
    
    
    public Response createChatRoom(final Object payload) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return chatRoomsApi.orgNameAppNameChatroomsPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(), (Chatroom) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response modifyChatRoom(final String roomId,final Object payload) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return chatRoomsApi.orgNameAppNameChatroomsChatroomIdPut(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),roomId, (ModifyChatroom) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response deleteChatRoom(final String roomId) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return chatRoomsApi.orgNameAppNameChatroomsChatroomIdDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),roomId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response getAllChatRooms() {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return chatRoomsApi.orgNameAppNameChatroomsGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken());
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response getChatRoomDetail(final String roomId) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return chatRoomsApi.orgNameAppNameChatroomsChatroomIdGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),roomId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response addSingleUserToChatRoom(final String roomId,final String userName) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return chatRoomsApi.orgNameAppNameChatroomsChatroomIdUsersUsernamePost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),roomId,userName);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response addBatchUsersToChatRoom(final String roomId,final Object payload) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return chatRoomsApi.orgNameAppNameChatroomsChatroomIdUsersPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),roomId, (UserNames) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response removeSingleUserFromChatRoom(final String roomId,final String userName) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return chatRoomsApi.orgNameAppNameChatroomsChatroomIdUsersUsernameDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),roomId,userName);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response removeBatchUsersFromChatRoom(final String roomId,final String[] userNames) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return chatRoomsApi.orgNameAppNameChatroomsChatroomIdUsersUsernamesDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),roomId, StringUtil.join(userNames,","));
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }
    
    
    public Response uploadFile(final Object file) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return uploadAndDownloadFilesApi.orgNameAppNameChatfilesPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),(File)file,true);
             }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response downloadFile(final String fileUUID,final  String shareSecret,final Boolean isThumbnail) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
               return uploadAndDownloadFilesApi.orgNameAppNameChatfilesUuidGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),fileUUID,shareSecret,isThumbnail);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }
    
    
	public Response createNewIMUserSingle(final Object payload) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME, (RegisterUsers) payload,getAccessToken());
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response createNewIMUserBatch(final Object payload) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME, (RegisterUsers) payload,getAccessToken());
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response getIMUserByUserName(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersUsernameGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),userName);
		}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response getIMUsersBatch(final Long limit,final String cursor) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),limit+"",cursor);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response deleteIMUserByUserName(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersUsernameDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response deleteIMUserBatch(final Long limit,final String cursor) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),limit+"",cursor);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response modifyIMUserPasswordWithAdminToken(final String userName, final Object payload) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersUsernamePasswordPut(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,userName, (NewPassword) payload,getAccessToken());
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response modifyIMUserNickNameWithAdminToken(final String userName,final Object payload) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersUsernamePut(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,userName, (Nickname) payload,getAccessToken());
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response addFriendSingle(final String userName,final String friendName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersOwnerUsernameContactsUsersFriendUsernamePost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),userName,friendName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response deleteFriendSingle(final String userName,final String friendName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersOwnerUsernameContactsUsersFriendUsernameDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),userName,friendName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response getFriends(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersOwnerUsernameContactsUsersGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response getBlackList(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersOwnerUsernameBlocksUsersGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response addToBlackList(final String userName,final Object payload) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersOwnerUsernameBlocksUsersPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),userName, (UserNames) payload);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response removeFromBlackList(final String userName,final String blackListName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersOwnerUsernameBlocksUsersBlockedUsernameDelete(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),userName,blackListName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response getIMUserStatus(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersUsernameStatusGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response getOfflineMsgCount(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersOwnerUsernameOfflineMsgCountGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response getSpecifiedOfflineMsgStatus(final String userName,final String msgId) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersUsernameOfflineMsgStatusMsgIdGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),userName,msgId);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response deactivateIMUser(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersUsernameDeactivatePost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response activateIMUser(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersUsernameActivatePost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response disconnectIMUser(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersUsernameDisconnectGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response getIMUserAllChatGroups(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersUsernameJoinedChatgroupsGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response getIMUserAllChatRooms(final String userName) {
		Object obj =  responseHandler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return usersApi.orgNameAppNameUsersUsernameJoinedChatroomsGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}
	
	
    public Response sendMessage(final Object payload) {
        Object obj =  responseHandler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return messagesApi.orgNameAppNameMessagesPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,getAccessToken(), (Msg) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }
	
}
