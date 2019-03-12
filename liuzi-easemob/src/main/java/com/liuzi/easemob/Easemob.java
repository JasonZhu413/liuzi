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
	private EasemobConfig easemobConfig;

	private ResponseHandler response_handler = easemobConfig.getResponse_handler();
	private GroupsApi groups_api = easemobConfig.getGroups_api();
	private ChatHistoryApi history_api = easemobConfig.getHistory_api();
	private ChatRoomsApi rooms_api = easemobConfig.getRooms_api();
	private UploadAndDownloadFilesApi u_d_api = easemobConfig.getU_d_api();
	private UsersApi users_api = easemobConfig.getUsers_api();
	private MessagesApi msg_api = easemobConfig.getMsg_api();
	
	public String getAuthToken(){
		return easemobConfig.getAccessToken();
	}
	
    public Response getChatGroups(final Long limit,final String cursor) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
            	logger.info("getChatGroups - params{limit: {}, cursor: {} }", limit, cursor);
            	String str = groups_api.orgNameAppNameChatgroupsGet(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),limit+"",cursor);
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
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groups_api.orgNameAppNameChatgroupsGroupIdsGet(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),StringUtil.join(groupIds,","));
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }
    
    
    public Response createChatGroup(final Object payload) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groups_api.orgNameAppNameChatgroupsPost(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(), (Group) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response modifyChatGroup(final String groupId,final Object payload) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groups_api.orgNameAppNameChatgroupsGroupIdPut(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),groupId, (ModifyGroup) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response deleteChatGroup(final String groupId) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groups_api.orgNameAppNameChatgroupsGroupIdDelete(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),groupId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response getChatGroupUsers(final String groupId) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groups_api.orgNameAppNameChatgroupsGroupIdUsersGet(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),groupId);
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
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groups_api.orgNameAppNameChatgroupsGroupIdUsersPost(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),groupId,userNames);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response addBatchUsersToChatGroup(final String groupId,final Object payload) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groups_api.orgNameAppNameChatgroupsGroupIdUsersPost(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),groupId, (UserNames) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response removeSingleUserFromChatGroup(final String groupId,final String userId) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groups_api.orgNameAppNameChatgroupsGroupIdUsersUsernameDelete(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),groupId,userId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response removeBatchUsersFromChatGroup(final String groupId,final String[] userIds) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groups_api.orgNameAppNameChatgroupsGroupIdUsersMembersDelete(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),groupId,StringUtil.join(userIds,","));
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response transferChatGroupOwner(final String groupId,final Object payload) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groups_api.orgNameAppNameChatgroupsGroupidPut(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),groupId, (NewOwner) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response getChatGroupBlockUsers(final String groupId) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groups_api.orgNameAppNameChatgroupsGroupIdBlocksUsersGet(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),groupId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response addSingleBlockUserToChatGroup(final String groupId,final String userId) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groups_api.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernamePost(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),groupId,userId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response addBatchBlockUsersToChatGroup(final String groupId,final Object payload) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groups_api.orgNameAppNameChatgroupsGroupIdBlocksUsersPost(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),groupId, (UserNames) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response removeSingleBlockUserFromChatGroup(final String groupId,final String userId) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groups_api.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernameDelete(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),groupId,userId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response removeBatchBlockUsersFromChatGroup(final String groupId,final String[] userIds) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return groups_api.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernamesDelete(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),groupId,StringUtil.join(userIds,","));
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }
    
    
    public Response exportChatMessages(final Long limit,final String cursor,final String query) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return history_api.orgNameAppNameChatmessagesGet(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),query,limit+"",cursor);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response exportChatMessages(final String timeStr) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return history_api.orgNameAppNameChatmessagesTimeGet(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),timeStr);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }
    
    
    public Response createChatRoom(final Object payload) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsPost(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(), (Chatroom) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response modifyChatRoom(final String roomId,final Object payload) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdPut(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),roomId, (ModifyChatroom) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response deleteChatRoom(final String roomId) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdDelete(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),roomId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response getAllChatRooms() {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsGet(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken());
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response getChatRoomDetail(final String roomId) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdGet(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),roomId);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response addSingleUserToChatRoom(final String roomId,final String userName) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdUsersUsernamePost(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),roomId,userName);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response addBatchUsersToChatRoom(final String roomId,final Object payload) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdUsersPost(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),roomId, (UserNames) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response removeSingleUserFromChatRoom(final String roomId,final String userName) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdUsersUsernameDelete(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),roomId,userName);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response removeBatchUsersFromChatRoom(final String roomId,final String[] userNames) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return rooms_api.orgNameAppNameChatroomsChatroomIdUsersUsernamesDelete(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),roomId, StringUtil.join(userNames,","));
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }
    
    
    public Response uploadFile(final Object file) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return u_d_api.orgNameAppNameChatfilesPost(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),(File)file,true);
             }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }

    
    public Response downloadFile(final String fileUUID,final  String shareSecret,final Boolean isThumbnail) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
               return u_d_api.orgNameAppNameChatfilesUuidGet(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),fileUUID,shareSecret,isThumbnail);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }
    
    
	public Response createNewIMUserSingle(final Object payload) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersPost(easemobConfig.getOrg(),easemobConfig.getApp(), (RegisterUsers) payload,easemobConfig.getAccessToken());
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response createNewIMUserBatch(final Object payload) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersPost(easemobConfig.getOrg(),easemobConfig.getApp(), (RegisterUsers) payload,easemobConfig.getAccessToken());
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response getIMUserByUserName(final String userName) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameGet(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),userName);
		}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response getIMUsersBatch(final Long limit,final String cursor) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersGet(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),limit+"",cursor);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response deleteIMUserByUserName(final String userName) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameDelete(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response deleteIMUserBatch(final Long limit,final String cursor) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersDelete(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),limit+"",cursor);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response modifyIMUserPasswordWithAdminToken(final String userName, final Object payload) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernamePasswordPut(easemobConfig.getOrg(),easemobConfig.getApp(),userName, (NewPassword) payload,easemobConfig.getAccessToken());
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response modifyIMUserNickNameWithAdminToken(final String userName,final Object payload) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernamePut(easemobConfig.getOrg(),easemobConfig.getApp(),userName, (Nickname) payload,easemobConfig.getAccessToken());
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response addFriendSingle(final String userName,final String friendName) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameContactsUsersFriendUsernamePost(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),userName,friendName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response deleteFriendSingle(final String userName,final String friendName) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameContactsUsersFriendUsernameDelete(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),userName,friendName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response getFriends(final String userName) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameContactsUsersGet(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response getBlackList(final String userName) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameBlocksUsersGet(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response addToBlackList(final String userName,final Object payload) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameBlocksUsersPost(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),userName, (UserNames) payload);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response removeFromBlackList(final String userName,final String blackListName) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameBlocksUsersBlockedUsernameDelete(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),userName,blackListName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response getIMUserStatus(final String userName) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameStatusGet(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response getOfflineMsgCount(final String userName) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersOwnerUsernameOfflineMsgCountGet(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response getSpecifiedOfflineMsgStatus(final String userName,final String msgId) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameOfflineMsgStatusMsgIdGet(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),userName,msgId);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response deactivateIMUser(final String userName) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameDeactivatePost(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response activateIMUser(final String userName) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameActivatePost(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response disconnectIMUser(final String userName) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameDisconnectGet(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response getIMUserAllChatGroups(final String userName) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameJoinedChatgroupsGet(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}

	
	public Response getIMUserAllChatRooms(final String userName) {
		Object obj =  response_handler.handle(new EasemobAPI() {
			
			public Object invokeEasemobAPI() throws ApiException {
				return users_api.orgNameAppNameUsersUsernameJoinedChatroomsGet(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(),userName);
			}
		});
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
	}
	
	
    public Response sendMessage(final Object payload) {
        Object obj =  response_handler.handle(new EasemobAPI() {
            
            public Object invokeEasemobAPI() throws ApiException {
                return msg_api.orgNameAppNameMessagesPost(easemobConfig.getOrg(),easemobConfig.getApp(),easemobConfig.getAccessToken(), (Msg) payload);
            }
        });
		
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(obj.toString(), Response.class);
    }
	
}
