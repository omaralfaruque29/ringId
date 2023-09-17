/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.service.uploaddownload.ImageUploader;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author user
 */
public class SendImageUploadRequest extends Thread {

    ImageUploader img_up = new ImageUploader();
    String url;
    int imageType;
    int post;
    int cropX;
    int cropY;
    int cropW;
    int cropH;
    JButton btnSkip;
    BufferedImage image;

    public SendImageUploadRequest(String url, BufferedImage image, int imageType, int cropX, int cropY, int cropW, int cropH, int post) {
        this.url = url;
        this.image = image;
        this.imageType = imageType;
        this.cropX = cropX;
        this.cropY = cropY;
        this.cropW = cropW;
        this.cropH = cropH;
        this.post = post;
        setName(this.getClass().getSimpleName());
    }

    /*
     * ***************************************************************************
     * PROFILE IMAGE UPLOAD FROM LOGIN SCREEN
     * ***************************************************************************
     */
    public SendImageUploadRequest(String url, BufferedImage image, int imageType, int cropX, int cropY, int cropW, int cropH, int post, JButton btnSkip) {
        this.url = url;
        this.image = image;
        this.imageType = imageType;
        this.cropX = cropX;
        this.cropY = cropY;
        this.cropW = cropW;
        this.cropH = cropH;
        this.post = post;
        this.btnSkip = btnSkip;
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            if (imageType == MyFnFSettings.PROFILE_IMAGE) {
                img_up.uploader_method_profile_image(url, image, -cropX, -cropY, cropW, cropH, post, btnSkip);
            } else if (imageType == MyFnFSettings.COVER_IMAGE) {
                img_up.uploader_method_cover_image(url, image, -cropX, -cropY, post);
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}
