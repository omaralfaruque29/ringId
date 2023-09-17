/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.model.JsonFields;
import com.ipvision.service.utility.Scalr;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.view.utility.ImageMousListenerInBook;
import com.ipvision.view.utility.ImagePane;

/**
 *
 * @author Wasif Islam
 */
public class SingleImageInAlbum extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SingleImageInAlbum.class);
    private int imageWidth = 150;
    private int imageHeight = 150;
    private JsonFields singleImg;
    private NewsFeedWithMultipleImage albumDto;
    private ImagePane imagePane;

    public SingleImageInAlbum(NewsFeedWithMultipleImage albumDto, JsonFields singleImg) {
        setLayout(new BorderLayout(0, 2));
        setOpaque(false);
        this.albumDto = albumDto;
        this.singleImg = singleImg;
        setPreferredSize(new Dimension(imageWidth, imageHeight));
        setBorder(DefaultSettings.DEFAULT_BORDER);
        imagePane = new ImagePane();

        add(imagePane);

        addImageDetails(singleImg.getIurl(), singleImg.getImT() == null ? 0 : singleImg.getImT());
    }

    private void addImageDetails(String url, int imageType) {
        try {
            new LoadImage(imagePane, url, imageType).start();
        } catch (Exception e) {
        }
    }

    class LoadImage extends Thread {

        private String imageUrl;
        private ImagePane imagePane;
        private Integer imageType;
        private BufferedImage img;

        LoadImage(ImagePane imageLabel, String url, int imageType) {
            imagePane = imageLabel;
            this.imageUrl = url;
            this.imageType = imageType;
        }

        @Override
        public void run() {
            try {

                JPanel loadingPanel = new JPanel(new GridBagLayout());
                loadingPanel.setOpaque(false);

                JLabel imageLabel = new JLabel();
                imageLabel.setIcon(DesignClasses.return_image(GetImages.PLEASE_WAIT_MINI));
                loadingPanel.add(imageLabel);

                imagePane.add(loadingPanel, BorderLayout.CENTER);
                imageLabel.revalidate();

                //img = ImageHelpers.getBookBuffedImageFromUrl(imageUrl, this.imageType);
                if (imageUrl != null && imageUrl.length() > 0) {
                    img = ImageHelpers.getBookBuffedImageFromUrl(ImageHelpers.get300Url(imageUrl), imageType);
                    if (img == null) {
                        img = ImageHelpers.getBookBuffedImageFromUrl(imageUrl, imageType);
                    }
                }
                /*String image_url_with_base = ServerAndPortSettings.IMAGE_SERVER_BASE + imageUrl;
                 URL url = new URL(image_url_with_base);
                 URLConnection yc = url.openConnection();
                 InputStream stream = yc.getInputStream();
                 img = ImageIO.read(stream);*/

                if (img != null) {
                    addMouseListener(new ImageMousListenerInBook(singleImg, true));

                    if (img.getWidth() > img.getHeight()) {
                        if (img.getWidth() > imageWidth) {
                            img = Scalr.resize(img, Scalr.Mode.FIT_TO_WIDTH, imageWidth, imageHeight, Scalr.OP_ANTIALIAS);
                        }
                    } else if (img.getHeight() > img.getWidth()) {
                        if (img.getHeight() > imageHeight) {
                            img = Scalr.resize(img, Scalr.Mode.FIT_TO_HEIGHT, imageWidth, imageHeight, Scalr.OP_ANTIALIAS);
                        }
                    } else {
                        img = Scalr.resize(img, Scalr.Mode.FIT_EXACT, imageWidth, imageHeight, Scalr.OP_ANTIALIAS);
                    }

                    imagePane.remove(loadingPanel);
                    imagePane.setImage(img, imageWidth, imageHeight);
                    imagePane.revalidate();
                } else {// addNoImage 
                    BufferedImage loadNotFound = DesignClasses.return_buffer_image(GetImages.NO_IMAGE_FOUND_150x150);
                    imagePane.remove(loadingPanel);
                    imagePane.setImage(loadNotFound, imageWidth, imageHeight);
                    imagePane.revalidate();
                    loadNotFound.flush();
                    loadNotFound = null;

                }

            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Error in initContainers ==>" + e.getMessage());
                if (img != null) {
                    img.flush();
                    img = null;
                }
            } finally {
                if (img != null) {
                    img.flush();
                    img = null;
                }
            }
        }


    }

}
