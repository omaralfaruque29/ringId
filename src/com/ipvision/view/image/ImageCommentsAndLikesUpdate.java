/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.model.JsonFields;

/**
 *
 * @author Faiz
 */
public class ImageCommentsAndLikesUpdate extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ImageCommentsAndLikesUpdate.class);
    JPanel captionAndCommentsPanel;
    NewsFeedWithMultipleImage statusDto;
    JsonFields imageDto;
    // JLabel timelabel;
    AddImageCaptionCommentsPanel addImageCaptionCommentsPanel;

    public ImageCommentsAndLikesUpdate(AddImageCaptionCommentsPanel addImageCaptionCommentsPanel, JPanel captionAndCommentsPane) {
        this.captionAndCommentsPanel = captionAndCommentsPane;
        //    this.timelabel = timelabel;
        this.addImageCaptionCommentsPanel = addImageCaptionCommentsPanel;
    }

    @Override
    public void run() {
        try {
            this.captionAndCommentsPanel.removeAll();
            addImageCaptionCommentsPanel.initDefaults();
            addImageCaptionCommentsPanel.addLikesDetails();
            this.captionAndCommentsPanel.add(addImageCaptionCommentsPanel, BorderLayout.CENTER);
            this.captionAndCommentsPanel.revalidate();

        } catch (Exception e) {
            //e.printStackTrace();
            log.error("Error in here ==>" + e.getMessage());

        }

    }
}
