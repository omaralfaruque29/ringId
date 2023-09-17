/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.view.utility.DefaultSettings;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

/**
 *
 * @author Faiz Ahmed
 */
public class MenuHelpers {

    private final int ITEM_PLAIN = 0;	// Item types
    private final int ITEM_CHECK = 1;
    private final int ITEM_RADIO = 2;
    ActionListener actionListener;

    public MenuHelpers(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public static void mouseLiseraction(final JComponent dd, final String timeString) {
        final JPopupMenu freindPopup = new JPopupMenu();
        freindPopup.setBorder(DefaultSettings.DEFAULT_BORDER);

        JMenuItem menuItem = new JMenuItem(timeString);
        menuItem.setFont(new Font("", Font.PLAIN, 12));
        menuItem.setBorder(null);
        menuItem.setForeground(Color.WHITE);
        menuItem.setBackground(Color.BLACK);
        freindPopup.add(menuItem);
        dd.addMouseListener(new MouseListener() {
            Color bg_color;

            @Override
            public void mouseEntered(MouseEvent e) {
                freindPopup.show(dd, dd.getX(), dd.getY());
                //  bg_color = e.getComponent().getBackground();
                //  e.getComponent().setBackground(DefaultSettings.bar_hover_color);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                freindPopup.show(dd, dd.getX(), dd.getY());
                //  dd.setPreferredSize(new Dimension(230, 70));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // show_window_with_offline(userid);
                //    show_single_user_profile(userid);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
                freindPopup.setVisible(false);
            }
        });
    }

    public JMenuItem createMenuItem(int iType, String sText,
            String image_source, int acceleratorKey,
            String sToolTip) {
        // Create the item
        JMenuItem menuItem;


        switch (iType) {
            case ITEM_RADIO:
                menuItem = new JRadioButtonMenuItem();
                break;

            case ITEM_CHECK:
                menuItem = new JCheckBoxMenuItem();
                break;

            default:
                menuItem = new JMenuItem();
                break;
        }
        menuItem.setText(sText);
        if (image_source != null) {
            try {
                URL imgURL = new Object() {
                }.getClass().getClassLoader().getResource(image_source);
                menuItem.setIcon(new ImageIcon(imgURL));
            } catch (Exception e) {
            }

        }
        if (acceleratorKey > 0) {
            menuItem.setMnemonic(acceleratorKey);
        }


        if (sToolTip != null) {
            menuItem.setToolTipText(sToolTip);
        }
        menuItem.setFont(new Font("", Font.PLAIN, 12));
        menuItem.addActionListener(actionListener);
        menuItem.setBackground(Color.WHITE);
        return menuItem;
    }
}
