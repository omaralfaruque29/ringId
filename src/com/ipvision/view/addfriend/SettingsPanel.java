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
import com.ipvision.constants.GetImages;
import com.ipvision.model.constants.StatusConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.service.ChangeSuggestionSettings;
import java.awt.FlowLayout;
import javax.swing.Box;

/**
 *
 * @author USER
 */
public class SettingsPanel extends JPanel implements MouseListener {

    JLabel commonFriendsLabel, chatAndAccessLabel, chatAndAccessLabel2;
    JLabel contactListLabel, chatAndAccessContactLabel, chatAndAccessContactLabel2;
    JLabel contactNumberLabel, callAndChatConNumLabel1, callAndChatConNumLabel2;
    public JCheckBox commonFriendsCheckBox, contactListCheckBox, checkBox3;
    JPanel rightHolder;
    JPanel upperPanel, middlePanel, lowerPanel;
    JPanel wrapperPanel;
    JPanel leftHolder;
    JPanel leftContactHolder;

    public SettingsPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(40, 100, 0, 100));

        JPanel commonFriendsPanel = new JPanel(new BorderLayout());
        commonFriendsPanel.setPreferredSize(new Dimension(330, 50));
        commonFriendsPanel.setOpaque(false);
        commonFriendsLabel = DesignClasses.makeLableBold("Common Friends", 16);
        commonFriendsLabel.setPreferredSize(new Dimension(330, 16));
        commonFriendsPanel.add(commonFriendsLabel, BorderLayout.CENTER);

        JPanel chatAndAccessPanel = new JPanel(new BorderLayout());
        chatAndAccessPanel.setPreferredSize(new Dimension(330, 50));
        chatAndAccessPanel.setOpaque(false);
        chatAndAccessLabel = DesignClasses.makeJLabelFullNamePlain2("Added Friends Automatically for Call & Chat Access only", 12);
        chatAndAccessLabel.setPreferredSize(new Dimension(330, 12));
        chatAndAccessLabel2 = DesignClasses.makeJLabelFullNamePlain2("by Common Friends", 12);
        chatAndAccessPanel.add(chatAndAccessLabel, BorderLayout.NORTH);
        chatAndAccessPanel.add(chatAndAccessLabel2, BorderLayout.CENTER);
        leftHolder = new JPanel(new BorderLayout());
        leftHolder.setPreferredSize(new Dimension(350, 100));
        leftHolder.setBorder(new EmptyBorder(0, 20, 0, 0));
        leftHolder.setOpaque(false);
        leftHolder.add(commonFriendsPanel, BorderLayout.NORTH);
        leftHolder.add(chatAndAccessPanel, BorderLayout.CENTER);

        rightHolder = new JPanel(new BorderLayout());
        //rightHolder.setBorder(new EmptyBorder(0, 10, 0, 0));
        rightHolder.setOpaque(false);
        rightHolder.setPreferredSize(new Dimension(50, 100));
        commonFriendsCheckBox = new JCheckBox(DesignClasses.return_image(GetImages.OFF));
        commonFriendsCheckBox.setSelectedIcon(DesignClasses.return_image(GetImages.ON));
        //commonFriendsCheckBox.setBackground(Color.WHITE);
        //commonFriendsCheckBox.setSelected(false);
        commonFriendsCheckBox.setOpaque(false);
        commonFriendsCheckBox.addMouseListener(this);
        if (MyFnFSettings.userProfile.getCommonFriendsSuggestion() != null && MyFnFSettings.userProfile.getCommonFriendsSuggestion() == 1) {
            commonFriendsCheckBox.setSelected(true);
        } else {
            commonFriendsCheckBox.setSelected(false);
        }
        rightHolder.add(commonFriendsCheckBox, BorderLayout.CENTER);
        upperPanel = new JPanel();
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
        upperPanel.setBackground(Color.WHITE);
        upperPanel.setPreferredSize(new Dimension(480, 100));
        //upperPanel.setBorder(new MatteBorder(1, 0, 1, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR));
        /*        upperPanel.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 3, 0, 0, RingColorCode.RING_THEME_COLOR),
         new MatteBorder(1, 0, 1, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR)));*/
        upperPanel.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 3, 0, 0, Color.WHITE),
                new MatteBorder(1, 0, 0, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR)));
        upperPanel.add(leftHolder);
        upperPanel.add(Box.createRigidArea(new Dimension(40, 0)));
        upperPanel.add(rightHolder);
        upperPanel.addMouseListener(this);

        JPanel contacListPanel = new JPanel(new BorderLayout());
        contacListPanel.setPreferredSize(new Dimension(330, 50));
        contacListPanel.setOpaque(false);
        contactListLabel = DesignClasses.makeLableBold("Contacts List", 16);
        contactListLabel.setPreferredSize(new Dimension(330, 16));
        contacListPanel.add(contactListLabel, BorderLayout.CENTER);
        chatAndAccessContactLabel = DesignClasses.makeJLabelFullNamePlain2("Added Friends Automatically for Call & Chat Access only", 12);
        chatAndAccessContactLabel.setPreferredSize(new Dimension(330, 12));
        chatAndAccessContactLabel2 = DesignClasses.makeJLabelFullNamePlain2("by your Contacts List", 12);
        chatAndAccessContactLabel2.setPreferredSize(new Dimension(330, 12));
        JPanel chatAndAccessPanelContactList = new JPanel(new BorderLayout());
        chatAndAccessPanelContactList.setPreferredSize(new Dimension(330, 50));
        chatAndAccessPanelContactList.setOpaque(false);
        chatAndAccessPanelContactList.add(chatAndAccessContactLabel, BorderLayout.NORTH);
        chatAndAccessPanelContactList.add(chatAndAccessContactLabel2, BorderLayout.CENTER);
        leftContactHolder = new JPanel(new BorderLayout());
        leftContactHolder.setOpaque(false);
        leftContactHolder.setBorder(new EmptyBorder(0, 20, 0, 0));
        leftContactHolder.setBackground(Color.WHITE);
        leftContactHolder.setPreferredSize(new Dimension(350, 100));
        leftContactHolder.add(contacListPanel, BorderLayout.NORTH);
        leftContactHolder.add(chatAndAccessPanelContactList, BorderLayout.CENTER);

        JPanel rightContactHolder = new JPanel(new BorderLayout());
        rightContactHolder.setPreferredSize(new Dimension(50, 100));
        rightContactHolder.setOpaque(false);
        contactListCheckBox = new JCheckBox(DesignClasses.return_image(GetImages.OFF));
        contactListCheckBox.setSelectedIcon(DesignClasses.return_image(GetImages.ON));
        //contactListCheckBox.setBackground(Color.WHITE);
        contactListCheckBox.setOpaque(false);
        contactListCheckBox.addMouseListener(this);
        if (MyFnFSettings.userProfile.getContactListSuggestion() != null && MyFnFSettings.userProfile.getContactListSuggestion().intValue() == 1) {
            contactListCheckBox.setSelected(true);
        } else {
            contactListCheckBox.setSelected(false);
        }
        rightContactHolder.add(contactListCheckBox, BorderLayout.CENTER);

        middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
        middlePanel.setBackground(Color.white);
        middlePanel.setPreferredSize(new Dimension(480, 100));
        //middlePanel.setBorder(new MatteBorder(0, 0, 1, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR));
        middlePanel.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 3, 0, 0, Color.WHITE),
                new MatteBorder(1, 0, 1, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR)));
        middlePanel.add(leftContactHolder);
        middlePanel.add(Box.createRigidArea(new Dimension(40, 0)));
        middlePanel.add(rightContactHolder);
        middlePanel.addMouseListener(this);
        /*  
         JPanel contacNumberPanel = new JPanel(new BorderLayout());
         contacNumberPanel.setPreferredSize(new Dimension(330, 50));
         contacNumberPanel.setOpaque(false);
         contactNumberLabel = DesignClasses.makeJLabelFullNamePlain2("Contacts Number", 16);
         contactNumberLabel.setPreferredSize(new Dimension(330, 16));
         contacNumberPanel.add(contactNumberLabel, BorderLayout.CENTER);

         callAndChatConNumLabel1 = DesignClasses.makeJLabelFullNamePlain2("Added Friends Automatically for Call & Chat Access only", 12);
         callAndChatConNumLabel1.setPreferredSize(new Dimension(330, 12));
         callAndChatConNumLabel2 = DesignClasses.makeJLabelFullNamePlain2("by your Contacts List", 12);
         callAndChatConNumLabel2.setPreferredSize(new Dimension(330, 12));
         JPanel chatAndAccessPanelContactNum = new JPanel(new BorderLayout());
         chatAndAccessPanelContactNum.setPreferredSize(new Dimension(330, 50));
         chatAndAccessPanelContactNum.setOpaque(false);
         chatAndAccessPanelContactNum.add(callAndChatConNumLabel1, BorderLayout.NORTH);
         chatAndAccessPanelContactNum.add(callAndChatConNumLabel2, BorderLayout.CENTER);

         JPanel leftContactNumHolder = new JPanel(new BorderLayout());
         leftContactNumHolder.setBorder(new EmptyBorder(0, 20, 0, 0));
         leftContactNumHolder.setOpaque(false);
         leftContactNumHolder.setPreferredSize(new Dimension(380, 100));
         leftContactNumHolder.add(contacNumberPanel, BorderLayout.NORTH);
         leftContactNumHolder.add(chatAndAccessPanelContactNum, BorderLayout.CENTER);

         JPanel rightContactNumHolder = new JPanel(new BorderLayout());
         rightContactNumHolder.setPreferredSize(new Dimension(50, 100));
         rightContactNumHolder.setBorder(new EmptyBorder(0, 20, 0, 0));
         rightContactNumHolder.setOpaque(false);
         checkBox3 = new JCheckBox(DesignClasses.return_image(GetImages.TICK));
         checkBox3.setSelectedIcon(DesignClasses.return_image(GetImages.TICK_H2));
         checkBox3.setRolloverIcon(DesignClasses.return_image(GetImages.TICK_H));
         checkBox3.setOpaque(false);
         rightContactNumHolder.add(checkBox3, BorderLayout.CENTER);

         lowerPanel = new JPanel(new BorderLayout());
         lowerPanel.setOpaque(false);
         lowerPanel.setPreferredSize(new Dimension(380, 100));
         lowerPanel.setBorder(new MatteBorder(0, 0, 1, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR));
         lowerPanel.add(leftContactNumHolder, BorderLayout.WEST);
         lowerPanel.add(rightContactNumHolder, BorderLayout.EAST);
         */
        // JPanel holder= new JPanel(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.add(upperPanel);
        panel.add(middlePanel);
        // panel.add(lowerPanel);
        wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(panel, BorderLayout.NORTH);
        add(wrapperPanel, BorderLayout.CENTER);

    }

    private int checkBoxValue(JCheckBox box) {
        if (box.isSelected()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

//        if (e.getSource() == upperPanel) {
//            upperPanel.setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
//            upperPanel.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 3, 0, 0, RingColorCode.RING_THEME_COLOR),
//                    new MatteBorder(1, 0, 0, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR)));
//            middlePanel.setBorder(BorderFactory.createCompoundBorder(null,
//                    new MatteBorder(1, 0, 1, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR)));
//            middlePanel.setBackground(Color.WHITE);
////            upperPanel.revalidate();
////            upperPanel.repaint();
////            middlePanel.revalidate();
////            middlePanel.repaint();
//            //   lowerPanel.setBackground(Color.WHITE);
//        } else if (e.getSource() == middlePanel) {
//            upperPanel.setBackground(Color.WHITE);
//            middlePanel.setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
//            middlePanel.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 3, 0, 0, RingColorCode.RING_THEME_COLOR),
//                    new MatteBorder(1, 0, 1, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR)));
//            upperPanel.setBorder(BorderFactory.createCompoundBorder(null,
//                    new MatteBorder(1, 0, 0, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR)));
////            upperPanel.revalidate();
////            upperPanel.repaint();
////            middlePanel.revalidate();
////            middlePanel.repaint();
        //       } 
        if (e.getSource() == commonFriendsCheckBox) {
            new ChangeSuggestionSettings(StatusConstants.COMMON_FRIENDS_SUGGESTION, checkBoxValue(commonFriendsCheckBox)).start();
        } else if (e.getSource() == contactListCheckBox) {
            new ChangeSuggestionSettings(StatusConstants.CONTACT_LIST_SUGGESTION, checkBoxValue(contactListCheckBox)).start();

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

        if (e.getSource() == upperPanel || e.getSource() == commonFriendsCheckBox) {
            upperPanel.setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
            upperPanel.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 3, 0, 0, RingColorCode.RING_THEME_COLOR),
                    new MatteBorder(1, 0, 0, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR)));
            middlePanel.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 3, 0, 0, Color.WHITE),
                    new MatteBorder(1, 0, 1, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR)));
            middlePanel.setBackground(Color.WHITE);

        } else if (e.getSource() == middlePanel || e.getSource() == contactListCheckBox) {
            upperPanel.setBackground(Color.WHITE);
            middlePanel.setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
            middlePanel.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 3, 0, 0, RingColorCode.RING_THEME_COLOR),
                    new MatteBorder(1, 0, 1, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR)));
            upperPanel.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 3, 0, 0, Color.WHITE),
                    new MatteBorder(1, 0, 0, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR)));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

        if (e.getSource() == upperPanel || e.getSource() == commonFriendsCheckBox) {
            upperPanel.setBackground(Color.WHITE);
            upperPanel.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 3, 0, 0, Color.WHITE),
                    new MatteBorder(1, 0, 0, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR)));
        } else if (e.getSource() == middlePanel || e.getSource() == contactListCheckBox) {
            middlePanel.setBackground(Color.WHITE);
            middlePanel.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 3, 0, 0, Color.WHITE),
                    new MatteBorder(1, 0, 1, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR)));
        }
    }

}
