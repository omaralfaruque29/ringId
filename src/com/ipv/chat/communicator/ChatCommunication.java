/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.communicator;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.ChatSettings;
import com.ipv.chat.dto.ChatSettingsDTO;
import com.ipv.chat.listener.ChatActionListener;
import com.ipv.chat.listener.PacketReceivedListener;
import java.io.IOException;
import com.ipv.chat.parser.ChatMsgParser;
import com.ipv.chat.service.ChatService;
import com.ipv.chat.storers.ChatStorer;
import com.ipv.chat.udp.IMDatagramPacket;
import com.ipv.chat.udp.IMDatagramSocket;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatCommunication implements PacketReceivedListener {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatCommunication.class);
    private IMDatagramSocket chatSocket;
    public static ChatCommunication instance;
    private ChatActionListener chatActionListener = null;
    private ChatReceiver chatReceiver;
    private boolean flag = false;

    public ChatCommunication() {

    }

    public static ChatCommunication getInstance() {
        if (instance == null) {
            instance = new ChatCommunication();
        }
        return instance;
    }

    public void startService(ChatSettingsDTO chatSettingsDTO, ChatActionListener chatActionListener) {
        ChatSettings.set(chatSettingsDTO);
        ChatStorer.clearAll();

        this.chatActionListener = chatActionListener;
        int localPort = ChatSettings.CHAT_LOCAL_PORT;
        flag = true;

        try {
            while (flag) {
                try {
                    chatSocket = new IMDatagramSocket(localPort++);
                    flag = false;
                    break;
                } catch (Exception e) {
                    try {
                        if (chatSocket != null) {
                            chatSocket.close();
                        }
                    } catch (Exception ex) {
                      //  ex.printStackTrace();
                    log.error("Error in ChatCommunication class in chatSocket" + ex.getMessage());
                    }
                }
            }

            chatReceiver = new ChatReceiver(chatSocket, this);
            chatReceiver.start();
        } catch (Exception e) {
        }
    }

    protected void send(IMDatagramPacket packet) throws IOException {
        if (chatReceiver != null) {
            chatReceiver.send(packet);
        } else {
            throw new IOException("Chat Datagram socket is NULL");
        }
    }

    public void stopService() {
        ChatService.unregisterAllChatConversation(ChatConstants.PRESENCE_OFFLINE);

        if (chatReceiver != null) {
            chatReceiver.halt();
        }

        ChatSettings.reset();
        ChatStorer.clearAll();
        instance = null;
    }

    @Override
    public void onPacketReceived(IMDatagramPacket packet) {
        if (packet.getLength() >= 0 && chatActionListener != null) {
            new ChatMsgParser(packet.getData(), chatActionListener).start();
        }
    }
}
