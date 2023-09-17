/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

/**
 *
 * @author Sirat Samyoun
 */
public class InstantMessageDTO {

    private String instantMessage;
    private int msgType; //0 for default,1 for customized msgs

    public String getInstantMessage() {
        return instantMessage;
    }

    public void setInstantMessage(String instantMessage) {
        this.instantMessage = instantMessage;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

}
