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
public class UpdateMyProfileFields {

    private Integer actn;
    private String sId;
    private String pckId;
    private String nOfHd;
    private String mDay;

    public Integer getAction() {
        return actn;
    }

    public void setAction(Integer action) {
        this.actn = action;
    }

    public String getSessionId() {
        return sId;
    }

    public void setSessionId(String sessionId) {
        this.sId = sessionId;
    }

    public String getPacketId() {
        return pckId;
    }

    public void setPacketId(String packetId) {
        this.pckId = packetId;
    }

    public String getNoOfHeaders() {
        return nOfHd;
    }

    public void setNoOfHeaders(String noOfHeaders) {
        this.nOfHd = noOfHeaders;
    }

    public String getMarriageDay() {
        return mDay;
    }

    public void setMarriageDay(String mDay) {
        this.mDay = mDay;
    }
}
