/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.chat;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.dto.MessageBaseDTO;
import com.ipv.chat.listener.DownloadProgressListener;
import com.ipv.chat.service.ChatService;
import com.ipvision.constants.RingColorCode;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import com.ipvision.model.dao.InsertIntoFriendMessageTable;
import com.ipvision.model.dao.InsertIntoGroupMessageTable;
import com.ipvision.view.utility.chat.ChatHelpers;
import com.ipvision.model.MessageDTO;
import com.ipvision.view.utility.chat.ChatHashMap;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.utility.ImageHelpers;

/**
 *
 * @author Shahadat
 */
public class ChatAudioMessagePanel extends JPanel {

    private SingleChatPanel chatPanel;
    private int msgFrom;
    private int chatType;
    private Color chatMsgColor;
    public JLabel timeLabel;
    public JLabel statusLabel;
    public JLabel imageLabel;
    private JPanel timeStatusPanel;
    private MessageDTO messageDTO;
    private JPanel topPanel;
    private JPanel topWrapperPanel;
    private JPanel bottomPanel;
    private JPanel bottomWrapperPanel;
    private JPanel chatMsgPanel;
    private JProgressBar downloadProgressBar;
    private JPopupMenu popupMenu = new JPopupMenu();
    private JMenuItem mnuDelete = new JMenuItem("Delete");

    public ChatAudioMessagePanel(SingleChatPanel chatPanel) {
        this.chatPanel = chatPanel;
        this.messageDTO = chatPanel.messageDTO;
        this.msgFrom = chatPanel.msgFrom;
        this.chatType = chatPanel.chatType;
        this.timeLabel = chatPanel.timeLabel;
        this.statusLabel = chatPanel.statusLabel;
        this.chatMsgColor = chatPanel.chatMsgColor;
        this.topPanel = chatPanel.topPanel;
        this.topWrapperPanel = chatPanel.topWrapperPanel;
        this.bottomPanel = chatPanel.bottomPanel;
        this.bottomWrapperPanel = chatPanel.bottomWrapperPanel;
        this.downloadProgressBar = chatPanel.downloadProgressBar;
        this.timeStatusPanel = chatPanel.timeStatusPanel;
        this.downloadProgressBar.setValue(0);
        this.downloadProgressBar.setToolTipText("Downloading Voice Record");
        this.statusLabel.setText("Voice Record");

        setOpaque(false);
        setLayout(new FlowLayout(msgFrom == SingleChatPanel.TYPE_ME ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
        initContents();
    }

    private void initContents() {
        try {
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
            mainPanel.setOpaque(false);
            this.add(mainPanel);

            chatMsgPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    g2d.setColor(chatMsgColor);
                    int w = getWidth();
                    int h = getHeight();
                    g2d.fillRoundRect(0, 0, w, h, 25, 25);
                }
            };
            chatMsgPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
            chatMsgPanel.setOpaque(false);

            JPanel chatMsgWrapperPanel = new JPanel(new FlowLayout(msgFrom == SingleChatPanel.TYPE_ME ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
            chatMsgWrapperPanel.setOpaque(false);
            if (msgFrom == SingleChatPanel.TYPE_ME) {
                chatMsgWrapperPanel.setBorder(new EmptyBorder(0, 0, 0, 6));
            } else {
                chatMsgWrapperPanel.setBorder(new EmptyBorder(0, 6, 0, 0));
            }
            chatMsgWrapperPanel.add(chatMsgPanel);

            JPanel masterPanel = new JPanel(new BorderLayout());
            masterPanel.setOpaque(false);

            masterPanel.add(topWrapperPanel, BorderLayout.NORTH);
            masterPanel.add(timeStatusPanel, BorderLayout.CENTER);
            masterPanel.add(bottomWrapperPanel, BorderLayout.SOUTH);

            imageLabel = new JLabel();

            if (msgFrom == SingleChatPanel.TYPE_ME) {
                masterPanel.add(chatMsgWrapperPanel, BorderLayout.EAST);
                mainPanel.add(masterPanel);
            } else {
                masterPanel.add(chatMsgWrapperPanel, BorderLayout.WEST);
                mainPanel.add(masterPanel);
            }

            if (messageDTO.getGroupId() != null && messageDTO.getGroupId() > 0) {
                processGroupChat();
            } else {
                processFriendChat();
            }

            mnuDelete.addActionListener(actionListener);
        } catch (Exception ex) {
        }
    }

    public void processFriendChat() {
        if (ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getFriendIdentity()) == null) {
            Map<String, SingleChatPanel> map = new ConcurrentHashMap<String, SingleChatPanel>();
            ChatHashMap.getInstance().getChatSingleChatPanel().put(messageDTO.getFriendIdentity(), map);
        }

        if (msgFrom == SingleChatPanel.TYPE_FRIEND) {
            if (messageDTO.getStatus() == ChatConstants.CHAT_FRIEND_SEEN) {
                //log.warn("## 1");
                timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));
                loadAudio();
            } else {
                //log.warn("## 2");
                ChatService.sendFriendChatSeenConfirmation(messageDTO.getPacketID(), messageDTO.getFriendIdentity());
                messageDTO.setStatus(ChatConstants.CHAT_FRIEND_SEEN);
                timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));

                List<MessageDTO> mList = new ArrayList<MessageDTO>();
                mList.add(messageDTO);
                new InsertIntoFriendMessageTable(mList).start();
                loadAudio();
            }

            ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getFriendIdentity()).put(messageDTO.getPacketID(), chatPanel);

        } else {
            if (messageDTO.getMessageId() > 0) {
                //log.warn("## 3");
                timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));
                if (messageDTO.getStatus() == ChatConstants.CHAT_FRIEND_PENDING) {
                    statusLabel.setText(ChatHelpers.STATUS_TXT_NOT_SENT);
                } else if (messageDTO.getStatus() == ChatConstants.CHAT_FRIEND_SENT) {
                    statusLabel.setText(ChatHelpers.STATUS_TXT_SENT_OFFLINE);
                } else if (messageDTO.getStatus() == ChatConstants.CHAT_FRIEND_DELIVERED) {
                    statusLabel.setText(ChatHelpers.STATUS_TXT_DELIVERED);
                } else if (messageDTO.getStatus() == ChatConstants.CHAT_FRIEND_SEEN) {
                    statusLabel.setText(ChatHelpers.STATUS_TXT_SEEN);
                }

                ChatHashMap.getInstance().getChatStatusMessage().put(messageDTO.getPacketID(), messageDTO);
                ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getFriendIdentity()).put(messageDTO.getPacketID(), chatPanel);
                loadAudio();
            } else {
                //log.warn("## 4");
                timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));
                statusLabel.setText(ChatHelpers.STATUS_TXT_SENDING);
                ChatHashMap.getInstance().getChatStatusMessage().put(messageDTO.getPacketID(), messageDTO);
                ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getFriendIdentity()).put(messageDTO.getPacketID(), chatPanel);

                List<MessageDTO> mList = new ArrayList<MessageDTO>();
                mList.add(messageDTO);
                new InsertIntoFriendMessageTable(mList).start();
                loadAudio();
            }
        }
    }

    public void processGroupChat() {
        if (ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getGroupId() + "") == null) {
            Map<String, SingleChatPanel> map = new ConcurrentHashMap<String, SingleChatPanel>();
            ChatHashMap.getInstance().getChatSingleChatPanel().put(messageDTO.getGroupId() + "", map);
        }

        if (msgFrom == SingleChatPanel.TYPE_FRIEND) {
            if (messageDTO.getStatus() == ChatConstants.CHAT_GROUP_SEEN) {
                //log.warn("## 1");
                timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));
                loadAudio();
            } else {
                //log.warn("## 2");
                ChatService.sendGroupChatSeenConfirmation(messageDTO.getPacketID(), messageDTO.getGroupId(), messageDTO.getUserIdentity());
                messageDTO.setStatus(ChatConstants.CHAT_GROUP_SEEN);
                timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));

                List<MessageDTO> mList = new ArrayList<MessageDTO>();
                mList.add(messageDTO);
                new InsertIntoGroupMessageTable(mList).start();
                loadAudio();
            }

            ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getGroupId() + "").put(messageDTO.getPacketID(), chatPanel);
        } else {
            if (messageDTO.getMessageId() > 0) {
                //log.warn("## 3");
                timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));
                if (messageDTO.getStatus() == ChatConstants.CHAT_GROUP_PENDING) {
                    statusLabel.setText(ChatHelpers.STATUS_TXT_NOT_SENT);
                } else if (messageDTO.getStatus() == ChatConstants.CHAT_GROUP_SENT) {
                    statusLabel.setText(ChatHelpers.STATUS_TXT_SENT);
                } else if (messageDTO.getStatus() == ChatConstants.CHAT_GROUP_DELIVERED) {
                    statusLabel.setText(ChatHelpers.STATUS_TXT_DELIVERED);
                } else if (messageDTO.getStatus() == ChatConstants.CHAT_GROUP_SEEN) {
                    statusLabel.setText(ChatHelpers.STATUS_TXT_SEEN);
                }

                ChatHashMap.getInstance().getChatStatusMessage().put(messageDTO.getPacketID(), messageDTO);
                ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getGroupId() + "").put(messageDTO.getPacketID(), chatPanel);
                loadAudio();
            } else {
                //log.warn("## 4");
                timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));
                statusLabel.setText(ChatHelpers.STATUS_TXT_SENDING);
                ChatHashMap.getInstance().getChatStatusMessage().put(messageDTO.getPacketID(), messageDTO);
                ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getGroupId() + "").put(messageDTO.getPacketID(), chatPanel);

                List<MessageDTO> mList = new ArrayList<MessageDTO>();
                mList.add(messageDTO);
                new InsertIntoGroupMessageTable(mList).start();
                loadAudio();
            }
        }
    }

    public boolean loadAudio() {
        boolean isLoaded = false;
        ImageIcon imageIcon = null;
        imageLabel.removeMouseListener(mouseListener);
        chatMsgPanel.removeAll();
        try {
            String audioName = ImageHelpers.getChatTransferFileName(messageDTO.getMessage());
            String extension = audioName.substring(audioName.lastIndexOf(".") + 1);
            audioName = audioName.replace(extension, "mp3");
            File file = new File(ChatHelpers.CHAT_DIRECTORY.getChatFileDestinationFolder() + "/" + audioName);
            if (file.exists()) {
                isLoaded = true;
                imageIcon = DesignClasses.return_image(GetImages.CHAT_DEFAULT_AUDIO);
                imageLabel.setToolTipText("Play Voice Record");
            } else {
                imageIcon = DesignClasses.return_image(GetImages.CHAT_DEFAULT_FILE_LOADING);
                imageLabel.setToolTipText("Click to Download");
            }

            imageLabel.addMouseListener(mouseListener);
            imageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            imageLabel.setIcon(imageIcon);
            chatMsgPanel.add(imageLabel);
            chatMsgPanel.revalidate();
            chatMsgPanel.repaint();
            this.revalidate();
            this.repaint();
        } catch (Exception ex) {
        }
        return isLoaded;
    }

    MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                if (msgFrom == SingleChatPanel.TYPE_ME) {
                    popupMenu.removeAll();
                    popupMenu.add(mnuDelete);
                    popupMenu.show(e.getComponent(), e.getPoint().x, e.getPoint().y);
                }
            } else {
                try {
                    String audioName = ImageHelpers.getChatTransferFileName(messageDTO.getMessage());
                    String extension = audioName.substring(audioName.lastIndexOf(".") + 1);
                    audioName = audioName.replace(extension, "mp3");
                    File file = new File(ChatHelpers.CHAT_DIRECTORY.getChatFileDestinationFolder() + "/" + audioName);
                    if (file.exists()) {
                        Desktop desktop = Desktop.getDesktop();
                        desktop.open(file);

                        if (msgFrom == SingleChatPanel.TYPE_FRIEND && messageDTO.getTimeout() > 0) {
                            Thread.sleep(3000);
                            timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));
                            chatPanel.showTimerLoader();
                        }
                    } else {
                        ChatService.downloadChatFile(messageDTO, ServerAndPortSettings.getChatContentsDownloadUrl(), ChatHelpers.CHAT_DIRECTORY.getChatFileDestinationFolder(), downloadProgressListener);
                    }
                } catch (Exception ex) {
                    HelperMethods.showPlaneDialogMessage(NotificationMessages.CAN_NOT_FOUND_FILE);
                }
            }
        }
    };

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == mnuDelete) {
                ChatHelpers.deleteChatMessage(messageDTO);
            }
        }
    };

    public DownloadProgressListener downloadProgressListener = new DownloadProgressListener() {
        @Override
        public void onProgress(int percentage, Object tag) {
            if (downloadProgressBar.getParent() == null) {
                chatMsgPanel.removeAll();
                chatMsgPanel.add(downloadProgressBar);
                chatMsgPanel.repaint();
                chatMsgPanel.revalidate();
            }
            downloadProgressBar.setValue(percentage);
        }

        @Override
        public void onSuccess(MessageBaseDTO mbdto) {
            try {
                onProgress(100, null);
                statusLabel.setForeground(RingColorCode.RING_CHAT_SHADE_FG);
                statusLabel.setText("Download Completed");
                loadAudio();

                if (messageDTO.getGroupId() != null && messageDTO.getGroupId() > 0) {
                    GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(messageDTO.getGroupId());
                    if (groupPanel != null && groupPanel.isDisplayable()) {
                        groupPanel.setScrollValue(groupPanel.messagePanel.getHeight());
                    }
                } else {
                    MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(messageDTO.getFriendIdentity());
                    if (friendChatCallPanel != null) {
                        friendChatCallPanel.myFriendChatPanel.setScrollValue(friendChatCallPanel.myFriendChatPanel.messagePanel.getHeight());
                    }
                }
            } catch (Exception ex) {
            }
        }

        @Override
        public void onFailed(MessageBaseDTO mbdto) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Voice Record Downloading Failed");
        }
    };

}
