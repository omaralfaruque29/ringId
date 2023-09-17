/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.communicator;

import com.ipv.chat.ChatSettings;
import com.ipv.chat.udp.IMInetAddress;
import com.ipv.chat.udp.IMDatagramPacket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatSender {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatSender.class);

    public static void sendRegisterPacket(byte[] sendingBytePacket, String chatServerIP, int chatResgisterPort) {
        try {
            IMDatagramPacket finalPacket = new IMDatagramPacket(sendingBytePacket, sendingBytePacket.length, new IMInetAddress(InetAddress.getByName(chatServerIP)), chatResgisterPort);
            ChatCommunication.getInstance().send(finalPacket);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Reg. Sending >> " + new String(sendingBytePacket));
            }
        } catch (UnknownHostException e) {
            log.error("HostEception in sendRegisterPacket==>" + e.getMessage());
        } catch (IOException e) {
            log.error("IOException in sendRegisterPacket==>" + e.getMessage());
        }
    }

    public static void sendChatPacket(byte[] sendingBytePacket, String chatServerIP, int chatBindingPort) {
        try {
            IMDatagramPacket finalPacket = new IMDatagramPacket(sendingBytePacket, sendingBytePacket.length, new IMInetAddress(InetAddress.getByName(chatServerIP)), chatBindingPort);
            ChatCommunication.getInstance().send(finalPacket);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Msg. Sending >> " + new String(sendingBytePacket));
            }
        } catch (UnknownHostException e) {
            log.error("UnknownHostException in sendChatPacket==>" + e.getMessage());
        } catch (IOException e) {
            log.error("IOException in sendChatPacket==>" + e.getMessage());
        }
    }

    public static void sendOfflineChatPacket(byte[] sendingBytePacket) {
        try {
            //UdpPacket finalPacket = new UdpPacket(sendingBytePacket, sendingBytePacket.length, new IpAddress(InetAddress.getByName("192.168.1.125")), 1200);
            IMDatagramPacket finalPacket = new IMDatagramPacket(sendingBytePacket, sendingBytePacket.length, new IMInetAddress(InetAddress.getByName(ChatSettings.CHAT_OFFLINE_IP)), ChatSettings.CHAT_OFFLINE_PORT);
            ChatCommunication.getInstance().send(finalPacket);
            if (ChatSettings.DEBUG) {
                System.err.println("Chat Off. Sending >> " + new String(sendingBytePacket));
            }
        } catch (UnknownHostException e) {
            log.error("UnknownHostException in sendOfflineChatPacket==>" + e.getMessage());
        } catch (IOException e) {
            log.error("IOException in sendOfflineChatPacket==>" + e.getMessage());
        }
    }

}
