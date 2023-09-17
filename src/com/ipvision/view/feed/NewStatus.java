/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.ContextMenuMouseListener;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.UploadImage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
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
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.JDialogWebcam;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.service.feed.FeedAddImageStatusRequest;
import com.ipvision.service.feed.FeedAddStatusRequest;
import com.ipvision.view.AddEmoticonsJDialog;
import com.ipvision.view.utility.JCustomMenuPopup;
import com.ipvision.view.utility.RoundedCornerButton;

/**
 *
 * @author Faiz Ahmed
 */
public class NewStatus extends JPanel implements ActionListener {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(NewStatus.class);
    private JTextArea textOnly;
    private String default_status = "What's on your mind?";
//    private final JPanel textPanel = new JPanel(new BorderLayout());
    public JPanel textPanel = new JPanel(new BorderLayout());
    public JButton postButton;
    private JLabel pleaseWait;
    public PreviewUploadingStatusImagePanel previewStatusImagePanel;
    private AddEmoticonsJDialog addEmoticonsJDialog;
    private String friendId;
    private String friend_name;
    private JPanel albumNameAndImagePanel;
    private Long circleId;
    private String circleName;
    private JButton addEmoticon;
    private static File fileToUpload = null;
    private JLabel selectedValidityLabel;
    private JButton validityPopUpButton;
    private JCustomMenuPopup validityPopUp = null;
    private JPanel validityPanel;
    private String[] validityArray = {"Unlimited", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
    private JButton btnOptions;
    private JCustomMenuPopup optionsPopup = null;
    private final String MNU_TAKE_PHOTO = "Take Photos";
    private final String MNU_UPLOAD_PC = "Upload from Computer";
    private final String MNU_CHOOSE_FROM_ALBUM = "Choose from Album";
    public NewsFeedWithMultipleImage statusDto;
    public JButton cancelButton;
    private static NewStatus instance;

    public static NewStatus getInstance() {
        return instance;
    }

    public Integer getValidity() {
        if (selectedValidityLabel.getText().equalsIgnoreCase("Unlimited")) {
            return -1;
        }
        return Integer.parseInt(selectedValidityLabel.getText());
    }

    public NewStatus(String friend_id, String friendName) {
        this.instance = this;
        this.friendId = friend_id;
        this.friend_name = friendName;
        this.default_status = "Write on ";
        this.default_status = this.default_status + this.friend_name + "'s wall";

        setOpaque(false);
        setLayout(new BorderLayout());
        initContainers();
    }

    public NewStatus(Long circleId, String circleName) {
        this.instance = this;
        this.circleId = circleId;
        this.circleName = circleName;
        this.default_status = "Write on " + this.circleName;
        setOpaque(false);
        setLayout(new BorderLayout());
        initContainers();
    }

    public NewStatus() {
        this.instance = this;
        setOpaque(false);
        setLayout(new BorderLayout());
        initContainers();
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

    public void buildPopUpMenu() {
        for (String validityArray1 : validityArray) {
            validityPopUp.addMenu(validityArray1, null);
        }
    }

    private void initContainers() {
        try {
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setOpaque(false);
            add(mainPanel, BorderLayout.CENTER);

            JPanel seperator = new JPanel();
            seperator.setPreferredSize(new Dimension(DefaultSettings.BOOK_SINGLE_PANEL_COMMON_WIDTH, MyFnFSettings.DEFAULT_VERTICAL_GAP));
            seperator.setBackground(RingColorCode.DEFAULT_BACKGROUND_COLOR);
            add(seperator, BorderLayout.SOUTH);

            previewStatusImagePanel = new PreviewUploadingStatusImagePanel(this);

            JPanel status_name_Panel = new JPanel(new BorderLayout(0, 0));
            status_name_Panel.setBorder(new MatteBorder(1, 1, 0, 1, RingColorCode.FEED_BORDER_COLOR));//setBorder(DefaultSettings.DEFAULT_BOOK_BORDER);
            status_name_Panel.setBackground(RingColorCode.FEED_BG_COLOR);

            textPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
            textPanel.setOpaque(false);

            textOnly = DesignClasses.createJTextAreaWithBanglaFontNoBorder(default_status, 13);
            textOnly.setForeground(DefaultSettings.disable_font_color);
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
            textOnly.setRows(2);
            textOnly.setEditable(true);
            textOnly.setBackground(Color.WHITE);
            textOnly.setBorder(new EmptyBorder(5, 15, 5, 10));
            textOnly.addMouseListener(new ContextMenuMouseListener());
            textOnly.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER && (e.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
                        textOnly.append("\n");
                    }

                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });
            postButton = new RoundedCornerButton("Post", "Click to post");
            pleaseWait = new JLabel();
            pleaseWait.setOpaque(false);
            pleaseWait.setPreferredSize(new Dimension(40, 23));

            validityPopUp = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_POST_VALIDITY);
            buildPopUpMenu();
            JPanel validityContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            validityContainer.setOpaque(false);
            JLabel lblValidity = DesignClasses.makeDisableFontLable("Validity (Days):", 12);
            validityContainer.add(lblValidity);
            validityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
            validityPanel.setOpaque(false);
            validityPopUpButton = DesignClasses.createImageButton(GetImages.IMAGE_BOTTOM_ARROW, GetImages.IMAGE_BOTTOM_ARROW, "Post Validity");
            validityPanel.add(validityPopUpButton);
            validityPopUpButton.addMouseListener(menuListener);
            selectedValidityLabel = DesignClasses.makeDisableFontLable(validityArray[0], 12);
            selectedValidityLabel.setPreferredSize(new Dimension(60, 12));
            validityPanel.add(selectedValidityLabel);
            validityPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            validityPanel.addMouseListener(menuListener);
            validityContainer.add(validityPanel);

            postButton.addActionListener(this);
            postButton.setEnabled(true);

            status_name_Panel.add(textPanel, BorderLayout.CENTER);
            mainPanel.add(status_name_Panel, BorderLayout.CENTER);

            JPanel selectedImageAndBrowsePanel = new JPanel(new BorderLayout(0, 0));
            selectedImageAndBrowsePanel.setOpaque(false);
            albumNameAndImagePanel = new JPanel(new BorderLayout(MyFnFSettings.DEFAULT_VERTICAL_GAP, MyFnFSettings.DEFAULT_VERTICAL_GAP));
            albumNameAndImagePanel.setLayout(new BorderLayout());
            albumNameAndImagePanel.setBorder(new EmptyBorder(3, 4, 4, 4));
            albumNameAndImagePanel.setOpaque(false);
            selectedImageAndBrowsePanel.add(albumNameAndImagePanel, BorderLayout.CENTER);

            JPanel otherOptions = new JPanel(new BorderLayout());
            //otherOptions.setBackground(DefaultSettings.COMMENTS_LIKE_BAR_COLOR);
            otherOptions.setBackground(Color.WHITE);
            otherOptions.setPreferredSize(new Dimension(0, DefaultSettings.SMALL_PANEL_HEIGHT));
            otherOptions.setBorder(DefaultSettings.DEFAULT_FEED_BORDER);
            mainPanel.add(otherOptions, BorderLayout.SOUTH);

            status_name_Panel.add(selectedImageAndBrowsePanel, BorderLayout.SOUTH);
            JPanel otherLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            otherLeft.setOpaque(false);
            JPanel othersRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, MyFnFSettings.DEFAULT_VERTICAL_GAP));
            othersRight.setOpaque(false);
            othersRight.add(pleaseWait);
            othersRight.add(validityContainer);
            othersRight.add(postButton);

            cancelButton = new RoundedCornerButton("Cancel", "Click to Cancel");
            cancelButton.setVisible(false);
            cancelButton.addActionListener(this);
            othersRight.add(cancelButton);

            otherOptions.add(otherLeft, BorderLayout.WEST);
            otherOptions.add(othersRight, BorderLayout.EAST);

            optionsPopup = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_UPLOAD_PHOTO);
            btnOptions = DesignClasses.createImageButton(GetImages.OPTIONS, GetImages.OPTIONS_H, "Add Photo");
            otherLeft.add(btnOptions);
            btnOptions.addActionListener(this);
            optionsPopup.addMenu(MNU_TAKE_PHOTO, GetImages.OPTION_TAKE_PHOTO);
            optionsPopup.addMenu(MNU_UPLOAD_PC, GetImages.OPTION_UPLOAD_PHOTO);
            optionsPopup.addMenu(MNU_CHOOSE_FROM_ALBUM, GetImages.OPTION_ALBUM_PHOTO);

            addEmoticon = DesignClasses.createImageButton(GetImages.EMOTICON, GetImages.EMOTICON_H, "Add Emotiocon");
            addEmoticon.addActionListener(this);
            otherLeft.add(addEmoticon);

            addDefaultStatus();
            refreshPreviewImageAlbum();

        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error in initContainers ==>" + e.getMessage());
        }
    }

    MouseListener menuListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == validityPopUpButton || e.getSource() == validityPanel) {
                if (!validityPopUp.isVisible()) {
                    validityPopUp.setVisible(validityPopUpButton, 20, - 4);
                } else {
                    validityPopUp.hideThis();
                }
            } else {
                JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
                if (menu.text.equalsIgnoreCase(MNU_TAKE_PHOTO)) {
                    action_take_photo();
                    optionsPopup.hideThis();
                    menu.setMouseExited();
                } else if (menu.text.equalsIgnoreCase(MNU_CHOOSE_FROM_ALBUM)) {
                    action_choose_album();
                    optionsPopup.hideThis();
                    menu.setMouseExited();
                } else if (menu.text.equalsIgnoreCase(MNU_UPLOAD_PC)) {
                    action_upload_pc();
                    optionsPopup.hideThis();
                    menu.setMouseExited();
                } else {
                    for (String v : validityArray) {
                        if (menu.text.equalsIgnoreCase(v)) {
                            selectedValidityLabel.setText(v);
                            validityPopUp.hideThis();
                            break;
                        }
                    }
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (e.getSource() != validityPopUpButton && e.getSource() != validityPanel) {
                JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
                menu.setMouseEntered();
            }

        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getSource() != validityPopUpButton && e.getSource() != validityPanel) {
                JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
                menu.setMouseExited();
            }

        }
    };

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnOptions) {
            if (!optionsPopup.isVisible()) {
                optionsPopup.setVisible(btnOptions, 16, -20);
            } else {
                optionsPopup.hideThis();
            }
        } else if (e.getSource() == postButton) {
            try {
                if (!previewStatusImagePanel.imageContainerMap.isEmpty() && previewStatusImagePanel.imageContainerMap.size() > 0) {
                    postStatusWithImage();
                } else {
                    postStatus();
                }
            } catch (Exception ex) {
                //ex.printStackTrace();
                 log.error("Error in here ==>" + ex.getMessage());
                previewStatusImagePanel.removeUploadedAllImageBlock();
                if (pleaseWait != null) {
                    pleaseWait.setIcon(null);
                }
            }
        } else if (e.getSource() == cancelButton) {
            previewStatusImagePanel.removeUploadedAllImageBlock();
        } else if (e.getSource() == addEmoticon) {
            try {
                if (pleaseWait != null) {
                    pleaseWait.setIcon(null);
                }
                int yPos = (int) addEmoticon.getLocationOnScreen().getY();
                int type_int = 1;
                if (yPos > DefaultSettings.MONITOR_HALF_HEIGHT) {
                    type_int = 0;
                }
                addEmoticonsJDialog = new AddEmoticonsJDialog(textOnly, type_int);
                addEmoticonsJDialog.setVisible(addEmoticon, true);
            } catch (Exception ex) {
               // ex.printStackTrace();
             log.error("Error in here ==>" + ex.getMessage());
            }
        }
    }

    private void action_take_photo() {
        if (pleaseWait != null) {
            pleaseWait.setIcon(null);
        }
        JDialogWebcam webCambookImage = new JDialogWebcam();
        if (JDialogWebcam.fileToUpload != null && JDialogWebcam.fileToUpload.exists()) {
            fileToUpload = JDialogWebcam.fileToUpload;
            previewStatusImagePanel.singleUploadedImageBlock(fileToUpload, null, null, null);
            refreshPreviewImageAlbum();
        }
    }

    private void action_choose_album() {
        if (pleaseWait != null) {
            pleaseWait.setIcon(null);
        }
        try {
            ChooseFromAlbum albumViewerJoptionPanel = new ChooseFromAlbum(getthisObj());
            albumViewerJoptionPanel.initContainers();
            albumViewerJoptionPanel.setVisible(true);
        } catch (Exception ex) {
          //  ex.printStackTrace();
         log.error("Error in action_choose_album ==>" + ex.getMessage());
        }
    }

    private void action_upload_pc() {
        try {
            if (pleaseWait != null) {
                pleaseWait.setIcon(null);
            }
            JFrame dd = new JFrame();
            FileDialog fc = new FileDialog(dd, "Browse", FileDialog.LOAD);
            fc.setFile("*.jpg;*.jpeg;*.png;*.gif");
            fc.setMultipleMode(true);
            fc.show();
            File[] fileToUploads = fc.getFiles();
            if (fileToUploads != null) {
                for (File single : fileToUploads) {
                    previewStatusImagePanel.singleUploadedImageBlock(single, null, null, null);
                }
                refreshPreviewImageAlbum();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            // fileToUploads = null;
            log.error("Error in action_upload_pc ==>" + ex.getMessage());
        }
    }

    private void addDefaultStatus() {
        if (textPanel != null) {
            textPanel.removeAll();
            JScrollPane scroll = DesignClasses.getDefaultScrollPaneThin(new JScrollPane(textOnly) {
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
                        prefHeight = maxHeight + 5 + 3;
                    } else {
                        if (this.getVerticalScrollBar().isVisible()) {
                            this.getVerticalScrollBar().setVisible(false);
                        }
                        if (textOnly.getHeight() < minHeight) {
                            prefHeight = minHeight + 5 + 3;
                        } else {
                            prefHeight = textOnly.getHeight() + 5 + 3;
                        }
                    }

                    textPanel.setPreferredSize(new Dimension(textOnly.getWidth(), prefHeight));
                    textPanel.revalidate();

                }
            });
            textPanel.add(scroll);
            textPanel.revalidate();
            textPanel.repaint();
        }
    }

//    public void clearPreviousImage() {
//        previewStatusImagePanel.removeUploadedAllImageBlock();
//        refreshPreviewImageAlbum();
//    }
    public void refreshPreviewImageAlbum() {
        if (albumNameAndImagePanel != null) {
            albumNameAndImagePanel.removeAll();
            if (!previewStatusImagePanel.imageContainerMap.isEmpty()) {
                albumNameAndImagePanel.add(previewStatusImagePanel, BorderLayout.CENTER);
                cancelButton.setVisible(true);
            } else {
                cancelButton.setVisible(false);
            }
            albumNameAndImagePanel.revalidate();
            albumNameAndImagePanel.repaint();
        }

    }

    private NewStatus getthisObj() {
        return this;
    }

    public static boolean isWhitespace(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    private void postStatus() {
        if (textOnly.getText().equals(default_status) || textOnly.getText().length() < 1 || isWhitespace(textOnly.getText())) {
            // JOptionPane.showConfirmDialog(null, "Please write something or upload a photo.", "Empty status", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
            HelperMethods.showWarningDialogMessage("Please write something or upload a photo.");
            // } //else if (textOnly.getText().length() > 400) {
            //   JOptionPane.showConfirmDialog(null, NotificationMessages.MAX_STATUS_LENGTH_OVER, "Status Overflow", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
            //   HelperMethods.showWarningDialogMessage(NotificationMessages.MAX_STATUS_LENGTH_OVER);
        } else {
            pleaseWait.setIcon(DesignClasses.return_image(GetImages.PLEASE_WAIT_MINI));
            if (friendId != null && friendId.length() > 0) {
                new FeedAddStatusRequest(pleaseWait, textOnly.getText(), friendId, getValidity(), postButton).start();
            } else if (circleId != null && circleId > 0) {
                new FeedAddStatusRequest(pleaseWait, textOnly.getText(), circleId, getValidity(), postButton).start();
            } else {
                new FeedAddStatusRequest(pleaseWait, textOnly.getText(), getValidity(), postButton).start();
            }
            textOnly.setText(default_status);
            textOnly.setForeground(DefaultSettings.disable_font_color);
        }
    }

    private void postStatusWithImage() {
        String status = "";
        if (textOnly != null && !textOnly.getText().equals(default_status)) {
            status = textOnly.getText();
        }

        pleaseWait.setIcon(DesignClasses.return_image(GetImages.PLEASE_WAIT_MINI));

        for (String key : previewStatusImagePanel.imageContainerMap.keySet()) {
            UploadImage uploadImage = (UploadImage) previewStatusImagePanel.imageContainerMap.get(key);
            if (uploadImage != null && uploadImage.getThumbnailPanel() != null) {
                uploadImage.getThumbnailPanel().removePanel.setVisible(false);
                if (uploadImage.getThumbnailPanel().textOnly != null) {
                    uploadImage.getThumbnailPanel().textOnly.setEditable(false);
                }
                if (uploadImage.getThumbnailPanel().emotionButton != null) {
                    uploadImage.getThumbnailPanel().emotionButton.setEnabled(false);
                }
                uploadImage.getThumbnailPanel().imagePaneMain.revalidate();
                uploadImage.getThumbnailPanel().wrappperPanel.revalidate();
            }

        }
        new FeedAddImageStatusRequest(textOnly, default_status, pleaseWait, postButton, getInstance(), status, friendId, circleId, getValidity()).start();

    }

}
