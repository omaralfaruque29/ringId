/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.loginsignup;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.ipvision.view.JDialogWebcam;
import com.ipvision.view.SplashScreenAfterLogin;
import com.ipvision.service.SendImageUploadRequest;
import com.ipvision.model.JsonFields;
import com.ipvision.view.utility.RoundedCornerButton;
import com.ipvision.view.utility.ImageInformation;
import com.ipvision.view.utility.ImagePane;
import com.ipvision.view.utility.ImageUtil;

/**
 *
 * @author raj
 */
public class ProfileImagePanel extends ImagePane implements KeyListener {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ProfileImagePanel.class);

    private JLabel profileImagelabel;
    //private JLabel errorLabel;
    private JPanel mainPanel;
    private JPanel imagePanel;
    private JPanel buttonPanel;
    private JButton btnBrowse;
    private JButton btnCamera;
    private JButton btnSkip;
    private JButton btnUsePicture;
    private JsonFields json_fields;
    private BufferedImage BG_IMAGE = DesignClasses.return_buffer_image(GetImages.MAIN_BACKGROUND_IMAGE);

    private ImageFilter1 imageFilter = new ImageFilter1();
    private static File fileToUpload = null;
    private BufferedImage image = null;
    private int cropX = 0;
    private int cropY = 0;
    private int WIDTH = DefaultSettings.PROFILE_PIC_UPLOAD_SCREEN_WIDTH;
    private int HEIGHT = DefaultSettings.PROFILE_PIC_UPLOAD_SCREEN_HEIGHT;
    private MouseHandler handler;

    public JsonFields getJson_fields() {
        return json_fields;
    }

    public void setJson_fields(JsonFields json_fields) {
        this.json_fields = json_fields;
    }

    public ProfileImagePanel() {
        if (BG_IMAGE != null) {
            this.setImage(BG_IMAGE);
            BG_IMAGE.flush();
        }

        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setOpaque(false);
        add(containerPanel, BorderLayout.EAST);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        containerPanel.add(topPanel, BorderLayout.NORTH);

        /*JPanel ringIconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 90, 0));
         ringIconPanel.setBorder(new EmptyBorder(50, 0, 0, 0));
         ringIconPanel.setOpaque(false);
         topPanel.add(ringIconPanel, BorderLayout.NORTH);

         JLabel lblRingIcon = new JLabel(DesignClasses.return_image(GetImages.RINGID_1));
         ringIconPanel.add(lblRingIcon);*/

        /*JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 100, 10));
         idPanel.setOpaque(false);
         topPanel.add(idPanel, BorderLayout.CENTER);

         JLabel ringIdLabel = new JLabel(MyFnFSettings.LOGIN_USER_ID);
         ringIdLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
         idPanel.add(ringIdLabel);*/
//        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 90, 5));
//        errorPanel.setPreferredSize(new Dimension(300, 27));
//        errorPanel.setBackground(Color.red);
//        // errorPanel.setOpaque(false);
//        topPanel.add(errorPanel, BorderLayout.SOUTH);
//
//        errorLabel = DesignClasses.makeAncorLabel("", 0, 12);
//        errorLabel.setForeground(DefaultSettings.blue_bar_background);
//        errorPanel.add(errorLabel, BorderLayout.WEST);
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        containerPanel.add(mainPanel, BorderLayout.CENTER);

        initContents();

    }

    private void initContents() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;

        profileImagelabel = new JLabel(DesignClasses.return_image(GetImages.PROFILE_IMAGE_BOX));

        imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imagePanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        //imagePanel.setBorder(new ShadowBorder(true));
        imagePanel.setOpaque(false);
        imagePanel.add(profileImagelabel);

        JPanel imageWrapperPanel = new JPanel(new BorderLayout());
        imageWrapperPanel.setBorder(BorderFactory.createDashedBorder(new Color(0xa1a1a1), 1.7f, 1.7f, 2.5f, false));
        imageWrapperPanel.setPreferredSize(new Dimension(WIDTH + 4, HEIGHT + 4));
        imageWrapperPanel.setOpaque(false);
        imageWrapperPanel.add(imagePanel);

        JPanel imageContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 90, 0));
        imageContainer.setOpaque(false);
        imageContainer.add(imageWrapperPanel);
        mainPanel.add(imageContainer, c);
        c.gridy += 1;
        c.insets = new Insets(5, 0, 5, 0);

        JPanel instructionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        instructionPanel.setOpaque(false);
        JLabel instructionLabel = new JLabel();
        instructionLabel.setForeground(new Color(0x686868));
        instructionLabel.setText("Set your profile picture. So friends, family and colleagues can find you.");
        instructionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        instructionPanel.add(instructionLabel);
        mainPanel.add(instructionPanel, c);
        c.gridy += 1;
        c.insets = new Insets(5, 0, 5, 0);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 55, 0));
        buttonPanel.setBorder(new EmptyBorder(0, 50, 0, 0));
        buttonPanel.setOpaque(false);
        btnSkip = new RoundedButton("Skip", "Skip");
        btnSkip.addActionListener(actionListener);
        btnSkip.setVisible(true);
        btnSkip.grabFocus();
        btnBrowse = DesignClasses.createImageButton(GetImages.DIRECTORY_UPLOAD, GetImages.DIRECTORY_UPLOAD_H, "Upload from computer");
        btnBrowse.addActionListener(actionListener);
        btnCamera = DesignClasses.createImageButton(GetImages.CAMERA_PROFILE, GetImages.CAMERA_PROFILE_H, "Camera");
        btnCamera.addActionListener(actionListener);
        buttonPanel.add(btnBrowse);
        buttonPanel.add(btnCamera);

        mainPanel.add(buttonPanel, c);
        c.gridy += 1;
        c.insets = new Insets(65, 0, 30, 0);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 0));
        bottomPanel.setOpaque(false);

        bottomPanel.add(btnSkip);
        btnUsePicture = new RoundedButton("Use Picture", "Use Picture this picture as profile image");
        btnUsePicture.setEnabled(false);
        btnUsePicture.addActionListener(actionListener);
        bottomPanel.add(btnUsePicture);
        mainPanel.add(bottomPanel, c);
        // btnSkip.requestFocusInWindow();
        btnSkip.grabFocus();
        //  btnSkip.requestFocus();
        btnSkip.addKeyListener(this);
        this.addKeyListener(this);
    }

    ActionListener actionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnBrowse) {
                openFile();
            } else if (e.getSource() == btnCamera) {
                JDialogWebcam webcam = new JDialogWebcam();
                if (JDialogWebcam.fileToUpload != null && JDialogWebcam.fileToUpload.exists()) {
                    fileToUpload = JDialogWebcam.fileToUpload;
                    showPictureInView();
                }
            } else if (e.getSource() == btnSkip) {
                json_fields.setProfileImage(MyFnFSettings.userProfile.getProfileImage());
                json_fields.setProfileImageId(MyFnFSettings.userProfile.getProfileImageId());
                /* if (LoginUI.getLoginUI() != null) {
                 LoginUI.getLoginUI().disposeLoginUI();
                 LoginUI.loginUI = null;
                 }
                 SplashScreenAfterLogin sp = new SplashScreenAfterLogin(json_fields);
                 sp.setVisible(true);*/
                LoginUI.getLoginUI().loadMainContent(LoginUI.getLoginUI().SUCCESS_UI);
                LoginUI.getLoginUI().successPanel.setValues(json_fields);

            } else if (e.getSource() == btnUsePicture) {
                btnBrowse.setEnabled(false);
                btnCamera.setEnabled(false);
                btnSkip.setEnabled(false);
                btnUsePicture.setEnabled(false);

                if (fileToUpload != null && fileToUpload.getAbsolutePath() != null && image != null) {
                    profileImagelabel.setIcon(DesignClasses.return_image(GetImages.PLEASE_WAIT_60));
                    SendImageUploadRequest dd = new SendImageUploadRequest(fileToUpload.getAbsolutePath(), image, MyFnFSettings.PROFILE_IMAGE, cropX, cropY, WIDTH, HEIGHT, 0, btnSkip);
                    dd.start();
                } else {
                    HelperMethods.showPlaneDialogMessage("Can not upload");
                    imagePanel.removeAll();
                    imagePanel.add(profileImagelabel);
                    imagePanel.revalidate();

                    btnBrowse.setEnabled(true);
                    btnCamera.setEnabled(true);
                    btnSkip.setEnabled(true);
                    btnUsePicture.setEnabled(false);
                }
            }
        }
    };

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if ((int) e.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSkip.doClick();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void openFile() {
        try {
            //JFileChooser fc = new JFileChooser();
            JFrame dd = new JFrame();
            FileDialog fc = new FileDialog(dd, "Browse", FileDialog.LOAD);
            fc.setFile("*.jpg;*.jpeg;*.png;*.gif");
            fc.setMultipleMode(false);
            fc.show();
            fileToUpload = fc.getFiles()[0];
            if (fileToUpload != null) {
                showPictureInView();
            }

        } catch (Exception ex) {
        }
    }

    public void showPictureInView() {
        image = null;
        try {
            if (fileToUpload != null && DesignClasses.file_exists(fileToUpload.getAbsolutePath())) {
                File file = new File(fileToUpload.getAbsolutePath());
                BufferedImage tmp = ImageIO.read(file);
                try {
                    ImageInformation info = ImageInformation.readImageFileInformation(file);
                    if (info.orientation > 1) {
                        AffineTransform aff = ImageInformation.getExifTransformation(info);
                        tmp = ImageInformation.transformImage(tmp, aff);
                    }
                } catch (Exception e) {
                    // e.printStackTrace();
                    log.error("Error in showPictureInView ==>" + e.getMessage());
                }

                tmp = ImageUtil.reduceQualityAndSize(tmp, DefaultSettings.MAXIMUM_PROFILE_IMAGE_WIDTH);

                if (tmp.getWidth() < WIDTH || tmp.getHeight() < HEIGHT) {
                    HelperMethods.showPlaneDialogMessage("Profile photo's minimum resulotion: " + WIDTH + " x " + HEIGHT + "");
                } else {
                    image = tmp;
                }
//                else {
//                    image = ImageUtil.reduceQualityAndSize(tmp, DefaultSettings.MAXIMUM_IMAGE_WIDTH);
//                }
            }
        } catch (Exception ex) {
            log.error("change profile picture ==>" + ex.getMessage());
        }

        if (image != null) {
            btnUsePicture.setEnabled(true);
            profileImagelabel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            profileImagelabel.setIcon(new ImageIcon(image));
            profileImagelabel.setBounds(0, 0, image.getWidth(), image.getHeight());
            profileImagelabel.setHorizontalAlignment(JLabel.LEFT);
            profileImagelabel.setVerticalAlignment(JLabel.TOP);
            handler = new MouseHandler(image, WIDTH, HEIGHT);
            profileImagelabel.addMouseListener(handler);
            profileImagelabel.addMouseMotionListener(handler);
        } else {
            btnUsePicture.setEnabled(false);
            profileImagelabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            profileImagelabel.removeMouseListener(handler);
            profileImagelabel.removeMouseMotionListener(handler);
        }
    }

    protected class MouseHandler extends MouseAdapter {

        private boolean active = false;
        private int xDisp;
        private int yDisp;
        private BufferedImage image;
        private int minX;
        private int minY;
        private int maxX;
        private int maxY;

        public MouseHandler(BufferedImage image, int width, int height) {
            this.image = image;
            this.minX = 0;
            this.minY = 0;
            this.maxX = (-image.getWidth() + width);
            this.maxY = (-image.getHeight() + height);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            active = true;
            JLabel label = (JLabel) e.getComponent();

            xDisp = e.getPoint().x - label.getLocation().x;
            yDisp = e.getPoint().y - label.getLocation().y;

            //label.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            active = false;
            JLabel label = (JLabel) e.getComponent();
            //label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (active) {
                JLabel label = (JLabel) e.getComponent();
                Point point = e.getPoint();

                cropX = point.x - xDisp;
                cropY = point.y - yDisp;
                cropX = cropX > minX ? minX : cropX;
                cropY = cropY > minY ? minY : cropY;
                cropX = cropX < maxX ? maxX : cropX;
                cropY = cropY < maxY ? maxY : cropY;

                label.setLocation(cropX, cropY);
                label.invalidate();
                label.repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }

    class ImageFilter1 implements FilenameFilter {

        @Override
        public boolean accept(File f, String name) {
            return f.getName().toLowerCase().endsWith(".png")
                    || f.getName().toLowerCase().endsWith(".jpg")
                    || f.getName().toLowerCase().endsWith(".gif")
                    || f.isDirectory();
        }

    }

}
