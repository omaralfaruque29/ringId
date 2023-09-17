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
import com.ipv.chat.utils.ChatPacketUility;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatFriendFileStreamRequestResender extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatFriendFileStreamRequestResender.class);
    private MessageBaseDTO serverLocation;
    private String packetID;
    private String friendIdentity;
    private List<Integer[]> indexRanges;
    private int totalChunk;

    public ChatFriendFileStreamRequestResender(String packetID, String friendIdentity, List<Integer[]> indexRanges, int totalChunk) {
        this.packetID = packetID;
        this.friendIdentity = friendIdentity;
        this.indexRanges = indexRanges;
        this.totalChunk = totalChunk;
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
            if (serverLocation != null && serverLocation.getPresence() == ChatConstants.PRESENCE_ONLINE) {
                Map<String, byte[]> packets = ChatPacketUility.makeFriendFileStreamRequestPacket(
                        ChatConstants.CHAT_FRIEND_FILE_STREAM_REQUEST,
                        packetID,
                        ChatSettings.USER_IDENTITY,
                        friendIdentity,
                        indexRanges,
                        totalChunk);

                sendChatStreamRequestPacket(packets);
                for (int i = ChatConstants.UNIT_OF_DELAY; i <= ChatConstants.NUMBER_OF_RESEND * ChatConstants.DELAY_OF_RESEND; i += ChatConstants.UNIT_OF_DELAY) {
                    Thread.sleep(ChatConstants.UNIT_OF_DELAY);

                    if (isAllPacketConfirmationReceived(packets) == false) {
                        if (i % ChatConstants.DELAY_OF_RESEND == 0) {
                            sendChatStreamRequestPacket(packets);
                        }
                    } else {
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
            log.error("Error in ChatFriendFileStreamRequestResender class ==>" + ex.getMessage());
        }
    }

    private void sendChatStreamRequestPacket(Map<String, byte[]> packets) {
        try {
            serverLocation = ChatStorer.SERVER_LOCATION.get(friendIdentity);
            if (serverLocation != null) {
                if (serverLocation.getPresence() == ChatConstants.PRESENCE_ONLINE) {
                    serverLocation.setMessageDate(System.currentTimeMillis());
                    for (byte[] packet : packets.values()) {
                        ChatSender.sendChatPacket(packet, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
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
            if (ChatStorer.FILE_STREAM_CONFIRMATION.get(packetId) != null) {
                ChatStorer.FILE_STREAM_CONFIRMATION.remove(packetId);
                packets.remove(packetId);
            }
        }
        return packets.size() <= 0;
    }

}
