/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.myprofile.AlbumsHome;
import com.ipvision.view.feed.ChooseFromAlbum;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class AlbumRequest extends Thread {

    public static final int TRIGGER_BY_ALBUM_HOME = 0;
    public static final int TRIGGER_BY_CHOOSE_IMAGE_POST = 1;
    private int triggerBy;

    public AlbumRequest(int triggerBy) {
        this.triggerBy = triggerBy;
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                /*
                 {"actn":189,"sId":"1396860067476nazmul","pckId":"1396860131658"}
                 */
                JsonFields singInpacked = new JsonFields();

                String pakId = SendToServer.getRanDomPacketID();
                singInpacked.setPacketId(pakId);
                singInpacked.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                singInpacked.setAction(AppConstants.TYPE_MY_ALBUMS);

                SendToServer.sendPacketAsString(singInpacked, ServerAndPortSettings.REQUEST_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(singInpacked, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        Thread.sleep(1000);
                        break;
                    }
                }

                if (triggerBy == TRIGGER_BY_ALBUM_HOME && getAlbumHome() != null && getAlbumHome().getAlbums() != null) {
                    getAlbumHome().getAlbums().addMyAlbumsFolders();
                } else if (triggerBy == TRIGGER_BY_CHOOSE_IMAGE_POST && ChooseFromAlbum.getInstance() != null) {
                    ChooseFromAlbum.getInstance().addMyAlbumsFolders();
                }
            } catch (Exception ex) {
            }
        }
    }

    private AlbumsHome getAlbumHome() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null && GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null) {
            return GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome();
        } else {
            return null;
        }
        //return GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null ? GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome() : null;
    }
}
