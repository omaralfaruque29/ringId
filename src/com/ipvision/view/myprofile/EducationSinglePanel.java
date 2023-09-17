/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.service.DeleteEducationInfo;
import com.ipvision.service.aboutme.UpdateEducationInfo;
import com.ipvision.view.utility.DatePanel;
import com.ipvision.view.utility.JCustomMenuPopup;
import com.ipvision.view.utility.JOptionPanelBasics;
import com.ipvision.view.utility.RoundedCornerButton;
import com.ipvision.view.utility.TextPanelWithEmoticon;

/**
 *
 * @author Sirat Samyoun
 */
public class EducationSinglePanel extends JPanel implements ActionListener {

    private EducationSinglePanel instance;
    private String institution, description, concentration, degree;
    public Long fromTime, toTime, educationInfoID, userTableID;
    private Integer attendedFor;
    public boolean isGraduated, isSchool, isCurr;
    private JPanel holder, wrapperPnl, content, toTimePanel, fromTimePanel, attendedForButtonsPanel, degreePanel;
    public JPanel mainPane, arrowPanel;
    private GridBagConstraints con, c1;
    public JRadioButton attendedClgRadioButton, attendedGradSchlRadioButton;  //isSchoolRadioButton, isClgRadioButton,
    public JTextField institutionTextField, concentrationTextField, degreeTextField;
    public JTextArea descArea;
    private JLabel currnt;
    public JLabel errorLabel;
    public JCheckBox currentlyStudying, graduated;
    private ButtonGroup group2;
    public DatePanel datePanel1, datePanel2;
    public JButton saveButton, cancelButton;
    private JButton actionPopUpButton;
//    private JPopupMenu actionPopup;
//    private JMenuItem deleteInfo, editInfo;
    JCustomMenuPopup editDeletePopup = null;
    public static String MNU_EDIT = "Edit";
    public static String MNU_DELETE = "Delete";

    public EducationSinglePanel getInstance() {
        return instance;
    }

    public EducationSinglePanel(String institution, String description, Long fromTime, Long toTime, Integer attendedFor, boolean isGraduated, boolean isSchool, String concentration, String degree, Long educationID, Long userTableID) {
        this.instance = this;
        this.institution = institution;
        this.description = description;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.attendedFor = attendedFor;
        this.isGraduated = isGraduated;
        this.isSchool = isSchool;
        this.concentration = concentration;
        this.educationInfoID = educationID;
        this.userTableID = userTableID;
        this.degree = degree;
        setName(educationID.toString());
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
        add(wrapperPnl, BorderLayout.CENTER);
        con = new GridBagConstraints();
        editDeletePopup = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_EDIT_DELETE);
        editDeletePopup.addMenu(MNU_EDIT, GetImages.EDIT_PHOTO);
        editDeletePopup.addMenu(MNU_DELETE, GetImages.DELETE_PHOTO);
        init();
    }

    public void init() {
        arrowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 1));
        arrowPanel.setOpaque(false);
        arrowPanel.setPreferredSize(new Dimension(31, 25));
        if (userTableID.equals(MyFnFSettings.LOGIN_USER_TABLE_ID)) {
//            actionPopup = new JPopupMenu();
            actionPopUpButton = DesignClasses.createImageButton(GetImages.PEN, GetImages.PEN, "Options");
            arrowPanel.add(actionPopUpButton);
            //arrowPanel.setVisible(false); 
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
        JPanel institutionShowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        institutionShowPanel.setOpaque(false);
        institutionShowPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        institutionShowPanel.setPreferredSize(new Dimension(495, 22));
        JLabel institutionLabel = DesignClasses.makeJLabelFullName2(institution, 13);//new JLabel(company + ", " + city);
        institutionShowPanel.add(institutionLabel);

        JPanel durationShowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        durationShowPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        durationShowPanel.setOpaque(false);
        durationShowPanel.setPreferredSize(new Dimension(495, 17));
        String fromDateasString = new SimpleDateFormat("MMMM d, yyyy").format(new Date(fromTime));
        String toDateasString;
        if (toTime == 0) {
            toDateasString = "Present";
            isCurr = true;
        } else {
            toDateasString = new SimpleDateFormat("MMMM d, yyyy").format(new Date(toTime));
            isCurr = false;
        }

        JLabel durationLabel = DesignClasses.makeJLabel_normal(fromDateasString + " to " + toDateasString, 12, DefaultSettings.text_color2);
        durationShowPanel.add(durationLabel);

//        if (!isSchool && !concentration.equals("")) {
//            durationShowPanel.add(DesignClasses.makeJLabel_normal(" . " + concentration, 12, DefaultSettings.text_color2));
//        }
//        if (isGraduated) {
//            durationShowPanel.add(DesignClasses.makeJLabel_normal(" . Graduated", 12, DefaultSettings.text_color2));
//        }
        ////////////////////////////
        JPanel degConcGradShowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        degConcGradShowPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        degConcGradShowPanel.setOpaque(false);
        degConcGradShowPanel.setPreferredSize(new Dimension(495, 17));
        String str1 = "", str2 = "", str3 = "", str;
        if (isGraduated) {
            str3 = " . Graduated";
        }
        if (!isSchool) {   //clg
            if (attendedFor.intValue() == 2) {  //gr schl
                if (degree != null) {
                    str1 = degree;
                }
            }
            str2 = concentration;
        }
        if (str1.trim().length() > 0 && str2.trim().length() > 0) {
            str = str1.trim() + " in " + str2.trim() + str3;
        } else {
            str = str1.trim() + str2.trim() + str3;
        }
        if (str1.trim().length() == 0 && str2.trim().length() == 0) {
            str = "Graduated";
        }
        JLabel degConcGradLabel = DesignClasses.makeJLabel_normal(str, 12, DefaultSettings.text_color2);
        degConcGradShowPanel.add(degConcGradLabel);

//////////////////
        JPanel descriptionShowPanel = new JPanel(new BorderLayout());
        descriptionShowPanel.setBorder(new EmptyBorder(2, 10, 0, 0));
        descriptionShowPanel.setOpaque(false);
        descriptionShowPanel.add(new TextPanelWithEmoticon(245, description), BorderLayout.CENTER);

        con.gridx = 0;
        con.gridy = 0;
        holder.add(institutionShowPanel, con);
        con.gridy++;
        holder.add(durationShowPanel, con);
        con.gridy++;
        if (str.trim().length() > 0) {
            holder.add(degConcGradShowPanel, con);
            con.gridy++;
        }
        con.anchor = GridBagConstraints.WEST;
        holder.add(descriptionShowPanel, con);
        con.gridy++;
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        iconPanel.setBorder(new EmptyBorder(2, 0, 0, 0));
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(43, 43));
        iconPanel.add(new JLabel(DesignClasses.return_image(GetImages.EDUCATION_ICON)));

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
        JPanel institutionShowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        institutionShowPanel.setOpaque(false);
        institutionShowPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        institutionShowPanel.setPreferredSize(new Dimension(495, 22));
        JLabel institutionLabel = DesignClasses.makeJLabelFullName2(institution, 13);//new JLabel(company + ", " + city);
        institutionShowPanel.add(institutionLabel);

        JPanel durationShowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        durationShowPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        durationShowPanel.setOpaque(false);
        durationShowPanel.setPreferredSize(new Dimension(495, 17));
        String fromDateasString = new SimpleDateFormat("MMMM d, yyyy").format(new Date(fromTime));
        String toDateasString;
        if (toTime == 0) {
            toDateasString = "Present";
            isCurr = true;
        } else {
            toDateasString = new SimpleDateFormat("MMMM d, yyyy").format(new Date(toTime));
            isCurr = false;
        }

        JLabel durationLabel = DesignClasses.makeJLabel_normal(fromDateasString + " to " + toDateasString, 12, DefaultSettings.text_color2);
        durationShowPanel.add(durationLabel);

//        if (!isSchool && !concentration.equals("")) {
//            durationShowPanel.add(DesignClasses.makeJLabel_normal(" . " + concentration, 12, DefaultSettings.text_color2));
//        }
//        if (isGraduated) {
//            durationShowPanel.add(DesignClasses.makeJLabel_normal(" . Graduated", 12, DefaultSettings.text_color2));
//        }
        ////////////////////////////
        JPanel degConcGradShowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        degConcGradShowPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        degConcGradShowPanel.setOpaque(false);
        degConcGradShowPanel.setPreferredSize(new Dimension(495, 17));
        String str1 = "", str2 = "", str3 = "", str;
        if (isGraduated) {
            str3 = " . Graduated";
        }
        if (!isSchool) {   //clg
            if (attendedFor.intValue() == 2) {  //gr schl
                if (degree != null) {
                    str1 = degree;
                }
            }
            str2 = concentration;
        }
        if (str1.trim().length() > 0 && str2.trim().length() > 0) {
            str = str1.trim() + " in " + str2.trim() + str3;
        } else {
            str = str1.trim() + str2.trim() + str3;
        }
        if (str1.trim().length() == 0 && str2.trim().length() == 0) {
            str = "Graduated";
        }
        JLabel degConcGradLabel = DesignClasses.makeJLabel_normal(str, 12, DefaultSettings.text_color2);
        degConcGradShowPanel.add(degConcGradLabel);

//////////////////
        JPanel descriptionShowPanel = new JPanel(new BorderLayout());
        descriptionShowPanel.setBorder(new EmptyBorder(2, 10, 0, 0));
        descriptionShowPanel.setOpaque(false);
        descriptionShowPanel.add(new TextPanelWithEmoticon(245, description), BorderLayout.CENTER);

        con.gridx = 0;
        con.gridy = 0;
        holder.add(institutionShowPanel, con);
        con.gridy++;
        holder.add(durationShowPanel, con);
        con.gridy++;
        if (str.trim().length() > 0) {
            holder.add(degConcGradShowPanel, con);
            con.gridy++;
        }
        con.anchor = GridBagConstraints.WEST;
        holder.add(descriptionShowPanel, con);
        con.gridy++;
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        iconPanel.setBorder(new EmptyBorder(2, 0, 0, 0));
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(43, 43));
        iconPanel.add(new JLabel(DesignClasses.return_image(GetImages.EDUCATION_ICON)));

        mainPane.add(iconPanel, BorderLayout.WEST);
        mainPane.add(holder, BorderLayout.CENTER);
        mainPane.add(arrowPanel, BorderLayout.EAST);
        mainPane.revalidate();
        mainPane.repaint();
    }

    public void setVariables(Long educationID) {
        this.educationInfoID = educationID;
        setName(educationID.toString());
        this.institution = institutionTextField.getText();
        this.isGraduated = graduated.isSelected();
        this.description = descArea.getText();
        this.fromTime = datePanel1.getDateinMillisecs();//Integer.parseInt(fromTimeField.getText());
        if (currentlyStudying.isSelected()) {
            this.toTime = 0L;
        } else {
            this.toTime = datePanel2.getDateinMillisecs();//tt;
        }
        if (!isSchool) {
            this.concentration = concentrationTextField.getText();
            if (attendedClgRadioButton.isSelected()) {
                this.attendedFor = 1;
            } else {
                this.attendedFor = 2;
                this.degree = degreeTextField.getText();
            }
        }
    }

    public void editEducationSinglePanel() {
        mainPane.removeAll();
        holder.removeAll();
        mainPane.add(holder, BorderLayout.CENTER);
        mainPane.revalidate();
        mainPane.repaint();

        holder.setBorder(new EmptyBorder(10, 0, 10, 0));
        con.gridx = 0;
        con.gridy = 0;

        content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        holder.add(content, con);
        con.gridy++;

        c1 = new GridBagConstraints();
        c1.anchor = GridBagConstraints.WEST;

        if (isSchool) {
            schoolUI(institution, isCurr, isGraduated, description, fromTime, toTime, true);
        } else {
            boolean clg = true;
            if (attendedFor == 2) {
                clg = false;
            }
            clgUI(institution, isCurr, isGraduated, description, fromTime, toTime, concentration, clg, true);                    //concentrationTextField.getText(), attendedClgRadioButton.isSelected(), true);
        }
    }

    public boolean sameValues() {
        return (institutionTextField.getText().equals(institution) && descArea.getText().equals(description) && isGraduated == graduated.isSelected() && fromTime == datePanel1.getDateinMillisecs()
                && ((currentlyStudying.isSelected() && toTime == 0L) || (!currentlyStudying.isSelected() && toTime == datePanel2.getDateinMillisecs()))
                && (isSchool || (!isSchool && (concentrationTextField.getText().equals(concentration) && ((attendedClgRadioButton.isSelected() && attendedFor == 1) || (!attendedClgRadioButton.isSelected() && attendedFor == 2 && degreeTextField.getText().equals(degree)))))));
    }

    public void commonEducationInfo(String ins, boolean curr, boolean grad, String desc, Long selectedfromTime, Long selectedtoTime) {
        //Long tt = datePanel2.getDateinMillisecs();
        JPanel institutionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        institutionPanel.setOpaque(false);
        institutionPanel.setPreferredSize(new Dimension(520, 30));
        institutionPanel.add(Box.createRigidArea(new Dimension(80, 5)));
        JPanel institutionNamePanel = new JPanel(new BorderLayout());
        institutionNamePanel.setOpaque(false);
        institutionNamePanel.setPreferredSize(new Dimension(110, 25));
        institutionNamePanel.add(DesignClasses.makeJLabel_normal("Institution   ", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        institutionPanel.add(institutionNamePanel);
        institutionTextField = DesignClasses.makeTextFieldLimitedTextSize(ins, 200, 20, 200);
        institutionPanel.add(institutionTextField);
        content.add(institutionPanel, c1);
        c1.gridy++;

        JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        checkBoxPanel.setOpaque(false);
        checkBoxPanel.setPreferredSize(new Dimension(520, 30));
        checkBoxPanel.add(Box.createRigidArea(new Dimension(80, 5)));
        JPanel checkLabelPanel = new JPanel(new BorderLayout());
        checkLabelPanel.setOpaque(false);
        checkLabelPanel.setPreferredSize(new Dimension(110, 25));
        checkLabelPanel.add(DesignClasses.makeJLabel_normal("Time Period", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        checkBoxPanel.add(checkLabelPanel);
        currentlyStudying = new JCheckBox(); //DesignClasses.makeTextFieldLimitedTextSize("", 200, 20, 200);
        currentlyStudying.setOpaque(false);
        currentlyStudying.setSelected(curr);
        currentlyStudying.addActionListener(this);
        checkBoxPanel.add(currentlyStudying);
        checkBoxPanel.add(DesignClasses.makeJLabel_normal("Currently studying here", 12, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        content.add(checkBoxPanel, c1);
        c1.gridy++;
        content.add(Box.createRigidArea(new Dimension(80, 3)), c1);
        c1.gridy++;
//////////////////////time needs correcytion
        fromTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        fromTimePanel.setOpaque(false);
        fromTimePanel.setPreferredSize(new Dimension(520, 30));
        fromTimePanel.add(Box.createRigidArea(new Dimension(80, 5)));
        JPanel fromTimeNamePanel = new JPanel(new BorderLayout());
        fromTimeNamePanel.setOpaque(false);
        fromTimeNamePanel.setPreferredSize(new Dimension(110, 25));
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
        if (selectedfromTime == null) {
            datePanel1 = new DatePanel(yr, mn, day);
        } else {
            String strDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(selectedfromTime));
            String[] parts = strDate.split("-");
            datePanel1 = new DatePanel(yr, mn, day, Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        }
        fromTimePanel.add(datePanel1);
        fromTimePanel.add(DesignClasses.makeJLabel_normal("to", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        currnt = DesignClasses.makeJLabel_normal("Present", 14, DefaultSettings.BLACK_FONT);
        fromTimePanel.add(currnt, BorderLayout.CENTER);
        currnt.setVisible(true);
        content.add(fromTimePanel, c1);
        c1.gridy++;

        toTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        toTimePanel.setOpaque(false);
        toTimePanel.setPreferredSize(new Dimension(520, 30));
        toTimePanel.add(Box.createRigidArea(new Dimension(80, 5)));
        JPanel toTimeNamePanel = new JPanel(new BorderLayout());
        toTimeNamePanel.setOpaque(false);
        toTimeNamePanel.setPreferredSize(new Dimension(110, 25));
        toTimePanel.add(toTimeNamePanel);
        if (selectedtoTime == null) {
            datePanel2 = new DatePanel(yr, mn, day);
        } else {
            String strDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(selectedtoTime));
            String[] parts = strDate.split("-");
            datePanel2 = new DatePanel(yr, mn, day, Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));

        }
        toTimePanel.add(datePanel2);
        toTimePanel.setVisible(false);
        content.add(toTimePanel, c1);
        c1.gridy++;
        content.add(Box.createRigidArea(new Dimension(80, 3)), c1);
        c1.gridy++;
        if (curr) {// if (selectedtoTime!=null && selectedtoTime == 0) {
            currentlyStudying.setSelected(true);
            currnt.setVisible(true);
            toTimePanel.setVisible(false);
        } else {
            currentlyStudying.setSelected(false);
            currnt.setVisible(false);
            toTimePanel.setVisible(true);
        }
///////////////////////////////////////time
        JPanel graduatedBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        graduatedBoxPanel.setOpaque(false);
        graduatedBoxPanel.setPreferredSize(new Dimension(520, 30));
        graduatedBoxPanel.add(Box.createRigidArea(new Dimension(80, 5)));
        JPanel graduatedLabelPanel = new JPanel(new BorderLayout());
        graduatedLabelPanel.setOpaque(false);
        graduatedLabelPanel.setPreferredSize(new Dimension(110, 25));
        graduatedLabelPanel.add(DesignClasses.makeJLabel_normal("Graduated", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        graduatedBoxPanel.add(graduatedLabelPanel);
        graduated = new JCheckBox(); //DesignClasses.makeTextFieldLimitedTextSize("", 200, 20, 200);
        graduated.setOpaque(false);
        graduated.setSelected(grad);
        graduated.addActionListener(this);
        graduatedBoxPanel.add(graduated);
        content.add(graduatedBoxPanel, c1);
        c1.gridy++;
        content.add(Box.createRigidArea(new Dimension(80, 3)), c1);
        c1.gridy++;

        JPanel descPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        descPanel.setOpaque(false);
        // descPanel.setPreferredSize(new Dimension(520, 60));
        descPanel.add(Box.createRigidArea(new Dimension(80, 5)));
        JPanel descNamePanel = new JPanel(new BorderLayout());
        descNamePanel.setOpaque(false);
        descNamePanel.setPreferredSize(new Dimension(110, 25));
        descNamePanel.add(DesignClasses.makeJLabel_normal("Description    ", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        descPanel.add(descNamePanel);
        descArea = DesignClasses.createJTextArea(desc, 13, 100);
        // descArea.setPreferredSize(new Dimension(200, 60));
        descArea.setColumns(17);
        descPanel.add(descArea);
        content.add(descPanel, c1);
        c1.gridy++;
        content.add(Box.createRigidArea(new Dimension(80, 3)), c1);
        c1.gridy++;
    }

    public void addDegree(String dgr) {
        degreePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        degreePanel.setOpaque(false);
        degreePanel.setPreferredSize(new Dimension(520, 30));
        degreePanel.add(Box.createRigidArea(new Dimension(80, 5)));
        JPanel degreeNamePanel = new JPanel(new BorderLayout());
        degreeNamePanel.setOpaque(false);
        degreeNamePanel.setPreferredSize(new Dimension(110, 25));
        degreeNamePanel.add(DesignClasses.makeJLabel_normal("Degree   ", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        degreePanel.add(degreeNamePanel);
        degreeTextField = DesignClasses.makeTextFieldLimitedTextSize(dgr, 200, 20, 200);
        degreePanel.add(degreeTextField);
        content.add(degreePanel, c1);
        c1.gridy++;
    }

    public void buttonsAdd(boolean ed) {
        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.setPreferredSize(new Dimension(520, 15));
        errorPanel.setOpaque(false);
        errorLabel = DesignClasses.makeJLabel_normal(" ", 10, DefaultSettings.errorLabelColor);//new JLabel();
        errorPanel.add(Box.createRigidArea(new Dimension(200, 5)), BorderLayout.WEST);
        errorPanel.add(errorLabel, BorderLayout.CENTER);
        content.add(errorPanel, c1);
        c1.gridy++;
        content.add(Box.createRigidArea(new Dimension(86, 3)), c1);
        c1.gridy++;
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setPreferredSize(new Dimension(520, 30));
        buttonsPanel.setOpaque(false);
        if (ed) {
            saveButton = new RoundedCornerButton("Save", "Save");
            saveButton.addActionListener(this);
            buttonsPanel.add(saveButton);
            cancelButton = new RoundedCornerButton("Cancel", "Cancel");
            cancelButton.addActionListener(this);
            buttonsPanel.add(cancelButton);
        }
        content.add(buttonsPanel, c1);
        c1.gridy++;
    }

    public void schoolUI(String ins, boolean curr, boolean grad, String desc, Long selectedfromTime, Long selectedtoTime, boolean ed) {
        c1.gridx = 0;
        c1.gridy = 0;
        commonEducationInfo(ins, curr, grad, desc, selectedfromTime, selectedtoTime);
        buttonsAdd(ed);

    }

    public void clgUI(String ins, boolean curr, boolean grad, String desc, Long selectedfromTime, Long selectedtoTime, String cns, boolean gc, boolean ed) {
        c1.gridx = 0;
        c1.gridy = 0;
        commonEducationInfo(ins, curr, grad, desc, selectedfromTime, selectedtoTime);

        JPanel concentrationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        concentrationPanel.setOpaque(false);
        concentrationPanel.setPreferredSize(new Dimension(520, 30));
        concentrationPanel.add(Box.createRigidArea(new Dimension(80, 5)));
        JPanel concentrationNamePanel = new JPanel(new BorderLayout());
        concentrationNamePanel.setOpaque(false);
        concentrationNamePanel.setPreferredSize(new Dimension(110, 25));
        concentrationNamePanel.add(DesignClasses.makeJLabel_normal("Concentrations   ", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        concentrationPanel.add(concentrationNamePanel);
        concentrationTextField = DesignClasses.makeTextFieldLimitedTextSize(cns, 200, 20, 200);
        concentrationPanel.add(concentrationTextField);
        content.add(concentrationPanel, c1);
        c1.gridy++;

        JPanel attendedForPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        attendedForPanel.setOpaque(false);
        attendedForPanel.setPreferredSize(new Dimension(520, 46));
        attendedForPanel.add(Box.createRigidArea(new Dimension(80, 5)));
        JPanel attendedForNamePanel = new JPanel(new BorderLayout());
        attendedForNamePanel.setOpaque(false);
        attendedForNamePanel.setPreferredSize(new Dimension(110, 25));
        attendedForNamePanel.add(DesignClasses.makeJLabel_normal("Attended for   ", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        attendedForPanel.add(attendedForNamePanel);

        attendedForButtonsPanel = new JPanel(new BorderLayout());
        attendedForButtonsPanel.setOpaque(false);
        //attendedForButtonsPanel.setPreferredSize(new Dimension(520, 40));
        //attendedForButtonsPanel.add(Box.createRigidArea(new Dimension(180, 5)));

        attendedClgRadioButton = new JRadioButton("College ");
        attendedClgRadioButton.setOpaque(false);
        if (gc) {
            attendedClgRadioButton.setSelected(true);
        }
        attendedClgRadioButton.addActionListener(this);

        attendedGradSchlRadioButton = new JRadioButton("Graduate School ");
        attendedGradSchlRadioButton.setOpaque(false);
        if (!gc) {
            attendedGradSchlRadioButton.setSelected(true);
        }
        attendedGradSchlRadioButton.addActionListener(this);

        group2 = new ButtonGroup();
        group2.add(attendedClgRadioButton);
        group2.add(attendedGradSchlRadioButton);

        attendedForButtonsPanel.add(attendedClgRadioButton, BorderLayout.NORTH);
        attendedForButtonsPanel.add(attendedGradSchlRadioButton, BorderLayout.CENTER);

        attendedForPanel.add(attendedForButtonsPanel);
        content.add(attendedForPanel, c1);
        c1.gridy++;
        if (!gc) {
            addDegree(degree);
            degreePanel.setVisible(true);
        } else {
            addDegree("");
            degreePanel.setVisible(false);
        }
        buttonsAdd(ed);
    }

    MouseListener menuListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            if (menu.text.equalsIgnoreCase(MNU_EDIT)) {
                editDeletePopup.hideThis();
                editEducationSinglePanel();
            } else if (menu.text.equalsIgnoreCase(MNU_DELETE)) {
                HelperMethods.showConfirmationDialogMessage("Are you sure to remove this Education information?");
                if (JOptionPanelBasics.YES_NO) {
                    editDeletePopup.hideThis();
                    new DeleteEducationInfo(educationInfoID, errorLabel).start();                                  //new FeedDeleteCommentRequest(nfid, cmdid, AppConstants.TYPE_DELETE_STATUS_COMMENT).start();
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
        if (e.getSource() == currentlyStudying) {
            if (!currentlyStudying.isSelected()) {
                currnt.setVisible(false);
                toTimePanel.setVisible(true);
            } else {
                currnt.setVisible(true);
                toTimePanel.setVisible(false);
            }
        } else if (e.getSource() == saveButton) {
            if (sameValues()) {
                errorLabel.setText("No change");
            } else {
                if (institutionTextField.getText().trim().length() < 1) {
                    errorLabel.setText("Please fill out the institution field");
                } else {
                    if (!currentlyStudying.isSelected() && datePanel1.getDateinMillisecs() >= datePanel2.getDateinMillisecs()) {
                        errorLabel.setText("Starting date must be before Ending date");
                    } else {
                        errorLabel.setText("");
                        new UpdateEducationInfo(instance).start();
                    }
                }
            }
        } else if (e.getSource() == cancelButton) {
            showUpdatedInfo();
        } //        else if (e.getSource() == editInfo) {
        //            editEducationSinglePanel();
        //        } 
        //        else if (e.getSource() == deleteInfo) {
        //            HelperMethods.showConfirmationDialogMessage("Are you sure to remove this Education information?");
        //            if (JOptionPanelBasics.YES_NO) {
        //                new DeleteEducationInfo(educationInfoID, errorLabel).start();                                  //new FeedDeleteCommentRequest(nfid, cmdid, AppConstants.TYPE_DELETE_STATUS_COMMENT).start();
        //            }
        //        } 
        else if (e.getSource() == attendedClgRadioButton || e.getSource() == attendedGradSchlRadioButton) {
            if (attendedGradSchlRadioButton.isSelected()) {
                degreePanel.setVisible(true);
            } else {
                degreePanel.setVisible(false);
            }

        }

    }

}
