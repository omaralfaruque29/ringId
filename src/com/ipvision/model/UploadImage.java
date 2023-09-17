/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import com.ipvision.view.feed.SingleImagePreviewPanel;

public class UploadImage {
    
    private SingleImagePreviewPanel thumbnailPanel;
    private Long imageId;
    private String iurl;
    private String caption;

    public UploadImage(SingleImagePreviewPanel thumbnailPanel, Long imageId, String iurl, String caption) {
        this.thumbnailPanel = thumbnailPanel;
        this.imageId = imageId;
        this.iurl = iurl;
        this.caption  = caption;
    }

    public SingleImagePreviewPanel getThumbnailPanel() {
        return thumbnailPanel;
    }

    public void setThumbnailPanel(SingleImagePreviewPanel thumbnailPanel) {
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

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

}