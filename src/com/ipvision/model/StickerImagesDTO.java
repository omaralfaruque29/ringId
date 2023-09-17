/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

/**
 *
 * @author Shahadat
 */
public class StickerImagesDTO {

    private Integer imId;
    private Integer sCtId;
    private String imUrl;

    public Integer getImageId() {
        return imId;
    }

    public void setImageId(Integer imageId) {
        this.imId = imageId;
    }

    public Integer getStickerCategoryId() {
        return sCtId;
    }

    public void setStickerCategoryId(Integer stickerCategoryId) {
        this.sCtId = stickerCategoryId;
    }

    public String getImageUrl() {
        return imUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imUrl = imageUrl;
    }
    
}
