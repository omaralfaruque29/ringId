/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.chat;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.dto.ChatFileDTO;
import com.ipv.chat.service.ChatService;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.service.utility.HelperMethods;
import java.io.File;
import com.ipvision.service.uploaddownload.DownLoaderHelps;
import com.ipvision.service.utility.MoveFileExample;
import org.apache.log4j.Logger;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.model.MessageDTO;
import com.ipvision.service.utility.chat.UploadProgressHandler;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.view.utility.ImageHelpers;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatFileShareProcessor extends Thread {

    org.apache.log4j.Logger log = Logger.getLogger(ChatFileShareProcessor.class);
    private DownLoaderHelps dHelps = new DownLoaderHelps();
    private int type;
    private File fileToUPload;
    private String[] EXT_IMAGE = {"jpg", "jpeg", "png"};
    private String[] EXT_AUDIO = {"mp3", "m4a"};
    private String[] EXT_VIDEO = {"mp4"};
    private MyFriendChatCallPanel myFriendChatCallPanel;
    private GroupPanel groupPanel;
    private Long groupId;
    private String friendIdentity;
    private ChatFileDTO chatFileDTO;
    private int timerTime = 0;

    public ChatFileShareProcessor(File fileToUPload, MyFriendChatCallPanel myFriendChatCallPanel, int type, int timerTime) {
        this.fileToUPload = fileToUPload;
        this.type = type;
        this.myFriendChatCallPanel = myFriendChatCallPanel;
        this.friendIdentity = this.myFriendChatCallPanel.friendIdentity;
        this.timerTime = timerTime;
        setName(this.getClass().getSimpleName());
    }

    public ChatFileShareProcessor(File fileToUPload, GroupPanel groupPanel, int type, int timerTime) {
        this.fileToUPload = fileToUPload;
        this.type = type;
        this.groupPanel = groupPanel;
        this.groupId = this.groupPanel.groupId;
        this.timerTime = timerTime;
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        processFile();
    }

    public void processFile() {
        try {
            String message = saveFileToTargetLoaction();
            MessageDTO messageDTO = showFileChatMessage(message);
            chatFileDTO = new ChatFileDTO();
            chatFileDTO.setFriendIdentity(messageDTO.getFriendIdentity());
            chatFileDTO.setGroupId(messageDTO.getGroupId());
            chatFileDTO.setMessage(messageDTO.getMessage());
            chatFileDTO.setMessageType(type);
            chatFileDTO.setPacketID(messageDTO.getPacketID());
            chatFileDTO.setFile(fileToUPload);
            if (chatFileDTO.getMessageType() == ChatConstants.TYPE_IMAGE_FILE_DIRECTORY || chatFileDTO.getMessageType() == ChatConstants.TYPE_IMAGE_FILE_RING_CAMERA) {
                chatFileDTO.setUploadUrl(ServerAndPortSettings.getChatImageUploadingUrl());
            } else if (chatFileDTO.getMessageType() == ChatConstants.TYPE_AUDIO_FILE) {
                chatFileDTO.setUploadUrl(ServerAndPortSettings.getChatMP3UploadingUrl());
            } else if (chatFileDTO.getMessageType() == ChatConstants.TYPE_VIDEO_FILE) {
                chatFileDTO.setUploadUrl(ServerAndPortSettings.getChatMP4UploadingUrl());
            }
            ChatService.uploadChatFile(chatFileDTO, new UploadProgressHandler());
        } catch (Exception ex) {
        }
    }

    private String saveFileToTargetLoaction() {
        String message = "";
        String extension = "";
        try {
            if (type == ChatConstants.TYPE_IMAGE_FILE_DIRECTORY || type == ChatConstants.TYPE_IMAGE_FILE_RING_CAMERA) {
                extension = "png";
                long currentTime = System.currentTimeMillis();
                String ext = HelperMethods.getExtension(fileToUPload.getName());
                if (ext != null && ext.length() > 0) {
                    extension = ext;
                }
                message = MyFnFSettings.LOGIN_USER_ID + "/" + currentTime + "." + extension;
                String newName = ImageHelpers.getChatTransferFileName(message);
                File srcDir = new File(dHelps.getChatFileDestinationFolder() + newName);
                MoveFileExample.copyFile(fileToUPload, srcDir);
                fileToUPload = new File(dHelps.getChatFileDestinationFolder() + newName);
            } else {
                message = fileToUPload.getName().replace("_", "/");
            }
        } catch (Exception ex) {
        }
        return message;
    }

    private MessageDTO showFileChatMessage(String message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageType(type);
        String packetID = SendToServer.getRanDomPacketID();
        messageDTO.setPacketID(packetID);
        messageDTO.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
        messageDTO.setMessage(message);
        messageDTO.setSystemDate(System.currentTimeMillis());

        if (groupId != null && groupId > 0) {
            messageDTO.setPacketType(ChatConstants.CHAT_GROUP);
            messageDTO.setStatus(ChatConstants.CHAT_GROUP_PENDING);
            messageDTO.setGroupId(groupId);
            messageDTO.setFullName(MyFnFSettings.userProfile.getFullName());
            messageDTO.setMessageDate(ChatService.getServerTime(groupId));
        } else if (friendIdentity != null && friendIdentity.length() > 0) {
            messageDTO.setPacketType(ChatConstants.CHAT_FRIEND);
            messageDTO.setStatus(ChatConstants.CHAT_FRIEND_PENDING);
            messageDTO.setFriendIdentity(friendIdentity);
            messageDTO.setMessageDate(ChatService.getServerTime(friendIdentity));
        }
        if (timerTime > 0) {
            messageDTO.setTimeout(timerTime);
        }

        if (myFriendChatCallPanel != null) {
            myFriendChatCallPanel.myFriendChatPanel.addSinglePanelInChatContainerPanel(messageDTO);
        } else if (groupPanel != null) {
            groupPanel.addSinglePanelInChatContainerPanel(messageDTO);
        }
        return messageDTO;
    }

}
