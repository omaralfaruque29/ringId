/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.utils;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.ChatSettings;
import java.io.UnsupportedEncodingException;
import com.ipv.chat.dto.MessageBaseDTO;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.ipv.chat.dto.ChatFileDTO;
import com.ipv.chat.storers.ChatStorer;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatPacketUility {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatPacketUility.class);

    //Register Packet
    public static byte[] makeFriendRegisterPacket(int packetType, String userIdentity, String friendIdentity, String packetID) throws UnsupportedEncodingException {

        byte[] packetIdByte = packetID.getBytes("UTF-8");
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");
        byte[] friendIdentityByte = friendIdentity.getBytes("UTF-8");

        int totalDataLenght = 6 + userIdentityByte.length + friendIdentityByte.length + packetIdByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) packetType;
        data[i++] = (byte) ChatSettings.CHAT_PLATFORM;

        data[i++] = (byte) packetIdByte.length;
        for (int n = 0; n < packetIdByte.length; n++) {
            data[i++] = packetIdByte[n];
        }

        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) friendIdentityByte.length;
        for (int n = 0; n < friendIdentityByte.length; n++) {
            data[i++] = friendIdentityByte[n];
        }

        return data;
    }

    public static byte[] makeGroupRegisterPacket(int packetType, String userIdentity, String fullName, long groupId, String packetID) throws UnsupportedEncodingException {
        byte[] packetIdByte = packetID.getBytes("UTF-8");
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");
        byte[] fullNameByte = fullName.getBytes("UTF-8");
        int fullNameLength = fullNameByte.length;

        if (fullNameLength > 127) {
            fullNameLength = 127;
        }

        int totalDataLenght = 14 + userIdentityByte.length + fullNameLength + packetIdByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) packetType;
        data[i++] = (byte) ChatSettings.CHAT_PLATFORM;

        data[i++] = (byte) packetIdByte.length;
        for (int n = 0; n < packetIdByte.length; n++) {
            data[i++] = packetIdByte[n];
        }

        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) fullNameLength;
        for (int n = 0; n < fullNameLength; n++) {
            data[i++] = fullNameByte[n];
        }

        data[i++] = (byte) (groupId >>> 56);
        data[i++] = (byte) (groupId >>> 48);
        data[i++] = (byte) (groupId >>> 40);
        data[i++] = (byte) (groupId >>> 32);
        data[i++] = (byte) (groupId >>> 24);
        data[i++] = (byte) (groupId >>> 16);
        data[i++] = (byte) (groupId >>> 8);
        data[i++] = (byte) (groupId);

        return data;
    }

    public static byte[] makeFriendUnRegisterPacket(int packetType, String userIdentity, String friendIdentity, int onlineStatus) throws UnsupportedEncodingException {
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");
        byte[] friendIdentityByte = friendIdentity.getBytes("UTF-8");

        int totalDataLenght = 4 + userIdentityByte.length + friendIdentityByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) packetType;

        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) friendIdentityByte.length;
        for (int n = 0; n < friendIdentityByte.length; n++) {
            data[i++] = friendIdentityByte[n];
        }

        data[i++] = (byte) onlineStatus;

        return data;
    }

    public static byte[] makeGroupUnRegisterPacket(int packetType, String userIdentity, long groupId, int onlineStatus) throws UnsupportedEncodingException {
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");

        int totalDataLenght = 11 + userIdentityByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) packetType;

        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) (groupId >>> 56);
        data[i++] = (byte) (groupId >>> 48);
        data[i++] = (byte) (groupId >>> 40);
        data[i++] = (byte) (groupId >>> 32);
        data[i++] = (byte) (groupId >>> 24);
        data[i++] = (byte) (groupId >>> 16);
        data[i++] = (byte) (groupId >>> 8);
        data[i++] = (byte) (groupId);

        data[i++] = (byte) onlineStatus;

        return data;
    }

    //Confirmation Packet
    public static byte[] makeConfirmationPacket(int packetType, String packetID) throws UnsupportedEncodingException {
        byte[] packetIdByte = packetID.getBytes("UTF-8");
        int totalDataLenght = 6 + packetIdByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) packetType;
        data[i++] = (byte) packetIdByte.length;
        for (int n = 0; n < packetIdByte.length; n++) {
            data[i++] = packetIdByte[n];
        }
        return data;
    }

    public static MessageBaseDTO getConfrimationPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        messageDTO.setPacketType(packetType);
        messageDTO.setPacketID(packetId.trim());

        return messageDTO;
    }

    public static MessageBaseDTO getGroupMemberListConfrimationPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;
        
        long groupId = getGroupId(totalRead, recievedBuffer);
        totalRead += 8;

        messageDTO.setPacketType(packetType);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setGroupId(groupId);

        return messageDTO;
    }
    
    public static MessageBaseDTO getOfflineConfrimationPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        long messageDate = getMessageDate(totalRead, recievedBuffer);
        totalRead += 8;

        messageDTO.setPacketType(packetType);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setServerDate(messageDate);

        return messageDTO;
    }

    public static MessageBaseDTO getFriendRegisterConfirmationPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int chatBindingPort = getChatBindingPort(totalRead, recievedBuffer);
        totalRead += 4;
        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, friendIdentityLength, "UTF-8");
        totalRead += friendIdentityLength;

        long messageDate = getMessageDate(totalRead, recievedBuffer);
        totalRead += 8;

        messageDTO.setPacketType(packetType);
        messageDTO.setChatBindingPort(chatBindingPort);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setFriendIdentity(friendIdentity);
        messageDTO.setServerDate(messageDate);
        return messageDTO;
    }

    public static MessageBaseDTO getGroupRegisterConfirmationPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {
        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int chatBindingPort = getChatBindingPort(totalRead, recievedBuffer);
        totalRead += 4;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        long groupId = getGroupId(totalRead, recievedBuffer);
        totalRead += 8;

        long messageDate = getMessageDate(totalRead, recievedBuffer);
        totalRead += 8;

        messageDTO.setPacketType(packetType);
        messageDTO.setChatBindingPort(chatBindingPort);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setGroupId(groupId);
        messageDTO.setServerDate(messageDate);
        return messageDTO;
    }

    public static MessageBaseDTO getFriendUnregisterPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int userIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, userIdentityLength, "UTF-8");
        totalRead += userIdentityLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, friendIdentityLength, "UTF-8");
        totalRead += friendIdentityLength;

        int onlineStatus = recievedBuffer[totalRead];
        totalRead++;

        messageDTO.setPacketType(packetType);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFriendIdentity(friendIdentity);
        messageDTO.setPresence(onlineStatus);
        return messageDTO;
    }

    public static MessageBaseDTO getGroupUnregisterPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {
        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int userIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, userIdentityLength, "UTF-8");
        totalRead += userIdentityLength;

        long groupId = getGroupId(totalRead, recievedBuffer);
        totalRead += 8;

        int onlineStatus = recievedBuffer[totalRead];
        totalRead++;

        messageDTO.setPacketType(packetType);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setGroupId(groupId);
        messageDTO.setPresence(onlineStatus);
        return messageDTO;
    }

    public static byte[] makeFriendMessageDeleteConfirmationPacket(int packetType, String userIdentity, String friendIdentity, String packetID) throws UnsupportedEncodingException {
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");
        byte[] friendIdentityByte = friendIdentity.getBytes("UTF-8");
        byte[] packetIdByte = packetID.getBytes("UTF-8");

        int totalDataLenght = 4 + userIdentityByte.length + friendIdentityByte.length + packetIdByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) packetType;
        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) friendIdentityByte.length;
        for (int n = 0; n < friendIdentityByte.length; n++) {
            data[i++] = friendIdentityByte[n];
        }

        data[i++] = (byte) packetIdByte.length;
        for (int n = 0; n < packetIdByte.length; n++) {
            data[i++] = packetIdByte[n];
        }
        return data;
    }

    public static MessageBaseDTO getFriendMessageDeleteConfirmationPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {
        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, friendIdentityLength, "UTF-8");
        totalRead += friendIdentityLength;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        messageDTO.setPacketType(packetType);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFriendIdentity(friendIdentity);
        messageDTO.setPacketID(packetId.trim());

        return messageDTO;
    }

    public static byte[] makeGroupMessageDeleteConfirmationPacket(int packetType, String userIdentity, String friendIdentity, long groupId, String packetID) throws UnsupportedEncodingException {
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");
        byte[] friendIdentityByte = friendIdentity.getBytes("UTF-8");
        byte[] packetIdByte = packetID.getBytes("UTF-8");

        int totalDataLenght = 13 + userIdentityByte.length + friendIdentityByte.length + packetIdByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) packetType;
        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) friendIdentityByte.length;
        for (int n = 0; n < friendIdentityByte.length; n++) {
            data[i++] = friendIdentityByte[n];
        }

        data[i++] = (byte) packetIdByte.length;
        for (int n = 0; n < packetIdByte.length; n++) {
            data[i++] = packetIdByte[n];
        }

        data[i++] = (byte) (groupId >>> 56);
        data[i++] = (byte) (groupId >>> 48);
        data[i++] = (byte) (groupId >>> 40);
        data[i++] = (byte) (groupId >>> 32);
        data[i++] = (byte) (groupId >>> 24);
        data[i++] = (byte) (groupId >>> 16);
        data[i++] = (byte) (groupId >>> 8);
        data[i++] = (byte) (groupId);

        return data;
    }

    public static MessageBaseDTO getGroupMessageDeleteConfirmationPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {
        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, friendIdentityLength, "UTF-8");
        totalRead += friendIdentityLength;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        long groupId = getGroupId(totalRead, recievedBuffer);
        totalRead += 8;

        messageDTO.setPacketType(packetType);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFriendIdentity(friendIdentity);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setGroupId(groupId);

        return messageDTO;
    }

    public static MessageBaseDTO getGroupDeleteConfirmationPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {
        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        long groupId = getGroupId(totalRead, recievedBuffer);
        totalRead += 8;

        messageDTO.setPacketType(packetType);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setGroupId(groupId);

        return messageDTO;
    }

    public static MessageBaseDTO getFriendSentConfirmationPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, friendIdentityLength, "UTF-8");
        totalRead += friendIdentityLength;

        messageDTO.setPacketType(packetType);
        messageDTO.setPacketID(packetId);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFriendIdentity(friendIdentity);

        return messageDTO;
    }

    public static MessageBaseDTO getGroupSentConfirmationPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        long groupId = getGroupId(totalRead, recievedBuffer);
        totalRead += 8;

        messageDTO.setPacketType(packetType);
        messageDTO.setPacketID(packetId);
        messageDTO.setGroupId(groupId);

        return messageDTO;
    }

    public static byte[] makeFriendFileStreamConfirmationPacket(int packetType, String packetID, String userIdentity, String friendIdentity) throws UnsupportedEncodingException {
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");
        byte[] friendIdentityByte = friendIdentity.getBytes("UTF-8");
        byte[] packetIdByte = packetID.getBytes("UTF-8");

        int inputByteLength = 4 + packetIdByte.length + userIdentityByte.length + friendIdentityByte.length;

        byte[] data = new byte[inputByteLength];
        int i = 0;
        data[i++] = (byte) packetType;

        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) friendIdentityByte.length;
        for (int n = 0; n < friendIdentityByte.length; n++) {
            data[i++] = friendIdentityByte[n];
        }

        data[i++] = (byte) packetIdByte.length;
        for (int n = 0; n < packetIdByte.length; n++) {
            data[i++] = packetIdByte[n];
        }

        return data;
    }

    public static MessageBaseDTO getFriendFileStreamConfirmationPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {
        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, friendIdentityLength, "UTF-8");
        totalRead += friendIdentityLength;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        messageDTO.setPacketType(packetType);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFriendIdentity(friendIdentity);
        messageDTO.setPacketID(packetId.trim());

        return messageDTO;
    }

    public static byte[] makeGroupFileStreamConfirmationPacket(int packetType, String packetID, String userIdentity, String friendIdentity, long groupId) throws UnsupportedEncodingException {
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");
        byte[] friendIdentityByte = friendIdentity.getBytes("UTF-8");
        byte[] packetIdByte = packetID.getBytes("UTF-8");

        int inputByteLength = 12 + packetIdByte.length + userIdentityByte.length + friendIdentityByte.length;

        byte[] data = new byte[inputByteLength];
        int i = 0;
        data[i++] = (byte) packetType;

        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) friendIdentityByte.length;
        for (int n = 0; n < friendIdentityByte.length; n++) {
            data[i++] = friendIdentityByte[n];
        }

        data[i++] = (byte) (groupId >>> 56);
        data[i++] = (byte) (groupId >>> 48);
        data[i++] = (byte) (groupId >>> 40);
        data[i++] = (byte) (groupId >>> 32);
        data[i++] = (byte) (groupId >>> 24);
        data[i++] = (byte) (groupId >>> 16);
        data[i++] = (byte) (groupId >>> 8);
        data[i++] = (byte) (groupId);

        data[i++] = (byte) packetIdByte.length;
        for (int n = 0; n < packetIdByte.length; n++) {
            data[i++] = packetIdByte[n];
        }

        return data;
    }

    public static MessageBaseDTO getGroupFileStreamConfirmationPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {
        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, friendIdentityLength, "UTF-8");
        totalRead += friendIdentityLength;

        long groupId = getGroupId(totalRead, recievedBuffer);
        totalRead += 8;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        messageDTO.setPacketType(packetType);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFriendIdentity(friendIdentity);
        messageDTO.setGroupId(groupId);
        messageDTO.setPacketID(packetId.trim());
        return messageDTO;
    }

    //Friend DeliveredORSeen Packet
    public static byte[] makeFriendDeliveredORSeenPacket(int packetType, String userIdentity, String friendIdentity, String packetID, long messageDate) throws UnsupportedEncodingException {
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");
        byte[] friendIdentityByte = friendIdentity.getBytes("UTF-8");
        byte[] packetIdByte = packetID.getBytes("UTF-8");

        int totalDataLenght = 12 + userIdentityByte.length + friendIdentityByte.length + packetIdByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) packetType;
        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) friendIdentityByte.length;
        for (int n = 0; n < friendIdentityByte.length; n++) {
            data[i++] = friendIdentityByte[n];
        }

        data[i++] = (byte) packetIdByte.length;
        for (int n = 0; n < packetIdByte.length; n++) {
            data[i++] = packetIdByte[n];
        }

        data[i++] = (byte) (messageDate >>> 56);
        data[i++] = (byte) (messageDate >>> 48);
        data[i++] = (byte) (messageDate >>> 40);
        data[i++] = (byte) (messageDate >>> 32);
        data[i++] = (byte) (messageDate >>> 24);
        data[i++] = (byte) (messageDate >>> 16);
        data[i++] = (byte) (messageDate >>> 8);
        data[i++] = (byte) (messageDate);

        return data;
    }

    public static MessageBaseDTO getFriendDeliveredORSeenPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, friendIdentityLength, "UTF-8");
        totalRead += friendIdentityLength;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        long messageDate = getMessageDate(totalRead, recievedBuffer);
        totalRead += 8;

        messageDTO.setPacketType(packetType);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFriendIdentity(friendIdentity);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setMessageDate(messageDate);

        return messageDTO;
    }

    //Friend TypingORIdel Packet
    public static byte[] makeFriendTypingORIdelPacket(int packetType, String userIdentity, String friendIdentity) throws UnsupportedEncodingException {
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");
        byte[] friendIdentityByte = friendIdentity.getBytes("UTF-8");

        int totalDataLenght = 3 + userIdentityByte.length + friendIdentityByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) packetType;

        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) friendIdentityByte.length;
        for (int n = 0; n < friendIdentityByte.length; n++) {
            data[i++] = friendIdentityByte[n];
        }

        return data;
    }

    public static MessageBaseDTO getFriendTypingORIdelPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, friendIdentityLength, "UTF-8");
        totalRead += friendIdentityLength;

        messageDTO.setPacketType(packetType);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFriendIdentity(friendIdentity);

        return messageDTO;
    }

    //Friend to Friend Chat Packet Generation and Parcing.
    //public static byte[] makeFriendChatPacket(int packetType, int messageType, int timeOut, float latitude, float longitude, String userIdentity, String friendIdenity, String message, String packetID) {
    public static byte[] makeFriendChatPacket(MessageBaseDTO messageDTO) throws UnsupportedEncodingException {

        byte[] userIdentityByte = messageDTO.getUserIdentity().getBytes("UTF-8");
        byte[] friendIdentityByte = messageDTO.getFriendIdentity().getBytes("UTF-8");
        byte[] messageByte = messageDTO.getMessage().getBytes("UTF-8");
        byte[] packetIdByte = messageDTO.getPacketID().getBytes("UTF-8");
        int packetType = messageDTO.getPacketType();
        int messageType = messageDTO.getMessageType();
        int timeOut = messageDTO.getTimeout();
        float latitude = messageDTO.getLatitude();
        float longitude = messageDTO.getLongitude();
        long messageDate = messageDTO.getMessageDate();

        int inputByteLength = 25 + packetIdByte.length + userIdentityByte.length + friendIdentityByte.length + messageByte.length;
        byte[] data = new byte[inputByteLength];
        int i = 0;
        data[i++] = (byte) packetType;
        data[i++] = (byte) messageType;
        data[i++] = (byte) (timeOut >>> 8);
        data[i++] = (byte) (timeOut);

        int bits = Float.floatToIntBits(latitude);
        data[i++] = (byte) (bits >>> 24);
        data[i++] = (byte) (bits >>> 16);
        data[i++] = (byte) (bits >>> 8);
        data[i++] = (byte) (bits);

        bits = Float.floatToIntBits(longitude);
        data[i++] = (byte) (bits >>> 24);
        data[i++] = (byte) (bits >>> 16);
        data[i++] = (byte) (bits >>> 8);
        data[i++] = (byte) (bits);

        data[i++] = (byte) packetIdByte.length;
        for (int n = 0; n < packetIdByte.length; n++) {
            data[i++] = packetIdByte[n];
        }

        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) friendIdentityByte.length;
        for (int n = 0; n < friendIdentityByte.length; n++) {
            data[i++] = friendIdentityByte[n];
        }

        data[i++] = (byte) (messageByte.length >>> 8);
        data[i++] = (byte) (messageByte.length);

        for (int n = 0; n < messageByte.length; n++) {
            data[i++] = messageByte[n];
        }

        data[i++] = (byte) (messageDate >>> 56);
        data[i++] = (byte) (messageDate >>> 48);
        data[i++] = (byte) (messageDate >>> 40);
        data[i++] = (byte) (messageDate >>> 32);
        data[i++] = (byte) (messageDate >>> 24);
        data[i++] = (byte) (messageDate >>> 16);
        data[i++] = (byte) (messageDate >>> 8);
        data[i++] = (byte) (messageDate);

        return data;
    }

    public static MessageBaseDTO getFriendMessagePacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;
        int messageType = recievedBuffer[totalRead];
        totalRead++;
        int messageTimeout = getTimeout(totalRead, recievedBuffer);
        totalRead += 2;
        float latitude = getLatitude(totalRead, recievedBuffer);
        totalRead += 4;
        float longitude = getLongitude(totalRead, recievedBuffer);
        totalRead += 4;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, friendIdentityLength, "UTF-8");
        totalRead += friendIdentityLength;

        int messageLength = getMessageLength(totalRead, recievedBuffer);
        totalRead += 2;
        String message = new String(recievedBuffer, totalRead, messageLength, "UTF-8");
        totalRead += messageLength;

        long messageDate = getMessageDate(totalRead, recievedBuffer);
        totalRead += 8;

        messageDTO.setPacketType(packetType);
        messageDTO.setMessageType(messageType);
        messageDTO.setTimeout(messageTimeout);
        messageDTO.setLatitude(latitude);
        messageDTO.setLongitude(longitude);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFriendIdentity(friendIdentity);
        messageDTO.setMessage(message);
        messageDTO.setMessageDate(messageDate);
        messageDTO.setSystemDate(messageDate);

        return messageDTO;
    }

    public static Map<String, byte[]> makeFriendChatPacketForBackgroundResend(int packetType, String userIdentity, String friendIdentity, List<MessageBaseDTO> messageDTOs) throws UnsupportedEncodingException {
        int index = 0;
        Map<String, byte[]> packetList = new ConcurrentHashMap<String, byte[]>();
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");
        byte[] friendIdentityByte = friendIdentity.getBytes("UTF-8");

        while (index < messageDTOs.size()) {
            String packetId = ChatUtility.getRandomPacketID();
            byte[] packetIdByte = packetId.getBytes("UTF-8");

            List<byte[]> tempPacketByteList = new ArrayList<byte[]>();
            Set<MessageBaseDTO> tempPacketSet = new HashSet<MessageBaseDTO>();

            int initDataLength = 5 + packetIdByte.length + userIdentityByte.length + friendIdentityByte.length;
            int totalDataLenght = initDataLength;

            while (index < messageDTOs.size()) {
                MessageBaseDTO messageDTO = messageDTOs.get(index);
                int msgType = messageDTO.getMessageType();
                int pType = messageDTO.getPacketType();
                int tOut = messageDTO.getTimeout();
                float lat = messageDTO.getLatitude();
                float log = messageDTO.getLongitude();
                byte[] pIdByte = messageDTO.getPacketID().getBytes("UTF-8");
                byte[] msgByte = messageDTO.getMessage().getBytes("UTF-8");
                long msgDate = messageDTO.getMessageDate();

                int currentByteLength = 23 + pIdByte.length + msgByte.length;

                if (initDataLength + currentByteLength > ChatSettings.BROKEN_PACKET_SIZE) {
                    Map<String, byte[]> brokenPackets = makeFriendBrokenMessagePacket(messageDTO);
                    for (Entry<String, byte[]> entrySet : brokenPackets.entrySet()) {
                        packetList.put(entrySet.getKey(), entrySet.getValue());
                    }
                    index++;
                } else {
                    if ((totalDataLenght + currentByteLength) > ChatSettings.BROKEN_PACKET_SIZE) {
                        break;
                    }

                    byte[] data = new byte[currentByteLength];
                    int i = 0;
                    data[i++] = (byte) pType;
                    data[i++] = (byte) msgType;
                    data[i++] = (byte) (tOut >>> 8);
                    data[i++] = (byte) (tOut);

                    int bits = Float.floatToIntBits(lat);
                    data[i++] = (byte) (bits >>> 24);
                    data[i++] = (byte) (bits >>> 16);
                    data[i++] = (byte) (bits >>> 8);
                    data[i++] = (byte) (bits);

                    bits = Float.floatToIntBits(log);
                    data[i++] = (byte) (bits >>> 24);
                    data[i++] = (byte) (bits >>> 16);
                    data[i++] = (byte) (bits >>> 8);
                    data[i++] = (byte) (bits);

                    data[i++] = (byte) pIdByte.length;
                    for (int n = 0; n < pIdByte.length; n++) {
                        data[i++] = pIdByte[n];
                    }

                    data[i++] = (byte) (msgByte.length >>> 8);
                    data[i++] = (byte) (msgByte.length);
                    for (int n = 0; n < msgByte.length; n++) {
                        data[i++] = msgByte[n];
                    }

                    data[i++] = (byte) (msgDate >>> 56);
                    data[i++] = (byte) (msgDate >>> 48);
                    data[i++] = (byte) (msgDate >>> 40);
                    data[i++] = (byte) (msgDate >>> 32);
                    data[i++] = (byte) (msgDate >>> 24);
                    data[i++] = (byte) (msgDate >>> 16);
                    data[i++] = (byte) (msgDate >>> 8);
                    data[i++] = (byte) (msgDate);

                    tempPacketByteList.add(data);
                    tempPacketSet.add(messageDTO);
                    totalDataLenght += currentByteLength;
                    index++;
                }
            }

            int numberOfMessage = tempPacketByteList.size();
            if (numberOfMessage > 0) {
                byte[] data = new byte[totalDataLenght];
                int i = 0;
                data[i++] = (byte) packetType;

                data[i++] = (byte) packetIdByte.length;
                for (int n = 0; n < packetIdByte.length; n++) {
                    data[i++] = packetIdByte[n];
                }

                data[i++] = (byte) userIdentityByte.length;
                for (int n = 0; n < userIdentityByte.length; n++) {
                    data[i++] = userIdentityByte[n];
                }

                data[i++] = (byte) friendIdentityByte.length;
                for (int n = 0; n < friendIdentityByte.length; n++) {
                    data[i++] = friendIdentityByte[n];
                }

                data[i++] = (byte) (numberOfMessage);

                for (int idx = 0; idx < numberOfMessage; idx++) {
                    byte[] bytes = tempPacketByteList.get(idx);

                    for (int n = 0; n < bytes.length; n++) {
                        data[i++] = bytes[n];
                    }
                }

                packetList.put(packetId, data);
                ChatStorer.TEMP_MULTIPLE_PACKET.put(packetId, tempPacketSet);
            }
        }

        return packetList;
    }

    public static MessageBaseDTO getFriendMultipleMessagePacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int fIdLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, fIdLength, "UTF-8");
        totalRead += fIdLength;

        int numberOfMessage = recievedBuffer[totalRead];
        totalRead++;

        messageDTO.setPacketType(packetType);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFriendIdentity(friendIdentity);
        messageDTO.setNumberOfPacket(numberOfMessage);

        List<MessageBaseDTO> mList = new ArrayList<MessageBaseDTO>();
        for (int inc = 0; inc < numberOfMessage; inc++) {
            try {
                MessageBaseDTO innerDTO = new MessageBaseDTO();
                int pType = recievedBuffer[totalRead];
                totalRead++;
                int messageType = recievedBuffer[totalRead];
                totalRead++;
                int messageTimeout = getTimeout(totalRead, recievedBuffer);
                totalRead += 2;
                float latitude = getLatitude(totalRead, recievedBuffer);
                totalRead += 4;
                float longitude = getLongitude(totalRead, recievedBuffer);
                totalRead += 4;

                int pIDLength = recievedBuffer[totalRead];
                totalRead++;
                String pId = new String(recievedBuffer, totalRead, pIDLength, "UTF-8");
                totalRead += pIDLength;

                int messageLength = getMessageLength(totalRead, recievedBuffer);
                totalRead += 2;
                String message = new String(recievedBuffer, totalRead, messageLength, "UTF-8");
                totalRead += messageLength;

                long messageDate = getMessageDate(totalRead, recievedBuffer);
                totalRead += 8;

                innerDTO.setPacketType(pType);
                innerDTO.setMessageType(messageType);
                innerDTO.setTimeout(messageTimeout);
                innerDTO.setLatitude(latitude);
                innerDTO.setLongitude(longitude);
                innerDTO.setPacketID(pId.trim());
                innerDTO.setUserIdentity(userIdentity);
                innerDTO.setFriendIdentity(friendIdentity);
                innerDTO.setMessage(message);
                innerDTO.setMessageDate(messageDate);
                innerDTO.setSystemDate(messageDate);
                mList.add(innerDTO);
            } catch (Exception ex) {
                //  ex.printStackTrace();
                log.error("Error in ChatPacketUility class ==>" + ex.getMessage());

            }
        }

        messageDTO.setMessageList(mList);
        return messageDTO;
    }

    public static Map<String, byte[]> makeFriendBrokenMessagePacket(MessageBaseDTO messageDTO) throws UnsupportedEncodingException {
        int offset = 0;
        Map<String, byte[]> packetList = new ConcurrentHashMap<String, byte[]>();
        byte[] userIdentityByte = messageDTO.getUserIdentity().getBytes("UTF-8");
        byte[] friendIdentityByte = messageDTO.getFriendIdentity().getBytes("UTF-8");
        byte[] messageByte = messageDTO.getMessage().getBytes("UTF-8");
        byte[] packetIdByte = messageDTO.getPacketID().getBytes("UTF-8");
        int packetType = messageDTO.getPacketType();
        int messageType = messageDTO.getMessageType();
        int timeOut = messageDTO.getTimeout();
        float latitude = messageDTO.getLatitude();
        float longitude = messageDTO.getLongitude();
        long messageDate = messageDTO.getMessageDate();

        int commonByteLength = 25 + packetIdByte.length + userIdentityByte.length + friendIdentityByte.length;
        List<Integer> msgLengthList = new ArrayList<Integer>();

        if (commonByteLength + messageByte.length > ChatSettings.BROKEN_PACKET_SIZE) {
            commonByteLength = 27 + packetIdByte.length + userIdentityByte.length + friendIdentityByte.length;
            int limit = ChatSettings.BROKEN_PACKET_SIZE - commonByteLength;
            int msgLength = messageByte.length;

            while (msgLength > 0) {
                if (msgLength > limit) {
                    msgLengthList.add(limit);
                    msgLength -= limit;
                } else {
                    msgLengthList.add(msgLength);
                    msgLength -= msgLength;
                }
            }
        } else {
            int msgLength = messageByte.length;
            msgLengthList.add(msgLength);
        }

        int numberOfpacket = msgLengthList.size();
        if (numberOfpacket > 1) {
            if (messageDTO.getPacketType() == ChatConstants.CHAT_FRIEND) {
                packetType = ChatConstants.CHAT_FRIEND_BROCKEN_MESSAGE;
            } else if (messageDTO.getPacketType() == ChatConstants.CHAT_FRIEND_CHAT_EDIT) {
                packetType = ChatConstants.CHAT_FRIEND_CHAT_EDIT_BROCKEN_MESSAGE;
            }
        }

        for (int idx = 0; idx < msgLengthList.size(); idx++) {
            int len = msgLengthList.get(idx);
            int inputByteLength = commonByteLength + len;
            int seqNo = idx + 1;
            String brokenPacketID = messageDTO.getPacketID() + (numberOfpacket > 1 ? ChatConstants.BROKEN_PACKET_SEPARATOR + seqNo : "");

            byte[] data = new byte[inputByteLength];
            int i = 0;
            data[i++] = (byte) packetType;
            data[i++] = (byte) messageType;
            data[i++] = (byte) (timeOut >>> 8);
            data[i++] = (byte) (timeOut);

            int bits = Float.floatToIntBits(latitude);
            data[i++] = (byte) (bits >>> 24);
            data[i++] = (byte) (bits >>> 16);
            data[i++] = (byte) (bits >>> 8);
            data[i++] = (byte) (bits);

            bits = Float.floatToIntBits(longitude);
            data[i++] = (byte) (bits >>> 24);
            data[i++] = (byte) (bits >>> 16);
            data[i++] = (byte) (bits >>> 8);
            data[i++] = (byte) (bits);

            data[i++] = (byte) packetIdByte.length;
            for (int n = 0; n < packetIdByte.length; n++) {
                data[i++] = packetIdByte[n];
            }

            data[i++] = (byte) userIdentityByte.length;
            for (int n = 0; n < userIdentityByte.length; n++) {
                data[i++] = userIdentityByte[n];
            }

            data[i++] = (byte) friendIdentityByte.length;
            for (int n = 0; n < friendIdentityByte.length; n++) {
                data[i++] = friendIdentityByte[n];
            }

            if (numberOfpacket > 1) {
                data[i++] = (byte) numberOfpacket;
                data[i++] = (byte) seqNo;
            }

            data[i++] = (byte) (len >>> 8);
            data[i++] = (byte) (len);

            int length = offset + len;
            for (; offset < length; offset++) {
                data[i++] = messageByte[offset];
            }

            data[i++] = (byte) (messageDate >>> 56);
            data[i++] = (byte) (messageDate >>> 48);
            data[i++] = (byte) (messageDate >>> 40);
            data[i++] = (byte) (messageDate >>> 32);
            data[i++] = (byte) (messageDate >>> 24);
            data[i++] = (byte) (messageDate >>> 16);
            data[i++] = (byte) (messageDate >>> 8);
            data[i++] = (byte) (messageDate);
            packetList.put(brokenPacketID, data);
        }

        return packetList;
    }

    public static MessageBaseDTO getFriendBrokenMessagePacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;
        int messageType = recievedBuffer[totalRead];
        totalRead++;
        int messageTimeout = getTimeout(totalRead, recievedBuffer);
        totalRead += 2;
        float latitude = getLatitude(totalRead, recievedBuffer);
        totalRead += 4;
        float longitude = getLongitude(totalRead, recievedBuffer);
        totalRead += 4;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, friendIdentityLength, "UTF-8");
        totalRead += friendIdentityLength;

        int numberOfpacket = recievedBuffer[totalRead];
        totalRead++;

        int seqNo = recievedBuffer[totalRead];
        totalRead++;

        int messageLength = getMessageLength(totalRead, recievedBuffer);
        totalRead += 2;
        String message = new String(recievedBuffer, totalRead, messageLength, "UTF-8");
        totalRead += messageLength;

        long messageDate = getMessageDate(totalRead, recievedBuffer);
        totalRead += 8;

        messageDTO.setPacketType(packetType);
        messageDTO.setMessageType(messageType);
        messageDTO.setTimeout(messageTimeout);
        messageDTO.setLatitude(latitude);
        messageDTO.setLongitude(longitude);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFriendIdentity(friendIdentity);
        messageDTO.setMessage(message);
        messageDTO.setMessageDate(messageDate);
        messageDTO.setSystemDate(messageDate);
        messageDTO.setNumberOfPacket(numberOfpacket);
        messageDTO.setSequenceNumber(seqNo);

        return messageDTO;
    }

    //Group DeliveredORSeen Packet
    public static byte[] makeGroupDeliveredORSeenPacket(int packetType, String userIdentity, String friendIdentity, long groupId, String packetID, long messageDate) throws UnsupportedEncodingException {
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");
        byte[] friendIdentityByte = friendIdentity.getBytes("UTF-8");
        byte[] packetIdByte = packetID.getBytes("UTF-8");

        int totalDataLenght = 21 + userIdentityByte.length + friendIdentityByte.length + packetIdByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) packetType;
        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) friendIdentityByte.length;
        for (int n = 0; n < friendIdentityByte.length; n++) {
            data[i++] = friendIdentityByte[n];
        }

        data[i++] = (byte) packetIdByte.length;
        for (int n = 0; n < packetIdByte.length; n++) {
            data[i++] = packetIdByte[n];
        }

        data[i++] = (byte) (groupId >>> 56);
        data[i++] = (byte) (groupId >>> 48);
        data[i++] = (byte) (groupId >>> 40);
        data[i++] = (byte) (groupId >>> 32);
        data[i++] = (byte) (groupId >>> 24);
        data[i++] = (byte) (groupId >>> 16);
        data[i++] = (byte) (groupId >>> 8);
        data[i++] = (byte) (groupId);

        data[i++] = (byte) (messageDate >>> 56);
        data[i++] = (byte) (messageDate >>> 48);
        data[i++] = (byte) (messageDate >>> 40);
        data[i++] = (byte) (messageDate >>> 32);
        data[i++] = (byte) (messageDate >>> 24);
        data[i++] = (byte) (messageDate >>> 16);
        data[i++] = (byte) (messageDate >>> 8);
        data[i++] = (byte) (messageDate);

        return data;
    }

    public static MessageBaseDTO getGroupDeliveredORSeenPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, friendIdentityLength, "UTF-8");
        totalRead += friendIdentityLength;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        long groupId = getGroupId(totalRead, recievedBuffer);
        totalRead += 8;

        long messageDate = getMessageDate(totalRead, recievedBuffer);
        totalRead += 8;

        messageDTO.setPacketType(packetType);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFriendIdentity(friendIdentity);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setGroupId(groupId);
        messageDTO.setMessageDate(messageDate);

        return messageDTO;
    }

    //Group TypingORIdel Packet
    public static byte[] makeGroupTypingORIdelPacket(int packetType, String userIdentity, long groupId) throws UnsupportedEncodingException {
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");

        int totalDataLenght = 11 + userIdentityByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) packetType;
        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) (groupId >>> 56);
        data[i++] = (byte) (groupId >>> 48);
        data[i++] = (byte) (groupId >>> 40);
        data[i++] = (byte) (groupId >>> 32);
        data[i++] = (byte) (groupId >>> 24);
        data[i++] = (byte) (groupId >>> 16);
        data[i++] = (byte) (groupId >>> 8);
        data[i++] = (byte) (groupId);

        return data;
    }

    public static MessageBaseDTO getGroupTypingORIdelPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        long groupId = getGroupId(totalRead, recievedBuffer);
        totalRead += 8;

        messageDTO.setPacketType(packetType);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setGroupId(groupId);

        return messageDTO;
    }

    //Group to Friend Chat Packet Generation and Parcing.
    //public static byte[] makeGroupChatPacket(int packetType, int messageType, int timeOut, float latitude, float longitude, String groupName, String userIdentity, String fullName, String message, String packetID) {
    public static byte[] makeGroupChatPacket(MessageBaseDTO messageDTO) throws UnsupportedEncodingException {

        byte[] userIdentityByte = messageDTO.getUserIdentity().getBytes("UTF-8");
        byte[] fullNameByte = messageDTO.getFullName().getBytes("UTF-8");
        byte[] messageByte = messageDTO.getMessage().getBytes("UTF-8");
        byte[] packetIdByte = messageDTO.getPacketID().getBytes("UTF-8");
        int packetType = messageDTO.getPacketType();
        int messageType = messageDTO.getMessageType();
        long groupId = messageDTO.getGroupId();
        int timeOut = messageDTO.getTimeout();
        float latitude = messageDTO.getLatitude();
        float longitude = messageDTO.getLongitude();
        long messageDate = messageDTO.getMessageDate();

        int fullNameLength = fullNameByte.length;
        if (fullNameLength > 127) {
            fullNameLength = 127;
        }

        int inputByteLength = 33 + packetIdByte.length + userIdentityByte.length + messageByte.length + fullNameLength;
        byte[] data = new byte[inputByteLength];
        int i = 0;
        data[i++] = (byte) packetType;
        data[i++] = (byte) messageType;
        data[i++] = (byte) (timeOut >>> 8);
        data[i++] = (byte) (timeOut);

        int bits = Float.floatToIntBits(latitude);
        data[i++] = (byte) (bits >>> 24);
        data[i++] = (byte) (bits >>> 16);
        data[i++] = (byte) (bits >>> 8);
        data[i++] = (byte) (bits);

        bits = Float.floatToIntBits(longitude);
        data[i++] = (byte) (bits >>> 24);
        data[i++] = (byte) (bits >>> 16);
        data[i++] = (byte) (bits >>> 8);
        data[i++] = (byte) (bits);

        data[i++] = (byte) packetIdByte.length;
        for (int n = 0; n < packetIdByte.length; n++) {
            data[i++] = packetIdByte[n];
        }

        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) fullNameLength;
        for (int n = 0; n < fullNameLength; n++) {
            data[i++] = fullNameByte[n];
        }

        data[i++] = (byte) (groupId >>> 56);
        data[i++] = (byte) (groupId >>> 48);
        data[i++] = (byte) (groupId >>> 40);
        data[i++] = (byte) (groupId >>> 32);
        data[i++] = (byte) (groupId >>> 24);
        data[i++] = (byte) (groupId >>> 16);
        data[i++] = (byte) (groupId >>> 8);
        data[i++] = (byte) (groupId);

        data[i++] = (byte) (messageByte.length >>> 8);
        data[i++] = (byte) (messageByte.length);

        for (int n = 0; n < messageByte.length; n++) {
            data[i++] = messageByte[n];
        }

        data[i++] = (byte) (messageDate >>> 56);
        data[i++] = (byte) (messageDate >>> 48);
        data[i++] = (byte) (messageDate >>> 40);
        data[i++] = (byte) (messageDate >>> 32);
        data[i++] = (byte) (messageDate >>> 24);
        data[i++] = (byte) (messageDate >>> 16);
        data[i++] = (byte) (messageDate >>> 8);
        data[i++] = (byte) (messageDate);

        return data;
    }

    public static MessageBaseDTO getGroupMessagePacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;
        int messageType = recievedBuffer[totalRead];
        totalRead++;
        int messageTimeout = getTimeout(totalRead, recievedBuffer);
        totalRead += 2;
        float latitude = getLatitude(totalRead, recievedBuffer);
        totalRead += 4;
        float longitude = getLongitude(totalRead, recievedBuffer);
        totalRead += 4;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int fullNameLength = recievedBuffer[totalRead];
        totalRead++;
        String fullName = new String(recievedBuffer, totalRead, fullNameLength, "UTF-8");
        totalRead += fullNameLength;

        long groupId = getGroupId(totalRead, recievedBuffer);
        totalRead += 8;

        int messageLength = getMessageLength(totalRead, recievedBuffer);
        totalRead += 2;
        String message = new String(recievedBuffer, totalRead, messageLength, "UTF-8");
        totalRead += messageLength;

        long messageDate = getMessageDate(totalRead, recievedBuffer);
        totalRead += 8;

        messageDTO.setPacketType(packetType);
        messageDTO.setMessageType(messageType);
        messageDTO.setTimeout(messageTimeout);
        messageDTO.setLatitude(latitude);
        messageDTO.setLongitude(longitude);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFullName(fullName);
        messageDTO.setGroupId(groupId);
        messageDTO.setMessage(message);
        messageDTO.setMessageDate(messageDate);
        messageDTO.setSystemDate(messageDate);

        return messageDTO;
    }

    public static Map<String, byte[]> makeGroupBrokenMessagePacket(MessageBaseDTO messageDTO) throws UnsupportedEncodingException {
        int offset = 0;
        Map<String, byte[]> packetList = new ConcurrentHashMap<String, byte[]>();
        byte[] userIdentityByte = messageDTO.getUserIdentity().getBytes("UTF-8");
        byte[] fullNameByte = messageDTO.getFullName().getBytes("UTF-8");
        byte[] messageByte = messageDTO.getMessage().getBytes("UTF-8");
        byte[] packetIdByte = messageDTO.getPacketID().getBytes("UTF-8");
        int packetType = messageDTO.getPacketType();
        int messageType = messageDTO.getMessageType();
        long groupId = messageDTO.getGroupId();
        int timeOut = messageDTO.getTimeout();
        float latitude = messageDTO.getLatitude();
        float longitude = messageDTO.getLongitude();
        long messageDate = messageDTO.getMessageDate();

        int fullNameLength = fullNameByte.length;
        if (fullNameLength > 127) {
            fullNameLength = 127;
        }

        int commonByteLength = 33 + packetIdByte.length + userIdentityByte.length + fullNameLength;
        List<Integer> msgLengthList = new ArrayList<Integer>();

        if (commonByteLength + messageByte.length > ChatSettings.BROKEN_PACKET_SIZE) {
            commonByteLength = 35 + packetIdByte.length + userIdentityByte.length + fullNameLength;
            int limit = ChatSettings.BROKEN_PACKET_SIZE - commonByteLength;
            int msgLength = messageByte.length;

            while (msgLength > 0) {
                if (msgLength > limit) {
                    msgLengthList.add(limit);
                    msgLength -= limit;
                } else {
                    msgLengthList.add(msgLength);
                    msgLength -= msgLength;
                }
            }
        } else {
            int msgLength = messageByte.length;
            msgLengthList.add(msgLength);
        }

        int numberOfpacket = msgLengthList.size();
        if (numberOfpacket > 1) {
            if (messageDTO.getPacketType() == ChatConstants.CHAT_GROUP) {
                packetType = ChatConstants.CHAT_GROUP_BROCKEN_MESSAGE;
            } else if (messageDTO.getPacketType() == ChatConstants.CHAT_GROUP_CHAT_EDIT) {
                packetType = ChatConstants.CHAT_GROUP_CHAT_EDIT_BROCKEN_MESSAGE;
            }
        }

        for (int idx = 0; idx < msgLengthList.size(); idx++) {

            int len = msgLengthList.get(idx);
            int inputByteLength = commonByteLength + len;
            int seqNo = idx + 1;
            String brokenPacketID = messageDTO.getPacketID() + (numberOfpacket > 1 ? ChatConstants.BROKEN_PACKET_SEPARATOR + seqNo : "");

            byte[] data = new byte[inputByteLength];
            int i = 0;
            data[i++] = (byte) packetType;
            data[i++] = (byte) messageType;
            data[i++] = (byte) (timeOut >>> 8);
            data[i++] = (byte) (timeOut);

            int bits = Float.floatToIntBits(latitude);
            data[i++] = (byte) (bits >>> 24);
            data[i++] = (byte) (bits >>> 16);
            data[i++] = (byte) (bits >>> 8);
            data[i++] = (byte) (bits);

            bits = Float.floatToIntBits(longitude);
            data[i++] = (byte) (bits >>> 24);
            data[i++] = (byte) (bits >>> 16);
            data[i++] = (byte) (bits >>> 8);
            data[i++] = (byte) (bits);

            data[i++] = (byte) packetIdByte.length;
            for (int n = 0; n < packetIdByte.length; n++) {
                data[i++] = packetIdByte[n];
            }

            data[i++] = (byte) userIdentityByte.length;
            for (int n = 0; n < userIdentityByte.length; n++) {
                data[i++] = userIdentityByte[n];
            }

            data[i++] = (byte) fullNameByte.length;
            for (int n = 0; n < fullNameByte.length; n++) {
                data[i++] = fullNameByte[n];
            }

            data[i++] = (byte) (groupId >>> 56);
            data[i++] = (byte) (groupId >>> 48);
            data[i++] = (byte) (groupId >>> 40);
            data[i++] = (byte) (groupId >>> 32);
            data[i++] = (byte) (groupId >>> 24);
            data[i++] = (byte) (groupId >>> 16);
            data[i++] = (byte) (groupId >>> 8);
            data[i++] = (byte) (groupId);

            if (numberOfpacket > 1) {
                data[i++] = (byte) numberOfpacket;
                data[i++] = (byte) seqNo;
            }

            data[i++] = (byte) (len >>> 8);
            data[i++] = (byte) (len);

            int length = offset + len;
            for (; offset < length; offset++) {
                data[i++] = messageByte[offset];
            }

            data[i++] = (byte) (messageDate >>> 56);
            data[i++] = (byte) (messageDate >>> 48);
            data[i++] = (byte) (messageDate >>> 40);
            data[i++] = (byte) (messageDate >>> 32);
            data[i++] = (byte) (messageDate >>> 24);
            data[i++] = (byte) (messageDate >>> 16);
            data[i++] = (byte) (messageDate >>> 8);
            data[i++] = (byte) (messageDate);
            packetList.put(brokenPacketID, data);
        }

        return packetList;
    }

    public static MessageBaseDTO getGroupBrokenMessagePacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;
        int messageType = recievedBuffer[totalRead];
        totalRead++;
        int messageTimeout = getTimeout(totalRead, recievedBuffer);
        totalRead += 2;
        float latitude = getLatitude(totalRead, recievedBuffer);
        totalRead += 4;
        float longitude = getLongitude(totalRead, recievedBuffer);
        totalRead += 4;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int fullNameLength = recievedBuffer[totalRead];
        totalRead++;
        String fullName = new String(recievedBuffer, totalRead, fullNameLength, "UTF-8");
        totalRead += fullNameLength;

        long groupId = getGroupId(totalRead, recievedBuffer);
        totalRead += 8;

        int numberOfpacket = recievedBuffer[totalRead];
        totalRead++;

        int seqNo = recievedBuffer[totalRead];
        totalRead++;

        int messageLength = getMessageLength(totalRead, recievedBuffer);
        totalRead += 2;
        String message = new String(recievedBuffer, totalRead, messageLength, "UTF-8");
        totalRead += messageLength;

        long messageDate = getMessageDate(totalRead, recievedBuffer);
        totalRead += 8;

        messageDTO.setPacketType(packetType);
        messageDTO.setMessageType(messageType);
        messageDTO.setTimeout(messageTimeout);
        messageDTO.setLatitude(latitude);
        messageDTO.setLongitude(longitude);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFullName(fullName);
        messageDTO.setGroupId(groupId);
        messageDTO.setMessage(message);
        messageDTO.setMessageDate(messageDate);
        messageDTO.setSystemDate(messageDate);
        messageDTO.setNumberOfPacket(numberOfpacket);
        messageDTO.setSequenceNumber(seqNo);

        return messageDTO;
    }

    //Offline Message
    public static byte[] makeOfflineRequestPacket(int packetType, String sessionId, String packetID) throws UnsupportedEncodingException {
        byte[] sessionIdByte = sessionId.getBytes("UTF-8");
        byte[] packetIDByte = packetID.getBytes("UTF-8");

        int totalDataLenght = 3 + sessionIdByte.length + packetIDByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) packetType;
        data[i++] = (byte) packetIDByte.length;
        for (int n = 0; n < packetIDByte.length; n++) {
            data[i++] = packetIDByte[n];
        }
        data[i++] = (byte) sessionIdByte.length;
        for (int n = 0; n < sessionIdByte.length; n++) {
            data[i++] = sessionIdByte[n];
        }

        return data;
    }

    public static MessageBaseDTO getFriendOfflineMessage(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int numberOfPacket = recievedBuffer[totalRead];
        totalRead++;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        messageDTO.setPacketType(packetType);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setNumberOfPacket(numberOfPacket);

        List<MessageBaseDTO> mList = new ArrayList<MessageBaseDTO>();
        for (int inc = 0; inc < numberOfPacket; inc++) {
            try {
                MessageBaseDTO innerDTO = new MessageBaseDTO();

                int pType = recievedBuffer[totalRead];
                totalRead++;
                int messageType = recievedBuffer[totalRead];
                totalRead++;
                int messageTimeout = getTimeout(totalRead, recievedBuffer);
                totalRead += 2;
                float latitude = getLatitude(totalRead, recievedBuffer);
                totalRead += 4;
                float longitude = getLongitude(totalRead, recievedBuffer);
                totalRead += 4;

                int uIdLength = recievedBuffer[totalRead];
                totalRead++;
                String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
                totalRead += uIdLength;

                int totalBrokenPacket = recievedBuffer[totalRead];
                totalRead++;

                int seqNo = recievedBuffer[totalRead];
                totalRead++;
                int messageLength = getMessageLength(totalRead, recievedBuffer);
                totalRead += 2;
                String message = new String(recievedBuffer, totalRead, messageLength, "UTF-8");
                totalRead += messageLength;

                long messageDate = getMessageDate(totalRead, recievedBuffer);
                totalRead += 8;

                int msgPacketIDLength = recievedBuffer[totalRead];
                totalRead++;
                String msgPacketID = new String(recievedBuffer, totalRead, msgPacketIDLength, "UTF-8");
                totalRead += msgPacketIDLength;

                innerDTO.setPacketType(pType);
                innerDTO.setMessageType(messageType);
                innerDTO.setTimeout(messageTimeout);
                innerDTO.setLatitude(latitude);
                innerDTO.setLongitude(longitude);
                innerDTO.setUserIdentity(userIdentity);
                innerDTO.setNumberOfPacket(totalBrokenPacket);
                innerDTO.setSequenceNumber(seqNo);
                innerDTO.setMessage(message);
                innerDTO.setMessageDate(messageDate);
                innerDTO.setSystemDate(messageDate);
                innerDTO.setPacketID(msgPacketID.trim());
                mList.add(innerDTO);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        messageDTO.setMessageList(mList);
        return messageDTO;
    }

    public static MessageBaseDTO getGroupOfflineMessage(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int numberOfPacket = recievedBuffer[totalRead];
        totalRead++;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        messageDTO.setPacketType(packetType);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setNumberOfPacket(numberOfPacket);

        List<MessageBaseDTO> mList = new ArrayList<MessageBaseDTO>();
        for (int inc = 0; inc < numberOfPacket; inc++) {
            MessageBaseDTO innerDTO = new MessageBaseDTO();

            int pType = recievedBuffer[totalRead];
            totalRead++;
            int messageType = recievedBuffer[totalRead];
            totalRead++;
            int messageTimeout = getTimeout(totalRead, recievedBuffer);
            totalRead += 2;
            float latitude = getLatitude(totalRead, recievedBuffer);
            totalRead += 4;
            float longitude = getLongitude(totalRead, recievedBuffer);
            totalRead += 4;

            int uIdLength = recievedBuffer[totalRead];
            totalRead++;
            String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
            totalRead += uIdLength;

            int fullNameLength = recievedBuffer[totalRead];
            totalRead++;
            String fullName = new String(recievedBuffer, totalRead, fullNameLength, "UTF-8");
            totalRead += fullNameLength;

            long groupId = getGroupId(totalRead, recievedBuffer);
            totalRead += 8;

            int totalBrokenPacket = recievedBuffer[totalRead];
            totalRead++;

            int seqNo = recievedBuffer[totalRead];
            totalRead++;
            int messageLength = getMessageLength(totalRead, recievedBuffer);
            totalRead += 2;
            String message = new String(recievedBuffer, totalRead, messageLength, "UTF-8");
            totalRead += messageLength;

            long messageDate = getMessageDate(totalRead, recievedBuffer);
            totalRead += 8;

            int msgPacketIDLength = recievedBuffer[totalRead];
            totalRead++;
            String msgPacketID = new String(recievedBuffer, totalRead, msgPacketIDLength, "UTF-8");
            totalRead += msgPacketIDLength;

            innerDTO.setPacketType(pType);
            innerDTO.setMessageType(messageType);
            innerDTO.setTimeout(messageTimeout);
            innerDTO.setLatitude(latitude);
            innerDTO.setLongitude(longitude);
            innerDTO.setUserIdentity(userIdentity);
            innerDTO.setFullName(fullName);
            innerDTO.setGroupId(groupId);
            innerDTO.setNumberOfPacket(totalBrokenPacket);
            innerDTO.setSequenceNumber(seqNo);
            innerDTO.setMessage(message);
            innerDTO.setMessageDate(messageDate);
            innerDTO.setSystemDate(messageDate);
            innerDTO.setPacketID(msgPacketID.trim());
            mList.add(innerDTO);
        }
        messageDTO.setMessageList(mList);
        return messageDTO;
    }

    //Friend Chat Delete
    public static byte[] makeFriendMessageDeletePacket(int packetType, String userIdentity, String friendIdentity, String packetID) throws UnsupportedEncodingException {
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");
        byte[] friendIdentityByte = friendIdentity.getBytes("UTF-8");
        byte[] packetIdByte = packetID.getBytes("UTF-8");

        int totalDataLenght = 4 + userIdentityByte.length + friendIdentityByte.length + packetIdByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) packetType;
        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) friendIdentityByte.length;
        for (int n = 0; n < friendIdentityByte.length; n++) {
            data[i++] = friendIdentityByte[n];
        }

        data[i++] = (byte) packetIdByte.length;
        for (int n = 0; n < packetIdByte.length; n++) {
            data[i++] = packetIdByte[n];
        }
        return data;
    }

    public static MessageBaseDTO getFriendMessageDeletePacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, friendIdentityLength, "UTF-8");
        totalRead += friendIdentityLength;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        messageDTO.setPacketType(packetType);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFriendIdentity(friendIdentity);
        messageDTO.setPacketID(packetId.trim());

        return messageDTO;
    }

    public static Map<String, byte[]> makeFriendMultipleMessageDeletePacket(int packetType, String userIdentity, String friendIdentity, List<String> packetIDs) throws UnsupportedEncodingException {
        int index = 0;
        Map<String, byte[]> packetList = new ConcurrentHashMap<String, byte[]>();
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");
        byte[] friendIdentityByte = friendIdentity.getBytes("UTF-8");

        while (index < packetIDs.size()) {
            String packetId = ChatUtility.getRandomPacketID();
            byte[] packetIdByte = packetId.getBytes("UTF-8");

            int totalDataLenght = 5 + userIdentityByte.length + friendIdentityByte.length + packetIdByte.length;

            List<byte[]> tempPacketIdList = new ArrayList<byte[]>();
            int countPacket = 0;

            while (index < packetIDs.size()) {
                String pId = packetIDs.get(index);
                byte[] pIdByte = pId.getBytes("UTF-8");
                int pIdLength = pIdByte.length;

                if ((1 + totalDataLenght + pIdLength) > ChatSettings.BROKEN_PACKET_SIZE) {
                    break;
                }

                tempPacketIdList.add(pIdByte);
                totalDataLenght += (1 + pIdLength);
                countPacket++;
                index++;
            }

            byte[] data = new byte[totalDataLenght];
            int i = 0;
            data[i++] = (byte) packetType;

            data[i++] = (byte) userIdentityByte.length;
            for (int n = 0; n < userIdentityByte.length; n++) {
                data[i++] = userIdentityByte[n];
            }

            data[i++] = (byte) friendIdentityByte.length;
            for (int n = 0; n < friendIdentityByte.length; n++) {
                data[i++] = friendIdentityByte[n];
            }

            data[i++] = (byte) packetIdByte.length;
            for (int n = 0; n < packetIdByte.length; n++) {
                data[i++] = packetIdByte[n];
            }

            data[i++] = (byte) (countPacket);

            for (int idx = 0; idx < countPacket; idx++) {
                byte[] pIdByte = tempPacketIdList.get(idx);

                data[i++] = (byte) pIdByte.length;
                for (int n = 0; n < pIdByte.length; n++) {
                    data[i++] = pIdByte[n];
                }
            }

            packetList.put(packetId, data);
        }

        return packetList;
    }

    public static MessageBaseDTO getFriendMultipleMessageDeletePacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int fIdLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, fIdLength, "UTF-8");
        totalRead += fIdLength;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        int numberOfPacket = recievedBuffer[totalRead];
        totalRead++;

        messageDTO.setPacketType(packetType);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFriendIdentity(friendIdentity);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setNumberOfPacket(numberOfPacket);

        List<MessageBaseDTO> mList = new ArrayList<MessageBaseDTO>();
        for (int inc = 0; inc < numberOfPacket; inc++) {
            try {
                MessageBaseDTO innerDTO = new MessageBaseDTO();

                int pIDLength = recievedBuffer[totalRead];
                totalRead++;
                String pId = new String(recievedBuffer, totalRead, pIDLength, "UTF-8");
                totalRead += pIDLength;

                innerDTO.setPacketID(pId);
                mList.add(innerDTO);
            } catch (Exception ex) {
                // ex.printStackTrace();
                log.error("Error in ChatPacketUility class ==>" + ex.getMessage());
            }
        }

        messageDTO.setMessageList(mList);
        return messageDTO;
    }

    //Tag Chat Delete
    public static byte[] makeGroupMessageDeletePacket(int packetType, String userIdentity, long groupId, String packetID) throws UnsupportedEncodingException {
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");
        byte[] packetIdByte = packetID.getBytes("UTF-8");

        int totalDataLenght = 12 + userIdentityByte.length + packetIdByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) packetType;
        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) packetIdByte.length;
        for (int n = 0; n < packetIdByte.length; n++) {
            data[i++] = packetIdByte[n];
        }

        data[i++] = (byte) (groupId >>> 56);
        data[i++] = (byte) (groupId >>> 48);
        data[i++] = (byte) (groupId >>> 40);
        data[i++] = (byte) (groupId >>> 32);
        data[i++] = (byte) (groupId >>> 24);
        data[i++] = (byte) (groupId >>> 16);
        data[i++] = (byte) (groupId >>> 8);
        data[i++] = (byte) (groupId);

        return data;
    }

    public static MessageBaseDTO getGroupMessageDeletePacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        long groupId = getGroupId(totalRead, recievedBuffer);
        totalRead += 8;

        messageDTO.setPacketType(packetType);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setGroupId(groupId);

        return messageDTO;
    }

    public static Map<String, byte[]> makeGroupMultipleMessageDeletePacket(int packetType, String userIdentity, long groupId, List<String> packetIDs) throws UnsupportedEncodingException {
        int index = 0;
        Map<String, byte[]> packetList = new ConcurrentHashMap<String, byte[]>();
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");

        while (index < packetIDs.size()) {
            String packetId = ChatUtility.getRandomPacketID();
            byte[] packetIdByte = packetId.getBytes("UTF-8");

            int totalDataLenght = 12 + userIdentityByte.length + packetIdByte.length;

            List<byte[]> tempPacketIdList = new ArrayList<byte[]>();
            int countPacket = 0;

            while (index < packetIDs.size()) {
                String pId = packetIDs.get(index);
                byte[] pIdByte = pId.getBytes("UTF-8");
                int pIdLength = pIdByte.length;

                if ((1 + totalDataLenght + pIdLength) > ChatSettings.BROKEN_PACKET_SIZE) {
                    break;
                }

                tempPacketIdList.add(pIdByte);
                totalDataLenght += (1 + pIdLength);
                countPacket++;
                index++;
            }

            byte[] data = new byte[totalDataLenght];
            int i = 0;
            data[i++] = (byte) packetType;

            data[i++] = (byte) userIdentityByte.length;
            for (int n = 0; n < userIdentityByte.length; n++) {
                data[i++] = userIdentityByte[n];
            }

            data[i++] = (byte) packetIdByte.length;
            for (int n = 0; n < packetIdByte.length; n++) {
                data[i++] = packetIdByte[n];
            }

            data[i++] = (byte) (groupId >>> 56);
            data[i++] = (byte) (groupId >>> 48);
            data[i++] = (byte) (groupId >>> 40);
            data[i++] = (byte) (groupId >>> 32);
            data[i++] = (byte) (groupId >>> 24);
            data[i++] = (byte) (groupId >>> 16);
            data[i++] = (byte) (groupId >>> 8);
            data[i++] = (byte) (groupId);

            data[i++] = (byte) (countPacket);

            for (int idx = 0; idx < countPacket; idx++) {
                byte[] pIdByte = tempPacketIdList.get(idx);

                data[i++] = (byte) pIdByte.length;
                for (int n = 0; n < pIdByte.length; n++) {
                    data[i++] = pIdByte[n];
                }
            }

            packetList.put(packetId, data);
        }

        return packetList;
    }

    public static MessageBaseDTO getGroupMultipleMessageDeletePacket(byte[] recievedBuffer) throws UnsupportedEncodingException {

        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        long groupId = getGroupId(totalRead, recievedBuffer);
        totalRead += 8;

        int numberOfPacket = recievedBuffer[totalRead];
        totalRead++;

        messageDTO.setPacketType(packetType);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setGroupId(groupId);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setNumberOfPacket(numberOfPacket);

        List<MessageBaseDTO> mList = new ArrayList<MessageBaseDTO>();
        for (int inc = 0; inc < numberOfPacket; inc++) {
            try {
                MessageBaseDTO innerDTO = new MessageBaseDTO();

                int pIDLength = recievedBuffer[totalRead];
                totalRead++;
                String pId = new String(recievedBuffer, totalRead, pIDLength, "UTF-8");
                totalRead += pIDLength;

                innerDTO.setPacketID(pId);
                mList.add(innerDTO);
            } catch (Exception ex) {
                //  ex.printStackTrace();
                log.error("Error in ChatPacketUility class ==>" + ex.getMessage());
            }
        }

        messageDTO.setMessageList(mList);
        return messageDTO;
    }

    //Friend Chat Information Message
    public static byte[] makeFriendInformationMessage(MessageBaseDTO messageDTO) throws UnsupportedEncodingException {
        int packetType = messageDTO.getPacketType();
        int device = messageDTO.getDevice();
        byte[] packetIdByte = messageDTO.getPacketID().getBytes("UTF-8");
        byte[] userIdentityByte = messageDTO.getUserIdentity().getBytes("UTF-8");
        byte[] userFullNameByte = messageDTO.getFullName().getBytes("UTF-8");
        byte[] friendIdentityByte = messageDTO.getFriendIdentity().getBytes("UTF-8");
        int presence = messageDTO.getPresence();
        byte[] deviceTokenByte = messageDTO.getDeviceToken().getBytes("UTF-8");

        int inputByteLength = 9 + packetIdByte.length + userIdentityByte.length + friendIdentityByte.length + userFullNameByte.length + deviceTokenByte.length;
        byte[] data = new byte[inputByteLength];
        int i = 0;
        data[i++] = (byte) packetType;
        data[i++] = (byte) device;

        data[i++] = (byte) packetIdByte.length;
        for (int n = 0; n < packetIdByte.length; n++) {
            data[i++] = packetIdByte[n];
        }

        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) userFullNameByte.length;
        for (int n = 0; n < userFullNameByte.length; n++) {
            data[i++] = userFullNameByte[n];
        }

        data[i++] = (byte) friendIdentityByte.length;
        for (int n = 0; n < friendIdentityByte.length; n++) {
            data[i++] = friendIdentityByte[n];
        }

        data[i++] = (byte) presence;

        data[i++] = (byte) (deviceTokenByte.length >>> 8);
        data[i++] = (byte) (deviceTokenByte.length);
        for (int n = 0; n < deviceTokenByte.length; n++) {
            data[i++] = deviceTokenByte[n];
        }

        return data;
    }

    //Chat File Upload Response
    public static ChatFileDTO getChatFileUploadResponse(byte[] recievedBuffer) throws UnsupportedEncodingException {

        ChatFileDTO chatFileDTO = new ChatFileDTO();
        int totalRead = 0;
        chatFileDTO.setStatus(recievedBuffer[0]);
        totalRead++;

        int msgLength = recievedBuffer[totalRead];
        totalRead++;
        if (chatFileDTO.getStatus() == 1) {
            chatFileDTO.setUploadUrl(new String(recievedBuffer, totalRead, msgLength, "UTF-8"));
        } else {
            chatFileDTO.setErrorMsg(new String(recievedBuffer, totalRead, msgLength, "UTF-8"));
        }
        return chatFileDTO;
    }

    //Group FriendList Information Message
    public static Map<String, byte[]> makeGroupMemberListPacket(int packetType, String userIdentity, long groupId, List<String> memebers) throws UnsupportedEncodingException {
        int index = 0;
        Map<String, byte[]> packetList = new ConcurrentHashMap<String, byte[]>();
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");

        while (index < memebers.size()) {
            String packetId = ChatUtility.getRandomPacketID();
            byte[] packetIdByte = packetId.getBytes("UTF-8");

            int totalDataLenght = 13 + userIdentityByte.length + packetIdByte.length;

            List<byte[]> tempMemberIdentityList = new ArrayList<byte[]>();
            int groupMemberCount = 0;

            while (index < memebers.size()) {
                String member = memebers.get(index);
                byte[] memberIdentityByte = member.getBytes("UTF-8");
                int memberIdentityLenth = memberIdentityByte.length;

                if ((1 + totalDataLenght + memberIdentityLenth) > ChatSettings.BROKEN_PACKET_SIZE) {
                    break;
                }

                tempMemberIdentityList.add(memberIdentityByte);
                totalDataLenght += (1 + memberIdentityLenth);
                groupMemberCount++;
                index++;
            }

            byte[] data = new byte[totalDataLenght];
            int i = 0;
            data[i++] = (byte) packetType;

            data[i++] = (byte) packetIdByte.length;
            for (int n = 0; n < packetIdByte.length; n++) {
                data[i++] = packetIdByte[n];
            }

            data[i++] = (byte) userIdentityByte.length;
            for (int n = 0; n < userIdentityByte.length; n++) {
                data[i++] = userIdentityByte[n];
            }

            data[i++] = (byte) (groupId >>> 56);
            data[i++] = (byte) (groupId >>> 48);
            data[i++] = (byte) (groupId >>> 40);
            data[i++] = (byte) (groupId >>> 32);
            data[i++] = (byte) (groupId >>> 24);
            data[i++] = (byte) (groupId >>> 16);
            data[i++] = (byte) (groupId >>> 8);
            data[i++] = (byte) (groupId);

            data[i++] = (byte) (groupMemberCount >>> 8);
            data[i++] = (byte) (groupMemberCount);

            for (int idx = 0; idx < groupMemberCount; idx++) {
                byte[] memberIdentityByte = tempMemberIdentityList.get(idx);

                data[i++] = (byte) memberIdentityByte.length;
                for (int n = 0; n < memberIdentityByte.length; n++) {
                    data[i++] = memberIdentityByte[n];
                }
            }

            packetList.put(packetId, data);
        }

        return packetList;
    }

    //Group Memeber Remove Or Leave Message
    public static byte[] makeGroupMemeberRemoveOrLeavePacket(int packetType, String userIdentity, String friendIdentity, long groupId, String packetID) throws UnsupportedEncodingException {
        byte[] packetIdByte = packetID.getBytes("UTF-8");
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");
        byte[] friendIdentityByte = friendIdentity.getBytes("UTF-8");

        int totalDataLenght = 12 + packetIdByte.length + userIdentityByte.length + friendIdentityByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) packetType;

        data[i++] = (byte) packetIdByte.length;
        for (int n = 0; n < packetIdByte.length; n++) {
            data[i++] = packetIdByte[n];
        }

        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) friendIdentityByte.length;
        for (int n = 0; n < friendIdentityByte.length; n++) {
            data[i++] = friendIdentityByte[n];
        }

        data[i++] = (byte) (groupId >>> 56);
        data[i++] = (byte) (groupId >>> 48);
        data[i++] = (byte) (groupId >>> 40);
        data[i++] = (byte) (groupId >>> 32);
        data[i++] = (byte) (groupId >>> 24);
        data[i++] = (byte) (groupId >>> 16);
        data[i++] = (byte) (groupId >>> 8);
        data[i++] = (byte) (groupId);

        return data;
    }

    public static byte[] makeGroupDeletePacket(int packetType, String userIdentity, long groupId, String packetID) throws UnsupportedEncodingException {
        byte[] packetIdByte = packetID.getBytes("UTF-8");
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");

        int totalDataLenght = 11 + packetIdByte.length + userIdentityByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) packetType;

        data[i++] = (byte) packetIdByte.length;
        for (int n = 0; n < packetIdByte.length; n++) {
            data[i++] = packetIdByte[n];
        }

        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) (groupId >>> 56);
        data[i++] = (byte) (groupId >>> 48);
        data[i++] = (byte) (groupId >>> 40);
        data[i++] = (byte) (groupId >>> 32);
        data[i++] = (byte) (groupId >>> 24);
        data[i++] = (byte) (groupId >>> 16);
        data[i++] = (byte) (groupId >>> 8);
        data[i++] = (byte) (groupId);

        return data;
    }

    //File Transfer
    public static List<byte[]> makeFriendFileStreamSendPacket(int packetType, String packetID, String userIdentity, String friendIdentity, byte[] fileContent, int currentChunkIndex, int totalChunk) throws UnsupportedEncodingException {
        int offset = 0;
        List<byte[]> packetList = new ArrayList<byte[]>();
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");
        byte[] friendIdentityByte = friendIdentity.getBytes("UTF-8");
        byte[] packetIdByte = packetID.getBytes("UTF-8");

        int commonByteLength = 16 + packetIdByte.length + userIdentityByte.length + friendIdentityByte.length;
        List<Integer> msgLengthList = new ArrayList<Integer>();

        int limit = ChatSettings.BROKEN_PACKET_SIZE - commonByteLength;
        int msgLength = fileContent.length;

        while (msgLength > 0) {
            if (msgLength > limit) {
                msgLengthList.add(limit);
                msgLength -= limit;
            } else {
                msgLengthList.add(msgLength);
                msgLength -= msgLength;
            }
        }

        int numberOfpacket = msgLengthList.size();
        for (int idx = 0;
                idx < msgLengthList.size();
                idx++) {
            int fileContentLength = msgLengthList.get(idx);
            int inputByteLength = commonByteLength + fileContentLength;
            int seqNo = idx + 1;

            byte[] data = new byte[inputByteLength];
            int i = 0;
            data[i++] = (byte) packetType;

            data[i++] = (byte) userIdentityByte.length;
            for (int n = 0; n < userIdentityByte.length; n++) {
                data[i++] = userIdentityByte[n];
            }

            data[i++] = (byte) friendIdentityByte.length;
            for (int n = 0; n < friendIdentityByte.length; n++) {
                data[i++] = friendIdentityByte[n];
            }

            data[i++] = (byte) packetIdByte.length;
            for (int n = 0; n < packetIdByte.length; n++) {
                data[i++] = packetIdByte[n];
            }

            data[i++] = (byte) (currentChunkIndex >>> 24);
            data[i++] = (byte) (currentChunkIndex >>> 16);
            data[i++] = (byte) (currentChunkIndex >>> 8);
            data[i++] = (byte) (currentChunkIndex);

            data[i++] = (byte) (totalChunk >>> 24);
            data[i++] = (byte) (totalChunk >>> 16);
            data[i++] = (byte) (totalChunk >>> 8);
            data[i++] = (byte) (totalChunk);

            data[i++] = (byte) numberOfpacket;
            data[i++] = (byte) seqNo;

            data[i++] = (byte) (fileContentLength >>> 8);
            data[i++] = (byte) (fileContentLength);

            int length = offset + fileContentLength;
            for (; offset < length; offset++) {
                data[i++] = fileContent[offset];
            }

            packetList.add(data);
        }

        return packetList;
    }

    public static MessageBaseDTO getFriendFileStreamSendPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {
        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, friendIdentityLength, "UTF-8");
        totalRead += friendIdentityLength;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        int currentChunkIndex = getChunkCount(totalRead, recievedBuffer);
        totalRead += 4;

        int totalChunk = getChunkCount(totalRead, recievedBuffer);
        totalRead += 4;

        int numberOfPacket = recievedBuffer[totalRead];
        totalRead++;

        int seqNo = recievedBuffer[totalRead];
        totalRead++;

        int fileContentLength = getMessageLength(totalRead, recievedBuffer);
        totalRead += 2;

        byte[] fileContent = new byte[fileContentLength];
        for (int i = 0; i < fileContentLength; i++) {
            fileContent[i] = recievedBuffer[totalRead];
            totalRead++;
        }

        messageDTO.setPacketType(packetType);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFriendIdentity(friendIdentity);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setFileInByte(fileContent);
        messageDTO.setFileChunkIndex(currentChunkIndex);
        messageDTO.setFileTotalChunk(totalChunk);
        messageDTO.setNumberOfPacket(numberOfPacket);
        messageDTO.setSequenceNumber(seqNo);

        return messageDTO;
    }

    public static List<byte[]> makeGroupFileStreamSendPacket(int packetType, String packetID, String userIdentity, String friendIdentity, long groupId, byte[] fileContent, int currentChunkIndex, int totalChunk) throws UnsupportedEncodingException {
        int offset = 0;
        List<byte[]> packetList = new ArrayList<byte[]>();
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");
        byte[] friendIdentityByte = friendIdentity.getBytes("UTF-8");
        byte[] packetIdByte = packetID.getBytes("UTF-8");

        int commonByteLength = 24 + packetIdByte.length + userIdentityByte.length + friendIdentityByte.length;
        List<Integer> msgLengthList = new ArrayList<Integer>();

        int limit = ChatSettings.BROKEN_PACKET_SIZE - commonByteLength;
        int msgLength = fileContent.length;

        while (msgLength > 0) {
            if (msgLength > limit) {
                msgLengthList.add(limit);
                msgLength -= limit;
            } else {
                msgLengthList.add(msgLength);
                msgLength -= msgLength;
            }
        }

        int numberOfpacket = msgLengthList.size();
        for (int idx = 0;
                idx < msgLengthList.size();
                idx++) {
            int fileContentLength = msgLengthList.get(idx);
            int inputByteLength = commonByteLength + fileContentLength;
            int seqNo = idx + 1;

            byte[] data = new byte[inputByteLength];
            int i = 0;
            data[i++] = (byte) packetType;

            data[i++] = (byte) userIdentityByte.length;
            for (int n = 0; n < userIdentityByte.length; n++) {
                data[i++] = userIdentityByte[n];
            }

            data[i++] = (byte) friendIdentityByte.length;
            for (int n = 0; n < friendIdentityByte.length; n++) {
                data[i++] = friendIdentityByte[n];
            }

            data[i++] = (byte) (groupId >>> 56);
            data[i++] = (byte) (groupId >>> 48);
            data[i++] = (byte) (groupId >>> 40);
            data[i++] = (byte) (groupId >>> 32);
            data[i++] = (byte) (groupId >>> 24);
            data[i++] = (byte) (groupId >>> 16);
            data[i++] = (byte) (groupId >>> 8);
            data[i++] = (byte) (groupId);

            data[i++] = (byte) packetIdByte.length;
            for (int n = 0; n < packetIdByte.length; n++) {
                data[i++] = packetIdByte[n];
            }

            data[i++] = (byte) (currentChunkIndex >>> 24);
            data[i++] = (byte) (currentChunkIndex >>> 16);
            data[i++] = (byte) (currentChunkIndex >>> 8);
            data[i++] = (byte) (currentChunkIndex);

            data[i++] = (byte) (totalChunk >>> 24);
            data[i++] = (byte) (totalChunk >>> 16);
            data[i++] = (byte) (totalChunk >>> 8);
            data[i++] = (byte) (totalChunk);

            data[i++] = (byte) numberOfpacket;
            data[i++] = (byte) seqNo;

            data[i++] = (byte) (fileContentLength >>> 8);
            data[i++] = (byte) (fileContentLength);

            int length = offset + fileContentLength;
            for (; offset < length; offset++) {
                data[i++] = fileContent[offset];
            }

            packetList.add(data);
        }

        return packetList;
    }

    public static MessageBaseDTO getGroupFileStreamSendPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {
        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, friendIdentityLength, "UTF-8");
        totalRead += friendIdentityLength;

        long groupId = getGroupId(totalRead, recievedBuffer);
        totalRead += 8;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        int currentChunkIndex = getChunkCount(totalRead, recievedBuffer);
        totalRead += 4;

        int totalChunk = getChunkCount(totalRead, recievedBuffer);
        totalRead += 4;

        int numberOfPacket = recievedBuffer[totalRead];
        totalRead++;

        int seqNo = recievedBuffer[totalRead];
        totalRead++;

        int fileContentLength = getMessageLength(totalRead, recievedBuffer);
        totalRead += 2;

        byte[] fileContent = new byte[fileContentLength];
        for (int i = 0; i < fileContentLength; i++) {
            fileContent[i] = recievedBuffer[totalRead];
            totalRead++;
        }

        messageDTO.setPacketType(packetType);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFriendIdentity(friendIdentity);
        messageDTO.setGroupId(groupId);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setFileInByte(fileContent);
        messageDTO.setFileChunkIndex(currentChunkIndex);
        messageDTO.setFileTotalChunk(totalChunk);
        messageDTO.setNumberOfPacket(numberOfPacket);
        messageDTO.setSequenceNumber(seqNo);

        return messageDTO;
    }

    public static Map<String, byte[]> makeFriendFileStreamRequestPacket(int packetType, String packetID, String userIdentity, String friendIdentity, List<Integer[]> indexRanges, int totalChunk) throws UnsupportedEncodingException {
        Map<String, byte[]> packetList = new ConcurrentHashMap<String, byte[]>();
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");
        byte[] friendIdentityByte = friendIdentity.getBytes("UTF-8");
        byte[] packetIdByte = packetID.getBytes("UTF-8");

        int commonByteLength = 11 + packetIdByte.length + userIdentityByte.length + friendIdentityByte.length;
        List<List<Integer[]>> msgRangeList = new ArrayList<List<Integer[]>>();
        List<Integer[]> indexList = new ArrayList<Integer[]>();
        List<Integer> msgLengthList = new ArrayList<Integer>();
        int limit = ChatSettings.BROKEN_PACKET_SIZE - commonByteLength;

        for (int idx = 0; idx < indexRanges.size();) {
            Integer[] indexRange = indexRanges.get(idx);
            if (Objects.equals(indexRange[0], indexRange[1])) {
                if (limit - (1 + 4) >= 0) {
                    indexList.add(indexRange);
                    limit -= (1 + 4);
                    idx++;
                } else {
                    msgRangeList.add(indexList);
                    msgLengthList.add(ChatSettings.BROKEN_PACKET_SIZE - commonByteLength - limit);
                    indexList = new ArrayList<Integer[]>();
                    limit = ChatSettings.BROKEN_PACKET_SIZE - commonByteLength;
                }
            } else {
                if (limit - (1 + 4 + 4) >= 0) {
                    indexList.add(indexRange);
                    limit -= (1 + 4 + 4);
                    idx++;
                } else {
                    msgRangeList.add(indexList);
                    msgLengthList.add(ChatSettings.BROKEN_PACKET_SIZE - commonByteLength - limit);
                    indexList = new ArrayList<Integer[]>();
                    limit = ChatSettings.BROKEN_PACKET_SIZE - commonByteLength;
                }
            }
        }
        msgRangeList.add(indexList);
        msgLengthList.add(ChatSettings.BROKEN_PACKET_SIZE - commonByteLength - limit);

        int numberOfpacket = msgLengthList.size();
        for (int idx = 0; idx < msgLengthList.size(); idx++) {
            int len = msgLengthList.get(idx);
            List<Integer[]> idxRange = msgRangeList.get(idx);

            int inputByteLength = commonByteLength + len;
            int seqNo = idx + 1;
            String brokenPacketID = packetID + ChatConstants.BROKEN_PACKET_SEPARATOR + seqNo;

            byte[] data = new byte[inputByteLength];
            int i = 0;
            data[i++] = (byte) packetType;

            data[i++] = (byte) userIdentityByte.length;
            for (int n = 0; n < userIdentityByte.length; n++) {
                data[i++] = userIdentityByte[n];
            }

            data[i++] = (byte) friendIdentityByte.length;
            for (int n = 0; n < friendIdentityByte.length; n++) {
                data[i++] = friendIdentityByte[n];
            }

            data[i++] = (byte) packetIdByte.length;
            for (int n = 0; n < packetIdByte.length; n++) {
                data[i++] = packetIdByte[n];
            }

            data[i++] = (byte) (totalChunk >>> 24);
            data[i++] = (byte) (totalChunk >>> 16);
            data[i++] = (byte) (totalChunk >>> 8);
            data[i++] = (byte) (totalChunk);

            data[i++] = (byte) numberOfpacket;
            data[i++] = (byte) seqNo;
            data[i++] = (byte) (idxRange.size());

            for (Integer[] range : idxRange) {
                int chunkIndexFrom = range[0];
                int chunkIndexTo = range[1];

                if (Objects.equals(range[0], range[1])) {
                    data[i++] = (byte) 1;

                    data[i++] = (byte) (chunkIndexFrom >>> 24);
                    data[i++] = (byte) (chunkIndexFrom >>> 16);
                    data[i++] = (byte) (chunkIndexFrom >>> 8);
                    data[i++] = (byte) (chunkIndexFrom);
                } else {
                    data[i++] = (byte) 2;

                    data[i++] = (byte) (chunkIndexFrom >>> 24);
                    data[i++] = (byte) (chunkIndexFrom >>> 16);
                    data[i++] = (byte) (chunkIndexFrom >>> 8);
                    data[i++] = (byte) (chunkIndexFrom);

                    data[i++] = (byte) (chunkIndexTo >>> 24);
                    data[i++] = (byte) (chunkIndexTo >>> 16);
                    data[i++] = (byte) (chunkIndexTo >>> 8);
                    data[i++] = (byte) (chunkIndexTo);
                }
            }
            packetList.put(brokenPacketID, data);
        }

        return packetList;
    }

    public static MessageBaseDTO getFriendFileStreamRequestPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {
        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, friendIdentityLength, "UTF-8");
        totalRead += friendIdentityLength;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        int totalChunk = getChunkCount(totalRead, recievedBuffer);
        totalRead += 4;

        int numberOfpacket = recievedBuffer[totalRead];
        totalRead++;

        int seqNo = recievedBuffer[totalRead];
        totalRead++;

        int totalCount = recievedBuffer[totalRead];
        totalRead++;

        List<MessageBaseDTO> mList = new ArrayList<MessageBaseDTO>();
        for (int inc = 0; inc < totalCount; inc++) {
            try {
                MessageBaseDTO innerDTO = new MessageBaseDTO();

                int type = recievedBuffer[totalRead];
                totalRead++;

                int chunkIndexFrom = getChunkCount(totalRead, recievedBuffer);
                totalRead += 4;

                int chunkIndexTo = chunkIndexFrom;
                if (type == 2) {
                    chunkIndexTo = getChunkCount(totalRead, recievedBuffer);
                    totalRead += 4;
                }

                innerDTO.setFileChunkIndexFrom(chunkIndexFrom);
                innerDTO.setFileChunkIndexTo(chunkIndexTo);
                mList.add(innerDTO);
            } catch (Exception ex) {
                //ex.printStackTrace();
                log.error("Error in ChatPacketUility class ==>" + ex.getMessage());
            }
        }
        messageDTO.setMessageList(mList);
        messageDTO.setPacketType(packetType);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFriendIdentity(friendIdentity);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setNumberOfPacket(numberOfpacket);
        messageDTO.setSequenceNumber(seqNo);
        messageDTO.setFileTotalChunk(totalChunk);
        return messageDTO;
    }

    public static Map<String, byte[]> makeGroupFileStreamRequestPacket(int packetType, String packetID, String userIdentity, String friendIdentity, long groupId, List<Integer[]> indexRanges, int totalChunk) throws UnsupportedEncodingException {
        Map<String, byte[]> packetList = new ConcurrentHashMap<String, byte[]>();
        byte[] userIdentityByte = userIdentity.getBytes("UTF-8");
        byte[] friendIdentityByte = friendIdentity.getBytes("UTF-8");
        byte[] packetIdByte = packetID.getBytes("UTF-8");

        int commonByteLength = 19 + packetIdByte.length + userIdentityByte.length + friendIdentityByte.length;
        List<List<Integer[]>> msgRangeList = new ArrayList<List<Integer[]>>();
        List<Integer[]> indexList = new ArrayList<Integer[]>();
        List<Integer> msgLengthList = new ArrayList<Integer>();
        int limit = ChatSettings.BROKEN_PACKET_SIZE - commonByteLength;

        for (int idx = 0; idx < indexRanges.size();) {
            Integer[] indexRange = indexRanges.get(idx);
            if (Objects.equals(indexRange[0], indexRange[1])) {
                if (limit - (1 + 4) >= 0) {
                    indexList.add(indexRange);
                    limit -= (1 + 4);
                    idx++;
                } else {
                    msgRangeList.add(indexList);
                    msgLengthList.add(ChatSettings.BROKEN_PACKET_SIZE - commonByteLength - limit);
                    indexList = new ArrayList<Integer[]>();
                    limit = ChatSettings.BROKEN_PACKET_SIZE - commonByteLength;
                }
            } else {
                if (limit - (1 + 4 + 4) >= 0) {
                    indexList.add(indexRange);
                    limit -= (1 + 4 + 4);
                    idx++;
                } else {
                    msgRangeList.add(indexList);
                    msgLengthList.add(ChatSettings.BROKEN_PACKET_SIZE - commonByteLength - limit);
                    indexList = new ArrayList<Integer[]>();
                    limit = ChatSettings.BROKEN_PACKET_SIZE - commonByteLength;
                }
            }
        }
        msgRangeList.add(indexList);
        msgLengthList.add(ChatSettings.BROKEN_PACKET_SIZE - commonByteLength - limit);

        int numberOfpacket = msgLengthList.size();
        for (int idx = 0; idx < msgLengthList.size(); idx++) {
            int len = msgLengthList.get(idx);
            List<Integer[]> idxRange = msgRangeList.get(idx);

            int inputByteLength = commonByteLength + len;
            int seqNo = idx + 1;
            String brokenPacketID = packetID + ChatConstants.BROKEN_PACKET_SEPARATOR + seqNo;

            byte[] data = new byte[inputByteLength];
            int i = 0;
            data[i++] = (byte) packetType;

            data[i++] = (byte) userIdentityByte.length;
            for (int n = 0; n < userIdentityByte.length; n++) {
                data[i++] = userIdentityByte[n];
            }

            data[i++] = (byte) friendIdentityByte.length;
            for (int n = 0; n < friendIdentityByte.length; n++) {
                data[i++] = friendIdentityByte[n];
            }

            data[i++] = (byte) (groupId >>> 56);
            data[i++] = (byte) (groupId >>> 48);
            data[i++] = (byte) (groupId >>> 40);
            data[i++] = (byte) (groupId >>> 32);
            data[i++] = (byte) (groupId >>> 24);
            data[i++] = (byte) (groupId >>> 16);
            data[i++] = (byte) (groupId >>> 8);
            data[i++] = (byte) (groupId);

            data[i++] = (byte) packetIdByte.length;
            for (int n = 0; n < packetIdByte.length; n++) {
                data[i++] = packetIdByte[n];
            }

            data[i++] = (byte) (totalChunk >>> 24);
            data[i++] = (byte) (totalChunk >>> 16);
            data[i++] = (byte) (totalChunk >>> 8);
            data[i++] = (byte) (totalChunk);

            data[i++] = (byte) numberOfpacket;
            data[i++] = (byte) seqNo;
            data[i++] = (byte) (idxRange.size());

            for (Integer[] range : idxRange) {
                int chunkIndexFrom = range[0];
                int chunkIndexTo = range[1];

                if (Objects.equals(range[0], range[1])) {
                    data[i++] = (byte) 1;

                    data[i++] = (byte) (chunkIndexFrom >>> 24);
                    data[i++] = (byte) (chunkIndexFrom >>> 16);
                    data[i++] = (byte) (chunkIndexFrom >>> 8);
                    data[i++] = (byte) (chunkIndexFrom);
                } else {
                    data[i++] = (byte) 2;

                    data[i++] = (byte) (chunkIndexFrom >>> 24);
                    data[i++] = (byte) (chunkIndexFrom >>> 16);
                    data[i++] = (byte) (chunkIndexFrom >>> 8);
                    data[i++] = (byte) (chunkIndexFrom);

                    data[i++] = (byte) (chunkIndexTo >>> 24);
                    data[i++] = (byte) (chunkIndexTo >>> 16);
                    data[i++] = (byte) (chunkIndexTo >>> 8);
                    data[i++] = (byte) (chunkIndexTo);
                }
            }
            packetList.put(brokenPacketID, data);
        }

        return packetList;
    }

    public static MessageBaseDTO getGroupFileStreamRequestPacket(byte[] recievedBuffer) throws UnsupportedEncodingException {
        MessageBaseDTO messageDTO = new MessageBaseDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength, "UTF-8");
        totalRead += uIdLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, friendIdentityLength, "UTF-8");
        totalRead += friendIdentityLength;

        long groupId = getGroupId(totalRead, recievedBuffer);
        totalRead += 8;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength, "UTF-8");
        totalRead += packetIDLength;

        int totalChunk = getChunkCount(totalRead, recievedBuffer);
        totalRead += 4;

        int numberOfpacket = recievedBuffer[totalRead];
        totalRead++;

        int seqNo = recievedBuffer[totalRead];
        totalRead++;

        int totalCount = recievedBuffer[totalRead];
        totalRead++;

        List<MessageBaseDTO> mList = new ArrayList<MessageBaseDTO>();
        for (int inc = 0; inc < totalCount; inc++) {
            try {
                MessageBaseDTO innerDTO = new MessageBaseDTO();

                int type = recievedBuffer[totalRead];
                totalRead++;

                int chunkIndexFrom = getChunkCount(totalRead, recievedBuffer);
                totalRead += 4;

                int chunkIndexTo = chunkIndexFrom;
                if (type == 2) {
                    chunkIndexTo = getChunkCount(totalRead, recievedBuffer);
                    totalRead += 4;
                }

                innerDTO.setFileChunkIndexFrom(chunkIndexFrom);
                innerDTO.setFileChunkIndexTo(chunkIndexTo);
                mList.add(innerDTO);
            } catch (Exception ex) {
                // ex.printStackTrace();
                log.error("Error in ChatPacketUility class ==>" + ex.getMessage());
            }
        }
        messageDTO.setMessageList(mList);
        messageDTO.setPacketType(packetType);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFriendIdentity(friendIdentity);
        messageDTO.setGroupId(groupId);
        messageDTO.setPacketID(packetId.trim());
        messageDTO.setNumberOfPacket(numberOfpacket);
        messageDTO.setSequenceNumber(seqNo);
        messageDTO.setFileTotalChunk(totalChunk);
        return messageDTO;
    }

    //Common Parcing Method
    public static int getChatBindingPort(int startPoint, byte[] bytes) {
        int result = 0;
        result += (bytes[startPoint++] & 0xFF) << 24;
        result += (bytes[startPoint++] & 0xFF) << 16;
        result += (bytes[startPoint++] & 0xFF) << 8;
        result += (bytes[startPoint++] & 0xFF);
        return result;
    }

    public static int getMessageLength(int startPoint, byte[] bytes) {
        int result = 0;
        result += (bytes[startPoint++] & 0xFF) << 8;
        result += (bytes[startPoint++] & 0xFF);
        return result;
    }

    public static int getTimeout(int startPoint, byte[] bytes) {
        int result = 0;
        result += (bytes[startPoint++] & 0xFF) << 8;
        result += (bytes[startPoint++] & 0xFF);
        return result;
    }

    public static float getLatitude(int startPoint, byte[] bytes) {
        int result = 0;
        result += (bytes[startPoint++] & 0xFF) << 24;
        result += (bytes[startPoint++] & 0xFF) << 16;
        result += (bytes[startPoint++] & 0xFF) << 8;
        result += (bytes[startPoint++] & 0xFF);
        return Float.intBitsToFloat(result);
    }

    public static float getLongitude(int startPoint, byte[] bytes) {
        int result = 0;
        result += (bytes[startPoint++] & 0xFF) << 24;
        result += (bytes[startPoint++] & 0xFF) << 16;
        result += (bytes[startPoint++] & 0xFF) << 8;
        result += (bytes[startPoint++] & 0xFF);
        return Float.intBitsToFloat(result);
    }

    public static int getChunkCount(int startPoint, byte[] bytes) {
        int result = 0;
        result += (bytes[startPoint++] & 0xFF) << 24;
        result += (bytes[startPoint++] & 0xFF) << 16;
        result += (bytes[startPoint++] & 0xFF) << 8;
        result += (bytes[startPoint++] & 0xFF);
        return result;
    }

    public static long getMessageDate(int startPoint, byte[] bytes) {

        byte[] b = new byte[8];
        b[0] = bytes[startPoint++];
        b[1] = bytes[startPoint++];
        b[2] = bytes[startPoint++];
        b[3] = bytes[startPoint++];
        b[4] = bytes[startPoint++];
        b[5] = bytes[startPoint++];
        b[6] = bytes[startPoint++];
        b[7] = bytes[startPoint++];

        ByteBuffer buf = ByteBuffer.wrap(b);
        long result = buf.getLong();
        return result;
    }

    public static long getGroupId(int startPoint, byte[] bytes) {

        byte[] b = new byte[8];
        b[0] = bytes[startPoint++];
        b[1] = bytes[startPoint++];
        b[2] = bytes[startPoint++];
        b[3] = bytes[startPoint++];
        b[4] = bytes[startPoint++];
        b[5] = bytes[startPoint++];
        b[6] = bytes[startPoint++];
        b[7] = bytes[startPoint++];

        ByteBuffer buf = ByteBuffer.wrap(b);
        long result = buf.getLong();
        return result;
    }

}
