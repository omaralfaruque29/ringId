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
public class MyScrollbarHorzontalUI extends BasicScrollBarUI {

    @Override
    protected JButton createDecreaseButton(int orientation) {
        JButton btnL = new JButton("");
        btnL.setFocusPainted(false);
        btnL.setBorder(null);
        btnL.setOpaque(false);
        btnL.setContentAreaFilled(false);
        return btnL;
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        JButton btnL = new JButton("");
        btnL.setFocusPainted(false);
        btnL.setBorder(null);
        btnL.setOpaque(false);
        btnL.setContentAreaFilled(false);
        return btnL;
    }

    @Override
    protected void paintDecreaseHighlight(Graphics g) {
        Insets insets = scrollbar.getInsets();
        Rectangle thumbR = getThumbBounds();
        g.setColor(DefaultSettings.APP_SCROLL_PANE_COLOR);

        int x, w;
        if (scrollbar.getComponentOrientation().isLeftToRight()) {
            x = decrButton.getX() + decrButton.getWidth();
            w = thumbR.x - x;
        } else {
            x = thumbR.x + thumbR.width;
            w = decrButton.getX() - x;
        }
        int y = insets.top + decrButton.getHeight() / 2 - 2;
        int h = 2;
        g.fillRect(x, y, w, h);

    }

    @Override
    protected void paintIncreaseHighlight(Graphics g) {
        Insets insets = scrollbar.getInsets();
        Rectangle thumbR = getThumbBounds();
        g.setColor(DefaultSettings.APP_BORDER_COLOR);

        if (scrollbar.getOrientation() == JScrollBar.HORIZONTAL) {
         
            int x = thumbR.x;
            int y = insets.top + decrButton.getHeight() / 2 - 2;
            int w = incrButton.getX() - x;
            int h = 2;
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
    private static final int SCROLL_BAR_ALPHA_ROLLOVER = 250;
    private static final int SCROLL_BAR_ALPHA = 200;
    private static final int THUMB_BORDER_SIZE = 0;
    private static final int THUMB_SIZE = 8;
    private static final Color THUMB_COLOR = DefaultSettings.APP_BORDER_COLOR;

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        int alpha = isThumbRollover() ? SCROLL_BAR_ALPHA_ROLLOVER : SCROLL_BAR_ALPHA;
        int orientation = scrollbar.getOrientation();
        int arc = THUMB_SIZE;
        int x = thumbBounds.x + THUMB_BORDER_SIZE;
        int y = thumbBounds.y + THUMB_BORDER_SIZE;
        //System.out.println(" thumbBounds.x==>" + thumbBounds.x);
        //System.out.println(" thumbBounds.y==>" + thumbBounds.y);
        int width = orientation == JScrollBar.VERTICAL
                ? THUMB_SIZE : thumbBounds.width - (THUMB_BORDER_SIZE * 2);
        width = Math.max(width, THUMB_SIZE);

        int height = orientation == JScrollBar.VERTICAL
                ? thumbBounds.height - (THUMB_BORDER_SIZE * 2) : THUMB_SIZE;
        height = Math.max(height, THUMB_SIZE);
        //int height = 100;
        Graphics2D graphics2D = (Graphics2D) g.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(new Color(THUMB_COLOR.getRed(),
                THUMB_COLOR.getGreen(), THUMB_COLOR.getBlue(), alpha));
        graphics2D.fillRoundRect(x, y, width, height, arc, arc);
        graphics2D.dispose();

    }
}
