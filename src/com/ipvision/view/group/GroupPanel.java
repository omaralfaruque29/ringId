/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.group;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.service.ChatService;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import static com.ipvision.view.utility.DesignClasses.getDefaultFont;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.constants.RingColorCode;
import com.ipvision.constants.SettingsConstants;
import com.ipvision.model.constants.StatusConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;
import com.ipvision.model.dao.InsertIntoGroupMessageTable;
import com.ipvision.service.GetGroupHistory;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.JDialogWebcam;
import com.ipvision.service.AddMemberInGroup;
import com.ipvision.service.ChangeGroupName;
import com.ipvision.service.DeleteGroup;
import com.ipvision.service.RemoveMemberFromGroup;
import com.ipvision.view.chat.AudioRecorderWindow;
import com.ipvision.view.chat.ChatDateGroupPanel;
import com.ipvision.model.RecentDTO;
import com.ipvision.model.dao.RecentContactDAO;
import com.ipvision.model.MessageDTO;
import com.ipvision.view.utility.chat.ChatHashMap;
import com.ipvision.view.chat.SingleChatPanel;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.model.GroupMemberDTO;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.view.image.JDialogStickerPanel;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.view.chat.VideoRecorderWindow;
import com.ipvision.model.ActivityDTO;
import com.ipvision.service.utility.chat.PacketResenderHandler;
import com.ipvision.service.chat.ChatFileShareProcessor;
import com.ipvision.service.chat.ChatFileTransferProcessor;
import com.ipvision.service.chat.ChatUserTyping;
import com.ipvision.view.utility.chat.ChatHelpers;
import com.ipvision.model.dao.RecentChatCallActivityDAO;
import com.ipvision.view.AddEmoticonsJDialog;
import com.ipvision.view.myprofile.SingleActivityPanel;
import com.ipvision.view.utility.ContextMenuMouseListener;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.settingsmenu.RingIDSettingsDialog;
import com.ipvision.view.utility.JCustomMenuPopup;
import com.ipvision.view.utility.JOptionPanelBasics;
import com.ipvision.view.utility.RoundedCornerButton;
import com.ipvision.view.utility.WrapLayout;
import com.ipvision.view.utility.ImagePane;

/**
 *
 * @author Shahadat Hossain
 */
public class GroupPanel extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GroupPanel.class);
    public JLabel groupNameLabel;
    public JLabel groupMembers;
    public JDialogStickerPanel jDialogStickerPanel;
    private JPanel chatWriterContainer;
    private JScrollPane scrollContent;
    public Long groupId;
    private static Long selectedGroupId;
    private String typing = " is typing...";
    public JTextArea chatWriter;
    private JPanel chatContainerPanel;
    public ImagePane masterPanel;
    public JPanel messagePanel;

    public String lastChatSenderIdentity;
    private JTextArea userTypingArea;
    private JLabel userTypingIcon;
    public ChatUserTyping chatUserTyping;
    public LoadHistoryOnWindowResize onWindowResize;

    public final JButton btnSendChat = DesignClasses.createImageButton(GetImages.CHAT_SEND, GetImages.CHAT_SEND_H, "Send Message");
    private final JButton btnEmoticon = DesignClasses.createImageButton(GetImages.BUTTON_CHAT_EMOTICON, GetImages.BUTTON_CHAT_EMOTICON_H, "Insert emoticon");
    private final JButton btnChatMoreOption = DesignClasses.createImageButton(GetImages.BUTTON_CHAT_MORE_OPTION, GetImages.BUTTON_CHAT_MORE_OPTION_H, "More Options");
    private final JButton btnRecent = DesignClasses.createImageButton(GetImages.BUTTON_CHAT_RECENT, GetImages.BUTTON_CHAT_RECENT_H, "Recent History");
    private FileDropHandler fileDropHandler = new FileDropHandler();
    private int chatWritercolumns = 44;
    public String groupName = "No Name";
    private GroupPanel instance;
    public JPanel membersPanel;
    private JPanel memebersWrapperPanel;
    private JPanel headerPanel;
    private DropTarget dropTarget;
    public JButton actionPopUpButton;

    public JTextField groupNameTextField;
    private JButton okButton;
    private JButton closeButton;
    public JPanel buttonsPanel;
    private JCustomMenuPopup recentPopup = null;
    private JCustomMenuPopup optionsPopup = null;
    private JCustomMenuPopup.CustomMenu mnuDeleteChat = null;
    private final String MNU_TODAY = "Today";
    private final String MNU_YESTERDAY = "Yesterday";
    private final String MNU_7_DAYS = "7 Days";
    private final String MNU_30_DAYS = "30 Days";
    private final String MNU_3_MONTHS = "3 Months";
    private final String MNU_SEND_PHOTO = "Send Photo";
    private final String MNU_SEND_FILE = "Send File";
    private final String MNU_TAKE_PHOTO = "Take a Photo";
    private final String MNU_VIDEO_MESG = "Send Video Message";
    private final String MNU_MESSAGE_DELETE = "Message Delete";
    private final String MNU_CANCEL_MESSAGE_DELETE = "Cancel Message Delete";
    private final String MNU_VOICE_MESG = "Send Voice Message";
    private final String MNU_CHANGE_BACKGROUND = "Change Background";
    private final String MNU_SELECT_ALL = "Select All";
    private final String MNU_DESELECT_ALL = "Deselect All";

    private JPanel deleteOptionContainer;
    private JButton btnSelectAll = new RoundedCornerButton(MNU_SELECT_ALL, MNU_SELECT_ALL, false);
    private JButton btnDelete = new RoundedCornerButton("Delete", "Delete Selected Message", false);
    private JButton btnCancel = new RoundedCornerButton("Cancel", "Cancel", false);

//  for CustomMenuPopup ----Rabiul 3-12-2015 
    JCustomMenuPopup editLeaveDeletePopup = null;
    JCustomMenuPopup editJoinDeletePopup = null;
    JCustomMenuPopup onlyLeavePopup = null;
    private final String MNU_LEAVE = "Leave from Group";
    private final String MNU_DELETE = "Delete this Group";
    private final String MNU_EDIT = "Edit Group Name";
    private final String MNU_JOIN = "Join this Group";
    private static int decider = 0;
//
    final private List<Data> LOADED_HISTORY_TIME = new ArrayList<Data>();
    public boolean isTopLoading = false;
    public boolean isBottomLoading = false;
    public int currentV = 0;
    private int unit_size = 45;
    private final int MAX_CHAT_TEXT_LENGTH = 8000;
    private JLabel groupImage;
    private JLabel lblSelected = new JLabel("", SwingConstants.RIGHT);
    private int UP_SCROLL_LIMIT = 150;
    //public JLabel lblMessageDate;

    public static Long getSelectedGroupId() {
        return selectedGroupId;
    }

    public static void setSelectedGroupId(Long aSelectedTagId) {
        selectedGroupId = aSelectedTagId;
    }

    public GroupPanel(Long groupId) {
        this.instance = this;
        this.groupId = groupId;
        setSelectedGroupId(this.groupId);
        setLayout(new BorderLayout(DefaultSettings.HGAP, 0));
        initContainers();
        setBackground(Color.WHITE);

        DesignClasses.setGroupProfileImage(groupImage, groupId);

    }

    private void initContainers() {
        try {
            headerPanel = new JPanel();
            headerPanel.setLayout(new BorderLayout(0, 0));
            headerPanel.setOpaque(false);

            JPanel topPanel = new JPanel(new BorderLayout(0, 5));
            topPanel.setPreferredSize(new Dimension(0, 51));
            topPanel.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
            topPanel.setOpaque(false);

            JPanel topPanelLeft = new JPanel(new BorderLayout(11, 0));
            topPanelLeft.setOpaque(false);
            topPanelLeft.setPreferredSize(new Dimension(300, 0));

            JPanel groupImagePanel = new JPanel(new GridBagLayout());
            groupImagePanel.setBorder(new EmptyBorder(0, 25, 0, 0));
            groupImagePanel.setOpaque(false);
            groupImage = DesignClasses.create_imageJlabel(GetImages.GROUP_SMALL);
            groupImagePanel.add(groupImage);
            topPanelLeft.add(groupImagePanel, BorderLayout.WEST);

            JPanel groupNameAndMembersWrapper = new JPanel(new GridBagLayout());
            groupNameAndMembersWrapper.setOpaque(false);

            JPanel groupNameAndMembers = new JPanel();
            groupNameAndMembers.setLayout(new BoxLayout(groupNameAndMembers, BoxLayout.Y_AXIS));
            groupNameAndMembers.setOpaque(false);
            groupNameAndMembersWrapper.add(groupNameAndMembers);

            JPanel groupNameTextFieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            groupNameTextFieldPanel.setOpaque(false);
            groupNameTextField = DesignClasses.makeTextFieldLimitedTextSize("", 220, 22, 100);
            groupNameTextField.setVisible(false);
            groupNameTextFieldPanel.add(groupNameTextField);
            groupNameAndMembers.add(groupNameTextFieldPanel);

            GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(this.groupId);
            if (groupInfo != null) {
                groupName = groupInfo.getGroupName();
            }

            JPanel groupNameLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            groupNameLabelPanel.setOpaque(false);
            groupNameLabel = DesignClasses.makeLableBold1(groupName, 18);
            groupNameLabel.setPreferredSize(new Dimension(220, 22));
            groupNameLabel.setVisible(true);
            groupNameLabelPanel.add(groupNameLabel);
            groupNameAndMembers.add(groupNameLabelPanel);

            JPanel groupMembersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            groupMembersPanel.setOpaque(false);
            groupMembers = DesignClasses.makeLableBold1("", 10);
            groupMembers.setCursor(new Cursor(Cursor.HAND_CURSOR));
            groupMembers.setPreferredSize(new Dimension(220, 12));

            groupMembersPanel.add(groupMembers);
            groupNameAndMembers.add(groupMembersPanel);
            groupMembers.addMouseListener(mouse_listner);

            topPanelLeft.add(groupNameAndMembersWrapper, BorderLayout.CENTER);
            topPanel.add(topPanelLeft, BorderLayout.WEST);

            JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
            topRightPanel.setOpaque(false);
            topRightPanel.setPreferredSize(new Dimension(50, 0));
//             for popupmenu
            editLeaveDeletePopup = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_EDIT_LEAVE_DELETE);
            editLeaveDeletePopup.addMenu(MNU_EDIT, GetImages.EDIT_PHOTO);
            editLeaveDeletePopup.addMenu(MNU_LEAVE, GetImages.LEAVE_PHOTO);
//            editLeaveDeleteJoinPopup.addMenu(MNU_JOIN, GetImages.DELETE_PHOTO);
            editLeaveDeletePopup.addMenu(MNU_DELETE, GetImages.DELETE_PHOTO);

            // editJoinDelete
            editJoinDeletePopup = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_EDIT_LEAVE_DELETE);
            editJoinDeletePopup.addMenu(MNU_EDIT, GetImages.EDIT_PHOTO);
            editJoinDeletePopup.addMenu(MNU_JOIN, GetImages.JOIN_PHOTO);
            editJoinDeletePopup.addMenu(MNU_DELETE, GetImages.DELETE_PHOTO);
            //
            // OnlyLeave
            onlyLeavePopup = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_ONLY_LEAVE);
            onlyLeavePopup.addMenu(MNU_LEAVE, GetImages.LEAVE_PHOTO);

            actionPopUpButton = DesignClasses.createImageButton(GetImages.SETTING_MINI, GetImages.SETTING_MINI_H, "Options");
            topRightPanel.add(actionPopUpButton);
            actionPopUpButton.setVisible(true);
//            actionPopUpButton.addActionListener(actionListener);
            actionPopUpButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (decider == 1) {
                        editLeaveDeletePopup.setVisible(actionPopUpButton, 155, -5);//wdth ,height//(118, -8 suitable for 2 word 2 line)
                    } else if (decider == 2) {
                        editJoinDeletePopup.setVisible(actionPopUpButton, 155, -5);
                    } else if (decider == 3) {
                        onlyLeavePopup.setVisible(actionPopUpButton, 155, -5);
                    }
                }
            });
//
            buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            buttonsPanel.setOpaque(false);
            buttonsPanel.setVisible(false);
            okButton = DesignClasses.createImageButton(GetImages.OK_MINI, GetImages.OK_MINI_H, "Save");
            buttonsPanel.add(okButton);
            okButton.addActionListener(actionListener);

            closeButton = DesignClasses.createImageButton(GetImages.BUTTON_CLOSE_MINI, GetImages.BUTTON_CLOSE_MINI_H, "Cancel");
            buttonsPanel.add(closeButton);
            closeButton.addActionListener(actionListener);

            topRightPanel.add(buttonsPanel);

            topPanel.add(topRightPanel, BorderLayout.EAST);

            headerPanel.add(topPanel, BorderLayout.NORTH);

            memebersWrapperPanel = new JPanel(new BorderLayout());
            memebersWrapperPanel.setOpaque(false);
            memebersWrapperPanel.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
            memebersWrapperPanel.setPreferredSize(new Dimension(0, 87));
            memebersWrapperPanel.setVisible(false);
            headerPanel.add(memebersWrapperPanel, BorderLayout.CENTER);

            membersPanel = new JPanel();
            membersPanel.setOpaque(false);
            // membersPanel.setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);
            membersPanel.setLayout(new WrapLayout(WrapLayout.LEFT, 5, 5));
            membersPanel.setDropTarget(new FileDropTarget(this, fileDropHandler));

            JScrollPane membersContainerPanelScroll = DesignClasses.getDefaultScrollPane(membersPanel);// DesignClasses.getDefaultScorllPane(chatContainerPanel);
            membersContainerPanelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            memebersWrapperPanel.add(membersContainerPanelScroll, BorderLayout.CENTER);

            headerPanel.revalidate();
            add(headerPanel, BorderLayout.NORTH);

            buildMembersPanel();
            addGroupChatPanel();
            buildPopUpMenu();

        } catch (Exception e) {
        }
    }

    public void setGroupMemberCount() {
        groupMembers.setText(HelperMethods.getGroupMembersCount(MyfnfHashMaps.getInstance().getGroup_hash_map().get(this.groupId)) + " Members");
        groupMembers.revalidate();
        groupMembers.repaint();
    }

    public void buildPopUpMenu() {
        GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(this.groupId);
        if (groupInfo != null && Objects.equals(groupInfo.getOwnerUserTableId(), MyFnFSettings.userProfile.getUserTableId())) {
            if (check_already_in_group(MyFnFSettings.LOGIN_USER_ID)) {
                decider = 1; // edit_leave_delete
            } else {
                decider = 2;//edit_JOIN_Delete
            }
        } else {
            decider = 3;//Only_Leave
        }
    }

    MouseListener mouse_listner = new MouseAdapter() {
        Font original;

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

        @Override
        public void mouseClicked(MouseEvent arg0) {
            if (arg0.getComponent() == groupMembers) {
                if (memebersWrapperPanel.isVisible()) {
                    memebersWrapperPanel.setVisible(false);
                } else {
                    memebersWrapperPanel.setVisible(true);
                }

                if (groupNameTextField.isVisible()) {
                    groupNameTextField.setVisible(false);
                }
                if (!groupNameLabel.isVisible()) {
                    groupNameLabel.setVisible(true);
                }
                if (!actionPopUpButton.isVisible()) {
                    actionPopUpButton.setVisible(true);
                }
                if (buttonsPanel.isVisible()) {
                    buttonsPanel.setVisible(false);
                }
                headerPanel.revalidate();
                headerPanel.repaint();
            }
        }
    };

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == okButton) {
                if (groupNameTextField.getText() != null && groupNameTextField.getText().trim().length() > 0) {
                    new ChangeGroupName(groupId, groupNameTextField.getText().trim()).start();
                } else {
                    HelperMethods.showWarningDialogMessage("Group name can not be empty");
                }
            } else if (e.getSource() == closeButton) {
                if (groupNameTextField.isVisible()) {
                    groupNameTextField.setVisible(false);
                }
                if (!groupNameLabel.isVisible()) {
                    groupNameLabel.setVisible(true);
                }
                if (!actionPopUpButton.isVisible()) {
                    actionPopUpButton.setVisible(true);
                }
                if (buttonsPanel.isVisible()) {
                    buttonsPanel.setVisible(false);
                }
            } else if (e.getSource() == btnEmoticon) {
                if (jDialogStickerPanel == null) {
                    jDialogStickerPanel = new JDialogStickerPanel(chatWriter, groupId);
                }
                jDialogStickerPanel.setVisible(btnEmoticon, 282);
            } else if (e.getSource() == btnSendChat) {
                if (chatWriter.getText().trim().length() > 0 && (chatWriter.getText().length() > MAX_CHAT_TEXT_LENGTH) == false) {
                    chatWriter.setText(chatWriter.getText());
                    buildChatPacket();
                    chatWriter.setText("");
                    chatWriter.grabFocus();
                }
            } else if (e.getSource() == btnChatMoreOption) {
                optionsPopup.setVisible(btnChatMoreOption, 2, optionsPopup.height);
            } else if (e.getSource() == btnRecent) {
                recentPopup.setVisible(btnRecent, 16, - 10);
            } else if (e.getSource() == btnSelectAll) {
                if (btnSelectAll.getText().equalsIgnoreCase(MNU_SELECT_ALL)) {
                    btnSelectAll.setText(MNU_DESELECT_ALL);
                    btnSelectAll.setToolTipText(MNU_DESELECT_ALL);
                    showHideChatSelection(true, true);
                } else {
                    btnSelectAll.setText(MNU_SELECT_ALL);
                    btnSelectAll.setToolTipText(MNU_SELECT_ALL);
                    showHideChatSelection(true, false);
                }
            } else if (e.getSource() == btnDelete) {
                deleteChatMessage();
            } else if (e.getSource() == btnCancel) {
                mnuDeleteChat.setText(MNU_MESSAGE_DELETE);
                showHideChatSelection(false, false);
            }
        }
    };

    FocusListener focusListener = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            ChatHelpers.startGroupChat(groupId, true);
        }
    };

    KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            if (chatWriter.getText().length() > MAX_CHAT_TEXT_LENGTH) {
                e.consume();
            } else {
                int length = chatWriter.getText().length();
                if (length > 4 && length % 5 == 0) {
                    ChatService.sendGroupChatTyping(groupId);
                }
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && (e.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
                    if (chatWriter.getText().length() > MAX_CHAT_TEXT_LENGTH) {
                        e.consume();
                    } else {
                        chatWriter.setText(chatWriter.getText() + "\n");
                    }
                } else if (chatWriter.getText().trim().length() > 0) {
                    e.consume();
                    btnSendChat.doClick();
                } else if (chatWriter.getText().length() > 0) {
                    e.consume();
                    chatWriter.setText("");
                } else {
                    e.consume();
                }
                chatWriter.grabFocus();
            }
        }
    };

    MouseListener menuListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            if (menu.text.equalsIgnoreCase(MNU_EDIT)) {
                editLeaveDeletePopup.hideThis();
                editJoinDeletePopup.hideThis();

                if (!groupNameTextField.isVisible()) {
                    groupNameTextField.setVisible(true);
                    groupNameTextField.setText(groupName);
                    groupNameTextField.grabFocus();
                    groupNameTextField.setFocusable(true);
                }
                if (groupNameLabel.isVisible()) {
                    groupNameLabel.setVisible(false);
                }
                if (actionPopUpButton.isVisible()) {
                    actionPopUpButton.setVisible(false);
                }
                if (!buttonsPanel.isVisible()) {
                    buttonsPanel.setVisible(true);
                }
            } else if (menu.text.equalsIgnoreCase(MNU_LEAVE)) {
                editLeaveDeletePopup.hideThis();
                onlyLeavePopup.hideThis();
                HelperMethods.showConfirmationDialogMessage(NotificationMessages.LEAVE_NOTIFICAITON);
                if (JOptionPanelBasics.YES_NO) {
                    new RemoveMemberFromGroup(groupId, MyFnFSettings.LOGIN_USER_ID, null).start();
                }
            } else if (menu.text.equalsIgnoreCase(MNU_JOIN)) {
                editJoinDeletePopup.hideThis();
                GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(groupId);
                if (groupInfo != null
                        && !check_already_in_group(MyFnFSettings.LOGIN_USER_ID)) {
                    GroupMemberDTO member = new GroupMemberDTO();
                    member.setGroupId(groupId);
                    member.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
                    member.setUserTableId(MyFnFSettings.userProfile.getUserTableId());
                    member.setFullName(MyFnFSettings.userProfile.getFullName());

                    new AddMemberInGroup(groupName, member, null).start();
                }
            } else if (menu.text.equalsIgnoreCase(MNU_DELETE)) {
                editLeaveDeletePopup.hideThis();
                HelperMethods.showConfirmationDialogMessage(NotificationMessages.DELETE_NOTIFICAITON);
                if (JOptionPanelBasics.YES_NO) {
                    new DeleteGroup(groupId).start();
                }
            }
            if (menu.text.equalsIgnoreCase(MNU_TODAY)) {
                lblSelected.setText(MNU_TODAY);
                loadHistoryOnRecent(RecentContactDAO._TODAY);
                recentPopup.hideThis();
            } else if (menu.text.equalsIgnoreCase(MNU_YESTERDAY)) {
                lblSelected.setText(MNU_YESTERDAY);
                loadHistoryOnRecent(RecentContactDAO._YESTERDAY);
                recentPopup.hideThis();
            } else if (menu.text.equalsIgnoreCase(MNU_7_DAYS)) {
                lblSelected.setText(MNU_7_DAYS);
                loadHistoryOnRecent(RecentContactDAO._7_DAYS);
                recentPopup.hideThis();
            } else if (menu.text.equalsIgnoreCase(MNU_30_DAYS)) {
                lblSelected.setText(MNU_30_DAYS);
                loadHistoryOnRecent(RecentContactDAO._30_DAYS);
                recentPopup.hideThis();
            } else if (menu.text.equalsIgnoreCase(MNU_3_MONTHS)) {
                lblSelected.setText(MNU_3_MONTHS);
                loadHistoryOnRecent(RecentContactDAO._90_DAYS);
                recentPopup.hideThis();
            } else if (menu.text.equalsIgnoreCase(MNU_SEND_FILE)) {
                JFrame dd = new JFrame();
                FileDialog fc = new FileDialog(dd, "Browse", FileDialog.LOAD);
                //fc.setFile("*.jpg;*.jpeg;*.png;*.gif");
                fc.setMultipleMode(true);
                fc.show();

                if (fc.getFiles() != null && fc.getFiles().length > 0) {
                    for (int i = 0; i < fc.getFiles().length; i++) {
                        new ChatFileTransferProcessor(instance, fc.getFiles()[i]).start();
                    }
                }
            } else if (menu.text.equalsIgnoreCase(MNU_SEND_PHOTO)) {
                JFrame dd = new JFrame();
                FileDialog fc = new FileDialog(dd, "Browse", FileDialog.LOAD);
                fc.setFile("*.jpg;*.jpeg;*.png;*.gif");
                fc.setMultipleMode(true);
                fc.show();

                if (fc.getFiles() != null && fc.getFiles().length > 0) {
                    for (int i = 0; i < fc.getFiles().length; i++) {
                        new ChatFileShareProcessor(fc.getFiles()[i], instance, ChatConstants.TYPE_IMAGE_FILE_DIRECTORY, 0).start();
                    }
                }
            } else if (menu.text.equalsIgnoreCase(MNU_TAKE_PHOTO)) {
                JDialogWebcam webcam = new JDialogWebcam();
                if (JDialogWebcam.fileToUpload != null && JDialogWebcam.fileToUpload.exists()) {
                    new ChatFileShareProcessor(JDialogWebcam.fileToUpload, instance, ChatConstants.TYPE_IMAGE_FILE_RING_CAMERA, 0).start();
                }
            } else if (menu.text.equalsIgnoreCase(MNU_VOICE_MESG)) {
                optionsPopup.hideThis();
                if (AudioRecorderWindow.instance != null) {
                    AudioRecorderWindow.instance.hideForceFully();
                }
                if (VideoRecorderWindow.instance != null) {
                    VideoRecorderWindow.instance.hideForceFully();
                }
                AudioRecorderWindow recorderWindow = new AudioRecorderWindow(groupId);
                recorderWindow.showRecodingVoiceChat();
            } else if (menu.text.equalsIgnoreCase(MNU_VIDEO_MESG)) {
                optionsPopup.hideThis();
                if (VideoRecorderWindow.instance != null) {
                    VideoRecorderWindow.instance.hideForceFully();
                }
                if (AudioRecorderWindow.instance != null) {
                    AudioRecorderWindow.instance.hideForceFully();
                }
                VideoRecorderWindow recorderWindow = new VideoRecorderWindow(groupId);
                recorderWindow.showRecodingVideoChat();
            } else if (menu.text.equalsIgnoreCase(MNU_MESSAGE_DELETE)) {
                optionsPopup.hideThis();
                btnSelectAll.setText(MNU_SELECT_ALL);
                btnSelectAll.setToolTipText(MNU_SELECT_ALL);
                showHideChatSelection(true, false);
                mnuDeleteChat.setText(MNU_CANCEL_MESSAGE_DELETE);
            } else if (menu.text.equalsIgnoreCase(MNU_CANCEL_MESSAGE_DELETE)) {
                optionsPopup.hideThis();
                btnSelectAll.setText(MNU_SELECT_ALL);
                btnSelectAll.setToolTipText(MNU_SELECT_ALL);
                showHideChatSelection(false, false);
                mnuDeleteChat.setText(MNU_MESSAGE_DELETE);
            } else if (menu.text.equalsIgnoreCase(MNU_CHANGE_BACKGROUND)) {
                RingIDSettingsDialog ringIDSettingsDialog = GuiRingID.getInstance().getTopMenuBar().getRingIDSettingsDialog();
                if (ringIDSettingsDialog == null) {
                    ringIDSettingsDialog = new RingIDSettingsDialog();
                    GuiRingID.getInstance().getTopMenuBar().setRingIDSettingsDialog(ringIDSettingsDialog);
                }
                MouseEvent me = new MouseEvent(ringIDSettingsDialog.mnuChatBackgroundSetting, 0, 0, 0, 10, 10, 1, false);
                for (MouseListener ml : ringIDSettingsDialog.mnuChatBackgroundSetting.getMouseListeners()) {
                    ml.mouseClicked(me);
                }
                ringIDSettingsDialog.setVisible(true);
                ringIDSettingsDialog.toFront();
            }
            menu.setMouseExited();
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

    public void buildMembersPanel() {
        membersPanel.removeAll();
        setGroupMemberCount();
        GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(groupId);
        if (groupInfo != null) {
            if (Objects.equals(groupInfo.getOwnerUserTableId(), MyFnFSettings.userProfile.getUserTableId())) {
                dropTarget = new DropTarget(this.membersPanel, dropTargetListener);
            }

            if (groupInfo.getMemberMap() != null && groupInfo.getMemberMap().size() > 0) {
                for (GroupMemberDTO member : groupInfo.getMemberMap().values()) {
                    if (!member.getUserIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                        ContactPanel panel = new ContactPanel(member, groupInfo);
                        membersPanel.add(panel);
                    }
                }
            }
        }
        membersPanel.revalidate();
        membersPanel.repaint();
    }

    DropTargetListener dropTargetListener = new DropTargetListener() {

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
                    addDroppedMember(dragContents);
                } else {
                    dtde.rejectDrop();
                }
            } catch (IOException e) {
                dtde.rejectDrop();
            } catch (UnsupportedFlavorException e) {
                dtde.rejectDrop();
            }
        }

    };

    private void addDroppedMember(String userIdentity) {
        GroupInfoDTO groupInfo = MyfnfHashMaps.getInstance().getGroup_hash_map().get(groupId);
        UserBasicInfo basicInfo = FriendList.getInstance().getFriend_hash_map().get(userIdentity);
        if (groupInfo != null
                && basicInfo != null
                && basicInfo.getFriendShipStatus() != null
                && basicInfo.getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED
                && !check_already_in_group(userIdentity)) {
            GroupMemberDTO member = new GroupMemberDTO();
            member.setGroupId(groupId);
            member.setUserIdentity(basicInfo.getUserIdentity());
            member.setUserTableId(basicInfo.getUserTableId());
            member.setFullName(HelperMethods.getUserFullName(basicInfo));

            ContactPanel panel = new ContactPanel(member, groupInfo);
            membersPanel.add(panel);
            membersPanel.revalidate();
            membersPanel.repaint();
            new AddMemberInGroup(groupName, member, panel).start();
        }
    }

    public boolean check_already_in_group(String user_id) {
        GroupInfoDTO groupInfoDTO = MyfnfHashMaps.getInstance().getGroup_hash_map().get(groupId);
        if (groupInfoDTO != null && groupInfoDTO.getMemberMap() != null && groupInfoDTO.getMemberMap().get(user_id) != null) {
            return true;
        }
        return false;
    }

    public class ContactPanel extends JPanel {

        public GroupMemberDTO member;
        private JButton btnClose;
        private ContactPanel contactPanel;
        public GroupInfoDTO groupInfo;

        public ContactPanel(GroupMemberDTO member1, GroupInfoDTO groupInfo) {
            this.contactPanel = this;
            this.member = member1;
            this.groupInfo = groupInfo;
            //this.setPopulateInfo();
            this.setLayout(new BorderLayout(5, 0));
            this.setBorder(new EmptyBorder(0, 15, 0, 5));
            this.setOpaque(false);

            JLabel jLabel = new JLabel(member.getFullName() != null && member.getFullName().length() > 0 ? member.getFullName() : member1.getUserIdentity());
            jLabel.setForeground(Color.WHITE);
            try {
                Font font = getDefaultFont(Font.PLAIN, 12);
                jLabel.setFont(font);
            } catch (Exception ex) {
                jLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            }
            jLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            jLabel.setOpaque(false);
            this.add(jLabel, BorderLayout.CENTER);

            JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 3));
            wrapperPanel.setPreferredSize(new Dimension(15, 22));
            wrapperPanel.setOpaque(false);
            btnClose = DesignClasses.createImageButton(GetImages.CLOSE_MINI, GetImages.CLOSE_MINI_H, "Remove");
            btnClose.setVisible(false);
            btnClose.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    membersPanel.remove(contactPanel);
                    membersPanel.revalidate();
                    membersPanel.repaint();
                    new RemoveMemberFromGroup(member.getGroupId(), member.getUserIdentity(), contactPanel).start();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    btnClose.setVisible(true);
                }
            });
            if (Objects.equals(groupInfo.getOwnerUserTableId(), MyFnFSettings.userProfile.getUserTableId())) {
                wrapperPanel.add(btnClose);
            }
            this.add(wrapperPanel, BorderLayout.EAST);
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btnClose.setVisible(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btnClose.setVisible(false);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(new Color(0xcccccc));
            int w = getWidth();
            int h = getHeight();
            g2d.fillRoundRect(0, 0, w, h, 18, 18);
        }

    }

    public void addGroupChatPanel() {
        masterPanel = new ImagePane();
        masterPanel.setLayout(new BorderLayout(DefaultSettings.HGAP, DefaultSettings.VGAP));
        masterPanel.setDropTarget(new FileDropTarget(this, fileDropHandler));
        setChatBackground();
        add(masterPanel, BorderLayout.CENTER);

        /* *********************************
         * Duration Panel
         * ********************************/
        JPanel recentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 5));
        recentPanel.setPreferredSize(new Dimension(168, 0));
        recentPanel.setOpaque(false);

        JLabel lblRecent = new JLabel("History", SwingConstants.RIGHT);
        lblRecent.setForeground(RingColorCode.RING_CHAT_SHADE_FG);
        lblRecent.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        recentPanel.add(lblRecent);
        recentPanel.add(btnRecent);

        lblSelected.setForeground(RingColorCode.RING_CHAT_SHADE_FG);
        lblSelected.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        recentPanel.add(lblSelected);

//        JPanel messageDatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
//        messageDatePanel.setOpaque(false);
//
//        lblMessageDate = lblMessageDate = DesignClasses.makeAncorLabel("", Font.BOLD, 11);;
//        messageDatePanel.add(lblMessageDate);
        JPanel timerAndRecentWrapper = new JPanel();
        timerAndRecentWrapper.setOpaque(false);
        timerAndRecentWrapper.setLayout(new GridBagLayout());

        JPanel timerAndRecentContiner = new JPanel(new BorderLayout());
        timerAndRecentContiner.setOpaque(false);
        timerAndRecentContiner.setPreferredSize(new Dimension(625, 30));
        timerAndRecentContiner.add(recentPanel, BorderLayout.WEST);
        //timerAndRecentContiner.add(messageDatePanel, BorderLayout.CENTER);
        timerAndRecentWrapper.add(timerAndRecentContiner);

        masterPanel.add(timerAndRecentWrapper, BorderLayout.NORTH);

        /* *********************************
         * Message Container Panel
         * ********************************/
        JPanel scrollWrapPanel = new JPanel(new BorderLayout());
        scrollWrapPanel.setOpaque(false);
        masterPanel.add(scrollWrapPanel, BorderLayout.CENTER);

        messagePanel = new JPanel();
        messagePanel.setOpaque(false);
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        JPanel chatContainerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        chatContainerWrapper.setOpaque(false);
        chatContainerWrapper.add(messagePanel);

        chatContainerPanel = new JPanel();
        chatContainerPanel.setLayout(new BorderLayout(0, 0));
        chatContainerPanel.setOpaque(false);
        chatContainerPanel.add(chatContainerWrapper, BorderLayout.SOUTH);

        scrollContent = DesignClasses.getDefaultScrollPane(chatContainerPanel);// DesignClasses.getDefaultScorllPane(chatContainerPanel);
        scrollContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollWrapPanel.add(scrollContent, BorderLayout.CENTER);

        buildTypingPanel(scrollWrapPanel);

        /* *********************************
         * Chat Writer Panel
         * ********************************/
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        masterPanel.add(bottomPanel, BorderLayout.SOUTH);

        deleteOptionContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 80));
                int w = getWidth();
                int h = getHeight();
                g2d.fillRect(0, 0, w, h);
            }
        };
        deleteOptionContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        deleteOptionContainer.setOpaque(false);
        bottomPanel.add(deleteOptionContainer, BorderLayout.NORTH);

        chatWriterContainer = new JPanel(new BorderLayout(10, 0));
        chatWriterContainer.setOpaque(false);

        JPanel chatWriterPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 40));
                int w = getWidth();
                int h = getHeight();
                g2d.fillRect(0, 0, w, h);
            }
        };
        chatWriterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        chatWriterPanel.setOpaque(false);
        chatWriterPanel.setBorder(new EmptyBorder(9, 0, 9, 0));
        chatWriterPanel.add(chatWriterContainer);

        JPanel chatWriterWrapper = new JPanel(new BorderLayout());
        chatWriterWrapper.setOpaque(false);
        chatWriterWrapper.setBorder(new MatteBorder(1, 0, 0, 0, new Color(0xcac3b9)));
        chatWriterWrapper.add(chatWriterPanel, BorderLayout.CENTER);
        bottomPanel.add(chatWriterWrapper, BorderLayout.CENTER);

        chatWriter = DesignClasses.createJTextArea("", MAX_CHAT_TEXT_LENGTH);
        chatWriter.setBackground(Color.WHITE);
        chatWriter.setColumns(chatWritercolumns);
        chatWriter.setBorder(new EmptyBorder(5, 0, 5, 5));
        chatWriter.setRows(1);
        chatWriter.addMouseListener(new ContextMenuMouseListener());
        chatWriter.setDropTarget(new FileDropTarget(chatWriter, fileDropHandler));
        try {
            chatWriter.setFont(DesignClasses.getDefaultFont(Font.PLAIN, 12f));
        } catch (Exception e) {
        }

        chatWriter.addKeyListener(keyListener);
        chatWriter.addFocusListener(focusListener);
        btnEmoticon.addActionListener(actionListener);
        btnSendChat.addActionListener(actionListener);
        btnChatMoreOption.addActionListener(actionListener);
        btnRecent.addActionListener(actionListener);

        recentPopup = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_CHAT_RECENT);
        recentPopup.addMenu(MNU_TODAY, null);
        recentPopup.addMenu(MNU_YESTERDAY, null);
        recentPopup.addMenu(MNU_7_DAYS, null);
        recentPopup.addMenu(MNU_30_DAYS, null);
        recentPopup.addMenu(MNU_3_MONTHS, null);

        optionsPopup = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_CHAT_OPTIONS);
        optionsPopup.addMenu(MNU_SEND_PHOTO, GetImages.OPTION_PHOTO_MSG);
        optionsPopup.addMenu(MNU_SEND_FILE, GetImages.OPTION_FILE_MSG);
        optionsPopup.addMenu(MNU_TAKE_PHOTO, GetImages.OPTION_TAKE_PHOTO);
        optionsPopup.addMenu(MNU_VIDEO_MESG, GetImages.OPTION_VIDEO_MSG);
        mnuDeleteChat = optionsPopup.addMenu(MNU_MESSAGE_DELETE, GetImages.DELETE_PHOTO);
        optionsPopup.addMenu(MNU_VOICE_MESG, GetImages.OPTION_VOICE_MSG);
        optionsPopup.addMenu(MNU_CHANGE_BACKGROUND, GetImages.OPTION_TIMER_MSG);

        buildChatDeletePanel();
        buildChatWritingArea();

        scrollContent.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                int containerHeight = messagePanel.getHeight();
                int topHeight = e.getValue();
                int barHeight = scrollContent.getVerticalScrollBar().getHeight();
                int bottomHeight = containerHeight - (topHeight + barHeight);

                if (scrollContent.getVerticalScrollBar().isVisible() && LOADED_HISTORY_TIME.size() > 0) {
                    if (AddEmoticonsJDialog.currentInstance != null && AddEmoticonsJDialog.currentInstance.isVisible()) {
                        AddEmoticonsJDialog.currentInstance.setVisible(false);
                        AddEmoticonsJDialog.currentInstance.dispose();
                        AddEmoticonsJDialog.currentInstance = null;
                    }
//                    if (JCustomMenuPopup.getInstance() != null && JCustomMenuPopup.getInstance().isDisplayable()) {
//                        JCustomMenuPopup.getInstance().setVisible(false);
//                        JCustomMenuPopup.getInstance().dispose();
//                        JCustomMenuPopup.getInstance().setNull();
//                    }
                    if (isTopLoading == false && topHeight < currentV && topHeight <= UP_SCROLL_LIMIT) {
                        // SCROLL TO TOP
                        //System.err.println("SCROLL TO TOP");
                        isTopLoading = true;
                        isBottomLoading = true;
                        new GetGroupHistory(groupId, LOADED_HISTORY_TIME.get(0).getTime(), 3, RecentChatCallActivityDAO.TYPE_SCROLL_UP).start();
                    }
                    currentV = topHeight;
                    //showMessageDateName();
                }
            }
        });

        loadInitHistory();
    }

    public void setChatBackground() {
        try {
            masterPanel.setImage(DesignClasses.return_chatbg_buffer_image(SettingsConstants.FNF_CHAT_BG));
        } catch (Exception ex) {
            try {
                masterPanel.setImage(DesignClasses.return_chatbg_buffer_image(SettingsConstants.FNF_DEAFULT_CHAT_BG));
            } catch (Exception exx) {
                masterPanel.setBackground(Color.WHITE);
            }
        }
        masterPanel.repaint();
    }

    public void buildChatWritingArea() {
        chatWriterContainer.removeAll();

        if (check_already_in_group(MyFnFSettings.LOGIN_USER_ID)) {
            JPanel text_emotion_panel = chat_box();
            chatWriterContainer.add(text_emotion_panel, BorderLayout.CENTER);
        } else {
            JPanel text_emotion_panel = empty_box();
            chatWriterContainer.add(text_emotion_panel, BorderLayout.CENTER);
        }

        chatWriterContainer.revalidate();
        chatWriterContainer.repaint();
    }

    public void buildTypingPanel(JPanel container) {
        /*
         TYPING PANEL
         */
        JPanel typingWrapPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        typingWrapPanel.setOpaque(false);
        container.add(typingWrapPanel, BorderLayout.SOUTH);

        JPanel typingBorderPanel = new JPanel(new BorderLayout());
        typingBorderPanel.setOpaque(false);
        typingWrapPanel.add(typingBorderPanel);
        JPanel userTypingIconWrapper = new JPanel(new BorderLayout());
        userTypingIconWrapper.setOpaque(false);
        userTypingIcon = new JLabel(DesignClasses.return_image(GetImages.TYPING));
        userTypingIcon.setOpaque(false);
        userTypingIcon.setVisible(false);
        userTypingIconWrapper.add(userTypingIcon, BorderLayout.NORTH);
        typingBorderPanel.add(userTypingIconWrapper, BorderLayout.WEST);

        userTypingArea = new JTextArea();
        try {
            userTypingArea.setFont(DesignClasses.getDefaultFont(Font.PLAIN, 10f));
        } catch (Exception e) {
        }
        userTypingArea.setOpaque(false);
        userTypingArea.setEditable(false);
        userTypingArea.setColumns(51);
        userTypingArea.setForeground(Color.WHITE);
        userTypingArea.setWrapStyleWord(true);
        userTypingArea.setLineWrap(true);
        userTypingArea.setDropTarget(new FileDropTarget(userTypingArea, fileDropHandler));
        typingBorderPanel.add(userTypingArea, BorderLayout.CENTER);
    }

    public void addTyping(String userId) {
        if (chatUserTyping == null || !chatUserTyping.isRunning()) {
            chatUserTyping = new ChatUserTyping(userTypingArea, userTypingIcon);
            chatUserTyping.addUserId(userId);
            chatUserTyping.startThread();
        } else {
            chatUserTyping.addUserId(userId);
        }
    }

    private void buildChatDeletePanel() {
        deleteOptionContainer.setVisible(false);

        JPanel deleteOptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 7));
        deleteOptionPanel.setOpaque(false);
        deleteOptionContainer.add(deleteOptionPanel);

        btnSelectAll.addActionListener(actionListener);
        btnDelete.addActionListener(actionListener);
        btnCancel.addActionListener(actionListener);

        btnSelectAll.setPreferredSize(new Dimension(110, 25));
        btnDelete.setPreferredSize(new Dimension(110, 25));
        btnCancel.setPreferredSize(new Dimension(110, 25));
        deleteOptionPanel.add(btnSelectAll);
        deleteOptionPanel.add(btnDelete);
        deleteOptionPanel.add(btnCancel);
    }

    public JPanel chat_box() {
        Component com1 = Box.createHorizontalStrut(30);
        Component com2 = Box.createHorizontalStrut(13);
        final JPanel chatWritingAreaPanel = new JPanel(new BorderLayout());
        chatWritingAreaPanel.setBorder(new EmptyBorder(9, 0, 11, 0));
        chatWritingAreaPanel.setOpaque(false);

        JScrollPane scrollPane = DesignClasses.getDefaultScrollPaneThin(new JScrollPane(chatWriter) {
            @Override
            public void doLayout() {
                super.doLayout();
                int minHeight = 26;
                int maxHeight = 220;
                int prefHeight;

                if (chatWriter.getHeight() > maxHeight) {
                    if (!this.getVerticalScrollBar().isVisible()) {
                        this.getVerticalScrollBar().setVisible(true);
                    }
                    prefHeight = maxHeight + 9 + 9;
                } else {
                    if (this.getVerticalScrollBar().isVisible()) {
                        this.getVerticalScrollBar().setVisible(false);
                    }
                    if (chatWriter.getHeight() < minHeight) {
                        prefHeight = minHeight + 9 + 9;
                    } else {
                        prefHeight = chatWriter.getHeight() + 9 + 9;
                    }
                }

                chatWritingAreaPanel.setPreferredSize(new Dimension(chatWritingAreaPanel.getWidth(), prefHeight));
                chatWritingAreaPanel.revalidate();

            }
        });
        chatWritingAreaPanel.add(scrollPane, BorderLayout.CENTER);

        ChatWrapperPanel contet = new ChatWrapperPanel(RingColorCode.RING_CHAT_AREA_ACTIVE_BG, RingColorCode.RING_CHAT_AREA_ACTIVE_SHADOW_BG);
        contet.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        contet.add(btnChatMoreOption);
        contet.add(com1);
        contet.add(chatWritingAreaPanel);
        contet.add(btnEmoticon);
        contet.add(com2);
        contet.add(btnSendChat);

        return contet;
    }

    private JPanel empty_box() {
        ChatWrapperPanel content = new ChatWrapperPanel(RingColorCode.RING_CHAT_AREA_INACTIVE_BG, RingColorCode.RING_CHAT_AREA_INACTIVE_SHADOW_BG);
        content.setLayout(new GridBagLayout());
        content.setPreferredSize(new Dimension(565, 46));

        JLabel label = new JLabel();
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        label.setForeground(Color.WHITE);
        label.setText("Chat conversation unavailable!");
        content.add(label);
        return content;
    }

    public class SquarePanel extends JPanel {

        @Override
        public Dimension getPreferredSize() {
            int size = Math.min(this.getParent().getHeight(), this.getParent().getWidth());
            return new Dimension(size, size);
        }
    }

    public class FileDropTarget extends DropTarget {

        public FileDropTarget(JComponent component, FileDropHandler fileDropHandler) {
            super(component, DnDConstants.ACTION_COPY_OR_MOVE, fileDropHandler, true);
        }

    }

    public class FileDropHandler implements DropTargetListener {

        private List<String> EXT_IMAGE = new ArrayList<String>();
        private final int WAITING = 1;
        private final int ACCEPT = 2;
        private final int REJECT = 3;
        private int state = WAITING;

        public FileDropHandler() {
            EXT_IMAGE.add("jpg");
            EXT_IMAGE.add("jpeg");
            EXT_IMAGE.add("png");
        }

        @Override
        public void dragEnter(DropTargetDragEvent dtde) {
            state = REJECT;
            Transferable t = dtde.getTransferable();
            Object td = null;

            if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                try {
                    td = t.getTransferData(DataFlavor.javaFileListFlavor);
                    if (td instanceof List) {
                        state = ACCEPT;
                        for (Object value : ((List) td)) {
                            if (value instanceof File) {
                                File file = (File) value;
                                String extension = HelperMethods.getExtension(file.getName().toLowerCase());
                                if (!EXT_IMAGE.contains(extension)) {
                                    state = REJECT;
                                    break;
                                }
                            } else {
                                state = REJECT;
                                break;
                            }
                        }
                    }
                } catch (Exception ex) {
                    //ex.printStackTrace();
                    log.error("Error in here ==>" + ex.getMessage());
                }
            }
            if (state == ACCEPT) {
                dtde.acceptDrag(DnDConstants.ACTION_COPY);
            } else {
                dtde.rejectDrag();
            }
        }

        @Override
        public void dragOver(DropTargetDragEvent dtde) {
        }

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {
        }

        @Override
        public void dragExit(DropTargetEvent dte) {
            state = WAITING;
        }

        @Override
        public void drop(DropTargetDropEvent dtde) {
            try {
                ChatHelpers.startGroupChat(groupId, true);

                Transferable t = dtde.getTransferable();
                if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : files) {
                        new ChatFileShareProcessor(file, instance, ChatConstants.TYPE_IMAGE_FILE_DIRECTORY, 0).start();
                    }
                }
            } catch (Exception ex) {
                //ex.printStackTrace();
                log.error("Error in here ==>" + ex.getMessage());
            }
            state = WAITING;
        }
    }

    private void buildChatPacket() {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageType(ChatConstants.TYPE_PLAIN_MSG);
        messageDTO.setPacketType(ChatConstants.CHAT_GROUP);
        messageDTO.setFullName(MyFnFSettings.userProfile.getFullName());
        String packetID = SendToServer.getRanDomPacketID();
        messageDTO.setPacketID(packetID);
        messageDTO.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
        messageDTO.setGroupId(groupId);
        messageDTO.setMessage(chatWriter.getText());
        messageDTO.setStatus(ChatConstants.CHAT_GROUP_PENDING);
        messageDTO.setSystemDate(System.currentTimeMillis());
        //addSinglePanelInChatContainerPanel(messageDTO);
        ChatService.sendGroupChat(messageDTO, new PacketResenderHandler(), InternetUnavailablityCheck.isInternetAvailable);
    }

    public synchronized boolean addSinglePanelInChatContainerPanel(final MessageDTO messageDTO) {
        boolean isLoaded = false;
        try {
            isTopLoading = true;
            isBottomLoading = true;

            RecentDTO recentDTO = new RecentDTO();
            recentDTO.setTime(messageDTO.getMessageDate() > 0 ? messageDTO.getMessageDate() : System.currentTimeMillis());
            recentDTO.setHistoryDateName(ChatHelpers.CHAT_DATE_FORMAT.format(new Date(recentDTO.getTime())));
            recentDTO.setMessageDTO(messageDTO);

            int size = LOADED_HISTORY_TIME.size();
            if (size <= 0 || recentDTO.getTime() > LOADED_HISTORY_TIME.get(size - 1).getTime()) {
                //System.err.println("1");
                setChatDateGroupInRear(recentDTO);

                SingleChatPanel singleChatPanel = new SingleChatPanel(messagePanel, messageDTO, lastChatSenderIdentity);
                singleChatPanel.showSelectBox(mnuDeleteChat.text.equalsIgnoreCase(MNU_CANCEL_MESSAGE_DELETE), btnSelectAll.getText().equalsIgnoreCase(MNU_DESELECT_ALL));
                messagePanel.add(singleChatPanel);
                LOADED_HISTORY_TIME.add(new Data(recentDTO));
                isLoaded = true;
                lastChatSenderIdentity = messageDTO.getUserIdentity();

                messagePanel.revalidate();
                messagePanel.repaint();
                setScrollValue(messagePanel.getHeight());
            } else if (recentDTO.getTime() > LOADED_HISTORY_TIME.get(0).getTime()) {
                //System.err.println("2");
                int componentIndex = getEstimatedComponentIndex(recentDTO.getTime());
                int dataIndex = getEstimatedDataIndex(recentDTO.getTime());
                String prevChatSenderIdentity = getPrevChatSenderIdentity(componentIndex);

                if (setChatDateGroupInBetween(recentDTO, dataIndex, componentIndex)) {
                    componentIndex++;
                }

                SingleChatPanel singleChatPanel = new SingleChatPanel(messagePanel, recentDTO.getMessageDTO(), prevChatSenderIdentity);
                singleChatPanel.showSelectBox(mnuDeleteChat.text.equalsIgnoreCase(MNU_CANCEL_MESSAGE_DELETE), btnSelectAll.getText().equalsIgnoreCase(MNU_DESELECT_ALL));
                messagePanel.add(singleChatPanel, componentIndex);
                LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
                isLoaded = true;
                componentIndex++;

                setChatSenderIdentityInRear(recentDTO.getMessageDTO().getUserIdentity(), componentIndex);
            } else if (messagePanel.getHeight() <= scrollContent.getHeight()) {
                //System.err.println("3");
                setChatDateGroupInFront(recentDTO);
                int componentIndex = 1;
                int dataIndex = 0;

                SingleChatPanel singleChatPanel = new SingleChatPanel(messagePanel, recentDTO.getMessageDTO(), null);
                singleChatPanel.showSelectBox(mnuDeleteChat.text.equalsIgnoreCase(MNU_CANCEL_MESSAGE_DELETE), btnSelectAll.getText().equalsIgnoreCase(MNU_DESELECT_ALL));
                messagePanel.add(singleChatPanel, componentIndex);
                LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
                isLoaded = true;
                componentIndex++;

                setChatSenderIdentityInRear(recentDTO.getMessageDTO().getUserIdentity(), componentIndex);
            }
        } catch (Exception e) {
            isTopLoading = false;
            isBottomLoading = false;
        }
        return isLoaded;
    }

    public synchronized void addSinglePanelInChatContainerPanel(final ActivityDTO activityDTO) {
        try {
            isTopLoading = true;
            isBottomLoading = true;

            RecentDTO recentDTO = new RecentDTO();
            recentDTO.setTime(activityDTO.getUpdateTime());
            recentDTO.setHistoryDateName(ChatHelpers.CHAT_DATE_FORMAT.format(new Date(recentDTO.getTime())));
            recentDTO.setActivityDTO(activityDTO);

            int size = LOADED_HISTORY_TIME.size();
            if (size <= 0 || recentDTO.getTime() > LOADED_HISTORY_TIME.get(size - 1).getTime()) {
                setChatDateGroupInRear(recentDTO);

                messagePanel.add(new SingleActivityPanel(activityDTO));
                LOADED_HISTORY_TIME.add(new Data(recentDTO));

                messagePanel.revalidate();
                messagePanel.repaint();
                setScrollValue(messagePanel.getHeight());
            } else if (recentDTO.getTime() > LOADED_HISTORY_TIME.get(0).getTime()) {
                int componentIndex = getEstimatedComponentIndex(recentDTO.getTime());
                int dataIndex = getEstimatedDataIndex(recentDTO.getTime());

                if (setChatDateGroupInBetween(recentDTO, dataIndex, componentIndex)) {
                    componentIndex++;
                }

                messagePanel.add(new SingleActivityPanel(recentDTO.getActivityDTO()), componentIndex);
                LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
            } else if (messagePanel.getHeight() <= scrollContent.getHeight()) {
                setChatDateGroupInFront(recentDTO);
                int componentIndex = 1;
                int dataIndex = 0;

                messagePanel.add(new SingleActivityPanel(recentDTO.getActivityDTO()), componentIndex);
                LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
            }
        } catch (Exception e) {
            isTopLoading = false;
            isBottomLoading = false;
        }
    }

    public synchronized void addHistory(List<RecentDTO> chatHistoryList, Integer limit) {
        try {
            if (messagePanel != null) {
                messagePanel.removeAll();
                messagePanel.revalidate();
                messagePanel.setVisible(false);
                LOADED_HISTORY_TIME.clear();
                lastChatSenderIdentity = null;
                boolean isFirstChatMsg = true;
                if (ChatHashMap.getInstance().getChatSingleChatPanel().get(groupId + "") != null) {
                    ChatHashMap.getInstance().getChatSingleChatPanel().get(groupId + "").clear();
                }

                for (RecentDTO recentDTO : chatHistoryList) {
                    if (recentDTO.getType() == RecentChatCallActivityDAO.TYPE_GROUP_CHAT) {
                        setChatDateGroupInFront(recentDTO);
                        int componentIndex = 1;
                        int dataIndex = 0;

                        SingleChatPanel singleChatPanel = new SingleChatPanel(messagePanel, recentDTO.getMessageDTO(), null);
                        singleChatPanel.showSelectBox(mnuDeleteChat.text.equalsIgnoreCase(MNU_CANCEL_MESSAGE_DELETE), btnSelectAll.getText().equalsIgnoreCase(MNU_DESELECT_ALL));
                        messagePanel.add(singleChatPanel, componentIndex);
                        LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
                        componentIndex++;

                        setChatSenderIdentityInRear(recentDTO.getMessageDTO().getUserIdentity(), componentIndex);
                        if (isFirstChatMsg) {
                            lastChatSenderIdentity = recentDTO.getMessageDTO().getUserIdentity();
                            isFirstChatMsg = false;
                        }
                    } else if (recentDTO.getType() == RecentChatCallActivityDAO.TYPE_GROUP_ACTIVITY) {
                        setChatDateGroupInFront(recentDTO);
                        int componentIndex = 1;
                        int dataIndex = 0;

                        messagePanel.add(new SingleActivityPanel(recentDTO.getActivityDTO()), componentIndex);
                        LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
                    }

                    messagePanel.revalidate();
                    if (limit != null && limit > 0 && messagePanel.getHeight() > scrollContent.getHeight() + UP_SCROLL_LIMIT) {
                        break;
                    }
                }

                messagePanel.setVisible(true);
                messagePanel.revalidate();
                messagePanel.repaint();
                Thread.sleep(100);
                setScrollValue(messagePanel.getHeight());
            }
        } catch (Exception e) {
            isTopLoading = false;
            isBottomLoading = false;
        }
    }

    public synchronized void addHistoryByScrollUp(List<RecentDTO> chatHistoryList) {
        try {
            if (messagePanel != null && chatHistoryList.size() > 0) {
                int height = messagePanel.getHeight();
                for (RecentDTO recentDTO : chatHistoryList) {
                    if (recentDTO.getType() == RecentChatCallActivityDAO.TYPE_GROUP_CHAT) {
                        setChatDateGroupInFront(recentDTO);
                        int componentIndex = 1;
                        int dataIndex = 0;

                        SingleChatPanel singleChatPanel = new SingleChatPanel(messagePanel, recentDTO.getMessageDTO(), null);
                        singleChatPanel.showSelectBox(mnuDeleteChat.text.equalsIgnoreCase(MNU_CANCEL_MESSAGE_DELETE), btnSelectAll.getText().equalsIgnoreCase(MNU_DESELECT_ALL));
                        messagePanel.add(singleChatPanel, componentIndex);
                        LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
                        componentIndex++;

                        setChatSenderIdentityInRear(recentDTO.getMessageDTO().getUserIdentity(), componentIndex);
                    } else if (recentDTO.getType() == RecentChatCallActivityDAO.TYPE_GROUP_ACTIVITY) {
                        setChatDateGroupInFront(recentDTO);
                        int componentIndex = 1;
                        int dataIndex = 0;

                        messagePanel.add(new SingleActivityPanel(recentDTO.getActivityDTO()), componentIndex);
                        LOADED_HISTORY_TIME.add(dataIndex, new Data(recentDTO));
                    }

                    messagePanel.revalidate();
                    if (messagePanel.getHeight() > height + UP_SCROLL_LIMIT) {
                        break;
                    }
                }

                messagePanel.revalidate();
                setScrollValue(messagePanel.getHeight() - height + UP_SCROLL_LIMIT);
            } else {
                isTopLoading = false;
                isBottomLoading = false;
            }
        } catch (Exception e) {
            isTopLoading = false;
            isBottomLoading = false;
        }
    }

    public synchronized void addHistoryByScrollDown(List<RecentDTO> chatHistoryList) {
        try {
            if (messagePanel != null && chatHistoryList.size() > 0) {
                for (RecentDTO recentDTO : chatHistoryList) {
                    if (recentDTO.getType() == RecentChatCallActivityDAO.TYPE_GROUP_CHAT) {
                        setChatDateGroupInRear(recentDTO);

                        SingleChatPanel singleChatPanel = new SingleChatPanel(messagePanel, recentDTO.getMessageDTO(), lastChatSenderIdentity);
                        singleChatPanel.showSelectBox(mnuDeleteChat.text.equalsIgnoreCase(MNU_CANCEL_MESSAGE_DELETE), btnSelectAll.getText().equalsIgnoreCase(MNU_DESELECT_ALL));
                        messagePanel.add(singleChatPanel);
                        LOADED_HISTORY_TIME.add(new Data(recentDTO));

                        lastChatSenderIdentity = recentDTO.getMessageDTO().getUserIdentity();
                    } else if (recentDTO.getType() == RecentChatCallActivityDAO.TYPE_GROUP_ACTIVITY) {
                        setChatDateGroupInRear(recentDTO);

                        messagePanel.add(new SingleActivityPanel(recentDTO.getActivityDTO()));
                        LOADED_HISTORY_TIME.add(new Data(recentDTO));
                    }
                }

                messagePanel.revalidate();
                messagePanel.repaint();
                setScrollValue(messagePanel.getHeight());
            } else {
                isTopLoading = false;
                isBottomLoading = false;
            }
        } catch (Exception e) {
            isTopLoading = false;
            isBottomLoading = false;
        }
    }

    public void setScrollValue(final int value) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //System.err.println(chatContainerPanel.getHeight() + " " + value);
                //showMessageDateName();
                scrollContent.getVerticalScrollBar().setValue(value);
                isTopLoading = false;
                isBottomLoading = false;
            }
        });
    }

    ComponentListener componentListener = new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            if (onWindowResize == null || !onWindowResize.isRunning()) {
                onWindowResize = new LoadHistoryOnWindowResize();
                onWindowResize.resizing();
                onWindowResize.startThread();
            } else {
                onWindowResize.resizing();
            }
        }
    };

    public void loadInitHistory() {
        new loadHistoryOnInitialization(null).start();
    }

    /*
     Need to call only from history remove
     */
    public void loadInitHistory(Boolean donNotCheckWindowDisplayable) {
        LOADED_HISTORY_TIME.clear();
        new loadHistoryOnInitialization(donNotCheckWindowDisplayable).start();
    }

    private class loadHistoryOnInitialization extends Thread {

        private Boolean donNotCheckWindowDisplayable;

        public loadHistoryOnInitialization(Boolean donNotCheckWindowDisplayable) {
            this.donNotCheckWindowDisplayable = donNotCheckWindowDisplayable;
        }

        @Override
        public void run() {
            try {
                //System.err.println("loadHistoryOnInitialization");
                masterPanel.removeComponentListener(componentListener);
                isTopLoading = true;
                isBottomLoading = true;

                if (donNotCheckWindowDisplayable == null || donNotCheckWindowDisplayable == false) {
                    do {
                        Thread.sleep(100);
                        scrollContent.revalidate();
                        scrollContent.repaint();
                    } while (scrollContent.isDisplayable() == false || scrollContent.getHeight() <= 0);
                }
                getHistoryFromDB(null);

                masterPanel.addComponentListener(componentListener);
            } catch (Exception ex) {
                isBottomLoading = false;
                isTopLoading = false;
                // ex.printStackTrace();
                log.error("Error in here ==>" + ex.getMessage());
            }
        }

    }

    private class LoadHistoryOnWindowResize extends Thread {

        private boolean running = true;
        private Long MAX_VALUE = (long) 1500;
        private Long time = 0L;

        public void resizing() {
            this.time = MAX_VALUE;
        }

        @Override
        public void run() {
            try {
                while (running) {
                    if (time <= 0) {
                        stopThread();
                        continue;
                    }

                    Thread.sleep(500);
                    time -= 500;

                    if (time <= 0) {
                        stopThread();
                    }
                }
            } catch (Exception e) {
                stopThread();
            }
        }

        public void startThread() {
            isBottomLoading = true;
            isTopLoading = true;
            running = true;
            this.start();
        }

        public void stopThread() {
            refrestHistory();
            running = false;
        }

        public boolean isRunning() {
            return running;
        }

        public void refrestHistory() {
            try {
                //System.err.println("refrestHistory");
                getHistoryFromDB(null);
            } catch (Exception ex) {
                isBottomLoading = false;
                isTopLoading = false;
                // ex.printStackTrace();
                log.error("Error in here ==>" + ex.getMessage());
            }
        }

    }

    private void loadHistoryOnRecent(int days) {
        try {
            //System.err.println("loadHistoryByDays(int days)");
            isTopLoading = true;
            isBottomLoading = true;
            getHistoryFromDB(days);
        } catch (Exception ex) {
            isBottomLoading = false;
            isTopLoading = false;
            // ex.printStackTrace();
            log.error("Error in here ==>" + ex.getMessage());
        }
    }

    private void getHistoryFromDB(Integer days) {
        scrollContent.revalidate();
        scrollContent.repaint();
        int limit = (scrollContent.getHeight() / unit_size);
        //System.err.println("Height = " + scrollContent.getHeight() + ", limit = " + limit);
        RecentDTO recentDTO = ChatHashMap.getInstance().getChatUnreadMessages().get(groupId + "");

        if (days != null) {
            new GetGroupHistory(groupId, days, null).start();
            if (recentDTO != null) {
                GuiRingID.getInstance().getMainButtonsPanel().clearChatNotification(groupId + "");
            }

        } else if (LOADED_HISTORY_TIME.size() <= 0) {
            new GetGroupHistory(groupId, RecentContactDAO._90_DAYS, limit).start();
            if (recentDTO != null) {
                GuiRingID.getInstance().getMainButtonsPanel().clearChatNotification(groupId + "");
            }

        } else if (LOADED_HISTORY_TIME.size() < limit) {
            if (recentDTO != null) {
                new GetGroupHistory(groupId, RecentContactDAO._90_DAYS, limit).start();
                GuiRingID.getInstance().getMainButtonsPanel().clearChatNotification(groupId + "");
            } else {
                new GetGroupHistory(groupId, LOADED_HISTORY_TIME.get(0).getTime(), limit - LOADED_HISTORY_TIME.size(), RecentChatCallActivityDAO.TYPE_SCROLL_UP).start();
            }

        } else if (recentDTO != null) {
            new GetGroupHistory(groupId, LOADED_HISTORY_TIME.get(LOADED_HISTORY_TIME.size() - 1).getTime(), null, RecentChatCallActivityDAO.TYPE_SCROLL_DOWN).start();
            GuiRingID.getInstance().getMainButtonsPanel().clearChatNotification(groupId + "");

        } else {
            isBottomLoading = false;
            isTopLoading = false;
        }
    }

    private class Data {

        private long time;
        private String dateTime;

        public Data(RecentDTO recentDTO) {
            this.time = recentDTO.getTime();
            this.dateTime = recentDTO.getHistoryDateName();
        }

        public long getTime() {
            return time;
        }

        public String getDateTime() {
            return dateTime;
        }

    }

    private void setChatDateGroupInFront(RecentDTO recentDTO) {
        if (LOADED_HISTORY_TIME.size() > 0) {
            String dateTime = LOADED_HISTORY_TIME.get(0).getDateTime();
            if (!dateTime.equalsIgnoreCase(recentDTO.getHistoryDateName())) {
                messagePanel.add(new ChatDateGroupPanel(recentDTO), 0);
            }
        } else {
            messagePanel.add(new ChatDateGroupPanel(recentDTO));
        }
    }

    private void setChatDateGroupInRear(RecentDTO recentDTO) {
        if (LOADED_HISTORY_TIME.size() > 0) {
            String dateTime = LOADED_HISTORY_TIME.get(LOADED_HISTORY_TIME.size() - 1).getDateTime();
            if (!dateTime.equalsIgnoreCase(recentDTO.getHistoryDateName())) {
                messagePanel.add(new ChatDateGroupPanel(recentDTO));
            }
        } else {
            messagePanel.add(new ChatDateGroupPanel(recentDTO));
        }
    }

    private boolean setChatDateGroupInBetween(RecentDTO recentDTO, int dataIndex, int componentIndex) {
        String prevDateTime = LOADED_HISTORY_TIME.get(dataIndex - 1).getDateTime();
        String nextDateTime = LOADED_HISTORY_TIME.get(dataIndex).getDateTime();
        if (!prevDateTime.equalsIgnoreCase(recentDTO.getHistoryDateName()) && !nextDateTime.equalsIgnoreCase(recentDTO.getHistoryDateName())) {
            messagePanel.add(new ChatDateGroupPanel(recentDTO), componentIndex);
            return true;
        } else if (!prevDateTime.equalsIgnoreCase(recentDTO.getHistoryDateName())) {
            return true;
        }
        return false;
    }

    private void setChatSenderIdentityInRear(String userIdentity, int index) {
        synchronized (messagePanel.getTreeLock()) {
            for (int idx = index; idx < messagePanel.getComponentCount(); idx++) {
                Component component = messagePanel.getComponents()[idx];
                if (component instanceof SingleChatPanel) {
                    SingleChatPanel singleChatPanel = (SingleChatPanel) component;
                    singleChatPanel.lastChatSenderIdentity = userIdentity;
                    singleChatPanel.setFullName();
                    break;
                }
            }
        }
    }

    private String getPrevChatSenderIdentity(int index) {
        synchronized (messagePanel.getTreeLock()) {
            if (messagePanel.getComponentCount() > 0) {
                for (int idx = index - 1; idx >= 0; idx--) {
                    Component component = messagePanel.getComponents()[idx];
                    if (component instanceof SingleChatPanel) {
                        SingleChatPanel singleChatPanel = (SingleChatPanel) component;
                        return singleChatPanel.messageDTO.getUserIdentity();
                    }
                }
            }
        }
        return null;
    }

    private int getEstimatedComponentIndex(long time) {
        int index = 0;
        synchronized (messagePanel.getTreeLock()) {
            if (messagePanel.getComponentCount() > 0) {
                for (int idx = messagePanel.getComponentCount() - 1; idx >= 0; idx--) {
                    Component component = messagePanel.getComponents()[idx];
                    if (component instanceof SingleChatPanel) {
                        SingleChatPanel singleChatPanel = (SingleChatPanel) component;
                        long messageDate = singleChatPanel.messageDTO.getMessageDate() > 0 ? singleChatPanel.messageDTO.getMessageDate() : singleChatPanel.messageDTO.getSystemDate();
                        if (time > messageDate) {
                            index = idx + 1;
                            break;
                        }
                    } else if (component instanceof SingleActivityPanel) {
                        SingleActivityPanel singleActivityPanel = (SingleActivityPanel) component;
                        if (time > singleActivityPanel.activityDTO.getUpdateTime()) {
                            index = idx + 1;
                            break;
                        }
                    }
                }
            }
        }
        return index;
    }

    private int getEstimatedDataIndex(long time) {
        int index = 0;
        synchronized (LOADED_HISTORY_TIME) {
            if (LOADED_HISTORY_TIME.size() > 0) {
                for (int idx = LOADED_HISTORY_TIME.size() - 1; idx >= 0; idx--) {
                    Data date = LOADED_HISTORY_TIME.get(idx);
                    if (time > date.getTime()) {
                        index = idx + 1;
                        break;
                    }
                }
            }
        }
        return index;
    }

    public void removeSingleChatPanel(SingleChatPanel panel) {
        try {
            if (panel != null) {
                String currentLastChatIdentity = panel.lastChatSenderIdentity;

                synchronized (messagePanel.getTreeLock()) {
                    int currIdx = messagePanel.getComponentZOrder(panel);
                    int prevIdx = currIdx - 1;
                    messagePanel.remove(panel);
                    messagePanel.revalidate();
                    messagePanel.repaint();

                    if (currIdx >= 0 && currIdx < messagePanel.getComponentCount()) {
                        for (int idx = currIdx; idx < messagePanel.getComponentCount(); idx++) {
                            if (messagePanel.getComponent(idx) instanceof SingleChatPanel) {
                                SingleChatPanel nextChatpanel = (SingleChatPanel) messagePanel.getComponent(idx);
                                nextChatpanel.lastChatSenderIdentity = currentLastChatIdentity;
                                nextChatpanel.setFullName();
                                nextChatpanel.revalidate();
                                nextChatpanel.repaint();
                                break;
                            }
                        }
                    } else {
                        lastChatSenderIdentity = currentLastChatIdentity;
                    }

                    if (prevIdx >= 0 && prevIdx < messagePanel.getComponentCount()) {
                        Component prevPanel = messagePanel.getComponent(prevIdx);

                        if (prevPanel instanceof ChatDateGroupPanel) {
                            Component currPanel = currIdx < messagePanel.getComponentCount() ? messagePanel.getComponent(currIdx) : null;

                            if (currPanel == null || currPanel instanceof ChatDateGroupPanel) {
                                messagePanel.remove(prevPanel);
                                messagePanel.revalidate();
                                messagePanel.repaint();
                            }
                        }
                    }
                }

                getHistoryFromDB(null);
            }

        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Error in removeSingleChatPanel ==>" + ex.getMessage());
        }
    }

    private class ChatWrapperPanel extends JPanel {

        private Color color;
        private Color chatShadowColor;
        private int arc = 50;
        private int subArc = 40;
        private int cropArc = 43;
        private int mainX = 45;
        private int mainY = 0;
        private int subX = 0;
        private int subY = 3;
        private int subWidth = 80;
        private int cropX = 38;
        private int cropY = 1;
        private int cropWidth = 45;

        public ChatWrapperPanel(Color color, Color chatBorderColor) {
            this.color = color;
            this.chatShadowColor = chatBorderColor;
            this.setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            Area mainShadowShape = new Area(new RoundRectangle2D.Double(mainX, mainY, w - mainX * 2, h, arc, arc));
            g2d.setColor(chatShadowColor);
            g2d.fill(mainShadowShape);

            Area sideShadowShape = new Area(new RoundRectangle2D.Double(subX, subY, subWidth, h - 6, subArc, subArc));
            sideShadowShape.subtract(new Area(new RoundRectangle2D.Double(cropX - 2, cropY, cropWidth, h - 2, cropArc, cropArc)));
            sideShadowShape.add(new Area(new RoundRectangle2D.Double(w - subX - subWidth, subY, subWidth, h - 6, subArc, subArc)));
            sideShadowShape.subtract(new Area(new RoundRectangle2D.Double(w - cropX - cropWidth + 2, cropY, cropWidth, h - 2, cropArc, cropArc)));
            g2d.setColor(RingColorCode.RING_CHAT_AREA_ACTIVE_SHADOW_BG);
            g2d.fill(sideShadowShape);

            Area mainShape = new Area(new RoundRectangle2D.Double(mainX, mainY - 1, w - mainX * 2, h, arc, arc));
            g2d.setColor(color);
            g2d.fill(mainShape);

            Area sideShape = new Area(new RoundRectangle2D.Double(subX, subY - 1, subWidth, h - 6, subArc, subArc));
            sideShape.subtract(new Area(new RoundRectangle2D.Double(cropX, cropY - 1, cropWidth, h - 2, cropArc, cropArc)));
            sideShape.add(new Area(new RoundRectangle2D.Double(w - subX - subWidth, subY - 1, subWidth, h - 6, subArc, subArc)));
            sideShape.subtract(new Area(new RoundRectangle2D.Double(w - cropX - cropWidth, cropY - 1, cropWidth, h - 2, cropArc, cropArc)));
            g2d.setColor(RingColorCode.RING_CHAT_AREA_SIDE_BG);
            g2d.fill(sideShape);
        }

    }

//    private void showMessageDateName() {
//        String dateName = "";
//        if (scrollContent.getVerticalScrollBar().isVisible()) {
//            int val = scrollContent.getVerticalScrollBar().getValue();
//            int recY = ((MyScrollbarVerticalUI) scrollContent.getVerticalScrollBar().getUI()).recY;
//            int value = val - recY;
//
////            int recHeight = ((MyScrollbarVerticalUI) scrollContent.getVerticalScrollBar().getUI()).recHeight;
////            int scrollHeight = scrollContent.getHeight();
////            System.err.println( " /// val = " + val 
////                    + " /// recY = " + recY
////                    + " /// recHeight = " + recHeight
////                    + " /// scrollHeight = " + scrollHeight
////                    + " /// CAL = " + (scrollHeight - (recY + recHeight))
////            );
//            if (messagePanel.getComponentCount() > 0) {
//                Component component = messagePanel.findComponentAt(0, value);
//                if (component != null) {
//                    System.err.println(component.getName());
//                    if (component instanceof SingleChatPanel) {
//                        SingleChatPanel chatPanel = (SingleChatPanel) component;
//                        dateName = getDateName(chatPanel.messageDTO.getMessageDate());
//                    } else if (component instanceof SingleCallPanel) {
//                        SingleCallPanel callPanel = (SingleCallPanel) component;
//                        dateName = getDateName(callPanel.callLog.getCallingTime());
//                    } else if (component instanceof SingleActivityPanel) {
//                        SingleActivityPanel activityPanel = (SingleActivityPanel) component;
//                        dateName = getDateName(activityPanel.activityDTO.getUpdateTime());
//                    } else if (component instanceof ChatDateGroupPanel) {
//                        ChatDateGroupPanel dateGroupPanel = (ChatDateGroupPanel) component;
//                        dateName = dateGroupPanel.dateName;
//                    }
//                }
//            }
//        }
//
//        lblMessageDate.setText(dateName);
//    }
//
//    private String getDateName(Long time) {
//        String dateName = "";
//        Date date = new Date();
//        date.setHours(0);
//        date.setMinutes(0);
//        date.setSeconds(0);
//        long currentDay = date.getTime();
//        date.setDate(date.getDate() - 1);
//        long yesterDay = date.getTime();
//
//        if (time >= currentDay) {
//            dateName = " Today, " + ChatHelpers.CHAT_DATE_FORMAT.format(new Date(time));
//        } else if (time < currentDay && time >= yesterDay) {
//            dateName = " Yesterday, " + ChatHelpers.CHAT_DATE_FORMAT.format(new Date(time));
//        } else {
//            dateName = ChatHelpers.CHAT_DATE_FORMAT.format(new Date(time));
//        }
//
//        return dateName;
//    }
    private void showHideChatSelection(boolean show, boolean isSelectAll) {
        Map<String, SingleChatPanel> singleChatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(groupId + "");
        if (singleChatPanelMap != null) {
            for (Entry<String, SingleChatPanel> entrySet : singleChatPanelMap.entrySet()) {
                SingleChatPanel singleChatPanel = entrySet.getValue();
                singleChatPanel.showSelectBox(show, isSelectAll);
            }
        }

        deleteOptionContainer.setVisible(show);
    }

    private void deleteChatMessage() {
        List<String> packetIds = new ArrayList<String>();
        List<MessageDTO> list = new ArrayList<MessageDTO>();
        List<SingleChatPanel> singleChatPanels = new ArrayList<SingleChatPanel>();

        Map<String, SingleChatPanel> singleChatPanelMap = ChatHashMap.getInstance().getChatSingleChatPanel().get(groupId + "");
        if (singleChatPanelMap != null && singleChatPanelMap.size() > 0) {
            for (Entry<String, SingleChatPanel> entrySet : singleChatPanelMap.entrySet()) {
                SingleChatPanel singleChatPanel = entrySet.getValue();
                if (singleChatPanel.chkSelect.isSelected()) {
                    list.add(singleChatPanel.messageDTO);
                    singleChatPanels.add(singleChatPanel);
                    if (singleChatPanel.messageDTO.getUserIdentity() != null && singleChatPanel.messageDTO.getUserIdentity().equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                        packetIds.add(singleChatPanel.messageDTO.getPacketID());
                    }
                }
            }
        }

        if (list.size() > 0) {
            if (packetIds.size() > 0) {
                ChatHelpers.startGroupChat(groupId, true);
            }

            HelperMethods.showConfirmationDialogMessage(NotificationMessages.DELETE_NOTIFICAITON);
            if (JOptionPanelBasics.YES_NO) {
                for (MessageDTO msgDTO : list) {
                    msgDTO.setStatus(ChatConstants.CHAT_GROUP_MESSAGE_DELETED);
                }

                new InsertIntoGroupMessageTable(list).start();

                if (packetIds.size() > 0) {
                    ChatHelpers.startGroupChat(groupId, true);
                    ChatService.sendGroupChatDelete(groupId, packetIds);
                }

                Collections.sort(singleChatPanels, new Comparator<SingleChatPanel>() {
                    @Override
                    public int compare(SingleChatPanel one, SingleChatPanel other) {
                        Long onesSeq = one.messageDTO.getMessageDate();
                        Long othersSeq = other.messageDTO.getMessageDate();
                        return othersSeq.compareTo(onesSeq);
                    }
                });

                for (SingleChatPanel singleChatPanel : singleChatPanels) {
                    removeSingleChatPanel(singleChatPanel);
                    if (singleChatPanelMap != null) {
                        singleChatPanelMap.remove(singleChatPanel.messageDTO.getPacketID());
                    }
                }
                singleChatPanels.clear();
                btnCancel.doClick();
                //showMessageDateName();
            }
        } else {
            HelperMethods.showWarningDialogMessage("Please, Select message!");
        }
    }
}
