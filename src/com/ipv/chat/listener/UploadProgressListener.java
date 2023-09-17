package com.ipv.chat.listener;

import com.ipv.chat.dto.ChatFileDTO;

/**
 *
 * @author Shahadat Hossain
 */
public interface UploadProgressListener {

    public void onSuccess(ChatFileDTO chatFileDTO);
    
    public void onProgress(int percentage, Object tag);
    
    public void onFailed(ChatFileDTO chatFileDTO);
    
}
