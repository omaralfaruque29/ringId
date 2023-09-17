/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.call;

import com.ipvision.service.utility.call.CallUtils;
import com.ipvision.service.utility.call.TopLabelWrapper;
import com.ipvision.model.call.CallerDto;
import com.desktopCall.dtos.CallSettingsDTo;
import com.desktopCall.dtos.VoiceMessageDTO;
import com.desktopCall.listeners.UiCommunicator;
import com.desktopCall.settings.ConfigFile;
import com.desktopCall.settings.SignalGenerator;
import com.desktopCall.settings.VoiceConstants;
import com.desktopCall.net.CallStates;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.model.UserBasicInfo;

import com.ipvision.view.call.MainClass;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;

/**
 *
 * @author Faiz
 */
public class CallSignalHandler implements UiCommunicator {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CallSignalHandler.class);
    public static long audioTime;

    //   private TopLabelWrapper topLabelWraper;
    @Override
    public void process_RTP(byte[] receivedBuffer, int length) {
        audioTime = System.currentTimeMillis();
        if (TopLabelWrapper.audioStreamPlayer != null) {
            TopLabelWrapper.audioStreamPlayer.onReceivedMessage(receivedBuffer, length);
        } else {
            System.out.println("null");
        }
        //  TopLabelWrapper.sendRTP(receivedBuffer);
    }

    @Override
    public void process_VOICE_REGISTER_CONFIRMATION(VoiceMessageDTO voiceMessageDTO) {
        // System.out.println("ConfigFile.CALL_ID==>" + ConfigFile.CALL_ID);
//        if (ConfigFile.CALL_ID != null && ConfigFile.CALL_ID.equals(voiceMessageDTO.getPacketID())) {
//            System.out.println("ConfigFile.CALL_ID==" + ConfigFile.CALL_ID);
//            MyfnfHashMaps.getInstance().getCallInitiators().get(voiceMessageDTO.getPacketID()).setVoiceBindingPort(voiceMessageDTO.getVoiceBindingPort());
//            TopLabelWrapper.sendKeepAlive(voiceMessageDTO.getPacketID(), voiceMessageDTO.getFriendIdentity());
//            ConfigFile.VOICE_BINDING_PORT = voiceMessageDTO.getVoiceBindingPort();
//        }
        if (!MyfnfHashMaps.getInstance().getCallInitiators().isEmpty() && MyfnfHashMaps.getInstance().getCallInitiators().get(voiceMessageDTO.getPacketID()) != null) {
            MyfnfHashMaps.getInstance().getCallInitiators().get(voiceMessageDTO.getPacketID()).setVoiceBindingPort(voiceMessageDTO.getVoiceBindingPort());
            TopLabelWrapper.sendKeepAlive(voiceMessageDTO.getPacketID(), voiceMessageDTO.getFriendIdentity());
            ConfigFile.VOICE_BINDING_PORT = voiceMessageDTO.getVoiceBindingPort();
        }
    }

    @Override
    public void process_CALLING(VoiceMessageDTO messageDTO) {
        if (ConfigFile.CALL_ID == null) {
            CallerDto callChatInitiatorDto = MyfnfHashMaps.getInstance().getCallInitiators().get(messageDTO.getPacketID());
            CallUtils.setCallContants(callChatInitiatorDto);

            if (MainClass.getMainClass() == null) {
                UserBasicInfo single_p = FriendList.getInstance().getFriend_hash_map().get(messageDTO.getFriendIdentity());//();
                if (single_p != null) {
                    if (single_p.getUserIdentity() != null && single_p.getUserIdentity().length() > 0) {
                        CallStates.changeStatus(CallStates.UA_INCOMING_CALL);
                        CallSettingsDTo callSettingsDTo = new CallSettingsDTo();
                        callSettingsDTo.setUserid(MyFnFSettings.LOGIN_USER_ID);
                        callSettingsDTo.setFndId(single_p.getUserIdentity());
                        callSettingsDTo.setFn(single_p.getFullName());
                        callSettingsDTo.setIncomming(1);
                        callSettingsDTo.setPrIm(single_p.getProfileImage());
                        MainClass incommingCallUI = new MainClass(callSettingsDTo);
                        incommingCallUI.showWindow();
                        MainClass.playAudioHelper.clip_ring.play();
                    }
//                    if (MainClass.getMainClass().playAudioHelper != null && MainClass.getMainClass().playAudioHelper.clip_ring != null) {
//                        MainClass.getMainClass().playAudioHelper.clip_ring.play();
//                    }
                }

            }
        } else if (ConfigFile.FRIEND_ID != null && messageDTO.getFriendIdentity().equals(ConfigFile.FRIEND_ID)) {

            CallerDto firstF = MyfnfHashMaps.getInstance().getCallInitiators().get(ConfigFile.CALL_ID);
            CallerDto secondF = MyfnfHashMaps.getInstance().getCallInitiators().get(messageDTO.getPacketID());
            if (firstF != null && secondF != null) {
                if (firstF.getTm() < secondF.getTm()) {
                    CallUtils.setCallContants(firstF);
                } else {
                    CallUtils.setCallContants(secondF);
                }
                SignalGenerator.sendConnect(messageDTO.getFriendIdentity());
            }

            //  processConnected();
        }
    }

    @Override
    public void process_RINGING(final VoiceMessageDTO voiceMessageDTO) {
        if (CallStates.getStatus() != CallStates.UA_ONCALL || CallStates.getStatus() == CallStates.UA_OUTGOING_CALL) {
            try {
                if (MainClass.playAudioHelper != null && MainClass.playAudioHelper.clip_progress != null) {
                    MainClass.playAudioHelper.clip_progress.play();

//                    java.awt.EventQueue.invokeLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (CallStates.getStatus() == CallStates.UA_OUTGOING_CALL) {
//                                ringing++;
//                                if (ringing >= tryingTimes) {
//                                    VoiceMessageDTO messageDTO1 = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
//                                    byte[] confirmationByte1 = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.CANCELED, ConfigFile.CALL_ID, MyFnFSettings.LOGIN_USER_ID, messageDTO1.getFriendIdentity());
//                                    CallUtils.sendSignalingPacketWithIpPort(confirmationByte1);
//                                    CallUtils.resetCallingWindows(false);
//                                }
//                                if (IncomingOutgoingUI.getIncomingOutgoingUI() == null) {
////                                                    ringing = tryingTimes;
//                                }
//                            }
//                        }
//                    });
                }
            } catch (Exception e) {
              //  e.printStackTrace();
                log.error("Exception in process_RINGING method ==>" + e.getMessage());
            }
        }
    }

    @Override
    public void process_IN_CALL(VoiceMessageDTO voiceMessageDTO) {
        if (MyfnfHashMaps.getInstance().getCallInitiators().get(voiceMessageDTO.getPacketID()) != null) {
            CallerDto callChatInitiatorDto = MyfnfHashMaps.getInstance().getCallInitiators().get(voiceMessageDTO.getPacketID());
            TopLabelWrapper.sendSignalingPacket(VoiceConstants.CALL_STATE.IN_CALL_CONFIRMATION, voiceMessageDTO, callChatInitiatorDto.getSwitchIp(), callChatInitiatorDto.getSwitchPort());
            CallUtils.removeFromCallInitiators(voiceMessageDTO.getPacketID());
            System.out.println("incall confirmaiton send to " + voiceMessageDTO.getFriendIdentity());
        }
        if (ConfigFile.FRIEND_ID != null && voiceMessageDTO.getFriendIdentity().equals(ConfigFile.FRIEND_ID)) {
            CallUtils.failedCall("User busy");
            TopLabelWrapper.resetCallingWindows(true);
        } else {
            CallRegisterRequest.inCall = true;
        }
    }

    @Override
    public void process_IN_CALL_CONFIRMATION(VoiceMessageDTO voiceMessageDTO) {
    }

    @Override
    public void process_ANSWER(VoiceMessageDTO voiceMessageDTO) {
        ConfigFile.stopKeepAlive = true;
        if (ConfigFile.CALL_ID != null && ConfigFile.CALL_ID.equals(voiceMessageDTO.getPacketID())) {
            CallUtils.removeFromCallInitiators(ConfigFile.CALL_ID);
            SignalGenerator.sendConnect(voiceMessageDTO.getFriendIdentity());
            if (CallStates.getStatus() != CallStates.UA_ONCALL && MainClass.getMainClass() != null) {
                MainClass.getMainClass().callConnected(true);
                TopLabelWrapper.startAudioPlayerAndRecorder();
            }
        }
    }

    @Override
    public void process_CONNECTED(VoiceMessageDTO voiceMessageDTO) {
        ConfigFile.stopKeepAlive = true;
        if (CallStates.getStatus() != CallStates.UA_ONCALL && MainClass.getMainClass() != null) {
            TopLabelWrapper.processConnected();
        } else {
            SignalGenerator.sendCallIn(ConfigFile.FRIEND_ID, ConfigFile.CALL_ID, ConfigFile.VOICE_SERVER_IP, ConfigFile.VOICE_REGISTER_PORT);

        }
    }

    @Override
    public void process_BUSY(VoiceMessageDTO voiceMessageDTO) {
        if (ConfigFile.CALL_ID != null && ConfigFile.CALL_ID.equals(voiceMessageDTO.getPacketID())) {
            TopLabelWrapper.resetCallingWindows(true);
        } else if (ConfigFile.FRIEND_ID != null && voiceMessageDTO.getFriendIdentity().equals(ConfigFile.FRIEND_ID)) {
            TopLabelWrapper.resetCallingWindows(true);
        }
//          if (VoiceConstants.CALL_ID != null && VoiceConstants.CALL_ID.equals(messageDTO.getPacketID())) {
////
////                    if (!getAllreadyHavePackedIds().contains(tempPakID)) {
////                        clearAndAddAlreadyHavePacket(tempPakID);
////                        CallUtils.resetCallingWindows(false);
////                    }
////                } else if (VoiceConstants.callingToFriendId != null && messageDTO.getFriendIdentity().equals(VoiceConstants.callingToFriendId)) {
//////                    System.out.println("busy==>" + VoiceConstants.CALL_ID);
//////                    System.out.println("busy id==>" + messageDTO.getPacketID());
//////                    System.out.println("busy from==> " + messageDTO.getFriendIdentity());
////                    CallUtils.resetCallingWindows(false);
////                }
    }

    @Override
    public void process_CANCELED(VoiceMessageDTO voiceMessageDTO) {
        if (ConfigFile.CALL_ID != null && ConfigFile.CALL_ID.equals(voiceMessageDTO.getPacketID())) {
            TopLabelWrapper.resetCallingWindows(true);
        } else if (ConfigFile.FRIEND_ID != null && voiceMessageDTO.getFriendIdentity().equals(ConfigFile.FRIEND_ID)) {
            TopLabelWrapper.resetCallingWindows(true);
        } else {
            System.out.println("cancel msg ignored");
        }
        //                    if (!NewsFeedMaps.getInstance().getAllreadyHavePackedIds().contains(tempPakID)) {
//                        clearAndAddAlreadyHavePacket(tempPakID);
//                        if (VoiceConstants.CALL_ID.equals(messageDTO.getPacketID())) {
//                            CallUtils.resetCallingWindows(true);
//                        } else if (VoiceConstants.SAME_FRIEND_CALL_ID != null) {
//                            CallUtils.resetCallingWindows(true);
//                        }
//                    }
    }

    @Override
    public void process_BYE(VoiceMessageDTO messageDTO) {
        if (ConfigFile.CALL_ID != null && ConfigFile.CALL_ID.equals(messageDTO.getPacketID())) {
            if (ConfigFile.FRIEND_ID != null && ConfigFile.FRIEND_ID.equals(messageDTO.getFriendIdentity())) {
                TopLabelWrapper.resetCallingWindows(true);
            }
        } else if (ConfigFile.FRIEND_ID != null && messageDTO.getFriendIdentity().equals(ConfigFile.FRIEND_ID)) {
            TopLabelWrapper.resetCallingWindows(true);
        } else {
            System.out.println("BYE msg ignored");
        }
    }

    @Override
    public void process_DISCONNECTED(VoiceMessageDTO voiceMessageDTO) {
        if (ConfigFile.CALL_ID != null && ConfigFile.CALL_ID.equals(voiceMessageDTO.getPacketID())) {
            TopLabelWrapper.resetCallingWindows(true);
        } else if (ConfigFile.FRIEND_ID != null && voiceMessageDTO.getFriendIdentity().equals(ConfigFile.FRIEND_ID)) {
            TopLabelWrapper.resetCallingWindows(true);
        }
    }

    @Override
    public void process_NO_ANSWER(VoiceMessageDTO voiceMessageDTO) {

        if (ConfigFile.FRIEND_ID != null && ConfigFile.FRIEND_ID.equals(voiceMessageDTO.getFriendIdentity())) {
            TopLabelWrapper.resetCallingWindows(true);
        }
    }

    @Override
    public void process_VOICE_REGISTER_PUSH(VoiceMessageDTO voiceMessageDTO) {
        TopLabelWrapper.sendKeepAlive(ConfigFile.CALL_ID, ConfigFile.FRIEND_ID);
    }

    @Override
    public void process_VOICE_REGISTER_PUSH_CONFIRMATION(VoiceMessageDTO voiceMessageDTO) {
    }

    @Override
    public void process_VOICE_CALL_HOLD(VoiceMessageDTO voiceMessageDTO) {
        if (ConfigFile.CALL_ID != null && ConfigFile.CALL_ID.equals(voiceMessageDTO.getPacketID())) {
            if (CallStates.getStatus() != CallStates.HOLD_CALL) {
                CallStates.changeStatus(CallStates.HOLD_CALL);

                TopLabelWrapper.stopPlayerAndRecorder();
                TopLabelWrapper.sendKeepAlive();
                if (MainClass.getMainClass() != null) {
                    MainClass.getMainClass().showHoldUnholdPanel(false);
                    if (MainClass.playAudioHelper.hold_tune != null) {
                        MainClass.playAudioHelper.hold_tune.play();

                    }

                }

            }
        }
    }

    @Override
    public void process_VOICE_CALL_HOLD_CONFIRMATION(VoiceMessageDTO voiceMessageDTO) {
    }

    @Override
    public void process_VOICE_CALL_UNHOLD(VoiceMessageDTO voiceMessageDTO) {
        if (ConfigFile.CALL_ID != null && ConfigFile.CALL_ID.equals(voiceMessageDTO.getPacketID())) {
            if (CallStates.getStatus() == CallStates.HOLD_CALL) {
                CallStates.changeStatus(CallStates.UA_ONCALL);
                if (MainClass.getMainClass() != null) {
                    MainClass.getMainClass().showHoldUnholdPanel(true);
                    if (MainClass.playAudioHelper != null && MainClass.playAudioHelper.hold_tune != null) {
                        MainClass.playAudioHelper.hold_tune.stop();
                    }
                }
                TopLabelWrapper.startAudioPlayerAndRecorder();

            }
        }
    }

    @Override
    public void process_VOICE_UNHOLD_CONFIRMATION(VoiceMessageDTO voiceMessageDTO) {
    }

    @Override
    public void process_VOICE_BUSY_MESSAGE(VoiceMessageDTO messageDTO) {
        if (ConfigFile.CALL_ID != null && ConfigFile.CALL_ID.equals(messageDTO.getPacketID())) {
            if (ConfigFile.FRIEND_ID != null && ConfigFile.FRIEND_ID.equals(messageDTO.getFriendIdentity())) {
                TopLabelWrapper.resetCallingWindows(true);
            }
        } else if (ConfigFile.FRIEND_ID != null && messageDTO.getFriendIdentity().equals(ConfigFile.FRIEND_ID)) {
            TopLabelWrapper.resetCallingWindows(true);
        }
        if (MainClass.getMainClass() != null) {
            MainClass.getMainClass().stopPrgress();
        }
        CallUtils.failedCall(messageDTO.getVoiceBusyMessage());

    }

    @Override
    public void process_VOICE_BUSY_MESSAGE_CONFIRMATION(VoiceMessageDTO voiceMessageDTO) {
    }
    //   int calling = 0;

}
