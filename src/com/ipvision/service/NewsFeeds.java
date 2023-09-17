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
import com.ipvision.view.feed.AllFeedsView;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class NewsFeeds extends Thread {
//

    private long time;
    private Short scl;
    private int limit;

    public NewsFeeds(long time, int limit) {
        this.time = time;
        this.limit = limit;
        setName(this.getClass().getSimpleName());
    }

    public NewsFeeds(long time, Short scl, int limit) {
        this.time = time;
        this.scl = scl;
        this.limit = limit;
        setName(this.getClass().getSimpleName());

    }

    private AllFeedsView getAllFeedView() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
            return GuiRingID.getInstance().getMainRight().getAllFeedsView();
        } else {
            return null;
        }
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                JsonFields singInpacked = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                singInpacked.setPacketId(pakId);
                singInpacked.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                singInpacked.setAction(AppConstants.TYPE_NEWS_FEED);
                singInpacked.setLimit(limit);
                if (scl != null && scl > 0) {
                    singInpacked.setScl(scl);
                }
                if (time > 0) {
                    singInpacked.setTm(time);
                }
                SendToServer.sendPacketAsString(singInpacked, ServerAndPortSettings.REQUEST_PORT);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500 * i);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(singInpacked, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        if (getAllFeedView() != null) {
                            if (getAllFeedView().getContent().getComponentCount() <= 1
                                    || (getAllFeedView().getContent().getComponentCount() == 2
                                    && getAllFeedView().getContent().getComponent(1).equals(getAllFeedView().bottomLoadingLabel))) {
                                for (int j = 500; j < 15000 && NewsFeedMaps.getInstance().getAllNewfeedId().size() < 20; j += 500) {
                                    Thread.sleep(500);
                                }
                                getAllFeedView().initLoad();
                            } else if (scl != null && scl == 1) {
                                Thread.sleep(1000);
                                getAllFeedView().topLoad();
                            } else if (scl != null && scl == 2) {
                                Thread.sleep(1000);
                                getAllFeedView().bottomLoad();
                            }
                        }

                        return;
                    }

                }
            } catch (Exception ex) {
            }
        }

        if (getAllFeedView() != null) {
            getAllFeedView().removeAllLoading();
        }
    }
}
