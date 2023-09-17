/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

/**
 *
 * @author user
 */
public class NewCircleFields {

    private String uId;
    private Boolean admin;
    private Boolean sAd;
    private String psnc;

    public String getPresence() {
        return psnc;
    }

    public void setPresence(String presence) {
        this.psnc = presence;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    private String firstName;
    private String lastName;
    private String gender;

    public String getUserIdentity() {
        return uId;
    }

    public void setUserIdentity(String userIdentity) {
        this.uId = userIdentity;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getSuperAdmin() {
        return sAd;
    }

    public void setSuperAdmin(Boolean superAdmin) {
        this.sAd = superAdmin;
    }
}
