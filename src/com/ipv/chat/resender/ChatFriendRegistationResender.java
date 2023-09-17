/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.resender;

import java.util.Map.Entry;
import com.ipv.chat.dto.MessageBaseDTO;
import com.ipv.chat.ChatConstants;
import com.ipv.chat.ChatSettings;
import com.ipv.chat.storers.ChatStorer;
import com.ipv.chat.utils.ChatPacketUility;
import com.ipv.chat.communicator.ChatSender;
import com.ipv.chat.dto.ChatRegistrationDTO;
import com.ipv.chat.utils.ChatUtility;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatFriendRegistationResender extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatFriendRegistationResender.class);
    private ChatRegistrationDTO entity;

    public ChatFriendRegistationResender(ChatRegistrationDTO entity) {
        this.entity = entity;
    }

    @Override
    public void run() {
        try {
            ChatIdleResender chatIdleResender = ChatStorer.IDLE_THREAD.get(entity.getFriendIdentity());
            if (chatIdleResender != null) {
                chatIdleResender.stopService();
            } else {
                ChatStorer.SERVER_LOCATION.remove(entity.getFriendIdentity());
            }

            if (entity.getPresence() == ChatConstants.PRESENCE_ONLINE) {
                String packetID = ChatUtility.getRandomPacketID();
                byte[] packet = ChatPacketUility.makeFriendRegisterPacket(
                        ChatConstants.CHAT_FRIEND_REGISTER,
                        ChatSettings.USER_IDENTITY,
                        entity.getFriendIdentity(),
                        packetID);
                ChatSender.sendRegisterPacket(packet, entity.getChatServerIp(), entity.getChatRegistrationPort());

                for (int i = ChatConstants.UNIT_OF_DELAY; i <= ChatConstants.NUMBER_OF_RESEND * ChatConstants.DELAY_OF_RESEND; i += ChatConstants.UNIT_OF_DELAY) {
                    Thread.sleep(ChatConstants.UNIT_OF_DELAY);

                    if (ChatStorer.PACKET_CONFIRMATION.get(packetID) == null) {
                        if (i % ChatConstants.DELAY_OF_RESEND == 0) {
                            ChatSender.sendRegisterPacket(packet, entity.getChatServerIp(), entity.getChatRegistrationPort());
                        }
                    } else {
                        long friendRegTime = System.currentTimeMillis();
                        MessageBaseDTO messageDTO = ChatStorer.PACKET_CONFIRMATION.get(packetID);
                        messageDTO.setPacketType(ChatConstants.CHAT_FRIEND_REGISTER);
                        messageDTO.setMessageDate(friendRegTime);
                        long diff = messageDTO.getServerDate() - friendRegTime;
                        messageDTO.setDateDifference(diff);
                        messageDTO.setChatServerIP(entity.getChatServerIp());
                        messageDTO.setChatRegisterPort(entity.getChatRegistrationPort());
                        messageDTO.setPresence(entity.getPresence());
                        messageDTO.setDevice(entity.getDevice());
                        messageDTO.setDeviceToken(entity.getDeviceToken());
                        ChatStorer.SERVER_LOCATION.put(entity.getFriendIdentity(), messageDTO);

                        if (entity.getDevice() != ChatConstants.PLATFORM_DESKTOP
                                && entity.getDevice() != ChatConstants.PLATFORM_WEB
                                && entity.getDeviceToken() != null && entity.getDeviceToken().trim().length() > 0) {
                            new ChatFriendInformationResender(messageDTO).start();
                        }
                        replacePortWithSameIp(messageDTO.getChatBindingPort());
                        startChatIdleResender(entity.getFriendIdentity());

                        if (ChatSettings.DEBUG) {
                            System.err.println("FRIEND CHAT REG ["
                                    + entity.getFriendIdentity() + "] >>> "
                                    + "CHAT_SERVER_IP = " + entity.getChatServerIp() + ", "
                                    + "CHAT_REGISTER_PORT = " + entity.getChatRegistrationPort() + ", "
                                    + "CHAT_SERVER_TIME_DIFF = " + (diff / 1000) + " Seconds, "
                                    + "CHAT_BINDING_PORT = " + messageDTO.getChatBindingPort());
                        }
                        ChatStorer.PACKET_CONFIRMATION.remove(packetID);
                        break;
                    }
                }
            } else {
                long friendRegTime = System.currentTimeMillis();
                MessageBaseDTO messageDTO = new MessageBaseDTO();
                messageDTO.setPacketType(ChatConstants.CHAT_FRIEND_REGISTER);
                messageDTO.setFriendIdentity(entity.getFriendIdentity());
                messageDTO.setMessageDate(friendRegTime);
                messageDTO.setPresence(entity.getPresence());
                messageDTO.setDevice(entity.getDevice());
                messageDTO.setDeviceToken(entity.getDeviceToken());
                messageDTO.setDateDifference(ChatSettings.OFFLINE_SERVER_TIME_DIFF);
                ChatStorer.SERVER_LOCATION.put(entity.getFriendIdentity(), messageDTO);

                sendOfflineBlankMessage();
                if (entity.getPresence() == ChatConstants.PRESENCE_AWAY
                        && entity.getDevice() != ChatConstants.PLATFORM_DESKTOP
                        && entity.getDevice() != ChatConstants.PLATFORM_WEB
                        && entity.getDeviceToken() != null && entity.getDeviceToken().trim().length() > 0) {
                    new ChatFriendInformationResender(messageDTO).start();
                }
            }
        } catch (Exception ex) {
          //  ex.printStackTrace();
            log.error("Error in ChatFriendRegistationResender class in chatSocket ==>" + ex.getMessage());
        }
    }

    private void startChatIdleResender(String friendIdentity) {
        ChatIdleResender chatIdleResender = new ChatIdleResender(friendIdentity, ChatConstants.CHAT_FRIEND_IDEL);
        chatIdleResender.start();
        ChatStorer.IDLE_THREAD.put(friendIdentity, chatIdleResender);
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

    private void sendOfflineBlankMessage() {

        try {
            long time = System.currentTimeMillis();
            String packetID = ChatUtility.getRandomPacketID();

            MessageBaseDTO messageDTO = new MessageBaseDTO();
            messageDTO.setPacketType(ChatConstants.CHAT_FRIEND);
            messageDTO.setMessageType(ChatConstants.TYPE_BLANK_MSG);
            messageDTO.setUserIdentity(ChatSettings.USER_IDENTITY);
            messageDTO.setFriendIdentity(entity.getFriendIdentity());
            messageDTO.setMessage("");
            messageDTO.setMessageDate(time);
            messageDTO.setPacketID(packetID);

            byte[] packet = ChatPacketUility.makeFriendChatPacket(messageDTO);
            ChatSender.sendOfflineChatPacket(packet);

            for (int i = ChatConstants.UNIT_OF_DELAY; i <= ChatConstants.NUMBER_OF_RESEND * ChatConstants.DELAY_OF_RESEND; i += ChatConstants.UNIT_OF_DELAY) {
                Thread.sleep(ChatConstants.UNIT_OF_DELAY);

                if (ChatStorer.PACKET_CONFIRMATION.get(packetID) == null) {
                    if (i % ChatConstants.DELAY_OF_RESEND == 0) {
                        ChatSender.sendOfflineChatPacket(packet);
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
