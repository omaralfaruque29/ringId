/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.settings;

//import com.ringid.voiceclient.ResendPacketDTO;
import com.desktopCall.dtos.ResendPacketDTO;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 *
 * @author Faiz
 */
public class VoiceConstants {

    public static final int REG_PACKET_SIZE = 512;
    public static final int VOICE_MEDIA = 0;
    public static final int VOICE_REGISTER = 1;
    public static final int VOICE_UNREGISTERED = 2;
    public static final int VOICE_REGISTER_CONFIRMATION = 3;
    public static Map<String, ResendPacketDTO> MESSAGE_RESEND_MAP = new ConcurrentHashMap<String, ResendPacketDTO>();
    public static Set<String> CALL_PACKET_FOR_CONFIRMATION = new ConcurrentSkipListSet<String>();
    public static final int NUMBER_OF_RESEND = 5;
    public static final int REGISTER_RESEND_TIME = 30000;
    //public static String CALL_PACKET_ID = null;//"38.108.92.154";//
    public static final int NUMBER_OF_MAX_GARBAGE = 5;
    public static final int PLATFORM_DESKTOP = 1;
    public static final int PLATFORM_ANDEOID = 2;
    public static final int PLATFORM_IPHONE = 3;
    public static final int PLATFORM_WINDOWS = 4;
    public static final int PLATFORM_WEB = 5;

    public class CALL_STATE {

        public final static byte KEEPALIVE = 4;
        public final static byte CALLING = 5;
        public final static byte RINGING = 6;
        public final static byte IN_CALL = 7;
        public final static byte ANSWER = 8;
        public final static byte BUSY = 9;
        public final static byte CANCELED = 10;
        public final static byte CONNECTED = 11;
        public final static byte DISCONNECTED = 12;
        public final static byte BYE = 13;
        public final static byte IDEL = 14;
        public final static byte NO_ANSWER = 15;
        public final static byte USER_AVAILABLE = 16;
        public final static byte USER_NOT_AVAILABLE = 17;
        public final static byte IN_CALL_CONFIRMATION = 18;
        public final static byte VIDEO_MEDIA = 19;
        public static final int VOICE_REGISTER_PUSH = 20;
        public static final int VOICE_REGISTER_PUSH_CONFIRMATION = 21;
        public static final int VOICE_CALL_HOLD = 22;
        public static final int VOICE_CALL_HOLD_CONFIRMATION = 23;
        public static final int VOICE_CALL_UNHOLD = 24;
        public static final int VOICE_UNHOLD_CONFIRMATION = 25;
        public static final int VOICE_BUSY_MESSAGE = 26;
        public static final int VOICE_BUSY_MESSAGE_CONFIRMATION = 27;
    };

    /**/
//    public static String callingToFriendId = null;
//    public static String CALL_ID = null;
//    public static String SAME_FRIEND_CALL_ID = null;
    // public static boolean callFailed = false;
}
