/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.addfriend;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.utility.ImagePane;
import com.ipvision.view.utility.SeparatorPanel;

/**
 *
 * @author Wasif Islam
 */
public class AddFriendPanel extends JPanel implements MouseListener {

    private JPanel categoryPanel;
    private ImagePane migrationTabPane = new ImagePane();
    private ImagePane suggestionTabPane = new ImagePane();
    private ImagePane settingsTabPane = new ImagePane();

    private JLabel migrationLbl = new JLabel();
    private JLabel suggestionLbl = new JLabel();
    private JLabel settingsLbl = new JLabel();

    public static final String MENU_TYPE_MIGRATION = "MENU_TYPE_MIGRATION";
    public static final String MENU_TYPE_SUGGESTION = "MENU_TYPE_SUGGESTION";
    public static final String MENU_TYPE_SETTINGS = "MENU_TYPE_SETTINGS";

    private Color defaultBgColor = Color.WHITE;//RingColorCode.DEFAULT_BACKGROUND_COLOR;
    private Color selectedBgColor = Color.WHITE;

    private Color defaultFontColor = Color.BLACK;
    private Color selectedFontColor = RingColorCode.RING_THEME_COLOR;

    private Border selectedBorder = new MatteBorder(0, 0, 2, 0, RingColorCode.RING_THEME_COLOR);

    private JPanel mainPanel;

    private MigrationPanel migrationPanel;
    private SuggestionPanel suggestionPanel;
    private SettingsPanel settingsPanel;

    //public static final int MENU_TYPE_MIGRATION = 1;
    public static String selectedFriendUserIdentity = null;
    public static SingleAddFriendPanel selectedFriendPanel = null;
    private static int sepWidth = 1, panelHeight = 50, topBottomgap = 8;

    public MigrationPanel getMigrationPanel() {
        return migrationPanel;
    }

    public SuggestionPanel getSuggestionPanel() {
        return suggestionPanel;
    }

    public SettingsPanel getSettingsPanel() {
        return settingsPanel;
    }

    public AddFriendPanel() {
        setLayout(new BorderLayout(DefaultSettings.DEFAULT_BORDER_WIDTH, DefaultSettings.DEFAULT_BORDER_WIDTH));
        setOpaque(false);

        categoryPanel = new JPanel();
        categoryPanel.setLayout(new GridLayout(0, 3, 0, 0));
        categoryPanel.setOpaque(false);
        categoryPanel.setPreferredSize(new Dimension(0, DefaultSettings.ADD_FRIEND_CATEGORY_HEIGHT));
        // categoryPanel.setBorder(new MatteBorder(0, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR));
        add(categoryPanel, BorderLayout.NORTH);
        init();

        mainPanel = new JPanel() {

//            @Override
//            public void doLayout() {
//                super.doLayout(); //To change body of generated methods, choose Tools | Templates.
//                mainPanel.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH, GuiRingID.getInstance().getMainRight().getAllFeedsView().getHeight() - 100));
//                mainPanel.revalidate();
//            }
        };
        //mainPanel.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH, DefaultSettings.ADD_FRIEND_CONTENT_PANEL_HEIGHT));
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(selectedBgColor);

        add(mainPanel, BorderLayout.CENTER);

        action_of_migration_panel();

    }

    private void init() {
        categoryPanel.add(getSingleCategoryPane(migrationTabPane, migrationLbl, "Migration", true));
        categoryPanel.add(getSingleCategoryPane(suggestionTabPane, suggestionLbl, "Suggestion", true));
        categoryPanel.add(getSingleCategoryPane(settingsTabPane, settingsLbl, "Settings", false));
    }

    public void resetMainButtonsColor(String selectedMenu) {
        if (selectedMenu == null) {
            migrationTabPane.setBackground(defaultBgColor);
            suggestionTabPane.setBackground(defaultBgColor);
            settingsTabPane.setBackground(defaultBgColor);

            migrationLbl.setForeground(defaultFontColor);
            suggestionLbl.setForeground(defaultFontColor);
            settingsLbl.setForeground(defaultFontColor);

            migrationTabPane.setBorder(null);
            suggestionTabPane.setBorder(null);
            settingsTabPane.setBorder(null);
        } else if (selectedMenu.equalsIgnoreCase(MENU_TYPE_MIGRATION)) {
            migrationTabPane.setBackground(selectedBgColor);
            suggestionTabPane.setBackground(defaultBgColor);
            settingsTabPane.setBackground(defaultBgColor);

            migrationLbl.setForeground(selectedFontColor);
            suggestionLbl.setForeground(defaultFontColor);
            settingsLbl.setForeground(defaultFontColor);

            migrationTabPane.setBorder(selectedBorder);
            suggestionTabPane.setBorder(null);
            settingsTabPane.setBorder(null);
        } else if (selectedMenu.equalsIgnoreCase(MENU_TYPE_SUGGESTION)) {
            migrationTabPane.setBackground(defaultBgColor);
            suggestionTabPane.setBackground(selectedBgColor);
            settingsTabPane.setBackground(defaultBgColor);

            migrationLbl.setForeground(defaultFontColor);
            suggestionLbl.setForeground(selectedFontColor);
            settingsLbl.setForeground(defaultFontColor);

            migrationTabPane.setBorder(null);
            suggestionTabPane.setBorder(selectedBorder);
            settingsTabPane.setBorder(null);
        } else if (selectedMenu.equalsIgnoreCase(MENU_TYPE_SETTINGS)) {
            migrationTabPane.setBackground(defaultBgColor);
            suggestionTabPane.setBackground(defaultBgColor);
            settingsTabPane.setBackground(selectedBgColor);

            migrationLbl.setForeground(defaultFontColor);
            suggestionLbl.setForeground(defaultFontColor);
            settingsLbl.setForeground(selectedFontColor);

            migrationTabPane.setBorder(null);
            suggestionTabPane.setBorder(null);
            settingsTabPane.setBorder(selectedBorder);
        }
    }

    public static void addButtonActionPanel(String userIdentity) {
        selectedFriendUserIdentity = userIdentity;
        if (selectedFriendPanel != null) {
            if (selectedFriendPanel.buttonActionPanel != null) {
                selectedFriendPanel.buttonActionPanel.setVisible(false);
            }
            selectedFriendPanel.friendInfoPanel.setVisible(true); //off foucs
            selectedFriendPanel.friendInfoPanel.setBackground(RingColorCode.FRIEND_LIST_BG_COLOR);
        }
        if (userIdentity != null && userIdentity.trim().length() > 0) {
            SingleAddFriendPanel friendPanelInBook = MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().get(userIdentity);
            if (friendPanelInBook != null) {
                selectedFriendPanel = friendPanelInBook;
                if (selectedFriendPanel.buttonActionPanel != null) {
                    selectedFriendPanel.buttonActionPanel.setVisible(true);
                }
                selectedFriendPanel.friendInfoPanel.setVisible(false);
                selectedFriendPanel.friendInfoPanel.setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
            }
        }
    }
    /*public static void setFriendSelectionColor(String userIdentity) {
     selectedFriendUserIdentity = userIdentity;
     if (selectedFriendPanel != null) {
     selectedFriendPanel.friendInfoPanel.setBackground(RingColorCode.FRIEND_LIST_BG_COLOR);//off foucs
     }
     if (userIdentity != null && userIdentity.trim().length() > 0) {
     SingleAddFriendPanel friendPanelInBook = MyfnfHashMaps.getInstance().getSingleFriendPanelInFeed().get(userIdentity);
     if (friendPanelInBook != null) {
     selectedFriendPanel = friendPanelInBook;
     selectedFriendPanel.friendInfoPanel.setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
     }
     }
     }*/

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
    private void action_of_migration_panel() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                resetMainButtonsColor(MENU_TYPE_MIGRATION);
                if (migrationPanel == null) {
                    migrationPanel = new MigrationPanel();
                } else {
                    migrationPanel.start_migration();
                    //migrationPanel.add_contents();
                }
                mainPanel.removeAll();
                mainPanel.add(migrationPanel, BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
    }

    private void action_of_settings_panel() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                resetMainButtonsColor(MENU_TYPE_SETTINGS);
                if (settingsPanel == null) {
                    settingsPanel = new SettingsPanel();
                }
                mainPanel.removeAll();
                mainPanel.add(settingsPanel, BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
    }

    private void action_of_suggestion_panel() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                resetMainButtonsColor(MENU_TYPE_SUGGESTION);
                if (suggestionPanel == null) {
                    suggestionPanel = new SuggestionPanel();
                }
                mainPanel.removeAll();
                mainPanel.add(suggestionPanel, BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == migrationTabPane) {
            action_of_migration_panel();
        } else if (e.getSource() == suggestionTabPane) {
            action_of_suggestion_panel();
        } else if (e.getSource() == settingsTabPane) {
            action_of_settings_panel();
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
        if (e.getSource() == migrationTabPane) {

        } else if (e.getSource() == suggestionTabPane) {

        } else if (e.getSource() == settingsTabPane) {

        }

    }

    @Override
    public void mouseExited(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        if (e.getSource() == migrationTabPane) {
        } else if (e.getSource() == suggestionTabPane) {

        } else if (e.getSource() == settingsTabPane) {

        }

    }
}
