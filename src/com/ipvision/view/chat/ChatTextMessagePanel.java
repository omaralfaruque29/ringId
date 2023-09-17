/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.chat;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.service.ChatService;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.view.utility.HtmlHelpers;
import com.ipvision.constants.RingColorCode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.geom.Area;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import com.ipvision.model.dao.InsertIntoFriendMessageTable;
import com.ipvision.model.dao.InsertIntoGroupMessageTable;
import com.ipvision.view.utility.chat.ChatHelpers;
import com.ipvision.constants.XMLConstants;
import com.ipvision.model.MessageDTO;
import com.ipvision.view.utility.chat.ChatHashMap;
import com.ipvision.view.utility.chat.ContextMenuChatMouseListener;
import com.ipvision.model.StickerCategoryDTO;
import com.ipvision.model.stores.StickerStorer;
import com.ipvision.view.utility.JTextWrapPane;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.image.LoadUnknownStickerCategory;

/**
 *
 * @author Shahadat
 */
public class ChatTextMessagePanel extends JPanel {

    private SingleChatPanel chatPanel;
    private int msgFrom;
    private int chatType;
    private Color chatMsgColor;
    private Color chatBorderColor;
    private HTMLDocument chat_doc;
    private HTMLEditorKit editorKit;
    private HtmlHelpers htmlHelp;
    public Pattern reqular_expresion;
    public JLabel timeLabel;
    public JLabel statusLabel;
    private JPanel timeStatusPanel;
    public JTextWrapPane textArea;
    private MessageDTO messageDTO;
    private JPanel topPanel;
    private JPanel topWrapperPanel;
    private JPanel bottomPanel;
    private JPanel bottomWrapperPanel;
    public JPanel chatMsgPanel;

    public ChatTextMessagePanel(SingleChatPanel chatPanel) {
        this.chatPanel = chatPanel;
        this.messageDTO = chatPanel.messageDTO;
        this.msgFrom = chatPanel.msgFrom;
        this.chatType = chatPanel.chatType;
        this.timeLabel = chatPanel.timeLabel;
        this.statusLabel = chatPanel.statusLabel;
        this.chatMsgColor = chatPanel.chatMsgColor;
        this.chatBorderColor = chatPanel.chatShadowColor;
        this.topPanel = chatPanel.topPanel;
        this.topWrapperPanel = chatPanel.topWrapperPanel;
        this.bottomPanel = chatPanel.bottomPanel;
        this.bottomWrapperPanel = chatPanel.bottomWrapperPanel;
        this.timeStatusPanel = chatPanel.timeStatusPanel;

        if (XMLConstants.EMOTION_REGULAR_EXPRESSION != null) {
            reqular_expresion = Pattern.compile(XMLConstants.EMOTION_REGULAR_EXPRESSION);
        }
        htmlHelp = new HtmlHelpers();

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

            String replace = htmlHelp.replaceStringForEmoticonsForChat(this, chatPanel.messageDTO, reqular_expresion);
            int currWidth = getContentHeight(replace);

            JPanel textAreaPanel = new JPanel(new BorderLayout(0, 0));
            textAreaPanel.setBorder(new EmptyBorder(0, 10, 1, 10));
            textAreaPanel.setOpaque(false);

            chatMsgPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    boolean isNew = chatPanel.lastChatSenderIdentity == null || !messageDTO.getUserIdentity().equalsIgnoreCase(chatPanel.lastChatSenderIdentity);

                    int w = getWidth();
                    int h = getHeight() - 1;
                    int offset = 6;
                    int shapeX = offset;
                    int aX1 = 0;
                    int aY1 = 0 + 2;
                    int aX2 = 17;
                    int aY2 = 0 - 6;
                    int aX3 = 21;
                    int aY3 = 0 + 30;
                    int sX1 = 0;
                    int sY1 = 0 + 2;
                    int sX2 = 11;
                    int sY2 = 0 + 1;
                    int sX3 = 0 + 4;
                    int sY3 = 0 + 30;

                    if (msgFrom == SingleChatPanel.TYPE_ME) {
                        shapeX = 0;
                        aX1 = w - 15;
                        aY1 = h - 30;
                        aX2 = w - 17;
                        aY2 = h + 6;
                        aX3 = w;
                        aY3 = h - 2;
                        sX1 = w - 3;
                        sY1 = h - 30;
                        sX2 = w - 10;
                        sY2 = h - 1;
                        sX3 = w;
                        sY3 = h - 2;
                    }

                    g2d.setColor(chatBorderColor);
                    Area shapeBorder = new Area(new RoundRectangle2D.Double(shapeX, 1, w - offset, h, 30, 30));
                    if (isNew) {
                        Area arraowBorder = new Area(new QuadCurve2D.Double(
                                aX1, aY1 + 1,
                                aX2, aY2 + 1,
                                aX3, aY3 + 1
                        ));
                        Area arraowBorder1 = new Area((new QuadCurve2D.Double(
                                sX1, sY1 + 1,
                                sX2, sY2 + 1,
                                sX3, sY3 + 1
                        )));
                        arraowBorder.subtract(arraowBorder1);
                        shapeBorder.add(arraowBorder);
                    }
                    g2d.fill(shapeBorder);
                    g2d.setColor(chatMsgColor);
                    Area shape = new Area(new RoundRectangle2D.Double(shapeX, 0, w - offset, h, 30, 30));
                    if (isNew) {
                        Area arraow = new Area(new QuadCurve2D.Double(
                                aX1, aY1,
                                aX2, aY2,
                                aX3, aY3
                        ));
                        Area arraow1 = new Area((new QuadCurve2D.Double(
                                sX1, sY1,
                                sX2, sY2,
                                sX3, sY3
                        )));
                        arraow.subtract(arraow1);
                        shape.add(arraow);
                    }
                    g2d.fill(shape);
                }
            };
            if (msgFrom == SingleChatPanel.TYPE_ME) {
                chatMsgPanel.setBorder(new EmptyBorder(0, 0, 0, 6));
            } else {
                chatMsgPanel.setBorder(new EmptyBorder(0, 6, 0, 0));
            }
            chatMsgPanel.setOpaque(false);
            chatMsgPanel.add(textAreaPanel);

            JPanel chatMsgWrapperPanel = new JPanel(new FlowLayout(msgFrom == SingleChatPanel.TYPE_ME ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
            chatMsgWrapperPanel.setOpaque(false);
            chatMsgWrapperPanel.add(chatMsgPanel);

            JPanel masterPanel = new JPanel(new BorderLayout());
            masterPanel.setOpaque(false);

            masterPanel.add(topWrapperPanel, BorderLayout.NORTH);
            masterPanel.add(timeStatusPanel, BorderLayout.CENTER);
            masterPanel.add(bottomWrapperPanel, BorderLayout.SOUTH);

            textArea = new JTextWrapPane();
            textArea.setOpaque(false);
            textArea.setEditable(false);
            textArea.setContentType("text/html");
            chat_doc = (HTMLDocument) textArea.getDocument();
            editorKit = (HTMLEditorKit) textArea.getEditorKit();
            textArea.setCaretPosition(chat_doc.getLength());
            textArea.addMouseListener(new ContextMenuChatMouseListener(chatPanel));
            textArea.addHyperlinkListener(hyperlinkListener);
            textAreaPanel.add(textArea, BorderLayout.CENTER);

            if (chatPanel.messageDTO.getPacketType() == ChatConstants.CHAT_FRIEND_CHAT_EDIT || chatPanel.messageDTO.getPacketType() == ChatConstants.CHAT_GROUP_CHAT_EDIT) {
                JPanel editIconPanel = new JPanel();
                editIconPanel.setOpaque(false);
                editIconPanel.add(new JLabel(DesignClasses.return_image(GetImages.CHAT_EDITED)));
                textAreaPanel.add(editIconPanel, BorderLayout.EAST);
                editorKit.insertHTML(chat_doc, chat_doc.getLength(), (currWidth > 330 ? htmlHelp.setStyle_inChat(260) : htmlHelp.setStyle_inChat()), 0, 0, null);
            } else {
                editorKit.insertHTML(chat_doc, chat_doc.getLength(), (currWidth > 360 ? htmlHelp.setStyle_inChat(280) : htmlHelp.setStyle_inChat()), 0, 0, null);
            }

            bottomWrapperPanel.setVisible(false);
            if (chatPanel.messageDTO.getMessageType() == ChatConstants.TYPE_DOWNLOADED_STICKER_MSG) {
                final StickerCategoryDTO stickerCategoryDTO = StickerStorer.getInstance().getCategoriesMap().get(chatPanel.messageDTO.getStickerCategoryId());
                if (stickerCategoryDTO == null
                        || stickerCategoryDTO.getDownloaded() == null
                        || stickerCategoryDTO.getDownloaded() == false) {

                    if (stickerCategoryDTO == null) {
                        new LoadUnknownStickerCategory(chatPanel.messageDTO.getStickerCategoryId(), chatPanel.messageDTO.getStickerCollectionId()).start();
                    }

                    String value = (messageDTO.getGroupId() != null && messageDTO.getGroupId() > 0 ? messageDTO.getGroupId() : messageDTO.getFriendIdentity()) + "_" + messageDTO.getPacketID();
                    Set<String> stickerSet = StickerStorer.getInstance().getChatStickerDownload().get(chatPanel.messageDTO.getStickerCategoryId());
                    if (stickerSet == null) {
                        stickerSet = new HashSet<String>();
                        stickerSet.add(value);
                        StickerStorer.getInstance().getChatStickerDownload().put(chatPanel.messageDTO.getStickerCategoryId(), stickerSet);
                    } else {
                        if (stickerSet.contains(value) == false) {
                            stickerSet.add(value);
                        }
                    }

                    JLabel downloadLabel = new JLabel("Download");
                    downloadLabel.setOpaque(false);
                    downloadLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    downloadLabel.setForeground(RingColorCode.RING_THEME_COLOR);
                    bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
                    bottomPanel.add(downloadLabel);
                    bottomWrapperPanel.setVisible(true);
                    downloadLabel.addMouseListener(new MouseAdapter() {
                        Font original;

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            e.getComponent().setFont(original);
                            if (GuiRingID.getInstance() != null) {
                                boolean isStateChange = GuiRingID.getInstance().getTopMenuBar().setExpand();
                                if (isStateChange == false) {
                                    GuiRingID.getInstance().getRingToolsRightContainer().addRingMarket();
                                }
                                GuiRingID.getInstance().getRingToolsRightContainer().getRingMarketPanel().showMarketCategoryByID(messageDTO.getStickerCategoryId());
                            }
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            original = e.getComponent().getFont();
                            Map attributes = original.getAttributes();
                            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                            e.getComponent().setFont(original.deriveFont(attributes));
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            e.getComponent().setFont(original);
                        }
                    });
                }
            }

            if (msgFrom == SingleChatPanel.TYPE_ME) {
                masterPanel.add(chatMsgWrapperPanel, BorderLayout.EAST);
                mainPanel.add(masterPanel);
            } else {
                masterPanel.add(chatMsgWrapperPanel, BorderLayout.WEST);
                mainPanel.add(masterPanel);
            }

            setMessageText(replace);

            if (messageDTO.getGroupId() != null && messageDTO.getGroupId() > 0) {
                processGroupChat();
            } else {
                processFriendChat();
            }
        } catch (Exception ex) {

        }
    }

    public static int getContentHeight(String content) {
        JTextWrapPane dummyEditorPane = new JTextWrapPane();
        dummyEditorPane.setText(content);
        return dummyEditorPane.getPreferredSize().width;
    }

    public void setMessageText(String replace) {
        String style = "singleChat";
        String url = "<div id=\"" + style + "\">" + replace + "</div>";
        try {
            editorKit.insertHTML(chat_doc, chat_doc.getLength(), url, 0, 0, null);
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
                chatPanel.showTimerLoader();
            } else {
                if (messageDTO.getStatus() == ChatConstants.CHAT_FRIEND_SEEN) {
                    //log.warn("## 2");
                    timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));
                } else {
                    //log.warn("## 3");
                    ChatService.sendFriendChatSeenConfirmation(messageDTO.getPacketID(), messageDTO.getFriendIdentity());
                    messageDTO.setStatus(ChatConstants.CHAT_FRIEND_SEEN);
                    timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));

                    List<MessageDTO> mList = new ArrayList<MessageDTO>();
                    mList.add(messageDTO);
                    new InsertIntoFriendMessageTable(mList).start();
                }
            }

            ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getFriendIdentity()).put(messageDTO.getPacketID(), chatPanel);
        } else {
            if (messageDTO.getMessageId() > 0) {
                //log.warn("## 4");
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
            } else {
                //log.warn("## 5");
                timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));
                statusLabel.setText(ChatHelpers.STATUS_TXT_SENDING);

                List<MessageDTO> mList = new ArrayList<MessageDTO>();
                mList.add(messageDTO);
                new InsertIntoFriendMessageTable(mList).start();
                //ChatService.sendFriendChat(messageDTO, new PacketResenderHandler(), InternetUnavailablityCheck.isInternetAvailable);
            }

            ChatHashMap.getInstance().getChatStatusMessage().put(messageDTO.getPacketID(), messageDTO);
            ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getFriendIdentity()).put(messageDTO.getPacketID(), chatPanel);
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
                chatPanel.showTimerLoader();
            } else {
                if (messageDTO.getStatus() == ChatConstants.CHAT_GROUP_SEEN) {
                    //log.warn("## 2");
                    timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));
                } else {
                    //log.warn("## 3");
                    ChatService.sendGroupChatSeenConfirmation(messageDTO.getPacketID(), messageDTO.getGroupId(), messageDTO.getUserIdentity());
                    messageDTO.setStatus(ChatConstants.CHAT_GROUP_SEEN);
                    timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));

                    List<MessageDTO> mList = new ArrayList<MessageDTO>();
                    mList.add(messageDTO);
                    new InsertIntoGroupMessageTable(mList).start();
                }
            }

            ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getGroupId() + "").put(messageDTO.getPacketID(), chatPanel);
        } else {
            if (messageDTO.getMessageId() > 0) {
                //log.warn("## 4");
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
            } else {
                //log.warn("## 5");
                timeLabel.setText(ChatHelpers.getDate(messageDTO.getMessageDate(), ChatHelpers.CHAT_TIME_FORMAT));
                statusLabel.setText(ChatHelpers.STATUS_TXT_SENDING);

                List<MessageDTO> mList = new ArrayList<MessageDTO>();
                mList.add(messageDTO);
                new InsertIntoGroupMessageTable(mList).start();
                //ChatService.sendGroupChat(messageDTO, new PacketResenderHandler(), InternetUnavailablityCheck.isInternetAvailable);
            }

            ChatHashMap.getInstance().getChatStatusMessage().put(messageDTO.getPacketID(), messageDTO);
            ChatHashMap.getInstance().getChatSingleChatPanel().get(messageDTO.getGroupId() + "").put(messageDTO.getPacketID(), chatPanel);
        }
    }

    HyperlinkListener hyperlinkListener = new HyperlinkListener() {
        @Override
        public void hyperlinkUpdate(HyperlinkEvent he) {
            HyperlinkEvent.EventType type = he.getEventType();
            if (type == HyperlinkEvent.EventType.ACTIVATED) {
                if (!(he instanceof HTMLFrameHyperlinkEvent)) {
                    try {
                        Desktop.getDesktop().browse(he.getURL().toURI());
                    } catch (Exception e) {
                    }
                }
            }
        }
    };

}
