/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.settingsmenu;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.UserLogInInfo;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.model.dao.InsertRingLoginSetting;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.utility.RoundedCornerButton;

/**
 *
 * @author Sirat Samyoun
 */
public class RingSettingPanel extends JPanel implements ActionListener {

    //  public static RingSettingPanel instance;
    private JButton btnSave;
    private JButton btnReset;
    private JPanel mainContent;
    JPanel buttonContainer;
    private JCheckBox autologinCheckbox;
    //private JCheckBox savePassCheckbox;
    private JCheckBox autoWindowsStartCheckbox;
    //boolean savePass;
    boolean autologin;
    boolean autostart;

    public RingSettingPanel() {
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

        JLabel myNameLabel = DesignClasses.makeLableBold1("Ring Setting", 15);
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
        autologinCheckbox = new JCheckBox(" Enable Automatic login");
        autologinCheckbox.setOpaque(false);
        if (MyFnFSettings.VALUE_LOGIN_AUTO_SIGNIN == 1) {
            autologin = true;
            autologinCheckbox.setSelected(true);
        } else {
            autologin = false;
            autologinCheckbox.setSelected(false);
        }
        mainContent.add(autologinCheckbox, con);
        con.gridy++;

//        savePassCheckbox = new JCheckBox(" Enable Password Save");
//        savePassCheckbox.setOpaque(false);
//        if (MyFnFSettings.LOGIN_SAVE_PASSWORD == 1) {
//            savePass = true;
//            savePassCheckbox.setSelected(true);
//        } else {
//            savePass = false;
//            savePassCheckbox.setSelected(false);
//        }
//        mainContent.add(savePassCheckbox, con);
//        con.gridy++;
        //  autologinCheckbox.addItemListener(this);
        // savePassCheckbox.addItemListener(this);
        autoWindowsStartCheckbox = new JCheckBox(" Automatically start with windows start up");
        autoWindowsStartCheckbox.setOpaque(false);
        if (MyFnFSettings.VALUE_LOGIN_AUTO_START == 1) {
            autostart = true;
            autoWindowsStartCheckbox.setSelected(true);
        } else {
            autostart = false;
            autoWindowsStartCheckbox.setSelected(false);
        }
        mainContent.add(autoWindowsStartCheckbox, con);
        con.gridy++;
        autologinCheckbox.addActionListener(this);
        //savePassCheckbox.addActionListener(this);
        autoWindowsStartCheckbox.addActionListener(this);
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

        btnReset = new RoundedCornerButton("Reset to default", "Reset");
        btnReset.addActionListener(this);
        buttonContainer.add(btnSave);
        buttonContainer.add(btnReset);
        btnSave.setVisible(false);
//        if (autologin && savePass && autostart) {
//            btnReset.setVisible(true);
//        } else {
//            btnReset.setVisible(false);
//        }
        if (MyFnFSettings.VALUE_LOGIN_AUTO_START == 0 && MyFnFSettings.VALUE_LOGIN_SAVE_PASSWORD == 0 && MyFnFSettings.VALUE_LOGIN_AUTO_SIGNIN == 0) {
            btnReset.setVisible(false);
        } else {
            btnReset.setVisible(true);
        }
        footerPanel.add(buttonContainer, BorderLayout.CENTER);
    }

    public void appendLoginInfointoDB() {
        List<UserLogInInfo> userLogInInfoList = new ArrayList<UserLogInInfo>();

        UserLogInInfo autologinInfo = new UserLogInInfo();
        autologinInfo.setKey(MyFnFSettings.KEY_LOGIN_AUTO_SIGNIN);
        autologinInfo.setValue(MyFnFSettings.VALUE_LOGIN_AUTO_SIGNIN + "");

        UserLogInInfo savePassInfo = new UserLogInInfo();
        savePassInfo.setKey(MyFnFSettings.KEY_LOGIN_SAVE_PASSWORD);
        savePassInfo.setValue(MyFnFSettings.VALUE_LOGIN_SAVE_PASSWORD + "");

        UserLogInInfo autostartInfo = new UserLogInInfo();
        autostartInfo.setKey(MyFnFSettings.KEY_LOGIN_AUTO_START);
        autostartInfo.setValue(MyFnFSettings.VALUE_LOGIN_AUTO_START + "");
      //  if (MyFnFSettings.VALUE_MOBILE_DIALING_CODE != null) {
            UserLogInInfo mblDcInfo = new UserLogInInfo();
            mblDcInfo.setKey(MyFnFSettings.KEY_MOBILE_DIALING_CODE);
            mblDcInfo.setValue(MyFnFSettings.VALUE_MOBILE_DIALING_CODE);
            userLogInInfoList.add(mblDcInfo);
      //  }
        userLogInInfoList.add(autologinInfo);
        userLogInInfoList.add(savePassInfo);
        userLogInInfoList.add(autostartInfo);
        (new InsertRingLoginSetting(userLogInInfoList)).start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

//        if (e.getSource() == savePassCheckbox) {
//
//            if (!savePassCheckbox.isSelected()) {
//                autologinCheckbox.setSelected(false);
//            }
//        }
//        else if (e.getSource() == autologinCheckbox) {
//            if (autologinCheckbox.isSelected()) {
//                savePassCheckbox.setSelected(true);
//            }
//        } 
        if (e.getSource() == btnSave) {
            if (autologinCheckbox.isSelected()) {
                MyFnFSettings.VALUE_LOGIN_AUTO_SIGNIN = 1;
                MyFnFSettings.VALUE_LOGIN_SAVE_PASSWORD = 1;
                autologin = true;
            } else {
                MyFnFSettings.VALUE_LOGIN_AUTO_SIGNIN = 0;
                autologin = false;
            }

//            if (savePassCheckbox.isSelected()) {
//                MyFnFSettings.LOGIN_SAVE_PASSWORD = 1;
//                savePass = true;
//            } else {
//                MyFnFSettings.LOGIN_SAVE_PASSWORD = 0;
//                savePass = false;
//            }
            if (autoWindowsStartCheckbox.isSelected()) {
                MyFnFSettings.VALUE_LOGIN_AUTO_START = 1;
                autostart = true;
            } else {
                MyFnFSettings.VALUE_LOGIN_AUTO_START = 0;
                autostart = false;
            }
            if (MyFnFSettings.VALUE_LOGIN_AUTO_START == 0 && MyFnFSettings.VALUE_LOGIN_SAVE_PASSWORD == 0 && MyFnFSettings.VALUE_LOGIN_AUTO_SIGNIN == 0) {
                btnReset.setVisible(false);
            } else {
                btnReset.setVisible(true);
            }
            //           btnReset.setVisible(true);
            btnSave.setVisible(false);
            appendLoginInfointoDB();
            GuiRingID.getInstance().getTopMenuBar().getRingIDSettingsDialog().hideThis();

        } else if (e.getSource() == btnReset) {
            MyFnFSettings.VALUE_LOGIN_AUTO_SIGNIN = 0;
            autologin = false;
            autologinCheckbox.setSelected(false);
            MyFnFSettings.VALUE_LOGIN_SAVE_PASSWORD = 0;
//            savePass = false;
//            savePassCheckbox.setSelected(false);
            MyFnFSettings.VALUE_LOGIN_AUTO_START = 0;
            autostart = false;
            autoWindowsStartCheckbox.setSelected(false);
            btnSave.setVisible(false);
            if (MyFnFSettings.VALUE_LOGIN_AUTO_START == 0 && MyFnFSettings.VALUE_LOGIN_SAVE_PASSWORD == 0 && MyFnFSettings.VALUE_LOGIN_AUTO_SIGNIN == 0) {
                btnReset.setVisible(false);
            } else {
                btnReset.setVisible(true);
            }
            appendLoginInfointoDB();
            GuiRingID.getInstance().getTopMenuBar().getRingIDSettingsDialog().hideThis();
        }

        if ((autologinCheckbox.isSelected() == autologin && autoWindowsStartCheckbox.isSelected() == autostart)) {
            btnSave.setVisible(false);
        } else {
            btnSave.setVisible(true);
            btnReset.setVisible(true);
        }
    }

}
