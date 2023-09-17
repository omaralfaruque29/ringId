/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.ArrayList;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.SingleMemberInCircleDto;

/**
 *
 * @author Faiz Ahmed
 */
public class LikesMap extends FeedBackFields {
//{"actn":92,"pckFs":12035,"seq":"1/1","likes":[{"uId":"ring8801728119927","fn":"Faiz","ln":"Ahmed"}],"sucs":true,"nfId":6588}

    private ArrayList<SingleMemberInCircleDto> likes = null;

    public ArrayList<SingleMemberInCircleDto> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<SingleMemberInCircleDto> likes) {
        this.likes = likes;
    }
}
