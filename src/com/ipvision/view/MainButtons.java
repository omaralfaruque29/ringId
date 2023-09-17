/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipvision.constants.GetImages;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.NewsFeeds;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.view.circle.CircleList;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.view.utility.ImagePane;
import com.ipvision.view.utility.NotificationCounterOvalLabel;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 *
 * @author Faiz
 */
public class MainButtons extends JPanel implements MouseListener {

    int width;
    int height;
    public final ImagePane bookHomePane = new ImagePane();
    public final ImagePane grpPane = new ImagePane();
    public final ImagePane circlePane = new ImagePane();
    public final ImagePane expandPane = new ImagePane();

    private final NotificationCounterOvalLabel bookHomeNotification = new NotificationCounterOvalLabel();
    private final NotificationCounterOvalLabel circleNotification = new NotificationCounterOvalLabel();

    public BufferedImage bookHomeImg = null, bookHomeHoverImg = null, bookHomeSelectedImg = null;
    public BufferedImage expandImg = null, expandHoverImg = null;//, expandSelectedImg = null;
    public BufferedImage collapseImg = null, collapseHoverImg = null;//, collapseSelectedImg = null;
    public BufferedImage grpImg = null, grpHoverImg = null, grpSelectedImg = null;
    public BufferedImage circleImg = null, circleHoverImg = null, circleSelectedImg = null;

    private BufferedImage bgImg = DesignClasses.return_buffer_image(GetImages.MENU_SIDE_BAR_BG);
    private boolean isExpendView = false;
    
    private JPanel expandWrapperPanel;
    private JPanel otherWrapperPanel;
    private JPanel container;

    public NotificationCounterOvalLabel getCircleNotification() {
        return circleNotification;
    }

    public NotificationCounterOvalLabel getBookHomeNotification() {
        return bookHomeNotification;
    }

    public MainButtons() {
        initImages();
        setOpaque(false);
        //setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        
        expandWrapperPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        expandWrapperPanel.setOpaque(false);
        
        container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        add(container, BorderLayout.CENTER);
        
        otherWrapperPanel = new JPanel();
        otherWrapperPanel.setOpaque(false);
        container.add(otherWrapperPanel);
        
        setMainButtonsView(false);
    }

    private void initImages() {
        try {
            bookHomeImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.BOOK_HOME_NOTIFICATION));
            bookHomeHoverImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.BOOK_HOME_NOTIFICATION_H));
            bookHomeSelectedImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.BOOK_HOME_NOTIFICATION_SELECTED));
            grpImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.GROUP_TOP));
            grpHoverImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.GROUP_TOP_H));
            grpSelectedImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.GROUP_TOP_SELECTED));
            circleImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.CIRCLE_TOP));
            circleHoverImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.CIRCLE_TOP_H));
            circleSelectedImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.CIRCLE_TOP_SELECTED));
            expandImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.EXPAND));
            expandHoverImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.EXPAND_H));
            collapseImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.COLLAPSE));
            collapseHoverImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.COLLAPSE_H));
        } catch (Exception e) {
        }

        bookHomePane.setOpaque(false);
        bookHomePane.setToolTipText("Feed");
        bookHomePane.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bookHomePane.setImage(bookHomeSelectedImg);
        bookHomePane.addMouseListener(this);
        bookHomePane.setRounded(false);
        bookHomePane.add(bookHomeNotification);

        circlePane.setOpaque(false);
        circlePane.setToolTipText("Circles");
        circlePane.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        circlePane.setImage(circleImg);
        circlePane.addMouseListener(this);
        circlePane.setRounded(false);
        circlePane.add(circleNotification);

        grpPane.setOpaque(false);
        grpPane.setToolTipText("Groups");
        grpPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        grpPane.setImage(grpImg);
        grpPane.addMouseListener(this);
        grpPane.setRounded(false);

        expandPane.setOpaque(false);
        expandPane.setToolTipText("Show Ring Tools & App");
        expandPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        expandPane.setImage(expandImg);
        expandPane.addMouseListener(this);
        expandPane.setRounded(false);

        // bookHomeNotification.setText("9");
        //bookHomeNotification.setText("99");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == bookHomePane || e.getSource() == bookHomeNotification) {
            bookHomeNotification.setText("");
            bookHomeNotification.revalidate();
            bookHomeNotification.repaint();
            if (e.getSource() == bookHomeNotification) {
                HelperMethods.clearAllBookHomeUIMaps();
            }
            action_of_book_button();
        } else if (e.getSource() == circlePane || e.getSource() == circleNotification) {
            circleNotification.setText("");
            circleNotification.revalidate();
            circleNotification.repaint();
            action_of_circle_button();
        } else if (e.getSource() == grpPane) {
            action_of_group_button();
        } else if (e.getSource() == expandPane) {
            expandPane.setOpaque(false);
            if (expandPane.getImage().equals(expandImg) || expandPane.getImage().equals(expandHoverImg)) {
                GuiRingID.getInstance().setRightContentVisible(true);
            } else {
                GuiRingID.getInstance().setRightContentVisible(false);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == bookHomePane) {
            bookHomePane.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (!bookHomePane.getImage().equals(bookHomeSelectedImg)) {
                bookHomePane.setImage(bookHomeHoverImg);
                bookHomePane.setOpaque(true);
                bookHomePane.setBackground(RingColorCode.RING_TOP_MENU_SELECTED_BG_COLOR);
            }
        } else if (e.getSource() == grpPane) {
            grpPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (!grpPane.getImage().equals(grpSelectedImg)) {
                grpPane.setImage(grpHoverImg);
                grpPane.setOpaque(true);
                grpPane.setBackground(RingColorCode.RING_TOP_MENU_SELECTED_BG_COLOR);
            }
        } else if (e.getSource() == circlePane) {
            circlePane.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (!circlePane.getImage().equals(circleSelectedImg)) {
                circlePane.setImage(circleHoverImg);
                circlePane.setOpaque(true);
                circlePane.setBackground(RingColorCode.RING_TOP_MENU_SELECTED_BG_COLOR);
            }
        } else if (e.getSource() == expandPane) {
            expandPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (expandPane.getImage().equals(expandImg)) {
                expandPane.setImage(expandHoverImg);
                expandPane.setOpaque(true);
                expandPane.setBackground(RingColorCode.RING_TOP_MENU_SELECTED_BG_COLOR);
            } else if (expandPane.getImage().equals(collapseImg)) {
                expandPane.setImage(collapseHoverImg);
                expandPane.setOpaque(true);
                expandPane.setBackground(RingColorCode.RING_TOP_MENU_SELECTED_BG_COLOR);
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == bookHomePane) {
            bookHomePane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            if (!bookHomePane.getImage().equals(bookHomeSelectedImg)) {
                bookHomePane.setImage(bookHomeImg);
                bookHomePane.setOpaque(false);
            }
        } else if (e.getSource() == grpPane) {
            grpPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (!grpPane.getImage().equals(grpSelectedImg)) {
                grpPane.setImage(grpImg);
                grpPane.setOpaque(false);
            }
        } else if (e.getSource() == circlePane) {
            circlePane.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (!circlePane.getImage().equals(circleSelectedImg)) {
                circlePane.setImage(circleImg);
                circlePane.setOpaque(false);
            }
        } else if (e.getSource() == expandPane) {
            expandPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            if (expandPane.getImage().equals(expandHoverImg)) {
                expandPane.setImage(expandImg);
                expandPane.setOpaque(false);
            } else if (expandPane.getImage().equals(collapseHoverImg)) {
                expandPane.setImage(collapseImg);
                expandPane.setOpaque(false);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

//    public void resetMenuColor(String selectedMenu) {
//        try {
//            if (selectedMenu == null) {
//                bookHomePane.setImage(bookHomeImg);
//                grpPane.setImage(grpImg);
//                circlePane.setImage(circleImg);
//            } else {
//                if (selectedMenu.equalsIgnoreCase(MENU_TYPE_BOOK_HOME)) {
//                    bookHomePane.setImage(bookHomeSelectedImg);
//                    grpPane.setImage(grpImg);
//                    circlePane.setImage(circleImg);
//                } else if (selectedMenu.equalsIgnoreCase(MENU_TYPE_CIRCLE)) {
//                    bookHomePane.setImage(bookHomeImg);
//                    grpPane.setImage(grpImg);
//                    circlePane.setImage(circleSelectedImg);
//                } else if (selectedMenu.equalsIgnoreCase(MENU_TYPE_GROUP)) {
//                    bookHomePane.setImage(bookHomeImg);
//                    grpPane.setImage(grpSelectedImg);
//                    circlePane.setImage(circleImg);
//                }
//            }
//            bookHomePane.setOpaque(false);
//            grpPane.setOpaque(false);
//            circlePane.setOpaque(false);
//            expandPane.setOpaque(false);
//        } catch (Exception e) {
//        }
//    }
    public void setMainHomeButton(boolean set) {
        if (set) {
            bookHomePane.setImage(bookHomeSelectedImg);
        } else {
            bookHomePane.setImage(bookHomeImg);
        }
        bookHomePane.setOpaque(false);
    }

    public void setMainGroupButton(boolean set) {
        if (set) {
            grpPane.setImage(grpSelectedImg);
        } else {
            grpPane.setImage(grpImg);
        }
        grpPane.setOpaque(false);
    }

    public void setMainCircleButton(boolean set) {
        if (set) {
            circlePane.setImage(circleSelectedImg);
        } else {
            circlePane.setImage(circleImg);
        }
        circlePane.setOpaque(false);
    }

    private MainRightDetailsView getMainRight() {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
            return GuiRingID.getInstance().getMainRight();
        } else {
            return null;
        }
    }

    private void loadIntoMainRightContent(JPanel panel) {
        if (GuiRingID.getInstance() != null) {
            GuiRingID.getInstance().loadIntoMainRightContent(panel);
        }
    }

    public synchronized void action_of_book_button() {
        GuiRingID.getInstance().getMainButtonsPanel().doFriendlistButtonClick();
        if (getMainRight() != null) {
            //resetMenuColor(MENU_TYPE_BOOK_HOME);
            //AllFriendList.setFriendSelectionColor(null, true);
            CircleList.setCircleSelectionColor(null);
            if (getMainRight().getComponentCount() > 0 && getMainRight().getComponent(0) != getMainRight().getAllFeedsView()) {
                loadIntoMainRightContent(getMainRight().getAllFeedsView());
            }

            if (NewsFeedMaps.getInstance().getAllNewfeedId().hasDataFromServer() == false) {
                new NewsFeeds(0, 20).start();
            } else if (NewsFeedMaps.getInstance().getUnreadBookHomeNewsFeeds().size() > 0 || getMainRight().getAllFeedsView().isDisplayable()) {
                getMainRight().getAllFeedsView().loadTopForcefully();
            }
            DesignClasses.setArrowKeyScroll(getMainRight().getAllFeedsView().scrollContent);
        }
    }

    public synchronized void action_of_group_button() {
        if (getMainRight() != null) {
            //  resetMenuColor(MENU_TYPE_GROUP);
            getMainRight().showGroupViewPanel();
        }
    }

    public synchronized void action_of_circle_button() {
        if (getMainRight() != null) {
            //  resetMenuColor(MENU_TYPE_CIRCLE);
            circleNotification.setText("");
            circleNotification.revalidate();
            circleNotification.repaint();
            getMainRight().showCircleViewPanel();
        }
    }

    public void setMainButtonsView(boolean isExpendView) {
        this.isExpendView = isExpendView;
        removeAll();
        otherWrapperPanel.removeAll();
        expandWrapperPanel.removeAll();

        if (this.isExpendView) {
            setLayout(new BorderLayout(18, 0));
            setPreferredSize(new Dimension(0, DefaultSettings.RIGHT_RING_TOOLS_EXPAND_HEIGHT));
            
            add(container, BorderLayout.CENTER);
            add(expandWrapperPanel, BorderLayout.EAST);
            
            expandPane.setImage(collapseImg);
            expandPane.setToolTipText("Hide Ring Tools & App");
            expandWrapperPanel.add(expandPane);
            
            otherWrapperPanel.setLayout(new BoxLayout(otherWrapperPanel, BoxLayout.X_AXIS));
            otherWrapperPanel.setPreferredSize(new Dimension(DefaultSettings.RIGHT_RING_TOOLS_EXPAND_WIDTH - 49, DefaultSettings.RIGHT_RING_TOOLS_EXPAND_HEIGHT));
            otherWrapperPanel.add(bookHomePane);
            otherWrapperPanel.add(Box.createHorizontalStrut(20));
            otherWrapperPanel.add(circlePane);
            otherWrapperPanel.add(Box.createHorizontalStrut(20));
            otherWrapperPanel.add(grpPane);
        } else {
            setLayout(new BorderLayout(0, 5));
            setPreferredSize(new Dimension(DefaultSettings.RIGHT_RING_TOOLS_COLLAPSE_WIDTH, 0));
            
            add(expandWrapperPanel, BorderLayout.NORTH);
            add(container, BorderLayout.CENTER);
            add(Box.createVerticalStrut(50), BorderLayout.SOUTH);
            
            expandPane.setImage(expandImg);
            expandPane.setToolTipText("Show Ring Tools & App");
            expandWrapperPanel.add(expandPane);
            
            otherWrapperPanel.setLayout(new BoxLayout(otherWrapperPanel, BoxLayout.Y_AXIS));
            otherWrapperPanel.setPreferredSize(new Dimension(DefaultSettings.RIGHT_RING_TOOLS_COLLAPSE_WIDTH, 160));
            otherWrapperPanel.add(bookHomePane);
            otherWrapperPanel.add(Box.createRigidArea(new Dimension(50, 5)));
            otherWrapperPanel.add(circlePane);
            otherWrapperPanel.add(Box.createRigidArea(new Dimension(50, 5)));
            otherWrapperPanel.add(grpPane);
        }

        revalidate();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (isExpendView) {
            g2.setColor(RingColorCode.LEFT_PANEL_BG_COLOR);
            g2.fill(new Rectangle(0, 0, getWidth(), getHeight()));
        } else {
            Rectangle r = new Rectangle(0, 0, bgImg.getWidth(), bgImg.getHeight());
            TexturePaint textrue = new TexturePaint(bgImg, r);
            g2.setPaint(textrue);
            g2.fill(new Rectangle(0, 0, getWidth(), getHeight()));
        }
    }

}
