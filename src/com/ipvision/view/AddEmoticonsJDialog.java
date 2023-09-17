/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipvision.model.EmotionDtos;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import com.ipvision.model.stores.StickerStorer;
import com.ipvision.model.stores.HashMapsBeforeLogin;

/**
 *
 * @author Sirat Samyoun
 */
public class AddEmoticonsJDialog extends JDialog implements MouseWheelListener {

    private final Image img;
    //public static AddEmoticonsJDialog instance;
    private JPanel wrapperPanel;
    private JPanel stickerContainer;
    private JPanel mainPanel;
    private JScrollPane containerPanelScroll;
    private JTextArea actionTextFields;
    private JLabel titleLabel;
    private JLabel imageNameLabel;//, imageSymbol;

    private Color bg_color_all = null;
    private Color image_hover_border = Color.LIGHT_GRAY;
    private int type_int;
    public static AddEmoticonsJDialog currentInstance = null;
    private int POPUP_WIDTH = 301;//+ 35;
    private int POPUP_HEIGHT = 361;//+ 41;

    public void setVisible(JComponent com) { //y reversely increase in both
        currentInstance = this;
        int location_x, location_y;
        if (type_int == 0) {  //lower half
            location_x = (int) com.getLocationOnScreen().getX() - 234;
            location_y = (int) com.getLocationOnScreen().getY() - 344;
        } else {   //upper half pointing above
            location_x = (int) com.getLocationOnScreen().getX() - 233;
            location_y = (int) com.getLocationOnScreen().getY() + 18;
        }
        setLocation(location_x, location_y);
        setVisible(true);

    }

    public void setVisible(JComponent com, boolean isStatus) {
        currentInstance = this;
        int location_x, location_y;
        if (type_int == 0) {
            location_x = (int) com.getLocationOnScreen().getX() - 484;
            location_y = (int) com.getLocationOnScreen().getY() - 304;
        } else { //upper half status
            location_x = (int) com.getLocationOnScreen().getX() - 239;
            location_y = (int) com.getLocationOnScreen().getY() + 15;
        }
        setLocation(location_x, location_y);
        setVisible(true);
        //loadDefaultEmoticonSticker(0);

    }

    public AddEmoticonsJDialog(JTextArea actionTextFields, int type_int) {
        if (type_int == 0) {
            img = DesignClasses.return_image(GetImages.EMOTICONS_POPUP_ARROW_DOWN).getImage();
        } else {
            img = DesignClasses.return_image(GetImages.EMOTICONS_POPUP_ARROW_UP).getImage();
        }
        this.actionTextFields = actionTextFields;
        this.type_int = type_int;
        setUndecorated(true);
        setResizable(false);
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0, 0));
        setMinimumSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
        setMaximumSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
        setLocation(300, 300);
        addMouseWheelListener(this);
        initComponent();
    }
    /*        setMinimumSize(new Dimension(280, 341)); //245, 300
     setMaximumSize(new Dimension(280, 341));*/

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

    private void initComponent() {
        JPanel panel = new ImagePanel();
        panel.setLayout(new BorderLayout());
        //panel.setBorder(new EmptyBorder(9, 7, 10, 8));
        panel.setBorder(new EmptyBorder(30, 34, 30, 32));
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
        // wrapperPanel.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
        panel.add(wrapperPanel, BorderLayout.CENTER);

        buildHeaderPanel();
        buildCategoryPanel();
        buildBodyPanel();
        loadDefaultEmoticonSticker();
    }

    private void buildHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        //headerPanel.setBorder(new EmptyBorder(0, 3, 3, 3));
        headerPanel.setPreferredSize(new Dimension(0, 35));
        headerPanel.setOpaque(false);
        wrapperPanel.add(headerPanel, BorderLayout.NORTH);

        titleLabel = new JLabel();
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        //titleLabel.setPreferredSize(new Dimension(280, 31));
        headerPanel.add(titleLabel);
    }

    private void buildCategoryPanel() {
        JPanel hoverPanel = new JPanel(new BorderLayout(0, 0));
        hoverPanel.setBorder(new EmptyBorder(8, 5, 5, 5));
        hoverPanel.setPreferredSize(new Dimension(0, 28));
        hoverPanel.setOpaque(false);
        //hoverPanel.setBackground(Color.WHITE); 
        imageNameLabel = new JLabel();//DesignClasses.makeJlabel_no_bonds(null, 11, 1, Color.WHITE, bg_color_all, 2);
        imageNameLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        //imageSymbol = new JLabel();//DesignClasses.makeJlabel_no_bonds(null, 11, 1, Color.WHITE, bg_color_all, 4);
        JPanel imageNamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imageNamePanel.setOpaque(false);
        imageNamePanel.setPreferredSize(new Dimension(105, 28));
        imageNamePanel.add(imageNameLabel);
        hoverPanel.add(imageNamePanel, BorderLayout.CENTER);
        // hoverPanel.add(imageSymbol, BorderLayout.EAST);
        //hoverPanel.setBorder(new MatteBorder(1, 0, 0, 0, DefaultSettings.ORANGE_BACKGROUND_COLOR));
        wrapperPanel.add(hoverPanel, BorderLayout.SOUTH);
    }

    private void buildBodyPanel() {
        mainPanel = new JPanel(new BorderLayout());
        //mainPanel.setBorder(new EmptyBorder(3, 2, 2, 0));
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
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        hideThis();
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

    public void hideThis() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(false);
                dispose();
                currentInstance = null;
            }
        });

    }

    private void setext_in_labels(String name) {
        imageNameLabel.setText(name);
    }

    public void loadDefaultEmoticonSticker() {
        stickerContainer.removeAll();
        int per_row = 7;
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
                            if (actionTextFields.getForeground() == DefaultSettings.disable_font_color) {
                                actionTextFields.setText("");
                                actionTextFields.setForeground(null);
                            }
                            actionTextFields.setText(actionTextFields.getText() + " " + symbol + " ");
                            hideThis();
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
                            //focus_in_chat_box();
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
