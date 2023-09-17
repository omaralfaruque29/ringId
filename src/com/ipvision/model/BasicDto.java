/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

/**
 *
 * @author Faiz
 */
public class BasicDto {

    private String uId;
    private Integer actn;
    private String pckId;
    private String sId;
    private String fndId;
    private Boolean sucs;

    public String getSessionId() {
        return sId;
    }

    public void setSessionId(String sessionId) {
        this.sId = sessionId;
    }

    public Integer getAction() {
        return actn;
    }

    public void setAction(Integer action) {
        this.actn = action;
    }

    public String getUserIdentity() {
        return uId;
    }

    public String getPacketId() {
        return pckId;
    }

    public void setPacketId(String packetId) {
        this.pckId = packetId;
    }

    public void setUserIdentity(String userIdentity) {
        this.uId = userIdentity;
    }

    public String getFriendIdentity() {
        return fndId;
    }

    public void setFriendIdentity(String friendId) {
        this.fndId = friendId;
    }

    public Boolean getSuccess() {
        return sucs;
    }

    public void setSuccess(Boolean success) {
        this.sucs = success;
    }
}
