/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

/**
 *
 * @author Wasif Islam
 */
public class AdditionalInfoDTO {
    
    private String noe;
    private Integer ivf;

    public String getNameOfEmail() {
        return noe;
    }

    public void setNameOfEmail(String noe) {
        this.noe = noe;
    }

    public Integer getIsVerified() {
        return ivf;
    }

    public void setIsVerified(Integer ivf) {
        this.ivf = ivf;
    }
    
    
}
