/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author Faiz Ahmed
 */
public class ImagePane extends JPanel {
    
    private BufferedImage img;
    private BufferedImage renderImg;
    private boolean rounded;
    private int x = 0;
    private int y = 0;
    
    public ImagePane() {
        setBackground(Color.WHITE);
        
    }
    
    public void setRounded(boolean value) {
        if (value != rounded) {
            rounded = value;
            renderImg = null;
            firePropertyChange("rounded", !rounded, rounded);
            repaint();
        }
    }
    
    public boolean isRounded() {
        return rounded;
    }
    
    public void setImage(BufferedImage value) {
        if (value != img) {
            BufferedImage old = img;
            img = value;
            renderImg = null;
            firePropertyChange("image", old, img);
            repaint();
        }
    }
    
    public void setImage(BufferedImage value, int panel_width, int panel_height) {
        if (value != img) {
            BufferedImage old = img;
            x = panel_width;
            y = panel_height;
            img = value;
            renderImg = null;
            firePropertyChange("image", old, img);
            repaint();
        }
    }
    
    public BufferedImage getImage() {
        return img;
    }
    
    @Override
    public Dimension getPreferredSize() {
        //Dimension size = img == null ? new Dimension(10, 200) : new Dimension(img.getWidth(), img.getHeight());
         Dimension size = img == null ? new Dimension(200, 200) : new Dimension(img.getWidth(), img.getHeight());
        Insets insets = getInsets();
        size.width += (insets.left + insets.right);
        size.height += (insets.top + insets.bottom);
        return size;
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
    
    protected BufferedImage getImageToRender() {
        
        if (renderImg == null) {
            BufferedImage source = getImage();
            if (source != null) {
                if (isRounded()) {
                    BufferedImage mask = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = mask.createGraphics();
                    applyQualityRenderHints(g2d);
                    g2d.setBackground(new Color(255, 255, 255, 0));
                    g2d.clearRect(0, 0, mask.getWidth(), mask.getHeight());
                    g2d.setBackground(new Color(255, 255, 255, 255));
                    g2d.fillRoundRect(0, 0, mask.getWidth(), mask.getHeight(), 40, 40);
                    g2d.dispose();
                    
                    BufferedImage comp = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    g2d = comp.createGraphics();
                    applyQualityRenderHints(g2d);
                    g2d.setBackground(new Color(255, 255, 255, 0));
                    g2d.clearRect(0, 0, source.getWidth(), source.getHeight());
                    g2d.drawImage(source, 0, 0, this);
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
                    g2d.drawImage(mask, x, y, this);
                    g2d.dispose();
                    
                    renderImg = comp;
                } else {
                    renderImg = source;
                }
            }
        }
        
        return renderImg;
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage img = getImageToRender();
        if (img != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            int width = getWidth();
            int height = getHeight();
            int x = ((width - img.getWidth()) / 2);
            int y = ((height - img.getHeight()) / 2);
            g2d.drawImage(img, x, y, this);
            g2d.dispose();
        }
    }
    
}
