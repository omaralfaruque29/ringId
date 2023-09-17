/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

import com.ipvision.view.utility.ImagePane;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.AddEmoticonsJDialog;
import static com.ipvision.view.image.SingleImgeDetails.setSingleImgeDetails;
import com.ipvision.view.feed.AnimatedLike;

/**
 *
 * @author Faiz
 */
public class BasicImageViewStructure extends JDialog implements MouseListener {

    private final JPanel content;
    private int posX = 0;
    private int posY = 0;
    public static int windowHeight;
    int windowWidth;
    int padding = 3;
    public static int commentWidth = 300;
    /**/
    private ImagePane imagePaneMain;
    private JButton closeButton;
    private JLabel timeLabel;
    private JPanel leftButtonPanel;
    private JPanel rightButtonPanel;
    private JLabel friend_image_panel;
    private JLabel firstLabel;
    private JLabel seperatorLabel;
    private JLabel secondLabel;
    private JPanel captionAndCommentsPanel;
    /**/
    public static EditDeleteImageCommentPopup editDeleteImageCommentPopup;
    public static ImageLikesPopup imageLikesPopup;
    public static LikesInImageCommentsPopup likesInImageCommentsPopup;
    public static boolean clieckedEditDeletepopup = false;
    public static AddEmoticonsJDialog addEmoticonsJDialog;
    public static SaveImageInDisk saveImageInDiskDialog;
    public static AnimatedLike animatedLike;
    /**/
    private JPanel likePanel;

    public BasicImageViewStructure() {
        Dimension getscreensize = Toolkit.getDefaultToolkit().getScreenSize();
        windowHeight = (int) getscreensize.getHeight() - 200;
        windowWidth = (int) getscreensize.getWidth() - 300;
        commentWidth = (windowWidth * 25) / 100;
        setSize(windowWidth, windowHeight);
        setLocationRelativeTo(null);
        setUndecorated(true);
        content = (JPanel) getContentPane();
        content.setLayout(new BorderLayout(5, 5));
        content.setBorder(new MatteBorder(1, 1, 1, 1, DefaultSettings.APP_DEFAULT_THEME_COLOR));
        content.addMouseListener(poxValueListener);
        //content.addMouseMotionListener(frameDragListener);
//        content.setBackground(RingColorCode.DEFAULT_BACKGROUND_COLOR);
        content.setBackground(Color.WHITE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                if (likesInImageCommentsPopup != null && likesInImageCommentsPopup.isDisplayable()) {
                    return;
                } else if (imageLikesPopup != null && imageLikesPopup.isDisplayable()) {
                    return;
                } else if (addEmoticonsJDialog != null && addEmoticonsJDialog.isDisplayable()) {
                    return;
                } else if (saveImageInDiskDialog != null && saveImageInDiskDialog.chooser != null && saveImageInDiskDialog.chooser.isDisplayable()) {
                    return;
                } else if (clieckedEditDeletepopup == true) {
                    return;
                } else if (animatedLike != null && animatedLike.isDisplayable()) {
                    return;
                }
                hideThis();
            }
        });
        addBasicPanels();
    }

    private void addBasicPanels() {
        closeButton = DesignClasses.createImageButton(GetImages.CLOSE_TRANSPARENT, GetImages.CLOSE_TRANSPARENT, "Close");
        JPanel commentsAndStatusPanel_1 = new JPanel(new BorderLayout(0, 0));
        commentsAndStatusPanel_1.setPreferredSize(new Dimension(commentWidth, windowHeight));
        commentsAndStatusPanel_1.setBorder(new EmptyBorder(padding, padding, padding, padding));
        commentsAndStatusPanel_1.setOpaque(false);
        content.add(commentsAndStatusPanel_1, BorderLayout.WEST);
        JPanel imageAndLikePanell_1 = new JPanel(new BorderLayout());
        imageAndLikePanell_1.setBackground(Color.BLACK);

        imagePaneMain = new ImagePane();
        imagePaneMain.setLayout(new BorderLayout());
        imagePaneMain.setOpaque(false);

        imageAndLikePanell_1.add(imagePaneMain, BorderLayout.CENTER);
        imageAndLikePanell_1.setBorder(new EmptyBorder(0, 0, 0, padding));
        // imageAndLikePanell_1.setPreferredSize(new Dimension(commentWidth, windowHeight));
        content.add(imageAndLikePanell_1, BorderLayout.CENTER);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        topPanel.setOpaque(false);
        topPanel.setPreferredSize(new Dimension(22, 22));
        topPanel.add(closeButton);
        leftButtonPanel = new JPanel(new GridBagLayout());
        leftButtonPanel.setOpaque(false);
        leftButtonPanel.setPreferredSize(new Dimension(30, 30));
        rightButtonPanel = new JPanel(new GridBagLayout());
        rightButtonPanel.setOpaque(false);
        rightButtonPanel.setPreferredSize(new Dimension(30, 30));

        imagePaneMain.add(topPanel, BorderLayout.NORTH);
        imagePaneMain.addMouseListener(this);
        imagePaneMain.addMouseMotionListener(frameDragListener);
        imagePaneMain.add(leftButtonPanel, BorderLayout.WEST);
        imagePaneMain.add(rightButtonPanel, BorderLayout.EAST);
        likePanel = new JPanel();
        likePanel.setBackground(new Color(0, 0, 0));
        //likePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JPanel likeTempPanel = new JPanel(new BorderLayout());
        likeTempPanel.setOpaque(false);
        likeTempPanel.setPreferredSize(new Dimension(25, 30));
        likeTempPanel.add(likePanel);
        imagePaneMain.add(likeTempPanel, BorderLayout.SOUTH);

        JPanel userNameAndImagePanle = new JPanel(new BorderLayout());
        userNameAndImagePanle.setOpaque(false);
        userNameAndImagePanle.setLayout(new BorderLayout(2, 2));
        commentsAndStatusPanel_1.add(userNameAndImagePanle, BorderLayout.NORTH);
        captionAndCommentsPanel = new JPanel(new BorderLayout());
        captionAndCommentsPanel.setOpaque(false);
        commentsAndStatusPanel_1.add(captionAndCommentsPanel, BorderLayout.CENTER);
        commentsAndStatusPanel_1.revalidate();

        JPanel basicPanel = new JPanel();
        basicPanel.setOpaque(false);
        basicPanel.setLayout(new BoxLayout(basicPanel, BoxLayout.X_AXIS));
        userNameAndImagePanle.add(basicPanel, BorderLayout.SOUTH);
        JPanel fullNameUpdateTextTimeLabel = new JPanel(new BorderLayout(2, 2));
        fullNameUpdateTextTimeLabel.setOpaque(false);
        fullNameUpdateTextTimeLabel.setBorder(new EmptyBorder(5, 5, 5, 2));
        friend_image_panel = new JLabel();
        friend_image_panel.setPreferredSize(new Dimension(38, 38));
        basicPanel.add(friend_image_panel);
        /**/
        JPanel fullNameContainerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
        fullNameContainerPanel.setOpaque(false);
        firstLabel = DesignClasses.makeJLabelFullName2("", DefaultSettings.FULL_NAME_FONT_SIZE_IN_FEED);
        firstLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fullNameContainerPanel.add(firstLabel);
        seperatorLabel = new JLabel();
        fullNameContainerPanel.add(seperatorLabel);
        secondLabel = DesignClasses.makeJLabelFullName2("", DefaultSettings.FULL_NAME_FONT_SIZE_IN_FEED);
        fullNameContainerPanel.add(secondLabel);

        /**/
        JPanel fullNameAndRemoveCommentPanel = new JPanel(new BorderLayout());
        fullNameAndRemoveCommentPanel.setBackground(Color.red);
        fullNameAndRemoveCommentPanel.setOpaque(false);
        fullNameAndRemoveCommentPanel.add(fullNameContainerPanel, BorderLayout.WEST);
        timeLabel = DesignClasses.makeJLabel_normal("", 10, DefaultSettings.text_color2);
        fullNameUpdateTextTimeLabel.add(timeLabel, BorderLayout.SOUTH);
        fullNameUpdateTextTimeLabel.add(fullNameAndRemoveCommentPanel, BorderLayout.CENTER);
        basicPanel.add(fullNameUpdateTextTimeLabel);
        showPanels(false);
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

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        posX = e.getX();
        posY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        showPanels(true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        showPanels(false);
    }

    private void showPanels(boolean show) {
        likePanel.setVisible(show);
    }

    public void hideThis() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(false);
                dispose();
                if (SingleImgeDetails.getSingleImgeDetails() != null) {
                    if (SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                        SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel.getCommentsOfthisImage().clear();
                        SingleImgeDetails.getSingleImgeDetails().addImageCaptionCommentsPanel.getImageComments().clear();
                    }
                } else if (SingleImageDetailsForNotifications.getSingleImgeDetails() != null) {
                    if (SingleImageDetailsForNotifications.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                        SingleImageDetailsForNotifications.getSingleImgeDetails().addImageCaptionCommentsPanel.getCommentsOfthisImage().clear();
                        SingleImageDetailsForNotifications.getSingleImgeDetails().addImageCaptionCommentsPanel.getImageComments().clear();
                    }
                } else if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null) {
                    if (SingleImageDetailsProfileCover.getSingleImgeDetails().addImageCaptionCommentsPanel != null) {
                        SingleImageDetailsProfileCover.getSingleImgeDetails().addImageCaptionCommentsPanel.getCommentsOfthisImage().clear();
                        SingleImageDetailsProfileCover.getSingleImgeDetails().addImageCaptionCommentsPanel.getImageComments().clear();
                    }
                }
                clieckedEditDeletepopup = false;
                editDeleteImageCommentPopup = null;
                imageLikesPopup = null;
                likesInImageCommentsPopup = null;
                animatedLike = null;
                if (addEmoticonsJDialog != null) {
                    addEmoticonsJDialog.setVisible(false);
                    addEmoticonsJDialog = null;
                }
                setSingleImgeDetails(null);
                System.gc();
                Runtime.getRuntime().gc();
            }
        }
        );

    }
    /*variable info*/
//    public static void main(String[] args) {
//        BasicImageViewStructure b = new BasicImageViewStructure();
//        b.setVisible(true);
//    }

    public ImagePane getImagePaneMain() {
        return imagePaneMain;
    }

    public void setImagePaneMain(ImagePane imagePaneMain) {
        this.imagePaneMain = imagePaneMain;
    }

    public JButton getCloseButton() {
        return closeButton;
    }

    public void setCloseButton(JButton closeButton) {
        this.closeButton = closeButton;
    }

    public JLabel getTimeLabel() {
        return timeLabel;
    }

    public void setTimeLabel(JLabel timeLabel) {
        this.timeLabel = timeLabel;
    }

    public JPanel getLeftButtonPanel() {
        return leftButtonPanel;
    }

    public void setLeftButtonPanel(JPanel leftButtonPanel) {
        this.leftButtonPanel = leftButtonPanel;
    }

    public JPanel getRightButtonPanel() {
        return rightButtonPanel;
    }

    public void setRightButtonPanel(JPanel rightButtonPanel) {
        this.rightButtonPanel = rightButtonPanel;
    }

    public JLabel getFriend_image_panel() {
        return friend_image_panel;
    }

    public void setFriend_image_panel(JLabel friend_image_panel) {
        this.friend_image_panel = friend_image_panel;
    }

    public JLabel getFirstLabel() {
        return firstLabel;
    }

    public void setFirstLabel(JLabel firstLabel) {
        this.firstLabel = firstLabel;
    }

    public JLabel getSeperatorLabel() {
        return seperatorLabel;
    }

    public void setSeperatorLabel(JLabel seperatorLabel) {
        this.seperatorLabel = seperatorLabel;
    }

    public JLabel getSecondLabel() {
        return secondLabel;
    }

    public void setSecondLabel(JLabel secondLabel) {
        this.secondLabel = secondLabel;
    }

    public JPanel getLikePanel() {
        return likePanel;
    }

    public void setLikePanel(JPanel likePanel) {
        this.likePanel = likePanel;
    }

    public JPanel getCaptionAndCommentsPanel() {
        return captionAndCommentsPanel;
    }

    public void setCaptionAndCommentsPanel(JPanel captionAndCommentsPanel) {
        this.captionAndCommentsPanel = captionAndCommentsPanel;
    }

}
