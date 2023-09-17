/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.view.feed.LikePopupSingleFeed;
import com.ipvision.model.FeedBackFields;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.JsonFields;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class CommentsLikes extends Thread {

    private Long statusid;
    private int type;
    private int comment_like_number = 0;

    public CommentsLikes(Long statusid, int type) {
        this.statusid = statusid;
        this.type = type;
        this.comment_like_number = 0;
        setName(this.getClass().getName());
    }

    public CommentsLikes(Long statusid, int type, int commentnumber) {
        this.statusid = statusid;
        this.type = type;
        this.comment_like_number = commentnumber;
        setName(this.getClass().getName());
    }

    public CommentsLikes(int commentnumber) {
        this.comment_like_number = commentnumber;
        setName(this.getClass().getName());
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                /*
                 {"stId":16,"actn":84,"sId":"1396514979687nazmul","pckId":"1396514996418"}
                 */
                JsonFields pakToSend = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                pakToSend.setPacketId(pakId);
                pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                pakToSend.setAction(this.type);
                if (type == AppConstants.TYPE_LIKES_FOR_IMAGE || type == AppConstants.TYPE_COMMENTS_FOR_IMAGE) {
                    pakToSend.setImageId(this.statusid);
                } else {
                    pakToSend.setNfId(this.statusid);

                }
                pakToSend.setStartLimit(this.comment_like_number);

                SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.REQUEST_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        FeedBackFields l = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (l.getSuccess() != null && l.getSuccess() == false) {
                            if (LikePopupSingleFeed.getInstance() != null && LikePopupSingleFeed.getInstance().isVisible()) {
                                LikePopupSingleFeed.getInstance().removeLoading();
                            }
                        } else {

                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);

                        break;
                    }

                }
            } catch (Exception ex) {
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}
