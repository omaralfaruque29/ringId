/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.resender;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.ChatSettings;
import com.ipv.chat.dto.MessageBaseDTO;
import com.ipv.chat.storers.ChatStorer;
import com.ipv.chat.utils.ChatPacketUility;
import com.ipv.chat.communicator.ChatSender;
import com.ipv.chat.listener.PacketResenderListener;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatGroupMessageResender extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatGroupMessageResender.class);
    private final MessageBaseDTO messageDTO;
    private MessageBaseDTO serverLocation;
    private PacketResenderListener listener;
    private boolean isInternatAvailable = true;

    public ChatGroupMessageResender(MessageBaseDTO messageDTO, PacketResenderListener listener, boolean isInternatAvailable) {
        this.messageDTO = messageDTO;
        this.listener = listener;
        this.isInternatAvailable = isInternatAvailable;
    }

    @Override
    public void run() {
        try {
            if (isInternatAvailable && ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
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
            }
            long currentTime = System.currentTimeMillis();

            if (serverLocation != null) {
                messageDTO.setMessageDate(currentTime + serverLocation.getDateDifference());
                listener.onInit(messageDTO);

                Map<String, byte[]> packets = ChatPacketUility.makeGroupBrokenMessagePacket(messageDTO);

                sendChatPacket(packets);
                for (int i = ChatConstants.UNIT_OF_DELAY; i <= ChatConstants.NUMBER_OF_RESEND * ChatConstants.DELAY_OF_RESEND; i += ChatConstants.UNIT_OF_DELAY) {
                    Thread.sleep(ChatConstants.UNIT_OF_DELAY);

                    if (isAllPacketConfirmationReceived(packets) == false) {
                        if (i % ChatConstants.DELAY_OF_RESEND == 0) {
                            sendChatPacket(packets);
                        }
                    } else {
                        listener.onSuccess(messageDTO);
                        break;
                    }

                    if (i == ChatConstants.NUMBER_OF_RESEND * ChatConstants.DELAY_OF_RESEND) {
                        listener.onFailed(messageDTO);
                    }
                }
            } else {
                messageDTO.setMessageDate(currentTime + ChatSettings.OFFLINE_SERVER_TIME_DIFF);
                listener.onInit(messageDTO);
                Thread.sleep(500);
                listener.onFailed(messageDTO);
            }

        } catch (Exception ex) {
          //  ex.printStackTrace();
         log.error("Error in ChatGroupMessageResender class ==>" + ex.getMessage());   
        }
    }

    private void sendChatPacket(Map<String, byte[]> packets) {
        serverLocation.setMessageDate(System.currentTimeMillis());
        for (byte[] packet : packets.values()) {
            ChatSender.sendChatPacket(packet, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
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
