/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.view.utility.DefaultSettings;
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
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.service.feed.FeedEditStatusRequest;
import com.ipvision.view.AddEmoticonsJDialog;

/**
 *
 * @author Wasif Islam
 */
public class FeedEditStatus extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FeedEditStatus.class);
    private JTextArea textOnly;
    // private JPanel textPanel;
    private NewsFeedWithMultipleImage statusDto;
    private AddEmoticonsJDialog addEmoticonsJDialog;
    private String previous_status_or_caption;
    private SingleFeedStructure singleFeedStructure;
    private boolean isStatus;
    JButton addEmoticon;
    private static FeedEditStatus instance;

    public static FeedEditStatus getInstance() {
        return instance;
    }

    public FeedEditStatus(SingleFeedStructure singleFeedStructure, String previous_status_or_caption, boolean status_or_caption) {
        this.instance = this;
        this.setLayout(new BorderLayout(5, 5));
        this.singleFeedStructure = singleFeedStructure;
        this.statusDto = singleFeedStructure.statusDto;
        this.previous_status_or_caption = previous_status_or_caption;
        this.isStatus = status_or_caption;
        this.setOpaque(false);
        initContainers();
    }

    public void hidePreviousAddStatus() {
        if (singleFeedStructure != null) {
            singleFeedStructure.addStatusAndLocationInfor();
        }
    }

    private void initContainers() {

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
                g2d.fillRoundRect(1, 1, w - 2, h - 18, 34, 34);
                BasicStroke stroke = new BasicStroke(1);
                g2d.setColor(RingColorCode.ROUNDED_PANEL_BORDER_COLOR);
                g2d.drawRoundRect(0, 0, w - 1, h - 18, 34, 34);
                g2d.setStroke(stroke);
            }
        };
        // textPanel.setBorder(new EmptyBorder(3, 2, 3, 5));
        // textPanel.setOpaque(false);
        status_name_Panel.setForeground(DefaultSettings.DEFAULT_FONT_COLOR);
        status_name_Panel.setBackground(Color.GREEN);
        status_name_Panel.setBorder(new EmptyBorder(1, 12, 0, 7));
        status_name_Panel.setOpaque(false);
        status_name_Panel.setLayout(new BorderLayout(5, 0));
        wrapperPanel.add(status_name_Panel);

        try {
            //JPanel status_name_Panel = new JPanel(new BorderLayout(0, 0));
            status_name_Panel.setForeground(DefaultSettings.DEFAULT_FONT_COLOR);

            if (isStatus) {
                textOnly = DesignClasses.createJTextArea(previous_status_or_caption, -1);
            } else {
                textOnly = DesignClasses.createJTextArea(previous_status_or_caption, -1);
            }
            textOnly.setRows(1);
            textOnly.setEditable(true);
            textOnly.setBorder(new EmptyBorder(11, 5, 2, 3));
            textOnly.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    /*if (textOnly.getText().length() > 400 || textOnly.getHeight() > 150) {
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
                            if (isStatus) {
                                statusDto.setStatus(textOnly.getText());
                            } else {
                                statusDto.setCptn(textOnly.getText());
                            }
                            statusDto.setNfId(statusDto.getNfId());
                            new FeedEditStatusRequest(statusDto).start();
                            textOnly.setText("");
//                            textOnly.setEditable(false);
                            textOnly.setEditable(true);
                        } else if (textOnly.getText().length() > 0) {
                            e.consume();
                            textOnly.setText("");
                        } else {
                            e.consume();
                        }
                        textOnly.grabFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        if (singleFeedStructure != null) {
                            singleFeedStructure.addStatusAndLocationInfor();
                        }
                    }
                    if (e.getKeyCode() == KeyEvent.VK_ENTER && (e.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
                        textOnly.append("\n");
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }
            });
            //textPanel.add(textOnly);
            status_name_Panel.add(textOnly, BorderLayout.CENTER);
            JLabel pressEsc = DesignClasses.makeJLabelUnderFullName("Press Esc to cancel.");
            pressEsc.setBorder(new EmptyBorder(7, 0, 0, 0));
            status_name_Panel.add(pressEsc, BorderLayout.SOUTH);

            addEmoticon = DesignClasses.createImageButton(GetImages.COMMENT_EMOTICON, GetImages.COMMENT_EMOTICON_H, "Add Emotiocon");
            addEmoticon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        int yPos = (int) getLocationOnScreen().getY();
                        int type_int = 1;
                        if (yPos > DefaultSettings.MONITOR_HALF_HEIGHT) {
                            type_int = 0;
                        }
                        //  if (addEmoticonsJDialog == null) {
                        addEmoticonsJDialog = new AddEmoticonsJDialog(textOnly, type_int);
                        addEmoticonsJDialog.setVisible(addEmoticon);
                        //   } else {
                        //       addEmoticonsJDialog.setVisible(addEmoticon);
                        //   }
                    } catch (Exception ed) {
                    }
                }
            });
            status_name_Panel.add(addEmoticon, BorderLayout.EAST);
            add(status_name_Panel);

        } catch (Exception e) {
          //  e.printStackTrace();
        log.error("Error in here ==>" + e.getMessage());
        }

    }

    public void grabTextBoxFocus() {
        textOnly.grabFocus();
        int len = textOnly.getDocument().getLength();
        textOnly.setCaretPosition(len);

        /* JScrollPane scroll = DesignClasses.getDefaultScrollPaneThin(new JScrollPane(textOnly) {
         @Override
         public void doLayout() {
         super.doLayout();
         int minHeight = 48;
         int maxHeight = 220;
         int prefHeight;

         if (textOnly.getHeight() > maxHeight) {
         if (!this.getVerticalScrollBar().isVisible()) {
         this.getVerticalScrollBar().setVisible(true);
         }
         prefHeight = maxHeight + 5 + 4;
         } else {
         if (this.getVerticalScrollBar().isVisible()) {
         this.getVerticalScrollBar().setVisible(false);
         }
         if (textOnly.getHeight() < minHeight) {
         prefHeight = minHeight + 5 + 4;
         } else {
         prefHeight = textOnly.getHeight() + 5 + 4;
         }
         }

         textPanel.setPreferredSize(new Dimension(textOnly.getWidth(), prefHeight));
         textPanel.revalidate();

         }
         });
         textPanel.add(scroll);*/
        //textPanel.add(textOnly);
        // textPanel.revalidate();
        //textPanel.repaint();
    }

}
