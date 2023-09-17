/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.loginsignup;

import com.ipvision.constants.GetImages;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.RingColorCode;
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
import com.ipvision.view.utility.DesignClasses;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Sirat Samyoun
 */
public class SignInSignupCustomPopup extends JDialog implements MouseWheelListener {

    private Image img = null;
    public int width = 0;
    public int height = 0;
    public int pTOP = 0;
    public int pBOTTOM = 0;
    public int pLEFT = 0;
    public int pRIGHT = 0;
    public int TEXT_V_PADDING = 5;
    public int TEXT_H_PADDING = 5;
    private JPanel content;
    private JScrollPane scrollPanel;
    private CustomMenu customMenuSelected = null;
    private SelectionListSigninSignup selectionList;
    private static String FORMAT_PNG = ".png";
    private String s = "";
    private String lastCountryName = "";

    public void setVisible(JComponent com, int x, int y) {
        int location_x = (int) com.getLocationOnScreen().getX() - x;
        int location_y = (int) com.getLocationOnScreen().getY() - y;
        setLocation(location_x, location_y);
        setVisible(true);
    }

    public SignInSignupCustomPopup(SelectionListSigninSignup selectionListSigninSignup) {
        this.selectionList = selectionListSigninSignup;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                hideThis2();
            }
        });
        this.setFocusable(true);
        this.requestFocus();
        this.addKeyListener(keyListener);
        addMouseWheelListener(this);
        img = DesignClasses.return_image(GetImages.POPUP_SIGN_IN_PHOTO).getImage();
        width = 310;//290;//194;
        height = 343;//250;
        pTOP = 25;
        pBOTTOM = 23;
        pLEFT = 28;
        pRIGHT = 22;
        TEXT_H_PADDING = 1;
        initComponent();
        buildAllMenuItem();
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

    private void initComponent() {
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

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        panel.add(wrapper, BorderLayout.CENTER);

        content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        scrollPanel = DesignClasses.getDefaultScrollPaneWide(content);
        wrapper.add(scrollPanel);
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
                setVisible(false);
                dispose();
            }
        });
    }

    public CustomMenu getSelectedMenu() {
        return customMenuSelected;
    }

    public CustomMenu addMenu(String text, String image) {
        CustomMenu menu = new CustomMenu(text, image);
        content.add(menu);
        menu.addMouseListener(menuListener);
        menu.addKeyListener(keyListener);
        return menu;
    }

    public void selectMenu(String text) {
        customMenuSelected = null;
        if (text != null) {
            for (Component menu : content.getComponents()) {
                CustomMenu customMenu = (CustomMenu) menu;
                if (customMenu.getName().equalsIgnoreCase(text)) {
                    customMenuSelected = customMenu;
                } else {
                    customMenu.setMouseExited();
                }
            }
            customMenuSelected.setMouseEntered();
            scrollPanel.getVerticalScrollBar().setValue(0);
            int downValue = -1 * (this.getY() - customMenuSelected.getY());
            if (selectionList.type == SelectionListSigninSignup.TYPE_SIGN_UP) {
                scrollPanel.getVerticalScrollBar().setValue(downValue + 430);
            } else {
                scrollPanel.getVerticalScrollBar().setValue(downValue + 250);
            }
        } else {
            for (Component menu : content.getComponents()) {
                CustomMenu customMenu = (CustomMenu) menu;
                customMenu.setMouseExited();
            }
        }
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

    MouseListener menuListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            CustomMenu menu = (CustomMenu) e.getSource();
            selectMenu(null);
            menu.setMouseEntered();
            //selectMenu(menu); 
//            customMenuSelected = menu;
//            customMenuSelected.setMouseEntered();
            selectionList.action_mouse_clicked(menu);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            CustomMenu menu = (CustomMenu) e.getSource();
            if (menu != null) {
                //selectMenu(null);
                menu.setMouseEntered();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            CustomMenu menu = (CustomMenu) e.getSource();
            if (menu != null) {
                menu.setMouseExited();
            }
        }

    };

    KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                CustomMenu menu = getSelectedMenu();
                selectionList.action_mouse_clicked(menu);
                selectMenu(null);
            } else {
                char key = (char) e.getKeyCode();
                selectTypedItem("" + key);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

    };

    private int getIndexinArray(String p) {
        int length = MyFnFSettings.COUNTRY_MOBILE_CODE.length;
        for (int i = 0; i < length; i++) {
            String countryName = MyFnFSettings.COUNTRY_MOBILE_CODE[i][0];
            if (countryName.equals(p)) {
                return i;
            }
        }
        return -1;
    }

    private void buildAllMenuItem() {
        int length = MyFnFSettings.COUNTRY_MOBILE_CODE.length;
        int startIndx = 0;
        if (selectionList.type == SelectionListSigninSignup.TYPE_SIGN_UP) {
            startIndx = 1;
        }
        for (int i = startIndx; i < length; i++) {
            String countryName = MyFnFSettings.COUNTRY_MOBILE_CODE[i][0];
            String imageSource = GetImages.FLAGS_ROOT_FOLDER + countryName + FORMAT_PNG;
            addMenu(countryName, imageSource);
        }
    }

    private void selectTypedItem(String str) {
        int maxLength = MyFnFSettings.COUNTRY_MOBILE_CODE.length;
        if (s.equals(str)) {
            int indx = getIndexinArray(lastCountryName) + 1;
            if (indx > 0 && indx < maxLength && MyFnFSettings.COUNTRY_MOBILE_CODE[indx][0].startsWith(s)) {
                String countryName = MyFnFSettings.COUNTRY_MOBILE_CODE[indx][0];
                lastCountryName = countryName;
                selectMenu(countryName);
                return;
            } else {
                s = str;
                for (int i = 0; i < maxLength; i++) {
                    String countryName = MyFnFSettings.COUNTRY_MOBILE_CODE[i][0];
                    if (countryName.startsWith(s)) {
                        lastCountryName = countryName;
                        selectMenu(countryName);
                        return;
                    }
                }
            }
        } else {
            s = str;
            for (int i = 0; i < maxLength; i++) {
                String countryName = MyFnFSettings.COUNTRY_MOBILE_CODE[i][0];
                if (countryName.startsWith(s)) {
                    lastCountryName = countryName;
                    selectMenu(countryName);
                    return;
                }
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
            this.setFocusable(true);
            setLayout(new BorderLayout());
            setOpaque(false);
            initContent();
        }

        private void initContent() {
            mainPanel = new JPanel(new BorderLayout());
            mainPanel.setOpaque(false);
            this.setName(text);
            if (imageUrl != null && imageUrl.length() > 0) {
                JLabel imageLabel = new JLabel();
                imageLabel.setOpaque(false);
                imageLabel.setIcon(DesignClasses.return_image(imageUrl));
                add(imageLabel, BorderLayout.WEST);
                mainPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
            }
            lblTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, TEXT_H_PADDING, TEXT_V_PADDING));
            lblTextPanel.setBackground(Color.WHITE);
            lblText = new JLabel(text);
            lblText.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
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
