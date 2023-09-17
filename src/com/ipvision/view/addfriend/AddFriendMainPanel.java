/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.addfriend;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.model.constants.StatusConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.utility.CenterLayout;
import com.ipvision.view.utility.ImagePane;
import com.ipvision.view.utility.SeparatorPanel;
import javax.swing.Box;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author Wasif Islam
 */
public class AddFriendMainPanel extends JPanel implements MouseListener, AncestorListener {

    private ImagePane topAddTabPane = new ImagePane();
    private ImagePane topInviteTabPane = new ImagePane();
    private ImagePane topSearchTabPane = new ImagePane();
    private ImagePane topPendingTabPane = new ImagePane();

    private BufferedImage topAddTabPaneImg = null, topAddTabPaneHoverImg = null, topAddTabPaneSelectedImg = null;
    private BufferedImage topInviteTabPaneImg = null, topInviteTabPaneHoverImg = null, topInviteTabPaneSelectedImg = null;
    private BufferedImage topSearchTabPaneImg = null, topSearchTabPaneHoverImg = null, topSearchTabPaneSelectedImg = null;
    private BufferedImage topPendingTabPaneImg = null, topPendingTabPaneHoverImg = null, topPendingTabPaneSelectedImg = null;

    private JPanel addTab, searchTab, inviteTab, pendingTab;

    public final String MENU_TYPE_ADD = "MENU_TYPE_ADD";
    public final String MENU_TYPE_INVITE = "MENU_TYPE_INVITE";
    public final String MENU_TYPE_SEARCH = "MENU_TYPE_SEARCH";
    public final String MENU_TYPE_PENDING = "MENU_TYPE_PENDING";

    private Color defaultBgColor = RingColorCode.DEFAULT_BACKGROUND_COLOR;
    private Color selectedBgColor = Color.WHITE;

    private AddFriendPanel addFriendPanel;
    private SearchFriendPanel searchFriendPanel;
    private InvitePanel invitePanel;
    private PendingFriendPanel pendingFriendPanel;

    private JPanel mainPanel;
    private ImageIcon seperatorImg = DesignClasses.return_image(GetImages.MENU_BAR_SEPARATOR);
    private Border selectedBorder = new MatteBorder(0, 0, 5, 0, RingColorCode.RING_THEME_COLOR);

    private Color defaultFontColor = Color.BLACK;
    private Color selectedFontColor = RingColorCode.RING_THEME_COLOR;
    private JLabel addLbl = new JLabel("Add");
    private JLabel inviteLbl = new JLabel("Invite");
    private JLabel searchLbl = new JLabel("Search");
    private JLabel pendingLbl = new JLabel("Pending");
    private int sepWidth = 1, panelHeight = 50, topBottomgap = 8;
    private int right = 25;

    //public static final int SUBMENU_TYPE_MIGRATION = 1;
    public SearchFriendPanel getSearchFriendPanel() {
        return searchFriendPanel;
    }

    public AddFriendPanel getAddFriendPanel() {
        return addFriendPanel;
    }

    public InvitePanel getInvitePanel() {
        return invitePanel;
    }

    public PendingFriendPanel getPendingFriendPanel() {
        return pendingFriendPanel;
    }

    public void refreshAllFriendTabs() {
        if (getSearchFriendPanel() != null) {
            getSearchFriendPanel().addSearchResultInContainer();
        }
        if (getAddFriendPanel() != null) {
            if (getAddFriendPanel().getMigrationPanel() != null) {
                getAddFriendPanel().getMigrationPanel().add_contents();
            }
            if (getAddFriendPanel().getSuggestionPanel() != null) {
                getAddFriendPanel().getSuggestionPanel().add_contents();
            }
        }
        if (getPendingFriendPanel() != null) {
            getPendingFriendPanel().add_contents();
        }
    }

    public AddFriendMainPanel() {
        //setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        setLayout(new CenterLayout(DefaultSettings.COVER_PIC_DISPLAY_WIDTH));
        setOpaque(false);
        addAncestorListener(this);
//        JPanel wrapper = new JPanel(new BorderLayout());
//        //wrapper.setOpaque(false);
//        wrapper.setBackground(Color.BLUE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(true);

        //wrapper.add(mainPanel, BorderLayout.NORTH);
        add(mainPanel);
        buildTopTabPanel();
    }

    private void buildTopTabPanel() {
        JPanel topTabPanel = new JPanel();//(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topTabPanel.setLayout(new GridLayout(0, 4, 0, 0));
        topTabPanel.setOpaque(false);
        //topTabPanel.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH, DefaultSettings.ADD_FRIEND_TOP_MENUBAR_HEIGHT));
        topTabPanel.setBorder(new MatteBorder(0, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR));
        mainPanel.add(topTabPanel, BorderLayout.NORTH);

        try {
            topAddTabPaneImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.ADD_ICON));
            topAddTabPaneHoverImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.ADD_ICON_H));
            topAddTabPaneSelectedImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.ADD_ICON_SELECTED));
        } catch (Exception e) {
        }
        topAddTabPane.setImage(topAddTabPaneImg);
        topAddTabPane.setRounded(false);
        topAddTabPane.setBackground(defaultBgColor);
        addLbl.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        addLbl.setForeground(Color.BLACK);
        addTab = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 6));
        // addTab.setPreferredSize(new Dimension(158, 58));
        addTab.setBackground(defaultBgColor);
        addTab.setToolTipText("Add");
        addTab.add(Box.createHorizontalStrut(25));
        addTab.add(topAddTabPane);
        addTab.add(Box.createHorizontalStrut(20));
        addTab.add(addLbl);
        addTab.add(Box.createHorizontalStrut(52));
        addTab.add(new SeparatorPanel(sepWidth, panelHeight, topBottomgap));
        addTab.addMouseListener(this);

        try {
            topInviteTabPaneImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.INVITE_ICON));
            topInviteTabPaneHoverImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.INVITE_H));
            topInviteTabPaneSelectedImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.INVITE_SELECTED));
        } catch (Exception e) {
        }
        topInviteTabPane.setImage(topInviteTabPaneImg);
        topInviteTabPane.setRounded(false);
        topInviteTabPane.setBackground(defaultBgColor);
        inviteLbl.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        inviteLbl.setForeground(Color.BLACK);
        inviteTab = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 6));
        //inviteTab.setPreferredSize(new Dimension(158, 58));
        inviteTab.setBackground(defaultBgColor);
        inviteTab.setToolTipText("Invite");
        inviteTab.add(Box.createHorizontalStrut(25));
        inviteTab.add(topInviteTabPane);
        inviteTab.add(Box.createHorizontalStrut(20));
        inviteTab.add(inviteLbl);
        inviteTab.add(Box.createHorizontalStrut(44));
        inviteTab.add(new SeparatorPanel(sepWidth, panelHeight, topBottomgap));
        inviteTab.addMouseListener(this);

        try {
            topSearchTabPaneImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.SEARCH_ICON));
            topSearchTabPaneHoverImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.SEARCH_H));
            topSearchTabPaneSelectedImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.SEARCH_SELECTED));
        } catch (Exception e) {
        }
        topSearchTabPane.setImage(topSearchTabPaneImg);
        topSearchTabPane.setRounded(false);
        topSearchTabPane.setBackground(defaultBgColor);
        searchLbl.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        searchLbl.setForeground(Color.BLACK);
        searchTab = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 6));
        //searchTab.setPreferredSize(new Dimension(158, 58));
        searchTab.setBackground(defaultBgColor);
        searchTab.setToolTipText("Search");
        searchTab.add(Box.createHorizontalStrut(25));
        searchTab.add(topSearchTabPane);
        searchTab.add(Box.createHorizontalStrut(20));
        searchTab.add(searchLbl);
        searchTab.add(Box.createHorizontalStrut(30));
        searchTab.add(new SeparatorPanel(sepWidth, panelHeight, topBottomgap));
        searchTab.addMouseListener(this);

        try {
            topPendingTabPaneImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.PENDING_ICON));
            topPendingTabPaneHoverImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.PENDING_H));
            topPendingTabPaneSelectedImg = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.PENDING_SELECTED));
        } catch (Exception e) {
        }
        topPendingTabPane.setImage(topPendingTabPaneImg);
        topPendingTabPane.setRounded(false);
        topPendingTabPane.setBackground(defaultBgColor);
        pendingLbl.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        pendingLbl.setForeground(Color.BLACK);
        pendingTab = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 12));
        //pendingTab.setPreferredSize(new Dimension(158, 58));
        pendingTab.setBackground(defaultBgColor);
        pendingTab.setToolTipText("Pending");
        pendingTab.add(Box.createHorizontalStrut(25));
        pendingTab.add(topPendingTabPane);
        pendingTab.add(Box.createHorizontalStrut(20));
        pendingTab.add(pendingLbl);
        //pendingTab.add(Box.createHorizontalStrut(30));
        //pendingTab.add(new SeparatorPanel(sepWidth, panelHeight, topBottomgap));
        pendingTab.addMouseListener(this);
        topTabPanel.add(searchTab);
        topTabPanel.add(addTab);
        topTabPanel.add(inviteTab);
        topTabPanel.add(pendingTab);
    }

    /*    public JPanel getSeperator() {
     JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); //7
     wrapper.setOpaque(false);
     wrapper.setPreferredSize(new Dimension(10, 35));
    
     JLabel seperator = new JLabel(seperatorImg);
     seperator.setOpaque(false);
     wrapper.add(seperator);
     return wrapper;
     }
     public JPanel getCustomSeperator() {
     JPanel seperator = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
     seperator.setBackground(RingColorCode.FEED_BORDER_COLOR);
     seperator.setPreferredSize(new Dimension(1, 1));
     return seperator;
     }*/
    public void resetMainButtonsColor(String selectedMenu) {
        if (selectedMenu == null) {
            topAddTabPane.setImage(topAddTabPaneImg);
            topInviteTabPane.setImage(topInviteTabPaneImg);
            topSearchTabPane.setImage(topSearchTabPaneImg);
            topPendingTabPane.setImage(topPendingTabPaneImg);

            addTab.setBorder(null);
            inviteTab.setBorder(null);
            searchTab.setBorder(null);
            pendingTab.setBorder(null);

            addLbl.setForeground(defaultFontColor);
            inviteLbl.setForeground(defaultFontColor);
            searchLbl.setForeground(defaultFontColor);
            pendingLbl.setForeground(defaultFontColor);
        } else if (selectedMenu.equalsIgnoreCase(MENU_TYPE_ADD)) {
            topAddTabPane.setImage(topAddTabPaneSelectedImg);
            topInviteTabPane.setImage(topInviteTabPaneImg);
            topSearchTabPane.setImage(topSearchTabPaneImg);
            topPendingTabPane.setImage(topPendingTabPaneImg);

            addTab.setBorder(selectedBorder);
            inviteTab.setBorder(null);
            searchTab.setBorder(null);
            pendingTab.setBorder(null);

            addTab.setBackground(selectedBgColor);
            inviteTab.setBackground(defaultBgColor);
            searchTab.setBackground(defaultBgColor);
            pendingTab.setBackground(defaultBgColor);

            addLbl.setForeground(selectedFontColor);
            inviteLbl.setForeground(defaultFontColor);
            searchLbl.setForeground(defaultFontColor);
            pendingLbl.setForeground(defaultFontColor);

            topAddTabPane.setBackground(selectedBgColor);
            topInviteTabPane.setBackground(defaultBgColor);
            topSearchTabPane.setBackground(defaultBgColor);
            topPendingTabPane.setBackground(defaultBgColor);
        } else if (selectedMenu.equalsIgnoreCase(MENU_TYPE_INVITE)) {
            topAddTabPane.setImage(topAddTabPaneImg);
            topInviteTabPane.setImage(topInviteTabPaneSelectedImg);
            topSearchTabPane.setImage(topSearchTabPaneImg);
            topPendingTabPane.setImage(topPendingTabPaneImg);

            addTab.setBorder(null);
            inviteTab.setBorder(selectedBorder);
            searchTab.setBorder(null);
            pendingTab.setBorder(null);

            addTab.setBackground(defaultBgColor);
            inviteTab.setBackground(selectedBgColor);
            searchTab.setBackground(defaultBgColor);
            pendingTab.setBackground(defaultBgColor);

            addLbl.setForeground(defaultFontColor);
            inviteLbl.setForeground(selectedFontColor);
            searchLbl.setForeground(defaultFontColor);
            pendingLbl.setForeground(defaultFontColor);

            topAddTabPane.setBackground(defaultBgColor);
            topInviteTabPane.setBackground(selectedBgColor);
            topSearchTabPane.setBackground(defaultBgColor);
            topPendingTabPane.setBackground(defaultBgColor);
        } else if (selectedMenu.equalsIgnoreCase(MENU_TYPE_SEARCH)) {
            topAddTabPane.setImage(topAddTabPaneImg);
            topInviteTabPane.setImage(topInviteTabPaneImg);
            topSearchTabPane.setImage(topSearchTabPaneSelectedImg);
            topPendingTabPane.setImage(topPendingTabPaneImg);

            addLbl.setForeground(defaultFontColor);
            inviteLbl.setForeground(defaultFontColor);
            searchLbl.setForeground(selectedFontColor);
            pendingLbl.setForeground(defaultFontColor);

            addTab.setBorder(null);
            inviteTab.setBorder(null);
            searchTab.setBorder(selectedBorder);
            pendingTab.setBorder(null);

            addTab.setBackground(defaultBgColor);
            inviteTab.setBackground(defaultBgColor);
            searchTab.setBackground(selectedBgColor);
            pendingTab.setBackground(defaultBgColor);

            topAddTabPane.setBackground(defaultBgColor);
            topInviteTabPane.setBackground(defaultBgColor);
            topSearchTabPane.setBackground(selectedBgColor);
            topPendingTabPane.setBackground(defaultBgColor);
        } else if (selectedMenu.equalsIgnoreCase(MENU_TYPE_PENDING)) {
            topAddTabPane.setImage(topAddTabPaneImg);
            topInviteTabPane.setImage(topInviteTabPaneImg);
            topSearchTabPane.setImage(topSearchTabPaneImg);
            topPendingTabPane.setImage(topPendingTabPaneSelectedImg);

            addLbl.setForeground(defaultFontColor);
            inviteLbl.setForeground(defaultFontColor);
            searchLbl.setForeground(defaultFontColor);
            pendingLbl.setForeground(selectedFontColor);

            addTab.setBorder(null);
            inviteTab.setBorder(null);
            searchTab.setBorder(null);
            pendingTab.setBorder(selectedBorder);

            addTab.setBackground(defaultBgColor);
            inviteTab.setBackground(defaultBgColor);
            searchTab.setBackground(defaultBgColor);
            pendingTab.setBackground(selectedBgColor);

            topAddTabPane.setBackground(defaultBgColor);
            topInviteTabPane.setBackground(defaultBgColor);
            topSearchTabPane.setBackground(defaultBgColor);
            topPendingTabPane.setBackground(selectedBgColor);
        }
    }

    public void action_of_add_tab_panel() {
        resetMainButtonsColor(MENU_TYPE_ADD);
        if (searchFriendPanel != null) {
            mainPanel.remove(searchFriendPanel);
        }
        if (invitePanel != null) {
            mainPanel.remove(invitePanel);
        }
        if (pendingFriendPanel != null) {
            mainPanel.remove(pendingFriendPanel);
        }
        if (addFriendPanel == null) {
            addFriendPanel = new AddFriendPanel();
        }
        mainPanel.add(addFriendPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void action_of_search_tab_panel() {
        resetMainButtonsColor(MENU_TYPE_SEARCH);
        if (addFriendPanel != null) {
            mainPanel.remove(addFriendPanel);
        }
        if (invitePanel != null) {
            mainPanel.remove(invitePanel);
        }
        if (pendingFriendPanel != null) {
            mainPanel.remove(pendingFriendPanel);
        }
        if (searchFriendPanel == null) {
            searchFriendPanel = new SearchFriendPanel();
        }
        mainPanel.add(searchFriendPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void action_of_pending_tab_panel() {
        resetMainButtonsColor(MENU_TYPE_PENDING);
        if (searchFriendPanel != null) {
            mainPanel.remove(searchFriendPanel);
        }
        if (addFriendPanel != null) {
            mainPanel.remove(addFriendPanel);
        }
        if (invitePanel != null) {
            mainPanel.remove(invitePanel);
        }
        if (pendingFriendPanel == null) {
            pendingFriendPanel = new PendingFriendPanel();
        }
        mainPanel.add(pendingFriendPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void action_of_invite_tab_panel() {
        resetMainButtonsColor(MENU_TYPE_INVITE);
        if (addFriendPanel != null) {
            mainPanel.remove(addFriendPanel);
        }
        if (searchFriendPanel != null) {
            mainPanel.remove(searchFriendPanel);
        }
        if (pendingFriendPanel != null) {
            mainPanel.remove(pendingFriendPanel);
        }
        if (invitePanel == null) {
            invitePanel = new InvitePanel();
        }
        mainPanel.add(invitePanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == addTab) {
            action_of_add_tab_panel();
        } else if (e.getSource() == inviteTab) {
            action_of_invite_tab_panel();
        } else if (e.getSource() == searchTab) {
            action_of_search_tab_panel();
        } else if (e.getSource() == pendingTab) {
            action_of_pending_tab_panel();
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
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (e.getSource() == addTab) {
            if (!topAddTabPane.getImage().equals(topAddTabPaneSelectedImg)) {
                topAddTabPane.setImage(topAddTabPaneHoverImg);
            }
        } else if (e.getSource() == inviteTab) {
            if (!topInviteTabPane.getImage().equals(topInviteTabPaneSelectedImg)) {
                topInviteTabPane.setImage(topInviteTabPaneHoverImg);
            }

        } else if (e.getSource() == searchTab) {
            if (!topSearchTabPane.getImage().equals(topSearchTabPaneSelectedImg)) {
                topSearchTabPane.setImage(topSearchTabPaneHoverImg);
            }

        } else if (e.getSource() == pendingTab) {
            if (!topPendingTabPane.getImage().equals(topPendingTabPaneSelectedImg)) {
                topPendingTabPane.setImage(topPendingTabPaneHoverImg);
            }

        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        if (e.getSource() == addTab) {
            if (!topAddTabPane.getImage().equals(topAddTabPaneSelectedImg)) {
                topAddTabPane.setImage(topAddTabPaneImg);
            }
        } else if (e.getSource() == inviteTab) {
            if (!topInviteTabPane.getImage().equals(topInviteTabPaneSelectedImg)) {
                topInviteTabPane.setImage(topInviteTabPaneImg);
            }

        } else if (e.getSource() == searchTab) {
            if (!topSearchTabPane.getImage().equals(topSearchTabPaneSelectedImg)) {
                topSearchTabPane.setImage(topSearchTabPaneImg);
            }

        } else if (e.getSource() == pendingTab) {
            if (!topPendingTabPane.getImage().equals(topPendingTabPaneSelectedImg)) {
                topPendingTabPane.setImage(topPendingTabPaneImg);
            }
        }
    }

    public static boolean isDisplayable(UserBasicInfo userBasicInfo, int type) {
        if (type == SingleActionPanel.TYPE_FOR_MIGRATION) {
            if (userBasicInfo.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED
                    && userBasicInfo.getContactType() != null && userBasicInfo.getContactType() == StatusConstants.ACCESS_CHAT_CALL
                    && !(userBasicInfo.getNewContactType() != null && userBasicInfo.getNewContactType() > 0)
                    && !(userBasicInfo.getBlocked() != null && userBasicInfo.getBlocked() > 0)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void ancestorAdded(AncestorEvent event) {
        if (GuiRingID.getInstance() != null) {
            GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().setHome(true);
        }

//        System.out.println("added");
//        if (GuiRingID.getInstance().getMainButtons() != null) {
//            GuiRingID.getInstance().getMainButtons().add(true);
//        }
    }

    @Override
    public void ancestorRemoved(AncestorEvent event) {
//        System.out.println("removed");
        if (GuiRingID.getInstance() != null) {
            GuiRingID.getInstance().getMainLeftContainer().getFriendListContainer().setHome(false);

        }
    }

    @Override
    public void ancestorMoved(AncestorEvent event) {
    }
}
