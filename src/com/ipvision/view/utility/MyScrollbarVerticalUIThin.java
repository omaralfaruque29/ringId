/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.view.utility.DefaultSettings;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 *
 * @author Faiz Ahmed
 */
public class MyScrollbarVerticalUIThin extends BasicScrollBarUI {

    int margin = 1;

    @Override
    protected JButton createDecreaseButton(int orientation) {
        JButton btnL = new JButton("");
        btnL.setFocusPainted(false);
        btnL.setBorder(null);
        // btnL.setOpaque(false);
        btnL.setContentAreaFilled(false);
        return btnL;
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        JButton btnL = new JButton("");
        btnL.setFocusPainted(false);
        btnL.setBorder(null);
        // btnL.setOpaque(false);
        btnL.setContentAreaFilled(false);
        return btnL;
    }

    @Override
    protected void paintDecreaseHighlight(Graphics g) {
        Insets insets = scrollbar.getInsets();
        Rectangle thumbR = getThumbBounds();
        g.setColor(Color.WHITE);
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            int x = insets.left + decrButton.getWidth() / 2 - margin;
            int y = decrButton.getY() + decrButton.getHeight();
            int w = 1;
            int h = thumbR.y - y;
            g.fillRect(x, y, w, h);
        } else {
            int x, w;
            if (scrollbar.getComponentOrientation().isLeftToRight()) {
                x = decrButton.getX() + decrButton.getWidth();
                w = thumbR.x - x;
            } else {
                x = thumbR.x + thumbR.width;
                w = decrButton.getX() - x;
            }
            int y = insets.top + decrButton.getHeight() / 2 - margin;
            int h = 1;
            g.fillRect(x, y, w, h);
        }
    }

    @Override
    protected void paintIncreaseHighlight(Graphics g) {
        Insets insets = scrollbar.getInsets();
        Rectangle thumbR = getThumbBounds();
        g.setColor(Color.WHITE);
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            int x = insets.left + decrButton.getWidth() / 2 - margin;
            int y = thumbR.y;
            int w = 1;
            int h = incrButton.getY() - y;
            g.fillRect(x, y, w, h);
        } else {
            int x = thumbR.x;
            int y = insets.top + decrButton.getHeight() / 2 - margin;
            int w = incrButton.getX() - x;
            int h = 1;
            g.fillRect(x, y, w, h);
        }
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(Color.WHITE);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        paintDecreaseHighlight(g);
        paintIncreaseHighlight(g);

    }
    private static final int SCROLL_BAR_ALPHA_ROLLOVER = 200;
    private static final int SCROLL_BAR_ALPHA = 120;
    private static final int THUMB_BORDER_SIZE = 1;
    private static final int THUMB_SIZE = 11;
    private static final Color THUMB_COLOR = DefaultSettings.APP_BORDER_COLOR;

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        int alpha = isThumbRollover() ? SCROLL_BAR_ALPHA_ROLLOVER : SCROLL_BAR_ALPHA;
        int orientation = scrollbar.getOrientation();
        int arc = THUMB_SIZE;
        int x = thumbBounds.x + THUMB_BORDER_SIZE;
        int y = thumbBounds.y + THUMB_BORDER_SIZE;

        int width = orientation == JScrollBar.VERTICAL
                ? THUMB_SIZE : thumbBounds.width - (THUMB_BORDER_SIZE * 1);
        width = Math.max(width, THUMB_SIZE);

        int height = orientation == JScrollBar.VERTICAL
                ? thumbBounds.height - (THUMB_BORDER_SIZE * 1) : (THUMB_SIZE-2);
        height = Math.max(height, (THUMB_SIZE-2));
        //int height = 100;
        Graphics2D graphics2D = (Graphics2D) g.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(new Color(THUMB_COLOR.getRed(),
                THUMB_COLOR.getGreen(), THUMB_COLOR.getBlue(), alpha));
        graphics2D.fillRect(x, y, width, height);
        graphics2D.dispose();
    }
//    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
//        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
//            return;
//        }
//
//        int w = 16;
//        int h = 16;
//
//        Graphics2D g2 = (Graphics2D) g;
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
//
//        g.translate(thumbBounds.x, thumbBounds.y);
//
//        GradientPaint gr = new GradientPaint(2, 0, new Color(158, 161, 162), 18, 16, new Color(96, 99, 98));
//        g2.setPaint(gr);
//        g2.fill(new Ellipse2D.Double(2, 0, w - 1, h - 1));
//
//        g2.setPaint(new Color(0, 0, 0));
//        g2.fill(new Ellipse2D.Double(6, 4, 7, 7));
//
//        g.translate(-thumbBounds.x, -thumbBounds.y);
//    }
}
