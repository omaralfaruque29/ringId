/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility.chat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicProgressBarUI;

/**
 *
 * @author shahadat
 */
public class ProgressRectangleUI extends BasicProgressBarUI {

    @Override
    public Dimension getPreferredSize(JComponent c) {
        Dimension d = super.getPreferredSize(c);
        int v = Math.max(d.width, d.height);
        d.setSize(v, v);
        return d;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Insets b = progressBar.getInsets(); // area for border
        int barRectWidth = progressBar.getWidth() - b.right - b.left - 1;
        int barRectHeight = progressBar.getHeight() - b.top - b.bottom - 1;
        int padding = 3;
        int backgroundMaxWidth = barRectWidth - (2 * padding) - 2;
        int backgroundMaxHeight = barRectHeight - (2 * padding) - 1;
        
        if (backgroundMaxWidth <= 0 || backgroundMaxHeight <= 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double progressWidth = backgroundMaxWidth * progressBar.getPercentComplete();
        
        Shape border = new Rectangle2D.Double(b.left, b.top, barRectWidth, barRectHeight);
        g2.setPaint(new Color(0xbdbdbd));
        g2.draw(border);
        
        Shape background = new Rectangle2D.Double(b.left + padding + 1, b.top + padding + 1, progressWidth, backgroundMaxHeight);
        g2.setPaint(progressBar.getForeground());
        g2.fill(background);
        g2.dispose();

        // Deal with possible text painting
        if (progressBar.isStringPainted()) {
            paintString(g, b.left, b.top, barRectWidth, barRectHeight, 0, b);
        }
    }
}