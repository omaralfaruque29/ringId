/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

import com.ipvision.constants.MaxLengths;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.EducationInfoDTO;
import com.ipvision.model.SkillInfoDTO;
import com.ipvision.model.WorkInfoDTO;
import com.ipvision.model.constants.StaticFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.ChangeEmail;
import com.ipvision.service.ChangeMobileNumber;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.ChangeMyProfile;
import com.ipvision.service.ChangeWhatInMind;
import com.ipvision.service.WorkEducationSkillListRequest;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.view.utility.ChangePasswordOpenDialog;
import com.ipvision.view.utility.DatePanel;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.image.ImageObjects;
import com.ipvision.view.utility.TextPanelWithEmoticon;
import javax.swing.ImageIcon;

/**
 *
 * @author Sirat Samyoun
 */
public class MyProfileAboutPanel extends JPanel implements MouseListener, ActionListener {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MyProfileAboutPanel.class);
    public MyProfileVisibilities emailvisibility;
    public MyProfileVisibilities mobilevisibility;
    public MyProfileVisibilities birthDVisibility;
    private JPanel basicInfoPanel, workInfoPanel, educationInfoPanel, skillInfoPanel;
//    private JPanel basicWrapper, livingWrapper, workWrapper, educationWrapper, skillWrapper, aboutMeWrapper;
//    private JPanel basicCenter, livingCenter, workCenter, educationCenter, skillCenter, aboutMeCenter;
//    private JPanel basicInfoHolder, livingInfoHolder, workInfoHolder, educationInfoHolder, skillInfoHolder, aboutMeInfoHolder;
    private JLabel editwhatTextd;
    private JLabel editfullName;
    private JLabel editlastName;
    private JLabel editPassword;
    private JLabel editemail, editMobileNumber;
    private JLabel editGender;
    private JLabel editBirthDay;//private JLabel editMobileNumber;
    private JLabel editAboutMe, editMarriageDay, editCurrentCity, editHomeCity;
    public JPanel whatInMind;
    public JPanel fullName;
    public JPanel password;
    public JPanel country;
    public JPanel phoneNumber;
    public JPanel currentCity, homeCity, marriageDay, aboutMe;
    public JPanel email;
    public JPanel gender;
    public JPanel birthDay;
    public JPanel singleBasicInfo;
    public GridBagConstraints conBasic;
    public GridBagConstraints gd, gbc, conAllInfo, bwork;
    public int d = 0, m = 0, y = 0;
    private JPanel allInfo;
    //private JButton basicArrow, livingArrow, educationArrow, skillArrow, workArrow, aboutMeArrow;
    private static String STR_LESS = "Show Less";
    private static String STR_MORE = "See Details";
    private static String COMMA_SEP = ", ";
    //private JLabel livingLabel, basicLabel, workLabel, educationLabel, skillLabel, aboutMeLabel;
    //private JLabel livingIcon, basicIcon, workIcon, educationIcon, skillIcon, aboutMeIcon;
    private JTextField valueboxFullName, valueBoxCurrentCity, valueBoxHomeCity, valueBoxMail;
    private JLabel pleasWWhatinMind, pleasWFullName, pleasWGender, pleasWBirthday, pleasWHomeCity, pleasWCurrentCity, pleasWMarriageDay, pleasWAboutMe, pleaseWMobile, pleaseWMail;
    private JPanel errorPanelFullName, errorPanelGender, errorPanelBirthday, errorPanelHomeCity, errorPanelCurrentCity, errorPanelMarriageday, errorPanelAboutMe, errorPanelMobileNumber, errorPanelMail;
    private JTextArea whatArea, aboutMeArea;
    private JButton closeButtonWhatinMind, okButtonWhatinMind, okButtonFullName, okButtonLastName, closeButtonFullName, closeButtonLastName, closeButtonGender, okButtonGender, okButtonBday, closeButtonBday, okButtonMail, closeButtonMail;
    private JButton okButtonCurrentCity, closeButtonCurrentCity, okButtonHomeCity, closeButtonHomeCity, okButtonMarriageDay, closeButtonMarriageDay, closeButtonAboutMe, okButtonAboutMe, closeButtonMobile, okButtonMobile;
    private JComboBox genderCombo;
    private DatePanel birthDatepanel, marriageDatePanel;
    public JComboBox monthcomboBox;
    public JComboBox yearcomboBox;
    public JComboBox daycomboBox;
    private WorkEducationSkillPanel workPanel, educationPanel, skillPanel; //workEducationSkillPanel, 
    private MyProfileEmailPanel emailPanel;
    private MyProfilePhoneNumberPanel phoneNumberPanel;
    public JPanel basicShort, workShort, educationShort, skillShort;
    public JPanel basicNew, workNew, educationNew, skillNew;
    private JTextArea skillLbl, workLbl1, workLbl2, eduLbl1, eduLbl2;
    private JButton dotBasic, dotWork, dotSkill, dotEducation;
    private JLabel addWork, addSkill;
    private static int TYPE_BASIC = 0;
    private static int TYPE_EDUCATION = 2;
    private static int TYPE_WORK = 1;
    private static int TYPE_SKILL = 4;
    private static double fraction = 0.57;
    public CreateWorkSinglePanel createWorkSinglePanelInstance;
    public CreateSkillSinglePanel createSkillSinglePanelInstance;
    public CountryChooseListAbout countryChooseList;

    public WorkEducationSkillPanel getWorkPanel() {
        return workPanel;
    }

    public WorkEducationSkillPanel getEducationPanel() {
        return educationPanel;
    }

    public WorkEducationSkillPanel getSkillPanel() {
        return skillPanel;
    }

    public MyProfileEmailPanel getEmailPanel() {
        return emailPanel;
    }

    public MyProfilePhoneNumberPanel getPhoneNumberPanel() {
        return phoneNumberPanel;
    }

    public MyProfileAboutPanel() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        init();
    }

    public void updateMySkillLable() {
        if (MyfnfHashMaps.getInstance().getSkillInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID) != null
                && !MyfnfHashMaps.getInstance().getSkillInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID).isEmpty()) {
            String str = "Learnt ";
            boolean isF = false;
            Map<Long, SkillInfoDTO> skillMap = MyfnfHashMaps.getInstance().getSkillInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID);
            for (SkillInfoDTO sk : skillMap.values()) {
                isF = true;
                str = str + sk.getSkill() + COMMA_SEP;
            }
            str = str.substring(0, str.length() - 2);
            str = appendAnd(str);
            skillLbl.setColumns((int) (str.length() * fraction) + 1);
            skillLbl.setText(str);
            skillLbl.revalidate();
            skillLbl.repaint();
            if (!isF) {
                skillLbl.setVisible(false);
            } else {
                skillLbl.setVisible(true);
            }
            skillShort.revalidate();
            skillShort.repaint();
        }
    }

    private String appendAnd(String str) {
        int i = str.lastIndexOf(',');
        if (i != -1) {
            StringBuilder sb = new StringBuilder(str);
            sb.replace(i, i + 1, " and");
            str = sb.toString();
        }
        return str;
    }

    public String txtMenu(int i) {
        switch (i) {
            case 0:
                return StaticFields.STR_BASIC;
            case 1:
                return StaticFields.STR_WORK;
            case 2:
                return StaticFields.STR_EDU;
            case 4:
                return StaticFields.STR_SKILL;
        }
        return "";
    }

    public void updateMyEducationLable() {
        if (MyfnfHashMaps.getInstance().getEducationInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID) != null
                && !MyfnfHashMaps.getInstance().getEducationInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID).isEmpty()) {

            String str1 = "Studies in ", str2 = "Previously Studied in ";
            boolean isF1 = false, isF2 = false;
            Map<Long, EducationInfoDTO> educationMap = MyfnfHashMaps.getInstance().getEducationInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID);
            for (EducationInfoDTO ed : educationMap.values()) {
                if (ed.getToTime().longValue() == 0L) {
                    isF1 = true;
                    str1 = str1 + ed.getSchool() + COMMA_SEP;
                } else {
                    isF2 = true;
                    str2 = str2 + ed.getSchool() + COMMA_SEP;
                }
            }
            if (isF1) {
                str1 = str1.substring(0, str1.length() - 2);
                str1 = appendAnd(str1);
            }
            if (isF2) {
                str2 = str2.substring(0, str2.length() - 2);
                str2 = appendAnd(str2);
            }
            System.out.println("sxfasz" + str1);
            System.out.println("sxfasz2" + str2);
            eduLbl1.setColumns((int) (str1.length() * fraction) + 1);
            eduLbl1.setText(str1);
            eduLbl1.revalidate();
            eduLbl1.repaint();
            eduLbl2.setColumns((int) (str2.length() * fraction) + 1);
            eduLbl2.setText(str2);
            eduLbl2.revalidate();
            eduLbl2.repaint();
            if (!isF1) {
                eduLbl1.setVisible(false);
            } else {
                eduLbl1.setVisible(true);
            }
            if (!isF2) {
                eduLbl2.setVisible(false);
            } else {
                eduLbl2.setVisible(true);
            }
            educationShort.revalidate();
            educationShort.repaint();
        }
    }

    public void updateMyWorkLable() {
        if (MyfnfHashMaps.getInstance().getWorkInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID) != null
                && !MyfnfHashMaps.getInstance().getWorkInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID).isEmpty()) {

            String str1 = "Works as ", str2 = "Previously Worked as ";
            boolean isF1 = false, isF2 = false;
            Map<Long, WorkInfoDTO> workMap = MyfnfHashMaps.getInstance().getWorkInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID);
            for (WorkInfoDTO w : workMap.values()) {
                if (w.getTotime() == 0) {
                    isF1 = true;
                    str1 = str1 + w.getDesignation() + " in " + w.getCompanyName() + COMMA_SEP;
                } else {
                    isF2 = true;
                    str2 = str2 + w.getDesignation() + " in " + w.getCompanyName() + COMMA_SEP;
                }
            }
            if (isF1) {
                str1 = str1.substring(0, str1.length() - 2);
                str1 = appendAnd(str1);
            }
            if (isF2) {
                str2 = str2.substring(0, str2.length() - 2);
                str2 = appendAnd(str2);
            }
            System.out.println("wdgve" + str1);
            System.out.println("wdvde2" + str2);
            workLbl1.setColumns((int) (str1.length() * fraction) + 1);
            workLbl1.setText(str1);
            workLbl1.revalidate();
            workLbl1.repaint();
            workLbl2.setColumns((int) (str2.length() * fraction) + 1);
            workLbl2.setText(str2);
            workLbl2.revalidate();
            workLbl2.repaint();

            if (!isF1) {
                workLbl1.setVisible(false);
            } else {
                workLbl1.setVisible(true);
            }
            if (!isF2) {
                workLbl2.setVisible(false);
            } else {
                workLbl1.setVisible(true);
            }
            workShort.revalidate();
            workShort.repaint();
        }
    }

    private JPanel titleHolder(final int i) {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnl.setOpaque(false);//Background(Color.red);
        pnl.setPreferredSize(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH - 55, 40));
        pnl.setBorder(new EmptyBorder(0, 35, 0, 0));
        pnl.add(DesignClasses.makeJLabeltitleAbout(txtMenu(i)));
        final JPanel jp = new JPanel(new BorderLayout());
        jp.setBackground(RingColorCode.SEARCH_PANEL_COLOR);
        jp.add(pnl, BorderLayout.CENTER);
        final JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(35, 20));
        jp.add(p, BorderLayout.EAST);
        if (i == TYPE_WORK) {
            addWork = DesignClasses.create_iconJlabel(ImageObjects.addMoreHIcon, "Add a Work");
            addWork.addMouseListener(this);
            addWork.setBorder(new EmptyBorder(0, 0, 0, 15));
            p.add(addWork, BorderLayout.EAST);
            addWork.setVisible(false);
        }
        if (i == TYPE_SKILL) {
            addSkill = DesignClasses.create_iconJlabel(ImageObjects.addMoreHIcon, "Add a Skill");
            addSkill.addMouseListener(this);
            addSkill.setBorder(new EmptyBorder(0, 0, 0, 15));
            p.add(addSkill, BorderLayout.EAST);
            addSkill.setVisible(false);
        }
        jp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (i == TYPE_WORK) {
                    if (!workNew.isVisible()) {
                        addWork.setVisible(true);
                    }
                } else if (i == TYPE_SKILL) {
                    if (!skillNew.isVisible()) {
                        addSkill.setVisible(true);
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (i == TYPE_WORK) {
                    addWork.setVisible(false);
                } else if (i == TYPE_SKILL) {
                    addSkill.setVisible(false);
                }
            }
        });
        return jp;
    }

    private JPanel wrapperPnl(JPanel title, JPanel details, JPanel small, JPanel nw, JButton btn) {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);
        details.setVisible(false);
        nw.setVisible(false);
        small.setVisible(true);
        pnl.add(nw, BorderLayout.NORTH);
        pnl.add(details, BorderLayout.CENTER);
        pnl.add(small, BorderLayout.SOUTH);
        JPanel jp = new JPanel(new BorderLayout());
        jp.setOpaque(false);
        jp.add(title, BorderLayout.NORTH);
        jp.add(pnl, BorderLayout.CENTER);
        jp.add(btn, BorderLayout.SOUTH);
        return jp;
    }

    private JPanel basicShortView() {
        basicShort = new JPanel();
        basicShort.setBackground(RingColorCode.ABOUT_PANEL_BG);
        basicShort.setLayout(new GridBagLayout());
        GridBagConstraints b = new GridBagConstraints();
        b.gridx = 0;
        b.gridy = 0;
        basicShort.add(Box.createVerticalStrut(3), b);
        b.gridy++;
        basicShort.add(editTitleText(MyFnFSettings.userProfile.getFullName()), b);
        b.gridy++;
        if (MyFnFSettings.userProfile.getBirthday() != null && MyFnFSettings.userProfile.getBirthday().trim().length() > 0) {
            basicShort.add(editTitleText2("Born on " + MyFnFSettings.userProfile.getBirthday()), b);
            b.gridy++;
        }
        if (MyFnFSettings.userProfile.getMarriageDay() != null && MyFnFSettings.userProfile.getMarriageDay().trim().length() > 0) {
            basicShort.add(editTitleText2("Married, on " + MyFnFSettings.userProfile.getMarriageDay()), b);
            b.gridy++;
        }
        if (MyFnFSettings.userProfile.getCurrentCity() != null && MyFnFSettings.userProfile.getCurrentCity().trim().length() > 0) {
            basicShort.add(editTitleText2("Lives in " + MyFnFSettings.userProfile.getCurrentCity()), b);
            b.gridy++;
        }
        if (MyFnFSettings.userProfile.getHomeCity() != null && MyFnFSettings.userProfile.getHomeCity().trim().length() > 0) {
            basicShort.add(editTitleText2("From " + MyFnFSettings.userProfile.getHomeCity()), b);
            b.gridy++;
        }
        basicShort.add(Box.createVerticalStrut(3), b);
        b.gridy++;
        return basicShort;
    }

    private JPanel skillShortView() {
        skillShort = new JPanel();
        skillShort.setBackground(RingColorCode.ABOUT_PANEL_BG);
        skillShort.setLayout(new GridBagLayout());
        GridBagConstraints b = new GridBagConstraints();
        b.gridx = 0;
        b.gridy = 0;
        skillShort.add(Box.createVerticalStrut(3), b);
        b.gridy++;
        skillLbl = DesignClasses.createJTextAreaForAbout("", 12);
        skillShort.add(skillLbl, b);
        b.gridy++;
        skillShort.add(Box.createVerticalStrut(3), b);
        b.gridy++;
        return skillShort;
    }

    private JPanel workShortView() {
        workShort = new JPanel();
        workShort.setBackground(RingColorCode.ABOUT_PANEL_BG);
        workShort.setLayout(new GridBagLayout());
        bwork = new GridBagConstraints();
        bwork.gridx = 0;
        bwork.gridy = 0;
        workShort.add(Box.createVerticalStrut(3), bwork);
        bwork.gridy++;
        workLbl1 = DesignClasses.createJTextAreaForAbout("", 12);
        workShort.add(workLbl1, bwork);
        bwork.gridy++;
        workLbl2 = DesignClasses.createJTextAreaForAbout("", 12);
        workShort.add(workLbl2, bwork);
        bwork.gridy++;
        workShort.add(Box.createVerticalStrut(3), bwork);
        bwork.gridy++;
        return workShort;
    }

    private JPanel educationShortView() {
        educationShort = new JPanel();
        educationShort.setBackground(RingColorCode.ABOUT_PANEL_BG);
        educationShort.setLayout(new GridBagLayout());
        GridBagConstraints b = new GridBagConstraints();
        b.gridx = 0;
        b.gridy = 0;
        educationShort.add(Box.createVerticalStrut(3), b);
        b.gridy++;

        eduLbl1 = DesignClasses.createJTextAreaForAbout("", 12);
        educationShort.add(eduLbl1, b);
        b.gridy++;
        eduLbl2 = DesignClasses.createJTextAreaForAbout("", 12);
        educationShort.add(eduLbl2, b);
        b.gridy++;
        educationShort.add(Box.createVerticalStrut(3), b);
        b.gridy++;
        return educationShort;
    }

    public void init() {
        allInfo = new JPanel();
        allInfo.setOpaque(false);
        //allInfo.setLayout(new BoxLayout(notificationContainerPanel, BoxLayout.Y_AXIS));
        allInfo.setLayout(new GridBagLayout());
        conAllInfo = new GridBagConstraints();
        conAllInfo.gridx = 0;
        conAllInfo.gridy = 0;
        conAllInfo.anchor = GridBagConstraints.WEST;
        conAllInfo.insets = new Insets(5, 10, 5, 0);
        allInfo.add(Box.createRigidArea(new Dimension(2, 2)), conAllInfo);
        conAllInfo.gridy++;
        new WorkEducationSkillListRequest().start();
        /////////////////////
        basicInfoPanel = new JPanel(new BorderLayout());
        basicInfoPanel.setBackground(RingColorCode.ABOUT_PANEL_BG);
        dotBasic = DesignClasses.createImageButtonWithIcons(ImageObjects.dotIcon, ImageObjects.dotHIcon, STR_MORE);
        dotBasic.addActionListener(this);
        basicNew = new JPanel(new BorderLayout());
        basicNew.setOpaque(false);
        allInfo.add(wrapperPnl(titleHolder(TYPE_BASIC), basicInfoPanel, basicShortView(), basicNew, dotBasic), conAllInfo);
        conAllInfo.gridy++;

        workInfoPanel = new JPanel(new BorderLayout());
        workInfoPanel.setBackground(RingColorCode.ABOUT_PANEL_BG);
        dotWork = DesignClasses.createImageButtonWithIcons(ImageObjects.dotIcon, ImageObjects.dotHIcon, STR_MORE);
        dotWork.addActionListener(this);
        workNew = new JPanel(new BorderLayout());
        workNew.setOpaque(false);
        allInfo.add(wrapperPnl(titleHolder(TYPE_WORK), workInfoPanel, workShortView(), workNew, dotWork), conAllInfo);
        conAllInfo.gridy++;

        educationInfoPanel = new JPanel(new BorderLayout());
        educationInfoPanel.setBackground(RingColorCode.ABOUT_PANEL_BG);
        dotEducation = DesignClasses.createImageButtonWithIcons(ImageObjects.dotIcon, ImageObjects.dotHIcon, STR_MORE);
        dotEducation.addActionListener(this);
        educationNew = new JPanel(new BorderLayout());
        educationNew.setOpaque(false);
        allInfo.add(wrapperPnl(titleHolder(TYPE_EDUCATION), educationInfoPanel, educationShortView(), educationNew, dotEducation), conAllInfo);
        conAllInfo.gridy++;

        skillInfoPanel = new JPanel(new BorderLayout());
        skillInfoPanel.setBackground(RingColorCode.ABOUT_PANEL_BG);
        dotSkill = DesignClasses.createImageButtonWithIcons(ImageObjects.dotIcon, ImageObjects.dotHIcon, STR_MORE);
        dotSkill.addActionListener(this);
        skillNew = new JPanel(new BorderLayout());
        skillNew.setOpaque(false);
        allInfo.add(wrapperPnl(titleHolder(TYPE_SKILL), skillInfoPanel, skillShortView(), skillNew, dotSkill), conAllInfo);
        conAllInfo.gridy++;
        allInfo.add(Box.createRigidArea(new Dimension(2, 4)), conAllInfo);
        conAllInfo.gridy++;
        add(allInfo, BorderLayout.CENTER);
        addDownContents();
    }

    private void addDownContents() {
        try {
            singleBasicInfo = new JPanel();
            singleBasicInfo.setOpaque(false);
            singleBasicInfo.setLayout(new GridBagLayout());
            conBasic = new GridBagConstraints();
            conBasic.gridx = 0;
            conBasic.gridy = 0;
            conBasic.insets = new Insets(2, 2, 2, 2);
            basicInfoPanel.add(singleBasicInfo, BorderLayout.CENTER);

            fullName = nullPanel();
            // lastName = nullPanel();
            password = nullPanel();
            phoneNumber = nullPanel();
            email = nullPanel();
            gender = nullPanel();
            birthDay = nullPanel();
            currentCity = nullPanel();
            homeCity = nullPanel();
            marriageDay = nullPanel();
            aboutMe = nullPanel();

            this.handleUserStatusChange();
        } catch (Exception e) {
        }

    }

    public void handleUserStatusChange() {
        if (MyFnFSettings.userProfile != null) {
            //changWhatinMind();
            changeFullName();
            singleBasicInfo.add(fullName, conBasic);
            conBasic.gridy++;
            changePassword();
            singleBasicInfo.add(password, conBasic);
            conBasic.gridy++;
            changeCurrentCity();
            singleBasicInfo.add(currentCity, conBasic);
            conBasic.gridy++;
            changeHomeCity();
            singleBasicInfo.add(homeCity, conBasic);
            conBasic.gridy++;
            changeGender();
            singleBasicInfo.add(gender, conBasic);
            conBasic.gridy++;
            changeBirthDay();
            singleBasicInfo.add(birthDay, conBasic);
            conBasic.gridy++;
            changeMarriageDay();
            singleBasicInfo.add(marriageDay, conBasic);
            conBasic.gridy++;
            changeEmail();
            singleBasicInfo.add(email, conBasic);
            conBasic.gridy++;
            changemobileNumber();
            singleBasicInfo.add(phoneNumber, conBasic);
            conBasic.gridy++;
            changeAboutMe();
            singleBasicInfo.add(aboutMe, conBasic);
            conBasic.gridy++;
            singleBasicInfo.add(nullPanel(), conBasic);
            conBasic.gridy++;

            GuiRingID.getInstance().getMainRight().getMyProfilePanel().myInfoScroller.getVerticalScrollBar().setValue(0);
            GuiRingID.getInstance().getMainRight().getMyProfilePanel().myInfoScroller.revalidate();
            GuiRingID.getInstance().getMainRight().getMyProfilePanel().myInfoScroller.repaint();
        }
    }

    private long getMSec(String dateString) {
        Date d = new Date();
        try {
            d = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (Exception e) {
        }
        return d.getTime();
    }

    private void edit_WhatinMind() {
        if (whatInMind != null) {
            try {
                whatInMind.removeAll();
                JPanel contanier = basicJPanel(290, 19, 0);
                //    contanier.setBackground(Color.red);
                JLabel name = DesignClasses.makeJLabel_normal("Change mood", 14, DefaultSettings.BLACK_FONT);
                contanier.add(name, BorderLayout.LINE_START);

                pleasWWhatinMind = DesignClasses.makeJLabel_normal(" ", 10, DefaultSettings.errorLabelColor);
                contanier.add(pleasWWhatinMind, BorderLayout.LINE_END);
                whatInMind.add(contanier, BorderLayout.PAGE_START);
                String what_in_mind;
                if (MyFnFSettings.userProfile.getWhatisInYourMind() != null && MyFnFSettings.userProfile.getWhatisInYourMind().trim().length() > 0) {
                    what_in_mind = MyFnFSettings.userProfile.getWhatisInYourMind();
                } else {
                    what_in_mind = "What's on mind?";
                }
                whatArea = DesignClasses.createJTextArea(what_in_mind, MaxLengths.WHATS_ON_MIND);
                whatArea.setVisible(true);
                whatArea.grabFocus();

                final JPanel buttonsPanel = new JPanel(new GridLayout(2, 0));
                buttonsPanel.setOpaque(false);
                JPanel secondButton = new JPanel();
                okButtonWhatinMind = DesignClasses.createImageButtonWithIcons(ImageObjects.okIcon, ImageObjects.okHIcon, StaticFields.SAVE);
                secondButton.add(okButtonWhatinMind);
                secondButton.setOpaque(false);
                buttonsPanel.add(secondButton);
                okButtonWhatinMind.addActionListener(this);
                closeButtonWhatinMind = DesignClasses.createImageButtonWithIcons(ImageObjects.closeIcon, ImageObjects.closeHIcon, StaticFields.CLOSE);
                secondButton.add(closeButtonWhatinMind);
                closeButtonWhatinMind.addActionListener(this);
                whatInMind.add(whatArea, BorderLayout.CENTER);
                whatInMind.add(buttonsPanel, BorderLayout.LINE_END);
                whatInMind.revalidate();
                whatInMind.repaint();
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Error in edit_WhatinMind ==>" + e.getMessage());
            }
        }
    }

    private JPanel createUnitAboutPanelPrivacy(final JLabel editlabelName, String title, String value, boolean privacyStatus, final String privacy, final int fieldCons) {
        final JPanel containerWrapper = new JPanel(new BorderLayout());
        containerWrapper.setBackground(Color.WHITE);
        //containerWrapper.setOpaque(false);

        JPanel sizePanel1 = new JPanel();
        sizePanel1.setOpaque(false);
        sizePanel1.setPreferredSize(new Dimension(600, 1));
        containerWrapper.add(sizePanel1, BorderLayout.NORTH);

        JPanel contanier = new JPanel();
        contanier.setOpaque(false);
        contanier.setLayout(new BoxLayout(contanier, BoxLayout.X_AXIS));
//        if (fieldCons != AppConstants.CONS_ABOUT_ME) {
//            contanier.setPreferredSize(new Dimension(600, 35));
//            //contanier.add(Box.createRigidArea(new Dimension(15, 0)));
//        }
//        else {
//            contanier.add(Box.createRigidArea(new Dimension(13, 0)));
//        }
        contanier.add(Box.createRigidArea(new Dimension(4, 0)));
        containerWrapper.add(contanier, BorderLayout.CENTER);

        JLabel name = editTitleText(title);
        JLabel valuelabel = editTitleText(value);
        valuelabel.setPreferredSize(new Dimension(150, 20));
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.setPreferredSize(new Dimension(115, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        namePanel.setOpaque(false);
        namePanel.add(name, BorderLayout.CENTER);

        JLabel colon = editTitleText(StaticFields.COLON);
        JPanel colonPanel = new JPanel(new BorderLayout());
        colonPanel.setPreferredSize(new Dimension(15, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        colonPanel.setOpaque(false);
        colonPanel.add(colon, BorderLayout.CENTER);

        JPanel valuePanel = new JPanel(new BorderLayout());
        valuePanel.setOpaque(false);
        //valuePanel.setBackground(Color.red);

        JPanel sizePanel = new JPanel();
        sizePanel.setOpaque(false);

        if (fieldCons == AppConstants.CONS_ABOUT_ME) {
            sizePanel.setPreferredSize(new Dimension(200, 5));
            valuePanel.add(new TextPanelWithEmoticon(135, value), BorderLayout.CENTER);
        } else {
            sizePanel.setPreferredSize(new Dimension(200, 1));
            valuePanel.add(valuelabel, BorderLayout.CENTER);
        }
        valuePanel.add(sizePanel, BorderLayout.NORTH);

        JPanel msgShowPanel = new JPanel(new BorderLayout());
        msgShowPanel.setPreferredSize(new Dimension(100, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        msgShowPanel.setOpaque(false);

        ImageIcon iconPrivacy = ImageObjects.publicIcon;
        if (privacyStatus) {
            if (privacy.equalsIgnoreCase(StaticFields.PRIVACY_ONLY_FRIEND)) {
                iconPrivacy = ImageObjects.onlyfriendsIcon;
            } else if (privacy.equalsIgnoreCase(StaticFields.PRIVACY_ONLY_ME)) {
                iconPrivacy = ImageObjects.onlyMeIcon;
            }
        }
        final JLabel privacyArrow = DesignClasses.create_iconJlabel(iconPrivacy, privacy);
//DesignClasses.createImageButtonWithIcons(iconPrivacy, iconPrivacy, privacy);//BasicArrowButton.SOUTH);

        JPanel privacyandEditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        privacyandEditPanel.setOpaque(false);
        privacyandEditPanel.setPreferredSize(new Dimension(133, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));

        final JPanel privacyPanel = new JPanel(new BorderLayout());
        JPanel editPanel = new JPanel(new BorderLayout());
        privacyPanel.setOpaque(false);
        editPanel.setOpaque(false);
        privacyPanel.setPreferredSize(new Dimension(45, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        editPanel.setPreferredSize(new Dimension(20, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        //editPanel.setBorder(new EmptyBorder(0, 2,0, 0));
        privacyandEditPanel.add(privacyPanel);
        privacyandEditPanel.add(Box.createRigidArea(new Dimension(51, 1)));
        privacyandEditPanel.add(editPanel);

        if (editlabelName != null) {
            editlabelName.addMouseListener(this);
            editlabelName.setToolTipText(StaticFields.EDIT);
            editPanel.add(editlabelName, BorderLayout.CENTER);
            editlabelName.setVisible(false);
//            if (InternetUnavailablityCheck.isInternetAvailable) {
//                editlabelName.show();
//            } else {
//                editlabelName.hide();
//            }
        }
        if (privacyStatus) {
            privacyPanel.add(privacyArrow, BorderLayout.CENTER);
            privacyArrow.setVisible(false);
            privacyArrow.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //if (InternetUnavailablityCheck.isInternetAvailable) {
                    if (fieldCons == AppConstants.CONS_EMAIL) {
                        emailvisibility = new MyProfileVisibilities(AppConstants.CONS_EMAIL);
                        emailvisibility.show(privacyPanel, privacyPanel.getX(), privacyPanel.getY() + 10);
                    } else if (fieldCons == AppConstants.CONS_MOBILE_PHONE) {
                        mobilevisibility = new MyProfileVisibilities(AppConstants.CONS_MOBILE_PHONE);
                        mobilevisibility.show(privacyPanel, privacyPanel.getX(), privacyPanel.getY() + 10);
                    } else if (fieldCons == AppConstants.CONS_BIRTH_DAY) {
                        birthDVisibility = new MyProfileVisibilities(AppConstants.CONS_BIRTH_DAY);
                        birthDVisibility.show(privacyPanel, privacyPanel.getX(), privacyPanel.getY() + 10);
                    }
                    // }
                }
            });
        }
        JPanel nameHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nameHolder.add(namePanel);
        JPanel colonHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
        colonHolder.add(colonPanel);
        JPanel msgShowHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
        msgShowHolder.add(msgShowPanel);
        JPanel privacyandEditHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
        privacyandEditHolder.add(privacyandEditPanel);

        nameHolder.setOpaque(false);
        colonHolder.setOpaque(false);
        msgShowHolder.setOpaque(false);
        privacyandEditHolder.setOpaque(false);

        contanier.add(nameHolder);
        contanier.add(colonHolder);
        contanier.add(valuePanel);
        contanier.add(msgShowHolder);
        contanier.add(privacyandEditHolder);
        containerWrapper.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                containerWrapper.setBackground(RingColorCode.SEARCH_PANEL_COLOR);
                if (editlabelName != null) {
                    editlabelName.setVisible(true);
                }
                if (privacyArrow != null) {
                    privacyArrow.setVisible(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isPrivacyPopupVisible(fieldCons)) {
                    containerWrapper.setBackground(Color.WHITE);
                    if (editlabelName != null) {
                        editlabelName.setVisible(false);
                    }
                    if (privacyArrow != null) {  //&& !isPrivacyPopupVisible(fieldCons)
                        privacyArrow.setVisible(false);
                    }
                }
            }
        });

        return containerWrapper;
    }

    boolean isPrivacyPopupVisible(int fieldCons) {
        if (fieldCons == AppConstants.CONS_EMAIL) {
            return (emailvisibility != null && emailvisibility.isVisible() && emailvisibility.isDisplayable());
        } else if (fieldCons == AppConstants.CONS_MOBILE_PHONE) {
            return (mobilevisibility != null && mobilevisibility.isVisible() && mobilevisibility.isDisplayable());
        } else if (fieldCons == AppConstants.CONS_BIRTH_DAY) {
            return (birthDVisibility != null && birthDVisibility.isVisible() && birthDVisibility.isDisplayable());
        }
        return false;
    }

    private void edit_fullName() {
        if (fullName != null) {
            try {
                String name = "";
                if (MyFnFSettings.userProfile.getFullName() != null) {
                    name = MyFnFSettings.userProfile.getFullName();
                }
                fullName.removeAll();
                fullName.setOpaque(false);
                JPanel containerWrapper = new JPanel(new BorderLayout());
                containerWrapper.setOpaque(false);
                JPanel sizePanel1 = new JPanel();
                sizePanel1.setOpaque(false);
                sizePanel1.setPreferredSize(new Dimension(600, 1));
                containerWrapper.add(sizePanel1, BorderLayout.NORTH);

                errorPanelFullName = new JPanel(new BorderLayout());
                errorPanelFullName.setOpaque(false);
                errorPanelFullName.setBorder(new EmptyBorder(6, 155, 0, 0));
                containerWrapper.add(errorPanelFullName, BorderLayout.SOUTH);
                errorPanelFullName.setVisible(false);

                JPanel contanier = new JPanel();
                contanier.setLayout(new BoxLayout(contanier, BoxLayout.X_AXIS));
                contanier.add(Box.createRigidArea(new Dimension(4, 0)));
                contanier.setOpaque(false);
                containerWrapper.add(contanier, BorderLayout.CENTER);
                JLabel nameLabel = editTitleText(StaticFields.FULL_NAME_TEXT);
                //              name.setBorder(new EmptyBorder(0, 0, 2, 0));
                valueboxFullName = DesignClasses.makeTextFieldLimitedTextSize(name, 100, 20, 100);
                valueboxFullName.grabFocus();
                JPanel namePanel = new JPanel(new BorderLayout());
                namePanel.setPreferredSize(new Dimension(115, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                namePanel.setOpaque(false);
                namePanel.add(nameLabel, BorderLayout.CENTER);
                JLabel colon = editTitleText(StaticFields.COLON);
                //             colon.setBorder(new EmptyBorder(0, 0, 2, 0));
                JPanel colonPanel = new JPanel(new BorderLayout());
                colonPanel.setPreferredSize(new Dimension(15, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                colonPanel.setOpaque(false);
                colonPanel.add(colon, BorderLayout.CENTER);

                JPanel valuePanel = new JPanel(new BorderLayout());
                valuePanel.setPreferredSize(new Dimension(200, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                valuePanel.setOpaque(false);
                valuePanel.add(valueboxFullName, BorderLayout.CENTER);

                pleasWFullName = DesignClasses.makeJLabel_normal(" ", 10, DefaultSettings.errorLabelColor);
                JPanel msgShowPanel = new JPanel(new BorderLayout());
                msgShowPanel.setPreferredSize(new Dimension(DefaultSettings.BUTTONS_PANEL_WIDTH, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                msgShowPanel.setOpaque(false);
                errorPanelFullName.add(pleasWFullName, BorderLayout.SOUTH);

                okButtonFullName = DesignClasses.createImageButtonWithIcons(ImageObjects.okIcon, ImageObjects.okHIcon, StaticFields.SAVE);
                closeButtonFullName = DesignClasses.createImageButtonWithIcons(ImageObjects.closeIcon, ImageObjects.closeHIcon, StaticFields.CLOSE);

                okButtonFullName.addActionListener(this);
                closeButtonFullName.addActionListener(this);

                JPanel privacyandEditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                privacyandEditPanel.setOpaque(false);
                privacyandEditPanel.setPreferredSize(new Dimension(193, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                JPanel privacyPanel = new JPanel(new BorderLayout());
                JPanel editPanel = new JPanel(new BorderLayout());
                privacyPanel.setOpaque(false);
                editPanel.setOpaque(false);
                privacyPanel.setPreferredSize(new Dimension(45, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                editPanel.setPreferredSize(new Dimension(20, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                msgShowPanel.add(okButtonFullName, BorderLayout.CENTER);
                msgShowPanel.add(closeButtonFullName, BorderLayout.EAST);
                privacyandEditPanel.add(privacyPanel);
                privacyandEditPanel.add(Box.createRigidArea(new Dimension(101, 1)));
                privacyandEditPanel.add(editPanel);

                JPanel nameHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                nameHolder.add(namePanel);
                JPanel colonHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                colonHolder.add(colonPanel);
                JPanel msgShowHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                msgShowHolder.add(msgShowPanel);
                JPanel privacyandEditHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                privacyandEditHolder.add(privacyandEditPanel);
                nameHolder.setOpaque(false);
                colonHolder.setOpaque(false);
                msgShowHolder.setOpaque(false);
                privacyandEditHolder.setOpaque(false);
                contanier.add(nameHolder);
                contanier.add(colonHolder);
                contanier.add(valuePanel);
                contanier.add(msgShowHolder);
                contanier.add(privacyandEditHolder);

                fullName.add(containerWrapper, BorderLayout.CENTER);
                fullName.revalidate();
                fullName.repaint();
            } catch (Exception e) {
                //  e.printStackTrace();
                log.error("name change failed" + e.getMessage());
            }
        }
    }

    private void edit_gender() {
        if (gender != null) {
            try {
                gender.removeAll();
                gender.setOpaque(false);
                JPanel containerWrapper = new JPanel(new BorderLayout());
                containerWrapper.setOpaque(false);

                JPanel sizePanel1 = new JPanel();
                sizePanel1.setOpaque(false);
                sizePanel1.setPreferredSize(new Dimension(600, 1));
                containerWrapper.add(sizePanel1, BorderLayout.NORTH);

                errorPanelGender = new JPanel(new BorderLayout());
                errorPanelGender.setOpaque(false);
                errorPanelGender.setBorder(new EmptyBorder(6, 155, 0, 0));
                containerWrapper.add(errorPanelGender, BorderLayout.SOUTH);
                errorPanelGender.setVisible(false);

                JPanel contanier = new JPanel();
                contanier.setLayout(new BoxLayout(contanier, BoxLayout.X_AXIS));
                contanier.add(Box.createRigidArea(new Dimension(4, 0)));
                contanier.setOpaque(false);
                containerWrapper.add(contanier, BorderLayout.CENTER);
                JLabel nameLabel = editTitleText(StaticFields.GENDER_TEXT);
                genderCombo = DesignClasses.createJCombobox(MyFnFSettings.GENDER_ARRAY);
                if (MyFnFSettings.userProfile.getGender() != null) {
                    genderCombo.setSelectedItem("  " + MyFnFSettings.userProfile.getGender());
                }
                JPanel genderPanel = new JPanel(new BorderLayout());
                //            genderPanel.setPreferredSize(new Dimension(50, 20));
                genderPanel.add(genderCombo, BorderLayout.CENTER);
                genderPanel.setOpaque(true);
                JPanel namePanel = new JPanel(new BorderLayout());
                namePanel.setPreferredSize(new Dimension(115, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                namePanel.setOpaque(false);
                namePanel.add(nameLabel, BorderLayout.CENTER);
                JLabel colon = editTitleText(StaticFields.COLON);
                //             colon.setBorder(new EmptyBorder(0, 0, 2, 0));
                JPanel colonPanel = new JPanel(new BorderLayout());
                colonPanel.setPreferredSize(new Dimension(15, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                colonPanel.setOpaque(false);
                colonPanel.add(colon, BorderLayout.CENTER);
                JPanel valuePanel = new JPanel(new BorderLayout());
                valuePanel.setPreferredSize(new Dimension(200, 25));
                valuePanel.setOpaque(false);
                valuePanel.add(genderPanel, BorderLayout.CENTER);
                valuePanel.add(Box.createRigidArea(new Dimension(70, 10)), BorderLayout.EAST);

                pleasWGender = DesignClasses.makeJLabel_normal(" ", 10, DefaultSettings.errorLabelColor);
                JPanel msgShowPanel = new JPanel(new BorderLayout());
                msgShowPanel.setPreferredSize(new Dimension(DefaultSettings.BUTTONS_PANEL_WIDTH, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                msgShowPanel.setOpaque(false);
                errorPanelGender.add(pleasWGender, BorderLayout.CENTER);

                okButtonGender = DesignClasses.createImageButtonWithIcons(ImageObjects.okIcon, ImageObjects.okHIcon, StaticFields.SAVE);
                closeButtonGender = DesignClasses.createImageButtonWithIcons(ImageObjects.closeIcon, ImageObjects.closeHIcon, StaticFields.CLOSE);

                JPanel privacyandEditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                privacyandEditPanel.setOpaque(false);
                privacyandEditPanel.setPreferredSize(new Dimension(193, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                JPanel privacyPanel = new JPanel(new BorderLayout());
                JPanel editPanel = new JPanel(new BorderLayout());
                privacyPanel.setOpaque(false);
                editPanel.setOpaque(false);
                privacyPanel.setPreferredSize(new Dimension(45, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                editPanel.setPreferredSize(new Dimension(20, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                msgShowPanel.add(okButtonGender, BorderLayout.CENTER);
                msgShowPanel.add(closeButtonGender, BorderLayout.EAST);
                privacyandEditPanel.add(privacyPanel);
                privacyandEditPanel.add(Box.createRigidArea(new Dimension(101, 1)));
                privacyandEditPanel.add(editPanel);

                okButtonGender.addActionListener(this);
                closeButtonGender.addActionListener(this);
                JPanel nameHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                nameHolder.add(namePanel);
                JPanel colonHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                colonHolder.add(colonPanel);
                JPanel msgShowHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                msgShowHolder.add(msgShowPanel);
                JPanel privacyandEditHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                privacyandEditHolder.add(privacyandEditPanel);
                nameHolder.setOpaque(false);
                colonHolder.setOpaque(false);
                msgShowHolder.setOpaque(false);
                privacyandEditHolder.setOpaque(false);
                contanier.add(nameHolder);
                contanier.add(colonHolder);
                contanier.add(valuePanel);
                contanier.add(msgShowHolder);
                contanier.add(privacyandEditHolder);
                gender.add(containerWrapper, BorderLayout.CENTER);
                gender.revalidate();
                gender.repaint();
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Gender change failed" + e.getMessage());
            }
        }
    }

    private void edit_birthDay() {
        if (birthDay != null) {
            try {
                birthDay.removeAll();
                birthDay.setOpaque(false);
                if (MyFnFSettings.userProfile.getBirthday() != null && MyFnFSettings.userProfile.getBirthday().trim().length() > 0) {
                    String birthdate = MyFnFSettings.userProfile.getBirthday();
                    String[] parts = birthdate.split(StaticFields.HYP_SEP);
                    birthDatepanel = new DatePanel(1960, 1, 1, Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                } else {
                    birthDatepanel = new DatePanel(1960, 1, 1);
                }
                JPanel containerWrapper = new JPanel(new BorderLayout());
                containerWrapper.setOpaque(false);
                JPanel sizePanel1 = new JPanel();
                sizePanel1.setOpaque(false);
                sizePanel1.setPreferredSize(new Dimension(600, 1));
                containerWrapper.add(sizePanel1, BorderLayout.NORTH);
                errorPanelBirthday = new JPanel(new BorderLayout());
                errorPanelBirthday.setOpaque(false);
                errorPanelBirthday.setBorder(new EmptyBorder(6, 158, 0, 0));
                containerWrapper.add(errorPanelBirthday, BorderLayout.SOUTH);
                errorPanelBirthday.setVisible(false);
                JPanel contanier = new JPanel();
                contanier.setLayout(new BoxLayout(contanier, BoxLayout.X_AXIS));
                contanier.add(Box.createRigidArea(new Dimension(4, 0)));
                contanier.setOpaque(false);
                containerWrapper.add(contanier, BorderLayout.CENTER);
                JLabel nameLabel = editTitleText(StaticFields.BIRTH_DAY_TEXT);
                JPanel namePanel = new JPanel(new BorderLayout());
                namePanel.setPreferredSize(new Dimension(115, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                namePanel.setOpaque(false);
                namePanel.add(nameLabel, BorderLayout.CENTER);
                JLabel colon = editTitleText(StaticFields.COLON);
                JPanel colonPanel = new JPanel(new BorderLayout());
                colonPanel.setPreferredSize(new Dimension(15, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                colonPanel.setOpaque(false);
                colonPanel.add(colon, BorderLayout.CENTER);
                JPanel valuePanel = new JPanel(new BorderLayout());
                //valuePanel.setPreferredSize(new Dimension(200, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                valuePanel.setOpaque(false);
                valuePanel.add(Box.createRigidArea(new Dimension(200, 8)), BorderLayout.NORTH);
                valuePanel.add(birthDatepanel, BorderLayout.CENTER);

                pleasWBirthday = DesignClasses.makeJLabel_normal(" ", 10, DefaultSettings.errorLabelColor);
                JPanel msgShowPanel = new JPanel(new BorderLayout());
                msgShowPanel.setPreferredSize(new Dimension(DefaultSettings.BUTTONS_PANEL_WIDTH, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                msgShowPanel.setOpaque(false);
                errorPanelBirthday.add(pleasWBirthday, BorderLayout.CENTER);

                okButtonBday = DesignClasses.createImageButtonWithIcons(ImageObjects.okIcon, ImageObjects.okHIcon, StaticFields.SAVE);
                closeButtonBday = DesignClasses.createImageButtonWithIcons(ImageObjects.closeIcon, ImageObjects.closeHIcon, StaticFields.CLOSE);
                JPanel privacyandEditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                privacyandEditPanel.setOpaque(false);
                privacyandEditPanel.setPreferredSize(new Dimension(193, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                contanier.add(privacyandEditPanel);
                JPanel privacyPanel = new JPanel(new BorderLayout());
                JPanel editPanel = new JPanel(new BorderLayout());
                privacyPanel.setOpaque(false);
                editPanel.setOpaque(false);
                privacyPanel.setPreferredSize(new Dimension(45, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                editPanel.setPreferredSize(new Dimension(20, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                msgShowPanel.add(okButtonBday, BorderLayout.CENTER);
                msgShowPanel.add(closeButtonBday, BorderLayout.EAST);
                privacyandEditPanel.add(privacyPanel);
                privacyandEditPanel.add(Box.createRigidArea(new Dimension(101, 1)));
                privacyandEditPanel.add(editPanel);
                okButtonBday.addActionListener(this);
                closeButtonBday.addActionListener(this);
                JPanel nameHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                nameHolder.add(namePanel);
                JPanel colonHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                colonHolder.add(colonPanel);
                JPanel msgShowHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                msgShowHolder.add(msgShowPanel);
                JPanel privacyandEditHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                privacyandEditHolder.add(privacyandEditPanel);
                nameHolder.setOpaque(false);
                colonHolder.setOpaque(false);
                msgShowHolder.setOpaque(false);
                privacyandEditHolder.setOpaque(false);
                contanier.add(nameHolder);
                contanier.add(colonHolder);
                contanier.add(valuePanel);
                contanier.add(msgShowHolder);
                contanier.add(privacyandEditHolder);

                birthDay.add(containerWrapper, BorderLayout.CENTER);
                birthDay.revalidate();
                birthDay.repaint();
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Birthday change failed" + e.getMessage());
            }
        }
    }

    private void edit_currentCity() {
        if (currentCity != null) {
            try {
                String city = "";
                if (MyFnFSettings.userProfile.getCurrentCity() != null) {
                    city = MyFnFSettings.userProfile.getCurrentCity();
                }
                currentCity.removeAll();
                currentCity.setOpaque(false);

                JPanel containerWrapper = new JPanel(new BorderLayout());
                containerWrapper.setOpaque(false);
                JPanel sizePanel1 = new JPanel();
                sizePanel1.setOpaque(false);
                sizePanel1.setPreferredSize(new Dimension(600, 1));
                containerWrapper.add(sizePanel1, BorderLayout.NORTH);
                errorPanelCurrentCity = new JPanel(new BorderLayout());
                errorPanelCurrentCity.setOpaque(false);
                errorPanelCurrentCity.setBorder(new EmptyBorder(6, 155, 0, 0));
                containerWrapper.add(errorPanelCurrentCity, BorderLayout.SOUTH);
                errorPanelCurrentCity.setVisible(false);
                JPanel contanier = new JPanel();
                contanier.setLayout(new BoxLayout(contanier, BoxLayout.X_AXIS));
                contanier.add(Box.createRigidArea(new Dimension(4, 0)));
                contanier.setOpaque(false);
                containerWrapper.add(contanier, BorderLayout.CENTER);
                JLabel currentCityLabel = editTitleText(StaticFields.CURRENT_CITY_TEXT);
                valueBoxCurrentCity = DesignClasses.makeTextFieldLimitedTextSize(city, 100, 20, 100);
                valueBoxCurrentCity.grabFocus();
                JPanel currentCityPanel = new JPanel(new BorderLayout());
                currentCityPanel.setPreferredSize(new Dimension(115, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                currentCityPanel.setOpaque(false);
                currentCityPanel.add(currentCityLabel, BorderLayout.CENTER);
                JLabel colon = editTitleText(StaticFields.COLON);
                JPanel colonPanel = new JPanel(new BorderLayout());
                colonPanel.setPreferredSize(new Dimension(15, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                colonPanel.setOpaque(false);
                colonPanel.add(colon, BorderLayout.CENTER);
                JPanel valuePanel = new JPanel(new BorderLayout());
                valuePanel.setPreferredSize(new Dimension(200, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                valuePanel.setOpaque(false);
                valuePanel.add(valueBoxCurrentCity, BorderLayout.CENTER);

                pleasWCurrentCity = DesignClasses.makeJLabel_normal(" ", 10, DefaultSettings.errorLabelColor);
                JPanel msgShowPanel = new JPanel(new BorderLayout());
                msgShowPanel.setPreferredSize(new Dimension(DefaultSettings.BUTTONS_PANEL_WIDTH, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                msgShowPanel.setOpaque(false);
                errorPanelCurrentCity.add(pleasWCurrentCity, BorderLayout.CENTER);

                okButtonCurrentCity = DesignClasses.createImageButtonWithIcons(ImageObjects.okIcon, ImageObjects.okHIcon, StaticFields.SAVE);
                closeButtonCurrentCity = DesignClasses.createImageButtonWithIcons(ImageObjects.closeIcon, ImageObjects.closeHIcon, StaticFields.CLOSE);
                okButtonCurrentCity.addActionListener(this);
                closeButtonCurrentCity.addActionListener(this);

                JPanel privacyandEditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                privacyandEditPanel.setOpaque(false);
                privacyandEditPanel.setPreferredSize(new Dimension(193, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                JPanel privacyPanel = new JPanel(new BorderLayout());
                JPanel editPanel = new JPanel(new BorderLayout());
                privacyPanel.setOpaque(false);
                editPanel.setOpaque(false);
                privacyPanel.setPreferredSize(new Dimension(45, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                editPanel.setPreferredSize(new Dimension(20, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                msgShowPanel.add(okButtonCurrentCity, BorderLayout.CENTER);
                msgShowPanel.add(closeButtonCurrentCity, BorderLayout.EAST);
                privacyandEditPanel.add(privacyPanel);
                privacyandEditPanel.add(Box.createRigidArea(new Dimension(101, 1)));
                privacyandEditPanel.add(editPanel);

                JPanel nameHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                nameHolder.add(currentCityPanel);
                JPanel colonHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                colonHolder.add(colonPanel);
                JPanel msgShowHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                msgShowHolder.add(msgShowPanel);
                JPanel privacyandEditHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                privacyandEditHolder.add(privacyandEditPanel);
                nameHolder.setOpaque(false);
                colonHolder.setOpaque(false);
                msgShowHolder.setOpaque(false);
                privacyandEditHolder.setOpaque(false);
                contanier.add(nameHolder);
                contanier.add(colonHolder);
                contanier.add(valuePanel);
                contanier.add(msgShowHolder);
                contanier.add(privacyandEditHolder);

                currentCity.add(containerWrapper, BorderLayout.CENTER);
                currentCity.revalidate();
                currentCity.repaint();
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("current city change failed" + e.getMessage());
            }
        }
    }

    private void edit_homeCity() {

        if (homeCity != null) {
            try {
                String city = "";
                if (MyFnFSettings.userProfile.getHomeCity() != null) {
                    city = MyFnFSettings.userProfile.getHomeCity();
                }
                homeCity.removeAll();
                homeCity.setOpaque(false);
                JPanel containerWrapper = new JPanel(new BorderLayout());
                containerWrapper.setOpaque(false);

                JPanel sizePanel1 = new JPanel();
                sizePanel1.setOpaque(false);
                sizePanel1.setPreferredSize(new Dimension(600, 1));
                containerWrapper.add(sizePanel1, BorderLayout.NORTH);
                errorPanelHomeCity = new JPanel(new BorderLayout());
                errorPanelHomeCity.setOpaque(false);
                errorPanelHomeCity.setBorder(new EmptyBorder(6, 155, 0, 0));
                containerWrapper.add(errorPanelHomeCity, BorderLayout.SOUTH);
                errorPanelHomeCity.setVisible(false);
                JPanel contanier = new JPanel();
                contanier.setLayout(new BoxLayout(contanier, BoxLayout.X_AXIS));
                contanier.add(Box.createRigidArea(new Dimension(4, 0)));
                contanier.setOpaque(false);
                containerWrapper.add(contanier, BorderLayout.CENTER);
                JLabel nameLabel = editTitleText(StaticFields.HOME_CITY_TEXT);
                //              name.setBorder(new EmptyBorder(0, 0, 2, 0));
                valueBoxHomeCity = DesignClasses.makeTextFieldLimitedTextSize(city, 100, 20, 100);
                valueBoxHomeCity.grabFocus();
                JPanel namePanel = new JPanel(new BorderLayout());
                namePanel.setPreferredSize(new Dimension(115, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                namePanel.setOpaque(false);
                namePanel.add(nameLabel, BorderLayout.CENTER);
                JLabel colon = editTitleText(StaticFields.COLON);
                //             colon.setBorder(new EmptyBorder(0, 0, 2, 0));
                JPanel colonPanel = new JPanel(new BorderLayout());
                colonPanel.setPreferredSize(new Dimension(15, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                colonPanel.setOpaque(false);
                colonPanel.add(colon, BorderLayout.CENTER);
                JPanel valuePanel = new JPanel(new BorderLayout());
                valuePanel.setPreferredSize(new Dimension(200, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                valuePanel.setOpaque(false);
                valuePanel.add(valueBoxHomeCity, BorderLayout.CENTER);

                pleasWHomeCity = DesignClasses.makeJLabel_normal(" ", 10, DefaultSettings.errorLabelColor);
                JPanel msgShowPanel = new JPanel(new BorderLayout());
                msgShowPanel.setPreferredSize(new Dimension(DefaultSettings.BUTTONS_PANEL_WIDTH, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                msgShowPanel.setOpaque(false);
                errorPanelHomeCity.add(pleasWHomeCity, BorderLayout.CENTER);

                okButtonHomeCity = DesignClasses.createImageButtonWithIcons(ImageObjects.okIcon, ImageObjects.okHIcon, StaticFields.SAVE);
                closeButtonHomeCity = DesignClasses.createImageButtonWithIcons(ImageObjects.closeIcon, ImageObjects.closeHIcon, StaticFields.CLOSE);
                okButtonHomeCity.addActionListener(this);
                closeButtonHomeCity.addActionListener(this);

                JPanel privacyandEditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                privacyandEditPanel.setOpaque(false);
                privacyandEditPanel.setPreferredSize(new Dimension(193, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                JPanel privacyPanel = new JPanel(new BorderLayout());
                JPanel editPanel = new JPanel(new BorderLayout());
                privacyPanel.setOpaque(false);
                editPanel.setOpaque(false);
                privacyPanel.setPreferredSize(new Dimension(45, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                editPanel.setPreferredSize(new Dimension(20, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                msgShowPanel.add(okButtonHomeCity, BorderLayout.CENTER);
                msgShowPanel.add(closeButtonHomeCity, BorderLayout.EAST);
                privacyandEditPanel.add(privacyPanel);
                privacyandEditPanel.add(Box.createRigidArea(new Dimension(101, 1)));
                privacyandEditPanel.add(editPanel);

                JPanel nameHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                nameHolder.add(namePanel);
                JPanel colonHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                colonHolder.add(colonPanel);
                JPanel msgShowHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                msgShowHolder.add(msgShowPanel);
                JPanel privacyandEditHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                privacyandEditHolder.add(privacyandEditPanel);
                nameHolder.setOpaque(false);
                colonHolder.setOpaque(false);
                msgShowHolder.setOpaque(false);
                privacyandEditHolder.setOpaque(false);
                contanier.add(nameHolder);
                contanier.add(colonHolder);
                contanier.add(valuePanel);
                contanier.add(msgShowHolder);
                contanier.add(privacyandEditHolder);

                homeCity.add(containerWrapper, BorderLayout.CENTER);
                homeCity.revalidate();
                homeCity.repaint();
                //    new ChangeMyProfile(pleasWHomeCity, 0L, AppConstants.CONS_MARRIAGE_DAY, okButtonMarriageDay).start();

            } catch (Exception e) {
                //  e.printStackTrace();
                log.error("home city change failed" + e.getMessage());
            }
        }
    }

    private void edit_marriageDay() {
        if (marriageDay != null) {
            try {
                marriageDay.removeAll();
                marriageDay.setOpaque(false);

                JPanel containerWrapper = new JPanel(new BorderLayout());
                containerWrapper.setOpaque(false);

                JPanel sizePanel1 = new JPanel();
                sizePanel1.setOpaque(false);
                sizePanel1.setPreferredSize(new Dimension(600, 1));
                containerWrapper.add(sizePanel1, BorderLayout.NORTH);
                errorPanelMarriageday = new JPanel(new BorderLayout());
                errorPanelMarriageday.setOpaque(false);
                errorPanelMarriageday.setBorder(new EmptyBorder(6, 158, 0, 0));
                containerWrapper.add(errorPanelMarriageday, BorderLayout.SOUTH);
                errorPanelMarriageday.setVisible(false);
                JPanel contanier = new JPanel();
                contanier.setLayout(new BoxLayout(contanier, BoxLayout.X_AXIS));
                contanier.add(Box.createRigidArea(new Dimension(4, 0)));
                contanier.setOpaque(false);
                containerWrapper.add(contanier, BorderLayout.CENTER);
                JLabel nameLabel = editTitleText(StaticFields.MARRIAGE_TEXT);
                JPanel namePanel = new JPanel(new BorderLayout());
                namePanel.setPreferredSize(new Dimension(115, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                namePanel.setOpaque(false);
                namePanel.add(nameLabel, BorderLayout.CENTER);
                JLabel colon = editTitleText(StaticFields.COLON);
                JPanel colonPanel = new JPanel(new BorderLayout());
                colonPanel.setPreferredSize(new Dimension(15, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                colonPanel.setOpaque(false);
                colonPanel.add(colon, BorderLayout.CENTER);
                JPanel valuePanel = new JPanel(new BorderLayout());
                //               valuePanel.setPreferredSize(new Dimension(200, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                valuePanel.setOpaque(false);
//                married = new JCheckBox("Married");
//                married.setOpaque(false);
//                married.addActionListener(this);
//                mdayValue.add(married, BorderLayout.NORTH);

                // valuePanel.setPreferredSize(new Dimension(200, 20));
                if (MyFnFSettings.userProfile.getMarriageDay() != null && MyFnFSettings.userProfile.getMarriageDay().trim().length() > 0) {
//                    mdayContanier.setPreferredSize(new Dimension(600, 60));
//                    mdayValue.setPreferredSize(new Dimension(200, 60));
//                    married.setSelected(true);
//                    Date d = new Date(MyFnFSettings.userProfile.getMarriageDay());
//                    String marriagedate = new SimpleDateFormat("yyyy-MM-dd").format(d);
                    String[] parts = MyFnFSettings.userProfile.getMarriageDay().split(StaticFields.HYP_SEP);
                    marriageDatePanel = new DatePanel(1960, 1, 1, Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                } else {
//                    married.setSelected(false);
                    marriageDatePanel = new DatePanel(1960, 1, 1);
//                    marriageDatePanel.setVisible(false);
                }
                valuePanel.add(Box.createRigidArea(new Dimension(200, 8)), BorderLayout.NORTH);
                valuePanel.add(marriageDatePanel, BorderLayout.CENTER);
                pleasWMarriageDay = DesignClasses.makeJLabel_normal(" ", 10, DefaultSettings.errorLabelColor);
                JPanel msgShowPanel = new JPanel(new BorderLayout());
                msgShowPanel.setPreferredSize(new Dimension(DefaultSettings.BUTTONS_PANEL_WIDTH, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                msgShowPanel.setOpaque(false);
                errorPanelMarriageday.add(pleasWMarriageDay, BorderLayout.CENTER);

                okButtonMarriageDay = DesignClasses.createImageButtonWithIcons(ImageObjects.okIcon, ImageObjects.okHIcon, StaticFields.SAVE);
                closeButtonMarriageDay = DesignClasses.createImageButtonWithIcons(ImageObjects.closeIcon, ImageObjects.closeHIcon, StaticFields.CLOSE);

                JPanel privacyandEditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                privacyandEditPanel.setOpaque(false);
                privacyandEditPanel.setPreferredSize(new Dimension(193, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                JPanel privacyPanel = new JPanel(new BorderLayout());
                JPanel editPanel = new JPanel(new BorderLayout());
                privacyPanel.setOpaque(false);
                editPanel.setOpaque(false);
                privacyPanel.setPreferredSize(new Dimension(45, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                editPanel.setPreferredSize(new Dimension(20, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                msgShowPanel.add(okButtonMarriageDay, BorderLayout.CENTER);
                msgShowPanel.add(closeButtonMarriageDay, BorderLayout.EAST);
                privacyandEditPanel.add(privacyPanel);
                privacyandEditPanel.add(Box.createRigidArea(new Dimension(101, 1)));
                privacyandEditPanel.add(editPanel);
                okButtonMarriageDay.addActionListener(this);
                closeButtonMarriageDay.addActionListener(this);
                JPanel nameHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                nameHolder.add(namePanel);
                JPanel colonHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                colonHolder.add(colonPanel);
                JPanel msgShowHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                msgShowHolder.add(msgShowPanel);
                JPanel privacyandEditHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                privacyandEditHolder.add(privacyandEditPanel);
                nameHolder.setOpaque(false);
                colonHolder.setOpaque(false);
                msgShowHolder.setOpaque(false);
                privacyandEditHolder.setOpaque(false);
                contanier.add(nameHolder);
                contanier.add(colonHolder);
                contanier.add(valuePanel);
                contanier.add(msgShowHolder);
                contanier.add(privacyandEditHolder);
                marriageDay.add(containerWrapper, BorderLayout.CENTER);
                marriageDay.revalidate();
                marriageDay.repaint();
            } catch (Exception e) {
                //  e.printStackTrace();
                log.error("MarriageDay change failed" + e.getMessage());
            }
        }
    }

    private void edit_aboutMe() {
        if (aboutMe != null) {
            try {
                String about = "";
                if (MyFnFSettings.userProfile.getAboutMe() != null) {
                    about = MyFnFSettings.userProfile.getAboutMe();
                }
                aboutMe.removeAll();
                aboutMe.setOpaque(false);

                JPanel containerWrapper = new JPanel(new BorderLayout());
                containerWrapper.setOpaque(false);
                JPanel sizePanel1 = new JPanel();
                sizePanel1.setOpaque(false);
                sizePanel1.setPreferredSize(new Dimension(600, 1));
                containerWrapper.add(sizePanel1, BorderLayout.NORTH);

                errorPanelAboutMe = new JPanel(new BorderLayout());
                errorPanelAboutMe.setOpaque(false);
                errorPanelAboutMe.setBorder(new EmptyBorder(6, 155, 0, 0));
                containerWrapper.add(errorPanelAboutMe, BorderLayout.SOUTH);
                errorPanelAboutMe.setVisible(false);

                JPanel contanier = new JPanel();
                contanier.setLayout(new BoxLayout(contanier, BoxLayout.X_AXIS));
                contanier.add(Box.createRigidArea(new Dimension(4, 0)));
                contanier.setOpaque(false);
                containerWrapper.add(contanier, BorderLayout.CENTER);
                JLabel nameLabel = editTitleText(StaticFields.ABOUT_ME_TEXT);
                JPanel namePanel = new JPanel(new BorderLayout());
                namePanel.setPreferredSize(new Dimension(115, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                namePanel.setOpaque(false);
                namePanel.add(nameLabel, BorderLayout.CENTER);
                JLabel colon = editTitleText(StaticFields.COLON);
                JPanel colonPanel = new JPanel(new BorderLayout());
                colonPanel.setPreferredSize(new Dimension(15, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                colonPanel.setOpaque(false);
                colonPanel.add(colon, BorderLayout.CENTER);

                aboutMeArea = DesignClasses.createJTextArea(about, 13, 2000);
                //aboutMeArea.setColumns(15);
                aboutMeArea.grabFocus();
                JPanel valuePanel = new JPanel(new BorderLayout());
                valuePanel.setOpaque(false);
                JPanel sizePanel = new JPanel();
                sizePanel.setOpaque(false);
                sizePanel.setPreferredSize(new Dimension(200, 1));
                valuePanel.add(sizePanel, BorderLayout.NORTH);
                valuePanel.add(aboutMeArea, BorderLayout.CENTER);

                pleasWAboutMe = DesignClasses.makeJLabel_normal(" ", 10, DefaultSettings.errorLabelColor);
                JPanel msgShowPanel = new JPanel(new BorderLayout());
                msgShowPanel.setPreferredSize(new Dimension(DefaultSettings.BUTTONS_PANEL_WIDTH, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                msgShowPanel.setOpaque(false);
                errorPanelAboutMe.add(pleasWAboutMe, BorderLayout.SOUTH);

                okButtonAboutMe = DesignClasses.createImageButtonWithIcons(ImageObjects.okIcon, ImageObjects.okHIcon, StaticFields.SAVE);
                closeButtonAboutMe = DesignClasses.createImageButtonWithIcons(ImageObjects.closeIcon, ImageObjects.closeHIcon, StaticFields.CLOSE);

                okButtonAboutMe.addActionListener(this);
                closeButtonAboutMe.addActionListener(this);

                JPanel privacyandEditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                privacyandEditPanel.setOpaque(false);
                privacyandEditPanel.setPreferredSize(new Dimension(193, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                JPanel privacyPanel = new JPanel(new BorderLayout());
                JPanel editPanel = new JPanel(new BorderLayout());
                privacyPanel.setOpaque(false);
                editPanel.setOpaque(false);
                privacyPanel.setPreferredSize(new Dimension(45, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                editPanel.setPreferredSize(new Dimension(20, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                msgShowPanel.add(okButtonAboutMe, BorderLayout.CENTER);
                msgShowPanel.add(closeButtonAboutMe, BorderLayout.EAST);
                privacyandEditPanel.add(privacyPanel);
                privacyandEditPanel.add(Box.createRigidArea(new Dimension(101, 1)));
                privacyandEditPanel.add(editPanel);

                JPanel nameHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                nameHolder.add(namePanel);
                JPanel colonHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                colonHolder.add(colonPanel);
                JPanel msgShowHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                msgShowHolder.add(msgShowPanel);
                JPanel privacyandEditHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                privacyandEditHolder.add(privacyandEditPanel);
                nameHolder.setOpaque(false);
                colonHolder.setOpaque(false);
                msgShowHolder.setOpaque(false);
                privacyandEditHolder.setOpaque(false);
                contanier.add(nameHolder);
                contanier.add(colonHolder);
                contanier.add(valuePanel);
                contanier.add(msgShowHolder);
                contanier.add(privacyandEditHolder);

                aboutMe.add(containerWrapper, BorderLayout.CENTER);
                aboutMe.revalidate();
                aboutMe.repaint();
            } catch (Exception e) {
                //  e.printStackTrace();
                log.error("About me change failed" + e.getMessage());
            }
        }
    }

    private void edit_mobile_number() {
        if (phoneNumber != null) {
            try {
                phoneNumber.removeAll();
                phoneNumber.setOpaque(false);

                JPanel containerWrapper = new JPanel(new BorderLayout());
                containerWrapper.setOpaque(false);

                JPanel sizePanel1 = new JPanel();
                sizePanel1.setOpaque(false);
                sizePanel1.setPreferredSize(new Dimension(600, 1));
                containerWrapper.add(sizePanel1, BorderLayout.NORTH);
                errorPanelMobileNumber = new JPanel(new BorderLayout());
                errorPanelMobileNumber.setOpaque(false);
                errorPanelMobileNumber.setBorder(new EmptyBorder(6, 158, 0, 0));
                containerWrapper.add(errorPanelMobileNumber, BorderLayout.SOUTH);
                errorPanelMobileNumber.setVisible(false);
                JPanel contanier = new JPanel();
                contanier.setLayout(new BoxLayout(contanier, BoxLayout.X_AXIS));
                contanier.add(Box.createRigidArea(new Dimension(4, 0)));
                contanier.setOpaque(false);
                containerWrapper.add(contanier, BorderLayout.CENTER);
                JLabel nameLabel = editTitleText(StaticFields.PHONE_NO_TEXT);
                JPanel namePanel = new JPanel(new BorderLayout());
                namePanel.setPreferredSize(new Dimension(115, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                namePanel.setOpaque(false);
                namePanel.add(nameLabel, BorderLayout.CENTER);

                JLabel colon = editTitleText(StaticFields.COLON);
                JPanel colonPanel = new JPanel(new BorderLayout());
                colonPanel.setPreferredSize(new Dimension(15, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                colonPanel.setOpaque(false);
                colonPanel.add(colon, BorderLayout.CENTER);

                //String mblwithDC = "";
                countryChooseList = new CountryChooseListAbout();
                if (MyFnFSettings.userProfile.getMobilePhone() != null
                        && MyFnFSettings.userProfile.getMobilePhone().trim().length() > 0
                        && MyFnFSettings.userProfile.getMobilePhoneDialingCode() != null
                        && MyFnFSettings.userProfile.getMobilePhoneDialingCode().trim().length() > 0) {
                    //mblwithDC = MyFnFSettings.userProfile.getMobilePhoneDialingCode() + "-" + MyFnFSettings.userProfile.getMobilePhone();
                    countryChooseList.countryCodeTextField.setText(MyFnFSettings.userProfile.getMobilePhoneDialingCode());
                    countryChooseList.ringIDorMobileorEmailTextField.setText(MyFnFSettings.userProfile.getMobilePhone());
                    String countryName = HelperMethods.get_country_name_from_contry_code(MyFnFSettings.userProfile.getMobilePhoneDialingCode().trim());
                    String imageSource = GetImages.FLAGS_ROOT_FOLDER + countryName + ".png";
                    countryChooseList.imageLabel.setIcon(DesignClasses.return_image(imageSource));
                    countryChooseList.ringIDorMobileorEmailTextField.setForeground(null);
                }
                JPanel valuePanel = new JPanel(new BorderLayout());
                valuePanel.setPreferredSize(new Dimension(200, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                valuePanel.setOpaque(false);
                valuePanel.add(countryChooseList, BorderLayout.CENTER);

                pleaseWMobile = DesignClasses.makeJLabel_normal(" ", 10, DefaultSettings.errorLabelColor);
                JPanel msgShowPanel = new JPanel(new BorderLayout());
                msgShowPanel.setPreferredSize(new Dimension(DefaultSettings.BUTTONS_PANEL_WIDTH, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                msgShowPanel.setOpaque(false);
                errorPanelMobileNumber.add(pleaseWMobile, BorderLayout.SOUTH);

                okButtonMobile = DesignClasses.createImageButtonWithIcons(ImageObjects.okIcon, ImageObjects.okHIcon, StaticFields.SAVE);
                closeButtonMobile = DesignClasses.createImageButtonWithIcons(ImageObjects.closeIcon, ImageObjects.closeHIcon, StaticFields.CLOSE);

                okButtonMobile.addActionListener(this);
                closeButtonMobile.addActionListener(this);

                JPanel privacyandEditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                privacyandEditPanel.setOpaque(false);
                privacyandEditPanel.setPreferredSize(new Dimension(193, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                JPanel privacyPanel = new JPanel(new BorderLayout());
                JPanel editPanel = new JPanel(new BorderLayout());
                privacyPanel.setOpaque(false);
                editPanel.setOpaque(false);
                privacyPanel.setPreferredSize(new Dimension(45, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                editPanel.setPreferredSize(new Dimension(20, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                msgShowPanel.add(okButtonMobile, BorderLayout.CENTER);
                msgShowPanel.add(closeButtonMobile, BorderLayout.EAST);
                privacyandEditPanel.add(privacyPanel);
                privacyandEditPanel.add(Box.createRigidArea(new Dimension(101, 1)));
                privacyandEditPanel.add(editPanel);

                JPanel nameHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                nameHolder.add(namePanel);
                JPanel colonHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                colonHolder.add(colonPanel);
                JPanel msgShowHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                msgShowHolder.add(msgShowPanel);
                JPanel privacyandEditHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                privacyandEditHolder.add(privacyandEditPanel);
                nameHolder.setOpaque(false);
                colonHolder.setOpaque(false);
                msgShowHolder.setOpaque(false);
                privacyandEditHolder.setOpaque(false);
                contanier.add(nameHolder);
                contanier.add(colonHolder);
                contanier.add(valuePanel);
                contanier.add(msgShowHolder);
                contanier.add(privacyandEditHolder);

                phoneNumber.add(containerWrapper, BorderLayout.CENTER);
                phoneNumber.revalidate();
                phoneNumber.repaint();
            } catch (Exception e) {
                //  e.printStackTrace();
                log.error("phoneNumber change failed" + e.getMessage());
            }
        }
    }

    private void edit_email() {
        if (email != null) {
            try {
                String el = "";
                if (MyFnFSettings.userProfile.getEmail() != null) {
                    el = MyFnFSettings.userProfile.getEmail();
                }
                email.removeAll();
                email.setOpaque(false);
                JPanel containerWrapper = new JPanel(new BorderLayout());
                containerWrapper.setOpaque(false);
                JPanel sizePanel1 = new JPanel();
                sizePanel1.setOpaque(false);
                sizePanel1.setPreferredSize(new Dimension(600, 1));
                containerWrapper.add(sizePanel1, BorderLayout.NORTH);

                errorPanelMail = new JPanel(new BorderLayout());
                errorPanelMail.setOpaque(false);
                errorPanelMail.setBorder(new EmptyBorder(6, 155, 0, 0));
                containerWrapper.add(errorPanelMail, BorderLayout.SOUTH);
                errorPanelMail.setVisible(false);

                JPanel contanier = new JPanel();
                contanier.setLayout(new BoxLayout(contanier, BoxLayout.X_AXIS));
                contanier.add(Box.createRigidArea(new Dimension(4, 0)));
                contanier.setOpaque(false);
                containerWrapper.add(contanier, BorderLayout.CENTER);
                JLabel nameLabel = editTitleText(StaticFields.EMAIL_TEXT);
                valueBoxMail = DesignClasses.makeTextFieldLimitedTextSize(el, 100, 20, 100);
                valueBoxMail.grabFocus();
                JPanel namePanel = new JPanel(new BorderLayout());
                namePanel.setPreferredSize(new Dimension(115, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                namePanel.setOpaque(false);
                namePanel.add(nameLabel, BorderLayout.CENTER);
                JLabel colon = editTitleText(StaticFields.COLON);
                JPanel colonPanel = new JPanel(new BorderLayout());
                colonPanel.setPreferredSize(new Dimension(15, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                colonPanel.setOpaque(false);
                colonPanel.add(colon, BorderLayout.CENTER);

                JPanel valuePanel = new JPanel(new BorderLayout());
                valuePanel.setPreferredSize(new Dimension(200, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                valuePanel.setOpaque(false);
                valuePanel.add(valueBoxMail, BorderLayout.CENTER);

                pleaseWMail = DesignClasses.makeJLabel_normal(" ", 10, DefaultSettings.errorLabelColor);
                JPanel msgShowPanel = new JPanel(new BorderLayout());
                msgShowPanel.setPreferredSize(new Dimension(DefaultSettings.BUTTONS_PANEL_WIDTH, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                msgShowPanel.setOpaque(false);
                errorPanelMail.add(pleaseWMail, BorderLayout.SOUTH);

                okButtonMail = DesignClasses.createImageButtonWithIcons(ImageObjects.okIcon, ImageObjects.okHIcon, StaticFields.SAVE);
                closeButtonMail = DesignClasses.createImageButtonWithIcons(ImageObjects.closeIcon, ImageObjects.closeHIcon, StaticFields.CLOSE);

                okButtonMail.addActionListener(this);
                closeButtonMail.addActionListener(this);

                JPanel privacyandEditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                privacyandEditPanel.setOpaque(false);
                privacyandEditPanel.setPreferredSize(new Dimension(193, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                JPanel privacyPanel = new JPanel(new BorderLayout());
                JPanel editPanel = new JPanel(new BorderLayout());
                privacyPanel.setOpaque(false);
                editPanel.setOpaque(false);
                privacyPanel.setPreferredSize(new Dimension(45, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                editPanel.setPreferredSize(new Dimension(20, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
                msgShowPanel.add(okButtonMail, BorderLayout.CENTER);
                msgShowPanel.add(closeButtonMail, BorderLayout.EAST);
                privacyandEditPanel.add(privacyPanel);
                privacyandEditPanel.add(Box.createRigidArea(new Dimension(101, 1)));
                privacyandEditPanel.add(editPanel);

                JPanel nameHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                nameHolder.add(namePanel);
                JPanel colonHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                colonHolder.add(colonPanel);
                JPanel msgShowHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                msgShowHolder.add(msgShowPanel);
                JPanel privacyandEditHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
                privacyandEditHolder.add(privacyandEditPanel);
                nameHolder.setOpaque(false);
                colonHolder.setOpaque(false);
                msgShowHolder.setOpaque(false);
                privacyandEditHolder.setOpaque(false);
                contanier.add(nameHolder);
                contanier.add(colonHolder);
                contanier.add(valuePanel);
                contanier.add(msgShowHolder);
                contanier.add(privacyandEditHolder);

                email.add(containerWrapper, BorderLayout.CENTER);
                email.revalidate();
                email.repaint();
            } catch (Exception e) {
                //  e.printStackTrace();
                log.error("email change failed" + e.getMessage());
            }
        }
    }

    public void changeFullName() {
        if (fullName != null) {
            try {
                String name = "";
                if (MyFnFSettings.userProfile.getFullName() != null && MyFnFSettings.userProfile.getFullName().trim().length() > 0) {
                    name = MyFnFSettings.userProfile.getFullName();
                }
                fullName.removeAll();
                fullName.setOpaque(false);
                editfullName = editTextLabel();
                JPanel titleandEditLabel = createUnitAboutPanelPrivacy(editfullName, StaticFields.FULL_NAME_TEXT, name, false, StaticFields.PRIVACY_ONLY_ME, AppConstants.CONS_FULL_NAME);
                fullName.add(titleandEditLabel, BorderLayout.CENTER);
                fullName.revalidate();
                fullName.repaint();
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Error in changeFullName ==>" + e.getMessage());
            }
        }
    }

    public void changePassword() {
        if (password != null) {
            try {
                editPassword = editTextLabel();
                JPanel titleandEditLabel = createUnitAboutPanelPrivacy(editPassword, StaticFields.PASSWORD_TEXT, "**************", false, StaticFields.PRIVACY_ONLY_ME, AppConstants.CONS_FULL_NAME);
                password.add(titleandEditLabel, BorderLayout.CENTER);
                //     password.add(addTextDefalt("**************"), BorderLayout.CENTER);
                password.revalidate();
                password.revalidate();
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Error in changePassword ==>" + e.getMessage());
            }
        }
    }

    public void changeCountry() {
        if (country != null) {
            try {
                String countryName = "";
                if (MyFnFSettings.userProfile.getCountry() != null) {
                    countryName = MyFnFSettings.userProfile.getCountry();
                }
                country.removeAll();
                JPanel titleandEditLabel = createUnitAboutPanelPrivacy(null, StaticFields.COUNTRY_TEXT, countryName, false, StaticFields.PRIVACY_ONLY_ME, AppConstants.CONS_COUNTRY);
                country.add(titleandEditLabel, BorderLayout.CENTER);
                country.revalidate();
                country.repaint();
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Error in changeCountry ==>" + e.getMessage());
            }
        }
    }

    public void changemobileNumber() {
        if (phoneNumber != null) {
            try {
                phoneNumber.removeAll();
                String privacy = StaticFields.PRIVACY_PUBLIC;
                if (MyFnFSettings.userProfile.getMobilePrivacy() == AppConstants.PRIVACY_SHORT_ONLY_FRIEND) {
                    privacy = StaticFields.PRIVACY_ONLY_FRIEND;
                } else if (MyFnFSettings.userProfile.getMobilePrivacy() == AppConstants.PRIVACY_SHORT_ONLY_ME) {
                    privacy = StaticFields.PRIVACY_ONLY_ME;
                }
                editMobileNumber = editTextLabel();
                String mblwithDC = "";
                if (MyFnFSettings.userProfile.getMobilePhone() != null
                        && MyFnFSettings.userProfile.getMobilePhone().trim().length() > 0
                        && MyFnFSettings.userProfile.getMobilePhoneDialingCode() != null
                        && MyFnFSettings.userProfile.getMobilePhoneDialingCode().trim().length() > 0) {
                    mblwithDC = MyFnFSettings.userProfile.getMobilePhoneDialingCode() + StaticFields.HYP_SEP + MyFnFSettings.userProfile.getMobilePhone();
                }
                JPanel titleandEditLabel = createUnitAboutPanelPrivacy(editMobileNumber, StaticFields.PHONE_NO_TEXT, mblwithDC, true, privacy, AppConstants.CONS_MOBILE_PHONE);
                phoneNumber.add(titleandEditLabel, BorderLayout.CENTER);
//                phoneNumberPanel = new MyProfilePhoneNumberPanel();
//                phoneNumber.add(phoneNumberPanel);
                phoneNumber.revalidate();
                phoneNumber.repaint();
            } catch (Exception e) {
            }
        }

    }

    public void changeGender() {
        if (gender != null) {
            try {
                gender.removeAll();
                editGender = editTextLabel();
                String gnd = "";
                if (MyFnFSettings.userProfile.getGender() != null && MyFnFSettings.userProfile.getGender().trim().length() > 0) {
                    gnd = MyFnFSettings.userProfile.getGender();
                }
                JPanel titleandEditLabel = createUnitAboutPanelPrivacy(editGender, StaticFields.GENDER_TEXT, gnd, false, StaticFields.PRIVACY_ONLY_ME, AppConstants.CONS_GENDER);
                gender.add(titleandEditLabel, BorderLayout.CENTER);
                gender.revalidate();
                gender.repaint();
            } catch (Exception e) {
                //  e.printStackTrace();
                log.error("Error in changeGender ==>" + e.getMessage());
            }
        }
    }

    public void changeBirthDay() {
        if (birthDay != null) {
            try {
                birthDay.removeAll();
                String birthday = "";
                String strDate = "";
                if (MyFnFSettings.userProfile.getBirthday() != null && MyFnFSettings.userProfile.getBirthday().trim().length() > 0) {
                    birthday = MyFnFSettings.userProfile.getBirthday();
                    Date d = new Date();
                    d = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
                    strDate = new SimpleDateFormat("MMMM d, yyyy").format(d);
                }
                String privacy = StaticFields.PRIVACY_PUBLIC;
                if (MyFnFSettings.userProfile.getBirthdayPrivacy() == AppConstants.PRIVACY_SHORT_ONLY_FRIEND) {
                    privacy = StaticFields.PRIVACY_ONLY_FRIEND;
                } else if (MyFnFSettings.userProfile.getBirthdayPrivacy() == AppConstants.PRIVACY_SHORT_ONLY_ME) {
                    privacy = StaticFields.PRIVACY_ONLY_ME;
                }
                editBirthDay = editTextLabel();
                JPanel titleandEditLabel = createUnitAboutPanelPrivacy(editBirthDay, StaticFields.BIRTH_DAY_TEXT, strDate, true, privacy, AppConstants.CONS_BIRTH_DAY);
                birthDay.add(titleandEditLabel, BorderLayout.CENTER);
                birthDay.revalidate();
                birthDay.repaint();
            } catch (Exception e) {
            }
        }
    }

    public void changeCurrentCity() {
        if (currentCity != null) {
            try {
                String city = "";
                if (MyFnFSettings.userProfile.getCurrentCity() != null && MyFnFSettings.userProfile.getCurrentCity().trim().length() > 0) {
                    city = MyFnFSettings.userProfile.getCurrentCity();
                }
                currentCity.removeAll();
                currentCity.setOpaque(false);
                editCurrentCity = editTextLabel();
                JPanel titleandEditLabel = createUnitAboutPanelPrivacy(editCurrentCity, StaticFields.CURRENT_CITY_TEXT, city, false, StaticFields.PRIVACY_ONLY_ME, AppConstants.CONS_CURRENT_CITY);
                currentCity.add(titleandEditLabel, BorderLayout.CENTER);
                currentCity.revalidate();
                currentCity.repaint();
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Error in changeCurrentCity ==>" + e.getMessage());
            }
        }
    }

    public void changeHomeCity() {
        if (homeCity != null) {
            try {
                String city = "";
                if (MyFnFSettings.userProfile.getHomeCity() != null && MyFnFSettings.userProfile.getHomeCity().trim().length() > 0) {
                    city = MyFnFSettings.userProfile.getHomeCity();
                }
                homeCity.removeAll();
                homeCity.setOpaque(false);
                editHomeCity = editTextLabel();
                JPanel titleandEditLabel = createUnitAboutPanelPrivacy(editHomeCity, StaticFields.HOME_CITY_TEXT, city, false, StaticFields.PRIVACY_ONLY_ME, AppConstants.CONS_HOME_CITY);
                homeCity.add(titleandEditLabel, BorderLayout.CENTER);
                homeCity.revalidate();
                homeCity.repaint();
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Error in changeHomeCity ==>" + e.getMessage());
            }
        }
    }

    public void changeMarriageDay() {
        if (marriageDay != null) {
            try {
                marriageDay.removeAll();
                String strDate = "";
                if (MyFnFSettings.userProfile.getMarriageDay() != null && MyFnFSettings.userProfile.getMarriageDay().trim().length() > 0) {
                    Date d = new Date();
                    d = new SimpleDateFormat("yyyy-MM-dd").parse(MyFnFSettings.userProfile.getMarriageDay());
                    strDate = new SimpleDateFormat("MMMM d, yyyy").format(d);
                }
                editMarriageDay = editTextLabel();
                JPanel titleandEditLabel = createUnitAboutPanelPrivacy(editMarriageDay, StaticFields.MARRIAGE_TEXT, strDate, false, StaticFields.PRIVACY_ONLY_ME, AppConstants.CONS_MARRIAGE_DAY);
                marriageDay.add(titleandEditLabel, BorderLayout.CENTER);
                marriageDay.revalidate();
                marriageDay.repaint();
            } catch (Exception e) {
            }
        }
    }

    public void changeAboutMe() {
        if (aboutMe != null) {
            try {
                String about = "";
                if (MyFnFSettings.userProfile.getAboutMe() != null && MyFnFSettings.userProfile.getAboutMe().trim().length() > 0) {
                    about = MyFnFSettings.userProfile.getAboutMe();
                }
                aboutMe.removeAll();
                aboutMe.setOpaque(false);
                editAboutMe = editTextLabel();
                JPanel titleandEditLabel = createUnitAboutPanelPrivacy(editAboutMe, StaticFields.ABOUT_ME_TEXT, about, false, StaticFields.PRIVACY_ONLY_ME, AppConstants.CONS_ABOUT_ME);//createAboutMePanel(editAboutMe, about);
                aboutMe.add(titleandEditLabel, BorderLayout.CENTER);
                aboutMe.revalidate();
                aboutMe.repaint();
            } catch (Exception e) {
                //  e.printStackTrace();
                log.error("Error in changeAboutMe ==>" + e.getMessage());
            }
        }
    }

    public void changeEmail() {
        if (email != null) {
            try {
                email.removeAll();
                String privacy = StaticFields.PRIVACY_PUBLIC;
                if (MyFnFSettings.userProfile.getEmailPrivacy() == AppConstants.PRIVACY_SHORT_ONLY_FRIEND) {
                    privacy = StaticFields.PRIVACY_ONLY_FRIEND;
                } else if (MyFnFSettings.userProfile.getEmailPrivacy() == AppConstants.PRIVACY_SHORT_ONLY_ME) {
                    privacy = StaticFields.PRIVACY_ONLY_ME;
                }
                editemail = editTextLabel();
                String el = "";
                if (MyFnFSettings.userProfile.getEmail() != null && MyFnFSettings.userProfile.getEmail().trim().length() > 0) {
                    el = MyFnFSettings.userProfile.getEmail();
                }
                JPanel titleandEditLabel = createUnitAboutPanelPrivacy(editemail, StaticFields.EMAIL_TEXT, el, true, privacy, AppConstants.CONS_EMAIL);
                email.add(titleandEditLabel, BorderLayout.CENTER);
//                emailPanel = new MyProfileEmailPanel();
//                email.add(emailPanel);
                email.revalidate();
                email.repaint();
            } catch (Exception e) {
                log.error("Mail change failed -->" + e.getMessage());
            }
        }
    }

    public void changEmailPrivacyIcon() {
        ImageIcon iconPrivacy = ImageObjects.publicIcon;
        if (MyFnFSettings.userProfile.getEmailPrivacy() == AppConstants.PRIVACY_SHORT_ONLY_FRIEND) {
            iconPrivacy = ImageObjects.onlyfriendsIcon;
        } else if (MyFnFSettings.userProfile.getEmailPrivacy() == AppConstants.PRIVACY_SHORT_ONLY_ME) {
            iconPrivacy = ImageObjects.onlyMeIcon;
        }
        getEmailPanel().primary.privacyButton.setIcon(iconPrivacy);
        getEmailPanel().primary.privacyButton.setRolloverIcon(iconPrivacy);
        getEmailPanel().primary.privacyButton.setPressedIcon(iconPrivacy);
    }

    private JLabel editTitleText(String title) {
        return DesignClasses.makeJLabel_normal(title, 14, DefaultSettings.BLACK_FONT);
    }

    private JLabel editTitleText2(String title) {
        return DesignClasses.makeJLabelFullName(title, 13);
    }

    private JPanel nullPanel() {
        JPanel basicJpanel = new JPanel(new BorderLayout());
        //     basicJpanel.setPreferredSize(new Dimension(600, 35));
        basicJpanel.setOpaque(false);
        basicJpanel.setBackground(RingColorCode.ABOUT_PANEL_BG);
        //  basicJpanel.setBorder(new MatteBorder(0, 0, 1, 0, DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR));
        return basicJpanel;
    }

    private JPanel basicJPanel(int width, int height, int border) {
        JPanel basicJpanel = new JPanel(new BorderLayout());
        basicJpanel.setPreferredSize(new Dimension(width, height));
        basicJpanel.setOpaque(false);
        //   basicJpanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, DefaultSettings.lightWhiteBorder));
        return basicJpanel;
    }

    public JLabel editTextLabel() {
        final JLabel penButt = DesignClasses.create_image_label_with_preferredSize(10, 10, GetImages.PEN);
        penButt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                penButt.setIcon(DesignClasses.return_image(GetImages.PEN_H));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                penButt.setIcon(DesignClasses.return_image(GetImages.PEN));
            }
        });
        return penButt;
    }

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
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == editwhatTextd) {
            edit_WhatinMind();
        } else if (e.getSource() == editfullName) {
            edit_fullName();
        } else if (e.getSource() == editlastName) {
            //  edit_lastName();
        } else if (e.getSource() == editPassword) {
            ChangePasswordOpenDialog changePasswordOpenDialog = new ChangePasswordOpenDialog();
            changePasswordOpenDialog.init();
        } else if (e.getSource() == editemail) {
            edit_email();
        } else if (e.getSource() == editGender) {
            edit_gender();
        } else if (e.getSource() == editBirthDay) {
            edit_birthDay();
        } else if (e.getSource() == editCurrentCity) {
            edit_currentCity();
        } else if (e.getSource() == editHomeCity) {
            edit_homeCity();
        } else if (e.getSource() == editMarriageDay) {
            edit_marriageDay();
        } else if (e.getSource() == editAboutMe) {
            edit_aboutMe();
        } else if (e.getSource() == editMobileNumber) {
            edit_mobile_number();
        } else if (e.getSource() == addSkill) {
            addSkill.setVisible(false);
            createSkillSinglePanelInstance = new CreateSkillSinglePanel();
            if (MyfnfHashMaps.getInstance().getSkillInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID) == null
                    || MyfnfHashMaps.getInstance().getSkillInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID).isEmpty()) {
                createSkillSinglePanelInstance.setBorder(null);
            }
            skillNew.removeAll();
            skillNew.add(createSkillSinglePanelInstance, BorderLayout.CENTER);
            skillNew.revalidate();
            skillNew.repaint();
            skillNew.setVisible(true);
        } else if (e.getSource() == addWork) {
            addWork.setVisible(false);
            createWorkSinglePanelInstance = new CreateWorkSinglePanel();
            if (MyfnfHashMaps.getInstance().getWorkInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID) == null
                    || MyfnfHashMaps.getInstance().getWorkInfoMap().get(MyFnFSettings.LOGIN_USER_TABLE_ID).isEmpty()) {
                createWorkSinglePanelInstance.setBorder(null);
            }
            workNew.removeAll();
            workNew.add(createWorkSinglePanelInstance, BorderLayout.CENTER);
            workNew.revalidate();
            workNew.repaint();
            workNew.setVisible(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButtonWhatinMind) {
            if (!whatArea.getText().equals(MyFnFSettings.userProfile.getWhatisInYourMind())) {
                if (whatArea.getText().length() < MaxLengths.WHATS_ON_MIND) {
                    okButtonWhatinMind.setEnabled(false);
                    new ChangeWhatInMind(pleasWWhatinMind, whatArea.getText(), AppConstants.CONS_WHAT_IS_IN_UR_MIND).start();
                } else {
                    pleasWWhatinMind.setText("Need < " + MaxLengths.WHATS_ON_MIND);
                }
            } else {
                pleasWWhatinMind.setText(StaticFields.NO_CHANGE);
            }
        } else if (e.getSource() == okButtonFullName) {
            if (valueboxFullName.getText().trim().length() <= 0) {
                errorPanelFullName.setVisible(true);
                pleasWFullName.setText(StaticFields.ENTER + StaticFields.FULL_NAME_TEXT);
            } else {
                if (valueboxFullName.getText().equals(MyFnFSettings.userProfile.getFullName())) {
                    errorPanelFullName.setVisible(true);
                    pleasWFullName.setText(StaticFields.NO_CHANGE);
                } else {
                    okButtonFullName.setEnabled(false);
                    new ChangeMyProfile(pleasWFullName, valueboxFullName.getText(), AppConstants.CONS_FULL_NAME, okButtonFullName).start();
                }
            }
        } else if (e.getSource() == closeButtonFullName) {
            changeFullName();
        } else if (e.getSource() == okButtonLastName) {
        } else if (e.getSource() == closeButtonLastName) {
            //  changeLastname();
        } else if (e.getSource() == okButtonGender) {
            if (!genderCombo.getSelectedItem().toString().trim().equals(MyFnFSettings.userProfile.getGender())) {
                okButtonGender.setEnabled(false);
                new ChangeMyProfile(pleasWGender, genderCombo.getSelectedItem().toString().trim(), AppConstants.CONS_GENDER, okButtonGender).start();
            } else {
                errorPanelGender.setVisible(true);
                pleasWGender.setText(StaticFields.NO_CHANGE);
            }
        } else if (e.getSource() == closeButtonGender) {
            changeGender();
        } else if (e.getSource() == okButtonBday) {
            if (MyFnFSettings.userProfile.getBirthday() != null && birthDatepanel.getDateinMillisecs() == getMSec(MyFnFSettings.userProfile.getBirthday())) {
                errorPanelBirthday.setVisible(true);
                pleasWBirthday.setText(StaticFields.NO_CHANGE);
            } else {
                if (MyFnFSettings.userProfile.getMarriageDay() != null && birthDatepanel.getDateinMillisecs() > getMSec(MyFnFSettings.userProfile.getMarriageDay())) {
                    errorPanelBirthday.setVisible(true);
                    pleasWBirthday.setText("Birthday can't be after marriage");
                } else {
                    okButtonBday.setEnabled(false);
                    new ChangeMyProfile(pleasWBirthday, birthDatepanel.getStringDate().trim(), AppConstants.CONS_BIRTH_DAY, okButtonBday).start();
                }
            }
        } else if (e.getSource() == closeButtonBday) {
            changeBirthDay();
        } else if (e.getSource() == okButtonMarriageDay) {
            if (MyFnFSettings.userProfile.getMarriageDay() != null && marriageDatePanel.getDateinMillisecs() == getMSec(MyFnFSettings.userProfile.getMarriageDay())) {
                errorPanelMarriageday.setVisible(true);
                pleasWMarriageDay.setText(StaticFields.NO_CHANGE);
            } else {
                if (MyFnFSettings.userProfile.getBirthday() != null && marriageDatePanel.getDateinMillisecs() < getMSec(MyFnFSettings.userProfile.getBirthday())) {
                    errorPanelMarriageday.setVisible(true);
                    pleasWMarriageDay.setText("Marriage can't be before birthday");
                } else {
                    new ChangeMyProfile(pleasWMarriageDay, marriageDatePanel.getStringDate().trim(), AppConstants.CONS_MARRIAGE_DAY, okButtonMarriageDay).start();
                }
            }
        } else if (e.getSource() == closeButtonMarriageDay) {
            changeMarriageDay();
        } else if (e.getSource() == okButtonCurrentCity) {
            if (valueBoxCurrentCity.getText().trim().length() > 0 && !valueBoxCurrentCity.getText().equals(MyFnFSettings.userProfile.getCurrentCity())) {
                if (valueBoxCurrentCity.getText().trim().length() < MaxLengths.CITY) {
                    okButtonCurrentCity.setEnabled(false);
                    new ChangeMyProfile(pleasWCurrentCity, valueBoxCurrentCity.getText(), AppConstants.CONS_CURRENT_CITY, okButtonCurrentCity).start();
                } else {
                    errorPanelCurrentCity.setVisible(true);
                    pleasWCurrentCity.setText(StaticFields.CURRENT_CITY_TEXT + " < " + MaxLengths.CITY);
                }
            } else {
                if (valueBoxCurrentCity.getText().trim().length() <= 0) {
                    errorPanelCurrentCity.setVisible(true);
                    pleasWCurrentCity.setText(StaticFields.ENTER + StaticFields.CURRENT_CITY_TEXT);
                } else {
                    errorPanelCurrentCity.setVisible(true);
                    pleasWCurrentCity.setText(StaticFields.NO_CHANGE);
                }
            }
        } else if (e.getSource() == closeButtonCurrentCity) {
            changeCurrentCity();
        } else if (e.getSource() == okButtonMobile) {
            String val = countryChooseList.getTextMobileNowithDC();//valuebox.getText().trim();
            String[] parts = val.split(StaticFields.HYP_SEP);
            if (HelperMethods.isValidNumber(parts[1])) {
                if (!val.equals(MyFnFSettings.userProfile.getMobilePhoneDialingCode() + StaticFields.HYP_SEP + MyFnFSettings.userProfile.getMobilePhone())) {
                    new ChangeMobileNumber(parts[1], parts[0], null).start();
                } else {
                    errorPanelMobileNumber.setVisible(true);
                    pleaseWMobile.setText(StaticFields.NO_CHANGE);
                }
            } else {
                errorPanelMobileNumber.setVisible(true);
                pleaseWMobile.setText(StaticFields.ENTER_VALID + StaticFields.PHONE_NO_TEXT);
            }
        } else if (e.getSource() == closeButtonMobile) {
            changemobileNumber();
        } else if (e.getSource() == okButtonHomeCity) {
            if (valueBoxHomeCity.getText().trim().length() > 0 && !valueBoxHomeCity.getText().equals(MyFnFSettings.userProfile.getHomeCity())) {
                if (valueBoxHomeCity.getText().trim().length() < MaxLengths.CITY) {
                    okButtonHomeCity.setEnabled(false);
                    new ChangeMyProfile(pleasWHomeCity, valueBoxHomeCity.getText(), AppConstants.CONS_HOME_CITY, okButtonHomeCity).start();
                } else {
                    errorPanelHomeCity.setVisible(true);
                    pleasWHomeCity.setText(StaticFields.HOME_CITY_TEXT + " < " + MaxLengths.CITY);
                }
            } else {
                if (valueBoxHomeCity.getText().trim().length() <= 0) {
                    errorPanelHomeCity.setVisible(true);
                    pleasWHomeCity.setText(StaticFields.ENTER + StaticFields.HOME_CITY_TEXT);
                } else {
                    errorPanelHomeCity.setVisible(true);
                    pleasWHomeCity.setText(StaticFields.NO_CHANGE);
                }
            }
        } else if (e.getSource() == closeButtonHomeCity) {
            changeHomeCity();
        } else if (e.getSource() == okButtonAboutMe) {
            if (aboutMeArea.getText().trim().length() > 0 && !aboutMeArea.getText().equals(MyFnFSettings.userProfile.getAboutMe())) {
                okButtonAboutMe.setEnabled(false);
                new ChangeMyProfile(pleasWAboutMe, aboutMeArea.getText(), AppConstants.CONS_ABOUT_ME, okButtonAboutMe).start();
            } else {
                if (aboutMeArea.getText().trim().length() <= 0) {
                    errorPanelAboutMe.setVisible(true);
                    pleasWAboutMe.setText(StaticFields.ENTER + StaticFields.ABOUT_ME_TEXT);
                } else {
                    errorPanelAboutMe.setVisible(true);
                    pleasWAboutMe.setText(StaticFields.NO_CHANGE);
                }
            }
        } else if (e.getSource() == closeButtonAboutMe) {
            changeAboutMe();
        } else if (e.getSource() == okButtonMail) {
            if (valueBoxMail.getText().trim().length() > 0 && !valueBoxMail.getText().equals(MyFnFSettings.userProfile.getEmail())) {
                if (HelperMethods.isValidEmail(valueBoxMail.getText())) {  //                    new ChangeMobileNumber(parts[1], parts[0], okButtonMobile, null).start();
                    okButtonMail.setEnabled(false);
                    new ChangeEmail(valueBoxMail.getText().trim(), okButtonMail, null).start();
                } else {
                    errorPanelMail.setVisible(true);
                    pleaseWMail.setText(StaticFields.ENTER_VALID + StaticFields.EMAIL_TEXT);
                }
            } else {
                if (valueBoxMail.getText().trim().length() <= 0) {
                    errorPanelMail.setVisible(true);
                    pleaseWMail.setText(StaticFields.ENTER + StaticFields.EMAIL_TEXT);
                } else {
                    errorPanelMail.setVisible(true);
                    pleaseWMail.setText(StaticFields.NO_CHANGE);
                }
            }
        } else if (e.getSource() == closeButtonMail) {
            changeEmail();
        } else if (e.getSource() == dotSkill) {
            if (skillPanel == null) {
                skillPanel = new WorkEducationSkillPanel(MyFnFSettings.LOGIN_USER_ID, TYPE_SKILL);
                skillInfoPanel.add(skillPanel, BorderLayout.CENTER);
                skillInfoPanel.revalidate();
                skillInfoPanel.repaint();
            }
            if (skillShort.isVisible()) {
                dotSkill.setToolTipText(STR_LESS);
                skillShort.setVisible(false);
                skillInfoPanel.setVisible(true);
            } else {
                dotSkill.setToolTipText(STR_MORE);
                skillShort.setVisible(true);
                skillInfoPanel.setVisible(false);
            }
        } else if (e.getSource() == dotBasic) {
            if (basicShort.isVisible()) {
                dotBasic.setToolTipText(STR_LESS);
                basicShort.setVisible(false);
                basicInfoPanel.setVisible(true);
            } else {
                dotBasic.setToolTipText(STR_MORE);
                basicShort.setVisible(true);
                basicInfoPanel.setVisible(false);
            }
        } else if (e.getSource() == dotWork) {
            if (workPanel == null) {
                workPanel = new WorkEducationSkillPanel(MyFnFSettings.LOGIN_USER_ID, TYPE_WORK);
                workInfoPanel.add(workPanel, BorderLayout.CENTER);
                workInfoPanel.revalidate();
                workInfoPanel.repaint();
            }
            if (workShort.isVisible()) {
                dotWork.setToolTipText(STR_LESS);
                workShort.setVisible(false);
                workInfoPanel.setVisible(true);
            } else {
                dotWork.setToolTipText(STR_MORE);
                workShort.setVisible(true);
                workInfoPanel.setVisible(false);
            }
        } else if (e.getSource() == dotEducation) {
            if (educationPanel == null) {
                educationPanel = new WorkEducationSkillPanel(MyFnFSettings.LOGIN_USER_ID, TYPE_EDUCATION);
                educationInfoPanel.add(educationPanel, BorderLayout.CENTER);
                educationInfoPanel.revalidate();
                educationInfoPanel.repaint();
            }
            if (educationShort.isVisible()) {
                dotEducation.setToolTipText(STR_LESS);
                educationShort.setVisible(false);
                educationInfoPanel.setVisible(true);
            } else {
                dotEducation.setToolTipText(STR_MORE);
                educationShort.setVisible(true);
                educationInfoPanel.setVisible(false);
            }
        }
    }
    /*    ActionListener actionListener = new ActionListener() {

     @Override
     public void actionPerformed(ActionEvent e) {
     if (e.getSource() == dotSkill) {
     if (skillPanel == null) {
     skillPanel = new WorkEducationSkillPanel(MyFnFSettings.LOGIN_USER_ID, TYPE_SKILL);
     skillInfoPanel.add(skillPanel, BorderLayout.CENTER);
     skillInfoPanel.revalidate();
     skillInfoPanel.repaint();
     }
     if (skillShort.isVisible()) {
     dotSkill.setToolTipText(STR_LESS);
     skillShort.setVisible(false);
     skillInfoPanel.setVisible(true);
     } else {
     dotSkill.setToolTipText(STR_MORE);
     skillShort.setVisible(true);
     skillInfoPanel.setVisible(false);
     }
     } else if (e.getSource() == dotBasic) {
     if (basicShort.isVisible()) {
     dotBasic.setToolTipText(STR_LESS);
     basicShort.setVisible(false);
     basicInfoPanel.setVisible(true);
     } else {
     dotBasic.setToolTipText(STR_MORE);
     basicShort.setVisible(true);
     basicInfoPanel.setVisible(false);
     }
     } else if (e.getSource() == dotWork) {
     if (workPanel == null) {
     workPanel = new WorkEducationSkillPanel(MyFnFSettings.LOGIN_USER_ID, TYPE_WORK);
     workInfoPanel.add(workPanel, BorderLayout.CENTER);
     workInfoPanel.revalidate();
     workInfoPanel.repaint();
     }
     if (workShort.isVisible()) {
     dotWork.setToolTipText(STR_LESS);
     workShort.setVisible(false);
     workInfoPanel.setVisible(true);
     } else {
     dotWork.setToolTipText(STR_MORE);
     workShort.setVisible(true);
     workInfoPanel.setVisible(false);
     }
     } else if (e.getSource() == dotEducation) {
     if (educationPanel == null) {
     educationPanel = new WorkEducationSkillPanel(MyFnFSettings.LOGIN_USER_ID, TYPE_EDUCATION);
     educationInfoPanel.add(educationPanel, BorderLayout.CENTER);
     educationInfoPanel.revalidate();
     educationInfoPanel.repaint();
     }
     if (educationShort.isVisible()) {
     dotEducation.setToolTipText(STR_LESS);
     educationShort.setVisible(false);
     educationInfoPanel.setVisible(true);
     } else {
     dotEducation.setToolTipText(STR_MORE);
     educationShort.setVisible(true);
     educationInfoPanel.setVisible(false);
     }
     } else if (e.getSource() == addSkill) {

     } else if (e.getSource() == addWork) {
     createWorkSinglePanelInstance = new CreateWorkSinglePanel();
     workNew.removeAll();
     workNew.add(createWorkSinglePanelInstance, BorderLayout.CENTER);
     workNew.revalidate();
     workNew.repaint();
     workNew.setVisible(true);
     }
     }
     };
     private void action_show_basic() {
     basicInfoPanel.setVisible(true);
     basicInfoHolder.setBorder(new EmptyBorder(1, 2, 1, 1));
     basicCenter.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
     basicArrow.setIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_UP));
     basicArrow.setRolloverIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_UP_H));
     basicArrow.setPressedIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_UP_H));
     basicArrow.setVisible(true);
     basicLabel.setForeground(RingColorCode.RING_THEME_COLOR);
     basicWrapper.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
     }

     private void action_hide_basic() {
     basicInfoPanel.setVisible(false);
     //basicInfoHolder.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 2, 0, 0, RingColorCode.RING_THEME_COLOR),
     //        new MatteBorder(1, 0, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR)));
     basicCenter.setBorder(new EmptyBorder(0, 0, 1, 0));
     basicArrow.setIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_DOWN));
     basicArrow.setRolloverIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_DOWN_H));
     basicArrow.setPressedIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_DOWN_H));
     //basicArrow.setVisible(true);
     // basicLabel.setForeground(RingColorCode.RING_THEME_COLOR);
     basicWrapper.setBorder(new EmptyBorder(1, 1, 1, 1));
     basicArrow.setVisible(false);
     basicLabel.setForeground(null);
     basicInfoHolder.setBorder(new EmptyBorder(1, 2, 1, 1));
     }

     private void action_show_living() {
     livingInfoPanel.setVisible(true);
     livingInfoHolder.setBorder(new EmptyBorder(1, 2, 1, 1));
     livingCenter.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
     livingArrow.setIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_UP));
     livingArrow.setRolloverIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_UP_H));
     livingArrow.setPressedIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_UP_H));
     livingArrow.setVisible(true);
     livingLabel.setForeground(RingColorCode.RING_THEME_COLOR);
     livingWrapper.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
     }

     private void action_hide_living() {
     livingInfoPanel.setVisible(false);
     //livingInfoHolder.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 2, 0, 0, RingColorCode.RING_THEME_COLOR),
     //        new MatteBorder(1, 0, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR)));
     livingCenter.setBorder(new EmptyBorder(0, 0, 1, 0));
     livingArrow.setIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_DOWN));
     livingArrow.setRolloverIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_DOWN_H));
     livingArrow.setPressedIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_DOWN_H));
     //livingArrow.setVisible(true);
     //livingLabel.setForeground(RingColorCode.RING_THEME_COLOR);
     livingWrapper.setBorder(new EmptyBorder(1, 1, 1, 1));
     livingArrow.setVisible(false);
     livingLabel.setForeground(null);
     livingInfoHolder.setBorder(new EmptyBorder(1, 2, 1, 1));
     }

     private void action_show_work() {
     workInfoPanel.setVisible(true);
     workInfoHolder.setBorder(new EmptyBorder(1, 2, 1, 1));
     workCenter.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
     workArrow.setIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_UP));
     workArrow.setRolloverIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_UP_H));
     workArrow.setPressedIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_UP_H));
     workArrow.setVisible(true);
     workLabel.setForeground(RingColorCode.RING_THEME_COLOR);
     workWrapper.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
     }

     private void action_hide_work() {
     workInfoPanel.setVisible(false);
     //  workInfoHolder.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 2, 0, 0, RingColorCode.RING_THEME_COLOR),
     //         new MatteBorder(1, 0, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR)));
     workCenter.setBorder(new EmptyBorder(0, 0, 1, 0));
     workArrow.setIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_DOWN));
     workArrow.setRolloverIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_DOWN_H));
     workArrow.setPressedIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_DOWN_H));
     //workArrow.setVisible(true);
     // workLabel.setForeground(RingColorCode.RING_THEME_COLOR);
     workWrapper.setBorder(new EmptyBorder(1, 1, 1, 1));
     workArrow.setVisible(false);
     workLabel.setForeground(null);
     workInfoHolder.setBorder(new EmptyBorder(1, 2, 1, 1));
     }

     private void action_show_education() {
     educationInfoPanel.setVisible(true);
     educationInfoHolder.setBorder(new EmptyBorder(1, 2, 1, 1));
     educationCenter.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
     educationArrow.setIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_UP));
     educationArrow.setRolloverIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_UP_H));
     educationArrow.setPressedIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_UP_H));
     educationArrow.setVisible(true);
     educationLabel.setForeground(RingColorCode.RING_THEME_COLOR);
     educationWrapper.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
     }

     private void action_hide_education() {
     educationInfoPanel.setVisible(false);
     // educationInfoHolder.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 2, 0, 0, RingColorCode.RING_THEME_COLOR),
     //         new MatteBorder(1, 0, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR)));
     educationCenter.setBorder(new EmptyBorder(0, 0, 1, 0));
     educationArrow.setIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_DOWN));
     educationArrow.setRolloverIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_DOWN_H));
     educationArrow.setPressedIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_DOWN_H));
     // educationArrow.setVisible(true);
     // educationLabel.setForeground(RingColorCode.RING_THEME_COLOR);
     educationWrapper.setBorder(new EmptyBorder(1, 1, 1, 1));
     educationArrow.setVisible(false);
     educationLabel.setForeground(null);
     educationInfoHolder.setBorder(new EmptyBorder(1, 2, 1, 1));
     }

     private void action_show_skill() {
     skillInfoPanel.setVisible(true);
     skillInfoHolder.setBorder(new EmptyBorder(1, 2, 1, 1));
     skillCenter.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
     skillArrow.setIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_UP));
     skillArrow.setRolloverIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_UP_H));
     skillArrow.setPressedIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_UP_H));
     skillArrow.setVisible(true);
     skillLabel.setForeground(RingColorCode.RING_THEME_COLOR);
     skillWrapper.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
     }

     private void action_hide_skill() {
     skillInfoPanel.setVisible(false);
     //skillInfoHolder.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 2, 0, 0, RingColorCode.RING_THEME_COLOR),
     //        new MatteBorder(1, 0, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR)));
     skillCenter.setBorder(new EmptyBorder(0, 0, 1, 0));
     skillArrow.setIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_DOWN));
     skillArrow.setRolloverIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_DOWN_H));
     skillArrow.setPressedIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_DOWN_H));
     //skillArrow.setVisible(true);
     //skillLabel.setForeground(RingColorCode.RING_THEME_COLOR);
     skillWrapper.setBorder(new EmptyBorder(1, 1, 1, 1));
     skillArrow.setVisible(false);
     skillLabel.setForeground(null);
     skillInfoHolder.setBorder(new EmptyBorder(1, 2, 1, 1));
     }

     private void action_show_aboutMe() {
     aboutMeInfoPanel.setVisible(true);
     aboutMeInfoHolder.setBorder(new EmptyBorder(1, 2, 1, 1));
     aboutMeCenter.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
     aboutMeArrow.setIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_UP));
     aboutMeArrow.setRolloverIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_UP_H));
     aboutMeArrow.setPressedIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_UP_H));
     aboutMeArrow.setVisible(true);
     aboutMeLabel.setForeground(RingColorCode.RING_THEME_COLOR);
     aboutMeWrapper.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
     }

     private void action_hide_aboutMe() {
     aboutMeInfoPanel.setVisible(false);
     //aboutMeInfoHolder.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 2, 0, 0, RingColorCode.RING_THEME_COLOR),
     //         new MatteBorder(1, 0, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR)));
     aboutMeCenter.setBorder(new EmptyBorder(0, 0, 1, 0));
     aboutMeArrow.setIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_DOWN));
     aboutMeArrow.setRolloverIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_DOWN_H));
     aboutMeArrow.setPressedIcon(DesignClasses.return_image(GetImages.ABOUT_ARROW_DOWN_H));
     //aboutMeArrow.setVisible(true);
     // aboutMeLabel.setForeground(RingColorCode.RING_THEME_COLOR);
     aboutMeWrapper.setBorder(new EmptyBorder(1, 1, 1, 1));
     aboutMeArrow.setVisible(false);
     aboutMeLabel.setForeground(null);
     aboutMeInfoHolder.setBorder(new EmptyBorder(1, 2, 1, 1));
     }

     private JPanel createAboutMePanel(final JLabel editlabelName, String value) {
     JPanel containerWrapper = new JPanel(new BorderLayout());
     containerWrapper.setOpaque(false);

     JPanel sizePanel1 = new JPanel();
     sizePanel1.setOpaque(false);
     sizePanel1.setPreferredSize(new Dimension(600, 1));
     containerWrapper.add(sizePanel1, BorderLayout.NORTH);

     JPanel contanier = new JPanel();
     contanier.setOpaque(false);
     contanier.setLayout(new BoxLayout(contanier, BoxLayout.X_AXIS));

     contanier.add(Box.createRigidArea(new Dimension(15, 0)));
     containerWrapper.add(contanier, BorderLayout.CENTER);

     JPanel valuePanel = new JPanel(new BorderLayout());
     valuePanel.setOpaque(false);

     JPanel sizePanel = new JPanel();
     sizePanel.setOpaque(false);

     sizePanel.setPreferredSize(new Dimension(200, 5));
     valuePanel.add(new TextPanelWithEmoticon(403, value), BorderLayout.CENTER);

     valuePanel.add(sizePanel, BorderLayout.NORTH);
     JPanel sizePanel2 = new JPanel();
     sizePanel2.setOpaque(false);

     sizePanel2.setPreferredSize(new Dimension(200, 5));
     valuePanel.add(sizePanel2, BorderLayout.SOUTH);

     JPanel privacyandEditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
     privacyandEditPanel.setOpaque(false);
     privacyandEditPanel.setPreferredSize(new Dimension(53, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));

     JPanel editPanel = new JPanel(new BorderLayout());
     editPanel.setOpaque(false);
     editPanel.setPreferredSize(new Dimension(20, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
     if (editlabelName != null) {
     editlabelName.addMouseListener(this);
     editlabelName.setToolTipText("Edit");
     editPanel.add(editlabelName, BorderLayout.CENTER);
     if (InternetUnavailablityCheck.isInternetAvailable) {
     editlabelName.show();
     } else {
     editlabelName.hide();
     }
     }
     JPanel privacyandEditHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
     privacyandEditHolder.add(editPanel);
     privacyandEditHolder.setOpaque(false);
     contanier.add(valuePanel);
     contanier.add(privacyandEditHolder);

     return containerWrapper;
     }

     MouseListener mouseListener = new MouseAdapter() {
     @Override
     public void mouseClicked(MouseEvent e) {
     if (e.getSource() == livingInfoHolder) {
     if (!livingInfoPanel.isVisible()) {
     action_show_living();
     action_hide_basic();
     action_hide_skill();
     action_hide_aboutMe();
     action_hide_education();
     action_hide_work();
     } else {
     action_hide_living();
     }
     } else if (e.getSource() == basicInfoHolder) {
     if (!basicInfoPanel.isVisible()) {
     action_hide_living();
     action_show_basic();
     action_hide_skill();
     action_hide_aboutMe();
     action_hide_education();
     action_hide_work();
     } else {
     action_hide_basic();
     }
     } else if (e.getSource() == workInfoHolder) {
     if (!workInfoPanel.isVisible()) {
     workEducationSkillPanel = new WorkEducationSkillPanel(MyFnFSettings.LOGIN_USER_ID, 1);/////////////
     workInfoPanel.add(workEducationSkillPanel, BorderLayout.CENTER);///////////
     action_hide_living();
     action_hide_basic();
     action_hide_skill();
     action_hide_aboutMe();
     action_hide_education();
     action_show_work();
     } else {
     action_hide_work();
     }
     } else if (e.getSource() == educationInfoHolder) {
     if (!educationInfoPanel.isVisible()) {
     workEducationSkillPanel = new WorkEducationSkillPanel(MyFnFSettings.LOGIN_USER_ID, 2);/////////
     educationInfoPanel.add(workEducationSkillPanel, BorderLayout.CENTER);//////////
     action_hide_living();
     action_hide_basic();
     action_hide_skill();
     action_hide_aboutMe();
     action_show_education();
     action_hide_work();
     } else {
     action_hide_education();
     }
     } else if (e.getSource() == skillInfoHolder) {
     if (!skillInfoPanel.isVisible()) {
     workEducationSkillPanel = new WorkEducationSkillPanel(MyFnFSettings.LOGIN_USER_ID, 4);////////////
     skillInfoPanel.add(workEducationSkillPanel, BorderLayout.CENTER);///////////
     action_hide_living();
     action_hide_basic();
     action_show_skill();
     action_hide_aboutMe();
     action_hide_education();
     action_hide_work();
     } else {
     action_hide_skill();
     }
     } else if (e.getSource() == aboutMeInfoHolder) {
     if (!aboutMeInfoPanel.isVisible()) {
     action_hide_living();
     action_hide_basic();
     action_hide_skill();
     action_show_aboutMe();
     action_hide_education();
     action_hide_work();
     } else {
     action_hide_aboutMe();
     }
     }
     }

     @Override
     public void mouseEntered(MouseEvent e) {
     if (e.getSource() == livingInfoHolder && !livingInfoPanel.isVisible()) {
     livingArrow.setVisible(true);
     livingLabel.setForeground(RingColorCode.RING_THEME_COLOR);
     livingInfoHolder.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 2, 0, 0, RingColorCode.RING_THEME_COLOR),
     new MatteBorder(1, 0, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR)));
     } else if (e.getSource() == basicInfoHolder && !basicInfoPanel.isVisible()) {
     basicArrow.setVisible(true);
     basicLabel.setForeground(RingColorCode.RING_THEME_COLOR);
     basicInfoHolder.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 2, 0, 0, RingColorCode.RING_THEME_COLOR),
     new MatteBorder(1, 0, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR)));
     } else if (e.getSource() == aboutMeInfoHolder && !aboutMeInfoPanel.isVisible()) {
     aboutMeArrow.setVisible(true);
     aboutMeLabel.setForeground(RingColorCode.RING_THEME_COLOR);
     aboutMeInfoHolder.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 2, 0, 0, RingColorCode.RING_THEME_COLOR),
     new MatteBorder(1, 0, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR)));
     } else if (e.getSource() == workInfoHolder && !workInfoPanel.isVisible()) {
     workArrow.setVisible(true);
     workLabel.setForeground(RingColorCode.RING_THEME_COLOR);
     workInfoHolder.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 2, 0, 0, RingColorCode.RING_THEME_COLOR),
     new MatteBorder(1, 0, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR)));
     } else if (e.getSource() == educationInfoHolder && !educationInfoPanel.isVisible()) {
     educationArrow.setVisible(true);
     educationLabel.setForeground(RingColorCode.RING_THEME_COLOR);
     educationInfoHolder.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 2, 0, 0, RingColorCode.RING_THEME_COLOR),
     new MatteBorder(1, 0, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR)));
     } else if (e.getSource() == skillInfoHolder && !skillInfoPanel.isVisible()) {
     skillArrow.setVisible(true);
     skillLabel.setForeground(RingColorCode.RING_THEME_COLOR);
     skillInfoHolder.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 2, 0, 0, RingColorCode.RING_THEME_COLOR),
     new MatteBorder(1, 0, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR)));
     }
     }

     @Override
     public void mouseExited(MouseEvent e) {
     if (e.getSource() == livingInfoHolder && !livingInfoPanel.isVisible()) {
     livingArrow.setVisible(false);
     livingLabel.setForeground(null);
     livingInfoHolder.setBorder(new EmptyBorder(1, 2, 1, 1));
     } else if (e.getSource() == basicInfoHolder && !basicInfoPanel.isVisible()) {
     basicArrow.setVisible(false);
     basicLabel.setForeground(null);
     basicInfoHolder.setBorder(new EmptyBorder(1, 2, 1, 1));
     } else if (e.getSource() == aboutMeInfoHolder && !aboutMeInfoPanel.isVisible()) {
     aboutMeArrow.setVisible(false);
     aboutMeLabel.setForeground(null);
     aboutMeInfoHolder.setBorder(new EmptyBorder(1, 2, 1, 1));
     } else if (e.getSource() == educationInfoHolder && !educationInfoPanel.isVisible()) {
     educationArrow.setVisible(false);
     educationLabel.setForeground(null);
     educationInfoHolder.setBorder(new EmptyBorder(1, 2, 1, 1));
     } else if (e.getSource() == workInfoHolder && !workInfoPanel.isVisible()) {
     workArrow.setVisible(false);
     workLabel.setForeground(null);
     workInfoHolder.setBorder(new EmptyBorder(1, 2, 1, 1));
     } else if (e.getSource() == skillInfoHolder && !skillInfoPanel.isVisible()) {
     skillArrow.setVisible(false);
     skillLabel.setForeground(null);
     skillInfoHolder.setBorder(new EmptyBorder(1, 2, 1, 1));
     }
     }
     };
     if (e.getSource() == livingLabel || e.getSource() == livingIcon) {
     basicIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     basicInfoPanel.setVisible(false);
     workIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     workInfoPanel.setVisible(false);
     educationIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     educationInfoPanel.setVisible(false);
     skillIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     skillInfoPanel.setVisible(false);
     aboutMeIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     aboutMeInfoPanel.setVisible(false);

     if (!livingInfoPanel.isVisible()) {
     livingInfoPanel.setVisible(true);
     livingIcon.setIcon(return_image(GetImages.IMAGE_BOTTOM_ARROW_BIG_H));
     } else {
     livingIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     livingInfoPanel.setVisible(false);
     }
     } else if (e.getSource() == basicLabel || e.getSource() == basicIcon) {
     workIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     workInfoPanel.setVisible(false);
     educationIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     educationInfoPanel.setVisible(false);
     skillIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     skillInfoPanel.setVisible(false);
     aboutMeIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     aboutMeInfoPanel.setVisible(false);
     livingIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     livingInfoPanel.setVisible(false);

     if (!basicInfoPanel.isVisible()) {
     basicInfoPanel.setVisible(true);
     basicIcon.setIcon(return_image(GetImages.IMAGE_BOTTOM_ARROW_BIG_H));
     } else {
     basicIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     basicInfoPanel.setVisible(false);
     }
     } else if (e.getSource() == workLabel || e.getSource() == workIcon) {
     basicIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     basicInfoPanel.setVisible(false);
     educationIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     educationInfoPanel.setVisible(false);
     skillIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     skillInfoPanel.setVisible(false);
     aboutMeIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     aboutMeInfoPanel.setVisible(false);
     livingIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     livingInfoPanel.setVisible(false);

     workInfoPanel.removeAll();
     if (!workInfoPanel.isVisible()) {
     workEducationSkillPanel = new WorkEducationSkillPanel(MyFnFSettings.LOGIN_USER_ID, 1);/////////////
     workInfoPanel.add(workEducationSkillPanel, BorderLayout.CENTER);///////////
     workIcon.setIcon(return_image(GetImages.IMAGE_BOTTOM_ARROW_BIG_H));
     workInfoPanel.setVisible(true);
     } else {
     workIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     workInfoPanel.setVisible(false);
     }
     workInfoPanel.revalidate();
     } else if (e.getSource() == educationLabel || e.getSource() == educationIcon) {
     basicIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     basicInfoPanel.setVisible(false);
     workIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     workInfoPanel.setVisible(false);
     skillIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     skillInfoPanel.setVisible(false);
     aboutMeIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     aboutMeInfoPanel.setVisible(false);
     livingIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     livingInfoPanel.setVisible(false);

     educationInfoPanel.removeAll();
     if (!educationInfoPanel.isVisible()) {
     workEducationSkillPanel = new WorkEducationSkillPanel(MyFnFSettings.LOGIN_USER_ID, 2);/////////
     educationInfoPanel.add(workEducationSkillPanel, BorderLayout.CENTER);//////////
     educationIcon.setIcon(return_image(GetImages.IMAGE_BOTTOM_ARROW_BIG_H));
     educationInfoPanel.setVisible(true);
     } else {
     educationIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     educationInfoPanel.setVisible(false);
     }
     educationInfoPanel.revalidate();
     } else if (e.getSource() == skillLabel || e.getSource() == skillIcon) {
     basicIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     basicInfoPanel.setVisible(false);
     educationIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     educationInfoPanel.setVisible(false);
     workIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     workInfoPanel.setVisible(false);
     aboutMeIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     aboutMeInfoPanel.setVisible(false);
     livingIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     livingInfoPanel.setVisible(false);

     skillInfoPanel.removeAll();
     if (!skillInfoPanel.isVisible()) {
     workEducationSkillPanel = new WorkEducationSkillPanel(MyFnFSettings.LOGIN_USER_ID, 4);////////////
     skillInfoPanel.add(workEducationSkillPanel, BorderLayout.CENTER);///////////
     skillIcon.setIcon(return_image(GetImages.IMAGE_BOTTOM_ARROW_BIG_H));
     skillInfoPanel.setVisible(true);
     } else {
     skillIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     skillInfoPanel.setVisible(false);
     }
     skillInfoPanel.revalidate();
     } else if (e.getSource() == aboutMeLabel || e.getSource() == aboutMeIcon) {
     workIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     workInfoPanel.setVisible(false);
     educationIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     educationInfoPanel.setVisible(false);
     skillIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     skillInfoPanel.setVisible(false);
     basicIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     basicInfoPanel.setVisible(false);
     livingIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     livingInfoPanel.setVisible(false);

     if (!aboutMeInfoPanel.isVisible()) {
     aboutMeInfoPanel.setVisible(true);
     aboutMeIcon.setIcon(return_image(GetImages.IMAGE_BOTTOM_ARROW_BIG_H));
     } else {
     aboutMeIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
     aboutMeInfoPanel.setVisible(false);
     }
     } else*/
}
