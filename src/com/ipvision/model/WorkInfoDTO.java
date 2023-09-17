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
public class WorkInfoDTO {

    private String cnm;
    private String pstn;
    private String desc;
    private String ct;
    private long tt;
    private long ft;
    private Long id;
    private Long ut;

    public void setUpdateTime(Long ut) {
        this.ut = ut;
    }

    public Long getUpdateTime() {
        return ut;
    }

    public Long getWorkInfoID() {
        return id;
    }

    public void setWorkInfoID(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return cnm;
    }

    public void setCompanyName(String cnm) {
        this.cnm = cnm;
    }

    public String getDesignation() {
        return pstn;
    }

    public void setDesignation(String pstn) {
        this.pstn = pstn;
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public String getCity() {
        return ct;
    }

    public void setCity(String ct) {
        this.ct = ct;
    }

    public long getTotime() {
        return tt;
    }

    public void setTotime(long tt) {
        this.tt = tt;
    }

    public long getFromtime() {
        return ft;
    }

    public void setFromtime(long ft) {
        this.ft = ft;
    }

}
