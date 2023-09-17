/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author Wasif Islam
 */
public class ImagePaneForCoverImage extends JPanel {

    private BufferedImage img;
    private int x = 0;
    private int y = 0;

    public ImagePaneForCoverImage() {
    }

    public void setImage(BufferedImage value) {
        if (value != img) {
            BufferedImage old = img;
            img = value;
            firePropertyChange("image", old, img);
            repaint();
        }
    }

    public void setImage(BufferedImage value, int panel_width, int panel_height) {
        if (value != img) {
            BufferedImage old = img;
            x = panel_width > 0 ? - panel_width : panel_width;
            y = panel_height > 0 ? - panel_height : panel_height;
            img = value;
            firePropertyChange("image", old, img);
            repaint();
        }
    }

    protected void applyQualityRenderHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            applyQualityRenderHints(g2d);

            g2d.drawImage(img, x, y, this);
            g2d.dispose();
        }
    }
}
