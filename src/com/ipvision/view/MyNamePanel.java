/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.StatusConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import com.ipvision.service.ChangePresence;
import com.ipvision.view.utility.JCustomMenuPopup;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.view.utility.ImagePane;
import javax.swing.BoxLayout;

/**
 *
 * @author Faiz
 */
public class MyNamePanel extends ImagePane implements MouseListener {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MyNamePanel.class);
    private JLabel myNameLabel;
    private JLabel myRingidLabel;
    private JLabel profilePictureLabel;
    public JLabel mobileNoLabel;
    public JLabel emailLabel;
    private JPanel pnlMobileNo;
    private JPanel pnlEmail;
    private JPanel bottomPanel;
    public static MyNamePanel myNamePanel;
    private int h;
    BufferedImage img = null;
    private ImageIcon statusIcon;
    private JCustomMenuPopup statusPopup = null;
    private JLabel statusImgLabel;
    private final String MNU_ONLINE = "Online";
    private final String MNU_OFFLINE = "Offline";
    private JLabel statusLabel;
    private ImageIcon currentIcon;
    private ImageIcon statusHoverIcon;

//    @Override
//    protected void paintComponent(Graphics g) {
//        try {
//            super.paintComponent(g);
//            img = ImageIO.read(new Object() {
//            }.getClass().getClassLoader().getResource(GetImages.DEFAULT_MYPROFILE_BACKGROUND_IMAGE));
//        } catch (Exception e) {
//        }
//        if (img != null) {
//            Graphics2D g2d = (Graphics2D) g.create();
//            g2d.drawImage(img, 0, 0, this);
//            g2d.dispose();
//        }
//        // System.out.println("IMG PAISE");
//    }
    public static MyNamePanel getInstance() {
        if (myNamePanel == null) {
            myNamePanel = new MyNamePanel();
        }
        return myNamePanel;
    }

    public MyNamePanel() {
        //setBackground(RingColorCode.SECOND_BAR_BG_COLOR);
        this.myNamePanel = this;
        statusHoverIcon = DesignClasses.return_image(GetImages.STATUS_HOVER);
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(0, DefaultSettings.LEFT_EMPTY_SPACE - 10, 0, 0));
        setLayout(new BorderLayout(0, 0));
        // setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
        setPreferredSize(new Dimension(DefaultSettings.LEFT_FRIEND_LIST_WIDTH, DefaultSettings.MY_NAME_PANEL_HEIGHT));
        changeBgImage(false);
        addProfilepicturcircleLabel();
        //  addAncestorListener(this);
        addMouseListener(this);
    }

    public final void changeBgImage(boolean active) {
        try {
            if (active) {
                setImage(ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.DEFAULT_MYPROFILE_BACKGROUND_IMAGE_H)));
            } else {
                setImage(ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.DEFAULT_MYPROFILE_BACKGROUND_IMAGE)));
            }
            revalidate();
        } catch (IOException ex) {
            //log.error("Buffer img error" + ex.getMessage());
        }
    }

    public void init() {
        JPanel nameWrapperPanel = new JPanel(new BorderLayout(0, 0));
        nameWrapperPanel.setBorder(new EmptyBorder(17, 0, 0, 0));
        nameWrapperPanel.setOpaque(false);

        JPanel mayname2 = new JPanel();
        mayname2.setLayout(new BorderLayout());
        mayname2.setOpaque(false);

        nameWrapperPanel.add(mayname2, BorderLayout.CENTER);

        JPanel pnlMyName = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlMyName.setOpaque(false);
        myNameLabel = DesignClasses.makeJLabelFullNameMyNamePanel("", 16);
        //myNameLabel = DesignClasses.makeLableBold("", 16);
        //myNameLabel.setForeground(new Color(0X43454c));
        myNameLabel.setPreferredSize(new Dimension(170, 20));
        pnlMyName.add(myNameLabel);
        mayname2.add(pnlMyName, BorderLayout.NORTH);

        bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(new EmptyBorder(0, 0, 5, 0));

        JPanel wrapperBottomPanel = new JPanel(new BorderLayout());
        wrapperBottomPanel.setOpaque(false);
        wrapperBottomPanel.add(bottomPanel, BorderLayout.NORTH);

        mayname2.add(wrapperBottomPanel, BorderLayout.CENTER);

        JPanel wrapperStatusImgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        wrapperStatusImgPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        wrapperStatusImgPanel.setOpaque(false);
        wrapperStatusImgPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JPanel statusImgPanel = new JPanel(new BorderLayout());
        //statusImgPanel.setBorder(new EmptyBorder(1, 0, 1, 0));
        statusImgPanel.setOpaque(false);
        statusImgPanel.setToolTipText("Change your status");
        statusImgLabel = new JLabel();
        statusImgPanel.add(statusImgLabel, BorderLayout.CENTER);
        wrapperStatusImgPanel.add(statusImgPanel);

        // nameWrapperPanel.add(wrapperStatusImgPanel, BorderLayout.WEST);
        statusImgPanel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                currentIcon = (ImageIcon) statusImgLabel.getIcon();
                // ImageIcon statusHoverIcon = DesignClasses.return_image(GetImages.STATUS_HOVER);
                statusImgLabel.setIcon(statusHoverIcon);
                statusImgLabel.revalidate();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                statusImgLabel.setIcon(currentIcon);
                statusImgLabel.revalidate();

            }

            @Override
            public void mouseClicked(MouseEvent e) {

                statusPopup = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_STATUS);
                statusPopup.addMenu(MNU_ONLINE, GetImages.STATUS_ONLINE);
                statusPopup.addMenu(MNU_OFFLINE, GetImages.STATUS_OFFLINE);
                statusPopup.setVisible(statusImgLabel, 25, -5);
            }

        });

        JPanel pnlRingId = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlRingId.setOpaque(false);
        myRingidLabel = DesignClasses.makeJLabelFullName("My RingId", 10);
        myRingidLabel.setForeground(new Color(0x868789));
        // myRingidLabel.setFont(getFont(10));
//        try {
//            Font font = DesignClasses.getDefaultFont(Font.PLAIN, 10);//new Font("Arial Unicode MS", Font.BOLD, fontsize);
//            HashMap<TextAttribute, Object> attrs = new HashMap<TextAttribute, Object>();
//            attrs.put(TextAttribute.TRACKING, .02);
//            font = font.deriveFont(attrs);
//            myRingidLabel.setFont(font);
//        } catch (Exception e) {
//        }
        pnlRingId.add(myRingidLabel);

        pnlMobileNo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlMobileNo.setOpaque(false);
        mobileNoLabel = DesignClasses.makeJLabelFullName("", 10);
        mobileNoLabel.setForeground(new Color(0x868789));
        //mobileNoLabel.setFont(getFont(10));
        pnlMobileNo.add(mobileNoLabel);

        pnlEmail = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlEmail.setOpaque(false);
        emailLabel = DesignClasses.makeJLabelFullName("", 10);
        emailLabel.setForeground(new Color(0x868789));
        //emailLabel.setFont(getFont(10));
        pnlEmail.add(emailLabel);

        statusLabel = DesignClasses.makeJLabelFullName(MNU_ONLINE, 10);
        statusLabel.setForeground(new Color(0x868789));
        wrapperStatusImgPanel.add(statusLabel);

        bottomPanel.add(pnlRingId);
        bottomPanel.add(pnlMobileNo);
        bottomPanel.add(pnlEmail);
        bottomPanel.add(wrapperStatusImgPanel);

        add(nameWrapperPanel, BorderLayout.CENTER);
        changeStatusIcon();

    }

    public void changeStatusIcon() {
        if (MyFnFSettings.userProfile.getPresence() == StatusConstants.PRESENCE_ONLINE) {
            statusLabel.setText(MNU_ONLINE);
            statusIcon = DesignClasses.return_image(GetImages.STATUS_ONLINE);
            statusImgLabel.setIcon(statusIcon);

        } else if (MyFnFSettings.userProfile.getPresence() == StatusConstants.PRESENCE_DO_NOT_DISTURB) {
            statusLabel.setText(MNU_OFFLINE);
            statusIcon = DesignClasses.return_image(GetImages.STATUS_OFFLINE);
            statusImgLabel.setIcon(statusIcon);
        } else if (MyFnFSettings.userProfile.getPresence() == StatusConstants.PRESENCE_OFFLINE) {
            statusLabel.setText(MNU_OFFLINE);
            statusIcon = DesignClasses.return_image(GetImages.STATUS_OFFLINE);
            statusImgLabel.setIcon(statusIcon);
        }
    }

    MouseListener menuListener = new MouseAdapter() {

        @Override
        public void mouseEntered(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            menu.setMouseEntered();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            menu.setMouseExited();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            if (menu.text.equalsIgnoreCase(MNU_ONLINE)) {
                if (MyFnFSettings.userProfile.getPresence() != StatusConstants.PRESENCE_ONLINE) {
                    new ChangePresence(StatusConstants.PRESENCE_ONLINE).start();
                    statusPopup.hideThis();

                }

            } else if (menu.text.equalsIgnoreCase(MNU_OFFLINE)) {
                if (MyFnFSettings.userProfile.getPresence() != StatusConstants.PRESENCE_DO_NOT_DISTURB) {
                    new ChangePresence(StatusConstants.PRESENCE_DO_NOT_DISTURB).start();
                    statusPopup.hideThis();

                }

            }

        }

    };

    private void viewProfilePhoto() {
        if (MyFnFSettings.userProfile.getProfileImage() != null && MyFnFSettings.userProfile.getProfileImage().trim().length() > 0) {
            ImageHelpers.addProfileImageThumb(profilePictureLabel, MyFnFSettings.userProfile.getProfileImage(), 70, 1, Color.GRAY);
            profilePictureLabel.revalidate();
            this.repaint();
            this.revalidate();
        } else {
            ImageHelpers.addProfileImageThumb(profilePictureLabel, GetImages.UNKNOW_IMAGE_70, 70, 1, Color.LIGHT_GRAY);
            profilePictureLabel.revalidate();
            this.repaint();
            this.revalidate();

        }

    }

    private void addProfilepicturcircleLabel() {
        ImagePane profilePicPanel = new ImagePane();
        profilePicPanel.setLayout(null);
        profilePicPanel.setOpaque(false);
        try {
            profilePicPanel.setImage(ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.CRICLE_OUTSIDE_PROFILE_PIC_LEFT)));
        } catch (IOException ex) {
            // ex.getMessage();
            log.error("Exception in addProfilepicturcircleLabel ==>" + ex.getMessage());
        }
        profilePicPanel.setPreferredSize(new Dimension(DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT, DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT));
        profilePictureLabel = new JLabel();
        // profilePictureLabel.setBounds(7, 36, DefaultSettings.PROFILE_PIC_WIDTH, DefaultSettings.PROFILE_PIC_WIDTH);
        profilePictureLabel.setBounds(7, 19, 70, 70);

        profilePicPanel.add(profilePictureLabel);
        add(profilePicPanel, BorderLayout.WEST);
        change_profile_image();

    }

    public void change_profile_image() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                viewProfilePhoto();
            }
        });
    }

    public void change_myFullNameRingID() {
        try {
            if (myNameLabel != null) {
                if (MyFnFSettings.userProfile != null) {
                    if ((MyFnFSettings.userProfile.getFullName() != null && MyFnFSettings.userProfile.getFullName().trim().length() > 0)) {
                        myNameLabel.setText(MyFnFSettings.userProfile.getFullName());
                    } else {
                        myNameLabel.setText(MyFnFSettings.userProfile.getUserIdentity());
                    }
                } else {
                    myNameLabel.setText("Invalid user");
                }

            } else {
                myNameLabel.setText("No name");
            }

            if (myRingidLabel != null) {
                //  if (MyFnFSettings.userProfile != null) {
                if (MyFnFSettings.userProfile != null) {
                    if ((MyFnFSettings.userProfile.getUserIdentity() != null && MyFnFSettings.userProfile.getUserIdentity().trim().length() > 0)) {
                        myRingidLabel.setText(HelperMethods.getRingID(MyFnFSettings.userProfile.getUserIdentity()));
                    } else {
                        myRingidLabel.setText("Invalid ringID");
                    }
                } else {
                    myRingidLabel.setText("Invalid ringID");
                }
                //   }
            }

            if (mobileNoLabel != null) {
                if (MyFnFSettings.userProfile != null
                        && MyFnFSettings.userProfile.getMobilePhone() != null
                        && MyFnFSettings.userProfile.getMobilePhone().trim().length() > 0
                        && MyFnFSettings.userProfile.getMobilePhoneDialingCode() != null
                        && MyFnFSettings.userProfile.getMobilePhoneDialingCode().trim().length() > 0) {
                    mobileNoLabel.setText(MyFnFSettings.userProfile.getMobilePhoneDialingCode() + " " + MyFnFSettings.userProfile.getMobilePhone());
                    pnlMobileNo.setVisible(true);
                } else {
                    pnlMobileNo.setVisible(false);
                }

            }

            if (emailLabel != null) {
                if (MyFnFSettings.userProfile != null
                        && MyFnFSettings.userProfile.getEmail() != null
                        && MyFnFSettings.userProfile.getEmail().trim().length() > 0) {
                    emailLabel.setText(MyFnFSettings.userProfile.getEmail());
                    pnlEmail.setVisible(true);
                } else {
                    pnlEmail.setVisible(false);
                }

            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("Exception in here ==>" + e.getMessage());
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == this) {
            if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
                GuiRingID.getInstance().getMainRight().actionofMynamePanel();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == this) {
            //setBackground(RingColorCode.SECOND_BAR_BG_COLOR_PRESSED);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == this) {
            //setBackground(RingColorCode.SECOND_BAR_BG_COLOR);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == this) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            //setBackground(RingColorCode.SECOND_BAR_BG_COLOR_PRESSED);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == this) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            //setBackground(RingColorCode.SECOND_BAR_BG_COLOR);
        }
    }

//    @Override
//    public void ancestorAdded(AncestorEvent event) {
//        System.out.println("add ancistor");
//        changeBgImage(true);
//    }
//
//    @Override
//    public void ancestorRemoved(AncestorEvent event) { System.out.println("remove ancistor");
//        System.out.println("add ancistor");
//        changeBgImage(false);
//    }
//
//    @Override
//    public void ancestorMoved(AncestorEvent event) {
//    }
}
