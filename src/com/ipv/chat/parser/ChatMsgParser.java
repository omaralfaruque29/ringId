/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.ipv.chat.dto.MessageBaseDTO;
import com.ipv.chat.storers.ChatStorer;
import com.ipv.chat.ChatConstants;
import com.ipv.chat.ChatSettings;
import com.ipv.chat.utils.ChatPacketUility;
import com.ipv.chat.communicator.ChatSender;
import com.ipv.chat.dto.ChatRegistrationDTO;
import com.ipv.chat.listener.ChatActionListener;
import com.ipv.chat.service.ChatService;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatMsgParser extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatMsgParser.class);
    private int packetType;
    private byte[] recievedBuffer;
    private ChatActionListener chatActionListener;
    private String format = "hh:mm aaa";

    public ChatMsgParser(byte[] recievedBuffer, ChatActionListener chatActionListener) {
        this.recievedBuffer = recievedBuffer;
        this.chatActionListener = chatActionListener;
    }

    @Override
    public void run() {
        try {
            proccesChatResponse(recievedBuffer);
        } catch (Exception e) {
            //   e.printStackTrace();
            log.error("Error in ChatMsgParser class in proccessingChatResponse==>" + e.getMessage());
        }
    }

    private void storePacketId(MessageBaseDTO messageDTO) {
        if (ChatStorer.PACKET_CONTAINER.size() > 500) {
            ChatStorer.PACKET_CONTAINER.clear();
        }
        ChatStorer.PACKET_CONTAINER.put(messageDTO.getPacketID(), messageDTO);
    }

    private void proccesChatResponse(byte[] recievedBuffer) {

        packetType = recievedBuffer[0];

        switch (packetType) {
            case ChatConstants.CHAT_FRIEND_UNREGISTER:                          //2
                processChatFriendUnregister();
                break;
            case ChatConstants.CHAT_FRIEND_REGISTER_CONFIRMATION:               //3
                processChatFriendRegisterConfirmation();
                break;
            case ChatConstants.CHAT_GROUP_UNREGISTER:                           //4
                processChatGroupUnregister();
                break;
            case ChatConstants.CHAT_FRIEND:                                     //5
                processChatFriend();
                break;
            case ChatConstants.CHAT_FRIEND_DELIVERED:                           //6
                processChatFriendDelivered();
                break;
            case ChatConstants.CHAT_FRIEND_SEEN:                                //7
                processChatFriendSeen();
                break;
            case ChatConstants.CHAT_FRIEND_SENT:                                //8
                processChatFriendSent();
                break;
            case ChatConstants.CHAT_FRIEND_TYPING:                              //9
                processChatFriendTyping();
                break;
            case ChatConstants.CHAT_FRIEND_IDEL:                                //10
                processChatFriendIdel();
                break;
            case ChatConstants.CHAT_GROUP_REGISTER_CONFIRMATION:                //13
                processChatGroupRegisterConfirmation();
                break;
            case ChatConstants.CHAT_GROUP:                                      //14
                processChatGroup();
                break;
            case ChatConstants.CHAT_GROUP_DELIVERED:                            //15
                processChatGroupDelivered();
                break;
            case ChatConstants.CHAT_GROUP_SEEN:                                 //16
                processChatGroupSeen();
                break;
            case ChatConstants.CHAT_GROUP_SENT:                                 //17
                processChatGroupSent();
                break;
            case ChatConstants.CHAT_GROUP_TYPING:                               //18
                processChatGroupTyping();
                break;
            case ChatConstants.CHAT_GROUP_IDEL:                                 //19
                processChatGroupIdel();
                break;
            case ChatConstants.CHAT_FRIEND_OFFLINE:                             //20
                processChatFriendOffline();
                break;
            case ChatConstants.CHAT_GROUP_OFFLINE:                              //21
                processChatGroupOffline();
                break;
            case ChatConstants.CHAT_OFFLINE_REQUEST_CONFIRMATION:               //23
                processChatOfflineRequestConfirmation();
                break;
            case ChatConstants.CHAT_FRIEND_MESSAGE_DELETED:                     //24
                processChatFriendMessageDeleted();
                break;
            case ChatConstants.CHAT_FRIEND_MESSAGE_DELETE_CONFIRMATION:         //25
                processChatFriendMessageDeletedConfirmation();
                break;
            case ChatConstants.CHAT_GROUP_MESSAGE_DELETED:                      //26
                processChatGroupMessageDeleted();
                break;
            case ChatConstants.CHAT_GROUP_MESSAGE_DELETE_CONFIRMATION:          //27
                processChatGroupMessageDeletedConfirmation();
                break;
            case ChatConstants.CHAT_FRIEND_INFORMATION_CONFIRMATION:            //29
                processChatFriendInformationConfirmation();
                break;

            case ChatConstants.CHAT_GROUP_MEMBERS_SEND_CONFIRMATION:            //35
                processChatGroupMembersSendConfirmation();
                break;
            case ChatConstants.CHAT_GROUP_MEMBER_REMOVE_OR_LEAVE_CONFIRMATION:  //38
                processChatGroupMemberRemoveOrLeaveConfirmation();
                break;
            case ChatConstants.CHAT_GROUP_DELETE_CONFIRMATION:                  //40
                processChatGroupDeleteConfirmation();
                break;
            case ChatConstants.CHAT_FRIEND_MULTIPLE_MESSAGE_DELETE:             //41
                processChatFriendMultipleMessageDeleted();
                break;
            case ChatConstants.CHAT_GROUP_MULTIPLE_MESSAGE_DELETE:              //42
                processChatGroupMultipleMessageDeleted();
                break;
            case ChatConstants.CHAT_FRIEND_CHAT_EDIT:                           //43
                processChatFriendChatEdit();
                break;
            case ChatConstants.CHAT_GROUP_CHAT_EDIT:                            //44
                processChatGroupChatEdit();
                break;
            case ChatConstants.CHAT_FRIEND_MULTIPLE_MESSAGE:                    //45
                processChatFriendMultipleMessage();
                break;
            case ChatConstants.CHAT_FRIEND_BROCKEN_MESSAGE:                     //46
                processChatFriendBrockenMessage();
                break;
            case ChatConstants.CHAT_GROUP_BROCKEN_MESSAGE:                      //47
                processChatGroupBrockenMessage();
                break;
            case ChatConstants.CHAT_FRIEND_CHAT_EDIT_BROCKEN_MESSAGE:           //48
                processChatFriendChatEditBrockenMessage();
                break;
            case ChatConstants.CHAT_GROUP_CHAT_EDIT_BROCKEN_MESSAGE:            //49
                processChatGroupChatEditBrockenMessage();
                break;
            case ChatConstants.CHAT_FRIEND_FILE_STREAM_REQUEST:                 //50
                processChatFriendFileStreamRequest();
                break;
            case ChatConstants.CHAT_FRIEND_FILE_STREAM_REQUEST_CONFIRMATION:    //51
                processChatFriendFileStreamRequestConfirmation();
                break;
            case ChatConstants.CHAT_FRIEND_FILE_STREAM_SEND:                    //52
                processChatFriendFileStream();
                break;
            case ChatConstants.CHAT_FRIEND_FILE_STREAM_SEND_CONFIRMATION:       //53
                processChatFriendFileStreamConfirmation();
                break;
            case ChatConstants.CHAT_GROUP_FILE_STREAM_REQUEST:                 //54
                processChatGroupFileStreamRequest();
                break;
            case ChatConstants.CHAT_GROUP_FILE_STREAM_REQUEST_CONFIRMATION:    //55
                processChatGroupFileStreamRequestConfirmation();
                break;
            case ChatConstants.CHAT_GROUP_FILE_STREAM_SEND:                    //56
                processChatGroupFileStream();
                break;
            case ChatConstants.CHAT_GROUP_FILE_STREAM_SEND_CONFIRMATION:       //57
                processChatGroupFileStreamConfirmation();
                break;

        }

    }

    private void processChatFriend() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendMessagePacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();

            if (ChatStorer.PACKET_CONTAINER.get(packetID) == null) {
                this.storePacketId(messageDTO);
                messageDTO.setStatus(ChatConstants.CHAT_FRIEND_DELIVERED);
                messageDTO.setFriendIdentity(messageDTO.getUserIdentity());
                ChatService.sendFriendChatDeliveredConfirmation(messageDTO.getPacketID(), messageDTO.getFriendIdentity());
                chatActionListener.onFriendChat(messageDTO);
            }
        } catch (Exception ex) {
        }
    }

    private void processChatFriendDelivered() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendDeliveredORSeenPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            messageDTO.setPacketID(messageDTO.getPacketID().split(ChatConstants.BROKEN_PACKET_SEPARATOR)[0]);
            ChatStorer.PACKET_CONFIRMATION.put(packetID, messageDTO);

            if (ChatStorer.TEMP_MULTIPLE_PACKET.containsKey(packetID)) {
                Set<MessageBaseDTO> msgDTOs = ChatStorer.TEMP_MULTIPLE_PACKET.get(packetID);
                for (MessageBaseDTO msg : msgDTOs) {
                    msg.setUserIdentity(msg.getFriendIdentity());
                    chatActionListener.onFriendChatDelivered(msg);
                }
                ChatStorer.TEMP_MULTIPLE_PACKET.remove(packetID);
            } else {
                chatActionListener.onFriendChatDelivered(messageDTO);
            }
        } catch (Exception ex) {
        }
    }

    private void processChatFriendSeen() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendDeliveredORSeenPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            messageDTO.setPacketID(messageDTO.getPacketID().split(ChatConstants.BROKEN_PACKET_SEPARATOR)[0]);
            ChatStorer.PACKET_CONFIRMATION.put(packetID, messageDTO);

            chatActionListener.onFriendChatSeen(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatFriendSent() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendSentConfirmationPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            messageDTO.setPacketID(messageDTO.getPacketID().split(ChatConstants.BROKEN_PACKET_SEPARATOR)[0]);
            ChatStorer.PACKET_CONFIRMATION.put(packetID, messageDTO);

            if (ChatStorer.TEMP_MULTIPLE_PACKET.containsKey(packetID)) {
                Set<MessageBaseDTO> msgDTOs = ChatStorer.TEMP_MULTIPLE_PACKET.get(packetID);
                for (MessageBaseDTO msg : msgDTOs) {
                    msg.setUserIdentity(msg.getFriendIdentity());
                    chatActionListener.onFriendChatSent(msg);
                }
                ChatStorer.TEMP_MULTIPLE_PACKET.remove(packetID);
            } else {
                chatActionListener.onFriendChatSent(messageDTO);
            }
        } catch (Exception ex) {
        }
    }

    private void processChatFriendTyping() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendTypingORIdelPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }

            chatActionListener.onFriendChatTyping(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatFriendIdel() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendTypingORIdelPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }

            chatActionListener.onFriendChatIdle(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatGroup() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupMessagePacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();

            if (ChatStorer.PACKET_CONTAINER.get(packetID) == null) {
                this.storePacketId(messageDTO);
                messageDTO.setStatus(ChatConstants.CHAT_GROUP_DELIVERED);
                ChatService.sendGroupChatDeliveredConfirmation(messageDTO.getPacketID(), messageDTO.getGroupId(), messageDTO.getUserIdentity());
                if (messageDTO.getMessageType() != ChatConstants.TYPE_BLANK_MSG) {
                    chatActionListener.onGroupChat(messageDTO);
                }
            }
        } catch (Exception ex) {
        }
    }

    private void processChatGroupDelivered() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupDeliveredORSeenPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            messageDTO.setPacketID(messageDTO.getPacketID().split(ChatConstants.BROKEN_PACKET_SEPARATOR)[0]);
            ChatStorer.PACKET_CONFIRMATION.put(packetID, messageDTO);

            chatActionListener.onGroupChatDelivered(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatGroupSeen() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupDeliveredORSeenPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            messageDTO.setPacketID(messageDTO.getPacketID().split(ChatConstants.BROKEN_PACKET_SEPARATOR)[0]);
            ChatStorer.PACKET_CONFIRMATION.put(packetID, messageDTO);

            messageDTO.setPacketID(packetID);
            chatActionListener.onGroupChatSeen(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatGroupSent() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupSentConfirmationPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }

            String packetID = messageDTO.getPacketID();
            messageDTO.setPacketID(messageDTO.getPacketID().split(ChatConstants.BROKEN_PACKET_SEPARATOR)[0]);
            ChatStorer.PACKET_CONFIRMATION.put(packetID, messageDTO);

            chatActionListener.onGroupChatSent(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatGroupTyping() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupTypingORIdelPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }

            chatActionListener.onGroupChatTyping(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatGroupIdel() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupTypingORIdelPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }

            chatActionListener.onGroupChatIdle(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatOfflineRequestConfirmation() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getOfflineConfrimationPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            ChatSettings.OFFLINE_SERVER_TIME_DIFF = messageDTO.getServerDate() - System.currentTimeMillis();
            ChatStorer.PACKET_CONFIRMATION.put(packetID, messageDTO);

            chatActionListener.onChatOfflineRequestConfirmation(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatFriendOffline() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendOfflineMessage(recievedBuffer);
            String packetID = messageDTO.getPacketID();

            byte[] confirmationByte = ChatPacketUility.makeConfirmationPacket(ChatConstants.CHAT_OFFLINE_REQUEST_CONFIRMATION, packetID);
            ChatSender.sendOfflineChatPacket(confirmationByte);
            ChatSender.sendOfflineChatPacket(confirmationByte);

            if (ChatStorer.PACKET_CONFIRMATION.get(packetID) == null) {
//                if (ChatSettings.DEBUG) {
//                    System.err.println("Chat Received >> " + messageDTO.toString());
//                }
                ChatStorer.PACKET_CONFIRMATION.put(packetID, messageDTO);

                List<MessageBaseDTO> tempList = new ArrayList<MessageBaseDTO>();
                List<MessageBaseDTO> mList = messageDTO.getMessageList();
                Map<String, MessageBaseDTO> latestChatByFriendIdentity = new ConcurrentHashMap<String, MessageBaseDTO>();

                for (int inc = 0; inc < mList.size(); inc++) {
                    MessageBaseDTO mDto = mList.get(inc);
                    if (ChatSettings.DEBUG) {
                        System.err.println("Chat Received (Friend Off.) >> " + inc + " >> " + mDto.toString());
                    }

                    if (mDto.getUserIdentity() != null && mDto.getUserIdentity().length() > 0) {
                        mDto.setFriendIdentity(mDto.getUserIdentity());

                        if (mDto.getMessageType() == ChatConstants.TYPE_DELETE_MSG) {
                            mDto.setStatus(ChatConstants.CHAT_FRIEND_MESSAGE_DELETED);
                            tempList.add(mDto);

                        } else if (mDto.getMessageType() == ChatConstants.TYPE_BLANK_MSG) {

                        } else {
                            mDto.setStatus(ChatConstants.CHAT_FRIEND_DELIVERED);
                            tempList.add(mDto);
                        }

                        if (latestChatByFriendIdentity.get(mDto.getFriendIdentity()) == null) {
                            latestChatByFriendIdentity.put(mDto.getFriendIdentity(), mDto);
                        } else if (mDto.getMessageDate() > latestChatByFriendIdentity.get(mDto.getFriendIdentity()).getMessageDate()) {
                            latestChatByFriendIdentity.put(mDto.getFriendIdentity(), mDto);
                        }
                    }
                }

                chatActionListener.onFriendChatOfflineFetch(tempList, latestChatByFriendIdentity);
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Error in ChatMsgParser class in processChatFriendOffline==>" + ex.getMessage());
        }
    }

    private void processChatGroupOffline() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupOfflineMessage(recievedBuffer);
            String packetID = messageDTO.getPacketID();

            byte[] confirmationByte = ChatPacketUility.makeConfirmationPacket(ChatConstants.CHAT_OFFLINE_REQUEST_CONFIRMATION, packetID);
            ChatSender.sendOfflineChatPacket(confirmationByte);
            ChatSender.sendOfflineChatPacket(confirmationByte);

            if (ChatStorer.PACKET_CONFIRMATION.get(packetID) == null) {
//                if (ChatSettings.DEBUG) {
//                    System.err.println("Chat Received >> " + messageDTO.toString());
//                }
                ChatStorer.PACKET_CONFIRMATION.put(packetID, messageDTO);

                List<MessageBaseDTO> tempList = new ArrayList<MessageBaseDTO>();
                List<MessageBaseDTO> mList = messageDTO.getMessageList();
                Map<Long, MessageBaseDTO> latestChatByGroupId = new ConcurrentHashMap<Long, MessageBaseDTO>();

                for (int inc = 0; inc < mList.size(); inc++) {
                    MessageBaseDTO mDto = mList.get(inc);
                    if (ChatSettings.DEBUG) {
                        System.err.println("Chat Received (Group Off.) >> " + inc + " >> " + mDto.toString());
                    }

                    if (mDto.getUserIdentity() != null && mDto.getUserIdentity().length() > 0) {

                        if (mDto.getMessageType() == ChatConstants.TYPE_DELETE_MSG) {
                            mDto.setStatus(ChatConstants.CHAT_GROUP_MESSAGE_DELETED);
                            tempList.add(mDto);

                        } else if (mDto.getMessageType() == ChatConstants.TYPE_BLANK_MSG) {

                        } else {
                            mDto.setStatus(ChatConstants.CHAT_GROUP_DELIVERED);
                            tempList.add(mDto);
                        }

                        if (latestChatByGroupId.get(mDto.getGroupId()) == null) {
                            latestChatByGroupId.put(mDto.getGroupId(), mDto);
                        } else if (mDto.getMessageDate() > latestChatByGroupId.get(mDto.getGroupId()).getMessageDate()) {
                            latestChatByGroupId.put(mDto.getGroupId(), mDto);
                        }
                    }
                }

                chatActionListener.onGroupChatOfflineFetch(tempList, latestChatByGroupId);
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Error in ChatMsgParser class in processChatGroupOffline==>" + ex.getMessage());
        }
    }

    private void processChatFriendRegisterConfirmation() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendRegisterConfirmationPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            ChatStorer.PACKET_CONFIRMATION.put(packetID, messageDTO);

            chatActionListener.onFriendChatRegisterConfirmation(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatGroupRegisterConfirmation() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupRegisterConfirmationPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            ChatStorer.PACKET_CONFIRMATION.put(packetID, messageDTO);

            chatActionListener.onGroupChatRegisterConfirmation(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatFriendMessageDeleted() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendMessageDeletePacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }

            ChatService.sendFriendChatDeleteConfirmation(messageDTO.getPacketID(), messageDTO.getUserIdentity());
            chatActionListener.onFriendChatDelete(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatGroupMessageDeleted() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupMessageDeletePacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }

            ChatService.sendGroupChatDeleteConfirmation(messageDTO.getPacketID(), messageDTO.getGroupId(), messageDTO.getUserIdentity());
            chatActionListener.onGroupChatDelete(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatFriendMessageDeletedConfirmation() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendMessageDeleteConfirmationPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            ChatStorer.PACKET_CONFIRMATION.put(packetID, messageDTO);

            chatActionListener.onFriendChatDeleteConfirmation(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatGroupMessageDeletedConfirmation() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupMessageDeleteConfirmationPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            ChatStorer.PACKET_CONFIRMATION.put(packetID, messageDTO);

            chatActionListener.onGroupChatDeleteConfirmation(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatFriendInformationConfirmation() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getConfrimationPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            ChatStorer.PACKET_CONFIRMATION.put(packetID, messageDTO);

            chatActionListener.onFriendInformationConfirmation(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatGroupMembersSendConfirmation() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupMemberListConfrimationPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            ChatStorer.PACKET_CONFIRMATION.put(packetID, messageDTO);

            chatActionListener.onGroupMembersSendConfirmation(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatGroupMemberRemoveOrLeaveConfirmation() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getConfrimationPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            ChatStorer.PACKET_CONFIRMATION.put(packetID, messageDTO);

            chatActionListener.onGroupMemberRemoveOrLeaveConfirmation(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatGroupDeleteConfirmation() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupDeleteConfirmationPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            ChatStorer.PACKET_CONFIRMATION.put(packetID, messageDTO);

            chatActionListener.onGroupDeleteConfirmation(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatFriendMultipleMessageDeleted() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendMultipleMessageDeletePacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }

            ChatService.sendFriendChatDeleteConfirmation(messageDTO.getPacketID(), messageDTO.getUserIdentity());
            if (messageDTO.getMessageList() != null) {
                chatActionListener.onFriendChatMultiDelete(messageDTO);
            }
        } catch (Exception ex) {
        }
    }

    private void processChatGroupMultipleMessageDeleted() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupMultipleMessageDeletePacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }

            ChatService.sendGroupChatDeleteConfirmation(messageDTO.getPacketID(), messageDTO.getGroupId(), messageDTO.getUserIdentity());
            if (messageDTO.getMessageList() != null) {
                chatActionListener.onGroupChatMultiDelete(messageDTO);
            }
        } catch (Exception ex) {
        }
    }

    private void processChatFriendChatEdit() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendMessagePacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }

            messageDTO.setFriendIdentity(messageDTO.getUserIdentity());
            ChatService.sendFriendChatDeliveredConfirmation(messageDTO.getPacketID(), messageDTO.getFriendIdentity());
            chatActionListener.onFriendChatEdit(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatGroupChatEdit() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupMessagePacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }

            ChatService.sendGroupChatDeliveredConfirmation(messageDTO.getPacketID(), messageDTO.getGroupId(), messageDTO.getUserIdentity());
            chatActionListener.onGroupChatEdit(messageDTO);
        } catch (Exception ex) {
        }
    }

    private void processChatFriendMultipleMessage() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendMultipleMessagePacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();

            if (ChatStorer.PACKET_CONTAINER.get(packetID) == null) {
                this.storePacketId(messageDTO);
                messageDTO.setFriendIdentity(messageDTO.getUserIdentity());
                ChatService.sendFriendChatDeliveredConfirmation(messageDTO.getPacketID(), messageDTO.getFriendIdentity());

                if (messageDTO.getMessageList() != null && messageDTO.getMessageList().size() > 0) {
                    for (MessageBaseDTO msgDTO : messageDTO.getMessageList()) {
                        msgDTO.setPacketType(ChatConstants.CHAT_FRIEND);
                        msgDTO.setFriendIdentity(messageDTO.getFriendIdentity());
                        msgDTO.setUserIdentity(messageDTO.getUserIdentity());
                        msgDTO.setStatus(ChatConstants.CHAT_FRIEND_DELIVERED);
                    }

                    Collections.sort(messageDTO.getMessageList(), new Comparator<MessageBaseDTO>() {
                        @Override
                        public int compare(MessageBaseDTO one, MessageBaseDTO other) {
                            Long onesDate = one.getMessageDate();
                            Long othersDate = other.getMessageDate();
                            return onesDate.compareTo(othersDate);
                        }
                    });

                    chatActionListener.onFriendChatMultipleMessage(messageDTO.getMessageList());
                }
            }
        } catch (Exception e) {
        }
    }

    private void processChatFriendBrockenMessage() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendBrokenMessagePacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            String brokenPacketID = packetID + ChatConstants.BROKEN_PACKET_SEPARATOR + messageDTO.getSequenceNumber();

            if (ChatStorer.PACKET_CONTAINER.get(brokenPacketID) == null) {
                this.storePacketId(messageDTO);
                messageDTO.setStatus(ChatConstants.CHAT_FRIEND_DELIVERED);
                messageDTO.setFriendIdentity(messageDTO.getUserIdentity());
                ChatService.sendFriendChatDeliveredConfirmation(brokenPacketID, messageDTO.getFriendIdentity());
                chatActionListener.onFriendChatBrockenMessage(messageDTO);
            }
        } catch (Exception e) {
        }
    }

    private void processChatGroupBrockenMessage() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupBrokenMessagePacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            String brokenPacketID = packetID + ChatConstants.BROKEN_PACKET_SEPARATOR + messageDTO.getSequenceNumber();

            if (ChatStorer.PACKET_CONTAINER.get(brokenPacketID) == null) {
                this.storePacketId(messageDTO);
                messageDTO.setStatus(ChatConstants.CHAT_GROUP_DELIVERED);
                ChatService.sendGroupChatDeliveredConfirmation(brokenPacketID, messageDTO.getGroupId(), messageDTO.getUserIdentity());
                chatActionListener.onGroupChatBrockenMessage(messageDTO);
            }
        } catch (Exception e) {
        }
    }

    private void processChatFriendChatEditBrockenMessage() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendBrokenMessagePacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String brokenPacketID = messageDTO.getPacketID() + ChatConstants.BROKEN_PACKET_SEPARATOR + messageDTO.getSequenceNumber();

            messageDTO.setFriendIdentity(messageDTO.getUserIdentity());
            ChatService.sendFriendChatDeliveredConfirmation(brokenPacketID, messageDTO.getFriendIdentity());
            chatActionListener.onFriendChatEditBrockenMessage(messageDTO);
        } catch (Exception e) {
        }
    }

    private void processChatGroupChatEditBrockenMessage() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupBrokenMessagePacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String brokenPacketID = messageDTO.getPacketID() + ChatConstants.BROKEN_PACKET_SEPARATOR + messageDTO.getSequenceNumber();

            ChatService.sendGroupChatDeliveredConfirmation(brokenPacketID, messageDTO.getGroupId(), messageDTO.getUserIdentity());
            chatActionListener.onGroupChatEditBrockenMessage(messageDTO);
        } catch (Exception e) {
        }
    }

    private void processChatFriendFileStream() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendFileStreamSendPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            messageDTO.setFriendIdentity(messageDTO.getUserIdentity());
            String brokenPacketID = messageDTO.getPacketID() + ChatConstants.BROKEN_PACKET_SEPARATOR + messageDTO.getFileChunkIndex();

            Map<Integer, MessageBaseDTO> fileStreamMap = ChatStorer.TEMP_FILE_STREAM_PACKET.get(brokenPacketID);
            if (fileStreamMap == null) {
                fileStreamMap = new ConcurrentHashMap<Integer, MessageBaseDTO>();
                ChatStorer.TEMP_FILE_STREAM_PACKET.put(brokenPacketID, fileStreamMap);
            }
            fileStreamMap.put(messageDTO.getSequenceNumber(), messageDTO);

            if (ChatStorer.TEMP_FILE_STREAM_PACKET.get(brokenPacketID) != null
                    && ChatStorer.TEMP_FILE_STREAM_PACKET.get(brokenPacketID).size() == messageDTO.getNumberOfPacket()) {
                ChatStorer.TEMP_FILE_STREAM_PACKET.remove(brokenPacketID);
                ChatService.sendFriendFileStreamConfirmation(ChatConstants.CHAT_FRIEND_FILE_STREAM_SEND_CONFIRMATION, brokenPacketID, messageDTO.getFriendIdentity());

                List<MessageBaseDTO> messageDTOs = new ArrayList<MessageBaseDTO>(fileStreamMap.values());
                Collections.sort(messageDTOs, new Comparator<MessageBaseDTO>() {
                    @Override
                    public int compare(MessageBaseDTO one, MessageBaseDTO other) {
                        Integer onesSeq = one.getSequenceNumber();
                        Integer othersSeq = other.getSequenceNumber();
                        return onesSeq.compareTo(othersSeq);
                    }
                });
                chatActionListener.onFriendFileStream(messageDTOs);
            }
        } catch (Exception e) {
        }
    }

    private void processChatFriendFileStreamConfirmation() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendFileStreamConfirmationPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }

            String packetID = messageDTO.getPacketID();
            ChatStorer.FILE_STREAM_CONFIRMATION.put(packetID, messageDTO);
            chatActionListener.onFriendFileStreamConfirmation(messageDTO);
        } catch (Exception e) {
        }
    }

    private void processChatGroupFileStream() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupFileStreamSendPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            messageDTO.setFriendIdentity(messageDTO.getUserIdentity());
            String brokenPacketID = messageDTO.getPacketID() + ChatConstants.BROKEN_PACKET_SEPARATOR + messageDTO.getFileChunkIndex();

            Map<Integer, MessageBaseDTO> fileStreamMap = ChatStorer.TEMP_FILE_STREAM_PACKET.get(brokenPacketID);
            if (fileStreamMap == null) {
                fileStreamMap = new ConcurrentHashMap<Integer, MessageBaseDTO>();
                ChatStorer.TEMP_FILE_STREAM_PACKET.put(brokenPacketID, fileStreamMap);
            }
            fileStreamMap.put(messageDTO.getSequenceNumber(), messageDTO);

            if (ChatStorer.TEMP_FILE_STREAM_PACKET.get(brokenPacketID) != null
                    && ChatStorer.TEMP_FILE_STREAM_PACKET.get(brokenPacketID).size() == messageDTO.getNumberOfPacket()) {
                ChatStorer.TEMP_FILE_STREAM_PACKET.remove(brokenPacketID);
                ChatService.sendGroupFileStreamConfirmation(ChatConstants.CHAT_GROUP_FILE_STREAM_SEND_CONFIRMATION, brokenPacketID, messageDTO.getGroupId(), messageDTO.getFriendIdentity());

                List<MessageBaseDTO> messageDTOs = new ArrayList<MessageBaseDTO>(fileStreamMap.values());
                Collections.sort(messageDTOs, new Comparator<MessageBaseDTO>() {
                    @Override
                    public int compare(MessageBaseDTO one, MessageBaseDTO other) {
                        Integer onesSeq = one.getSequenceNumber();
                        Integer othersSeq = other.getSequenceNumber();
                        return onesSeq.compareTo(othersSeq);
                    }
                });
                chatActionListener.onGroupFileStream(messageDTOs);
            }
        } catch (Exception e) {
        }
    }

    private void processChatGroupFileStreamConfirmation() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupFileStreamConfirmationPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            messageDTO.setFriendIdentity(messageDTO.getUserIdentity());
            String packetID = messageDTO.getFriendIdentity() + ChatConstants.BROKEN_PACKET_SEPARATOR + messageDTO.getPacketID();
            ChatStorer.FILE_STREAM_CONFIRMATION.put(packetID, messageDTO);
            chatActionListener.onGroupFileStreamConfirmation(messageDTO);
        } catch (Exception e) {
        }
    }

    private void processChatFriendFileStreamRequest() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendFileStreamRequestPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            messageDTO.setFriendIdentity(messageDTO.getUserIdentity());
            String brokenPacketID = messageDTO.getPacketID() + ChatConstants.BROKEN_PACKET_SEPARATOR + messageDTO.getSequenceNumber();
            String key = messageDTO.getPacketID();

            Map<Integer, MessageBaseDTO> fileStreamRequestMap = ChatStorer.TEMP_FILE_STREAM_PACKET.get(key);
            if (fileStreamRequestMap == null) {
                fileStreamRequestMap = new ConcurrentHashMap<Integer, MessageBaseDTO>();
                ChatStorer.TEMP_FILE_STREAM_PACKET.put(key, fileStreamRequestMap);
            }
            fileStreamRequestMap.put(messageDTO.getSequenceNumber(), messageDTO);
            ChatService.sendFriendFileStreamConfirmation(ChatConstants.CHAT_FRIEND_FILE_STREAM_REQUEST_CONFIRMATION, brokenPacketID, messageDTO.getFriendIdentity());

            if (ChatStorer.TEMP_FILE_STREAM_PACKET.get(key) != null
                    && ChatStorer.TEMP_FILE_STREAM_PACKET.get(key).size() == messageDTO.getNumberOfPacket()) {
                ChatStorer.TEMP_FILE_STREAM_PACKET.remove(key);
                List<MessageBaseDTO> tempMsgDTOs = new ArrayList<MessageBaseDTO>();
                for (MessageBaseDTO tempMsgDTO : fileStreamRequestMap.values()) {
                    tempMsgDTOs.addAll(tempMsgDTO.getMessageList());
                }
                messageDTO.setMessageList(tempMsgDTOs);
                chatActionListener.onFriendFileStreamRequest(messageDTO);
            }
        } catch (Exception e) {
            //  e.printStackTrace();
            log.error("Error in ChatMsgParser class in processChatFriendFileStreamRequest==>" + e.getMessage());
        }
    }

    private void processChatFriendFileStreamRequestConfirmation() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendFileStreamConfirmationPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            ChatStorer.FILE_STREAM_CONFIRMATION.put(packetID, messageDTO);
            chatActionListener.onFriendFileStreamRequestConfirmation(messageDTO);
        } catch (Exception e) {
        }
    }

    private void processChatGroupFileStreamRequest() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupFileStreamRequestPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            messageDTO.setFriendIdentity(messageDTO.getUserIdentity());

            String brokenPacketID = messageDTO.getPacketID() + ChatConstants.BROKEN_PACKET_SEPARATOR + messageDTO.getSequenceNumber();
            String key = messageDTO.getFriendIdentity() + ChatConstants.BROKEN_PACKET_SEPARATOR + messageDTO.getPacketID();

            Map<Integer, MessageBaseDTO> fileStreamRequestMap = ChatStorer.TEMP_FILE_STREAM_PACKET.get(key);
            if (fileStreamRequestMap == null) {
                fileStreamRequestMap = new ConcurrentHashMap<Integer, MessageBaseDTO>();
                ChatStorer.TEMP_FILE_STREAM_PACKET.put(key, fileStreamRequestMap);
            }
            fileStreamRequestMap.put(messageDTO.getSequenceNumber(), messageDTO);
            ChatService.sendGroupFileStreamConfirmation(ChatConstants.CHAT_GROUP_FILE_STREAM_REQUEST_CONFIRMATION, brokenPacketID, messageDTO.getGroupId(), messageDTO.getFriendIdentity());

            if (ChatStorer.TEMP_FILE_STREAM_PACKET.get(key) != null
                    && ChatStorer.TEMP_FILE_STREAM_PACKET.get(key).size() == messageDTO.getNumberOfPacket()) {
                ChatStorer.TEMP_FILE_STREAM_PACKET.remove(key);
                List<MessageBaseDTO> tempMsgDTOs = new ArrayList<MessageBaseDTO>();
                for (MessageBaseDTO tempMsgDTO : fileStreamRequestMap.values()) {
                    tempMsgDTOs.addAll(tempMsgDTO.getMessageList());
                }
                messageDTO.setMessageList(tempMsgDTOs);
                chatActionListener.onGroupFileStreamRequest(messageDTO);
            }
        } catch (Exception e) {
        }
    }

    private void processChatGroupFileStreamRequestConfirmation() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupFileStreamConfirmationPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            ChatStorer.FILE_STREAM_CONFIRMATION.put(packetID, messageDTO);
            chatActionListener.onGroupFileStreamRequestConfirmation(messageDTO);
        } catch (Exception e) {
        }
    }

    private void processChatFriendUnregister() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getFriendUnregisterPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            messageDTO.setFriendIdentity(messageDTO.getUserIdentity());

            MessageBaseDTO serverLocation = ChatStorer.SERVER_LOCATION.get(messageDTO.getFriendIdentity());
            if (serverLocation != null
                    && messageDTO.getPresence() != ChatConstants.PRESENCE_ONLINE
                    && serverLocation.getPresence() != messageDTO.getPresence()) {
                ChatRegistrationDTO entity = new ChatRegistrationDTO();
                entity.setDevice(serverLocation.getDevice());
                entity.setDeviceToken(serverLocation.getDeviceToken());
                entity.setFriendIdentity(messageDTO.getFriendIdentity());
                entity.setPresence(messageDTO.getPresence());
                ChatService.registerFriendChat(entity);
            }

            chatActionListener.onFriendChatUnregister(messageDTO);
        } catch (Exception e) {
        }
    }

    private void processChatGroupUnregister() {
        try {
            MessageBaseDTO messageDTO = ChatPacketUility.getGroupUnregisterPacket(recievedBuffer);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Received >> " + messageDTO.toString());
            }
            String packetID = messageDTO.getPacketID();
            messageDTO.setFriendIdentity(messageDTO.getUserIdentity());
            chatActionListener.onGroupChatUnregister(messageDTO);
        } catch (Exception e) {
        }
    }

}
