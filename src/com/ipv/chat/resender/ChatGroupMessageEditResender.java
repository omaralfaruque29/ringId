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
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatGroupMessageEditResender extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatGroupMessageEditResender.class);
    private MessageBaseDTO messageDTO;
    private MessageBaseDTO serverLocation;

    public ChatGroupMessageEditResender(MessageBaseDTO messageDTO) {
        this.messageDTO = messageDTO;
    }

    @Override
    public void run() {
        try {
            if (ChatStorer.SERVER_LOCATION.get(messageDTO.getGroupId() + "") == null) {
                for (int i = 0; i < 20; i++) {
                    if (ChatStorer.SERVER_LOCATION.get(messageDTO.getGroupId() + "") == null) {
                        Thread.sleep(500);
                    } else {
                        break;
                    }
                }
            }

            serverLocation = ChatStorer.SERVER_LOCATION.get(messageDTO.getGroupId() + "");
            if (serverLocation != null) {
                Map<String, byte[]> packets = ChatPacketUility.makeGroupBrokenMessagePacket(messageDTO);

                sendChatEditPacket(packets);
                for (int i = ChatConstants.UNIT_OF_DELAY; i <= ChatConstants.NUMBER_OF_RESEND * ChatConstants.DELAY_OF_RESEND; i += ChatConstants.UNIT_OF_DELAY) {
                    Thread.sleep(ChatConstants.UNIT_OF_DELAY);

                    if (isAllPacketConfirmationReceived(packets) == false) {
                        if (i % ChatConstants.DELAY_OF_RESEND == 0) {
                            sendChatEditPacket(packets);
                        }
                    } else {
                        break;
                    }
                }
            }
        } catch (Exception ex) {
           // ex.printStackTrace();
         log.error("Error in ChatGroupMessageEditResender class ==>" + ex.getMessage());   
        }
    }

    private void sendChatEditPacket(Map<String, byte[]> packets) {
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
