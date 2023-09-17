/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.loginsignup;

import com.ipvision.constants.RingColorCode;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

/**
 *
 * @author Wasif Islam
 */
public class RoundedPanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        g2d.setColor(RingColorCode.RING_THEME_COLOR);
        Area border = new Area(new RoundRectangle2D.Double(0, 0, w, h, 15, 15));
        g2d.fill(border);

        g2d.setColor(Color.WHITE);
        Area area = new Area(new RoundRectangle2D.Double(0.5, 0.5, w - 1, h - 1, 15, 15));
        g2d.fill(area);

    }
}
