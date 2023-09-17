/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.uploaddownload.CreateAlbumPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.service.utility.Scalr;
import com.ipvision.view.AddEmoticonsJDialog;
import com.ipvision.view.utility.ImageInformation;

/**
 *
 * @author Sirat Samyoun
 */
public class SingleChatImageForFeedPost extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SingleChatImageForFeedPost.class);
    private JPanel wrappperPanel, holder;
    public File file;
    private PreviewUploadingStatusImagePanel previewPanel;
    private JButton emotionButton;
    public AddEmoticonsJDialog addEmoticonsJDialog;
    private JTextArea textOnly;
    private CreateAlbumPanel createAlbum;
    private Long imageId;
    private String key;
    private static String default_caption = "Add a Caption";

    public SingleChatImageForFeedPost(File file, Long imageId, String key) {
        this.file = file;
        this.imageId = imageId;
        this.key = key;
        // this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        this.setBackground(Color.WHITE);
        this.wrappperPanel = new JPanel(new BorderLayout());
        this.wrappperPanel.setBorder(new MatteBorder(1, 1, 1, 1, DefaultSettings.APP_DEFAULT_MENUBAR_BG_COLOR));
        // this.add(this.wrappperPanel);
        this.initContent();
    }

    private void initContent() {
        setLayout(new BorderLayout());
        holder = new JPanel();
        holder.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        holder.setBackground(Color.WHITE);
        holder.add(wrappperPanel);
        add(holder, BorderLayout.CENTER);
        try {
            JLabel label = new JLabel();
            BufferedImage bufferImg = ImageIO.read(file);
            try {
                ImageInformation info = ImageInformation.readImageFileInformation(file);
                if (info.orientation > 1) {
                    AffineTransform transform = ImageInformation.getExifTransformation(info);
                    bufferImg = ImageInformation.transformImage(bufferImg, transform);
                }
            } catch (Exception e) {
                //  e.printStackTrace();
           log.error("Error in initContent ==>" + e.getMessage());
            }

            int mW = 650;
            int mH = 280;
            int oW = bufferImg.getWidth();
            int oH = bufferImg.getHeight();

            if (oW <= mW && oH <= mH) {
                label.setIcon(new ImageIcon(bufferImg));
            } else {
                if ((float) oW / (float) oH < (float) mW / (float) mH) {
                    label.setIcon(new ImageIcon(Scalr.resize(bufferImg, Scalr.Mode.FIT_TO_HEIGHT, 650, 280, Scalr.OP_ANTIALIAS)));
                } else {
                    label.setIcon(new ImageIcon(Scalr.resize(bufferImg, Scalr.Mode.FIT_TO_WIDTH, 650, 280, Scalr.OP_ANTIALIAS)));
                }

            }

            JButton removeLabel = DesignClasses.createImageButton(GetImages.BUTTON_CLOSE_MINI, GetImages.BUTTON_CLOSE_MINI_H, "Remove image");

            JPanel removePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            removePanel.setBorder(new EmptyBorder(3, 0, 0, 3));
            removePanel.setOpaque(false);
            removePanel.add(removeLabel);
            //wrappperPanel.add(removePanel, BorderLayout.NORTH);

            JPanel imgPanel = new JPanel(new GridBagLayout());
            imgPanel.setBorder(new EmptyBorder(5, 10, 10, 10));
            imgPanel.setOpaque(false);
            imgPanel.add(label);
            wrappperPanel.add(imgPanel, BorderLayout.CENTER);

            JPanel captionPanel = new JPanel(new BorderLayout());
            captionPanel.setMinimumSize(new Dimension(190, 32));
            wrappperPanel.add(captionPanel, BorderLayout.SOUTH);

            if (imageId != null && imageId.intValue() > 0) {
                captionPanel.setOpaque(false);
            } else {
                captionPanel.setOpaque(true);
                captionPanel.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
                captionPanel.setBorder(new MatteBorder(1, 0, 0, 0, DefaultSettings.APP_DEFAULT_MENUBAR_BG_COLOR));

                textOnly = DesignClasses.createJTextArea(default_caption, 400);
                textOnly.setColumns(15);
                textOnly.setForeground(DefaultSettings.disable_font_color);
                textOnly.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        if (textOnly.getText().equals(default_caption) || textOnly.getText().length() < 1) {
                            set_reset_defalut_text(textOnly, default_caption, false);
                        } else if (textOnly.getText().length() < 1) {
                            set_reset_defalut_text(textOnly, default_caption, true);
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        // if (textOnly.getText().equals(default_status) || textOnly.getText().length() < 1) {
                        //    set_reset_defalut_text(textOnly, default_status, false);
                        //} else if (textOnly.getText().length() < 1) {
                        set_reset_defalut_text(textOnly, default_caption, true);
                        // }
                    }
                });
//                JScrollPane editorScrollPane = DesignClasses.getDefaultScrollPaneThin(textOnly);
//                editorScrollPane.getViewport().setBackground(Color.WHITE);
                captionPanel.add(textOnly, BorderLayout.CENTER);

                emotionButton = DesignClasses.createImageButton(GetImages.EMOTICON, GetImages.EMOTICON_H, "Insert emoticon");
                emotionButton.setBorder(null);
                emotionButton.setBackground(Color.WHITE);
                emotionButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            int yPos = (int) emotionButton.getLocationOnScreen().getY();
                            int type_int = 1;
                            if (yPos > DefaultSettings.MONITOR_HALF_HEIGHT) {
                                type_int = 0;
                            }
                            //   if (addEmoticonsJDialog == null) {
                            addEmoticonsJDialog = new AddEmoticonsJDialog(textOnly, type_int);
                            addEmoticonsJDialog.setVisible(emotionButton);
                            //   } else {
                            //       addEmoticonsJDialog.setVisible(emotionButton);
                            //    }
                        } catch (Exception ed) {
                        }
                    }
                });

                JPanel buttonPanel = new JPanel(new GridBagLayout());
                buttonPanel.setBorder(new MatteBorder(0, 1, 0, 0, DefaultSettings.APP_DEFAULT_MENUBAR_BG_COLOR));
                buttonPanel.setOpaque(false);
                buttonPanel.setPreferredSize(new Dimension(30, 0));
                buttonPanel.add(emotionButton);
                captionPanel.add(buttonPanel, BorderLayout.EAST);
            }

            removeLabel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (previewPanel != null) {
                        previewPanel.successUploadedImageBlock(key);
                    } else if (createAlbum != null) {
                        createAlbum.removeSelectedImageBlock(key);
                        createAlbum.repaintImageContentPanel();
                    }
                }
            });
        } catch (IOException ex) {
        }
    }

    public void dispose_this() {
        if (addEmoticonsJDialog != null) {
            addEmoticonsJDialog.setVisible(false);
            addEmoticonsJDialog = null;
        }
        System.gc();
        Runtime.getRuntime().gc();
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
