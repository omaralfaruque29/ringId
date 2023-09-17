/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.resender;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.ChatSettings;
import com.ipv.chat.dto.MessageBaseDTO;
import com.ipv.chat.storers.ChatStorer;
import com.ipv.chat.communicator.ChatSender;
import com.ipv.chat.listener.PacketResenderListener;
import com.ipv.chat.utils.ChatPacketUility;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatFriendMessageDeleteResender extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatFriendMessageDeleteResender.class);
    private String friendIdentity;
    private MessageBaseDTO serverLocation;
    private List<String> packetIDs;
    private PacketResenderListener listener;

    public ChatFriendMessageDeleteResender(String friendIdentity, List<String> packetIDs, PacketResenderListener listener) {
        this.friendIdentity = friendIdentity;
        this.packetIDs = packetIDs;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            if (ChatStorer.SERVER_LOCATION.get(friendIdentity) == null) {
                for (int i = 0; i < 20; i++) {
                    if (ChatStorer.SERVER_LOCATION.get(friendIdentity) == null) {
                        Thread.sleep(500);
                    } else {
                        break;
                    }
                }
            }

            serverLocation = ChatStorer.SERVER_LOCATION.get(friendIdentity);
            if (serverLocation != null) {
                boolean isInitOnlineRegistration = serverLocation.getPresence() == ChatConstants.PRESENCE_ONLINE;
                int numerOfResend = ChatConstants.NUMBER_OF_RESEND + (isInitOnlineRegistration ? ChatConstants.NUMBER_OF_ADDITIONAL_RESEND : 0);
                Map<String, byte[]> packets = ChatPacketUility.makeFriendMultipleMessageDeletePacket(
                        ChatConstants.CHAT_FRIEND_MULTIPLE_MESSAGE_DELETE,
                        ChatSettings.USER_IDENTITY,
                        friendIdentity,
                        packetIDs);

                sendChatDeletePacket(packets);
                for (int i = ChatConstants.UNIT_OF_DELAY; i <= numerOfResend * ChatConstants.DELAY_OF_RESEND; i += ChatConstants.UNIT_OF_DELAY) {
                    Thread.sleep(ChatConstants.UNIT_OF_DELAY);

                    if (isAllPacketConfirmationReceived(packets) == false) {
                        if (i % ChatConstants.DELAY_OF_RESEND == 0) {
                            sendChatDeletePacket(packets);
                        }
                    } else {
                        break;
                    }

                    if (isInitOnlineRegistration && i == ChatConstants.NUMBER_OF_RESEND * ChatConstants.DELAY_OF_RESEND) {
                        MessageBaseDTO messageDTO = new MessageBaseDTO();
                        messageDTO.setPacketType(ChatConstants.CHAT_FRIEND_MULTIPLE_MESSAGE_DELETE);
                        messageDTO.setFriendIdentity(friendIdentity);
                        listener.onProgress(messageDTO);
                    }

                    if (i == numerOfResend * ChatConstants.DELAY_OF_RESEND) {
                        for (byte[] packet : packets.values()) {
                            ChatSender.sendOfflineChatPacket(packet);
                        }
                    }
                }
            }
        } catch (Exception ex) {
           // ex.printStackTrace();
         log.error("Error in here ==>" + ex.getMessage());
        }
    }

    private void sendChatDeletePacket(Map<String, byte[]> packets) {
        try {
            serverLocation = ChatStorer.SERVER_LOCATION.get(friendIdentity);
            if (serverLocation != null) {
                if (serverLocation.getPresence() == ChatConstants.PRESENCE_ONLINE) {
                    serverLocation.setMessageDate(System.currentTimeMillis());
                    for (byte[] packet : packets.values()) {
                        ChatSender.sendChatPacket(packet, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                        Thread.sleep(25);
                    }
                } else {
                    for (byte[] packet : packets.values()) {
                        ChatSender.sendOfflineChatPacket(packet);
                        Thread.sleep(25);
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    private boolean isAllPacketConfirmationReceived(Map<String, byte[]> packets) {
        Set<String> packetIds = packets.keySet();
        for (String packetId : packetIds) {
            if (ChatStorer.PACKET_CONFIRMATION.get(packetId) != null) {
                ChatStorer.PACKET_CONFIRMATION.remove(packetId);
                packets.remove(packetId);
            }
        }
        return packets.size() <= 0;
    }

}
