/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.singinsignup;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import com.ipvision.view.loginsignup.LoginUI;
import com.ipvision.service.utility.SetSocketPorts;

/**
 *
 * @author Wasif Islam
 */
public class RingIdRequest extends Thread {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RingIdRequest.class);
    JLabel loadingOrtimePanel;
    JTextArea errorTextArea;
    JButton loginbutton;
    JLabel createLabel;

    public RingIdRequest(JLabel loadingOrtimePanel, JTextArea errorTextArea, JButton loginbutton, JLabel createLabel) {
        this.loadingOrtimePanel = loadingOrtimePanel;
        this.errorTextArea = errorTextArea;
        this.loginbutton = loginbutton;
        this.createLabel = createLabel;
        setName(this.getClass().getName());
    }

    @Override
    public void run() {
        this.loginbutton.setEnabled(false);
        this.createLabel.setEnabled(false);
        this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.WATING_IMAGE));
        this.errorTextArea.setForeground(DefaultSettings.blue_bar_background);
        this.errorTextArea.setText(" Please wait...");
        //set_socket_port();
        if (MyFnFSettings.VALUE_NEW_USER_NAME.length() > 0) {
            if (ServerAndPortSettings.AUTH_SERVER_IP != null && ServerAndPortSettings.AUTH_SERVER_IP.length() > 0 && ServerAndPortSettings.AUTHENTICATION_PORT > 0) {
                LoginUI.getLoginUI().loadMainContent(LoginUI.EMAIL_MOBILE_VARIFICATION_UI);
            } else {
                SetSocketPorts obj = new SetSocketPorts(this, MyFnFSettings.VALUE_NEW_USER_NAME);
                obj.set_socket_port_with_existing_ringIDorEmailorPhone(MyFnFSettings.VALUE_LOGIN_USER_TYPE);
            }
        } else {
            SetSocketPorts obj = new SetSocketPorts(this);
            obj.set_socket_ports_with_new_ringID();
        }
    }

    public void startSignUpProcess() {
        try {
            if (ServerAndPortSettings.AUTH_SERVER_IP != null && ServerAndPortSettings.AUTH_SERVER_IP.length() > 0 && ServerAndPortSettings.AUTHENTICATION_PORT > 0) {
                LoginUI.getLoginUI().loadMainContent(LoginUI.EMAIL_MOBILE_VARIFICATION_UI);
            } else {
                log.error("Communication server not responding");
                this.loginbutton.setEnabled(true);
                this.createLabel.setEnabled(true);
                this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                this.errorTextArea.setForeground(DefaultSettings.errorLabelColor);
                this.errorTextArea.setText(" Failed! Try again");
            }
        } catch (Exception ex) {
            log.error("Exception in startSignUpProcess ==>" + ex.getMessage());
            this.loginbutton.setEnabled(true);
            this.createLabel.setEnabled(true);
            this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
            this.errorTextArea.setForeground(DefaultSettings.errorLabelColor);
            this.errorTextArea.setText(" Failed! Try again");
        }
    }

}
