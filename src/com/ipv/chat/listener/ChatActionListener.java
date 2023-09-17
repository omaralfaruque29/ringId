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
 * @author Shahadat Hossain
 */
public interface ChatActionListener {

    public void onFriendChatRegisterConfirmation(MessageBaseDTO messageDTO);

    public void onFriendChatUnregister(MessageBaseDTO messageDTO);
    
    public void onFriendChat(MessageBaseDTO messageDTO);

    public void onFriendChatDelivered(MessageBaseDTO messageDTO);

    public void onFriendChatSeen(MessageBaseDTO messageDTO);

    public void onFriendChatSent(MessageBaseDTO messageDTO);

    public void onFriendChatTyping(MessageBaseDTO messageDTO);

    public void onFriendChatIdle(MessageBaseDTO messageDTO);

    public void onFriendChatOfflineFetch(List<MessageBaseDTO> messageDTOs, Map<String, MessageBaseDTO> latestChatByFriendIdentity);

    public void onFriendChatDelete(MessageBaseDTO messageDTO);

    public void onFriendChatDeleteConfirmation(MessageBaseDTO messageDTO);

    public void onFriendChatMultiDelete(MessageBaseDTO messageDTO);

    public void onFriendChatMultiDeleteConfirmation(MessageBaseDTO messageDTO);

    public void onFriendChatEdit(MessageBaseDTO messageDTO);

    public void onFriendChatMultipleMessage(List<MessageBaseDTO> messageDTOs);

    public void onFriendChatBrockenMessage(MessageBaseDTO messageDTO);

    public void onFriendChatEditBrockenMessage(MessageBaseDTO messageDTO);

    public void onFriendInformationConfirmation(MessageBaseDTO messageDTO);

    public void onFriendFileStream(List<MessageBaseDTO> messageDTOs);
    
    public void onFriendFileStreamConfirmation(MessageBaseDTO messageDTO);
    
    public void onFriendFileStreamRequest(MessageBaseDTO messageDTO);
    
    public void onFriendFileStreamRequestConfirmation(MessageBaseDTO messageDTO);
    
    public void onGroupChatRegisterConfirmation(MessageBaseDTO messageDTO);
    
    public void onGroupChatUnregister(MessageBaseDTO messageDTO);

    public void onGroupChat(MessageBaseDTO messageDTO);

    public void onGroupChatDelivered(MessageBaseDTO messageDTO);

    public void onGroupChatSeen(MessageBaseDTO messageDTO);

    public void onGroupChatSent(MessageBaseDTO messageDTO);

    public void onGroupChatTyping(MessageBaseDTO messageDTO);

    public void onGroupChatIdle(MessageBaseDTO messageDTO);

    public void onGroupChatOfflineFetch(List<MessageBaseDTO> messageDTOs, Map<Long, MessageBaseDTO> latestChatByGroupId);

    public void onGroupChatDelete(MessageBaseDTO messageDTO);

    public void onGroupChatDeleteConfirmation(MessageBaseDTO messageDTO);

    public void onGroupChatMultiDelete(MessageBaseDTO messageDTO);

    public void onGroupChatMultiDeleteConfirmation(MessageBaseDTO messageDTO);

    public void onGroupChatEdit(MessageBaseDTO messageDTO);

    public void onGroupChatBrockenMessage(MessageBaseDTO messageDTO);

    public void onGroupChatEditBrockenMessage(MessageBaseDTO messageDTO);

    public void onGroupMembersSendConfirmation(MessageBaseDTO messageDTO);

    public void onGroupMemberRemoveOrLeaveConfirmation(MessageBaseDTO messageDTO);

    public void onGroupDeleteConfirmation(MessageBaseDTO messageDTO);
    
    public void onGroupFileStream(List<MessageBaseDTO> messageDTOs);
    
    public void onGroupFileStreamConfirmation(MessageBaseDTO messageDTO);
    
    public void onGroupFileStreamRequest(MessageBaseDTO messageDTO);
    
    public void onGroupFileStreamRequestConfirmation(MessageBaseDTO messageDTO);

    public void onChatOfflineRequestConfirmation(MessageBaseDTO messageDTO);

}
