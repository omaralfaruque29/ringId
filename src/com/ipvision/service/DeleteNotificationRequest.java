/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.model.dao.DeleteFromNotificationHistoryTable;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.view.SingleNotification;
import com.ipvision.view.feed.AllNotificationPanel;
import com.ipvision.model.FeedBackFields;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.GuiRingID;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author Wasif Islam
 */
public class DeleteNotificationRequest extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteNotificationRequest.class);
    private String id = "";
    public SingleNotification singlePopupNotif;
    private int compToDlt;
    //private JPanel NotificationWrapperPanel;
    private List<String> notificationsToDelete = new ArrayList<>();
    private List<SingleNotification> SingleNotificationPanels = new ArrayList<>();

    public DeleteNotificationRequest(String id, SingleNotification singleNotif) {
        this.id = id;
        this.singlePopupNotif = singleNotif;
        setName(this.getClass().getSimpleName());
    }

    public DeleteNotificationRequest(String id) {
        this.id = id;
        setName(this.getClass().getSimpleName());

    }

    public DeleteNotificationRequest(List<String> notificationsToDelete, List<SingleNotification> SingleNotificationPanels) {
        this.notificationsToDelete = notificationsToDelete;
        this.SingleNotificationPanels = SingleNotificationPanels;
        //this.NotificationWrapperPanel = NotificationWrapperPanel;
        if (notificationsToDelete != null && notificationsToDelete.size() > 0) {
            for (int i = 0; i < notificationsToDelete.size(); i++) {
                this.id = id + notificationsToDelete.get(i);
                if (i < notificationsToDelete.size() - 1) {
                    this.id = id + ",";
                }
            }
        }

    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {

            try {
                /*
                 {"pckId":"mE5VZrZkring8801670854678","ids":"-1,1163,1164,1165,1166,1168,1169,1167","actn":112,"sId":"1403517443218ring8801670854678"}
                 */
                JsonFields pakToSend = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();//AppConstants.TYPE_DELETE_MY_NOTIFICATIONS);
                pakToSend.setPacketId(pakId);
                pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                pakToSend.setAction(AppConstants.TYPE_DELETE_MY_NOTIFICATIONS);
                pakToSend.setIds(id);
                SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.REQUEST_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (fields.getSuccess()) {
//                            if (singlePopupNotif != null) {
//                                singlePopupNotif.setBorder(null);
//                                singlePopupNotif.removeAll();
//                            } else {
//                                compNumber = AllNotificationPanel.getInstance().content.getComponentCount();
//                                for (int j = 0; j < compNumber; j++) {
//                                    if (AllNotificationPanel.getInstance().content.getComponent(j).getName() != null && AllNotificationPanel.getInstance().content.getComponent(j).getName().equals(id)) {
//                                        compToDlt = j;
//                                        break;
//                                    }
//                                }
//                                AllNotificationPanel.getInstance().content.remove(compToDlt);
//                                AllNotificationPanel.getInstance().content.revalidate();
//                            }
                            if (notificationsToDelete != null && notificationsToDelete.size() > 0) {
                                for (String id : notificationsToDelete) {
                                    for (int j = 0; j < GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.getComponentCount(); j++) {
                                        SingleNotification sp = (SingleNotification) GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.getComponent(j);
                                        if (id.equalsIgnoreCase(sp.notificationObj.getId())) {
                                            GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.remove(j);
                                            GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.revalidate();
                                            new DeleteFromNotificationHistoryTable(id).start();
                                            MyfnfHashMaps.getInstance().getNotificationsMap().remove(id);
                                            SingleNotificationPanels.remove(j);
                                        }
                                    }
                                }
                                AllNotificationPanel.getInstance().addAllNotifications();
                                notificationsToDelete.clear();
                            } else {
                                for (int j = 0; j < AllNotificationPanel.getInstance().content.getComponentCount(); j++) {
                                    if (AllNotificationPanel.getInstance().content.getComponent(j).getName() != null && AllNotificationPanel.getInstance().content.getComponent(j).getName().equals(id)) {
                                        compToDlt = j;
                                        break;
                                    }
                                }

                                AllNotificationPanel.getInstance().content.remove(compToDlt);
                                AllNotificationPanel.getInstance().content.revalidate();

                                for (int k = 0; k < GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.getComponentCount(); k++) {
                                    SingleNotification sp = (SingleNotification) GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.getComponent(k);
                                    if (id.equalsIgnoreCase(sp.notificationObj.getId())) {
                                        GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.remove(k);
                                        GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.revalidate();
                                        GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().SingleNotificationPanels.remove(k);
                                    }
                                }
                                new DeleteFromNotificationHistoryTable(id).start();
                                MyfnfHashMaps.getInstance().getNotificationsMap().remove(id);

                            }

                            /*for (int j = 0; j < GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.getComponentCount(); j++) {
                             SingleNotification sp = (SingleNotification) GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.getComponent(j);
                             if (notificationsToDelete.contains(sp.notificationObj.getId())) {
                             GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.remove(j);
                             GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.revalidate();
                             }
                             }*/
                            //GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.removeAll();
                            // GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel.revalidate();
//                            new DeleteFromNotificationHistoryTable(id).start();
//                             MyfnfHashMaps.getInstance().getNotificationsMap().remove(id);
                        } else {
                            if (GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer() != null && id != null) {
                                HelperMethods.showPlaneDialogMessage("Delete notification failed!");
                            }

//                            if (singlePopupNotif != null || id != null) {
//                                HelperMethods.showPlaneDialogMessage("Delete notification failed!");
//                                singlePopupNotif.wrapperDeletePanel.setVisible(false);
//                                singlePopupNotif.rightContentPanel.setVisible(true);
//                            }
                        }
                        if (GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().NotificationWrapperPanel != null && GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().SingleNotificationPanels < 1) {
                            GuiRingID.getInstance().getMainLeftContainer().getNotificationHistoryContainer().buttonPanel.setVisible(false);

                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        break;
                    }
                }

            } catch (Exception ex) {
                log.error("Error in DeletingNotificationRequest ==>" + ex.getMessage());
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}
