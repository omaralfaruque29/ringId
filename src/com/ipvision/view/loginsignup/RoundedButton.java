/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.loginsignup;

/**
 *
 * @author Wasif Islam
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.ipvision.constants.RingColorCode;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;

/**
 *
 * @author Faiz
 */
public class RoundedButton extends JButton {

    private static final float ARC_WIDTH = 10f;
    private static final float ARC_HEIGHT = 10f;
    protected static final int FOCUS_STROKE = 1;

    protected final Color default_color = RingColorCode.RING_THEME_COLOR;
    protected final Color hover_color = Color.WHITE;//new Color(250, 250, 250);
    protected final Color disable_color = new Color(0xffbc8a); //new Color(0xC8C8C8)
    protected Shape shape;
    protected Shape border;
    protected Shape base;
    protected boolean showBorder = false;

    public RoundedButton(String text, String toolTiptext) {
        super(text);
        setToolTipText(toolTiptext);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void updateUI() {
        super.updateUI();
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBackground(default_color);
        setFont(new Font("Arial", Font.PLAIN, 13));
        initShape();

    }

    public void initShape() {
        if (!getBounds().equals(base)) {
            base = getBounds();
            //shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
            //border = new RoundRectangle2D.Float(FOCUS_STROKE, FOCUS_STROKE,  getWidth() - 1 - FOCUS_STROKE * 2, getHeight() - 1 - FOCUS_STROKE * 2, ARC_WIDTH, ARC_HEIGHT);
            border = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), ARC_WIDTH, ARC_HEIGHT);
            shape = new RoundRectangle2D.Float(FOCUS_STROKE, FOCUS_STROKE, getWidth() - (FOCUS_STROKE * 2), getHeight() - (FOCUS_STROKE * 2), ARC_WIDTH, ARC_HEIGHT);
        }

    }

    public void paintFocusAndRollover(Graphics2D g2, Color color) {
        g2.setColor(hover_color);
        setForeground(default_color);
        g2.fill(shape);

    }

    @Override
    public void paintComponent(Graphics g) {
        initShape();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        if (getModel().isArmed()) {
            g2.setColor(hover_color);
            g2.fill(shape);
        } else if (isRolloverEnabled() && getModel().isRollover()) {
            paintFocusAndRollover(g2, hover_color);
        } else if (!getModel().isEnabled()) {
            g2.setColor(disable_color);
            setForeground(hover_color);
            g2.fill(shape);
        } else {
            g2.setColor(default_color);
            setForeground(hover_color);
            g2.fill(shape);
        }
      //  g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        //g2.setColor(getBackground());
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        initShape();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        if (showBorder) {
            g2.setColor(default_color);
        } else {
            if (getModel().isArmed()) {
                g2.setColor(default_color);
            } else if (isRolloverEnabled() && getModel().isRollover()) {
                g2.setColor(default_color);
            } else if (!getModel().isEnabled()) {
                g2.setColor(disable_color);
            } else {
                g2.setColor(default_color);
            }
        }

        g2.draw(shape);
       // g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        initShape();
        return shape == null ? false : shape.contains(x, y);
    }
}
