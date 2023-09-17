/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.chat;

import com.ipv.chat.ChatConstants;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.constants.RingColorCode;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import org.apache.log4j.Logger;
import com.ipvision.model.dao.InsertIntoFriendMessageTable;
import com.ipvision.model.dao.InsertIntoGroupMessageTable;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.MessageDTO;
import com.ipvision.view.utility.chat.ChatHashMap;
import com.ipvision.view.utility.chat.ProgressCircleUI;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.utility.ImageHelpers;

/**
 *
 * @author Shahadat Hossain
 */
public class SingleChatPanel extends JPanel {

    org.apache.log4j.Logger log = Logger.getLogger(SingleChatPanel.class);

    public MessageDTO messageDTO;
    public String lastChatSenderIdentity;
    public int msgFrom;
    public int chatType;
    public Color chatMsgColor;
    public Color chatShadowColor = RingColorCode.RING_CHAT_SHADOW;

    private SingleChatPanel instance;
    public ChatTextMessagePanel chatTextMessagePanel;
    public ChatImageMessagePanel chatImageMessagePanel;
    public ChatAudioMessagePanel chatAudioMessagePanel;
    public ChatVideoMessagePanel chatVideoMessagePanel;
    public ChatFileMessagePanel chatFileMessagePanel;
    public ChatEditMessagePanel chatEditMessagePanel;
    private JPanel messagePanel;
    private JPanel imagePanel;
    private JLabel imageLabel;
    public JLabel timeLabel;
    public JLabel statusLabel;
    private JLabel fullNameLabel;
    public JLabel fileNameLabel;
    public JPanel topPanel;
    public JPanel topWrapperPanel;
    public JPanel bottomPanel;
    public JPanel bottomWrapperPanel;
    public JProgressBar downloadProgressBar;
    public JCheckBox chkSelect;
    public JPanel chkSelectPanel;
    public JPanel timeStatusPanel;
    private JPanel imageSelectWrapper;

    public static final int TYPE_ME = 0;
    public static final int TYPE_FRIEND = 1;
    public static final int TYPE_FRIEND_CHAT = 0;
    public static final int TYPE_GROUP_CHAT = 1;

    public SingleChatPanel(JPanel messagePanel, MessageDTO messageDTO, String lastChatSenderIdentity) {
        this.lastChatSenderIdentity = lastChatSenderIdentity;
        this.messagePanel = messagePanel;
        this.messageDTO = messageDTO;
        this.instance = this;

        this.setName(this.messageDTO.getMessage());
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(0, 10, 0, 0));
        this.setOpaque(false);

        JPanel nPanel = new JPanel();
        nPanel.setOpaque(false);
        nPanel.setPreferredSize(new Dimension(565, 5));
        this.add(nPanel, BorderLayout.NORTH);

        JPanel sPanel = new JPanel();
        sPanel.setOpaque(false);
        sPanel.setPreferredSize(new Dimension(565, 5));
        this.add(sPanel, BorderLayout.SOUTH);

        if (messageDTO.getUserIdentity() != null && messageDTO.getUserIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
            msgFrom = TYPE_ME;
            chatMsgColor = RingColorCode.RING_CHAT_TEXT_FROM_ME;
        } else {
            msgFrom = TYPE_FRIEND;
            chatMsgColor = RingColorCode.RING_CHAT_TEXT_FROM_FRIEND;
        }

        if (messageDTO.getMessageType() == ChatConstants.TYPE_DOWNLOADED_STICKER_MSG) {
            chatMsgColor = RingColorCode.RING_CHAT_STICKER;
            chatShadowColor = RingColorCode.RING_CHAT_STICKER_SHADOW;
        }

        if (messageDTO.getGroupId() != null && messageDTO.getGroupId() > 0) {
            chatType = TYPE_GROUP_CHAT;
        } else {
            chatType = TYPE_FRIEND_CHAT;
        }

        this.initComponents();
    }

    private void initComponents() {
        try {
            fullNameLabel = DesignClasses.makeLableBold1("", 10, Font.BOLD);
            fullNameLabel.setForeground(RingColorCode.RING_CHAT_SHADE_FG);

            imageSelectWrapper = new JPanel(new BorderLayout());
            imageSelectWrapper.setPreferredSize(new Dimension(68, 0));
            imageSelectWrapper.setOpaque(false);
            
            chkSelectPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 3));
            chkSelectPanel.setBackground(Color.YELLOW);
            chkSelectPanel.setOpaque(false);
            
            imageLabel = new JLabel();
            imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); 
            chkSelectPanel.setBackground(Color.GREEN);
            imagePanel.setOpaque(false);
            if (msgFrom == TYPE_ME) {
                imagePanel.setBorder(new EmptyBorder(0, 4, 0, 0));
                imageSelectWrapper.add(imagePanel, BorderLayout.WEST);
                imageSelectWrapper.add(chkSelectPanel, BorderLayout.EAST);
                this.add(imageSelectWrapper, BorderLayout.EAST);
            } else {
                imagePanel.setBorder(new EmptyBorder(0, 0, 0, 4));
                imageSelectWrapper.add(imagePanel, BorderLayout.EAST);
                imageSelectWrapper.add(chkSelectPanel, BorderLayout.WEST);
                this.add(imageSelectWrapper, BorderLayout.WEST);
            }

            topWrapperPanel = new JPanel(new BorderLayout());
            topWrapperPanel.setOpaque(false);

            topPanel = new JPanel(new FlowLayout(msgFrom == SingleChatPanel.TYPE_FRIEND ? FlowLayout.LEFT : FlowLayout.RIGHT));
            topPanel.setOpaque(false);
            topPanel.add(fullNameLabel);
            topWrapperPanel.add(topPanel);

            bottomWrapperPanel = new JPanel(new BorderLayout());
            bottomWrapperPanel.setBorder(new EmptyBorder(3, 0, 0, 0));
            bottomWrapperPanel.setOpaque(false);

            bottomPanel = new JPanel();
            bottomPanel.setOpaque(false);
            bottomWrapperPanel.add(bottomPanel);
            bottomWrapperPanel.setVisible(false);

            this.setFullName();

            timeLabel = new JLabel("", SwingConstants.RIGHT);
            timeLabel.setForeground(RingColorCode.RING_CHAT_SHADE_FG);
            timeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));

            statusLabel = new JLabel();
            statusLabel.setForeground(RingColorCode.RING_CHAT_SHADE_FG);
            statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));

            fileNameLabel = new JLabel();
            fileNameLabel.setForeground(RingColorCode.RING_CHAT_SHADE_FG);
            fileNameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));

            if (msgFrom == TYPE_ME) {
                timeLabel.setAlignmentX(RIGHT_ALIGNMENT);
                statusLabel.setAlignmentX(RIGHT_ALIGNMENT);
                fileNameLabel.setAlignmentX(RIGHT_ALIGNMENT);
            }

            downloadProgressBar = new JProgressBar() {
                @Override
                public void updateUI() {
                    super.updateUI();
                    setUI(new ProgressCircleUI());
                    setBorder(null);
                }
            };
            downloadProgressBar.setPreferredSize(new Dimension(100, 100));
            downloadProgressBar.setStringPainted(true);
            downloadProgressBar.setOpaque(false);
            downloadProgressBar.setFont(downloadProgressBar.getFont().deriveFont(17f));

            timeStatusPanel = new JPanel();
            timeStatusPanel.setBorder(new EmptyBorder(5, 5, 0, 5));
            timeStatusPanel.setOpaque(false);
            timeStatusPanel.setLayout(new BoxLayout(timeStatusPanel, BoxLayout.Y_AXIS));

            timeStatusPanel.add(timeLabel);
            timeStatusPanel.add(Box.createVerticalStrut(3));
            timeStatusPanel.add(statusLabel);
            timeStatusPanel.add(Box.createVerticalStrut(3));

            chkSelect = new JCheckBox(DesignClasses.return_image(GetImages.TICK));
            chkSelect.setSelectedIcon(DesignClasses.return_image(GetImages.TICK_H2));
            chkSelect.setRolloverIcon(DesignClasses.return_image(GetImages.TICK_H));
            chkSelect.setOpaque(false);
            chkSelectPanel.add(chkSelect);

            setMainPanel();
        } catch (Exception ex) {
           // ex.printStackTrace();
        log.error("Error in here ==>" + ex.getMessage());
        }
    }

    public void setEditPanel() {
        if (messageDTO.getMessageType() == ChatConstants.TYPE_PLAIN_MSG || messageDTO.getMessageType() == ChatConstants.TYPE_EMOTICON_MSG) {
            if (chatTextMessagePanel != null) {
                this.remove(chatTextMessagePanel);
                this.revalidate();
                chatTextMessagePanel = null;
            }
            chatEditMessagePanel = new ChatEditMessagePanel(instance);
            this.add(chatEditMessagePanel, BorderLayout.CENTER);
            this.revalidate();
            chatEditMessagePanel.grabTextBoxFocus();
        }
    }

    public void setMainPanel() {
        if (chatEditMessagePanel != null) {
            this.remove(chatEditMessagePanel);
            this.revalidate();
            chatEditMessagePanel = null;
        }
        if (chatTextMessagePanel != null) {
            this.remove(chatTextMessagePanel);
            this.revalidate();
            chatTextMessagePanel = null;
        }
        if (chatImageMessagePanel != null) {
            this.remove(chatImageMessagePanel);
            this.revalidate();
            chatImageMessagePanel = null;
        }
        if (chatAudioMessagePanel != null) {
            this.remove(chatAudioMessagePanel);
            this.revalidate();
            chatAudioMessagePanel = null;
        }
        if (chatVideoMessagePanel != null) {
            this.remove(chatVideoMessagePanel);
            this.revalidate();
            chatVideoMessagePanel = null;
        }
        if (chatFileMessagePanel != null) {
            this.remove(chatFileMessagePanel);
            this.revalidate();
            chatFileMessagePanel = null;
        }
        if (messageDTO.getMessageType() == ChatConstants.TYPE_IMAGE_FILE_DIRECTORY || messageDTO.getMessageType() == ChatConstants.TYPE_IMAGE_FILE_RING_CAMERA) {
            chatImageMessagePanel = new ChatImageMessagePanel(instance);
            this.add(chatImageMessagePanel, BorderLayout.CENTER);
        } else if (messageDTO.getMessageType() == ChatConstants.TYPE_AUDIO_FILE) {
            chatAudioMessagePanel = new ChatAudioMessagePanel(instance);
            this.add(chatAudioMessagePanel, BorderLayout.CENTER);
        } else if (messageDTO.getMessageType() == ChatConstants.TYPE_VIDEO_FILE) {
            chatVideoMessagePanel = new ChatVideoMessagePanel(instance);
            this.add(chatVideoMessagePanel, BorderLayout.CENTER);
        } else if (messageDTO.getMessageType() == ChatConstants.TYPE_STREAM_FILE) {
            chatFileMessagePanel = new ChatFileMessagePanel(instance);
            this.add(chatFileMessagePanel, BorderLayout.CENTER);
        } else {
            chatTextMessagePanel = new ChatTextMessagePanel(instance);
            this.add(chatTextMessagePanel, BorderLayout.CENTER);
        }
        this.revalidate();
    }

    public void setFullName() {
        imageSelectWrapper.setBorder(null);
        imagePanel.removeAll();
        imageLabel.setIcon(null);
        topWrapperPanel.setVisible(false);

        if (messageDTO.getUserIdentity() != null && (lastChatSenderIdentity == null || !messageDTO.getUserIdentity().equalsIgnoreCase(lastChatSenderIdentity))) {
            String fullName = "Me";
            BufferedImage bufferedImage = null;

            if (msgFrom == TYPE_FRIEND) {
                UserBasicInfo userBasicInfo = FriendList.getInstance().getFriend_hash_map().get(messageDTO.getUserIdentity());
                if (userBasicInfo != null) {
                    fullName = userBasicInfo.getFullName();
                } else if (messageDTO.getFullName() != null && messageDTO.getFullName().length() > 0) {
                    fullName = messageDTO.getFullName();
                } else {
                    fullName = HelperMethods.getRingID(messageDTO.getUserIdentity());
                }

                if (ChatHashMap.getInstance().getChatProfileImage().get(messageDTO.getUserIdentity()) != null) {
                    bufferedImage = ChatHashMap.getInstance().getChatProfileImage().get(messageDTO.getUserIdentity());
                } else {
                    bufferedImage = ImageHelpers.getProfileImageChatView(messageDTO.getUserIdentity());
                    ChatHashMap.getInstance().getChatProfileImage().put(messageDTO.getUserIdentity(), bufferedImage);
                }
                
                if(bufferedImage != null) {
                    imageSelectWrapper.setBorder(new EmptyBorder(23, 0, 0, 0));
                }

                imageLabel.setIcon(new ImageIcon(bufferedImage));
                imageLabel.setToolTipText(fullName);
                imagePanel.add(imageLabel);

                fullNameLabel.setText(fullName);
                topWrapperPanel.setVisible(true);
            } else {
                if (ChatHashMap.getInstance().getChatProfileImage().get(messageDTO.getUserIdentity()) != null) {
                    bufferedImage = ChatHashMap.getInstance().getChatProfileImage().get(messageDTO.getUserIdentity());
                } else {
                    bufferedImage = ImageHelpers.getBufferImageFromFolder(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER, ImageHelpers.getImageNameThumbFromUrl(MyFnFSettings.userProfile.getProfileImage()));
                    if (bufferedImage != null) {
                        bufferedImage = DesignClasses.scaleToRoundedImageWithBorder(35, 35, bufferedImage, 35);
                    } else {
                        bufferedImage = ImageHelpers.createDefaultBufferImage(35, HelperMethods.getShortName(MyFnFSettings.userProfile.getFullName()));//ImageHelpers.getUnknownImage(true);
                    }
                    ChatHashMap.getInstance().getChatProfileImage().put(messageDTO.getUserIdentity(), bufferedImage);
                }
                
                if(bufferedImage != null) {
                    imageSelectWrapper.setBorder(new EmptyBorder(23, 0, 0, 0));
                }

                imageLabel.setIcon(new ImageIcon(bufferedImage));
                imageLabel.setToolTipText(fullName);
                imagePanel.add(imageLabel);

                fullNameLabel.setText(fullName);
                topWrapperPanel.setVisible(true);
            }
        }

        imageSelectWrapper.revalidate();
        topWrapperPanel.revalidate();
        if (chatTextMessagePanel != null && chatTextMessagePanel.chatMsgPanel != null) {
            chatTextMessagePanel.chatMsgPanel.revalidate();
        }
    }

    public void setMessage(MessageDTO messageDTO) {
        this.messageDTO = messageDTO;
    }

    public void showTimerLoader() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (messageDTO.getGroupId() != null && messageDTO.getGroupId() > 0) {
                        messageDTO.setStatus(ChatConstants.CHAT_GROUP_MESSAGE_DELETED);
                        List<MessageDTO> mList = new ArrayList<MessageDTO>();
                        mList.add(messageDTO);
                        new InsertIntoGroupMessageTable(mList).start();
                    } else {
                        messageDTO.setStatus(ChatConstants.CHAT_FRIEND_MESSAGE_DELETED);
                        List<MessageDTO> mList = new ArrayList<MessageDTO>();
                        mList.add(messageDTO);
                        new InsertIntoFriendMessageTable(mList).start();
                    }

                    for (int idx = messageDTO.getTimeout(); idx >= 0; idx--) {
                        Thread.sleep(1000);
                    }

                    if (messageDTO.getGroupId() != null && messageDTO.getGroupId() > 0) {
                        GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(messageDTO.getGroupId());
                        if (groupPanel != null) {
                            groupPanel.removeSingleChatPanel(instance);
                        }
                    } else {
                        MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(messageDTO.getFriendIdentity());
                        if (friendChatCallPanel != null && friendChatCallPanel.myFriendChatPanel != null) {
                            friendChatCallPanel.myFriendChatPanel.removeSingleChatPanel(instance);
                        }
                    }
                } catch (Exception ex) {

                }
            }
        }).start();
    }

    public void showSelectBox(boolean show, boolean isSelected) {
        chkSelectPanel.setVisible(show);
        chkSelect.setSelected(isSelected);
    }
}
