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
public class ChatIdleResender extends Thread {

    private static final int INTERVAL = 20000;
    private Long groupId;
    private String userIdentity;
    private boolean running = true;
    private int packetType;

    public ChatIdleResender(Long groupId, int packetType) {
        this.groupId = groupId;
        this.packetType = packetType;
        this.running = true;
    }

    public ChatIdleResender(String userIdentity, int packetType) {
        this.userIdentity = userIdentity;
        this.packetType = packetType;
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            try {
                long currentDate = System.currentTimeMillis();
                String key = groupId != null && groupId > 0 ? groupId + "" : userIdentity;
                MessageBaseDTO serverLocation = ChatStorer.SERVER_LOCATION.get(key);

                if (serverLocation == null) {
                    stopService();
                    break;
                }
                long messageDate = serverLocation.getMessageDate();
                String chatServerIP = serverLocation.getChatServerIP();
                int chatServerPort = serverLocation.getChatBindingPort();

                if (ChatSettings.DEBUG) {
                    System.err.println(key + " :: REG SESSION TIME >> " + (currentDate - messageDate));
                }
                if (currentDate - messageDate >= ChatConstants.REGISTRATION_HOLD_DURATION) {
                    stopService();
                    break;
                }

                byte[] packet = packetType == ChatConstants.CHAT_FRIEND_IDEL
                        ? ChatPacketUility.makeFriendTypingORIdelPacket(packetType, ChatSettings.USER_IDENTITY, userIdentity)
                        : ChatPacketUility.makeGroupTypingORIdelPacket(packetType, ChatSettings.USER_IDENTITY, groupId);
                ChatSender.sendChatPacket(packet, chatServerIP, chatServerPort);
                Thread.sleep(INTERVAL);
            } catch (Exception ex) {
            }
        }
    }

    public void stopService() {
        try {
            String key = groupId != null && groupId > 0 ? groupId + "" : userIdentity;
            ChatStorer.SERVER_LOCATION.remove(key);
            ChatStorer.IDLE_THREAD.remove(key);
            running = false;
        } catch (Exception ex) {
            running = false;
        }
    }

    public boolean isRunning() {
        return running;
    }

}
