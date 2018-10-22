package com.liuzi.easemob.boot.service;

import com.liuzi.easemob.boot.comm.Response;


public interface EasemobService{
	/**
	 * Request an Authentication Token
	 */
	String getAuthToken ();
	/**
	 * 获取群组，参数为空时获取所有群组 <br>
	 * GET
	 * 
	 * @param limit
	 *            单页数量
	 * @param cursor
	 *            游标，存在更多记录时产生
	 * @return
	 */
	Response getChatGroups(Long limit, String cursor);

	/**
	 * 获取一个或者多个群组的详情 <br>
	 * GET
	 * 
	 * @param groupIds
	 *            群组ID数组
	 * @return
	 */
	Response getChatGroupDetails(String[] groupIds);

	/**
	 * 创建一个群组 <br>
	 * POST
	 * 
	 * @param payload
	 *            <code>{"groupname":"testrestgrp12","desc":"server create group","public":true,"maxusers":300,"approval":true,"owner":"jma1","members":["jma2","jma3"]}</code>
	 * @return
	 */
	Response createChatGroup(Object payload);

	/**
	 * 修改群组信息 <br>
	 * PUT
	 * 
	 * @param groupId
	 *            群组标识
	 * @param payload
	 *            <code>{"groupname":"testrestgrp12",description":"update groupinfo","maxusers":300}</code>
	 * @return
	 */
	Response modifyChatGroup(String groupId, Object payload);

	/**
	 * 删除群组 <br>
	 * DELETE
	 * 
	 * @param groupId
	 *            群组标识
	 * @return
	 */
	Response deleteChatGroup(String groupId);

	/**
	 * 获取群组所有用户 <br>
	 * GET
	 * 
	 * @param groupId
	 *            群组标识
	 * @return
	 */
	Response getChatGroupUsers(String groupId);

	/**
	 * 群组加人[单个] <br>
	 * POST
	 * 
	 * @param groupId
	 *            群组标识
	 * @param userId
	 *            用户ID或用户名
	 * @return
	 */
	Response addSingleUserToChatGroup(String groupId, String userId);

	/**
	 * 群组加人[批量] <br>
	 * POST
	 * 
	 * @param groupId
	 *            群组标识
	 * @param payload
	 *            用户ID或用户名，数组形式
	 * @return
	 * @see com.easemob.server.example.comm.body.UserNamesBody
	 */
	Response addBatchUsersToChatGroup(String groupId, Object payload);

	/**
	 * 群组减人[单个] <br>
	 * DELETE
	 * 
	 * @param groupId
	 *            群组标识
	 * @param userId
	 *            用户ID或用户名
	 * @return
	 */
	Response removeSingleUserFromChatGroup(String groupId, String userId);

	/**
	 * 群组减人[批量] <br>
	 * DELETE
	 * 
	 * @param groupId
	 *            群组标识
	 * @param userIds
	 *            用户ID或用户名，数组形式
	 * @return
	 */
	Response removeBatchUsersFromChatGroup(String groupId, String[] userIds);

	/**
	 * 群组转让 <br>
	 * PUT
	 * 
	 * @param groupId
	 *            群组标识
	 * @param payload
	 *            新群主ID或用户名
	 * @return
     * @see com.easemob.server.example.comm.body.GroupOwnerTransferBody
	 */
	Response transferChatGroupOwner(String groupId, Object payload);

	/**
	 * 查询群组黑名单 <br>
	 * GET
	 * 
	 * @param groupId
	 *            群组标识
	 * @return
	 */
	Response getChatGroupBlockUsers(String groupId);

	/**
	 * 群组黑名单个添加 <br>
	 * POST
	 * 
	 * @param groupId
	 *            群组标识
	 * @param userId
	 *            用户ID或用户名
	 * @return
	 */
	Response addSingleBlockUserToChatGroup(String groupId, String userId);

	/**
	 * 群组黑名单批量添加 <br>
	 * POST
	 * 
	 * @param groupId
	 *            群组标识
	 * @param payload
	 *            用户ID或用户名，数组形式
	 * @return
     * @see com.easemob.server.example.comm.body.UserNamesBody
	 */
	Response addBatchBlockUsersToChatGroup(String groupId, Object payload);

	/**
	 * 群组黑名单单个删除 <br>
	 * DELETE
	 * 
	 * @param groupId
	 *            群组标识
	 * @param userId
	 *            用户ID或用户名
	 * @return
	 */
	Response removeSingleBlockUserFromChatGroup(String groupId, String userId);

	/**
	 * 群组黑名单批量删除 <br>
	 * DELETE
	 * 
	 * @param groupId
	 *            群组标识
	 * @param userIds
	 *            用户ID或用户名，数组形式
	 * @return
	 */
	Response removeBatchBlockUsersFromChatGroup(String groupId, String[] userIds);
	/**
	 * 导出聊天记录，默认返回10条 <br>
	 * GET
	 *
	 * @param limit
	 *            单页条数，最多1000
	 * @param cursor
	 *            游标，存在更多页时产生
	 * @param query
	 *            查询语句 <code>ql=select * where timestamp>1403164734226</code>
	 * @return
	 *
	 * 此接口已经过期，下个版本将会提供新接口
	 */
	Response exportChatMessages(Long limit, String cursor, String query);

    /**
     * 获取聊天记录下载链接
     * GET
     * @param timeStr
     *              目标聊天记录的时间
     * @return
     */
	Response exportChatMessages(String timeStr);
	/**
	 * 创建聊天室 <br>
	 * POST
	 * 
	 * @param payload
	 *            <code>{name":"testchatroom","description":"server create chatroom","maxusers":300,"owner":"jma1","members":["jma2","jma3"]}</code>
	 * @return
	 */
	Response createChatRoom(Object payload);

	/**
	 * 修改聊天室信息 <br>
	 * PUT
	 * 
	 * @param roomId
	 *            聊天室标识
	 * @param payload
	 *            <code>{"name":"test chatroom","description":
	 *            "update chatroominfo","maxusers":200}
	 * @return
	 */
	Response modifyChatRoom(String roomId, Object payload);

	/**
	 * 删除聊天室 <br>
	 * DELETE
	 * 
	 * @param roomId
	 *            聊天室标识
	 * @return
	 */
	Response deleteChatRoom(String roomId);

	/**
	 * 获取app中所有的聊天室 <br>
	 * GET
	 * 
	 * @return
	 */
	Response getAllChatRooms();

	/**
	 * 获取一个聊天室详情 <br>
	 * GET
	 * 
	 * @param roomId
	 *            聊天室标识
	 * @return
	 */
	Response getChatRoomDetail(String roomId);

	/**
	 * 聊天室成员添加[单个] <br>
	 * POST
	 * 
	 * @param roomId
	 *            聊天室标识
	 * @param userName
	 *            用户ID或用户名
	 * @return
	 */
	Response addSingleUserToChatRoom(String roomId, String userName);

	/**
	 * 聊天室成员添加[批量] <br>
	 * POST
	 * 
	 * @param roomId
	 *            聊天室标识
	 * @param payload
	 *            用户ID或用户名，数组形式
	 * @return
	 * @see com.easemob.server.example.comm.body.UserNamesBody
	 */
	Response addBatchUsersToChatRoom(String roomId, Object payload);

	/**
	 * 聊天室成员删除[单个] <br>
	 * DELETE
	 * 
	 * @param roomId
	 *            聊天室标识
	 * @param userName
	 *            用户ID或用户名
	 * @return
	 */
	Response removeSingleUserFromChatRoom(String roomId, String userName);

	/**
	 * 聊天室成员删除[批量] <br>
	 * DELETE
	 * 
	 * @param roomId
	 *            聊天室标识
	 * @param userNames
	 *            用户ID或用户名，数组形式
	 * @return
	 */
	Response removeBatchUsersFromChatRoom(String roomId, String[] userNames);
	/**
	 * 上传文件 <br>
	 * POST
	 * 
	 * @param file
	 *            上传的文件对象，可以是地址、流等，以实现类为准
	 * @return
	 */
	Response uploadFile(Object file);

	/**
	 * 下载文件 <br>
	 * GET
	 * 
	 * @param fileUUID
	 *            文件唯一标识，从上传Response-entities-uuid中获取
	 * @param shareSecret
	 *            文件访问秘钥，从上传Response-entities-share-secret中获取
	 * @param isThumbnail
	 *            ，如果下载图片，是否为缩略图
	 * @return
	 */
	Response downloadFile(String fileUUID, String shareSecret, Boolean isThumbnail);
	/**
	 * 注册IM用户[单个] <br>
	 * POST
	 * 
	 * @param payload
	 *            <code>{"username":"${用户名}","password":"${密码}", "nickname":"${昵称值}"}</code>
	 * @return
	 */
	Response createNewIMUserSingle(Object payload);

	/**
	 * 注册IM用户[批量] <br>
	 * POST
	 * 
	 * @param payload
	 *            <code>[{"username":"${用户名1}","password":"${密码}"},…,{"username":"${用户名2}","password":"${密码}"}]</code>
	 * @return
	 */
	Response createNewIMUserBatch(Object payload);

	/**
	 * 获取IM用户[单个] <br>
	 * GET
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 */
	Response getIMUserByUserName(String userName);

	/**
	 * 获取IM用户[批量]，参数为空时默认返回最早创建的10个用户 <br>
	 * GET
	 * 
	 * @param limit
	 *            单页获取数量
	 * @param cursor
	 *            游标，大于单页记录时会产生
	 * @return
	 */
	Response getIMUsersBatch(Long limit, String cursor);

	/**
	 * 删除IM用户[单个] <br>
	 * DELETE
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 */
	Response deleteIMUserByUserName(String userName);

	/**
	 * 删除IM用户[批量]，随机删除 <br>
	 * DELETE
	 * 
	 * @param limit
	 *            删除数量，建议100-500
	 * @return
	 */
	Response deleteIMUserBatch(Long limit, String cursor);

	/**
	 * 重置IM用户密码 <br>
	 * PUT
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param payload
	 *            <code>{"newpassword" : "${新密码指定的字符串}"}</code>
	 * @return
	 */
	Response modifyIMUserPasswordWithAdminToken(String userName, Object payload);

	/**
	 * 修改用户昵称 <br>
	 * PUT
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param payload
	 *            <code>{"nickname" : "${昵称值}"}</code>
	 * @return
	 */
	Response modifyIMUserNickNameWithAdminToken(String userName, Object payload);

	/**
	 * 给IM用户的添加好友 <br>
	 * POST
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param friendName
	 *            好友用戶名或用戶ID
	 * @return
	 */
	Response addFriendSingle(String userName, String friendName);

	/**
	 * 解除IM用户的好友关系 <br>
	 * DELETE
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param friendName
	 *            好友用戶名或用戶ID
	 * @return
	 */
	Response deleteFriendSingle(String userName, String friendName);

	/**
	 * 查看某个IM用户的好友信息 <br>
	 * GET
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 */
	Response getFriends(String userName);

	/**
	 * 获取IM用户的黑名单 <br>
	 * GET
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 */
	Response getBlackList(String userName);

	/**
	 * 往IM用户的黑名单中加人 <br>
	 * POST
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param payload
	 *            <code>{"usernames":["5cxhactgdj", "mh2kbjyop1"]}</code>
	 * @return
	 */
	Response addToBlackList(String userName, Object payload);

	/**
	 * 从IM用户的黑名单中减人 <br>
	 * DELETE
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param blackListName
	 *            黑名单用戶名或用戶ID
	 * @return
	 */
	Response removeFromBlackList(String userName, String blackListName);

	/**
	 * 查看用户在线状态 <br>
	 * GET
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 */
	Response getIMUserStatus(String userName);

	/**
	 * 查询离线消息数 <br>
	 * GET
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 */
	Response getOfflineMsgCount(String userName);

	/**
	 * 查询某条离线消息状态 <br>
	 * GET
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param msgId
	 *            消息ID
	 * @return
	 */
	Response getSpecifiedOfflineMsgStatus(String userName, String msgId);

	/**
	 * 用户账号禁用 <br>
	 * POST
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 */
	Response deactivateIMUser(String userName);

	/**
	 * 用户账号解禁 <br>
	 * POST
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 */
	Response activateIMUser(String userName);

	/**
	 * 强制用户下线 <br>
	 * GET
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 */
	Response disconnectIMUser(String userName);

	/**
	 * 获取用户参与的群组 <br>
	 * GET
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 * @see http://docs.easemob.com/doku.php?id=start:100serverintegration:
	 *      60groupmgmt
	 */
	Response getIMUserAllChatGroups(String userName);

	/**
	 * 获取用户所有参与的聊天室 <br>
	 * GET
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 * @see http://docs.easemob.com/doku.php?id=start:100serverintegration:
	 *      70chatroommgmt
	 */
	Response getIMUserAllChatRooms(String userName);
	/**
	 * send msg
	 * @param payload
	 * @return
	 */
	Response sendMessage(Object payload);
}
