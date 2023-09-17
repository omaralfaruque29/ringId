/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

/**
 *
 * @author Faiz Ahmed
 */
public class SingleMemberInCircleDto {
    /*
     {
     "uId": "ring8801712026023",
     "admin": true,
     "psnc": 1,
     "fn": "sanbir ",
     "ln": "hasan",
     "gr": "Male",
     "mbl": "+8801712026023",
     "grpId": 106,
     "ut": 1403665230698,
     "ists": 0
     }
     */

    private String uId;
    private boolean admin;
    private int psnc;
    private String fn;
    //private String ln;
    private String gr;
    private String mbl;
    private long grpId;
    private long ut;
    private short ists;
    private String prIm = "";

    public String getProfileImage() {
        return prIm;
    }

    public void setProfileImage(String profileImage) {
        this.prIm = profileImage;
    }

    public String getUserIdentity() {
        return uId;
    }

    public void setUserIdentity(String userIdentity) {
        this.uId = userIdentity;
    }

    public String getGender() {
        return gr;
    }

    public void setGender(String gender) {
        this.gr = gender;
    }

    public String getFullName() {
        return fn;
    }

    public void setFullName(String fullName) {
        this.fn = fullName;
    }

    /*public String getLastName() {
        return ln;
    }

    public void setLastName(String lastName) {
        this.ln = lastName;
    }*/

    public String getMobilePhone() {
        return mbl;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mbl = mobilePhone;
    }

    public int getPresence() {
        return psnc;
    }

    public void setPresence(int presence) {
        this.psnc = presence;
    }

    public long getCircleId() {
        return grpId;
    }

    public void setCircleId(long groupId) {
        this.grpId = groupId;
    }

    public long getUt() {
        return ut;
    }

    public void setUt(long ut) {
        this.ut = ut;
    }

    public short getIntegerStatus() {
        return ists;
    }

    public void setIntegerStatus(short ists) {
        this.ists = ists;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
