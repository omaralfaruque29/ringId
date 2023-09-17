/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.listener;

import com.ipv.chat.dto.MessageBaseDTO;

/**
 *
 * @author Shahadat
 */
public interface DownloadProgressListener {

    public void onSuccess(MessageBaseDTO messageDTO);

    public void onProgress(int percentage, Object tag);
    
    public void onFailed(MessageBaseDTO messageDTO);

}
