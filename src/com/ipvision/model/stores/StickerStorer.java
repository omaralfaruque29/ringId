/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.stores;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import com.ipvision.model.StickerCategoryDTO;
import com.ipvision.model.StickerImagesDTO;
import javax.swing.ImageIcon;

/**
 *
 * @author Shahadat
 */
public class StickerStorer {

    public static StickerStorer instance;

    public static StickerStorer getInstance() {
        if (instance == null) {
            instance = new StickerStorer();
        }
        return instance;
    }

    private Map<Integer, StickerCategoryDTO> topCategoriesMap = new ConcurrentHashMap<Integer, StickerCategoryDTO>();
    private Map<Integer, StickerCategoryDTO> newCategoriesMap = new ConcurrentHashMap<Integer, StickerCategoryDTO>();
    private Map<Integer, StickerCategoryDTO> freeCategoriesMap = new ConcurrentHashMap<Integer, StickerCategoryDTO>();
    private Map<Integer, StickerCategoryDTO> categoriesMap = new ConcurrentHashMap<Integer, StickerCategoryDTO>();
    private Map<Integer, Map<Integer, StickerImagesDTO>> imagesMap = new ConcurrentHashMap<Integer, Map<Integer, StickerImagesDTO>>();
    private Map<String, ImageIcon> chatEmoticonsImageIconMap = new ConcurrentHashMap<String, ImageIcon>();
    private Map<Integer, Set<String>> chatStickerDownload = new ConcurrentHashMap<Integer, Set<String>>();

    public Map<String, ImageIcon> getChatEmoticonsImageIconMap() {
        return chatEmoticonsImageIconMap;
    }

    public void setChatEmoticonsImageIconMap(Map<String, ImageIcon> chatEmoticonsBuffererImagesMap) {
        this.chatEmoticonsImageIconMap = chatEmoticonsBuffererImagesMap;
    }

    public Map<Integer, StickerCategoryDTO> getTopCategoriesMap() {
        return topCategoriesMap;
    }

    public void setTopCategoriesMap(Map<Integer, StickerCategoryDTO> topCategoriesMap) {
        this.topCategoriesMap = topCategoriesMap;
    }

    public Map<Integer, StickerCategoryDTO> getNewCategoriesMap() {
        return newCategoriesMap;
    }

    public void setNewCategoriesMap(Map<Integer, StickerCategoryDTO> newCategoriesMap) {
        this.newCategoriesMap = newCategoriesMap;
    }

    public Map<Integer, StickerCategoryDTO> getFreeCategoriesMap() {
        return freeCategoriesMap;
    }

    public void setFreeCategoriesMap(Map<Integer, StickerCategoryDTO> freeCategoriesMap) {
        this.freeCategoriesMap = freeCategoriesMap;
    }

    public Map<Integer, StickerCategoryDTO> getCategoriesMap() {
        return categoriesMap;
    }

    public void setCategoriesMap(Map<Integer, StickerCategoryDTO> categoriesMap) {
        this.categoriesMap = categoriesMap;
    }

    public Map<Integer, Map<Integer, StickerImagesDTO>> getImagesMap() {
        return imagesMap;
    }

    public void setImagesMap(Map<Integer, Map<Integer, StickerImagesDTO>> imagesMap) {
        this.imagesMap = imagesMap;
    }

    public Map<Integer, Set<String>> getChatStickerDownload() {
        return chatStickerDownload;
    }

    public void setChatStickerDownload(Map<Integer, Set<String>> chatStickerDownload) {
        this.chatStickerDownload = chatStickerDownload;
    }

    public void clear() {
        getTopCategoriesMap().clear();
        getNewCategoriesMap().clear();
        getFreeCategoriesMap().clear();
        getCategoriesMap().clear();
        getImagesMap().clear();
        getChatEmoticonsImageIconMap().clear();
        getChatStickerDownload().clear();
        instance = null;
    }

}
