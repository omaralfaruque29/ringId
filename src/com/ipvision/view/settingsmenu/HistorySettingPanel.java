/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.settingsmenu;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.dao.DeleteChatCallHistoryDAO;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.model.stores.MyfnfHashMaps;

/**
 *
 * @author Dell
 */
public class HistorySettingPanel extends JPanel {

    private JPanel mainContent;
    private JScrollPane scrollPane;
    private JPanel buttonContainer;
    private Map<String, SingleChatBackgroundPanel> chatBackgroundPanelMap = new ConcurrentHashMap<String, SingleChatBackgroundPanel>();

    public final static String CHAT_SETTING_MAIN = "CHAT_SETTING_MAIN";
    public final static String CHAT_BG_CHNAGE = "CHAT_BG_CHNAGE";
    public String type = CHAT_SETTING_MAIN;
    public String selected_bg = null;
    public JButton clearHistoryBtn;
    private JComboBox historyComboBox;
    private long daysInSecond = 86400000;

    public HistorySettingPanel() {
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.initComponents();
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout(5, 5));
        headerPanel.setPreferredSize(new Dimension(0, 30));
        headerPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        headerPanel.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
        add(headerPanel, BorderLayout.NORTH);

        JLabel myNameLabel = DesignClasses.makeLableBold1("History", 15);
        headerPanel.add(myNameLabel, BorderLayout.WEST);

        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBorder(new MatteBorder(0, 1, 0, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        bodyPanel.setOpaque(false);

        scrollPane = DesignClasses.getDefaultScrollPane(bodyPanel);
        add(scrollPane, BorderLayout.CENTER);

        mainContent = new JPanel();
        mainContent.setBorder(new EmptyBorder(7, 7, 7, 7));
        mainContent.setOpaque(false);
        bodyPanel.add(mainContent, BorderLayout.NORTH);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new MatteBorder(1, 0, 0, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        footerPanel.setOpaque(false);
        footerPanel.setPreferredSize(new Dimension(0, 35));
        add(footerPanel, BorderLayout.SOUTH);

        buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonContainer.setBorder(new EmptyBorder(0, 0, 0, 10));
        buttonContainer.setOpaque(false);
        footerPanel.add(buttonContainer, BorderLayout.CENTER);

        buildChatSettingAction();
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == clearHistoryBtn) {
                deleteChatCallActivityHistory();

                if (GuiRingID.getInstance() != null
                        && GuiRingID.getInstance().getMainLeftContainer() != null
                        && GuiRingID.getInstance().getMainLeftContainer().getCallHistoryContainer() != null) {
                    GuiRingID.getInstance().getMainLeftContainer().getCallHistoryContainer().loadCallListData();
                }
                if (GuiRingID.getInstance() != null
                        && GuiRingID.getInstance().getMainLeftContainer() != null
                        && GuiRingID.getInstance().getMainLeftContainer().getChatHistoryContainer() != null) {
                    GuiRingID.getInstance().getMainLeftContainer().getChatHistoryContainer().loadRecentChatHistory();
                }
                GuiRingID.getInstance().getTopMenuBar().getRingIDSettingsDialog().hideThis();
            }
        }
    };

    private void deleteChatCallActivityHistory() {
        Date currDate = new Date();
        long time = currDate.getTime();

        if (historyComboBox.getSelectedIndex() > 0) {
            currDate.setHours(0);
            currDate.setMinutes(0);
            currDate.setSeconds(0);
            time = currDate.getTime() - ((MyFnFSettings.HISTORY_ARRAY_DAY[historyComboBox.getSelectedIndex()]) * daysInSecond);
        }

        DeleteChatCallHistoryDAO.deleteChatCallHistory(time);

        for (MyFriendChatCallPanel chatCallPanel : MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().values()) {
            chatCallPanel.myFriendChatPanel.loadInitHistory(true);
        }

        for (GroupPanel groupPanel : MyfnfHashMaps.getInstance().getGroupPanelMap().values()) {
            groupPanel.loadInitHistory(true);
        }
    }

    private void buildChatSettingAction() {
        selected_bg = null;
        type = CHAT_SETTING_MAIN;
        chatBackgroundPanelMap.clear();

        buttonContainer.removeAll();
        buttonContainer.revalidate();
        buttonContainer.repaint();

        mainContent.removeAll();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));

        JPanel chatDeleteContainer = new JPanel(new BorderLayout(3, 0));
        chatDeleteContainer.setBorder(new EmptyBorder(4, 0, 0, 0));
        chatDeleteContainer.setOpaque(false);
        mainContent.add(chatDeleteContainer);
        JLabel deleteTitle = DesignClasses.makeJLabelFullName("Remove History older than ", 13);
        chatDeleteContainer.add(deleteTitle, BorderLayout.WEST);
        historyComboBox = DesignClasses.createJCombobox(MyFnFSettings.HISTORY_ARRAY);
        historyComboBox.setSelectedIndex(0);
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setOpaque(false);
        historyPanel.setBorder(new EmptyBorder(0, 150, 0, 60));
        historyPanel.add(historyComboBox, BorderLayout.CENTER);
        chatDeleteContainer.add(historyPanel, BorderLayout.CENTER);
        clearHistoryBtn = new JButton("Clear History");
        clearHistoryBtn.setOpaque(false);
        clearHistoryBtn.addActionListener(actionListener);
        chatDeleteContainer.add(clearHistoryBtn, BorderLayout.EAST);

        mainContent.revalidate();
        mainContent.repaint();
    }

    public void onBackgroundImageChange(String name) {
        selected_bg = null;
        for (SingleChatBackgroundPanel entity : chatBackgroundPanelMap.values()) {
            if (name.equalsIgnoreCase(entity.chatBgImageDTO.getName())) {
                selected_bg = name;
            } else {
                entity.chkSetBg.setSelected(false);
            }
        }
    }

}
