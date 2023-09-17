/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.singinsignup;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.SettingsConstants;
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
public class ForgetPassowordGetPorts extends Thread {

    private JLabel errorLabel;
    private JTextArea errorTextArea;
    private JButton btnSignin;
    private JLabel lblCreateNew;

    public ForgetPassowordGetPorts(JLabel errorLabel, JTextArea errorTextArea, JButton btnSignin, JLabel lblCreateNew) {
        this.errorLabel = errorLabel;
        this.errorTextArea = errorTextArea;
        this.btnSignin = btnSignin;
        this.lblCreateNew = lblCreateNew;
    }

    @Override
    public void run() {
        if (ServerAndPortSettings.AUTH_SERVER_IP != null && ServerAndPortSettings.AUTH_SERVER_IP.length() > 0 && ServerAndPortSettings.AUTHENTICATION_PORT > 0) {
            buildForgetPasswordUI();
        } else {
            if (btnSignin != null) {
                btnSignin.setEnabled(false);
            }
            if (lblCreateNew != null) {
                lblCreateNew.setEnabled(false);
            }

            if (errorLabel != null) {
                errorLabel.setIcon(DesignClasses.return_image(GetImages.WATING_IMAGE));
                errorLabel.setVisible(true);
            }
            if (errorTextArea != null) {
                errorTextArea.setForeground(DefaultSettings.blue_bar_background);
                errorTextArea.setText(" Please wait...");
            }
            SetSocketPorts obj = null;
            if (MyFnFSettings.VALUE_LOGIN_USER_TYPE > 0) {
                if (MyFnFSettings.VALUE_LOGIN_USER_TYPE == SettingsConstants.MOBILE_LOGIN) {
                    obj = new SetSocketPorts(this, MyFnFSettings.VALUE_LOGIN_USER_NAME, MyFnFSettings.VALUE_MOBILE_DIALING_CODE);
                } else {
                    obj = new SetSocketPorts(this, MyFnFSettings.VALUE_LOGIN_USER_NAME);
                }
                obj.set_socket_port_with_existing_ringIDorEmailorPhone(MyFnFSettings.VALUE_LOGIN_USER_TYPE);
            } else {
                obj = new SetSocketPorts(this);
                obj.set_socket_ports_with_new_ringID();
            }
        }
    }

    public void buildForgetPasswordUI() {
        if (btnSignin != null) {
            btnSignin.setEnabled(true);
        }
        if (lblCreateNew != null) {
            lblCreateNew.setEnabled(true);
        }

        if (errorLabel != null) {
            errorLabel.setIcon(null);
            errorLabel.setVisible(false);
        }
        if (errorTextArea != null) {
            errorTextArea.setText("");
        }
        LoginUI.getLoginUI().loadMainContent(LoginUI.getLoginUI().RECOVER_PASSWORD_UI);
    }
}
