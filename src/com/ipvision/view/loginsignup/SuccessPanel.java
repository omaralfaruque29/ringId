/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.loginsignup;

import com.ipvision.constants.AppConstants;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.SettingsConstants;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.model.constants.StatusConstants;
import com.ipvision.model.dao.RingUserSettingsDAO;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.EnableCommonFriendsSuggesiton;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.view.SplashScreenAfterLogin;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.view.utility.ImagePane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Wasif Islam
 */
public class SuccessPanel extends ImagePane implements KeyListener {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SuccessPanel.class);
    private BufferedImage BG_IMAGE;
    private JPanel containerPanel;
    private JPanel mainPanel;

    private JPasswordField pass_field;
    private ImagePane show_hide_password;
    private BufferedImage pass_hide_image;
    private BufferedImage pass_show_image;
    private JButton doneButton;
    private JsonFields fields;
    private int type;
    private JLabel name_lbl;
    private JLabel email_phn_lbl1;
    private JLabel email_phn_lbl2;
    private JLabel ringid_lbl2;

    public void setValues(JsonFields fields) {
        this.fields = fields;
        this.name_lbl.setText(MyFnFSettings.userProfile.getFullName());
        this.ringid_lbl2.setText(MyFnFSettings.userProfile.getUserIdentity());
        this.pass_field.setText(MyFnFSettings.VALUE_LOGIN_USER_PASSWORD);
        if (fields.getEmail() != null && fields.getEmail().length() > 0) {
            this.email_phn_lbl1.setText("Your E-mail");
            this.email_phn_lbl2.setText(fields.getEmail());
        } else if (fields.getMobilePhoneDialingCode() != null && fields.getMobilePhoneDialingCode().length() > 0 && fields.getMobilePhone() != null && fields.getMobilePhone().length() > 0) {
            this.email_phn_lbl1.setText("Your Phone number");
            this.email_phn_lbl2.setText(fields.getMobilePhoneDialingCode() + fields.getMobilePhone());
        }
        new EnableCommonFriendsSuggesiton().start();
    }

    

    public SuccessPanel() {

        try {
            BG_IMAGE = DesignClasses.return_buffer_image(GetImages.MAIN_BACKGROUND_IMAGE);
            pass_hide_image = DesignClasses.return_buffer_image(GetImages.PASSWORD_HIDE);
            pass_show_image = DesignClasses.return_buffer_image(GetImages.PASSWORD_SHOW);
        } catch (Exception e) {
            log.error("error while loading default image");
        }

        if (BG_IMAGE != null) {
            this.setImage(BG_IMAGE);
            BG_IMAGE.flush();
        }
        setLayout(new BorderLayout());
        setOpaque(false);

        containerPanel = new JPanel(new BorderLayout());
        containerPanel.setPreferredSize(new Dimension(350, 0));
        containerPanel.setOpaque(false);
        add(containerPanel, BorderLayout.EAST);

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.red);
        // mainPanel.setBorder(new EmptyBorder(200, 0, 0, 0));
        mainPanel.setOpaque(false);
        containerPanel.add(mainPanel, BorderLayout.CENTER);
        initContents();
    }

    private void initContents() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 0, 0);

        JLabel congrts_lbl = new JLabel("Congratulation!");
        congrts_lbl.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

        name_lbl = new JLabel();
        name_lbl.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        name_lbl.setForeground(RingColorCode.RING_THEME_COLOR);

        mainPanel.add(getWrapperPanel(getWrapTwoLabelsInPanel(congrts_lbl, name_lbl)), c);

        c.gridy++;
        JLabel ringid_lbl1 = new JLabel("Your ringID number");
        ringid_lbl1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));

        ringid_lbl2 = new JLabel();
        ringid_lbl2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        ringid_lbl2.setForeground(RingColorCode.RING_THEME_COLOR);

        mainPanel.add(getWrapperPanel(getWrapTwoLabelsInPanel(ringid_lbl1, ringid_lbl2)), c);

        c.gridy++;
        email_phn_lbl1 = new JLabel();
        email_phn_lbl1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));

        email_phn_lbl2 = new JLabel();
        email_phn_lbl2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        email_phn_lbl2.setForeground(RingColorCode.RING_THEME_COLOR);

        mainPanel.add(getWrapperPanel(getWrapTwoLabelsInPanel(email_phn_lbl1, email_phn_lbl2)), c);

        c.gridy++;
        JLabel pass_lbl1 = new JLabel("Your Password");
        pass_lbl1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));

        JPanel pass_panel = new RoundedPanel();
        pass_panel.setPreferredSize(new Dimension(200, 35));
        pass_panel.setBorder(new EmptyBorder(0, 5, 0, 5));
        pass_panel.setLayout(new BorderLayout());
        pass_panel.setOpaque(false);
        pass_field = new JPasswordField();
        pass_field.setOpaque(false);
        pass_field.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        pass_field.setForeground(RingColorCode.RING_THEME_COLOR);
        pass_field.setEditable(false);
        pass_field.setBorder(null);
        //pass_field.setEchoChar((char) 0);
        final char pass_char = pass_field.getEchoChar();
        pass_panel.add(pass_field, BorderLayout.WEST);
        show_hide_password = new ImagePane();
        show_hide_password.setOpaque(false);
        show_hide_password.setImage(pass_hide_image);
        show_hide_password.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pass_panel.add(show_hide_password, BorderLayout.EAST);
        show_hide_password.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (show_hide_password.getImage() == pass_hide_image) {
                    pass_field.setEchoChar((char) 0);
                    show_hide_password.setImage(pass_show_image);
                } else if (show_hide_password.getImage() == pass_show_image) {
                    pass_field.setEchoChar(pass_char);
                    show_hide_password.setImage(pass_hide_image);
                }
            }

        });

        mainPanel.add(getWrapperPanel(getWrapTwoLabelsInPanel(pass_lbl1, pass_panel)), c);

        c.gridy++;
        c.insets = new Insets(10, 0, 0, 0);

        JPanel buttonWrapperPanel = new JPanel();
        buttonWrapperPanel.setPreferredSize(new Dimension(80, 30));
        buttonWrapperPanel.setOpaque(false);
        doneButton = new RoundedButton("Done", "Done");
        doneButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                /*if ((fields.getProfileImage() == null || fields.getProfileImage().trim().length() <= 0) && RingUserSettingsDAO.getLastLoggedInTime() <= 0) {
                    LoginUI.getLoginUI().loadMainContent(LoginUI.PROFILE_IMAGE_UI);
                    LoginUI.getLoginUI().profileImagePanel.setJson_fields(fields);
                } else {*/
                    if (LoginUI.getLoginUI() != null) {
                        LoginUI.getLoginUI().disposeLoginUI();
                        LoginUI.loginUI = null;
                    }
                    SplashScreenAfterLogin sp = new SplashScreenAfterLogin(fields);
                    sp.setVisible(true);
              //  }
            }
        });
        doneButton.addKeyListener(this);
        buttonWrapperPanel.add(doneButton);
        mainPanel.add(getWrapperPanel(buttonWrapperPanel), c);

        c.gridy++;
        mainPanel.add(Box.createVerticalStrut(70), c);
    }

    private JPanel getWrapTwoLabelsInPanel(JLabel banner, JPanel info) {
        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new GridBagLayout());
        GridBagConstraints inner_c = new GridBagConstraints();
        inner_c.gridx = 0;
        inner_c.gridy = 0;
        inner_c.anchor = GridBagConstraints.CENTER;

        wrap.add(banner, inner_c);

        inner_c.gridy++;
        wrap.add(info, inner_c);

        return wrap;
    }

    private JPanel getWrapTwoLabelsInPanel(JLabel banner, JLabel info) {
        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new GridBagLayout());
        GridBagConstraints inner_c = new GridBagConstraints();
        inner_c.gridx = 0;
        inner_c.gridy = 0;
        inner_c.anchor = GridBagConstraints.CENTER;

        wrap.add(banner, inner_c);

        inner_c.gridy++;
        wrap.add(info, inner_c);

        return wrap;
    }

    private JPanel getWrapperPanel(JPanel panel) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setPreferredSize(new Dimension(350, 60));
        wrapper.add(panel);
        wrapper.setOpaque(false);
        return wrapper;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = (int) e.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            doneButton.doClick();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
