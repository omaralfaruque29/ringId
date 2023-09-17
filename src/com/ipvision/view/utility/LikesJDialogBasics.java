/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import com.ipvision.view.utility.ImagePane;
//import org.ipvision.book.LikePopupSingleFeed;

/**
 *
 * @author Faiz
 */
public class LikesJDialogBasics extends JDialog {

    public int width = 260;
    public int height = 325;
    private int posX = 0;
    private int posY = 0;
    ImagePane componentContent;
    public JLabel titleBarLabel;
    public JPanel content;
    public JLabel seeMoreLabel;
    public  JPanel seeMorePanel;
    //LikePopupSingleFeed likeSingleFeed;

    public LikesJDialogBasics() {
        setSize(width, height);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        componentContent = new ImagePane();
        componentContent.setBorder(new EmptyBorder(17, 12, 17, 10));
        componentContent.setOpaque(false);
        componentContent.setLayout(new BorderLayout());
        try {
            BufferedImage image = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.LIKE_LIST));
            componentContent.setImage(image);
            image.flush();
            image = null;
        } catch (IOException ex) {
        }
        setContentPane(componentContent);
        componentContent.addMouseListener(poxValueListener);
        componentContent.addMouseMotionListener(frameDragListener);

        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                hideThis();
            }
        });
        initContainers();
    }

    private void initContainers() {
        try {

            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setPreferredSize(new Dimension(0, 30));
            headerPanel.setOpaque(false);

            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            titlePanel.setOpaque(false);
            headerPanel.add(titlePanel, BorderLayout.WEST);

            titleBarLabel = DesignClasses.makeLableBold1("Likes", 11);
            titlePanel.add(titleBarLabel);

            JPanel crossPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            crossPanel.setOpaque(false);
            final JLabel closeButton = DesignClasses.create_imageJlabel(GetImages.BUTTON_CLOSE_MINI);
            crossPanel.add(closeButton);
            closeButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    hideThis();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    closeButton.setIcon(DesignClasses.return_image(GetImages.BUTTON_CLOSE_MINI_H));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    closeButton.setIcon(DesignClasses.return_image(GetImages.BUTTON_CLOSE_MINI));
                }
            });
            closeButton.setToolTipText("Close");
            closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            headerPanel.add(crossPanel, BorderLayout.EAST);
            componentContent.add(headerPanel, BorderLayout.NORTH);

            content = new JPanel();
            content.setOpaque(false);
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

            JPanel nullPanle = new JPanel();
            nullPanle.setOpaque(false);
            nullPanle.add(content, BorderLayout.NORTH);

            JPanel scrollContent = new JPanel(new BorderLayout(0, 0));
            scrollContent.setOpaque(false);
            scrollContent.add(nullPanle, BorderLayout.CENTER);

            JScrollPane downContent = DesignClasses.getDefaultScrollPaneThin(scrollContent);// DesignClasses.getDefaultScorllPane(scrollContent);

            JPanel profiles = new JPanel();
            profiles.setOpaque(false);
            profiles.setLayout(new BorderLayout(MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN));
            profiles.setBorder(new EmptyBorder(MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN));
            profiles.add(downContent, BorderLayout.CENTER);

            componentContent.add(profiles, BorderLayout.CENTER);
        
        } catch (Exception e) {
        }

    }

    public void hideThis() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(false);
                dispose();
//                System.gc();
//                Runtime.getRuntime().gc();
            }
        });

    }
    MouseAdapter poxValueListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            posX = e.getX();
            posY = e.getY();
        }
    };

    MouseMotionAdapter frameDragListener = new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent evt) {
            setLocation(evt.getXOnScreen() - posX, evt.getYOnScreen() - posY);
        }
    };
//
//    public static void main(String[] args) {
//        LikesJDialogBasics likes = new LikesJDialogBasics();
//        likes.setVisible(true);
//    }
}
