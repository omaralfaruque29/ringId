/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.utility.call;

import com.desktopCall.dtos.CallSettingsDTo;
import com.desktopCall.dtos.VoiceMessageDTO;
import com.desktopCall.media.AudioStreamPlayer;
import com.desktopCall.media.AudioStreamRecorder;
import com.desktopCall.settings.ConfigFile;
import com.desktopCall.settings.Helpers;
import com.desktopCall.settings.SignalGenerator;
import com.desktopCall.settings.VoiceConstants;
import com.desktopCall.threads.KeepAliveWhileHOld;
import com.desktopCall.threads.SendKeepAliveForCall;
import com.desktopCall.net.CallStates;
import com.desktopCall.net.VoicePacketProcessor;
import com.desktopCall.net.VoiceSignalProcessor;
import com.ipvision.model.call.CallLogHelper;
import com.ipvision.view.call.InstantMessagesJDialog;
import com.ipvision.view.call.MainClass;
import com.ipvision.service.call.CallSignalHandler;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.audio.AudioController;

/**
 *
 * @author Faiz
 */
public class TopLabelWrapper {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TopLabelWrapper.class);
    public static AudioStreamPlayer audioStreamPlayer;
    public static AudioStreamRecorder audioStreamRecorder;

    /**/
    public static void resetValues() {
        ConfigFile.stopKeepAlive = false;
    }

    public static void setInitSettings(CallSettingsDTo callSettingsDTo) {
        ConfigFile.set(callSettingsDTo);
    }

    public static void stopKeepAlives() {
        ConfigFile.stopKeepAlive = true;
    }
    /**/

    public static void sendKeepAlive(String callId, final String freindID) {
        (new SendKeepAliveForCall(callId, freindID)).start();
    }

    public static void sendKeepAlive() {
        new KeepAliveWhileHOld().start();
    }

    public static void startAudioPlayerAndRecorder() {
        if (audioStreamRecorder == null) {
            audioStreamRecorder = new AudioStreamRecorder();
        }
        if (audioStreamPlayer == null) {
            audioStreamPlayer = new AudioStreamPlayer();
        }
        if (MainClass.getMainClass() != null) {
            MainClass.getMainClass().stopRinging();
            MainClass.getMainClass().showHoldUnholdPanel(true);

        }

    }

    public static void stopPlayerAndRecorder() {
        if (MainClass.getMainClass() != null) {
            MainClass.getMainClass().playClipOffSound();
        }
        if (audioStreamPlayer != null) {
            audioStreamPlayer.stopPlayer();
            audioStreamPlayer = null;
        }
        if (audioStreamRecorder != null) {
            audioStreamRecorder.stopRecorder();
            audioStreamRecorder = null;
        }

    }

    public static void processConnected() {
        if (CallStates.getStatus() != CallStates.UA_ONCALL && MainClass.getMainClass() != null) {
            startAudioPlayerAndRecorder();
            MainClass.getMainClass().callConnected(false);
            // MainClass.getMainClass().showAcceptButton(false);
        }
    }

    public static void holdButtonAction() {
        if (!VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.isEmpty()) {
            String tempPakID = Helpers.getPack(ConfigFile.CALL_ID, VoiceConstants.CALL_STATE.VOICE_CALL_HOLD);
            VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.remove(tempPakID);
        }
        SignalGenerator.sendHoldSignal(true);
        stopPlayerAndRecorder();
        sendKeepAlive();
    }

    public static void unHoldButtonAction() {
        if (!VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.isEmpty()) {
            String tempPakID = Helpers.getPack(ConfigFile.CALL_ID, VoiceConstants.CALL_STATE.VOICE_CALL_UNHOLD);
            VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.remove(tempPakID);
        }
        SignalGenerator.sendHoldSignal(false);
        startAudioPlayerAndRecorder();
    }

    public static void cancelButtonAction(boolean incomming) {
        SignalGenerator.cancelButtonAction(incomming);
        resetCallingWindows(true);
    }

    public static void cancelButtonAction(boolean incomming, boolean noSignal) {
        resetCallingWindows(true);
    }

    public static void answerButtonAction() {
        SignalGenerator.answerButtonAction(ConfigFile.FRIEND_ID);
    }

    public static void endButtonAction() {
    }

    public static void callConnectedAction() {
        CallSignalHandler.audioTime = System.currentTimeMillis();
    }

    public static void noResponse(boolean incomming) {
        SignalGenerator.notResponding(incomming);
        if (MainClass.getMainClass() != null) {
            MainClass.getMainClass().stopRinging();
        }

    }

    public static void sendReg() {
        SignalGenerator.sendRegButtonAction(ConfigFile.FRIEND_ID, ConfigFile.CALL_ID, ConfigFile.VOICE_SERVER_IP, ConfigFile.VOICE_REGISTER_PORT);
    }

    public static void startSignalProcessor(CallSignalHandler callSignalHandler) {
        VoiceSignalProcessor.getInstance().initUdp(callSignalHandler);
    }

    public static void resetVoiceSocket() {
        VoiceSignalProcessor.getInstance().resetVoiceAll();
    }

    public static void resetCallingWindows(final boolean callLog) {
        try {
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (InstantMessagesJDialog.currentInstance != null) {
                            InstantMessagesJDialog.currentInstance.hideThis();
                        }
                        ConfigFile.noUI = true;
                        CallStates.changeStatus(CallStates.UA_IDLE);
                        stopPlayerAndRecorder();
                        if (MainClass.getMainClass() != null && ConfigFile.CALL_ID != null && ConfigFile.FRIEND_ID != null) {
                            MainClass.getMainClass().stopHoldTune();
                            if (MainClass.getMainClass().vol != null) {
                                MainClass.getMainClass().vol.setVisible(false);
                            }
                            if (AudioController.getMasterOutputMute() != null) {
                                AudioController.setMasterOutputMute(false);
                            }
                            //    MainClass.getMainClass().stopRinging();
                            CallDurationHelper2 callDurationHelper2 = MainClass.getMainClass().callDurationHelper;
                            if (callLog) {
                                boolean isOutgoing = false;
                                boolean missCall = true;
                                if (MainClass.getMainClass().incommingCall > 0) {
                                    if (callDurationHelper2.sec > 0 || callDurationHelper2.min > 0 || callDurationHelper2.hour > 0) {
                                        missCall = false;
                                        isOutgoing = false;
                                    }
                                } else {
                                    isOutgoing = true;
                                    missCall = false;
                                }
                                CallLogHelper.callEndTimeToCallLog(ConfigFile.CALL_ID, ConfigFile.FRIEND_ID, (long) ((callDurationHelper2.hour * 3600 + callDurationHelper2.min * 60 + callDurationHelper2.sec) * 1000), missCall, isOutgoing);
                            }
                            callDurationHelper2.setStopDurationCounting(true);
                            callDurationHelper2.reset();
                        }
                        CallUtils.removeFromCallInitiators(ConfigFile.CALL_ID);
                        ConfigFile.resetAllParameters();
                        VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.clear();
                        resetUpperLabelValues();
                    } catch (Exception e) {
                        //  e.printStackTrace();
                        log.error("Error in here ==>" + e.getMessage());
                    } finally {
                        resetUI();
                    }

                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void sendSignalingPacket(int type, VoiceMessageDTO voiceMessageDTO, String ip, int port) {
        byte[] confirmationByte = VoicePacketProcessor.makeSignalingPacket(type, voiceMessageDTO.getPacketID(), ConfigFile.USER_ID, ConfigFile.FRIEND_ID);
        SignalGenerator.sendSignalingPacketWithIpPort(confirmationByte, ip, port);
    }

    private static void resetUpperLabelValues() {
        CallSignalHandler.audioTime = 0;
        MyfnfHashMaps.getInstance().getCallInitiators().clear();
    }

    private static void resetUI() {
        if (MainClass.getMainClass() != null) {
            MainClass.getMainClass().stopRinging();
            MainClass.getMainClass().hideAndDeleteObject();
        }
    }

}
