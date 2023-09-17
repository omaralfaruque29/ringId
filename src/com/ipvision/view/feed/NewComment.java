/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.service.NewImageCommentRequest;
import com.ipvision.service.feed.FeedAddCommentRequest;
import com.ipvision.view.AddEmoticonsJDialog;
import com.ipvision.model.JsonFields;
import com.ipvision.view.utility.ContextMenuMouseListener;
import com.ipvision.view.image.BasicImageViewStructure;
import com.ipvision.view.utility.ImageHelpers;

/**
 *
 * @author Rabiul
 */
public class NewComment extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(NewComment.class);
    public JTextArea textOnly;
    private String default_status = "Write a comment...";
    //private JPanel commentWrintingPanel;
    private JPanel textPanel;
    private NewsFeedWithMultipleImage statusDto;
    private JsonFields imageDto;
    private int identity;
    public JButton addCommentButton;
    private JButton addEmoticon;
    private AddEmoticonsJDialog addEmoticonsJDialog;
    public static int FEED_COMMENT = 1;
    public static int IMAGE_COMMENT = 2;
    private int newCommentRows;
//    public static int FEED_COMMENT_EDIT =3;
//    public static int IMAGE_COMMENT_EDIT =4;

// this constructor comment for feed
    public NewComment(NewsFeedWithMultipleImage statusDto, int identity, int newCommentRows) {
        this.setLayout(new BorderLayout(0, 0));
        this.statusDto = statusDto;
        this.identity = identity;
        this.newCommentRows = newCommentRows;
        this.setOpaque(false);
        initContainers();

    }
// this constructor Comment For image 

    public NewComment(JsonFields imageDto, int identity) {
        this.setLayout(new BorderLayout(5, 5));
        this.imageDto = imageDto;
        this.identity = identity;
        this.setOpaque(false);
        initContainers();

    }

    private void initContainers() {
        try {

            JPanel imagePanel = new JPanel(new BorderLayout(0, 0));
            imagePanel.setOpaque(false);
            imagePanel.setPreferredSize(new Dimension(45, 0));
            add(imagePanel, BorderLayout.WEST);

            JPanel wrapperPanel = new JPanel(new BorderLayout());
            wrapperPanel.setOpaque(false);
            add(wrapperPanel, BorderLayout.CENTER);

            JPanel commentWrintingPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    int w = getWidth();
                    int h = getHeight();
                    g2d.setColor(Color.WHITE);
//                    g2d.fillRoundRect(1, 1, w - 2, h - 2, 35, 35);
                    g2d.fillRoundRect(1, 1, w - 2, h - 2, 32, 32);
                    BasicStroke stroke = new BasicStroke(1);
                    g2d.setColor(RingColorCode.ROUNDED_PANEL_BORDER_COLOR);
//                    g2d.drawRoundRect(0, 0, w - 1, h - 1, 35, 35);
                    g2d.drawRoundRect(0, 0, w - 1, h - 1, 32, 32);
                    g2d.setStroke(stroke);
                }
            };
//            commentWrintingPanel.setBorder(new EmptyBorder(0, 12, 4, 7));
            commentWrintingPanel.setBorder(new EmptyBorder(0, 12, 0, 8));
            commentWrintingPanel.setOpaque(false);
            commentWrintingPanel.setLayout(new BorderLayout());
            wrapperPanel.add(commentWrintingPanel);

            JPanel buttonPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                    g2d.setColor(Color.WHITE);
                    Ellipse2D.Double hole1 = new Ellipse2D.Double();
                    hole1.width = 33;
                    hole1.height = 33;
                    hole1.x = 1;
                    hole1.y = 1;
                    g2d.fill(hole1);

                    g2d.setColor(RingColorCode.ROUNDED_PANEL_BORDER_COLOR);
                    Ellipse2D.Double hole = new Ellipse2D.Double();
                    hole.width = 34;
                    hole.height = 34;
                    hole.x = 0;
                    hole.y = 0;
                    g2d.draw(hole);

                }
            };
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel.setOpaque(false);
            //buttonPanel.setPreferredSize(new Dimension(45, 0));

            JPanel buttonWrapper = new JPanel(new GridBagLayout());
            buttonWrapper.setOpaque(false);
            buttonWrapper.setPreferredSize(new Dimension(45, 0));
            buttonWrapper.add(buttonPanel);
            buttonWrapper.setBorder(new EmptyBorder(0, 7, 0, 0));

            add(buttonWrapper, BorderLayout.EAST);

            int imagesize = 35;

            JLabel friend_image_panel = new JLabel();
            friend_image_panel.setOpaque(false);
            friend_image_panel.setPreferredSize(new Dimension(imagesize, imagesize));
            imagePanel.add(friend_image_panel);

            if (MyFnFSettings.userProfile.getProfileImage() != null) {
                ImageHelpers.addProfileImageThumb(friend_image_panel, MyFnFSettings.userProfile.getProfileImage());
            } else {
                BufferedImage img2 = ImageHelpers.getUnknownImage(true);//.getBufferImageFromurl(null);
                friend_image_panel.setIcon(new ImageIcon(img2));
                img2 = null;
            }

            textPanel = new JPanel(new BorderLayout());
            textPanel.setOpaque(false);
//            textPanel.setBorder(new EmptyBorder(2, 0, 3, 0));
            textPanel.setBorder(new EmptyBorder(1, 0, 3, 0));

            textOnly = DesignClasses.createJTextArea(default_status, -1);
//            Border roundedBorder=new LineBorder(Color.WHITE, 3, true);
//            textOnly.setBorder(new EmptyBorder(10, 5, 3, 3));
            textOnly.setBorder(new EmptyBorder(9, 5, 7, 3));
            if (identity == FEED_COMMENT) {
                if (newCommentRows > -1) {
                    textOnly.setColumns(newCommentRows);
                } else {
                    textOnly.setColumns(50);
                }
            }
            if (identity == IMAGE_COMMENT) {
                textOnly.setColumns(23);
            }
            textOnly.setForeground(DefaultSettings.disable_font_color);
            textOnly.addMouseListener(new ContextMenuMouseListener());
            textOnly.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (textOnly.getText().equals(default_status) || textOnly.getText().length() < 1) {
                        set_reset_defalut_text(textOnly, default_status, false);
                    } else if (textOnly.getText().length() < 1) {
                        set_reset_defalut_text(textOnly, default_status, true);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (textOnly.getText().equals(default_status) || textOnly.getText().length() < 1) {
                        set_reset_defalut_text(textOnly, default_status, false);
                    } else if (textOnly.getText().length() < 1) {
                        set_reset_defalut_text(textOnly, default_status, true);
                    }
                }
            });
            textOnly.setRows(1);
            textOnly.setEditable(true);
            textOnly.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    /* if (textOnly.getText().length() > 400 || textOnly.getHeight() > 150) {
                     e.consume();
                     }*/
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER && (e.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
                            /*if (textOnly.getText().length() > 400 || textOnly.getHeight() > 150) {
                             e.consume();
                             }*/
                        } else if (textOnly.getText().trim().length() > 0) {
                            e.consume();
                            if (addCommentButton != null) {
                                addCommentButton.doClick();
                            }
//                            JsonFields singleComment = new JsonFields();
//                            singleComment.setFullName(MyFnFSettings.userProfile.getFullName());
//                            singleComment.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
//                            singleComment.setComment(textOnly.getText());
//                            singleComment.setTm(System.currentTimeMillis());
//                            singleComment.setNfId(statusDto.getNfId());
//                            new FeedAddCommentRequest(singleComment).start();
//                            textOnly.setText("");
                        } else if (textOnly.getText().length() > 0) {
                            e.consume();
                            textOnly.setText("");
                        } else {
                            e.consume();
                        }
                        textOnly.grabFocus();

                    }
                    if (e.getKeyCode() == KeyEvent.VK_ENTER && (e.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
                        textOnly.append("\n");
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }
            });

            /* JScrollPane scroll = DesignClasses.getDefaultScrollPaneThin(new JScrollPane(textOnly) {
             @Override
             public void doLayout() {
             super.doLayout();
             int minHeight = 30;
             int maxHeight = 150;
             int prefHeight;

             if (textOnly.getHeight() > maxHeight) {
             if (!this.getVerticalScrollBar().isVisible()) {
             this.getVerticalScrollBar().setVisible(true);
             }
             prefHeight = maxHeight + 2 + 1;
             } else {
             if (this.getVerticalScrollBar().isVisible()) {
             this.getVerticalScrollBar().setVisible(false);
             }
             if (textOnly.getHeight() < minHeight) {
             prefHeight = minHeight + 2 + 1;
             } else {
             prefHeight = textOnly.getHeight() + 2 + 1;
             }
             }

             textPanel.setPreferredSize(new Dimension(textPanel.getWidth(), prefHeight));
             textPanel.revalidate();
             // System.err.println(commentWrintingPanel.getHeight()  + "," + textOnly.getHeight());

             }
             });

             textPanel.add(scroll, BorderLayout.CENTER);*/
            textPanel.add(textOnly, BorderLayout.CENTER);
            commentWrintingPanel.add(textPanel, BorderLayout.CENTER);

            addEmoticon = DesignClasses.createImageButton(GetImages.COMMENT_EMOTICON, GetImages.COMMENT_EMOTICON_H, "Insert emoticon");
            addEmoticon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (identity == FEED_COMMENT) {
                            emoticonJDialogActionForFeed();
                        }
                        if (identity == IMAGE_COMMENT) {
                            emoticonJDialogActionForImage();
                        }
                        //else System.err.println("error adding Jdialog emoticon");
                    } catch (Exception ed) {
                    }
                }
            });
            commentWrintingPanel.add(addEmoticon, BorderLayout.EAST);
            //add(status_name_Panel, BorderLayout.CENTER);
            addCommentButton = DesignClasses.createImageButton(GetImages.BUTTON_ADD_COMMENT, GetImages.BUTTON_ADD_COMMENT_H, "Post this comment");
            addCommentButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (textOnly.getText().trim().length() > 0 && !textOnly.getText().equals(default_status)) {
                        if (identity == FEED_COMMENT) {
                            submitButtonAction();
                        }
                        if (identity == IMAGE_COMMENT) {
                            submitButtonActionImage();
                        }
//                            else {
//                            System.err.println(" some problem in adding comment");
//                        }
//                        JsonFields singleComment = new JsonFields();
//                        singleComment.setFullName(MyFnFSettings.userProfile.getFullName());
//                        //singleComment.setLastName(MyFnFSettings.userProfile.getLastName());
//                        singleComment.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
//                        singleComment.setComment(textOnly.getText());
//                        singleComment.setTm(System.currentTimeMillis());
//                        singleComment.setNfId(statusDto.getNfId());
//                        singleComment.setUt(statusDto.getUt());
//                        new FeedAddCommentRequest(singleComment).start();
//                        textOnly.setText("");
                    }
                    textOnly.grabFocus();
                }
            });
            buttonPanel.add(addCommentButton);

        } catch (Exception e) {
           // e.printStackTrace();
        log.error("Error in here ==>" + e.getMessage());
        }
    }

    private void emoticonJDialogActionForFeed() {
        int yPos = (int) getLocationOnScreen().getY();
        int type_int = 1;
        if (yPos > DefaultSettings.MONITOR_HALF_HEIGHT) {
            type_int = 0;
        }
        addEmoticonsJDialog = new AddEmoticonsJDialog(textOnly, type_int);
        addEmoticonsJDialog.setVisible(addEmoticon);

    }

    private void emoticonJDialogActionForImage() {

        BasicImageViewStructure.addEmoticonsJDialog = new AddEmoticonsJDialog(textOnly, 0);
        BasicImageViewStructure.addEmoticonsJDialog.setVisible(addEmoticon);
    }

    private void submitButtonAction() {
        JsonFields singleComment = new JsonFields();
        singleComment.setFullName(MyFnFSettings.userProfile.getFullName());
        //singleComment.setLastName(MyFnFSettings.userProfile.getLastName());
        singleComment.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
        singleComment.setComment(textOnly.getText());
        singleComment.setTm(System.currentTimeMillis());
        singleComment.setNfId(statusDto.getNfId());
        singleComment.setUt(statusDto.getUt());
        new FeedAddCommentRequest(singleComment).start();
        textOnly.setText("");
    }

    private void submitButtonActionImage() {
        JsonFields singleComment = new JsonFields();
        singleComment.setFullName(MyFnFSettings.userProfile.getFullName());
        singleComment.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
        singleComment.setComment(textOnly.getText());
        singleComment.setTm(System.currentTimeMillis());
        singleComment.setImageId(imageDto.getImageId());
        new NewImageCommentRequest(singleComment).start();
        textOnly.setText("");
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

}
