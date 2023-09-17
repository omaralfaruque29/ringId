/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

import com.ipvision.view.utility.DatePanel;
import com.ipvision.constants.MyFnFSettings;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.service.DeleteWorkInfo;
import com.ipvision.service.aboutme.UpdateWorkInfo;
import com.ipvision.view.utility.JCustomMenuPopup;
import com.ipvision.view.utility.JOptionPanelBasics;
import com.ipvision.view.utility.RoundedCornerButton;
import com.ipvision.view.utility.TextPanelWithEmoticon;

/**
 *
 * @author Sirat Samyoun
 */
public class WorkSinglePanel extends JPanel implements ActionListener {

    public JTextField positionField;
    public JTextField companyField;
    public JTextField cityField;
    public JTextArea descArea;
    public JTextField fromTimeField;
    public JTextField toTimeField;
    public JTextField updateTimeField;
    public JButton saveButton;
    public JButton cancelButton, deleteButton;//, cancelButton2;
    public GridBagConstraints con;
    public JLabel errorLabel;//errorPosition,errorCompany,errorCity,errorTime;
    public DatePanel datePanel1, datePanel2;
    private String company, position, city, description;
    private long fromTime, toTime;
    private JLabel positionLabel, durationLabel, companyCityLabel, currnt;
    private JPanel companyCityShowPanel, positionDurationShowPanel, descriptionShowPanel, toTimePanel, holder, wrapperPnl;
    public JPanel mainPane;
    private Date fromDate, toDate;
    public Long workInfoID;
    public JCheckBox currentlyWorking;
    private JButton actionPopUpButton;
//    private JPopupMenu actionPopup;
//    private JMenuItem deleteInfo, editInfo;
    JCustomMenuPopup editDeletePopup = null;
    public static String MNU_EDIT = "Edit";
    public static String MNU_DELETE = "Delete";
    private WorkSinglePanel instance;
    public boolean isCurrentWork;
    public Long userTableID;

    public WorkSinglePanel getInstance() {
        return instance;
    }

    public WorkSinglePanel(String company, String position, String city, String description, long fromTime, long toTime, Long workInfoID, Long userTableID) {
        this.instance = this;
        this.company = company;
        this.description = description;
        this.position = position;
        this.city = city;
        this.fromTime = fromTime;
        this.toTime = toTime;
        fromDate = new Date(fromTime);
        toDate = new Date(toTime);
        this.workInfoID = workInfoID;
        this.userTableID = userTableID;
        setName(workInfoID.toString());
        //setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        wrapperPnl = new JPanel(new BorderLayout());
        wrapperPnl.setBackground(Color.WHITE);
        wrapperPnl.setBorder(new EmptyBorder(5, 26, 0, 6));

        holder = new JPanel(new GridBagLayout());
        holder.setOpaque(false);
        mainPane = new JPanel(new BorderLayout());
        this.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        mainPane.setOpaque(false);
        wrapperPnl.add(mainPane, BorderLayout.CENTER);
        add(wrapperPnl, BorderLayout.CENTER);
        wrapperPnl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                wrapperPnl.setBackground(RingColorCode.SEARCH_PANEL_COLOR);
                // if(arrowPanel!=null) arrowPanel.setVisible(true); 
            }

            @Override
            public void mouseExited(MouseEvent e) {
                wrapperPnl.setBackground(Color.WHITE);
                // if(arrowPanel!=null) arrowPanel.setVisible(false); 
            }
        });
        con = new GridBagConstraints();
        editDeletePopup = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_EDIT_DELETE);
        editDeletePopup.addMenu(MNU_EDIT, GetImages.EDIT_PHOTO);
        editDeletePopup.addMenu(MNU_DELETE, GetImages.DELETE_PHOTO);
        init();
    }

    public void init() {
        JPanel arrowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 1));
        arrowPanel.setOpaque(false);
        arrowPanel.setPreferredSize(new Dimension(31, 25));
        if (userTableID.equals(MyFnFSettings.LOGIN_USER_TABLE_ID)) {
//            actionPopup = new JPopupMenu();
            actionPopUpButton = DesignClasses.createImageButton(GetImages.PEN, GetImages.PEN, "Options");

            arrowPanel.add(actionPopUpButton);
            actionPopUpButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editDeletePopup.setVisible(actionPopUpButton, 128, -6);
//                    actionPopup.show(actionPopUpButton, actionPopup.getX() - 80, actionPopup.getY() + 30);
                }
            });
//            deleteInfo = new JMenuItem("Delete", DesignClasses.return_image(GetImages.CLOSE_MINI));
//            deleteInfo.addActionListener(this);
//            editInfo = new JMenuItem("Edit", DesignClasses.return_image(GetImages.ADD_PHOTO));
//            actionPopup.add(editInfo);
//            actionPopup.add(deleteInfo);
//            editInfo.addActionListener(this);
        }
        companyCityShowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        companyCityShowPanel.setOpaque(false);
        companyCityShowPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        companyCityShowPanel.setPreferredSize(new Dimension(495, 22));
        companyCityLabel = DesignClasses.makeJLabelFullName2(company + ", " + city, 13);
        companyCityShowPanel.add(companyCityLabel);

        positionDurationShowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        positionDurationShowPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        positionDurationShowPanel.setOpaque(false);
        positionDurationShowPanel.setPreferredSize(new Dimension(495, 17));
        positionLabel = DesignClasses.makeJLabel_normal(position + " . ", 12, DefaultSettings.text_color2);
        positionDurationShowPanel.add(positionLabel);
        String fromDateasString = new SimpleDateFormat("MMMM d, yyyy").format(new Date(fromTime));
        String toDateasString;
        if (toTime == 0) {
            toDateasString = "Present";
        } else {
            toDateasString = new SimpleDateFormat("MMMM d, yyyy").format(new Date(toTime));
        }
        durationLabel = DesignClasses.makeJLabel_normal(fromDateasString + " to " + toDateasString, 12, DefaultSettings.text_color2);
        positionDurationShowPanel.add(durationLabel);

        descriptionShowPanel = new JPanel(new BorderLayout());
        descriptionShowPanel.setBorder(new EmptyBorder(2, 10, 0, 0));
        descriptionShowPanel.setOpaque(false);
        descriptionShowPanel.add(new TextPanelWithEmoticon(245, description), BorderLayout.CENTER);

        con.gridx = 0;
        con.gridy = 0;
        holder.add(companyCityShowPanel, con);
        con.gridy++;
        holder.add(positionDurationShowPanel, con);
        con.gridy++;
        con.anchor = GridBagConstraints.WEST;
        holder.add(descriptionShowPanel, con);
        con.gridy++;
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        iconPanel.setBorder(new EmptyBorder(2, 0, 0, 0));
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(43, 43));
        iconPanel.add(new JLabel(DesignClasses.return_image(GetImages.WORK_ICON)));

        mainPane.add(iconPanel, BorderLayout.WEST);
        mainPane.add(holder, BorderLayout.CENTER);
        mainPane.add(arrowPanel, BorderLayout.EAST);
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
        actionPopUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editDeletePopup.setVisible(actionPopUpButton, 128, -6);
//                actionPopup.show(actionPopUpButton, actionPopup.getX() - 80, actionPopup.getY() + 30);
            }
        });
//        deleteInfo = new JMenuItem("Delete", DesignClasses.return_image(GetImages.CLOSE_MINI));
//        deleteInfo.addActionListener(this);
//        editInfo = new JMenuItem("Edit", DesignClasses.return_image(GetImages.ADD_PHOTO));
//        actionPopup.add(editInfo);
//        actionPopup.add(deleteInfo);
//        editInfo.addActionListener(this);

        companyCityShowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        companyCityShowPanel.setOpaque(false);
        companyCityShowPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        companyCityShowPanel.setPreferredSize(new Dimension(495, 22));
        companyCityLabel = DesignClasses.makeJLabelFullName2(company + ", " + city, 13);
        companyCityShowPanel.add(companyCityLabel);

        positionDurationShowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        positionDurationShowPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        positionDurationShowPanel.setOpaque(false);
        positionDurationShowPanel.setPreferredSize(new Dimension(495, 17));
        positionLabel = DesignClasses.makeJLabel_normal(position + " . ", 12, DefaultSettings.text_color2);
        positionDurationShowPanel.add(positionLabel);
        String fromDateasString = new SimpleDateFormat("MMMM d, yyyy").format(new Date(fromTime));
        String toDateasString;
        if (toTime == 0) {
            toDateasString = "Present";
        } else {
            toDateasString = new SimpleDateFormat("MMMM d, yyyy").format(new Date(toTime));
        }
        durationLabel = DesignClasses.makeJLabel_normal(fromDateasString + " to " + toDateasString, 12, DefaultSettings.text_color2);
        positionDurationShowPanel.add(durationLabel);

        descriptionShowPanel = new JPanel(new BorderLayout());
        descriptionShowPanel.setBorder(new EmptyBorder(2, 10, 0, 0));
        descriptionShowPanel.setOpaque(false);
        descriptionShowPanel.add(new TextPanelWithEmoticon(245, description), BorderLayout.CENTER);

        con.gridx = 0;
        con.gridy = 0;
        holder.add(companyCityShowPanel, con);
        con.gridy++;
        holder.add(positionDurationShowPanel, con);
        con.gridy++;
        con.anchor = GridBagConstraints.WEST;
        holder.add(descriptionShowPanel, con);
        con.gridy++;
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        iconPanel.setBorder(new EmptyBorder(2, 0, 0, 0));
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(43, 43));
        iconPanel.add(new JLabel(DesignClasses.return_image(GetImages.WORK_ICON)));

        mainPane.add(iconPanel, BorderLayout.WEST);
        mainPane.add(holder, BorderLayout.CENTER);
        mainPane.add(arrowPanel, BorderLayout.EAST);
        mainPane.revalidate();
        mainPane.repaint();
    }

    public void editWorkSinglePanel() {
        mainPane.removeAll();
        holder.removeAll();
        mainPane.add(holder, BorderLayout.CENTER);

        holder.setBorder(new EmptyBorder(10, 0, 10, 0));
        con.gridx = 0;
        con.gridy = 0;
        con.anchor = GridBagConstraints.WEST;
        JPanel companyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        companyPanel.setOpaque(false);
        companyPanel.setPreferredSize(new Dimension(520, 30));
        companyPanel.add(Box.createRigidArea(new Dimension(80, 5)));
        JPanel companyNamePanel = new JPanel(new BorderLayout());
        companyNamePanel.setOpaque(false);
        companyNamePanel.setPreferredSize(new Dimension(100, 25));
        companyNamePanel.add(DesignClasses.makeJLabel_normal("Company    ", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        companyPanel.add(companyNamePanel);
        companyField = DesignClasses.makeTextFieldLimitedTextSize(company, 200, 20, 200);
        companyPanel.add(companyField);
        holder.add(companyPanel, con);
        con.gridy++;

        JPanel positionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        positionPanel.setOpaque(false);
        positionPanel.setPreferredSize(new Dimension(520, 30));
        positionPanel.add(Box.createRigidArea(new Dimension(80, 5)));
        JPanel positionNamePanel = new JPanel(new BorderLayout());
        positionNamePanel.setOpaque(false);
        positionNamePanel.setPreferredSize(new Dimension(100, 25));
        positionNamePanel.add(DesignClasses.makeJLabel_normal("Position    ", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        positionPanel.add(positionNamePanel);
        positionField = DesignClasses.makeTextFieldLimitedTextSize(position, 200, 20, 200);
        positionPanel.add(positionField);
        holder.add(positionPanel, con);
        con.gridy++;

        JPanel cityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        cityPanel.setOpaque(false);
        cityPanel.setPreferredSize(new Dimension(520, 30));
        cityPanel.add(Box.createRigidArea(new Dimension(80, 5)));
        JPanel cityNamePanel = new JPanel(new BorderLayout());
        cityNamePanel.setOpaque(false);
        cityNamePanel.setPreferredSize(new Dimension(100, 25));
        cityNamePanel.add(DesignClasses.makeJLabel_normal("City    ", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        cityPanel.add(cityNamePanel);
        cityField = DesignClasses.makeTextFieldLimitedTextSize(city, 200, 20, 200);
        cityPanel.add(cityField);
        holder.add(cityPanel, con);
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

        JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        checkBoxPanel.setOpaque(false);
        checkBoxPanel.setPreferredSize(new Dimension(520, 30));
        checkBoxPanel.add(Box.createRigidArea(new Dimension(80, 5)));
        JPanel checkLabelPanel = new JPanel(new BorderLayout());
        checkLabelPanel.setOpaque(false);
        checkLabelPanel.setPreferredSize(new Dimension(100, 25));
        checkLabelPanel.add(DesignClasses.makeJLabel_normal("Time Period", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        checkBoxPanel.add(checkLabelPanel);
        currentlyWorking = new JCheckBox(); //DesignClasses.makeTextFieldLimitedTextSize("", 200, 20, 200);
        currentlyWorking.setOpaque(false);
        currentlyWorking.setSelected(true);
        currentlyWorking.addActionListener(this);

        checkBoxPanel.add(currentlyWorking);
        checkBoxPanel.add(DesignClasses.makeJLabel_normal("Currently working here", 12, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        holder.add(checkBoxPanel, con);
        con.gridy++;

        holder.add(Box.createRigidArea(new Dimension(80, 3)), con);
        con.gridy++;

        JPanel fromTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        fromTimePanel.setOpaque(false);
        fromTimePanel.setPreferredSize(new Dimension(520, 30));
        fromTimePanel.add(Box.createRigidArea(new Dimension(80, 5)));
        JPanel fromTimeNamePanel = new JPanel(new BorderLayout());
        fromTimeNamePanel.setOpaque(false);
        fromTimeNamePanel.setPreferredSize(new Dimension(100, 25));
        //fromTimeNamePanel.add(DesignClasses.makeJLabel_normal("From", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        fromTimePanel.add(fromTimeNamePanel);
        int yr = 1970, mn = 1, day = 1;
        if (MyFnFSettings.userProfile != null && MyFnFSettings.userProfile.getBirthday() != null && !MyFnFSettings.userProfile.getBirthday().equals("")) {
            String birthdate = MyFnFSettings.userProfile.getBirthday();
            String[] parts = birthdate.split("-");
            yr = Integer.parseInt(parts[0]);
            mn = Integer.parseInt(parts[1]);
            day = Integer.parseInt(parts[2]);
        }
        Date selectedfromDate = new Date(fromTime);
        String strDate = new SimpleDateFormat("yyyy-MM-dd").format(selectedfromDate);
        String[] parts = strDate.split("-");
        datePanel1 = new DatePanel(yr, mn, day, Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        fromTimePanel.add(datePanel1);
        fromTimePanel.add(DesignClasses.makeJLabel_normal("to", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        currnt = DesignClasses.makeJLabel_normal("Present", 14, DefaultSettings.BLACK_FONT);
        fromTimePanel.add(currnt, BorderLayout.CENTER);
        currnt.setVisible(true);
        holder.add(fromTimePanel, con);
        con.gridy++;

        toTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        toTimePanel.setOpaque(false);
        toTimePanel.setPreferredSize(new Dimension(520, 30));
        toTimePanel.add(Box.createRigidArea(new Dimension(80, 5)));
        JPanel toTimeNamePanel = new JPanel(new BorderLayout());
        toTimeNamePanel.setOpaque(false);
        toTimeNamePanel.setPreferredSize(new Dimension(100, 25));
        toTimePanel.add(toTimeNamePanel);
        Date selectedtoDate = new Date(toTime);
        strDate = new SimpleDateFormat("yyyy-MM-dd").format(selectedtoDate);
        parts = strDate.split("-");
        datePanel2 = new DatePanel(yr, mn, day, Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));

        toTimePanel.add(datePanel2);
        toTimePanel.setVisible(false);
        holder.add(toTimePanel, con);
        con.gridy++;

        if (toTime == 0) {
            currentlyWorking.setSelected(true);
            currnt.setVisible(true);
            toTimePanel.setVisible(false);
        } else {
            currentlyWorking.setSelected(false);
            currnt.setVisible(false);
            toTimePanel.setVisible(true);
        }
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
        mainPane.revalidate();
        mainPane.repaint();
    }

    public void setVariables(Long id) {
        this.workInfoID = id;
        this.company = companyField.getText();
        this.position = positionField.getText();
        this.city = cityField.getText();
        this.description = descArea.getText();
        this.fromTime = datePanel1.getDateinMillisecs();//Integer.parseInt(fromTimeField.getText());
        if (currentlyWorking.isSelected()) {
            this.toTime = 0;
        } else {
            this.toTime = datePanel2.getDateinMillisecs();//tt;
        }
        setName(id.toString());
    }

    public boolean sameValues() {
        return (companyField.getText().equals(company) && descArea.getText().equals(description)
                && positionField.getText().equals(position) && cityField.getText().equals(city)
                && ((currentlyWorking.isSelected() && toTime == 0L) || (!currentlyWorking.isSelected() && toTime == datePanel2.getDateinMillisecs())));
    }
    MouseListener menuListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            if (menu.text.equalsIgnoreCase(MNU_EDIT)) {
                editDeletePopup.hideThis();
                editWorkSinglePanel();
            } else if (menu.text.equalsIgnoreCase(MNU_DELETE)) {

                editDeletePopup.hideThis();
                HelperMethods.showConfirmationDialogMessage("Are you sure to remove this work information?");
                if (JOptionPanelBasics.YES_NO) {
                    new DeleteWorkInfo(workInfoID, errorLabel).start();
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
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == currentlyWorking) {
            if (!currentlyWorking.isSelected()) {
                currnt.setVisible(false);
                toTimePanel.setVisible(true);
            } else {
                currnt.setVisible(true);
                toTimePanel.setVisible(false);
            }
        } else if (e.getSource() == saveButton) {
            isCurrentWork = currentlyWorking.isSelected();
            if (sameValues()) {
                errorLabel.setText("No change");
            } else {
                if (companyField.getText().trim().length() < 1 || cityField.getText().trim().length() < 1 || positionField.getText().trim().length() < 1) {
                    errorLabel.setText("Please fill out the company,city and position fields");
                } else {
                    if (HelperMethods.hasDigit(cityField.getText()) || HelperMethods.hasDigit(positionField.getText())) {
                        errorLabel.setText("City or Position cannot contain digits");
                    } else {
                        if (!isCurrentWork && datePanel1.getDateinMillisecs() >= datePanel2.getDateinMillisecs()) {
                            errorLabel.setText("Starting date must be before Ending date");
                        } else {
                            errorLabel.setText("");
                            new UpdateWorkInfo(instance).start();
                        }
                    }
                }
            }
        } else if (e.getSource() == cancelButton) {
            showUpdatedInfo();
        }
//        else if (e.getSource() == editInfo) {
//            editWorkSinglePanel();
//        } else if (e.getSource() == deleteInfo) {
//            HelperMethods.showConfirmationDialogMessage("Are you sure to remove this work information?");
//            if (JOptionPanelBasics.YES_NO) {
//                new DeleteWorkInfo(workInfoID, errorLabel).start();
//            }
//        }

    }
}
