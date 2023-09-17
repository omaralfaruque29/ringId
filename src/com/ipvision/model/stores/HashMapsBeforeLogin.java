/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.stores;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.ipvision.model.EmotionDtos;

/**
 *
 * @author Faiz Ahmed
 */
public class HashMapsBeforeLogin {

    public static HashMapsBeforeLogin hashMapsBeforeLogin;

    public static HashMapsBeforeLogin getInstance() {
        if (hashMapsBeforeLogin == null) {
            hashMapsBeforeLogin = new HashMapsBeforeLogin();
        }
        return hashMapsBeforeLogin;
    }

    private Map<String, EmotionDtos> emotionHashmap = new ConcurrentHashMap<String, EmotionDtos>();

    public Map<String, EmotionDtos> getEmotionHashmap() {
        return emotionHashmap;
    }

    public void setEmotionHashmap(Map<String, EmotionDtos> emotionHashmap) {
        this.emotionHashmap = emotionHashmap;
    }

}
