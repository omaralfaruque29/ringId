/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.friendlist;

import com.desktopCall.dtos.CallSettingsDTo;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.BlockUnblockFriend;
import com.ipvision.service.ChangeFriendAccess;
import com.ipvision.service.DeleteFreind;
import com.ipvision.view.call.MainClass;
import com.ipvision.view.friendprofile.MyFriendProfile;
import com.ipvision.view.friendprofile.MyfriendSlider;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.utility.JDialogContactType;
import com.ipvision.view.utility.JOptionPanelBasics;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author raj
 */
public class SingleFriendPopupMenu extends JPopupMenu {

    //private UserBasicInfo user;
    public UserBasicInfo user;
    //  private JMenuItem mnuCall;

    private JPanel mnuCall;
    private JPanel mnuVideoCall;
    private JPanel mnuChat;
    private JPanel mnuFeed;
    private JPanel mnuFriendList;
    private JPanel mnuPhotos;
    private JPanel mnuAbout;
    private JPanel mnuBlock;
    private JPanel mnuUnblock;
    private JPanel mnuChangeAccess;
    private JPanel mnuRemove;
    private Color color;

    public SingleFriendPopupMenu(UserBasicInfo user) {
        this.user = user;
        setBorder(new MatteBorder(1, 1, 1, 1, new Color(0xd5d5d5)));
        setBackground(Color.WHITE);
        setVisible(false);
        initComponents(user.getFullName());
    }

    private void initComponents(String fullName) {
        mnuCall = getMenuItem("Call to " + fullName, GetImages.LEFT_FRIEND_MENU_CALL);
        mnuVideoCall = getMenuItem("Video Call", GetImages.LEFT_FRIEND_MENU_VIDEO_CALL);
        mnuChat = getMenuItem("Chat", GetImages.LEFT_FRIEND_MENU_CHAT);
        mnuFeed = getMenuItem("Feed", GetImages.LEFT_FRIEND_MENU_BOOK);
        mnuFriendList = getMenuItem("Friends", GetImages.LEFT_FRIEND_MENU_FRIENDS);
        mnuPhotos = getMenuItem("Photos", GetImages.LEFT_FRIEND_MENU_PHOTOS);
        mnuAbout = getMenuItem("About", GetImages.LEFT_FRIEND_MENU_ABOUT);
        mnuBlock = getMenuItem("Block", GetImages.LEFT_FRIEND_MENU_BLOCK_USER);
        mnuUnblock = getMenuItem("Unblock", GetImages.LEFT_FRIEND_MENU_UNBLOCK_USER);
        mnuChangeAccess = getMenuItem("Change Access", GetImages.LEFT_FRIEND_MENU_CHANGE_ACCESS);
        mnuRemove = getMenuItem("Remove From Contacts", GetImages.LEFT_FRIEND_MENU_REMOVE_CONTACT);

        add(mnuCall);
        add(getCustomSeperator());
        add(mnuVideoCall);
        add(getCustomSeperator());
        add(mnuChat);
        add(getCustomSeperator());
        add(mnuFeed);
        add(getCustomSeperator());
        add(mnuFriendList);
        add(getCustomSeperator());
        add(mnuPhotos);
        add(getCustomSeperator());
        add(mnuAbout);
        add(getCustomSeperator());
        if (user.getBlocked() != null && user.getBlocked() > 0) {
            add(mnuUnblock);
            add(getCustomSeperator());
        } else {
            add(mnuBlock);
            add(getCustomSeperator());
        }
        add(mnuChangeAccess);
        add(getCustomSeperator());
        add(mnuRemove);

    }

    private JPanel getMenuItem(String text, String imageUrl) {
        JPanel menu = new JPanel(new BorderLayout());
        menu.setBackground(Color.WHITE);
        menu.setPreferredSize(new Dimension(180, 35));
        JLabel nameLabel = new JLabel(text);
        nameLabel.setBorder(new EmptyBorder(0, 2, 0, 0));
        nameLabel.setOpaque(false);
        nameLabel.setIcon(DesignClasses.return_image(imageUrl));
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        nameLabel.setPreferredSize(new Dimension(175, 35));
        menu.setBorder(null);
        menu.setOpaque(true);
        menu.add(nameLabel, BorderLayout.CENTER);
        menu.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getSource() == mnuCall) {

                    if (MainClass.getMainClass() == null) {
                        CallSettingsDTo callSettingsDTo = new CallSettingsDTo();
                        // callSettingsDTo.setDvc(friendProfileInfo.getDevice());
                        callSettingsDTo.setUserid(MyFnFSettings.LOGIN_USER_ID);
                        callSettingsDTo.setFndId(user.getUserIdentity());
                        callSettingsDTo.setFn(user.getFullName());
                        callSettingsDTo.setPrIm(user.getProfileImage());
                        callSettingsDTo.setIncomming(0);
                        MainClass mainClass2 = new MainClass(callSettingsDTo);
                        mainClass2.showWindow();
                    } else {
                        MainClass.getMainClass().showWindow();
                    }
                    setVisible(false);
                } else if (e.getSource() == mnuVideoCall) {
                    HelperMethods.showWarningDialogMessage(NotificationMessages.VIDEO_CALL_NOTIFICATION);
                    setVisible(false);

                } else if (e.getSource() == mnuChat) {
                    GuiRingID.getInstance().getMainRight().showFriendProfile(user.getUserIdentity(), MyfriendSlider.TYPE_MY_FRIEND_CHAT_CALL);
                    setVisible(false);

                } else if (e.getSource() == mnuFeed) {
                    GuiRingID.getInstance().getMainRight().showFriendProfile(user.getUserIdentity(), MyfriendSlider.TYPE_MY_FRIEND_PROFILE);
                    //GuiRingID.getInstance().showFriendProfile(user.getUserIdentity());
                    MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(user.getUserIdentity());
                    if (friendProfile != null) {
                        friendProfile.setFriendProfileInfo(user.getUserIdentity());
                        friendProfile.setType(MyFriendProfile.TYPE_BOOK);
                        friendProfile.onTabClick();
                    }
                    //  GuiRingID.getInstance().getMainRight().showFriendProfile(user.getUserIdentity(), MyfriendSlider.TYPE_MY_FRIEND_PROFILE);
                    setVisible(false);

                } else if (e.getSource() == mnuFriendList) {
                    GuiRingID.getInstance().getMainRight().showFriendProfile(user.getUserIdentity(), MyfriendSlider.TYPE_MY_FRIEND_PROFILE);
                    //GuiRingID.getInstance().showFriendProfile(user.getUserIdentity());
                    MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(user.getUserIdentity());
                    if (friendProfile != null) {
                        friendProfile.setFriendProfileInfo(user.getUserIdentity());
                        friendProfile.setType(MyFriendProfile.TYPE_CONTACTLIST);
                        friendProfile.onTabClick();
                    }
                    setVisible(false);

                } else if (e.getSource() == mnuPhotos) {
                    GuiRingID.getInstance().getMainRight().showFriendProfile(user.getUserIdentity(), MyfriendSlider.TYPE_MY_FRIEND_PROFILE);
                    //GuiRingID.getInstance().showFriendProfile(user.getUserIdentity());
                    MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(user.getUserIdentity());
                    if (friendProfile != null) {
                        friendProfile.setFriendProfileInfo(user.getUserIdentity());
                        friendProfile.setType(MyFriendProfile.TYPE_ALBUM);
                        friendProfile.onTabClick();
                    }
                    setVisible(false);

                } else if (e.getSource() == mnuAbout) {
                    GuiRingID.getInstance().getMainRight().showFriendProfile(user.getUserIdentity(), MyfriendSlider.TYPE_MY_FRIEND_PROFILE);
                    //GuiRingID.getInstance().showFriendProfile(user.getUserIdentity());
                    MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(user.getUserIdentity());
                    if (friendProfile != null) {
                        friendProfile.setFriendProfileInfo(user.getUserIdentity());
                        friendProfile.setType(MyFriendProfile.TYPE_ABOUT);
                        friendProfile.onTabClick();
                    }
                    setVisible(false);

                } else if (e.getSource() == mnuBlock) {
                    new BlockUnblockFriend(user.getUserIdentity(), user.getUserTableId(), 1).start();
                    MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(user.getUserIdentity());
                    if (friendProfile != null) {
                        friendProfile.blockFriendProfile();
                    }
                    setVisible(false);

                } else if (e.getSource() == mnuUnblock) {
                    new BlockUnblockFriend(user.getUserIdentity(), user.getUserTableId(), 0).start();
                    MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(user.getUserIdentity());
                    if (friendProfile != null) {
                        friendProfile.unBlockFriendProfile();
                    }
                    setVisible(false);

                } else if (e.getSource() == mnuChangeAccess) {
                    JDialogContactType dialog = new JDialogContactType();
                    if (JDialogContactType.contactType > 0 && (user.getContactType() != JDialogContactType.contactType)) {
                        new ChangeFriendAccess(user.getUserIdentity(), user.getUserTableId(), JDialogContactType.contactType).start();
                    }

                } else if (e.getSource() == mnuRemove) {
                    HelperMethods.showConfirmationDialogMessage(NotificationMessages.REMOVE_NOTIFICAITON);
                    if (JOptionPanelBasics.YES_NO) {
                        new DeleteFreind(user.getUserIdentity()).start();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {

                if (e.getSource() == mnuCall) {
                    mnuCall.setBackground(RingColorCode.RING_TOP_MENU_SELECTED_BG_COLOR);
                } else if (e.getSource() == mnuVideoCall) {
                    mnuVideoCall.setBackground(RingColorCode.RING_TOP_MENU_SELECTED_BG_COLOR);
                } else if (e.getSource() == mnuChat) {
                    mnuChat.setBackground(RingColorCode.RING_TOP_MENU_SELECTED_BG_COLOR);
                } else if (e.getSource() == mnuFeed) {
                    //  if (!mnuFeed.getBackground().equals(RingColorCode.FRIEND_LIST_SELECTION_COLOR)) {
                    mnuFeed.setBackground(RingColorCode.RING_TOP_MENU_SELECTED_BG_COLOR);
                    //  }
                } else if (e.getSource() == mnuFriendList) {
                    mnuFriendList.setBackground(RingColorCode.RING_TOP_MENU_SELECTED_BG_COLOR);
                } else if (e.getSource() == mnuPhotos) {
                    mnuPhotos.setBackground(RingColorCode.RING_TOP_MENU_SELECTED_BG_COLOR);
                } else if (e.getSource() == mnuAbout) {
                    mnuAbout.setBackground(RingColorCode.RING_TOP_MENU_SELECTED_BG_COLOR);
                } else if (e.getSource() == mnuBlock) {
                    mnuBlock.setBackground(RingColorCode.RING_TOP_MENU_SELECTED_BG_COLOR);
                } else if (e.getSource() == mnuUnblock) {
                    mnuUnblock.setBackground(RingColorCode.RING_TOP_MENU_SELECTED_BG_COLOR);
                } else if (e.getSource() == mnuChangeAccess) {
                    mnuChangeAccess.setBackground(RingColorCode.RING_TOP_MENU_SELECTED_BG_COLOR);
                } else if (e.getSource() == mnuRemove) {
                    mnuRemove.setBackground(RingColorCode.RING_TOP_MENU_SELECTED_BG_COLOR);
                }
            }

            @Override
            public void mouseExited(MouseEvent e
            ) {
                if (e.getSource() == mnuCall) {
                    mnuCall.setBackground(Color.WHITE);
                } else if (e.getSource() == mnuVideoCall) {
                    mnuVideoCall.setBackground(Color.WHITE);
                } else if (e.getSource() == mnuChat) {
                    mnuChat.setBackground(Color.WHITE);
                } else if (e.getSource() == mnuFeed) {
                    //   if (!mnuFeed.getBackground().equals(RingColorCode.FRIEND_LIST_SELECTION_COLOR)) {
                    mnuFeed.setBackground(Color.WHITE);
                } else if (e.getSource() == mnuFriendList) {
                    mnuFriendList.setBackground(Color.WHITE);
                } else if (e.getSource() == mnuPhotos) {
                    mnuPhotos.setBackground(Color.WHITE);
                } else if (e.getSource() == mnuAbout) {
                    mnuAbout.setBackground(Color.WHITE);
                } else if (e.getSource() == mnuBlock) {
                    mnuBlock.setBackground(Color.WHITE);
                } else if (e.getSource() == mnuUnblock) {
                    mnuUnblock.setBackground(Color.WHITE);
                } else if (e.getSource() == mnuChangeAccess) {
                    mnuChangeAccess.setBackground(Color.WHITE);
                } else if (e.getSource() == mnuRemove) {
                    mnuRemove.setBackground(Color.WHITE);
                }
            }
        }
        );
        return menu;

    }

    public void buildPopUpMenu() {
//        UIManager.put("Menu.background", RingColorCode.RING_TOP_MENUITEM_BG_COLOR);
//        UIManager.put("Menu.foreground", RingColorCode.RING_TOP_MENUITEM_FG_COLOR);
//
//        UIManager.put("MenuItem.background", RingColorCode.RING_TOP_MENUITEM_BG_COLOR);
//        UIManager.put("MenuItem.foreground", RingColorCode.RING_TOP_MENUITEM_FG_COLOR);

        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBorder(null);
        popupMenu.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0xd5d5d5)));
        popupMenu.setBackground(Color.WHITE);

    }

    public JPanel getCustomSeperator() {
        JPanel seperator = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        seperator.setBackground(new Color(0xe5e6e9));
        seperator.setPreferredSize(new Dimension(1, 1));
        return seperator;
    }
}
