/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

/**
 *
 * @author nazmul
 */
public class CircleMemberDto{

    private String uId;
    private Boolean admin;
    private String psnc;
    private String fn;
    private String ln;
    private String gr;
    private String mbl;

    public String getMobilePhone() {
        return mbl;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mbl = mobilePhone;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String getGender() {
        return gr;
    }

    public void setGender(String gender) {
        this.gr = gender;
    }

    public String getFirstName() {
        return fn;
    }

    public void setFirstName(String firstName) {
        this.fn = firstName;
    }

    public String getLastName() {
        return ln;
    }

    public void setLastName(String lastName) {
        this.ln = lastName;
    }

    public String getUserIdentity() {
        return uId;
    }

    public void setUserIdentity(String userIdentity) {
        this.uId = userIdentity;
    }

//    public boolean isAdmin() {
//        return admin;
//    }
//
//    public void setAdmin(boolean admin) {
//        this.admin = admin;
//    }
    public String getPresence() {
        return psnc;
    }

    public void setPresence(String presence) {
        this.psnc = presence;
    }
}
