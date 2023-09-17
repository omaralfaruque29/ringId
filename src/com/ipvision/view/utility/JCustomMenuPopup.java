/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.constants.StatusConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import com.ipvision.view.image.BasicImageViewStructure;
import java.awt.event.KeyListener;

/**
 *
 * @author Shahadat
 */
public class JCustomMenuPopup extends JDialog implements MouseWheelListener {

    public static int TYPE_CHAT_OPTIONS = 1;
    public static int TYPE_CHAT_RECENT = 2;
    public static int TYPE_FRIENDLIST_CATEGORY = 3;
    public static int TYPE_UPLOAD_PHOTO = 4;
    public static int TYPE_POST_VALIDITY = 5;
    public static int TYPE_EDIT_DELETE = 6;// for comment, status post 
    public static int TYPE_ONLY_DELETE = 7;// for  comment, status post
    public static int TYPE_EDIT_LEAVE_DELETE = 8;// or edit join Leave,, 3 word and 3 line// for showing in group popup window -->GroupPanel class----> Rabiul 3-11-2015
    public static int TYPE_JOIN_LEAVE = 9;
    public static int TYPE_ONLY_LEAVE = 10;
    public static int TYPE_STATUS = 11;
    public static int TYPE_SIGN_IN = 12;
    private MouseListener mouseListener = null;
    private KeyListener keyListener = null;
    private Image img = null;
    public int width = 0;
    public int height = 0;
    public int pTOP = 0;
    public int pBOTTOM = 0;
    public int pLEFT = 0;
    public int pRIGHT = 0;
    public int TEXT_V_PADDING = 5;
    public int TEXT_H_PADDING = 5;
    public int type = TYPE_CHAT_OPTIONS;
    public static JCustomMenuPopup instance;
    public JPanel content;
    public JScrollPane scrollPanel;
    private String s = "";
    private String lastCountryName = "";
    public CustomMenu customMenuSelected = null;

    public static JCustomMenuPopup getInstance() {
        return instance;
    }

    public static void setNull() {
        instance = null;
    }

    public void setVisible(JComponent com, int x, int y) {
        instance = this;
        int location_x = (int) com.getLocationOnScreen().getX() - x;
        int location_y = (int) com.getLocationOnScreen().getY() - y;
        setLocation(location_x, location_y);
        setVisible(true);
    }

    public JCustomMenuPopup(MouseListener mouseListener, int type) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                hideThis2();
            }
        });
        this.mouseListener = mouseListener;
        this.type = type;
        addMouseWheelListener(this);
        initComponent();
    }

    public JCustomMenuPopup(MouseListener mouseListener, KeyListener keyListener, int type) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                hideThis2();
            }
        });
        this.mouseListener = mouseListener;
        this.keyListener = keyListener;
        this.setFocusable(true);
        this.requestFocus();
        this.addKeyListener(keyListener);
        this.type = type;
        addMouseWheelListener(this);
        initComponent();
    }

    public MouseListener getMouseListener() {
        return mouseListener;
    }

    public void setMouseListener(MouseListener mouseListener) {
        this.mouseListener = mouseListener;
    }

    @Override
    protected JRootPane createRootPane() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
            }
        };
        JRootPane rootPane2 = new JRootPane();
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane2.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        return rootPane2;
    }

    public CustomMenu getSelectedMenu() {
        return customMenuSelected;
    }

    private void initComponent() {
        if (this.type == TYPE_CHAT_OPTIONS) {
            img = DesignClasses.return_image(GetImages.POPUP_CHAT_OPTIONS).getImage();
            width = 232;
            height = 244;
            pTOP = 24;
            pBOTTOM = 22;
            pLEFT = 23;
            pRIGHT = 25;
        } else if (this.type == TYPE_CHAT_RECENT) {
            img = DesignClasses.return_image(GetImages.POPUP_CHAT_RECENT).getImage();
            width = 136;
            height = 181;
            pTOP = 26;
            pBOTTOM = 26;
            pLEFT = 26;
            pRIGHT = 26;
        } else if (this.type == TYPE_FRIENDLIST_CATEGORY) {
            img = DesignClasses.return_image(GetImages.POPUP_FRIENDLIST).getImage();
            width = 206;
            height = 181;
            pTOP = 26;
            pBOTTOM = 26;
            pLEFT = 26;
            pRIGHT = 24;
        } /*        else if (this.type == TYPE_FRIENDLIST_CATEGORY) {
         img = DesignClasses.return_image(GetImages.POPUP_FRIENDLIST_CATEGORY).getImage(); //POPUP_FRIENDLIST_CATEGORY
         width = 144;
         height = 267;
         pTOP = 26;
         pBOTTOM = 26;
         pLEFT = 26;
         pRIGHT = 26;
         }*/ else if (this.type == TYPE_UPLOAD_PHOTO) {
            img = DesignClasses.return_image(GetImages.POPUP_UPLOAD_PHOTO).getImage();
            width = 234;
            height = 130;
            pTOP = 25;
            pBOTTOM = 23;
            pLEFT = 19;
            pRIGHT = 25;
            TEXT_H_PADDING = 1;
        } else if (this.type == TYPE_POST_VALIDITY) {
            img = DesignClasses.return_image(GetImages.POPUP_CHAT_RECENT).getImage();
            width = 136;
            height = 181;
            pTOP = 26;
            pBOTTOM = 26;
            pLEFT = 26;
            pRIGHT = 20;
        } else if (this.type == TYPE_EDIT_DELETE) {
            img = DesignClasses.return_image(GetImages.POPUP_EDIT_DELETE_PHOTO).getImage();
            width = 162;
            height = 101;
            pTOP = 25;
            pBOTTOM = 23;
            pLEFT = 19;
            pRIGHT = 25;
            TEXT_H_PADDING = 1;

        } else if (this.type == TYPE_ONLY_DELETE) {
            img = DesignClasses.return_image(GetImages.POPUP_DELETE_PHOTO).getImage();
            width = 162;
            height = 73;
            pTOP = 25;
            pBOTTOM = 23;
            pLEFT = 19;
            pRIGHT = 25;
            TEXT_H_PADDING = 1;
        } else if (this.type == TYPE_EDIT_LEAVE_DELETE) {
            img = DesignClasses.return_image(GetImages.POPUP_EDIT_DELETE_LEAVE_JOIN_PHOTO).getImage();
            width = 200;
            height = 126;
            pTOP = 25;
            pBOTTOM = 23;
            pLEFT = 19;
            pRIGHT = 25;
            TEXT_H_PADDING = 1;

        } else if (this.type == TYPE_JOIN_LEAVE) {
            img = DesignClasses.return_image(GetImages.POPUP_JOIN_LEAVE_PHOTO).getImage();
            width = 200;
            height = 101;
            pTOP = 25;
            pBOTTOM = 23;
            pLEFT = 19;
            pRIGHT = 25;
            TEXT_H_PADDING = 1;

        } else if (this.type == TYPE_ONLY_LEAVE) {
            img = DesignClasses.return_image(GetImages.POPUP_ONLY_LEAVE_PHOTO).getImage();
            width = 200;
            height = 73;
            pTOP = 25;
            pBOTTOM = 23;
            pLEFT = 19;
            pRIGHT = 25;
            TEXT_H_PADDING = 1;
        } else if (this.type == TYPE_STATUS) {
            img = DesignClasses.return_image(GetImages.STATUS_POPUP_BG).getImage();
            width = 162;
            height = 101;
            pTOP = 28;
            pBOTTOM = 20;
            pLEFT = 25;
            pRIGHT = 25;
            TEXT_H_PADDING = 1;
        } else if (this.type == TYPE_SIGN_IN) {
            img = DesignClasses.return_image(GetImages.POPUP_SIGN_IN_PHOTO).getImage();
            width = 310;//290;//194;
            height = 343;//250;
            pTOP = 25;
            pBOTTOM = 23;
            pLEFT = 28;
            pRIGHT = 22;
            TEXT_H_PADDING = 1;
        }
        setUndecorated(true);
        setResizable(false);
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0, 0));
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));

        JPanel panel = new ImagePanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(pTOP, pLEFT, pBOTTOM, pRIGHT));
        panel.setOpaque(false);
        setContentPane(panel);
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowDeactivated(WindowEvent e) {
//                hideThis2();
//            }
//        });

        JPanel wrapper = new JPanel(new BorderLayout());
        //wrapper.setBackground(Color.GREEN);
        wrapper.setOpaque(false);
        panel.add(wrapper, BorderLayout.CENTER);

        content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        if (this.type == TYPE_POST_VALIDITY) {
            scrollPanel = DesignClasses.getDefaultScrollPaneThin(content);
            wrapper.add(scrollPanel);
        } else if (this.type == TYPE_SIGN_IN) {
            scrollPanel = DesignClasses.getDefaultScrollPaneWide(content);
            wrapper.add(scrollPanel);

        } else {
            wrapper.add(content);
        }

    }

    public class ImagePanel extends JPanel {

        public ImagePanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, null);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        hideThis2();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void hideThis() {
        this.setVisible(false);
    }

    public void hideThis2() {
        if (BasicImageViewStructure.editDeleteImageCommentPopup != null) {
            BasicImageViewStructure.clieckedEditDeletepopup = false;
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
//                System.out.println("ddddddddd");
                //System.out.println("sdfsd");
                setVisible(false);
                dispose();
//                if (addEmoticonsJDialog != null) {
//                    addEmoticonsJDialog = null;
//                }
            }
        });

    }

    public CustomMenu addMenu(String text, String image) {
        CustomMenu menu = new CustomMenu(text, image);
        content.add(menu);
        menu.addMouseListener(mouseListener);
        if (keyListener != null) {
            menu.addKeyListener(keyListener);
        }
        return menu;
    }

    public void hideMenu(String text) {
        for (Component menu : content.getComponents()) {
            if (menu.getName().equalsIgnoreCase(text)) {
                menu.setVisible(false);
                break;
            }
        }
    }

    public void showMenu(String text) {
        for (Component menu : content.getComponents()) {
            if (menu.getName().equalsIgnoreCase(text)) {
                menu.setVisible(true);
                break;
            }
        }
    }

    public class CustomMenu extends JPanel {

        public String text;
        public String imageUrl;
        private JPanel mainPanel;
        private JLabel lblText;
        private JPanel lblTextPanel;

        public CustomMenu(String text, String imageUrl) {
            this.text = text;
            this.imageUrl = imageUrl;
            setLayout(new BorderLayout());
            setOpaque(false);
            initContent();

        }

        private void initContent() {
            mainPanel = new JPanel(new BorderLayout());
            //mainPanel.setBackground(Color.WHITE);
            mainPanel.setOpaque(false);
            this.setName(text);

            if (imageUrl != null && imageUrl.length() > 0) {
                JLabel imageLabel = new JLabel();
                //   imageLabel.setBorder(new EmptyBorder(0, 3, 0, 0));
                imageLabel.setOpaque(false);
                imageLabel.setIcon(DesignClasses.return_image(imageUrl));
                add(imageLabel, BorderLayout.WEST);
                mainPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
            }

            lblTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, TEXT_H_PADDING, TEXT_V_PADDING));
            lblTextPanel.setBackground(Color.WHITE);
            lblText = new JLabel(text);
            if (type == TYPE_STATUS) {
                if (MyFnFSettings.userProfile.getPresence() == StatusConstants.PRESENCE_ONLINE && lblText.getText().equalsIgnoreCase("Online")) {
                    lblText.setForeground(DefaultSettings.disable_font_color);
                    lblText.setEnabled(false);
                } else if (MyFnFSettings.userProfile.getPresence() == StatusConstants.PRESENCE_DO_NOT_DISTURB && lblText.getText().equalsIgnoreCase("Offline")) {
                    lblText.setForeground(DefaultSettings.disable_font_color);
                    lblText.setEnabled(false);
                }

            } else {
                lblText.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
            }

            lblText.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            lblTextPanel.add(lblText);
            mainPanel.add(lblTextPanel, BorderLayout.CENTER);
            add(mainPanel, BorderLayout.CENTER);
        }

        public void setText(String text) {
            this.text = text;
            lblText.setText(this.text);
        }

        public void setMouseEntered() {
            lblTextPanel.setBackground(RingColorCode.RING_THEME_COLOR);
            lblText.setForeground(Color.WHITE);
        }

        public void setMouseExited() {
            if (lblText.getText().equalsIgnoreCase("Online") || lblText.getText().equalsIgnoreCase("Offline")) {
                lblText.setForeground(Color.BLACK);
            } else {
                lblText.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
            }
            lblTextPanel.setBackground(Color.WHITE);
        }

    }

}
