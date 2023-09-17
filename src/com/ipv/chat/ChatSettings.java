/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat;

import com.ipv.chat.dto.ChatSettingsDTO;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatSettings {

    public static boolean DEBUG = false;
    public static String AUTH_SERVER_IP = "";
    public static int AUTH_SERVER_PORT = 0;
    public static String SESSION_ID = "";
    public static String CHAT_OFFLINE_IP = "";
    public static int CHAT_OFFLINE_PORT = 0;
    public static String USER_IDENTITY = "";
    public static String USER_FULLNAME = "";
    public static int CHAT_LOCAL_PORT = 9876;
    public static int BUFFER_SIZE = 2048;
    public static int BROKEN_PACKET_SIZE = 512;
    public static int CHAT_PLATFORM = ChatConstants.PLATFORM_DESKTOP;
    public static int FILE_CHUNK_SIZE = 5120;
    public static long OFFLINE_SERVER_TIME_DIFF = 0;

    public static void reset() {
        AUTH_SERVER_IP = "";
        AUTH_SERVER_PORT = 0;
        SESSION_ID = "";
        CHAT_OFFLINE_IP = "";
        CHAT_OFFLINE_PORT = 0;
        USER_IDENTITY = "";
        USER_FULLNAME = "";
        CHAT_LOCAL_PORT = 9876;
        BUFFER_SIZE = 2048;
        BROKEN_PACKET_SIZE = 512;
        FILE_CHUNK_SIZE = 5120;
        CHAT_PLATFORM = ChatConstants.PLATFORM_DESKTOP;
        OFFLINE_SERVER_TIME_DIFF = 0;
    }
    
    public static void set(ChatSettingsDTO entity) {
        AUTH_SERVER_IP = entity.getAuthServerIP();
        AUTH_SERVER_PORT = entity.getAuthServerPort();
        SESSION_ID = entity.getSessionID();
        CHAT_OFFLINE_IP = entity.getChatOfflineIP();
        CHAT_OFFLINE_PORT = entity.getChatOfflinePort();
        USER_IDENTITY = entity.getUserIdentity();
        USER_FULLNAME = entity.getFullName();
        CHAT_LOCAL_PORT = entity.getChatLocalPort();
        BUFFER_SIZE = entity.getBufferSize();
        BROKEN_PACKET_SIZE = entity.getBrokenPacketSize();
        CHAT_PLATFORM = entity.getChatPlatform();
        FILE_CHUNK_SIZE = entity.getFileChunkSize();
        OFFLINE_SERVER_TIME_DIFF = 0;
    }

}
