package com.liuzi.easemob.boot.api.impl;

import com.liuzi.easemob.boot.api.FileAPI;
import com.liuzi.easemob.boot.comm.EasemobAPI;
import com.liuzi.easemob.boot.comm.ResponseHandler;
import com.liuzi.easemob.boot.EasemobConfig;
import io.swagger.client.ApiException;
import io.swagger.client.api.UploadAndDownloadFilesApi;
import java.io.File;


public class EasemobFile extends EasemobConfig implements FileAPI {
    private ResponseHandler responseHandler = new ResponseHandler();
    private UploadAndDownloadFilesApi u_d_api = new UploadAndDownloadFilesApi();
    @Override
    public Object uploadFile(final Object file) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return u_d_api.orgNameAppNameChatfilesPost( ORG_NAME, APP_NAME, getAccessToken(),(File)file,true);
             }
        });
    }

    @Override
    public Object downloadFile(final String fileUUID,final  String shareSecret,final Boolean isThumbnail) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
               return u_d_api.orgNameAppNameChatfilesUuidGet( ORG_NAME, APP_NAME, getAccessToken(),fileUUID,shareSecret,isThumbnail);
            }
        });
    }
}
