/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.utility.chat;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.dto.MessageBaseDTO;
import com.ipv.chat.listener.PacketResenderAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.ipvision.model.dao.InsertIntoFriendMessageTable;
import com.ipvision.model.dao.InsertIntoGroupMessageTable;
import com.ipvision.view.chat.SingleChatPanel;
import com.ipvision.model.MessageDTO;
import com.ipvision.model.RecentDTO;
import com.ipvision.service.chat.ChatFriendStartResender;
import com.ipvision.service.chat.ChatGroupStartResender;
import com.ipvision.view.utility.chat.ChatHashMap;
import com.ipvision.view.utility.chat.ChatHelpers;
import com.ipvision.model.dao.RecentChatCallActivityDAO;
import com.ipvision.model.dao.RecentContactDAO;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.view.leftdata.ChatHistoryContainer;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.model.stores.MyfnfHashMaps;

/**
 *
 * @author shahadat
 */
public class PacketResenderHandler extends PacketResenderAdapter {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PacketResenderHandler.class);

    private MessageDTO getMessageDTO(MessageBaseDTO messageBaseDTO) {
        return messageBaseDTO instanceof MessageDTO ? (MessageDTO) messageBaseDTO : (MessageDTO) messageBaseDTO.copyTo(MessageDTO.class);
    }

    @Override
    public void onInit(MessageBaseDTO msgDTO) {
        try {
            MessageDTO messageDTO = getMessageDTO(msgDTO);
            boolean isShareFile = messageDTO.getMessageType() == ChatConstants.TYPE_IMAGE_FILE_DIRECTORY
                    || messageDTO.getMessageType() == ChatConstants.TYPE_IMAGE_FILE_RING_CAMERA
                    || messageDTO.getMessageType() == ChatConstants.TYPE_AUDIO_FILE
                    || messageDTO.getMessageType() == ChatConstants.TYPE_VIDEO_FILE;

            if (messageDTO.getPacketType() == ChatConstants.CHAT_FRIEND) {
                RecentDTO recentDTO = new RecentDTO();
                recentDTO.setTime(messageDTO.getMessageDate());
                recentDTO.setContactId(messageDTO.getFriendIdentity());
                recentDTO.setContactType(RecentContactDAO.TYPE_FRIEND);
                recentDTO.setType(RecentChatCallActivityDAO.TYPE_FRIEND_CHAT);
                recentDTO.setMessageDTO(messageDTO);
                ChatHistoryContainer.changeStatus(recentDTO);

                if (isShareFile == false) {
                    MyFriendChatCallPanel myFriendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(messageDTO.getFriendIdentity());
                    if (!(myFriendChatCallPanel != null && myFriendChatCallPanel.myFriendChatPanel.addSinglePanelInChatContainerPanel(messageDTO))) {
                        saveInFriendChatDB(messageDTO);
                    }
                }
            } else if (messageDTO.getPacketType() == ChatConstants.CHAT_GROUP) {
                RecentDTO recentDTO = new RecentDTO();
                recentDTO.setTime(messageDTO.getMessageDate());
                recentDTO.setContactId(messageDTO.getGroupId() + "");
                recentDTO.setContactType(RecentContactDAO.TYPE_GROUP);
                recentDTO.setType(RecentChatCallActivityDAO.TYPE_GROUP_CHAT);
                recentDTO.setMessageDTO(messageDTO);
                ChatHistoryContainer.changeStatus(recentDTO);

                if (isShareFile == false) {
                    GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(messageDTO.getGroupId());
                    if (!(groupPanel != null && groupPanel.addSinglePanelInChatContainerPanel(messageDTO))) {
                        saveInFriendChatDB(messageDTO);
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void onSuccess(MessageBaseDTO msgDTO) {
        try {

        } catch (Exception ex) {
            //   ex.printStackTrace();
            log.error("Error in onSuccess ==>" + ex.getMessage());
        }
    }

    @Override
    public void onProgress(MessageBaseDTO messageDTO) {
        try {
            if (messageDTO.getPacketType() == ChatConstants.CHAT_FRIEND
                    || messageDTO.getPacketType() == ChatConstants.CHAT_FRIEND_CHAT_EDIT
                    || messageDTO.getPacketType() == ChatConstants.CHAT_FRIEND_MULTIPLE_MESSAGE_DELETE) {
                new ChatFriendStartResender(messageDTO.getFriendIdentity()).start();
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void onFailed(MessageBaseDTO msgDTO) {
        try {
            MessageDTO messageDTO = getMessageDTO(msgDTO);

            if (messageDTO.getPacketType() == ChatConstants.CHAT_FRIEND) {
                saveInFriendChatDB(messageDTO);
                SingleChatPanel chatPanel = getSingleChatPanel(messageDTO);
                if (chatPanel != null) {
                    String sendTime = ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT);
                    chatPanel.timeLabel.setText(sendTime);
                    chatPanel.timeLabel.revalidate();
                    chatPanel.statusLabel.setText(ChatHelpers.STATUS_TXT_NOT_SENT);
                    chatPanel.statusLabel.revalidate();
                }
                new ChatFriendStartResender(messageDTO.getFriendIdentity()).start();
            } else if (messageDTO.getPacketType() == ChatConstants.CHAT_GROUP) {
                saveInGroupChatDB(messageDTO);
                SingleChatPanel chatPanel = getSingleChatPanel(messageDTO);
                if (chatPanel != null) {
                    String sendTime = ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT);
                    chatPanel.timeLabel.setText(sendTime);
                    chatPanel.timeLabel.revalidate();
                    chatPanel.statusLabel.setText(ChatHelpers.STATUS_TXT_NOT_SENT);
                    chatPanel.statusLabel.revalidate();
                }
                new ChatGroupStartResender(messageDTO.getGroupId(), true).start();
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
        log.error("Error in onFailed ==>" + ex.getMessage());
        }
    }

    private SingleChatPanel getSingleChatPanel(MessageBaseDTO messageDTO) {
        Map<String, SingleChatPanel> chatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getGroupId() != null && messageDTO.getGroupId() > 0 ? (messageDTO.getGroupId() + "") : messageDTO.getFriendIdentity());
        SingleChatPanel chatPanel = chatPanelMap != null ? chatPanelMap.get(messageDTO.getPacketID()) : null;
        return chatPanel;
    }

    private void saveInGroupChatDB(MessageDTO messageDTO) {
        List<MessageDTO> mList = new ArrayList<MessageDTO>();
        mList.add(messageDTO);
        new InsertIntoGroupMessageTable(mList).start();
    }

    private void saveInFriendChatDB(MessageDTO messageDTO) {
        List<MessageDTO> mList = new ArrayList<MessageDTO>();
        mList.add(messageDTO);
        new InsertIntoFriendMessageTable(mList).start();
    }

}
