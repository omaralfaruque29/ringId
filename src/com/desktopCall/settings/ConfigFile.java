/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.settings;

import com.desktopCall.dtos.CallSettingsDTo;
import java.net.InetAddress;

/**
 *
 * @author Faiz
 */
public class ConfigFile {

    public static String VOICE_SERVER_IP = "38.127.68.57";
    public static int VOICE_REGISTER_PORT = 1250;
    public static String USER_ID;
    public static String FRIEND_ID;
    public static int VOICE_BINDING_PORT;
    public static int VOICE_FRIEND_DEVICE = VoiceConstants.PLATFORM_DESKTOP;
    public static String CALL_ID;
    public static String DEVICE_TOKEN;
    public static boolean systemPrint = true;
    public static boolean stopKeepAlive = true;
    public static InetAddress IPAddress;
    public static boolean noUI = true;
    public static int numberOfRTPsending = 0;
    public static int numberOfRTPreceiving = 0;
    public static String BUSY_TEXT = null;

    public static void callID(String callid) {
        CALL_ID = callid;
    }

    public static void resetAllParameters() {
        VOICE_SERVER_IP = "";
        VOICE_REGISTER_PORT = 0;
        USER_ID = null;
        FRIEND_ID = null;
        VOICE_BINDING_PORT = 0;
        VOICE_FRIEND_DEVICE = VoiceConstants.PLATFORM_DESKTOP;
        CALL_ID = null;
        systemPrint = true;
        DEVICE_TOKEN = null;
        IPAddress = null;
        ConfigFile.stopKeepAlive = true;
        numberOfRTPsending = 0;
        numberOfRTPreceiving = 0;
    }

    public static void set(CallSettingsDTo callSettingsDTo) {
        USER_ID = callSettingsDTo.getUserid();
        FRIEND_ID = callSettingsDTo.getFndId();
        VOICE_FRIEND_DEVICE = callSettingsDTo.getDvc();
        if (callSettingsDTo.getDt() != null) {
            DEVICE_TOKEN = callSettingsDTo.getDt();
        }
    }

//    public static void setIpPortCallId(String callId, String ip, int port) {
//        VOICE_SERVER_IP = ip;
//        VOICE_REGISTER_PORT = port;
//        CALL_ID = callId;
//        try {
//            IPAddress = InetAddress.getByName(ConfigFile.VOICE_SERVER_IP);
//        } catch (Exception e) {
//        }
//
//    }
//    public void setVoiceBindingPort(int port) {
//        VOICE_BINDING_PORT = port;
//    }
}
