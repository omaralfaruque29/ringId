/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.chat;

import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.ipvision.model.RecentDTO;

/**
 *
 * @author shahadat
 */
public class ChatDateGroupPanel extends JPanel {

    public RecentDTO recentDTO;
    public String dateName;
    public Color shadowColor = new Color(0, 0, 0, 100);

    public ChatDateGroupPanel(RecentDTO recentDTO) {
        this.recentDTO = recentDTO;
        init();
    }

    private void init() {

        dateName = "";
        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        long currentDay = date.getTime();
        date.setDate(date.getDate() - 1);
        long yesterDay = date.getTime();

        if (recentDTO.getTime() >= currentDay) {
            dateName = " Today, " + recentDTO.getHistoryDateName();
        } else if (recentDTO.getTime() < currentDay && recentDTO.getTime() >= yesterDay) {
            dateName = " Yesterday, " + recentDTO.getHistoryDateName();
        } else {
            dateName = recentDTO.getHistoryDateName();
        }

        setLayout(new GridBagLayout());
        setName("CHAT_GROUP_PANEL");
        setBorder(new EmptyBorder(7, 0, 7, 0));
        setOpaque(false);

        JLabel label = new JLabel(dateName);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        label.setForeground(Color.WHITE);

        final JPanel tagNamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tagNamePanel.add(label);
        tagNamePanel.setOpaque(false);

        JPanel tagNameContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int cW = tagNamePanel.getWidth() + 34;
                int arrowWidth = (w - cW) / 2;

                Area mainShadowShape = new Area(new RoundRectangle2D.Double(arrowWidth, 0, cW, h, h + 1, h + 1));
                g2d.setColor(shadowColor);
                g2d.fill(mainShadowShape);

                Polygon polyLeft = new Polygon(
                        new int[]{0, arrowWidth + 1, arrowWidth + 1},
                        new int[]{10, 10, 11},
                        3);
                g2d.fill(polyLeft);
                
                Polygon polyRight = new Polygon(
                        new int[]{w, arrowWidth + cW - 1, arrowWidth + cW - 1},
                        new int[]{10, 10, 11},
                        3);
                g2d.fill(polyRight);
            }
        };
        tagNameContainer.setLayout(new GridBagLayout());
        tagNameContainer.setOpaque(false);
        tagNameContainer.setPreferredSize(new Dimension(500, 20));

        tagNameContainer.add(tagNamePanel);
        add(tagNameContainer);
    }

}
