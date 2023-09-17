/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.Cursor;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import com.ipvision.view.utility.ImageHelpers;

/**
 *
 * @author Faiz
 */
public class LoadImageInLabel extends Thread {

    String imageUrl;
    JLabel imagePane;
    int imageType;
    int width;
    int height;
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LoadImageInLabel.class);

    public LoadImageInLabel(JLabel image_pane, String image_url, int image_type, int widht, int height) {
        this.imagePane = image_pane;
        this.imageUrl = image_url;
        this.imageType = image_type;
        this.width = widht;
        this.height = height;
    }

    @Override
    public void run() {
        try {
            imagePane.setIcon(DesignClasses.return_image(GetImages.PLEASE_WAIT_MINI));
            BufferedImage img = ImageHelpers.getBookBuffedImageFromUrl(imageUrl, this.imageType);
            if (img != null) {
                ImageHelpers.setImageInLabelFromBufferedImage(imagePane, width, height, img);
                img.flush();
                imagePane.setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else {
                imagePane.setIcon(DesignClasses.return_image(GetImages.NO_IMAGE_FOUND_200x200));
                imagePane.revalidate();
                imagePane.repaint();
                log.error("Images not found LoadImageInLabel==>" + imageUrl);
            }
            imagePane.revalidate();

            Runtime.getRuntime().gc();
        } catch (Exception ex) {
            imagePane.setIcon(null);
            imagePane.revalidate();
            log.error("Error while downloading image from ==>" + imageUrl);
        }
    }
}
