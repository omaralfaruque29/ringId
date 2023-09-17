/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.ArrayList;
import com.ipvision.model.JsonFields;
import com.ipvision.model.SingleBookDetails;

/**
 *
 * @author Faiz Ahmed
 */
public class NewsFeedWithMultipleImage extends JsonFields {

    private ArrayList<JsonFields> imageList = null;

    public ArrayList<JsonFields> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<JsonFields> imageList) {
        this.imageList = imageList;
    }
    private ArrayList<SingleBookDetails> whoShare;

    public ArrayList<SingleBookDetails> getWhoShare() {
        return whoShare;
    }

    public void setWhoShare(ArrayList<SingleBookDetails> whoShare) {
        this.whoShare = whoShare;
    }


    @Override
    public Object clone() {
        try {
            return (Object) super.clone();
        } catch (Exception e) {
            return this;
        }
    }
}
