/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.listeners;

import com.desktopCall.dtos.VoiceMessageDTO;

/**
 *
 * @author Faiz
 */
public interface UiCommunicator {

    public void process_RTP(byte[] receivedBuffer, int length);

    public void process_VOICE_REGISTER_CONFIRMATION(VoiceMessageDTO voiceMessageDTO);

    public void process_CALLING(VoiceMessageDTO voiceMessageDTO);

    public void process_RINGING(VoiceMessageDTO voiceMessageDTO);

    public void process_IN_CALL(VoiceMessageDTO voiceMessageDTO);

    public void process_IN_CALL_CONFIRMATION(VoiceMessageDTO voiceMessageDTO);

    public void process_ANSWER(VoiceMessageDTO voiceMessageDTO);

    public void process_CONNECTED(VoiceMessageDTO voiceMessageDTO);

    public void process_BUSY(VoiceMessageDTO voiceMessageDTO);

    public void process_CANCELED(VoiceMessageDTO voiceMessageDTO);

    public void process_BYE(VoiceMessageDTO voiceMessageDTO);

    public void process_DISCONNECTED(VoiceMessageDTO voiceMessageDTO);

    public void process_NO_ANSWER(VoiceMessageDTO voiceMessageDTO);

    public void process_VOICE_REGISTER_PUSH(VoiceMessageDTO voiceMessageDTO);

    public void process_VOICE_REGISTER_PUSH_CONFIRMATION(VoiceMessageDTO voiceMessageDTO);

    public void process_VOICE_CALL_HOLD(VoiceMessageDTO voiceMessageDTO);

    public void process_VOICE_CALL_HOLD_CONFIRMATION(VoiceMessageDTO voiceMessageDTO);

    public void process_VOICE_CALL_UNHOLD(VoiceMessageDTO voiceMessageDTO);

    public void process_VOICE_UNHOLD_CONFIRMATION(VoiceMessageDTO voiceMessageDTO);

    public void process_VOICE_BUSY_MESSAGE(VoiceMessageDTO voiceMessageDTO);

    public void process_VOICE_BUSY_MESSAGE_CONFIRMATION(VoiceMessageDTO voiceMessageDTO);

    /*
     case VoiceConstants.VOICE_REGISTER_CONFIRMATION:
     messageDTO = VoicePacketProcessor.getRegisterConfirmationPacket(receivedBuffer);
     if (!MyfnfHashMaps.getInstance().getCallInitiators().isEmpty() && MyfnfHashMaps.getInstance().getCallInitiators().get(messageDTO.getPacketID()) != null) {
     tempPakID = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.VOICE_REGISTER_CONFIRMATION);
     VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID);
     if (!NewsFeedMaps.getInstance().getAllreadyHavePackedIds().contains(tempPakID)) {
     clearAndAddAlreadyHavePacket(tempPakID);
     MyfnfHashMaps.getInstance().getCallInitiators().get(messageDTO.getPacketID()).setVoiceBindingPort(messageDTO.getVoiceBindingPort());
     sendKeepAlive(messageDTO.getPacketID(), messageDTO.getFriendIdentity());
     }
     }
     break;
     case VoiceConstants.CALL_STATE.CALLING:
     messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
     tempPakID = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.CALLING);
     //System.out.println("Calling==>" + messageDTO.getPacketID());
     if (!NewsFeedMaps.getInstance().getAllreadyHavePackedIds().contains(tempPakID)) {
     clearAndAddAlreadyHavePacket(tempPakID);
     if (VoiceConstants.CALL_ID == null) {
     CallChatInitiatorDto callChatInitiatorDto = MyfnfHashMaps.getInstance().getCallInitiators().get(messageDTO.getPacketID());
     CallUtils.setCallContants(callChatInitiatorDto);
     confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.RINGING, messageDTO.getPacketID(), MyFnFSettings.LOGIN_USER_ID, messageDTO.getFriendIdentity());
     CallUtils.sendSignalingPacket(confirmationByte);

     if (IncomingOutgoingUI.getIncomingOutgoingUI() == null) {
     UserBasicInfo single_p = FriendList.getInstance().getFriend_hash_map().get(messageDTO.getFriendIdentity());//();
     if (single_p != null) {
     if (single_p.getUserIdentity() != null && single_p.getUserIdentity().length() > 0) {
     CallStates.changeStatus(CallStates.UA_INCOMING_CALL);
     IncomingOutgoingUI incommingCallUI = new IncomingOutgoingUI(true, single_p);
     incommingCallUI.setVisible(true);
     }
     if (playAudioHelper.clip_ring != null) {
     playAudioHelper.clip_ring.play();
     }
     }

     }
     } else if (VoiceConstants.callingToFriendId != null && messageDTO.getFriendIdentity().equals(VoiceConstants.callingToFriendId)) {

     String tempPakID3 = CallUtils.getPack(VoiceConstants.CALL_ID, VoiceConstants.CALL_STATE.RINGING);
     VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID3);
     CallChatInitiatorDto firstF = MyfnfHashMaps.getInstance().getCallInitiators().get(VoiceConstants.CALL_ID);
     CallChatInitiatorDto secondF = MyfnfHashMaps.getInstance().getCallInitiators().get(messageDTO.getPacketID());
     if (firstF != null && secondF != null) {
     if (firstF.getTm() < secondF.getTm()) {
     CallUtils.setCallContants(firstF);
     } else {
     CallUtils.setCallContants(secondF);
     }
     System.out.println("sending connected while calling found==>");
     CallUtils.sendConnect(messageDTO.getFriendIdentity());
     }

     //  processConnected();
     }
     }
     break;
     case VoiceConstants.CALL_STATE.RINGING:
     messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
     //  //System.out.println("ringign.............");
     if (VoiceConstants.CALL_ID != null && VoiceConstants.CALL_ID.equals(messageDTO.getPacketID())) {
     tempPakID = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.RINGING);
     if (!NewsFeedMaps.getInstance().getAllreadyHavePackedIds().contains(tempPakID)) {
     clearAndAddAlreadyHavePacket(tempPakID);
     VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID);
     if (CallStates.getStatus() != CallStates.UA_ONCALL || CallStates.getStatus() == CallStates.UA_OUTGOING_CALL) {
     try {
     if (playAudioHelper.clip_progress != null) {
     playAudioHelper.clip_progress.play();
     }
     ringing = 0;
     if (IncomingOutgoingUI.getIncomingOutgoingUI() != null) {
     java.awt.EventQueue.invokeLater(new Runnable() {
     @Override
     public void run() {
     if (CallStates.getStatus() == CallStates.UA_OUTGOING_CALL) {
     ringing++;
     if (ringing >= tryingTimes) {
     VoiceMessageDTO messageDTO1 = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
     byte[] confirmationByte1 = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.CANCELED, VoiceConstants.CALL_ID, MyFnFSettings.LOGIN_USER_ID, messageDTO1.getFriendIdentity());
     CallUtils.sendSignalingPacket(confirmationByte1);
     CallUtils.resetCallingWindows(false);
     }
     if (IncomingOutgoingUI.getIncomingOutgoingUI() == null) {
     //                                                    ringing = tryingTimes;
     }
     }
     }
     });
     }
     } catch (Exception e) {
     e.printStackTrace();
     }
     } else {
     //System.out.println("CallUtils.getStatus()==>" + CallStates.getStatus());
     }
     }
     }

     break;
     case VoiceConstants.CALL_STATE.IN_CALL:
     try {
     messageDTO = VoicePacketProcessor.getIncallPacket(receivedBuffer);
     tempPakID = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.IN_CALL);
     if (!NewsFeedMaps.getInstance().getAllreadyHavePackedIds().contains(tempPakID)) {
     clearAndAddAlreadyHavePacket(tempPakID);
     confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.IN_CALL_CONFIRMATION, messageDTO.getPacketID(), MyFnFSettings.LOGIN_USER_ID, messageDTO.getFriendIdentity());
     CallUtils.sendSignalingPacket(confirmationByte);
     if (VoiceConstants.callingToFriendId != null && messageDTO.getFriendIdentity().equals(VoiceConstants.callingToFriendId)) {
     try {
     System.out.println("IN_CALL==>" + VoiceConstants.CALL_ID);
     System.out.println("IN_CALL id==>" + messageDTO.getPacketID());
     System.out.println("IN_CALL from==> " + messageDTO.getFriendIdentity());
     CallUtils.failedCall("User busy");
     CallUtils.resetCallingWindows(false);
     } catch (Exception e3) {
     e3.printStackTrace();
     }

     } else {
     CallRegisterRequest.inCall = true;
     }
     }
     } catch (Exception e) {
     }
     break;
     case VoiceConstants.CALL_STATE.IN_CALL_CONFIRMATION:
     try {
     messageDTO = VoicePacketProcessor.getIncallPacket(receivedBuffer);
     tempPakID = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.IN_CALL_CONFIRMATION);
     VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID);
     } catch (Exception e) {
     }
     break;
     case VoiceConstants.CALL_STATE.ANSWER:
     try {
     messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
     tempPakID = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.ANSWER);
     CallUtils.removeFromCallInitiators(VoiceConstants.CALL_ID);
     if (!NewsFeedMaps.getInstance().getAllreadyHavePackedIds().contains(tempPakID)) {
     clearAndAddAlreadyHavePacket(tempPakID);
     if (VoiceConstants.CALL_ID != null && VoiceConstants.CALL_ID.equals(messageDTO.getPacketID())) {
     CallUtils.sendConnect(messageDTO.getFriendIdentity());
     if (CallStates.getStatus() != CallStates.UA_ONCALL && IncomingOutgoingUI.getIncomingOutgoingUI() != null) {
     IncomingOutgoingUI.getIncomingOutgoingUI().callConnected(true);
     startAudioPlayerAndRecorder();
     }
     }
     }
     } catch (Exception e) {
     e.printStackTrace();
     }

     break;
     case VoiceConstants.CALL_STATE.CONNECTED:
     messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
     tempPakID = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.CONNECTED);
     //System.out.println("CONNECTED " + messageDTO.getPacketID());

     if (!NewsFeedMaps.getInstance().getAllreadyHavePackedIds().contains(tempPakID)) {
     clearAndAddAlreadyHavePacket(tempPakID);
     if (CallStates.getStatus() != CallStates.UA_ONCALL && IncomingOutgoingUI.getIncomingOutgoingUI() != null) {
     if (VoiceConstants.CALL_ID != null && VoiceConstants.CALL_ID.equals(messageDTO.getPacketID())) {
     VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID);
     processConnected();
     if (VoiceConstants.callingToFriendId != null && messageDTO.getFriendIdentity().equals(VoiceConstants.callingToFriendId)) {
     String tempPakID3 = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.RINGING);
     VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID3);
     }
     //System.out.println("normal connect");
     //                        if (VoiceConstants.SAME_FRIEND_CALL_ID != null) {
     //                            String tempPakID3 = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.RINGING);
     //                            VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID3);
     //                            if (CallStates.getStatus() != CallStates.UA_ONCALL && IncomingOutgoingUI.getIncomingOutgoingUI() != null) {
     //                                CallChatInitiatorDto callChatInitiatorDto = MyfnfHashMaps.getInstance().getCallInitiators().get(messageDTO.getPacketID());
     //                                CallUtils.setCallContants(callChatInitiatorDto);
     //                                CallUtils.sendConnect(messageDTO.getFriendIdentity());
     //                            }
     //                        }

     } else if (VoiceConstants.CALL_ID != null && VoiceConstants.callingToFriendId != null && messageDTO.getFriendIdentity().equals(VoiceConstants.callingToFriendId)) {
     System.out.println("same caller connected found");
     String tempPakID3 = CallUtils.getPack(VoiceConstants.CALL_ID, VoiceConstants.CALL_STATE.RINGING);
     VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID3);
     processConnected();

     }
     } else {
     System.out.println("sending in call ");
     CallChatInitiatorDto callChatInitiatorDto = MyfnfHashMaps.getInstance().getCallInitiators().get(messageDTO.getPacketID());
     if (callChatInitiatorDto != null) {
     CallUtils.sendCallIn(callChatInitiatorDto.getFriendIdentity(), callChatInitiatorDto.getCallID(), callChatInitiatorDto.getSwitchIp(), callChatInitiatorDto.getSwitchPort());
     }
     }
     }
     break;

     case VoiceConstants.CALL_STATE.BUSY:
     messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
     tempPakID = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.BUSY);
     if (VoiceConstants.CALL_ID != null && VoiceConstants.CALL_ID.equals(messageDTO.getPacketID())) {
     confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.DISCONNECTED, messageDTO.getPacketID(), MyFnFSettings.LOGIN_USER_ID, messageDTO.getFriendIdentity());
     CallUtils.sendSignalingPacket(confirmationByte);
     if (!NewsFeedMaps.getInstance().getAllreadyHavePackedIds().contains(tempPakID)) {
     clearAndAddAlreadyHavePacket(tempPakID);
     CallUtils.resetCallingWindows(false);
     }
     } else if (VoiceConstants.callingToFriendId != null && messageDTO.getFriendIdentity().equals(VoiceConstants.callingToFriendId)) {
     //                    System.out.println("busy==>" + VoiceConstants.CALL_ID);
     //                    System.out.println("busy id==>" + messageDTO.getPacketID());
     //                    System.out.println("busy from==> " + messageDTO.getFriendIdentity());
     CallUtils.resetCallingWindows(false);
     }
     break;
     case VoiceConstants.CALL_STATE.CANCELED:
     messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
     tempPakID = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.CANCELED);
     if (!NewsFeedMaps.getInstance().getAllreadyHavePackedIds().contains(tempPakID)) {
     confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.DISCONNECTED, messageDTO.getPacketID(), MyFnFSettings.LOGIN_USER_ID, messageDTO.getFriendIdentity());
     CallUtils.sendSignalingPacket(confirmationByte);
     if (!NewsFeedMaps.getInstance().getAllreadyHavePackedIds().contains(tempPakID)) {
     clearAndAddAlreadyHavePacket(tempPakID);
     if (VoiceConstants.CALL_ID.equals(messageDTO.getPacketID())) {
     CallUtils.resetCallingWindows(true);
     } else if (VoiceConstants.SAME_FRIEND_CALL_ID != null) {
     CallUtils.resetCallingWindows(true);
     }
     }
     }
     break;

     case VoiceConstants.CALL_STATE.BYE:
     messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
     tempPakID = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.BYE);
     confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.DISCONNECTED, messageDTO.getPacketID(), MyFnFSettings.LOGIN_USER_ID, messageDTO.getFriendIdentity());
     if (!NewsFeedMaps.getInstance().getAllreadyHavePackedIds().contains(tempPakID)) {
     clearAndAddAlreadyHavePacket(tempPakID);
     CallUtils.removeFromCallInitiators(messageDTO.getPacketID());
     CallUtils.sendSignalingPacket(confirmationByte);
     if (VoiceConstants.CALL_ID != null && VoiceConstants.CALL_ID.equals(messageDTO.getPacketID())) {
     if (VoiceConstants.callingToFriendId != null && VoiceConstants.callingToFriendId.equals(messageDTO.getFriendIdentity())) {
     CallUtils.resetCallingWindows(false);
     }
     } else if (VoiceConstants.callingToFriendId != null && messageDTO.getFriendIdentity().equals(VoiceConstants.callingToFriendId)) {
     //                        System.out.println("calli==>" + VoiceConstants.CALL_ID);
     //                        System.out.println("bye id==>" + messageDTO.getPacketID());
     //                        System.out.println("bye from==> " + messageDTO.getFriendIdentity());
     CallUtils.resetCallingWindows(false);
     }
     } else {
     }
     break;

     case VoiceConstants.CALL_STATE.DISCONNECTED:
     messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
     tempPakID = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.DISCONNECTED);
     VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.DISCONNECTED));
     if (!NewsFeedMaps.getInstance().getAllreadyHavePackedIds().contains(tempPakID)) {
     clearAndAddAlreadyHavePacket(tempPakID);
     if (VoiceConstants.CALL_ID != null && VoiceConstants.CALL_ID.equals(messageDTO.getPacketID())) {
     CallUtils.resetCallingWindows(false);
     } else if (VoiceConstants.callingToFriendId != null && messageDTO.getFriendIdentity().equals(VoiceConstants.callingToFriendId)) {
     //                        System.out.println("DISCONNECTED calli==>" + VoiceConstants.CALL_ID);
     //                        System.out.println("DISCONNECTED id==>" + messageDTO.getPacketID());
     //                        System.out.println("DISCONNECTED from==> " + messageDTO.getFriendIdentity());
     CallUtils.resetCallingWindows(false);
     }
     }
     break;
     case VoiceConstants.CALL_STATE.NO_ANSWER:
     messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
     tempPakID = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.NO_ANSWER);
     if (VoiceConstants.CALL_ID != null && VoiceConstants.CALL_ID.equals(messageDTO.getPacketID())) {
     //  CallUtils.removeFromCallInitiators(VoiceConstants.CALL_ID);
     confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.DISCONNECTED, messageDTO.getPacketID(), MyFnFSettings.LOGIN_USER_ID, messageDTO.getFriendIdentity());
     CallUtils.sendSignalingPacket(confirmationByte);
     if (!NewsFeedMaps.getInstance().getAllreadyHavePackedIds().contains(tempPakID)) {
     clearAndAddAlreadyHavePacket(tempPakID);
     if (VoiceConstants.callingToFriendId != null && VoiceConstants.callingToFriendId.equals(messageDTO.getFriendIdentity())) {
     CallUtils.resetCallingWindows(false);
     }
     }
     }
     break;
     case VoiceConstants.CALL_STATE.VOICE_REGISTER_PUSH:
     messageDTO = VoicePacketProcessor.getPushMessageConfirmationPacket(receivedBuffer);
     tempPakID = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.VOICE_REGISTER_PUSH_CONFIRMATION);
     if (!MyfnfHashMaps.getInstance().getCallInitiators().isEmpty() && MyfnfHashMaps.getInstance().getCallInitiators().get(messageDTO.getPacketID()) != null) {
     VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID);
     if (!NewsFeedMaps.getInstance().getAllreadyHavePackedIds().contains(tempPakID)) {
     clearAndAddAlreadyHavePacket(tempPakID);
     MyfnfHashMaps.getInstance().getCallInitiators().get(messageDTO.getPacketID()).setVoiceBindingPort(messageDTO.getVoiceBindingPort());
     sendKeepAlive(messageDTO.getPacketID(), messageDTO.getFriendIdentity());
     }
     }
     break;
     case VoiceConstants.CALL_STATE.VOICE_REGISTER_PUSH_CONFIRMATION:
     messageDTO = VoicePacketProcessor.getPushMessageConfirmationPacket(receivedBuffer);
     tempPakID = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.VOICE_REGISTER_PUSH_CONFIRMATION);
     VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID);
     break;
     case VoiceConstants.CALL_STATE.VOICE_CALL_HOLD:
     System.out.println("hold found");
     messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
     tempPakID = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.VOICE_CALL_HOLD);
     confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.VOICE_CALL_HOLD_CONFIRMATION, messageDTO.getPacketID(), MyFnFSettings.LOGIN_USER_ID, messageDTO.getFriendIdentity());
     CallUtils.sendSignalingPacket(confirmationByte);
     if (VoiceConstants.CALL_ID != null && VoiceConstants.CALL_ID.equals(messageDTO.getPacketID())) {
     if (CallStates.getStatus() != CallStates.HOLD_CALL) {
     CallStates.changeStatus(CallStates.HOLD_CALL);

     stopPlayerAndRecorder();
     sendKeepAlive();
     if (IncomingOutgoingUI.getIncomingOutgoingUI() != null) {
     IncomingOutgoingUI.getIncomingOutgoingUI().showHoldePanel(false);
     }
     if (playAudioHelper.hold_tune != null) {
     playAudioHelper.hold_tune.play();
     }
     }
     }

     break;
     case VoiceConstants.CALL_STATE.VOICE_CALL_HOLD_CONFIRMATION:
     messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
     if (VoiceConstants.CALL_ID != null && VoiceConstants.CALL_ID.equals(messageDTO.getPacketID())) {
     messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
     tempPakID = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.VOICE_CALL_HOLD);
     VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID);
     //                    if (playAudioHelper.hold_tune != null) {
     //                        playAudioHelper.hold_tune.play();
     //                    }
     }
     break;
     case VoiceConstants.CALL_STATE.VOICE_CALL_UNHOLD:
     System.out.println("unhold found ");
     messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
     confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.VOICE_UNHOLD_CONFIRMATION, messageDTO.getPacketID(), MyFnFSettings.LOGIN_USER_ID, messageDTO.getFriendIdentity());
     CallUtils.sendSignalingPacket(confirmationByte);
     if (VoiceConstants.CALL_ID != null && VoiceConstants.CALL_ID.equals(messageDTO.getPacketID())) {
     if (CallStates.getStatus() == CallStates.HOLD_CALL) {
     CallStates.changeStatus(CallStates.UA_ONCALL);
     if (playAudioHelper.hold_tune != null) {
     playAudioHelper.hold_tune.stop();
     }
     startAudioPlayerAndRecorder();
     if (IncomingOutgoingUI.getIncomingOutgoingUI() != null) {
     IncomingOutgoingUI.getIncomingOutgoingUI().showHoldePanel(true);
     }
     }
     }
     break;
     case VoiceConstants.CALL_STATE.VOICE_UNHOLD_CONFIRMATION:
     messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
     if (VoiceConstants.CALL_ID != null && VoiceConstants.CALL_ID.equals(messageDTO.getPacketID())) {
     System.out.println("unhold found confirmation");
     //                    if (playAudioHelper.hold_tune != null) {
     //                        playAudioHelper.hold_tune.stop();
     //                    }
     messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
     tempPakID = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.VOICE_CALL_UNHOLD);
     VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID);
     }
     break;
     case VoiceConstants.CALL_STATE.VOICE_BUSY_MESSAGE:
     messageDTO = VoicePacketProcessor.getSignalingPacketWithBusymessage(receivedBuffer);
     confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.VOICE_BUSY_MESSAGE_CONFIRMATION, messageDTO.getPacketID(), MyFnFSettings.LOGIN_USER_ID, messageDTO.getFriendIdentity());
     tempPakID = CallUtils.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.VOICE_BUSY_MESSAGE_CONFIRMATION);
     CallUtils.sendSignalingPacket(confirmationByte);
     if (!NewsFeedMaps.getInstance().getAllreadyHavePackedIds().contains(tempPakID)) {

     clearAndAddAlreadyHavePacket(tempPakID);
     if (VoiceConstants.CALL_ID != null && VoiceConstants.CALL_ID.equals(messageDTO.getPacketID())) {
     if (VoiceConstants.callingToFriendId != null && VoiceConstants.callingToFriendId.equals(messageDTO.getFriendIdentity())) {
     CallUtils.resetCallingWindows(false);
     }
     } else if (VoiceConstants.callingToFriendId != null && messageDTO.getFriendIdentity().equals(VoiceConstants.callingToFriendId)) {
     CallUtils.resetCallingWindows(false);
     }
     CallUtils.failedCall(messageDTO.getVoiceBusyMessage());
     }

     break;
     */
}
