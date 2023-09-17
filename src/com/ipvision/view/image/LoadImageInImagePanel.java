/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.Cursor;
import java.awt.image.BufferedImage;
import com.ipvision.service.utility.Scalr;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.model.JsonFields;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.view.utility.ImagePane;

/**
 *
 * @author Faiz Ahmed
 */
public class LoadImageInImagePanel extends Thread {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LoadImageInImagePanel.class);
    ImagePane label;
    String url;
    int maxWidth;
    int maxHeight;
    private boolean viewfullimage = true;
    private NewsFeedWithMultipleImage statusDto;
    private JsonFields imageDto;
    int imageType;

    public LoadImageInImagePanel(ImagePane label, String url, int maxWidth, int maxheight, NewsFeedWithMultipleImage statusDto, JsonFields imageDto, boolean viewfullimage, int imageType) {
        this.label = label;
        this.url = url;
        this.maxWidth = maxWidth;
        this.maxHeight = maxheight;
        this.statusDto = statusDto;
        this.imageDto = imageDto;
        this.viewfullimage = viewfullimage;
        this.imageType = imageType;
    }

    public LoadImageInImagePanel(ImagePane label, String url, int maxWidth, int maxheight, boolean viewfullimage, int imageType) {
        this.label = label;
        this.url = url;
        this.maxWidth = maxWidth;
        this.maxHeight = maxheight;
        this.viewfullimage = viewfullimage;
        this.imageType = imageType;
    }

    @Override
    public void run() {
        try {
            //   label.setIcon(DesignClasses.return_image(GetImages.PLEASE_WAIT_MINI));
            try {
                BufferedImage load = DesignClasses.return_buffer_image(GetImages.PLEASE_WAIT_MINI);
                label.setImage(load);
                load.flush();
                load = null;

            } catch (Exception ex) {
            }
            BufferedImage img = ImageHelpers.getBookBuffedImageFromUrl(url, this.imageType);
            if (img != null) {

                if (img.getWidth() < maxWidth && img.getHeight() < maxHeight) {
                    img = Scalr.resize(img, Scalr.Mode.AUTOMATIC, img.getWidth(), img.getHeight(), Scalr.OP_ANTIALIAS);
                } else {
                    if (img.getWidth() > img.getHeight()) {
                        if (img.getWidth() > maxWidth) {
                            img = Scalr.resize(img, Scalr.Mode.FIT_TO_WIDTH, maxWidth, maxHeight, Scalr.OP_ANTIALIAS);
                        }
                    } else if (img.getHeight() > img.getWidth()) {
                        if (img.getHeight() > maxHeight) {
                            img = Scalr.resize(img, Scalr.Mode.FIT_TO_HEIGHT, maxWidth, maxHeight, Scalr.OP_ANTIALIAS);
                        }
                    } else {
                        img = Scalr.resize(img, Scalr.Mode.FIT_EXACT, maxWidth, maxHeight, Scalr.OP_ANTIALIAS);
                    }
                }
                //       HelperMethods.setImageInLabelFromBufferedImage(label, maxWidth, maxHeight, img);
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
                try {
                    label.setImage(img);
                    img.flush();
                    img = null;
                    label.revalidate();
                } catch (Exception ex) {
                }
                if (viewfullimage) {
                    //  label.addMouseListener(new ImageMousListener(label, img, statusDto, imageDto));
                }
            } else {
                // label.setIcon(DesignClasses.return_image(GetImages.NO_IMAGE_FOUND_200x200));
                try {
                    BufferedImage load = DesignClasses.return_buffer_image(GetImages.NO_IMAGE_FOUND_200x200);
                    label.setImage(load);
                    label.revalidate();
                    label.repaint();
                    load.flush();
                    load = null;
                } catch (Exception ex) {
                }
                log.error("Images not found LoadImageInLabelFromUrl==>" + url);
            }
            if (img != null) {
                img.flush();
                img = null;
            }
        } catch (Exception ex) {
            label.setImage(null);
            label.revalidate();
            log.error("Error while downloading image from ==>" + url);
        } finally {
            Runtime.getRuntime().gc();
        }
    }
}
