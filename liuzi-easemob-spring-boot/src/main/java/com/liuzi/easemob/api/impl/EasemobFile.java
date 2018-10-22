package com.liuzi.easemob.api.impl;

import com.liuzi.easemob.api.FileAPI;
import com.liuzi.easemob.comm.EasemobAPI;
import com.liuzi.easemob.comm.ResponseHandler;
import com.liuzi.easemob.EasemobConfig;
import io.swagger.client.ApiException;
import io.swagger.client.api.UploadAndDownloadFilesApi;
import java.io.File;


public class EasemobFile implements FileAPI {
    private ResponseHandler responseHandler = new ResponseHandler();
    private UploadAndDownloadFilesApi u_d_api = new UploadAndDownloadFilesApi();
    @Override
    public Object uploadFile(final Object file) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return u_d_api.orgNameAppNameChatfilesPost(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),(File)file,true);
             }
        });
    }

    @Override
    public Object downloadFile(final String fileUUID,final  String shareSecret,final Boolean isThumbnail) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
               return u_d_api.orgNameAppNameChatfilesUuidGet(EasemobConfig.ORG_NAME,EasemobConfig.APP_NAME,EasemobConfig.getAccessToken(),fileUUID,shareSecret,isThumbnail);
            }
        });
    }
}
