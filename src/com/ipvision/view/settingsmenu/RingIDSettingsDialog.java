/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.settingsmenu;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 *
 * @author Shahadat
 */
public class RingIDSettingsDialog extends JFrame {

//    public static RingIDSettingsDialog instance;
//
//    public static RingIDSettingsDialog getInstance() {
//        return instance;
//    }
    private JPanel content;
    private JPanel rightPanel;
    private JPanel leftPanel;
    private SingleSettingMenuItem mnuSoundSetting;
    private SingleSettingMenuItem mnuPrivacySetting;
    private SingleSettingMenuItem mnuFontSetting;
    private SingleSettingMenuItem mnuHistorySetting;
    public SingleSettingMenuItem mnuChatBackgroundSetting;
    private SingleSettingMenuItem mnuRingSetting;
    private SingleSettingMenuItem mnuCustomizeMsgSetting;
    private SingleSettingMenuItem mnuSuggestionSettings;
    private SingleSettingMenuItem mnuDeactivateSettings;

    private int posX = 0;
    private int posY = 0;

    private JLabel closeButton = DesignClasses.create_imageJlabel(GetImages.CLOSE);

    public static final String MNU_SOUND_SETTING = "Sound";
    public static final String MNU_PRIVACY_SETTING = "Privacy Settings";
    public static final String MNU_FONT_SETTING = "Font";
    //public static final String MNU_MAIL_SETTING = "Mail Settings";
    public static final String MNU_HISTORY_SETTING = "History";
    public static final String MNU_CHAT_BACKGROUND_SETTING = "Chat Background";
    public static final String MNU_RING_SETTING = "Ring Settings";
    public static final String MNU_CUSTOMIZE_MSG_SETTING = "Customize Messages";
    public static final String MNU_SUGGESTION_SETTING = "Suggestion Settings";
    public static final String MNU_DEACTIVATE_SETTING = "Deactivate Account";

    private SingleSettingMenuItem selectedMenu;

    private SoundSettingPanel soundSettingPanel;
    //private MailSettingPanel mailSettingPanel;
    private FontSettingPanel fontSettingPanel;
    private PrivacySettingPanel privacySettingPanel;
    private HistorySettingPanel historySettingPanel;
    private ChatBackgroundSetting chatBackgroundSetting;
    private RingSettingPanel ringSettingPanel;
    private CustomizeMessagesSettingsPanel customizeMessagesSettingsPanel;
    private SuggestionSettingPanel suggestionSettingPanel;
    private DeactivateAccountSettingsPanel deactivateAccountSettingsPanel;

    public SuggestionSettingPanel getSuggestionSettingPanel() {
        return suggestionSettingPanel;
    }

//    public MailSettingPanel getMailSettingPanel() {
//        return mailSettingPanel;
//    }
//
//    public void setMailSettingPanel(MailSettingPanel mailSettingPanel) {
//        this.mailSettingPanel = mailSettingPanel;
//    }
    public void setSuggestionSettingPanel(SuggestionSettingPanel suggestionSettingPanel) {
        this.suggestionSettingPanel = suggestionSettingPanel;
    }

    public SoundSettingPanel getSoundSettingPanel() {
        return soundSettingPanel;
    }

    public void setSoundSettingPanel(SoundSettingPanel soundSettingPanel) {
        this.soundSettingPanel = soundSettingPanel;
    }

    public PrivacySettingPanel getPrivacySettingPanel() {
        return privacySettingPanel;
    }

    public void setPrivacySettingPanel(PrivacySettingPanel privacySettingPanel) {
        this.privacySettingPanel = privacySettingPanel;
    }

    public HistorySettingPanel getHistorySettingPanel() {
        return historySettingPanel;
    }

    public void setHistorySettingPanel(HistorySettingPanel historySettingPanel) {
        this.historySettingPanel = historySettingPanel;
    }

    public RingSettingPanel getRingSettingPanel() {
        return ringSettingPanel;
    }

    public void setRingSettingPanel(RingSettingPanel ringSettingPanel) {
        this.ringSettingPanel = ringSettingPanel;
    }

    public FontSettingPanel getFontSettingPanel() {
        return fontSettingPanel;
    }

    public void setFontSettingPanel(FontSettingPanel fontSettingPanel) {
        this.fontSettingPanel = fontSettingPanel;
    }

    public CustomizeMessagesSettingsPanel getCustomizeMessagesSettingsPanel() {
        return customizeMessagesSettingsPanel;
    }

    public void setCustomizeMessagesSettingsPanel(CustomizeMessagesSettingsPanel customizeMessagesSettingsPanel) {
        this.customizeMessagesSettingsPanel = customizeMessagesSettingsPanel;
    }

    public DeactivateAccountSettingsPanel getDeactivateAccountSettingsPanel() {
        return deactivateAccountSettingsPanel;
    }

    public void setDeactivateAccountSettingsPanel(DeactivateAccountSettingsPanel deactivateAccountSettingsPanel) {
        this.deactivateAccountSettingsPanel = deactivateAccountSettingsPanel;
    }

    public RingIDSettingsDialog() {
        // instance = this;
        setUndecorated(true);
        setSize(DefaultSettings.SETTINGS_DEFAULT_WIDTH - 80, DefaultSettings.FRAME_DEFAULT_HEIGHT - 30);
        setLocationRelativeTo(null);
        setTitle("Options");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        ImageIcon imageIcon = DesignClasses.return_image(GetImages.ICON_24MYFNF);
        Image image = imageIcon.getImage();
        setIconImage(image);

        content = (JPanel) getContentPane();
        content.setLayout(new BorderLayout());
        content.setBorder(new MatteBorder(1, 1, 1, 1, DefaultSettings.APP_DEFAULT_THEME_COLOR));
        content.setBackground(DefaultSettings.APP_DEFAULT_MENUBAR_BG_COLOR);
        content.addMouseListener(poxValueListener);
        content.addMouseMotionListener(frameDragListener);

        initContents();
    }

    private void initContents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(8, 13, 13, 13));
        mainPanel.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
        content.add(mainPanel, BorderLayout.CENTER);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 40));
        headerPanel.setOpaque(false);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        headerPanel.add(titlePanel, BorderLayout.WEST);

        JLabel titleLabel = DesignClasses.makeLableBold1("Options", 16);
        titlePanel.add(titleLabel);

        JPanel crossPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        crossPanel.setOpaque(false);
        crossPanel.addMouseListener(poxValueListener);
        crossPanel.addMouseMotionListener(frameDragListener);
        crossPanel.add(closeButton);
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hideThis();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setIcon(DesignClasses.return_image(GetImages.CLOSE_H));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setIcon(DesignClasses.return_image(GetImages.CLOSE));
            }
        });
        closeButton.setToolTipText("Close");
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        headerPanel.add(crossPanel, BorderLayout.EAST);

        JPanel centralPanel = new JPanel(new BorderLayout());
        centralPanel.setBackground(Color.WHITE);
        mainPanel.add(centralPanel, BorderLayout.CENTER);

        leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JPanel leftPanelWrapper = new JPanel(new BorderLayout());
        leftPanelWrapper.setBorder(new EmptyBorder(5, 5, 5, 5));
        leftPanelWrapper.setPreferredSize(new Dimension(220, 0));
        leftPanelWrapper.setOpaque(false);
        leftPanelWrapper.add(leftPanel, BorderLayout.NORTH);
        centralPanel.add(leftPanelWrapper, BorderLayout.WEST);

        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(new EmptyBorder(5, 0, 5, 5));
        centralPanel.add(rightPanel, BorderLayout.CENTER);

        buildLeftMenu();
    }

    private void buildLeftMenu() {
        mnuRingSetting = new SingleSettingMenuItem(MNU_RING_SETTING);
        leftPanel.add(mnuRingSetting);
        setMenuMouseListener(mnuRingSetting);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        mnuPrivacySetting = new SingleSettingMenuItem(MNU_PRIVACY_SETTING);
        leftPanel.add(mnuPrivacySetting);
        setMenuMouseListener(mnuPrivacySetting);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        mnuHistorySetting = new SingleSettingMenuItem(MNU_HISTORY_SETTING);
        leftPanel.add(mnuHistorySetting);
        setMenuMouseListener(mnuHistorySetting);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        mnuChatBackgroundSetting = new SingleSettingMenuItem(MNU_CHAT_BACKGROUND_SETTING);
        leftPanel.add(mnuChatBackgroundSetting);
        setMenuMouseListener(mnuChatBackgroundSetting);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        mnuFontSetting = new SingleSettingMenuItem(MNU_FONT_SETTING);
        leftPanel.add(mnuFontSetting);
        setMenuMouseListener(mnuFontSetting);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        mnuSoundSetting = new SingleSettingMenuItem(MNU_SOUND_SETTING);
        leftPanel.add(mnuSoundSetting);
        setMenuMouseListener(mnuSoundSetting);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        mnuCustomizeMsgSetting = new SingleSettingMenuItem(MNU_CUSTOMIZE_MSG_SETTING);
        leftPanel.add(mnuCustomizeMsgSetting);
        setMenuMouseListener(mnuCustomizeMsgSetting);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        mnuSuggestionSettings = new SingleSettingMenuItem(MNU_SUGGESTION_SETTING);
        leftPanel.add(mnuSuggestionSettings);
        setMenuMouseListener(mnuSuggestionSettings);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        mnuDeactivateSettings = new SingleSettingMenuItem(MNU_DEACTIVATE_SETTING);
        leftPanel.add(mnuDeactivateSettings);
        setMenuMouseListener(mnuDeactivateSettings);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        setSelectedMenu(mnuRingSetting);
    }

    private void setMenuMouseListener(final SingleSettingMenuItem component) {
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setSelectedMenu(component);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selectedMenu.mnuName.equalsIgnoreCase(component.mnuName)) {
                    component.setBackground(Color.WHITE);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!selectedMenu.mnuName.equalsIgnoreCase(component.mnuName)) {
                    component.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
                }
            }
        });
    }

    private void setSelectedMenu(SingleSettingMenuItem component) {
        selectedMenu = component;

        if (mnuHistorySetting.mnuName.equalsIgnoreCase(selectedMenu.mnuName)) {
            mnuHistorySetting.setBackground(Color.WHITE);
            rightPanel.removeAll();
            if (historySettingPanel == null) {
                setAlwaysOnTop(true);
                historySettingPanel = new HistorySettingPanel();
                setAlwaysOnTop(false);
            }
            //chatSettingPanel = ChatSettingPanel.getInstance();
            rightPanel.add(historySettingPanel, BorderLayout.CENTER);
            rightPanel.revalidate();
            rightPanel.repaint();
        } else {
            mnuHistorySetting.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
        }
        if (mnuChatBackgroundSetting.mnuName.equalsIgnoreCase(selectedMenu.mnuName)) {
            mnuChatBackgroundSetting.setBackground(Color.WHITE);
            rightPanel.removeAll();
            if (chatBackgroundSetting == null) {
                chatBackgroundSetting = new ChatBackgroundSetting();
            }
            //chatSettingPanel = ChatSettingPanel.getInstance();
            rightPanel.add(chatBackgroundSetting, BorderLayout.CENTER);
            rightPanel.revalidate();
            rightPanel.repaint();
        } else {
            mnuChatBackgroundSetting.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
        }

        if (mnuSoundSetting.mnuName.equalsIgnoreCase(selectedMenu.mnuName)) {
            mnuSoundSetting.setBackground(Color.WHITE);
            rightPanel.removeAll();
            if (soundSettingPanel == null) {
                soundSettingPanel = new SoundSettingPanel();
            }
            //   soundSettingPanel = SoundSettingPanel.getInstance();
            rightPanel.add(soundSettingPanel, BorderLayout.CENTER);
            rightPanel.revalidate();
            rightPanel.repaint();
        } else {
            mnuSoundSetting.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
        }

        if (mnuPrivacySetting.mnuName.equalsIgnoreCase(selectedMenu.mnuName)) {
            mnuPrivacySetting.setBackground(Color.WHITE);
            rightPanel.removeAll();
            if (privacySettingPanel == null) {
                privacySettingPanel = new PrivacySettingPanel();
            }
            //  privacySettingPanel = PrivacySettingPanel.getInstance();
            rightPanel.add(privacySettingPanel, BorderLayout.CENTER);
            rightPanel.revalidate();
            rightPanel.repaint();
        } else {
            mnuPrivacySetting.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
        }

        if (mnuFontSetting.mnuName.equalsIgnoreCase(selectedMenu.mnuName)) {
            mnuFontSetting.setBackground(Color.WHITE);
            rightPanel.removeAll();
            if (fontSettingPanel == null) {
                fontSettingPanel = new FontSettingPanel();
            }
            rightPanel.add(fontSettingPanel, BorderLayout.CENTER);
            rightPanel.revalidate();
            rightPanel.repaint();
        } else {
            mnuFontSetting.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
        }

        if (mnuSuggestionSettings.mnuName.equalsIgnoreCase(selectedMenu.mnuName)) {
            mnuSuggestionSettings.setBackground(Color.WHITE);
            rightPanel.removeAll();
            if (suggestionSettingPanel == null) {
                suggestionSettingPanel = new SuggestionSettingPanel();
            }
            rightPanel.add(suggestionSettingPanel, BorderLayout.CENTER);
            rightPanel.revalidate();
            rightPanel.repaint();
        } else {
            mnuSuggestionSettings.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
        }

        if (mnuCustomizeMsgSetting.mnuName.equalsIgnoreCase(selectedMenu.mnuName)) {
            mnuCustomizeMsgSetting.setBackground(Color.WHITE);
            rightPanel.removeAll();
            if (customizeMessagesSettingsPanel == null) {
                customizeMessagesSettingsPanel = new CustomizeMessagesSettingsPanel();
            }
            rightPanel.add(customizeMessagesSettingsPanel, BorderLayout.CENTER);
            rightPanel.revalidate();
            rightPanel.repaint();
        } else {
            mnuCustomizeMsgSetting.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
        }
        
        if (mnuRingSetting.mnuName.equalsIgnoreCase(selectedMenu.mnuName)) {
            mnuRingSetting.setBackground(Color.WHITE);
            rightPanel.removeAll();
            if (ringSettingPanel == null) {
                ringSettingPanel = new RingSettingPanel();
            }
            rightPanel.add(ringSettingPanel, BorderLayout.CENTER);
            rightPanel.revalidate();
            rightPanel.repaint();
        } else {
            mnuRingSetting.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
        }

        if (mnuDeactivateSettings.mnuName.equalsIgnoreCase(selectedMenu.mnuName)) {
            mnuDeactivateSettings.setBackground(Color.WHITE);
            rightPanel.removeAll();
            if (deactivateAccountSettingsPanel == null) {
                deactivateAccountSettingsPanel = new DeactivateAccountSettingsPanel();
            }
            rightPanel.add(deactivateAccountSettingsPanel, BorderLayout.CENTER);
            rightPanel.revalidate();
            rightPanel.repaint();
        } else {
            mnuDeactivateSettings.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
        }
    }

    public void hideThis() {
        dispose();
        //  instance = null;
        Runtime.getRuntime().gc();
    }

    MouseAdapter poxValueListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            posX = e.getX();
            posY = e.getY();
        }
    };

    MouseMotionAdapter frameDragListener = new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent evt) {
            setLocation(evt.getXOnScreen() - posX, evt.getYOnScreen() - posY);
        }
    };
}
