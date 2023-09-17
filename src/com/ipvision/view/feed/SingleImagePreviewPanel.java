/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.uploaddownload.CreateAlbumPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.service.utility.Scalr;
import com.ipvision.view.AddEmoticonsJDialog;
import com.ipvision.view.utility.ContextMenuMouseListener;
import com.ipvision.view.utility.ImageInformation;
import com.ipvision.view.utility.ImagePane;
import sun.security.ec.ECPrivateKeyImpl;

/**
 *
 * @author Shahadat Hossain
 */
public class SingleImagePreviewPanel extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SingleImagePreviewPanel.class);
    public JPanel wrappperPanel;
    public File file;
    private String caption;
    private PreviewUploadingStatusImagePanel previewPanel;
    public JButton emotionButton;
    private AddEmoticonsJDialog addEmoticonsJDialog;
    public JTextArea textOnly;
    private CreateAlbumPanel createAlbum;
    private Long imageId;
    private String key;
    public static String DEFAULT_CAPTION = "Photo Caption...";
    public JPanel imgPanel;
    public JPanel removePanel;
    public JPanel uploadSuccessPanel;
    public ImagePane imagePaneMain;
    private int previewWidth = 155;
    private int previewHeight = 145;
    private int wrapperPanelWidth = 170;
    private int wrapperPanelHeight = 160;
    private BufferedImage bufferImg;
    private JLabel closeButton = DesignClasses.create_imageJlabel(GetImages.CROSS);
    private static SingleImagePreviewPanel instance;

    public static SingleImagePreviewPanel getInstance() {
        return instance;
    }

    public SingleImagePreviewPanel(PreviewUploadingStatusImagePanel previewPanel, File file, String caption, Long imageId, String key) {
        this.instance = this;
        this.file = file;
        this.previewPanel = previewPanel;
        this.caption = caption;
        this.imageId = imageId;
        this.key = key;
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        this.setOpaque(false);
        this.wrappperPanel = new JPanel(new BorderLayout(0, 0));
        this.wrappperPanel.setBackground(Color.WHITE);
        this.wrappperPanel.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR));
        //this.wrappperPanel.setPreferredSize(new Dimension(wrapperPanelWidth, wrapperPanelHeight));
        this.add(this.wrappperPanel);
        //this.add(Box.createHorizontalStrut(10));
        if (imageId != null && imageId.intValue() > 0) {
            this.setBorder(new EmptyBorder(0, 0, 35, 10));
        } else {
            this.setBorder(new EmptyBorder(0, 0, 10, 10));
        }
        this.initContent();
        if (this.previewPanel != null) {
            this.previewPanel.revalidate();
        }
    }

    /* public SingleImagePreviewPanel(File file, String caption, Long imageId, String key) {
     this.file = file;
     this.caption = caption;
     this.imageId = imageId;
     this.key = key;
     this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
     this.setBackground(Color.WHITE);
     this.wrappperPanel = new JPanel(new BorderLayout(0, 0));
     // this.wrappperPanel.setBorder(new MatteBorder(1, 1, 1, 1, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR));
     this.add(this.wrappperPanel);
     this.initContent();
     }*/
    public SingleImagePreviewPanel(CreateAlbumPanel createAlbum, File file, String key) {
        this.instance = this;
        this.file = file;
        this.createAlbum = createAlbum;
        this.key = key;
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        this.setOpaque(false);
        this.wrappperPanel = new JPanel(new BorderLayout());
        this.wrappperPanel.setBackground(Color.WHITE);
        this.wrappperPanel.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR));
        this.wrappperPanel.setPreferredSize(new Dimension(wrapperPanelWidth, wrapperPanelHeight));
        this.add(this.wrappperPanel);
        //this.setBorder(new EmptyBorder(0, 0, 10, 10));
        //this.add(Box.createHorizontalStrut(10));
        this.initContent();
    }

    private void initContent() {
        try {
            imagePaneMain = new ImagePane();
            imagePaneMain.setLayout(new BorderLayout(0, 0));
            imagePaneMain.setBackground(Color.WHITE);

            bufferImg = ImageIO.read(file);
            try {
                ImageInformation info = ImageInformation.readImageFileInformation(file);
                if (info.orientation > 1) {
                    AffineTransform transform = ImageInformation.getExifTransformation(info);
                    bufferImg = ImageInformation.transformImage(bufferImg, transform);
                }
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Error in initContainers ==>" + e.getMessage());
                bufferImg.flush();
                bufferImg = null;
            }
//            label.setIcon(new ImageIcon(Scalr.resize(bufferImg, Scalr.Mode.AUTOMATIC, 170, 170, Scalr.OP_ANTIALIAS)));
            bufferImg = Scalr.resize(bufferImg, Scalr.Mode.AUTOMATIC, previewWidth, previewHeight, Scalr.OP_ANTIALIAS);
            imagePaneMain.setImage(bufferImg, previewWidth, previewHeight);

            // bufferImg = ImageUtil.reduceQualityAndSize(bufferImg, previewWidth);
            removePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(0, 0, 0, 80));
                    int w = getWidth();
                    int h = getHeight();
                    g2d.fillRect(0, 0, w, h);
                }
            };
            removePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            removePanel.setBorder(new EmptyBorder(1, 0, 0, 1));
            removePanel.setOpaque(false);
            removePanel.setPreferredSize(new Dimension(0, 50));
            removePanel.setVisible(false);

            imgPanel = new JPanel(new BorderLayout(0, 0));
            imgPanel.setBackground(RingColorCode.DEFAULT_BACKGROUND_COLOR);
            imgPanel.setPreferredSize(new Dimension(previewWidth + 5, previewHeight + 5));
            imgPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

            wrappperPanel.add(imgPanel, BorderLayout.CENTER);
            imgPanel.add(imagePaneMain, BorderLayout.CENTER);
            imagePaneMain.add(removePanel, BorderLayout.SOUTH);
            imagePaneMain.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (NewStatus.getInstance().postButton.isEnabled()) {
                        removePanel.setVisible(true);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (NewStatus.getInstance().postButton.isEnabled()) {
                        removePanel.setVisible(false);
                    }
                }
            });
            removePanel.add(closeButton);
            closeButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (previewPanel != null) {
                        previewPanel.removeSelectedImageBlock(key);
                    } else if (createAlbum != null) {
                        createAlbum.removeSelectedImageBlock(key);
                        createAlbum.repaintImageContentPanel();
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    closeButton.setIcon(DesignClasses.return_image(GetImages.CROSS_H));
                    closeButton.setToolTipText("Remove Image");
                    closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    removePanel.setVisible(true);

                }

                @Override
                public void mouseExited(MouseEvent e) {
                    closeButton.setIcon(DesignClasses.return_image(GetImages.CROSS));
                    // removePanel.setVisible(true);
                }
            });

            uploadSuccessPanel = new JPanel();
            uploadSuccessPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            uploadSuccessPanel.setOpaque(false);
            uploadSuccessPanel.setPreferredSize(new Dimension(0, 30));
            uploadSuccessPanel.setVisible(false);

            JLabel uploadSuccessLabel = new JLabel();
            uploadSuccessLabel.setIcon(DesignClasses.return_image(GetImages.LOADED_IMAGE_SUCCESSFULLY));
            uploadSuccessPanel.add(uploadSuccessLabel);

            imagePaneMain.add(uploadSuccessPanel, BorderLayout.NORTH);

            JPanel captionPanel = new JPanel(new BorderLayout(0, 0));
            captionPanel.setOpaque(false);
            wrappperPanel.add(captionPanel, BorderLayout.SOUTH);

            if (imageId != null && imageId.intValue() > 0) {
                captionPanel.setVisible(false);
            } else {

                JPanel textPanel = new JPanel(new BorderLayout(0, 0));
                textPanel.setOpaque(false);

                textOnly = DesignClasses.createJTextArea(DEFAULT_CAPTION, 250);
                textOnly.setOpaque(false);
                textOnly.setFont(new Font(DEFAULT_CAPTION, Font.PLAIN, 11));
                textOnly.setBorder(new EmptyBorder(6, 5, 0, 2));
                textOnly.setForeground(DefaultSettings.disable_font_color);
                textOnly.addMouseListener(new ContextMenuMouseListener());
                textOnly.setEditable(true);
                textOnly.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        if (textOnly.getText().equals(DEFAULT_CAPTION) || textOnly.getText().length() < 1) {
                            set_reset_defalut_text(textOnly, DEFAULT_CAPTION, false);
                        } else if (textOnly.getText().length() < 1) {
                            set_reset_defalut_text(textOnly, DEFAULT_CAPTION, true);
                        }

                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        if (textOnly.getText().equals(DEFAULT_CAPTION) || textOnly.getText().length() < 1) {
                            set_reset_defalut_text(textOnly, DEFAULT_CAPTION, false);
                        } else if (textOnly.getText().length() < 1) {
                            set_reset_defalut_text(textOnly, DEFAULT_CAPTION, true);
                        }

                    }

                });
                textOnly.addKeyListener(new KeyListener() {

                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (previewPanel != null) {
                            previewPanel.revalidate();
                        }
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (previewPanel != null) {
                            previewPanel.revalidate();
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        if (previewPanel != null) {
                            previewPanel.revalidate();
                        }
                    }
                });

                textPanel.add(textOnly);
                JPanel textArearAndEmoticon = new JPanel(new BorderLayout(0, 0));
                textArearAndEmoticon.setOpaque(false);
                textArearAndEmoticon.add(textPanel, BorderLayout.CENTER);
                captionPanel.add(textArearAndEmoticon, BorderLayout.CENTER);

                emotionButton = DesignClasses.createImageButton(GetImages.EMOTICON, GetImages.EMOTICON_H, "Insert emoticon");
                emotionButton.setBorder(new EmptyBorder(1, 0, 0, 0));
                emotionButton.setOpaque(false);
                emotionButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            int yPos = (int) emotionButton.getLocationOnScreen().getY();
                            int type_int = 1;
                            if (yPos > DefaultSettings.MONITOR_HALF_HEIGHT) {
                                type_int = 0;
                            }
                            addEmoticonsJDialog = new AddEmoticonsJDialog(textOnly, type_int);
                            addEmoticonsJDialog.setVisible(emotionButton);

                        } catch (Exception ex) {
                            //  ex.printStackTrace();
                            log.error("Error in initContainers ==>" + ex.getMessage());
                        }
                    }
                });

                textArearAndEmoticon.add(emotionButton, BorderLayout.EAST);
                /*if (imageId != null && imageId.intValue() > 0) {
                 textOnly.setEditable(false);
                 emotionButton.setEnabled(false);
                 }*/
            }
        } catch (IOException ex) {
            //  ex.printStackTrace();
            log.error("Error in initContainers ==>" + ex.getMessage());
            bufferImg.flush();
            bufferImg = null;
        } finally {
            System.gc();
            bufferImg.flush();
            bufferImg = null;
        }
    }

    private void set_reset_defalut_text(JTextArea textfield, String default_text, Boolean status) {
        if (status) {
            if (textfield.getText().length() < 1) {
                textfield.setText(default_text);
                textfield.setForeground(DefaultSettings.disable_font_color);
            }
        } else {
            textfield.setText("");
            textfield.setForeground(null);
        }
    }

    public String getCaption() {
        return textOnly != null ? textOnly.getText() : "";
    }

//    public static void main(String[] args){
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (ClassNotFoundException | InstantiationException |
//                IllegalAccessException | UnsupportedLookAndFeelException ex) {
//            ex.printStackTrace();
//        }
//        JFrame frame = new JFrame("RoundButton");
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.setSize(new Dimension(400, 400));
//        frame.setMinimumSize(new Dimension(400, 400));
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//        
//        JPanel panel = (JPanel) frame.getContentPane();
//        panel.setPreferredSize(new Dimension(400, 400));
//        panel.setBackground(Color.GRAY);
//        panel.setLayout(new GridBagLayout());
//
//        File f = new File(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER + File.separator + "1406197229892.png");
//        SingleImagePreviewPanel panelasd = new SingleImagePreviewPanel(null, f, null);
//        
//        panel.add(panelasd);
//        panel.validate();
//        panel.repaint();
//    }
}
