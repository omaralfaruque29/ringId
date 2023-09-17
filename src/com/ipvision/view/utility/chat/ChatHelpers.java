/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility.chat;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.dto.MessageBaseDTO;
import com.ipv.chat.service.ChatService;
import com.ipv.chat.storers.ChatStorer;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.service.uploaddownload.DownLoaderHelps;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.ipvision.model.dao.InsertIntoFriendMessageTable;
import com.ipvision.model.dao.InsertIntoGroupMessageTable;
import com.ipvision.view.chat.SingleChatPanel;
import com.ipvision.model.MessageDTO;
import com.ipvision.service.utility.chat.PacketResenderHandler;
import com.ipvision.service.chat.ChatFriendStartResender;
import com.ipvision.service.chat.ChatGroupStartResender;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author user
 */
public class ChatHelpers {

    public static SimpleDateFormat CHAT_DATE_FORMAT = new SimpleDateFormat("EEEE, dd MMMM yyyy");
    public static SimpleDateFormat CHAT_TIME_FORMAT = new SimpleDateFormat("hh:mm aaa");
    public static String STATUS_TXT_NOT_SENT = "Pending";
    public static String STATUS_TXT_SENT_OFFLINE = "Sent Offline";
    public static String STATUS_TXT_DELIVERED = "Delivered";
    public static String STATUS_TXT_SEEN = "Seen";
    public static String STATUS_TXT_SENDING = "Sending...";
    public static String STATUS_TXT_SENT = "Sent";

    public static DownLoaderHelps CHAT_DIRECTORY = new DownLoaderHelps();

    public static String getDate(long milliSeconds, SimpleDateFormat dateFormat) {
        Date date = new Date(milliSeconds);
        String formatted = dateFormat.format(date);
        return formatted;
    }

    public static void deleteChatMessage(MessageDTO messageDTO) {
        try {
            if (messageDTO.getGroupId() != null && messageDTO.getGroupId() > 0) {
                GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(messageDTO.getGroupId());
                Map<String, SingleChatPanel> chatPanels = ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getGroupId() + "");
                SingleChatPanel chatPanel = chatPanels != null ? chatPanels.get(messageDTO.getPacketID()) : null;

                if (groupPanel != null && chatPanels != null && chatPanel != null) {
                    groupPanel.removeSingleChatPanel(chatPanel);
                    chatPanels.remove(messageDTO.getPacketID());
                } else if (chatPanels != null) {
                    chatPanels.remove(messageDTO.getPacketID());
                }

                List<MessageDTO> list = new ArrayList<MessageDTO>();
                messageDTO.setStatus(ChatConstants.CHAT_GROUP_MESSAGE_DELETED);
                list.add(messageDTO);
                new InsertIntoGroupMessageTable(list).start();

                if (messageDTO.getUserIdentity() != null && messageDTO.getUserIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                    ChatHelpers.startGroupChat(messageDTO.getGroupId(), true);

                    List<String> packetIds = new ArrayList<String>();
                    packetIds.add(messageDTO.getPacketID());
                    ChatService.sendGroupChatDelete(messageDTO.getGroupId(), packetIds);
                }
            } else {
                MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(messageDTO.getFriendIdentity());
                Map<String, SingleChatPanel> chatPanels = ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getFriendIdentity());
                SingleChatPanel chatPanel = chatPanels != null ? chatPanels.get(messageDTO.getPacketID()) : null;

                if (friendChatCallPanel != null && friendChatCallPanel.myFriendChatPanel != null && chatPanels != null && chatPanel != null) {
                    friendChatCallPanel.myFriendChatPanel.removeSingleChatPanel(chatPanel);
                    chatPanels.remove(messageDTO.getPacketID());
                } else if (chatPanels != null) {
                    chatPanels.remove(messageDTO.getPacketID());
                }

                List<MessageDTO> list = new ArrayList<MessageDTO>();
                messageDTO.setStatus(ChatConstants.CHAT_FRIEND_MESSAGE_DELETED);
                list.add(messageDTO);
                new InsertIntoFriendMessageTable(list).start();

                if (messageDTO.getUserIdentity() != null && messageDTO.getUserIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                    ChatHelpers.startFriendChat(messageDTO.getFriendIdentity());

                    List<String> packetIds = new ArrayList<String>();
                    packetIds.add(messageDTO.getPacketID());
                    ChatService.sendFriendChatDelete(messageDTO.getFriendIdentity(), packetIds, new PacketResenderHandler());
                }
            }
        } catch (Exception ex) {

        }
    }

    public static void startFriendChat(String friendIdentity) {
        if (InternetUnavailablityCheck.isInternetAvailable) {
            MessageBaseDTO serverLocation = ChatStorer.SERVER_LOCATION.get(friendIdentity);
            if (serverLocation == null || (System.currentTimeMillis() - serverLocation.getMessageDate()) >= ChatConstants.REGISTRATION_HOLD_DURATION) {
                new ChatFriendStartResender(friendIdentity).start();
            }
        }
    }

    public static void startGroupChat(Long groupId, boolean showWarning) {
        if (InternetUnavailablityCheck.isInternetAvailable) {
            MessageBaseDTO serverLocation = ChatStorer.SERVER_LOCATION.get(groupId + "");
            if (serverLocation == null || (System.currentTimeMillis() - serverLocation.getMessageDate()) >= ChatConstants.REGISTRATION_HOLD_DURATION) {
                new ChatGroupStartResender(groupId, showWarning).start();
            }
        }
    }

    public static File[] getChunkFiles(final String packetID) {
        File[] matchingFiles = null;
        try {
            File directory = new File(ChatHelpers.getRootDirectoryOfChunkFile(packetID));
            if (directory.exists() && directory.isDirectory()) {
                matchingFiles = directory.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.startsWith(packetID);
                        //return name.startsWith(packetID) && name.matches("^\\.\\d+$");
                    }
                });
            }
        } catch (Exception ex) {
        }
        return matchingFiles == null ? new File[0] : matchingFiles;
    }

    public static File[] getChunkFilesByIndex(final String packetID, List<MessageBaseDTO> messageBaseDTOs) {
        File[] files = null;
        try {
            if (messageBaseDTOs.size() <= 0) {
                return ChatHelpers.getChunkFiles(packetID);
            }
            String rootDirectory = ChatHelpers.getRootDirectoryOfChunkFile(packetID);

            List<File> fileList = new ArrayList<File>();
            for (MessageBaseDTO msgDTO : messageBaseDTOs) {
                // System.err.println(msgDTO.getFileChunkIndexFrom() + "///" + msgDTO.getFileChunkIndexTo());
                for (int chunkIndex = msgDTO.getFileChunkIndexFrom(); chunkIndex <= msgDTO.getFileChunkIndexTo(); chunkIndex++) {
                    File file = new File(rootDirectory + File.separator + packetID + "." + chunkIndex);
                    if (file.exists()) {
                        fileList.add(file);
                    }
                }
            }

            files = new File[fileList.size()];
            for (int i = 0; i < fileList.size(); i++) {
                File file = fileList.get(i);
                files[i] = file;
            }
        } catch (Exception ex) {
        }
        return files == null ? new File[0] : files;
    }

    public static String getRootDirectoryOfChunkFile(String packetID) {
        return CHAT_DIRECTORY.getBrokenFilesFolder() + MyFnFSettings.LOGIN_USER_ID + File.separator + packetID;
    }

    public static List<Integer[]> getMissingChunkIndex(String packetID, int totalChunk) {
        List<Integer[]> missingIndexList = new ArrayList<Integer[]>();

        File[] chunkFiles = ChatHelpers.getChunkFiles(packetID);
        String rootDirectory = ChatHelpers.getRootDirectoryOfChunkFile(packetID);

        if (chunkFiles.length == 0) {
            Integer[] array = new Integer[2];
            array[0] = 1;
            array[1] = totalChunk;
            missingIndexList.add(array);
        } else {
            int prevIndex = -1;
            Integer[] array = null;

            for (int chunkIndex = 1; chunkIndex <= totalChunk; chunkIndex++) {
                File file = new File(rootDirectory + File.separator + packetID + "." + chunkIndex);
                if (!file.exists()) {
                    if (chunkIndex - prevIndex > 1) {
                        if (array != null) {
                            array[1] = prevIndex;
                            missingIndexList.add(array);
                        }
                        array = new Integer[2];
                        array[0] = chunkIndex;
                    }
                    prevIndex = chunkIndex;
                }
            }

            if (array != null) {
                array[1] = prevIndex;
                missingIndexList.add(array);
            }
        }

        return missingIndexList;
    }

    public static void mergeChunkFile(String fileName, String packetID, int totalChunk) {
        try {
            String rootDirectory = ChatHelpers.getRootDirectoryOfChunkFile(packetID);

            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(ChatHelpers.CHAT_DIRECTORY.getChatFileDestinationFolder() + "/" + fileName));
            for (int chunkIndex = 1; chunkIndex <= totalChunk; chunkIndex++) {
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(rootDirectory + File.separator + packetID + "." + chunkIndex));
                int b;
                while ((b = in.read()) != -1) {
                    out.write(b);
                }
                in.close();
            }
            out.close();
        } catch (Exception ex) {
        }
    }

    public static String getMergedChunkFileName(String message, String packetID) {
        String extension = "txt";
        String split = message.split("/")[1];
        String ext = HelperMethods.getExtension(split);
        String name = HelperMethods.getImageName(split);
        if (ext != null && ext.length() > 0) {
            extension = ext;
        }

        return name + "_" + packetID + MyFnFSettings.LOGIN_USER_ID + "." + extension;
    }

    public static int getTotalChunk(String message) {
        int total = 0;
        String split = message.split("/")[0];
        try {
            total = Integer.parseInt(split.trim());
        } catch (Exception e) {
        }
        return total;
    }

}
