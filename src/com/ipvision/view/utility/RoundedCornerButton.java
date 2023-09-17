/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.constants.RingColorCode;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 *
 * @author Faiz
 */
public class RoundedCornerButton extends JButton {

    private static final float ARC_WIDTH = 10f;
    private static final float ARC_HEIGHT = 10f;
    protected static final int FOCUS_STROKE = 2;
//    protected final Color fc = new Color(100, 150, 255, 200);
//    protected final Color ac = new Color(230, 230, 230);
    protected final Color rc = RingColorCode.RING_THEME_COLOR;//DefaultSettings.APP_BORDER_COLOR;
    protected Shape shape;
    protected Shape border;
    protected Shape base;
    protected boolean showBorder = true;

    public RoundedCornerButton() {
        super();
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public RoundedCornerButton(Icon icon) {
        super(icon);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public RoundedCornerButton(String text) {
        super(text);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public RoundedCornerButton(String text, String toolTiptext) {
        super(text);
        setToolTipText(toolTiptext);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public RoundedCornerButton(String text, String toolTiptext, boolean showBorder) {
        super(text);
        setToolTipText(toolTiptext);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.showBorder = showBorder;
    }

    public RoundedCornerButton(Action a) {
        super(a);
    }

    public RoundedCornerButton(String text, Icon icon) {
        super(text, icon);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBackground(new Color(250, 250, 250));
        setFont(new Font("Arial", Font.PLAIN, 13));
        initShape();

    }

    public void initShape() {
        if (!getBounds().equals(base)) {
            base = getBounds();
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
            border = new RoundRectangle2D.Float(FOCUS_STROKE, FOCUS_STROKE,
                    getWidth() - 1 - FOCUS_STROKE * 2,
                    getHeight() - 1 - FOCUS_STROKE * 2,
                    ARC_WIDTH, ARC_HEIGHT);
        }
    }

    public void paintFocusAndRollover(Graphics2D g2, Color color) {
        g2.setColor(rc);
        setForeground(new Color(250, 250, 250));
        g2.fill(shape);

    }

    @Override
    public void paintComponent(Graphics g) {
        initShape();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (getModel().isArmed()) {
            g2.setColor(rc);
            g2.fill(shape);
        } else if (isRolloverEnabled() && getModel().isRollover()) {
            paintFocusAndRollover(g2, rc);
        } else if (!getModel().isEnabled()) {
            g2.setColor(new Color(0xC8C8C8));

            g2.fill(shape);
        } else {
            g2.setColor(getBackground());
            setForeground(rc);
            g2.fill(shape);
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setColor(getBackground());
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        initShape();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (showBorder) {
            g2.setColor(rc);
        } else {
            if (getModel().isArmed()) {
                g2.setColor(rc);
            } else if (isRolloverEnabled() && getModel().isRollover()) {
                g2.setColor(rc);
            } else if (!getModel().isEnabled()) {
                g2.setColor(new Color(0xC8C8C8));
            } else {
                g2.setColor(getBackground());
            }
        }

        g2.draw(shape);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        initShape();
        return shape == null ? false : shape.contains(x, y);
    }
}
