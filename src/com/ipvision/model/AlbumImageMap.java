/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.ArrayList;

/**
 *
 * @author Faiz Ahmed
 */
public class AlbumImageMap extends FeedBackFields {

    private ArrayList<JsonFields> imageList = null;
    private int timg;

    public int getTotalImage() {
        return timg;
    }

    public void setTotalImage(int timg) {
        this.timg = timg;
    }

    public ArrayList<JsonFields> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<JsonFields> imageList) {
        this.imageList = imageList;
    }
}
