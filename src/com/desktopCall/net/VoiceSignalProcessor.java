/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.net;

import com.desktopCall.listeners.OnReceiveVoiceInterface;
import com.desktopCall.dtos.VoiceMessageDTO;
import com.desktopCall.listeners.UiCommunicator;
import com.desktopCall.settings.ConfigFile;
import com.desktopCall.settings.Helpers;
import com.desktopCall.settings.SignalGenerator;
import com.desktopCall.settings.VoiceConstants;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 *
 * @author Faiz
 */
public class VoiceSignalProcessor implements OnReceiveVoiceInterface {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(VoiceSignalProcessor.class);
    public VoiceReceiverSocket voiceSocket;
    public static VoiceSignalProcessor voiceMainReceiver;
    private UiCommunicator uiCommunicator;

    public static void VoiceSignalProcessor(VoiceSignalProcessor udp_transport) {
        VoiceSignalProcessor.voiceMainReceiver = udp_transport;
    }

    public static VoiceSignalProcessor getInstance() {
        if (voiceMainReceiver == null) {
            voiceMainReceiver = new VoiceSignalProcessor();
        }
        return voiceMainReceiver;
    }

    public void send(DatagramPacket packet) throws IOException {
        if (voiceSocket != null) {
            voiceSocket.send(packet);
        }
    }

    public void close_socket() {
        if (voiceSocket != null) {
            voiceSocket.halt();
        }
    }

    public void initUdp(UiCommunicator uiCommunicator) {
        DatagramSocket udp_socket = null;
        try {
            udp_socket = new DatagramSocket();
            voiceSocket = new VoiceReceiverSocket(udp_socket, this);
            voiceSocket.start();
            this.uiCommunicator = uiCommunicator;
        } catch (SocketException ex) {
           // Helpers.systemPrint("Can not initiat voice packet");
        log.error("Error in VoiceSignalProcessor class" + ex.getMessage());
        }

    }

    public void resetVoiceAll() {
        close_socket();
        voiceMainReceiver = null;
    }
    int ringing = 0;
    String tempPakID;

    @Override
    public void onReceivedPacket(final byte[] receivedBuffer, int length) {
        int packetType = receivedBuffer[0];

        VoiceMessageDTO messageDTO = null;
        byte[] confirmationByte;
        switch (packetType) {
            case VoiceConstants.VOICE_MEDIA:
                uiCommunicator.process_RTP(receivedBuffer, length);
                ConfigFile.numberOfRTPreceiving++;
                break;
            case VoiceConstants.VOICE_REGISTER_CONFIRMATION:
                messageDTO = VoicePacketProcessor.getRegisterConfirmationPacket(receivedBuffer);
                tempPakID = Helpers.getPack(messageDTO.getPacketID(), VoiceConstants.VOICE_REGISTER_CONFIRMATION);
                VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID);
                if (!getAllreadyHavePackedIds().contains(tempPakID)) {
                    clearAndAddAlreadyHavePacket(tempPakID);
                    uiCommunicator.process_VOICE_REGISTER_CONFIRMATION(messageDTO);
                }
                break;
            case VoiceConstants.CALL_STATE.CALLING:
                messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
                tempPakID = Helpers.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.CALLING);
                if (!getAllreadyHavePackedIds().contains(tempPakID)) {
                    clearAndAddAlreadyHavePacket(tempPakID);
                    uiCommunicator.process_CALLING(messageDTO);
                    if (ConfigFile.CALL_ID == null) {
                        confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.RINGING, messageDTO.getPacketID(), ConfigFile.USER_ID, messageDTO.getFriendIdentity());
                        SignalGenerator.sendSignalingPacket(confirmationByte);
                    } else if (ConfigFile.CALL_ID != null) {
                        confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.RINGING, messageDTO.getPacketID(), ConfigFile.USER_ID, messageDTO.getFriendIdentity());
                        SignalGenerator.sendSignalingPacket(confirmationByte);
                    } else if (ConfigFile.FRIEND_ID != null && messageDTO.getFriendIdentity().equals(ConfigFile.FRIEND_ID)) {

                        String tempPakID3 = Helpers.getPack(ConfigFile.CALL_ID, VoiceConstants.CALL_STATE.RINGING);
                        VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID3);
                    }

                }
                break;
            case VoiceConstants.CALL_STATE.RINGING:
                messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
                if (ConfigFile.CALL_ID != null && ConfigFile.CALL_ID.equals(messageDTO.getPacketID())) {
                    tempPakID = Helpers.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.RINGING);
                    if (!getAllreadyHavePackedIds().contains(tempPakID)) {
                        clearAndAddAlreadyHavePacket(tempPakID);
                        VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID);
                        uiCommunicator.process_RINGING(messageDTO);
                    }
                }

                break;
            case VoiceConstants.CALL_STATE.IN_CALL:
                try {
                    messageDTO = VoicePacketProcessor.getIncallPacket(receivedBuffer);
                    tempPakID = Helpers.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.IN_CALL);
                    if (!getAllreadyHavePackedIds().contains(tempPakID)) {
                        clearAndAddAlreadyHavePacket(tempPakID);
                        uiCommunicator.process_IN_CALL(messageDTO);

                    }
                } catch (Exception e) {
                }
                break;
            case VoiceConstants.CALL_STATE.IN_CALL_CONFIRMATION:
                try {

                    messageDTO = VoicePacketProcessor.getIncallPacket(receivedBuffer);
                    System.out.println("IN_CALL_CONFIRMATION==>" + messageDTO.getPacketID());
                    tempPakID = Helpers.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.IN_CALL_CONFIRMATION);
                    VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID);
                } catch (Exception e) {
                }
                break;
            case VoiceConstants.CALL_STATE.ANSWER:
                try {
                    messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
                    tempPakID = Helpers.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.ANSWER);

                    if (!getAllreadyHavePackedIds().contains(tempPakID)) {
                        clearAndAddAlreadyHavePacket(tempPakID);
                        uiCommunicator.process_ANSWER(messageDTO);
                    }
                } catch (Exception e) {
                   // e.printStackTrace();
                log.error("Error in VoiceSignalProcessor class" + e.getMessage());
                }

                break;
            case VoiceConstants.CALL_STATE.CONNECTED:
                messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
                tempPakID = Helpers.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.CONNECTED);

                if (!getAllreadyHavePackedIds().contains(tempPakID)) {
                    clearAndAddAlreadyHavePacket(tempPakID);
                    VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID);
                    uiCommunicator.process_CONNECTED(messageDTO);
                }
                break;

            case VoiceConstants.CALL_STATE.BUSY:
                messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);

                confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.DISCONNECTED, messageDTO.getPacketID(), ConfigFile.USER_ID, messageDTO.getFriendIdentity());
                SignalGenerator.sendSignalingPacket(confirmationByte);
                tempPakID = Helpers.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.BUSY);
                if (!getAllreadyHavePackedIds().contains(tempPakID)) {
                    clearAndAddAlreadyHavePacket(tempPakID);
                    uiCommunicator.process_BUSY(messageDTO);
                }
                break;
            case VoiceConstants.CALL_STATE.CANCELED:
                messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
                tempPakID = Helpers.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.CANCELED);
                confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.DISCONNECTED, messageDTO.getPacketID(), ConfigFile.USER_ID, messageDTO.getFriendIdentity());
                SignalGenerator.sendSignalingPacket(confirmationByte);
                if (!getAllreadyHavePackedIds().contains(tempPakID)) {
                    clearAndAddAlreadyHavePacket(tempPakID);
                    uiCommunicator.process_CANCELED(messageDTO);
                }
                break;

            case VoiceConstants.CALL_STATE.BYE:
                messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
                tempPakID = Helpers.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.BYE);
                confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.DISCONNECTED, messageDTO.getPacketID(), ConfigFile.USER_ID, messageDTO.getFriendIdentity());
                SignalGenerator.sendSignalingPacket(confirmationByte);
                if (!getAllreadyHavePackedIds().contains(tempPakID)) {
                    clearAndAddAlreadyHavePacket(tempPakID);
                    uiCommunicator.process_BYE(messageDTO);
                } else {
                }
                break;

            case VoiceConstants.CALL_STATE.DISCONNECTED:
                messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
                tempPakID = Helpers.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.DISCONNECTED);
                VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(Helpers.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.DISCONNECTED));
                if (!getAllreadyHavePackedIds().contains(tempPakID)) {
                    clearAndAddAlreadyHavePacket(tempPakID);
                    uiCommunicator.process_DISCONNECTED(messageDTO);
                }
                break;
            case VoiceConstants.CALL_STATE.NO_ANSWER:
                messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
                confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.DISCONNECTED, messageDTO.getPacketID(), ConfigFile.USER_ID, messageDTO.getFriendIdentity());
                SignalGenerator.sendSignalingPacket(confirmationByte);
                tempPakID = Helpers.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.NO_ANSWER);
                if (!getAllreadyHavePackedIds().contains(tempPakID)) {
                    clearAndAddAlreadyHavePacket(tempPakID);
                    uiCommunicator.process_NO_ANSWER(messageDTO);
                }
                break;
            case VoiceConstants.CALL_STATE.VOICE_REGISTER_PUSH:
                messageDTO = VoicePacketProcessor.getPushMessageConfirmationPacket(receivedBuffer);
                tempPakID = Helpers.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.VOICE_REGISTER_PUSH_CONFIRMATION);
                if (!getAllreadyHavePackedIds().contains(tempPakID)) {
                    clearAndAddAlreadyHavePacket(tempPakID);
                    uiCommunicator.process_VOICE_REGISTER_PUSH(messageDTO);
                }
                break;
            case VoiceConstants.CALL_STATE.VOICE_REGISTER_PUSH_CONFIRMATION:
                messageDTO = VoicePacketProcessor.getPushMessageConfirmationPacket(receivedBuffer);
                tempPakID = Helpers.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.VOICE_REGISTER_PUSH_CONFIRMATION);
                VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID);
                break;
            case VoiceConstants.CALL_STATE.VOICE_CALL_HOLD:
                System.out.println("hold found");
                messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
                tempPakID = Helpers.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.VOICE_CALL_HOLD);
                confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.VOICE_CALL_HOLD_CONFIRMATION, messageDTO.getPacketID(), ConfigFile.USER_ID, messageDTO.getFriendIdentity());
                SignalGenerator.sendSignalingPacket(confirmationByte);
                uiCommunicator.process_VOICE_CALL_HOLD(messageDTO);
                break;
            case VoiceConstants.CALL_STATE.VOICE_CALL_HOLD_CONFIRMATION:
                messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
                if (ConfigFile.CALL_ID != null && ConfigFile.CALL_ID.equals(messageDTO.getPacketID())) {
                    messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
                    tempPakID = Helpers.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.VOICE_CALL_HOLD);
                    VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID);
                }
                break;
            case VoiceConstants.CALL_STATE.VOICE_CALL_UNHOLD:
                System.out.println("unhold found ");
                messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
                confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.VOICE_UNHOLD_CONFIRMATION, messageDTO.getPacketID(), ConfigFile.USER_ID, messageDTO.getFriendIdentity());
                SignalGenerator.sendSignalingPacket(confirmationByte);
                uiCommunicator.process_VOICE_CALL_UNHOLD(messageDTO);
                break;
            case VoiceConstants.CALL_STATE.VOICE_UNHOLD_CONFIRMATION:
                messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
                if (ConfigFile.CALL_ID != null && ConfigFile.CALL_ID.equals(messageDTO.getPacketID())) {
                    System.out.println("unhold found confirmation");
                    messageDTO = VoicePacketProcessor.getSignalingPacket(receivedBuffer);
                    tempPakID = Helpers.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.VOICE_CALL_UNHOLD);
                    VoiceConstants.CALL_PACKET_FOR_CONFIRMATION.add(tempPakID);
                    uiCommunicator.process_VOICE_UNHOLD_CONFIRMATION(messageDTO);
                }
                break;
            case VoiceConstants.CALL_STATE.VOICE_BUSY_MESSAGE:
                messageDTO = VoicePacketProcessor.getSignalingPacketWithBusymessage(receivedBuffer);
                confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.VOICE_BUSY_MESSAGE_CONFIRMATION, messageDTO.getPacketID(), ConfigFile.USER_ID, messageDTO.getFriendIdentity());
                tempPakID = Helpers.getPack(messageDTO.getPacketID(), VoiceConstants.CALL_STATE.VOICE_BUSY_MESSAGE_CONFIRMATION);
                SignalGenerator.sendSignalingPacket(confirmationByte);
                if (!getAllreadyHavePackedIds().contains(tempPakID)) {
                    clearAndAddAlreadyHavePacket(tempPakID);
                    uiCommunicator.process_VOICE_BUSY_MESSAGE(messageDTO);
                }

                break;
        }
        if (messageDTO != null && messageDTO.getPacketType() > 0 && messageDTO.getPacketID() != null) {
            Helpers.systemPrint("from client==" + messageDTO.getPacketID() + "==>" + messageDTO.getPacketType());
        }
    }
    private Set<String> allreadyHavePackedIds = new ConcurrentSkipListSet<String>();

    public Set<String> getAllreadyHavePackedIds() {
        return allreadyHavePackedIds;
    }

    public void setAllreadyHavePackedIds(Set<String> allreadyHavePackedIds) {
        this.allreadyHavePackedIds = allreadyHavePackedIds;
    }

    private void clearAndAddAlreadyHavePacket(String pakID) {
        if (getAllreadyHavePackedIds().size() > 1000) {
            getAllreadyHavePackedIds().clear();
        }
        getAllreadyHavePackedIds().add(pakID);
    }

}
