/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.service;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.ChatSettings;
import com.ipv.chat.communicator.ChatSender;
import com.ipv.chat.dto.ChatFileDTO;
import com.ipv.chat.dto.ChatRegistrationDTO;
import com.ipv.chat.dto.MessageBaseDTO;
import com.ipv.chat.file.ChatFileDownloader;
import com.ipv.chat.file.ChatFileUploader;
import com.ipv.chat.listener.DownloadProgressListener;
import com.ipv.chat.listener.PacketResenderListener;
import com.ipv.chat.listener.UploadProgressListener;
import com.ipv.chat.resender.ChatFriendFileStreamRequestResender;
import com.ipv.chat.resender.ChatFriendFileStreamSendResender;
import com.ipv.chat.resender.ChatGroupFileStreamSendResender;
import com.ipv.chat.resender.ChatFriendMessageBackgroundResender;
import com.ipv.chat.resender.ChatFriendMessageResender;
import com.ipv.chat.resender.ChatFriendRegistationResender;
import com.ipv.chat.resender.ChatGroupDeleteResender;
import com.ipv.chat.resender.ChatGroupMemberListResender;
import com.ipv.chat.resender.ChatGroupMemberRemoveResender;
import com.ipv.chat.resender.ChatGroupMessageBackgroundResender;
import com.ipv.chat.resender.ChatGroupMessageResender;
import com.ipv.chat.resender.ChatGroupRegistationResender;
import com.ipv.chat.resender.ChatIdleResender;
import com.ipv.chat.resender.ChatFriendMessageDeleteResender;
import com.ipv.chat.resender.ChatFriendMessageEditResender;
import com.ipv.chat.resender.ChatGroupFileStreamRequestResender;
import com.ipv.chat.resender.ChatGroupMessageDeleteResender;
import com.ipv.chat.resender.ChatGroupMessageEditResender;
import com.ipv.chat.resender.ChatOfflineRequestResender;
import com.ipv.chat.resender.ChatUnRegistation;
import com.ipv.chat.storers.ChatStorer;
import com.ipv.chat.utils.ChatPacketUility;
import java.io.File;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author shahadat
 */
public class ChatService {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatService.class);

    public static void unregisterAllChatConversation(int onlineStatus) {
        try {
            if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
                for (String key : ChatStorer.SERVER_LOCATION.keySet()) {
                    MessageBaseDTO messageDTO = ChatStorer.SERVER_LOCATION.get(key);
                    if (messageDTO.getPacketType() == ChatConstants.CHAT_FRIEND_REGISTER) {
                        byte[] packet = ChatPacketUility.makeFriendUnRegisterPacket(ChatConstants.CHAT_FRIEND_UNREGISTER, ChatSettings.USER_IDENTITY, messageDTO.getFriendIdentity(), onlineStatus);
                        ChatSender.sendRegisterPacket(packet, messageDTO.getChatServerIP(), messageDTO.getChatRegisterPort());
                        Thread.sleep(25);
                        ChatSender.sendRegisterPacket(packet, messageDTO.getChatServerIP(), messageDTO.getChatRegisterPort());
                        Thread.sleep(25);
                        ChatSender.sendRegisterPacket(packet, messageDTO.getChatServerIP(), messageDTO.getChatRegisterPort());
                    } else if (messageDTO.getPacketType() == ChatConstants.CHAT_GROUP_REGISTER) {
                        byte[] packet = ChatPacketUility.makeGroupUnRegisterPacket(ChatConstants.CHAT_GROUP_UNREGISTER, ChatSettings.USER_IDENTITY, messageDTO.getGroupId(), onlineStatus);
                        ChatSender.sendRegisterPacket(packet, messageDTO.getChatServerIP(), messageDTO.getChatRegisterPort());
                        Thread.sleep(25);
                        ChatSender.sendRegisterPacket(packet, messageDTO.getChatServerIP(), messageDTO.getChatRegisterPort());
                        Thread.sleep(25);
                        ChatSender.sendRegisterPacket(packet, messageDTO.getChatServerIP(), messageDTO.getChatRegisterPort());
                    }
                }

                for (Entry<String, ChatIdleResender> entry : ChatStorer.IDLE_THREAD.entrySet()) {
                    ChatIdleResender chatIdleResender = entry.getValue();
                    if (chatIdleResender != null && chatIdleResender.isRunning()) {
                        chatIdleResender.stopService();
                    }
                }

                ChatStorer.IDLE_THREAD.clear();
                ChatStorer.SERVER_LOCATION.clear();
            }
        } catch (Exception e) {
            //  e.printStackTrace();
            log.error("Error in ChatGroupRegistationResender class in unregisterAllChatConversation method ==>" + e.getMessage());
        }
    }

    public static void unregisterFriendChat(String friendIdentity, int onlineStatus) {
        if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
            new ChatUnRegistation(friendIdentity, null, onlineStatus).start();
        }
    }

    public static void unregisterGroupChat(Long groupId, int onlineStatus) {
        if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
            new ChatUnRegistation(null, groupId, onlineStatus).start();
        }
    }

    public static void registerFriendChat(ChatRegistrationDTO entity) {
        if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
            new ChatFriendRegistationResender(entity).start();
        }
    }

    public static void registerGroupChat(ChatRegistrationDTO entity, List<String> memberList) {
        if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
            new ChatGroupRegistationResender(entity, memberList).start();
        }
    }

    public static void sendFriendChat(MessageBaseDTO messageDTO, PacketResenderListener listener, boolean isInternatAvailable) {
        new ChatFriendMessageResender(messageDTO, listener, isInternatAvailable).start();
    }

    public static void sendGroupChat(MessageBaseDTO messageDTO, PacketResenderListener listener, boolean isInternatAvailable) {
        new ChatGroupMessageResender(messageDTO, listener, isInternatAvailable).start();
    }

    public static void sendGroupDelete(Long groupId) {
        if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
            new ChatGroupDeleteResender(groupId).start();
        }
    }

    public static void sendGroupMemberList(Long groupId, List<String> members, PacketResenderListener listener) {
        try {
            if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
                new ChatGroupMemberListResender(groupId, members, listener).start();
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Error in ChatGroupRegistationResender class in sendGroupMemberList method ==>" + ex.getMessage());
        }
    }

    public static void sendGroupMemberRemove(Long groupId, String friendIdentity) {
        if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
            new ChatGroupMemberRemoveResender(groupId, friendIdentity).start();
        }
    }

    public static void sendFriendChatDelete(String friendIdentity, List<String> packetIDs, PacketResenderListener listener) {
        try {
            if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
                new ChatFriendMessageDeleteResender(friendIdentity, packetIDs, listener).start();
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Error in ChatGroupRegistationResender class in sendFriendChatDelete method ==>" + ex.getMessage());
        }
    }

    public static void sendGroupChatDelete(Long groupId, List<String> packetIDs) {
        try {
            if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
                new ChatGroupMessageDeleteResender(groupId, packetIDs).start();
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Error in ChatGroupRegistationResender class in sendGroupChatDelete method ==>" + ex.getMessage());
        }
    }

    public static void sendFriendChatTyping(String friendIdentity) {
        try {
            if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
                MessageBaseDTO serverLoaction = ChatStorer.SERVER_LOCATION.get(friendIdentity);
                if (serverLoaction != null) {
                    serverLoaction.setMessageDate(System.currentTimeMillis());
                    byte[] sendingBytePacket = ChatPacketUility.makeFriendTypingORIdelPacket(ChatConstants.CHAT_FRIEND_TYPING, ChatSettings.USER_IDENTITY, friendIdentity);
                    ChatSender.sendChatPacket(sendingBytePacket, serverLoaction.getChatServerIP(), serverLoaction.getChatBindingPort());
                }
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Error in ChatGroupRegistationResender class in sendFriendChatTyping method ==>" + ex.getMessage());
        }
    }

    public static void sendGroupChatTyping(Long groupId) {
        try {
            if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
                MessageBaseDTO serverLocation = ChatStorer.SERVER_LOCATION.get(groupId + "");
                if (serverLocation != null) {
                    serverLocation.setMessageDate(System.currentTimeMillis());
                    byte[] sendingBytePacket = ChatPacketUility.makeGroupTypingORIdelPacket(ChatConstants.CHAT_GROUP_TYPING, ChatSettings.USER_IDENTITY, groupId);
                    ChatSender.sendChatPacket(sendingBytePacket, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                }
            }
        } catch (Exception ex) {
            //  ex.printStackTrace();
            log.error("Error in ChatGroupRegistationResender class in sendGroupChatTyping method ==>" + ex.getMessage());
        }
    }

    public static void sendFriendChatSeenConfirmation(String packetID, String friendIdentity) {
        try {
            if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
                MessageBaseDTO serverLocation = ChatStorer.SERVER_LOCATION.get(friendIdentity);
                if (serverLocation != null) {
                    long currentDate = System.currentTimeMillis();
                    serverLocation.setMessageDate(currentDate);
                    long serverDate = currentDate + serverLocation.getDateDifference();
                    byte[] sendingBytePacket = ChatPacketUility.makeFriendDeliveredORSeenPacket(ChatConstants.CHAT_FRIEND_SEEN, ChatSettings.USER_IDENTITY, friendIdentity, packetID, serverDate);
                    ChatSender.sendChatPacket(sendingBytePacket, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                    Thread.sleep(25);
                    ChatSender.sendChatPacket(sendingBytePacket, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                }
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Error in ChatGroupRegistationResender class in sendFriendChatSeenConfirmation method ==>" + ex.getMessage());
        }
    }

    public static void sendGroupChatSeenConfirmation(String packetID, long groupId, String friendIdentity) {
        try {
            if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
                MessageBaseDTO serverLocation = ChatStorer.SERVER_LOCATION.get(groupId + "");
                if (serverLocation != null) {
                    long currentDate = System.currentTimeMillis();
                    serverLocation.setMessageDate(currentDate);
                    long serverDate = currentDate + serverLocation.getDateDifference();
                    byte[] sendingBytePacket = ChatPacketUility.makeGroupDeliveredORSeenPacket(ChatConstants.CHAT_GROUP_SEEN, ChatSettings.USER_IDENTITY, friendIdentity, groupId, packetID, serverDate);
                    ChatSender.sendChatPacket(sendingBytePacket, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                    Thread.sleep(25);
                    ChatSender.sendChatPacket(sendingBytePacket, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                }
            }
        } catch (Exception ex) {
            //  ex.printStackTrace();
            log.error("Error in ChatGroupRegistationResender class in sendGroupChatSeenConfirmation method ==>" + ex.getMessage());
        }
    }

    public static void sendFriendChatDeliveredConfirmation(String packetID, String friendIdentity) {
        try {
            if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
                MessageBaseDTO serverLocation = ChatStorer.SERVER_LOCATION.get(friendIdentity);
                if (serverLocation != null) {
                    long currentDate = System.currentTimeMillis();
                    serverLocation.setMessageDate(currentDate);
                    long serverDate = currentDate + serverLocation.getDateDifference();
                    byte[] confirmationByte = ChatPacketUility.makeFriendDeliveredORSeenPacket(ChatConstants.CHAT_FRIEND_DELIVERED, ChatSettings.USER_IDENTITY, friendIdentity, packetID, serverDate);
                    ChatSender.sendChatPacket(confirmationByte, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                    Thread.sleep(25);
                    ChatSender.sendChatPacket(confirmationByte, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                }
            }
        } catch (Exception ex) {
            //  ex.printStackTrace();
            log.error("Error in ChatGroupRegistationResender class in sendFriendChatDeliveredConfirmation method ==>" + ex.getMessage());
        }
    }

    public static void sendGroupChatDeliveredConfirmation(String packetID, long groupId, String friendIdentity) {
        try {
            if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
                MessageBaseDTO serverLocation = ChatStorer.SERVER_LOCATION.get(groupId + "");
                if (serverLocation != null) {
                    long currentDate = System.currentTimeMillis();
                    serverLocation.setMessageDate(currentDate);
                    long serverDate = currentDate + serverLocation.getDateDifference();
                    byte[] confirmationByte = ChatPacketUility.makeGroupDeliveredORSeenPacket(ChatConstants.CHAT_GROUP_DELIVERED, ChatSettings.USER_IDENTITY, friendIdentity, groupId, packetID, serverDate);
                    ChatSender.sendChatPacket(confirmationByte, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                    Thread.sleep(25);
                    ChatSender.sendChatPacket(confirmationByte, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                }
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Error in ChatGroupRegistationResender class in sendGroupChatDeliveredConfirmation method ==>" + ex.getMessage());
        }
    }

    public static void sendFriendChatDeleteConfirmation(String packetID, String friendIdentity) {
        try {
            if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
                MessageBaseDTO serverLocation = ChatStorer.SERVER_LOCATION.get(friendIdentity);
                if (serverLocation != null) {
                    serverLocation.setMessageDate(System.currentTimeMillis());
                    byte[] confirmationByte = ChatPacketUility.makeFriendMessageDeleteConfirmationPacket(ChatConstants.CHAT_FRIEND_MESSAGE_DELETE_CONFIRMATION, ChatSettings.USER_IDENTITY, friendIdentity, packetID);
                    ChatSender.sendChatPacket(confirmationByte, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                    Thread.sleep(25);
                    ChatSender.sendChatPacket(confirmationByte, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                }
            }
        } catch (Exception ex) {
          //  ex.printStackTrace();
          log.error("Error in ChatGroupRegistationResender class in sendFriendChatDeleteConfirmation method ==>" + ex.getMessage());  
        }
    }

    public static void sendGroupChatDeleteConfirmation(String packetID, long groupId, String friendIdentity) {
        try {
            if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
                MessageBaseDTO serverLocation = ChatStorer.SERVER_LOCATION.get(groupId + "");
                if (serverLocation != null) {
                    serverLocation.setMessageDate(System.currentTimeMillis());
                    byte[] confirmationByte = ChatPacketUility.makeGroupMessageDeleteConfirmationPacket(ChatConstants.CHAT_GROUP_MESSAGE_DELETE_CONFIRMATION, ChatSettings.USER_IDENTITY, friendIdentity, groupId, packetID);
                    ChatSender.sendChatPacket(confirmationByte, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                    Thread.sleep(25);
                    ChatSender.sendChatPacket(confirmationByte, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                }
            }
        } catch (Exception ex) {
           // ex.printStackTrace();
            log.error("Error in ChatGroupRegistationResender class in sendGroupChatDeleteConfirmation method ==>" + ex.getMessage());  
        }
    }

    public static void sendFriendChatEdit(MessageBaseDTO messageDTO, PacketResenderListener listener) {
        if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
            new ChatFriendMessageEditResender(messageDTO, listener).start();
        }
    }

    public static void sendGroupChatEdit(MessageBaseDTO messageDTO) {
        if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
            new ChatGroupMessageEditResender(messageDTO).start();
        }
    }

    public static void sendOfflineChatRequest() {
        if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
            new ChatOfflineRequestResender().start();
        }
    }

    public static void uploadChatFile(ChatFileDTO chatFileDTO, UploadProgressListener listener) {
        if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
            new ChatFileUploader(chatFileDTO, listener).start();
        }
    }

    public static void downloadChatFile(MessageBaseDTO messageDTO, String sourceUrl, String destinationPath, DownloadProgressListener listener) {
        if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
            new ChatFileDownloader(messageDTO, sourceUrl, destinationPath, listener).start();
        }
    }

    public static void sendFriendChatInBackground(String friendIdentity, List<MessageBaseDTO> messageDTOs) {
        if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
            new ChatFriendMessageBackgroundResender(friendIdentity, messageDTOs).start();
        }
    }

    public static void sendGroupChatInBackground(Long groupId, List<MessageBaseDTO> messageDTOs) {
        if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
            new ChatGroupMessageBackgroundResender(groupId, messageDTOs).start();
        }
    }

    public static void sendFriendFileStream(String friendIdentity, String packetID, File[] files, int totalChunk) {
        if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
            new ChatFriendFileStreamSendResender(friendIdentity, packetID, files, totalChunk).start();
        }
    }

    public static void sendGroupFileStream(Long groupId, String friendIdentity, String packetID, File[] files, int totalChunk) {
        if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
            new ChatGroupFileStreamSendResender(groupId, friendIdentity, packetID, files, totalChunk).start();
        }
    }

    public static void sendFriendFileStreamRequest(String packetID, String friendIdentity, List<Integer[]> indexRange, int totalChunk) {
        if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
            new ChatFriendFileStreamRequestResender(packetID, friendIdentity, indexRange, totalChunk).start();
        }
    }

    public static void sendGroupFileStreamRequest(String packetID, Long groupId, String friendIdentity, List<Integer[]> indexRanges, int totalChunk) {
        if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
            new ChatGroupFileStreamRequestResender(packetID, groupId, friendIdentity, indexRanges, totalChunk).start();
        }
    }

    public static void sendFriendFileStreamConfirmation(int packetType, String packetID, String friendIdentity) {
        try {
            if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
                MessageBaseDTO serverLocation = ChatStorer.SERVER_LOCATION.get(friendIdentity);
                if (serverLocation != null) {
                    serverLocation.setMessageDate(System.currentTimeMillis());
                    byte[] confirmationByte = ChatPacketUility.makeFriendFileStreamConfirmationPacket(packetType, packetID, ChatSettings.USER_IDENTITY, friendIdentity);
                    ChatSender.sendChatPacket(confirmationByte, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                    Thread.sleep(25);
                    ChatSender.sendChatPacket(confirmationByte, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                }
            }
        } catch (Exception ex) {
           // ex.printStackTrace();
             log.error("Error in ChatGroupRegistationResender class in sendFriendFileStreamConfirmation method ==>" + ex.getMessage());  
        }
    }

    public static void sendGroupFileStreamConfirmation(int packetType, String packetID, long groupId, String friendIdentity) {
        try {
            if (ChatSettings.SESSION_ID != null && ChatSettings.SESSION_ID.length() > 0) {
                MessageBaseDTO serverLocation = ChatStorer.SERVER_LOCATION.get(groupId + "");
                if (serverLocation != null) {
                    serverLocation.setMessageDate(System.currentTimeMillis());
                    byte[] confirmationByte = ChatPacketUility.makeGroupFileStreamConfirmationPacket(packetType, packetID, ChatSettings.USER_IDENTITY, friendIdentity, groupId);
                    ChatSender.sendChatPacket(confirmationByte, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                    Thread.sleep(25);
                    ChatSender.sendChatPacket(confirmationByte, serverLocation.getChatServerIP(), serverLocation.getChatBindingPort());
                }
            }
        } catch (Exception ex) {
           // ex.printStackTrace();
         log.error("Error in ChatGroupRegistationResender class in sendGroupFileStreamConfirmation method ==>" + ex.getMessage());     
        }
    }

    public static long getServerTime(String friendIdentity) {
        long time = 0;
        try {
            if (ChatStorer.SERVER_LOCATION.get(friendIdentity) == null) {
                for (int i = 0; i < 10; i++) {
                    if (ChatStorer.SERVER_LOCATION.get(friendIdentity) == null) {
                        Thread.sleep(500);
                    } else {
                        time = System.currentTimeMillis() + ChatStorer.SERVER_LOCATION.get(friendIdentity).getDateDifference();
                        break;
                    }
                }
            } else {
                time = System.currentTimeMillis() + ChatStorer.SERVER_LOCATION.get(friendIdentity).getDateDifference();
            }

            if (time <= 0) {
                time = System.currentTimeMillis();
            }
        } catch (Exception ex) {
            time = System.currentTimeMillis();
        }
        return time;
    }

    public static long getServerTime(Long groupId) {
        long time = 0;
        try {
            if (ChatStorer.SERVER_LOCATION.get(groupId + "") == null) {
                for (int i = 0; i < 10; i++) {
                    if (ChatStorer.SERVER_LOCATION.get(groupId + "") == null) {
                        Thread.sleep(500);
                    } else {
                        time = System.currentTimeMillis() + ChatStorer.SERVER_LOCATION.get(groupId + "").getDateDifference();
                        break;
                    }
                }
            } else {
                time = System.currentTimeMillis() + ChatStorer.SERVER_LOCATION.get(groupId + "").getDateDifference();
            }

            if (time <= 0) {
                time = System.currentTimeMillis();
            }
        } catch (Exception ex) {
            time = System.currentTimeMillis();
        }
        return time;
    }

}
