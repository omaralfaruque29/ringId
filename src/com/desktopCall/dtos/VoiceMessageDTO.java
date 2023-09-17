/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.dtos;

/**
 *
 * @author Faiz
 */
public class VoiceMessageDTO {

    private int packetType;
    private String packetID;
    private String userIdentity;
    private String friendIdentity;
    private int voiceBindingPort;
    private String voiceBusyMessage;

    public VoiceMessageDTO() {
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

    public String getFriendIdentity() {
        return friendIdentity;
    }

    public void setFriendIdentity(String friendIdentity) {
        this.friendIdentity = friendIdentity;
    }

    public int getVoiceBindingPort() {
        return voiceBindingPort;
    }

    public void setVoiceBindingPort(int voiceBindingPort) {
        this.voiceBindingPort = voiceBindingPort;
    }

    public String getVoiceBusyMessage() {
        return voiceBusyMessage;
    }

    public void setVoiceBusyMessage(String voiceBusyMessage) {
        this.voiceBusyMessage = voiceBusyMessage;
    }

}
