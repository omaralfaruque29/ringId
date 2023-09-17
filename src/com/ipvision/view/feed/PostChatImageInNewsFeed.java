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
import com.ipvision.model.UploadChatImage;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import com.ipvision.service.chat.PostChatImageToFeedRequest;
import com.ipvision.view.AddEmoticonsJDialog;
import com.ipvision.view.utility.ContextMenuMouseListener;
import com.ipvision.view.utility.RoundedCornerButton;
import com.ipvision.view.utility.ImagePane;

/**
 *
 * @author Sirat Samyoun
 */
public class PostChatImageInNewsFeed extends JDialog implements ActionListener, KeyListener, FocusListener {

    private ImagePane content;
    public JPanel textArearAndEmoticon, selectedImageAndBrowsePanel;
    public JTextArea textOnly;
    public JButton postButton;
    private JLabel pleaseWait;
    private String default_status = "Write something...";
    private int posX = 0;
    private int posY = 0;
    private JButton cancelButton, closeButton, addEmoticon;
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PostChatImageInNewsFeed.class);
    private static PostChatImageInNewsFeed instance;
    private JComboBox cmbValidity;
    private String[] validityArray = {"Unlimited", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
    private static final Color FG = DefaultSettings.disable_font_color;
    private File fileToUpload;// = new File("D:/aa.jpg");
    private UploadChatImage uploadChatImage;
    private JPanel feedHere;
    private AddEmoticonsJDialog addEmoticonsJDialog;
    private SingleChatImageForFeedPost singleChatImageForFeedPost;

    public static PostChatImageInNewsFeed getInstance() {
        return instance;
    }

    public PostChatImageInNewsFeed(File fileToUpload) {
        this.fileToUpload = fileToUpload;
        instance = this;
        setMinimumSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH + 55, DefaultSettings.MAIN_RIGHT_CONTENT_HEIGHT + 10));
        setLocationRelativeTo(null);
        setUndecorated(true);
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
        content.addMouseListener(poxValueListener);
        content.addMouseMotionListener(frameDragListener);
        this.setContentPane(content);
        init();
    }

    private void init() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(3, 10, 3, 5));
        headerPanel.setOpaque(false);
        content.add(headerPanel, BorderLayout.NORTH);
        //JLabel title = DesignClasses.makeJLabel("Share this post", 15, 1, Color.BLACK, JLabel.LEFT);
        //headerPanel.add(title, BorderLayout.WEST);
        closeButton = DesignClasses.createImageButton(GetImages.BUTTON_CLOSE_MINI, GetImages.BUTTON_CLOSE_MINI_H, GetImages.BUTTON_CLOSE_MINI_H, "Cancel");
        closeButton.addActionListener(this);
        headerPanel.add(closeButton, BorderLayout.EAST);

        feedHere = new JPanel();
        feedHere.setLayout(new BorderLayout(0, 3));
        feedHere.setBorder(new EmptyBorder(5, 5, 5, 5));
        feedHere.setOpaque(false);
        feedHere.addMouseListener(poxValueListener);
        feedHere.addMouseMotionListener(frameDragListener);
        content.add(feedHere, BorderLayout.CENTER);
        JPanel textEmoticonHolder = new JPanel(new BorderLayout());
        textEmoticonHolder.setOpaque(false);
        textEmoticonHolder.setBorder(new EmptyBorder(0, 5, 5, 5));
        textArearAndEmoticon = new JPanel(new BorderLayout(0, 0));
        textArearAndEmoticon.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR));
        textArearAndEmoticon.setBackground(Color.WHITE);
        textOnly = DesignClasses.createJTextArea(default_status, 300);
        textOnly.setBorder(new EmptyBorder(8, 5, 3, 3));
        textOnly.setForeground(DefaultSettings.disable_font_color);
        textOnly.addMouseListener(new ContextMenuMouseListener());
        textOnly.addFocusListener(this);
        textOnly.setRows(1);
        textOnly.setEditable(true);
        textOnly.addKeyListener(this);
        textArearAndEmoticon.add(textOnly, BorderLayout.CENTER);
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
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                if (addEmoticonsJDialog != null && addEmoticonsJDialog.isDisplayable()) {
                    return;
                }
                if (singleChatImageForFeedPost.addEmoticonsJDialog != null && singleChatImageForFeedPost.addEmoticonsJDialog.isDisplayable()) {
                    return;
                }
                dispose_this();
            }
        });
        textArearAndEmoticon.add(addEmoticon, BorderLayout.EAST);
        textEmoticonHolder.add(textArearAndEmoticon, BorderLayout.CENTER);
        feedHere.add(textEmoticonHolder, BorderLayout.NORTH);

        pleaseWait = new JLabel();
        pleaseWait.setOpaque(false);
        pleaseWait.setPreferredSize(new Dimension(40, 23));
        cancelButton = new RoundedCornerButton("Cancel", "Cancel");
        cancelButton.addActionListener(this);
        postButton = new RoundedCornerButton("Post", "Post into wall");
        postButton.addActionListener(this);
        JPanel validityContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        validityContainer.setOpaque(false);
        JLabel lblValidity = new JLabel("Validity (Days):");
        lblValidity.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        lblValidity.setForeground(FG);//FG
        lblValidity.setOpaque(false);
        validityContainer.add(lblValidity);
        cmbValidity = new JComboBox(validityArray);
        cmbValidity.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        cmbValidity.setBackground(RingColorCode.COMMENTS_LIKE_BAR_COLOR);
        cmbValidity.setForeground(FG);
        cmbValidity.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        cmbValidity.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton();
                btn.setFocusPainted(false);
                btn.setContentAreaFilled(false);
                btn.setBorder(null);
                btn.setIcon(DesignClasses.return_image(GetImages.IMAGE_BOTTOM_ARROW));
                return btn;
            }
        });
        cmbValidity.setOpaque(false);
        validityContainer.add(cmbValidity);
        JPanel otherOptions = new JPanel(new BorderLayout());
        otherOptions.setBackground(RingColorCode.COMMENTS_LIKE_BAR_COLOR);
        otherOptions.setPreferredSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH, DefaultSettings.SMALL_PANEL_HEIGHT));
        otherOptions.setBorder(DefaultSettings.DEFAULT_FEED_BORDER);
        JPanel otherLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        otherLeft.setOpaque(false);
        JPanel othersRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, MyFnFSettings.DEFAULT_VERTICAL_GAP));
        othersRight.setOpaque(false);
        othersRight.add(pleaseWait);
        othersRight.add(validityContainer);
        othersRight.add(postButton);
        othersRight.add(cancelButton);
        otherOptions.add(otherLeft, BorderLayout.WEST);
        otherOptions.add(othersRight, BorderLayout.EAST);
        feedHere.add(otherOptions, BorderLayout.SOUTH);

        Random rand = new Random();
        String key = (System.currentTimeMillis() + fileToUpload.getName() + rand.nextInt(99));
        singleChatImageForFeedPost = new SingleChatImageForFeedPost(fileToUpload, null, key);
        uploadChatImage = new UploadChatImage(singleChatImageForFeedPost, null, null);
        JScrollPane scroll = DesignClasses.getDefaultScrollPane(singleChatImageForFeedPost);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        feedHere.add(scroll, BorderLayout.CENTER);
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

    public Integer getValidity() {
        if (cmbValidity.getSelectedItem().toString().equalsIgnoreCase("Unlimited")) {
            return -1;
        }
        return Integer.parseInt(cmbValidity.getSelectedItem().toString());
    }

    private void postStatusWithImage() {
        String status = "";
        if (textOnly != null && textOnly.getText().length() > 400) {
            HelperMethods.showWarningDialogMessage(NotificationMessages.MAX_STATUS_LENGTH_OVER);
        } else if (textOnly != null && !textOnly.getText().equals(default_status)) {
            status = textOnly.getText();
        }
        pleaseWait.setIcon(DesignClasses.return_image(GetImages.PLEASE_WAIT_MINI));
        new PostChatImageToFeedRequest(instance, uploadChatImage, textOnly, default_status, pleaseWait, postButton, status, getValidity()).start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == postButton) {
            postStatusWithImage();
        } else if (e.getSource() == cancelButton || e.getSource() == closeButton) {
            dispose_this();
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

    @Override
    public void keyTyped(KeyEvent e) {
        if (textOnly.getText().length() > 400 || textOnly.getHeight() > 150) {
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER && (e.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
                if (textOnly.getText().length() > 400 || textOnly.getHeight() > 150) {
                    e.consume();
                }
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
        set_reset_defalut_text(textOnly, default_status, true);
    }
}
