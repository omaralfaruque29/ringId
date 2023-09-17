/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JLabel;

/**
 *
 * @author Faiz
 */
public class ProfilePicBigPanel extends ImagePane {

    JLabel imageLabel;

    public ProfilePicBigPanel(JLabel imJLabel) {
        this.imageLabel = imJLabel;
        setLayout(null);
        setBackground(Color.GREEN);
        setOpaque(false);
        try {
            setImage(ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.CRICLE_OUTSIDE_PROFILE_PIC_BIG)));
        } catch (IOException ex) {
        }
//        setPreferredSize(new Dimension(150, 150));
        setPreferredSize(new Dimension(110, 150));
//        imageLabel.setBounds(29, 15, DefaultSettings.PROFILE_PIC_DISPLAY_WIDTH, DefaultSettings.PROFILE_PIC_DISPLAY_WIDTH);
        imageLabel.setBounds(13, 26, DefaultSettings.PROFILE_PIC_DISPLAY_WIDTH, DefaultSettings.PROFILE_PIC_DISPLAY_WIDTH);
        add(imageLabel);
    }

    public ProfilePicBigPanel(JLabel imJLabel, boolean call) {
        this.imageLabel = imJLabel;
        setLayout(null);
        setOpaque(false);
        try {
            setImage(ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.CRICLE_OUTSIDE_PROFILE_PIC_BIG)));
        } catch (IOException ex) {
        }
        setPreferredSize(new Dimension(150, 150));
        imageLabel.setBounds(13, 25, DefaultSettings.PROFILE_PIC_DISPLAY_WIDTH, DefaultSettings.PROFILE_PIC_DISPLAY_WIDTH);
        add(imageLabel);
    }
}
