/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.settingsmenu;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.constants.SettingsConstants;
import com.ipvision.view.utility.DesignClasses;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.model.dao.InsertIntoRingUserSettings;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.ChatBgImageDTO;
import com.ipvision.service.chat.LoadChatBackground;
import com.ipvision.view.utility.chat.ChatHashMap;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.utility.RoundedCornerButton;
import com.ipvision.view.utility.WrapLayout;

/**
 *
 * @author Dell
 */
public class ChatBackgroundSetting extends JPanel {

    private JPanel mainContent;
    private JScrollPane scrollPane;
    private JPanel buttonContainer;
    private JButton btnSave;
    private JButton btnBack;
    private JButton btnChangeChatBG;
    private Map<String, SingleChatBackgroundPanel> chatBackgroundPanelMap = new ConcurrentHashMap<String, SingleChatBackgroundPanel>();

    public final static String CHAT_SETTING_MAIN = "CHAT_SETTING_MAIN";
    public final static String CHAT_BG_CHNAGE = "CHAT_BG_CHNAGE";
    public String type = CHAT_SETTING_MAIN;
    public String selected_bg = null;
    public JButton clearHistoryBtn;
    private JComboBox historyComboBox;
    private long daysInSecond = 86400000;

    public ChatBackgroundSetting() {
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

        JLabel myNameLabel = DesignClasses.makeLableBold1("Chat Background", 15);
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

        btnSave = new RoundedCornerButton("Save Changes", "Save");
        //DesignClasses.createImageButton(GetImages.SAVE_CHANGES, GetImages.SAVE_CHANGES_H, "Save");
        btnSave.addActionListener(actionListener);

        buildChatBackgroundChangeUI();
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnSave) {
                if (type.equalsIgnoreCase(CHAT_BG_CHNAGE)) {
                    SettingsConstants.FNF_CHAT_BG = (selected_bg != null && selected_bg.length() > 0) ? selected_bg : SettingsConstants.FNF_DEAFULT_CHAT_BG;
                    new InsertIntoRingUserSettings().start();
                    new ChangeChatBackground().start();
                    GuiRingID.getInstance().getTopMenuBar().getRingIDSettingsDialog().hideThis();
                }
            } else if (btnChangeChatBG != null && e.getSource() == btnChangeChatBG) {
                buildChatBackgroundChangeUI();
            }
        }
    };

    private void buildChatBackgroundChangeUI() {
        selected_bg = null;
        type = CHAT_BG_CHNAGE;
        chatBackgroundPanelMap.clear();

        buttonContainer.removeAll();
        buttonContainer.add(btnSave);
        //buttonContainer.add(btnBack);
        buttonContainer.revalidate();
        buttonContainer.repaint();

        mainContent.removeAll();
        mainContent.setLayout(new WrapLayout(WrapLayout.LEFT, 10, 10));
        mainContent.add(DesignClasses.loadingLable(false));
        mainContent.revalidate();
        mainContent.repaint();

        new LoadChatBackground(this).start();
    }

    public void buildChatBackgroundImageUI() {
        selected_bg = null;
        mainContent.removeAll();
        for (ChatBgImageDTO entity : ChatHashMap.getInstance().getChatBgImage().values()) {
            SingleChatBackgroundPanel panel = new SingleChatBackgroundPanel(entity, this);
            chatBackgroundPanelMap.put(entity.getName(), panel);
            mainContent.add(panel);
        }
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

    private class ChangeChatBackground extends Thread {

        @Override
        public void run() {
            for (MyFriendChatCallPanel friendChatCallPanel : MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().values()) {
                if (friendChatCallPanel != null && friendChatCallPanel.myFriendChatPanel != null) {
                    friendChatCallPanel.myFriendChatPanel.setChatBackground();
                }
            }

            for (GroupPanel groupPanel : MyfnfHashMaps.getInstance().getGroupPanelMap().values()) {
                if (groupPanel != null) {
                    groupPanel.setChatBackground();
                }
            }
        }
    }

}
