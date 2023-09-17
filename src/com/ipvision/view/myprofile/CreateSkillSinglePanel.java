/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.service.AddSkillInfo;
import com.ipvision.service.DeleteSkillInfo;
import com.ipvision.service.aboutme.UpdateAddedSkillInfo;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.utility.JCustomMenuPopup;
import com.ipvision.view.utility.JOptionPanelBasics;
import com.ipvision.view.utility.RoundedCornerButton;
import com.ipvision.view.utility.TextPanelWithEmoticon;

/**
 *
 * @author Sirat Samyoun
 */
public class CreateSkillSinglePanel extends JPanel implements ActionListener {

    public JTextField skillField;
    public JTextArea descArea;
    public JButton saveButton, cancelButton, cancelButtoninUpdateInfo, saveButtoninUpdateInfo;
    public GridBagConstraints con;
    public JLabel errorLabel;
    private String skill, description;
    private JLabel skillLabel;
    private JPanel skillShowPanel, descriptionShowPanel, holder, wrapperPnl, mainPane;
    private CreateSkillSinglePanel instance;
    public Long skillInfoID;
    private JButton actionPopUpButton;
//    private JPopupMenu actionPopup;
//    private JMenuItem deleteInfo, editInfo;
    JCustomMenuPopup editDeletePopup = null;
    public static String MNU_EDIT = "Edit";
    public static String MNU_DELETE = "Delete";

    public CreateSkillSinglePanel getInstance() {
        return instance;
    }

    public CreateSkillSinglePanel() {
        this.instance = this;
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        wrapperPnl = new JPanel(new BorderLayout());
        wrapperPnl.setOpaque(false);
        wrapperPnl.setBorder(new EmptyBorder(5, 26, 0, 6));

        holder = new JPanel(new GridBagLayout());
        holder.setOpaque(false);
        mainPane = new JPanel(new BorderLayout());
        this.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        mainPane.setOpaque(false);
        wrapperPnl.add(mainPane, BorderLayout.CENTER);
        add(wrapperPnl, BorderLayout.CENTER);
        con = new GridBagConstraints();
        init();
    }

    public void init() {
        holder.setBorder(new EmptyBorder(10, 0, 10, 0));
        mainPane.add(holder, BorderLayout.CENTER);
        con.gridx = 0;
        con.gridy = 0;
        con.anchor = GridBagConstraints.WEST;
        JPanel skillPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        skillPanel.setOpaque(false);
        skillPanel.setPreferredSize(new Dimension(520, 30));
        skillPanel.add(Box.createRigidArea(new Dimension(80, 5)));
        JPanel skillNamePanel = new JPanel(new BorderLayout());
        skillNamePanel.setOpaque(false);
        skillNamePanel.setPreferredSize(new Dimension(100, 25));
        skillNamePanel.add(DesignClasses.makeJLabel_normal("Skill    ", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        skillPanel.add(skillNamePanel);
        skillField = DesignClasses.makeTextFieldLimitedTextSize("", 200, 20, 200);
        skillPanel.add(skillField);
        holder.add(skillPanel, con);
        con.gridy++;

        JPanel descPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        descPanel.setOpaque(false);
        // descPanel.setPreferredSize(new Dimension(520, 60));
        descPanel.add(Box.createRigidArea(new Dimension(80, 5)));
        JPanel descNamePanel = new JPanel(new BorderLayout());
        descNamePanel.setOpaque(false);
        descNamePanel.setPreferredSize(new Dimension(100, 25));
        descNamePanel.add(DesignClasses.makeJLabel_normal("Description    ", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        descPanel.add(descNamePanel);
        descArea = DesignClasses.createJTextArea("", 13, 100);
        // descArea.setPreferredSize(new Dimension(200, 60));
        descArea.setColumns(17);
        descPanel.add(descArea);
        holder.add(descPanel, con);
        con.gridy++;

        holder.add(Box.createRigidArea(new Dimension(80, 3)), con);
        con.gridy++;

        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.setPreferredSize(new Dimension(520, 15));
        errorPanel.setOpaque(false);
        errorLabel = DesignClasses.makeJLabel_normal(" ", 10, DefaultSettings.errorLabelColor);//new JLabel();
        errorPanel.add(Box.createRigidArea(new Dimension(200, 5)), BorderLayout.WEST);
        errorPanel.add(errorLabel, BorderLayout.CENTER);
        holder.add(errorPanel, con);
        con.gridy++;
        holder.add(Box.createRigidArea(new Dimension(80, 3)), con);
        con.gridy++;
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setPreferredSize(new Dimension(520, 30));
        buttonsPanel.setOpaque(false);
        saveButton = new RoundedCornerButton("Save", "Save");
        saveButton.addActionListener(this);
        cancelButton = new RoundedCornerButton("Cancel", "Cancel");
        cancelButton.addActionListener(this);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);
        holder.add(buttonsPanel, con);
        con.gridy++;
    }

    public void editSkillSinglePanel() {
        mainPane.removeAll();
        holder.removeAll();
        mainPane.add(holder, BorderLayout.CENTER);
        mainPane.revalidate();
        mainPane.repaint();
        holder.setBorder(new EmptyBorder(10, 0, 10, 0));
        con.gridx = 0;
        con.gridy = 0;
        con.anchor = GridBagConstraints.WEST;
        JPanel skillPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        skillPanel.setOpaque(false);
        skillPanel.setPreferredSize(new Dimension(520, 30));
        skillPanel.add(Box.createRigidArea(new Dimension(80, 5)));
        JPanel skillNamePanel = new JPanel(new BorderLayout());
        skillNamePanel.setOpaque(false);
        skillNamePanel.setPreferredSize(new Dimension(100, 25));
        skillNamePanel.add(DesignClasses.makeJLabel_normal("Skill    ", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        skillPanel.add(skillNamePanel);
        skillField = DesignClasses.makeTextFieldLimitedTextSize(skill, 200, 20, 200);
        skillPanel.add(skillField);
        holder.add(skillPanel, con);
        con.gridy++;

        JPanel descPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        descPanel.setOpaque(false);
        // descPanel.setPreferredSize(new Dimension(520, 60));
        descPanel.add(Box.createRigidArea(new Dimension(80, 5)));
        JPanel descNamePanel = new JPanel(new BorderLayout());
        descNamePanel.setOpaque(false);
        descNamePanel.setPreferredSize(new Dimension(100, 25));
        descNamePanel.add(DesignClasses.makeJLabel_normal("Description    ", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        descPanel.add(descNamePanel);
        descArea = DesignClasses.createJTextArea(description, 13, 100);
        // descArea.setPreferredSize(new Dimension(200, 60));
        descArea.setColumns(17);
        descPanel.add(descArea);
        holder.add(descPanel, con);
        con.gridy++;

        holder.add(Box.createRigidArea(new Dimension(80, 3)), con);
        con.gridy++;
        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.setPreferredSize(new Dimension(520, 15));
        errorPanel.setOpaque(false);
        errorLabel = DesignClasses.makeJLabel_normal(" ", 10, DefaultSettings.errorLabelColor);//new JLabel();
        errorPanel.add(Box.createRigidArea(new Dimension(200, 5)), BorderLayout.WEST);
        errorPanel.add(errorLabel, BorderLayout.CENTER);
        holder.add(errorPanel, con);
        con.gridy++;
        holder.add(Box.createRigidArea(new Dimension(80, 3)), con);
        con.gridy++;
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setPreferredSize(new Dimension(520, 30));
        buttonsPanel.setOpaque(false);
        saveButtoninUpdateInfo = new RoundedCornerButton("Save", "Save");
        saveButtoninUpdateInfo.addActionListener(this);
        cancelButtoninUpdateInfo = new RoundedCornerButton("Cancel", "Cancel");
        cancelButtoninUpdateInfo.addActionListener(this);
        buttonsPanel.add(saveButtoninUpdateInfo);
        buttonsPanel.add(cancelButtoninUpdateInfo);
        holder.add(buttonsPanel, con);
        con.gridy++;
        /*     con.anchor = GridBagConstraints.WEST;
         JPanel blankPanel1 = new JPanel();
         blankPanel1.setOpaque(false);
         blankPanel1.setPreferredSize(new Dimension(500, 5));
         blankPanel1.setBorder(new MatteBorder(0, 0, 1, 0, DEFAULT_BOOK_BORDER_COLOR));
         holder.add(blankPanel1, con);
         con.gridy++;
         holder.revalidate();
         holder.repaint();*/
    }

    public void setVariables(Long id) {
        this.skillInfoID = id;
        this.skill = skillField.getText();
        this.description = descArea.getText();
        setName(id.toString());
    }

    public void showUpdatedInfo() {
        mainPane.removeAll();
        holder.removeAll();
        holder.setBorder(null);

        JPanel arrowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 1));
        arrowPanel.setOpaque(false);
        arrowPanel.setPreferredSize(new Dimension(31, 25));
//        actionPopup = new JPopupMenu();
        actionPopUpButton = DesignClasses.createImageButton(GetImages.PEN, GetImages.PEN, "Options");
        arrowPanel.add(actionPopUpButton);

        editDeletePopup = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_EDIT_DELETE);
        editDeletePopup.addMenu(MNU_EDIT, GetImages.EDIT_PHOTO);
        editDeletePopup.addMenu(MNU_DELETE, GetImages.DELETE_PHOTO);

        actionPopUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editDeletePopup.setVisible(actionPopUpButton, 128, -6);
//                actionPopup.show(actionPopUpButton, actionPopup.getX() - 80, actionPopup.getY() + 30);
            }
        });
//        deleteInfo = new JMenuItem("Delete", DesignClasses.return_image(GetImages.DELETE_PHOTO));
//        deleteInfo.addActionListener(this);
//        editInfo = new JMenuItem("Edit", DesignClasses.return_image(GetImages.EDIT_PHOTO));
//        actionPopup.add(editInfo);
//        actionPopup.add(deleteInfo);
//        editInfo.addActionListener(this);
        skillShowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        skillShowPanel.setOpaque(false);
        skillShowPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        skillShowPanel.setPreferredSize(new Dimension(495, 22));
        skillLabel = DesignClasses.makeJLabelFullName2(skill, 13);
        skillShowPanel.add(skillLabel);

        descriptionShowPanel = new JPanel(new BorderLayout());
        descriptionShowPanel.setBorder(new EmptyBorder(2, 10, 0, 0));
        descriptionShowPanel.setOpaque(false);
        descriptionShowPanel.add(new TextPanelWithEmoticon(245, description), BorderLayout.CENTER);

        con.gridx = 0;
        con.gridy = 0;
        holder.add(skillShowPanel, con);
        con.gridy++;
        con.anchor = GridBagConstraints.WEST;
        holder.add(descriptionShowPanel, con);
        con.gridy++;
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        iconPanel.setBorder(new EmptyBorder(2, 0, 0, 0));
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(43, 43));
        iconPanel.add(new JLabel(DesignClasses.return_image(GetImages.SKILL_ICON)));

        mainPane.add(iconPanel, BorderLayout.WEST);
        mainPane.add(holder, BorderLayout.CENTER);
        mainPane.add(arrowPanel, BorderLayout.EAST);
        mainPane.revalidate();
        mainPane.repaint();
    }

    public boolean sameValues() {
        return (skillField.getText().equals(skill) && descArea.getText().equals(description));
    }
    MouseListener menuListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            if (menu.text.equalsIgnoreCase(MNU_EDIT)) {
                editDeletePopup.hideThis();
                editSkillSinglePanel();
            } else if (menu.text.equalsIgnoreCase(MNU_DELETE)) {

                editDeletePopup.hideThis();
                HelperMethods.showConfirmationDialogMessage("Are you sure to remove this Skill information?");
                if (JOptionPanelBasics.YES_NO) {
                    new DeleteSkillInfo(skillInfoID, errorLabel).start();
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

    @Override
    public void actionPerformed(ActionEvent e) {  //new UpdateAddedSkillInfo(instance).start();
        if (e.getSource() == saveButton) {
            if (skillField.getText().trim().length() < 1 || descArea.getText().trim().length() < 1) {
                errorLabel.setText("Please fill out all the fields");
            } else {
                errorLabel.setText("");
                GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().skillNew.setVisible(false);
                new AddSkillInfo(instance).start();
            }
        } else if (e.getSource() == saveButtoninUpdateInfo) {
            if (sameValues()) {
                errorLabel.setText("No change");
            } else {
                if (skillField.getText().trim().length() < 1 || descArea.getText().trim().length() < 1) {
                    errorLabel.setText("Please fill out all the fields");
                } else {
                    errorLabel.setText("");
                    new UpdateAddedSkillInfo(instance).start();
                }
            }
        } else if (e.getSource() == cancelButton) {
            GuiRingID.getInstance().getMainRight().getMyProfilePanel().getMyProfileAbout().skillNew.setVisible(false);
        } else if (e.getSource() == cancelButtoninUpdateInfo) {
            showUpdatedInfo();
        }
//        else if (e.getSource() == editInfo) {
//            editSkillSinglePanel();
//        } else if (e.getSource() == deleteInfo) {
//            HelperMethods.showConfirmationDialogMessage("Are you sure to remove this skill information?");
//            if (JOptionPanelBasics.YES_NO) {
//                new DeleteSkillInfo(skillInfoID, errorLabel).start();
//            }
//        }
    }
}
