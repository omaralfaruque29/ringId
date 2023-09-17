/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.service.utility.HelperMethods;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.JDialogWebcam;
import com.ipvision.main.Main;
import com.ipvision.model.JsonFields;
import com.ipvision.view.myprofile.ProfileCoverChangePanel;

/**
 *
 * @author Dell
 */
public class OptionActionPopup extends JPopupMenu implements ActionListener {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(OptionActionPopup.class);

    public JMenuItem profile_picture;
    public JMenuItem cover_picture;
    private JsonFields imagedto;
    private BufferedImage mainImage;

    public OptionActionPopup(JsonFields imagedto) {
        this.imagedto = imagedto;
        profile_picture = new JMenuItem(" Make Profile Picture ");
        profile_picture.addActionListener(this);
        cover_picture = new JMenuItem(" Make Cover Picture ");
        cover_picture.addActionListener(this);

        add(profile_picture);
        add(cover_picture);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        if (SingleImgeDetails.getSingleImgeDetails() != null) {
            SingleImgeDetails.getSingleImgeDetails().hideThis();
        } else if (SingleImageDetailsForNotifications.getSingleImgeDetails() != null) {
            SingleImageDetailsForNotifications.getSingleImgeDetails().hideThis();
        } else if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null) {
            SingleImageDetailsProfileCover.getSingleImgeDetails().hideThis();
        }

        if (e.getSource() == profile_picture) {
            mainImage = ImageHelpers.getBookBuffedImageFromUrl(imagedto.getIurl(), imagedto.getImT());
            if (mainImage != null && mainImage.getWidth() >= DefaultSettings.PROFILE_PIC_UPLOAD_SCREEN_WIDTH && mainImage.getHeight() >= DefaultSettings.PROFILE_PIC_UPLOAD_SCREEN_HEIGHT) {
                File file = new File(JDialogWebcam.TAKEN_PICTURE_NAME);
                try {
                    ImageIO.write(mainImage, "PNG", file);
                } catch (IOException ex) {
                    log.error("Exception in here ==> "+ex.getMessage());
                    //ex.printStackTrace();
                }

                GuiRingID.getInstance().getMainRight().showProfileImageChange(MyFnFSettings.PROFILE_IMAGE);
                ProfileCoverChangePanel.setFileToUpload(new File(JDialogWebcam.TAKEN_PICTURE_NAME));
                ProfileCoverChangePanel.getInstance().showPictureInView();
            } else {
                HelperMethods.showPlaneDialogMessage("Profile photo's minimum resulotion: " + DefaultSettings.PROFILE_PIC_UPLOAD_SCREEN_WIDTH + " x " + DefaultSettings.PROFILE_PIC_UPLOAD_SCREEN_HEIGHT + "");
            }
        } else if (e.getSource() == cover_picture) {
            mainImage = ImageHelpers.getBookBuffedImageFromUrl(imagedto.getIurl(), imagedto.getImT());
            if (mainImage != null && mainImage.getWidth() >= DefaultSettings.COVER_PIC_UPLOAD_SCREEN_WIDTH && mainImage.getHeight() >= DefaultSettings.COVER_PIC_UPLOAD_SCREEN_HEIGHT) {
                File file = new File(JDialogWebcam.TAKEN_PICTURE_NAME);
                try {
                    ImageIO.write(mainImage, "PNG", file);
                } catch (IOException ex) {
                    log.error("Exception in here ==> "+ex.getMessage());
                }

                GuiRingID.getInstance().getMainRight().showProfileImageChange(MyFnFSettings.COVER_IMAGE);
                ProfileCoverChangePanel.setFileToUpload(new File(JDialogWebcam.TAKEN_PICTURE_NAME));
                ProfileCoverChangePanel.getInstance().showPictureInView();
            } else {
                HelperMethods.showPlaneDialogMessage("Cover photo's minimum resulotion: " + DefaultSettings.COVER_PIC_UPLOAD_SCREEN_WIDTH + " x " + DefaultSettings.COVER_PIC_UPLOAD_SCREEN_HEIGHT + "");
            }
        }

    }

}
