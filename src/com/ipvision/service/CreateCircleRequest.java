/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import java.awt.Color;
import javax.swing.JLabel;
import com.ipvision.model.constants.NotificationMessages;
import javax.swing.JButton;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.CreateCircle;
import com.ipvision.model.NewCircleFields;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.MainRightDetailsView;
import com.ipvision.model.FeedBackFields;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class CreateCircleRequest extends Thread {

    private JLabel loadingLable;
    private JButton sendbutton;
    private String circleName;

    public CreateCircleRequest(JLabel loadingLable, JButton sendbutton, String circleName) {
        this.loadingLable = loadingLable;
        this.sendbutton = sendbutton;
        this.circleName = circleName;
         setName(this.getClass().getName());
    }

    private MainRightDetailsView getMainRightDetailsView() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
            return GuiRingID.getInstance().getMainRight();
        } else {
            return null;
        }
//        return GuiRingID.getInstance().getMainRight() != null ? GuiRingID.getInstance().getMainRight() : null;
    }

    private void setMessage(int a) {
        String string;
        switch (a) {
            case 1:
                string = ".";
                break;
            case 2:
                string = "..";
                break;
            case 3:
                string = "...";
                break;
            case 4:
                string = "....";
                break;
            case 5:
                string = ".....";
                break;
            default:
                string = "";

        }
        this.loadingLable.setText("Please wait" + string);

    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                this.sendbutton.setVisible(false);
                this.loadingLable.setForeground(DefaultSettings.blue_bar_background);

                String pakId = SendToServer.getRanDomPacketID();
                CreateCircle grpClass = new CreateCircle();
                for (String key : MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate().keySet()) {
                    NewCircleFields grpMapp = new NewCircleFields();
                    grpMapp.setUserIdentity(key);
                    grpMapp.setAdmin(MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate().get(key));
                    grpClass.getCircleMembers().add(grpMapp);
                }
                grpClass.setAction(AppConstants.TYPE_CREATE_CIRCLE);
                grpClass.setPacketId(pakId);
                grpClass.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                grpClass.setCircleName(circleName);
                SendToServer.sendPacketAsString(grpClass, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(550);

                for (int i = 1; i <= 5; i++) {
                    setMessage(i);
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(grpClass, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (fields != null && fields.getSuccess()) {
                            (new DefaultRequest(AppConstants.TYPE_CIRCLE_LIST)).start();
                            Thread.sleep(1500);
                            this.sendbutton.setVisible(true);
                            this.loadingLable.setText(NotificationMessages.CIRCLE_CREATE_SUCCESS);
                            Thread.sleep(1000);

                            if (getMainRightDetailsView().getCircleViewRight() != null) {
                                getMainRightDetailsView().getCircleViewRight().buildCircleCount();
                                getMainRightDetailsView().getCircleViewRight().buildMyCircle();
                            }

                            getMainRightDetailsView().showCircleProfile(fields.getCircleId());
                        } else {
                            this.loadingLable.setForeground(Color.RED);
                            this.loadingLable.setText(" server response failed "+NotificationMessages.CAN_NOT_CREATE_CIRCLE+" Click Reset Button and Create Again");
                        }

                        MyfnfHashMaps.getInstance().getTempCircleMemberContainerforUpdate().clear();
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        break;
                    }

                }
                this.loadingLable.setForeground(Color.RED);
                this.sendbutton.setVisible(true);
                //this.loadingLable.setText("Failed !");
            } catch (Exception e) {
                this.loadingLable.setForeground(Color.RED);
                this.sendbutton.setVisible(true);
                this.loadingLable.setText("Failed !");
            }

        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}
