/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipvision.view.GuiRingID;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.model.constants.StaticFields;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import com.ipvision.view.utility.ImagePane;

/**
 *
 * @author Faiz Ahmed
 */
public class SplashScreenBG extends JFrame {

    public ImagePane profilePicPanel;

    public SplashScreenBG() {
        this.setTitle(StaticFields.APP_NAME);
        this.setLayout(new BorderLayout());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width / 2 - this.getSize().width / 2) - (DefaultSettings.FRAME_DEFAULT_WIDTH / 2), (dim.height / 2 - this.getSize().height / 2) - (DefaultSettings.FRAME_DEFAULT_HEIGHT / 2));
        this.setSize(new Dimension(DefaultSettings.FRAME_DEFAULT_WIDTH, DefaultSettings.FRAME_DEFAULT_HEIGHT));

        try {
            ImageIcon imageIcon = DesignClasses.return_image(GetImages.APP_LOGO);
            Image image = imageIcon.getImage();
            this.setIconImage(image);
        } catch (Exception e) {
        }
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setUndecorated(true);
        profilePicPanel = new ImagePane();
        profilePicPanel.setLayout(new GridBagLayout());
        profilePicPanel.setBackground(new Color(0, 0, 0, 0));
        try {
            BufferedImage image = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.SPLASH_SCREEN));
            profilePicPanel.setImage(image);
            image.flush();
        } catch (IOException ex) {
        }
        profilePicPanel.setPreferredSize(new Dimension(122, 120));
        profilePicPanel.setBackground(new Color(0, 0, 0, 0));
        add(profilePicPanel);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                GuiRingID.setInstance(null);
                System.exit(0);

            }
        });

    }
}
