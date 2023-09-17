/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.MenuPanel;
import com.ipvision.view.utility.SeparatorPanel;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 *
 * @author shahadat
 */
public class RingToolsButtonPanel extends JPanel implements MouseListener {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RingToolsButtonPanel.class);
    public static final String MENU_TYPE_MARKET = "Market";
    public static final String MENU_TYPE_LIVE_POLL = "Live Poll";
    public static final String MENU_TYPE_CLOUD = "Cloud";

    private MenuPanel marketPane = new MenuPanel(MENU_TYPE_MARKET);
    private MenuPanel livePollPane = new MenuPanel(MENU_TYPE_LIVE_POLL);
    private MenuPanel cloudPane = new MenuPanel(MENU_TYPE_CLOUD);
    private JPanel topPanel;

    public RingToolsButtonPanel() {
        setBackground(RingColorCode.LEFT_PANEL_BG_COLOR);
        setPreferredSize(new Dimension(DefaultSettings.RIGHT_RING_TOOLS_EXPAND_WIDTH, DefaultSettings.MENUBER_HEIGHT));
        setLayout(new BorderLayout());
    }

    public void init() {
        try {
            topPanel = new JPanel();
            topPanel.setOpaque(false);
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
            add(topPanel, BorderLayout.CENTER);

            marketPane.addMouseListener(this);
            //livePollPane.addMouseListener(this);
            //cloudPane.addMouseListener(this);

            JPanel sep2 = new SeparatorPanel(1, 50, 8);
            JPanel sep3 = new SeparatorPanel(1, 50, 8);

            topPanel.add(marketPane);
            topPanel.add(sep2);
            topPanel.add(livePollPane);
            topPanel.add(sep3);
            topPanel.add(cloudPane);
        } catch (Exception ex) {
            //ex.printStackTrace();
        log.error("Exception in RingToolsButtonPanel ==>" + ex.getMessage());
        }
    }

    public void resetMainButtonsColor(String selectedMenu) {
        try {
            if (selectedMenu == null) {
                marketPane.setExit();
                livePollPane.setExit();
                cloudPane.setExit();
            } else {
                if (selectedMenu.equalsIgnoreCase(MENU_TYPE_MARKET)) {
                    marketPane.setSelected();
                    livePollPane.setExit();
                    cloudPane.setExit();
                } else if (selectedMenu.equalsIgnoreCase(MENU_TYPE_LIVE_POLL)) {
                    marketPane.setExit();
                    livePollPane.setSelected();
                    cloudPane.setExit();
                } else if (selectedMenu.equalsIgnoreCase(MENU_TYPE_CLOUD)) {
                    marketPane.setExit();
                    livePollPane.setExit();
                    cloudPane.setSelected();
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == marketPane) {
            onClick_RingMarket();
        } else if (e.getSource() == livePollPane) {
            onClick_LivePoll();
        } else if (e.getSource() == cloudPane) {
            onClick_Cloud();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

        if (e.getSource() == marketPane) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (!marketPane.isSelected) {
                marketPane.setEntered();
            }
        } else if (e.getSource() == livePollPane) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (!livePollPane.isSelected) {
                livePollPane.setEntered();
            }
        } else if (e.getSource() == cloudPane) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (!cloudPane.isSelected) {
                cloudPane.setEntered();
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

        if (e.getSource() == marketPane) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            if (!marketPane.isSelected) {
                marketPane.setExit();
            }
        } else if (e.getSource() == livePollPane) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            if (!livePollPane.isSelected) {
                livePollPane.setExit();
            }
        } else if (e.getSource() == cloudPane) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            if (!cloudPane.isSelected) {
                cloudPane.setExit();
            }
        }
    }

    public RingToolsRightContainer getRingToolsRightContainer() {
        return GuiRingID.getInstance().getRingToolsRightContainer() != null ? GuiRingID.getInstance().getRingToolsRightContainer() : null;
    }

    public void onClick_RingMarket() {
        resetMainButtonsColor(MENU_TYPE_MARKET);
        GuiRingID.getInstance().getRingToolsRightContainer().addRingMarket();
    }

    public void onClick_LivePoll() {
        resetMainButtonsColor(MENU_TYPE_LIVE_POLL);
    }

    public void onClick_Cloud() {
        resetMainButtonsColor(MENU_TYPE_CLOUD);
    }
  

}
