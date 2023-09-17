/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.call;

import com.ipvision.service.utility.call.TopLabelWrapper;
import com.desktopCall.settings.ConfigFile;
import static com.desktopCall.settings.Helpers.getPack;
import com.desktopCall.settings.SignalGenerator;
import com.desktopCall.settings.VoiceConstants;
import com.desktopCall.net.CallStates;
import com.desktopCall.net.VoicePacketProcessor;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.ipvision.view.call.MainClass;
import com.ipvision.view.utility.call.CallUIHelpers;

/**
 *
 * @author Faiz
 */
public class CallIngPacketResender extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CallIngPacketResender.class);
    long PROCESSING_INTERVAL = 500L;
    //ResendPacketDTO resendPacketDTO;
    String pakId;
    private String friendIdentity;
    private String voiceServerIP;
    int port;

    public CallIngPacketResender(String friendIdentity, String pakId, String voiceServerip, int port) {
        //  this.resendPacketDTO = resendPacketDTO;
        this.friendIdentity = friendIdentity;
        this.voiceServerIP = voiceServerip;
        this.port = port;
        setName(this.getClass().getName());
    }
    int waiting_in_ringing_state = 20;

    @Override
    public void run() {
        if (ConfigFile.CALL_ID != null) {
            try {//VoiceConstants.CALL_STATE.RINGING
                SignalGenerator.callButtonAction(VoiceConstants.CALL_STATE.CALLING, this.friendIdentity);

                final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                Runnable periodicTask = new Runnable() {
                    int calling = 0;

                    @Override
                    public void run() {

                        if (MainClass.getMainClass() == null && ConfigFile.CALL_ID == null) {
                            executor.shutdown();
                        }
                        if (CallStates.getStatus() != CallStates.UA_OUTGOING_CALL) {
                            executor.shutdown();
                        }
                        if (CallRegisterRequest.inCall) {
                            TopLabelWrapper.resetCallingWindows(true);
                            executor.shutdown();
                        }
                        if (VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.contains(getPack(ConfigFile.CALL_ID, VoiceConstants.CALL_STATE.RINGING))) {
                            CallUIHelpers.callSuccessChecking(30);
                            executor.shutdown();
                        }
                        if (calling > waiting_in_ringing_state) {
                            try {
                                if (ConfigFile.DEVICE_TOKEN != null && ConfigFile.DEVICE_TOKEN.length() > 0) {
                                    byte[] finalBytePacketPush = VoicePacketProcessor.makePushMessage(ConfigFile.CALL_ID, friendIdentity, ConfigFile.FRIEND_ID, ConfigFile.DEVICE_TOKEN, ConfigFile.VOICE_FRIEND_DEVICE);
                                    DatagramPacket finalPacketPush = new DatagramPacket(finalBytePacketPush, finalBytePacketPush.length, InetAddress.getByName(voiceServerIP), port);
                                    SignalGenerator.sendMessage(finalPacketPush);
                                    Thread.sleep(20);
                                    pakId = getPack(ConfigFile.CALL_ID, VoiceConstants.CALL_STATE.VOICE_REGISTER_PUSH_CONFIRMATION);
                                    for (int i = 1; i < 30; i++) {
                                        if (MainClass.getMainClass() == null) {
                                            executor.shutdown();
                                        }
                                        if (VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.contains(pakId)) {
                                            VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.remove(pakId);
                                            executor.shutdown();
                                        } else {
                                            Thread.sleep(PROCESSING_INTERVAL);
                                            if (i % 6 == 0 && !VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.contains(pakId)) {
                                                SignalGenerator.sendMessage(finalPacketPush);

                                            }

                                        }
                                    }
                                }
                                CallUIHelpers.callSuccessChecking(20);
                                executor.shutdown();
                            } catch (Exception e) {
                            }

                        }
                        calling++;
                    }

                };
                executor.scheduleAtFixedRate(periodicTask,
                        2, 1, TimeUnit.SECONDS);
            } catch (Exception ex) {
                // ex.printStackTrace();
                log.error("Exception in CallIngPacketResender ==>" + ex.getMessage());
            }
        }
    }
}
