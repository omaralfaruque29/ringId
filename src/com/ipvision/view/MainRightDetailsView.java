/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipvision.constants.RingColorCode;
import com.ipvision.model.UserBasicInfo;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.ipvision.view.feed.AllFeedsView;
import com.ipvision.view.circle.CircleHome;
import com.ipvision.view.circle.CircleList;
import com.ipvision.view.circle.CircleViewRight;
import com.ipvision.view.circle.CircleProfile;
import com.ipvision.view.circle.CreateCirclePanel;
import com.ipvision.view.friendprofile.MyFriendProfile;
import com.ipvision.view.myprofile.MyProfilePanel;
import com.ipvision.view.myprofile.ProfileCoverChangePanel;
import com.ipvision.view.friendprofile.UnknownProfile;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.group.CreateGroupPanel;
import com.ipvision.view.group.GroupViewRight;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.view.feed.SingleNotificationDialog;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.view.friendprofile.MyfriendSlider;
import javax.swing.SwingUtilities;

/**
 *
 * @author Faiz
 */
public class MainRightDetailsView extends JPanel {

    private MyProfilePanel myProfilePanel;
    private MyFriendProfile myFriendProfile;
    private UnknownProfile unknownProfile;
    private CircleProfile circleProfile;
    private CircleHome fnFHomePanel;
    private ProfileCoverChangePanel profileCoverImageChangePanel;
    private CreateCirclePanel createNewCirclePanel;
    private CircleViewRight circleViewRight;
    private AllFeedsView allFeedsView;
    private GroupViewRight groupMainView;
    private GroupPanel groupPanel;
    private CreateGroupPanel createNewGroupPanel;
    private MyFriendChatCallPanel myFriendChatCallPanel;

    public GroupViewRight getGroupMainView() {
        return groupMainView;
    }

    public GroupPanel getGroupPanel() {
        return groupPanel;
    }

    public MyProfilePanel getMyProfilePanel() {
        return myProfilePanel;
    }

    public void setMyProfilePanel(MyProfilePanel myProfilePanel) {
        this.myProfilePanel = myProfilePanel;
    }

    public MyFriendChatCallPanel getMyFriendChatCallPanel() {
        return myFriendChatCallPanel;
    }

    public void setMyFriendChatCallPanel(MyFriendChatCallPanel myFriendChatCallPanel) {
        this.myFriendChatCallPanel = myFriendChatCallPanel;
    }

    public MyFriendProfile getMyFriendProfile() {
        return myFriendProfile;
    }

    public void setMyFriendProfile(MyFriendProfile myFriendProfile) {
        this.myFriendProfile = myFriendProfile;
    }

    public UnknownProfile getUnknownProfile() {
        return unknownProfile;
    }

    public void setUnknownProfile(UnknownProfile unknownProfile) {
        this.unknownProfile = unknownProfile;
    }

    public CircleProfile getCircleProfile() {
        return circleProfile;
    }

    public void setCircleProfile(CircleProfile circleProfile) {
        this.circleProfile = circleProfile;
    }

    public CircleHome getFnFHomePanel() {
        return fnFHomePanel;
    }

    public void setFnFHomePanel(CircleHome fnFHomePanel) {
        this.fnFHomePanel = fnFHomePanel;
    }

    public ProfileCoverChangePanel getProfileCoverImageChangePanel() {
        return profileCoverImageChangePanel;
    }

    public void setProfileCoverImageChangePanel(ProfileCoverChangePanel profileCoverImageChangePanel) {
        this.profileCoverImageChangePanel = profileCoverImageChangePanel;
    }

    public CreateCirclePanel getCreateNewCirclePanel() {
        return createNewCirclePanel;
    }

    public void setCreateNewCirclePanel(CreateCirclePanel createNewGroupPanel) {
        this.createNewCirclePanel = createNewGroupPanel;
    }

    public CreateGroupPanel getCreateNewGroupPanel() {
        return createNewGroupPanel;
    }

    public void setCreateNewGroupPanel(CreateGroupPanel createNewGroupPanel) {
        this.createNewGroupPanel = createNewGroupPanel;
    }

    public CircleViewRight getCircleViewRight() {
        return circleViewRight;
    }

    public void setCircleViewRight(CircleViewRight circleViewRight) {
        this.circleViewRight = circleViewRight;
    }

    public AllFeedsView getAllFeedsView() {
        return allFeedsView;
    }

    int emptyBorder = 5;

    public MainRightDetailsView() {
        setLayout(new BorderLayout());
        setBackground(RingColorCode.DEFAULT_BACKGROUND_COLOR);
        // setBorder(new EmptyBorder(0, emptyBorder, 0, 0));
        allFeedsView = new AllFeedsView();
        add(allFeedsView);
    }

    private void loadIntoMainRightContent(JPanel jPanel) {
        //GuiRingID.getInstance().getMainButtonsPanel().resetMainButtonsColor(null);
        //    GuiRingID.getInstance().getTopMenuBar().resetMenuColor(null);
        if (GuiRingID.getInstance() != null) {
            GuiRingID.getInstance().loadIntoMainRightContent(jPanel);
        }
    }
    public MyfriendSlider myfriendSlider;

    public void showFriendProfile(String userIdentity, Integer type) {
        myFriendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(userIdentity);
        myFriendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(userIdentity);

        boolean isNewFriendProfile = false;
        boolean isNewFriendChatCallPanel = false;
        final boolean isNewSlider = (myfriendSlider == null
                || type != null
                || myFriendProfile == null
                || myFriendChatCallPanel == null
                || !myfriendSlider.getFriendIdentity().equalsIgnoreCase(userIdentity));

        if (isNewSlider) {
            myfriendSlider = new MyfriendSlider(userIdentity, type);
        }
        loadIntoMainRightContent(myfriendSlider);
        GuiRingID.getInstance().getTopMenuBar().resetMenuColor(null);
        CircleList.setCircleSelectionColor(null);

        if (myfriendSlider.getType() == MyfriendSlider.TYPE_MY_FRIEND_CHAT_CALL
                && myFriendChatCallPanel != null
                && !myFriendChatCallPanel.isDisplayable()) {
            myFriendChatCallPanel.myFriendChatPanel.hideTimerOnInit();
        }

        if (myFriendProfile != null) {
            myFriendProfile.myfriendSlider = myfriendSlider;
            myFriendProfile.setFriendProfileInfo(userIdentity);
            MyFriendProfile.setSeleected_user_identiy(userIdentity);
        } else {
            isNewFriendProfile = true;
            myFriendProfile = new MyFriendProfile(userIdentity, myfriendSlider);
            MyfnfHashMaps.getInstance().getMyFriendProfiles().put(userIdentity, myFriendProfile);
        }

        if (myFriendChatCallPanel != null) {
            myFriendChatCallPanel.myfriendSlider = myfriendSlider;
        } else {
            isNewFriendChatCallPanel = true;
            myFriendChatCallPanel = new MyFriendChatCallPanel(userIdentity, myfriendSlider);
            MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().put(userIdentity, myFriendChatCallPanel);
        }

        final boolean isNewProfile = isNewFriendProfile;
        final boolean isNewChat = isNewFriendChatCallPanel;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (isNewProfile) {
                    myFriendProfile.initContainers();
                }

                if (isNewChat) {
                    myFriendChatCallPanel.myFriendChatPanel.addFriendChatPanel();
                    myFriendChatCallPanel.refreshMyFriendChatCallPanelAll();
                }

                if (isNewSlider) {
                    if (myfriendSlider.getType() == MyfriendSlider.TYPE_MY_FRIEND_CHAT_CALL) {
                        myfriendSlider.addSliderComponent(myFriendChatCallPanel);
                        myfriendSlider.addSliderComponent(myFriendProfile);
                    } else {
                        myfriendSlider.addSliderComponent(myFriendProfile);
                        myfriendSlider.addSliderComponent(myFriendChatCallPanel);
                    }
                }

                if (myfriendSlider.getType() == MyfriendSlider.TYPE_MY_FRIEND_PROFILE && isNewProfile) {
                    myFriendProfile.loadPresenceStatus();
                    myFriendProfile.onTabClick();
                } else if (myfriendSlider.getType() == MyfriendSlider.TYPE_MY_FRIEND_PROFILE && !isNewProfile) {
                    myFriendProfile.checkSelectedTab();
                } else if (myfriendSlider.getType() == MyfriendSlider.TYPE_MY_FRIEND_CHAT_CALL) {
                    myFriendChatCallPanel.myFriendChatPanel.loadInitHistory();
                }

            }
        });
    }

    public void showNotificationStatus(Long notificationId) {
        JPanel prevMainRightPanel = (JPanel) GuiRingID.getInstance().getMainRight().getComponent(0);
        SingleNotificationDialog showNotificationStatus = new SingleNotificationDialog(notificationId, prevMainRightPanel);
        loadIntoMainRightContent(showNotificationStatus);
        GuiRingID.getInstance().getTopMenuBar().resetMenuColor(null);
        //AllFriendList.setFriendSelectionColor(null, true);
        CircleList.setCircleSelectionColor(null);

    }

    public void showGroupPanel(Long groupId) {
        groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(groupId);
        if (groupPanel != null) {
            groupPanel.loadInitHistory();
        } else {
            groupPanel = new GroupPanel(groupId);
            MyfnfHashMaps.getInstance().getGroupPanelMap().put(groupId, groupPanel);
        }
        loadIntoMainRightContent(groupPanel);
        GuiRingID.getInstance().getTopMenuBar().resetMenuColor(null);
    }

    public void showCircleProfile(long circleId) {
        if (MyfnfHashMaps.getInstance().getCircleMembers().get(circleId) != null) {
            circleProfile = MyfnfHashMaps.getInstance().getCircleProfiles().get(circleId);
            if (circleProfile == null) {
                circleProfile = new CircleProfile(circleId);
                MyfnfHashMaps.getInstance().getCircleProfiles().put(circleId, circleProfile);
            } else {
                circleProfile.checkSelectedTab();
            }
            loadIntoMainRightContent(circleProfile);
            GuiRingID.getInstance().getTopMenuBar().resetMenuColor(null);
        }

        //AllFriendList.setFriendSelectionColor(null, true);
        CircleList.setCircleSelectionColor(circleId);
    }

    public void showUnknownProfile(UserBasicInfo basicInfo, MyFriendProfile myFriendProfile) {

        unknownProfile = new UnknownProfile(basicInfo, myFriendProfile);
        loadIntoMainRightContent(unknownProfile);
        GuiRingID.getInstance().getTopMenuBar().resetMenuColor(null);
        //   AllFriendList.setFriendSelectionColor(basicInfo.getUserIdentity(), true);
        CircleList.setCircleSelectionColor(null);

    }

    public void showUnknownProfile(UserBasicInfo basicInfo, int type_int) {
        unknownProfile = new UnknownProfile(basicInfo, type_int);
        loadIntoMainRightContent(unknownProfile);
        GuiRingID.getInstance().getTopMenuBar().resetMenuColor(null);
        // AllFriendList.changeSelectedFriend(basicInfo.getUserIdentity());
        //  AllFriendList.setFriendSelectionColor(basicInfo.getUserIdentity(), true);
        CircleList.setCircleSelectionColor(null);

    }

    public void showUnknownProfile(UserBasicInfo basicInfo) {
        unknownProfile = new UnknownProfile(basicInfo);
        loadIntoMainRightContent(unknownProfile);
        GuiRingID.getInstance().getTopMenuBar().resetMenuColor(null);
        //  AllFriendList.changeSelectedFriend(basicInfo.getUserIdentity());
        CircleList.setCircleSelectionColor(null);
    }

    public void circleDetails(long circleId) {
        if (MyfnfHashMaps.getInstance().getCircleMembers().get(circleId) != null) {
            if (MyfnfHashMaps.getInstance().getCircleProfiles().get(circleId) != null) {
                circleProfile = MyfnfHashMaps.getInstance().getCircleProfiles().get(circleId);
            } else {
                circleProfile = new CircleProfile(circleId);
                MyfnfHashMaps.getInstance().getCircleProfiles().put(circleId, circleProfile);
            }
            loadIntoMainRightContent(circleProfile);
            GuiRingID.getInstance().getTopMenuBar().resetMenuColor(null);
        }
        //AllFriendList.setFriendSelectionColor(null, true);
        CircleList.setCircleSelectionColor(circleId);
    }

    public void actionofMynamePanel() {
        if (myProfilePanel == null) {
            myProfilePanel = new MyProfilePanel();
            myProfilePanel.setType(MyProfilePanel.TYPE_MY_BOOK);
            myProfilePanel.initContainers();
            myProfilePanel.change_myFullName();
            myProfilePanel.changeMyImage();
            loadIntoMainRightContent(myProfilePanel);
        } else {
            myProfilePanel.setType(MyProfilePanel.TYPE_MY_BOOK);
            myProfilePanel.checkSelectedTab();
            loadIntoMainRightContent(myProfilePanel);
        }
        GuiRingID.getInstance().getTopMenuBar().resetMenuColor(null);
        //AllFriendList.setFriendSelectionColor(null, true);
    }

    public void showCircleViewPanel() {
        try {
            if (circleViewRight == null) {
                circleViewRight = new CircleViewRight();
            }
            circleViewRight.loadPreviousComponent(GuiRingID.getInstance().getMainRightContent());
            circleViewRight.loadData();
            loadIntoMainRightContent(circleViewRight);
        } catch (Exception e) {
        }
    }

    public void showGroupViewPanel() {
        try {
            if (groupMainView == null) {
                groupMainView = new GroupViewRight();
            }
            groupMainView.loadPreviousComponent(GuiRingID.getInstance().getMainRightContent());
            groupMainView.loadData();
            loadIntoMainRightContent(groupMainView);
        } catch (Exception e) {
        }
    }

    public void showProfileImageChange(int imageType) {

        //GuiRingID.getInstance().getLeftMainMenuPanel().resetMenuColor(null);
        //profileCoverImageChangePanel = ProfileCoverChangePanel.getInstance();
        JPanel prevMainRightPanel = (JPanel) GuiRingID.getInstance().getMainRight().getComponent(0);
        profileCoverImageChangePanel = new ProfileCoverChangePanel(prevMainRightPanel);
        profileCoverImageChangePanel.initialize(imageType);
        loadIntoMainRightContent(profileCoverImageChangePanel);
        GuiRingID.getInstance().getTopMenuBar().resetMenuColor(null);
        //profileCoverImageChangePanel.scrollPnlTopPC.setVisible(false);
        //profileCoverImageChangePanel.buttonAndImageChangePanel.revalidate();
        // profileCoverImageChangePanel.buttonAndImageChangePanel.repaint();

        if (profileCoverImageChangePanel.scrollContent.getVerticalScrollBar() != null
                && profileCoverImageChangePanel.scrollContent.getVerticalScrollBar().isVisible()) {
            profileCoverImageChangePanel.scrollPnlTopPC.setVisible(true);
        } else {
            profileCoverImageChangePanel.scrollPnlTopPC.setVisible(false);
        }
        profileCoverImageChangePanel.buttonAndImageChangePanel.revalidate();
        profileCoverImageChangePanel.buttonAndImageChangePanel.repaint();

        profileCoverImageChangePanel.scrollContent.getViewport().addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (profileCoverImageChangePanel.scrollContent.getVerticalScrollBar().isVisible()) {
                    profileCoverImageChangePanel.scrollPnlTopPC.setVisible(true);
                } else {
                    profileCoverImageChangePanel.scrollPnlTopPC.setVisible(false);
                }
                profileCoverImageChangePanel.buttonAndImageChangePanel.revalidate();
                profileCoverImageChangePanel.buttonAndImageChangePanel.repaint();
            }
        });
        //AllFriendList.setFriendSelectionColor(null, true);
        CircleList.setCircleSelectionColor(null);

    }

    public void action_of_myProfile_button() {
        if (myProfilePanel == null) {
            myProfilePanel = new MyProfilePanel();
            myProfilePanel.initContainers();
            myProfilePanel.change_myFullName();
            myProfilePanel.changeMyImage();
            //myProfilePanel.changWhatinMind();
            loadIntoMainRightContent(myProfilePanel);
        } else {
            myProfilePanel.setType(MyProfilePanel.TYPE_MY_BOOK);
            myProfilePanel.onTabClick();
            loadIntoMainRightContent(myProfilePanel);
        }
        GuiRingID.getInstance().getTopMenuBar().resetMenuColor(null);
        //getMainFriendListContainer().addAcceptedFriendList();
        //GuiRingID.getInstance().getLeftMainMenuPanel().resetMenuColor(null);
        //AllFriendList.setFriendSelectionColor(null, true);
        CircleList.setCircleSelectionColor(null);

    }

    public void action_of_fnf_create_circle_button() {
        createNewCirclePanel = new CreateCirclePanel();
        createNewCirclePanel.clear_selected_members();
        createNewCirclePanel.resetNumberofAddedMembers();
      //  createNewCirclePanel.addSelectedCircleFreindList();
        loadIntoMainRightContent(createNewCirclePanel);
        GuiRingID.getInstance().getTopMenuBar().resetMenuColor(null);
        //AllFriendList.setFriendSelectionColor(null, true);
        CircleList.setCircleSelectionColor(null);
    }

    public void action_of_fnf_create_group_button() {
        createNewGroupPanel = new CreateGroupPanel();
        createNewGroupPanel.clear_selected_members();
        createNewGroupPanel.resetNumberofAddedMembers();
        createNewGroupPanel.addSelectedGroupFreindList();
        loadIntoMainRightContent(createNewGroupPanel);
        GuiRingID.getInstance().getTopMenuBar().resetMenuColor(null);

        //AllFriendList.setFriendSelectionColor(null, true);
    }

}
