/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.call;

import com.ipvision.model.call.CallerDto;
import com.desktopCall.settings.ConfigFile;
import com.desktopCall.settings.SignalGenerator;
import com.ipvision.model.stores.MyfnfHashMaps;

/**
 *
 * @author Faiz
 */
public class SendIncallToOthers extends Thread {

    public SendIncallToOthers() {
        setName(this.getClass().getName());
    }

    @Override
    public void run() {
        if (!MyfnfHashMaps.getInstance().getCallInitiators().isEmpty()) {
            for (String key : MyfnfHashMaps.getInstance().getCallInitiators().keySet()) {
                CallerDto callingTimeDto = MyfnfHashMaps.getInstance().getCallInitiators().get(key);
//                if (VoiceConstants.CALL_ID != null && !VoiceConstants.CALL_ID.equals(key)) {
//                    CallUtils.sendCallIn(callingTimeDto.getFriendId(), callingTimeDto.getCallID(), callingTimeDto.getSwitchIp(), callingTimeDto.getSwitchPort());
//                    MyfnfHashMaps.getInstance().getCallInitiators().remove(key);
//                } else 
                if (ConfigFile.FRIEND_ID != null && !callingTimeDto.getFriendIdentity().equals(ConfigFile.FRIEND_ID)) {
                    SignalGenerator.sendCallIn(callingTimeDto.getFriendIdentity(), callingTimeDto.getCallID(), callingTimeDto.getSwitchIp(), callingTimeDto.getSwitchPort());
                    MyfnfHashMaps.getInstance().getCallInitiators().remove(key);
                }
//                else {
//                 //   System.out.println("not sent incall to==>" + callingTimeDto.getCallID());
//                }
            }
        }

    }
}
