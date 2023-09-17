/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.view.utility.RoundedCornerButton;
import com.ipvision.view.utility.JOptionPanelBasicsShadow;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.model.constants.StatusConstants;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author shahadat
 */
public class JDialogContactType extends JOptionPanelBasicsShadow implements ActionListener {

    private JCheckBox chkChatCallAccess;
    private JCheckBox chkFullAccess;
    private JButton btnSave;
    private JButton btnCancel;
    private int action;
    public static int contactType = 0;

    public JDialogContactType() {
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        GridBagConstraints con = new GridBagConstraints();
        con.gridx = 0;
        con.ipady = 15;
        con.gridy = 0;

        JPanel emptyContainer1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emptyContainer1.setOpaque(false);
        emptyContainer1.setPreferredSize(new Dimension(0, 30));
        content.add(emptyContainer1, con);
        con.gridy++;

        ImageIcon radioIcon = DesignClasses.return_image(GetImages.RADIO_BUTTON);
        ImageIcon radioIconSelected = DesignClasses.return_image(GetImages.RADIO_BUTTON_SELECTED);

        JPanel inputContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        inputContainer.setOpaque(false);

        JPanel fullAccessContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 0));
        fullAccessContainer.setOpaque(false);
        fullAccessContainer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClick(StatusConstants.ACCESS_FULL);
            }
        });
        inputContainer.add(fullAccessContainer);

        chkFullAccess = new JCheckBox(radioIcon);
        chkFullAccess.setSelectedIcon(radioIconSelected);
        chkFullAccess.setOpaque(false);
        chkFullAccess.addActionListener(this);
        fullAccessContainer.add(chkFullAccess);

        JLabel lblFullAccess = new JLabel("Complete Profile");
        lblFullAccess.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        lblFullAccess.setOpaque(false);
        lblFullAccess.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClick(StatusConstants.ACCESS_FULL);
            }
        });
        fullAccessContainer.add(lblFullAccess);

        JPanel chatCallAccessContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 0));
        chatCallAccessContainer.setOpaque(false);
        chatCallAccessContainer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClick(StatusConstants.ACCESS_CHAT_CALL);
            }
        });
        inputContainer.add(chatCallAccessContainer);

        chkChatCallAccess = new JCheckBox(radioIcon);
        chkChatCallAccess.setSelectedIcon(radioIconSelected);
        chkChatCallAccess.setOpaque(false);
        chkChatCallAccess.addActionListener(this);
        chatCallAccessContainer.add(chkChatCallAccess);

        JLabel lblChatCallAccess = new JLabel("Call & Chat");
        lblChatCallAccess.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        lblChatCallAccess.setOpaque(false);
        lblChatCallAccess.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClick(StatusConstants.ACCESS_CHAT_CALL);
            }
        });
        chatCallAccessContainer.add(lblChatCallAccess);

        content.add(inputContainer, con);
        con.gridy++;

        JPanel emptyContainer3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emptyContainer3.setOpaque(false);
        emptyContainer3.setPreferredSize(new Dimension(30, 0));
        content.add(emptyContainer3, con);
        con.gridy++;

        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonContainer.setOpaque(false);

        btnSave = new RoundedCornerButton("Set", "Set Access Permission");
        btnSave.addActionListener(this);

        btnCancel = new RoundedCornerButton("Cancel", "Cancel");
        btnCancel.addActionListener(this);

        buttonContainer.add(btnSave);
        buttonContainer.add(btnCancel);

        content.add(buttonContainer, con);
        con.gridy++;

        onClick(StatusConstants.ACCESS_FULL);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            contactType = chkChatCallAccess.isSelected() ? StatusConstants.ACCESS_CHAT_CALL : StatusConstants.ACCESS_FULL;
            setVisible(false);
        } else if (e.getSource() == btnCancel) {
            contactType = 0;
            setVisible(false);
        } else if (e.getSource() == chkFullAccess) {
            onClick(StatusConstants.ACCESS_FULL);
        } else if (e.getSource() == chkChatCallAccess) {
            onClick(StatusConstants.ACCESS_CHAT_CALL);
        }
    }

    private void onClick(int type) {
        if (type == StatusConstants.ACCESS_CHAT_CALL) {
            chkFullAccess.setSelected(false);
            chkChatCallAccess.setSelected(true);
        } else if (type == StatusConstants.ACCESS_FULL) {
            chkFullAccess.setSelected(true);
            chkChatCallAccess.setSelected(false);
        }
    }

}
