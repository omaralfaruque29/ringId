/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.friendlist;

import com.ipvision.view.leftdata.FriendListContainer;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.SearchItem;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.constants.StaticFields;
import com.ipvision.model.constants.StatusConstants;
import com.ipvision.view.utility.UiMethods;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.SearchFriendsByName;
import com.ipvision.view.utility.ContextMenuMouseListener;
import com.ipvision.view.utility.JCustomMenuPopup;
import javax.swing.border.Border;
import utils.SearchStringQueue;

/**
 *
 * @author Faiz
 */
public class FriendSearchPanel extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FriendSearchPanel.class);
    private UiMethods extra_methods;
    public JTextField searchTextField;
    private JLabel totalFriendLabel;
    private int count = 0;
    public JPanel totalFriendPanel;
    MiniCallChatPanel miniCallChatPanel;
    FriendListContainer container;
    //public ArrayList<String> peopleYouMayKnowUIBuiltList = new ArrayList<>();
    private List<UserBasicInfo> sortbyUt;
    public String SELECTED_MNU;
    public String MNU_ALL_FRIENDS = "All Friends";
    // public String MNU_BLOCKED = "Blocked";
    public String MNU_FULLACCESS = "Complete Profile Access";
    public String MNU_CALLCHAT = "Chat & Call Access";
    public String MNU_BLOCK = "Blocked Access";
    public String MNU_RECENT = "Recently Added Friends";

    //public String MNU_ALL_PENDING = "All Pending Requests";
    //public String MNU_INCOMING_ACCESS_REQ = "Incoming Access Requests";
    //public String MNU_OUTGOING_ACCESS_REQ = "Outgoing Access Requests";
    //public String MNU_PENDING = "Outgoing Friend Requests";
    //public String MNU_INCOMING = "Incoming Friend Requests";
    private JCustomMenuPopup actionPopUp;
    public JPanel categoryPanel;
    private JButton actionPopUpButton;
    private JLabel selectedLabel;

    public FriendSearchPanel(final FriendListContainer container) {
        this.container = container;
        searchTextField = DesignClasses.makeTextFieldLimitedTextSize(StaticFields.SEARCH_FRIENDS, 146, 26, 100);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        extra_methods = new UiMethods();
        actionPopUp = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_FRIENDLIST_CATEGORY);
        Border border = new EmptyBorder(7, 10, 0, 4);
        SearchFriendsByName searchFriendsByName = new SearchFriendsByName(searchTextField, border);
        add(searchFriendsByName);
        searchTextField.setBorder(new EmptyBorder(3, 5, 3, 0));
        searchTextField.setOpaque(false);
        //searchTextField.setBorder(new EmptyBorder(0, 5, 0, 0));
        searchTextField.setText(StaticFields.SEARCH_FRIENDS);
        searchTextField.setForeground(DefaultSettings.disable_font_color);
        searchTextField.addMouseListener(new ContextMenuMouseListener());
        searchTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                extra_methods.set_reset_defalut_text(searchTextField, StaticFields.SEARCH_FRIENDS, true);
            }

            @Override
            public void focusGained(FocusEvent e) {
                if (searchTextField.getText().length() <= 0 || searchTextField.getText().equals(StaticFields.SEARCH_FRIENDS)) {
                    extra_methods.set_reset_defalut_text(searchTextField, StaticFields.SEARCH_FRIENDS, false);
                } else if (searchTextField.getText().length() < 1) {
                    extra_methods.set_reset_defalut_text(searchTextField, StaticFields.SEARCH_FRIENDS, true);
                }

            }
        });
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                showHideFriendList();
                // }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                showHideFriendList();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                showHideFriendList();

            }
        });
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                buildFilterFreindPanel();
            }
        });
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

    public void showHideFriendList() {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                try {
                    for (Entry entity : FriendList.getInstance().getFriend_hash_map().entrySet()) {
                        UserBasicInfo friendInfo = (UserBasicInfo) entity.getValue();
                        if (isDisplayableFriend(friendInfo)) {
                            showFriend(friendInfo, true);
                            count++;
                        } else {
                            showFriend(friendInfo, false);
                        }
                    }
                    totalFriendLabel.setText("Total: (" + count + ")");
                    container.friendListWrapperPanel.revalidate();
                } catch (Exception e) {
                    //  e.printStackTrace();
                    log.error("Error in showHideFriendList ==>" + e.getMessage());
                }
            }
        });
    }

    public void buildPopUpMenu() {
        actionPopUp.addMenu(MNU_ALL_FRIENDS, null);
        actionPopUp.addMenu(MNU_FULLACCESS, null);
        actionPopUp.addMenu(MNU_CALLCHAT, null);
        actionPopUp.addMenu(MNU_BLOCK, null);
        actionPopUp.addMenu(MNU_RECENT, null);
    }

    public void buildFilterFreindPanel() {
        totalFriendLabel = DesignClasses.titleOfFriendList("Total: (0)", 1, 11);
        //totalFriendLabel.set//Foreground(Color.CYAN);
        totalFriendPanel = new JPanel();
        totalFriendPanel.setBorder(new EmptyBorder(0, 10, 0, DefaultSettings.LEFT_EMPTY_SPACE));
        totalFriendPanel.setOpaque(false);
        totalFriendPanel.setLayout(new BorderLayout());
        totalFriendPanel.setPreferredSize(new Dimension(DefaultSettings.LEFT_SINGLE_PANEL_WIDTH, 21));
        totalFriendPanel.add(totalFriendLabel, BorderLayout.EAST);
        categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        categoryPanel.setOpaque(false);
        categoryPanel.setBorder(new EmptyBorder(0, DefaultSettings.LEFT_EMPTY_SPACE, 0, 0));
        actionPopUpButton = DesignClasses.createImageButton(GetImages.IMAGE_BOTTOM_ARROW, GetImages.IMAGE_BOTTOM_ARROW, "Options");
        categoryPanel.add(actionPopUpButton);
//        actionPopUpButton.addMouseListener(this);
        selectedLabel = DesignClasses.titleOfFriendList(MNU_ALL_FRIENDS, 1, 11);
        categoryPanel.add(selectedLabel);
        categoryPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //  selectedLabel.addMouseListener(this);
        categoryPanel.addMouseListener(menuListener);
        actionPopUpButton.addMouseListener(menuListener);
        buildPopUpMenu();
        totalFriendPanel.add(categoryPanel, BorderLayout.WEST);

        add(totalFriendPanel);
//        showHideFriendList();
        SELECTED_MNU = MNU_ALL_FRIENDS;
        revalidate();
    }

    public boolean isDisplayableFriend(UserBasicInfo entity) {
        boolean isDisplayable = false;
        try {
            entity.getUserIdentity();
            if (searchTextField != null) {
                if (searchTextField.getText().length() <= 0
                        || searchTextField.getText().equals(StaticFields.SEARCH_FRIENDS)
                        || HelperMethods.check_string_contains_substring(entity.getFullName(), searchTextField.getText())
                        || HelperMethods.check_string_contains_substring(entity.getUserIdentity(), searchTextField.getText())) {

                    if (SELECTED_MNU.equalsIgnoreCase(MNU_ALL_FRIENDS)) {
                        return (entity.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED /*&& !(entity.getNewContactType() != null && entity.getNewContactType() > 0)*/);
                    } else if (SELECTED_MNU.equalsIgnoreCase(MNU_FULLACCESS)) {
                        return (entity.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED && entity.getContactType() == StatusConstants.ACCESS_FULL
                                && !(entity.getNewContactType() != null && entity.getNewContactType() > 0));
                    } else if (SELECTED_MNU.equalsIgnoreCase(MNU_CALLCHAT)) {
                        return (entity.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED && entity.getContactType() == StatusConstants.ACCESS_CHAT_CALL
                                && !(entity.getNewContactType() != null && entity.getNewContactType() > 0));
                    } else if (SELECTED_MNU.equalsIgnoreCase(MNU_BLOCK)) {
                        return (entity.getBlocked() != null && entity.getBlocked().intValue() > 0);
                    } else if (SELECTED_MNU.equalsIgnoreCase(MNU_RECENT)) {
                        //   if (sortbyUt.size() == FriendList.getInstance().getFriend_hash_map().size()) {
                        int LIMIT = 10;
                        if (sortbyUt.size() < LIMIT) {
                            LIMIT = sortbyUt.size();
                        } //&& !(sortbyUt.get(i).getNewContactType() != null && sortbyUt.get(i).getNewContactType() > 0)
                        for (int i = 0; i < LIMIT; i++) {
                            if (sortbyUt.get(i).getUserIdentity().equalsIgnoreCase(entity.getUserIdentity())) {
                                isDisplayable = true;
                            }
                        }
                        // }
                        return isDisplayable;
                    } /*else if (SELECTED_MNU.equalsIgnoreCase(MNU_ALL_PENDING)) {
                     return (entity.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_PENDING || entity.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_INCOMING
                     || (entity.getNewContactType() != null && entity.getNewContactType() > 0));
                     } else if (SELECTED_MNU.equalsIgnoreCase(MNU_INCOMING)) {
                     return (entity.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_INCOMING && !(entity.getNewContactType() != null && entity.getNewContactType() > 0));
                     } else if (SELECTED_MNU.equalsIgnoreCase(MNU_PENDING)) {
                     return (entity.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_PENDING && !(entity.getNewContactType() != null && entity.getNewContactType() > 0));
                     } else if (SELECTED_MNU.equalsIgnoreCase(MNU_INCOMING_ACCESS_REQ)) {
                     return (entity.getNewContactType() != null && entity.getNewContactType() > 0 && !entity.getIsChangeRequester());
                     } else if (SELECTED_MNU.equalsIgnoreCase(MNU_OUTGOING_ACCESS_REQ)) {
                     return (entity.getNewContactType() != null && entity.getNewContactType() > 0 && entity.getIsChangeRequester());
                     } */ else {
                        return false;
                    }
                }
            } else {
                isDisplayable = true;
                return isDisplayable;
            }
        } catch (Exception e) {
        }
        return isDisplayable;
    }

    /*    public boolean isDisplayableFriend(UserBasicInfo entity) {
     return true;
     }*/
    public void sortFriendListByTime() {
        sortbyUt = new ArrayList<>();
        for (Map.Entry entity : FriendList.getInstance().getFriend_hash_map().entrySet()) {
            UserBasicInfo friendInfo = (UserBasicInfo) entity.getValue();
            if (friendInfo.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED && !(friendInfo.getNewContactType() != null && friendInfo.getNewContactType() > 0)) {
                sortbyUt.add(friendInfo);
            }
        }
        Collections.sort(sortbyUt, new Comparator<UserBasicInfo>() {
            @Override
            public int compare(UserBasicInfo one, UserBasicInfo other) {
                Long oneUt = one.getUt();
                Long otherUt = other.getUt();
                return otherUt.compareTo(oneUt);
            }
        });

        List<UserBasicInfo> temp = new ArrayList<>();
        for (UserBasicInfo e : sortbyUt) {
            if (e.getUt() == 0) {
                temp.add(e);
            }
        }
        for (UserBasicInfo e : sortbyUt) {
            if (e.getUt() > 0) {
                temp.add(e);
            }
        }
        sortbyUt = temp;

    }
    MouseListener menuListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == categoryPanel || e.getSource() == actionPopUpButton) {
                if (!actionPopUp.isVisible()) {
                    actionPopUp.setVisible(actionPopUpButton, 20, - 4);
                } else {
                    actionPopUp.hideThis();
                }
            } else {
                JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
                if (menu.text.equalsIgnoreCase(MNU_ALL_FRIENDS)) {
                    SELECTED_MNU = MNU_ALL_FRIENDS;
                    selectedLabel.setText(MNU_ALL_FRIENDS);
                    actionPopUp.hideThis();
                } else if (menu.text.equalsIgnoreCase(MNU_FULLACCESS)) {
                    SELECTED_MNU = MNU_FULLACCESS;
                    selectedLabel.setText(MNU_FULLACCESS);
                    actionPopUp.hideThis();
                } else if (menu.text.equalsIgnoreCase(MNU_CALLCHAT)) {
                    SELECTED_MNU = MNU_CALLCHAT;
                    selectedLabel.setText(MNU_CALLCHAT);
                    actionPopUp.hideThis();
                } else if (menu.text.equalsIgnoreCase(MNU_BLOCK)) {
                    SELECTED_MNU = MNU_BLOCK;
                    selectedLabel.setText(MNU_BLOCK);
                    actionPopUp.hideThis();
                } else if (menu.text.equalsIgnoreCase(MNU_RECENT)) {
                    SELECTED_MNU = MNU_RECENT;
                    selectedLabel.setText(MNU_RECENT);
                    sortFriendListByTime();
                    actionPopUp.hideThis();
                }
                showHideFriendList();
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
            if (e.getSource() != categoryPanel && e.getSource() != actionPopUpButton) {
                JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
                menu.setMouseEntered();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getSource() != categoryPanel && e.getSource() != actionPopUpButton) {
                JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
                menu.setMouseExited();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    };

    public void addSingleFriendFromServer(final UserBasicInfo friendInfo) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isDisplayableFriend(friendInfo)) {
                        friendInfo.getUserIdentity();
                        showFriend(friendInfo, true);
                        count++;
                        totalFriendLabel.setText("Total: (" + count + ")");
                    } else {
                        showFriend(friendInfo, false);
                    }
                    container.friendListWrapperPanel.revalidate();
                    container.friendListWrapperPanel.repaint();
                } catch (Exception e) {
                    //  e.printStackTrace();
                    log.error("Error in addSingleFriendFromServer ==>" + e.getMessage());
                }
            }
        }
        );
    }

    /* public synchronized void addSearchResultInContainer() {
     java.awt.EventQueue.invokeLater(new Runnable() {
     public void run() {
     int count = 0;
     container.inviteListWrapperPanel.removeAll();
     try {
     if (!MyfnfHashMaps.getInstance().getInviteFriendsContainer().isEmpty() && container.inviteListWrapperPanel != null && searchTextField.getText().length() > 0) {
     boolean hasResult = false;
     for (String key : MyfnfHashMaps.getInstance().getInviteFriendsContainer().keySet()) {
     UserBasicInfo json = MyfnfHashMaps.getInstance().getInviteFriendsContainer().get(key);
     String searched_name = "";
     if (json.getFullName() != null) {
     searched_name += json.getFullName().toLowerCase();
     }
     if (FriendList.getInstance().getFriend_hash_map().get(key) == null) {
     if (searched_name.contains(searchTextField.getText().toLowerCase())) {
     UserBasicInfo entity = MyfnfHashMaps.getInstance().getInviteFriendsContainer().get(key);
     if (MyfnfHashMaps.getInstance().getSingleFriendPanel().get(entity.getUserIdentity()) != null) {
     container.inviteListWrapperPanel.add(MyfnfHashMaps.getInstance().getSingleFriendPanel().get(entity.getUserIdentity()));
     } else {
     SingleFriendPanel single = new SingleFriendPanel(entity);
     container.inviteListWrapperPanel.add(single);
     MyfnfHashMaps.getInstance().getSingleFriendPanel().put(entity.getUserIdentity(), single);
     }
     count++;
     hasResult = true;
     }
     }
     }
     if (hasResult == false) {
     container.inviteListWrapperPanel.add(addNoResultScreen());
     }
     } else {
     //addFriendPanel();
     }
     } catch (Exception ex) {
     }
     if (GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().categoryPnl.mid.isSelected) {
     totalFriendLabel.setText("Suggested: (" + count + ")");
     }
     container.inviteListWrapperPanel.revalidate();
     container.inviteListWrapperPanel.repaint();
     }
     });
     }*/

    /*public void createPeopleYouMayKnowList() {
     if (FriendList.getInstance().getPeopleYouMayKnow() != null) {
     int len = FriendList.getInstance().getPeopleYouMayKnow().size();
     UserBasicInfo entity;
     for (int i = 0; i < len; i++) {
     entity = (UserBasicInfo) FriendList.getInstance().getPeopleYouMayKnow().get(i).getUserBasicInfo();
     if (!peopleYouMayKnowUIBuiltList.contains(entity.getUserIdentity())) {
     if (MyfnfHashMaps.getInstance().getSingleFriendPanel().get(entity.getUserIdentity()) != null) {
     container.peopleYouMayKnowWrapperPanel.add(MyfnfHashMaps.getInstance().getSingleFriendPanel().get(entity.getUserIdentity()));
     } else {
     SingleFriendPanel single = new SingleFriendPanel(entity);
     container.peopleYouMayKnowWrapperPanel.add(single);
     MyfnfHashMaps.getInstance().getSingleFriendPanel().put(entity.getUserIdentity(), single);
     }
     peopleYouMayKnowUIBuiltList.add(entity.getUserIdentity());
     }
     }
     if (GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().categoryPnl.mid.isSelected) {
     totalFriendLabel.setText("Suggested: (" + len + ")");
     }
     }
     container.peopleYouMayKnowWrapperPanel.revalidate();
     container.peopleYouMayKnowWrapperPanel.repaint();
     }*/

    /*public void createInviteFriendList() {
     if (SearchThread.getStatus() == false) {
     SearchThread th = new SearchThread(StatusConstants.SEARCH_BY_ALL);
     SearchThread.startSearchThread();
     th.start();
     }
     get_all_matches(searchTextField.getText());
     container.inviteListWrapperPanel.removeAll();
     addSearchResultInContainer();
     }*/
    public void show_all_tab() {
        SELECTED_MNU = MNU_ALL_FRIENDS;
        selectedLabel.setText(MNU_ALL_FRIENDS);

        actionPopUp.showMenu(MNU_ALL_FRIENDS);
        actionPopUp.showMenu(MNU_FULLACCESS);
        actionPopUp.showMenu(MNU_CALLCHAT);
        actionPopUp.showMenu(MNU_BLOCK);
        actionPopUp.showMenu(MNU_RECENT);

        /*actionPopUp.hideMenu(MNU_ALL_PENDING);
         actionPopUp.hideMenu(MNU_INCOMING);
         actionPopUp.hideMenu(MNU_PENDING);
         actionPopUp.hideMenu(MNU_INCOMING_ACCESS_REQ);
         actionPopUp.hideMenu(MNU_OUTGOING_ACCESS_REQ);*/
    }

    /* public void show_pending_tab() {
     SELECTED_MNU = MNU_ALL_PENDING;
     selectedLabel.setText(MNU_ALL_PENDING);

     actionPopUp.hideMenu(MNU_ALL_FRIENDS);
     actionPopUp.hideMenu(MNU_FULLACCESS);
     actionPopUp.hideMenu(MNU_CALLCHAT);
     actionPopUp.hideMenu(MNU_BLOCK);
     actionPopUp.hideMenu(MNU_RECENT);

     actionPopUp.showMenu(MNU_ALL_PENDING);
     actionPopUp.showMenu(MNU_INCOMING);
     actionPopUp.showMenu(MNU_PENDING);
     actionPopUp.showMenu(MNU_INCOMING_ACCESS_REQ);
     actionPopUp.showMenu(MNU_OUTGOING_ACCESS_REQ);
     }*/
    public void createFriendList(List<UserBasicInfo> friendInfoList) {
        /*if (GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().categoryPnl.first.isSelected) {
         categoryPanel.setVisible(true);
         show_all_tab();
         } else if (GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().categoryPnl.last.isSelected) {
         categoryPanel.setVisible(true);
         show_pending_tab();
         //        } else {
         //            categoryPanel.setVisible(false);
         }*/
        int count = 0;
        if (friendInfoList != null) {
            friendInfoList = sortFriendList(friendInfoList);
            container.friendListWrapperPanel.removeAll();
            for (UserBasicInfo userBasicInfo : friendInfoList) {
                SingleFriendPanel friendPanel = MyfnfHashMaps.getInstance().getSingleFriendPanel().get(userBasicInfo.getUserIdentity());
                if (friendPanel != null) {
                    if (isDisplayableFriend(userBasicInfo)) {
                        friendPanel.user = userBasicInfo;
                        friendPanel.setVisible(true);
                        count++;
                    } else {
                        friendPanel.setVisible(false);
                    }
                    //    friendPanel.changeFriendShipStatus();
                    container.friendListWrapperPanel.add(friendPanel);
                } else {
                    friendPanel = new SingleFriendPanel(userBasicInfo);
                    friendPanel.setName(userBasicInfo.getUserIdentity());
                    if (isDisplayableFriend(userBasicInfo)) {
                        friendPanel.setVisible(true);
                        count++;
                    } else {
                        friendPanel.setVisible(false);
                    }
                    MyfnfHashMaps.getInstance().getSingleFriendPanel().put(userBasicInfo.getUserIdentity(), friendPanel);
                    container.friendListWrapperPanel.add(friendPanel);
                }
                friendPanel.setBackground(Color.WHITE);
                if (AllFriendList.selectedFriendUserIdentity != null && friendPanel.userIdentity.equalsIgnoreCase(AllFriendList.selectedFriendUserIdentity)) {
                    friendPanel.setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
                    AllFriendList.selectedFriendPanel = friendPanel;
                }
            }
            totalFriendLabel.setText("Total: (" + count + ")");
            container.friendListWrapperPanel.revalidate();
            container.friendListWrapperPanel.repaint();
        }
    }

    public List sortFriendList(List<UserBasicInfo> list) {
        Collections.sort(list, new Comparator<UserBasicInfo>() {
            @Override
            public int compare(UserBasicInfo one, UserBasicInfo other) {
                String onesFullName = one.getFullName().toUpperCase();
                String othersFullName = other.getFullName().toUpperCase();
                return onesFullName.compareTo(othersFullName);
            }
        });

        Collections.sort(list, new Comparator<UserBasicInfo>() {
            @Override
            public int compare(UserBasicInfo one, UserBasicInfo other) {
                Integer onesLevel = getPriorityLevel(one.getPresence(), one.getFriendShipStatus());
                Integer othersLevel = getPriorityLevel(other.getPresence(), other.getFriendShipStatus());
                return onesLevel.compareTo(othersLevel);
            }
        });
        return list;
    }

    public static Integer getPriorityLevel(int presence, int friendShipStatus) {
        Integer level = 99;
        if (friendShipStatus == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED) {
            level = 0;
        } else if (friendShipStatus == StatusConstants.FRIENDSHIP_STATUS_INCOMING) {
            level = 1;
        } else if (friendShipStatus == StatusConstants.FRIENDSHIP_STATUS_PENDING) {
            level = 2;
        }
        return level;
    }

    public void showFriend(UserBasicInfo friendInfo, boolean show) {
        SingleFriendPanel friendPanel = MyfnfHashMaps.getInstance().getSingleFriendPanel().get(friendInfo.getUserIdentity());
        if (friendPanel != null) {
            // friendPanel.changeFriendShipStatus();
            friendPanel.setVisible(show);
        } else {
            friendPanel = new SingleFriendPanel(friendInfo);
            friendPanel.setName(friendInfo.getUserIdentity());
            friendPanel.setVisible(show);
            MyfnfHashMaps.getInstance().getSingleFriendPanel().put(friendInfo.getUserIdentity(), friendPanel);
            container.friendListWrapperPanel.add(friendPanel);
        }
        container.friendListWrapperPanel.revalidate();
    }

    public void set_reset_defalut_text(JTextField textfield, String default_text, Boolean status) {
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

    public static void setFriendSelectionColor(String userIdentity) {
        AllFriendList.selectedFriendUserIdentity = userIdentity;
        if (AllFriendList.selectedFriendPanel != null) {
            AllFriendList.selectedFriendPanel.setBackground(Color.WHITE);
        }
        if (userIdentity != null && userIdentity.trim().length() > 0) {
            SingleFriendPanel friendPanelInBook = MyfnfHashMaps.getInstance().getSingleFriendPanel().get(userIdentity);
            if (friendPanelInBook != null) {
                friendPanelInBook.setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
                AllFriendList.selectedFriendPanel = friendPanelInBook;
            }
        }
    }
}
