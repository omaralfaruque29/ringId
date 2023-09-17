/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.chat;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.service.ChatService;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import com.ipvision.model.dao.InsertIntoFriendMessageTable;
import com.ipvision.model.dao.InsertIntoGroupMessageTable;
import com.ipvision.model.MessageDTO;
import com.ipvision.service.utility.chat.PacketResenderHandler;
import com.ipvision.view.utility.chat.ChatHelpers;
import com.ipvision.view.AddEmoticonsJDialog;
import com.ipvision.model.EmotionDtos;
import com.ipvision.constants.XMLConstants;
import com.ipvision.model.stores.HashMapsBeforeLogin;

/**
 *
 * @author Faiz Ahmed
 */
public class ChatEditMessagePanel extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatEditMessagePanel.class);
    private JTextArea textOnly;
    private MessageDTO messageDTO;
    private AddEmoticonsJDialog addEmoticonsJDialog;
    private String previsou_text;
    private SingleChatPanel chatPanel;
    private int msgFrom;
    private JPanel editPanel;
    private Color chatMsgColor;
    JButton addEmoticon;
    private final int MAX_CHAT_TEXT_LENGTH = 8000;

    public ChatEditMessagePanel(SingleChatPanel chatPanel) {
        this.chatPanel = chatPanel;
        this.messageDTO = chatPanel.messageDTO;
        this.msgFrom = chatPanel.msgFrom;
        this.chatMsgColor = chatPanel.chatMsgColor;
        this.previsou_text = this.messageDTO.getMessage();

        setOpaque(false);
        if (msgFrom == SingleChatPanel.TYPE_ME) {
            setBorder(new EmptyBorder(0, 0, 0, 6));
        } else {
            setBorder(new EmptyBorder(0, 6, 0, 0));
        }
        setLayout(new FlowLayout(msgFrom == SingleChatPanel.TYPE_ME ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
        initContainers();
    }

    public void grabTextBoxFocus() {
        textOnly.grabFocus();
        int len = textOnly.getDocument().getLength();
        textOnly.setCaretPosition(len);
    }

    private void initContainers() {
        try {
            editPanel = new JPanel() {
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
            editPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
            editPanel.setOpaque(false);
            add(editPanel);

            JPanel textPanel = new JPanel(new BorderLayout());
            textPanel.setBorder(new EmptyBorder(3, 2, 3, 5));
            textPanel.setOpaque(false);

            JPanel status_name_Panel = new JPanel(new BorderLayout(0, 0));
            status_name_Panel.setForeground(DefaultSettings.DEFAULT_FONT_COLOR);
            status_name_Panel.setBackground(Color.WHITE);
            textOnly = DesignClasses.createJTextArea(previsou_text, MAX_CHAT_TEXT_LENGTH);
            textOnly.setRows(1);
            textOnly.setEditable(true);
            textOnly.setColumns(35);
            textOnly.setToolTipText(null);
            textOnly.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if (textOnly.getText().length() > MAX_CHAT_TEXT_LENGTH) {
                        e.consume();
                    }
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER && (e.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
                            if (textOnly.getText().length() > MAX_CHAT_TEXT_LENGTH) {
                                e.consume();
                            }
                        } else if (textOnly.getText().trim().length() > 0 && !textOnly.getText().equals(messageDTO.getMessage())) {
                            e.consume();

                            List<MessageDTO> list = new ArrayList<MessageDTO>();
                            list.add(messageDTO);

                            messageDTO.setMessageId(9999);//RESEND ESCAPE
                            messageDTO.setMessage(textOnly.getText());
                            messageDTO.setMessageType(getMessageType());

                            if (messageDTO.getGroupId() != null && messageDTO.getGroupId() > 0) {
                                ChatHelpers.startGroupChat(messageDTO.getGroupId(), true);
                                messageDTO.setPacketType(ChatConstants.CHAT_GROUP_CHAT_EDIT);
                                new InsertIntoGroupMessageTable(list).start();
                                ChatService.sendGroupChatEdit(messageDTO);
                            } else {
                                ChatHelpers.startFriendChat(messageDTO.getFriendIdentity());
                                messageDTO.setPacketType(ChatConstants.CHAT_FRIEND_CHAT_EDIT);
                                new InsertIntoFriendMessageTable(list).start();
                                ChatService.sendFriendChatEdit(messageDTO, new PacketResenderHandler());
                            }

                            textOnly.setText("");
                            textOnly.setEditable(false);
                            chatPanel.setMainPanel();
                        } else {
                            e.consume();
                        }
                        textOnly.grabFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        if (messageDTO.getMessageId() <= 0) {
                            messageDTO.setMessageId(9999);//RESEND ESCAPE
                        }
                        chatPanel.setMainPanel();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }
            });
            textPanel.add(textOnly);
            status_name_Panel.add(textPanel, BorderLayout.CENTER);
            JLabel pressEsc = DesignClasses.makeJLabelUnderFullName("Press Esc to cancel.");
            status_name_Panel.add(pressEsc, BorderLayout.SOUTH);

            addEmoticon = DesignClasses.createEmoticonButton();
            addEmoticon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        int yPos = (int) getLocationOnScreen().getY();
                        int type_int = 1;
                        if (yPos > DefaultSettings.MONITOR_HALF_HEIGHT) {
                            type_int = 0;
                        }

                        addEmoticonsJDialog = new AddEmoticonsJDialog(textOnly, type_int);
                        addEmoticonsJDialog.setVisible(addEmoticon);
                    } catch (Exception ed) {
                    }
                }
            });
            status_name_Panel.add(addEmoticon, BorderLayout.EAST);
            editPanel.add(status_name_Panel);
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error in here ==>" + e.getMessage());
        }

    }

    public int getMessageType() {
        String entered_string = messageDTO.getMessage();
        if (XMLConstants.EMOTION_REGULAR_EXPRESSION != null) {
            Pattern reqular_expresion = Pattern.compile(XMLConstants.EMOTION_REGULAR_EXPRESSION);
            Matcher m = reqular_expresion.matcher(entered_string.trim());

            while (m.find()) {
                if (m.group().length() > 0) {
                    EmotionDtos tememo = HashMapsBeforeLogin.getInstance().getEmotionHashmap().get(m.group());
                    if (tememo != null) {
                        return ChatConstants.TYPE_EMOTICON_MSG;
                    }
                }
            }
        }

        return ChatConstants.TYPE_PLAIN_MSG;
    }
}
