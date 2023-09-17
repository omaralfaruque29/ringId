/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.SettingsConstants;
import java.awt.Cursor;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.ipvision.service.utility.Scalr;
import com.ipvision.model.JsonFields;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.view.utility.ImageMousListenerInBook;

/**
 *
 * @author Faiz Ahmed
 */
public class LoadCoverImageInLabel extends Thread {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LoadCoverImageInLabel.class);
    ImagePaneForCoverImage coverImagePanel;
    JPanel loadingPanel;
    JPanel friendImageWrapper;
    BufferedImage originalBufferImage;
    JLabel loadingLabel = new JLabel();
    JsonFields imageDto;
    int cropX;
    int cropY;
    ImageMousListenerInBook listener;
    int type_int;

    public LoadCoverImageInLabel(JPanel friendImageWrapper, ImagePaneForCoverImage coverImagePanel, JPanel loadingPanel, JsonFields imageDto, int cropX, int cropY, ImageMousListenerInBook listener) {
        this.friendImageWrapper = friendImageWrapper;
        this.coverImagePanel = coverImagePanel;
        this.loadingPanel = loadingPanel;
        this.imageDto = imageDto;
        this.cropX = cropX;
        this.cropY = cropY;
        type_int = 1;
        this.listener = listener;
    }

    public LoadCoverImageInLabel(int type_int, JPanel friendImageWrapper, ImagePaneForCoverImage coverImagePanel, JPanel loadingPanel, JsonFields imageDto, int cropX, int cropY, ImageMousListenerInBook listener) {
        this.friendImageWrapper = friendImageWrapper;
        this.coverImagePanel = coverImagePanel;
        this.loadingPanel = loadingPanel;
        this.imageDto = imageDto;
        this.cropX = cropX;
        this.cropY = cropY;
        this.type_int = type_int;
        this.listener = listener;
    }

    @Override
    public void run() {

        try {
            loadingPanel.removeAll();
            loadingLabel.setOpaque(false);
            loadingLabel.setIcon(DesignClasses.return_image(GetImages.PLEASE_WAIT_60));
            loadingPanel.add(loadingLabel);
            friendImageWrapper.revalidate();

            originalBufferImage = ImageHelpers.getBookBuffedImageFromUrl(imageDto.getIurl(), SettingsConstants.TYPE_COVER_IMAGE);
            if (originalBufferImage != null) {
                loadingPanel.remove(loadingLabel);
                setCoverImage();
                if (type_int == 1) {
                    coverImagePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    coverImagePanel.addMouseListener(listener);
                }
                coverImagePanel.revalidate();
                coverImagePanel.repaint();

            } else {
                //loadDefaultImage();
                loadingPanel.remove(loadingLabel);
                coverImagePanel.revalidate();
                coverImagePanel.repaint();

            }
            friendImageWrapper.revalidate();
        } catch (Exception ex) {
            //loadDefaultImage();
            loadingPanel.remove(loadingLabel);
            coverImagePanel.revalidate();
            coverImagePanel.repaint();
             friendImageWrapper.revalidate();
            log.error("Error while downloading image from ==>" + imageDto.getIurl());
        }
    }

    private void loadDefaultImage() {
        try {
            coverImagePanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            loadingPanel.remove(loadingLabel);
            BufferedImage img = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.DEFAULT_COVER_IMAGE));
            coverImagePanel.setImage(img, 0, 0);
            //   coverImagePanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            coverImagePanel.repaint();
            coverImagePanel.revalidate();
            coverImagePanel.removeMouseListener(listener);
        } catch (Exception ex) {
        }
    }

    private void setCoverImage() {
        cropX = cropX < 0 ? -cropX : cropX;
        cropY = cropY < 0 ? -cropY : cropY;
        log.warn("Original : Url = " + imageDto.getIurl() + ", Width = " + originalBufferImage.getWidth() + ", Height = " + originalBufferImage.getHeight() + ", cropX = " + cropX + ", cropY = " + cropY);
        if (originalBufferImage.getWidth() < DefaultSettings.COVER_PIC_DISPLAY_WIDTH || originalBufferImage.getHeight() < DefaultSettings.COVER_PIC_DISPLAY_HEIGHT) {
            int iWidth = originalBufferImage.getWidth();
            int iHeight = originalBufferImage.getHeight();

            Scalr.Mode mode = Scalr.Mode.FIT_TO_WIDTH;
            if (originalBufferImage.getWidth() < DefaultSettings.COVER_PIC_DISPLAY_WIDTH && originalBufferImage.getHeight() < DefaultSettings.COVER_PIC_DISPLAY_HEIGHT) {
                if (((float) iWidth / (float) iHeight) < ((float) DefaultSettings.COVER_PIC_DISPLAY_WIDTH / (float) DefaultSettings.COVER_PIC_DISPLAY_HEIGHT)) {
                    mode = Scalr.Mode.FIT_TO_WIDTH;
                } else {
                    mode = Scalr.Mode.FIT_TO_HEIGHT;
                }
            } else if (originalBufferImage.getWidth() < DefaultSettings.COVER_PIC_DISPLAY_WIDTH) {
                mode = Scalr.Mode.FIT_TO_WIDTH;
            } else if (originalBufferImage.getHeight() < DefaultSettings.COVER_PIC_DISPLAY_HEIGHT) {
                mode = Scalr.Mode.FIT_TO_HEIGHT;
            }

            originalBufferImage = Scalr.resize(originalBufferImage, mode, DefaultSettings.COVER_PIC_DISPLAY_WIDTH, DefaultSettings.COVER_PIC_DISPLAY_HEIGHT, Scalr.OP_ANTIALIAS);

            cropX = (int) (cropX * ((float) originalBufferImage.getWidth() / (float) iWidth));
            cropY = (int) (cropY * ((float) originalBufferImage.getHeight() / (float) iHeight));

            int overflowX = (originalBufferImage.getWidth() - cropX);
            if (overflowX < DefaultSettings.COVER_PIC_DISPLAY_WIDTH) {
                cropX = cropX - (DefaultSettings.COVER_PIC_DISPLAY_WIDTH - overflowX);
            }

            int overflowY = (originalBufferImage.getHeight() - cropY);
            if (overflowY < DefaultSettings.COVER_PIC_DISPLAY_HEIGHT) {
                cropY = cropY - (DefaultSettings.COVER_PIC_DISPLAY_HEIGHT - overflowY);
            }
            log.warn("Calculated : Url = " + imageDto.getIurl() + ", Width = " + originalBufferImage.getWidth() + ", Height = " + originalBufferImage.getHeight() + ", cropX = " + cropX + ", cropY = " + cropY);
        }
        coverImagePanel.setImage(originalBufferImage, cropX, cropY);
    }

}
