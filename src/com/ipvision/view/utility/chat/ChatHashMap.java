/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility.chat;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.ipvision.model.RecentDTO;
import com.ipvision.view.chat.SingleChatPanel;
import com.ipvision.model.ChatBgImageDTO;
import com.ipvision.model.MessageDTO;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatHashMap {

    public static ChatHashMap instance;

    public static ChatHashMap getInstance() {
        if (instance == null) {
            instance = new ChatHashMap();
        }
        return instance;
    }

    private Map<String, MessageDTO> chatStatusMessage = new ConcurrentHashMap<String, MessageDTO>();
    private Map<String, RecentDTO> chatUnreadMessages = new ConcurrentHashMap<String, RecentDTO>();
    private Map<String, Map<String, SingleChatPanel>> chatSingleChatPanel = new ConcurrentHashMap<String, Map<String, SingleChatPanel>>();
    private Map<String, ChatBgImageDTO> chatBgImage = new ConcurrentHashMap<String, ChatBgImageDTO>();
    private Map<String, BufferedImage> chatProfileImage = new ConcurrentHashMap<String, BufferedImage>();

    public Map<String, MessageDTO> getChatStatusMessage() {
        return chatStatusMessage;
    }

    public void setChatStatusMessage(Map<String, MessageDTO> chatStatusMessage) {
        this.chatStatusMessage = chatStatusMessage;
    }

    public Map<String, RecentDTO> getChatUnreadMessages() {
        return chatUnreadMessages;
    }

    public void setChatUnreadMessages(Map<String, RecentDTO> chatUnreadMessages) {
        this.chatUnreadMessages = chatUnreadMessages;
    }
    private Map<String, RecentDTO> callUnreadHistories = new ConcurrentHashMap<String, RecentDTO>();

    public Map<String, RecentDTO> getCallUnreadHistories() {
        return callUnreadHistories;
    }

    public void setCallUnreadHistories(Map<String, RecentDTO> callUnreadHistories) {
        this.callUnreadHistories = callUnreadHistories;
    }
    private ArrayList<String> notificationCall = new ArrayList<>();

    public ArrayList<String> getNotificationCall() {
        return notificationCall;
    }

    public void setNotificationCall(ArrayList<String> notificationCall) {
        this.notificationCall = notificationCall;
    }

    private ArrayList<String> notificationChat = new ArrayList<>();

    public ArrayList<String> getNotificationChat() {
        return notificationChat;
    }

    public void setNotificationChat(ArrayList<String> notificationChat) {
        this.notificationChat = notificationChat;
    }

    public Map<String, Map<String, SingleChatPanel>> getChatSingleChatPanel() {
        return chatSingleChatPanel;
    }

    public void setChatSingleChatPanel(Map<String, Map<String, SingleChatPanel>> chatSingleFriendChatPanel) {
        this.chatSingleChatPanel = chatSingleFriendChatPanel;
    }

    public Map<String, ChatBgImageDTO> getChatBgImage() {
        return chatBgImage;
    }

    public void setChatBgImage(Map<String, ChatBgImageDTO> chatBgImage) {
        this.chatBgImage = chatBgImage;
    }

    public Map<String, BufferedImage> getChatProfileImage() {
        return chatProfileImage;
    }

    public void setChatProfileImage(Map<String, BufferedImage> chatProfileImage) {
        this.chatProfileImage = chatProfileImage;
    }

    public void clearAll() {
        chatStatusMessage.clear();
        chatUnreadMessages.clear();
        chatSingleChatPanel.clear();
        chatBgImage.clear();
        chatProfileImage.clear();
        instance = null;
    }

}
