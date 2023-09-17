/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.utility.chat;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.dto.ChatFileDTO;
import com.ipv.chat.listener.UploadProgressAdapter;
import com.ipv.chat.service.ChatService;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.service.uploaddownload.DownLoaderHelps;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.ipvision.model.dao.InsertIntoFriendMessageTable;
import com.ipvision.model.dao.InsertIntoGroupMessageTable;
import com.ipvision.view.chat.SingleChatPanel;
import com.ipvision.model.MessageDTO;
import com.ipvision.view.utility.chat.ChatHashMap;
import com.ipvision.view.utility.chat.ChatHelpers;
import com.ipvision.model.dao.GetSingleMessageDAO;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.utility.ImageHelpers;

/**
 *
 * @author shahadat
 */
public class UploadProgressHandler extends UploadProgressAdapter {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UploadProgressHandler.class);
    DownLoaderHelps helps = new DownLoaderHelps();

    @Override
    public void onSuccess(ChatFileDTO chatFileDTO) {
        try {
            String rename = ImageHelpers.getChatTransferFileName(chatFileDTO.getMessage());
            String extension = rename.substring(rename.lastIndexOf(".") + 1);
            rename = chatFileDTO.getMessageType() == ChatConstants.TYPE_AUDIO_FILE ? rename.replace(extension, "mp3") : rename;

            File renameFile = new File(helps.getChatFileDestinationFolder() + rename);
            boolean flag = chatFileDTO.getFile().renameTo(renameFile);
            //System.err.println("File Renamed :: " + flag);

            if (chatFileDTO.getGroupId() != null && chatFileDTO.getGroupId() > 0) {
                ChatHelpers.startGroupChat(chatFileDTO.getGroupId(), true);
                Map<String, SingleChatPanel> chatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(chatFileDTO.getGroupId() + "");
                SingleChatPanel chatPanel = chatPanelMap != null ? chatPanelMap.get(chatFileDTO.getPacketID()) : null;
                MessageDTO messageDTO = ChatHashMap.getInstance().getChatStatusMessage().get(chatFileDTO.getPacketID());
                if (messageDTO == null) {
                    messageDTO = GetSingleMessageDAO.getMessageDtoByPacketId(null, chatFileDTO.getGroupId(), chatFileDTO.getPacketID());
                }

                if (messageDTO != null) {
                    messageDTO.setMessage(chatFileDTO.getMessage());
                    List<MessageDTO> mList = new ArrayList<MessageDTO>();
                    mList.add(messageDTO);
                    new InsertIntoGroupMessageTable(mList).start();
                    ChatService.sendGroupChat(messageDTO, new PacketResenderHandler(), InternetUnavailablityCheck.isInternetAvailable);
                }

                if (chatPanel != null) {
                    MessageDTO msgDTO = chatPanel.messageDTO;
                    msgDTO.setMessage(chatFileDTO.getMessage());
                }
            } else {
                ChatHelpers.startFriendChat(chatFileDTO.getFriendIdentity());
                Map<String, SingleChatPanel> chatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(chatFileDTO.getFriendIdentity());
                SingleChatPanel chatPanel = chatPanelMap != null ? chatPanelMap.get(chatFileDTO.getPacketID()) : null;
                MessageDTO messageDTO = ChatHashMap.getInstance().getChatStatusMessage().get(chatFileDTO.getPacketID());
                if (messageDTO == null) {
                    messageDTO = GetSingleMessageDAO.getMessageDtoByPacketId(chatFileDTO.getFriendIdentity(), null, chatFileDTO.getPacketID());
                }

                if (messageDTO != null) {
                    messageDTO.setMessage(chatFileDTO.getMessage());
                    List<MessageDTO> mList = new ArrayList<MessageDTO>();
                    mList.add(messageDTO);
                    new InsertIntoFriendMessageTable(mList).start();
                    ChatService.sendFriendChat(messageDTO, new PacketResenderHandler(), InternetUnavailablityCheck.isInternetAvailable);
                }

                if (chatPanel != null) {
                    MessageDTO msgDTO = chatPanel.messageDTO;
                    msgDTO.setMessage(chatFileDTO.getMessage());
                }
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
            log.error("Error in onSuccess ==>" + ex.getMessage());
        }
    }

    @Override
    public void onProgress(int i, Object o) {

    }

    @Override
    public void onFailed(ChatFileDTO chatFileDTO) {
        try {
            if (chatFileDTO.getGroupId() != null && chatFileDTO.getGroupId() > 0) {
                Map<String, SingleChatPanel> chatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(chatFileDTO.getGroupId() + "");
                SingleChatPanel chatPanel = chatPanelMap != null ? chatPanelMap.get(chatFileDTO.getPacketID()) : null;
                MessageDTO messageDTO = ChatHashMap.getInstance().getChatStatusMessage().get(chatFileDTO.getPacketID());
                if (messageDTO == null) {
                    messageDTO = GetSingleMessageDAO.getMessageDtoByPacketId(null, chatFileDTO.getGroupId(), chatFileDTO.getPacketID());
                }

                if (messageDTO != null) {
                    messageDTO.setStatus(ChatConstants.CHAT_GROUP_PENDING);
                    messageDTO.setMessage(chatFileDTO.getMessage());
                    messageDTO.setMessageDate(messageDTO.getSystemDate());
                    List<MessageDTO> mList = new ArrayList<MessageDTO>();
                    mList.add(messageDTO);
                    new InsertIntoGroupMessageTable(mList).start();
                }

                if (chatPanel != null) {
                    MessageDTO msgDTO = chatPanel.messageDTO;
                    msgDTO.setMessage(chatFileDTO.getMessage());
                    chatPanel.timeLabel.setText(ChatHelpers.getDate(msgDTO.getSystemDate(), ChatHelpers.CHAT_TIME_FORMAT));
                    chatPanel.timeLabel.revalidate();
                    chatPanel.statusLabel.setText(ChatHelpers.STATUS_TXT_NOT_SENT);
                    chatPanel.statusLabel.revalidate();
                }

            } else {
                Map<String, SingleChatPanel> chatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(chatFileDTO.getFriendIdentity());
                SingleChatPanel chatPanel = chatPanelMap != null ? chatPanelMap.get(chatFileDTO.getPacketID()) : null;
                MessageDTO messageDTO = ChatHashMap.getInstance().getChatStatusMessage().get(chatFileDTO.getPacketID());
                if (messageDTO == null) {
                    messageDTO = GetSingleMessageDAO.getMessageDtoByPacketId(chatFileDTO.getFriendIdentity(), null, chatFileDTO.getPacketID());
                }

                if (messageDTO != null) {
                    messageDTO.setStatus(ChatConstants.CHAT_FRIEND_PENDING);
                    messageDTO.setMessage(chatFileDTO.getMessage());
                    messageDTO.setMessageDate(messageDTO.getSystemDate());
                    List<MessageDTO> mList = new ArrayList<MessageDTO>();
                    mList.add(messageDTO);
                    new InsertIntoFriendMessageTable(mList).start();
                }

                if (chatPanel != null) {
                    MessageDTO msgDTO = chatPanel.messageDTO;
                    msgDTO.setMessage(chatFileDTO.getMessage());
                    chatPanel.timeLabel.setText(ChatHelpers.getDate(msgDTO.getSystemDate(), ChatHelpers.CHAT_TIME_FORMAT));
                    chatPanel.timeLabel.revalidate();
                    chatPanel.statusLabel.setText(ChatHelpers.STATUS_TXT_NOT_SENT);
                    chatPanel.statusLabel.revalidate();
                }
            }

        } catch (Exception ex) {
          //  ex.printStackTrace();
        log.error("Error in onFailed ==>" + ex.getMessage());
        }
    }

}
