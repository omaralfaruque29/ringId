/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.chat;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.service.ChatService;
import com.ipvision.constants.RingColorCode;
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
import com.ipvision.view.utility.JOptionPanelBasics;
import com.ipvision.view.utility.ImageHelpers;

/**
 *
 * @author Shahadat
 */
public class ChatFileMessagePanel extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatFileMessagePanel.class);
    private SingleChatPanel chatPanel;
    private int msgFrom;
    private int chatType;
    private Color chatMsgColor;
    public JLabel timeLabel;
    public JLabel statusLabel;
    public JLabel imageLabel;
    private JLabel fileNameLabel;
    private JPanel timeStatusPanel;
    private MessageDTO messageDTO;
    private JPopupMenu popupMenu = new JPopupMenu();
    private JMenuItem mnuDelete = new JMenuItem("Delete");
    private JMenuItem mnuSaveFileas = new JMenuItem("Save File as");
    private JPanel topPanel;
    private JPanel topWrapperPanel;
    private JPanel bottomPanel;
    private JPanel bottomWrapperPanel;
    private JPanel chatMsgPanel;
    private JProgressBar downloadProgressBar;
    private int totalChunk = 0;
    private String fileName = "";

    public ChatFileMessagePanel(SingleChatPanel chatPanel) {
        this.chatPanel = chatPanel;
        this.messageDTO = chatPanel.messageDTO;
        this.msgFrom = chatPanel.msgFrom;
        this.chatType = chatPanel.chatType;
        this.timeLabel = chatPanel.timeLabel;
        this.statusLabel = chatPanel.statusLabel;
        this.fileNameLabel = chatPanel.fileNameLabel;
        this.chatMsgColor = chatPanel.chatMsgColor;
        this.topPanel = chatPanel.topPanel;
        this.topWrapperPanel = chatPanel.topWrapperPanel;
        this.bottomPanel = chatPanel.bottomPanel;
        this.bottomWrapperPanel = chatPanel.bottomWrapperPanel;
        this.downloadProgressBar = chatPanel.downloadProgressBar;
        this.timeStatusPanel = chatPanel.timeStatusPanel;
        this.downloadProgressBar.setValue(0);
        this.downloadProgressBar.setToolTipText("Downloading File");
        this.statusLabel.setText("File");
        this.fileNameLabel.setText(getShortFileName(this.messageDTO.getMessage()));
        this.totalChunk = ChatHelpers.getTotalChunk(this.messageDTO.getMessage());
        this.fileName = ChatHelpers.getMergedChunkFileName(this.messageDTO.getMessage(), this.messageDTO.getPacketID());

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

            timeStatusPanel.add(fileNameLabel);

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
            mnuSaveFileas.addActionListener(actionListener);
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
                loadFile();
            } else {
                //log.warn("## 2");
                ChatService.sendFriendChatSeenConfirmation(messageDTO.getPacketID(), messageDTO.getFriendIdentity());
                messageDTO.setStatus(ChatConstants.CHAT_FRIEND_SEEN);
                timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));

                List<MessageDTO> mList = new ArrayList<MessageDTO>();
                mList.add(messageDTO);
                new InsertIntoFriendMessageTable(mList).start();
                loadFile();
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
                loadFile();
            } else {
                //log.warn("## 4");
                timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));
                statusLabel.setText(ChatHelpers.STATUS_TXT_SENDING);
                ChatHashMap.getInstance().getChatStatusMessage().put(messageDTO.getPacketID(), messageDTO);
                ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getFriendIdentity()).put(messageDTO.getPacketID(), chatPanel);

                List<MessageDTO> mList = new ArrayList<MessageDTO>();
                mList.add(messageDTO);
                new InsertIntoFriendMessageTable(mList).start();
                loadFile();
                //ChatService.sendFriendChat(messageDTO, new PacketResenderHandler(), InternetUnavailablityCheck.isInternetAvailable);
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
                loadFile();
            } else {
                //log.warn("## 2");
                ChatService.sendGroupChatSeenConfirmation(messageDTO.getPacketID(), messageDTO.getGroupId(), messageDTO.getUserIdentity());
                messageDTO.setStatus(ChatConstants.CHAT_GROUP_SEEN);
                timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));

                List<MessageDTO> mList = new ArrayList<MessageDTO>();
                mList.add(messageDTO);
                new InsertIntoGroupMessageTable(mList).start();
                loadFile();
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
                loadFile();
            } else {
                //log.warn("## 4");
                timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));
                statusLabel.setText(ChatHelpers.STATUS_TXT_SENDING);
                ChatHashMap.getInstance().getChatStatusMessage().put(messageDTO.getPacketID(), messageDTO);
                ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getGroupId() + "").put(messageDTO.getPacketID(), chatPanel);

                List<MessageDTO> mList = new ArrayList<MessageDTO>();
                mList.add(messageDTO);
                new InsertIntoGroupMessageTable(mList).start();
                loadFile();
                //ChatService.sendGroupChat(messageDTO, new PacketResenderHandler(), InternetUnavailablityCheck.isInternetAvailable);
            }
        }

    }

    public boolean loadFile() {
        boolean isLoaded = false;
        ImageIcon imageIcon = null;
        imageLabel.removeMouseListener(mouseListener);
        chatMsgPanel.removeAll();
        try {
            File file = new File(ChatHelpers.CHAT_DIRECTORY.getChatFileDestinationFolder() + "/" + fileName);
            if (!file.exists() && msgFrom == SingleChatPanel.TYPE_FRIEND) {
                imageIcon = DesignClasses.return_image(GetImages.CHAT_DEFAULT_FILE_LOADING);
                imageLabel.setToolTipText("Click to Download");
            } else {
                imageIcon = DesignClasses.return_image(GetImages.CHAT_DEFAULT_FILE);
                imageLabel.setToolTipText("Click to Open");
                isLoaded = true;
            }

            imageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            imageLabel.addMouseListener(mouseListener);
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
                } else {
                    File file = new File(ChatHelpers.CHAT_DIRECTORY.getChatFileDestinationFolder() + "/" + fileName);
                    popupMenu.setVisible(false);
                    if (messageDTO.getTimeout() <= 0 && file.exists()) {
                        popupMenu.add(mnuSaveFileas);
                        popupMenu.show(e.getComponent(), e.getPoint().x, e.getPoint().y);
                        popupMenu.setVisible(true);
                    }
                }
            } else {
                try {
                    File file = new File(ChatHelpers.CHAT_DIRECTORY.getChatFileDestinationFolder() + "/" + fileName);
                    if (file.exists()) {
                        Desktop desktop = Desktop.getDesktop();
                        desktop.open(file);
                    } else {
                        if (msgFrom == SingleChatPanel.TYPE_FRIEND) {
                            if (messageDTO.getGroupId() != null && messageDTO.getGroupId() > 0) {
                                ChatHelpers.startGroupChat(messageDTO.getGroupId(), true);
                                List<Integer[]> indexRangeList = ChatHelpers.getMissingChunkIndex(messageDTO.getPacketID(), totalChunk);
                                int totalMissing = 0;

                                if (indexRangeList.size() > 0) {
                                    ChatService.sendGroupFileStreamRequest(messageDTO.getPacketID(), messageDTO.getGroupId(), messageDTO.getUserIdentity(), indexRangeList, totalChunk);
                                    for (Integer[] indexRange : indexRangeList) {
                                        totalMissing += (indexRange[1] - indexRange[0] + 1);
                                    }
                                    onProgress(((totalChunk - totalMissing) * 100) / totalChunk);
                                } else {
                                    ChatHelpers.mergeChunkFile(fileName, messageDTO.getPacketID(), totalChunk);
                                    loadFile();
                                }
                            } else {
                                ChatHelpers.startFriendChat(messageDTO.getFriendIdentity());
                                List<Integer[]> indexRangeList = ChatHelpers.getMissingChunkIndex(messageDTO.getPacketID(), totalChunk);
                                int totalMissing = 0;

                                if (indexRangeList.size() > 0) {
                                    ChatService.sendFriendFileStreamRequest(messageDTO.getPacketID(), messageDTO.getFriendIdentity(), indexRangeList, totalChunk);
                                    for (Integer[] indexRange : indexRangeList) {
                                        totalMissing += (indexRange[1] - indexRange[0] + 1);
                                    }
                                    onProgress(((totalChunk - totalMissing) * 100) / totalChunk);
                                } else {
                                    ChatHelpers.mergeChunkFile(fileName, messageDTO.getPacketID(), totalChunk);
                                    loadFile();
                                }
                            }
                        } else {
                            HelperMethods.showPlaneDialogMessage(NotificationMessages.CAN_NOT_FOUND_FILE);
                        }
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
            } else if (e.getSource() == mnuSaveFileas) {
                File sourceFile = new File(ChatHelpers.CHAT_DIRECTORY.getChatFileDestinationFolder() + "/" + fileName);
                File saveFile = new File(ChatHelpers.CHAT_DIRECTORY.getChatFileDestinationFolder() + "/" + messageDTO.getMessage().split("/")[1]);

                JFileChooser chooser = new JFileChooser();
                JFrame dd = new JFrame();
                ImageHelpers.setAppIcon(dd, null);
                chooser.setDialogTitle("Save File");
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
                        // ex.printStackTrace();
                        log.error("Error in here ==>" + ex.getMessage());
                    }
                }
            }
        }
    };

    public void onProgress(int value) {
        if (downloadProgressBar.getParent() == null) {
            chatMsgPanel.removeAll();
            chatMsgPanel.add(downloadProgressBar);
            chatMsgPanel.repaint();
            chatMsgPanel.revalidate();
        }
        downloadProgressBar.setValue(value);
    }

    public void onSuccess() {
        try {
            onProgress(100);
            statusLabel.setForeground(RingColorCode.RING_CHAT_SHADE_FG);
            statusLabel.setText("Download Completed");
            ChatHelpers.mergeChunkFile(fileName, messageDTO.getPacketID(), totalChunk);
            loadFile();
        } catch (Exception ex) {
        }
    }

    public void onFailed() {
        statusLabel.setForeground(Color.RED);
        statusLabel.setText("File Downloading Failed");
    }

    private String getShortFileName(String message) {
        String extension = "txt";
        String split = message.split("/")[1];
        String ext = HelperMethods.getExtension(split);
        String name = HelperMethods.getImageName(split);
        if (ext != null && ext.length() > 0) {
            extension = ext;
        }

        if (name.length() > 40) {
            name = name.substring(0, 25) + "..." + name.substring(name.length() - 15, name.length());
        }

        return name + "." + extension;
    }
}
