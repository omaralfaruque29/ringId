/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.listener;

import com.ipv.chat.dto.ChatFileDTO;

/**
 *
 * @author shahadat
 */
public abstract class UploadProgressAdapter implements UploadProgressListener {

    @Override
    public void onSuccess(ChatFileDTO chatFileDTO) {
        
    }
    
    @Override
    public void onProgress(int percentage, Object tag) {
        
    }

    @Override
    public void onFailed(ChatFileDTO chatFileDTO) {
        
    }
    
}
