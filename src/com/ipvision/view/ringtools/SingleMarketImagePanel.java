/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.ringtools;

import com.ipvision.model.StickerCategoryDTO;
import com.ipvision.model.StickerImagesDTO;
import com.ipv.chat.ChatConstants;
import com.ipv.chat.service.ChatService;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.service.uploaddownload.DownLoaderHelps;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.ipvision.service.utility.Scalr;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.MessageDTO;
import com.ipvision.service.utility.chat.PacketResenderHandler;
import com.ipvision.view.utility.chat.ChatHelpers;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Shahadat
 */
public class SingleMarketImagePanel extends JPanel {

    public StickerCategoryDTO categoryDTO;
    public StickerImagesDTO imagesDTO;
    private SingleMarketImagePanel instance;
    int commonWidth = 115;
    int commonHeight = 115;

    public SingleMarketImagePanel(StickerImagesDTO imagesDTO, StickerCategoryDTO categoryDTO) {
        this.instance = this;
        this.categoryDTO = categoryDTO;
        this.imagesDTO = imagesDTO;
        this.setLayout(new GridBagLayout());
        this.setBackground(RingColorCode.RING_MARKET_CENTER_COLOR);
        this.setPreferredSize(new Dimension(commonWidth, commonHeight));
        this.addMouseListener(mouseAdapter);
        init();
    }

    private void init() {
        String src = categoryDTO.getStickerCollectionId() + File.separator + categoryDTO.getStickerCategoryId() + File.separator + imagesDTO.getImageUrl();
        BufferedImage buffered = return_sticker(src);
        if (buffered != null) {
            try {
                int cHeight = (buffered.getHeight() * (commonWidth - 15)) / buffered.getWidth();
                buffered = Scalr.resize(buffered, Scalr.Mode.FIT_EXACT, (commonWidth - 15), cHeight, Scalr.OP_ANTIALIAS);
            } catch (Exception ex) {
            }
        }

        JLabel iconLabel = new JLabel();
        if (buffered != null) {
            iconLabel.setIcon(new ImageIcon((Image) buffered));
        }
        add(iconLabel);
    }

    MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseExited(MouseEvent e) {
            setBackground(RingColorCode.RING_MARKET_CENTER_COLOR);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setBackground(RingColorCode.RING_MARKET_IMAGE_HOVER);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (GuiRingID.getInstance() != null
                    && GuiRingID.getInstance().getMainRight() != null) {
                GroupPanel groupPanel = GuiRingID.getInstance().getMainRight().getGroupPanel();
                MyFriendChatCallPanel friendChatCallPanel = GuiRingID.getInstance().getMainRight().getMyFriendChatCallPanel();
                String symbol = categoryDTO.getStickerCollectionId() + "/" + categoryDTO.getStickerCategoryId() + "/" + imagesDTO.getImageUrl();

                if (friendChatCallPanel != null && friendChatCallPanel.isDisplayable() && friendChatCallPanel.isVisible()) {
                    ChatHelpers.startFriendChat(friendChatCallPanel.friendIdentity);
                    buildFriendChatPacket(symbol, friendChatCallPanel);
                } else if (groupPanel != null && groupPanel.isDisplayable()) {
                    ChatHelpers.startGroupChat(groupPanel.groupId, true);
                    buildGroupChatPacket(symbol, groupPanel);
                }
            }
        }
    };

    private void buildFriendChatPacket(String symbol, MyFriendChatCallPanel friendChatCallPanel) {
        if (FriendList.getInstance().getFriend_hash_map().get(friendChatCallPanel.friendIdentity) != null) {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessageType(ChatConstants.TYPE_DOWNLOADED_STICKER_MSG);
            messageDTO.setPacketType(ChatConstants.CHAT_FRIEND);
            if (friendChatCallPanel.myFriendChatPanel.timerTime > 0) {
                messageDTO.setTimeout(friendChatCallPanel.myFriendChatPanel.timerTime);
            }
            String packetID = SendToServer.getRanDomPacketID();
            messageDTO.setPacketID(packetID);
            messageDTO.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
            messageDTO.setFriendIdentity(friendChatCallPanel.friendIdentity);
            messageDTO.setMessage(symbol);
            messageDTO.setStatus(ChatConstants.CHAT_FRIEND_PENDING);
            messageDTO.setSystemDate(System.currentTimeMillis());
            ChatService.sendFriendChat(messageDTO, new PacketResenderHandler(), InternetUnavailablityCheck.isInternetAvailable);
        } else {
            HelperMethods.showPlaneDialogMessage(NotificationMessages.NO_LONGER_FRIEND_NOTIFICAITON);
        }
    }

    private void buildGroupChatPacket(String symbol, GroupPanel groupPanel) {
        GroupInfoDTO groupInfoDTO = MyfnfHashMaps.getInstance().getGroup_hash_map().get(groupPanel.groupId);
        if (groupInfoDTO != null
                && groupInfoDTO.getMemberMap() != null
                && groupInfoDTO.getMemberMap().get(MyFnFSettings.LOGIN_USER_ID) != null) {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessageType(ChatConstants.TYPE_DOWNLOADED_STICKER_MSG);
            messageDTO.setPacketType(ChatConstants.CHAT_GROUP);
            String packetID = SendToServer.getRanDomPacketID();
            messageDTO.setPacketID(packetID);
            messageDTO.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
            messageDTO.setGroupId(groupPanel.groupId);
            messageDTO.setFullName(MyFnFSettings.userProfile.getFullName());
            messageDTO.setMessage(symbol);
            messageDTO.setStatus(ChatConstants.CHAT_GROUP_PENDING);
            messageDTO.setSystemDate(System.currentTimeMillis());
            ChatService.sendGroupChat(messageDTO, new PacketResenderHandler(), InternetUnavailablityCheck.isInternetAvailable);
        } else {
            HelperMethods.showPlaneDialogMessage(NotificationMessages.NO_LONGER_IN_GROUP_NOTIFICAITON);
        }
    }

    public BufferedImage return_sticker(String image_source) {
        DownLoaderHelps dHelp = new DownLoaderHelps();
        BufferedImage img = null;
        try {
            File f = new File(dHelp.getSticketDestinationFolder() + File.separator + image_source);
            if (f.exists()) {
                img = ImageIO.read(f);
            }
        } catch (Exception e) {
        }

        return img;
    }
}
