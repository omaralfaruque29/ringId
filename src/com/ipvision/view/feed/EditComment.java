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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import com.ipvision.service.feed.FeedEditCommentRequest;
import com.ipvision.service.feed.FeedEditImageCommentRequest;
import com.ipvision.view.AddEmoticonsJDialog;
import com.ipvision.model.JsonFields;
import com.ipvision.view.image.BasicImageViewStructure;
import com.ipvision.view.image.SingeImageCommentDetails;

/**
 *
 * @author Rabiul
 */
public class EditComment extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EditComment.class);
    public JTextArea textOnly;
    private JsonFields commentDto;
    private String previsou_text;
    private SingleCommentView singleCommentView;
    private SingeImageCommentDetails singeImageCommentDetails;
    private NewComment newComment;
    private JButton addEmoticon;
    public static int FEED_COMMENT_EDIT = 3;
    public static int IMAGE_COMMENT_EDIT = 4;
    private int type;
    private static EditComment instance;

    public static EditComment getInstance() {
        return instance;
    }

    public EditComment(SingleCommentView singleCommentView, String previous_text) {
        this.instance = this;
        this.setLayout(new BorderLayout(0, 0));
        this.previsou_text = previous_text;
        this.singleCommentView = singleCommentView;
        this.commentDto = this.singleCommentView.commentDto;
        this.type = FEED_COMMENT_EDIT;
        this.newComment = this.singleCommentView.singleFeedStructure.newComment;
        this.setOpaque(false);
        initContainers();
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                grabTextBoxFocus();
            }
        });
    }

    public EditComment(SingeImageCommentDetails singeImageCommentDetails, String previous_text) {
        this.instance = this;
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(false);
        this.previsou_text = previous_text;
        this.singeImageCommentDetails = singeImageCommentDetails;
        this.commentDto = this.singeImageCommentDetails.commentDto;
        this.type = IMAGE_COMMENT_EDIT;
        initContainers();
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                grabTextBoxFocus();
            }
        });

    }

    public void grabTextBoxFocus() {
        textOnly.grabFocus();
        int len = textOnly.getDocument().getLength();
        textOnly.setCaretPosition(len);
    }

    public void hidePreviousEditPanels() {
        if (singleCommentView != null) {
            singleCommentView.changeComment();
        }
        if (singeImageCommentDetails != null) {
            singeImageCommentDetails.changeComment();
        }
    }

    private void initContainers() {
        try {

            JPanel wrapperPanel = new JPanel(new GridBagLayout());
            wrapperPanel.setOpaque(false);
            add(wrapperPanel, BorderLayout.CENTER);

            JPanel status_name_Panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    int w = getWidth();
                    int h = getHeight();
                    g2d.setColor(Color.WHITE);
                    g2d.fillRoundRect(1, 1, w - 2, h - 20, 30, 30);
                    BasicStroke stroke = new BasicStroke(1);
                    g2d.setColor(RingColorCode.ROUNDED_PANEL_BORDER_COLOR);
                    g2d.drawRoundRect(0, 0, w - 1, h - 20, 30, 30);
                    g2d.setStroke(stroke);
                }
            };
            status_name_Panel.setForeground(DefaultSettings.DEFAULT_FONT_COLOR);
            status_name_Panel.setBackground(Color.WHITE);
            status_name_Panel.setBorder(new EmptyBorder(1, 12, 1, 7));
            status_name_Panel.setBackground(Color.GREEN);
            status_name_Panel.setOpaque(false);
            status_name_Panel.setLayout(new BorderLayout(5, 0));
            wrapperPanel.add(status_name_Panel);
            textOnly = DesignClasses.createJTextArea(previsou_text, -1);
            textOnly.setRows(1);
            textOnly.setEditable(true);
            textOnly.setBorder(new EmptyBorder(9, 5, 7, 3));
            textOnly.setColumns(21);

            textOnly.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER && (e.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
                        textOnly.append("\n");
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (type == FEED_COMMENT_EDIT && textOnly.getText().trim().length() > 0) {
                            e.consume();
                            commentDto.setComment(textOnly.getText());
//                            new FeedEditCommentRequest(commentDto).start();
                            new FeedEditCommentRequest(commentDto, newComment).start();
                            textOnly.setText("");
                            textOnly.setEditable(false);
//                            if (NewComment.getInstance() != null && NewComment.getInstance().isDisplayable()) {
//                                NewComment.getInstance().setVisible(true);
//                            }
                        } else if (type == IMAGE_COMMENT_EDIT && textOnly.getText().trim().length() > 0) {
                            JsonFields edited = new JsonFields();
                            edited.setFullName(MyFnFSettings.userProfile.getFullName());
                            edited.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
                            edited.setComment(textOnly.getText());
                            edited.setImageId(commentDto.getImageId());
                            edited.setCmnId(commentDto.getCmnId());
                            new FeedEditImageCommentRequest(singeImageCommentDetails, edited).start();
                            textOnly.setText("");
                            textOnly.setEditable(false);
                        } else {
                            e.consume();

                        }
                        textOnly.grabFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        if (type == FEED_COMMENT_EDIT) {
                            singleCommentView.changeComment();
                            if (newComment != null && !newComment.isVisible()) {
                                newComment.setVisible(true);
                            }
                        } else if (type == IMAGE_COMMENT_EDIT) {
                            singeImageCommentDetails.changeComment();
                        }

                    }

                }

                @Override
                public void keyReleased(KeyEvent e) {
                }
            });

            status_name_Panel.add(textOnly, BorderLayout.CENTER);
            JLabel pressEsc = DesignClasses.makeJLabelUnderFullName("Press Esc to cancel.");
            pressEsc.setBorder(new EmptyBorder(5, 0, 0, 0));
            status_name_Panel.add(pressEsc, BorderLayout.SOUTH);

            addEmoticon = DesignClasses.createImageButton(GetImages.COMMENT_EMOTICON, GetImages.COMMENT_EMOTICON_H, "Insert emoticon");
            addEmoticon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
//                        int yPos = (int) getLocationOnScreen().getY();
//                        System.out.println(yPos);
//                        int monitorHalfY = 440;//(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2;
//                        if (yPos <= monitorHalfY) {
//                            if (addEmoticonsJDialog == null) {
//                                addEmoticonsJDialog = new AddEmoticonsJDialog(textOnly);
//                                addEmoticonsJDialog.setVisible(addEmoticon);
//                            } else {
//                                addEmoticonsJDialog.setVisible(addEmoticon);
//                            }
//                        } else {
//                            if (addEmoticonsTopJDialog == null) {
//                                addEmoticonsTopJDialog = new AddEmoticonsTopJDialog(textOnly);
//                                addEmoticonsTopJDialog.setVisible(addEmoticon);
//                            } else {
//                                addEmoticonsTopJDialog.setVisible(addEmoticon);
//                            }
//                        }

                        int yPos = (int) getLocationOnScreen().getY();
                        int type_int = 1;
                        if (yPos > DefaultSettings.MONITOR_HALF_HEIGHT) {
                            type_int = 0;
                        }
                        BasicImageViewStructure.addEmoticonsJDialog = new AddEmoticonsJDialog(textOnly, type_int);
                        BasicImageViewStructure.addEmoticonsJDialog.setVisible(addEmoticon);

//                        //    if (addEmoticonsJDialog == null) {
//                        addEmoticonsJDialog = new AddEmoticonsJDialog(textOnly, type_int);
//                        addEmoticonsJDialog.setVisible(addEmoticon);
                        //    } else {
                        //       addEmoticonsJDialog.setVisible(addEmoticon);
                        //   }
                    } catch (Exception ed) {
                        // ed.printStackTrace();
                        log.error("Error in here ==>" + ed.getMessage());
                    }

                }
            });
            status_name_Panel.add(addEmoticon, BorderLayout.EAST);
            add(status_name_Panel);

        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error in here ==>" + e.getMessage());
        }

    }

}
