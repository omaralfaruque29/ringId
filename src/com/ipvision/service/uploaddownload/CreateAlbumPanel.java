/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.uploaddownload;

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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.JDialogWebcam;
import com.ipvision.view.feed.SingleImagePreviewPanel;
import com.ipvision.view.utility.ContextMenuMouseListener;
import com.ipvision.view.utility.RoundedCornerButton;

/**
 *
 * @author Wasif Islam
 */
public class CreateAlbumPanel extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CreateAlbumPanel.class);
    private int posX = 0;
    private int posY = 0;
    private String default_text = "Album name";
    //public JTextArea new_album_name = DesignClasses.createJTextAreaWithBanglaFontNoBorder(default_text, 11);
    public JTextField new_album_name = DesignClasses.makeTextFieldLimitedTextSize(default_text, 220, 25, 100);
    public File[] fileToUploads;
    private CreateAlbumPanel createAlbum;
    //final JPanel contentPanel = new JPanel(new WrapLayout(WrapLayout.LEFT, 5, 5));
    final JPanel contentPanel = new JPanel(new GridLayout(0, 3, 5, 5));
    private JLabel pleaseWait;
    public ConcurrentHashMap<String, SingleImagePreviewPanel> imageContainerMap;
    private String albumName;
    private String albumID;
    public JButton importPhotoBtn = new RoundedCornerButton("Import Photo", "Import Photo from Computer");
    public JButton cameraBtn = new RoundedCornerButton("Take photo", "Take photo");
    public JButton doneBtn = new RoundedCornerButton("Done", "Done");
    public JLabel backButton = DesignClasses.create_imageJlabel(GetImages.BACK_MINI);
    private static File fileToUpload = null;

    public CreateAlbumPanel() {
        imageContainerMap = new ConcurrentHashMap<String, SingleImagePreviewPanel>();
        this.createAlbum = this;
        init();
    }

    public CreateAlbumPanel(String album_name, String album_id) {
        this.albumName = album_name;
        this.albumID = album_id;
        imageContainerMap = new ConcurrentHashMap<String, SingleImagePreviewPanel>();
        this.createAlbum = this;
        init();
    }

    public void init() {
        setLayout(new BorderLayout());
        setOpaque(false);
        // setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);
        addMouseListener(poxValueListener);
        addMouseMotionListener(frameDragListener);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(8, 13, 13, 13));
        mainPanel.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
        add(mainPanel, BorderLayout.CENTER);

        JPanel new_album_name_panel = new JPanel(new BorderLayout());
        //new_album_name_panel.setBorder(new MatteBorder(1, 1, 1, 1, DefaultSettings.APP_BORDER_COLOR));
        new_album_name_panel.setOpaque(false);
        new_album_name.setForeground(DefaultSettings.disable_font_color);
        if (this.albumName != null) {
            new_album_name.setText(this.albumName);

            new_album_name.setEditable(false);

            backButton.setToolTipText("Back");
            backButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // BookMain bookMain = BookMain.getBookMain();
                    //bookMain.addMyallbums();
                    GuiRingID.getInstance().loadIntoMainRightContent(GuiRingID.getInstance().getMainRight().getMyProfilePanel());
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    backButton.setIcon(DesignClasses.return_image(GetImages.BACK_MINI_H));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    backButton.setIcon(DesignClasses.return_image(GetImages.BACK_MINI));
                }
            });

        } else {

            new_album_name.setBackground(Color.WHITE);
            new_album_name.setText(default_text);
            new_album_name.setEditable(true);
        }
        //new_album_name.setBorder(new EmptyBorder(7, 6, 3, 7));
        new_album_name.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new_album_name.getText().equals(default_text) || new_album_name.getText().length() < 1) {
                    set_reset_default_text(new_album_name, default_text, false);
                } else if (new_album_name.getText().length() < 1) {
                    set_reset_default_text(new_album_name, default_text, true);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new_album_name.getText().equals(default_text) || new_album_name.getText().length() < 1) {
                    set_reset_default_text(new_album_name, default_text, false);
                } else if (new_album_name.getText().length() < 1) {
                    set_reset_default_text(new_album_name, default_text, true);
                }
            }
        });

        //new_album_name.setBackground(Color.WHITE);
        new_album_name.addMouseListener(new ContextMenuMouseListener());
        //new_album_name.setPreferredSize(new Dimension(170, 30));
        new_album_name_panel.add(new_album_name, BorderLayout.CENTER);

        JPanel centralPanel = new JPanel(new BorderLayout());
        centralPanel.setOpaque(false);
        mainPanel.add(centralPanel, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(0, 40));
        topPanel.setOpaque(false);

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        //namePanel.setPreferredSize(new Dimension(0, 40));
        namePanel.setOpaque(false);
        namePanel.add(new_album_name_panel);

        topPanel.add(namePanel, BorderLayout.WEST);

        //centralPanel.add(namePanel, BorderLayout.NORTH);
        centralPanel.add(topPanel, BorderLayout.NORTH);

        if (this.albumName != null) {
            JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            backPanel.setOpaque(false);
            backPanel.add(backButton);
            backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            topPanel.add(backPanel, BorderLayout.EAST);
        }

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.WHITE);

        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(8, 13, 13, 13));
        wrapperPanel.add(contentPanel, BorderLayout.NORTH);

        JScrollPane downContent = DesignClasses.getDefaultScrollPane(wrapperPanel);
        centralPanel.add(downContent, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setPreferredSize(new Dimension(0, 40));
        buttonPanel.setOpaque(false);

        importPhotoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFrame dd = new JFrame();
                    FileDialog fc = new FileDialog(dd, "Browse", FileDialog.LOAD);
                    fc.setFile("*.jpg;*.jpeg;*.png;*.gif");
                    fc.setMultipleMode(true);
                    fc.show();
                    File[] fileToUploads = fc.getFiles();
                    if (fileToUploads != null) {
                        for (int i = 0; i < fileToUploads.length; i++) {
                            singleUploadedImageBlock(fileToUploads[i]);
                            contentPanel.revalidate();
                        }
                    }

                } catch (Exception ex) {
                    //  ed.printStackTrace();
                    log.error("Error in here ==>" + ex.getMessage());
                }

            }
        });
        buttonPanel.add(importPhotoBtn);

        cameraBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialogWebcam webCamAlbumImage = new JDialogWebcam();
                if (JDialogWebcam.fileToUpload != null && JDialogWebcam.fileToUpload.exists()) {
                    fileToUpload = JDialogWebcam.fileToUpload;
                    singleUploadedImageBlock(fileToUpload);

                }
//                if (WebCamBookImage.getChangeImage() != null) {
//                    WebCamBookImage.getChangeImage().setVisible(true);
//                } else {
//                    WebCamBookImage webcam = new WebCamBookImage(createAlbum);
//                    webcam.setVisible(true);
//                }
            }
        });
        buttonPanel.add(cameraBtn);

        //DesignClasses.createImageButton(GetImages.DONE, GetImages.DONE_H, "Done");
        doneBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (new_album_name.getText() == null || new_album_name.getText().length() < 1 || new_album_name.getText().equals(default_text)) {
                    // JOptionPane.showConfirmDialog(null, "" + "Album name can not be empty", StaticFields.APP_NAME, JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                    HelperMethods.showPlaneDialogMessage("" + "Album name can not be empty");
                } else if (imageContainerMap == null || imageContainerMap.isEmpty()) {
                    HelperMethods.showPlaneDialogMessage("" + "Must upload an image");
                    // JOptionPane.showConfirmDialog(null, "" + "Must upload an image", StaticFields.APP_NAME, JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                } else {
                    String GenerateAlbumID = "";
                    if (albumID != null && albumID.length() > 0) {
                        GenerateAlbumID = albumID;
                    } else {
                        GenerateAlbumID = MyFnFSettings.LOGIN_USER_ID + System.currentTimeMillis();
                    }
                    pleaseWait.setIcon(DesignClasses.return_image(GetImages.PLEASE_WAIT_MINI));
                    new_album_name.setEditable(false);
                    importPhotoBtn.setEnabled(false);
                    cameraBtn.setEnabled(false);

                    if (albumName != null && albumName.length() > 0 && albumID != null && albumID.length() > 0) {
                        new AlbumImageUpload(GenerateAlbumID, new_album_name.getText(), imageContainerMap, 0, 4, doneBtn, pleaseWait, createAlbum, false).start();
                    } else {
                        new AlbumImageUpload(GenerateAlbumID, new_album_name.getText(), imageContainerMap, 0, 4, doneBtn, pleaseWait, createAlbum, true).start();
                    }
                }
            }
        });
        buttonPanel.add(doneBtn, FlowLayout.RIGHT);

        pleaseWait = new JLabel();
        pleaseWait.setOpaque(false);
        pleaseWait.setPreferredSize(new Dimension(40, 23));
        buttonPanel.add(pleaseWait, FlowLayout.RIGHT);

        centralPanel.add(buttonPanel, BorderLayout.SOUTH);

    }

    public void removeSelectedImageBlock(String key) {
        SingleImagePreviewPanel a = (SingleImagePreviewPanel) imageContainerMap.get(key);
        if (a != null) {
            int index = contentPanel.getComponentZOrder(a);
            contentPanel.remove(index);
            contentPanel.revalidate();
            imageContainerMap.remove(key);
        }
    }

    public void repaintImageContentPanel() {
        contentPanel.revalidate();
    }

    public void singleUploadedImageBlock(File file) {
        Random rand = new Random();
        String key = System.currentTimeMillis() + file.getName() + rand.nextInt(99);

        SingleImagePreviewPanel previewPanel = new SingleImagePreviewPanel(createAlbum, file, key);
        contentPanel.add(previewPanel);
        contentPanel.revalidate();
        imageContainerMap.put(key, previewPanel);
    }

    public void dispose_create_album() {
        if (fileToUploads != null) {
            fileToUploads = null;
        }
        if (imageContainerMap != null) {
            imageContainerMap.clear();
        }
        contentPanel.removeAll();
        createAlbum = null;
    }

    class ImageFilter extends javax.swing.filechooser.FileFilter {

        public boolean accept(File f) {
            return f.getName().toLowerCase().endsWith(".png")
                    || f.getName().toLowerCase().endsWith(".jpg")
                    || f.getName().toLowerCase().endsWith(".jif")
                    || f.isDirectory();
        }

        public String getDescription() {
            return "Image files (*.png)";
        }
    }

    private void set_reset_default_text(JTextField textfield, String default_text, Boolean status) {
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

//    public static void main(String[] ars) {
//        CreateAlbum obj = new CreateAlbum();
//        obj.setVisible(true);
//    }
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
}
