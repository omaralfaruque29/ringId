/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.leftdata;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.MessageDTO;
import com.ipvision.model.RecentDTO;
import com.ipvision.model.dao.RecentContactDAO;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.utility.CategoryButtonPanel;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.utility.JOptionPanelBasics;

/**
 *
 * @author Sirat Samyoun
 */
public class ChatHistoryContainer extends JPanel {

    JPanel chatHistoryShade;
    public JPanel chatListWrapperPanel;
    public JPanel chatListCategoryPanel;
    public static String CATEGORY_SINGLE_CHAT = "Chats";
    public static String CATEGORY_GROUP_CHAT = "Group Chat";
    ArrayList<MessageDTO> msgList = new ArrayList<>();
    private static SingleChatHistoryPanel selectedChatHistoryPanel = null;
    //  private static String selectedId = null;

    private List<SingleChatHistoryPanel> chatHistoryPanels = new ArrayList<>();
    private JPanel buttonPanel;
    private JLabel editLabel;
    List<String> contactIds = new ArrayList<String>();
    private String SELECT_ALL = "Select All";
    private String DESELECT_ALL = "Deselect All";
    private JPanel CallLogCategoryPanel;
    public CategoryButtonPanel.CustomPanel item;
    private String CATEGORY_SELECT_ALL = "Select All";
    private String CATEGORY_CANCEL = "Cancel";
    private String CATEGORY_DELETE = "Delete";
    CategoryButtonPanel categoryPnl;
    public JPanel categoryPanel;

    public ChatHistoryContainer() {
        setLayout(new BorderLayout(DefaultSettings.DEFAULT_BORDER_WIDTH, DefaultSettings.DEFAULT_BORDER_WIDTH));
        setOpaque(false);

        JPanel wrapperPanel = new JPanel(new CardLayout());
        wrapperPanel.setOpaque(false);

        chatHistoryShade = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(MyFnFSettings.LOGIN_SESSIONID != null
                        && MyFnFSettings.LOGIN_SESSIONID.length() > 0
                        && InternetUnavailablityCheck.isInternetAvailable
                                ? RingColorCode.RING_FRIEND_LIST_ONLINE_BG
                                : RingColorCode.RING_FRIEND_LIST_OFFLINE_BG);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        chatHistoryShade.setOpaque(false);
        wrapperPanel.add("CHAT_HISTORY_SHADE", chatHistoryShade);

        chatListWrapperPanel = new JPanel();
        chatListWrapperPanel.setLayout(new BoxLayout(chatListWrapperPanel, BoxLayout.Y_AXIS));
        chatListWrapperPanel.setBorder(new EmptyBorder(0, 0, 0, 5));
        chatListWrapperPanel.setOpaque(false);

        JPanel chatListPanel = new JPanel(new BorderLayout());
        chatListPanel.setOpaque(false);

        editLabel = new JLabel("Edit");
        editLabel.setBorder(new EmptyBorder(3, 12, 0, 0));
        editLabel.setOpaque(false);
        editLabel.setIcon(DesignClasses.return_image(GetImages.PEN_H));
        editLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        editLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editLabel.setForeground(RingColorCode.RING_THEME_COLOR);
        editLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (SingleChatHistoryPanel ins : chatHistoryPanels) {
                    ins.checkboxPanel.setVisible(true);
                    ins.deleteBox.setVisible(true);
                }
                editLabel.setVisible(false);
                CallLogCategoryPanel.setVisible(true);
            }
        });

        CallLogCategoryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        CallLogCategoryPanel.setOpaque(false);
        CallLogCategoryPanel.setPreferredSize(new Dimension(DefaultSettings.LEFT_FRIEND_LIST_WIDTH, DefaultSettings.MENUBER_HEIGHT));
        if (categoryPnl == null) {
            categoryPnl = new CategoryButtonPanel(mouseListener);
            categoryPnl.btnSelectAll.setText(CATEGORY_SELECT_ALL);
            categoryPnl.btnCancel.setText(CATEGORY_CANCEL);
            categoryPnl.btnDelete.setText(CATEGORY_DELETE);
        }
        CallLogCategoryPanel.add(categoryPnl);
        CallLogCategoryPanel.setVisible(false);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(6, 0, 0, 0),
                new MatteBorder(0, 0, 1, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR)));
        buttonPanel.setPreferredSize(new Dimension(DefaultSettings.LEFT_SINGLE_PANEL_WIDTH, DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT - 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(editLabel);
        buttonPanel.add(CallLogCategoryPanel);
        buttonPanel.setVisible(false);

        add(buttonPanel, BorderLayout.NORTH);
        chatListPanel.add(chatListWrapperPanel, BorderLayout.NORTH);
        wrapperPanel.add("CHAT_HISTORY", chatListPanel);
        chatHistoryShade.setVisible(true);
        chatListPanel.setVisible(true);
        JScrollPane chatScrollPanel = DesignClasses.getDefaultScrollPaneThin(wrapperPanel);
        add(chatScrollPanel, BorderLayout.CENTER);

    }

    public void loadRecentChatHistory() {
        new LoadRecentContactListFromDB().start();
    }

    MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            item = (CategoryButtonPanel.CustomPanel) e.getSource();

            if (item.text.equalsIgnoreCase(CATEGORY_SELECT_ALL)) {
                categoryPnl.btnSelectAll.setMouseClicked();
                categoryPnl.btnCancel.setMouseExited();
                categoryPnl.btnDelete.setMouseExited();
                categoryPnl.btnSelectAll.isSelected = false;
                if (categoryPnl.btnSelectAll.lblText.getText().equals(SELECT_ALL)) {
                    categoryPnl.btnSelectAll.lblText.setText(DESELECT_ALL);
                    for (SingleChatHistoryPanel ins : chatHistoryPanels) {
                        ins.deleteBox.setSelected(true);
                    }
                } else if (categoryPnl.btnSelectAll.lblText.getText().equals(DESELECT_ALL)) {
                    categoryPnl.btnSelectAll.lblText.setText(SELECT_ALL);
                    for (SingleChatHistoryPanel ins : chatHistoryPanels) {
                        ins.deleteBox.setSelected(false);
                    }
                }
            } else if (item.text.equalsIgnoreCase(CATEGORY_CANCEL)) {

                categoryPnl.btnCancel.setMouseClicked();
                categoryPnl.btnSelectAll.setMouseExited();
                categoryPnl.btnDelete.setMouseExited();
                categoryPnl.btnCancel.isSelected = false;
                for (SingleChatHistoryPanel ins : chatHistoryPanels) {
                    ins.checkboxPanel.setVisible(false);
                }
                editLabel.setVisible(true);
                CallLogCategoryPanel.setVisible(false);
            } else if (item.text.equalsIgnoreCase(CATEGORY_DELETE)) {
                categoryPnl.btnDelete.setMouseClicked();
                categoryPnl.btnSelectAll.setMouseExited();
                categoryPnl.btnCancel.setMouseExited();
                categoryPnl.btnDelete.isSelected = false;
                ArrayList<RecentDTO> tempList = new ArrayList<>();
                for (SingleChatHistoryPanel ins : chatHistoryPanels) {
                    if (ins.deleteBox.isSelected()) {
                        //     for (String str : ins.recentDTO.getContactId()) {
                        tempList.add(ins.recentDTO);
                        MyfnfHashMaps.getInstance().getSingleChatHistoryPanel().remove(ins.recentDTO.getContactId());
                        //  }
                    }
                }
                if (!tempList.isEmpty()) {
                    HelperMethods.showConfirmationDialogMessage("Do you want to completely delete these conversations?");
                    if (JOptionPanelBasics.YES_NO) {
                        editLabel.setVisible(true);
                        CallLogCategoryPanel.setVisible(false);
                        for (RecentDTO recentDTO : tempList) {
                            if (recentDTO.getMessageDTO().getGroupId() != null && recentDTO.getMessageDTO().getGroupId() > 0) {
                                GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(recentDTO.getMessageDTO().getGroupId());
                                if (groupPanel != null) {
                                    groupPanel.loadInitHistory(Boolean.TRUE);
                                }
                            } else {
                                MyFriendChatCallPanel callPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(recentDTO.getMessageDTO().getFriendIdentity());
                                if (callPanel != null) {
                                    callPanel.myFriendChatPanel.loadInitHistory(Boolean.TRUE);
                                }
                            }
                        }
                        RecentContactDAO.deletechatHistoryFromDB(tempList);
                        chatHistoryPanels.clear();
                        loadRecentChatHistory();
                    } else {
                    }
                } else {
                    new JOptionPanelBasics(JOptionPanelBasics.PLANE_MASSAGE, "Please select chat record to delete").setVisible(true);
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            CategoryButtonPanel.CustomPanel item = (CategoryButtonPanel.CustomPanel) e.getSource();
            if (!item.isSelected) {
                item.setMouseEntered();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {

            CategoryButtonPanel.CustomPanel item = (CategoryButtonPanel.CustomPanel) e.getSource();
            if (!item.isSelected) {
                item.setMouseExited();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
    };
    /*
     @Override
     public void actionPerformed(ActionEvent e) {
     if (e.getSource() == editLabel) {
     for (SingleChatHistoryPanel ins : chatHistoryPanels) {
     ins.checkboxPanel.setVisible(true);
     ins.deleteBox.setVisible(true);
     }
     editLabel.setVisible(false);
     cancelButton.setVisible(true);
     selectAll.setVisible(true);
     deleteButton.setVisible(true);

     } else if (e.getSource() == deleteButton) {
     ArrayList<RecentDTO> tempList = new ArrayList<>();
     for (SingleChatHistoryPanel ins : chatHistoryPanels) {
     if (ins.deleteBox.isSelected()) {
     //     for (String str : ins.recentDTO.getContactId()) {
     tempList.add(ins.recentDTO);
     MyfnfHashMaps.getInstance().getSingleChatHistoryPanel().remove(ins.recentDTO.getContactId());
     //  }
     }
     }
     if (!tempList.isEmpty()) {
     HelperMethods.showConfirmationDialogMessage("Do you want to completely delete these conversations?");
     if (JOptionPanelBasics.YES_NO) {
     editLabel.setVisible(true);
     selectAll.setVisible(false);
     deleteButton.setVisible(false);
     cancelButton.setVisible(false);
     for (RecentDTO recentDTO : tempList) {
     if (recentDTO.getMessageDTO().getGroupId() != null && recentDTO.getMessageDTO().getGroupId() > 0) {
     GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(recentDTO.getMessageDTO().getGroupId());
     if (groupPanel != null) {
     groupPanel.loadInitHistory(Boolean.TRUE);
     }
     } else {
     MyFriendChatCallPanel callPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(recentDTO.getMessageDTO().getFriendIdentity());
     if (callPanel != null) {
     callPanel.myFriendChatPanel.loadInitHistory(Boolean.TRUE);
     }
     }
     }
     RecentContactDAO.deletechatHistoryFromDB(tempList);
     chatHistoryPanels.clear();
     loadRecentChatHistory();
     } else {

     }

     } else {
     new JOptionPanelBasics(JOptionPanelBasics.PLANE_MASSAGE, "Please select chat record to delete").setVisible(true);
     }

     } else if (e.getSource() == cancelButton) {
     for (SingleChatHistoryPanel ins : chatHistoryPanels) {
     ins.checkboxPanel.setVisible(false);
     }
     editLabel.setVisible(true);
     selectAll.setVisible(false);
     deleteButton.setVisible(false);
     cancelButton.setVisible(false);
     } else if (e.getSource() == selectAll) {
     /*            if (selectAll.isSelected()) {
     for (SingleCallHistoryPanel ins : callHistoryPanels) {
     ins.deleteBox.setSelected(true);
     }
     } else if (!selectAll.isSelected()) {
     for (SingleCallHistoryPanel ins : callHistoryPanels) {
     ins.deleteBox.setSelected(false);
     }
     }*/
    /*    if (selectAll.getText().equals(SELECT_ALL)) {
     selectAll.setText(DESELECT_ALL);
     for (SingleChatHistoryPanel ins : chatHistoryPanels) {
     ins.deleteBox.setSelected(true);
     }
     } else if (selectAll.getText().equals(DESELECT_ALL)) {
     selectAll.setText(SELECT_ALL);
     for (SingleChatHistoryPanel ins : chatHistoryPanels) {
     ins.deleteBox.setSelected(false);
     }
     }

     }
     }*/

    class LoadRecentContactListFromDB extends Thread {

        @Override
        public void run() {
            List<RecentDTO> recentDTOs = RecentContactDAO.loadRecentChatContactList();
            if (recentDTOs.isEmpty()) {
                buttonPanel.setVisible(false);
            } else {
                buttonPanel.setVisible(true);
            }
            chatListWrapperPanel.removeAll();
            setChatSelectionColor(null);

            for (RecentDTO recentDTO : recentDTOs) {
                SingleChatHistoryPanel chatPanel = MyfnfHashMaps.getInstance().getSingleChatHistoryPanel().get(recentDTO.getContactId());
                if (chatPanel != null) {
                    chatPanel.setRecentDTO(recentDTO);
                    chatPanel.changeStatus();
                } else {
                    chatPanel = new SingleChatHistoryPanel(recentDTO);
                }
                if (!editLabel.isVisible()) {
                    chatPanel.deleteBox.setVisible(true);
                    chatPanel.checkboxPanel.setVisible(true);
                } else {
                    chatPanel.deleteBox.setVisible(false);
                    chatPanel.checkboxPanel.setVisible(false);
                }
                chatListWrapperPanel.add(chatPanel);
                chatHistoryPanels.add(chatPanel);
                contactIds.add(recentDTO.getContactId());
            }

            Set<String> contactIdSet = MyfnfHashMaps.getInstance().getSingleChatHistoryPanel().keySet();
            for (String id : contactIdSet) {
                if (contactIds.contains(id) == false) {
                    MyfnfHashMaps.getInstance().getSingleChatHistoryPanel().remove(id);
                }
            }
            chatListWrapperPanel.revalidate();
            chatListWrapperPanel.repaint();
        }
    }

    public static void setChatSelectionColor(SingleChatHistoryPanel chatPanel) {
        //  selectedId = null;
        if (selectedChatHistoryPanel != null) {
            selectedChatHistoryPanel.setBackground(Color.WHITE);
        }
        if (chatPanel != null) {
            //    selectedId = chatPanel.contactId;
            chatPanel.setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
        }
        selectedChatHistoryPanel = chatPanel;
        GuiRingID.getInstance().getMainLeftContainer().getChatHistoryContainer().chatHistoryShade.repaint();
        GuiRingID.getInstance().getMainLeftContainer().getChatHistoryContainer().chatHistoryShade.revalidate();
    }

    public static void changeAllInformation(String contactId) {
        SingleChatHistoryPanel chatPanel = MyfnfHashMaps.getInstance().getSingleChatHistoryPanel().get(contactId);
        if (chatPanel != null) {
            chatPanel.changeAllInformation();
        }
    }

    public static void changeFullName(String contactId) {
        SingleChatHistoryPanel chatPanel = MyfnfHashMaps.getInstance().getSingleChatHistoryPanel().get(contactId);
        if (chatPanel != null) {
            chatPanel.changeFullName();
        }
    }

    public static void changeStatus(RecentDTO recent) {
        if (GuiRingID.getInstance() != null
                && GuiRingID.getInstance().getMainLeftContainer() != null
                && GuiRingID.getInstance().getMainLeftContainer().getChatHistoryContainer() != null) {
            SingleChatHistoryPanel chatPanel = MyfnfHashMaps.getInstance().getSingleChatHistoryPanel().get(recent.getContactId());
            if (chatPanel != null) {
                chatPanel.setRecentDTO(recent);
                chatPanel.changeStatus();
            } else {
                GuiRingID.getInstance().getMainLeftContainer().getChatHistoryContainer().loadRecentChatHistory();
            }
            // GuiRingID.getInstance().getMainLeftContainer().getChatHistoryContainer().loadRecentChatHistory();
        }
    }

    public static void clearNotificationLabel(String contactId) {
        SingleChatHistoryPanel chatPanel = MyfnfHashMaps.getInstance().getSingleChatHistoryPanel().get(contactId);
        if (chatPanel != null) {
            chatPanel.clearNotificationLabel();
        }
    }

}
