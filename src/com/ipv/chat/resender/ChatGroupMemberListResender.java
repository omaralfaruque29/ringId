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
public class ChatGroupMemberListResender extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatGroupMemberListResender.class);
    private List<String> members;
    private long groupId;
    private MessageBaseDTO serverLocation;
    private PacketResenderListener listener;

    public ChatGroupMemberListResender(long groupId, List<String> members, PacketResenderListener listener) {
        this.groupId = groupId;
        this.members = members;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            MessageBaseDTO messageBaseDTO = new MessageBaseDTO();
            messageBaseDTO.setGroupId(groupId);

            serverLocation = ChatStorer.SERVER_LOCATION.get(groupId + "");
            if (serverLocation != null) {
                Map<String, byte[]> packets = ChatPacketUility.makeGroupMemberListPacket(
                        ChatConstants.CHAT_GROUP_MEMBERS_SEND,
                        ChatSettings.USER_IDENTITY,
                        groupId,
                        members);
                sendMemberListPacket(packets);

                for (int i = ChatConstants.UNIT_OF_DELAY; i <= ChatConstants.NUMBER_OF_RESEND * ChatConstants.DELAY_OF_RESEND; i += ChatConstants.UNIT_OF_DELAY) {
                    Thread.sleep(ChatConstants.UNIT_OF_DELAY);

                    if (isAllPacketConfirmationReceived(packets) == false) {
                        if (i % ChatConstants.DELAY_OF_RESEND == 0) {
                            sendMemberListPacket(packets);
                        }
                    } else {
                        if (listener != null) {
                            listener.onSuccess(messageBaseDTO);
                        }
                        break;
                    }

                    if (i == ChatConstants.NUMBER_OF_RESEND * ChatConstants.DELAY_OF_RESEND) {
                        if (listener != null) {
                            listener.onFailed(messageBaseDTO);
                        }
                    }
                }
            }

        } catch (Exception ex) {
           // ex.printStackTrace();
            log.error("Error in ChatGroupMemberListResender class ==>" + ex.getMessage());
        }
    }

    private void sendMemberListPacket(Map<String, byte[]> packets) {
        try {
            serverLocation.setMessageDate(System.currentTimeMillis());
            for (byte[] packet : packets.values()) {
                ChatSender.sendChatPacket(packet, serverLocation.getChatServerIP(), serverLocation.getChatRegisterPort());
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
