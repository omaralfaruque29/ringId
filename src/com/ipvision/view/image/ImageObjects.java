/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

import com.ipvision.constants.GetImages;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.view.utility.call.CallUIHelpers;
import javax.swing.ImageIcon;

/**
 *
 * @author Faiz
 */
public class ImageObjects {

    public static ImageIcon voiceCallImage;
    public static ImageIcon voiceCallImage_h;
    public static ImageIcon vedioCallImage;
    public static ImageIcon vedioCallImage_h;
    public static ImageIcon chatImage;
    public static ImageIcon chatImage_h;
    public static ImageIcon friend_call_chat_access;
    public static ImageIcon friend_call_chat_selected;
    public static ImageIcon friend_full_access_default;
    public static ImageIcon friend_full_access_selected;
    public static ImageIcon addIcon;
    public static ImageIcon incomingIcon;
    public static ImageIcon outgoingIcon;
    public static ImageIcon unknown_70;
    public static ImageIcon dotIcon;
    public static ImageIcon dotHIcon;
    public static ImageIcon unknown_39;
    public static ImageIcon unknown_35;
    public static ImageIcon unknown_25;
    public static ImageIcon addMoreIcon;
    public static ImageIcon editIcon;
    public static ImageIcon okIcon;
    public static ImageIcon closeIcon;
    public static ImageIcon addMoreHIcon;
    public static ImageIcon editHIcon;
    public static ImageIcon okHIcon;
    public static ImageIcon closeHIcon;
    public static ImageIcon publicIcon;
    public static ImageIcon onlyfriendsIcon;
    public static ImageIcon onlyMeIcon;

    public static void initImageObjects() {
        if (voiceCallImage == null) {
            voiceCallImage = DesignClasses.return_image(CallUIHelpers.SIP_CALL);
        }
        if (voiceCallImage_h == null) {
            voiceCallImage_h = DesignClasses.return_image(CallUIHelpers.SIP_CALL_H);
        }
        if (vedioCallImage == null) {
            vedioCallImage = DesignClasses.return_image(CallUIHelpers.VIDEO_CALL);
        }
        if (vedioCallImage_h == null) {
            vedioCallImage_h = DesignClasses.return_image(CallUIHelpers.VIDEO_CALL_H);
        }
        if (chatImage == null) {
            chatImage = DesignClasses.return_image(CallUIHelpers.CHAT_MINI);
        }
        if (chatImage_h == null) {
            chatImage_h = DesignClasses.return_image(CallUIHelpers.CHAT_MINI_H);
        }
        if (friend_call_chat_access == null) {
            friend_call_chat_access = DesignClasses.return_image(GetImages.FRIEND_CALL_CHAT_ACCESS_DEFAULT);
        }
        if (friend_call_chat_selected == null) {
            friend_call_chat_selected = DesignClasses.return_image(GetImages.FRIEND_CALL_CHAT_ACCESS_SELECTED);
        }
        if (friend_full_access_default == null) {
            friend_full_access_default = DesignClasses.return_image(GetImages.FRIEND_FULL_ACCESS_DEFAULT);
        }
        if (friend_full_access_selected == null) {
            friend_full_access_selected = DesignClasses.return_image(GetImages.FRIEND_FULL_ACCESS_SELECTED);
        }
        if (addIcon == null) {
            addIcon = DesignClasses.return_image(GetImages.ADD_FRIEND_CONTACT);
        }
        if (incomingIcon == null) {
            incomingIcon = DesignClasses.return_image(GetImages.FRIEND_INCOMING);
        }
        if (outgoingIcon == null) {
            outgoingIcon = DesignClasses.return_image(GetImages.FRIEND_OUTGOING);
        }
        if (unknown_70 == null) {
            unknown_70 = DesignClasses.return_image(GetImages.UNKNOW_IMAGE_70);
        }
        if (dotIcon == null) {
            dotIcon = DesignClasses.return_image(GetImages.DOT_ABOUT);
        }
        if (dotHIcon == null) {
            dotHIcon = DesignClasses.return_image(GetImages.DOT_ABOUT_H);
        }
        if (unknown_39 == null) {
            unknown_39 = DesignClasses.return_image(GetImages.UNKNOW_IMAGE_39);
        }
        if (unknown_35 == null) {
            unknown_35 = DesignClasses.return_image(GetImages.UNKNOW_IMAGE_35);
        }
        if (unknown_25 == null) {
            unknown_25 = DesignClasses.return_image(GetImages.UNKNOW_IMAGE_25);
        }
        if (addMoreIcon == null) {
            addMoreIcon = DesignClasses.return_image(GetImages.ADD_MORE);
        }
        if (editIcon == null) {
            editIcon = DesignClasses.return_image(GetImages.PEN);
        }
        if (okIcon == null) {
            okIcon = DesignClasses.return_image(GetImages.OK_MINI);
        }
        if (closeIcon == null) {
            closeIcon = DesignClasses.return_image(GetImages.BUTTON_CLOSE_MINI);
        }
        if (addMoreHIcon == null) {
            addMoreHIcon = DesignClasses.return_image(GetImages.ADD_MORE_H);
        }
        if (editHIcon == null) {
            editHIcon = DesignClasses.return_image(GetImages.PEN_H);
        }
        if (okHIcon == null) {
            okHIcon = DesignClasses.return_image(GetImages.OK_MINI_H);
        }
        if (closeHIcon == null) {
            closeHIcon = DesignClasses.return_image(GetImages.BUTTON_CLOSE_MINI_H);
        }
        if (publicIcon == null) {
            publicIcon = DesignClasses.return_image(GetImages._PUBLIC);
        }
        if (onlyfriendsIcon == null) {
            onlyfriendsIcon = DesignClasses.return_image(GetImages._ONLY_FRIEND);
        }
        if (onlyMeIcon == null) {
            onlyMeIcon = DesignClasses.return_image(GetImages._ONLY_ME);
        }
    }

    public static void reset() {
        voiceCallImage = null;
        voiceCallImage_h = null;
        vedioCallImage = null;
        vedioCallImage_h = null;
        chatImage = null;
        chatImage_h = null;
        friend_call_chat_access = null;
        friend_call_chat_selected = null;
        friend_full_access_default = null;
        friend_full_access_selected = null;
        addIcon = null;
        incomingIcon = null;
        outgoingIcon = null;
        unknown_70 = null;
        dotIcon = null;
        dotHIcon = null;
        unknown_39 = null;
        unknown_35 = null;
        unknown_25 = null;
        addMoreIcon = null;
        editIcon = null;
        okIcon = null;
        closeIcon = null;
        addMoreHIcon = null;
        editHIcon = null;
        okHIcon = null;
        closeHIcon = null;
        publicIcon = null;
        onlyfriendsIcon = null;
        onlyMeIcon = null;
    }

}
