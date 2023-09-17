/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

/**
 *
 * @author Faiz Ahmed
 */
public class SingleCircleDto {
    /*
     {
     "gNm": "tst",
     "grpId": 79,
     "sAd": "ring8801557855624",
     "ut": 1402915947258,
     "ists": 0
     }
     */

    private String gNm;
    private long grpId;
    private String sAd;
    private Long ut;
    private Short ists;
    private String cvImg;
    private Long cImId;

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

    public Long getUt() {
        return ut;
    }

    public void setUt(Long ut) {
        this.ut = ut;
    }

    public Short getIntegerStatus() {
        return ists;
    }

    public void setIntegerStatus(Short ists) {
        this.ists = ists;
    }
    
    public String getCoverImage() {
        return cvImg;
    }

    public void setCoverImage(String cvImg) {
        this.cvImg = cvImg;
    }
    
    public Long getCoverImageId() {
        return cImId;
    }

    public void setCoverImageId(Long cImId) {
        this.cImId = cImId;
    }
    
}
