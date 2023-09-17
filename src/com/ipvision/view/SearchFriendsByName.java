/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipvision.constants.GetImages;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author USER
 */
public class SearchFriendsByName extends JPanel {

    private JTextField searchTextField;
    private Border border;

    public SearchFriendsByName(JTextField searchTextField, Border border) {
        this.setLayout(new BorderLayout());
        this.border = border;
        setOpaque(false);
        this.searchTextField = searchTextField;
        JPanel createSearchbox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                g2d.setColor(RingColorCode.RING_THEME_COLOR);
         //       Area border = new Area(new RoundRectangle2D.Double(0, 0, w, h, 10, 10));
         //       g2d.fill(border);

                g2d.setColor(RingColorCode.SEARCH_PANEL_COLOR);
                Area shape = new Area(new RoundRectangle2D.Double(0.5, 0.5, w - 1, h - 1, 9, 9));
                g2d.fill(shape);
            }
        };

        createSearchbox.setLayout(new BoxLayout(createSearchbox, BoxLayout.X_AXIS));
        JPanel searchPanelHolder = new JPanel(new BorderLayout());
        searchPanelHolder.setOpaque(false);
        searchPanelHolder.setBorder(border); //(new EmptyBorder(5, 8, 0, 0));
        searchPanelHolder.add(createSearchbox, BorderLayout.CENTER);
        add(searchPanelHolder, BorderLayout.CENTER);
        createSearchbox.setOpaque(false);
        JLabel search_image = DesignClasses.create_imageJlabel(GetImages.SEARCH_IMG);
        search_image.setBorder(new EmptyBorder(0, DefaultSettings.LEFT_EMPTY_SPACE , 3, 0));
        createSearchbox.add(search_image);
        createSearchbox.add(searchTextField);

    }

}
