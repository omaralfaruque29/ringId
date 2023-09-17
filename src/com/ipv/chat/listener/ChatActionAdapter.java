/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.listener;

import com.ipv.chat.dto.MessageBaseDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shahadat
 */
public abstract class ChatActionAdapter implements ChatActionListener {

    @Override
    public void onFriendChatRegisterConfirmation(MessageBaseDTO messageDTO) {

    }
    
    @Override
    public void onFriendChatUnregister(MessageBaseDTO messageDTO) {
        
    }
    

    @Override
    public void onFriendChat(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onFriendChatDelivered(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onFriendChatSeen(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onFriendChatSent(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onFriendChatTyping(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onFriendChatIdle(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onFriendChatOfflineFetch(List<MessageBaseDTO> messageDTOs, Map<String, MessageBaseDTO> latestChatByFriendIdentity) {

    }

    @Override
    public void onFriendChatDelete(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onFriendChatDeleteConfirmation(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onFriendChatMultiDelete(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onFriendChatMultiDeleteConfirmation(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onFriendChatEdit(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onFriendChatMultipleMessage(List<MessageBaseDTO> messageDTOs) {

    }

    @Override
    public void onFriendChatBrockenMessage(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onFriendChatEditBrockenMessage(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onFriendInformationConfirmation(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onFriendFileStream(List<MessageBaseDTO> messageDTOs) {
        
    }
    
    @Override
    public void onFriendFileStreamConfirmation(MessageBaseDTO messageDTO) {
        
    }
    
    @Override
    public void onFriendFileStreamRequest(MessageBaseDTO messageDTO) {
        
    }
    
    @Override
    public void onFriendFileStreamRequestConfirmation(MessageBaseDTO messageDTO) {
        
    }
    
    @Override
    public void onGroupChatRegisterConfirmation(MessageBaseDTO messageDTO) {

    }
    
    @Override
    public void onGroupChatUnregister(MessageBaseDTO messageDTO) {
        
    }

    @Override
    public void onGroupChat(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onGroupChatDelivered(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onGroupChatSeen(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onGroupChatSent(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onGroupChatTyping(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onGroupChatIdle(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onGroupChatOfflineFetch(List<MessageBaseDTO> messageDTOs, Map<Long, MessageBaseDTO> latestChatByGroupId) {

    }

    @Override
    public void onGroupChatDelete(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onGroupChatDeleteConfirmation(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onGroupChatMultiDelete(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onGroupChatMultiDeleteConfirmation(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onGroupChatEdit(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onGroupChatBrockenMessage(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onGroupChatEditBrockenMessage(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onGroupMembersSendConfirmation(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onGroupMemberRemoveOrLeaveConfirmation(MessageBaseDTO messageDTO) {

    }

    @Override
    public void onGroupDeleteConfirmation(MessageBaseDTO messageDTO) {

    }
    
    @Override
    public void onGroupFileStream(List<MessageBaseDTO> messageDTOs) {
        
    }
    
    @Override
    public void onGroupFileStreamConfirmation(MessageBaseDTO messageDTO) {
        
    }
    
    @Override
    public void onGroupFileStreamRequest(MessageBaseDTO messageDTO) {
        
    }
    
    @Override
    public void onGroupFileStreamRequestConfirmation(MessageBaseDTO messageDTO) {
        
    }

    @Override
    public void onChatOfflineRequestConfirmation(MessageBaseDTO messageDTO) {

    }

}
