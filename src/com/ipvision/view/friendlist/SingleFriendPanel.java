/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.friendlist;

import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.StatusConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.stores.FriendList;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.view.image.ImageObjects;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 *
 * @author IPvision
 */
public class SingleFriendPanel extends JPanel implements DragGestureListener, DragSourceListener, MouseListener {

    private DragGestureRecognizer dragGestureRecognizer;
    public String userIdentity;
    private final JLabel frienImageLabel;
    private final JLabel fullNameLabel;
    //  private final JLabel lastMessageLabel;
    // private final JPanel lastMessagePanel;
    private final JPanel friendShipStatusPanel;
    private final JLabel friendShipStatusLabel;
    int friendInfowidth = 230;
    public JPanel friendInfoPanel;
    public JLabel accessIcon;
    public boolean callChatAccess = true;
    //public JPanel friendIconImage;
    public UserBasicInfo user;
    private final JPanel fullNameWhatInmindWrapper;
    SingleFriendPopupMenu popupMenu;
    SingleFriendPanel friendPanelInBook;
    private int thumbSize = 35;
    public MiniCallChatPanel miniCallChatPanel;

    public SingleFriendPanel(UserBasicInfo entity) {
        this.user = entity;
        this.userIdentity = entity.getUserIdentity();
        frienImageLabel = new JLabel();
        miniCallChatPanel = new MiniCallChatPanel(userIdentity);
        this.setLayout(new BorderLayout());
        setBackground(RingColorCode.FRIEND_LIST_BG_COLOR);
        friendInfoPanel = new JPanel(new BorderLayout());
        friendInfoPanel.setBackground(RingColorCode.FRIEND_LIST_BG_COLOR);
        friendInfoPanel.setBorder(new EmptyBorder(0, DefaultSettings.LEFT_EMPTY_SPACE, 0, 0));
        friendInfoPanel.setPreferredSize(new Dimension(DefaultSettings.LEFT_SINGLE_PANEL_WIDTH, DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT + 5));
        friendInfoPanel.addMouseListener(this);
        JPanel friendImagePanel = new JPanel(new BorderLayout());
        friendImagePanel.setPreferredSize(new Dimension(DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT + DefaultSettings.VGAP_5, DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT));
        friendImagePanel.setBorder(new EmptyBorder(0, 5, 0, 0));
        friendImagePanel.add(frienImageLabel, BorderLayout.CENTER);
        friendImagePanel.setOpaque(false);
        fullNameLabel = DesignClasses.makeJLabelFullName("", 13);
        fullNameLabel.setPreferredSize(new Dimension(friendInfowidth - 70, 13));
//        lastMessagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
//        lastMessagePanel.setOpaque(false);
//        lastMessageLabel = DesignClasses.makeJLabelUnderFullName("");
//        lastMessageLabel.setForeground(DefaultSettings.DEFAULT_TITLE_COLOR_IN_FRIEND);
//        lastMessagePanel.add(lastMessageLabel);
        friendShipStatusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        friendShipStatusPanel.setOpaque(false);
        friendShipStatusLabel = DesignClasses.makeJLabelUnderFullName("");
        friendShipStatusLabel.setPreferredSize(new Dimension(friendInfowidth - 70, 10));
        fullNameWhatInmindWrapper = new JPanel(new GridBagLayout());
        fullNameWhatInmindWrapper.setOpaque(false);
        GridBagConstraints con = new GridBagConstraints();
        con.gridx = 0;
        con.gridy = 0;
        fullNameWhatInmindWrapper.add(fullNameLabel, con);
        con.gridy++;
        fullNameWhatInmindWrapper.add(friendShipStatusLabel, con);
        accessIcon = new JLabel();
        friendInfoPanel.add(friendImagePanel, BorderLayout.WEST);
        friendInfoPanel.add(fullNameWhatInmindWrapper, BorderLayout.CENTER);
        friendInfoPanel.add(accessIcon, BorderLayout.EAST);
        add(friendInfoPanel, BorderLayout.CENTER);
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        changeFriendAllInformation();
                    }
                }
        );
    }

    public void changeUnknownInformation() {

        if (user.getProfileImage() != null) {
            ImageHelpers.addProfileImageThumbInQueue(frienImageLabel, user.getProfileImage(), thumbSize, HelperMethods.getShortName(user.getFullName()));
        } else {
            BufferedImage img = ImageHelpers.createDefaultBufferImage(35, HelperMethods.getShortName(user.getFullName()));//ImageHelpers.getUnknownImage(true);//.getBufferImageFromurl(null);
            frienImageLabel.setIcon(new ImageIcon(img));
            // img = null;
            img.flush();
        }

        if ((user.getFullName() != null && user.getFullName().trim().length() > 0)) {
            fullNameLabel.setText(HelperMethods.getUserFullName(user));
            fullNameLabel.revalidate();
        }
    }

    public void changeFriendAllInformation() {
        if (FriendList.getInstance().getFriend_hash_map().get(userIdentity) != null) {
            changeFriendProfileImage();
            changeFullName();
        } else {
            changeUnknownInformation();
        }
        changeAccessImage(false);
        changeFriendShipStatus();
        setBorder(new MatteBorder(0, 0, 1, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR));
    }

    public void changeFriendProfileImage() {
        try {
            UserBasicInfo friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(this.userIdentity);
            short[] friendPrivacy = new short[5];
            if (friendProfileInfo.getPrivacy() != null) {
                friendPrivacy = friendProfileInfo.getPrivacy();
            }
            short profileImagePrivacy = friendPrivacy[2];
            if (friendProfileInfo.getProfileImage() != null
                    && friendProfileInfo.getProfileImage().trim().length() > 0
                    && profileImagePrivacy > 0
                    && (profileImagePrivacy == AppConstants.PRIVACY_SHORT_PUBLIC || profileImagePrivacy == AppConstants.PRIVACY_SHORT_ONLY_FRIEND)) {
                ImageHelpers.addProfileImageThumbInQueue(frienImageLabel, friendProfileInfo.getProfileImage(), thumbSize, HelperMethods.getShortName(user.getFullName()));
            } else {
                BufferedImage img = ImageHelpers.createDefaultBufferImage(35, HelperMethods.getShortName(user.getFullName()));//ImageHelpers.getUnknownImage(true);
                frienImageLabel.setIcon(new ImageIcon(img));
            }
        } catch (Exception ex) {
        }
        frienImageLabel.revalidate();
    }

    public void changeFullName() {
        try {
            UserBasicInfo friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(this.userIdentity);
            fullNameLabel.setText(HelperMethods.getUserFullName(friendProfileInfo));
            fullNameLabel.revalidate();
        } catch (Exception e) {
        }
    }

    public void changeFriendShipStatus() {
        try {
            if (dragGestureRecognizer != null) {
                dragGestureRecognizer.removeDragGestureListener(this);
            }
            friendShipStatusLabel.setText("");
            UserBasicInfo friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(this.userIdentity);
            if (friendProfileInfo != null) {
                if (friendProfileInfo.getBlocked() != null && friendProfileInfo.getBlocked() > 0) {
                    friendShipStatusLabel.setText("Blocked");
                }
                dragGestureRecognizer = DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(friendInfoPanel, DnDConstants.ACTION_COPY_OR_MOVE, this);
            }
            friendShipStatusPanel.revalidate();
            friendShipStatusPanel.repaint();
        } catch (Exception ex) {
        }

    }

    private SingleFriendPanel getThis() {
        return this;
    }

    public void setCallChatAccess() {
        UserBasicInfo friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(userIdentity);
        if (friendProfileInfo != null) {
            if (friendProfileInfo.getContactType() != null && friendProfileInfo.getContactType() == StatusConstants.ACCESS_CHAT_CALL) {
                callChatAccess = true;
            } else if (friendProfileInfo.getContactType() != null && friendProfileInfo.getContactType() == StatusConstants.ACCESS_FULL) {
                callChatAccess = false;
            }
        }
    }

    public void changeAccessImage(boolean selected) {
        setCallChatAccess();
//        if (callChatAccess) {
//            if (selected) {
//                accessIcon.setIcon(DesignClasses.return_image(GetImages.FRIEND_CALL_CHAT_ACCESS_SELECTED));
//            } else {
//                accessIcon.setIcon(DesignClasses.return_image(GetImages.FRIEND_CALL_CHAT_ACCESS_DEFAULT));
//            }
//        } else {
//            if (selected) {
//                accessIcon.setIcon(DesignClasses.return_image(GetImages.FRIEND_FULL_ACCESS_SELECTED));
//            } else {
//                accessIcon.setIcon(DesignClasses.return_image(GetImages.FRIEND_FULL_ACCESS_DEFAULT));
//            }
//        }
        if (callChatAccess) {
            if (selected) {
                accessIcon.setIcon(ImageObjects.friend_call_chat_selected);
            } else {
                accessIcon.setIcon(ImageObjects.friend_call_chat_access);
            }
        } else {
            if (selected) {
                accessIcon.setIcon(ImageObjects.friend_full_access_selected);
            } else {
                accessIcon.setIcon(ImageObjects.friend_full_access_default);
            }
        }
    }

    public BufferedImage buildDragBufferImage(String userIdentity) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Arial", Font.ITALIC, 12);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(userIdentity);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);

        fm = g2d.getFontMetrics();
        g2d.setColor(Color.BLACK);
        g2d.drawString(userIdentity, 0, fm.getAscent());
        g2d.dispose();

        return img;
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent dge) {
        Transferable transferable = new StringSelection(userIdentity);
        UserBasicInfo entity = FriendList.getInstance().getFriend_hash_map().get(this.userIdentity);
        this.setBackground(Color.WHITE);
        dge.startDrag(DragSource.DefaultCopyDrop, buildDragBufferImage(HelperMethods.getUserFullName(entity)), new Point(- 30, 0), transferable, this);
    }

    @Override
    public void dragEnter(DragSourceDragEvent dsde) {
    }

    @Override
    public void dragOver(DragSourceDragEvent dsde) {
    }

    @Override
    public void dropActionChanged(DragSourceDragEvent dsde) {
    }

    @Override
    public void dragExit(DragSourceEvent dse) {
        System.out.println("dragExit");
    }

    @Override
    public void dragDropEnd(DragSourceDropEvent dsde) {
        if (prevSelectedColor != null
                && AllFriendList.selectedFriendPanel != null
                && AllFriendList.selectedFriendPanel != getThis()) {
            AllFriendList.selectedFriendPanel.friendInfoPanel.setBackground(prevSelectedColor);
        }
        if (color != null) {
            friendInfoPanel.setBackground(color);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
    Color ringColor;

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == friendInfoPanel && SwingUtilities.isLeftMouseButton(e)) {
            if (AllFriendList.selectedFriendPanel != null
                    && AllFriendList.selectedFriendPanel != getThis()) {
                prevSelectedColor = AllFriendList.selectedFriendPanel.friendInfoPanel.getBackground();
                AllFriendList.selectedFriendPanel.friendInfoPanel.setBackground(RingColorCode.FRIEND_LIST_BG_COLOR);
            } else {
                prevSelectedColor = null;
            }

            if (AllFriendList.selectedFriendPanel == null
                    || AllFriendList.selectedFriendPanel != getThis()) {
                color = friendInfoPanel.getBackground();
                friendInfoPanel.setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
            } else {
                color = null;
            }
        }
    }
    Color color;
    Color prevSelectedColor;

    @Override
    public void mouseReleased(final MouseEvent e) {
        if (e.getSource() == friendInfoPanel && SwingUtilities.isLeftMouseButton(e)) {
            if (AllFriendList.selectedFriendPanel == null || AllFriendList.selectedFriendPanel.friendInfoPanel != friendInfoPanel) {
                add(miniCallChatPanel, BorderLayout.SOUTH);
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
                            if (userIdentity.equals(MyFnFSettings.LOGIN_USER_ID)) {
                                GuiRingID.getInstance().getMainRight().action_of_myProfile_button();
                            } else if (FriendList.getInstance().getFriend_hash_map().get(userIdentity) != null) {
                                GuiRingID.getInstance().showFriendProfile(userIdentity);
                            } else {
                                GuiRingID.getInstance().getMainRight().showUnknownProfile(user, 3);
                            }
                        }

                    }

                });
            } else {
                if (AllFriendList.selectedFriendPanel.friendInfoPanel == friendInfoPanel) {
                    if (AllFriendList.selectedFriendPanel.getComponentCount() > 1) {
                        AllFriendList.selectedFriendPanel.remove(1);
                    } else {
                        AllFriendList.selectedFriendPanel.add(miniCallChatPanel, BorderLayout.SOUTH);
                    }
                    AllFriendList.selectedFriendPanel.revalidate();
                }

            }
        } else if (e.getSource() == friendInfoPanel && SwingUtilities.isRightMouseButton(e)) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {

                    popupMenu = new SingleFriendPopupMenu(user);
                    int screenHeight = DefaultSettings.MONITOR_HALF_HEIGHT * 2;
                    int popupHeight = (int) popupMenu.getPreferredSize().getHeight();

                    if (e.getYOnScreen() + popupHeight > screenHeight) {
                        popupMenu.show(e.getComponent(), e.getX(), e.getY() - popupHeight);
                    } else {
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }

//                    if (userIdentity != null && userIdentity.trim().length() > 0) {
//                        friendPanelInBook = MyfnfHashMaps.getInstance().getSingleFriendPanel().get(userIdentity);
//                        if (friendPanelInBook != null) {
//                            UserBasicInfo friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(userIdentity);
//                            if (friendProfileInfo != null) {
//                                if (friendProfileInfo.getContactType() != null && friendProfileInfo.getContactType() == StatusConstants.ACCESS_CHAT_CALL) {
//                                    friendPanelInBook.accessIcon.setIcon(DesignClasses.return_image(GetImages.FRIEND_CALL_CHAT_ACCESS_SELECTED));
//                                    friendPanelInBook.callChatAccess = true;
//                                } else if (friendProfileInfo.getContactType() != null && friendProfileInfo.getContactType() == StatusConstants.ACCESS_FULL) {
//                                    friendPanelInBook.accessIcon.setIcon(DesignClasses.return_image(GetImages.FRIEND_FULL_ACCESS_SELECTED));
//                                    friendPanelInBook.callChatAccess = false;
//                                }
//                            }
//                        }
//                    }
                    final Color bgColor = friendInfoPanel.getBackground();
                    if (AllFriendList.selectedFriendPanel == null || AllFriendList.selectedFriendPanel.friendInfoPanel != friendInfoPanel) {
                        friendInfoPanel.setBackground(RingColorCode.FRIEND_LIST_RIGHT_BUTTON_PRESSED_COLOR);
                    }
                    popupMenu.addPopupMenuListener(new PopupMenuListener() {
                        @Override
                        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                        }

                        @Override
                        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                            friendInfoPanel.setBackground(bgColor);
//                            if (friendPanelInBook.callChatAccess) {
//                                friendPanelInBook.accessIcon.setIcon(DesignClasses.return_image(GetImages.FRIEND_CALL_CHAT_ACCESS_DEFAULT));
//                            } else {
//                                friendPanelInBook.accessIcon.setIcon(DesignClasses.return_image(GetImages.FRIEND_FULL_ACCESS_DEFAULT));
//                            }
                        }

                        @Override
                        public void popupMenuCanceled(PopupMenuEvent e) {

                        }
                    });
                }

            });
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

        if (e.getSource() == accessIcon) {
            accessIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
