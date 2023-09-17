/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.storers;

import com.ipv.chat.resender.ChatIdleResender;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.ipv.chat.dto.MessageBaseDTO;
import java.util.Set;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatStorer {
    
    public static Map<String, MessageBaseDTO> PACKET_CONTAINER = new ConcurrentHashMap<String, MessageBaseDTO>();
    public static Map<String, MessageBaseDTO> PACKET_CONFIRMATION = new ConcurrentHashMap<String, MessageBaseDTO>();
    public static Map<String, MessageBaseDTO> FILE_STREAM_CONFIRMATION = new ConcurrentHashMap<String, MessageBaseDTO>();
    public static Map<String, MessageBaseDTO> SERVER_LOCATION = new ConcurrentHashMap<String, MessageBaseDTO>();
    public static Map<String, ChatIdleResender> IDLE_THREAD = new ConcurrentHashMap<String, ChatIdleResender>();
    public static Map<String, Set<MessageBaseDTO>> TEMP_MULTIPLE_PACKET = new ConcurrentHashMap<String, Set<MessageBaseDTO>>();
    public static Map<String, Map<Integer, MessageBaseDTO>> TEMP_FILE_STREAM_PACKET = new ConcurrentHashMap<String, Map<Integer, MessageBaseDTO>>();
   
    public static void clearAll() {
        PACKET_CONTAINER.clear();
        PACKET_CONFIRMATION.clear();
        SERVER_LOCATION.clear();
        IDLE_THREAD.clear();
        TEMP_MULTIPLE_PACKET.clear();
        TEMP_FILE_STREAM_PACKET.clear();
        FILE_STREAM_CONFIRMATION.clear();
    }

}
