/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.resender;

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
public class ChatOfflineRequestResender extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatOfflineRequestResender.class);

    @Override
    public void run() {
        try {
            String packetID = ChatUtility.getRandomPacketID();

            byte[] packet = ChatPacketUility.makeOfflineRequestPacket(ChatConstants.CHAT_OFFLINE_REQUEST, ChatSettings.USER_IDENTITY, packetID);
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
           // ex.printStackTrace();
        log.error("Error in ChatOfflineRequestResender class ==>" + ex.getMessage());
        }
    }

}
