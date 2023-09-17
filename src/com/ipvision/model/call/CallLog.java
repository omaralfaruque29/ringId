/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.call;

/**
 *
 * @author FaizAhmed
 */
public class CallLog {

    private Integer calT;//outgoing=1,incoming=2,else missed
    private String fndId;
    //private String phN;
    private Long calD;
    private Long caTm;
    private Long ut;
    private String callID;

    public String getCallID() {
        return callID;
    }

    public void setCallID(String callID) {
        this.callID = callID;
    }

    
    public Long getCallDuration() {
        return calD;
    }

    public void setCallDuration(Long calD) {
        this.calD = calD;
    }

    public Integer getCallType() {
        return calT;
    }

    public void setCallType(Integer calT) {
        this.calT = calT;
    }

    public Long getCallingTime() {
        return caTm;
    }

    public void setCallingTime(Long caTm) {
        this.caTm = caTm;
    }
//
//    public String getPhoneNo() {
//        return phN;
//    }
//
//    public void setPhoneNo(String phN) {
//        this.phN = phN;
//    }

    public String getFriendIdentity() {
        return fndId;
    }

    public void setFriendIdentity(String fndId) {
        this.fndId = fndId;
    }

    public Long getUpdateTime() {
        return ut;
    }

    public void setUpdateTime(Long ut) {
        this.ut = ut;
    }


    @Override
    public String toString() {
        return getCallID() + " " + getCallDuration() + " " + getCallType() + " " + getCallingTime() + " " + getFriendIdentity() + " " + getUpdateTime(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
