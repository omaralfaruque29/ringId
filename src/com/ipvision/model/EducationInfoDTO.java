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
public class EducationInfoDTO {

    private Long id;
    private Long ft;
    private Long tt;
    private Long ut;
    private String scl; //School
    private String desc; //Description
    private Integer af; //AttentedFor
    private Boolean grtd; //Graduated
    private Boolean iss; //IsSchool
    private String cntn; //Concentration
    private String dgr; //degree

    public String getDegree() {
        return dgr;
    }

    public void setDegree(String dgr) {
        this.dgr = dgr;
    }

    public Long getEducationId() {
        return id;
    }

    public void setEducationId(Long id) {
        this.id = id;
    }

    public Long getFromTime() {
        return ft;
    }

    public void setFromTime(Long ft) {
        this.ft = ft;
    }

    public Long getToTime() {
        return tt;
    }

    public void setToTime(Long tt) {
        this.tt = tt;
    }

    public Long getUpdateTime() {
        return ut;
    }

    public void setUpdateTime(Long ut) {
        this.ut = ut;
    }

    public String getSchool() {
        return scl;
    }

    public void setSchool(String scl) {
        this.scl = scl;
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public Integer getAttendedFor() {
        return af;
    }

    public void setAttendedFor(Integer af) {
        this.af = af;
    }

    public Boolean getGraduated() {
        return grtd;
    }

    public void setGraduated(Boolean grtd) {
        this.grtd = grtd;
    }

    public Boolean getIsSchool() {
        return iss;
    }

    public void setIsSchool(Boolean iss) {
        this.iss = iss;
    }

    public String getConcentration() {
        return cntn;
    }

    public void setConcentration(String cntn) {
        this.cntn = cntn;
    }
    
    
}
