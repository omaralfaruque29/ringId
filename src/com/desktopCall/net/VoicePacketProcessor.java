/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.net;

import com.desktopCall.dtos.VoiceMessageDTO;
import com.desktopCall.settings.VoiceConstants;

/**
 *
 * @author Faiz
 */
public class VoicePacketProcessor {

    public static byte[] makeRegisterPacket(int packetType, String userIdentity, String friendIdentity, String packetID) {
        byte[] packetIdByte = packetID.getBytes();
        byte[] userIdentityByte = userIdentity.getBytes();
        byte[] friendIdentityByte = friendIdentity.getBytes();
        int friendIdentityLength = friendIdentityByte.length;

        int totalDataLenght = 5 + userIdentityByte.length + friendIdentityLength + packetIdByte.length;
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

        data[i++] = (byte) friendIdentityLength;
        for (int n = 0; n < friendIdentityLength; n++) {
            data[i++] = friendIdentityByte[n];
        }

        return data;
    }

    public static byte[] makeUnRegisterPacket(int packetType, String userIdentity) {
        byte[] userIdentityByte = userIdentity.getBytes();

        int totalDataLenght = 2 + userIdentityByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) packetType;

        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        return data;
    }

    public static byte[] makeSignalingPacket(int packetType, String packetID, String userIdentity, String friendIdentity) {
        byte[] packetIDByte = packetID.getBytes();
        byte[] userIdentityByte = userIdentity.getBytes();
        byte[] friendIdentityByte = friendIdentity.getBytes();

        int totalDataLenght = 4 + packetIDByte.length + userIdentityByte.length + friendIdentityByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) packetType;
        data[i++] = (byte) packetIDByte.length;
        for (int n = 0; n < packetIDByte.length; n++) {
            data[i++] = packetIDByte[n];
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

    public static byte[] makePushMessage(String packetID, String userIdentity, String userfullName, String friendDeviceToken, int platform) {
        /*
         Packet Type(1 byte)
         +PacketID Length(1 byte)
         +Packet ID (1 byte)
         +User Identity Length(1 byte)
         +User Idenity
         +User Full Name Length =< 127 (1 byte)
         +User Full Name
         +Friend Patform (1 byte)
         +Friend Device Token Length (2 byte)
         +Friend Device Token
         */

        byte[] packetIDByte = packetID.getBytes();
        byte[] userIdentityByte = userIdentity.getBytes();
        byte[] fullNameByte = userfullName.getBytes();
        byte[] friendDeviceTokenByte = friendDeviceToken.getBytes();

        int l = fullNameByte.length;
        if (l > 127) {
            l = 127;
        }
        int totalDataLenght = 8 + packetIDByte.length + userIdentityByte.length + l + friendDeviceTokenByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) VoiceConstants.CALL_STATE.VOICE_REGISTER_PUSH;
        data[i++] = (byte) packetIDByte.length;
        for (int n = 0; n < packetIDByte.length; n++) {
            data[i++] = packetIDByte[n];
        }

        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) l;
        for (int n = 0; n < l; n++) {
            data[i++] = fullNameByte[n];
        }
        data[i++] = (byte) platform;
        data[i++] = (byte) (friendDeviceTokenByte.length >>> 8);
        data[i++] = (byte) (friendDeviceTokenByte.length);

        for (int n = 0; n < friendDeviceTokenByte.length; n++) {
            data[i++] = friendDeviceTokenByte[n];
        }

        return data;
    }

    public static byte[] makeBusyMessage(String packetID, String userIdentity, String friendId, String msg) {
        /*
         Busy Signal Packet with  Specific Message Text(VOICE_BUSY_MESSAGE):
         Packet Type+PacketID Length+Packet ID+User Identity Length+User Idenity+Friend Identity Length+Friend Idenity+Message Length+Message
         
     
         Packet Type(1 byte)
         +PacketID Length(1 byte)
         +Packet ID (1 byte)
         +User Identity Length(1 byte)
         +Freind id Length  (1 byte)
         +Message legth (1 byte)
     
         */

        byte[] packetIDByte = packetID.getBytes();
        byte[] userIdentityByte = userIdentity.getBytes();
        byte[] friendIDByte = friendId.getBytes();
        byte[] messageByte = msg.getBytes();

        int totalDataLenght = 6 + packetIDByte.length + userIdentityByte.length + friendIDByte.length + messageByte.length;
        byte[] data = new byte[totalDataLenght];
        int i = 0;
        data[i++] = (byte) VoiceConstants.CALL_STATE.VOICE_BUSY_MESSAGE;
        data[i++] = (byte) packetIDByte.length;
        for (int n = 0; n < packetIDByte.length; n++) {
            data[i++] = packetIDByte[n];
        }

        data[i++] = (byte) userIdentityByte.length;
        for (int n = 0; n < userIdentityByte.length; n++) {
            data[i++] = userIdentityByte[n];
        }

        data[i++] = (byte) friendIDByte.length;
        for (int n = 0; n < friendIDByte.length; n++) {
            data[i++] = friendIDByte[n];
        }

        data[i++] = (byte) (messageByte.length);
        // data[i++] = (byte) (friendDeviceTokenByte.length);

        for (int n = 0; n < messageByte.length; n++) {
            data[i++] = messageByte[n];
        }

        return data;
    }

    public static VoiceMessageDTO getPushMessageConfirmationPacket(byte[] receivedBuffer) {
        /*
         Packet Type
         +PacketID Length
         +Packet ID
         +Friend Identity Length
         +Friend Idenity
         */

        VoiceMessageDTO messageDTO = new VoiceMessageDTO();
        int totalRead = 0;
        int packetType = receivedBuffer[0];
        totalRead++;
        int packetIDLength = receivedBuffer[totalRead];
        totalRead++;
        String packetID = new String(receivedBuffer, totalRead, packetIDLength);
        totalRead += packetIDLength;

        int friendIdentityLength = receivedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(receivedBuffer, totalRead, friendIdentityLength);
        totalRead += friendIdentityLength;

        messageDTO.setPacketType(packetType);
        messageDTO.setPacketID(packetID);
        messageDTO.setFriendIdentity(friendIdentity);
        return messageDTO;
    }

    public static VoiceMessageDTO getRegisterConfirmationPacket(
            byte[] receivedBuffer) {
        VoiceMessageDTO messageDTO = new VoiceMessageDTO();
        int totalRead = 0;
        int packetType = receivedBuffer[0];
        totalRead++;
        int bindingPort = getChatBindingPort(totalRead, receivedBuffer);
        totalRead += 4;
        int packetIDLength = receivedBuffer[totalRead];
        totalRead++;
        String packetID = new String(receivedBuffer, totalRead, packetIDLength);
        totalRead += packetIDLength;

        int friendIdentityLength = receivedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(receivedBuffer, totalRead, friendIdentityLength);
        totalRead += friendIdentityLength;

        messageDTO.setPacketType(packetType);
        messageDTO.setPacketID(packetID);
        messageDTO.setVoiceBindingPort(bindingPort);
        messageDTO.setFriendIdentity(friendIdentity);
        return messageDTO;
    }

    public static VoiceMessageDTO getSignalingPacket(byte[] recievedBuffer) {
        VoiceMessageDTO messageDTO = new VoiceMessageDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;
        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetID = new String(recievedBuffer, totalRead, packetIDLength);
        totalRead += packetIDLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, friendIdentityLength);
        totalRead += friendIdentityLength;

        messageDTO.setPacketType(packetType);
        messageDTO.setPacketID(packetID);
        messageDTO.setFriendIdentity(friendIdentity);
        return messageDTO;
    }

    public static VoiceMessageDTO getSignalingPacketWithBusymessage(byte[] recievedBuffer) {
        /*
         Busy Signal Packet with  Specific Message Text(VOICE_BUSY_MESSAGE):
         Packet Type+PacketID Length+Packet ID+User Identity Length+User Idenity+Friend Identity Length+Friend Idenity+Message Length+Message
         
     
         Packet Type(1 byte)
         +PacketID Length(1 byte)
         +Packet ID (1 byte)
         +User Identity Length(1 byte)
         +Freind id Length  (1 byte)
         +Message legth (1 byte)
     
         */
        VoiceMessageDTO messageDTO = new VoiceMessageDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;
        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetID = new String(recievedBuffer, totalRead, packetIDLength);
        totalRead += packetIDLength;

        int userIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userId = new String(recievedBuffer, totalRead, userIdLength);
        totalRead += userIdLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdentity = new String(recievedBuffer, totalRead, friendIdentityLength);
        totalRead += friendIdentityLength;
        int busyMessageLength = recievedBuffer[totalRead];
        totalRead++;
        String busyMessage = new String(recievedBuffer, totalRead, busyMessageLength);

        messageDTO.setPacketType(packetType);
        messageDTO.setPacketID(packetID);
        messageDTO.setUserIdentity(userId);
        messageDTO.setFriendIdentity(friendIdentity);
        messageDTO.setVoiceBusyMessage(busyMessage);
        return messageDTO;
    }

    public static VoiceMessageDTO getIncallPacket(byte[] recievedBuffer) {

        VoiceMessageDTO messageDTO = new VoiceMessageDTO();
        int totalRead = 0;
        int packetType = recievedBuffer[0];
        totalRead++;

        int packetIDLength = recievedBuffer[totalRead];
        totalRead++;
        String packetId = new String(recievedBuffer, totalRead, packetIDLength);
        totalRead += packetIDLength;

        int uIdLength = recievedBuffer[totalRead];
        totalRead++;
        String userIdentity = new String(recievedBuffer, totalRead, uIdLength);
        totalRead += uIdLength;

        int friendIdentityLength = recievedBuffer[totalRead];
        totalRead++;
        String friendIdenity = new String(recievedBuffer, totalRead, friendIdentityLength);
        totalRead += friendIdentityLength;

        messageDTO.setPacketType(packetType);
        messageDTO.setPacketID(packetId);
        messageDTO.setUserIdentity(userIdentity);
        messageDTO.setFriendIdentity(friendIdenity);

        return messageDTO;
    }

    public static int getChatBindingPort(int stratPoint, byte[] bytes) {
        int result = 0;
        result += (bytes[stratPoint++] & 0xFF) << 24;
        result += (bytes[stratPoint++] & 0xFF) << 16;
        result += (bytes[stratPoint++] & 0xFF) << 8;
        result += (bytes[stratPoint++] & 0xFF);
        return result;
    }
}
