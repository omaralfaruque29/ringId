/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.utility;

import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.view.utility.HtmlHelpers;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class NotificationCounterRedLabel extends JLabel {

    private ImageIcon icon;
    final public static int SIZE_0 = 0;
    final public static int SIZE_31 = 31;

    public NotificationCounterRedLabel() {
        setOpaque(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setFont(HtmlHelpers.getArialUnicodeMsFont(1, 10));
        setForeground(Color.WHITE);
        setPreferredSize(new Dimension(SIZE_31, SIZE_31));
        setPreferredSize(new Dimension(SIZE_0, SIZE_0));
    }

    @Override
    public void setText(String text) {
        if (text == null || text.trim().length() <= 0) {
            super.setText(text);
            setPreferredSize(new Dimension(SIZE_0, SIZE_0));
        } else {
            super.setText(text);
            setPreferredSize(new Dimension(SIZE_31, SIZE_31));
        }
    }

    public void paintComponent(Graphics g) {
        if (getWidth() >= SIZE_31) {
            icon = DesignClasses.return_image(GetImages.COUNTER_RED_31);
            g.drawImage(icon.getImage(), 0, 0, null);
        } else {
            g.drawImage(null, 0, 0, null);
            g.drawImage(null, 0, 0, null);
        }
        super.paintComponent(g);
    }
}
