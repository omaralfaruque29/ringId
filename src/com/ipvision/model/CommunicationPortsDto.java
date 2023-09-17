/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

/**
 *
 * @author Faiz Ahmed <faiz@ipvision.ca>
 */
public class CommunicationPortsDto {

    /*{
	"success": true,
	"voicePort": 9166,
	"confirmationPort": 9162,
	"requestPort": 9163,
	"updatePort": 9164,
	"chatPort": 9165,
	"authPort": 9160,
	"keepAlivePort": 9161,
	"dataSize": 500,
	"headerSize": 12,
	"authServerIP": "38.108.92.243"
    }*/
    
    private Integer voicePort;
    private Integer confirmationPort;
    private Integer requestPort;
    private Integer updatePort;
    private String authServerIP;
    private Integer smsPort;
    private Integer chatPort;
    private Integer authPort;
    private Integer keepAlivePort;
    private String localServer;
    private Integer dataSize;
    private Integer headerSize;
    private String ringID;

    public String getRingID() {
        return ringID;
    }

    public void setRingID(String ringID) {
        this.ringID = ringID;
    }
    
   
    public Integer getAuthPort() {
        return authPort;
    }

    public void setAuthPort(Integer authPort) {
        this.authPort = authPort;
    }

    public Integer getKeepAlivePort() {
        return keepAlivePort;
    }

    public void setKeepAlivePort(Integer keepAlivePort) {
        this.keepAlivePort = keepAlivePort;
    }

    public String getAuthServerIP() {
        return authServerIP;
    }

    public void setAuthServerIP(String authServerIP) {
        this.authServerIP = authServerIP;
    }

    public Integer getConfirmationPort() {
        return confirmationPort;
    }

    public void setConfirmationPort(Integer confirmationPort) {
        this.confirmationPort = confirmationPort;
    }

    public Integer getRequestPort() {
        return requestPort;
    }

    public void setRequestPort(Integer requestPort) {
        this.requestPort = requestPort;
    }

    public Integer getUpdatePort() {
        return updatePort;
    }

    public void setUpdatePort(Integer updatePort) {
        this.updatePort = updatePort;
    }

    public Integer getSmsPort() {
        return smsPort;
    }

    public void setSmsPort(Integer smsPort) {
        this.smsPort = smsPort;
    }

    public Integer getChatPort() {
        return chatPort;
    }

    public void setChatPort(Integer chatPort) {
        this.chatPort = chatPort;
    }

    public String getLocalServer() {
        return localServer;
    }

    public void setLocalServer(String localServer) {
        this.localServer = localServer;
    }

    public Integer getDataSize() {
        return dataSize;
    }

    public void setDataSize(Integer dataSize) {
        this.dataSize = dataSize;
    }

    public Integer getHeaderSize() {
        return headerSize;
    }

    public void setHeaderSize(Integer headerSize) {
        this.headerSize = headerSize;
    }

    public Integer getVoicePort() {
        return voicePort;
    }

    public void setVoicePort(Integer voicePort) {
        this.voicePort = voicePort;
    }
    
    
}
