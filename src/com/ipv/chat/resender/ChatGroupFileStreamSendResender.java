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
import com.ipv.chat.utils.ChatPacketUility;
import com.ipv.chat.utils.ChatUtility;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatGroupFileStreamSendResender extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatGroupFileStreamSendResender.class);
    private String friendIdentity;
    private Long groupId;
    private String packetID;
    private File[] files;
    private int totalChunk;
    private MessageBaseDTO serverLocation;

    public ChatGroupFileStreamSendResender(Long groupId, String friendIdentity, String packetID, File[] files, int totalChunk) {
        this.groupId = groupId;
        this.friendIdentity = friendIdentity;
        this.packetID = packetID;
        this.files = files;
        this.totalChunk = totalChunk;
    }

    @Override
    public void run() {
        try {
            if (ChatStorer.SERVER_LOCATION.get(groupId + "") == null) {
                for (int i = 0; i < 20; i++) {
                    if (ChatStorer.SERVER_LOCATION.get(groupId + "") == null) {
                        Thread.sleep(500);
                    } else {
                        break;
                    }
                }
            }
            serverLocation = ChatStorer.SERVER_LOCATION.get(groupId + "");

            if (serverLocation != null) {
                Map<String, List<byte[]>> packetMap = new ConcurrentHashMap<String, List<byte[]>>();

                for (File file : files) {
                    int chunkIndex = ChatUtility.getChunkNumber(file.getName());
                    byte[] bytes = ChatUtility.readBytes(file);

                    if (bytes != null && bytes.length > 0 && chunkIndex > 0) {
                        String key = friendIdentity + ChatConstants.BROKEN_PACKET_SEPARATOR + packetID + ChatConstants.BROKEN_PACKET_SEPARATOR + chunkIndex;
                        List<byte[]> packets = ChatPacketUility.makeGroupFileStreamSendPacket(ChatConstants.CHAT_GROUP_FILE_STREAM_SEND,
                                packetID,
                                ChatSettings.USER_IDENTITY,
                                friendIdentity,
                                groupId,
                                bytes,
                                chunkIndex,
                                totalChunk);
                        packetMap.put(key, packets);
                    }
                }

                sendFileStreamPacket(packetMap, 20);

                for (int i = 1; i <= ChatConstants.NUMBER_OF_RESEND; i++) {
                    Thread.sleep(ChatConstants.DELAY_OF_RESEND);

                    if (ChatSettings.DEBUG) {
                        System.out.println("CHECKING FILE CHUNK ==> " + packetMap.size());
                    }
                    if (isAllPacketConfirmationReceived(packetMap) == false) {
                        if (ChatSettings.DEBUG) {
                            System.out.println("RETRY FILE CHUNK ==> " + packetMap.size());
                        }
                        sendFileStreamPacket(packetMap, 30);
                    } else {
                        break;
                    }
                }
            }

        } catch (Exception ex) {
          //  ex.printStackTrace();
          log.error("Error in ChatGroupFileStreamSendResender class ==>" + ex.getMessage());  
        }
    }

    private boolean isAllPacketConfirmationReceived(Map<String, List<byte[]>> packetMap) {
        Set<String> keys = packetMap.keySet();
        for (String key : keys) {
            if (ChatStorer.FILE_STREAM_CONFIRMATION.get(key) != null) {
                ChatStorer.FILE_STREAM_CONFIRMATION.remove(key);
                packetMap.remove(key);
            }
        }
        return keys.size() <= 0;
    }

    private void sendFileStreamPacket(Map<String, List<byte[]>> packetMap, int delay) {
        try {
            serverLocation = ChatStorer.SERVER_LOCATION.get(groupId + "");
            if (serverLocation != null) {
                for (Entry<String, List<byte[]>> entrySet : packetMap.entrySet()) {
                    serverLocation.setMessageDate(System.currentTimeMillis());
                    if (ChatSettings.DEBUG) {
                        System.out.println("CHUNK INDEX ==> " + entrySet.getKey());
                    }
                    List<byte[]> packets = entrySet.getValue();
                    for (byte[] packet : packets) {
                        ChatSender.sendChatPacket(packet, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                        Thread.sleep(delay);
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

}
