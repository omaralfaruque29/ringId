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
import com.ipvision.view.myprofile.MyBookHome;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class MyNewsFeeds extends Thread {
//

    private long time;
    private Short scl;

    public MyNewsFeeds(long time, Short scl) {
        this.time = time;
        this.scl = scl;
        setName(this.getClass().getSimpleName());
    }

    public MyNewsFeeds(long time) {
        this.time = time;
        setName(this.getClass().getSimpleName());
    }

    private MyBookHome getMyBookHome() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null && GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null) {
            return GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyFeeds();
        } else {
            return null;
        }
        //return GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyFeeds();
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                JsonFields singInpacked = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                singInpacked.setPacketId(pakId);
                singInpacked.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                singInpacked.setAction(AppConstants.TYPE_MY_BOOK);
                singInpacked.setLimit(10);
                if (scl != null && scl > 0) {
                    singInpacked.setScl(scl);
                }
                if (time > 0) {
                    singInpacked.setTm(time);
                }
                SendToServer.sendPacketAsString(singInpacked, ServerAndPortSettings.REQUEST_PORT);
                Thread.sleep(1000);

                for (int i = 0; i < 5; i++) {
                    Thread.sleep(500 * i);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(singInpacked, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);

                        if (getMyBookHome() != null) {
                            if (getMyBookHome().getContent().getComponentCount() <= 1
                                    || (getMyBookHome().getContent().getComponentCount() == 2
                                    && getMyBookHome().getContent().getComponent(1).equals(getMyBookHome().bottomLoadingLabel))) {
                                for (int j = 500; j < 10000 && NewsFeedMaps.getInstance().getMyNewsFeedId().size() < 10; j += 500) {
                                    System.err.println("MY FEED >> WAITING TIME :: " + j + ", RECEIVED FEEDS :: " + NewsFeedMaps.getInstance().getMyNewsFeedId().size());
                                    Thread.sleep(500);
                                }
                                getMyBookHome().initLoad();
                            } else if (scl != null && scl == 1) {
                                Thread.sleep(1000);
                                getMyBookHome().topLoad();
                            } else if (scl != null && scl == 2) {
                                Thread.sleep(1000);
                                getMyBookHome().bottomLoad();
                            }
                        }

                        return;
                    }
                }
            } catch (Exception ex) {
            }
        }

        if (getMyBookHome() != null) {
            getMyBookHome().removeAllLoading();
        }

    }
}
