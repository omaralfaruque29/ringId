/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.threads;

import com.desktopCall.dtos.ResendPacketDTO;
import com.desktopCall.settings.ConfigFile;
import com.desktopCall.settings.VoiceConstants;

/**
 *
 * @author Faiz
 */
public class CallPacketReSender extends Thread {

    long PROCESSING_INTERVAL = 500L;
    ResendPacketDTO resendPacketDTO;
    String pakId;

    public CallPacketReSender(ResendPacketDTO resendPacketDTO, String pakid) {
        this.resendPacketDTO = resendPacketDTO;
        this.pakId = pakid;
    }

    @Override
    public void run() {

        for (int i = 1; i <= 30; i++) {

            if (ConfigFile.noUI) {
                break;
            }
            if (VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.contains(pakId)) {
                break;
            } else {
                try {

                    Thread.sleep(PROCESSING_INTERVAL);
                    if (i % 6 == 0) {
                        System.out.println("resending..........." + pakId);
                        if (!VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.contains(pakId)) {
                            resendPacketDTO.getUdpSocket().send(resendPacketDTO.getPacket());
                        }
                    }
                } catch (Exception ex) {
                }
            }
        }
    }
}
