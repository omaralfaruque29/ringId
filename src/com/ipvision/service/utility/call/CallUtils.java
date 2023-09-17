/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.utility.call;

import com.ipvision.model.call.CallerDto;
import com.desktopCall.settings.ConfigFile;
import com.ipvision.service.utility.HelperMethods;
import java.net.InetAddress;
import com.ipvision.view.call.MainClass;
import com.ipvision.model.stores.MyfnfHashMaps;

/**
 *
 * @author Faiz Ahmed
 */
public class CallUtils {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CallUtils.class);

    public static void setCallContants(CallerDto callChatInitiatorDto) {
        ConfigFile.CALL_ID = callChatInitiatorDto.getCallID();
        ConfigFile.VOICE_SERVER_IP = callChatInitiatorDto.getSwitchIp();
        ConfigFile.VOICE_BINDING_PORT = callChatInitiatorDto.getVoiceBindingPort();
        if (callChatInitiatorDto.getFriendIdentity() != null) {
            ConfigFile.FRIEND_ID = callChatInitiatorDto.getFriendIdentity();
        }
        try {
            ConfigFile.IPAddress = InetAddress.getByName(ConfigFile.VOICE_SERVER_IP);
        } catch (Exception e) {
        }

    }

    public static void addCallinitiators(CallerDto callChatInitiatorDto) {
        try {
            if (MyfnfHashMaps.getInstance().getCallInitiators().isEmpty() || MyfnfHashMaps.getInstance().getCallInitiators().get(callChatInitiatorDto.getCallID()) == null) {
                MyfnfHashMaps.getInstance().getCallInitiators().put(callChatInitiatorDto.getCallID(), callChatInitiatorDto);
            }
        } catch (Exception e) {
        }

    }

    public static void removeFromCallInitiators(String userid) {
        try {
            if (userid != null && MyfnfHashMaps.getInstance().getCallInitiators() != null && !MyfnfHashMaps.getInstance().getCallInitiators().isEmpty() && MyfnfHashMaps.getInstance().getCallInitiators().get(userid) != null) {
                MyfnfHashMaps.getInstance().getCallInitiators().remove(userid);
            } else {
                // System.out.println("already removed callid==>" + userid);
            }
        } catch (Exception e) {
            log.error("Error while delete from chatCallInitiators");

        }

    }

    public static String generateSameCallerKey(String callID) {
        return callID + "_2";
    }

    public static void failedCall(String msg) {
        if (msg != null) {
            if (MainClass.getMainClass() != null) {
                HelperMethods.showWarningDialogMessage(msg);
                TopLabelWrapper.cancelButtonAction(false);

            }
        }

    }

    public static void failedCall(String msg, boolean noSignal) {
        if (msg != null) {
            if (MainClass.getMainClass() != null) {
                HelperMethods.showWarningDialogMessage(msg);
                TopLabelWrapper.cancelButtonAction(false, noSignal);

            }
        }

    }

    public static void notResponding(boolean incomming) {
        TopLabelWrapper.cancelButtonAction(incomming);

    }
}
