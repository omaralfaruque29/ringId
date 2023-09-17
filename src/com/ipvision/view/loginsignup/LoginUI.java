/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.loginsignup;

import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;
import com.ipvision.view.UnitFrame;
import com.ipvision.view.utility.JOptionPanelBasics;

public class LoginUI extends UnitFrame {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LoginUI.class);
    public static LoginUI loginUI;
    public MainLoginPanel mainLoginPanel;
    public CreateUserPanel createUserPanel;
    public EmailorMobileVerificationPanel emailVarificationPanel;
    public ProfileImagePanel profileImagePanel;
    public RecoverPasswordPanel recoverPasswordPanel;
    public ResetPasswordPanel resetPasswordPanel;
    public VerifyPasswordPanel verifyinRecoverPasswordPanel;
    public SuccessPanel successPanel;

    public final static int MAIN_LOGIN_UI = 0;
    public final static int CREATE_USER_UI = 1;
    public final static int EMAIL_MOBILE_VARIFICATION_UI = 2;
    public final static int PROFILE_IMAGE_UI = 3;
    public final static int RECOVER_PASSWORD_UI = 4;
    public final static int VERIFY_RECOVER_PASSWORD_UI = 5;
    public final static int RESET_PASSWORD_UI = 6;
    public final static int SUCCESS_UI = 7;

    public static LoginUI getLoginUI() {
        if (loginUI == null) {
            loginUI = new LoginUI();
        }
        return loginUI;
    }

    public LoginUI() {
        main_contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                HelperMethods.showConfirmationDialogMessage(NotificationMessages.SIGN_OUT_NOTIFICAITON);
                if (JOptionPanelBasics.YES_NO) {
                    System.exit(0);
                }
            }
        });
    }

    public void disposeLoginUI() {
        this.dispose();
        System.gc();
        Runtime.getRuntime().gc();
    }

    public void loadMainContent(int type) {
        main_contentPane.removeAll();
        if (type == MAIN_LOGIN_UI) {
            mainLoginPanel = new MainLoginPanel();
            main_contentPane.add(mainLoginPanel, BorderLayout.CENTER);
            mainLoginPanel.requestFocus();
            //successPanel = new SuccessPanel();
            // main_contentPane.add(successPanel, BorderLayout.CENTER);
        } else if (type == CREATE_USER_UI) {
            createUserPanel = new CreateUserPanel();
            main_contentPane.add(createUserPanel, BorderLayout.CENTER);
        } else if (type == EMAIL_MOBILE_VARIFICATION_UI) {
            emailVarificationPanel = new EmailorMobileVerificationPanel();
            main_contentPane.add(emailVarificationPanel, BorderLayout.CENTER);
        } else if (type == PROFILE_IMAGE_UI) {
            profileImagePanel = new ProfileImagePanel();
            main_contentPane.add(profileImagePanel, BorderLayout.CENTER);

        } else if (type == RECOVER_PASSWORD_UI) {
            recoverPasswordPanel = new RecoverPasswordPanel();
            main_contentPane.add(recoverPasswordPanel, BorderLayout.CENTER);
            recoverPasswordPanel.requestFocus();
        } else if (type == RESET_PASSWORD_UI) {
            resetPasswordPanel = new ResetPasswordPanel();
            main_contentPane.add(resetPasswordPanel, BorderLayout.CENTER);
        } else if (type == SUCCESS_UI) {
            successPanel = new SuccessPanel();
            main_contentPane.add(successPanel, BorderLayout.CENTER);
        }
        main_contentPane.revalidate();
        main_contentPane.repaint();
    }

    public void loadMainContent(int type, String str) {
        main_contentPane.removeAll();
        if (type == VERIFY_RECOVER_PASSWORD_UI) {
            verifyinRecoverPasswordPanel = new VerifyPasswordPanel(str);
            main_contentPane.add(verifyinRecoverPasswordPanel, BorderLayout.CENTER);
        }
        main_contentPane.revalidate();
        main_contentPane.repaint();
    }
}
