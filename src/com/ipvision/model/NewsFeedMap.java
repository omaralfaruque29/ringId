/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.ArrayList;
import com.ipvision.model.FeedBackFields;

/**
 *
 * @author Faiz Ahmed
 */
public class NewsFeedMap extends FeedBackFields {
    /*/{"actn":88,"pckFs":10955,"seq":"4/8",
     * "newsFeedList":[
     * {"stId":659,"uId":"toruFinal","sts":"Rtrtrtrrrdff  ","tm":1398585439338,"type":2,"nc":0,"nl":1,"iLike":0,"sucs":true,"fn":"Torongo","ln":"Azad"},
     * {"stId":657,"uId":"test_c","iurl":"http://38.108.92.154/auth/clients_image/test_c/default/1398575586422.png","albId":"default","albn":"default","cptn":"","tm":1398575586445,"type":1,"nc":0,"nl":2,"iLike":0,"sucs":true,"fn":"Faiz","ln":"Ahmed"}
     * ],"sucs":true}*/

    private ArrayList<NewsFeedWithMultipleImage> newsFeedList = null;

    public ArrayList<NewsFeedWithMultipleImage> getNewsFeedList() {
        return newsFeedList;
    }

    public void setNewsFeedList(ArrayList<NewsFeedWithMultipleImage> newsFeedList) {
        this.newsFeedList = newsFeedList;
    }
}