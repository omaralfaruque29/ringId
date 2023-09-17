/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

/**
 *
 * @author Shahadat
 */
public class StickerCategoryDTO {

    private Integer sCtId;
    private String sctName;
    private Integer sClId;
    private String cgBnrImg;
    private Integer rnk;
    private String dtlImg;
    private Boolean cgNw;
    private String icn;
    private Float prz;
    private Boolean free;
    private String dcpn;
    private Boolean downloaded;

    public Integer getStickerCategoryId() {
        return sCtId;
    }

    public void setStickerCategoryId(Integer stickerCategoryId) {
        this.sCtId = stickerCategoryId;
    }

    public String getStickerCategoryName() {
        return sctName;
    }

    public void setStickerCategoryName(String stickerCategoryName) {
        this.sctName = stickerCategoryName;
    }

    public Integer getStickerCollectionId() {
        return sClId;
    }

    public void setStickerCollectionId(Integer stickerCollectionId) {
        this.sClId = stickerCollectionId;
    }

    public String getCategoryBannerImage() {
        return cgBnrImg;
    }

    public void setCategoryBannerImage(String categoryBannerImage) {
        this.cgBnrImg = categoryBannerImage;
    }

    public Integer getRank() {
        return rnk;
    }

    public void setRank(Integer rank) {
        this.rnk = rank;
    }

    public String getDetailImage() {
        return dtlImg;
    }

    public void setDetailImage(String detailImage) {
        this.dtlImg = detailImage;
    }

    public Boolean getCategoryNew() {
        return cgNw;
    }

    public void setCategoryNew(Boolean categoryNew) {
        this.cgNw = categoryNew;
    }

    public String getIcon() {
        return icn;
    }

    public void setIcon(String icon) {
        this.icn = icon;
    }

    public Float getPrize() {
        return prz;
    }

    public void setPrize(Float prize) {
        this.prz = prize;
    }

    public Boolean getFree() {
        return free;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }

    public String getDescription() {
        return dcpn;
    }

    public void setDescription(String description) {
        this.dcpn = description;
    }

    public Boolean getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(Boolean downloaded) {
        this.downloaded = downloaded;
    }
    
    
    
}
