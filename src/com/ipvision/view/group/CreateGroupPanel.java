/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.group;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.constants.StatusConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.CreateGroupRequest;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.model.GroupMemberDTO;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.utility.CenterLayout;
import com.ipvision.view.utility.RoundedCornerButton;
import com.ipvision.view.utility.WrapLayout;
import com.ipvision.view.utility.ImageHelpers;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author Wasif Islam
 */
public class CreateGroupPanel extends JPanel implements AncestorListener {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CreateGroupPanel.class);
    //  private JPanel container;
    private JPanel changeablePanel;
    private JTextField group_Name_text_field;
    private JPanel rightGroupmember;
    private JPanel selectedFriends;
    private JLabel pleaseWait;
    private JLabel addedMme;
    private JButton buttonCreate;
    private JButton buttonReset;
    private JButton buttonBack;
    private static String defaultGroupName = "Write Group Name..";
    private static CreateGroupPanel instance;

    public static CreateGroupPanel getInstance() {
        if (instance == null) {
            instance = new CreateGroupPanel();
        }
        return instance;
    }

    public static void setInstance(CreateGroupPanel createGroupPanel) {
        instance = createGroupPanel;
    }

    public CreateGroupPanel() {
        instance = this;
        addAncestorListener(this);
        setBackground(RingColorCode.DEFAULT_BACKGROUND_COLOR);
        setLayout(new CenterLayout(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH + 20));
        initContainers();
    }

    private void initContainers() {
        try {

            JPanel wrapperPanel = new JPanel();
//            wrapperPanel.setBackground(Color.WHITE);
            wrapperPanel.setLayout(new BorderLayout(DefaultSettings.HGAP, DefaultSettings.VGAP + 2));
            wrapperPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
            wrapperPanel.setOpaque(false);

            JPanel createResetBackPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            createResetBackPanel.setPreferredSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH, 50));
            createResetBackPanel.setBackground(Color.WHITE);
            createResetBackPanel.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));

            buttonCreate = new RoundedCornerButton("Create", "Create");
            buttonReset = new RoundedCornerButton("Reset", "Reset");
            buttonBack = new RoundedCornerButton("Back", "Back");

            createResetBackPanel.add(Box.createRigidArea(new Dimension(175, 40)));
            createResetBackPanel.add(buttonCreate);
            createResetBackPanel.add(Box.createRigidArea(new Dimension(40, 40)));
            createResetBackPanel.add(buttonReset);
            createResetBackPanel.add(Box.createRigidArea(new Dimension(40, 40)));
            createResetBackPanel.add(buttonBack);

            wrapperPanel.add(createResetBackPanel, BorderLayout.NORTH);

//            JPanel headerPanel = new JPanel();
//            headerPanel.setLayout(new BorderLayout(5, 5));
//            //   headerPanel.setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);
//            headerPanel.setOpaque(false);
//            JLabel headLabel = DesignClasses.makeLableBold1("Create New Group", 18);
//            headLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
//            headerPanel.add(headLabel, BorderLayout.WEST);
//
//            JPanel backPanel = new JPanel();
//            backPanel.setOpaque(false);
//            backPanel.setBorder(new MatteBorder(0, 1, 0, 0, RingColorCode.DEFAULT_BORDER_COLOR));
//            backPanel.add(backButton);
//            backButton.addMouseListener(new MouseListener() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    GuiRingID.getInstance().getMainRight().showGroupViewPanel();
//                }
//
//                @Override
//                public void mousePressed(MouseEvent e) {
//                }
//
//                @Override
//                public void mouseReleased(MouseEvent e) {
//                }
//
//                @Override
//                public void mouseEntered(MouseEvent e) {
//                    backButton.setIcon(DesignClasses.return_image(GetImages.BACK_MINI_H));
//                }
//
//                @Override
//                public void mouseExited(MouseEvent e) {
//                    backButton.setIcon(DesignClasses.return_image(GetImages.BACK_MINI));
//                }
//            });
//
//            headerPanel.add(backPanel, BorderLayout.EAST);
//
//            add(headerPanel, BorderLayout.NORTH);
//
            changeablePanel = new JPanel();
            changeablePanel.setBorder((new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR)));
//            changeablePanel.setOpaque(false);
            changeablePanel.setBackground(Color.WHITE);
            changeablePanel.setLayout(new BorderLayout(DefaultSettings.HGAP + 2, DefaultSettings.VGAP + 2));

            JPanel createGroupName_selectedMemberPanel = new JPanel(new BorderLayout());
            createGroupName_selectedMemberPanel.setOpaque(false);
            createGroupName_selectedMemberPanel.setBorder(new EmptyBorder(20, 55, 2, 55));
            // topInviteContainer.setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);
            JLabel createGroupTitle = DesignClasses.makeJLabelFullName("Group Name:");
            createGroupTitle.setBorder(new EmptyBorder(1, 0, 1, 5));

            group_Name_text_field = DesignClasses.makeTextFieldLimitedTextSize(defaultGroupName, 230, 25, 100);
            group_Name_text_field.setForeground(DefaultSettings.disable_font_color);// at first font color make grey 
            group_Name_text_field.grabFocus();
            group_Name_text_field.setFocusable(true);
            group_Name_text_field.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    group_Name_text_field.setText("");
                    group_Name_text_field.setForeground(null);// when it focused gained -->its set null that means default font color --->black
                }

                @Override
                public void focusLost(FocusEvent e) {

                }
            });

            JPanel groupNamepanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            groupNamepanel.setOpaque(false);
            groupNamepanel.add(createGroupTitle);
            groupNamepanel.add(group_Name_text_field);
            groupNamepanel.setBorder(new EmptyBorder(15, 0, 0, 0));
            createGroupName_selectedMemberPanel.add(groupNamepanel, BorderLayout.WEST);

            JPanel createNewGroup = DesignClasses.createlabelOvalPanel("          Create New Group          ");
            createGroupName_selectedMemberPanel.add(createNewGroup, BorderLayout.NORTH);
            changeablePanel.add(createGroupName_selectedMemberPanel, BorderLayout.NORTH);

            pleaseWait = DesignClasses.makeJLabelFullName("");
            pleaseWait.setBorder(new EmptyBorder(0, 60, 0, 5));

            JPanel centerOfchangeablePanel = new JPanel();
            centerOfchangeablePanel.setOpaque(false);
            centerOfchangeablePanel.setLayout(new BorderLayout());
            changeablePanel.add(centerOfchangeablePanel, BorderLayout.CENTER);

            JPanel addMemberPanel = new JPanel();
            addMemberPanel.setLayout(new BorderLayout(DefaultSettings.HGAP, DefaultSettings.VGAP));
            addMemberPanel.setOpaque(false);

            JPanel notifyStringPanel = new JPanel(new BorderLayout());
            //  addMemberString.setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);
            notifyStringPanel.setOpaque(false);

            addedMme = DesignClasses.makeJLabelFullName("");
            addedMme.setBorder(new EmptyBorder(15, 15, 2, 5));
            addedMme.setOpaque(false);
            createGroupName_selectedMemberPanel.add(addedMme, BorderLayout.EAST);
//
//            JPanel buttonsPanel = new JPanel();
//            buttonsPanel.setOpaque(false);
//            buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
//

            buttonCreate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttonCreate.grabFocus();
                    boolean success = true;
                    if (group_Name_text_field.getText().trim().length() > 0 && !(group_Name_text_field.getText().equals(defaultGroupName))) {
                        for (Long key : MyfnfHashMaps.getInstance().getGroup_hash_map().keySet()) {
                            GroupInfoDTO tag = MyfnfHashMaps.getInstance().getGroup_hash_map().get(key);
                            if (tag.getGroupName().equalsIgnoreCase(group_Name_text_field.getText().trim())) {
                                pleaseWait.setForeground(Color.RED);
                                pleaseWait.setText("Group name already exists!!!!");
                                success = false;
                                break;
                            }
                        }

                        if (success && MyfnfHashMaps.getInstance().getTempGroupMemberContainerforUpdate().size() > 0) {
                            new CreateGroupRequest(pleaseWait, buttonCreate, buttonReset, group_Name_text_field.getText()).start();
                        } else {
                            if (success) {
                                pleaseWait.setForeground(Color.RED);
                                pleaseWait.setText("No member added!!!!");
                            }
                        }
                    } else {
                        pleaseWait.setForeground(Color.RED);
                        pleaseWait.setText("Please add group name!!!!");
                    }
                }
            });

            buttonReset.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        group_Name_text_field.setText(defaultGroupName);
                        group_Name_text_field.setForeground(DefaultSettings.disable_font_color);
                        pleaseWait.setText("");
                        buttonReset.grabFocus();
                        clear_selected_members();
                        resetNumberofAddedMembers();
                        addSelectedGroupFreindList();
                    } catch (Exception eee) {
                    }
                }
            });
            buttonBack.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        GuiRingID.getInstance().getMainRight().showGroupViewPanel();
                    } catch (Exception eee) {
                    }
                }
            });
//
//            buttonsPanel.add(buttonCreate);
//            buttonsPanel.add(buttonReset);
//
            if (InternetUnavailablityCheck.isInternetAvailable) {
                buttonCreate.setEnabled(true);
            } else {
                buttonCreate.setEnabled(false);
            }

//            addMemberString.setBorder(new EmptyBorder(0, 60, 0, 0));
//            addMemberString.add(buttonsPanel, BorderLayout.EAST);
            notifyStringPanel.add(pleaseWait, BorderLayout.WEST);
            addMemberPanel.add(notifyStringPanel, BorderLayout.PAGE_START);

            rightGroupmember = returnPanel();
            rightGroupmember.setOpaque(false);
            // rightGroupmember.setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);
            addMemberPanel.add(rightGroupmember, BorderLayout.CENTER);

            centerOfchangeablePanel.add(addMemberPanel, BorderLayout.CENTER);
            wrapperPanel.add(changeablePanel, BorderLayout.CENTER);

            addRightPanelDetails();
            resetNumberofAddedMembers();

            add(wrapperPanel);
        } catch (Exception e) {
        }
    }

    private void addRightPanelDetails() {
        DropAreaPanel selectedFriendCon = new DropAreaPanel(this);
        selectedFriendCon.setLayout(new BorderLayout());
        selectedFriendCon.setBorder(new EmptyBorder(2, 60, 2, 60));
        selectedFriendCon.setOpaque(false);

        selectedFriends = new JPanel();;//new FlowLayout(FlowLayout.LEFT, 5,0));
        selectedFriends.setLayout(new WrapLayout(WrapLayout.LEFT, 25, 10));
        selectedFriends.setOpaque(false);

        JScrollPane freindlistScorllPanel = DesignClasses.getDefaultScrollPane(selectedFriends);//new JScrollPane(selectedFriendCon);
        freindlistScorllPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        freindlistScorllPanel.setBorder(BorderFactory.createDashedBorder(DefaultSettings.APP_DEFAULT_MENUBAR_BG_HOVER_COLOR, 1.5f, 4.0f, 2.0f, false));

        selectedFriendCon.add(freindlistScorllPanel);
        rightGroupmember.add(selectedFriendCon, BorderLayout.CENTER);
        addSelectedGroupFreindList();

    }

    private void addDroppedMember(String userIdentity) {
        UserBasicInfo basicinfo = FriendList.getInstance().getFriend_hash_map().get(userIdentity);
        if (basicinfo != null
                && Objects.equals(basicinfo.getFriendShipStatus(), StatusConstants.FRIENDSHIP_STATUS_ACCEPTED)
                && MyfnfHashMaps.getInstance().getTempGroupMemberContainerforUpdate().get(userIdentity) == null) {
            GroupMemberDTO memberDTO = new GroupMemberDTO();
            memberDTO.setUserIdentity(userIdentity);
            memberDTO.setUserTableId(basicinfo.getUserTableId());
            memberDTO.setFullName(HelperMethods.getUserFullName(basicinfo));
            MyfnfHashMaps.getInstance().getTempGroupMemberContainerforUpdate().put(userIdentity, memberDTO);
            addSelectedGroupFreindList();
            pleaseWait.setText("");
        }
//        else {
//            pleaseWait.setForeground(Color.RED);
//            pleaseWait.setText("Friend already added!!");
//        }
    }

    public synchronized void addSelectedGroupFreindList() {
        boolean isAdded = false;

        if (selectedFriends != null) {  //&& selectedFriends.isDisplayable()
            try {
                if (selectedFriends != null) {
                    selectedFriends.removeAll();
                    if (MyfnfHashMaps.getInstance().getTempGroupMemberContainerforUpdate() != null
                            && !MyfnfHashMaps.getInstance().getTempGroupMemberContainerforUpdate().isEmpty()) {
                        for (Entry<String, GroupMemberDTO> entry : MyfnfHashMaps.getInstance().getTempGroupMemberContainerforUpdate().entrySet()) {
                            isAdded = true;
//                            selectedFriends.add(Box.createRigidArea(new Dimension(30, 70)));
                            selectedFriends.add(create_selected_SingleFriends(entry.getValue()));

                            resetNumberofAddedMembers();
                        }
                    }
                }
                selectedFriends.revalidate();
                selectedFriends.repaint();
            } catch (Exception eee) {
            }
        }

        if (!isAdded) {
            JLabel createDragLabel = DesignClasses.makeJLabel_normal("                                 Drag here from contact list that you want to add", 12, DefaultSettings.DEFAULT_TITLE_COLOR_IN_FRIEND);
            selectedFriends.add(createDragLabel);
            addedMme.setVisible(false);
        } else {
            addedMme.setVisible(true);
        }
    }

    public UserBasicInfo getUserInfo(GroupMemberDTO js) {
        UserBasicInfo basicinfo = new UserBasicInfo();
        if (FriendList.getInstance().getFriend_hash_map().get(js.getUserIdentity()) != null) {
            basicinfo = FriendList.getInstance().getFriend_hash_map().get(js.getUserIdentity());
        } else {
            if (js.getUserIdentity() != null) {
                basicinfo.setUserIdentity(js.getUserIdentity());
                basicinfo.setFullName(js.getUserIdentity());
            }
            if (js.getFullName() != null) {
                basicinfo.setFullName(js.getFullName());
            } else {
                basicinfo.setFullName(js.getUserIdentity());
            }
        }

        return basicinfo;
    }

    private JPanel create_selected_SingleFriends(final GroupMemberDTO entity) {

        final JPanel holder = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        holder.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
        holder.setBackground(Color.WHITE);
        holder.add(Box.createRigidArea(new Dimension(2, 1)));

        final JPanel rowPanel = new JPanel(new BorderLayout(5, 5));
        rowPanel.setPreferredSize(new Dimension(210, 70));
        rowPanel.setBackground(Color.WHITE);
        holder.add(rowPanel);
        holder.add(Box.createRigidArea(new Dimension(2, 1)));
        try {
            UserBasicInfo friend_info = FriendList.getInstance().getFriend_hash_map().get(entity.getUserIdentity());
            JLabel frienImageLabel = new JLabel();
            if (friend_info != null) {
                ImageHelpers.addProfileImageThumbInQueue(frienImageLabel, friend_info.getProfileImage(), 39, HelperMethods.getShortName(entity.getFullName()));
                // ImageHelpers.addProfileImageThumb(frienImageLabel, friend_info.getProfileImage(), 39);
            }
//            else {
//                BufferedImage img;
//                img = ImageHelpers.createDefaultBufferImage(39, HelperMethods.getShortName(entity.getFullName()));
//                frienImageLabel.setIcon(new ImageIcon(img));
//                img.flush();
//                img = null;
//            }
            JPanel friendImagePanel = new JPanel(new GridLayout());
            friendImagePanel.setBorder(new EmptyBorder(3, 3, 3, 0));
            friendImagePanel.setOpaque(false);
            friendImagePanel.add(frienImageLabel);
            rowPanel.add(friendImagePanel, BorderLayout.WEST);

            JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
            wrapperPanel.setBorder(new EmptyBorder(3, 0, 3, 0));
            wrapperPanel.setOpaque(false);

            JPanel infoContainer = new JPanel();
            infoContainer.setLayout(new BoxLayout(infoContainer, BoxLayout.Y_AXIS));
            infoContainer.setOpaque(false);

            JLabel fullNameLabel = DesignClasses.makeAncorLabelDefaultColor(entity.getFullName(), 0, 12);
            fullNameLabel.setPreferredSize(new Dimension(125, 20));

            JPanel fullNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 17));
            fullNamePanel.setOpaque(false);
            fullNamePanel.add(fullNameLabel);
            infoContainer.add(fullNamePanel);

            final JButton removeButton = DesignClasses.createImageButton(GetImages.BUTTON_CLOSE_BOLD, GetImages.BUTTON_CLOSE_BOLD_H, "Remove");

            JPanel buttonPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridy = 0;
            //c.insets(1,1,1,1);
            c.insets = new Insets(-40, 0, 0, 0);// "remove button" position can be chahnged using this
            //   buttonPanel.setPreferredSize(new Dimension(20, 0));
            buttonPanel.setOpaque(false);
            //buttonPanel.add(Box.createRigidArea(new Dimension(1, 1)), c);
            // c.gridy++;
            buttonPanel.add(removeButton, c);

            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        MyfnfHashMaps.getInstance().getTempGroupMemberContainerforUpdate().remove(entity.getUserIdentity());
                        selectedFriends.remove(holder);
                        selectedFriends.revalidate();
                        selectedFriends.repaint();
                        resetNumberofAddedMembers();
                        if (MyfnfHashMaps.getInstance().getTempGroupMemberContainerforUpdate().isEmpty()) {
                            JLabel createDragLabel = DesignClasses.makeJLabel_normal("                                 Drag here from contact list that you want to add", 12, DefaultSettings.DEFAULT_TITLE_COLOR_IN_FRIEND);
                            selectedFriends.add(createDragLabel);
                            addedMme.setVisible(false);
                        } else {
                            addedMme.setVisible(true);
                        }

                    } catch (Exception ex) {
                        // ex.printStackTrace();
                        log.error("Error in here ==>" + ex.getMessage());
                    }
                }
            });
            rowPanel.add(buttonPanel, BorderLayout.EAST);

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
                        GuiRingID.getInstance().showUnknownProfile(getUserInfo(entity));
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

            wrapperPanel.add(infoContainer);
            rowPanel.add(wrapperPanel, BorderLayout.CENTER);
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error in here ==>" + e.getMessage());
        }

        return holder;
    }

    public void resetNumberofAddedMembers() {
        addedMme.setText("Selected Members : " + MyfnfHashMaps.getInstance().getTempGroupMemberContainerforUpdate().size());
    }

    private JPanel returnPanel() {
        JPanel nullPan = new JPanel();
        nullPan.setLayout(new BorderLayout());
        nullPan.setOpaque(false);
        return nullPan;
    }

    public void clear_selected_members() {
        MyfnfHashMaps.getInstance().getTempGroupMemberContainerforUpdate().clear();
    }

    @Override
    public void ancestorAdded(AncestorEvent event) {
        if (GuiRingID.getInstance() != null) {
            GuiRingID.getInstance().getMainButtons().setMainGroupButton(true);
        }
    }

    @Override
    public void ancestorRemoved(AncestorEvent event) {
        if (GuiRingID.getInstance() != null) {
            GuiRingID.getInstance().getMainButtons().setMainGroupButton(false);
        }
    }

    @Override
    public void ancestorMoved(AncestorEvent event) {
    }

    public class DropAreaPanel extends JPanel implements DropTargetListener {

        private CreateGroupPanel createNewGroupPanel;

        public DropAreaPanel(CreateGroupPanel createNewGroupPanel) {
            this.createNewGroupPanel = createNewGroupPanel;
            new DropTarget(this, this);
        }

        @Override
        public void dragEnter(DropTargetDragEvent dtde) {
        }

        @Override
        public void dragOver(DropTargetDragEvent dtde) {
        }

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {
        }

        @Override
        public void dragExit(DropTargetEvent dte) {
        }

        @Override
        public void drop(DropTargetDropEvent dtde) {
            try {
                Transferable transferable = dtde.getTransferable();
                if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    String dragContents = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                    dtde.getDropTargetContext().dropComplete(true);
                    createNewGroupPanel.addDroppedMember(dragContents);
                } else {
                    dtde.rejectDrop();
                }
            } catch (IOException e) {
                dtde.rejectDrop();
            } catch (UnsupportedFlavorException e) {
                dtde.rejectDrop();
            }
        }
    }
}
