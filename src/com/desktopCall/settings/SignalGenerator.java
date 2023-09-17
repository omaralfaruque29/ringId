/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.settings;

import com.desktopCall.net.CallStates;
import com.desktopCall.dtos.ResendPacketDTO;
import static com.desktopCall.settings.Helpers.getPack;
import com.desktopCall.net.VoicePacketProcessor;
import com.desktopCall.net.VoiceSignalProcessor;
import com.desktopCall.threads.CallPacketReSender;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author Faiz
 */
public class SignalGenerator {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SignalGenerator.class);

    public static void sendRTP(byte[] sendingBytes) {
        try {
            ConfigFile.numberOfRTPsending++;
            DatagramPacket sendPacket = new DatagramPacket(sendingBytes, sendingBytes.length, ConfigFile.IPAddress, ConfigFile.VOICE_BINDING_PORT);
            sendMessage(sendPacket);

        } catch (Exception e) {
           log.error("Error in SignalGenerator class to sendPacket ==>" + e.getMessage());
        }

    }

    public static void sendRegButtonAction(String friendIdentity, String callID, String voiceServerIP, int port) {
        try {
            if (friendIdentity != null && ConfigFile.USER_ID != null) {
                byte[] finalBytePacket = VoicePacketProcessor.makeRegisterPacket(VoiceConstants.VOICE_REGISTER, ConfigFile.USER_ID, friendIdentity, callID);
                DatagramPacket finalPacket = new DatagramPacket(finalBytePacket, finalBytePacket.length, InetAddress.getByName(voiceServerIP), port);
                sendMessage(finalPacket);
                ResendPacketDTO resendPacketDTO = new ResendPacketDTO();
                resendPacketDTO.setUdpSocket(VoiceSignalProcessor.getInstance().voiceSocket.getUdpSocket());
                resendPacketDTO.setPacket(finalPacket);
                resendPacketDTO.setNumberOfResend(1);
                String pakId = getPack(callID, VoiceConstants.VOICE_REGISTER_CONFIRMATION);
                //    System.out.println("pakId==>" + pakId);
                (new CallPacketReSender(resendPacketDTO, pakId)).start();
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }

    }

    public static void sendCallIn(String friendIdentity, String pakid, String ip, int port) {
        try {
            byte[] finalBytePacket = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.IN_CALL, pakid, ConfigFile.USER_ID, friendIdentity);
            DatagramPacket finalPacket = new DatagramPacket(finalBytePacket, finalBytePacket.length, InetAddress.getByName(ip), port);
            sendMessage(finalPacket);
            ResendPacketDTO resendPacketDTO = new ResendPacketDTO();
            resendPacketDTO.setUdpSocket(VoiceSignalProcessor.getInstance().voiceSocket.getUdpSocket());
            resendPacketDTO.setPacket(finalPacket);
            resendPacketDTO.setNumberOfResend(1);
            (new CallPacketReSender(resendPacketDTO, getPack(pakid, VoiceConstants.CALL_STATE.IN_CALL_CONFIRMATION))).start();
        } catch (UnknownHostException ex) {
            //  log.error("sendCallIn==>" + ex.getMessage());
        }
    }

    public static void sendPacket(byte[] sendingBytePacket, String ip, int port) {

        try {
            DatagramPacket finalPacket = new DatagramPacket(sendingBytePacket, sendingBytePacket.length, InetAddress.getByName(ip), port);
            sendMessage(finalPacket);
        } catch (UnknownHostException e) {
            //  log.error("sendPacket==>" + e.getMessage());
        }
    }

    public static void callButtonAction(int packetType, String friendIdentity) {
        if (ConfigFile.CALL_ID != null) {
            byte[] finalBytePacket = VoicePacketProcessor.makeSignalingPacket(packetType, ConfigFile.CALL_ID, ConfigFile.USER_ID, friendIdentity);
            sendSignalingPacket(getPack(ConfigFile.CALL_ID, VoiceConstants.CALL_STATE.RINGING), finalBytePacket, ConfigFile.VOICE_SERVER_IP, ConfigFile.VOICE_BINDING_PORT);
        } else {
            //   log.error("callbuttonAction==>CALL_ID null");
        }
    }

    public static void sendSignalingPacket(String packetID, byte[] sendingBytePacket, String voiceServerIP, int port) {
        try {
            DatagramPacket finalPacket;
            finalPacket = new DatagramPacket(sendingBytePacket, sendingBytePacket.length, InetAddress.getByName(voiceServerIP), port);
            sendMessage(finalPacket);
            ResendPacketDTO resendPacketDTO = new ResendPacketDTO();
            resendPacketDTO.setUdpSocket(VoiceSignalProcessor.getInstance().voiceSocket.getUdpSocket());
            resendPacketDTO.setPacket(finalPacket);
            resendPacketDTO.setNumberOfResend(1);
            (new CallPacketReSender(resendPacketDTO, packetID)).start();
        } catch (Exception e) {
            //  log.error("sendSignalingPacketWithIpPort==>" + e.getMessage());
        }
    }

    public static void sendSignalingPacket(byte[] sendingBytePacket) {
        sendSignalingPacketWithIpPort(sendingBytePacket, ConfigFile.VOICE_SERVER_IP, ConfigFile.VOICE_BINDING_PORT);
    }

    public static void sendSignalingPacketWithIpPort(byte[] sendingBytePacket, String ip, int port) {
        try {
            DatagramPacket finalPacket2 = new DatagramPacket(sendingBytePacket, sendingBytePacket.length, InetAddress.getByName(ip), port);
            try {
                VoiceSignalProcessor.getInstance().send(finalPacket2);
            } catch (IOException ex) {
                //  Logger.getLogger(CallMsgParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception e) {
        }
    }

    public static void answerButtonAction(String friendid) {
        int packetType = VoiceConstants.CALL_STATE.ANSWER;
        String packetID = ConfigFile.CALL_ID;
        byte[] finalBytePacket = VoicePacketProcessor.makeSignalingPacket(packetType, packetID, ConfigFile.USER_ID, friendid);
        sendSignalingPacket(getPack(packetID, VoiceConstants.CALL_STATE.CONNECTED), finalBytePacket, ConfigFile.VOICE_SERVER_IP, ConfigFile.VOICE_BINDING_PORT);
    }

    public static void sendHoldSignal(boolean hold) {
        int packetType = VoiceConstants.CALL_STATE.VOICE_CALL_HOLD;
        if (!hold) {
            packetType = VoiceConstants.CALL_STATE.VOICE_CALL_UNHOLD;
        }
        byte[] finalBytePacket = VoicePacketProcessor.makeSignalingPacket(packetType, ConfigFile.CALL_ID, ConfigFile.USER_ID, ConfigFile.FRIEND_ID);
        sendSignalingPacket(getPack(ConfigFile.CALL_ID, packetType), finalBytePacket, ConfigFile.VOICE_SERVER_IP, ConfigFile.VOICE_BINDING_PORT);
    }

    public static void cancelButtonAction(boolean incomming) {
        int packetType;
        try {
            if (CallStates.getStatus() == CallStates.UA_ONCALL) {
                packetType = VoiceConstants.CALL_STATE.BYE;
            } else {
                if (incomming) {
                    packetType = VoiceConstants.CALL_STATE.BUSY;
                    if (CallStates.getStatus() != CallStates.UA_ONCALL && ConfigFile.BUSY_TEXT != null && ConfigFile.BUSY_TEXT.length() > 0) {
                        sendBusyMessage(ConfigFile.BUSY_TEXT);
                    }
                } else {
                    packetType = VoiceConstants.CALL_STATE.CANCELED;
                }
            }
            cancelUIActions(packetType);
        } catch (Exception e) {
           // e.printStackTrace();
            log.error("Error in SignalGenerator class sending Packet ==>" + e.getMessage());
        }
    }

    public static void sendBusyMessage(String msg) {
        String packetID = ConfigFile.CALL_ID;
        byte[] finalBytePacket = VoicePacketProcessor.makeBusyMessage(packetID, ConfigFile.USER_ID, ConfigFile.FRIEND_ID, msg);
        sendSignalingPacket(getPack(packetID, VoiceConstants.CALL_STATE.VOICE_BUSY_MESSAGE_CONFIRMATION), finalBytePacket, ConfigFile.VOICE_SERVER_IP, ConfigFile.VOICE_BINDING_PORT);
    }

    private static void cancelUIActions(int type) {
        if (ConfigFile.VOICE_BINDING_PORT > 0) {
            String tempType = getPack(ConfigFile.CALL_ID, VoiceConstants.CALL_STATE.DISCONNECTED);
            byte[] finalBytePacket = VoicePacketProcessor.makeSignalingPacket(type, ConfigFile.CALL_ID, ConfigFile.USER_ID, ConfigFile.FRIEND_ID);
            sendSignalingPacket(tempType, finalBytePacket, ConfigFile.VOICE_SERVER_IP, ConfigFile.VOICE_BINDING_PORT);
        }

    }

    public static void notResponding(boolean incomming) {
        int packetType;
        try {
            if (incomming) {
                packetType = VoiceConstants.CALL_STATE.NO_ANSWER;
            } else {
                packetType = VoiceConstants.CALL_STATE.CANCELED;
            }
            cancelUIActions(packetType);
        } catch (Exception e) {
        }
    }

//    public static void unRegisterPacked() {
//        byte[] finalBytePacket = VoicePacketProcessor.makeUnRegisterPacket(VoiceConstants.VOICE_UNREGISTERED, ConfigFile.USER_ID);
//        sendRegisterPacket(finalBytePacket);
//
//    }
    public static void sendMessage(DatagramPacket datagram) {
        try {

            VoiceSignalProcessor.getInstance().send(datagram);
        } catch (IOException ex) {
           // ex.printStackTrace();
             log.error("Error in SignalGenerator class to sendMessage==>" + ex.getMessage());
        }
    }

    public static void sendConnect(String userid) {
        try {
            byte[] confirmationByte = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.CONNECTED, ConfigFile.CALL_ID, ConfigFile.USER_ID, userid);
            sendSignalingPacket(confirmationByte);
        } catch (Exception e) {
        }

    }

}
