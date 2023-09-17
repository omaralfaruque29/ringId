/*
 * To change this template, choose Tools | Templates
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
public class ChatFriendInformationResender extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatFriendInformationResender.class);
    private MessageBaseDTO location;

    public ChatFriendInformationResender(MessageBaseDTO location) {
        this.location = location;
    }

    @Override
    public void run() {
        try {
            if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
                String packetID = ChatUtility.getRandomPacketID();
                MessageBaseDTO messageDTO = new MessageBaseDTO();
                messageDTO.setPacketType(ChatConstants.CHAT_FRIEND_INFORMATION);
                messageDTO.setDevice(location.getDevice());
                messageDTO.setPacketID(packetID);
                messageDTO.setUserIdentity(ChatSettings.USER_IDENTITY);
                messageDTO.setFullName(ChatSettings.USER_FULLNAME);
                messageDTO.setFriendIdentity(location.getFriendIdentity());
                messageDTO.setPresence(location.getPresence());
                messageDTO.setDeviceToken(location.getDeviceToken());

                byte[] packet = ChatPacketUility.makeFriendInformationMessage(messageDTO);
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
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Error in ChatFriendInformationResender class ==>" + ex.getMessage());
        }
    }

}
