/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.List;

/**
 *
 * @author Faiz
 */
public class CircleDto {

    private String gNm;
    private long grpId;
    private List<CircleMemberDto> groupMembers;
    private String sAd;
    private List<String> removedMembers;
	
    public String getSuperAdmin() {
        return sAd;
    }

    public void setSuperAdmin(String superAdmin) {
        this.sAd = superAdmin;
    }

    public String getCircleName() {
        return gNm;
    }

    public void setCircleName(String groupName) {
        this.gNm = groupName;
    }

    public long getCircleId() {
        return grpId;
    }

    public void setCircleId(long groupId) {
        this.grpId = groupId;
    }

    public List<CircleMemberDto> getCircleMembers() {
        return groupMembers;
    }

    public void setCircleMembers(List<CircleMemberDto> groupMembers) {
        this.groupMembers = groupMembers;
    }
    
    public List<String> getRemovedMembers() {
        return removedMembers;
    }
    
    public void setRemovedMembers(List<String> removedMembers) {
        this.removedMembers = removedMembers;
    }
}
