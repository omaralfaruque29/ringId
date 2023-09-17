/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.utility.chat;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.dto.MessageBaseDTO;
import com.ipv.chat.listener.ChatActionAdapter;
import com.ipv.chat.service.ChatService;
import com.ipvision.audio.PlayAudioHelper;
import com.ipvision.model.constants.StatusConstants;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import com.ipvision.model.dao.InsertIntoFriendMessageTable;
import com.ipvision.model.dao.InsertIntoGroupMessageTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.GetGroupDetails;
import com.ipvision.view.chat.SingleChatPanel;
import com.ipvision.model.MessageDTO;
import com.ipvision.model.RecentDTO;
import com.ipvision.service.chat.ChatFriendStartResender;
import com.ipvision.service.chat.ChatGroupStartResender;
import com.ipvision.view.utility.chat.ChatHashMap;
import com.ipvision.view.utility.chat.ChatHelpers;
import com.ipvision.model.dao.BrokenChatDAO;
import com.ipvision.model.dao.GetSingleMessageDAO;
import com.ipvision.model.dao.RecentChatCallActivityDAO;
import com.ipvision.model.dao.RecentContactDAO;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.view.leftdata.ChatHistoryContainer;
import com.ipvision.service.utility.OnGroupResponseListener;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.view.friendprofile.MyFriendProfile;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;

/**
 *
 * @author shahadat
 */
public class ChatActionHandler extends ChatActionAdapter {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatActionHandler.class);
    private String format = "hh:mm aaa";
    private PlayAudioHelper playAudioHelper;

    public ChatActionHandler() {
        playAudioHelper = new PlayAudioHelper();
        playAudioHelper.initChatSounds();
    }

    private MessageDTO getMessageDTO(MessageBaseDTO messageBaseDTO) {
        return messageBaseDTO instanceof MessageDTO ? (MessageDTO) messageBaseDTO : (MessageDTO) messageBaseDTO.copyTo(MessageDTO.class);
    }

    @Override
    public void onFriendChat(MessageBaseDTO messageBaseDTO) {
        try {
            MessageDTO messageDTO = getMessageDTO(messageBaseDTO);
            MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(messageDTO.getUserIdentity());
            if (!(friendChatCallPanel != null && friendChatCallPanel.isDisplayable() && friendChatCallPanel.isVisible())) {
                try {
                    if (playAudioHelper.chat_tune != null) {
                        playAudioHelper.chat_tune.stop();
                    }
                    if (playAudioHelper.chat_tune != null) {
                        playAudioHelper.chat_tune.play();
                    }
                } catch (Exception e) {
                }
            }

            List<MessageDTO> mList = new ArrayList<MessageDTO>();
            mList.add(messageDTO);
            new InsertIntoFriendMessageTable(mList).start();

            RecentDTO recentDTO = new RecentDTO();
            recentDTO.setTime(messageDTO.getMessageDate());
            recentDTO.setContactId(messageDTO.getFriendIdentity());
            recentDTO.setContactType(RecentContactDAO.TYPE_FRIEND);
            recentDTO.setType(RecentChatCallActivityDAO.TYPE_FRIEND_CHAT);
            recentDTO.setMessageDTO(messageDTO);

            if (friendChatCallPanel != null && friendChatCallPanel.isDisplayable() && friendChatCallPanel.isVisible()) {
                friendChatCallPanel.myFriendChatPanel.addSinglePanelInChatContainerPanel(messageDTO);
            } else {
                RecentDTO prevRecentDTO = ChatHashMap.getInstance().getChatUnreadMessages().get(recentDTO.getContactId());
                if (prevRecentDTO == null) {
                    ChatHashMap.getInstance().getChatUnreadMessages().put(recentDTO.getContactId(), recentDTO);
                    addChatNotification(recentDTO.getContactId());
                } else {
                    if (recentDTO.getTime() >= prevRecentDTO.getTime()) {
                        ChatHashMap.getInstance().getChatUnreadMessages().put(recentDTO.getContactId(), recentDTO);
                        addChatNotification(recentDTO.getContactId());
                    } else {
                        recentDTO = prevRecentDTO;
                    }
                }
            }

            ChatHistoryContainer.changeStatus(recentDTO);
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Error in here ==>" + ex.getMessage());
        }
    }

    @Override
    public void onFriendChatDelivered(MessageBaseDTO messageDTO) {
        try {
            Map<String, SingleChatPanel> chatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getUserIdentity());
            SingleChatPanel chatPanel = chatPanelMap != null ? chatPanelMap.get(messageDTO.getPacketID()) : null;

            MessageDTO friendDeliveredMessage = ChatHashMap.getInstance().getChatStatusMessage().get(messageDTO.getPacketID());
            if (friendDeliveredMessage == null) {
                friendDeliveredMessage = GetSingleMessageDAO.getMessageDtoByPacketId(messageDTO.getUserIdentity(), null, messageDTO.getPacketID());
            }

            boolean isAlreadyDelieveredOrSeen = friendDeliveredMessage != null
                    && (friendDeliveredMessage.getStatus() == ChatConstants.CHAT_FRIEND_DELIVERED
                    || friendDeliveredMessage.getStatus() == ChatConstants.CHAT_FRIEND_SEEN);

            if (chatPanel != null && isAlreadyDelieveredOrSeen == false) {
                chatPanel.statusLabel.setText(ChatHelpers.STATUS_TXT_DELIVERED);
                chatPanel.statusLabel.revalidate();
            }

            if (friendDeliveredMessage != null && isAlreadyDelieveredOrSeen == false) {
                friendDeliveredMessage.setStatus(ChatConstants.CHAT_FRIEND_DELIVERED);
                List<MessageDTO> mList = new ArrayList<MessageDTO>();
                mList.add(friendDeliveredMessage);
                new InsertIntoFriendMessageTable(mList).start();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onFriendChatSeen(MessageBaseDTO messageDTO) {
        try {
            Map<String, SingleChatPanel> chatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getUserIdentity());
            SingleChatPanel chatPanel = chatPanelMap != null ? chatPanelMap.get(messageDTO.getPacketID()) : null;

            MessageDTO friendSeenMessage = ChatHashMap.getInstance().getChatStatusMessage().get(messageDTO.getPacketID());
            if (friendSeenMessage == null) {
                friendSeenMessage = GetSingleMessageDAO.getMessageDtoByPacketId(messageDTO.getUserIdentity(), null, messageDTO.getPacketID());
            }

            boolean isAlreadySeen = friendSeenMessage != null && friendSeenMessage.getStatus() == ChatConstants.CHAT_FRIEND_SEEN;

            if (chatPanel != null && isAlreadySeen == false) {
                chatPanel.statusLabel.setText(ChatHelpers.STATUS_TXT_SEEN);
                chatPanel.statusLabel.revalidate();
            }

            if (friendSeenMessage != null && isAlreadySeen == false) {
                friendSeenMessage.setStatus(ChatConstants.CHAT_FRIEND_SEEN);
                List<MessageDTO> mList = new ArrayList<MessageDTO>();
                mList.add(friendSeenMessage);
                new InsertIntoFriendMessageTable(mList).start();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onFriendChatSent(MessageBaseDTO messageDTO) {
        try {
            Map<String, SingleChatPanel> chatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getUserIdentity());
            SingleChatPanel chatPanel = chatPanelMap != null ? chatPanelMap.get(messageDTO.getPacketID()) : null;

            MessageDTO friendSentMessage = ChatHashMap.getInstance().getChatStatusMessage().get(messageDTO.getPacketID());
            if (friendSentMessage == null) {
                friendSentMessage = GetSingleMessageDAO.getMessageDtoByPacketId(messageDTO.getUserIdentity(), null, messageDTO.getPacketID());
            }

            if (chatPanel != null) {
                chatPanel.statusLabel.setText(ChatHelpers.STATUS_TXT_SENT_OFFLINE);
                chatPanel.statusLabel.revalidate();
            }

            if (friendSentMessage != null) {
                friendSentMessage.setStatus(ChatConstants.CHAT_FRIEND_SENT);
                List<MessageDTO> mList = new ArrayList<MessageDTO>();
                mList.add(friendSentMessage);
                new InsertIntoFriendMessageTable(mList).start();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onFriendChatTyping(MessageBaseDTO messageDTO) {
        try {
            MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(messageDTO.getUserIdentity());
            if (friendChatCallPanel != null && friendChatCallPanel.isDisplayable() && friendChatCallPanel.isVisible()) {
                UserBasicInfo basicInfo = FriendList.getInstance().getFriend_hash_map().get(messageDTO.getUserIdentity());
                String fullName = "";

                if (basicInfo != null && basicInfo.getFullName() != null) {
                    fullName = basicInfo.getFullName();
                } else {
                    fullName = messageDTO.getUserIdentity();
                }

                friendChatCallPanel.myFriendChatPanel.addTyping(fullName);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onFriendChatEdit(MessageBaseDTO messageDTO) {
        try {
            MessageDTO friendEditMessage = ChatHashMap.getInstance().getChatStatusMessage().get(messageDTO.getPacketID());
            if (friendEditMessage == null) {
                friendEditMessage = GetSingleMessageDAO.getMessageDtoByPacketId(messageDTO.getUserIdentity(), null, messageDTO.getPacketID());
            }

            if (friendEditMessage != null) {
                friendEditMessage.setPacketType(ChatConstants.CHAT_FRIEND_CHAT_EDIT);
                friendEditMessage.setMessage(messageDTO.getMessage());
                friendEditMessage.setMessageType(messageDTO.getMessageType());
                friendEditMessage.setStatus(ChatConstants.CHAT_FRIEND_DELIVERED);
            } else {
                friendEditMessage = getMessageDTO(messageDTO);
                friendEditMessage.setStatus(ChatConstants.CHAT_FRIEND_DELIVERED);
            }

            List<MessageDTO> mList = new ArrayList<MessageDTO>();
            mList.add(friendEditMessage);
            new InsertIntoFriendMessageTable(mList).start();

            Map<String, SingleChatPanel> chatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getUserIdentity());
            SingleChatPanel chatPanel = chatPanelMap != null ? chatPanelMap.get(messageDTO.getPacketID()) : null;
            if (chatPanel != null) {
                chatPanel.messageDTO = friendEditMessage;
                chatPanel.setMainPanel();
            }
        } catch (Exception e) {
        //    e.printStackTrace();
        log.error("Error in onFriendChatEdit ==>" + e.getMessage());
        }
    }

    @Override
    public void onFriendChatDelete(MessageBaseDTO messageDTO) {
        try {
            MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(messageDTO.getUserIdentity());
            Map<String, SingleChatPanel> chatPanels = ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getUserIdentity());
            SingleChatPanel chatPanel = chatPanels != null ? chatPanels.get(messageDTO.getPacketID()) : null;

            if (friendChatCallPanel != null && friendChatCallPanel.myFriendChatPanel != null && chatPanel != null) {
                friendChatCallPanel.myFriendChatPanel.removeSingleChatPanel(chatPanel);
            }

            if (chatPanels != null && chatPanel != null) {
                chatPanels.remove(messageDTO.getPacketID());
            }

            MessageDTO msDTO = GetSingleMessageDAO.getMessageDtoByPacketId(messageDTO.getUserIdentity(), null, messageDTO.getPacketID());
            if (msDTO != null) {
                List<MessageDTO> list = new ArrayList<MessageDTO>();
                msDTO.setStatus(ChatConstants.CHAT_FRIEND_MESSAGE_DELETED);
                list.add(msDTO);
                new InsertIntoFriendMessageTable(list).start();
            }

            ChatHashMap.getInstance().getChatStatusMessage().remove(messageDTO.getPacketID());
        } catch (Exception e) {
        }
    }

    @Override
    public void onFriendChatMultiDelete(MessageBaseDTO messageDTO) {
        try {
            if (messageDTO.getMessageList() != null) {
                MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(messageDTO.getUserIdentity());
                Map<String, SingleChatPanel> chatPanels = ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getUserIdentity());

                for (MessageBaseDTO msgDTO : messageDTO.getMessageList()) {
                    SingleChatPanel chatPanel = chatPanels != null ? chatPanels.get(msgDTO.getPacketID()) : null;
                    if (friendChatCallPanel != null && friendChatCallPanel.myFriendChatPanel != null && chatPanel != null) {
                        friendChatCallPanel.myFriendChatPanel.removeSingleChatPanel(chatPanel);
                    }

                    if (chatPanels != null && chatPanel != null) {
                        chatPanels.remove(msgDTO.getPacketID());
                    }

                    MessageDTO mDTO = GetSingleMessageDAO.getMessageDtoByPacketId(messageDTO.getUserIdentity(), null, msgDTO.getPacketID());
                    if (mDTO != null) {
                        List<MessageDTO> list = new ArrayList<MessageDTO>();
                        mDTO.setStatus(ChatConstants.CHAT_FRIEND_MESSAGE_DELETED);
                        list.add(mDTO);
                        new InsertIntoFriendMessageTable(list).start();
                    }

                    ChatHashMap.getInstance().getChatStatusMessage().remove(msgDTO.getPacketID());
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onFriendChatBrockenMessage(MessageBaseDTO messageBaseDTO) {
        try {
            MessageDTO messageDTO = BrokenChatDAO.getMergedBorkenChat(messageBaseDTO);
            if (messageDTO != null) {
                onFriendChat(messageDTO);
            }
        } catch (Exception ex) {
           // ex.printStackTrace();
        log.error("Error in onFriendChatBrockenMessage ==>" + ex.getMessage());
        }
    }

    @Override
    public void onFriendChatMultipleMessage(List<MessageBaseDTO> messageBaseDTOs) {
        try {
            for (MessageBaseDTO msgDTO : messageBaseDTOs) {
                onFriendChat(msgDTO);
            }
        } catch (Exception ex) {
           // ex.printStackTrace();
        log.error("Error in onFriendChatMultipleMessage ==>" + ex.getMessage());
        }
    }

    @Override
    public void onFriendChatEditBrockenMessage(MessageBaseDTO messageBaseDTO) {
        try {
            MessageDTO messageDTO = BrokenChatDAO.getMergedBorkenChat(messageBaseDTO);
            if (messageDTO != null) {
                onFriendChatEdit(messageDTO);
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
         log.error("Error in onFriendChatEditBrockenMessage ==>" + ex.getMessage());
        }
    }

    @Override
    public void onFriendFileStream(List<MessageBaseDTO> messageDTOs) {
        try {
            MessageBaseDTO firstDTO = messageDTOs.get(0);
            String rootDirectory = ChatHelpers.getRootDirectoryOfChunkFile(firstDTO.getPacketID());
            makeDirectory(rootDirectory);
            makeChunkFiles(firstDTO, messageDTOs, rootDirectory);
            File[] files = ChatHelpers.getChunkFiles(firstDTO.getPacketID());

            Map<String, SingleChatPanel> chatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(firstDTO.getFriendIdentity());
            SingleChatPanel chatPanel = chatPanelMap != null ? chatPanelMap.get(firstDTO.getPacketID()) : null;

            if (chatPanel != null && chatPanel.chatFileMessagePanel != null) {
                if (files.length == firstDTO.getFileTotalChunk()) {
                    chatPanel.chatFileMessagePanel.onSuccess();
                } else {
                    chatPanel.chatFileMessagePanel.onProgress((files.length * 100) / firstDTO.getFileTotalChunk());
                }
            }
        } catch (Exception ex) {
           // ex.printStackTrace();
        log.error("Error in onFriendFileStream ==>" + ex.getMessage());
        }
    }

    @Override
    public void onFriendFileStreamRequest(MessageBaseDTO messageDTO) {
        try {
            File[] files = ChatHelpers.getChunkFilesByIndex(messageDTO.getPacketID(), messageDTO.getMessageList());
            //System.err.println(files.length + ", " + messageDTO.getFileChunkIndexFrom() + ", " + messageDTO.getFileChunkIndexTo());
            ChatService.sendFriendFileStream(messageDTO.getFriendIdentity(), messageDTO.getPacketID(), files, messageDTO.getFileTotalChunk());
        } catch (Exception ex) {
        //    ex.printStackTrace();
        log.error("Error in onFriendFileStreamRequest ==>" + ex.getMessage());
        }
    }

    @Override
    public void onFriendChatOfflineFetch(List<MessageBaseDTO> messageBaseDTOs, Map<String, MessageBaseDTO> latestChatByFriendIdentity) {
        try {
            Map<String, List<MessageDTO>> friendNewOfflineChats = new ConcurrentHashMap<>();
            List<MessageDTO> messageDTOs = new ArrayList<MessageDTO>();
            Map<String, MessageDTO> messageMap = new ConcurrentHashMap<String, MessageDTO>();

            for (MessageBaseDTO entity : messageBaseDTOs) {
                if (entity.getPacketType() == ChatConstants.CHAT_FRIEND_BROCKEN_MESSAGE
                        || entity.getPacketType() == ChatConstants.CHAT_FRIEND_CHAT_EDIT_BROCKEN_MESSAGE) {
                    MessageDTO msgDTO = BrokenChatDAO.getMergedBorkenChat(entity);
                    if (msgDTO != null) {
                        messageDTOs.add(msgDTO);
                        messageMap.put(msgDTO.getPacketID(), msgDTO);

                        List<MessageDTO> tempList = friendNewOfflineChats.get(msgDTO.getFriendIdentity());
                        if (tempList == null) {
                            tempList = new ArrayList<>();
                            friendNewOfflineChats.put(msgDTO.getFriendIdentity(), tempList);
                        }
                        tempList.add(msgDTO);
                    }
                } else {
                    MessageDTO msgDTO = getMessageDTO(entity);
                    messageDTOs.add(msgDTO);
                    messageMap.put(msgDTO.getPacketID(), msgDTO);

                    List<MessageDTO> tempList = friendNewOfflineChats.get(msgDTO.getFriendIdentity());
                    if (tempList == null) {
                        tempList = new ArrayList<>();
                        friendNewOfflineChats.put(msgDTO.getFriendIdentity(), tempList);
                    }
                    tempList.add(msgDTO);
                }
            }
            new InsertIntoFriendMessageTable(messageDTOs).start();

            long systemTime = System.currentTimeMillis();
            for (Entry<String, MessageBaseDTO> entry : latestChatByFriendIdentity.entrySet()) {
                MessageBaseDTO msgBaseDTO = entry.getValue();
                MessageDTO mDto = messageMap.get(msgBaseDTO.getPacketID());
                MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(entry.getKey());

                if (mDto != null
                        && mDto.getStatus() == ChatConstants.CHAT_FRIEND_DELIVERED
                        && (mDto.getPacketType() == ChatConstants.CHAT_FRIEND || mDto.getPacketType() == ChatConstants.CHAT_FRIEND_BROCKEN_MESSAGE)
                        && !(friendChatCallPanel != null && friendChatCallPanel.isDisplayable() && friendChatCallPanel.isVisible())) {
                    RecentDTO recentDTO = new RecentDTO();
                    recentDTO.setTime(mDto.getMessageDate());
                    recentDTO.setContactId(mDto.getFriendIdentity());
                    recentDTO.setContactType(RecentContactDAO.TYPE_FRIEND);
                    recentDTO.setType(RecentChatCallActivityDAO.TYPE_FRIEND_CHAT);
                    recentDTO.setMessageDTO(mDto);

                    RecentDTO prevRecentDTO = ChatHashMap.getInstance().getChatUnreadMessages().get(recentDTO.getContactId());
                    if (prevRecentDTO == null) {
                        ChatHashMap.getInstance().getChatUnreadMessages().put(recentDTO.getContactId(), recentDTO);
                        addChatNotification(recentDTO.getContactId());
                    } else if (recentDTO.getTime() >= prevRecentDTO.getTime()) {
                        ChatHashMap.getInstance().getChatUnreadMessages().put(recentDTO.getContactId(), recentDTO);
                        addChatNotification(recentDTO.getContactId());
                    }
                }

                if ((systemTime - msgBaseDTO.getMessageDate()) <= ChatConstants.REGISTRATION_HOLD_DURATION) {
                    new ChatFriendStartResender(msgBaseDTO.getFriendIdentity()).start();
                }
            }

            loadRecentContactList();

            for (Entry<String, List<MessageDTO>> entrySet : friendNewOfflineChats.entrySet()) {
                String friendIdentity = entrySet.getKey();
                List<MessageDTO> messageList = entrySet.getValue();

                MyFriendChatCallPanel chatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(friendIdentity);
                if (chatCallPanel != null && chatCallPanel.myFriendChatPanel != null) {
                    Map<String, SingleChatPanel> chatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(friendIdentity);

                    for (MessageDTO messageDTO : messageList) {
                        if (messageDTO.getPacketType() == ChatConstants.CHAT_FRIEND_CHAT_EDIT || messageDTO.getPacketType() == ChatConstants.CHAT_FRIEND_CHAT_EDIT_BROCKEN_MESSAGE) {
                            SingleChatPanel chatPanel = chatPanelMap != null ? chatPanelMap.get(messageDTO.getPacketID()) : null;
                            if (chatPanel != null) {
                                chatPanel.messageDTO = messageDTO;
                                chatPanel.setMainPanel();
                            } else {
                                chatCallPanel.myFriendChatPanel.addSinglePanelInChatContainerPanel(messageDTO);
                            }
                        } else if (messageDTO.getMessageType() == ChatConstants.TYPE_DELETE_MSG) {
                            SingleChatPanel chatPanel = chatPanelMap != null ? chatPanelMap.get(messageDTO.getPacketID()) : null;
                            if (chatPanel != null) {
                                chatCallPanel.myFriendChatPanel.removeSingleChatPanel(chatPanel);
                            }
                        } else {
                            chatCallPanel.myFriendChatPanel.addSinglePanelInChatContainerPanel(messageDTO);
                        }
                    }
                }
            }
        } catch (Exception e) {
          //  e.printStackTrace();
        log.error("Error in here ==>" + e.getMessage());
        }
    }

    @Override
    public void onGroupChat(MessageBaseDTO messageBaseDTO) {
        try {
            MessageDTO messageDTO = getMessageDTO(messageBaseDTO);
            GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(messageDTO.getGroupId());
            if (!(groupPanel != null && groupPanel.isDisplayable())) {
                try {
                    if (playAudioHelper.chat_tune != null) {
                        playAudioHelper.chat_tune.stop();
                    }
                    if (playAudioHelper.chat_tune != null) {
                        playAudioHelper.chat_tune.play();
                    }
                } catch (Exception e) {
                }
            }

            List<MessageDTO> mList = new ArrayList<MessageDTO>();
            mList.add(messageDTO);
            new InsertIntoGroupMessageTable(mList).start();

            RecentDTO recentDTO = new RecentDTO();
            recentDTO.setTime(messageDTO.getMessageDate());
            recentDTO.setContactId(messageDTO.getGroupId() + "");
            recentDTO.setContactType(RecentContactDAO.TYPE_GROUP);
            recentDTO.setType(RecentChatCallActivityDAO.TYPE_GROUP_CHAT);
            recentDTO.setMessageDTO(messageDTO);

            if (groupPanel != null && groupPanel.isDisplayable()) {
                groupPanel.addSinglePanelInChatContainerPanel(messageDTO);
            } else {
                RecentDTO prevRecentDTO = ChatHashMap.getInstance().getChatUnreadMessages().get(recentDTO.getContactId());
                if (prevRecentDTO == null) {
                    ChatHashMap.getInstance().getChatUnreadMessages().put(recentDTO.getContactId(), recentDTO);
                    addChatNotification(recentDTO.getContactId());
                } else {
                    if (recentDTO.getTime() >= prevRecentDTO.getTime()) {
                        ChatHashMap.getInstance().getChatUnreadMessages().put(recentDTO.getContactId(), recentDTO);
                        addChatNotification(recentDTO.getContactId());
                    } else {
                        recentDTO = prevRecentDTO;
                    }
                }
            }

            //RecentHistoryContainer.changeMessageLabel(recentDTO);
            ChatHistoryContainer.changeStatus(recentDTO);
        } catch (Exception e) {
           // e.printStackTrace();
         log.error("Error in here ==>" + e.getMessage());
        }
    }

    @Override
    public void onGroupChatDelivered(MessageBaseDTO messageDTO) {
        try {
            Map<String, SingleChatPanel> chatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getGroupId() + "");
            SingleChatPanel chatPanel = chatPanelMap != null ? chatPanelMap.get(messageDTO.getPacketID()) : null;

            MessageDTO groupDeliveredMessage = ChatHashMap.getInstance().getChatStatusMessage().get(messageDTO.getPacketID());
            if (groupDeliveredMessage == null) {
                groupDeliveredMessage = GetSingleMessageDAO.getMessageDtoByPacketId(null, messageDTO.getGroupId(), messageDTO.getPacketID());
            }

            boolean isAlreadyDelieveredOrSeen = groupDeliveredMessage != null
                    && (groupDeliveredMessage.getStatus() == ChatConstants.CHAT_GROUP_DELIVERED
                    || groupDeliveredMessage.getStatus() == ChatConstants.CHAT_GROUP_SEEN);

            if (chatPanel != null && isAlreadyDelieveredOrSeen == false) {
                chatPanel.statusLabel.setText(ChatHelpers.STATUS_TXT_DELIVERED);
                chatPanel.statusLabel.revalidate();
            }

            if (groupDeliveredMessage != null && isAlreadyDelieveredOrSeen == false) {
                groupDeliveredMessage.setStatus(ChatConstants.CHAT_GROUP_DELIVERED);
                List<MessageDTO> mList = new ArrayList<MessageDTO>();
                mList.add(groupDeliveredMessage);
                new InsertIntoGroupMessageTable(mList).start();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onGroupChatSeen(MessageBaseDTO messageDTO) {
        try {
            Map<String, SingleChatPanel> chatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getGroupId() + "");
            SingleChatPanel chatPanel = chatPanelMap != null ? chatPanelMap.get(messageDTO.getPacketID()) : null;

            MessageDTO groupSeenMessage = ChatHashMap.getInstance().getChatStatusMessage().get(messageDTO.getPacketID());
            if (groupSeenMessage == null) {
                groupSeenMessage = GetSingleMessageDAO.getMessageDtoByPacketId(null, messageDTO.getGroupId(), messageDTO.getPacketID());
            }

            boolean isAlreadySeen = groupSeenMessage != null && groupSeenMessage.getStatus() == ChatConstants.CHAT_GROUP_SEEN;

            if (chatPanel != null && isAlreadySeen == false) {
                chatPanel.statusLabel.setText(ChatHelpers.STATUS_TXT_SEEN);
                chatPanel.statusLabel.revalidate();
            }

            if (groupSeenMessage != null && isAlreadySeen == false) {
                groupSeenMessage.setStatus(ChatConstants.CHAT_GROUP_SEEN);
                List<MessageDTO> mList = new ArrayList<MessageDTO>();
                mList.add(groupSeenMessage);
                new InsertIntoGroupMessageTable(mList).start();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onGroupChatSent(MessageBaseDTO messageDTO) {
        try {
            Map<String, SingleChatPanel> chatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getGroupId() + "");
            SingleChatPanel chatPanel = chatPanelMap != null ? chatPanelMap.get(messageDTO.getPacketID()) : null;

            MessageDTO groupSentMessage = ChatHashMap.getInstance().getChatStatusMessage().get(messageDTO.getPacketID());
            if (groupSentMessage == null) {
                groupSentMessage = GetSingleMessageDAO.getMessageDtoByPacketId(null, messageDTO.getGroupId(), messageDTO.getPacketID());
            }

            if (chatPanel != null) {
                chatPanel.statusLabel.setText(ChatHelpers.STATUS_TXT_SENT);
                chatPanel.statusLabel.revalidate();
            }

            if (groupSentMessage != null) {
                groupSentMessage.setStatus(ChatConstants.CHAT_GROUP_SENT);
                List<MessageDTO> mList = new ArrayList<MessageDTO>();
                mList.add(groupSentMessage);
                new InsertIntoGroupMessageTable(mList).start();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onGroupChatTyping(MessageBaseDTO messageDTO) {
        try {
            GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(messageDTO.getGroupId());
            if (groupPanel != null && groupPanel.isDisplayable()) {
                UserBasicInfo basicInfo = FriendList.getInstance().getFriend_hash_map().get(messageDTO.getUserIdentity());
                String fullName = "";

                if (basicInfo != null && basicInfo.getFullName() != null) {
                    fullName = basicInfo.getFullName();
                } else {
                    fullName = messageDTO.getUserIdentity();
                }
                groupPanel.addTyping(fullName);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onGroupChatEdit(MessageBaseDTO messageDTO) {
        try {
            MessageDTO groupEditMessage = ChatHashMap.getInstance().getChatStatusMessage().get(messageDTO.getPacketID());
            if (groupEditMessage == null) {
                groupEditMessage = GetSingleMessageDAO.getMessageDtoByPacketId(messageDTO.getUserIdentity(), null, messageDTO.getPacketID());
            }

            if (groupEditMessage != null) {
                groupEditMessage.setPacketType(ChatConstants.CHAT_GROUP_CHAT_EDIT);
                groupEditMessage.setMessage(messageDTO.getMessage());
                groupEditMessage.setMessageType(messageDTO.getMessageType());
                groupEditMessage.setStatus(ChatConstants.CHAT_GROUP_DELIVERED);
            } else {
                groupEditMessage = getMessageDTO(messageDTO);
                groupEditMessage.setStatus(ChatConstants.CHAT_GROUP_DELIVERED);
            }

            List<MessageDTO> mList = new ArrayList<MessageDTO>();
            mList.add(groupEditMessage);
            new InsertIntoGroupMessageTable(mList).start();

            Map<String, SingleChatPanel> chatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getGroupId() + "");
            SingleChatPanel chatPanel = chatPanelMap != null ? chatPanelMap.get(messageDTO.getPacketID()) : null;
            if (chatPanel != null) {
                chatPanel.messageDTO = groupEditMessage;
                chatPanel.setMainPanel();
            }
        } catch (Exception e) {
          //  e.printStackTrace();
         log.error("Error in onGroupChatEdit ==>" + e.getMessage());
        }
    }

    @Override
    public void onGroupChatDelete(MessageBaseDTO messageDTO) {
        try {
            GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(messageDTO.getGroupId());
            Map<String, SingleChatPanel> chatPanels = ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getGroupId() + "");
            SingleChatPanel chatPanel = chatPanels != null ? chatPanels.get(messageDTO.getPacketID()) : null;

            if (groupPanel != null && chatPanel != null) {
                groupPanel.removeSingleChatPanel(chatPanel);
            }

            if (chatPanels != null && chatPanel != null) {
                chatPanels.remove(messageDTO.getPacketID());
            }

            MessageDTO msDTO = GetSingleMessageDAO.getMessageDtoByPacketId(null, messageDTO.getGroupId(), messageDTO.getPacketID());
            if (msDTO != null) {
                List<MessageDTO> list = new ArrayList<MessageDTO>();
                msDTO.setStatus(ChatConstants.CHAT_GROUP_MESSAGE_DELETED);
                list.add(msDTO);
                new InsertIntoGroupMessageTable(list).start();
            }

            ChatHashMap.getInstance().getChatStatusMessage().remove(messageDTO.getPacketID());
        } catch (Exception e) {
        }
    }

    @Override
    public void onGroupChatMultiDelete(MessageBaseDTO messageDTO) {
        try {
            if (messageDTO.getMessageList() != null) {
                GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(messageDTO.getGroupId());
                Map<String, SingleChatPanel> chatPanels = ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getGroupId() + "");

                for (MessageBaseDTO msgDTO : messageDTO.getMessageList()) {
                    SingleChatPanel chatPanel = chatPanels != null ? chatPanels.get(msgDTO.getPacketID()) : null;
                    if (groupPanel != null && chatPanel != null) {
                        groupPanel.removeSingleChatPanel(chatPanel);
                    }

                    if (chatPanels != null && chatPanel != null) {
                        chatPanels.remove(msgDTO.getPacketID());
                    }

                    MessageDTO mDTO = GetSingleMessageDAO.getMessageDtoByPacketId(null, messageDTO.getGroupId(), msgDTO.getPacketID());
                    if (mDTO != null) {
                        List<MessageDTO> list = new ArrayList<MessageDTO>();
                        mDTO.setStatus(ChatConstants.CHAT_GROUP_MESSAGE_DELETED);
                        list.add(mDTO);
                        new InsertIntoGroupMessageTable(list).start();
                    }

                    ChatHashMap.getInstance().getChatStatusMessage().remove(msgDTO.getPacketID());
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onGroupChatBrockenMessage(MessageBaseDTO messageBaseDTO) {
        try {
            MessageDTO messageDTO = BrokenChatDAO.getMergedBorkenChat(messageBaseDTO);
            if (messageDTO != null) {
                onGroupChat(messageDTO);
            }
        } catch (Exception ex) {
          //  ex.printStackTrace();
        log.error("Error in onGroupChatBrockenMessage ==>" + ex.getMessage());
        }
    }

    @Override
    public void onGroupChatEditBrockenMessage(MessageBaseDTO messageBaseDTO) {
        try {
            MessageDTO messageDTO = BrokenChatDAO.getMergedBorkenChat(messageBaseDTO);
            if (messageDTO != null) {
                onGroupChatEdit(messageDTO);
            }
        } catch (Exception ex) {
           // ex.printStackTrace();
         log.error("Error in onGroupChatEditBrockenMessage ==>" + ex.getMessage());
        }
    }

    @Override
    public void onGroupFileStream(List<MessageBaseDTO> messageDTOs) {
        try {
            MessageBaseDTO firstDTO = messageDTOs.get(0);
            String rootDirectory = ChatHelpers.getRootDirectoryOfChunkFile(firstDTO.getPacketID());
            makeDirectory(rootDirectory);
            makeChunkFiles(firstDTO, messageDTOs, rootDirectory);
            File[] files = ChatHelpers.getChunkFiles(firstDTO.getPacketID());

            Map<String, SingleChatPanel> chatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(firstDTO.getGroupId() + "");
            SingleChatPanel chatPanel = chatPanelMap != null ? chatPanelMap.get(firstDTO.getPacketID()) : null;

            if (chatPanel != null && chatPanel.chatFileMessagePanel != null) {
                if (files.length == firstDTO.getFileTotalChunk()) {
                    chatPanel.chatFileMessagePanel.onSuccess();
                } else {
                    chatPanel.chatFileMessagePanel.onProgress((files.length * 100) / firstDTO.getFileTotalChunk());
                }
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
        log.error("Error in onGroupFileStream ==>" + ex.getMessage());
        }
    }

    @Override
    public void onGroupFileStreamRequest(MessageBaseDTO messageDTO) {
        try {
            File[] files = ChatHelpers.getChunkFilesByIndex(messageDTO.getPacketID(), messageDTO.getMessageList());
            ChatService.sendGroupFileStream(messageDTO.getGroupId(), messageDTO.getFriendIdentity(), messageDTO.getPacketID(), files, messageDTO.getFileTotalChunk());
        } catch (Exception ex) {
          //  ex.printStackTrace();
          log.error("Error in onGroupFileStreamRequest ==>" + ex.getMessage());
        }
    }

    @Override
    public void onGroupChatOfflineFetch(List<MessageBaseDTO> messageBaseDTOs, Map<Long, MessageBaseDTO> latestChatByGroupId) {
        try {
            Map<Long, List<MessageDTO>> groupNewOfflineChats = new ConcurrentHashMap<>();
            List<MessageDTO> messageDTOs = new ArrayList<MessageDTO>();
            Map<String, MessageDTO> messageMap = new ConcurrentHashMap<String, MessageDTO>();

            for (MessageBaseDTO entity : messageBaseDTOs) {
                if (entity.getPacketType() == ChatConstants.CHAT_GROUP_BROCKEN_MESSAGE
                        || entity.getPacketType() == ChatConstants.CHAT_GROUP_CHAT_EDIT_BROCKEN_MESSAGE) {
                    MessageDTO msgDTO = BrokenChatDAO.getMergedBorkenChat(entity);
                    if (msgDTO != null) {
                        messageDTOs.add(msgDTO);
                        messageMap.put(msgDTO.getPacketID(), msgDTO);

                        List<MessageDTO> tempList = groupNewOfflineChats.get(msgDTO.getGroupId());
                        if (tempList == null) {
                            tempList = new ArrayList<>();
                            groupNewOfflineChats.put(msgDTO.getGroupId(), tempList);
                        }
                        tempList.add(msgDTO);
                    }
                } else {
                    MessageDTO msgDTO = getMessageDTO(entity);
                    messageDTOs.add(msgDTO);
                    messageMap.put(msgDTO.getPacketID(), msgDTO);

                    List<MessageDTO> tempList = groupNewOfflineChats.get(msgDTO.getGroupId());
                    if (tempList == null) {
                        tempList = new ArrayList<>();
                        groupNewOfflineChats.put(msgDTO.getGroupId(), tempList);
                    }
                    tempList.add(msgDTO);
                }
            }
            new InsertIntoGroupMessageTable(messageDTOs).start();

            long systemTime = System.currentTimeMillis();
            final Set<Long> groupIdSet = latestChatByGroupId.keySet();
            List<RecentDTO> tempRecentList = new ArrayList<RecentDTO>();

            for (Entry<Long, MessageBaseDTO> entry : latestChatByGroupId.entrySet()) {
                MessageBaseDTO msgBaseDTO = entry.getValue();
                MessageDTO mDto = messageMap.get(msgBaseDTO.getPacketID());
                GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(entry.getKey());

                if (mDto != null
                        && mDto.getStatus() == ChatConstants.CHAT_GROUP_DELIVERED
                        && (mDto.getPacketType() == ChatConstants.CHAT_GROUP || mDto.getPacketType() == ChatConstants.CHAT_GROUP_BROCKEN_MESSAGE)
                        && !(groupPanel != null && groupPanel.isDisplayable())) {
                    RecentDTO recentDTO = new RecentDTO();
                    recentDTO.setTime(mDto.getMessageDate());
                    recentDTO.setContactId(mDto.getGroupId() + "");
                    recentDTO.setContactType(RecentContactDAO.TYPE_GROUP);
                    recentDTO.setType(RecentChatCallActivityDAO.TYPE_GROUP_CHAT);
                    recentDTO.setMessageDTO(mDto);

                    RecentDTO prevRecentDTO = ChatHashMap.getInstance().getChatUnreadMessages().get(recentDTO.getContactId());
                    MessageDTO tempMsg = GetSingleMessageDAO.getMessageDtoByPacketId(null, mDto.getGroupId(), mDto.getPacketID());
                    if ((prevRecentDTO == null || recentDTO.getTime() >= prevRecentDTO.getTime()) && tempMsg == null) {
                        tempRecentList.add(recentDTO);

                    }
                }

                if ((systemTime - msgBaseDTO.getMessageDate()) <= ChatConstants.REGISTRATION_HOLD_DURATION) {
                    if (!MyfnfHashMaps.getInstance().getGroup_hash_map().containsKey(msgBaseDTO.getGroupId())) {
                        new ChatGroupStartResender(msgBaseDTO.getGroupId(), false, new OnGroupResponseListener() {
                            @Override
                            public void onResponse(Long groupId) {
                                groupIdSet.remove(groupId);
                                ChatHistoryContainer.changeFullName(groupId + "");

                            }
                        }).start();
                    } else {
                        new ChatGroupStartResender(msgBaseDTO.getGroupId(), false).start();
                        groupIdSet.remove(msgBaseDTO.getGroupId());
                    }
                } else if (!MyfnfHashMaps.getInstance().getGroup_hash_map().containsKey(msgBaseDTO.getGroupId())) {
                    new GetGroupDetails(msgBaseDTO.getGroupId(), true, new OnGroupResponseListener() {
                        @Override
                        public void onResponse(Long groupId) {
                            groupIdSet.remove(groupId);
                            ChatHistoryContainer.changeFullName(groupId + "");

                        }
                    }).start();
                } else {
                    groupIdSet.remove(msgBaseDTO.getGroupId());
                }
            }

            for (int i = 500; i < 7000 && groupIdSet.size() > 0; i += 500) {
                Thread.sleep(500);
            }

            for (RecentDTO recentDTO : tempRecentList) {
                ChatHashMap.getInstance().getChatUnreadMessages().put(recentDTO.getContactId(), recentDTO);
                addChatNotification(recentDTO.getContactId());
            }

            loadRecentContactList();

            for (Entry<Long, List<MessageDTO>> entrySet : groupNewOfflineChats.entrySet()) {
                Long groupId = entrySet.getKey();
                List<MessageDTO> messageList = entrySet.getValue();

                GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(groupId);
                if (groupPanel != null) {
                    Map<String, SingleChatPanel> chatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(groupId + "");

                    for (MessageDTO messageDTO : messageList) {
                        if (messageDTO.getPacketType() == ChatConstants.CHAT_GROUP_CHAT_EDIT || messageDTO.getPacketType() == ChatConstants.CHAT_GROUP_CHAT_EDIT_BROCKEN_MESSAGE) {
                            SingleChatPanel chatPanel = chatPanelMap != null ? chatPanelMap.get(messageDTO.getPacketID()) : null;
                            if (chatPanel != null) {
                                chatPanel.messageDTO = messageDTO;
                                chatPanel.setMainPanel();
                            } else {
                                groupPanel.addSinglePanelInChatContainerPanel(messageDTO);
                            }
                        } else if (messageDTO.getMessageType() == ChatConstants.TYPE_DELETE_MSG) {
                            SingleChatPanel chatPanel = chatPanelMap != null ? chatPanelMap.get(messageDTO.getPacketID()) : null;
                            if (chatPanel != null) {
                                groupPanel.removeSingleChatPanel(chatPanel);
                            }
                        } else {
                            groupPanel.addSinglePanelInChatContainerPanel(messageDTO);
                        }
                    }
                }
            }

        } catch (Exception e) {
           // e.printStackTrace();
        log.error("Error in here ==>" + e.getMessage());
        }
    }

    @Override
    public void onFriendChatUnregister(MessageBaseDTO messageDTO) {
        try {
            UserBasicInfo friendInfo = FriendList.getInstance().getFriend_hash_map().get(messageDTO.getFriendIdentity());
            if (friendInfo != null && friendInfo.getPresence() != messageDTO.getPresence()) {
                System.err.println("onFriendChatUnregister(MessageBaseDTO messageDTO)");
                if (friendInfo.getPresence() == StatusConstants.PRESENCE_ONLINE) {
                    friendInfo.setLastOnlineTime(System.currentTimeMillis());
                }
                friendInfo.setPresence(messageDTO.getPresence());

                MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(messageDTO.getFriendIdentity());
                if (friendChatCallPanel != null) {
                    friendChatCallPanel.setFriendProfileInfo(friendInfo);
                    friendChatCallPanel.refreshMyFriendChatCallPanelStatus();
                }

                MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(messageDTO.getFriendIdentity());
                if (friendProfile != null) {
                    friendProfile.setFriendProfileInfo(friendInfo);
                    friendProfile.buildStatusImage();
                }
            }

        } catch (Exception ex) {
           // ex.printStackTrace();
        log.error("Error in onFriendChatUnregister ==>" + ex.getMessage());
        }
    }

    @Override
    public void onGroupChatUnregister(MessageBaseDTO messageDTO) {
        try {

        } catch (Exception ex) {
            //ex.printStackTrace();
        log.error("Error in onGroupChatUnregister ==>" + ex.getMessage());
        }
    }

    private void loadRecentContactList() {
        if (GuiRingID.getInstance() != null
                && GuiRingID.getInstance().getMainLeftContainer() != null
                && GuiRingID.getInstance().getMainLeftContainer().getChatHistoryContainer() != null) {
            GuiRingID.getInstance().getMainLeftContainer().getChatHistoryContainer().loadRecentChatHistory();
        }
    }

    private void addChatNotification(String contactId) {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainButtonsPanel() != null) {
            GuiRingID.getInstance().getMainButtonsPanel().addChatNotification(contactId);
        }
    }

    private void makeDirectory(String rootDirectory) {
        try {
            File dir = new File(rootDirectory);
            if (!(dir.exists() && dir.isDirectory())) {
                dir.mkdirs();
            }
        } catch (Exception ex) {
        }
    }

    private void makeChunkFiles(MessageBaseDTO firstDTO, List<MessageBaseDTO> messageDTOs, String rootDirectory) {
        try {
            String fileName = rootDirectory + File.separator + firstDTO.getPacketID() + "." + firstDTO.getFileChunkIndex();
            File file = new File(fileName);
            if (!file.exists()) {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                for (MessageBaseDTO messageDTO : messageDTOs) {
                    out.write(messageDTO.getFileInByte());
                }
                out.close();
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
        log.error("Error in makeChunkFiles ==>" + ex.getMessage());
        }
    }

}
