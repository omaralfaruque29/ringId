/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.auth;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Faiz
 */
public class BreakingPacketData {

    private Long time;
    private ConcurrentHashMap<Integer, byte[]> value;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public ConcurrentHashMap<Integer, byte[]> getValue() {
        return value;
    }

    public void setValue(ConcurrentHashMap<Integer, byte[]> value) {
        this.value = value;
    }

}
