/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.dto;

import java.util.List;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatFileBaseDTO {
    
    private List<ChatFileDTO> response;

    public List<ChatFileDTO> getResponse() {
        return response;
    }

    public void setResponse(List<ChatFileDTO> response) {
        this.response = response;
    }

}
