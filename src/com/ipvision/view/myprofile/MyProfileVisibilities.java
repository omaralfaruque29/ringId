/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.AppConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.ChangePrivacy;

/**
 *
 * @author Faiz Ahmed
 */
public class MyProfileVisibilities extends MyProfileVisibilitiesPopUpBasics implements ActionListener {

//    public JPopupMenu visibilityPopup;
//    private JMenuItem _public;
//    private JMenuItem onlyFriend;
//    private JMenuItem onlyMe;
//    private MenuHelpers menuHelp;
    private int cons;

    public MyProfileVisibilities(int fieldsConstants) {

//        menuHelp = new MenuHelpers(this);
//        visibilityPopup = new JPopupMenu();
//        visibilityPopup.setBackground(Color.WHITE);
        this.cons = fieldsConstants;
        _public.addActionListener(this);
        onlyFriend.addActionListener(this);
        onlyMe.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == _public) {
            if (cons == AppConstants.CONS_EMAIL) {
                if (GuiRingID.getInstance() != null && MyFnFSettings.userProfile != null) {
                    new ChangePrivacy(AppConstants.CONS_EMAIL, AppConstants.PRIVACY_SHORT_PUBLIC).start();
                }
            } else if (cons == AppConstants.CONS_MOBILE_PHONE) {
                if (GuiRingID.getInstance() != null && MyFnFSettings.userProfile != null) {
                    new ChangePrivacy(AppConstants.CONS_MOBILE_PHONE, AppConstants.PRIVACY_SHORT_PUBLIC).start();
                }
            } else if (cons == AppConstants.CONS_PROFILE_IMAGE) {
                if (GuiRingID.getInstance() != null && MyFnFSettings.userProfile != null) {
                    new ChangePrivacy(AppConstants.CONS_PROFILE_IMAGE, AppConstants.PRIVACY_SHORT_PUBLIC).start();
                }
            } else if (cons == AppConstants.CONS_BIRTH_DAY) {
                new ChangePrivacy(AppConstants.CONS_BIRTH_DAY, AppConstants.PRIVACY_SHORT_PUBLIC).start();
            }
        } else if (e.getSource() == onlyFriend) {
            if (cons == AppConstants.CONS_EMAIL) {
                if (GuiRingID.getInstance() != null && MyFnFSettings.userProfile != null) {
                    new ChangePrivacy(AppConstants.CONS_EMAIL, AppConstants.PRIVACY_SHORT_ONLY_FRIEND).start();
                }
            } else if (cons == AppConstants.CONS_MOBILE_PHONE) {
                if (GuiRingID.getInstance() != null && MyFnFSettings.userProfile != null) {
                    new ChangePrivacy(AppConstants.CONS_MOBILE_PHONE, AppConstants.PRIVACY_SHORT_ONLY_FRIEND).start();
                }
            } else if (cons == AppConstants.CONS_PROFILE_IMAGE) {
                if (GuiRingID.getInstance() != null && MyFnFSettings.userProfile != null) {
                    new ChangePrivacy(AppConstants.CONS_PROFILE_IMAGE, AppConstants.PRIVACY_SHORT_ONLY_FRIEND).start();
                }
            } else if (cons == AppConstants.CONS_BIRTH_DAY) {
                new ChangePrivacy(AppConstants.CONS_BIRTH_DAY, AppConstants.PRIVACY_SHORT_ONLY_FRIEND).start();
            }
        } else if (e.getSource() == onlyMe) {
            if (cons == AppConstants.CONS_EMAIL) {
                if (GuiRingID.getInstance() != null && MyFnFSettings.userProfile != null) {
                    new ChangePrivacy(AppConstants.CONS_EMAIL, AppConstants.PRIVACY_SHORT_ONLY_ME).start();
                }
            } else if (cons == AppConstants.CONS_MOBILE_PHONE) {
                if (GuiRingID.getInstance() != null && MyFnFSettings.userProfile != null) {
                    new ChangePrivacy(AppConstants.CONS_MOBILE_PHONE, AppConstants.PRIVACY_SHORT_ONLY_ME).start();
                }
            } else if (cons == AppConstants.CONS_PROFILE_IMAGE) {
                if (GuiRingID.getInstance() != null && MyFnFSettings.userProfile != null) {
                    new ChangePrivacy(AppConstants.CONS_PROFILE_IMAGE, AppConstants.PRIVACY_SHORT_ONLY_ME).start();
                }
            } else if (cons == AppConstants.CONS_BIRTH_DAY) {
                new ChangePrivacy(AppConstants.CONS_BIRTH_DAY, AppConstants.PRIVACY_SHORT_ONLY_ME).start();
            }
        }
    }
}
