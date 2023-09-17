/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import com.ipv.chat.dto.MessageBaseDTO;

/**
 *
 * @author Shahadat Hossain
 */
public class MessageDTO extends MessageBaseDTO {

    private long messageId;
    private int stickerCategoryId;
    private int stickerCollectionId;

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public int getStickerCategoryId() {
        return stickerCategoryId;
    }

    public void setStickerCategoryId(int stickerCategoryId) {
        this.stickerCategoryId = stickerCategoryId;
    }

    public int getStickerCollectionId() {
        return stickerCollectionId;
    }

    public void setStickerCollectionId(int stickerCollectionId) {
        this.stickerCollectionId = stickerCollectionId;
    }
}
