/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.constants.RingColorCode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author shahadat
 */
public class MenuPanel extends JPanel {

    private int width = 0;
    private int height = 50;
    private String text;
    private JLabel label;
    public boolean isSelected = false;

    public MenuPanel(String text) {
        this.text = text;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        init();
    }
    
    public MenuPanel(String text, int width, int height) {
        this.text = text;
        this.height = height;
        setPreferredSize(new Dimension(width, this.height));
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        init();
    }

    private void init() {
        JPanel north = new JPanel(new GridBagLayout());
        north.setBackground(Color.WHITE);
        north.setPreferredSize(new Dimension(0, height - 2));
        add(north, BorderLayout.NORTH);

        label = new JLabel(text);
        label.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        north.add(label);
    }

    public void setSelected() {
        isSelected = true;
        setBackground(RingColorCode.RING_THEME_COLOR);
        label.setForeground(RingColorCode.RING_THEME_COLOR);
    }

    public void setEntered() {
        isSelected = false;
        setBackground(RingColorCode.RING_THEME_COLOR);
        label.setForeground(RingColorCode.RING_THEME_COLOR);
    }

    public void setExit() {
        isSelected = false;
        setBackground(Color.WHITE);
        label.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
    }

}
