/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.ipvision.view.utility.ImagePane;

/**
 *
 * @author Sirat Samyoun
 */
public class JOptionPanelBasicsShadow extends JDialog {

    public int width = 400;
    public int height = 220;
    public int padding = 12;
    public ImagePane componentContent;
    public JPanel content;

    public JOptionPanelBasicsShadow() {
        setMinimumSize(new Dimension(width, height));
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setLocationRelativeTo(null);
        setModal(true);
        componentContent = new ImagePane();
        componentContent.setBorder(new EmptyBorder(padding + 5, padding, padding, padding));
        componentContent.setOpaque(false);
        try {
            BufferedImage image = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.DIALOG_SHADOW));
            componentContent.setImage(image);
            image.flush();
            image = null;
        } catch (IOException ex) {
        }
        setContentPane(componentContent);
        content = new RoundPanel(false);
        content.setBackground(Color.WHITE);
        content.setPreferredSize(new Dimension(width - 55, height - 55));
        content.setLayout(new GridBagLayout());
        componentContent.add(content, BorderLayout.CENTER);
        setAlwaysOnTop(true);
    }

}
