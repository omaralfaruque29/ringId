/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.friendlist;

import com.ipvision.view.leftdata.FriendListContainer;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.UserBasicInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;

/**
 *
 * @author Shahadat
 */
public class AllFriendList extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AllFriendList.class);
    public static String selectedFriendUserIdentity = null;
    public static SingleFriendPanel selectedFriendPanel = null;
    int mb = 1024 * 1024;

    public AllFriendList() {
////        new DefaultRequest(AppConstants.TYPE_YOU_MAY_KNOW_LIST).start();
//        fetchInviteFriendList();
        selectedFriendUserIdentity = null;
        selectedFriendPanel = null;
        this.setOpaque(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    }

    public void clearFriendList() {
        this.removeAll();
        this.revalidate();
    }

    private FriendListContainer getFriendListContainer() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainLeftContainer() != null) {
            return GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer();
        } else {
            return null;
        }
    }

    public void buildFriendList() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    /*if (getFriendListContainer().categoryPnl.mid.isSelected) {
                     if (getFriendListContainer().getFriendSearchPanel() != null) {
                     getFriendListContainer().getFriendSearchPanel().categoryPanel.setVisible(false);
                     getFriendListContainer().friendListWrapperPanel.setVisible(false);
                     if (getFriendListContainer().getFriendSearchPanel().isInviteSearch()) {
                     getFriendListContainer().getFriendSearchPanel().createInviteFriendList();
                     } else {
                     getFriendListContainer().getFriendSearchPanel().createPeopleYouMayKnowList();
                     }
                     }
                     } else {*/
                    List<UserBasicInfo> friendInfoList = new ArrayList<>();
                    for (Entry entity : FriendList.getInstance().getFriend_hash_map().entrySet()) {
                        UserBasicInfo friendInfo = (UserBasicInfo) entity.getValue();
                        friendInfoList.add(friendInfo);
                    }
                    if (getFriendListContainer().getFriendSearchPanel() != null) {
                        /* getFriendListContainer().friendListWrapperPanel.setVisible(true);
                         getFriendListContainer().inviteListWrapperPanel.setVisible(false);
                         getFriendListContainer().peopleYouMayKnowWrapperPanel.setVisible(false);*/
                        getFriendListContainer().getFriendSearchPanel().createFriendList(friendInfoList);
                    }
                    Runtime runtime = Runtime.getRuntime();
                    long freeSpace = runtime.freeMemory() / mb;
                    System.err.println("free space==>" + freeSpace);
                    //}
                } catch (Exception e) {
                    // e.printStackTrace();
                    log.error("Error in buildFriendList ==>" + e.getMessage());
                }
            }
        });
    }

    public static void changeFriendAllInformation(String userIdentity) {
        SingleFriendPanel friendPanelInBook = MyfnfHashMaps.getInstance().getSingleFriendPanel().get(userIdentity);
        if (friendPanelInBook != null) {
            friendPanelInBook.changeFriendAllInformation();
        }
    }

    public static void changeFriendProfileImage(String userIdentity) {
        SingleFriendPanel friendPanelInBook = MyfnfHashMaps.getInstance().getSingleFriendPanel().get(userIdentity);
        if (friendPanelInBook != null) {
            friendPanelInBook.changeFriendProfileImage();
        }
    }

    public static void changeFullName(String userIdentity) {
        SingleFriendPanel friendPanelInBook = MyfnfHashMaps.getInstance().getSingleFriendPanel().get(userIdentity);
        if (friendPanelInBook != null) {
            friendPanelInBook.changeFullName();
        }
    }

    public static void changeFriendShipStatus(String userIdentity) {
        SingleFriendPanel friendPanelInBook = MyfnfHashMaps.getInstance().getSingleFriendPanel().get(userIdentity);
        if (friendPanelInBook != null) {
            friendPanelInBook.changeFriendShipStatus();
        }
    }

    public static void changeAccessControl(String userIdentity) {
        SingleFriendPanel friendPanelInBook = MyfnfHashMaps.getInstance().getSingleFriendPanel().get(userIdentity);
        if (friendPanelInBook != null) {
            friendPanelInBook.setCallChatAccess();
            friendPanelInBook.changeAccessImage(false);
        }
    }

    public static void removeSelection() {
        if (selectedFriendPanel != null) {
            selectedFriendPanel.friendInfoPanel.setBackground(RingColorCode.FRIEND_LIST_BG_COLOR);//off foucs
            if (selectedFriendPanel.getComponentCount() > 1) {
                selectedFriendPanel.remove(1);
                selectedFriendPanel.revalidate();
            }
            selectedFriendPanel.changeAccessImage(false);
            if (GuiRingID.getInstance() != null) {
                GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().friendListShade.revalidate();
            }
        }
        selectedFriendPanel = null;

    }

    public static void addSelection(String userIdentity) {
        if (userIdentity != null) {
            SingleFriendPanel friendPanelInBook = MyfnfHashMaps.getInstance().getSingleFriendPanel().get(userIdentity);
            if (friendPanelInBook != null) {
                friendPanelInBook.friendInfoPanel.setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
                friendPanelInBook.changeAccessImage(true);
                selectedFriendPanel = friendPanelInBook;
                if (GuiRingID.getInstance() != null) {
                    GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().friendListShade.revalidate();
                }
            }
        }

    }
}
