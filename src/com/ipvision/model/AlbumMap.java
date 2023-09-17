/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.ArrayList;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;

/**
 *
 * @author Faiz Ahmed
 */
public class AlbumMap extends FeedBackFields {

    private ArrayList<JsonFields> albumList = null;

    public ArrayList<JsonFields> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(ArrayList<JsonFields> albumList) {
        this.albumList = albumList;
    }
}