/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.dto;

/**
 *
 * @author shahadat
 */
public class ChatSettingsDTO {
    
    private String authServerIP;
    private int authServerPort;
    private String chatOfflineIP;
    private int chatOfflinePort;
    private String sessionID;
    private String userIdentity;
    private String fullName;
    private int chatLocalPort;
    private int bufferSize;
    private int brokenPacketSize;
    private int fileChunkSize;
    private int chatPlatform;

    public String getAuthServerIP() {
        return authServerIP;
    }

    public void setAuthServerIP(String authServerIP) {
        this.authServerIP = authServerIP;
    }

    public int getAuthServerPort() {
        return authServerPort;
    }

    public void setAuthServerPort(int authServerPort) {
        this.authServerPort = authServerPort;
    }

    public String getChatOfflineIP() {
        return chatOfflineIP;
    }

    public void setChatOfflineIP(String chatOfflineIP) {
        this.chatOfflineIP = chatOfflineIP;
    }

    public int getChatOfflinePort() {
        return chatOfflinePort;
    }

    public void setChatOfflinePort(int chatOfflinePort) {
        this.chatOfflinePort = chatOfflinePort;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
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

    public int getChatLocalPort() {
        return chatLocalPort;
    }

    public void setChatLocalPort(int chatLocalPort) {
        this.chatLocalPort = chatLocalPort;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public int getChatPlatform() {
        return chatPlatform;
    }

    public void setChatPlatform(int chatPlatform) {
        this.chatPlatform = chatPlatform;
    }

    public int getBrokenPacketSize() {
        return brokenPacketSize;
    }

    public void setBrokenPacketSize(int brokenPacketSize) {
        this.brokenPacketSize = brokenPacketSize;
    }

    public int getFileChunkSize() {
        return fileChunkSize;
    }

    public void setFileChunkSize(int fileChunkSize) {
        this.fileChunkSize = fileChunkSize;
    }
    
}
