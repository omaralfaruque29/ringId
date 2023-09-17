/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

/**
 *
 * @author Faiz Ahmed
 */
public class PressenceDto extends UserBasicInfo {

    private int[] inOH;

    public int[] getIndexOfHeaders() {
        return inOH;
    }

    public void setIndexOfHeaders(int[] indexOfHeaders) {
        this.inOH = indexOfHeaders;
    }
}
