/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.friendprofile;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.service.ChatService;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.constants.RingColorCode;
import com.ipvision.constants.SettingsConstants;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.model.constants.StatusConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.model.dao.InsertIntoFriendMessageTable;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.JDialogWebcam;
import com.ipvision.service.FriendPresenceInfo;
import com.ipvision.service.GetFriendHistory;
import com.ipvision.model.call.CallLog;
import com.ipvision.view.chat.AudioRecorderWindow;
import com.ipvision.view.chat.ChatDateGroupPanel;
import com.ipvision.view.chat.SingleChatPanel;
import com.ipvision.view.chat.VideoRecorderWindow;
import com.ipvision.model.ActivityDTO;
import com.ipvision.model.MessageDTO;
import com.ipvision.model.RecentDTO;
import com.ipvision.service.utility.chat.PacketResenderHandler;
import com.ipvision.service.chat.ChatFileShareProcessor;
import com.ipvision.service.chat.ChatFileTransferProcessor;
import com.ipvision.service.chat.ChatUserTyping;
import com.ipvision.view.utility.chat.ChatHashMap;
import com.ipvision.view.utility.chat.ChatHelpers;
import com.ipvision.model.dao.RecentChatCallActivityDAO;
import com.ipvision.model.dao.RecentContactDAO;
import com.ipvision.view.AddEmoticonsJDialog;
import com.ipvision.view.myprofile.SingleActivityPanel;
import com.ipvision.view.myprofile.SingleCallPanel;
import com.ipvision.view.image.JDialogStickerPanel;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.view.utility.ContextMenuMouseListener;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.settingsmenu.RingIDSettingsDialog;
import com.ipvision.view.utility.JCustomMenuPopup;
import com.ipvision.view.utility.JOptionPanelBasics;
import com.ipvision.view.utility.RoundedCornerButton;
import com.ipvision.view.utility.ImagePane;
import java.awt.Cursor;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.SwingWorker;

/**
 *
 * @author Faiz
 */
public class MyFriendChatPanel extends ImagePane {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MyFriendChatPanel.class);
    public MyFriendChatCallPanel myFriendChatCallPanel;
    public String friendIdentity;
    public String lastChatSenderIdentity;

    private JPanel chatContainerPanel;
    private JScrollPane scrollContent;
    public JPanel messagePanel;
    private JPanel chatWriterContainer;
    public JTextArea chatWriter;
    //public JLabel lblTimer;
    private JTextArea userTypingArea;
    private JLabel userTypingIcon;
    public ChatUserTyping chatUserTyping;
    public LoadHistoryOnWindowResize onWindowResize;
    public JDialogStickerPanel jDialogStickerPanel;
    private int chatWritercolumns = 44;
    public final JButton btnSendChat = DesignClasses.createImageButton(GetImages.CHAT_SEND, GetImages.CHAT_SEND_H, "Send Message");
    private final JButton btnEmoticon = DesignClasses.createImageButton(GetImages.BUTTON_CHAT_EMOTICON, GetImages.BUTTON_CHAT_EMOTICON_H, "Insert emoticon");
    private final JButton btnChatMoreOption = DesignClasses.createImageButton(GetImages.BUTTON_CHAT_MORE_OPTION, GetImages.BUTTON_CHAT_MORE_OPTION_H, "More Options");
    private final JButton btnRecent = DesignClasses.createImageButton(GetImages.BUTTON_CHAT_RECENT, GetImages.BUTTON_CHAT_RECENT_H, "Recent History");
    private FileDropHandler fileDropHandler = new FileDropHandler();

    private JCustomMenuPopup recentPopup = null;
    private JCustomMenuPopup optionsPopup = null;
    private JCustomMenuPopup.CustomMenu mnuDeleteChat = null;
    private final String MNU_TODAY = "Today";
    private final String MNU_YESTERDAY = "Yesterday";
    private final String MNU_7_DAYS = "7 Days";
    private final String MNU_30_DAYS = "30 Days";
    private final String MNU_3_MONTHS = "3 Months";
    private final String MNU_SEND_PHOTO = "Send Photo";
    private final String MNU_SEND_FILE = "Send File";
    private final String MNU_TAKE_PHOTO = "Take a Photo";
    private final String MNU_VIDEO_MESSAGE = "Send Video Message";
    private final String MNU_MESSAGE_DELETE = "Message Delete";
    private final String MNU_CANCEL_MESSAGE_DELETE = "Cancel Message Delete";
    private final String MNU_VOICE_MESSAGE = "Send Voice Message";
    private final String MNU_CHANGE_BACKGROUND = "Change Background";
    private final String MNU_SELECT_ALL = "Select All";
    private final String MNU_DESELECT_ALL = "Deselect All";

    private JPanel deleteOptionContainer;
    private JPanel timerContainer;
    private JPanel timerWrapper;

    public int timerTime = 0;
    private JCheckBox chkTimerSwitch;
    private ImagePane watchPanel;
    private JLabel btnPlus;
    private JLabel btnMinus;
    private JLabel lblLeft;
    private JLabel lblRight;
    private JLabel lblCenter;
    private JLabel lblUnit;
    private final BufferedImage imgWatch ;
    private final BufferedImage imgWatchDisable ;
    private ImageIcon imgPlus;
    private ImageIcon imgPlusDisable ;
    private ImageIcon imgMinus;
    private ImageIcon imgMinusDisable;
    private String[] secondsArray = {"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
        "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
        "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
        "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80",
        "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100",
        "101", "102", "103", "104", "105", "106", "107", "108", "109", "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120", ""};

    private JButton btnSelectAll = new RoundedCornerButton(MNU_SELECT_ALL, MNU_SELECT_ALL, false);
    private JButton btnDelete = new RoundedCornerButton("Delete", "Delete Selected Message", false);
    private JButton btnCancel = new RoundedCornerButton("Cancel", "Cancel", false);

    final private List<Data> LOADED_HISTORY_TIME = new ArrayList<Data>();
    public boolean isTopLoading = false;
    public boolean isBottomLoading = false;
    public int currentV = 0;
    private int unit_size = 45;
    private final int MAX_CHAT_TEXT_LENGTH = 8000;
    private JPanel recentPanel;
    private JLabel lblSelected = new JLabel("", SwingConstants.RIGHT);
    private int UP_SCROLL_LIMIT = 150;

    //public JLabel lblMessageDate;
    public MyFriendChatPanel(MyFriendChatCallPanel myFriendChatCallPanel) {
        this.myFriendChatCallPanel = myFriendChatCallPanel;
        this.friendIdentity = this.myFriendChatCallPanel.friendIdentity;
        this.setLayout(new BorderLayout(DefaultSettings.HGAP, DefaultSettings.VGAP));
        this.setDropTarget(new FileDropTarget(this, fileDropHandler));
        imgWatch = DesignClasses.return_buffer_image(GetImages.WATCH_H);
        imgWatchDisable = DesignClasses.return_buffer_image(GetImages.WATCH);
        imgPlus = DesignClasses.return_image(GetImages.PLUS_H);
        imgPlusDisable = DesignClasses.return_image(GetImages.PLUS);
        imgMinus = DesignClasses.return_image(GetImages.MINUS_H);
        imgMinusDisable = DesignClasses.return_image(GetImages.MINUS);
        //addFriendChatPanel();
    }

    public void addFriendChatPanel() {
        setChatBackground();

        /* *********************************
         * Duration Panel
         * ********************************/
        recentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 2));
        recentPanel.setPreferredSize(new Dimension(168, 0));
        recentPanel.setOpaque(false);

        JLabel lblRecent = new JLabel("History", SwingConstants.RIGHT);
        lblRecent.setForeground(RingColorCode.RING_CHAT_SHADE_FG);
        lblRecent.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        recentPanel.add(lblRecent);
        recentPanel.add(btnRecent);

        lblSelected.setForeground(RingColorCode.RING_CHAT_SHADE_FG);
        lblSelected.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        recentPanel.add(lblSelected);

//        JPanel messageDatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
//        messageDatePanel.setOpaque(false);
//
//        lblMessageDate = lblMessageDate = DesignClasses.makeAncorLabel("", Font.BOLD, 11);;
//        messageDatePanel.add(lblMessageDate);
        JPanel timerAndRecentWrapper = new JPanel();
        timerAndRecentWrapper.setOpaque(false);
        timerAndRecentWrapper.setLayout(new BorderLayout());
        add(timerAndRecentWrapper, BorderLayout.NORTH);

        timerContainer = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 80));
                int w = getWidth();
                int h = getHeight();
                g2d.fillRect(0, 0, w, h);
            }
        };
        timerContainer.setOpaque(false);
        timerContainer.setVisible(false);
        timerAndRecentWrapper.add(timerContainer, BorderLayout.NORTH);

        timerWrapper = new JPanel(new BorderLayout());
        timerWrapper.setOpaque(false);
        timerWrapper.setPreferredSize(new Dimension(625, 44));
        timerContainer.add(timerWrapper);

        buildTimerPanel();

        JPanel recentContiner = new JPanel(new GridBagLayout());
        recentContiner.setOpaque(false);
        timerAndRecentWrapper.add(recentContiner, BorderLayout.CENTER);

        JPanel recentWrapper = new JPanel(new BorderLayout());
        recentWrapper.setOpaque(false);
        recentWrapper.setPreferredSize(new Dimension(625, 18));
        recentWrapper.add(recentPanel, BorderLayout.WEST);
        //recentContiner.add(messageDatePanel, BorderLayout.CENTER);
        recentContiner.add(recentWrapper);

        /* *********************************
         * Message Container Panel
         * ********************************/
        JPanel scrollWrapPanel = new JPanel(new BorderLayout());
        scrollWrapPanel.setOpaque(false);
        add(scrollWrapPanel, BorderLayout.CENTER);

        messagePanel = new JPanel();
        messagePanel.setOpaque(false);
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        JPanel chatContainerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        chatContainerWrapper.setOpaque(false);
        chatContainerWrapper.add(messagePanel);

        chatContainerPanel = new JPanel();
        chatContainerPanel.setLayout(new BorderLayout(0, 0));
        chatContainerPanel.setOpaque(false);
        chatContainerPanel.add(chatContainerWrapper, BorderLayout.SOUTH);

        scrollContent = DesignClasses.getDefaultScrollPane(chatContainerPanel);// DesignClasses.getDefaultScorllPane(chatContainerPanel);
        scrollContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollWrapPanel.add(scrollContent, BorderLayout.CENTER);
        buildTypingPanel(scrollWrapPanel);

        /* *********************************
         * Chat Writer Panel
         * ********************************/
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        add(bottomPanel, BorderLayout.SOUTH);

        deleteOptionContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 80));
                int w = getWidth();
                int h = getHeight();
                g2d.fillRect(0, 0, w, h);
            }
        };
        deleteOptionContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        deleteOptionContainer.setOpaque(false);
        bottomPanel.add(deleteOptionContainer, BorderLayout.NORTH);

        chatWriterContainer = new JPanel(new BorderLayout(10, 0));
        chatWriterContainer.setOpaque(false);

        JPanel chatWriterPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 40));
                int w = getWidth();
                int h = getHeight();
                g2d.fillRect(0, 0, w, h);
            }
        };
        chatWriterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        chatWriterPanel.setOpaque(false);
        chatWriterPanel.setBorder(new EmptyBorder(9, 0, 9, 0));
        chatWriterPanel.add(chatWriterContainer);

        JPanel chatWriterWrapper = new JPanel(new BorderLayout());
        chatWriterWrapper.setOpaque(false);
        chatWriterWrapper.setBorder(new MatteBorder(1, 0, 0, 0, new Color(0xcac3b9)));
        chatWriterWrapper.add(chatWriterPanel, BorderLayout.CENTER);
        bottomPanel.add(chatWriterWrapper, BorderLayout.CENTER);

        chatWriter = DesignClasses.createJTextArea("", MAX_CHAT_TEXT_LENGTH);
        chatWriter.setBackground(Color.WHITE);
        chatWriter.setColumns(chatWritercolumns);
        chatWriter.setBorder(new EmptyBorder(5, 0, 5, 5));
        chatWriter.setRows(1);
        chatWriter.addMouseListener(new ContextMenuMouseListener());
        chatWriter.setDropTarget(new FileDropTarget(chatWriter, fileDropHandler));
        try {
            chatWriter.setFont(DesignClasses.getDefaultFont(Font.PLAIN, 12f));
        } catch (Exception e) {
        }
        chatWriter.addKeyListener(keyListener);
        chatWriter.addFocusListener(focusListener);
        btnEmoticon.addActionListener(actionListener);
        btnSendChat.addActionListener(actionListener);
        btnChatMoreOption.addActionListener(actionListener);
        btnRecent.addActionListener(actionListener);

        recentPopup = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_CHAT_RECENT);
        recentPopup.addMenu(MNU_TODAY, null);
        recentPopup.addMenu(MNU_YESTERDAY, null);
        recentPopup.addMenu(MNU_7_DAYS, null);
        recentPopup.addMenu(MNU_30_DAYS, null);
        recentPopup.addMenu(MNU_3_MONTHS, null);

        optionsPopup = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_CHAT_OPTIONS);
        optionsPopup.addMenu(MNU_SEND_PHOTO, GetImages.OPTION_PHOTO_MSG);
        optionsPopup.addMenu(MNU_SEND_FILE, GetImages.OPTION_FILE_MSG);
        optionsPopup.addMenu(MNU_TAKE_PHOTO, GetImages.OPTION_TAKE_PHOTO);
        optionsPopup.addMenu(MNU_VIDEO_MESSAGE, GetImages.OPTION_VIDEO_MSG);
        mnuDeleteChat = optionsPopup.addMenu(MNU_MESSAGE_DELETE, GetImages.OPTION_CONTACT_MSG);
        optionsPopup.addMenu(MNU_VOICE_MESSAGE, GetImages.OPTION_VOICE_MSG);
        optionsPopup.addMenu(MNU_CHANGE_BACKGROUND, GetImages.OPTION_TIMER_MSG);

        buildChatDeletePanel();

        scrollContent.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                int containerHeight = messagePanel.getHeight();
                int topHeight = e.getValue();
                int barHeight = scrollContent.getVerticalScrollBar().getHeight();

                if (scrollContent.getVerticalScrollBar().isVisible() && LOADED_HISTORY_TIME.size() > 0) {
                    if (AddEmoticonsJDialog.currentInstance != null && AddEmoticonsJDialog.currentInstance.isVisible()) {
                        AddEmoticonsJDialog.currentInstance.setVisible(false);
                        AddEmoticonsJDialog.currentInstance.dispose();
                        AddEmoticonsJDialog.currentInstance = null;
                    }
//                    if (JCustomMenuPopup.getInstance() != null && JCustomMenuPopup.getInstance().isDisplayable()) {
//                        JCustomMenuPopup.getInstance().setVisible(false);
//                        JCustomMenuPopup.getInstance().dispose();
//                        JCustomMenuPopup.getInstance().setNull();
//                    }
                    if (isTopLoading == false && topHeight < currentV && topHeight <= UP_SCROLL_LIMIT) {
                        // SCROLL TO TOP
                        //System.err.println("SCROLL TO TOP");
                        isTopLoading = true;
                        isBottomLoading = true;
                        new GetFriendHistory(friendIdentity, LOADED_HISTORY_TIME.get(0).getTime(), 3, RecentChatCallActivityDAO.TYPE_SCROLL_UP).start();
                    }
                    currentV = topHeight;
                    //showMessageDateName();
                }
            }
        });

    }

    private MyFriendChatPanel getThis() {
        return this;
    }

    public void setChatBackground() {
        try {
            this.setImage(DesignClasses.return_chatbg_buffer_image(SettingsConstants.FNF_CHAT_BG));
        } catch (Exception ex) {
            try {
                this.setImage(DesignClasses.return_chatbg_buffer_image(SettingsConstants.FNF_DEAFULT_CHAT_BG));
            } catch (Exception exx) {
                this.setBackground(Color.WHITE);
            }
        }
        this.repaint();
    }

    public class SquarePanel extends JPanel {

        @Override
        public Dimension getPreferredSize() {
            int size = Math.min(this.getParent().getHeight(), this.getParent().getWidth());
            return new Dimension(size, size);
        }
    }

    public JPanel chat_box() {
        Component com1 = Box.createHorizontalStrut(30);
        Component com2 = Box.createHorizontalStrut(13);
        final JPanel chatWritingAreaPanel = new JPanel(new BorderLayout());
        chatWritingAreaPanel.setBorder(new EmptyBorder(9, 0, 11, 0));
        chatWritingAreaPanel.setOpaque(false);

        JScrollPane scrollPane = DesignClasses.getDefaultScrollPaneThin(new JScrollPane(chatWriter) {
            @Override
            public void doLayout() {
                super.doLayout();
                int minHeight = 26;
                int maxHeight = 220;
                int prefHeight;

                if (chatWriter.getHeight() > maxHeight) {
                    if (!this.getVerticalScrollBar().isVisible()) {
                        this.getVerticalScrollBar().setVisible(true);
                    }
                    prefHeight = maxHeight + 9 + 9;
                } else {
                    if (this.getVerticalScrollBar().isVisible()) {
                        this.getVerticalScrollBar().setVisible(false);
                    }
                    if (chatWriter.getHeight() < minHeight) {
                        prefHeight = minHeight + 9 + 9;
                    } else {
                        prefHeight = chatWriter.getHeight() + 9 + 9;
                    }
                }

                chatWritingAreaPanel.setPreferredSize(new Dimension(chatWritingAreaPanel.getWidth(), prefHeight));
                chatWritingAreaPanel.revalidate();

            }
        });
        chatWritingAreaPanel.add(scrollPane, BorderLayout.CENTER);

        ChatWrapperPanel contet = new ChatWrapperPanel(RingColorCode.RING_CHAT_AREA_ACTIVE_BG, RingColorCode.RING_CHAT_AREA_ACTIVE_SHADOW_BG);
        contet.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        contet.add(btnChatMoreOption);
        contet.add(com1);
        contet.add(chatWritingAreaPanel);
        contet.add(btnEmoticon);
        contet.add(com2);
        contet.add(btnSendChat);

        return contet;
    }

    private JPanel empty_box() {
        ChatWrapperPanel content = new ChatWrapperPanel(RingColorCode.RING_CHAT_AREA_INACTIVE_BG, RingColorCode.RING_CHAT_AREA_INACTIVE_SHADOW_BG);
        content.setLayout(new GridBagLayout());
        content.setPreferredSize(new Dimension(565, 46));

        JLabel label = new JLabel();
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        label.setForeground(Color.WHITE);
        label.setText("Chat conversation unavailable!");
        content.add(label);
        return content;
    }

    public void buildChatWritingArea() {
        chatWriterContainer.removeAll();
        if (isChatAreaDisplayable()) {
            JPanel text_emotion_panel = chat_box();
            chatWriterContainer.add(text_emotion_panel, BorderLayout.CENTER);
        } else {
            JPanel text_emotion_panel = empty_box();
            chatWriterContainer.add(text_emotion_panel, BorderLayout.CENTER);
        }
        chatWriterContainer.revalidate();
        chatWriterContainer.repaint();
    }

    private boolean isChatAreaDisplayable() {
        boolean flag = true;
        if (myFriendChatCallPanel.getFriendProfileInfo() == null) {
            flag = false;
        } else if (myFriendChatCallPanel.getFriendProfileInfo().getFriendShipStatus().intValue() != StatusConstants.FRIENDSHIP_STATUS_ACCEPTED) {
            flag = false;
        } else if (myFriendChatCallPanel.getFriendProfileInfo().getBlocked() != null && myFriendChatCallPanel.getFriendProfileInfo().getBlocked() > 0) {
            flag = false;
        }
        return flag;
    }

    public void buildTypingPanel(JPanel container) {
        /*
         TYPING PANEL
         */
        JPanel typingWrapPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        typingWrapPanel.setOpaque(false);
        container.add(typingWrapPanel, BorderLayout.SOUTH);

        JPanel typingBorderPanel = new JPanel(new BorderLayout());
        typingBorderPanel.setOpaque(false);
        typingWrapPanel.add(typingBorderPanel);
        JPanel userTypingIconWrapper = new JPanel(new BorderLayout());
        userTypingIconWrapper.setOpaque(false);
        userTypingIcon = new JLabel(DesignClasses.return_image(GetImages.TYPING));
        userTypingIcon.setOpaque(false);
        userTypingIcon.setVisible(false);
        userTypingIconWrapper.add(userTypingIcon, BorderLayout.NORTH);
        typingBorderPanel.add(userTypingIconWrapper, BorderLayout.WEST);

        userTypingArea = new JTextArea();
        try {
            userTypingArea.setFont(DesignClasses.getDefaultFont(Font.PLAIN, 10f));
        } catch (Exception e) {
        }
        userTypingArea.setOpaque(false);
        userTypingArea.setEditable(false);
        userTypingArea.setColumns(51);
        userTypingArea.setForeground(Color.WHITE);
        userTypingArea.setWrapStyleWord(true);
        userTypingArea.setLineWrap(true);
        userTypingArea.setDropTarget(new FileDropTarget(userTypingArea, fileDropHandler));
        typingBorderPanel.add(userTypingArea, BorderLayout.CENTER);
    }

    public void addTyping(String userId) {
        if (chatUserTyping == null || !chatUserTyping.isRunning()) {
            chatUserTyping = new ChatUserTyping(userTypingArea, userTypingIcon);
            chatUserTyping.addUserId(userId);
            chatUserTyping.startThread();
        } else {
            chatUserTyping.addUserId(userId);
        }
    }

    private void buildTimerPanel() {
        JLabel lblTitle = new JLabel("Secret Chat");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        lblTitle.setOpaque(false);

        chkTimerSwitch = new JCheckBox(DesignClasses.return_image(GetImages.OFF));
        chkTimerSwitch.setSelectedIcon(DesignClasses.return_image(GetImages.ON));
        chkTimerSwitch.setOpaque(false);
        chkTimerSwitch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chkTimerSwitch.isSelected()) {
                    //lblLeft.setForeground(Color.WHITE);
                    //lblRight.setForeground(Color.WHITE);
                    lblCenter.setForeground(new Color(0x19e500));
                    lblUnit.setForeground(new Color(0x19e500));
                    watchPanel.setImage(imgWatch);
                    btnPlus.setIcon(imgPlus);
                    btnMinus.setIcon(imgMinus);
                    btnPlus.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    btnMinus.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    timerTime = 5;
                } else {
                    //lblLeft.setForeground(new Color(0xd6d5d5));
                    //lblRight.setForeground(new Color(0xd6d5d5));
                    lblCenter.setForeground(new Color(0xd6d5d5));
                    lblUnit.setForeground(new Color(0xd6d5d5));
                    watchPanel.setImage(imgWatchDisable);
                    btnPlus.setIcon(imgPlusDisable);
                    btnMinus.setIcon(imgMinusDisable);
                    btnPlus.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    btnMinus.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    timerTime = 0;
                }

                lblLeft.setText(secondsArray[4]);
                lblCenter.setText(secondsArray[5]);
                lblRight.setText(secondsArray[6]);
            }
        });
        watchPanel = new ImagePane();
        watchPanel.setLayout(new GridBagLayout());
        watchPanel.setImage(imgWatchDisable);
        watchPanel.setToolTipText("Current Timeout");
        watchPanel.setOpaque(false);

        btnPlus = new JLabel(imgPlusDisable);
        btnPlus.setToolTipText("Next");
        btnPlus.addMouseListener(timerListener);

        btnMinus = new JLabel(imgMinusDisable);
        btnMinus.setToolTipText("Previous");
        btnMinus.addMouseListener(timerListener);

        lblLeft = new JLabel("", SwingConstants.RIGHT);
        lblLeft.setForeground(new Color(0xd6d5d5));
        lblLeft.setPreferredSize(new Dimension(21, 10));
        lblLeft.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));

        lblRight = new JLabel();
        lblRight.setForeground(new Color(0xd6d5d5));
        lblRight.setPreferredSize(new Dimension(21, 10));
        lblRight.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));

        lblCenter = new JLabel("", SwingConstants.CENTER);
        lblCenter.setPreferredSize(new Dimension(30, 15));
        lblCenter.setForeground(new Color(0xd6d5d5));
        lblCenter.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

        lblUnit = new JLabel("Sec", SwingConstants.CENTER);
        lblUnit.setPreferredSize(new Dimension(30, 8));
        lblUnit.setForeground(new Color(0xd6d5d5));
        lblUnit.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));

        JPanel watchWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        watchWrapper.setPreferredSize(new Dimension(30, 28));
        watchWrapper.setBorder(new EmptyBorder(5, 0, 0, 0));
        watchWrapper.setOpaque(false);
        watchWrapper.add(lblCenter);
        watchWrapper.add(lblUnit);
        watchPanel.add(watchWrapper);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 5));
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(180, 0));
        leftPanel.add(lblTitle);
        leftPanel.add(chkTimerSwitch);
        timerWrapper.add(leftPanel, BorderLayout.WEST);

        JPanel emptyPanel = new JPanel();
        emptyPanel.setOpaque(false);
        emptyPanel.setPreferredSize(new Dimension(180, 0));
        timerWrapper.add(emptyPanel, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerPanel.setOpaque(false);
        centerPanel.add(btnMinus);
        centerPanel.add(Box.createRigidArea(new Dimension(20, 20)));
        centerPanel.add(lblLeft);
        centerPanel.add(Box.createRigidArea(new Dimension(5, 20)));
        centerPanel.add(watchPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(5, 20)));
        centerPanel.add(lblRight);
        centerPanel.add(Box.createRigidArea(new Dimension(20, 20)));
        centerPanel.add(btnPlus);
        timerWrapper.add(centerPanel, BorderLayout.CENTER);

        lblLeft.setText(secondsArray[4]);
        lblCenter.setText(secondsArray[5]);
        lblRight.setText(secondsArray[6]);
    }

    public void hideTimerOnInit() {
        if (timerContainer.isVisible() && chkTimerSwitch.isSelected() == false) {
            timerContainer.setVisible(false);
        }
    }

    private void buildChatDeletePanel() {
        deleteOptionContainer.setVisible(false);

        JPanel deleteOptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 7));
        deleteOptionPanel.setOpaque(false);
        deleteOptionContainer.add(deleteOptionPanel);

        btnSelectAll.addActionListener(actionListener);
        btnDelete.addActionListener(actionListener);
        btnCancel.addActionListener(actionListener);

        btnSelectAll.setPreferredSize(new Dimension(110, 25));
        btnDelete.setPreferredSize(new Dimension(110, 25));
        btnCancel.setPreferredSize(new Dimension(110, 25));
        deleteOptionPanel.add(btnSelectAll);
        deleteOptionPanel.add(btnDelete);
        deleteOptionPanel.add(btnCancel);
    }

    MouseListener timerListener = new MouseAdapter() {
        private Timer t;

        @Override
        public void mouseClicked(MouseEvent e) {
            if (chkTimerSwitch.isSelected()) {
                if (e.getSource() == btnPlus) {
                    if (timerTime < 120) {
                        timerTime++;
                        lblLeft.setText(secondsArray[timerTime - 1]);
                        lblCenter.setText(secondsArray[timerTime]);
                        lblRight.setText(secondsArray[timerTime + 1]);
                        btnMinus.setIcon(imgMinus);
                    }
                } else if (e.getSource() == btnMinus) {
                    if (timerTime > 1) {
                        timerTime--;
                        lblLeft.setText(secondsArray[timerTime - 1]);
                        lblCenter.setText(secondsArray[timerTime]);
                        lblRight.setText(secondsArray[timerTime + 1]);
                        btnPlus.setIcon(imgPlus);
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (chkTimerSwitch.isSelected()) {
                if (t == null) {
                    t = new Timer();
                }
                final MouseEvent event = e;
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mouseClicked(event);
                    }
                }, 500, 100);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (t != null) {
                t.cancel();
                t = null;
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (chkTimerSwitch.isSelected()) {
                if (e.getSource() == btnPlus) {
                    btnPlus.setIcon(imgPlusDisable);
                } else if (e.getSource() == btnMinus) {
                    btnMinus.setIcon(imgMinusDisable);
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (chkTimerSwitch.isSelected()) {
                if (e.getSource() == btnPlus) {
                    if (timerTime != 120) {
                        btnPlus.setIcon(imgPlus);
                    }
                } else if (e.getSource() == btnMinus) {
                    if (timerTime != 1) {
                        btnMinus.setIcon(imgMinus);
                    }
                }
            }
        }
    };

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnEmoticon) {
                if (jDialogStickerPanel == null) {
                    jDialogStickerPanel = new JDialogStickerPanel(chatWriter, friendIdentity);
                }
                jDialogStickerPanel.setVisible(btnEmoticon, 282);
            } else if (e.getSource() == btnSendChat) {
                if (chatWriter.getText().trim().length() > 0 && (chatWriter.getText().length() > MAX_CHAT_TEXT_LENGTH) == false) {
                    chatWriter.setText(chatWriter.getText());
                    buildChatPacket();
                    chatWriter.setText("");
                    chatWriter.grabFocus();
                }
            } else if (e.getSource() == btnChatMoreOption) {
                optionsPopup.setVisible(btnChatMoreOption, 8, optionsPopup.height);
            } else if (e.getSource() == btnRecent) {
                recentPopup.setVisible(btnRecent, 20, - 3);
            } else if (e.getSource() == btnSelectAll) {
                if (btnSelectAll.getText().equalsIgnoreCase(MNU_SELECT_ALL)) {
                    btnSelectAll.setText(MNU_DESELECT_ALL);
                    btnSelectAll.setToolTipText(MNU_DESELECT_ALL);
                    showHideChatSelection(true, true);
                } else {
                    btnSelectAll.setText(MNU_SELECT_ALL);
                    btnSelectAll.setToolTipText(MNU_SELECT_ALL);
                    showHideChatSelection(true, false);
                }
            } else if (e.getSource() == btnDelete) {
                deleteChatMessage();
            } else if (e.getSource() == btnCancel) {
                mnuDeleteChat.setText(MNU_MESSAGE_DELETE);
                showHideChatSelection(false, false);
            }
        }
    };

    FocusListener focusListener = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            ChatHelpers.startFriendChat(myFriendChatCallPanel.friendIdentity);

            if (!timerContainer.isVisible()) {
                timerContainer.setLocation(timerContainer.getX(), - 44);
                timerContainer.setVisible(true);

                SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                    @Override
                    protected String doInBackground() throws Exception {
                        int i = - 44;
                        while (i <= 0) {
                            try {
                                Thread.sleep(10);
                            } catch (Exception e) {
                            }

                            timerContainer.setLocation((int) timerContainer.getX(), (int) i);
                            invalidate();
                            i += 2;
                        }
                        timerContainer.setLocation(timerContainer.getX(), 0);
                        return null;
                    }
                };
                worker.execute();
            }
        }
    };

    KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            if (chatWriter.getText().length() > MAX_CHAT_TEXT_LENGTH) {
                e.consume();
            } else {
                int length = chatWriter.getText().length();
                if (length > 4 && length % 5 == 0) {
                    ChatService.sendFriendChatTyping(myFriendChatCallPanel.friendIdentity);
                }
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && (e.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
                    if (chatWriter.getText().length() > MAX_CHAT_TEXT_LENGTH) {
                        e.consume();
                    } else {
                        chatWriter.setText(chatWriter.getText() + "\n");
                    }
                } else if (chatWriter.getText().trim().length() > 0) {
                    e.consume();
                    btnSendChat.doClick();
                } else if (chatWriter.getText().length() > 0) {
                    e.consume();
                    chatWriter.setText("");
                } else {
                    e.consume();
                }
                chatWriter.grabFocus();
            }
        }
    };

    MouseListener menuListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            if (menu.text.equalsIgnoreCase(MNU_TODAY)) {
                lblSelected.setText(MNU_TODAY);
                loadHistoryOnRecent(RecentContactDAO._TODAY);
                recentPopup.hideThis();
            } else if (menu.text.equalsIgnoreCase(MNU_YESTERDAY)) {
                lblSelected.setText(MNU_YESTERDAY);
                loadHistoryOnRecent(RecentContactDAO._YESTERDAY);
                recentPopup.hideThis();
            } else if (menu.text.equalsIgnoreCase(MNU_7_DAYS)) {
                lblSelected.setText(MNU_7_DAYS);
                loadHistoryOnRecent(RecentContactDAO._7_DAYS);
                recentPopup.hideThis();
            } else if (menu.text.equalsIgnoreCase(MNU_30_DAYS)) {
                lblSelected.setText(MNU_30_DAYS);
                loadHistoryOnRecent(RecentContactDAO._30_DAYS);
                recentPopup.hideThis();
            } else if (menu.text.equalsIgnoreCase(MNU_3_MONTHS)) {
                lblSelected.setText(MNU_3_MONTHS);
                loadHistoryOnRecent(RecentContactDAO._90_DAYS);
                recentPopup.hideThis();
            } else if (menu.text.equalsIgnoreCase(MNU_SEND_PHOTO)) {
                JFrame dd = new JFrame();
                FileDialog fc = new FileDialog(dd, "Browse", FileDialog.LOAD);
                fc.setFile("*.jpg;*.jpeg;*.png;*.gif");
                fc.setMultipleMode(true);
                fc.show();

                if (fc.getFiles() != null && fc.getFiles().length > 0) {
                    for (int i = 0; i < fc.getFiles().length; i++) {
                        new ChatFileShareProcessor(fc.getFiles()[i], myFriendChatCallPanel, ChatConstants.TYPE_IMAGE_FILE_DIRECTORY, timerTime).start();
                    }
                }
            } else if (menu.text.equalsIgnoreCase(MNU_SEND_FILE)) {
                JFrame dd = new JFrame();
                FileDialog fc = new FileDialog(dd, "Browse", FileDialog.LOAD);
                //fc.setFile("*.jpg;*.jpeg;*.png;*.gif");
                fc.setMultipleMode(true);
                fc.show();

                if (fc.getFiles() != null && fc.getFiles().length > 0) {
                    for (int i = 0; i < fc.getFiles().length; i++) {
                        new ChatFileTransferProcessor(myFriendChatCallPanel, fc.getFiles()[i]).start();
                    }
                }
            } else if (menu.text.equalsIgnoreCase(MNU_TAKE_PHOTO)) {
                JDialogWebcam webcam = new JDialogWebcam();
                if (JDialogWebcam.fileToUpload != null && JDialogWebcam.fileToUpload.exists()) {
                    new ChatFileShareProcessor(JDialogWebcam.fileToUpload, myFriendChatCallPanel, ChatConstants.TYPE_IMAGE_FILE_RING_CAMERA, timerTime).start();
                }
            } else if (menu.text.equalsIgnoreCase(MNU_VOICE_MESSAGE)) {
                optionsPopup.hideThis();
                if (AudioRecorderWindow.instance != null) {
                    AudioRecorderWindow.instance.hideForceFully();
                }
                if (VideoRecorderWindow.instance != null) {
                    VideoRecorderWindow.instance.hideForceFully();
                }
                AudioRecorderWindow recorderWindow = new AudioRecorderWindow(friendIdentity);
                recorderWindow.showRecodingVoiceChat();
            } else if (menu.text.equalsIgnoreCase(MNU_VIDEO_MESSAGE)) {
                optionsPopup.hideThis();
                if (VideoRecorderWindow.instance != null) {
                    VideoRecorderWindow.instance.hideForceFully();
                }
                if (AudioRecorderWindow.instance != null) {
                    AudioRecorderWindow.instance.hideForceFully();
                }
                VideoRecorderWindow recorderWindow = new VideoRecorderWindow(friendIdentity);
                recorderWindow.showRecodingVideoChat();
            } else if (menu.text.equalsIgnoreCase(MNU_MESSAGE_DELETE)) {
                optionsPopup.hideThis();
                showHideChatSelection(true, false);
                mnuDeleteChat.setText(MNU_CANCEL_MESSAGE_DELETE);
            } else if (menu.text.equalsIgnoreCase(MNU_CANCEL_MESSAGE_DELETE)) {
                optionsPopup.hideThis();
                showHideChatSelection(false, false);
                mnuDeleteChat.setText(MNU_MESSAGE_DELETE);
            } else if (menu.text.equalsIgnoreCase(MNU_MESSAGE_DELETE)) {
                optionsPopup.hideThis();
                btnSelectAll.setText(MNU_SELECT_ALL);
                btnSelectAll.setToolTipText(MNU_SELECT_ALL);
                showHideChatSelection(true, false);
                mnuDeleteChat.setText(MNU_CANCEL_MESSAGE_DELETE);
            } else if (menu.text.equalsIgnoreCase(MNU_CANCEL_MESSAGE_DELETE)) {
                optionsPopup.hideThis();
                btnSelectAll.setText(MNU_SELECT_ALL);
                btnSelectAll.setToolTipText(MNU_SELECT_ALL);
                showHideChatSelection(false, false);
                mnuDeleteChat.setText(MNU_MESSAGE_DELETE);
            } else if (menu.text.equalsIgnoreCase(MNU_CHANGE_BACKGROUND)) {
                RingIDSettingsDialog ringIDSettingsDialog = GuiRingID.getInstance().getTopMenuBar().getRingIDSettingsDialog();
                if (ringIDSettingsDialog == null) {
                    ringIDSettingsDialog = new RingIDSettingsDialog();
                    GuiRingID.getInstance().getTopMenuBar().setRingIDSettingsDialog(ringIDSettingsDialog);
                }
                MouseEvent me = new MouseEvent(ringIDSettingsDialog.mnuChatBackgroundSetting, 0, 0, 0, 10, 10, 1, false);
                for (MouseListener ml : ringIDSettingsDialog.mnuChatBackgroundSetting.getMouseListeners()) {
                    ml.mouseClicked(me);
                }
                ringIDSettingsDialog.setVisible(true);
                ringIDSettingsDialog.toFront();
            }
            menu.setMouseExited();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            menu.setMouseEntered();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            menu.setMouseExited();
        }
    };

    public class FileDropTarget extends DropTarget {

        public FileDropTarget(JComponent component, FileDropHandler fileDropHandler) {
            super(component, DnDConstants.ACTION_COPY_OR_MOVE, fileDropHandler, true);
        }

    }

    public class FileDropHandler implements DropTargetListener {

        private List<String> EXT_IMAGE = new ArrayList<String>();
        private final int WAITING = 1;
        private final int ACCEPT = 2;
        private final int REJECT = 3;
        private int state = WAITING;

        public FileDropHandler() {
            EXT_IMAGE.add("jpg");
            EXT_IMAGE.add("jpeg");
            EXT_IMAGE.add("png");
        }

        @Override
        public void dragEnter(DropTargetDragEvent dtde) {
            state = REJECT;
            Transferable t = dtde.getTransferable();
            Object td = null;

            if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                try {
                    td = t.getTransferData(DataFlavor.javaFileListFlavor);
                    if (td instanceof List) {
                        state = ACCEPT;
                        for (Object value : ((List) td)) {
                            if (value instanceof File) {
                                File file = (File) value;
                                String extension = HelperMethods.getExtension(file.getName().toLowerCase());
                                if (!EXT_IMAGE.contains(extension)) {
                                    state = REJECT;
                                    break;
                                }
                            } else {
                                state = REJECT;
                                break;
                            }
                        }
                    }
                } catch (Exception ex) {
                    //ex.printStackTrace();
                    log.error("Error in here ==>" + ex.getMessage());
                }
            }
            if (state == ACCEPT) {
                dtde.acceptDrag(DnDConstants.ACTION_COPY);
            } else {
                dtde.rejectDrag();
            }
        }

        @Override
        public void dragOver(DropTargetDragEvent dtde) {
        }

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {
        }

        @Override
        public void dragExit(DropTargetEvent dte) {
            state = WAITING;
        }

        @Override
        public void drop(DropTargetDropEvent dtde) {
            try {
                ChatHelpers.startFriendChat(myFriendChatCallPanel.friendIdentity);

                Transferable t = dtde.getTransferable();
                if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : files) {
                        new ChatFileShareProcessor(file, myFriendChatCallPanel, ChatConstants.TYPE_IMAGE_FILE_DIRECTORY, timerTime).start();
                    }
                }
            } catch (Exception ex) {
                // ex.printStackTrace();
                log.error("Error in here ==>" + ex.getMessage());
            }
            state = WAITING;
        }
    }

    private void buildChatPacket() {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageType(ChatConstants.TYPE_PLAIN_MSG);
        messageDTO.setPacketType(ChatConstants.CHAT_FRIEND);
        if (timerTime > 0) {
            messageDTO.setTimeout(timerTime);
        }
        String packetID = SendToServer.getRanDomPacketID();
        messageDTO.setPacketID(packetID);
        messageDTO.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
        messageDTO.setFriendIdentity(friendIdentity);
        messageDTO.setMessage(chatWriter.getText());
        messageDTO.setStatus(ChatConstants.CHAT_FRIEND_PENDING);
        messageDTO.setSystemDate(System.currentTimeMillis());
        ChatService.sendFriendChat(messageDTO, new PacketResenderHandler(), InternetUnavailablityCheck.isInternetAvailable);
        //addSinglePanelInChatContainerPanel(messageDTO);
    }

    public synchronized boolean addSinglePanelInChatContainerPanel(final MessageDTO messageDTO) {
        boolean isLoaded = false;
        try {
            isTopLoading = true;
            isBottomLoading = true;

            RecentDTO recentDTO = new RecentDTO();
            recentDTO.setTime(messageDTO.getMessageDate() > 0 ? messageDTO.getMessageDate() : System.currentTimeMillis());
            recentDTO.setHistoryDateName(ChatHelpers.CHAT_DATE_FORMAT.format(new Date(recentDTO.getTime())));
            recentDTO.setMessageDTO(messageDTO);

            int size = LOADED_HISTORY_TIME.size();
            if (size <= 0 || recentDTO.getTime() > LOADED_HISTORY_TIME.get(size - 1).getTime()) {
                //System.err.println("1");
                setChatDateGroupInRear(recentDTO);

                SingleChatPanel singleChatPanel = new SingleChatPanel(messagePanel, messageDTO, lastChatSenderIdentity);
                singleChatPanel.showSelectBox(mnuDeleteChat.text.equalsIgnoreCase(MNU_CANCEL_MESSAGE_DELETE), btnSelectAll.getText().equalsIgnoreCase(MNU_DESELECT_ALL));
                messagePanel.add(singleChatPanel);
                LOADED_HISTORY_TIME.add(new Data(recentDTO));
                isLoaded = true;
                lastChatSenderIdentity = messageDTO.getUserIdentity();

                messagePanel.revalidate();
                messagePanel.repaint();
                setScrollValue(messagePanel.getHeight());
            } else if (recentDTO.getTime() > LOADED_HISTORY_TIME.get(0).getTime()) {
                //System.err.println("2");
                int componentIndex = getEstimatedComponentIndex(recentDTO.getTime());
                int dataIndex = getEstimatedDataIndex(recentDTO.getTime());
                String prevChatSenderIdentity = getPrevChatSenderIdentity(componentIndex);

                if (setChatDateGroupInBetween(recentDTO, dataIndex, componentIndex)) {
                    componentIndex++;
                }

                SingleChatPanel singleChatPanel = new SingleChatPanel(messagePanel, recentDTO.getMessageDTO(), prevChatSenderIdentity);
                singleChatPanel.showSelectBox(mnuDeleteChat.text.equalsIgnoreCase(MNU_CANCEL_MESSAGE_DELETE), btnSelectAll.getText().equalsIgnoreCase(MNU_DESELECT_ALL));
                messagePanel.add(singleChatPanel, componentIndex);
                LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
                isLoaded = true;
                componentIndex++;

                setChatSenderIdentityInRear(recentDTO.getMessageDTO().getUserIdentity(), componentIndex);
            } else if (messagePanel.getHeight() <= scrollContent.getHeight()) {
                //System.err.println("3");
                setChatDateGroupInFront(recentDTO);
                int componentIndex = 1;
                int dataIndex = 0;

                SingleChatPanel singleChatPanel = new SingleChatPanel(messagePanel, recentDTO.getMessageDTO(), null);
                singleChatPanel.showSelectBox(mnuDeleteChat.text.equalsIgnoreCase(MNU_CANCEL_MESSAGE_DELETE), btnSelectAll.getText().equalsIgnoreCase(MNU_DESELECT_ALL));
                messagePanel.add(singleChatPanel, componentIndex);
                LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
                isLoaded = true;
                componentIndex++;

                setChatSenderIdentityInRear(recentDTO.getMessageDTO().getUserIdentity(), componentIndex);
            }

        } catch (Exception e) {
            isTopLoading = false;
            isBottomLoading = false;
        }

        return isLoaded;
    }

    public synchronized void addSinglePanelInChatContainerPanel(final CallLog callLog) {
        try {
            isTopLoading = true;
            isBottomLoading = true;

            RecentDTO recentDTO = new RecentDTO();
            recentDTO.setTime(callLog.getCallingTime());
            recentDTO.setHistoryDateName(ChatHelpers.CHAT_DATE_FORMAT.format(new Date(recentDTO.getTime())));
            recentDTO.setCallLog(callLog);

            int size = LOADED_HISTORY_TIME.size();
            if (size <= 0 || recentDTO.getTime() > LOADED_HISTORY_TIME.get(size - 1).getTime()) {
                setChatDateGroupInRear(recentDTO);

                messagePanel.add(new SingleCallPanel(callLog));
                LOADED_HISTORY_TIME.add(new Data(recentDTO));

                messagePanel.revalidate();
                messagePanel.repaint();
                setScrollValue(messagePanel.getHeight());
            } else if (recentDTO.getTime() > LOADED_HISTORY_TIME.get(0).getTime()) {
                int componentIndex = getEstimatedComponentIndex(recentDTO.getTime());
                int dataIndex = getEstimatedDataIndex(recentDTO.getTime());

                if (setChatDateGroupInBetween(recentDTO, dataIndex, componentIndex)) {
                    componentIndex++;
                }

                messagePanel.add(new SingleCallPanel(recentDTO.getCallLog()), componentIndex);
                LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
            } else if (messagePanel.getHeight() <= scrollContent.getHeight()) {
                setChatDateGroupInFront(recentDTO);
                int componentIndex = 1;
                int dataIndex = 0;

                messagePanel.add(new SingleCallPanel(recentDTO.getCallLog()), componentIndex);
                LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
            }

        } catch (Exception e) {
            isTopLoading = false;
            isBottomLoading = false;
        }
    }

    public synchronized void addSinglePanelInChatContainerPanel(final ActivityDTO activityDTO) {
        try {
            isTopLoading = true;
            isBottomLoading = true;

            RecentDTO recentDTO = new RecentDTO();
            recentDTO.setTime(activityDTO.getUpdateTime());
            recentDTO.setHistoryDateName(ChatHelpers.CHAT_DATE_FORMAT.format(new Date(recentDTO.getTime())));
            recentDTO.setActivityDTO(activityDTO);

            int size = LOADED_HISTORY_TIME.size();
            if (size <= 0 || recentDTO.getTime() > LOADED_HISTORY_TIME.get(size - 1).getTime()) {
                setChatDateGroupInRear(recentDTO);

                messagePanel.add(new SingleActivityPanel(activityDTO));
                LOADED_HISTORY_TIME.add(new Data(recentDTO));

                messagePanel.revalidate();
                messagePanel.repaint();
                setScrollValue(messagePanel.getHeight());
            } else if (recentDTO.getTime() > LOADED_HISTORY_TIME.get(0).getTime()) {
                int componentIndex = getEstimatedComponentIndex(recentDTO.getTime());
                int dataIndex = getEstimatedDataIndex(recentDTO.getTime());

                if (setChatDateGroupInBetween(recentDTO, dataIndex, componentIndex)) {
                    componentIndex++;
                }

                messagePanel.add(new SingleActivityPanel(recentDTO.getActivityDTO()), componentIndex);
                LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
            } else if (messagePanel.getHeight() <= scrollContent.getHeight()) {
                setChatDateGroupInFront(recentDTO);
                int componentIndex = 1;
                int dataIndex = 0;

                messagePanel.add(new SingleActivityPanel(recentDTO.getActivityDTO()), componentIndex);
                LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
            }

        } catch (Exception e) {
            isTopLoading = false;
            isBottomLoading = false;
        }
    }

    public synchronized void addHistory(List<RecentDTO> chatHistoryList, Integer limit) {
        try {
            if (messagePanel != null) {
                messagePanel.removeAll();
                messagePanel.revalidate();
                messagePanel.setVisible(false);
                LOADED_HISTORY_TIME.clear();
                lastChatSenderIdentity = null;
                boolean isFirstChatMsg = true;
                if (ChatHashMap.getInstance().getChatSingleChatPanel().get(friendIdentity) != null) {
                    ChatHashMap.getInstance().getChatSingleChatPanel().get(friendIdentity).clear();
                }

                for (RecentDTO recentDTO : chatHistoryList) {
                    if (recentDTO.getType() == RecentChatCallActivityDAO.TYPE_FRIEND_CHAT) {
                        setChatDateGroupInFront(recentDTO);
                        int componentIndex = 1;
                        int dataIndex = 0;

                        SingleChatPanel singleChatPanel = new SingleChatPanel(messagePanel, recentDTO.getMessageDTO(), null);
                        singleChatPanel.showSelectBox(mnuDeleteChat.text.equalsIgnoreCase(MNU_CANCEL_MESSAGE_DELETE), btnSelectAll.getText().equalsIgnoreCase(MNU_DESELECT_ALL));
                        messagePanel.add(singleChatPanel, componentIndex);
                        LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
                        componentIndex++;

                        setChatSenderIdentityInRear(recentDTO.getMessageDTO().getUserIdentity(), componentIndex);
                        if (isFirstChatMsg) {
                            lastChatSenderIdentity = recentDTO.getMessageDTO().getUserIdentity();
                            isFirstChatMsg = false;
                        }
                    } else if (recentDTO.getType() == RecentChatCallActivityDAO.TYPE_CALL_LOG) {
                        setChatDateGroupInFront(recentDTO);
                        int componentIndex = 1;
                        int dataIndex = 0;

                        messagePanel.add(new SingleCallPanel(recentDTO.getCallLog()), componentIndex);
                        LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
                    } else if (recentDTO.getType() == RecentChatCallActivityDAO.TYPE_FRIEND_ACTIVITY) {
                        setChatDateGroupInFront(recentDTO);
                        int componentIndex = 1;
                        int dataIndex = 0;

                        messagePanel.add(new SingleActivityPanel(recentDTO.getActivityDTO()), componentIndex);
                        LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
                    }

                    messagePanel.revalidate();
                    if (limit != null && limit > 0 && messagePanel.getHeight() > scrollContent.getHeight() + UP_SCROLL_LIMIT) {
                        break;
                    }
                }

                messagePanel.setVisible(true);
                messagePanel.revalidate();
                messagePanel.repaint();
                Thread.sleep(100);
                setScrollValue(messagePanel.getHeight());
            }
        } catch (Exception e) {
            isTopLoading = false;
            isBottomLoading = false;
        }
    }

    public synchronized void addHistoryByScrollUp(List<RecentDTO> chatHistoryList) {
        try {
            if (messagePanel != null && chatHistoryList.size() > 0) {
                int height = messagePanel.getHeight();
                for (RecentDTO recentDTO : chatHistoryList) {
                    if (recentDTO.getType() == RecentChatCallActivityDAO.TYPE_FRIEND_CHAT) {
                        setChatDateGroupInFront(recentDTO);
                        int componentIndex = 1;
                        int dataIndex = 0;

                        SingleChatPanel singleChatPanel = new SingleChatPanel(messagePanel, recentDTO.getMessageDTO(), null);
                        singleChatPanel.showSelectBox(mnuDeleteChat.text.equalsIgnoreCase(MNU_CANCEL_MESSAGE_DELETE), btnSelectAll.getText().equalsIgnoreCase(MNU_DESELECT_ALL));
                        messagePanel.add(singleChatPanel, componentIndex);
                        LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
                        componentIndex++;

                        setChatSenderIdentityInRear(recentDTO.getMessageDTO().getUserIdentity(), componentIndex);
                    } else if (recentDTO.getType() == RecentChatCallActivityDAO.TYPE_CALL_LOG) {
                        setChatDateGroupInFront(recentDTO);
                        int componentIndex = 1;
                        int dataIndex = 0;

                        messagePanel.add(new SingleCallPanel(recentDTO.getCallLog()), componentIndex);
                        LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
                    } else if (recentDTO.getType() == RecentChatCallActivityDAO.TYPE_FRIEND_ACTIVITY) {
                        setChatDateGroupInFront(recentDTO);
                        int componentIndex = 1;
                        int dataIndex = 0;

                        messagePanel.add(new SingleActivityPanel(recentDTO.getActivityDTO()), componentIndex);
                        LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
                    }

                    messagePanel.revalidate();
                    if (messagePanel.getHeight() > height + UP_SCROLL_LIMIT) {
                        break;
                    }
                }

                messagePanel.repaint();
                setScrollValue(messagePanel.getHeight() - height + UP_SCROLL_LIMIT);
            } else {
                isTopLoading = false;
                isBottomLoading = false;
            }
        } catch (Exception e) {
            isTopLoading = false;
            isBottomLoading = false;
        }
    }

    public synchronized void addHistoryByScrollDown(List<RecentDTO> chatHistoryList) {
        try {
            if (messagePanel != null && chatHistoryList.size() > 0) {
                for (RecentDTO recentDTO : chatHistoryList) {
                    if (recentDTO.getType() == RecentChatCallActivityDAO.TYPE_FRIEND_CHAT) {
                        setChatDateGroupInRear(recentDTO);

                        SingleChatPanel singleChatPanel = new SingleChatPanel(messagePanel, recentDTO.getMessageDTO(), lastChatSenderIdentity);
                        singleChatPanel.showSelectBox(mnuDeleteChat.text.equalsIgnoreCase(MNU_CANCEL_MESSAGE_DELETE), btnSelectAll.getText().equalsIgnoreCase(MNU_DESELECT_ALL));
                        messagePanel.add(singleChatPanel);
                        LOADED_HISTORY_TIME.add(new Data(recentDTO));

                        lastChatSenderIdentity = recentDTO.getMessageDTO().getUserIdentity();
                    } else if (recentDTO.getType() == RecentChatCallActivityDAO.TYPE_CALL_LOG) {
                        setChatDateGroupInRear(recentDTO);

                        messagePanel.add(new SingleCallPanel(recentDTO.getCallLog()));
                        LOADED_HISTORY_TIME.add(new Data(recentDTO));
                    } else if (recentDTO.getType() == RecentChatCallActivityDAO.TYPE_FRIEND_ACTIVITY) {
                        setChatDateGroupInRear(recentDTO);

                        messagePanel.add(new SingleActivityPanel(recentDTO.getActivityDTO()));
                        LOADED_HISTORY_TIME.add(new Data(recentDTO));
                    }
                }

                messagePanel.revalidate();
                messagePanel.repaint();
                setScrollValue(messagePanel.getHeight());
            } else {
                isTopLoading = false;
                isBottomLoading = false;
            }
        } catch (Exception e) {
            isTopLoading = false;
            isBottomLoading = false;
        }
    }

    public void setScrollValue(final int value) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //showMessageDateName();
                scrollContent.getVerticalScrollBar().setValue(value);
                isTopLoading = false;
                isBottomLoading = false;
            }
        });
    }

    ComponentListener componentListener = new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            if (onWindowResize == null || !onWindowResize.isRunning()) {
                onWindowResize = new LoadHistoryOnWindowResize();
                onWindowResize.resizing();
                onWindowResize.startThread();
            } else {
                onWindowResize.resizing();
            }
        }
    };

    public void loadInitHistory() {
        new FriendPresenceInfo(friendIdentity).start();
        new loadHistoryOnInitialization(null).start();
    }

    /*
     Need to call only from history remove
     */
    public void loadInitHistory(Boolean donNotCheckWindowDisplayable) {
        LOADED_HISTORY_TIME.clear();
        new loadHistoryOnInitialization(donNotCheckWindowDisplayable).start();
    }

    private class loadHistoryOnInitialization extends Thread {

        private Boolean donNotCheckWindowDisplayable;

        public loadHistoryOnInitialization(Boolean donNotCheckWindowDisplayable) {
            this.donNotCheckWindowDisplayable = donNotCheckWindowDisplayable;
        }

        @Override
        public void run() {
            try {
                //System.err.println("loadHistoryOnInitialization");
                getThis().removeComponentListener(componentListener);
                isTopLoading = true;
                isBottomLoading = true;

                if (donNotCheckWindowDisplayable == null || donNotCheckWindowDisplayable == false) {
                    do {
                        Thread.sleep(100);
                        scrollContent.revalidate();
                        scrollContent.repaint();
                    } while (scrollContent.isDisplayable() == false || scrollContent.getHeight() <= 0);
                }
                getHistoryFromDB(null);

                getThis().addComponentListener(componentListener);
            } catch (Exception ex) {
                isBottomLoading = false;
                isTopLoading = false;
                //  ex.printStackTrace();
                log.error("Error in here ==>" + ex.getMessage());
            }
        }

    }

    private class LoadHistoryOnWindowResize extends Thread {

        private boolean running = true;
        private Long MAX_VALUE = (long) 1500;
        private Long time = 0L;

        public void resizing() {
            this.time = MAX_VALUE;
        }

        @Override
        public void run() {
            try {
                while (running) {
                    if (time <= 0) {
                        stopThread();
                        continue;
                    }

                    Thread.sleep(500);
                    time -= 500;

                    if (time <= 0) {
                        stopThread();
                    }
                }
            } catch (Exception e) {
                stopThread();
            }
        }

        public void startThread() {
            isBottomLoading = true;
            isTopLoading = true;
            running = true;
            this.start();
        }

        public void stopThread() {
            refrestHistory();
            running = false;
        }

        public boolean isRunning() {
            return running;
        }

        public void refrestHistory() {
            try {
                //System.err.println("refrestHistory");
                getHistoryFromDB(null);
            } catch (Exception ex) {
                isBottomLoading = false;
                isTopLoading = false;
                //ex.printStackTrace();
                log.error("Error in refrestHistory ==>" + ex.getMessage());
            }
        }

    }

    private void loadHistoryOnRecent(int days) {
        try {
            //System.err.println("loadHistoryByDays(int days)");
            isTopLoading = true;
            isBottomLoading = true;
            getHistoryFromDB(days);
        } catch (Exception ex) {
            isBottomLoading = false;
            isTopLoading = false;
            // ex.printStackTrace();
            log.error("Error in loadHistoryOnRecent ==>" + ex.getMessage());
        }
    }

    private void getHistoryFromDB(Integer days) {
        scrollContent.revalidate();
        scrollContent.repaint();
        int limit = (scrollContent.getHeight() / unit_size);
        //System.err.println("Height = " + scrollContent.getHeight() + ", limit = " + limit);
        RecentDTO chatRecentDTO = ChatHashMap.getInstance().getChatUnreadMessages().get(friendIdentity);
        RecentDTO callRecentDTO = ChatHashMap.getInstance().getCallUnreadHistories().get(friendIdentity);

        if (days != null) {
            new GetFriendHistory(friendIdentity, days, null).start();
            if (chatRecentDTO != null) {
                GuiRingID.getInstance().getMainButtonsPanel().clearChatNotification(friendIdentity);
            }
            if (callRecentDTO != null) {
                GuiRingID.getInstance().getMainButtonsPanel().clearCallNotification(friendIdentity);
            }
        } else if (LOADED_HISTORY_TIME.size() <= 0) {
            new GetFriendHistory(friendIdentity, RecentContactDAO._90_DAYS, limit).start();
            if (chatRecentDTO != null) {
                GuiRingID.getInstance().getMainButtonsPanel().clearChatNotification(friendIdentity);
            }
            if (callRecentDTO != null) {
                GuiRingID.getInstance().getMainButtonsPanel().clearCallNotification(friendIdentity);
            }

        } else if (LOADED_HISTORY_TIME.size() < limit) {
            if (chatRecentDTO != null || callRecentDTO != null) {
                new GetFriendHistory(friendIdentity, RecentContactDAO._90_DAYS, limit).start();
                GuiRingID.getInstance().getMainButtonsPanel().clearChatNotification(friendIdentity);
                GuiRingID.getInstance().getMainButtonsPanel().clearCallNotification(friendIdentity);
            } else {
                new GetFriendHistory(friendIdentity, LOADED_HISTORY_TIME.get(0).getTime(), limit - LOADED_HISTORY_TIME.size(), RecentChatCallActivityDAO.TYPE_SCROLL_UP).start();
            }

        } else if (chatRecentDTO != null || callRecentDTO != null) {
            new GetFriendHistory(friendIdentity, LOADED_HISTORY_TIME.get(LOADED_HISTORY_TIME.size() - 1).getTime(), null, RecentChatCallActivityDAO.TYPE_SCROLL_DOWN).start();
            GuiRingID.getInstance().getMainButtonsPanel().clearChatNotification(friendIdentity);
            GuiRingID.getInstance().getMainButtonsPanel().clearCallNotification(friendIdentity);
        } else {
            isBottomLoading = false;
            isTopLoading = false;
        }
    }

    private class Data {

        private long time;
        private String dateTime;

        public Data(RecentDTO recentDTO) {
            this.time = recentDTO.getTime();
            this.dateTime = recentDTO.getHistoryDateName();
        }

        public long getTime() {
            return time;
        }

        public String getDateTime() {
            return dateTime;
        }

    }

    private void setChatDateGroupInFront(RecentDTO recentDTO) {
        if (LOADED_HISTORY_TIME.size() > 0) {
            String dateTime = LOADED_HISTORY_TIME.get(0).getDateTime();
            if (!dateTime.equalsIgnoreCase(recentDTO.getHistoryDateName())) {
                messagePanel.add(new ChatDateGroupPanel(recentDTO), 0);
            }
        } else {
            messagePanel.add(new ChatDateGroupPanel(recentDTO));
        }
    }

    public void reqChatAreaFocus() {
        chatWriter.requestFocus();
        chatWriter.grabFocus();
        chatWriter.requestFocusInWindow();
    }

    private void setChatDateGroupInRear(RecentDTO recentDTO) {
        if (LOADED_HISTORY_TIME.size() > 0) {
            String dateTime = LOADED_HISTORY_TIME.get(LOADED_HISTORY_TIME.size() - 1).getDateTime();
            if (!dateTime.equalsIgnoreCase(recentDTO.getHistoryDateName())) {
                messagePanel.add(new ChatDateGroupPanel(recentDTO));
            }
        } else {
            messagePanel.add(new ChatDateGroupPanel(recentDTO));
        }
    }

    private boolean setChatDateGroupInBetween(RecentDTO recentDTO, int dataIndex, int componentIndex) {
        String prevDateTime = LOADED_HISTORY_TIME.get(dataIndex - 1).getDateTime();
        String nextDateTime = LOADED_HISTORY_TIME.get(dataIndex).getDateTime();
        if (!prevDateTime.equalsIgnoreCase(recentDTO.getHistoryDateName()) && !nextDateTime.equalsIgnoreCase(recentDTO.getHistoryDateName())) {
            messagePanel.add(new ChatDateGroupPanel(recentDTO), componentIndex);
            return true;
        } else if (!prevDateTime.equalsIgnoreCase(recentDTO.getHistoryDateName())) {
            return true;
        }
        return false;
    }

    private void setChatSenderIdentityInRear(String userIdentity, int index) {
        synchronized (messagePanel.getTreeLock()) {
            for (int idx = index; idx < messagePanel.getComponentCount(); idx++) {
                Component component = messagePanel.getComponents()[idx];
                if (component instanceof SingleChatPanel) {
                    SingleChatPanel singleChatPanel = (SingleChatPanel) component;
                    singleChatPanel.lastChatSenderIdentity = userIdentity;
                    singleChatPanel.setFullName();
                    break;
                }
            }
        }
    }

    private String getPrevChatSenderIdentity(int index) {
        synchronized (messagePanel.getTreeLock()) {
            if (messagePanel.getComponentCount() > 0) {
                for (int idx = index - 1; idx >= 0; idx--) {
                    Component component = messagePanel.getComponents()[idx];
                    if (component instanceof SingleChatPanel) {
                        SingleChatPanel singleChatPanel = (SingleChatPanel) component;
                        return singleChatPanel.messageDTO.getUserIdentity();
                    }
                }
            }
        }
        return null;
    }

    private int getEstimatedComponentIndex(long time) {
        int index = 0;
        synchronized (messagePanel.getTreeLock()) {
            if (messagePanel.getComponentCount() > 0) {
                for (int idx = messagePanel.getComponentCount() - 1; idx >= 0; idx--) {
                    Component component = messagePanel.getComponents()[idx];
                    if (component instanceof SingleChatPanel) {
                        SingleChatPanel singleChatPanel = (SingleChatPanel) component;
                        long messageDate = singleChatPanel.messageDTO.getMessageDate() > 0 ? singleChatPanel.messageDTO.getMessageDate() : singleChatPanel.messageDTO.getSystemDate();
                        if (time > messageDate) {
                            index = idx + 1;
                            break;
                        }
                    } else if (component instanceof SingleCallPanel) {
                        SingleCallPanel singleCallPanel = (SingleCallPanel) component;
                        if (time > singleCallPanel.callLog.getCallingTime()) {
                            index = idx + 1;
                            break;
                        }
                    } else if (component instanceof SingleActivityPanel) {
                        SingleActivityPanel singleActivityPanel = (SingleActivityPanel) component;
                        if (time > singleActivityPanel.activityDTO.getUpdateTime()) {
                            index = idx + 1;
                            break;
                        }
                    }
                }
            }
        }
        return index;
    }

    private int getEstimatedDataIndex(long time) {
        int index = 0;
        synchronized (LOADED_HISTORY_TIME) {
            if (LOADED_HISTORY_TIME.size() > 0) {
                for (int idx = LOADED_HISTORY_TIME.size() - 1; idx >= 0; idx--) {
                    Data date = LOADED_HISTORY_TIME.get(idx);
                    if (time > date.getTime()) {
                        index = idx + 1;
                        break;
                    }
                }
            }
        }
        return index;
    }

    public void removeSingleCallPanel(List<String> callIds) {
        try {
            if (messagePanel.getComponentCount() > 0) {
                for (int idx = messagePanel.getComponentCount() - 1; idx >= 0 && callIds.size() > 0; idx--) {
                    if (messagePanel.getComponent(idx) instanceof SingleCallPanel) {
                        SingleCallPanel panel = (SingleCallPanel) messagePanel.getComponent(idx);
                        if (callIds.contains(panel.callLog.getCallID())) {
                            int prevIdx = idx - 1;
                            messagePanel.remove(panel);
                            messagePanel.revalidate();
                            messagePanel.repaint();
                            callIds.remove(panel.callLog.getCallID());

                            if (prevIdx >= 0 && prevIdx < messagePanel.getComponentCount()) {
                                Component prevPanel = messagePanel.getComponent(prevIdx);

                                if (prevPanel instanceof ChatDateGroupPanel) {
                                    Component currPanel = idx < messagePanel.getComponentCount() ? messagePanel.getComponent(idx) : null;

                                    if (currPanel == null || currPanel instanceof ChatDateGroupPanel) {
                                        messagePanel.remove(prevPanel);
                                        messagePanel.revalidate();
                                        messagePanel.repaint();
                                    }
                                }
                            }
                        }
                    }
                }
                getHistoryFromDB(null);
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Error in removeSingleCallPanel ==>" + ex.getMessage());
        }
    }

    public void removeSingleChatPanel(SingleChatPanel panel) {
        try {
            if (panel != null) {
                String currentLastChatIdentity = panel.lastChatSenderIdentity;

                synchronized (messagePanel.getTreeLock()) {
                    int currIdx = messagePanel.getComponentZOrder(panel);
                    int prevIdx = currIdx - 1;
                    messagePanel.remove(panel);
                    messagePanel.revalidate();
                    messagePanel.repaint();

                    if (currIdx >= 0 && currIdx < messagePanel.getComponentCount()) {
                        for (int idx = currIdx; idx < messagePanel.getComponentCount(); idx++) {
                            if (messagePanel.getComponent(idx) instanceof SingleChatPanel) {
                                SingleChatPanel nextChatpanel = (SingleChatPanel) messagePanel.getComponent(idx);
                                nextChatpanel.lastChatSenderIdentity = currentLastChatIdentity;
                                nextChatpanel.setFullName();
                                nextChatpanel.revalidate();
                                nextChatpanel.repaint();
                                break;
                            }
                        }
                    } else {
                        lastChatSenderIdentity = currentLastChatIdentity;
                    }

                    if (prevIdx >= 0 && prevIdx < messagePanel.getComponentCount()) {
                        Component prevPanel = messagePanel.getComponent(prevIdx);

                        if (prevPanel instanceof ChatDateGroupPanel) {
                            Component currPanel = currIdx < messagePanel.getComponentCount() ? messagePanel.getComponent(currIdx) : null;

                            if (currPanel == null || currPanel instanceof ChatDateGroupPanel) {
                                messagePanel.remove(prevPanel);
                                messagePanel.revalidate();
                                messagePanel.repaint();
                            }
                        }
                    }
                }

                getHistoryFromDB(null);
            }

        } catch (Exception ex) {
            //ex.printStackTrace();
            log.error("Error in removeSingleChatPanel ==>" + ex.getMessage());
        }
    }

    private class ChatWrapperPanel extends JPanel {

        private Color color;
        private Color chatShadowColor;
        private int arc = 50;
        private int subArc = 40;
        private int cropArc = 43;
        private int mainX = 45;
        private int mainY = 0;
        private int subX = 0;
        private int subY = 3;
        private int subWidth = 80;
        private int cropX = 38;
        private int cropY = 1;
        private int cropWidth = 45;

        public ChatWrapperPanel(Color color, Color chatBorderColor) {
            this.color = color;
            this.chatShadowColor = chatBorderColor;
            this.setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            Area mainShadowShape = new Area(new RoundRectangle2D.Double(mainX, mainY, w - mainX * 2, h, arc, arc));
            g2d.setColor(chatShadowColor);
            g2d.fill(mainShadowShape);

            Area sideShadowShape = new Area(new RoundRectangle2D.Double(subX, subY, subWidth, h - 6, subArc, subArc));
            sideShadowShape.subtract(new Area(new RoundRectangle2D.Double(cropX - 2, cropY, cropWidth, h - 2, cropArc, cropArc)));
            sideShadowShape.add(new Area(new RoundRectangle2D.Double(w - subX - subWidth, subY, subWidth, h - 6, subArc, subArc)));
            sideShadowShape.subtract(new Area(new RoundRectangle2D.Double(w - cropX - cropWidth + 2, cropY, cropWidth, h - 2, cropArc, cropArc)));
            g2d.setColor(RingColorCode.RING_CHAT_AREA_ACTIVE_SHADOW_BG);
            g2d.fill(sideShadowShape);

            Area mainShape = new Area(new RoundRectangle2D.Double(mainX, mainY - 1, w - mainX * 2, h, arc, arc));
            g2d.setColor(color);
            g2d.fill(mainShape);

            Area sideShape = new Area(new RoundRectangle2D.Double(subX, subY - 1, subWidth, h - 6, subArc, subArc));
            sideShape.subtract(new Area(new RoundRectangle2D.Double(cropX, cropY - 1, cropWidth, h - 2, cropArc, cropArc)));
            sideShape.add(new Area(new RoundRectangle2D.Double(w - subX - subWidth, subY - 1, subWidth, h - 6, subArc, subArc)));
            sideShape.subtract(new Area(new RoundRectangle2D.Double(w - cropX - cropWidth, cropY - 1, cropWidth, h - 2, cropArc, cropArc)));
            g2d.setColor(RingColorCode.RING_CHAT_AREA_SIDE_BG);
            g2d.fill(sideShape);
        }

    }

//    private void showMessageDateName() {
//        String dateName = "";
//        if (scrollContent.getVerticalScrollBar().isVisible()) {
//            int value = scrollContent.getVerticalScrollBar().getValue() - ((MyScrollbarVerticalUI) scrollContent.getVerticalScrollBar().getUI()).recY;
//            //System.err.println(scrollContent.getVerticalScrollBar().getValue() + " /// " + ((MyScrollbarVerticalUI) scrollContent.getVerticalScrollBar().getUI()).recY);
//            if (messagePanel.getComponentCount() > 0) {
//                Component component = messagePanel.getComponentAt(0, value);
//                if (component != null) {
//                    //System.err.println(component.getName());
//                    if (component instanceof SingleChatPanel) {
//                        SingleChatPanel chatPanel = (SingleChatPanel) component;
//                        dateName = getDateName(chatPanel.messageDTO.getMessageDate());
//                    } else if (component instanceof SingleCallPanel) {
//                        SingleCallPanel callPanel = (SingleCallPanel) component;
//                        dateName = getDateName(callPanel.callLog.getCallingTime());
//                    } else if (component instanceof SingleActivityPanel) {
//                        SingleActivityPanel activityPanel = (SingleActivityPanel) component;
//                        dateName = getDateName(activityPanel.activityDTO.getUpdateTime());
//                    } else if (component instanceof ChatDateGroupPanel) {
//                        ChatDateGroupPanel dateGroupPanel = (ChatDateGroupPanel) component;
//                        dateName = dateGroupPanel.dateName;
//                    }
//                }
//            }
//        }
//
//        lblMessageDate.setText(dateName);
//    }
//
//    private String getDateName(Long time) {
//        String dateName = "";
//        Date date = new Date();
//        date.setHours(0);
//        date.setMinutes(0);
//        date.setSeconds(0);
//        long currentDay = date.getTime();
//        date.setDate(date.getDate() - 1);
//        long yesterDay = date.getTime();
//
//        if (time >= currentDay) {
//            dateName = " Today, " + ChatHelpers.CHAT_DATE_FORMAT.format(new Date(time));
//        } else if (time < currentDay && time >= yesterDay) {
//            dateName = " Yesterday, " + ChatHelpers.CHAT_DATE_FORMAT.format(new Date(time));
//        } else {
//            dateName = ChatHelpers.CHAT_DATE_FORMAT.format(new Date(time));
//        }
//
//        return dateName;
//    }
    private void showHideChatSelection(boolean show, boolean isSelectAll) {
        Map<String, SingleChatPanel> singleChatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(friendIdentity);
        if (singleChatPanelMap != null) {
            for (Entry<String, SingleChatPanel> entrySet : singleChatPanelMap.entrySet()) {
                SingleChatPanel singleChatPanel = entrySet.getValue();
                singleChatPanel.showSelectBox(show, isSelectAll);
            }
        }

        deleteOptionContainer.setVisible(show);
    }

    private void deleteChatMessage() {
        List<String> packetIds = new ArrayList<String>();
        List<MessageDTO> list = new ArrayList<MessageDTO>();
        List<SingleChatPanel> singleChatPanels = new ArrayList<SingleChatPanel>();

        Map<String, SingleChatPanel> singleChatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(friendIdentity);
        if (singleChatPanelMap != null && singleChatPanelMap.size() > 0) {
            for (Entry<String, SingleChatPanel> entrySet : singleChatPanelMap.entrySet()) {
                SingleChatPanel singleChatPanel = entrySet.getValue();
                if (singleChatPanel.chkSelect.isSelected()) {
                    list.add(singleChatPanel.messageDTO);
                    singleChatPanels.add(singleChatPanel);
                    if (singleChatPanel.messageDTO.getUserIdentity() != null && singleChatPanel.messageDTO.getUserIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                        packetIds.add(singleChatPanel.messageDTO.getPacketID());
                    }
                }
            }
        }

        if (list.size() > 0) {
            if (packetIds.size() > 0) {
                ChatHelpers.startFriendChat(friendIdentity);
            }

            HelperMethods.showConfirmationDialogMessage(NotificationMessages.DELETE_NOTIFICAITON);
            if (JOptionPanelBasics.YES_NO) {
                for (MessageDTO msgDTO : list) {
                    msgDTO.setStatus(ChatConstants.CHAT_GROUP_MESSAGE_DELETED);
                }

                new InsertIntoFriendMessageTable(list).start();

                if (packetIds.size() > 0) {
                    ChatHelpers.startFriendChat(friendIdentity);
                    ChatService.sendFriendChatDelete(friendIdentity, packetIds, new PacketResenderHandler());
                }

                Collections.sort(singleChatPanels, new Comparator<SingleChatPanel>() {
                    @Override
                    public int compare(SingleChatPanel one, SingleChatPanel other) {
                        Long onesSeq = one.messageDTO.getMessageDate();
                        Long othersSeq = other.messageDTO.getMessageDate();
                        return othersSeq.compareTo(onesSeq);
                    }
                });

                for (SingleChatPanel singleChatPanel : singleChatPanels) {
                    removeSingleChatPanel(singleChatPanel);
                    if (singleChatPanelMap != null) {
                        singleChatPanelMap.remove(singleChatPanel.messageDTO.getPacketID());
                    }
                }
                singleChatPanels.clear();
                btnCancel.doClick();
                //showMessageDateName();
            }
        } else {
            HelperMethods.showWarningDialogMessage("Please, Select message!");
        }
    }
}
