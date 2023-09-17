/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.dto;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatRegistrationDTO {

    private Long grpId;
    private String fndId;
    private String chIp;
    private int chRp;
    private int dvc;
    private int psnc;
    private String dt;

    public String getChatServerIp() {
        return chIp;
    }

    public void setChatServerIp(String chatServerIp) {
        this.chIp = chatServerIp;
    }

    public Integer getChatRegistrationPort() {
        return chRp;
    }

    public void setChatRegistrationPort(Integer registrationPort) {
        this.chRp = registrationPort;
    }
    
    public int getDevice() {
        return dvc;
    }

    public void setDevice(int device) {
        this.dvc = device;
    }

    public int getPresence() {
        return psnc;
    }

    public void setPresence(int presence) {
        this.psnc = presence;
    }

    public String getDeviceToken() {
        return dt;
    }

    public void setDeviceToken(String deviceToken) {
        this.dt = deviceToken;
    }

    public String getFriendIdentity() {
        return fndId;
    }

    public void setFriendIdentity(String fndId) {
        this.fndId = fndId;
    }
    
    public Long getGroupId() {
        return grpId;
    }

    public void setGroupId(Long grpId) {
        this.grpId = grpId;
    }

}
