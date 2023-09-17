/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import com.ipvision.view.feed.SingleChatImageForFeedPost;

/**
 *
 * @author Sirat Samyoun
 */
public class UploadChatImage {

    private SingleChatImageForFeedPost thumbnailPanel;
    private Long imageId;
    private String iurl;

    public UploadChatImage(SingleChatImageForFeedPost thumbnailPanel, Long imageId, String iurl) {
        this.thumbnailPanel = thumbnailPanel;
        this.imageId = imageId;
        this.iurl = iurl;
    }

    public SingleChatImageForFeedPost getThumbnailPanel() {
        return thumbnailPanel;
    }

    public void setThumbnailPanel(SingleChatImageForFeedPost thumbnailPanel) {
        this.thumbnailPanel = thumbnailPanel;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getIurl() {
        return iurl;
    }

    public void setIurl(String iurl) {
        this.iurl = iurl;
    }
}
