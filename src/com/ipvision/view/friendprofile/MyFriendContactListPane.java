/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.friendprofile;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.StatusConstants;
import com.ipvision.view.utility.UiMethods;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.stores.FriendList;
import com.ipvision.service.SendFriendRequest;
import com.ipvision.service.SendFriendsInfoRequest;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.utility.ContextMenuMouseListener;
import com.ipvision.view.utility.JDialogContactType;
import com.ipvision.view.utility.WrapLayout;
import com.ipvision.view.utility.ImageHelpers;

/**
 *
 * @author Shahadat Hossain
 */
public class MyFriendContactListPane extends JPanel {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MyFriendContactListPane.class);
    private MyFriendProfile myFriendProfile;
    public JPanel contactListPanel;
    public JPanel allContactListPanel;
    public JPanel mutualContactListPanel;
    public JScrollPane freindlistScorllPanel;
    private JLabel lblAllFriend;
    private JLabel lblMutualFriend;
    private JPanel createSearchbox;
//    private List<UserBasicInfo> mutualContactList = new ArrayList<UserBasicInfo>();
//    private List<UserBasicInfo> allContactList = new ArrayList<UserBasicInfo>();
    int default_content_width = 200;
    int default_content_height = 55;
    JLabel loading = DesignClasses.create_imageJlabel(GetImages.PLEASE_WAIT);
    private int mutualContactSize = 0;
    private int allContactSize = 0;
    public static final String DEFAULT_SEARCH_TEXT = "Search in Friends";
    public JTextField searchTextField = DesignClasses.makeTextFieldLimitedTextSize(DEFAULT_SEARCH_TEXT, 150, 20, 100);
    private UiMethods extra_methods = new UiMethods();
    public Map<String, SingleFriendsContact> singleAllFriendContactPanel = new ConcurrentHashMap<String, SingleFriendsContact>();
    public Map<String, SingleFriendsContact> singleMutualFriendContactPanel = new ConcurrentHashMap<String, SingleFriendsContact>();

    public MyFriendContactListPane(MyFriendProfile myFriendProfile) {
        this.setLayout(new BorderLayout());
        //this.setBorder(new EmptyBorder(MyFnFSettings.DEFAULT_MARGIN, 0, 0, 0));
        this.setOpaque(false);
        this.myFriendProfile = myFriendProfile;
        this.initContents();
    }

    private MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == lblAllFriend) {
                lblMutualFriend.setForeground(DefaultSettings.disable_font_color);
                lblAllFriend.setForeground(DefaultSettings.DEFAULT_TITLE_COLOR_IN_FRIEND);
                allContactListPanel.setVisible(true);
                mutualContactListPanel.setVisible(false);
            } else if (e.getSource() == lblMutualFriend) {
                lblAllFriend.setForeground(DefaultSettings.disable_font_color);
                lblMutualFriend.setForeground(DefaultSettings.DEFAULT_TITLE_COLOR_IN_FRIEND);
                allContactListPanel.setVisible(false);
                mutualContactListPanel.setVisible(true);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    };

    public void initContents() {
        JPanel mainContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainContainer.setOpaque(false);

        this.contactListPanel = new JPanel();
        this.contactListPanel.setLayout(new BoxLayout(this.contactListPanel, BoxLayout.Y_AXIS));
        this.contactListPanel.setBackground(Color.white);//Opaque(false); 

        lblAllFriend = DesignClasses.titleOfFriendList("All Friends", 1, 13);
        lblAllFriend.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblAllFriend.addMouseListener(mouseListener);

        lblMutualFriend = DesignClasses.titleOfFriendList("Mutual Friends", 1, 13);
        lblMutualFriend.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblMutualFriend.setForeground(DefaultSettings.disable_font_color);
        lblMutualFriend.addMouseListener(mouseListener);

        JPanel pnlTab = new JPanel();
        pnlTab.setOpaque(false);
        pnlTab.setLayout(new BorderLayout(0, 0));
        pnlTab.setBorder(new EmptyBorder(8, 5, 5, 8));
        pnlTab.setPreferredSize(new Dimension(638, 35));

        JPanel leftTab = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftTab.setOpaque(false);
        leftTab.add(lblAllFriend);
        leftTab.add(lblMutualFriend);

        pnlTab.add(leftTab, BorderLayout.WEST);

        createSearchbox = new JPanel();
        createSearchbox.setOpaque(false);
        createSearchbox.setLayout(new BoxLayout(createSearchbox, BoxLayout.X_AXIS));
        pnlTab.add(createSearchbox, BorderLayout.EAST);
        createSearchbox.setOpaque(false);
        createSearchbox.setBorder(DefaultSettings.DEFAULT_BORDER);
        JLabel search_image = DesignClasses.create_imageJlabel(GetImages.SEARCH_IMG);
        createSearchbox.add(search_image);
        createSearchbox.add(searchTextField);
        searchTextField.setBorder(null);
        searchTextField.setForeground(DefaultSettings.disable_font_color);
        searchTextField.addMouseListener(new ContextMenuMouseListener());
        searchTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                extra_methods.set_reset_defalut_text(searchTextField, DEFAULT_SEARCH_TEXT, true);
            }

            @Override
            public void focusGained(FocusEvent e) {
                if (searchTextField.getText().length() <= 0 || searchTextField.getText().equals(DEFAULT_SEARCH_TEXT)) {
                    extra_methods.set_reset_defalut_text(searchTextField, DEFAULT_SEARCH_TEXT, false);
                } else if (searchTextField.getText().length() < 1) {
                    extra_methods.set_reset_defalut_text(searchTextField, DEFAULT_SEARCH_TEXT, true);
                }
            }
        });
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                if (MyfnfHashMaps.getInstance().getFriendsContactList().get(myFriendProfile.getUserIdentity()) != null
                        && MyfnfHashMaps.getInstance().getFriendsContactList().get(myFriendProfile.getUserIdentity()).size() > 0) {
                    showHideFriendList();
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                if (MyfnfHashMaps.getInstance().getFriendsContactList().get(myFriendProfile.getUserIdentity()) != null
                        && MyfnfHashMaps.getInstance().getFriendsContactList().get(myFriendProfile.getUserIdentity()).size() > 0) {
                    showHideFriendList();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (MyfnfHashMaps.getInstance().getFriendsContactList().get(myFriendProfile.getUserIdentity()) != null
                        && MyfnfHashMaps.getInstance().getFriendsContactList().get(myFriendProfile.getUserIdentity()).size() > 0) {
                    showHideFriendList();
                }
            }
        });

        this.contactListPanel.add(pnlTab);

        this.allContactListPanel = new JPanel();
        this.allContactListPanel.setLayout(new WrapLayout(WrapLayout.LEFT, 10, 10, 638));
        this.allContactListPanel.setOpaque(false);
        JPanel totalP = new JPanel(new BorderLayout());
        totalP.setOpaque(false);
        totalP.add(allContactListPanel, BorderLayout.CENTER);
        this.contactListPanel.add(totalP);

        this.mutualContactListPanel = new JPanel();
        this.mutualContactListPanel.setLayout(new WrapLayout(WrapLayout.LEFT, 10, 10, 638));
        this.mutualContactListPanel.setBackground(Color.white);//Opaque(false); 
        JPanel mutualP = new JPanel(new BorderLayout());
        mutualP.setOpaque(false);
        mutualP.add(mutualContactListPanel, BorderLayout.CENTER);
        this.contactListPanel.add(mutualP);
        mutualContactListPanel.setVisible(false);

        //setLayout(new GridLayout(0, cols, 5, 5));
        //setLayout(new GridLayout(0, cols, 5, 5));
        JPanel contactListContainer = new JPanel();
        contactListContainer.setLayout(new BorderLayout());
        contactListContainer.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
        contactListContainer.setOpaque(false);
        contactListContainer.add(contactListPanel, BorderLayout.NORTH);

        // mainContainer.add(contactListContainer, BorderLayout.CENTER);
        mainContainer.add(contactListContainer);

        freindlistScorllPanel = DesignClasses.getDefaultScrollPane(mainContainer);

        this.add(freindlistScorllPanel, BorderLayout.CENTER);
        loadFriendList();
    }

    public void showHideFriendList() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //buildFriendList2();
                int countTotal = 0, countMutual = 0;
                try {
                    for (Entry entity : MyfnfHashMaps.getInstance().getFriendsContactList().get(myFriendProfile.getUserIdentity()).entrySet()) {
                        UserBasicInfo friendInfo = (UserBasicInfo) entity.getValue();
                        String uId = entity.getKey().toString();
                        if (isDisplayableFriend(friendInfo)) {
                            showFriend(friendInfo, true);
                            countTotal++;
                        } else {
                            showFriend(friendInfo, false);
                        }
                        if (FriendList.getInstance().getFriend_hash_map().containsKey(uId)
                                && FriendList.getInstance().getFriend_hash_map().get(uId).getFriendShipStatus().equals(StatusConstants.FRIENDSHIP_STATUS_ACCEPTED)) {
                            if (isDisplayableFriend(friendInfo)) {
                                showMutualFriend(friendInfo, true);
                                countMutual++;
                            } else {
                                showMutualFriend(friendInfo, false);
                            }
                        }
                    }
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            freindlistScorllPanel.getVerticalScrollBar().setValue(0);
                        }
                    });
                    lblAllFriend.setText("All Friends(" + countTotal + ")");
                    allContactListPanel.revalidate();
                    lblMutualFriend.setText("Mutual Friends(" + countMutual + ")");
                    mutualContactListPanel.revalidate();
                } catch (Exception e) {
                  //  e.printStackTrace();
                log.error("Error in showHideFriendList ==>" + e.getMessage());
                }
            }
        });
    }

    public void loadFriendList() {
        allContactListPanel.removeAll();
        //allContactListPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        allContactListPanel.add(loading);

        allContactListPanel.revalidate();
        allContactListPanel.repaint();

        if (MyfnfHashMaps.getInstance().getFriendsContactList().get(myFriendProfile.getUserIdentity()) != null && !MyfnfHashMaps.getInstance().getFriendsContactList().get(myFriendProfile.getUserIdentity()).isEmpty()) {
            buildFriendList();
        } else {
            (new SendFriendsInfoRequest(myFriendProfile.getUserIdentity(), AppConstants.TYPE_FRIEND_CONTACT_LIST, 0)).start();
        }
    }

    public void addSingleFriendFromServer(UserBasicInfo friendInfo) {
        allContactListPanel.remove(loading);
        String uId = friendInfo.getUserIdentity();
        if (isDisplayableFriend(friendInfo)) {
            showFriend(friendInfo, true);
            allContactSize++;
            lblAllFriend.setText("All Friends" + "(" + allContactSize + ")");
        } else {
            showFriend(friendInfo, false);
        }
        if (FriendList.getInstance().getFriend_hash_map().containsKey(uId)
                && FriendList.getInstance().getFriend_hash_map().get(uId).getFriendShipStatus().equals(StatusConstants.FRIENDSHIP_STATUS_ACCEPTED)) {
            if (isDisplayableFriend(friendInfo)) {
                showMutualFriend(friendInfo, true);
                mutualContactSize++;
                lblMutualFriend.setText("Mutual Friends" + "(" + mutualContactSize + ")");
            } else {
                showMutualFriend(friendInfo, false);
            }
        }
        allContactListPanel.revalidate();
        allContactListPanel.repaint();
        mutualContactListPanel.revalidate();
        mutualContactListPanel.repaint();
    }

    public void showFriend(UserBasicInfo friendInfo, boolean show) {
        SingleFriendsContact singleFriendsContact = singleAllFriendContactPanel.get(friendInfo.getUserIdentity());
        if (singleFriendsContact != null) {
            singleFriendsContact.setVisible(show);
        } else {
            int frndShipStatus;
            if (FriendList.getInstance().getFriend_hash_map().containsKey(friendInfo.getUserIdentity())) {
                frndShipStatus = FriendList.getInstance().getFriend_hash_map().get(friendInfo.getUserIdentity()).getFriendShipStatus().intValue();
            } else {
                frndShipStatus = 0;
            }
            singleFriendsContact = new SingleFriendsContact(friendInfo, frndShipStatus);
            singleFriendsContact.setVisible(show);
            singleAllFriendContactPanel.put(friendInfo.getUserIdentity(), singleFriendsContact);
            allContactListPanel.add(singleFriendsContact);
        }
        allContactListPanel.revalidate();
        allContactListPanel.repaint();
    }

    public void showMutualFriend(UserBasicInfo friendInfo, boolean show) {
        SingleFriendsContact singleFriendsContact = singleMutualFriendContactPanel.get(friendInfo.getUserIdentity());
        if (singleFriendsContact != null) {
            singleFriendsContact.setVisible(show);
        } else {
            singleFriendsContact = new SingleFriendsContact(friendInfo, StatusConstants.FRIENDSHIP_STATUS_ACCEPTED);
            singleFriendsContact.setVisible(show);
            singleMutualFriendContactPanel.put(friendInfo.getUserIdentity(), singleFriendsContact);
            mutualContactListPanel.add(singleFriendsContact);
        }
        mutualContactListPanel.revalidate();
        mutualContactListPanel.repaint();
    }

    public void removeLoadingLabel() {
        allContactListPanel.remove(loading);
        if (allContactSize < 1) {
            createSearchbox.setVisible(false);
        }
        contactListPanel.revalidate();
        contactListPanel.repaint();
    }

    public void buildFriendList() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                List<UserBasicInfo> friendInfoList = new ArrayList<>();
                List<UserBasicInfo> mutualInfoList = new ArrayList<>();
                try {
                    for (Entry entity : MyfnfHashMaps.getInstance().getFriendsContactList().get(myFriendProfile.getUserIdentity()).entrySet()) {
                        UserBasicInfo friendInfo = (UserBasicInfo) entity.getValue();
                        String uId = entity.getKey().toString();
                        friendInfoList.add(friendInfo);
                        if (FriendList.getInstance().getFriend_hash_map().containsKey(uId)
                                && FriendList.getInstance().getFriend_hash_map().get(uId).getFriendShipStatus().equals(StatusConstants.FRIENDSHIP_STATUS_ACCEPTED)) {
                            mutualInfoList.add(friendInfo);
                        }
                    }
                    //sortbyUt = friendInfoList;
                    createFriendList(friendInfoList, mutualInfoList);
                } catch (Exception e) {
                }
            }
        });
    }

    public void createFriendList(List<UserBasicInfo> friendInfoList, List<UserBasicInfo> mutualInfoList) {
        if (friendInfoList != null) {
            int countTotal = 0, countMutual = 0;
            allContactListPanel.removeAll();
            mutualContactListPanel.removeAll();
            //totalFriendLabel.setText("Total: (" + friendInfoList.size() + ")");
            for (UserBasicInfo userBasicInfo : friendInfoList) {
                SingleFriendsContact friendPanel = singleAllFriendContactPanel.get(userBasicInfo.getUserIdentity());
                if (friendPanel != null) {
                    if (isDisplayableFriend(userBasicInfo)) {
                        friendPanel.setVisible(true);
                        countTotal++;
                    } else {
                        friendPanel.setVisible(false);
                    }
                    allContactListPanel.add(friendPanel);
                } else {
                    friendPanel = new SingleFriendsContact(userBasicInfo, userBasicInfo.getFriendShipStatus().intValue());
                    friendPanel.setName(userBasicInfo.getUserIdentity());
                    if (isDisplayableFriend(userBasicInfo)) {
                        friendPanel.setVisible(true);
                        countTotal++;
                    } else {
                        friendPanel.setVisible(false);
                    }
                    singleAllFriendContactPanel.put(userBasicInfo.getUserIdentity(), friendPanel);
                    allContactListPanel.add(friendPanel);
                }
            }
            lblAllFriend.setText("Total: (" + countTotal + ")");
            allContactListPanel.revalidate();
            allContactListPanel.repaint();

            for (UserBasicInfo userBasicInfo : mutualInfoList) {
                SingleFriendsContact friendPanel = singleMutualFriendContactPanel.get(userBasicInfo.getUserIdentity());
                if (friendPanel != null) {
                    if (isDisplayableFriend(userBasicInfo)) {
                        friendPanel.setVisible(true);
                        countMutual++;
                    } else {
                        friendPanel.setVisible(false);
                    }
                    mutualContactListPanel.add(friendPanel);
                } else {
                    friendPanel = new SingleFriendsContact(userBasicInfo, userBasicInfo.getFriendShipStatus().intValue());
                    friendPanel.setName(userBasicInfo.getUserIdentity());
                    if (isDisplayableFriend(userBasicInfo)) {
                        friendPanel.setVisible(true);
                        countMutual++;
                    } else {
                        friendPanel.setVisible(false);
                    }
                    singleMutualFriendContactPanel.put(userBasicInfo.getUserIdentity(), friendPanel);
                    mutualContactListPanel.add(friendPanel);
                }
            }
            lblMutualFriend.setText("Mutual: (" + countMutual + ")");
            mutualContactListPanel.revalidate();
            mutualContactListPanel.repaint();
        }
    }

    public boolean isDisplayableFriend(UserBasicInfo entity) {
        boolean isDisplayable = false;
        try {
            if (searchTextField != null) {
                if (searchTextField.getText().length() <= 0
                        || searchTextField.getText().equals(DEFAULT_SEARCH_TEXT)
                        || HelperMethods.check_string_contains_substring(entity.getFullName() /*+ " " + entity.getLastName()*/, searchTextField.getText())
                        || HelperMethods.check_string_contains_substring(entity.getUserIdentity(), searchTextField.getText())) {
                    isDisplayable = true;
                }
            } else {
                isDisplayable = true;
            }
        } catch (Exception e) {
        }

        return isDisplayable;

    }

    // Rabiul 3-23-2015
    public class SingleFriendsContact extends JPanel {

        public UserBasicInfo entity;
        public int type;
        public JLabel sendRequest;

        public SingleFriendsContact(UserBasicInfo entity, int type) {
            this.entity = entity;
            this.type = type;
            this.setLayout(new BorderLayout(5, 5));
            this.setBorder(DefaultSettings.DEFAULT_BOOK_BORDER);
            this.setBackground(Color.WHITE);
            this.setPreferredSize(new Dimension(default_content_width - 3, default_content_height - 7));
            initFriendsContact();
        }

        private void initFriendsContact() {
            try {
                String name = "No Name";
                /*if ((entity.getFullName() != null && entity.getFullName().trim().length() > 0) && (entity.getLastName() != null && entity.getLastName().trim().length() > 0)) {
                 name = entity.getFullName() + " " + entity.getLastName();
                 } else */
                if ((entity.getFullName() != null && entity.getFullName().trim().length() > 0)) {
                    name = entity.getFullName();
                } /*else if ((entity.getLastName() != null && entity.getLastName().trim().length() > 0)) {
                 name = entity.getLastName();
                 }*/

                JLabel frienImageLabel = new JLabel();
                if (entity.getProfileImage() != null && entity.getProfileImage().length() > 0) {
                    ImageHelpers.addProfileImageThumb(frienImageLabel, entity.getProfileImage());
                } else {
                    BufferedImage img = ImageHelpers.getUnknownImage(true);//.getBufferImageFromurl(null);
                    frienImageLabel.setIcon(new ImageIcon(img));
                    img = null;
                }
                JPanel friendImagePanel = new JPanel();
                friendImagePanel.setOpaque(false);
                friendImagePanel.add(frienImageLabel);
                this.add(friendImagePanel, BorderLayout.WEST);

                JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
                wrapperPanel.setBorder(new EmptyBorder(3, 0, 3, 0));
                wrapperPanel.setOpaque(false);

                JPanel infoContainer = new JPanel();
                infoContainer.setPreferredSize(new Dimension(120, 40));
                infoContainer.setLayout(new BoxLayout(infoContainer, BoxLayout.Y_AXIS));
                infoContainer.setOpaque(false);

                JLabel fullNameLabel = DesignClasses.makeAncorLabelDefaultColor(name, 0, 12);
                fullNameLabel.setPreferredSize(new Dimension(110, 12));

                JPanel fullNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                fullNamePanel.setOpaque(false);
                fullNamePanel.add(fullNameLabel);
                infoContainer.add(fullNamePanel);

                //String displayIdentity = HelperMethods.getShortRingID(entity.getUserIdentity(), entity.getMobilePhoneDialingCode());
                String displayIdentity = HelperMethods.getRingID(entity.getUserIdentity());
//                long numberOfMutualFriends = entity.getNumberOfMutualFriends();
                JLabel memberInfoLabel = DesignClasses.makeJLabel_normal(displayIdentity, 9, DefaultSettings.APP_DEFAULT_FONT_COLOR);
//                JLabel memberInfoLabel = new JLabel(displayIdentity);
//                memberInfoLabel.setForeground(DefaultSettings.APP_DEFAULT_FONT_COLOR);

                JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                statusPanel.setOpaque(false);
                statusPanel.add(memberInfoLabel);
                infoContainer.add(statusPanel);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                buttonPanel.setOpaque(false);
                final JButton sendRequestImageButton;

                if (type == 0) {
                    sendRequestImageButton = DesignClasses.createImageButton(GetImages.ADD_FRIEND_CONTACT, GetImages.ADD_FRIEND_CONTACT_H, "Add as Friend");
//                    sendRequest = DesignClasses.titleOfFriendList("Add as Friend", 0, 11);
//                    sendRequest.setIcon(addIcon);
//                    sendRequest.setCursor(new Cursor(Cursor.HAND_CURSOR));
//                    sendRequest.setToolTipText("Add as Friend");
//                    sendRequestImageButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    buttonPanel.add(sendRequestImageButton);
                    sendRequestImageButton.addMouseListener(new MouseAdapter() {
                        Font original;

                        @Override
                        public void mouseEntered(MouseEvent e) {
//                         
//                            original = e.getComponent().getFont();
//                            Map attributes = original.getAttributes();
//                            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
//                            e.getComponent().setFont(original.deriveFont(attributes));
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
//                            e.getComponent().setFont(original);
                        }

                        @Override
                        public void mouseClicked(MouseEvent e) {
//                            e.getComponent().setFont(original);
                            JDialogContactType dialog = new JDialogContactType();
                            if (JDialogContactType.contactType > 0) {
                                new SendFriendRequest(entity.getUserIdentity(), sendRequestImageButton, myFriendProfile.myFriendContactListPane, JDialogContactType.contactType).start();
                            }
                        }
                    });
                } else if (type == StatusConstants.FRIENDSHIP_STATUS_INCOMING) {
                    sendRequestImageButton = DesignClasses.createImageButtonWithoutCursor(GetImages.FRIEND_INCOMING, GetImages.FRIEND_INCOMING_H, "Incoming Request");
                    buttonPanel.add(sendRequestImageButton);

//                    JLabel friendShipStatusLabel = DesignClasses.makeJLabelUnderFullName("Incoming Request");
//                    buttonPanel.add(friendShipStatusLabel);
                } else if (type == StatusConstants.FRIENDSHIP_STATUS_PENDING) {
                    sendRequestImageButton = DesignClasses.createImageButtonWithoutCursor(GetImages.FRIEND_OUTGOING, GetImages.FRIEND_OUTGOING_H, "Pending Request");
//                    buttonPanel.add(sendRequestImageButton,BorderLayout.CENTER);
//                    JLabel friendShipStatusLabel = DesignClasses.makeJLabelUnderFullName("Pending Request");
                    buttonPanel.add(sendRequestImageButton);
                }

                fullNameLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                fullNameLabel.addMouseListener(new MouseAdapter() {
                    Font original;

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (entity.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
                            e.getComponent().setFont(original);
                            GuiRingID.getInstance().action_of_myProfile_button();
                        } else if (FriendList.getInstance().getFriend_hash_map().get(entity.getUserIdentity()) != null) {
                            e.getComponent().setFont(original);
                            GuiRingID.getInstance().showFriendProfile(entity.getUserIdentity());
                        } else {
                            e.getComponent().setFont(original);
                            GuiRingID.getInstance().getMainRight().showUnknownProfile(entity, myFriendProfile);
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        original = e.getComponent().getFont();
                        Map attributes = original.getAttributes();
                        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                        e.getComponent().setFont(original.deriveFont(attributes));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        e.getComponent().setFont(original);
                    }
                });
//                infoContainer.add(buttonPanel,BorderLayout.NORTH);
                this.add(buttonPanel, BorderLayout.EAST);
                wrapperPanel.add(infoContainer);
                this.add(wrapperPanel, BorderLayout.CENTER);
            } catch (Exception e) {
               // e.printStackTrace();
           log.error("Error in here ==>" + e.getMessage());
            }
        }

    }

    public void friendAdded(String userId) {

        if (allContactListPanel.getComponentCount() > 0) {
            long number = allContactListPanel.getComponentCount();
            for (int i = 0; i < number; i++) {
                if (allContactListPanel.getComponent(i) instanceof SingleFriendsContact) {
                    SingleFriendsContact single = (SingleFriendsContact) allContactListPanel.getComponent(i);
                    if (single.entity != null && single.entity.getUserIdentity().equalsIgnoreCase(userId)) {
                        allContactListPanel.remove(single);
                        singleAllFriendContactPanel.remove(userId);

                        SingleFriendsContact singleFriendsContact = new SingleFriendsContact(single.entity, StatusConstants.FRIENDSHIP_STATUS_PENDING);
                        allContactListPanel.add(singleFriendsContact, i);
                        allContactListPanel.revalidate();
                        allContactListPanel.repaint();
                        singleAllFriendContactPanel.put(userId, singleFriendsContact);
                        break;
                    }

                }
            }
            /*try {
             if (mutualContactListPanel.getComponentCount() > 0) {
             long number = mutualContactListPanel.getComponentCount();
             for (int i = 0; i < number; i++) {
             if (mutualContactListPanel.getComponent(i) instanceof SingleFriendsContact) {
             SingleFriendsContact singleStatusPanel = (SingleFriendsContact) mutualContactListPanel.getComponent(i);
             if (singleStatusPanel.entity != null && singleStatusPanel.entity.getUserIdentity().equalsIgnoreCase(userId)) {
             mutualContactListPanel.remove(singleStatusPanel);
             break;
             }
             }
             }
             }

             if (allContactList.size() > 0) {
             long number = allContactList.size();
             for (int i = 0; i < number; i++) {
             UserBasicInfo basicInfo = allContactList.get(i);
             if (basicInfo != null && basicInfo.getUserIdentity().equalsIgnoreCase(userId)) {
             allContactListPanel.add(new SingleFriendsContact(basicInfo, 1), 0);
             //mutualContactList.add(basicInfo);
             //allContactList.remove(basicInfo);
             break;
             }
             }
             }

             lblAllFriend.setText("Mutual Friends" + "(" + mutualContactList.size() + ")");
             if (mutualContactList.size() > 0) {
             lblAllFriend.setVisible(true);
             } else {
             lblAllFriend.setVisible(false);
             }

             lblMutualFriend.setText("Other Friends" + "(" + allContactList.size() + ")");
             if (allContactList.size() > 0) {
             lblMutualFriend.setVisible(true);
             } else {
             lblMutualFriend.setVisible(false);
             }

             allContactListPanel.revalidate();
             allContactListPanel.repaint();

             mutualContactListPanel.revalidate();
             mutualContactListPanel.repaint();
             }catch (Exception e) {
             e.printStackTrace();
             }*/
        }
    }
}
