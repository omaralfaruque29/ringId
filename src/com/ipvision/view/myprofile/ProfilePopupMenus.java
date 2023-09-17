/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.constants.RingColorCode;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.ProfileCoverImageRemove;
import com.ipvision.view.utility.JOptionPanelBasics;

/**
 *
 * @author Sirat Samyoun
 */
public class ProfilePopupMenus {

    private JMenuItem mnuItemAddProfilePicture;
    private JMenuItem mnuItemChangeProfilePicture;
    private JMenuItem mnuItemDeleteProfilePicture;
    private JMenuItem mnuItemAddCoverPhoto;
    private JMenuItem mnuItemChangeCoverPhoto;
    private JMenuItem mnuItemDeleteCoverPhoto;
    private JMenu mnuProfilePhoto;
    private JMenu mnuCoverPhoto;
    private ImageIcon seperatorImg = DesignClasses.return_image(GetImages.MENU_BAR_SEPARATOR);
    private JPanel seperatorAddProfilePhoto;
    private JPanel seperatorChangeProfilePhoto;
    private JPanel seperatorAddCoverPhoto;
    private JPanel seperatorChangeCoverPhoto;
//    private JMenuItem mnuPrivateProfilePicture;
//    private JMenuItem mnuPublicWallPhoto;
//    private JMenuItem mnuPrivateWallPhoto;
    //   private JMenu privacyPicture;
    int type_int;
    // private JPopupMenu actionPopUp = new JPopupMenu();

    public ProfilePopupMenus(int type_int) {
        this.type_int = type_int;
        // if(type_int==0) buildPopUpMenuMyProfile();
    }

    public void buildPopUpMenuMyProfile() {
        UIManager.put("Menu.background", RingColorCode.RING_TOP_MENUITEM_BG_COLOR);
        UIManager.put("Menu.foreground", RingColorCode.RING_TOP_MENUITEM_FG_COLOR);
        //UIManager.put("Menu.selectionBackground", RingColorCode.RING_TOP_MENU_SELECTED_BG_COLOR);
        //UIManager.put("Menu.selectionForeground", RingColorCode.RING_TOP_MENU_SELECTED_FG_COLOR);
        UIManager.put("MenuItem.background", RingColorCode.RING_TOP_MENUITEM_BG_COLOR);
        UIManager.put("MenuItem.foreground", RingColorCode.RING_TOP_MENUITEM_FG_COLOR);

        JPopupMenu popupMenu = GuiRingID.getInstance().getMainRight().getMyProfilePanel().actionPopUp;
        popupMenu.setBorder(null);
        popupMenu.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0xd5d5d5)));
        popupMenu.setBackground(Color.WHITE);

        mnuProfilePhoto = getMenu("Profile Photo");
        popupMenu.add(mnuProfilePhoto);
        popupMenu.add(getCustomSeperator());

        mnuItemAddProfilePicture = getMenuItem(ProfilePanelConstants.ADD_PROFILE_PICTURE, GetImages.EDIT_PHOTO);
        mnuProfilePhoto.add(mnuItemAddProfilePicture);
        removePopupBorder(mnuItemAddProfilePicture);
        seperatorAddProfilePhoto = getCustomSeperator();
        mnuProfilePhoto.add(seperatorAddProfilePhoto);

        mnuItemChangeProfilePicture = getMenuItem(ProfilePanelConstants.CHANGE_PROFILE_PICTURE, GetImages.EDIT_PHOTO);
        mnuProfilePhoto.add(mnuItemChangeProfilePicture);
        seperatorChangeProfilePhoto = getCustomSeperator();
        mnuProfilePhoto.add(seperatorChangeProfilePhoto);

        mnuItemDeleteProfilePicture = getMenuItem(ProfilePanelConstants.DELETE_PROFILE_PICTURE, GetImages.DELETE);
        mnuProfilePhoto.add(mnuItemDeleteProfilePicture);

        mnuCoverPhoto = getMenu("Cover Photo");
        popupMenu.add(mnuCoverPhoto);

        mnuItemAddCoverPhoto = getMenuItem(ProfilePanelConstants.ADD_WALL_PHOTO, GetImages.EDIT_PHOTO);
        mnuCoverPhoto.add(mnuItemAddCoverPhoto);
        removePopupBorder(mnuItemAddCoverPhoto);
        seperatorAddCoverPhoto = getCustomSeperator();
        mnuCoverPhoto.add(seperatorAddCoverPhoto);

        mnuItemChangeCoverPhoto = getMenuItem(ProfilePanelConstants.CHANGE_WALL_PHOTO, GetImages.EDIT_PHOTO);
        mnuCoverPhoto.add(mnuItemChangeCoverPhoto);
        seperatorChangeCoverPhoto = getCustomSeperator();
        mnuCoverPhoto.add(seperatorChangeCoverPhoto);

        mnuItemDeleteCoverPhoto = getMenuItem(ProfilePanelConstants.DELETE_WALL_PHOTO, GetImages.DELETE_PHOTO);
        mnuCoverPhoto.add(mnuItemDeleteCoverPhoto);

        setPopUpMenuItemVisibilityMyProfile();
    }

    private JMenu getMenu(String title) {
        JMenu jMenu = new JMenu(title);
        jMenu.setPreferredSize(new Dimension(180, 35));
        jMenu.setBorder(null);
        jMenu.setOpaque(true);
        jMenu.setBorder(null);
        return jMenu;
    }

    private JMenuItem getMenuItem(String title, String imageUrl) {
        JMenuItem jMenuItem = new JMenuItem(title);
        jMenuItem.setIcon(DesignClasses.return_image(imageUrl));
        jMenuItem.setPreferredSize(new Dimension(180, 35));
        jMenuItem.setBorder(null);
        jMenuItem.setOpaque(true);
        jMenuItem.addActionListener(actionListener);
        return jMenuItem;
    }

    public JPanel getSeperator() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); //7
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(10, 35));

        JLabel seperator = new JLabel(seperatorImg);
        seperator.setOpaque(false);
        wrapper.add(seperator);
        return wrapper;
    }

    public JPanel getCustomSeperator() {
        JPanel seperator = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        seperator.setBackground(new Color(0xe5e6e9));
        seperator.setPreferredSize(new Dimension(1, 1));
        return seperator;
    }

    private void removePopupBorder(JMenuItem menuItem) {
        JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
        popupMenu.setBorder(null);
        popupMenu.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0xd5d5d5)));
        popupMenu.setBackground(Color.WHITE);

    }

    public void setPopUpMenuItemVisibilityMyProfile() {
        //    privacyPicture.setVisible(true);
        if ((MyFnFSettings.userProfile.getProfileImage() != null && MyFnFSettings.userProfile.getProfileImage().trim().length() > 0)) {
            mnuItemAddProfilePicture.setVisible(false);
            seperatorAddProfilePhoto.setVisible(false);
            mnuItemChangeProfilePicture.setVisible(true);
            seperatorChangeProfilePhoto.setVisible(true);
            mnuItemDeleteProfilePicture.setVisible(true);

        } else {
            mnuItemAddProfilePicture.setVisible(true);
            seperatorAddProfilePhoto.setVisible(true);
            mnuItemChangeProfilePicture.setVisible(false);
            seperatorChangeProfilePhoto.setVisible(false);
            mnuItemDeleteProfilePicture.setVisible(false);

        }

        if ((MyFnFSettings.userProfile.getCoverImage() != null && MyFnFSettings.userProfile.getCoverImage().trim().length() > 0)) {
            mnuItemAddCoverPhoto.setVisible(false);
            seperatorAddCoverPhoto.setVisible(false);
            mnuItemChangeCoverPhoto.setVisible(true);
            seperatorChangeCoverPhoto.setVisible(true);
            mnuItemDeleteCoverPhoto.setVisible(true);

        } else {
            mnuItemAddCoverPhoto.setVisible(true);
            seperatorAddCoverPhoto.setVisible(true);
            mnuItemChangeCoverPhoto.setVisible(false);
            seperatorChangeCoverPhoto.setVisible(false);
            mnuItemDeleteCoverPhoto.setVisible(false);
//            mnuPublicWallPhoto.setVisible(false);
//            mnuPrivateWallPhoto.setVisible(false);
        }
//        if ((MyFnFSettings.userProfile.getProfileImage() == null || MyFnFSettings.userProfile.getProfileImage().trim().length() <= 0)
//                && (MyFnFSettings.userProfile.getCoverImage() == null || MyFnFSettings.userProfile.getCoverImage().trim().length() <= 0)) {
//            privacyPicture.setVisible(false);
//        }
    }
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == mnuItemChangeProfilePicture || e.getSource() == mnuItemAddProfilePicture) {
                GuiRingID.getInstance().showProfileImageChange(MyFnFSettings.PROFILE_IMAGE);
            } else if (e.getSource() == mnuItemDeleteProfilePicture) {
                //     int dialogResult = JOptionPane.showConfirmDialog(null, NotificationMessages.REMOVE_NOTIFICAITON, "Profile Picture", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                //     if (dialogResult == JOptionPane.YES_OPTION) {
                HelperMethods.showConfirmationDialogMessage(NotificationMessages.REMOVE_NOTIFICAITON);
                if (JOptionPanelBasics.YES_NO) {
                    new ProfileCoverImageRemove(MyFnFSettings.PROFILE_IMAGE).start();
                }
            } else if (e.getSource() == mnuItemChangeCoverPhoto || e.getSource() == mnuItemAddCoverPhoto) {
                GuiRingID.getInstance().showProfileImageChange(MyFnFSettings.COVER_IMAGE);
            } else if (e.getSource() == mnuItemDeleteCoverPhoto) {
                //     int dialogResult = JOptionPane.showConfirmDialog(null, NotificationMessages.REMOVE_NOTIFICAITON, "Profile Picture", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                //     if (dialogResult == JOptionPane.YES_OPTION) {
                HelperMethods.showConfirmationDialogMessage(NotificationMessages.REMOVE_NOTIFICAITON);
                if (JOptionPanelBasics.YES_NO) {
                    new ProfileCoverImageRemove(MyFnFSettings.COVER_IMAGE).start();

                }
            } /*else if (e.getSource() == mnuPublicProfilePicture) {
             new ChangePrivacy(AppConstants.CONS_PROFILE_IMAGE, AppConstants.PRIVACY_SHORT_PUBLIC).start();
             } else if (e.getSource() == mnuPrivateProfilePicture) {
             new ChangePrivacy(AppConstants.CONS_PROFILE_IMAGE, AppConstants.PRIVACY_SHORT_ONLY_ME).start();
             } else if (e.getSource() == mnuPublicWallPhoto) {
             new ChangePrivacy(AppConstants.CONS_COVER_IMAGE, AppConstants.PRIVACY_SHORT_PUBLIC).start();
             } else if (e.getSource() == mnuPrivateWallPhoto) {
             new ChangePrivacy(AppConstants.CONS_COVER_IMAGE, AppConstants.PRIVACY_SHORT_ONLY_ME).start();
             } else if (e.getSource() == aboutButton) {
             setType(TYPE_ABOUT);
             onTabClick();
             } else if (e.getSource() == bookButton) {
             setType(TYPE_MY_BOOK);
             onTabClick();
             } else if (e.getSource() == albumButton) {
             setType(TYPE_ALBUM);
             onTabClick();
             }*/

        }
    };
}
