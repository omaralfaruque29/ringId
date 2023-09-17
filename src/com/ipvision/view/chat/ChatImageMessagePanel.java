/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.chat;

import com.ipvision.view.feed.PostChatImageInNewsFeed;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
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
import com.ipvision.view.utility.JOptionPanelBasics;
import com.ipvision.view.utility.ImageHelpers;

/**
 *
 * @author Shahadat
 */
public class ChatImageMessagePanel extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatImageMessagePanel.class);
    private SingleChatPanel chatPanel;
    private int msgFrom;
    private int chatType;
    private Color chatMsgColor;
    public JLabel timeLabel;
    public JLabel statusLabel;
    public JLabel imageLabel;
    private JPanel timeStatusPanel;
    private MessageDTO messageDTO;
    private JPopupMenu popupMenu = new JPopupMenu();
    private JMenuItem mnuDelete = new JMenuItem("Delete");
    private JMenuItem mnuPost = new JMenuItem("Post in News Feed");
    private JMenuItem mnuSaveImageas = new JMenuItem("Save Image as");
    private final int MAX_HIGHT = 200;
    private final int MAX_WEIGHT = 266;
    private final float RATION_WEIGHT_HIGHT = (float) 1.33;
    private BufferedImage bufferedImage, originalBufferedImg;
    private JPanel topPanel;
    private JPanel topWrapperPanel;
    private JPanel bottomPanel;
    private JPanel bottomWrapperPanel;
    private JPanel chatMsgPanel;
    private JProgressBar downloadProgressBar;

    public ChatImageMessagePanel(SingleChatPanel chatPanel) {
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
        this.downloadProgressBar.setToolTipText("Downloading Photo");
        this.statusLabel.setText("Photo");

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

            JPanel masterPanel = new JPanel(new BorderLayout(0, 0));
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
            mnuPost.addActionListener(actionListener);
            mnuSaveImageas.addActionListener(actionListener);
        } catch (Exception ex) {
        }
    }

    public void processFriendChat() {
        if (ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getFriendIdentity()) == null) {
            Map<String, SingleChatPanel> map = new ConcurrentHashMap<String, SingleChatPanel>();
            ChatHashMap.getInstance().getChatSingleChatPanel().put(messageDTO.getFriendIdentity(), map);
        }

        if (msgFrom == SingleChatPanel.TYPE_FRIEND) {
            if (messageDTO.getTimeout() > 0) {
                //log.warn("## 1");
                timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));
                ChatService.sendFriendChatSeenConfirmation(messageDTO.getPacketID(), messageDTO.getFriendIdentity());
                loadImage();
            } else {
                if (messageDTO.getStatus() == ChatConstants.CHAT_FRIEND_SEEN) {
                    //log.warn("## 1");
                    timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));
                    loadImage();
                } else {
                    //log.warn("## 2");
                    ChatService.sendFriendChatSeenConfirmation(messageDTO.getPacketID(), messageDTO.getFriendIdentity());
                    messageDTO.setStatus(ChatConstants.CHAT_FRIEND_SEEN);
                    timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));

                    List<MessageDTO> mList = new ArrayList<MessageDTO>();
                    mList.add(messageDTO);
                    new InsertIntoFriendMessageTable(mList).start();
                    loadImage();
                }
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
                loadImage();
            } else {
                //log.warn("## 4");
                timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));
                statusLabel.setText(ChatHelpers.STATUS_TXT_SENDING);
                ChatHashMap.getInstance().getChatStatusMessage().put(messageDTO.getPacketID(), messageDTO);
                ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getFriendIdentity()).put(messageDTO.getPacketID(), chatPanel);

                List<MessageDTO> mList = new ArrayList<MessageDTO>();
                mList.add(messageDTO);
                new InsertIntoFriendMessageTable(mList).start();
                loadImage();
            }
        }
    }

    public void processGroupChat() {
        if (ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getGroupId() + "") == null) {
            Map<String, SingleChatPanel> map = new ConcurrentHashMap<String, SingleChatPanel>();
            ChatHashMap.getInstance().getChatSingleChatPanel().put(messageDTO.getGroupId() + "", map);
        }

        if (msgFrom == SingleChatPanel.TYPE_FRIEND) {
            if (messageDTO.getTimeout() > 0) {
                //log.warn("## 1");
                timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));
                ChatService.sendGroupChatSeenConfirmation(messageDTO.getPacketID(), messageDTO.getGroupId(), messageDTO.getUserIdentity());
                loadImage();
            } else {
                if (messageDTO.getStatus() == ChatConstants.CHAT_GROUP_SEEN) {
                    //log.warn("## 1");
                    timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));
                    loadImage();
                } else {
                    //log.warn("## 2");
                    ChatService.sendGroupChatSeenConfirmation(messageDTO.getPacketID(), messageDTO.getGroupId(), messageDTO.getUserIdentity());
                    messageDTO.setStatus(ChatConstants.CHAT_GROUP_SEEN);
                    timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));

                    List<MessageDTO> mList = new ArrayList<MessageDTO>();
                    mList.add(messageDTO);
                    new InsertIntoGroupMessageTable(mList).start();
                    loadImage();
                }
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
                loadImage();
            } else {
                //log.warn("## 4");
                timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));
                statusLabel.setText(ChatHelpers.STATUS_TXT_SENDING);
                ChatHashMap.getInstance().getChatStatusMessage().put(messageDTO.getPacketID(), messageDTO);
                ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getGroupId() + "").put(messageDTO.getPacketID(), chatPanel);

                List<MessageDTO> mList = new ArrayList<MessageDTO>();
                mList.add(messageDTO);
                new InsertIntoGroupMessageTable(mList).start();
                loadImage();
            }
        }

    }

    public boolean loadImage() {
        boolean isLoaded = false;
        ImageIcon imageIcon = null;
        imageLabel.removeMouseListener(mouseListener);
        chatMsgPanel.removeAll();
        try {
            originalBufferedImg = ImageHelpers.getChatTransferImage(messageDTO.getMessage());
            if (originalBufferedImg != null) {
                int oW = originalBufferedImg.getWidth();
                int oH = originalBufferedImg.getHeight();
                int nW = oW;
                int nH = oH;

                if (nW > MAX_WEIGHT || nH > MAX_HIGHT) {
                    float ration = (float) oW / (float) oH;
                    if (ration > RATION_WEIGHT_HIGHT) {
                        nW = MAX_WEIGHT;
                        nH = (int) (nW * oH) / oW;
                    } else {
                        nH = MAX_HIGHT;
                        nW = (int) (nH * oW) / oH;
                    }
                }
                bufferedImage = DesignClasses.scaleImage(nW, nH, originalBufferedImg);
                imageIcon = new ImageIcon(bufferedImage);
                imageLabel.setToolTipText("Click to Open");
                if (msgFrom == SingleChatPanel.TYPE_FRIEND && messageDTO.getTimeout() > 0) {
                    chatPanel.showTimerLoader();
                }
                isLoaded = true;
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
                if (msgFrom == SingleChatPanel.TYPE_ME) { //
                    popupMenu.removeAll();
                    popupMenu.add(mnuPost);
                    popupMenu.add(mnuDelete);
                    popupMenu.show(e.getComponent(), e.getPoint().x, e.getPoint().y);
                } else {
                    if (messageDTO.getTimeout() <= 0) {
                        popupMenu.removeAll();
                        popupMenu.add(mnuPost);
                        popupMenu.add(mnuSaveImageas);
                        popupMenu.show(e.getComponent(), e.getPoint().x, e.getPoint().y);
                    }
                }
            } else {
                try {
                    String imageName = ImageHelpers.getChatTransferFileName(messageDTO.getMessage());
                    File file = new File(ChatHelpers.CHAT_DIRECTORY.getChatFileDestinationFolder() + "/" + imageName);
                    if (file.exists()) {
                        Desktop desktop = Desktop.getDesktop();
                        desktop.open(file);
                    } else {
                        ChatService.downloadChatFile(messageDTO, ServerAndPortSettings.getChatContentsDownloadUrl(), ChatHelpers.CHAT_DIRECTORY.getChatFileDestinationFolder(), downloadProgressListener);
                    }
                } catch (Exception ex) {
                    HelperMethods.showPlaneDialogMessage(NotificationMessages.CAN_NOT_FOUND_IMAGE);
                }
            }
        }
    };

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == mnuDelete) {
                ChatHelpers.deleteChatMessage(messageDTO);
            } else if (e.getSource() == mnuPost) {
                try {
                    String imageName = ImageHelpers.getChatTransferFileName(messageDTO.getMessage());
                    File file = new File(ChatHelpers.CHAT_DIRECTORY.getChatFileDestinationFolder() + "/" + imageName);
                    if (file.exists()) {
                        PostChatImageInNewsFeed chatImagePostInNewsFeed = new PostChatImageInNewsFeed(file);
                        chatImagePostInNewsFeed.setVisible(true);
                    } else {
                        HelperMethods.showPlaneDialogMessage(NotificationMessages.CAN_NOT_FOUND_IMAGE);
                    }
                } catch (Exception ex) {
                    HelperMethods.showPlaneDialogMessage(NotificationMessages.CAN_NOT_FOUND_IMAGE);
                }
            } else if (e.getSource() == mnuSaveImageas) {
                File sourceFile = new File(ChatHelpers.CHAT_DIRECTORY.getChatFileDestinationFolder() + "/" + ImageHelpers.getChatTransferFileName(messageDTO.getMessage()));
                File saveFile = new File(ChatHelpers.CHAT_DIRECTORY.getChatFileDestinationFolder() + "/" + messageDTO.getMessage().split("/")[1]);

                JFileChooser chooser = new JFileChooser();
                JFrame dd = new JFrame();
                ImageHelpers.setAppIcon(dd, null);
                chooser.setDialogTitle("Save Photo");
                chooser.setSelectedFile(saveFile);

                int saveValue = chooser.showSaveDialog(dd);
                if (saveValue == chooser.APPROVE_OPTION) {
                    try {
                        saveFile = chooser.getSelectedFile();
                        if (saveFile.exists()) {
                            HelperMethods.showConfirmationDialogMessage("Replace existing file?");
                            if (!JOptionPanelBasics.YES_NO) {
                                return;
                            } else {
                                saveFile.delete();
                            }
                        }

                        FileChannel source = new FileInputStream(sourceFile).getChannel();
                        FileChannel destination = new FileOutputStream(saveFile).getChannel();
                        destination.transferFrom(source, 0, source.size());
                        source.close();
                        destination.close();
                    } catch (IOException ex) {
                        //ex.printStackTrace();
                        log.error("Error in here ==>" + ex.getMessage());
                    }
                }
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
                loadImage();

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
            statusLabel.setText("Photo Downloading Failed");
        }
    };

}
