/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Wasif Islam
 */
public class GroupInfoDTO {

    private Long tid;
    private Long tut;
    private String tgn;
    private Integer ists;
    private Long ownUtId;
    private ArrayList<Long> frndList;
    private Map<String, GroupMemberDTO> memberMap;
    
    public Long getGroupId() {
        return tid;
    }

    public void setGroupId(Long tid) {
        this.tid = tid;
    }

    public Long getGroupUt() {
        return tut;
    }

    public void setGroupUt(Long tut) {
        this.tut = tut;
    }

    public String getGroupName() {
        return tgn;
    }

    public void setGroupName(String tgn) {
        this.tgn = tgn;
    }

    public ArrayList<Long> getFrndList() {
        return frndList;
    }

    public void setFrndList(ArrayList<Long> frndList) {
        this.frndList = frndList;
    }

    public Integer getIntegerStatus() {
        return ists;
    }

    public void setIntegerStatus(Integer ists) {
        this.ists = ists;
    }

    public Map<String, GroupMemberDTO> getMemberMap() {
        return memberMap;
    }

    public void setMemberMap(Map<String, GroupMemberDTO> memberMap) {
        this.memberMap = memberMap;
    }

    public Long getOwnerUserTableId() {
        return ownUtId;
    }

    public void setOwnerUserTableId(Long oUtId) {
        this.ownUtId = oUtId;
    }
    
    
   
}
