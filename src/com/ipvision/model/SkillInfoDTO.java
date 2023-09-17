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
public class SkillInfoDTO {

    private String desc;
    private String skl;
    private Long id;
    private Long ut;

    public String getDescription() {
        return desc;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public String getSkill() {
        return skl;
    }

    public void setSkill(String skl) {
        this.skl = skl;
    }

    public void setUpdateTime(Long ut) {
        this.ut = ut;
    }

    public Long getUpdateTime() {
        return ut;
    }

    public Long getSkillInfoID() {
        return id;
    }

    public void setSkillInfoID(Long id) {
        this.id = id;
    }
}
