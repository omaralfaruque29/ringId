/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.Map;
import com.ipvision.model.FeedBackFields;

/**
 *
 * @author Faiz Ahmed
 */
public class ChatIntiatorDto extends FeedBackFields {

    private String fdt;
    private String chIp;
    private int chRp;
    private int dvc;
    private int psnc;
    private String dt;
    private Long twid;
    private Integer rc;
    private Map<String, String> frnds;
    private Long lot;

    public Long getLastOnlineTime() {
        return lot;
    }

    public void setLastOnlineTime(Long lot) {
        this.lot = lot;
    }

    public String getFriendDeviceToken() {
        return fdt;
    }

    public void setFriendDeviceToken(String fdt) {
        this.fdt = fdt;
    }

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

    public Map<String, String> getFriends() {
        return frnds;
    }

    public void setFriends(Map<String, String> frnds) {
        this.frnds = frnds;
    }

    public Long getGroupOwnerUserTableId() {
        return twid;
    }

    public void setGroupOwnerUserTableId(Long twid) {
        this.twid = twid;
    }

    public Integer getRc() {
        return rc;
    }

    public void setRc(Integer rc) {
        this.rc = rc;
    }

}
