/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.resender;

import com.ipv.chat.dto.MessageBaseDTO;
import com.ipv.chat.ChatConstants;
import com.ipv.chat.ChatSettings;
import com.ipv.chat.storers.ChatStorer;
import com.ipv.chat.utils.ChatPacketUility;
import com.ipv.chat.communicator.ChatSender;
import com.ipv.chat.utils.ChatUtility;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatGroupDeleteResender extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatGroupDeleteResender.class);
    private Long groupId;

    public ChatGroupDeleteResender(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public void run() {
        try {
            MessageBaseDTO serverLocation = ChatStorer.SERVER_LOCATION.get(groupId + "");
            if (serverLocation != null) {
                serverLocation.setMessageDate(System.currentTimeMillis());

                String packetID = ChatUtility.getRandomPacketID();
                byte[] packet = ChatPacketUility.makeGroupDeletePacket(
                        ChatConstants.CHAT_GROUP_DELETE,
                        ChatSettings.USER_IDENTITY,
                        groupId,
                        packetID);

                ChatSender.sendRegisterPacket(packet, serverLocation.getChatServerIP(), serverLocation.getChatRegisterPort());

                for (int i = ChatConstants.UNIT_OF_DELAY; i <= ChatConstants.NUMBER_OF_RESEND * ChatConstants.DELAY_OF_RESEND; i += ChatConstants.UNIT_OF_DELAY) {
                    Thread.sleep(ChatConstants.UNIT_OF_DELAY);

                    if (ChatStorer.PACKET_CONFIRMATION.get(packetID) == null) {
                        if (i % ChatConstants.DELAY_OF_RESEND == 0) {
                            ChatSender.sendRegisterPacket(packet, serverLocation.getChatServerIP(), serverLocation.getChatRegisterPort());
                        }
                    } else {
                        ChatStorer.PACKET_CONFIRMATION.remove(packetID);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
           // ex.printStackTrace();
            log.error("Error in ChatGroupDeleteResender class ==>" + ex.getMessage());
        }
    }

}
