/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import javax.swing.border.EmptyBorder;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.service.feed.FeedShareStatusRequest;
import com.ipvision.view.AddEmoticonsJDialog;
import com.ipvision.constants.XMLConstants;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.view.utility.ContextMenuMouseListener;
import com.ipvision.view.utility.ImagePane;
import com.ipvision.view.utility.RoundedCornerButton;

/**
 *
 * @author Faiz
 */
public class ShareThisFeedPopUp extends JDialog {

    private NewsFeedWithMultipleImage statusDto;
    private SingleShareStatus sin;
    private JsonFields ob;
    private ImagePane content;
    private JPanel feedHere;
    public JPanel textAreaAndEmoticonPanel;
    public JTextArea textOnly;
    private JPanel textPanel;
    private String default_status = "Write something...";
    private AddEmoticonsJDialog addEmoticonsJDialog;
    JButton addEmoticon;
    private JButton closeButton;
    private Pattern reqular_expresion;
    public static boolean isClieckedOrPress;
    private JLabel loadingLabel;
    private int posX = 0;
    private int posY = 0;
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ShareThisFeedPopUp.class);
    private static ShareThisFeedPopUp instance;

    public static ShareThisFeedPopUp getInstance() {
        return instance;
    }

    public ShareThisFeedPopUp(Long nfId) {
        instance = this;
        if (XMLConstants.EMOTION_REGULAR_EXPRESSION != null) {
            reqular_expresion = Pattern.compile(XMLConstants.EMOTION_REGULAR_EXPRESSION);
        }
        this.statusDto = NewsFeedMaps.getInstance().getAllNewsFeeds().get(nfId);
        loadingLabel = DesignClasses.loadingLable(true);
        //setMinimumSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH , DefaultSettings.MAIN_RIGHT_CONTENT_HEIGHT ));
        setMinimumSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH + 55, DefaultSettings.MAIN_RIGHT_CONTENT_HEIGHT + 10));
        setLocationRelativeTo(null);
        setUndecorated(true);
        // setModal(true);
        setBackground(new Color(0, 0, 0, 0));
        content = new ImagePane();

        int margin = 11;
        content.setBorder(new EmptyBorder(margin, margin, margin, margin));
        try {
            BufferedImage bImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.SCREEN_SHARE_POPUP));
            content.setImage(bImg);
            bImg.flush();
            bImg = null;
        } catch (IOException ex) {
            log.error("Buffer img error" + ex.getMessage());
        }
        content.setLayout(new BorderLayout(0, 3));
        content.setBackground(new Color(0, 0, 0, 0));
        // content.setBorder(BorderFactory.createLineBorder(DefaultSettings.ORANGE_BACKGROUND_COLOR));
        content.addMouseListener(poxValueListener);
        content.addMouseMotionListener(frameDragListener);
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(5, 10, 5, 5));
        headerPanel.setOpaque(false);
        //  headerPanel.setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);
        JLabel title = DesignClasses.makeJLabel("Share this post", 15, 1, Color.BLACK, JLabel.LEFT);
        headerPanel.add(title, BorderLayout.WEST);

        closeButton = DesignClasses.createImageButton(GetImages.BUTTON_CLOSE_MINI, GetImages.BUTTON_CLOSE_MINI_H, GetImages.BUTTON_CLOSE_MINI_H, "Cancel");
        closeButton.addActionListener(actionListener);
        headerPanel.add(closeButton, BorderLayout.EAST);
        content.add(headerPanel, BorderLayout.NORTH);
        feedHere = new JPanel();
        feedHere.setLayout(new BorderLayout(0, 3));
        feedHere.setBorder(new EmptyBorder(5, 5, 5, 5));
        feedHere.setOpaque(false);
        //feedHere.setBorder(new EmptyBorder(5, 1, 5, 1));
        // feedHere.setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);
        feedHere.addMouseListener(poxValueListener);
        feedHere.addMouseMotionListener(frameDragListener);
        content.add(feedHere, BorderLayout.CENTER);
        // textAreaAndEmoticonPanel = new JPanel(new BorderLayout(0, 0));
        textAreaAndEmoticonPanel = new JPanel(new BorderLayout(0, 0));
        textAreaAndEmoticonPanel.setBorder(new EmptyBorder(0, 5, 5, 5));
        textAreaAndEmoticonPanel.setBackground(Color.WHITE);
        feedHere.add(textAreaAndEmoticonPanel, BorderLayout.NORTH);
        this.setContentPane(content);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                if (addEmoticonsJDialog != null && addEmoticonsJDialog.isDisplayable()) {
                    return;
                }
//                if(isClieckedOrPress == false){
                dispose_this();
//                }
            }
        });
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

    public ShareThisFeedPopUp getShareThisFeedJopUp() {
        return this;
    }

    public void addTextAndEmoticon() {

        textOnly = DesignClasses.createJTextArea(default_status, -1);
        textOnly.setBorder(new EmptyBorder(8, 5, 3, 3));
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
                /*if (textOnly.getText().length() > 400 || textOnly.getHeight() > 150) {
                 e.consume();
                 }*/
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER && (e.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
                        /* if (textOnly.getText().length() > 400 || textOnly.getHeight() > 150) {
                         e.consume();
                         }*/
                    } else if (textOnly.getText().trim().length() > 0) {
                        e.consume();
                        textOnly.setText("");
                    } else if (textOnly.getText().length() > 0) {
                        e.consume();
                        textOnly.setText("");
                    } else {
                        e.consume();
                    }
                    textOnly.grabFocus();
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.setBorder(new EmptyBorder(2, 0, 3, 0));
        JScrollPane scroll = DesignClasses.getDefaultScrollPaneThin(new JScrollPane(textOnly) {
            @Override
            public void doLayout() {
                super.doLayout();
                int minHeight = 15;
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
            }
        });

        textPanel.add(scroll, BorderLayout.CENTER);
        textAreaAndEmoticonPanel.add(textPanel, BorderLayout.CENTER);
        addEmoticon = DesignClasses.createImageButton(GetImages.EMOTICON, GetImages.EMOTICON_H, "Add Emotiocon");
        addEmoticon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    int yPos = (int) addEmoticon.getLocationOnScreen().getY();
                    int type_int = 1;
                    if (yPos > DefaultSettings.MONITOR_HALF_HEIGHT) {
                        type_int = 0;
                    }
                    addEmoticonsJDialog = new AddEmoticonsJDialog(textOnly, type_int);
                    addEmoticonsJDialog.setVisible(addEmoticon, true);

                } catch (Exception ed) {
                }
            }
        });
        textAreaAndEmoticonPanel.add(addEmoticon, BorderLayout.EAST);
        textAreaAndEmoticonPanel.revalidate();

    }

    private JPanel contentInsideScroller;

    public void addPreviousPost() {
        JPanel previousCommentPanel = new JPanel(new BorderLayout(0, 0));

        //JPanel previousCommentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,3));
        //previousCommentPanel.setBorder(new EmptyBorder(8, 5, 3, 3));
        //previousCommentPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        //previousCommentPanel.setBorder(BorderFactory.createLineBorder(DefaultSettings.ORANGE_BACKGROUND_COLOR));
        previousCommentPanel.setBackground(Color.WHITE);
        //previousCommentPanel.setPreferredSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH, 50));
        //previousCommentPanel.setOpaque(false);

        contentInsideScroller = new JPanel();
        //contentInsideScroller.setPreferredSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH, 26));
        //contentInsideScroller = new JPanel(new FlowLayout(FlowLayout.CENTER,1,1));
        //contentInsideScroller.setBackground(Color.WHITE);
        //contentInsideScroller.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentInsideScroller.setOpaque(false);
//        rounPanel.add(contentInsideScroller);
        //previousCommentPanel.add(contentInsideScroller);
        JScrollPane contentScroll = DesignClasses.getDefaultScrollPaneThin(contentInsideScroller);
//        previousCommentPanel.add(contentScroll, BorderLayout.CENTER);
//        feedHere.add(previousCommentPanel, BorderLayout.CENTER);
        previousCommentPanel.add(contentScroll);
        contentScroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        feedHere.add(previousCommentPanel, BorderLayout.CENTER);
        //feedHere.add(previousCommentPanel);
        //new SingleShareStatus(();
        //sin = new SingleShareStatus((long) ob.getNfId());
        sin = new SingleShareStatus(statusDto);
        // sin.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
        //sin.setBorder(BorderFactory.createLineBorder(Color.yellow));
        contentInsideScroller.add(sin);
        feedHere.revalidate();

    }


    /*public void addPostImage() {
     if (contentInsideScroller != null) {
     contentInsideScroller.removeAll();
     contentInsideScroller.setLayout(new BorderLayout(3, 0));

     if (statusDto != null && statusDto.getType() > 0) {
     JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
     imagePanel.setBorder(new EmptyBorder(0, 5, 5, 5));
     //            imagePanel.setPreferredSize(new Dimension(130, 120));    
     imagePanel.setPreferredSize(new Dimension(130, 120));
     imagePanel.setOpaque(false);
     JLabel imageLabel = new JLabel();
     imagePanel.add(imageLabel);

     if (statusDto.getType() == 2) {
     //imageLabel.setPreferredSize(new Dimension(95, 95));
     imageLabel.setPreferredSize(new Dimension(105, 105));
     if (statusDto.getUserIdentity() != null) {
     UserBasicInfo basic = FriendList.getInstance().getFriend_hash_map().get(statusDto.getUserIdentity());
     try {
     if (basic != null && basic.getProfileImage() != null) {
     ImageHelpers.addProfileImageThumb(imageLabel, basic.getProfileImage(), DefaultSettings.PROFILE_PIC_DISPLAY_WIDTH, true);
     } else {
     BufferedImage img2 = ImageHelpers.getUnknownImage(false);//.getBufferImageFromurl(null);
     imageLabel.setIcon(new ImageIcon(img2));
     img2 = null;
     }
     } catch (Exception e) {
     log.error("SharedThisFeedPopUP==>" + e.getMessage());
     }

     }

     } else {
     imageLabel.setPreferredSize(new Dimension(120, 120));
     if (statusDto.getImageList() != null) {
     JsonFields singleImge = statusDto.getImageList().get(0);
     new LoadImageInLabel(imageLabel, singleImge.getIurl(), singleImge.getImT(), 120, 120).start();

     }
     }
     contentInsideScroller.add(imagePanel, BorderLayout.WEST);
     }
     contentInsideScroller.revalidate();
     contentInsideScroller.repaint();
     }
     }

     public void addContent() {
     if (contentInsideScroller != null) {
     if (statusDto != null && statusDto.getType() > 0) {
     JPanel contents = new JPanel(new GridBagLayout());
     GridBagConstraints con = new GridBagConstraints();
     con.gridx = 0;
     con.gridy = 0;
     con.anchor = GridBagConstraints.NORTHWEST;
     con.weightx = 1.0;
     con.weighty = 1.0;
     contents.setBorder(new EmptyBorder(5, 5, 5, 5));
     contents.setOpaque(false);
     JLabel statusType = DesignClasses.makeLableBold1("Updated status", Color.BLACK);

     String fullName = this.statusDto.getUserIdentity();
     if (this.statusDto.getFriendId() != null) {
     statusType.setText("posted on " + this.statusDto.getFfn() + " " + this.statusDto.getFln() + "wall");

     } else if (this.statusDto.getType() == 1) {
     if (statusDto.getImageList() != null) {
     JsonFields imageDetails = statusDto.getImageList().get(0);
     statusType.setText(" Posted a photo");
     if (imageDetails.getImT() != null && imageDetails.getImT() == 1) {
     statusType.setText(" Posted a photo");
     } else if (imageDetails.getImT() != null && imageDetails.getImT() == 2) {
     statusType.setText("Changed profile picture");
     } else if (imageDetails.getImT() != null && imageDetails.getImT() == 3) {
     statusType.setText("Changed cover picture");
     }
     }
     } else if (this.statusDto.getType() == 3) {
     int numberofPhotos = 0;
     if (statusDto != null && statusDto.getImageList() != null) {
     numberofPhotos = statusDto.getTotalImage();
     }
     statusType.setText("Added " + numberofPhotos + " new" + ((numberofPhotos > 1) ? " photos" : " photo"));
     } else if (this.statusDto.getType() == 4) {
     int numberofPhotos = 0;
     String albumName = "";
     if (statusDto != null && statusDto.getImageList() != null) {
     JsonFields imageDetails = statusDto.getImageList().get(0);
     albumName = imageDetails.getAlbn();
     numberofPhotos = statusDto.getTotalImage();
     }
     statusType.setText("Added " + numberofPhotos + " new" + ((numberofPhotos > 1) ? " photos" : " photo") + " in album " + albumName);
     }
     contents.add(statusType, con);
     if (statusDto.getFirstName() != null && statusDto.getLastName() != null) {
     fullName = statusDto.getFirstName() + " " + statusDto.getLastName();
     } else if (statusDto.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
     fullName = MyFnFSettings.userProfile.getFirstName() + " " + MyFnFSettings.userProfile.getLastName();
     } else if (FriendList.getInstance().getFriend_hash_map().get(statusDto.getUserIdentity()) != null) {
     fullName = FriendList.getInstance().getFriend_hash_map().get(statusDto.getUserIdentity()).getFirstName() + " " + FriendList.getInstance().getFriend_hash_map().get(statusDto.getUserIdentity()).getLastName();
     }
     JLabel by = DesignClasses.makeLableBold1("By " + fullName, 12);
     con.gridy++;
     contents.add(by, con);
     JPanel panel = statusTextPanel();
     panel.setOpaque(false);
     JScrollPane contentScroll = DesignClasses.getDefaultScrollPaneThin(panel);
     contentScroll.setPreferredSize(new Dimension(418, 248));
     con.gridy++;
     contents.add(contentScroll, con);
     contentInsideScroller.add(contents, BorderLayout.CENTER);
     }
     contentInsideScroller.revalidate();

     }
     }*/
//    public JPanel statusTextPanel() {
//        JPanel textPanel = new JPanel(new BorderLayout());
//        textPanel.setOpaque(false);
//        HTMLDocument chat_doc = null;
//        HTMLEditorKit editorKit;
//        HtmlHelpers htmlHelp = new HtmlHelpers();
//        String text = this.statusDto.getStatus() != null ? this.statusDto.getStatus() : "";
//
//        if (text != null && text.length() > 0) {
//            String replace = htmlHelp.replaceStringForEmoticons(text, reqular_expresion);
//
//            JTextWrapPane area = new JTextWrapPane();
//            try {
//                area.setEditable(false);
//                area.setOpaque(false);
//                area.setContentType("text/html");
//                area.addMouseListener(new ContextMenuMouseListener());
//                chat_doc = (HTMLDocument) area.getDocument();
//                editorKit = (HTMLEditorKit) area.getEditorKit();
//                editorKit.insertHTML(chat_doc, chat_doc.getLength(), htmlHelp.addCSSforText(312, 11, false), 0, 0, null);
//                String style = "singleChat";
//                String url = "<div  id=\"" + style + "\">" + replace + "</div>";
//                try {
//                    editorKit.insertHTML(chat_doc, chat_doc.getLength(), url, 0, 0, null);
//                } catch (BadLocationException ex) {
//                } catch (IOException ex) {
//                }
//
//            } catch (Exception e) {
//            }
//            textPanel.add(area);
//            area.setCaretPosition(0);
//            area.revalidate();
//
//        }
//        return textPanel;
//    }
    private JButton cancelButton;
    private JButton shareButton;
    private JPanel buttons;

    public void addButtons() {
        buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttons.setOpaque(false);
        buttons.setPreferredSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH, 40));
        shareButton = new RoundedCornerButton("Share", "Share");
        //DesignClasses.createImageButton(GetImages.BUTTON_SHARE, GetImages.BUTTON_SHARE_H, GetImages.BUTTON_SHARE_H, "Share");
        shareButton.addActionListener(actionListener);
        cancelButton = new RoundedCornerButton("Cancel", "Cancel");//DesignClasses.createImageButton(GetImages.BUTTON_CANCEL, GetImages.BUTTON_CANCEL_H, GetImages.BUTTON_CANCEL_H, "Cancel");
        buttons.add(loadingLabel);
        loadingLabel.setVisible(false);
        buttons.add(shareButton);
        buttons.add(cancelButton);
        cancelButton.addActionListener(actionListener);
        feedHere.add(buttons, BorderLayout.SOUTH);
        feedHere.revalidate();

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

    public void dispose_this() {

        this.dispose();
        instance = null;
        if (addEmoticonsJDialog != null) {
            addEmoticonsJDialog.setVisible(false);
            addEmoticonsJDialog = null;
        }
        System.gc();
        Runtime.getRuntime().gc();

    }
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == cancelButton || e.getSource() == closeButton) {
                dispose_this();
            } else if (e.getSource() == shareButton) {

                if (statusDto != null) {
                    String sts = "";
                    if (textOnly.getText().length() > 0 && !textOnly.getText().equals(default_status)) {
                        sts = textOnly.getText();
                    }
                    new FeedShareStatusRequest(sts, statusDto.getNfId(), loadingLabel, shareButton, getShareThisFeedJopUp()).start();
                }
            }
        }
    };

}
