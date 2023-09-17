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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.RecentDTO;
import com.ipvision.model.dao.RecentContactDAO;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.utility.CategoryButtonPanel;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.utility.JOptionPanelBasics;

/**
 *
 * @author Sirat Samyoun
 */
public class CallHistoryContainer extends JPanel {

    private JPanel callHistoryShade, buttonPanel;
    public JPanel callListWrapperPanel;
    public JPanel callListCategoryPanel;
    public static String CATEGORY_ALL = "All";
    public static String CATEGORY_MISSED = "Missed";
    public CategoryButtonPanel.CustomPanel item;
    //private static String selectedId = null;
    private static SingleCallHistoryPanel selectedCallHistoryPanel = null;
    public BufferedImage deleteImage = null, deleteHoverImg = null, deleteSelectedImg = null;
    //private JButton editButton;

    private List<SingleCallHistoryPanel> callHistoryPanels = new ArrayList<>();
    private List<SingleCallHistoryPanel> temp = new ArrayList<>();
    private JLabel editLabel;
    // private String SELECT_ALL = "Select All";
    // private String DESELECT_ALL = "Deselect All";
    private JPanel CallLogCategoryPanel;
    private String CATEGORY_SELECT_ALL = "Select All";
    private String CATEGORY_DESELECT_ALL = "Deselect All";
    private String CATEGORY_CANCEL = "Cancel";
    private String CATEGORY_DELETE = "Delete";
    CategoryButtonPanel categoryPnl;
    public JPanel categoryPanel;
    public boolean confirmation;
    public List<Long> selectedCheckboxList = new ArrayList<Long>();

    public CallHistoryContainer() {
        setLayout(new BorderLayout(DefaultSettings.DEFAULT_BORDER_WIDTH, DefaultSettings.DEFAULT_BORDER_WIDTH));
        setOpaque(false);

        JPanel wrapperPanel = new JPanel(new CardLayout());
        wrapperPanel.setOpaque(false);

        callHistoryShade = new JPanel() {
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
        callHistoryShade.setOpaque(false);
        wrapperPanel.add("CALL_HISTORY_SHADE", callHistoryShade);

        callListWrapperPanel = new JPanel();
        callListWrapperPanel.setLayout(new BoxLayout(callListWrapperPanel, BoxLayout.Y_AXIS));
        callListWrapperPanel.setOpaque(false);
        JPanel callListHolder = new JPanel(new BorderLayout());
        callListHolder.setOpaque(false);

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
                for (SingleCallHistoryPanel ins : callHistoryPanels) {
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
        buttonPanel.setBorder(new EmptyBorder(5, 0, 0, 0));

        buttonPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(6, 0, 0, 0),
                new MatteBorder(0, 0, 1, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR)));
        buttonPanel.setPreferredSize(new Dimension(DefaultSettings.LEFT_SINGLE_PANEL_WIDTH, DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT - 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(editLabel);
        buttonPanel.add(CallLogCategoryPanel);
        buttonPanel.setVisible(false);
        add(buttonPanel, BorderLayout.NORTH);
        callListHolder.add(callListWrapperPanel, BorderLayout.NORTH);
        wrapperPanel.add("CALL_HISTORY", callListHolder);
        callHistoryShade.setVisible(true);
        callListHolder.setVisible(true);
        JScrollPane callScrollPanel = DesignClasses.getDefaultScrollPaneThin(wrapperPanel);
        add(callScrollPanel, BorderLayout.CENTER);
    }

    public void loadCallListData() {
        new LoadRecentCallListFromDB().start();
    }

    private boolean isAllPanelsSelected() {
        for (int i = 0; i < callListWrapperPanel.getComponentCount(); i++) {
            SingleCallHistoryPanel sp = (SingleCallHistoryPanel) callListWrapperPanel.getComponent(i);
            if (!sp.deleteBox.isSelected()) {
                return false;
            }
        }
        return true;
    }

    private boolean isAllPanelsDeselected() {
        for (int i = 0; i < callListWrapperPanel.getComponentCount(); i++) {
            SingleCallHistoryPanel sp = (SingleCallHistoryPanel) callListWrapperPanel.getComponent(i);
            if (sp.deleteBox.isSelected()) {
                return false;
            }
        }
        return true;
    }

    public void action_all_selected() {
        if (isAllPanelsDeselected()) {
            categoryPnl.btnSelectAll.setText(CATEGORY_SELECT_ALL);
        } else if (isAllPanelsSelected()) {
            categoryPnl.btnSelectAll.setText(CATEGORY_DESELECT_ALL);
        }
        categoryPnl.btnSelectAll.revalidate();
    }
    MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            item = (CategoryButtonPanel.CustomPanel) e.getSource();

            if (item.text.equalsIgnoreCase(CATEGORY_SELECT_ALL) || (item.text.equalsIgnoreCase(CATEGORY_DESELECT_ALL))) {
                categoryPnl.btnSelectAll.setMouseClicked();
                categoryPnl.btnCancel.setMouseExited();
                categoryPnl.btnDelete.setMouseExited();
                categoryPnl.btnSelectAll.isSelected = false;
                if (categoryPnl.btnSelectAll.lblText.getText().equalsIgnoreCase(CATEGORY_SELECT_ALL)) {
                    categoryPnl.btnSelectAll.lblText.setText(CATEGORY_DESELECT_ALL);
                    for (SingleCallHistoryPanel ins : callHistoryPanels) {
                        ins.deleteBox.setSelected(true);
                    }
                } else if (categoryPnl.btnSelectAll.lblText.getText().equalsIgnoreCase(CATEGORY_DESELECT_ALL)) {
                    categoryPnl.btnSelectAll.lblText.setText(CATEGORY_SELECT_ALL);
                    for (SingleCallHistoryPanel ins : callHistoryPanels) {
                        ins.deleteBox.setSelected(false);

                    }
                }
            } else if (item.text.equalsIgnoreCase(CATEGORY_CANCEL)) {
                categoryPnl.btnCancel.setMouseClicked();
                categoryPnl.btnSelectAll.setMouseExited();
                categoryPnl.btnDelete.setMouseExited();
                categoryPnl.btnCancel.isSelected = false;
                for (SingleCallHistoryPanel ins : callHistoryPanels) {
                    ins.checkboxPanel.setVisible(false);
                    callListWrapperPanel.revalidate();
                    callListWrapperPanel.repaint();
                }
                editLabel.setVisible(true);
                CallLogCategoryPanel.setVisible(false);

            } else if (item.text.equalsIgnoreCase(CATEGORY_DELETE)) {
                ArrayList<String> tempCallIDList = new ArrayList<>();
                ArrayList<String> userIDList = new ArrayList<>();
                categoryPnl.btnDelete.setMouseClicked();
                categoryPnl.btnSelectAll.setMouseExited();
                categoryPnl.btnCancel.setMouseExited();
                categoryPnl.btnDelete.isSelected = false;
                confirmation = true;

                for (SingleCallHistoryPanel ins : callHistoryPanels) {
                    if (ins.deleteBox.isSelected()) {
                        if (confirmation) {
                            HelperMethods.showConfirmationDialogMessage("Do you want to completely delete these records?");
                        }
                        if (JOptionPanelBasics.YES_NO) {
                            for (int i = 0; i < callListWrapperPanel.getComponentCount(); i++) {
                                SingleCallHistoryPanel sp = (SingleCallHistoryPanel) callListWrapperPanel.getComponent(i);
                                if (ins.recentDTO.getCallLog().getCallingTime().longValue() == sp.recentDTO.getCallLog().getCallingTime().longValue()) {
                                    callListWrapperPanel.remove(i);
                                    confirmation = false;
                                }
                            }
                            userIDList.add(ins.recentDTO.getContactId());
                            for (String str : ins.recentDTO.getCallIds()) {
                                tempCallIDList.add(str);
                            }
                            //callHistoryPanels.remove(ins);

                            ins.deleteBox.setSelected(false);
                            ins.checkboxPanel.setVisible(false);
                            ins.deleteBox.setVisible(false);

                            callListWrapperPanel.revalidate();
                            callListWrapperPanel.repaint();

                        } else {
                            confirmation = false;
                        }
                    }

                }
                if (!tempCallIDList.isEmpty()) {
                    editLabel.setVisible(true);
                    CallLogCategoryPanel.setVisible(false);

                    for (int i = 0; i < callListWrapperPanel.getComponentCount(); i++) {
                        SingleCallHistoryPanel sp = (SingleCallHistoryPanel) callListWrapperPanel.getComponent(i);
                        sp.checkboxPanel.setVisible(false);
                        sp.deleteBox.setVisible(false);
                    }

                    if (callListWrapperPanel.getComponentCount() < 1) {
                        buttonPanel.setVisible(false);
                    } else {
                        buttonPanel.setVisible(true);
                    }

                    for (String tmp : userIDList) {
                        MyFriendChatCallPanel callPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(tmp);
                        if (callPanel != null) {
                            callPanel.myFriendChatPanel.loadInitHistory(Boolean.TRUE);
                            //callPanel.myFriendChatPanel.removeSingleCallPanel(userIDList);
                        }
                    }
                    RecentContactDAO.deleteCallHistoryFromDB(tempCallIDList);
                    tempCallIDList.clear();
                } else if (confirmation) {
                    new JOptionPanelBasics(JOptionPanelBasics.PLANE_MASSAGE, "Please select call record to delete").setVisible(true);
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
     if (e.getSource() == editButton) {
     for (SingleCallHistoryPanel ins : callHistoryPanels) {
     ins.checkboxPanel.setVisible(true);
     ins.deleteBox.setVisible(true);
     }
     editButton.setVisible(false);
     CallLogCategoryPanel.setVisible(true);
     //  cancelButton.setVisible(true);
     // selectAll.setVisible(true);
     // deleteButton.setVisible(true);

     } else if (e.getSource() == categoryPnl.btnDelete) {
     ArrayList<String> tempCallIDList = new ArrayList<>();
     ArrayList<String> userIDList = new ArrayList<>();

     for (SingleCallHistoryPanel ins : callHistoryPanels) {
     if (ins.deleteBox.isSelected()) {
     for (int i = 0; i < callListWrapperPanel.getComponentCount(); i++) {
     SingleCallHistoryPanel sp = (SingleCallHistoryPanel) callListWrapperPanel.getComponent(i);
     if (ins.recentDTO.getCallLog().getCallingTime().longValue() == sp.recentDTO.getCallLog().getCallingTime().longValue()) {
     callListWrapperPanel.remove(i);
     }
     }
     userIDList.add(ins.recentDTO.getContactId());
     for (String str : ins.recentDTO.getCallIds()) {
     tempCallIDList.add(str);
     }
     //callHistoryPanels.remove(ins);
     } else {
     ins.checkboxPanel.setVisible(false);
     ins.deleteBox.setVisible(false);
     }
     callListWrapperPanel.revalidate();
     callListWrapperPanel.repaint();
     }
     if (!tempCallIDList.isEmpty()) {
     editButton.setVisible(true);
     // selectAll.setVisible(false);
     //  deleteButton.setVisible(false);
     //   cancelButton.setVisible(false);
     CallLogCategoryPanel.setVisible(true);
     for (String tmp : userIDList) {
     MyFriendChatCallPanel callPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(tmp);
     if (callPanel != null) {
     callPanel.myFriendChatPanel.loadInitHistory(Boolean.TRUE);
     }
     }
     RecentContactDAO.deleteCallHistoryFromDB(tempCallIDList);
     // callHistoryPanels.clear();
     //deleteCallRecordsFromUI(temp);
     //loadCallListData();
     } else {
     new JOptionPanelBasics(JOptionPanelBasics.PLANE_MASSAGE, "Please select call record to delete").setVisible(true);
     }

     } else if (e.getSource() == categoryPnl.btnCancel) {

     for (SingleCallHistoryPanel ins : callHistoryPanels) {
     ins.checkboxPanel.setVisible(false);
     }
     editButton.setVisible(true);
     CallLogCategoryPanel.setVisible(false);

     } else if (e.getSource() == categoryPnl.btnSelectAll) {
     /*            if (selectAll.isSelected()) {
     for (SingleCallHistoryPanel ins : callHistoryPanels) {
     ins.deleteBox.setSelected(true);
     }
     } else if (!selectAll.isSelected()) {
     for (SingleCallHistoryPanel ins : callHistoryPanels) {
     ins.deleteBox.setSelected(false);
     }
     }*/

    /*         if (categoryPnl.btnSelectAll.lblText.getText().equals(SELECT_ALL)) {
     categoryPnl.btnSelectAll.lblText.setText(DESELECT_ALL);
     for (SingleCallHistoryPanel ins : callHistoryPanels) {
     ins.deleteBox.setSelected(true);
     }
     } else if (selectAll.getText().equals(DESELECT_ALL)) {
     categoryPnl.btnSelectAll.lblText.setText(SELECT_ALL);
     for (SingleCallHistoryPanel ins : callHistoryPanels) {
     ins.deleteBox.setSelected(false);
     }
     }

     }
     }
     */
    class LoadRecentCallListFromDB extends Thread {

        @Override
        public void run() {
            List<RecentDTO> recentDTOs = RecentContactDAO.loadRecentCallContactList();

            for (SingleCallHistoryPanel ins : callHistoryPanels) {
                if (ins.deleteBox.isSelected()) {
                selectedCheckboxList.add(ins.recentDTO.getCallLog().getCallingTime().longValue());
                }
            }

            if (recentDTOs.isEmpty()) {
                buttonPanel.setVisible(false);
            } else {
                buttonPanel.setVisible(true);
            }
            setCallSelectionColor(null);

            int totalComponent = callListWrapperPanel.getComponentCount();
            RecentDTO firstDTO = recentDTOs.size() > 0 ? recentDTOs.get(0) : null;
            SingleCallHistoryPanel callPanel = (SingleCallHistoryPanel) (totalComponent > 0 ? callListWrapperPanel.getComponent(0) : null);

            if (!(recentDTOs.size() == totalComponent
                    && firstDTO != null
                    && callPanel != null
                    && callPanel.recentDTO.getContactId().equalsIgnoreCase(firstDTO.getContactId())
                    && Objects.equals(callPanel.recentDTO.getType(), firstDTO.getCallLog().getCallType()))) {
                callListWrapperPanel.removeAll();
                callHistoryPanels.clear();
                MyfnfHashMaps.getInstance().getSingleCallHistoryPanel().clear();
                for (RecentDTO recentDTO : recentDTOs) {
                    SingleCallHistoryPanel ins = new SingleCallHistoryPanel(recentDTO);
                    if (!editLabel.isVisible()) {
                        // ins.deleteBox.setVisible(true);
                        ins.checkboxPanel.setVisible(true);
                        if (selectedCheckboxList.contains(ins.recentDTO.getCallLog().getCallingTime().longValue())) {
                            ins.deleteBox.setSelected(true);
                        }
                    } else {
                        // ins.deleteBox.setVisible(false);
                        ins.checkboxPanel.setVisible(false);
                    }
                    callHistoryPanels.add(ins);
                    callListWrapperPanel.add(ins);
                }
            }
            selectedCheckboxList.clear();
            callListWrapperPanel.revalidate();
            callListWrapperPanel.repaint();
        }
    }

    public static void changeStatus(RecentDTO recent) {
        if (GuiRingID.getInstance() != null
                && GuiRingID.getInstance().getMainLeftContainer() != null
                && GuiRingID.getInstance().getMainLeftContainer().getCallHistoryContainer() != null) {

            JPanel container = GuiRingID.getInstance().getMainLeftContainer().getCallHistoryContainer().callListWrapperPanel;
            SingleCallHistoryPanel callPanel = (SingleCallHistoryPanel) (container.getComponentCount() > 0 ? container.getComponent(0) : null);

            if (callPanel != null
                    && callPanel.recentDTO.getContactId().equalsIgnoreCase(recent.getContactId())
                    && Objects.equals(callPanel.recentDTO.getCallLog().getCallType(), recent.getCallLog().getCallType())) {
                callPanel.recentDTO.setTime(recent.getTime());
                callPanel.recentDTO.setCallCount(callPanel.recentDTO.getCallCount() + 1);
                callPanel.recentDTO.getCallLog().setCallingTime(recent.getCallLog().getCallingTime());
                callPanel.recentDTO.getCallLog().setCallDuration(recent.getCallLog().getCallDuration());
                callPanel.recentDTO.getCallIds().add(recent.getCallLog().getCallID());
                callPanel.changeStatus();
            } else {
                GuiRingID.getInstance().getMainLeftContainer().getCallHistoryContainer().loadCallListData();
            }
        }
    }

    public static void setCallSelectionColor(SingleCallHistoryPanel callPanel) {
        //selectedId = null;
        if (selectedCallHistoryPanel != null) {
            selectedCallHistoryPanel.setBackground(Color.WHITE);
        }
        if (callPanel != null) {
            //selectedId = callPanel.contactId;
            callPanel.setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
        }
        selectedCallHistoryPanel = callPanel;
        GuiRingID.getInstance().getMainLeftContainer().getCallHistoryContainer().callHistoryShade.repaint();
        GuiRingID.getInstance().getMainLeftContainer().getCallHistoryContainer().callHistoryShade.revalidate();
    }

    public static void clearNotificationLabel(String contactId) {
        Set<String> keySet = MyfnfHashMaps.getInstance().getSingleCallHistoryPanel().keySet();
        for (String cId : keySet) {
            if (cId.endsWith("_" + contactId)) {
                SingleCallHistoryPanel callPanel = MyfnfHashMaps.getInstance().getSingleCallHistoryPanel().get(cId);
                if (callPanel != null) {
                    callPanel.clearNotificationLabel();
                }
            }
        }
    }

}
/*    class LoadRecentCallListFromDB extends Thread {

 @Override
 public void run() {
 List<RecentDTO> recentDTOs = RecentContactDAO.loadRecentCallContactList();
 if (recentDTOs.isEmpty()) {
 buttonPanel.setVisible(false);
 } else {
 buttonPanel.setVisible(true);
 }
 setCallSelectionColor(null);
 for (RecentDTO recentDTO : recentDTOs) {
 SingleCallHistoryPanel callPanel = MyfnfHashMaps.getInstance().getSingleCallHistoryPanel().get(recentDTO.getContactId());
 if (callPanel != null) {
 callPanel.setRecentDTO(recentDTO);
 callPanel.changeStatus();
 } else {
 callPanel = new SingleCallHistoryPanel(recentDTO);
 MyfnfHashMaps.getInstance().getSingleCallHistoryPanel().put(recentDTO.getContactId(), callPanel);
 }
 if (!editButton.isVisible()) {
 callPanel.deleteBox.setVisible(true);
 } else {
 callPanel.deleteBox.setVisible(false);
 }
 callHistoryPanels.add(callPanel);
 callListWrapperPanel.add(callPanel);
 }

 callListWrapperPanel.revalidate();
 callListWrapperPanel.repaint();
 }
 }*/
