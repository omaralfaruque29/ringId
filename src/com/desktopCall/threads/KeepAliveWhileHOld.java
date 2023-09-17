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
import com.desktopCall.net.CallStates;

/**
 *
 * @author Faiz
 */
public class KeepAliveWhileHOld extends Thread {

    byte[] confirmationByte;

    public KeepAliveWhileHOld() {
        ConfigFile.stopKeepAlive = false;
    }

    @Override
    public void run() {
        confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.KEEPALIVE, ConfigFile.CALL_ID, ConfigFile.USER_ID, ConfigFile.FRIEND_ID);
        SignalGenerator.sendSignalingPacket(confirmationByte);

        while (true) {
            System.out.println("KeepAliveWhileHOld");
            if (CallStates.getStatus() != CallStates.HOLD_CALL || ConfigFile.CALL_ID == null) {
                break;
            }
            if (ConfigFile.stopKeepAlive) {
                break;
            }
            try {
                Thread.sleep(5000);
                if (ConfigFile.stopKeepAlive) {
                    break;
                }
                if (CallStates.getStatus() == CallStates.HOLD_CALL) {
                    SignalGenerator.sendSignalingPacket(confirmationByte);
                }

            } catch (Exception e) {
            }

        }
    }

}
