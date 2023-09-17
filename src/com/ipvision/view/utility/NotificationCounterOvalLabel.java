/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

/**
 *
 * @author Wasif Islam
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.ipvision.constants.GetImages;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class NotificationCounterOvalLabel extends JLabel {
private ImageIcon icon;
    final public static int SIZE_0 = 0;
    final public static int SIZE_12 = 12;
    final public static int SIZE_20 = 20;

    public NotificationCounterOvalLabel() {
        setOpaque(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setFont(HtmlHelpers.getArialUnicodeMsFont(1, 9));
        setForeground(Color.WHITE);
        setPreferredSize(new Dimension(SIZE_0, SIZE_0));
    }

    @Override
    public void setText(String text) {
        if (text == null || text.trim().length() <= 0) {
            super.setText(text);
            setPreferredSize(new Dimension(SIZE_0, SIZE_0));
        } else if (text.trim().length() >0) {
            super.setText(text);
            setPreferredSize(new Dimension(SIZE_20, SIZE_12));
        }

    }

    public void paintComponent(Graphics g) {
        if (getWidth() > 0) {
            icon = DesignClasses.return_image(GetImages.COUNTER_BOOK_NOTIFICATION);
            g.drawImage(icon.getImage(), 0, 0, null);
        }else {
            g.drawImage(null, 0, 0, null);
            g.drawImage(null, 0, 0, null);
        }
        super.paintComponent(g);
    }
    /*final public static int SIZE_0 = 0;
     final public static int SIZE_12 = 12;
     final public static int SIZE_15 = 14;
     final public static int SIZE_18 = 18;
     final public static int SIZE_21 = 22;

     public NotificationCounterOvalLabel() {
     setOpaque(false);
     setPreferredSize(new Dimension(SIZE_0, SIZE_0));
     }

     @Override
     public void setText(String text) {
     super.setText(text);
     if (text == null || text.trim().length() <= 0) {
     setPreferredSize(new Dimension(SIZE_0, SIZE_0));
     } else if (text.trim().length() == 1) {
     setPreferredSize(new Dimension(SIZE_15, SIZE_12));
     } else if (text.trim().length() == 2) {
     setPreferredSize(new Dimension(SIZE_18, SIZE_12));
     } else {
     setPreferredSize(new Dimension(SIZE_21, SIZE_12));
     }
     }

     public void paintComponent(Graphics g) {

     super.paintComponent(g);
     Graphics2D g2d = (Graphics2D) g;
     g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

     int w = getWidth();
     int h = getHeight();

     g2d.setColor(RingColorCode.RING_THEME_COLOR);
     Area main = new Area(new Rectangle2D.Double(0, 0, w, h));
     g2d.fill(main);
        
     g2d.setColor(new Color(0xF9A267));
     Area shape = new Area(new RoundRectangle2D.Double(0, 0, w, h, h, h));
     g2d.fill(shape);

     g2d.setFont(new Font("Arial", Font.PLAIN, 9));
     g2d.setColor(Color.WHITE);
     g2d.drawString(super.getText(), (float) (w - (super.getText().length() * 5)) / 2F, 9F);

     }*/
}
