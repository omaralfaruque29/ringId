/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipvision.view.GuiRingID;
import com.ipvision.view.LoadUserBasicInformation;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.JsonFields;
import java.awt.Color;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import com.ipvision.service.singinsignup.SignOutActions;

/**
 *
 * @author Faiz Ahmed
 */
public class SplashScreenAfterLogin extends SplashScreenBG {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SplashScreenAfterLogin.class);
    private JLabel loading_JLabel;
    private JsonFields fields;

    public SplashScreenAfterLogin(JsonFields jsonFields) {
        this.fields = jsonFields;
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                GuiRingID.setInstance(null);
                SignOutActions.send_logout_request();
                System.exit(0);

            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                addWelcomeMessage();
                new WaitSomeTime(fields).start();
            }
        });
    }

    private void addWelcomeMessage() {
        loading_JLabel = DesignClasses.makeJLabel("", 30, 1, Color.WHITE, SwingConstants.LEFT);
        JLabel welcomeLabel = DesignClasses.makeJLabel("Welcome", 30, 0, Color.WHITE, SwingConstants.CENTER);
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 0;
        profilePicPanel.add(welcomeLabel, cons);
        cons.gridy++;
        profilePicPanel.add(loading_JLabel, cons);
        profilePicPanel.revalidate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (MyFnFSettings.userProfile.getFullName() != null) {
                    loading_JLabel.setText(MyFnFSettings.userProfile.getFullName());
                    loading_JLabel.revalidate();
                }

            }
        }).start();
    }

    private class WaitSomeTime extends Thread {

        private JsonFields fields;

        public WaitSomeTime(JsonFields fields) {
            this.fields = fields;
        }

        @Override
        public void run() {
            try {
                HelperMethods.bindMyProfileObject(fields, true);
                HelperMethods.startVoiceAndChatSockets();

                LoadUserBasicInformation loadUserBasicInformation = new LoadUserBasicInformation(fields);
                loadUserBasicInformation.loadInformation();
                //AcceptedFriendList.getInstance().buildFriendList();
                //GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().getAcceptedFriendList().buildFriendList();

                dispose();
                System.gc();
                Runtime.getRuntime().gc();

                GuiRingID guiRingID = new GuiRingID();
                guiRingID.setVisible(true);
            } catch (Exception e) {
                log.error("System error  ==>" + e.getMessage());
                System.exit(0);
            }
        }
    }

}
