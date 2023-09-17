/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.threads;

import com.desktopCall.settings.ConfigFile;
import com.desktopCall.settings.SignalGenerator;
import com.desktopCall.settings.VoiceConstants;
import com.desktopCall.net.VoicePacketProcessor;

/**
 *
 * @author Faiz
 */
public class SendKeepAliveForCall extends Thread {

    byte[] confirmationByte;
    String callId;
    String friendID;

    public SendKeepAliveForCall(String callId, String friendID) {
        this.callId = callId;
        this.friendID = friendID;
        ConfigFile.stopKeepAlive = false;
    }

    @Override
    public void run() {
        confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.KEEPALIVE, this.callId, ConfigFile.USER_ID, friendID);
        SignalGenerator.sendSignalingPacket(confirmationByte);
        for (int i = 1; i <= 60; i++) {
            if (ConfigFile.stopKeepAlive) {
                return;
            }
            try {
                Thread.sleep(500);
                if (ConfigFile.stopKeepAlive) {
                    return;
                }
                if (i % 10 == 0) {
                    //  System.out.println("sendig call keepalive ==>" + this.callId);
                    SignalGenerator.sendSignalingPacket(confirmationByte);
                }
            } catch (Exception e) {
            }
        }
    }

}
