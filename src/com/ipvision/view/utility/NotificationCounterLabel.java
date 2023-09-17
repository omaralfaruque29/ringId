/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.view.utility.HtmlHelpers;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class NotificationCounterLabel extends JLabel{
    private ImageIcon icon;
    final public static int SIZE_0 = 0;
    final public static int SIZE_13 = 13;
    final public static int SIZE_17 = 17;
    final public static int SIZE_20 = 20;
    final public static int SIZE_24 = 24;
    
    public NotificationCounterLabel(){
        setOpaque(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setFont(HtmlHelpers.getArialUnicodeMsFont(0, 9));
        setForeground(new Color(0xa78480));
        setPreferredSize(new Dimension(SIZE_0, SIZE_0));
    }
    
    @Override
    public void setText(String text){
        if(text == null || text.trim().length() <= 0){
            super.setText(text);
            setPreferredSize(new Dimension(SIZE_0, SIZE_0));
        } else if(text.trim().length() == 1){
            super.setText(text);
            setPreferredSize(new Dimension(SIZE_13, SIZE_13));
        } else if(text.trim().length() == 2){
            super.setText(text);
            setPreferredSize(new Dimension(SIZE_17, SIZE_17));
        } else if(text.trim().length() == 3){
            super.setText(text);
            setPreferredSize(new Dimension(SIZE_20, SIZE_20));
        } else if(text.trim().length() >= 4){
            super.setText("999");
            setPreferredSize(new Dimension(SIZE_20, SIZE_20));
        }
        
    }
    
    public void paintComponent(Graphics g) {
        if(getWidth() >= SIZE_20){
            icon = DesignClasses.return_image(GetImages.COUNTER_20);
            g.drawImage(icon.getImage(), 0, 0, null);
        } if(getWidth() >= SIZE_17){
            icon = DesignClasses.return_image(GetImages.COUNTER_17);
            g.drawImage(icon.getImage(), 0, 0, null);
        } else if(getWidth() >= SIZE_13){
            icon = DesignClasses.return_image(GetImages.COUNTER_13);
            g.drawImage(icon.getImage(), 0, 0, null);
        } else {
            g.drawImage(null, 0, 0, null);
            g.drawImage(null, 0, 0, null);
        }
        super.paintComponent(g);
    }
}