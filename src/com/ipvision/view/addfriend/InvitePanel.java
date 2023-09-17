/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.addfriend;

import com.ipvision.constants.RingColorCode;
import com.ipvision.constants.GetImages;
import com.ipvision.view.utility.DefaultSettings;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.ipvision.view.utility.ImagePane;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author Rabiul
 */
public class InvitePanel extends JPanel {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(InvitePanel.class);
    ImagePane fbImagePane = new ImagePane();
    ImagePane twImagePane = new ImagePane();
    ImagePane gplsImagePane = new ImagePane();
    ImagePane emailImagePane = new ImagePane();
    private static final String fbShareLink = "https://www.facebook.com/sharer/sharer.php?u=www.ringid.com&t=TITLE";
    private static final String twShareLink = "https://twitter.com/share?url=www.ringid.com&via=ringidapp&text=Don't%20miss%20the%20free%20calling,%20hottest%20messaging%20features,%20fun%20stickers,%20secret%20chat%20and%20many%20more!%20http://www.ringid.com";
    private static final String gplsSharelink = "https://plus.google.com/share?url=www.ringid.com";

    JLabel fbLabel = new JLabel("Facebook");
    JLabel twLabel = new JLabel("Twitter");
    JLabel gplsLabel = new JLabel("Google Plus");
    JLabel emailLabel = new JLabel("E - Mail");

    BufferedImage bckgrnd = null;
    BufferedImage fbIcon = null;
    BufferedImage fbH = null;
    BufferedImage ttIcon = null;
    BufferedImage ttH = null;
    BufferedImage gplsIcon = null;
    BufferedImage gplsH = null;
    BufferedImage emailIcon = null;
    BufferedImage emailH = null;

    public InvitePanel() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(DefaultSettings.DEFAULT_BORDER_WIDTH, DefaultSettings.DEFAULT_BORDER_WIDTH));
        JPanel wrapperPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                Color color = RingColorCode.FEED_BORDER_COLOR;
                g2d.setColor(color);
                g2d.drawLine(310, 120, 310, 200);//line upper
                g2d.drawLine(310, 340, 310, 420);// line down
                g2d.drawLine(150, 270, 230, 270);//line left
                g2d.drawLine(380, 270, 460, 270);// line right
            }
        };
        wrapperPanel.setLayout(null);
        //wrapperPanel.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH, DefaultSettings.ADD_FRIEND_CONTENT_PANEL_HEIGHT));
        wrapperPanel.setOpaque(false);

        try {
            bckgrnd = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.INVITE_BACKGROUND));

            fbIcon = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.FB_ICON));
            fbH = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.FB_H));

            ttIcon = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.TT_ICON));
            ttH = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.TT_H));

            gplsIcon = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.GPLUS_ICON));
            gplsH = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.GPLUS_H));

            emailIcon = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.EMAIL_ICON));
            emailH = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.EMAIL_H));

        } catch (Exception e) {
        }

        fbImagePane.setLayout(null);
        fbImagePane.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fbImagePane.setBounds(60, 50, 180, 160);/// x distance,y distance, wedth,height
        fbImagePane.setImage(fbIcon);
        fbLabel.setBounds(65, 125, 50, 20);
        fbLabel.setForeground(Color.BLACK);
        fbImagePane.add(fbLabel);
        fbImagePane.addMouseListener(mouseListener);
        wrapperPanel.add(fbImagePane);

        twImagePane.setLayout(null);
        twImagePane.setCursor(new Cursor(Cursor.HAND_CURSOR));
        twImagePane.setBounds(375, 50, 180, 160);/// x distance,y distance, wedth,height
        twImagePane.setImage(ttIcon);
        twLabel.setBounds(70, 125, 50, 20);
        twLabel.setForeground(Color.BLACK);
        twImagePane.add(twLabel);
        twImagePane.addMouseListener(mouseListener);
        wrapperPanel.add(twImagePane);

        gplsImagePane.setLayout(null);
        gplsImagePane.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gplsImagePane.setBounds(60, 330, 180, 160);/// x distance,y distance, wedth,height
        gplsImagePane.setImage(gplsIcon);
        gplsLabel.setBounds(65, 125, 70, 20);
        gplsLabel.setForeground(Color.BLACK);
        gplsImagePane.add(gplsLabel);
        gplsImagePane.addMouseListener(mouseListener);
        wrapperPanel.add(gplsImagePane);

        emailImagePane.setLayout(null);
        emailImagePane.setCursor(new Cursor(Cursor.HAND_CURSOR));
        emailImagePane.setBounds(375, 330, 180, 160);/// x distance,y distance, wedth,height
        emailImagePane.setImage(emailIcon);
        emailLabel.setBounds(70, 125, 50, 20);
        emailLabel.setForeground(Color.BLACK);
        emailImagePane.add(emailLabel);
        emailImagePane.addMouseListener(mouseListener);
        wrapperPanel.add(emailImagePane);

        JLabel bckgrndLabel = new JLabel();
        ImageIcon bckgrndIcon = new ImageIcon(bckgrnd);
        bckgrndLabel.setIcon(bckgrndIcon);
        bckgrndLabel.setBounds(250, 210, 118, 118);
        wrapperPanel.add(bckgrndLabel, BorderLayout.NORTH);
        add(wrapperPanel,BorderLayout.CENTER);

    }

    MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getComponent() == fbImagePane) {
                try {
                    URL fb = new URL(fbShareLink);
                    openWebpage(fb);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(InvitePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (e.getComponent() == twImagePane) {
                try {
                    URL tw = new URL(twShareLink);
                    openWebpage(tw);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(InvitePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (e.getComponent() == gplsImagePane) {
                try {
                    URL gpls = new URL(gplsSharelink);
                    openWebpage(gpls);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(InvitePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (e.getComponent() == emailImagePane) {
                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (desktop != null && desktop.isSupported(Desktop.Action.MAIL)) {
                    try {
                        desktop.mail(new URI("mailto:id@receivermailid.com?subject=RingID&body=Don't%20miss%20the%20free%20calling%20hottest%20messaging%20features%20of%20ringID%20with%20fun%20stickers%2C%20secret%20chat%2C%20multimedia%20sharing%2C%20newsfeed%20%3B%20more.%20Get%20your%20ringID%20Now!%0Ahttp%3A%2F%2Fwww.ringid.com"));
                    } catch (Exception ex) {
                        // ex.printStackTrace();
                        log.error("Error in here ==>" + ex.getMessage());
                    }
                }
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
            if (e.getComponent() == fbImagePane) {
                fbImagePane.setImage(fbH);
                fbLabel.setForeground(Color.WHITE);
            } else if (e.getComponent() == twImagePane) {
                twImagePane.setImage(ttH);
                twLabel.setForeground(Color.WHITE);
            } else if (e.getComponent() == gplsImagePane) {
                gplsImagePane.setImage(gplsH);
                gplsLabel.setForeground(Color.WHITE);
            } else if (e.getComponent() == emailImagePane) {
                emailImagePane.setImage(emailH);
                emailLabel.setForeground(Color.WHITE);
            }

        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getComponent() == fbImagePane) {
                fbImagePane.setImage(fbIcon);
                fbLabel.setForeground(Color.BLACK);
            } else if (e.getComponent() == twImagePane) {
                twImagePane.setImage(ttIcon);
                twLabel.setForeground(Color.BLACK);
            } else if (e.getComponent() == gplsImagePane) {
                gplsImagePane.setImage(gplsIcon);
                gplsLabel.setForeground(Color.BLACK);
            } else if (e.getComponent() == emailImagePane) {
                emailImagePane.setImage(emailIcon);
                emailLabel.setForeground(Color.BLACK);
            }
        }

    };

    private static void openWebpage(URL url) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(url.toURI());
            } catch (Exception e) {
                //e.printStackTrace();
                log.error("browser problem ==>" + e.getMessage());
            }
        }
    }

}
