/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.circle;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Faiz Ahmed
 */
public class CircleHome extends JPanel {

    public CircleHome() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        initContainers();
    }

    private void initContainers() {
        try {
            String text = "Welcome";
            JLabel welcomeLabel = new JLabel(text);
            welcomeLabel.setOpaque(false);
            welcomeLabel.setFont(DefaultSettings.APP_DEFAULT_FONT.deriveFont(Font.PLAIN, 40f));
            welcomeLabel.setForeground(DefaultSettings.APP_DEFAULT_FONT_COLOR);
            JLabel ringIdImage = DesignClasses.create_imageJlabel(GetImages.RINGID);

            JPanel headerPanel = DesignClasses.makeTopHeaderPanel();
            JLabel headLabel = DesignClasses.makeLableBold1("Circle", 18);
            headLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
            headerPanel.add(headLabel, BorderLayout.WEST);

            add(headerPanel, BorderLayout.NORTH);

            JPanel mainPanel = new JPanel(new GridBagLayout());
            mainPanel.setOpaque(false);

            JPanel instructionImagePanel = new JPanel();
            instructionImagePanel.setLayout(new BoxLayout(instructionImagePanel, BoxLayout.Y_AXIS));
            instructionImagePanel.setOpaque(false);

            JPanel instructionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            instructionPanel.setOpaque(false);
            instructionPanel.add(welcomeLabel);
            instructionImagePanel.add(instructionPanel);

            JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            imagePanel.setOpaque(false);
            imagePanel.add(ringIdImage);
            instructionImagePanel.add(imagePanel);

            mainPanel.add(instructionImagePanel);
            JScrollPane scrollContent = DesignClasses.getDefaultScrollPane(mainPanel);

            add(scrollContent, BorderLayout.CENTER);
        } catch (Exception e) {
        }

    }
}
