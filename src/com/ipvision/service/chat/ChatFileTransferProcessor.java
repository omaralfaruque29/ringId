/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.chat;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.ChatSettings;
import com.ipv.chat.service.ChatService;
import com.ipv.chat.utils.ChatUtility;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.service.utility.MoveFileExample;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import com.ipvision.model.MessageDTO;
import com.ipvision.service.utility.chat.PacketResenderHandler;
import com.ipvision.view.utility.chat.ChatHelpers;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author shahadat
 */
public class ChatFileTransferProcessor extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatFileTransferProcessor.class);
    private MyFriendChatCallPanel myFriendChatCallPanel;
    private GroupPanel groupPanel;
    private String friendIdentity;
    private Long groupId;
    private File fileToUpload;
    private String rootDirectory;
    private String packetId;

    public ChatFileTransferProcessor(MyFriendChatCallPanel myFriendChatCallPanel, File fileToUpload) {
        this.myFriendChatCallPanel = myFriendChatCallPanel;
        this.friendIdentity = this.myFriendChatCallPanel.friendIdentity;
        this.fileToUpload = fileToUpload;
    }

    public ChatFileTransferProcessor(GroupPanel groupPanel, File fileToUpload) {
        this.groupPanel = groupPanel;
        this.groupId = this.groupPanel.groupId;
        this.fileToUpload = fileToUpload;
    }

    @Override
    public void run() {
        this.packetId = ChatUtility.getRandomPacketID();
        this.rootDirectory = ChatHelpers.getRootDirectoryOfChunkFile(this.packetId);
        makeDirectory();
        makeChunkFiles();
    }

    private void makeChunkFiles() {
        try {
            if (groupId != null && groupId > 0) {
                ChatHelpers.startGroupChat(groupId, true);
            } else {
                ChatHelpers.startFriendChat(friendIdentity);
            }

            BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileToUpload));
            long fileSize = fileToUpload.length();

            int chunkIndex;
            for (chunkIndex = 1; chunkIndex <= (fileSize / ChatSettings.FILE_CHUNK_SIZE); chunkIndex++) {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(rootDirectory + File.separator + packetId + "." + chunkIndex));
                byte[] buf = new byte[ChatSettings.FILE_CHUNK_SIZE];
                if (in.read(buf) != -1) {
                    out.write(buf);
                }
                out.close();
            }

            if (fileSize != ChatSettings.FILE_CHUNK_SIZE * (chunkIndex - 1)) {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(rootDirectory + File.separator + packetId + "." + chunkIndex));
                for (int read; (read = in.read()) != -1;) {
                    out.write(read);
                }
                out.close();
            }

            buildChatMessage(chunkIndex);
            in.close();
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Error in makeChunkFiles ==>" + ex.getMessage());
        }
    }

    private void buildChatMessage(int totalChunk) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageType(ChatConstants.TYPE_STREAM_FILE);
        messageDTO.setPacketID(packetId);
        messageDTO.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
        messageDTO.setSystemDate(System.currentTimeMillis());

        saveFileToTargetLoaction(messageDTO, fileToUpload, totalChunk);

        if (groupId != null && groupId > 0) {
            messageDTO.setPacketType(ChatConstants.CHAT_GROUP);
            messageDTO.setStatus(ChatConstants.CHAT_GROUP_PENDING);
            messageDTO.setGroupId(groupId);
            messageDTO.setFullName(MyFnFSettings.userProfile.getFullName());
            ChatService.sendGroupChat(messageDTO, new PacketResenderHandler(), InternetUnavailablityCheck.isInternetAvailable);
        } else if (friendIdentity != null && friendIdentity.length() > 0) {
            messageDTO.setPacketType(ChatConstants.CHAT_FRIEND);
            messageDTO.setStatus(ChatConstants.CHAT_FRIEND_PENDING);
            messageDTO.setFriendIdentity(friendIdentity);
            ChatService.sendFriendChat(messageDTO, new PacketResenderHandler(), InternetUnavailablityCheck.isInternetAvailable);
        }
    }

    private void makeDirectory() {
        try {
            File dir = new File(rootDirectory);
            if (!(dir.exists() && dir.isDirectory())) {
                dir.mkdirs();
                if (ChatSettings.DEBUG) {
                    System.err.println("Creating Directory \"/" + rootDirectory + "\" .................");
                }
            }
        } catch (Exception ex) {
        }
    }

    private void saveFileToTargetLoaction(MessageDTO messageDTO, File fileToUPload, int totalChunk) {
        String extension = "txt";
        try {
            String ext = HelperMethods.getExtension(fileToUPload.getName());
            String name = HelperMethods.getImageName(fileToUPload.getName());
            if (ext != null && ext.length() > 0) {
                extension = ext;
            }
            if (name.length() > 150) {
                name = name.substring(0, 150);
            }
            messageDTO.setMessage(totalChunk + "/" + name + "." + extension);

            String fileName = ChatHelpers.CHAT_DIRECTORY.getChatFileDestinationFolder() + name + "_" + messageDTO.getPacketID() + MyFnFSettings.LOGIN_USER_ID + "." + extension;
            File srcDir = new File(fileName);
            MoveFileExample.copyFile(fileToUPload, srcDir);
        } catch (Exception ex) {
        }
    }

}
