/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.call;

import com.ipvision.model.BasicDto;

/**
 *
 * @author Faiz
 */
public class CallerDto extends BasicDto {

    private String swIp;
    private Integer swPr;
    private String fdt;
    private Integer dvc;
    private String dt;
    private String callID;
    private Integer voiceBindingPort;
    private Long tm;

    public Integer getVoiceBindingPort() {
        return voiceBindingPort;
    }

    public void setVoiceBindingPort(Integer voiceBindingPort) {
        this.voiceBindingPort = voiceBindingPort;
    }

    public String getSwitchIp() {
        return swIp;
    }

    public void setSwitchIp(String switchIp) {
        this.swIp = switchIp;
    }

    public Integer getSwitchPort() {
        return swPr;
    }

    public void setSwitchPort(Integer switchPort) {
        this.swPr = switchPort;
    }

    public String getFriendDeviceToken() {
        return fdt;
    }

    public void setFriendDeviceToken(String fdt) {
        this.fdt = fdt;
    }

    public Integer getDevice() {
        return dvc;
    }

    public void setDevice(Integer device) {
        this.dvc = device;
    }

    public String getDeviceToken() {
        return dt;
    }

    public void setDeviceToken(String deviceToken) {
        this.dt = deviceToken;
    }

    public String getCallID() {
        return callID;
    }

    public void setCallID(String callID) {
        this.callID = callID;
    }

    public Long getTm() {
        return tm;
    }

    public void setTm(Long tm) {
        this.tm = tm;
    }
}
