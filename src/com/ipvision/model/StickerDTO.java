/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.ArrayList;

/**
 *
 * @author Shahadat
 */
public class StickerDTO {

    private boolean sucs;
    private ArrayList<StickerCollectionDTO> stickerCollectionsList;
    private ArrayList<StickerCategoryDTO> topCategoriesList;
    private ArrayList<StickerCategoryDTO> stNewCategoriesList;
    private ArrayList<StickerCategoryDTO> freeCategoriesList;
    private ArrayList<StickerCategoryDTO> categoriesList;
    private ArrayList<StickerImagesDTO> imagesList;
    private ArrayList<StickerCategoryDTO> singleCategoryInfo;

    public boolean getSuccess() {
        return sucs;
    }

    public void setSuccess(boolean success) {
        this.sucs = success;
    }

    public ArrayList<StickerCollectionDTO> getStickerCollectionsList() {
        return stickerCollectionsList;
    }

    public void setStickerCollectionsList(ArrayList<StickerCollectionDTO> stickerCollectionsList) {
        this.stickerCollectionsList = stickerCollectionsList;
    }

    public ArrayList<StickerCategoryDTO> getTopCategoriesList() {
        return topCategoriesList;
    }

    public void setTopCategoriesList(ArrayList<StickerCategoryDTO> topCategoriesList) {
        this.topCategoriesList = topCategoriesList;
    }

    public ArrayList<StickerCategoryDTO> getStNewCategoriesList() {
        return stNewCategoriesList;
    }

    public void setStNewCategoriesList(ArrayList<StickerCategoryDTO> stNewCategoriesList) {
        this.stNewCategoriesList = stNewCategoriesList;
    }

    public ArrayList<StickerCategoryDTO> getFreeCategoriesList() {
        return freeCategoriesList;
    }

    public void setFreeCategoriesList(ArrayList<StickerCategoryDTO> freeCategoriesList) {
        this.freeCategoriesList = freeCategoriesList;
    }

    public ArrayList<StickerCategoryDTO> getCategoriesList() {
        return categoriesList;
    }

    public void setCategoriesList(ArrayList<StickerCategoryDTO> categoriesList) {
        this.categoriesList = categoriesList;
    }

    public ArrayList<StickerImagesDTO> getImagesList() {
        return imagesList;
    }

    public void setImagesList(ArrayList<StickerImagesDTO> imagesList) {
        this.imagesList = imagesList;
    }

    public ArrayList<StickerCategoryDTO> getSingleCategoryInfo() {
        return singleCategoryInfo;
    }

    public void setSingleCategoryInfo(ArrayList<StickerCategoryDTO> singleCategoryInfo) {
        this.singleCategoryInfo = singleCategoryInfo;
    }

}
