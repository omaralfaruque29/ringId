/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.resender;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.dto.MessageBaseDTO;
import com.ipv.chat.storers.ChatStorer;
import com.ipv.chat.utils.ChatPacketUility;
import com.ipv.chat.communicator.ChatSender;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatGroupMessageBackgroundResender extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatGroupMessageBackgroundResender.class);
    private Long groupId;
    private List<MessageBaseDTO> messageDTOs;
    private MessageBaseDTO serverLocation;

    public ChatGroupMessageBackgroundResender(Long groupId, List<MessageBaseDTO> messageDTOs) {
        this.groupId = groupId;
        this.messageDTOs = messageDTOs;
    }

    @Override
    public void run() {
        try {
            if (ChatStorer.SERVER_LOCATION.get(groupId + "") == null) {
                for (int i = 0; i < 20; i++) {
                    if (ChatStorer.SERVER_LOCATION.get(groupId + "") == null) {
                        Thread.sleep(500);
                    } else {
                        break;
                    }
                }
            }

            serverLocation = ChatStorer.SERVER_LOCATION.get(groupId + "");
            if (serverLocation != null) {
                Map<String, byte[]> packets = new ConcurrentHashMap<String, byte[]>();
                for (MessageBaseDTO messageDTO : messageDTOs) {
                    Map<String, byte[]> brokenPacket = ChatPacketUility.makeGroupBrokenMessagePacket(messageDTO);
                    for (Entry<String, byte[]> entrySet : brokenPacket.entrySet()) {
                        packets.put(entrySet.getKey(), entrySet.getValue());
                    }
                }

                sendChatPacket(packets);
                for (int i = ChatConstants.UNIT_OF_DELAY; i <= ChatConstants.NUMBER_OF_RESEND * ChatConstants.DELAY_OF_RESEND; i += ChatConstants.UNIT_OF_DELAY) {
                    Thread.sleep(ChatConstants.UNIT_OF_DELAY);

                    if (isAllPacketConfirmationReceived(packets) == false) {
                        if (i % ChatConstants.DELAY_OF_RESEND == 0) {
                            sendChatPacket(packets);
                        }
                    } else {
                        break;
                    }
                }
            }

        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Error in ChatCommunication class ==>" + ex.getMessage());
        }
    }

    private void sendChatPacket(Map<String, byte[]> packets) {
        try {
            serverLocation.setMessageDate(System.currentTimeMillis());
            for (byte[] packet : packets.values()) {
                ChatSender.sendChatPacket(packet, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                Thread.sleep(25);
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
