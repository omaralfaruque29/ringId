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
import com.ipvision.view.utility.RoundedCornerButton;

/**
 *
 * @author Shahadat
 */
public class PrivacySettingPanel extends JPanel implements ActionListener {

    //  public static PrivacySettingPanel instance;
    private JButton btnSave;
//    private JButton btnReset;
    private JPanel mainContent;
    JPanel buttonContainer;
    JLabel label;
    public JCheckBox profilePictureSettingBox;
    public JCheckBox coverPictureSettingBox;
    private static final String COVER_PUBLIC = "Make Cover Picture public";
    private static final String COVER_PRIVATE = "Make Cover Picture private";
    private static final String PROFILE_PUBLIC = "Make Profile Picture public";
    private static final String PROFILE_PRIVATE = "Make Profile Picture private";
    //   private JCheckBox autoWindowsStartCheckbox;

//    public static PrivacySettingPanel getInstance() {
//        if(instance == null) {
//            instance = new PrivacySettingPanel();
//        }
//        return instance;
//    }
    public PrivacySettingPanel() {
        //  instance = this;
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

        JLabel myNameLabel = DesignClasses.makeLableBold1("Privacy Settings", 15);
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
        profilePictureSettingBox = new JCheckBox();
        profilePictureSettingBox.setSelected(false);
        coverPictureSettingBox = new JCheckBox();
        coverPictureSettingBox.setSelected(false);

        if ((MyFnFSettings.userProfile.getProfileImage() != null && MyFnFSettings.userProfile.getProfileImage().trim().length() > 0)) {
            profilePictureSettingBox.setOpaque(false);
            if (MyFnFSettings.userProfile.getProfileImagePrivacy() == AppConstants.PRIVACY_SHORT_PUBLIC) {
                profilePictureSettingBox.setText(PROFILE_PRIVATE);
            } else if (MyFnFSettings.userProfile.getProfileImagePrivacy() == AppConstants.PRIVACY_SHORT_ONLY_ME) {
                profilePictureSettingBox.setText(PROFILE_PUBLIC);
            }
            mainContent.add(profilePictureSettingBox, con);
            con.gridy++;
        }
        if ((MyFnFSettings.userProfile.getCoverImage() != null && MyFnFSettings.userProfile.getCoverImage().trim().length() > 0)) {
            coverPictureSettingBox.setOpaque(false);
            if (MyFnFSettings.userProfile.getCoverImagePrivacy() == AppConstants.PRIVACY_SHORT_PUBLIC) {
                coverPictureSettingBox.setText(COVER_PRIVATE);
            } else if (MyFnFSettings.userProfile.getCoverImagePrivacy() == AppConstants.PRIVACY_SHORT_ONLY_ME) {
                coverPictureSettingBox.setText(COVER_PUBLIC);
            }
            mainContent.add(coverPictureSettingBox, con);
            con.gridy++;
        }
        profilePictureSettingBox.addActionListener(this);
        coverPictureSettingBox.addActionListener(this);
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

//        btnReset = new RoundedCornerButton("Reset to default", "Reset");
//        btnReset.addActionListener(this);
        buttonContainer.add(btnSave);
//        buttonContainer.add(btnReset);
        btnSave.setVisible(false);

        //   if (autologin && savePass && autostart) {
        //        btnReset.setVisible(true);
        //    } else {
        //        btnReset.setVisible(false);
        //    }
        label= new JLabel("");
        footerPanel.add(buttonContainer, BorderLayout.CENTER);
        footerPanel.add(label,BorderLayout.NORTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!profilePictureSettingBox.isSelected() && !coverPictureSettingBox.isSelected()) {
            btnSave.setVisible(false);
        } else {
            btnSave.setVisible(true);
        }
        if (e.getSource() == btnSave) {
            if (profilePictureSettingBox.isSelected() && MyFnFSettings.userProfile.getProfileImage() != null) {
                if (profilePictureSettingBox.getText().equals(PROFILE_PRIVATE)) {
                    new ChangePrivacy(AppConstants.CONS_PROFILE_IMAGE, AppConstants.PRIVACY_SHORT_ONLY_ME).start();
                } else if (profilePictureSettingBox.getText().equals(PROFILE_PUBLIC)) {
                    new ChangePrivacy(AppConstants.CONS_PROFILE_IMAGE, AppConstants.PRIVACY_SHORT_PUBLIC).start();
                }
            }
            if (coverPictureSettingBox.isSelected() && MyFnFSettings.userProfile.getCoverImage() != null) {
                if (coverPictureSettingBox.getText().equals(COVER_PRIVATE)) {
                    new ChangePrivacy(AppConstants.CONS_COVER_IMAGE, AppConstants.PRIVACY_SHORT_ONLY_ME).start();
                } else if (coverPictureSettingBox.getText().equals(COVER_PUBLIC)) {
                    new ChangePrivacy(AppConstants.CONS_COVER_IMAGE, AppConstants.PRIVACY_SHORT_PUBLIC).start();
                }
            }
            btnSave.setVisible(false);
            GuiRingID.getInstance().getTopMenuBar().getRingIDSettingsDialog().hideThis();
        }
    }
}
