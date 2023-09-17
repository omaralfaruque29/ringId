/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility.call;

import com.desktopCall.settings.ConfigFile;
import com.desktopCall.net.CallStates;
import com.desktopCall.net.VoicePacketProcessor;
import static com.desktopCall.settings.SignalGenerator.sendSignalingPacket;
import com.desktopCall.settings.VoiceConstants;
import com.ipvision.view.call.MainClass;
import com.ipvision.service.utility.HelperMethods;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.ipvision.service.call.CallRegisterRequest;
import com.ipvision.service.utility.call.CallUtils;
import com.ipvision.service.utility.call.TopLabelWrapper;

/**
 *
 * @author Faiz
 */
public class CallUIHelpers {

    private static final String rootImageFolder = "com/ipvision/images/call/";
    public static final String CALLING = rootImageFolder + "calling.gif";
    public static final String BUTTON_CALL_MOBILE = rootImageFolder + "friend_profile_call_mobile.png";
    public static final String BUTTON_CALL_MOBILE_H = rootImageFolder + "friend_profile_call_mobile_h.png";//
    public static final String SIP_CALL = rootImageFolder + "sip_call.png";
    public static final String SIP_CALL_H = rootImageFolder + "sip_call_h.png";
    public static final String BUTTON_CALL_HOLD = rootImageFolder + "hold.png";
    public static final String BUTTON_CALL_HOLD_H = rootImageFolder + "hold_h.png";
    public static final String BUTTON_CALL_UNHOLD = rootImageFolder + "unhold.png";
    public static final String BUTTON_CALL_UNHOLD_H = rootImageFolder + "unhold_h.png";
    public static final String BUTTON_UNMUTE_MINI = rootImageFolder + "unmute.png";
    public static final String BUTTON_UNMUTE_MINI_H = rootImageFolder + "unmute_h.png";
    public static final String VIDEO_CALL = rootImageFolder + "video_call.png";
    public static final String VIDEO_CALL_H = rootImageFolder + "video_call_h.png";
    public static final String BUTTON_MUTE = rootImageFolder + "mute.png";
    public static final String BUTTON_MUTE_H = rootImageFolder + "mute_h.png";
    public static final String BUTTON_ARTICULATE = rootImageFolder + "articulate.png";
    public static final String BUTTON_ARTICULATE_H = rootImageFolder + "articulate_h.png";
    public static final String CHAT_MINI = rootImageFolder + "chat_mini.png";
    public static final String CHAT_MINI_H = rootImageFolder + "chat_mini_h.png";

    // topu
    public static final String Message_Button = rootImageFolder + "messege.png";
    public static final String Message_Button_H = rootImageFolder + "messege_h.png";
    public static final String BUTTON_CALL_RECEIVE = rootImageFolder + "receive.png";
    public static final String BUTTON_CALL_RECEIVE_H = rootImageFolder + "receive_h.png";
    public static final String Animated_Label_Image = rootImageFolder + "incoming_call_animation.gif";
    public static final String CALL_IGNORE = rootImageFolder + "ignore.png";
    public static final String CALL_IGNORE_H = rootImageFolder + "ignore_h.png";

    public static final String OUT_GOING_ANIMATIONL = rootImageFolder + "outgoingCallAnim.gif";
    public static final String Mute_Button = rootImageFolder + "mute.png";
    public static final String Mute_Button_H = rootImageFolder + "mute_h.png";
    public static final String Volume_Button = rootImageFolder + "speker.png";
    public static final String Volume_Button_H = rootImageFolder + "speker_h.png";
    public static final String Button_Video = rootImageFolder + "video.png";
    public static final String Button_Video_H = rootImageFolder + "video_h.png";
    public static final String Call_End_Button = rootImageFolder + "end_call.png";
    public static final String Call_End_Button_H = rootImageFolder + "end_call_h.png";

    public static void callSuccessChecking(final int waiting_in_ringing_state) {

        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable periodicTask = new Runnable() {
            int calling = 0;

            @Override
            public void run() {
                if (MainClass.getMainClass() == null && ConfigFile.CALL_ID == null) {
                    executor.shutdown();
                }
                if (CallStates.getStatus() != CallStates.UA_OUTGOING_CALL) {
                    try {
                        executor.shutdown();
                    } catch (Exception e) {
                    }
                }
                if (CallRegisterRequest.inCall) {
                    CallUtils.failedCall("User busy!!", true);
                    executor.shutdown();
                }
                if (calling > waiting_in_ringing_state) {
                    TopLabelWrapper.noResponse(false);
                    if (MainClass.getMainClass() != null) {
                        HelperMethods.showWarningDialogMessage("Not Responding.");
                    }
                    TopLabelWrapper.resetCallingWindows(true);
                    executor.shutdown();
                }
                if (calling % 12 == 0) {
                    if (ConfigFile.CALL_ID != null) {
                        byte[] finalBytePacket = VoicePacketProcessor.makeSignalingPacket(VoiceConstants.CALL_STATE.CALLING, ConfigFile.CALL_ID, ConfigFile.USER_ID, ConfigFile.FRIEND_ID);
                        sendSignalingPacket(finalBytePacket);
                    }

                }
                calling++;
            }

        };
        executor.scheduleAtFixedRate(periodicTask,
                2, 1, TimeUnit.SECONDS);

    }
}
