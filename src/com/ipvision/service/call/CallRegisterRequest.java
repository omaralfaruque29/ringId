/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.call;

import com.ipvision.service.utility.call.CallUtils;
import com.ipvision.model.call.CallerDto;
import com.desktopCall.settings.ConfigFile;
import com.desktopCall.settings.SignalGenerator;
import com.desktopCall.settings.VoiceConstants;
import com.desktopCall.net.CallStates;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import javax.swing.JLabel;
import com.ipvision.view.utility.call.CallUIHelpers;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;

/**
 *
 * @author Faiz Ahmed
 */
public class CallRegisterRequest extends Thread {
//

    private final String friend_id;
    // private final String phone_no;
    private final JLabel durationLabel;
    private FeedBackFields responsePaked;
    public static boolean inCall = false;
    int loop_rang = 60;
    String callid;
    CallerDto callChatInitiatorDto;
    // String connecting = "Connecting";
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CallRegisterRequest.class);
    String msg = "Sorry!! Can not process call request";

    public CallRegisterRequest(String friend_id, JLabel statusLabel) {
        this.friend_id = friend_id;
        this.durationLabel = statusLabel;
        setName(this.getClass().getName());
    }

    @Override
    public void run() {
        boolean successCall = false;
        ConfigFile.VOICE_BINDING_PORT = 0;
        CallRegisterRequest.inCall = false;
        try {

            callid = SendToServer.getRanDomPacketID();
            CallerDto sendToserver = new CallerDto();

            //  CallLogHelper.callStartTimeToCallLog("CALL_ID_CLIENT_" + System.currentTimeMillis(), friend_id, friend_id, true);
            sendToserver.setAction(AppConstants.TYPE_SEND_REGISTER);
            sendToserver.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
            sendToserver.setFriendIdentity(friend_id);
            ConfigFile.FRIEND_ID = friend_id;
            ConfigFile.USER_ID = MyFnFSettings.LOGIN_USER_ID;
            sendToserver.setPacketId(callid);
            sendToserver.setCallID(callid);
            SendToServer.sendPacketAsString(sendToserver, ServerAndPortSettings.VOICE_PORT);
            Thread.sleep(20);
            ConfigFile.CALL_ID = callid;
            for (int i = 1; i <= loop_rang; i++) {

                Thread.sleep(500);
                if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(sendToserver.getPacketId()) == null) {
                    if (i % 10 == 0) {
                        SendToServer.sendPacketAsString(sendToserver, ServerAndPortSettings.VOICE_PORT);
                    }
                } else if (CallRegisterRequest.inCall) {
                    durationLabel.setIcon(null);
                    durationLabel.setText("User busy");
                    break;
                } else {
                    responsePaked = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(sendToserver.getPacketId());
                    System.out.println("responsePaked 174==>" + responsePaked.getMessage());
                    MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(sendToserver.getPacketId());
                    break;
                }
            }
            if (CallRegisterRequest.inCall) {
                msg = "User busy!!";
                CallUtils.failedCall(msg, true);
            } else if (responsePaked != null && responsePaked.getSuccess()) {
                callChatInitiatorDto = HelperMethods.getCallerDto(responsePaked.getMessage());
                CallUtils.addCallinitiators(callChatInitiatorDto);
                SignalGenerator.sendRegButtonAction(friend_id, callChatInitiatorDto.getCallID(), callChatInitiatorDto.getSwitchIp(), callChatInitiatorDto.getSwitchPort());
                ConfigFile.VOICE_FRIEND_DEVICE = callChatInitiatorDto.getDevice();
                try {
                    Thread.sleep(500);
                    for (int reg = 1; reg <= loop_rang; reg++) {
                        if (ConfigFile.CALL_ID == null) {
                            System.out.println("no ConfigFile.CALL_ID");
                            //  return;
                            break;
                        }
                        if (MyfnfHashMaps.getInstance().getCallInitiators().get(callid) == null) {
                            break;
                        }
                        if (MyfnfHashMaps.getInstance().getCallInitiators().get(callid).getVoiceBindingPort() > 0) {
                            break;
                        } else {
                            Thread.sleep(500);
                        }
                    }
                } catch (Exception e) {

                }
                CallUtils.setCallContants(callChatInitiatorDto);
                successCall = ConfigFile.VOICE_BINDING_PORT > 0;
            } else {
                successCall = false;
                CallUtils.failedCall(responsePaked.getMessage());

            }
            if (successCall) {
                processingOutGoingCall(responsePaked.getFriendIdentity());
            }

//            else {
//                CallUtils.failedCall("Call failed");
//            }
        } catch (Exception ex) {
            CallUtils.failedCall(null);
        }
    }

    private void processingOutGoingCall(String callUrl) {
        if (CallStates.getStatus() == CallStates.UA_IDLE) {
            if (callUrl != null && callUrl.length() > 0) {

                CallStates.changeStatus(CallStates.UA_OUTGOING_CALL);
                if (ConfigFile.VOICE_FRIEND_DEVICE == VoiceConstants.PLATFORM_ANDEOID || ConfigFile.VOICE_FRIEND_DEVICE == VoiceConstants.PLATFORM_ANDEOID || ConfigFile.VOICE_FRIEND_DEVICE == VoiceConstants.PLATFORM_ANDEOID) {
                    new CallIngPacketResender(friend_id, this.callid, callChatInitiatorDto.getSwitchIp(), callChatInitiatorDto.getSwitchPort()).start();
                } else {
                    SignalGenerator.callButtonAction(VoiceConstants.CALL_STATE.CALLING, friend_id);
                    CallUIHelpers.callSuccessChecking(35);
                }
            } else {
                HelperMethods.showPlaneDialogMessage("Invalid call request");
                CallStates.changeStatus(CallStates.UA_IDLE);

            }
        }
    }
}
