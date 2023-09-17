/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.settingsmenu;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.RingColorCode;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.ChangePrivacy;
import com.ipvision.service.ChangeSuggestionSettings;
import com.ipvision.view.utility.RoundedCornerButton;

/**
 *
 * @author Sirat Samyoun
 */
public class SuggestionSettingPanel extends JPanel implements ActionListener {

    private JButton btnSave;
    private JPanel mainContent;
    private JPanel buttonContainer;
    private JLabel label;
    public JCheckBox commonFriendSuggSettingBox;
    public JCheckBox phoneNumberSuggSettingBox;
    public JCheckBox contactListSuggSettingBox;
    private static final String COMMON_FRIEND = "Enable common friends suggestion";
    private static final String PHONE_NUMBER = "Enable phone number suggestion";
    private static final String CONTACT_LIST = "Enable contact list suggestion";

    public SuggestionSettingPanel() {
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

        JLabel myNameLabel = DesignClasses.makeLableBold1("Suggestion Settings", 15);
        headerPanel.add(myNameLabel, BorderLayout.WEST);

        JPanel bodyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        bodyPanel.setBorder(new MatteBorder(0, 1, 0, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        bodyPanel.setOpaque(false);
        add(bodyPanel, BorderLayout.CENTER);
        mainContent = new JPanel(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();
        con.gridx = 0;
        con.gridy = 0;
        con.insets = new Insets(10, 5, 5, 2);
        con.anchor = GridBagConstraints.WEST;

        mainContent.setOpaque(false);
        commonFriendSuggSettingBox = new JCheckBox();
        commonFriendSuggSettingBox.setOpaque(false);
        commonFriendSuggSettingBox.setText(COMMON_FRIEND);
        mainContent.add(commonFriendSuggSettingBox, con);
        con.gridy++;
        phoneNumberSuggSettingBox = new JCheckBox();
        phoneNumberSuggSettingBox.setOpaque(false);
        phoneNumberSuggSettingBox.setText(PHONE_NUMBER);
        mainContent.add(phoneNumberSuggSettingBox, con);
        con.gridy++;
        contactListSuggSettingBox = new JCheckBox();
        contactListSuggSettingBox.setOpaque(false);
        contactListSuggSettingBox.setText(CONTACT_LIST);
        mainContent.add(contactListSuggSettingBox, con);
        con.gridy++;

        if (MyFnFSettings.userProfile.getCommonFriendsSuggestion() != null && MyFnFSettings.userProfile.getCommonFriendsSuggestion().intValue() == 1) {
            commonFriendSuggSettingBox.setSelected(true);
        }
        if (MyFnFSettings.userProfile.getPhoneNumberSuggestion() != null && MyFnFSettings.userProfile.getPhoneNumberSuggestion().intValue() == 1) {
            phoneNumberSuggSettingBox.setSelected(true);
        }
        if (MyFnFSettings.userProfile.getContactListSuggestion() != null && MyFnFSettings.userProfile.getContactListSuggestion().intValue() == 1) {
            contactListSuggSettingBox.setSelected(true);
        }

        commonFriendSuggSettingBox.addActionListener(this);
        phoneNumberSuggSettingBox.addActionListener(this);
        contactListSuggSettingBox.addActionListener(this);
        bodyPanel.add(mainContent);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new MatteBorder(1, 0, 0, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        footerPanel.setOpaque(false);
        footerPanel.setPreferredSize(new Dimension(0, 35));
        add(footerPanel, BorderLayout.SOUTH);

        buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonContainer.setBorder(new EmptyBorder(0, 0, 0, 10));
        buttonContainer.setOpaque(false);

        btnSave = new RoundedCornerButton("Save Changes", "Save");
        btnSave.addActionListener(this);
        buttonContainer.add(btnSave);
        btnSave.setVisible(false);

        label = new JLabel("");
        footerPanel.add(buttonContainer, BorderLayout.CENTER);
        footerPanel.add(label, BorderLayout.NORTH);
    }

    private int checkBoxValue(JCheckBox box) {
        if (box.isSelected()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ((MyFnFSettings.userProfile.getCommonFriendsSuggestion().intValue() == checkBoxValue(commonFriendSuggSettingBox))
                && (MyFnFSettings.userProfile.getContactListSuggestion().intValue() == checkBoxValue(contactListSuggSettingBox))
                && (MyFnFSettings.userProfile.getPhoneNumberSuggestion().intValue() == checkBoxValue(phoneNumberSuggSettingBox))) {
            btnSave.setVisible(false);
        } else {
            btnSave.setVisible(true);
        }
        if (e.getSource() == btnSave) {
            if (MyFnFSettings.userProfile.getCommonFriendsSuggestion().intValue() != checkBoxValue(commonFriendSuggSettingBox)) {
                new ChangeSuggestionSettings(3, checkBoxValue(commonFriendSuggSettingBox)).start();
            }
            if (MyFnFSettings.userProfile.getPhoneNumberSuggestion().intValue() != checkBoxValue(phoneNumberSuggSettingBox)) {
                new ChangeSuggestionSettings(4, checkBoxValue(phoneNumberSuggSettingBox)).start();
            }
            if (MyFnFSettings.userProfile.getContactListSuggestion().intValue() != checkBoxValue(contactListSuggSettingBox)) {
                new ChangeSuggestionSettings(5, checkBoxValue(contactListSuggSettingBox)).start();
            }
            //new ChangeSuggestionSettings(checkBoxValue(commonFriendSuggSettingBox), checkBoxValue(phoneNumberSuggSettingBox), checkBoxValue(contactListSuggSettingBox)).start();
            btnSave.setVisible(false);
            GuiRingID.getInstance().getTopMenuBar().getRingIDSettingsDialog().hideThis();
        }
    }
}
