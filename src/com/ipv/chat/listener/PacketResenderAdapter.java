/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.listener;

import com.ipv.chat.dto.MessageBaseDTO;

/**
 *
 * @author shahadat
 */
public abstract class PacketResenderAdapter implements PacketResenderListener {

    @Override
    public void onInit(MessageBaseDTO messageDTO) {
        
    }
    
    @Override
    public void onSuccess(MessageBaseDTO messageDTO) {
        
    }

    @Override
    public void onProgress(MessageBaseDTO messageDTO) {
        
    }

    @Override
    public void onFailed(MessageBaseDTO messageDTO) {
        
    }
    
}
