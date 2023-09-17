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

/**
 *
 * @author Shahadat Hossain
 */
public class ChatUnRegistation extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatUnRegistation.class);
    private String userIdentity;
    private Long groupId;
    private int onlineStatus;

    public ChatUnRegistation(String userIdentity, Long groupId, int onlineStatus) {
        this.userIdentity = userIdentity;
        this.groupId = groupId;
        this.onlineStatus = onlineStatus;
    }

    @Override
    public void run() {
        try {
            String id = userIdentity != null ? userIdentity : groupId + "";
            MessageBaseDTO messageDTO = ChatStorer.SERVER_LOCATION.get(id);

            if (messageDTO != null) {
                if (messageDTO.getPacketType() == ChatConstants.CHAT_FRIEND_REGISTER) {
                    byte[] packet = ChatPacketUility.makeFriendUnRegisterPacket(ChatConstants.CHAT_FRIEND_UNREGISTER, ChatSettings.USER_IDENTITY, messageDTO.getFriendIdentity(), onlineStatus);
                    ChatSender.sendRegisterPacket(packet, messageDTO.getChatServerIP(), messageDTO.getChatRegisterPort());
                    Thread.sleep(25);
                    ChatSender.sendRegisterPacket(packet, messageDTO.getChatServerIP(), messageDTO.getChatRegisterPort());
                    Thread.sleep(25);
                    ChatSender.sendRegisterPacket(packet, messageDTO.getChatServerIP(), messageDTO.getChatRegisterPort());
                } else if (messageDTO.getPacketType() == ChatConstants.CHAT_GROUP_REGISTER) {
                    byte[] packet = ChatPacketUility.makeGroupUnRegisterPacket(ChatConstants.CHAT_GROUP_UNREGISTER, ChatSettings.USER_IDENTITY, messageDTO.getGroupId(), onlineStatus);
                    ChatSender.sendRegisterPacket(packet, messageDTO.getChatServerIP(), messageDTO.getChatRegisterPort());
                    Thread.sleep(25);
                    ChatSender.sendRegisterPacket(packet, messageDTO.getChatServerIP(), messageDTO.getChatRegisterPort());
                    Thread.sleep(25);
                    ChatSender.sendRegisterPacket(packet, messageDTO.getChatServerIP(), messageDTO.getChatRegisterPort());
                }
            }

            ChatIdleResender chatIdleResender = ChatStorer.IDLE_THREAD.get(id);
            if (chatIdleResender != null && chatIdleResender.isRunning()) {
                chatIdleResender.stopService();
            }

            ChatStorer.IDLE_THREAD.remove(id);
            ChatStorer.SERVER_LOCATION.remove(id);
        } catch (Exception ex) {
            //ex.printStackTrace();
         log.error("Error in ChatUnRegistation class ==>" + ex.getMessage());   
        }
    }
}
