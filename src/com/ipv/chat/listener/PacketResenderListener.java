package com.ipv.chat.listener;

import com.ipv.chat.dto.MessageBaseDTO;

/**
 *
 * @author Shahadat Hossain
 */
public interface PacketResenderListener {

    public void onInit(MessageBaseDTO messageDTO);
    
    public void onSuccess(MessageBaseDTO messageDTO);
    
    public void onProgress(MessageBaseDTO messageDTO);
    
    public void onFailed(MessageBaseDTO messageDTO);
    
}
