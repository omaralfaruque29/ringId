/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.dto;

import java.lang.reflect.Field;
import java.util.List;

/**
 *
 * @author Shahadat Hossain
 */
public class MessageBaseDTO implements Cloneable {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MessageBaseDTO.class);
    private int packetType;
    private String packetID;
    private String userIdentity;
    private String fullName;
    private String friendIdentity;
    private Long groupId;
    private String message;
    private long messageDate;
    private int messageType;
    private int messageStatus;
    private int timeout;
    private int presence;
    private int device;
    private String deviceToken;
    private float latitude;
    private float longitude;
    private int chatBindingPort;
    private int numberOfPacket;
    private int sequenceNumber;
    private List<MessageBaseDTO> messageList;

    private long systemDate;
    private long serverDate;
    private long dateDifference;
    private int status;
    private int chatRegisterPort;
    private String chatServerIP;

    private byte[] fileInByte;
    private int fileChunkIndex;
    private int fileTotalChunk;
    private int fileChunkIndexFrom;
    private int fileChunkIndexTo;

    public MessageBaseDTO() {
    }

    public int getPacketType() {
        return packetType;
    }

    public void setPacketType(int packetType) {
        this.packetType = packetType;
    }

    public String getPacketID() {
        return packetID;
    }

    public void setPacketID(String packetID) {
        this.packetID = packetID;
    }

    public String getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFriendIdentity() {
        return friendIdentity;
    }

    public void setFriendIdentity(String friendIdentity) {
        this.friendIdentity = friendIdentity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(long messageDate) {
        this.messageDate = messageDate;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getNumberOfPacket() {
        return numberOfPacket;
    }

    public void setNumberOfPacket(int numberOfPacket) {
        this.numberOfPacket = numberOfPacket;
    }

    public List<MessageBaseDTO> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageBaseDTO> messageList) {
        this.messageList = messageList;
    }

    public int getChatBindingPort() {
        return chatBindingPort;
    }

    public void setChatBindingPort(int chatBindingPort) {
        this.chatBindingPort = chatBindingPort;
    }

    public long getSystemDate() {
        return systemDate;
    }

    public void setSystemDate(long systemDate) {
        this.systemDate = systemDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getChatRegisterPort() {
        return chatRegisterPort;
    }

    public void setChatRegisterPort(int chatRegisterPort) {
        this.chatRegisterPort = chatRegisterPort;
    }

    public String getChatServerIP() {
        return chatServerIP;
    }

    public void setChatServerIP(String chatServerIP) {
        this.chatServerIP = chatServerIP;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public int getPresence() {
        return presence;
    }

    public void setPresence(int presence) {
        this.presence = presence;
    }

    public long getServerDate() {
        return serverDate;
    }

    public void setServerDate(long serverDate) {
        this.serverDate = serverDate;
    }

    public long getDateDifference() {
        return dateDifference;
    }

    public void setDateDifference(long dateDifference) {
        this.dateDifference = dateDifference;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public byte[] getFileInByte() {
        return fileInByte;
    }

    public void setFileInByte(byte[] fileInByte) {
        this.fileInByte = fileInByte;
    }

    public int getFileChunkIndex() {
        return fileChunkIndex;
    }

    public void setFileChunkIndex(int fileChunkIndex) {
        this.fileChunkIndex = fileChunkIndex;
    }

    public int getFileTotalChunk() {
        return fileTotalChunk;
    }

    public void setFileTotalChunk(int fileTotalChunk) {
        this.fileTotalChunk = fileTotalChunk;
    }

    public int getFileChunkIndexFrom() {
        return fileChunkIndexFrom;
    }

    public void setFileChunkIndexFrom(int fileChunkIndexFrom) {
        this.fileChunkIndexFrom = fileChunkIndexFrom;
    }

    public int getFileChunkIndexTo() {
        return fileChunkIndexTo;
    }

    public void setFileChunkIndexTo(int fileChunkIndexTo) {
        this.fileChunkIndexTo = fileChunkIndexTo;
    }

    @Override
    public String toString() {
        return "MessageDTO [pT=" + packetType + ", pId=" + packetID + ", uId=" + userIdentity + ", fN=" + fullName + ", fId=" + friendIdentity
                + ", grpId=" + groupId + ", msg=" + message + ", mT=" + messageType + ", mD=" + messageDate + ", sysD=" + systemDate + ", srvD=" + serverDate
                + ", dDiff=" + dateDifference + ", mSts=" + messageStatus + ", tOut=" + timeout + ", sts=" + status + ", cbp=" + chatBindingPort
                + ", seqNo=" + sequenceNumber + ", nop=" + numberOfPacket + ", mL=" + messageList + "]";
    }

    @Override
    public Object clone() {
        try {
            return (Object) super.clone();
        } catch (Exception e) {
           // e.printStackTrace();
            log.error("Error in MessageBaseDTO class in returning object==>" + e.getMessage());
            return this;
        }
    }

    public Object copyTo(Class<?> targetClass) {
        Object instance = null;
        try {
            instance = targetClass.newInstance();
            Field[] fromFields = this.getClass().getDeclaredFields();
            for (Field field : fromFields) {
                field.set(instance, field.get(this));
            }
        } catch (Exception ex) {
           // ex.printStackTrace();
         log.error("Error in MessageBaseDTO class in returning targetClass==>" + ex.getMessage());
        }
        return instance;
    }

}
