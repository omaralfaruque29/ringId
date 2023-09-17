/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.List;

/**
 *
 * @author Shahadat
 */
public class ChatBgDTO {
    
    private Boolean sucs;
    private List<ChatBgImageDTO> chatBgImageList;
 
    public Boolean getSuccess() {
        return sucs;
    }

    public void setSuccess(Boolean sucs) {
        this.sucs = sucs;
    }

    public List<ChatBgImageDTO> getChatBgImageList() {
        return chatBgImageList;
    }

    public void setChatBgImageList(List<ChatBgImageDTO> chatBgImageList) {
        this.chatBgImageList = chatBgImageList;
    }
    
}
