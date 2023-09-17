/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.audio;

import com.ipvision.view.utility.DefaultSettings;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 *
 * @author Wasif Islam
 */
public class CustomSliderUI extends BasicSliderUI {

//    public static void main(String[] args) {
//        JFrame frame = new JFrame();
//        JSlider slider = new JSlider(JSlider.VERTICAL, 0, 100, 50);
//        // slider.setPaintTicks(true);
//        // slider.setPaintLabels(true);
//        // slider.setMinorTickSpacing(5);
//        //slider.setMajorTickSpacing(25);
//        slider.setUI(new CustomSliderUI(slider));
//        frame.add(slider);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//    }
    // private BasicStroke stroke = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0f, new float[]{0f, 0f}, 0f);
    private BasicStroke stroke = new BasicStroke(3f);

    public CustomSliderUI(JSlider b) {
        super(b);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g, c);
    }

    @Override
    protected Dimension getThumbSize() {
        return new Dimension(12, 16);
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Stroke old = g2d.getStroke();
        g2d.setStroke(stroke);
        g2d.setPaint(DefaultSettings.APP_SCROLL_PANE_COLOR);
        if (slider.getOrientation() == SwingConstants.HORIZONTAL) {
            g2d.drawLine(trackRect.x, trackRect.y + trackRect.height / 2, trackRect.x + trackRect.width, trackRect.y + trackRect.height / 2);
        } else {
            g2d.drawLine(trackRect.x + trackRect.width / 2, trackRect.y, trackRect.x + trackRect.width / 2, trackRect.y + trackRect.height);
        }
        g2d.setStroke(old);
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int x = thumbRect.x + 2;
        int width = thumbRect.width - 4;
        int y = thumbRect.y + thumbRect.height / 2 - thumbRect.width / 3;
        GeneralPath shape = new GeneralPath(new Ellipse2D.Float(x, y, width, width));
        shape.moveTo(x, y);
        //g2d.setPaint(new Color(81, 83, 186));
        g2d.setColor(DefaultSettings.APP_SCROLL_PANE_COLOR);
        /// g2d.setPaint(DefaultSettings.APP_SCROLL_PANE_COLOR);
        g2d.draw(shape);
        Stroke old = g2d.getStroke();
        g2d.setStroke(new BasicStroke(4f));
        //g2d.setPaint(new Color(131, 127, 211));
        g2d.setPaint(DefaultSettings.APP_SCROLL_PANE_COLOR);
        g2d.draw(shape);
        g2d.setStroke(old);
    }
}
