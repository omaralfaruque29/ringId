/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.chat;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.service.uploaddownload.DownLoaderHelps;
import com.ipvision.view.settingsmenu.ChatBackgroundSetting;
import com.ipvision.model.ChatBgDTO;
import com.ipvision.model.ChatBgImageDTO;
import com.ipvision.view.utility.chat.ChatHashMap;

/**
 *
 * @author Shahadat
 */
public class LoadChatBackground extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LoadChatBackground.class);

    private String serviceUrl = ServerAndPortSettings.getChatBackgroundImageServiceUrl();
    private String downloadUrl = ServerAndPortSettings.getChatBackgroundImageDownloadUrl();
    private DownLoaderHelps dHelp = new DownLoaderHelps();
    private ChatBackgroundSetting chatBackgroundSetting;

    public LoadChatBackground(ChatBackgroundSetting chatBackgroundSetting) {
        this.chatBackgroundSetting = chatBackgroundSetting;
         setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        ChatHashMap.getInstance().getChatBgImage().clear();
        loadBackgroundListFromServer();
    }

    private void loadBackgroundListFromServer() {
        String allUrl = serviceUrl + "?chatBgIm=" + 1;
        String allResponse = HelperMethods.fetchFromUrl(allUrl);

        ChatBgDTO bgDTO = new ChatBgDTO();
        if (allResponse != null && allResponse.length() > 0) {
            bgDTO = HelperMethods.mapChatBackground(allResponse);
        }

        if (bgDTO.getSuccess() != null && bgDTO.getSuccess()) {
            if (bgDTO.getChatBgImageList() != null && bgDTO.getChatBgImageList().size() > 0) {
                for (ChatBgImageDTO entity : bgDTO.getChatBgImageList()) {
                    String imageUrl = downloadUrl + "/" + MyFnFSettings.D_FULL + "/" + entity.getName();
                    entity.setUrl(imageUrl);
                    ChatHashMap.getInstance().getChatBgImage().put(entity.getName(), entity);
                }
            }
        }

        if (chatBackgroundSetting != null && chatBackgroundSetting.type.equalsIgnoreCase(ChatBackgroundSetting.CHAT_BG_CHNAGE)) {
            chatBackgroundSetting.buildChatBackgroundImageUI();
        }
    }
}
