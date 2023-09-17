/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.settingsmenu;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.constants.RingColorCode;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.utility.RoundedCornerButton;

/**
 *
 * @author Shahadat
 */
public class FontSettingPanel extends JPanel {

    public static FontSettingPanel instance;
    private JButton btnSave;
    private JButton btnInstall;
    private JButton btnReset;
    private JPanel mainContent;

//    public static FontSettingPanel getInstance() {
//        if (instance == null) {
//            instance = new FontSettingPanel();
//        }
//        return instance;
//    }
    public FontSettingPanel() {
        // instance = this;
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

        JLabel myNameLabel = DesignClasses.makeLableBold1("Font", 15);
        headerPanel.add(myNameLabel, BorderLayout.WEST);

        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBorder(new MatteBorder(0, 1, 0, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        bodyPanel.setOpaque(false);
        add(bodyPanel, BorderLayout.CENTER);

        mainContent = new JPanel();
        mainContent.setBorder(new EmptyBorder(7, 7, 7, 7));
        mainContent.setOpaque(false);
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        bodyPanel.add(mainContent, BorderLayout.NORTH);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new MatteBorder(1, 0, 0, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        footerPanel.setOpaque(false);
        footerPanel.setPreferredSize(new Dimension(0, 35));
        add(footerPanel, BorderLayout.SOUTH);

        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonContainer.setBorder(new EmptyBorder(0, 0, 0, 10));
        buttonContainer.setOpaque(false);
        btnSave = new RoundedCornerButton("Save Changes", "Save");
        btnSave.addActionListener(actionListener);
        btnReset = new RoundedCornerButton("Reset Changes", "Reset");
        btnReset.addActionListener(actionListener);
        footerPanel.add(buttonContainer, BorderLayout.CENTER);

        buildFontSettings();
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnSave) {

            } else if (e.getSource() == btnReset) {

            } else if (e.getSource() == btnInstall) {
                try {
                    if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                        String cmd = "rundll32 url.dll,FileProtocolHandler " + new File("resources/" + MyFnFSettings.DEFAULT_FONT_NAME).getCanonicalPath();
                        Runtime.getRuntime().exec(cmd);
                        GuiRingID.getInstance().getTopMenuBar().getRingIDSettingsDialog().hideThis();
                    } else {
                        //JOptionPane.showConfirmDialog(null, "" + "Can not process!!", StaticFields.APP_NAME, JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                        HelperMethods.showPlaneDialogMessage("" + "Can not process!!");
                    }
                } catch (Exception dfe) {
                }
            }
        }
    };

    private void buildFontSettings() {
        mainContent.removeAll();

        JPanel installFontContainer = new JPanel(new BorderLayout());
        installFontContainer.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        installFontContainer.setOpaque(false);
        mainContent.add(installFontContainer);

        JPanel installFontPanel = new JPanel(new BorderLayout());
        installFontPanel.setOpaque(false);
        installFontPanel.setBorder(new EmptyBorder(5, 0, 10, 0));
        installFontContainer.add(installFontPanel, BorderLayout.CENTER);

        JPanel installFontWest = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 7));
        installFontWest.setOpaque(false);
        installFontWest.setPreferredSize(new Dimension(170, 25));
        installFontPanel.add(installFontWest, BorderLayout.WEST);

        JLabel titleText = DesignClasses.makeJLabelFullName("Install Unicode Font: ", 13);
        installFontWest.add(titleText);

        JPanel installFontEast = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 3));
        installFontEast.setOpaque(false);
        installFontPanel.add(installFontEast, BorderLayout.EAST);

        btnInstall = new JButton(" Install... ");
        btnInstall.addActionListener(actionListener);
        installFontEast.add(btnInstall);

    }
}
