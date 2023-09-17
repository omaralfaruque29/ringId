/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.dtos;

/**
 *
 * @author Faiz Ahmed
 */
public class CallerDtos {

    private String swIp;
    private int swPr;
    private int dvc;
    private String callID;
    private String fndId;
    private int voiceBindingPort;
    private Long tm;
//{"fndId":"2000002160","actn":174,"pckId":"2000001005171425989249236","swIp":"38.127.68.57","swPr":1250,"sucs":true,"psnc":2,"dvc":1,"callID":"2000001005171425989249236","tm":7972870212971205,"tag":"CALLDATA"}
//{"fndId":"2000002160","actn":374,"pckFs":39562,"swIp":"38.127.68.57","swPr":1250,"sucs":true,"psnc":2,"dvc":1,"callID":"2000002160121425989426340","tm":7972636085632451,"tag":"CALLDATA"}

    public Long getTm() {
        return tm;
    }

    public void setTm(Long tm) {
        this.tm = tm;
    }

    public String getFriendIdentity() {
        return fndId;
    }

    public void setFriendIdentity(String friendId) {
        this.fndId = friendId;
    }

    public int getVoiceBindingPort() {
        return voiceBindingPort;
    }

    public void setVoiceBindingPort(int voiceBindingPort) {
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

    public int getDevice() {
        return dvc;
    }

    public void setDevice(int device) {
        this.dvc = device;
    }

    public String getCallID() {
        return callID;
    }

    public void setCallID(String callID) {
        this.callID = callID;
    }
}
