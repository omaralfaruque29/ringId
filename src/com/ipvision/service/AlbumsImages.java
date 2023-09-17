/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.constants.AppConstants;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class AlbumsImages extends Thread {
//

    String albumId;
    Integer startLimit;

    public AlbumsImages(String albumid, int start_limit) {
        this.albumId = albumid;
        this.startLimit = start_limit;
    }

    public AlbumsImages(int start_limit) {
        this.startLimit = start_limit;
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                /*
                 {"actn":190,"sId":"1396860067476nazmul","pckId":"1396860360916","albId":"123"}
                 */
                JsonFields singInpacked = new JsonFields();

                String pakId = SendToServer.getRanDomPacketID();
                singInpacked.setPacketId(pakId);
                singInpacked.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                singInpacked.setAction(AppConstants.TYPE_MY_ALBUM_IMAGES);
                if (albumId != null) {
                    singInpacked.setAlbId(albumId);
                }
                singInpacked.setStartLimit(startLimit);
                SendToServer.sendPacketAsString(singInpacked, ServerAndPortSettings.REQUEST_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(singInpacked, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        break;
                    }

                }
            } catch (Exception ex) {
            }
        }
    }
}
