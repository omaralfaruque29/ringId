/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryService;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamPicker;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.ipvision.service.utility.Scalr;
import com.ipvision.view.utility.RoundedCornerButton;

/**
 *
 * @author raj
 */
public class JDialogWebcam extends JDialog implements WebcamListener, WindowListener {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JDialogWebcam.class);
    public static JDialogWebcam instance;
    private Webcam webcam = null;
    private WebcamPanel webcamPanel = null;
    private WebcamPicker picker = null;

    public int width = 510;
    public int height = 370;
    public int padding = 12;
    //public ImagePane componentContent;
    public JPanel componentContent;
    public JPanel content;
    private JPanel webcamWrapperPanel;
    private JButton btnClose;
    private JButton btnTakePicture = new RoundedCornerButton("Take Picture", "Take Picture");
    private JButton btnTryAgain = new RoundedCornerButton("Try Again", "Try Again");
    public JButton btnUsePicture = new RoundedCornerButton("Use Picture", "Use Picture");
    private JPanel imagePanel;
    private JPanel buttonPanel;
    private JPanel firstPreviousPanel;
    private JPanel secondPreviousPanel;
    public static File fileToUpload = null;
    public static final String FIRST_ARCHIEVE_IMAGE = "_first_arc.png";
    public static final String SECOND_ARCHIEVE_IMAGE = "_second_arc.png";
    public static final String TAKEN_PICTURE_NAME = "took_image.png";

    public JDialogWebcam() {
        fileToUpload = null;
        instance = this;
        setMinimumSize(new Dimension(width, height));
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setLocationRelativeTo(null);
        setModal(true);
        setAlwaysOnTop(true);
        addWindowListener(this);

        //componentContent = new ImagePane();
        componentContent = new JPanel();
        componentContent.setBorder(new EmptyBorder(padding + 10, padding, padding + 5, padding));
        componentContent.setOpaque(false);
//        try {
//            BufferedImage image = ImageIO.read(new Object() {
//            }.getClass().getClassLoader().getResource(GetImages.WEBCAM_DIALOG_SHADOW));
//            componentContent.setImage(image);
//            image.flush();
//            image = null;
//        } catch (IOException ex) {
//        }
        setContentPane(componentContent);

        //content = new RoundPanel(false);
        content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();

                g2d.setColor(RingColorCode.RING_THEME_COLOR);
                // g2d.fillRoundRect(0, 0, w, h, 15, 15);
//                Area border = new Area(new RoundRectangle2D.Double(0, 0, w + 2, h + 2, 11, 11));
                Area border = new Area(new RoundRectangle2D.Double(0, 0, w, h, 10, 10));
                g2d.fill(border);

                g2d.setColor(Color.WHITE);//(RingColorCode.FRIEND_LIST_SELECTION_COLOR); 
                //g2d.fillRoundRect(1, 1, w - 2, h - 2, 13, 13);
//                Area shape = new Area(new RoundRectangle2D.Double(2, 2, w - 2, h - 2, 11, 11));
                Area shape = new Area(new RoundRectangle2D.Double(0.5, 0.5, w - 1, h - 1, 9, 9));
                g2d.fill(shape);
            }
        };

        content.setBackground(Color.WHITE);
        content.setOpaque(false);
        content.setLayout(new BorderLayout(0, 0));
        componentContent.add(content, BorderLayout.CENTER);

        initComponents();
        loadPreviousImage();
        startWebcam();
//        SwingUtilities.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
//                loadPreviousImage();
//                startWebcam();
//            }
//        });

    }

    private void initComponents() {
        JPanel toppanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toppanel.setPreferredSize(new Dimension(0, 32));
        toppanel.setBorder(new EmptyBorder(5, 0, 0, 5));
        toppanel.setOpaque(false);
        btnClose = DesignClasses.createImageButton(GetImages.BUTTON_CLOSE_MINI, GetImages.BUTTON_CLOSE_MINI_H, GetImages.BUTTON_CLOSE_MINI_H, "Cancel webcam");
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(actionListener);
        toppanel.add(btnClose);
        content.add(toppanel, BorderLayout.NORTH);

        webcamWrapperPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        webcamWrapperPanel.setOpaque(false);
        webcamWrapperPanel.setBorder(new EmptyBorder(0, 15, 0, 0));
        content.add(webcamWrapperPanel, BorderLayout.CENTER);

        imagePanel = new JPanel(new GridBagLayout());
        //imagePanel.setPreferredSize(new Dimension(DefaultSettings.WEBCAM_CAPTURE_WIDTH, DefaultSettings.WEBCAM_CAPTURE_HEIGHT));
        imagePanel.setOpaque(false);
        imagePanel.add(new JLabel(DesignClasses.return_image(GetImages.PROFILE_IMAGE_BOX)));
        webcamWrapperPanel.add(imagePanel);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(120, 0));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(new EmptyBorder(0, 10, 0, 15));
        content.add(rightPanel, BorderLayout.EAST);

        JPanel previousImagePanel = new JPanel();
        previousImagePanel.setLayout(new BoxLayout(previousImagePanel, BoxLayout.Y_AXIS));
        previousImagePanel.setOpaque(false);
        rightPanel.add(previousImagePanel, BorderLayout.NORTH);

        firstPreviousPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        firstPreviousPanel.setPreferredSize(new Dimension(95, 71));
        firstPreviousPanel.setOpaque(false);
        previousImagePanel.add(firstPreviousPanel);
        previousImagePanel.add(Box.createVerticalStrut(10));

        secondPreviousPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        secondPreviousPanel.setPreferredSize(new Dimension(95, 71));
        secondPreviousPanel.setOpaque(false);
        previousImagePanel.add(secondPreviousPanel);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setPreferredSize(new Dimension(0, 45));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        btnTakePicture.addActionListener(actionListener);
        btnTryAgain.addActionListener(actionListener);
        btnUsePicture.addActionListener(actionListener);
        content.add(buttonPanel, BorderLayout.SOUTH);

    }
    int i = 0;

    private void startWebcam() {
        try {
            fileToUpload = null;
            picker = new WebcamPicker();
            webcam = picker.getSelectedWebcam();
            if (webcam != null) {
                Dimension[] webcameSizes = webcam.getViewSizes();
                maxWebSize(webcameSizes);
////                webcam.setViewSize(new Dimension(DefaultSettings.WEBCAM_CAPTURE_WIDTH, DefaultSettings.WEBCAM_CAPTURE_HEIGHT));
//                //webcam.setViewSize(new Dimension(DefaultSettings.WEBCAM_CAPTURE_WIDTH, DefaultSettings.WEBCAM_CAPTURE_HEIGHT));
                int widthDiff = (int) (max.width - DefaultSettings.WEBCAM_CAPTURE_WIDTH);
                int heightDiff = (int) (max.height - DefaultSettings.WEBCAM_CAPTURE_HEIGHT);

                setMinimumSize(new Dimension((int) max.width + widthDiff, (int) max.height + heightDiff));
                pack();
                webcam.setViewSize(new Dimension((int) max.width, (int) max.height));
                webcam.addWebcamListener(JDialogWebcam.this);
                webcamPanel = new WebcamPanel(webcam, false);
                webcamPanel.setFPSDisplayed(true);
                webcamWrapperPanel.removeAll();
                webcamWrapperPanel.add(webcamPanel);
                webcamWrapperPanel.revalidate();
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        webcamPanel.start();
                    }
                };
                t.setName("WEBCAM - THREAD");
                t.setDaemon(true);
                t.start();
                setVisible(true);
            } else {
                this.setVisible(false);
                this.dispose();
                HelperMethods.showWarningDialogMessage("Webcam device not found!");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("Exception in here ==>" + e.getMessage());
        }
    }
    Dimension max;

    private Dimension maxWebSize(Dimension[] webcameSizes) {
        max = new Dimension(DefaultSettings.WEBCAM_CAPTURE_WIDTH, DefaultSettings.WEBCAM_CAPTURE_HEIGHT);
        for (Dimension maxDimension : webcameSizes) {
            if (max.width * max.height < maxDimension.width * maxDimension.height) {
                max = maxDimension;
            }
        }
        System.err.println("max" + max);
        return max;

    }

//    public static void main(String[] args) {
//        JDialogWebcam webCamPopUP1 = new JDialogWebcam();
//    }
    @Override
    public void webcamOpen(WebcamEvent we) {
        buttonPanel.removeAll();
        buttonPanel.add(btnTakePicture);
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    @Override
    public void webcamClosed(WebcamEvent we) {
    }

    @Override
    public void webcamDisposed(WebcamEvent we) {
    }

    @Override
    public void webcamImageObtained(WebcamEvent we) {
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnTakePicture) {
                fileToUpload = null;
                try {
                    if (webcam != null && webcamPanel != null) {
                        BufferedImage image = webcam.getImage();
                        ImageIcon imageIcon = null;
                        if (image != null) {
                            File file = new File(TAKEN_PICTURE_NAME);
                            fileToUpload = file;
                            ImageIO.write(image, "PNG", file);
                            imageIcon = new ImageIcon(ImageIO.read(fileToUpload));
                        }
                        webcamPanel.stop();

                        if (imageIcon != null) {
                            imagePanel.removeAll();
                            imagePanel.add(new JLabel(imageIcon));
                            imagePanel.revalidate();
                            webcamWrapperPanel.removeAll();
                            webcamWrapperPanel.add(imagePanel);
                            webcamWrapperPanel.revalidate();
                        }

                        buttonPanel.removeAll();
                        buttonPanel.add(btnTryAgain);
                        buttonPanel.add(btnUsePicture);
                        buttonPanel.revalidate();
                        buttonPanel.repaint();
                    } else {
                        fileToUpload = null;
                        instance.setVisible(false);
                        instance.dispose();
                    }
                } catch (Exception ex) {
                }
            } else if (e.getSource() == btnTryAgain) {
                fileToUpload = null;
                try {
                    if (webcam != null && webcamPanel != null) {
                        webcamPanel.start();

                        webcamWrapperPanel.removeAll();
                        webcamWrapperPanel.add(webcamPanel);
                        webcamWrapperPanel.revalidate();

                        buttonPanel.removeAll();
                        buttonPanel.add(btnTakePicture);
                        buttonPanel.revalidate();
                        buttonPanel.repaint();

                    } else {
                        fileToUpload = null;
                        instance.setVisible(false);
                        instance.dispose();
                    }
                } catch (Exception ed) {
                }
            } else if (e.getSource() == btnUsePicture) {
                savePreviousImage();
                instance.setVisible(false);
                instance.dispose();
            } else if (e.getSource() == btnClose) {
                fileToUpload = null;
                instance.setVisible(false);
                instance.dispose();
            }
        }
    };

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (webcam != null) {
            WebcamDiscoveryService discovery = Webcam.getDiscoveryService();
            discovery.stop();
            webcam.close();
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        if (webcam != null) {
            WebcamDiscoveryService discovery = Webcam.getDiscoveryService();
            discovery.stop();
            webcam.close();
        }
    }

    @Override
    public void windowIconified(WindowEvent e) {
        if (webcamPanel != null) {
            webcamPanel.pause();
        }
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        if (webcamPanel != null) {
            webcamPanel.resume();
        }
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    private void savePreviousImage() {
        try {
            if (fileToUpload != null && fileToUpload.exists()) {
                BufferedImage bimg = ImageIO.read(fileToUpload);
                if (bimg != null) {
                    File file_first = new File(MyFnFSettings.LOGIN_USER_ID + FIRST_ARCHIEVE_IMAGE);
                    if (file_first.exists()) {
                        File file_second = new File(MyFnFSettings.LOGIN_USER_ID + SECOND_ARCHIEVE_IMAGE);
                        if (file_second.exists()) {
                            file_second.delete();
                        }
                        file_first.renameTo(new File(MyFnFSettings.LOGIN_USER_ID + SECOND_ARCHIEVE_IMAGE));
                    }
                    ImageIO.write(bimg, "PNG", new File(MyFnFSettings.LOGIN_USER_ID + FIRST_ARCHIEVE_IMAGE));
                }
            }
        } catch (Exception ex) {

        }
    }

    private void loadPreviousImage() {
        try {
            BufferedImage img_first = null;
            try {
                img_first = ImageIO.read(new File(MyFnFSettings.LOGIN_USER_ID + FIRST_ARCHIEVE_IMAGE));
            } catch (IOException e) {
            }
            if (img_first != null) {
                //img_first = DesignClasses.scaleImage(90, 80, img_first);
                img_first = Scalr.resize(img_first, Scalr.Mode.FIT_EXACT, 95, 71, Scalr.OP_ANTIALIAS);
                JLabel prevousImage_first = new JLabel(new ImageIcon(img_first));
                prevousImage_first.setOpaque(false);
                firstPreviousPanel.add(prevousImage_first);
            }

            BufferedImage img_second = null;
            try {
                img_second = ImageIO.read(new File(MyFnFSettings.LOGIN_USER_ID + SECOND_ARCHIEVE_IMAGE));
            } catch (IOException e) {
            }
            if (img_second != null) {
                //img_second = DesignClasses.scaleImage(90, 80, img_second);
                img_second = Scalr.resize(img_second, Scalr.Mode.FIT_EXACT, 95, 71, Scalr.OP_ANTIALIAS);
                JLabel prevousImage_second = new JLabel(new ImageIcon(img_second));
                prevousImage_second.setOpaque(false);
                secondPreviousPanel.add(prevousImage_second);
            }

        } catch (Exception ex) {

        }
    }

}
