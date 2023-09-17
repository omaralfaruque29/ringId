/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.group;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.Map;
import java.util.Objects;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.AddMemberInGroup;
import com.ipvision.service.RemoveMemberFromGroup;
import com.ipvision.service.DeleteGroup;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.model.GroupMemberDTO;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.utility.JCustomMenuPopup;
import com.ipvision.view.utility.JOptionPanelBasics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 *
 * @author Wasif Islam
 */
public class SingleGroupPanel extends JPanel {

    public long groupId;
    private GroupInfoDTO groupDto;
    private int height = 50;
//    private JPopupMenu actionPopUp;
//    public JMenuItem mnuLeaveGroup;
//    public JMenuItem mnuDeleteGroup;
//    public JMenuItem mnuJoinGroup;

    JCustomMenuPopup leaveDeletePopup = null;
    JCustomMenuPopup joinDeletePopup = null;
    JCustomMenuPopup onlyLeavePopup = null;
    private final String MNU_LEAVE = "Leave from Group";
    private final String MNU_DELETE = "Delete this Group";
    private final String MNU_JOIN = "Join this Group";
    private JPanel holder, actionPopUpPanel;
    JLabel groupName;
    private JButton actionPopUpButton;
    private GridBagConstraints c;
    public JPanel np;

    public SingleGroupPanel(GroupInfoDTO groupDto) {
        this.groupDto = groupDto;
        this.groupId = groupDto.getGroupId();
        this.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH + 10, height));
        this.initComponents();
    }

    private void initComponents() {
        holder = new JPanel(new FlowLayout(FlowLayout.LEFT));
        holder.setBackground(Color.WHITE);
        holder.addMouseListener(mouseListener);
        holder.add(Box.createRigidArea(new Dimension(5, 20)));
        JLabel groupMembersImageLabel = new JLabel();//DesignClasses.create_imageJlabel(GetImages.GROUP_MINI);
        holder.add(groupMembersImageLabel);
        DesignClasses.setGroupProfileImage(groupMembersImageLabel, groupId);

        groupName = DesignClasses.makeJLabelFullName2(groupDto.getGroupName(), DefaultSettings.FULL_NAME_FONT_SIZE_IN_FEED); //DesignClasses.makeJLabelFullName(circleDto.getCircleName(), 13);
        groupName.setCursor(new Cursor(Cursor.HAND_CURSOR));
        groupName.addMouseListener(new MouseAdapter() {
            Font original;
            @Override
            public void mouseEntered(MouseEvent e) {
                original = e.getComponent().getFont();
                Map attributes = original.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                e.getComponent().setFont(original.deriveFont(attributes));
                holder.setBackground(RingColorCode.FRIEND_LIST_RIGHT_BUTTON_PRESSED_COLOR);
                actionPopUpButton.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.getComponent().setFont(original);
                holder.setBackground(Color.WHITE);
                actionPopUpButton.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
                    GuiRingID.getInstance().getMainRight().showGroupPanel(groupDto.getGroupId());
                }
            }
        });
        JPanel groupNameAndMEmbersPanel = new JPanel(new BorderLayout());
        groupNameAndMEmbersPanel.setPreferredSize(new Dimension(400, 30));
        groupNameAndMEmbersPanel.setOpaque(false);
        groupNameAndMEmbersPanel.add(groupName, BorderLayout.NORTH);

        long numberOfMembers = HelperMethods.getGroupMembersCount(MyfnfHashMaps.getInstance().getGroup_hash_map().get(groupId));
        JLabel members = DesignClasses.makeJLabel_normal(numberOfMembers + " Members", 10, DefaultSettings.disable_font_color);
       // members.setToolTipText("Number of Members (" + numberOfMembers + ")");
//        JPanel membersPanel = new JPanel(new BorderLayout());
//        //membersPanel.setPreferredSize(new Dimension(80, 20));
//        membersPanel.setOpaque(false);
//        membersPanel.add(members, BorderLayout.CENTER);
        groupNameAndMEmbersPanel.add(members, BorderLayout.CENTER);
        holder.add(groupNameAndMEmbersPanel);
        // holder.add(Box.createRigidArea(new Dimension(100, 20)));
        // holder.add(membersPanel);
        holder.add(Box.createRigidArea(new Dimension(120, 20)));

        //             for popupmenu
        leaveDeletePopup = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_JOIN_LEAVE);
        leaveDeletePopup.addMenu(MNU_LEAVE, GetImages.LEAVE_PHOTO);
        leaveDeletePopup.addMenu(MNU_DELETE, GetImages.DELETE_PHOTO);
        leaveDeletePopup.addWindowListener(listener);

        // JoinDelete
        joinDeletePopup = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_JOIN_LEAVE);
        joinDeletePopup.addMenu(MNU_JOIN, GetImages.JOIN_PHOTO);
        joinDeletePopup.addMenu(MNU_DELETE, GetImages.DELETE_PHOTO);
        joinDeletePopup.addWindowListener(listener);
        //
        // OnlyLeave
        onlyLeavePopup = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_ONLY_LEAVE);
        onlyLeavePopup.addMenu(MNU_LEAVE, GetImages.LEAVE_PHOTO);
        onlyLeavePopup.addWindowListener(listener);

        actionPopUpButton = DesignClasses.createImageButton(GetImages.IMAGE_BOTTOM_ARROW, GetImages.IMAGE_BOTTOM_ARROW_H, "Settings");
        actionPopUpButton.setVisible(false);
        actionPopUpPanel = new JPanel(new BorderLayout());
        actionPopUpPanel.setOpaque(false);
        actionPopUpPanel.setPreferredSize(new Dimension(15, 20));
        actionPopUpPanel.add(actionPopUpButton, BorderLayout.CENTER);
        holder.add(actionPopUpPanel);
        actionPopUpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                actionPopUpButton.setVisible(true);
                holder.setBackground(RingColorCode.FRIEND_LIST_RIGHT_BUTTON_PRESSED_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!leaveDeletePopup.isVisible() && !joinDeletePopup.isVisible() && !onlyLeavePopup.isVisible()) {
                    actionPopUpButton.setVisible(false);
                    holder.setBackground(Color.WHITE);
                }
            }

        });
        actionPopUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(groupDto.getOwnerUserTableId(), MyFnFSettings.userProfile.getUserTableId())) {
                    if (check_already_in_group(MyFnFSettings.LOGIN_USER_ID)) {
                        leaveDeletePopup.setVisible(actionPopUpButton, 159, -5);//wdth ,height//(118, -8 suitable for 2 word 2 line)
                    } else {
                        joinDeletePopup.setVisible(actionPopUpButton, 159, -5);
                    }

                } else {
                    onlyLeavePopup.setVisible(actionPopUpButton, 159, -5);
                }
            }
        });
        this.add(holder, c);
        c.gridy++;
        c.anchor = GridBagConstraints.CENTER;
        np = new JPanel();
        np.setBackground(Color.WHITE);
        np.setPreferredSize(new Dimension(560, 2));
        np.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.COMMENTS_BORDER_COLOR));

        this.add(np, c);
    }

    public boolean check_already_in_group(String user_id) {
        GroupInfoDTO groupInfoDTO = MyfnfHashMaps.getInstance().getGroup_hash_map().get(groupId);
        if (groupInfoDTO != null && groupInfoDTO.getMemberMap() != null && groupInfoDTO.getMemberMap().get(user_id) != null) {
            return true;
        }
        return false;
    }

    MouseListener menuListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            if (menu.text.equalsIgnoreCase(MNU_LEAVE)) {
                leaveDeletePopup.hideThis();
                onlyLeavePopup.hideThis();
                HelperMethods.showConfirmationDialogMessage(NotificationMessages.LEAVE_NOTIFICAITON);
                if (JOptionPanelBasics.YES_NO) {
                    new RemoveMemberFromGroup(groupId, MyFnFSettings.LOGIN_USER_ID, null).start();
                }

            } else if (menu.text.equalsIgnoreCase(MNU_DELETE)) {
                leaveDeletePopup.hideThis();
                HelperMethods.showConfirmationDialogMessage(NotificationMessages.DELETE_NOTIFICAITON);
                if (JOptionPanelBasics.YES_NO) {
                    new DeleteGroup(groupId).start();
                }
            } else if (menu.text.equalsIgnoreCase(MNU_JOIN)) {
                joinDeletePopup.hideThis();
                GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(groupId);
                if (groupInfo != null
                        && !check_already_in_group(MyFnFSettings.LOGIN_USER_ID)) {
                    GroupMemberDTO member = new GroupMemberDTO();
                    member.setGroupId(groupId);
                    member.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
                    member.setUserTableId(MyFnFSettings.userProfile.getUserTableId());
                    member.setFullName(MyFnFSettings.userProfile.getFullName());

                    new AddMemberInGroup(groupInfo.getGroupName(), member, null).start();
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            menu.setMouseEntered();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            menu.setMouseExited();
        }
    };

    WindowListener listener = new WindowAdapter() {
        @Override
        public void windowDeactivated(WindowEvent e) {
            holder.setBackground(Color.WHITE);
            actionPopUpButton.setVisible(false);
        }
    };

    MouseListener mouseListener = new MouseListener() {
        Font original;

        @Override
        public void mouseEntered(MouseEvent e) {
            if ((e.getSource() == holder || e.getSource() == actionPopUpButton) && (!leaveDeletePopup.isVisible() && !joinDeletePopup.isVisible() && !onlyLeavePopup.isVisible())) {
                holder.setBackground(RingColorCode.FRIEND_LIST_RIGHT_BUTTON_PRESSED_COLOR);
            }
            actionPopUpButton.setVisible(true);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if ((e.getSource() == holder || e.getSource() == actionPopUpButton) && (!leaveDeletePopup.isVisible() && !joinDeletePopup.isVisible() && !onlyLeavePopup.isVisible())) {
                holder.setBackground(Color.WHITE);
                actionPopUpButton.setVisible(false);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
    };

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
//            if (e.getSource() == mnuLeaveGroup) {
//                HelperMethods.showConfirmationDialogMessage(NotificationMessages.LEAVE_NOTIFICAITON);
//                if (JOptionPanelBasics.YES_NO) {
//                    new RemoveMemberFromGroup(groupId, MyFnFSettings.LOGIN_USER_ID, null).start();
//                }
//            } else if (e.getSource() == mnuDeleteGroup) {
//                HelperMethods.showConfirmationDialogMessage(NotificationMessages.DELETE_NOTIFICAITON);
//                if (JOptionPanelBasics.YES_NO) {
//                    new DeleteGroup(groupId).start();
//                }
//            } else if (e.getSource() == mnuJoinGroup) {
//                GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(groupId);
//                if (groupInfo != null
//                        && !check_already_in_group(MyFnFSettings.LOGIN_USER_ID)) {
//                    GroupMemberDTO member = new GroupMemberDTO();
//                    member.setGroupId(groupId);
//                    member.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
//                    member.setUserTableId(MyFnFSettings.userProfile.getUserTableId());
//                    member.setFullName(MyFnFSettings.userProfile.getFullName());
//
//                    new AddMemberInGroup(groupInfo.getGroupName(), member, null).start();
//                }
//            }
        }
    };
}
