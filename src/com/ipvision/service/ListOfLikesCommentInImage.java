/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.feed.LikesInCommentsJdialog;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.image.LikesInImageCommentsPopup;

/**
 *
 * @author Faiz Ahmed
 */
public class ListOfLikesCommentInImage extends Thread {
//

    private Long statusid;
    private String commentId;
    private int type;
    private int startFrom;

    public ListOfLikesCommentInImage(Long statusid, String commetnId, int type, int startFrom) {
        this.statusid = statusid;
        this.type = type;
        this.commentId = commetnId;
        this.startFrom = startFrom;
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                /*
                 >{"actn":116,"pckId":"1404103172595ring8801617634317","sId":"1404103172434ring8801617634317","nfId":810,"cmnId":790,"st":0}
                 */
                /* {"actn":196,"pckId":"1409806377720ring8801717634317","sId":"1409806377567ring8801717634317","cmnId":2,"imgId":295}*/
                JsonFields pakToSend = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                pakToSend.setPacketId(pakId);
                pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                pakToSend.setAction(this.type);
                if (this.type == AppConstants.TYPE_IMAGE_COMMENT_LIKES) {
                    pakToSend.setImageId(this.statusid);
                } else {
                    pakToSend.setNfId(this.statusid);
                }
                pakToSend.setCmnId(this.commentId);
                pakToSend.setStartLimit(startFrom);

                SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.REQUEST_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        FeedBackFields l = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (l.getSuccess() != null && l.getSuccess() == false) {
                            if (LikesInCommentsJdialog.getInstance() != null && LikesInCommentsJdialog.getInstance().isVisible()) {
                                LikesInCommentsJdialog.getInstance().removeLoading();
                            }
                            if (LikesInImageCommentsPopup.getInstance() != null && LikesInImageCommentsPopup.getInstance().isVisible()) {
                                LikesInImageCommentsPopup.getInstance().removeLoading();
                            }
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        break;
                    }
                }
            } catch (Exception ex) {
            }
        }
    }
}
