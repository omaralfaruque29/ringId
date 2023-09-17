/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

import com.ipvision.model.stores.StickerStorer;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.EmotionDtos;
import com.ipvision.view.group.GroupPanel;
import com.ipvision.model.stores.HashMapsBeforeLogin;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.view.utility.DefaultSettings;

/**
 *
 * @author Shahadat
 */
public class JDialogStickerPanel extends JDialog {

    private Image img = DesignClasses.return_image(GetImages.STICKERS_POPUP).getImage();
    public JDialogStickerPanel instance;
    private JPanel wrapperPanel;
    private JPanel bottomPanel;
    private JPanel stickerContainer;
    private JPanel mainPanel;
    private JScrollPane containerPanelScroll;
    private JTextArea actionTextFields;
    private JLabel titleLabel;
    private JLabel imageNameLabel;
    public static JButton[] buttons = new JButton[20];
    public static final int TYPE_FRIEND = 1;
    public static final int TYPE_GROUP = 2;
    private String friendIdentity;
    private Long groupId;
    private JButton stkmkt;
    int totalGroup = 1;
    int selectedGroup = 0;
    private Color bg_color_all = null;
    private Color image_hover_border = Color.LIGHT_GRAY;
    private int POPUP_WIDTH = 361;
    private int POPUP_HEIGHT = 393;

    public JDialogStickerPanel getInstance() {
        return instance;
    }

    public void setVisible(JComponent com, int x) {
        int location_x = (int) com.getLocationOnScreen().getX() - x - 10;
        int location_y = (int) com.getLocationOnScreen().getY() - 381; //350;
        setLocation(location_x, location_y);
        stkmkt.setVisible(GuiRingID.getInstance() == null || GuiRingID.getInstance().getMainButtons().getWidth() <= DefaultSettings.RIGHT_RING_TOOLS_COLLAPSE_WIDTH);
        setVisible(true);
        loadEmoticon();
    }

    public JDialogStickerPanel(JTextArea actionTextFields, String userId) {
        this.actionTextFields = actionTextFields;
        this.friendIdentity = userId;
        instance = this;
        initComponent();
    }

    public JDialogStickerPanel(JTextArea actionTextFields, Long groupId) {
        this.actionTextFields = actionTextFields;
        this.groupId = groupId;
        instance = this;
        initComponent();
    }

    private void initComponent() {
        setUndecorated(true);
        setResizable(false);
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0, 0));
        setMinimumSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
        setMaximumSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
        setLocation(300, 300);

        JPanel panel = new ImagePanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(35, 35, 30, 35));
        panel.setOpaque(false);
        setContentPane(panel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                hideThis();
            }
        });

        wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setLayout(new BorderLayout());
        wrapperPanel.setOpaque(false);
        panel.add(wrapperPanel, BorderLayout.CENTER);

        buildHeaderPanel();
        buildCategoryPanel();
        buildBodyPanel();
    }

    private void buildHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        headerPanel.setPreferredSize(new Dimension(0, 31));
        headerPanel.setOpaque(false);
        wrapperPanel.add(headerPanel, BorderLayout.NORTH);

        titleLabel = new JLabel();
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        headerPanel.add(titleLabel);
    }

    private void buildCategoryPanel() {
        bottomPanel = new JPanel(new BorderLayout(0, 0));
        bottomPanel.setPreferredSize(new Dimension(0, 37)); //35
        bottomPanel.setOpaque(false);
        wrapperPanel.add(bottomPanel, BorderLayout.SOUTH);

        JPanel imageNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        imageNamePanel.setOpaque(false);
        bottomPanel.add(imageNamePanel, BorderLayout.WEST);
        
        imageNameLabel = new JLabel();
        imageNameLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        imageNamePanel.add(imageNameLabel);
        
        stkmkt = DesignClasses.createImageButton(GetImages.ADD_MORE_STICKER, GetImages.ADD_MORE_STICKER_H, "Sticker");
        stkmkt.setCursor(new Cursor(Cursor.HAND_CURSOR));
        stkmkt.addActionListener(actionListener);
        JPanel marketPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 1));
        marketPanel.setPreferredSize(new Dimension(35, 35));
        marketPanel.add(stkmkt);
        marketPanel.setOpaque(false);
        bottomPanel.add(marketPanel, BorderLayout.EAST);
    }

    private void buildBodyPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(3, 2, 2, 0));
        mainPanel.setBackground(Color.WHITE);
        wrapperPanel.add(mainPanel, BorderLayout.CENTER);

        stickerContainer = new JPanel();
        stickerContainer.setOpaque(false);

        JPanel wPanel = new JPanel();
        wPanel.setLayout(new BorderLayout(0, 0));
        wPanel.setOpaque(false);
        wPanel.add(stickerContainer, BorderLayout.NORTH);

        containerPanelScroll = DesignClasses.getDefaultScrollPaneThin(wPanel);// DesignClasses.getDefaultScorllPane(chatContainerPanel);
        containerPanelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(containerPanelScroll, BorderLayout.CENTER);
    }

    private void focus_in_chat_box() {
        if (actionTextFields != null) {
            actionTextFields.requestFocus();
            actionTextFields.grabFocus();
            actionTextFields.requestFocusInWindow();
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

    private void setext_in_labels(String name) {
        imageNameLabel.setText(name);
    }
    
    public void hideThis() {
        this.setVisible(false);
    }

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == stkmkt) {
                instance.setVisible(false);
                if (GuiRingID.getInstance() != null) {
                    GuiRingID.getInstance().getTopMenuBar().setExpand();
                }
            }
        }
    };

    public void loadEmoticon() {
        stickerContainer.removeAll();
        int per_row = 9;
        titleLabel.setText("Emoticons");
        titleLabel.setToolTipText("Emoticons");

        if (!HashMapsBeforeLogin.getInstance().getEmotionHashmap().isEmpty()) {
            stickerContainer.setLayout(new GridLayout(0, per_row));
            for (String imgDto : HashMapsBeforeLogin.getInstance().getEmotionHashmap().keySet()) {
                final EmotionDtos dtos = HashMapsBeforeLogin.getInstance().getEmotionHashmap().get(imgDto);
                String src = dtos.getUrl();

                ImageIcon icon = StickerStorer.getInstance().getChatEmoticonsImageIconMap().get(src);
                if (icon == null) {
                    icon = DesignClasses.return_emoticon(src);
                    StickerStorer.getInstance().getChatEmoticonsImageIconMap().put(src, icon);
                }
                
                try {
                    final String symbol = !dtos.getSymbol().contains("&lt;") ? dtos.getSymbol() : dtos.getSymbol().replace("&lt;", "<");
                    final JLabel label = new JLabel(icon);
                    label.setToolTipText(symbol);
                    label.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, bg_color_all));
                    label.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            label.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, image_hover_border));
                            if (friendIdentity != null && friendIdentity.trim().length() > 0) {
                                MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(friendIdentity);
                                friendChatCallPanel.myFriendChatPanel.chatWriter.setText(friendChatCallPanel.myFriendChatPanel.chatWriter.getText() + " " + symbol + " ");
                                friendChatCallPanel.myFriendChatPanel.chatWriter.grabFocus();
                                friendChatCallPanel.myFriendChatPanel.chatWriter.requestFocus();
                                friendChatCallPanel.myFriendChatPanel.chatWriter.requestFocusInWindow();
                            } else if (groupId != null && groupId > 0) {
                                GroupPanel groupPanel = MyfnfHashMaps.getInstance().getGroupPanelMap().get(groupId);
                                groupPanel.chatWriter.setText(groupPanel.chatWriter.getText() + " " + symbol + " ");
                                groupPanel.chatWriter.grabFocus();
                                groupPanel.chatWriter.requestFocus();
                                groupPanel.chatWriter.requestFocusInWindow();
                            }
                            focus_in_chat_box();
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {
                            label.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, bg_color_all));
                            setext_in_labels(null);
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            label.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, image_hover_border));
                            setext_in_labels(dtos.getName());
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            label.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, bg_color_all));
                            setext_in_labels(null);
                        }
                    });
                    stickerContainer.add(label);
                } catch (Exception ex) {
                    //  Logger.getLogger(EmotionPopup.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            stickerContainer.repaint();
            stickerContainer.revalidate();
            mainPanel.revalidate();
            mainPanel.repaint();

            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    containerPanelScroll.getVerticalScrollBar().setValue(0);
                    containerPanelScroll.getHorizontalScrollBar().setValue(0);
                    containerPanelScroll.revalidate();
                    containerPanelScroll.repaint();
                }
            });
        }
    }

}
