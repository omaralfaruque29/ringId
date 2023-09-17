/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.addfriend;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.model.SearchItem;
import com.ipvision.model.constants.StaticFields;
import com.ipvision.model.constants.StatusConstants;
import com.ipvision.view.utility.UiMethods;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.ipvision.service.SearchThread;
import javax.swing.SwingUtilities;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.SearchFriendsByName;
import com.ipvision.view.utility.ContextMenuMouseListener;
import com.ipvision.view.utility.ImagePane;
import com.ipvision.view.utility.SeparatorPanel;
import utils.SearchStringQueue;

/**
 *
 * @author Wasif Islam
 */
public class SearchFriendPanel extends JPanel implements MouseListener {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SearchFriendPanel.class);
    private JPanel categoryPanel;
    private JPanel mainPanel;

    private Color defaultBgColor =  Color.WHITE;//RingColorCode.DEFAULT_BACKGROUND_COLOR;
    private Color selectedBgColor = Color.WHITE;

    private ImagePane nameTabPane = new ImagePane();
    private ImagePane phoneTabPane = new ImagePane();
    private ImagePane emailTabPane = new ImagePane();
    private ImagePane ringidTabPane = new ImagePane();
    private ImagePane locationTabPane = new ImagePane();

    private JLabel nameLbl = new JLabel();
    private JLabel phoneLbl = new JLabel();
    private JLabel emailLbl = new JLabel();
    private JLabel ringidLbl = new JLabel();
    private JLabel locationLbl = new JLabel();

    private String DEFAULT_STR_TYPE_NAME = "Search by name";
    private String DEFAULT_STR_TYPE_PHONE = "Search by phone number";
    private String DEFAULT_STR_TYPE_EMAIL = "Search by Email";
    private String DEFAULT_STR_TYPE_RINGID = "Search by ringID";
    private String DEFAULT_STR_TYPE_LOCATION = "Search by location";
    private int SELECTED_MENU = StatusConstants.SEARCH_BY_ALL;
    private String SELECTED_STR = "";

    private Color defaultFontColor = Color.BLACK;
    private Color selectedFontColor = RingColorCode.RING_THEME_COLOR;

    private Border selectedBorder = new MatteBorder(0, 0, 2, 0, RingColorCode.RING_THEME_COLOR);

    public JTextField searchTextField = DesignClasses.makeTextFieldLimitedTextSize(StaticFields.SEARCH_FRIENDS, 150, 30, 100);
    private UiMethods extra_methods;
    private JPanel scrollContent;
    private SingleAddFriendPanel single;
    private static int sepWidth = 1, panelHeight = 50, topBottomgap = 8;

    public SearchFriendPanel() {
        setLayout(new BorderLayout(DefaultSettings.DEFAULT_BORDER_WIDTH, DefaultSettings.DEFAULT_BORDER_WIDTH));
        setBackground(Color.WHITE);
        extra_methods = new UiMethods();

        categoryPanel = new JPanel();
        categoryPanel.setLayout(new GridLayout(0, 5, 0, 0));
        categoryPanel.setOpaque(false);
        categoryPanel.setPreferredSize(new Dimension(0, DefaultSettings.ADD_FRIEND_CATEGORY_HEIGHT));
        categoryPanel.setBorder(new MatteBorder(0, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR));
        add(categoryPanel, BorderLayout.NORTH);

        mainPanel = new JPanel();
        //mainPanel.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH, DefaultSettings.ADD_FRIEND_CONTENT_PANEL_HEIGHT));
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(selectedBgColor);
        add(mainPanel, BorderLayout.CENTER);

        init();

        SELECTED_MENU = StatusConstants.SEARCH_BY_NAME;
        SELECTED_STR = DEFAULT_STR_TYPE_NAME;
        resetMainButtonsColor(SELECTED_STR);

    }

    private void init() {
        categoryPanel.add(getSingleCategoryPane(nameTabPane, nameLbl, "Name", true));
        categoryPanel.add(getSingleCategoryPane(phoneTabPane, phoneLbl, "Phone", true));
        categoryPanel.add(getSingleCategoryPane(emailTabPane, emailLbl, "Email", true));
        categoryPanel.add(getSingleCategoryPane(ringidTabPane, ringidLbl, "RingID", true));
        categoryPanel.add(getSingleCategoryPane(locationTabPane, locationLbl, "Location", false));

        /*
         JPanel createSearchbox = new JPanel() {
         @Override
         protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         Graphics2D g2d = (Graphics2D) g;
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
         int w = getWidth();
         int h = getHeight();
         g2d.setColor(RingColorCode.RING_THEME_COLOR);
         Area border = new Area(new RoundRectangle2D.Double(0, 0, w, h, 10, 10));
         g2d.fill(border);

         g2d.setColor(Color.WHITE);
         Area shape = new Area(new RoundRectangle2D.Double(0.5, 0.5, w - 1, h - 1, 9, 9));
         g2d.fill(shape);
         }
         };
         createSearchbox.setLayout(new BoxLayout(createSearchbox, BoxLayout.X_AXIS));
         JPanel searchPanelHolder = new JPanel(new BorderLayout());
         searchPanelHolder.setOpaque(false);
         searchPanelHolder.setBorder(new EmptyBorder(25, 100, 25, 100));
         searchPanelHolder.add(createSearchbox, BorderLayout.CENTER);
         mainPanel.add(searchPanelHolder, BorderLayout.NORTH);
         createSearchbox.setOpaque(false);
         JLabel search_image = DesignClasses.create_imageJlabel(GetImages.SEARCH_IMG);
         search_image.setBorder(new EmptyBorder(0, DefaultSettings.LEFT_EMPTY_SPACE - 5, 0, 0));
         createSearchbox.add(search_image);
         createSearchbox.add(searchTextField);
        
         */
        Border border = new EmptyBorder(25, 100, 25, 100);
        SearchFriendsByName searchFriendsByName = new SearchFriendsByName(searchTextField, border);
        mainPanel.add(searchFriendsByName, BorderLayout.NORTH);
        searchTextField.setBorder(new EmptyBorder(0, 5, 0, 0));
        searchTextField.setOpaque(false);
        searchTextField.setText(SELECTED_STR);
        searchTextField.setForeground(DefaultSettings.disable_font_color);
        searchTextField.addMouseListener(new ContextMenuMouseListener());
        searchTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                extra_methods.set_reset_defalut_text(searchTextField, SELECTED_STR, true);
            }

            @Override
            public void focusGained(FocusEvent e) {
                if (searchTextField.getText().length() <= 0 || searchTextField.getText().equals(SELECTED_STR)) {
                    extra_methods.set_reset_defalut_text(searchTextField, SELECTED_STR, false);
                } else if (searchTextField.getText().length() < 1) {
                    extra_methods.set_reset_defalut_text(searchTextField, SELECTED_STR, true);
                }

            }
        });
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                searchFromServer();
                if (searchTextField.getText().length() < 1) {
                    MyfnfHashMaps.getInstance().getInviteFriendsContainer().clear();
                    addSearchResultInContainer();
                }

            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                searchFromServer();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchFromServer();
            }
        });

        add_search_content_panel();
    }

    private void add_search_content_panel() {
        JPanel searchContentPanel = new JPanel();
        searchContentPanel.setLayout(new BorderLayout());
        searchContentPanel.setOpaque(false);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setOpaque(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(scrollContent, BorderLayout.NORTH);

        wrapper.add(panel);

        JScrollPane scrollPane = DesignClasses.getDefaultScrollPane(wrapper);
        searchContentPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(searchContentPanel, BorderLayout.CENTER);
        addSearchResultInContainer();
    }

    public void get_all_matches(String search_string) {
        if (search_string != null && search_string.length() > 0) {
            SearchItem item = new SearchItem();
            item.setSearch_string(search_string);
            item.setSearch_time(System.currentTimeMillis());
            item.setUser_id(MyFnFSettings.LOGIN_USER_ID);
            item.setSession_id(MyFnFSettings.LOGIN_SESSIONID);
            // item.setInvite_frnd(this);
            SearchStringQueue.getInstance().push(item);
        }
    }

    public void remove_content(final String userId) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SingleAddFriendPanel single = MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().get(userId);
                if (single != null) {
                    scrollContent.remove(single);
                    MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().remove(userId);
                    scrollContent.revalidate();
                    scrollContent.repaint();
                } else {
                    addSearchResultInContainer();
                }
            }
        });
    }

    public void addSearchResultInContainer() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                scrollContent.removeAll();
                try {
                    if (!MyfnfHashMaps.getInstance().getInviteFriendsContainer().isEmpty() && searchTextField.getText().length() > 0) {
                        if (MyfnfHashMaps.getInstance().getInviteFriendsContainer().get(SELECTED_MENU) != null) {
                            for (String key : MyfnfHashMaps.getInstance().getInviteFriendsContainer().get(SELECTED_MENU).keySet()) {
                                UserBasicInfo json = MyfnfHashMaps.getInstance().getInviteFriendsContainer().get(SELECTED_MENU).get(key);
                                String searched_param = "";
                                if (SELECTED_MENU == StatusConstants.SEARCH_BY_NAME && json.getFullName() != null) {
                                    searched_param += json.getFullName().toLowerCase();
                                } else if (SELECTED_MENU == StatusConstants.SEARCH_BY_PHONE && json.getMobilePhone() != null && json.getMobilePhoneDialingCode() != null) {
                                    searched_param += (json.getMobilePhoneDialingCode() + json.getMobilePhone()).toLowerCase();
                                } else if (SELECTED_MENU == StatusConstants.SEARCH_BY_EMAIL && json.getEmail() != null) {
                                    searched_param += json.getEmail().toLowerCase();
                                } else if (SELECTED_MENU == StatusConstants.SEARCH_BY_RINGID && json.getUserIdentity() != null) {
                                    searched_param += json.getUserIdentity().toLowerCase();
                                } else if (SELECTED_MENU == StatusConstants.SEARCH_BY_LOCATION && json.getHomeCity() != null && json.getCurrentCity() != null) {
                                    searched_param += (json.getHomeCity() + json.getCurrentCity()).toLowerCase();
                                }
                                if (FriendList.getInstance().getFriend_hash_map().get(key) == null) {
                                    if (searched_param.contains(searchTextField.getText().toLowerCase())) {
                                        final UserBasicInfo entity = MyfnfHashMaps.getInstance().getInviteFriendsContainer().get(SELECTED_MENU).get(key);
                                        single = MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().get(entity.getUserIdentity());
                                        if (single == null) {
                                            single = new SingleAddFriendPanel(entity);
                                            MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().put(entity.getUserIdentity(), single);
                                        }
                                        if (single.mouseListener != null) {
                                            single.removeMouseListener(single.mouseListener);
                                        }
                                        single.mouseListener = new SinglePanelMouseListener(single, SingleActionPanel.TYPE_INVITE_REQUEST);
                                        single.addMouseListener(single.mouseListener);
                                        if (SELECTED_MENU == StatusConstants.SEARCH_BY_NAME && json.getFullName() != null) {
                                            // single.friendShipStatusLabel.setText("");
                                        } else if (SELECTED_MENU == StatusConstants.SEARCH_BY_PHONE
                                                && json.getMobilePhone() != null
                                                && json.getMobilePhoneDialingCode() != null) {
                                            single.friendShipStatusLabel.setText(json.getMobilePhoneDialingCode() + "-" + json.getMobilePhone());
                                        } else if (SELECTED_MENU == StatusConstants.SEARCH_BY_EMAIL && json.getEmail() != null) {
                                            single.friendShipStatusLabel.setText(json.getEmail());
                                        } else if (SELECTED_MENU == StatusConstants.SEARCH_BY_RINGID && json.getUserIdentity() != null) {
                                            single.friendShipStatusLabel.setText(json.getUserIdentity());
                                        } else if (SELECTED_MENU == StatusConstants.SEARCH_BY_LOCATION && json.getHomeCity() != null && json.getCurrentCity() != null) {
                                            single.friendShipStatusLabel.setText("Home City: " + json.getHomeCity() + ", Current City: " + json.getCurrentCity());
                                        }
                                        // System.out.println(single.friendShipStatusLabel.getText());//single.friendShipStatusLabel.getText();
                                        single.fullNameWhatInmindWrapper.revalidate();
                                        single.fullNameWhatInmindWrapper.repaint();
                                        single.friendInfoPanel.setVisible(true);
                                        single.buttonActionPanel.setVisible(false);
                                        scrollContent.add(single);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    // ex.printStackTrace();
                    log.error("Error in addSearchResultInContainer ==>" + ex.getMessage());
                }
                scrollContent.revalidate();
                scrollContent.repaint();
            }
        });
    }

    private ImagePane getSingleCategoryPane(ImagePane category, JLabel lbl, String str, boolean addSeparator) {
        category.setLayout(new BorderLayout());
        if (addSeparator) {
            category.add(new SeparatorPanel(sepWidth, panelHeight, topBottomgap), BorderLayout.EAST);
        }
        lbl.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        lbl.setForeground(Color.BLACK);
        lbl.setText(str);
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 13));
        pnl.setOpaque(false);
        pnl.add(lbl);
        category.add(pnl, BorderLayout.CENTER);
        category.setBackground(defaultBgColor);
        category.setToolTipText(str);
        category.setRounded(false);
        category.addMouseListener(this);
        return category;
    }

    /*    private JPanel getCustomSeperator() {
     JPanel seperator = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
     seperator.setBackground(RingColorCode.FEED_BORDER_COLOR);
     seperator.setPreferredSize(new Dimension(1, 1));
     return seperator;
     }*/
    private void searchFromServer() {
        if (SearchThread.getStatus() == false) {
            SearchThread thread = new SearchThread(SELECTED_MENU);
            SearchThread.startSearchThread();
            thread.start();
        } else {
            SearchThread.setSearchCategory(SELECTED_MENU);
        }
        get_all_matches(searchTextField.getText());
    }

    public void resetMainButtonsColor(String selectedMenu) {
        if (selectedMenu == null) {
            nameTabPane.setBackground(defaultBgColor);
            phoneTabPane.setBackground(defaultBgColor);
            emailTabPane.setBackground(defaultBgColor);
            ringidTabPane.setBackground(defaultBgColor);
            locationTabPane.setBackground(defaultBgColor);

            nameLbl.setForeground(defaultFontColor);
            phoneLbl.setForeground(defaultFontColor);
            emailLbl.setForeground(defaultFontColor);
            ringidLbl.setForeground(defaultFontColor);
            locationLbl.setForeground(defaultFontColor);

            nameTabPane.setBorder(null);
            phoneTabPane.setBorder(null);
            emailTabPane.setBorder(null);
            ringidTabPane.setBorder(null);
            locationTabPane.setBorder(null);
        } else if (selectedMenu.equalsIgnoreCase(DEFAULT_STR_TYPE_NAME)) {
            nameTabPane.setBackground(selectedBgColor);
            phoneTabPane.setBackground(defaultBgColor);
            emailTabPane.setBackground(defaultBgColor);
            ringidTabPane.setBackground(defaultBgColor);
            locationTabPane.setBackground(defaultBgColor);

            nameLbl.setForeground(selectedFontColor);
            phoneLbl.setForeground(defaultFontColor);
            emailLbl.setForeground(defaultFontColor);
            ringidLbl.setForeground(defaultFontColor);
            locationLbl.setForeground(defaultFontColor);

            nameTabPane.setBorder(selectedBorder);
            phoneTabPane.setBorder(null);
            emailTabPane.setBorder(null);
            ringidTabPane.setBorder(null);
            locationTabPane.setBorder(null);
        } else if (selectedMenu.equalsIgnoreCase(DEFAULT_STR_TYPE_PHONE)) {
            nameTabPane.setBackground(defaultBgColor);
            phoneTabPane.setBackground(selectedBgColor);
            emailTabPane.setBackground(defaultBgColor);
            ringidTabPane.setBackground(defaultBgColor);
            locationTabPane.setBackground(defaultBgColor);

            nameLbl.setForeground(defaultFontColor);
            phoneLbl.setForeground(selectedFontColor);
            emailLbl.setForeground(defaultFontColor);
            ringidLbl.setForeground(defaultFontColor);
            locationLbl.setForeground(defaultFontColor);

            nameTabPane.setBorder(null);
            phoneTabPane.setBorder(selectedBorder);
            emailTabPane.setBorder(null);
            ringidTabPane.setBorder(null);
            locationTabPane.setBorder(null);
        } else if (selectedMenu.equalsIgnoreCase(DEFAULT_STR_TYPE_EMAIL)) {
            nameTabPane.setBackground(defaultBgColor);
            phoneTabPane.setBackground(defaultBgColor);
            emailTabPane.setBackground(selectedBgColor);
            ringidTabPane.setBackground(defaultBgColor);
            locationTabPane.setBackground(defaultBgColor);

            nameLbl.setForeground(defaultFontColor);
            phoneLbl.setForeground(defaultFontColor);
            emailLbl.setForeground(selectedFontColor);
            ringidLbl.setForeground(defaultFontColor);
            locationLbl.setForeground(defaultFontColor);

            nameTabPane.setBorder(null);
            phoneTabPane.setBorder(null);
            emailTabPane.setBorder(selectedBorder);
            ringidTabPane.setBorder(null);
            locationTabPane.setBorder(null);
        } else if (selectedMenu.equalsIgnoreCase(DEFAULT_STR_TYPE_RINGID)) {
            nameTabPane.setBackground(defaultBgColor);
            phoneTabPane.setBackground(defaultBgColor);
            emailTabPane.setBackground(defaultBgColor);
            ringidTabPane.setBackground(selectedBgColor);
            locationTabPane.setBackground(defaultBgColor);

            nameLbl.setForeground(defaultFontColor);
            phoneLbl.setForeground(defaultFontColor);
            emailLbl.setForeground(defaultFontColor);
            ringidLbl.setForeground(selectedFontColor);
            locationLbl.setForeground(defaultFontColor);

            nameTabPane.setBorder(null);
            phoneTabPane.setBorder(null);
            emailTabPane.setBorder(null);
            ringidTabPane.setBorder(selectedBorder);
            locationTabPane.setBorder(null);
        } else if (selectedMenu.equalsIgnoreCase(DEFAULT_STR_TYPE_LOCATION)) {
            nameTabPane.setBackground(defaultBgColor);
            phoneTabPane.setBackground(defaultBgColor);
            emailTabPane.setBackground(defaultBgColor);
            ringidTabPane.setBackground(defaultBgColor);
            locationTabPane.setBackground(selectedBgColor);

            nameLbl.setForeground(defaultFontColor);
            phoneLbl.setForeground(defaultFontColor);
            emailLbl.setForeground(defaultFontColor);
            ringidLbl.setForeground(defaultFontColor);
            locationLbl.setForeground(selectedFontColor);

            nameTabPane.setBorder(null);
            phoneTabPane.setBorder(null);
            emailTabPane.setBorder(null);
            ringidTabPane.setBorder(null);
            locationTabPane.setBorder(selectedBorder);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == nameTabPane) {
            SELECTED_MENU = StatusConstants.SEARCH_BY_NAME;
            SELECTED_STR = DEFAULT_STR_TYPE_NAME;
        } else if (e.getSource() == phoneTabPane) {
            SELECTED_MENU = StatusConstants.SEARCH_BY_PHONE;
            SELECTED_STR = DEFAULT_STR_TYPE_PHONE;
        } else if (e.getSource() == emailTabPane) {
            SELECTED_MENU = StatusConstants.SEARCH_BY_EMAIL;
            SELECTED_STR = DEFAULT_STR_TYPE_EMAIL;
        } else if (e.getSource() == ringidTabPane) {
            SELECTED_MENU = StatusConstants.SEARCH_BY_RINGID;
            SELECTED_STR = DEFAULT_STR_TYPE_RINGID;
        } else if (e.getSource() == locationTabPane) {
            SELECTED_MENU = StatusConstants.SEARCH_BY_LOCATION;
            SELECTED_STR = DEFAULT_STR_TYPE_LOCATION;
        }
        if (searchTextField.getForeground() == DefaultSettings.disable_font_color) {
            searchTextField.setText(SELECTED_STR);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                resetMainButtonsColor(SELECTED_STR);
                addSearchResultInContainer();
                searchFromServer();
            }
        });

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (e.getSource() == nameTabPane) {

        } else if (e.getSource() == phoneTabPane) {

        } else if (e.getSource() == emailTabPane) {

        } else if (e.getSource() == ringidTabPane) {

        } else if (e.getSource() == locationTabPane) {

        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        if (e.getSource() == nameTabPane) {

        } else if (e.getSource() == phoneTabPane) {

        } else if (e.getSource() == emailTabPane) {

        } else if (e.getSource() == ringidTabPane) {

        } else if (e.getSource() == locationTabPane) {

        }
    }

}
