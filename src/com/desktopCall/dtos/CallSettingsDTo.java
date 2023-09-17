/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.dtos;

/**
 *
 * @author Faiz
 */
public class CallSettingsDTo {

    private String swIp;
    private int swPr;
    private int dvc;
    private String userid;
    private String callID;
    private String fndId;
    private String prIm = "";
    private String fn;
    private String dt;
    private int incomming;

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    /*
     VOICE_SERVER_IP = "38.127.68.57";
     VOICE_REGISTER_PORT = 1250;
     USER_ID = null;
     FRIEND_ID = null;
     VOICE_FRIEND_DEVICE = 0;
     CALL_ID = null;
     */
    public String getSwIp() {
        return swIp;
    }

    public void setSwIp(String swIp) {
        this.swIp = swIp;
    }

    public int getSwPr() {
        return swPr;
    }

    public void setSwPr(int swPr) {
        this.swPr = swPr;
    }

    public int getDvc() {
        return dvc;
    }

    public void setDvc(int dvc) {
        this.dvc = dvc;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCallID() {
        return callID;
    }

    public void setCallID(String callID) {
        this.callID = callID;
    }

    public String getFndId() {
        return fndId;
    }

    public void setFndId(String fndId) {
        this.fndId = fndId;
    }

       public String getPrIm() {
        return prIm;
    }

    public void setPrIm(String prIm) {
        this.prIm = prIm;
    }

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public int getIncomming() {
        return incomming;
    }

    public void setIncomming(int incomming) {
        this.incomming = incomming;
    }
    

}
