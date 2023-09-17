/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.resender;

import java.util.List;
import com.ipv.chat.dto.MessageBaseDTO;
import com.ipv.chat.ChatConstants;
import com.ipv.chat.ChatSettings;
import com.ipv.chat.storers.ChatStorer;
import com.ipv.chat.utils.ChatPacketUility;
import com.ipv.chat.communicator.ChatSender;
import com.ipv.chat.dto.ChatRegistrationDTO;
import com.ipv.chat.listener.PacketResenderAdapter;
import com.ipv.chat.service.ChatService;
import com.ipv.chat.utils.ChatUtility;
import java.util.Map.Entry;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatGroupRegistationResender extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatGroupRegistationResender.class);
    private List<String> memberList;
    private ChatRegistrationDTO entity;

    public ChatGroupRegistationResender(ChatRegistrationDTO entity, List<String> memberList) {
        this.entity = entity;
        this.memberList = memberList;
    }

    @Override
    public void run() {
        try {
            ChatIdleResender chatIdleResender = ChatStorer.IDLE_THREAD.get(entity.getGroupId() + "");
            if (chatIdleResender != null) {
                chatIdleResender.stopService();
            } else {
                ChatStorer.SERVER_LOCATION.remove(entity.getGroupId() + "");
            }

            String packetID = ChatUtility.getRandomPacketID();
            byte[] packet = ChatPacketUility.makeGroupRegisterPacket(
                    ChatConstants.CHAT_GROUP_REGISTER,
                    ChatSettings.USER_IDENTITY,
                    ChatSettings.USER_FULLNAME,
                    entity.getGroupId(),
                    packetID);
            ChatSender.sendRegisterPacket(packet, entity.getChatServerIp(), entity.getChatRegistrationPort());

            for (int i = ChatConstants.UNIT_OF_DELAY; i <= ChatConstants.NUMBER_OF_RESEND * ChatConstants.DELAY_OF_RESEND; i += ChatConstants.UNIT_OF_DELAY) {
                Thread.sleep(ChatConstants.UNIT_OF_DELAY);

                if (ChatStorer.PACKET_CONFIRMATION.get(packetID) == null) {
                    if (i % ChatConstants.DELAY_OF_RESEND == 0) {
                        ChatSender.sendRegisterPacket(packet, entity.getChatServerIp(), entity.getChatRegistrationPort());
                    }
                } else {
                    long groupRegTime = System.currentTimeMillis();
                    final MessageBaseDTO messageDTO = ChatStorer.PACKET_CONFIRMATION.get(packetID);
                    messageDTO.setPacketType(ChatConstants.CHAT_GROUP_REGISTER);
                    messageDTO.setMessageDate(groupRegTime);
                    long diff = messageDTO.getServerDate() - groupRegTime;
                    messageDTO.setDateDifference(diff);
                    messageDTO.setChatServerIP(entity.getChatServerIp());
                    messageDTO.setChatRegisterPort(entity.getChatRegistrationPort());
                    ChatStorer.SERVER_LOCATION.put(entity.getGroupId() + "", messageDTO);

                    if (entity.getFriendIdentity() == null || entity.getFriendIdentity().trim().length() <= 0) {
                        if (ChatSettings.DEBUG) {
                            System.err.println("GROUP CHAT >> GROUP MEMBER SENDING >> " + memberList);
                        }
                        ChatService.sendGroupMemberList(entity.getGroupId(), memberList, new PacketResenderAdapter() {
                            @Override
                            public void onFailed(MessageBaseDTO msgDTO) {
                                sendOfflineBlankMessage(messageDTO);
                            }

                            @Override
                            public void onSuccess(MessageBaseDTO msgDTO) {
                                sendOfflineBlankMessage(messageDTO);
                            }
                        });

                    }

                    replacePortWithSameIp(messageDTO.getChatBindingPort());
                    startChatIdleResender(entity.getGroupId());

                    if (ChatSettings.DEBUG) {
                        System.err.println("GROUP CHAT REG ["
                                + entity.getGroupId() + "] >>> "
                                + "CHAT_SERVER_IP = " + entity.getChatServerIp() + ", "
                                + "CHAT_REGISTER_PORT = " + entity.getChatRegistrationPort() + ", "
                                + "CHAT_SERVER_TIME_DIFF = " + (diff / 1000) + " Seconds, "
                                + "CHAT_BINDING_PORT = " + messageDTO.getChatBindingPort());
                    }
                    ChatStorer.PACKET_CONFIRMATION.remove(packetID);
                    break;
                }
            }
        } catch (Exception ex) {
           // ex.printStackTrace();
          log.error("Error in ChatGroupRegistationResender class ==>" + ex.getMessage());  
        }
    }

    private void startChatIdleResender(Long groupId) {
        ChatIdleResender chatIdleResender = new ChatIdleResender(groupId, ChatConstants.CHAT_GROUP_IDEL);
        chatIdleResender.start();
        ChatStorer.IDLE_THREAD.put(groupId + "", chatIdleResender);
    }

    private void replacePortWithSameIp(int chatBindingPort) {
        for (Entry<String, MessageBaseDTO> en : ChatStorer.SERVER_LOCATION.entrySet()) {
            MessageBaseDTO messageDTO = en.getValue();
            if (messageDTO.getChatServerIP() != null
                    && messageDTO.getChatServerIP().length() > 0
                    && messageDTO.getChatServerIP().equalsIgnoreCase(entity.getChatServerIp())) {
                messageDTO.setChatBindingPort(chatBindingPort);
            }
        }
    }

    private void sendOfflineBlankMessage(MessageBaseDTO serverLocation) {
        try {
            long currentTime = System.currentTimeMillis();
            String packetID = ChatUtility.getRandomPacketID();

            MessageBaseDTO messageDTO = new MessageBaseDTO();
            messageDTO.setPacketType(ChatConstants.CHAT_GROUP);
            messageDTO.setMessageType(ChatConstants.TYPE_BLANK_MSG);
            messageDTO.setUserIdentity(ChatSettings.USER_IDENTITY);
            messageDTO.setFullName(ChatSettings.USER_FULLNAME);
            messageDTO.setGroupId(entity.getGroupId());
            messageDTO.setMessage("");
            messageDTO.setMessageDate(currentTime + serverLocation.getDateDifference());
            messageDTO.setPacketID(packetID);

            byte[] packet = ChatPacketUility.makeGroupChatPacket(messageDTO);
            ChatSender.sendChatPacket(packet, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());

            for (int i = ChatConstants.UNIT_OF_DELAY; i <= ChatConstants.NUMBER_OF_RESEND * ChatConstants.DELAY_OF_RESEND; i += ChatConstants.UNIT_OF_DELAY) {
                Thread.sleep(ChatConstants.UNIT_OF_DELAY);

                if (ChatStorer.PACKET_CONFIRMATION.get(packetID) == null) {
                    if (i % ChatConstants.DELAY_OF_RESEND == 0) {
                        ChatSender.sendChatPacket(packet, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                    }
                } else {
                    ChatStorer.PACKET_CONFIRMATION.remove(packetID);
                    break;
                }
            }

        } catch (Exception ex) {

        }
    }

}
