/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.RingColorCode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author shahadat
 */
public class RingToolsTitlePanel extends JPanel {

    private JPanel mainPanel;
    private JLabel titleLabel;
    BufferedImage bgImg = DesignClasses.return_buffer_image(GetImages.RING_TOOLS_TITLE_BG);

    public RingToolsTitlePanel() {
        setBorder(new EmptyBorder(0, DefaultSettings.LEFT_EMPTY_SPACE - 5, 0, 0));
        setLayout(new BorderLayout(0, 0));
        setPreferredSize(new Dimension(DefaultSettings.RIGHT_RING_TOOLS_EXPAND_WIDTH, DefaultSettings.MY_NAME_PANEL_HEIGHT - DefaultSettings.RIGHT_RING_TOOLS_EXPAND_HEIGHT));

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        add(mainPanel);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Rectangle r = new Rectangle(0, 0, bgImg.getWidth(), bgImg.getHeight());
        TexturePaint textrue = new TexturePaint(bgImg, r);
        g2.setPaint(textrue);
        g2.fill(new Rectangle(0, 0, getWidth(), getHeight()));
    }

    public void init() {
        titleLabel = new JLabel("ringID Tools");
        titleLabel.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 17));
        mainPanel.add(titleLabel);
    }

}
