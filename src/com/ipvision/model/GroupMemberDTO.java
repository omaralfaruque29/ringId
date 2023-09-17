/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

public class GroupMemberDTO {

    private Long utId;
    private String uId;
    private String name;
    private Short ists;
    private Long tid;

    public Long getUserTableId() {
        return utId;
    }

    public void setUserTableId(Long utId) {
        this.utId = utId;
    }

    public String getUserIdentity() {
        return uId;
    }

    public void setUserIdentity(String uId) {
        this.uId = uId;
    }

    public String getFullName() {
        return name;
    }

    public void setFullName(String name) {
        this.name = name;
    }

    public Short getIntegerStatus() {
        return ists;
    }

    public void setIntegerStatus(Short ists) {
        this.ists = ists;
    }

    public Long getGroupId() {
        return tid;
    }

    public void setGroupId(Long tid) {
        this.tid = tid;
    }

}
