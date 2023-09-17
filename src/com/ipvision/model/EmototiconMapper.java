/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class EmototiconMapper {

    private String emoUrl;
    private Integer emVsn;    /*user basicinfo*/

    private String seq;
    private ArrayList<EmotionDtos> emoticonsList = null;

    public void setSequence(String seq) {
        this.seq = seq;
    }

    public String getSequence() {
        return seq;
    }

    public String getEmoticonUrl() {
        return emoUrl;
    }

    public void setEmoticonUrl(String emoUrl) {
        this.emoUrl = emoUrl;
    }

    public Integer getEmoticonVersion() {
        return emVsn;
    }

    public void setEmoticonVersion(Integer emVsn) {
        this.emVsn = emVsn;
    }

    public ArrayList<EmotionDtos> getEmoticonsList() {
        return emoticonsList;
    }

    public void setEmoticonsList(ArrayList<EmotionDtos> emoticonsList) {
        this.emoticonsList = emoticonsList;
    }
}
