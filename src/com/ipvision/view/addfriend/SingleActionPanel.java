/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.addfriend;

import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import com.ipvision.model.constants.StatusConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.service.AcceptFriendAccess;
import com.ipvision.service.AcceptFriendRequest;
import com.ipvision.service.ChangeFriendAccess;
import com.ipvision.service.DeleteFreind;
import com.ipvision.service.SendFriendRequest;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.view.image.ImageObjects;
import com.ipvision.view.utility.JOptionPanelBasics;
import com.ipvision.view.utility.RoundedCornerButton;
import com.ipvision.view.utility.ImageHelpers;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wasif Islam
 */
public class SingleActionPanel extends JPanel implements ActionListener {

    public static int TYPE_INVITE_REQUEST = 1;
    public static int TYPE_OUTGOING_REQUEST = 2;
    public static int TYPE_INCOMING_REQUEST = 3;
    public static int TYPE_OUTGOING_CHANGE_ACCESS = 4;
    public static int TYPE_INCOMING_CHANGE_ACCESS = 5;
    public static int TYPE_FOR_MIGRATION = 6;
    public List<JButton> tobeDisabledList = new ArrayList<>();
    public static String INVITE_TXT = "Invite with";
    public static String INCOMING_TXT = "Incoming request";
    public static String INCOMING_APPR_TXT = "Incoming approval";
    public static String PENDING_TXT = "Pending request";
    public static String PENDING_APPR_TXT = "Pending approval";
    public static String MIGRATE_TXT = "Migrate with";
    private UserBasicInfo user;
    private JLabel txtLabel;
    private JButton completeProfileBtn;
    private JButton callChatBtn;
    private JButton cancelBtn;
    private JButton laterBtn;
    private JButton acceptBtn;
    private JButton unfriendBtn;
    public int type;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel imagePanel;
    private int thumbSize = 70;

    public SingleActionPanel(UserBasicInfo user, int type) {
        this.user = user;
        this.type = type;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
        setPreferredSize(new Dimension(380, 155));
        setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.FRIEND_PANEL_SELECTED_BORDER_COLOR));

        leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(180, 150));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        add(leftPanel);

        rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(180, 150));
        rightPanel.setBorder(new MatteBorder(0, 1, 0, 0, RingColorCode.FRIEND_PANEL_SELECTED_BORDER_COLOR));
        add(rightPanel);

        imagePanel = new JPanel();
        imagePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        imagePanel.setOpaque(false);
        JLabel imageLabel = new JLabel();

        if (user.getProfileImage() != null && user.getProfileImage().trim().length() > 0) {
            ImageHelpers.addProfileImageThumbInQueue(imageLabel, user.getProfileImage(), thumbSize);
        } else {
//            BufferedImage img = ImageHelpers.getUnknownImage70();
//            imageLabel.setIcon(new ImageIcon(img));
//            img = null;
            imageLabel.setIcon(ImageObjects.unknown_70);
        }
        imagePanel.add(imageLabel);

        init();
    }

    public void changeStatus(int type) {
        this.type = type;
        init();
    }

    private void init() {
        leftPanel.removeAll();
        rightPanel.removeAll();
        buildLeftPane();
        buildRightPane();
        changeText();
    }

    private void buildLeftPane() {
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        txtLabel = DesignClasses.makeJLabelFullName("", 16);
        textPanel.add(txtLabel);
        leftPanel.add(textPanel);
        if (type == TYPE_INVITE_REQUEST || type == TYPE_FOR_MIGRATION) {
            completeProfileBtn = new RoundedCornerButton("Complete Profile", "Complete Profile");
            completeProfileBtn.addActionListener(this);
            JPanel completePn = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            completePn.setOpaque(false);
            completePn.setBorder(new EmptyBorder(0, 10, 0, 0));
            completePn.add(completeProfileBtn);
            leftPanel.add(completePn);
            if (type == TYPE_INVITE_REQUEST) {
                callChatBtn = new RoundedCornerButton("Call & Chat", "Call & Chat");
                callChatBtn.setPreferredSize(new Dimension(completeProfileBtn.getPreferredSize().width, callChatBtn.getPreferredSize().height));
                callChatBtn.addActionListener(this);
                JPanel callChatPn = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
                callChatPn.setOpaque(false);
                callChatPn.setBorder(new EmptyBorder(0, 10, 0, 0));
                callChatPn.add(callChatBtn);
                leftPanel.add(callChatPn);
            } else if (type == TYPE_FOR_MIGRATION) {
                completeProfileBtn.setPreferredSize(new Dimension(completeProfileBtn.getPreferredSize().width + 17, completeProfileBtn.getPreferredSize().height));
                unfriendBtn = new RoundedCornerButton("Unfriend", "Remove from Friends");
                unfriendBtn.addActionListener(this);
                laterBtn = new RoundedCornerButton("Later", "Later");
                laterBtn.addActionListener(this);
                JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 3));
                buttonsPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
                buttonsPanel.setOpaque(false);//Background(Color.red);
                buttonsPanel.add(unfriendBtn);
                buttonsPanel.add(laterBtn);
                leftPanel.add(buttonsPanel);
            }
        } else if (type == TYPE_INCOMING_REQUEST) {
            completeProfileBtn = new RoundedCornerButton("Complete Profile", "Complete Profile");
            completeProfileBtn.setPreferredSize(new Dimension(completeProfileBtn.getPreferredSize().width + 7, completeProfileBtn.getPreferredSize().height));
            completeProfileBtn.addActionListener(this);
            JPanel completePn = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            completePn.setOpaque(false);
            completePn.setBorder(new EmptyBorder(0, 10, 0, 0));
            completePn.add(completeProfileBtn);
            leftPanel.add(completePn);
            callChatBtn = new RoundedCornerButton("Call & Chat", "Call & Chat");
            callChatBtn.setPreferredSize(new Dimension(callChatBtn.getPreferredSize().width + 35, callChatBtn.getPreferredSize().height));
            callChatBtn.addActionListener(this);
            JPanel callChatPn = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            callChatPn.setOpaque(false);
            callChatPn.setBorder(new EmptyBorder(0, 10, 0, 0));
            callChatPn.add(callChatBtn);
            leftPanel.add(callChatPn);
            cancelBtn = new RoundedCornerButton("Reject", "Reject request");
            cancelBtn.addActionListener(this);
            laterBtn = new RoundedCornerButton("Later", "Later");
            laterBtn.addActionListener(this);
            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 3));
            buttonsPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
            buttonsPanel.setOpaque(false);
            buttonsPanel.add(cancelBtn);
            buttonsPanel.add(laterBtn);
            leftPanel.add(buttonsPanel);
            //System.out.println("sdcsc" + buttonsPanel.getPreferredSize());
        } else if (type == TYPE_OUTGOING_REQUEST) {
            completeProfileBtn = new RoundedCornerButton("Complete Profile", "Complete Profile");
            completeProfileBtn.setPreferredSize(new Dimension(completeProfileBtn.getPreferredSize().width + 10, completeProfileBtn.getPreferredSize().height));
            completeProfileBtn.addActionListener(this);
            JPanel completePn = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            completePn.setOpaque(false);
            completePn.setBorder(new EmptyBorder(0, 10, 0, 0));
            completePn.add(completeProfileBtn);
            leftPanel.add(completePn);
            callChatBtn = new RoundedCornerButton("Call & Chat", "Call & Chat");
            callChatBtn.setPreferredSize(new Dimension(callChatBtn.getPreferredSize().width + 38, callChatBtn.getPreferredSize().height));
            callChatBtn.addActionListener(this);
            JPanel callChatPn = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            callChatPn.setOpaque(false);
            callChatPn.setBorder(new EmptyBorder(0, 10, 0, 0));
            callChatPn.add(callChatBtn);
            leftPanel.add(callChatPn);
            cancelBtn = new RoundedCornerButton("Cancel", "Cancel request");
            cancelBtn.addActionListener(this);
            laterBtn = new RoundedCornerButton("Later", "Later");
            laterBtn.addActionListener(this);
            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 3));
            buttonsPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
            buttonsPanel.setOpaque(false);//Background(Color.red);
            buttonsPanel.add(cancelBtn);
            buttonsPanel.add(laterBtn);
            leftPanel.add(buttonsPanel);
            //System.out.println("sdcsc" + buttonsPanel.getPreferredSize());
            completeProfileBtn.setEnabled(false);
            callChatBtn.setEnabled(false);
        } else if (type == TYPE_OUTGOING_CHANGE_ACCESS) {
            JPanel accessTxtPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            accessTxtPanel.setOpaque(false);
            JTextArea txtArea = new JTextArea();
            txtArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
            txtArea.setOpaque(false);
            txtArea.setColumns(18);
            txtArea.setLineWrap(true);
            txtArea.setEditable(false);
            txtArea.setText("Request for Complete profile has been sent");
            accessTxtPanel.add(txtArea);
            leftPanel.add(accessTxtPanel);

            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            buttonsPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
            buttonsPanel.setOpaque(false);
            cancelBtn = new RoundedCornerButton("Cancel", "Cancel request");
            cancelBtn.addActionListener(this);
            buttonsPanel.add(cancelBtn);
            laterBtn = new RoundedCornerButton("Later", "Later");
            laterBtn.addActionListener(this);
            buttonsPanel.add(laterBtn);
            leftPanel.add(buttonsPanel);
        } else if (type == TYPE_INCOMING_CHANGE_ACCESS) {
            JPanel accessTxtPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            accessTxtPanel.setOpaque(false);
            JTextArea txtArea = new JTextArea();
            txtArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
            txtArea.setOpaque(false);
            txtArea.setColumns(18);
            txtArea.setLineWrap(true);
            txtArea.setEditable(false);
            txtArea.setText("You have received a request for Complete profile");
            accessTxtPanel.add(txtArea);
            leftPanel.add(accessTxtPanel);

            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            buttonsPanel.setBorder(new EmptyBorder(0, 8, 0, 0));
            buttonsPanel.setOpaque(false);
            acceptBtn = new RoundedCornerButton("Accept", "Accept request");
            acceptBtn.addActionListener(this);
            buttonsPanel.add(acceptBtn);
            cancelBtn = new RoundedCornerButton("Reject", "Reject request");
            cancelBtn.addActionListener(this);
            buttonsPanel.add(cancelBtn);
            leftPanel.add(buttonsPanel);
        }

    }

    private void buildRightPane() {

        rightPanel.add(imagePanel);

        JLabel titleLabel = DesignClasses.makeJLabelFullName(user.getFullName(), 16);
        JPanel titlePanel = new JPanel();
        titlePanel.setPreferredSize(new Dimension(180, 25));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        rightPanel.add(titlePanel);

        JPanel txtWrapper = new JPanel();
        txtWrapper.setPreferredSize(new Dimension(180, 55));
        txtWrapper.setOpaque(false);
        txtWrapper.setLayout(new BoxLayout(txtWrapper, BoxLayout.Y_AXIS));
        JLabel ringIdLabel = new JLabel(HelperMethods.getRingID(user.getUserIdentity()));
        JPanel ringPane = new JPanel(new BorderLayout());
        ringPane.setOpaque(false);
        ringPane.setBorder(new EmptyBorder(0, 40, 0, 0));
        ringPane.add(ringIdLabel, BorderLayout.CENTER);
        txtWrapper.add(ringPane);

        JLabel mutualLabel = new JLabel();
        if (user.getNumberOfMutualFriends() != null && user.getNumberOfMutualFriends() > 0) {
            if (user.getNumberOfMutualFriends() == 1) {
                mutualLabel.setText("1 Mutual Friend");
            } else {
                mutualLabel.setText(user.getNumberOfMutualFriends() + " Mutual Friends");
            }
        }
        JPanel mutualPane = new JPanel(new BorderLayout());
        mutualPane.setOpaque(false);
        mutualPane.setBorder(new EmptyBorder(0, 45, 0, 0));
        mutualPane.add(mutualLabel, BorderLayout.CENTER);
        txtWrapper.add(mutualPane);
        rightPanel.add(txtWrapper);

    }

    private void changeText() {
        if (type == TYPE_INVITE_REQUEST) {
            txtLabel.setText(INVITE_TXT);
        } else if (type == TYPE_INCOMING_REQUEST) {
            txtLabel.setText(INCOMING_TXT);
        } else if (type == TYPE_OUTGOING_REQUEST) {
            txtLabel.setText(PENDING_TXT);
        } else if (type == TYPE_OUTGOING_CHANGE_ACCESS) {
            txtLabel.setText(PENDING_APPR_TXT);
        } else if (type == TYPE_INCOMING_CHANGE_ACCESS) {
            txtLabel.setText(INCOMING_APPR_TXT);
        } else if (type == TYPE_FOR_MIGRATION) {
            txtLabel.setText(MIGRATE_TXT);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == completeProfileBtn) {
            if (type == TYPE_INCOMING_REQUEST) {
                tobeDisabledList.clear();
                tobeDisabledList.add(callChatBtn);
                tobeDisabledList.add(completeProfileBtn);
                tobeDisabledList.add(laterBtn);
                tobeDisabledList.add(cancelBtn);
                new AcceptFriendRequest(user.getUserIdentity(), tobeDisabledList, StatusConstants.ACCESS_FULL).start();
            } else if (type == TYPE_INVITE_REQUEST) {
                new SendFriendRequest(user.getUserIdentity(), completeProfileBtn, callChatBtn, StatusConstants.ACCESS_FULL).start();
            } else if (type == TYPE_FOR_MIGRATION) {
                new ChangeFriendAccess(user.getUserIdentity(), unfriendBtn, laterBtn, completeProfileBtn, user.getUserTableId(), StatusConstants.ACCESS_FULL).start();
            }
        } else if (e.getSource() == callChatBtn) {
            if (type == TYPE_INCOMING_REQUEST) {
                tobeDisabledList.clear();
                tobeDisabledList.add(callChatBtn);
                tobeDisabledList.add(completeProfileBtn);
                tobeDisabledList.add(laterBtn);
                tobeDisabledList.add(cancelBtn);
                new AcceptFriendRequest(user.getUserIdentity(), tobeDisabledList, StatusConstants.ACCESS_CHAT_CALL).start();
            } else if (type == TYPE_INVITE_REQUEST) {
                new SendFriendRequest(user.getUserIdentity(), completeProfileBtn, callChatBtn, StatusConstants.ACCESS_CHAT_CALL).start();
            }
        } else if (e.getSource() == cancelBtn) {
            if (type == TYPE_OUTGOING_CHANGE_ACCESS) {
                new AcceptFriendAccess(user.getUserIdentity(), user.getUserTableId(), user.getNewContactType(), false, laterBtn, cancelBtn).start();
            } else if (type == TYPE_INCOMING_CHANGE_ACCESS) {
                new AcceptFriendAccess(user.getUserIdentity(), user.getUserTableId(), user.getNewContactType(), false, acceptBtn, cancelBtn).start();
            } else if (type == TYPE_INCOMING_REQUEST) {
                HelperMethods.showConfirmationDialogMessage(NotificationMessages.REMOVE_NOTIFICAITON);
                if (JOptionPanelBasics.YES_NO) {
                    tobeDisabledList.clear();
                    tobeDisabledList.add(callChatBtn);
                    tobeDisabledList.add(completeProfileBtn);
                    tobeDisabledList.add(laterBtn);
                    tobeDisabledList.add(cancelBtn);
                    new DeleteFreind(user.getUserIdentity(), tobeDisabledList).start();
                }
            } else if (type == TYPE_OUTGOING_REQUEST) {
                HelperMethods.showConfirmationDialogMessage(NotificationMessages.REMOVE_NOTIFICAITON);
                if (JOptionPanelBasics.YES_NO) {
                    tobeDisabledList.clear();
                    tobeDisabledList.add(laterBtn);
                    tobeDisabledList.add(cancelBtn);
                    new DeleteFreind(user.getUserIdentity(), tobeDisabledList).start();
                }
            }
        } else if (e.getSource() == laterBtn) {
            AddFriendPanel.addButtonActionPanel(null);
        } else if (e.getSource() == acceptBtn) {
            if (type == TYPE_INCOMING_CHANGE_ACCESS) {
                new AcceptFriendAccess(user.getUserIdentity(), user.getUserTableId(), user.getNewContactType(), true, acceptBtn, cancelBtn).start();
            }
        } else if (e.getSource() == unfriendBtn) {
            HelperMethods.showConfirmationDialogMessage(NotificationMessages.REMOVE_NOTIFICAITON);
            if (JOptionPanelBasics.YES_NO) {
                tobeDisabledList.clear();
                tobeDisabledList.add(laterBtn);
                tobeDisabledList.add(completeProfileBtn);
                tobeDisabledList.add(unfriendBtn);
                new DeleteFreind(user.getUserIdentity(), tobeDisabledList).start();
            }
        }

    }
}
