/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.constants.RingColorCode;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Faiz
 */
public class SeparatorPanel extends JPanel {

    public SeparatorPanel(int sepWidth, int panelHeight, int topBottomGap) {
        setOpaque(false);
        setLayout(new GridLayout());
        setPreferredSize(new Dimension(sepWidth, panelHeight));
        setBorder(new EmptyBorder(topBottomGap, 0, topBottomGap, 0));
        JPanel sp = new JPanel();
        sp.setBackground(RingColorCode.DEFAULT_BORDER_COLOR);
        add(sp);
    }
        public SeparatorPanel(int sepWidth, int panelHeight, int topBottomGap,Color c) {
        setOpaque(false);
        setLayout(new GridLayout());
        setPreferredSize(new Dimension(sepWidth, panelHeight));
        setBorder(new EmptyBorder(topBottomGap, 0, topBottomGap, 0));
        JPanel sp = new JPanel();
        sp.setBackground(c);
        add(sp);
    }
}
