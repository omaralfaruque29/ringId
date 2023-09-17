/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.AppConstants;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import javax.swing.JLabel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.CreateCircle;
import com.ipvision.model.NewCircleFields;
import com.ipvision.service.utility.SendToServer;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import com.ipvision.model.dao.InsertIntoCircleMemberTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.MainRightDetailsView;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.SingleMemberInCircleDto;
import com.ipvision.model.stores.FriendList;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Faiz Ahmed
 */
public class AddMemberInCircle extends Thread {

    private JLabel loadingOrtimePanel;
    private long circleId;
    private final JButton saveButton;
    private final JButton resetButton;

    public AddMemberInCircle(JLabel loadingOrtimePanel, long circleId, JButton saveButton, JButton resetButton) {
        this.loadingOrtimePanel = loadingOrtimePanel;
        this.circleId = circleId;
        this.saveButton = saveButton;
        this.resetButton = resetButton;
    }

    private MainRightDetailsView getMainRight() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
            return GuiRingID.getInstance().getMainRight();
        } else {
            return null;
        }
//        return GuiRingID.getInstance().getMainRight() != null ? GuiRingID.getInstance().getMainRight() : null;
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                if (MyfnfHashMaps.getInstance().getTempCircleMembersStore().get(circleId) != null && !MyfnfHashMaps.getInstance().getTempCircleMembersStore().get(circleId).isEmpty()) {
                    String pakId = SendToServer.getRanDomPacketID();

                    CreateCircle grpClass = new CreateCircle();
                    for (String key : MyfnfHashMaps.getInstance().getTempCircleMembersStore().get(circleId).keySet()) {
                        NewCircleFields grpMapp = new NewCircleFields();
                        grpMapp.setUserIdentity(key);
                        grpMapp.setAdmin(MyfnfHashMaps.getInstance().getTempCircleMembersStore().get(circleId).get(key));
                        grpClass.getCircleMembers().add(grpMapp);
                    }
                    grpClass.setAction(AppConstants.TYPE_ADD_CIRCLE_MEMBER);
                    grpClass.setPacketId(pakId);
                    grpClass.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                    grpClass.setCircleId(circleId);
                    SendToServer.sendPacketAsString(grpClass, ServerAndPortSettings.UPDATE_PORT);
                    Thread.sleep(25);

                    this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.WATING_IMAGE));
                    for (int i = 1; i <= 5; i++) {
                        Thread.sleep(500);
                        if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                            SendToServer.sendPacketAsString(grpClass, ServerAndPortSettings.UPDATE_PORT);
                        } else {
                            FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                            if (fields.getSuccess()) {
                                this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.OK_MINI));
                                this.loadingOrtimePanel.setForeground(DefaultSettings.blue_bar_background);
                                this.loadingOrtimePanel.setText("Added successfully");

                                for (String key : MyfnfHashMaps.getInstance().getTempCircleMembersStore().get(circleId).keySet()) {
                                    SingleMemberInCircleDto singleUser = new SingleMemberInCircleDto();
                                    singleUser.setUserIdentity(key);
                                    singleUser.setAdmin(MyfnfHashMaps.getInstance().getTempCircleMembersStore().get(circleId).get(key));
                                    singleUser.setCircleId(circleId);
                                    singleUser.setFullName(FriendList.getInstance().getFriend_hash_map().get(key).getFullName());
                                    // singleUser.setLastName(FriendList.getInstance().getFriend_hash_map().get(key).getLastName());
                                    singleUser.setGender(FriendList.getInstance().getFriend_hash_map().get(key).getGender());
                                    singleUser.setMobilePhone(FriendList.getInstance().getFriend_hash_map().get(key).getMobilePhone());

                                    if (MyfnfHashMaps.getInstance().getCircleMembers().get(this.circleId) != null) {
                                        MyfnfHashMaps.getInstance().getCircleMembers().get(this.circleId).put(key, singleUser);

                                    }

                                    List<SingleMemberInCircleDto> memberList = new ArrayList<SingleMemberInCircleDto>();
                                    memberList.add(singleUser);
                                    new InsertIntoCircleMemberTable(memberList).start();
                                }

                                if (getMainRight().getCircleViewRight() != null) {
                                    getMainRight().getCircleViewRight().buildCircleList();
                                    ///  if (GuiRingID.getInstance().circleLeft.textMyCircles.getForeground() == DefaultSettings.ORANGE_BACKGROUND_COLOR) {
                                    ///GuiRingID.getInstance().circleLeft.buildMyCircle();
                                    ///    } else if (GuiRingID.getInstance().circleLeft.textCircleofMe.getForeground() == DefaultSettings.ORANGE_BACKGROUND_COLOR) {
                                    ///GuiRingID.getInstance().circleLeft.buildCircleOfMe();
                                    ///   }
                                }
                                //GuiRingID.getInstance().drawGroupList();
                                MyfnfHashMaps.getInstance().getTempCircleMembersStore().get(circleId).clear();
                                getMainRight().getCircleProfile().getCircleMembersListPanel().buildMembersList();

                            } else {

                                this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.BACK_MINI));
                                this.loadingOrtimePanel.setForeground(DefaultSettings.errorLabelColor);
                                this.loadingOrtimePanel.setText("Failed!" + fields.getMessage());

                            }

                            MyfnfHashMaps.getInstance().getTempCircleMembersStore().get(circleId).clear();
                            MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);

                            saveButton.setVisible(true);
                            resetButton.setVisible(true);

                            return;
                        }

                    }

                    this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.BACK_MINI));
                    this.loadingOrtimePanel.setForeground(DefaultSettings.errorLabelColor);
                    this.loadingOrtimePanel.setText("Failed!!");
                    saveButton.setVisible(true);
                    resetButton.setVisible(true);
                }
            } catch (Exception ex) {
                saveButton.setVisible(true);
                resetButton.setVisible(true);
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}
