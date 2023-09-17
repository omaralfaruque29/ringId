/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

/**
 *
 * @author Shahadat
 */
public class StickerCollectionDTO {

    private Integer sClId;
    private String name;
    private String bnrImg;
    private String thmColr;

    public Integer getStickerCollectionId() {
        return sClId;
    }

    public void setStickerCollectionId(Integer stickerCollectionId) {
        this.sClId = stickerCollectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBannerImage() {
        return bnrImg;
    }

    public void setBannerImage(String bannerImage) {
        this.bnrImg = bannerImage;
    }

    public String getThemeColor() {
        return thmColr;
    }

    public void setThemeColor(String themeColor) {
        this.thmColr = themeColor;
    }
    
}
