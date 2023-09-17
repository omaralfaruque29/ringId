/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.image;

import com.ipvision.view.utility.ImageMousListenerInBook;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.ipvision.service.utility.Scalr;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.model.JsonFields;
import com.ipvision.view.image.FeedImageInfo;
import com.ipvision.view.image.FeedImageViewPanel;

/**
 *
 * @author Faiz
 */
public class LoadImageInPanel extends Thread {

    JPanel stackPane;
    int numberOfImages = 3;
    ArrayList<JsonFields> imageList;
    int type;
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LoadImageInPanel.class);
    NewsFeedWithMultipleImage statusDto;
    /*
     "imageList": [{
     "imgId": 8471,
     "iurl": "http://38.127.68.50/imageServer/clients_image//profileimages/1414980835813.png",
     "albId": "profileimages",
     "albn": "ProfileImages",
     "ih": 168,
     "iw": 300,
     "imT": 2,
     "tm": 1415002267810,
     "nl": 0,
     "il": 0,
     "ic": 0,
     "nc": 0
     }]
     */

    public LoadImageInPanel(NewsFeedWithMultipleImage statusDto, JPanel stackPane, ArrayList<JsonFields> imageList) {
        this.stackPane = stackPane;
        this.imageList = imageList;
        this.statusDto = statusDto;
        if (imageList.size() < 3) {
            numberOfImages = imageList.size();
        }
        setName(this.getClass().getSimpleName());
    }
    int maxWidth = DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH;
    int maxHeight = DefaultSettings.MAIN_RIGHT_CONTENT_HEIGHT;

    @Override
    public void run() {
        if (GuiRingID.getInstance() == null) {
            log.error("GuiRingID is null");
            return;
        }

        stackPane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel loadLabel = new JLabel();
        try {
            try {
                BufferedImage load = DesignClasses.return_buffer_image(GetImages.PLEASE_WAIT_MINI);
                loadLabel.setIcon(new ImageIcon(load));
                this.stackPane.add(loadLabel);
                this.stackPane.revalidate();
                load.flush();
                load = null;
            } catch (Exception ex) {
            }

            List<FeedImageInfo> images = new ArrayList<FeedImageInfo>();
            for (int i = 0; i < numberOfImages; i++) {
                JsonFields fields = imageList.get(i);

                BufferedImage bfImg = ImageHelpers.getBookBuffedImageFromUrl(ImageHelpers.get600Url(fields.getIurl()), fields.getImT());
                if (bfImg == null) {
                    bfImg = ImageHelpers.getBookBuffedImageFromUrl(fields.getIurl(), fields.getImT());
                }

                if (bfImg != null) {
                    FeedImageInfo first = new FeedImageInfo();
                    first.setWidth(bfImg.getWidth());
                    first.setHeight(bfImg.getHeight());
                    first.setImageId(fields.getImageId());
                    first.setIurl(fields.getIurl());
                    first.setBufferedImage(bfImg);
                    first.setIndex(i);
                    images.add(first);
                }
            }

            if (images.size() > 0) {
                FeedImageViewPanel feedImageViewPanel = new FeedImageViewPanel(images);
                FeedImageInfo[] feedImageInfos = feedImageViewPanel.imageArray;

                for (FeedImageInfo feedImageInfo : feedImageInfos) {
                    BufferedImage bfImg = null;
                    try {
                        if (feedImageInfo.getFitMode() != null) {
                            bfImg = Scalr.resize(feedImageInfo.getBufferedImage(), feedImageInfo.getFitMode(), feedImageInfo.getReWidth(), feedImageInfo.getReHeight(), Scalr.OP_ANTIALIAS);
                        } else {
                            bfImg = feedImageInfo.getBufferedImage();
                        }
                        if (bfImg != null) {
                            feedImageInfo.getImageLabel().setOpaque(false);
                            feedImageInfo.getImageLabel().setIcon(new ImageIcon(bfImg));
                            feedImageInfo.getImageLabel().setCursor(new Cursor(Cursor.HAND_CURSOR));
                            addMouseListener(feedImageInfo.getImageLabel(), this.statusDto.getNfId(), feedImageInfo.getIndex());
                        }
                    } catch (Exception ex) {
                        // ex.printStackTrace();
                        log.error("Error in here ==>" + ex.getMessage());
                    } finally {
                        if (bfImg != null) {
                            bfImg.flush();
                            bfImg = null;
                        }
                    }
                }

                stackPane.removeAll();
                stackPane.add(feedImageViewPanel);
                stackPane.revalidate();
            } else {
                addNoImage();
            }

        } catch (Exception e) {
            log.error("LoadImageINPanel==>" + e.getMessage());
        } finally {
            loadLabel.setIcon(null);
            loadLabel = null;
            this.stackPane.revalidate();
            Runtime.getRuntime().gc();
        }

    }

    private void addNoImage() {
        this.stackPane.removeAll();
        BufferedImage loadNotFound = DesignClasses.return_buffer_image(GetImages.NO_IMAGE_FOUND_200x200);
        JLabel noImage = new JLabel(new ImageIcon(loadNotFound));
        loadNotFound.flush();
        loadNotFound = null;
        this.stackPane.add(noImage, BorderLayout.WEST);
        noImage = null;
    }

    private void addMouseListener(JLabel label, long nfid, int selectedIndex) {
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new ImageMousListenerInBook(nfid, selectedIndex));
    }
}
