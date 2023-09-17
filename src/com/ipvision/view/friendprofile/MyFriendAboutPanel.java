/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.friendprofile;

import com.ipvision.constants.MaxLengths;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import static com.ipvision.view.utility.DesignClasses.return_image;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.view.utility.HtmlHelpers;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.constants.StaticFields;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import com.ipvision.constants.XMLConstants;
import com.ipvision.model.constants.StatusConstants;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.border.MatteBorder;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.view.myprofile.WorkEducationSkillPanel;
import com.ipvision.model.stores.FriendList;
import com.ipvision.view.utility.TextPanelWithEmoticon;

/**
 * @author Shahadat Hossain
 */
public class MyFriendAboutPanel extends JPanel implements MouseListener {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MyFriendAboutPanel.class);
    private MyFriendProfile myFriendProfile;
    public JScrollPane freindInfoScorllPanel;
    private JLabel friendNameLabel;

    private Pattern reqularex;
    private HtmlHelpers htmlHelp;

    private JPanel whatInMind;
    private JPanel fullName;
    private JPanel phoneNumber;
    private JPanel gender;
    private JPanel birthDay, email;
    private JPanel ringEmail;
    public JPanel currentCity, homeCity, marriageDay, aboutMe;
    private JPanel singleBasicInfo, singleLivingInfo, singleAboutMe;
    public GridBagConstraints conBasic, conLiving, conAboutMe;
    private JPanel allInfo;
    private JLabel livingInfo, basicInfo, workInfo, educationInfo, skillInfo, aboutMeInfo;
    private JLabel livingIcon, basicIcon, workIcon, educationIcon, skillIcon, aboutMeIcon;
    private JPanel basicInfoPanel, livingInfoPanel, workInfoPanel, educationInfoPanel, skillInfoPanel, aboutMeInfoPanel;
    private JPanel basicInfoHolder, livingInfoHolder, workInfoHolder, educationInfoHolder, skillInfoHolder, aboutMeInfoHolder;
    int width = 290;
    int commonHeight = 45;
    int gap = 3;
    int flag, clik;
    public GridBagConstraints con;
    public GridBagConstraints gd, gbc, conAllInfo;
    private String mobileNmuber;
    private WorkEducationSkillPanel workEducationSkillPanel;
    private UserBasicInfo frndInfo;

    public WorkEducationSkillPanel getWorkandEducationPanel() {
        return workEducationSkillPanel;
    }

    public MyFriendAboutPanel(MyFriendProfile myFriendProfile) {
        htmlHelp = new HtmlHelpers();
        if (XMLConstants.EMOTION_REGULAR_EXPRESSION != null) {
            reqularex = Pattern.compile(XMLConstants.EMOTION_REGULAR_EXPRESSION);
        }
        //      setBorder(new MatteBorder(1, 1, 1, 1, DefaultSettings.DEFAULT_BOOK_BORDER_COLOR));
        this.setBorder(new EmptyBorder(5, 0, 0, 0));
        this.setLayout(new BorderLayout());
        // this.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);;
        setOpaque(false);
        this.myFriendProfile = myFriendProfile;
        this.initContents();
    }

    private void initContents() {
        JPanel friendInformations = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        allInfo = new JPanel();
        allInfo.setBackground(Color.WHITE);
        allInfo.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR));
        // allInfo.setBorder(new EmptyBorder(0, 19, 7, 0));
        allInfo.setLayout(new GridBagLayout());
        conAllInfo = new GridBagConstraints();
        conAllInfo.gridx = 0;
        conAllInfo.gridy = 0;
        conAllInfo.anchor = GridBagConstraints.WEST;
        conAllInfo.insets = new Insets(5, 15, 5, 0);
        allInfo.add(Box.createRigidArea(new Dimension(DefaultSettings.COVER_PIC_DISPLAY_WIDTH - 17, 1)), conAllInfo);
        conAllInfo.gridy++;
        /////////////////////
        basicInfoPanel = new JPanel(new BorderLayout());
        basicInfoPanel.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
        basicInfoPanel.setBackground(RingColorCode.ABOUT_PANEL_BG);
        basicInfo = DesignClasses.makeJLabelFullName2("Basic Information", 13);
        basicInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        basicInfo.addMouseListener(this);
        basicIcon = DesignClasses.create_imageJlabel(GetImages.IMAGE_RIGHT_ARROW_BIG_H);
        basicIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        basicIcon.addMouseListener(this);
        basicInfoHolder = new JPanel(new BorderLayout(4, 0));
        basicInfoHolder.setOpaque(false);
        basicInfoHolder.add(basicInfo, BorderLayout.CENTER);
        basicInfoHolder.add(basicIcon, BorderLayout.WEST);
        allInfo.add(basicInfoHolder, conAllInfo);
        conAllInfo.gridy++;
        allInfo.add(basicInfoPanel, conAllInfo);
        conAllInfo.gridy++;
        basicInfoPanel.setVisible(true);
        ////////////////////    
        if (myFriendProfile.getFriendProfileInfo().getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED) {
            clik = 1;
            livingInfoPanel = new JPanel(new BorderLayout());
            livingInfoPanel.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
            livingInfoPanel.setBackground(RingColorCode.ABOUT_PANEL_BG);
            livingInfo = DesignClasses.makeJLabelFullName2("Living", 13);
            livingInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            livingInfo.addMouseListener(this);
            livingIcon = DesignClasses.create_imageJlabel(GetImages.IMAGE_RIGHT_ARROW_BIG_H);
            livingIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
            livingIcon.addMouseListener(this);
            livingInfoHolder = new JPanel(new BorderLayout(4, 0));
            livingInfoHolder.setOpaque(false);
            livingInfoHolder.add(livingInfo, BorderLayout.CENTER);
            livingInfoHolder.add(livingIcon, BorderLayout.WEST);
            allInfo.add(livingInfoHolder, conAllInfo);
            conAllInfo.gridy++;
            allInfo.add(livingInfoPanel, conAllInfo);
            conAllInfo.gridy++;
            livingInfoPanel.setVisible(false);
            //////////////// 
            educationInfoPanel = new JPanel(new BorderLayout());
            educationInfoPanel.setBackground(RingColorCode.ABOUT_PANEL_BG);
            educationInfo = DesignClasses.makeJLabelFullName2("Education", 13);
            educationInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            educationInfo.addMouseListener(this);
            educationIcon = DesignClasses.create_imageJlabel(GetImages.IMAGE_RIGHT_ARROW_BIG_H);
            educationIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
            educationIcon.addMouseListener(this);
            educationInfoHolder = new JPanel(new BorderLayout(4, 0));
            educationInfoHolder.setOpaque(false);
            educationInfoHolder.add(educationInfo, BorderLayout.CENTER);
            educationInfoHolder.add(educationIcon, BorderLayout.WEST);
            allInfo.add(educationInfoHolder, conAllInfo);
            conAllInfo.gridy++;
            allInfo.add(educationInfoPanel, conAllInfo);
            conAllInfo.gridy++;
            educationInfoPanel.setVisible(false);
            ////////////
            workInfoPanel = new JPanel(new BorderLayout());
            workInfoPanel.setBackground(RingColorCode.ABOUT_PANEL_BG);
            workInfo = DesignClasses.makeJLabelFullName2("Work", 13);
            workInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            workInfo.addMouseListener(this);
            workIcon = DesignClasses.create_imageJlabel(GetImages.IMAGE_RIGHT_ARROW_BIG_H);
            workIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
            workIcon.addMouseListener(this);
            workInfoHolder = new JPanel(new BorderLayout(4, 0));
            workInfoHolder.setOpaque(false);
            workInfoHolder.add(workInfo, BorderLayout.CENTER);
            workInfoHolder.add(workIcon, BorderLayout.WEST);
            allInfo.add(workInfoHolder, conAllInfo);
            conAllInfo.gridy++;
            allInfo.add(workInfoPanel, conAllInfo);
            conAllInfo.gridy++;
            workInfoPanel.setVisible(false);
            //////////////
            skillInfoPanel = new JPanel(new BorderLayout());
            skillInfoPanel.setBackground(RingColorCode.ABOUT_PANEL_BG);
            skillInfo = DesignClasses.makeJLabelFullName2("Skill", 13);
            skillInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            skillInfo.addMouseListener(this);
            skillIcon = DesignClasses.create_imageJlabel(GetImages.IMAGE_RIGHT_ARROW_BIG_H);
            skillIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
            skillIcon.addMouseListener(this);
            skillInfoHolder = new JPanel(new BorderLayout(4, 0));
            skillInfoHolder.setOpaque(false);
            skillInfoHolder.add(skillInfo, BorderLayout.CENTER);
            skillInfoHolder.add(skillIcon, BorderLayout.WEST);
            allInfo.add(skillInfoHolder, conAllInfo);
            conAllInfo.gridy++;
            allInfo.add(skillInfoPanel, conAllInfo);
            conAllInfo.gridy++;
            skillInfoPanel.setVisible(false);
            //////////////
            aboutMeInfoPanel = new JPanel(new BorderLayout());
            aboutMeInfoPanel.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
            aboutMeInfoPanel.setBackground(RingColorCode.ABOUT_PANEL_BG);
            aboutMeInfo = DesignClasses.makeJLabelFullName2("About Me", 13);
            aboutMeInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            aboutMeInfo.addMouseListener(this);
            aboutMeIcon = DesignClasses.create_imageJlabel(GetImages.IMAGE_RIGHT_ARROW_BIG_H);
            aboutMeIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
            aboutMeIcon.addMouseListener(this);
            aboutMeInfoHolder = new JPanel(new BorderLayout(4, 0));
            aboutMeInfoHolder.setOpaque(false);
            aboutMeInfoHolder.add(aboutMeInfo, BorderLayout.CENTER);
            aboutMeInfoHolder.add(aboutMeIcon, BorderLayout.WEST);
            allInfo.add(aboutMeInfoHolder, conAllInfo);
            conAllInfo.gridy++;
            allInfo.add(aboutMeInfoPanel, conAllInfo);
            conAllInfo.gridy++;
            aboutMeInfoPanel.setVisible(false);
        }
        allInfo.add(Box.createRigidArea(new Dimension(2, 4)), conAllInfo);
        conAllInfo.gridy++;

        friendInformations.setOpaque(false);
        //friendInformations.setBorder(new MatteBorder(1, 1, 1, 1, DefaultSettings.DEFAULT_BOOK_BORDER_COLOR));
        //    friendInformations.setBorder(new EmptyBorder(5, 0, 0, 0));
        friendInformations.add(allInfo);
        freindInfoScorllPanel = DesignClasses.getDefaultScrollPane(friendInformations);

        this.add(freindInfoScorllPanel, BorderLayout.CENTER);

        loadFriendInformation();
    }

    private JPanel createUnitAboutPanel(String title, String value, final int fieldCons) {
        JPanel containerWrapper = new JPanel(new BorderLayout());
        containerWrapper.setOpaque(false);

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
        contanier.add(Box.createRigidArea(new Dimension(15, 0)));
        containerWrapper.add(contanier, BorderLayout.CENTER);

        JLabel name = editTitleText(title);
        JLabel valuelabel = editTitleText(value);
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.setPreferredSize(new Dimension(100, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        namePanel.setOpaque(false);
        namePanel.add(name, BorderLayout.CENTER);

        JLabel colon = editTitleText(":    ");
        JPanel colonPanel = new JPanel(new BorderLayout());
        colonPanel.setPreferredSize(new Dimension(20, DefaultSettings.ABOUT_SINGLE_PANEL_HEIGHT));
        colonPanel.setOpaque(false);
        colonPanel.add(colon, BorderLayout.CENTER);

        JPanel valuePanel = new JPanel(new BorderLayout());
        valuePanel.setOpaque(false);
        //valuePanel.setBackground(Color.red);

        JPanel sizePanel = new JPanel();
        sizePanel.setOpaque(false);

//        if (fieldCons == AppConstants.CONS_ABOUT_ME) {
//            sizePanel.setPreferredSize(new Dimension(200, 5));
//            valuePanel.add(new TextPanelWithEmoticon(135, value), BorderLayout.CENTER);
//        } else {
        sizePanel.setPreferredSize(new Dimension(200, 1));
        valuePanel.add(valuelabel, BorderLayout.CENTER);
//        }
        valuePanel.add(sizePanel, BorderLayout.NORTH);

        JPanel nameHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nameHolder.add(namePanel);
        JPanel colonHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
        colonHolder.add(colonPanel);
        nameHolder.setOpaque(false);
        colonHolder.setOpaque(false);

        contanier.add(nameHolder);
        contanier.add(colonHolder);
        contanier.add(valuePanel);

        return containerWrapper;
    }

    private JPanel createAboutMePanel(String value) {
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
        valuePanel.add(new TextPanelWithEmoticon(360, value), BorderLayout.CENTER);

        valuePanel.add(sizePanel, BorderLayout.NORTH);
        JPanel sizePanel2 = new JPanel();
        sizePanel2.setOpaque(false);

        sizePanel2.setPreferredSize(new Dimension(200, 5));
        valuePanel.add(sizePanel2, BorderLayout.SOUTH);
        contanier.add(valuePanel);

        return containerWrapper;
    }

    private JLabel editTitleText(String title) {
        return DesignClasses.makeJLabel_normal(title, 14, DefaultSettings.BLACK_FONT);
    }

    private void addRightContent() {
        try {
            singleBasicInfo = new JPanel();
            singleBasicInfo.setOpaque(false);
            singleBasicInfo.setLayout(new GridBagLayout());
            conBasic = new GridBagConstraints();
            conBasic.gridx = 0;
            conBasic.gridy = 0;
            conBasic.insets = new Insets(2, 2, 2, 2);
            basicInfoPanel.removeAll();
            basicInfoPanel.add(singleBasicInfo, BorderLayout.CENTER);
            fullName = nullPanel();

            if (myFriendProfile.getFriendProfileInfo().getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED) {
                singleLivingInfo = new JPanel();
                singleLivingInfo.setOpaque(false);
                singleLivingInfo.setLayout(new GridBagLayout());
                conLiving = new GridBagConstraints();
                conLiving.gridx = 0;
                conLiving.gridy = 0;
                livingInfoPanel.removeAll();
                livingInfoPanel.add(singleLivingInfo, BorderLayout.CENTER);
                conLiving.insets = new Insets(2, 2, 2, 2);
                singleAboutMe = new JPanel();
                singleAboutMe.setOpaque(false);
                singleAboutMe.setLayout(new GridBagLayout());
                conAboutMe = new GridBagConstraints();
                conAboutMe.gridx = 0;
                conAboutMe.gridy = 0;
                conAboutMe.insets = new Insets(2, 2, 2, 2);
                aboutMeInfoPanel.removeAll();
                aboutMeInfoPanel.add(singleAboutMe, BorderLayout.CENTER);

                phoneNumber = nullPanel();
                email = nullPanel();
                gender = nullPanel();
                birthDay = nullPanel();
                currentCity = nullPanel();
                homeCity = nullPanel();
                marriageDay = nullPanel();
                aboutMe = nullPanel();
            }
        } catch (Exception e) {
        }
    }

    public void loadFriendInformation() {
        addRightContent();
        if (myFriendProfile.getFriendProfileInfo() != null) {
            ///////////////////////////////////////
            if (myFriendProfile.getFriendProfileInfo().getFullName() != null && myFriendProfile.getFriendProfileInfo().getFullName().trim().length() > 0) {
                changeFullName();
                singleBasicInfo.add(fullName, conBasic);
                conBasic.gridy++;
            }
            if (myFriendProfile.getFriendProfileInfo().getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED) {
                if (myFriendProfile.getFriendProfileInfo().getGender() != null && myFriendProfile.getFriendProfileInfo().getGender().trim().length() > 0) {
                    changeGender();
                    singleBasicInfo.add(gender, conBasic);
                    conBasic.gridy++;
                }
                if (myFriendProfile.getFriendProfileInfo().getBirthday() != null && !myFriendProfile.getFriendProfileInfo().getBirthday().trim().equals("") && !myFriendProfile.getFriendProfileInfo().getBirthday().equalsIgnoreCase("null") && myFriendProfile.getFriendProfileInfo().getFriendShipStatus() != null) {
                    if (myFriendProfile.getFriendProfileInfo().getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED) {
                        if (myFriendProfile.getFriendProfileInfo().getBirthdayPrivacy() == AppConstants.PRIVACY_SHORT_ONLY_FRIEND || myFriendProfile.getFriendProfileInfo().getBirthdayPrivacy() == AppConstants.PRIVACY_SHORT_PUBLIC) {
                            changeBirthDay();
                            singleBasicInfo.add(birthDay, conBasic);
                            conBasic.gridy++;
                        }
                    } else {
                        if (myFriendProfile.getFriendProfileInfo().getBirthdayPrivacy() == AppConstants.PRIVACY_SHORT_PUBLIC) {
                            changeBirthDay();
                            singleBasicInfo.add(birthDay, conBasic);
                            conBasic.gridy++;
                        }
                    }
                }
                frndInfo = FriendList.getInstance().getFriend_hash_map().get(myFriendProfile.getFriendProfileInfo().getUserIdentity());
                if (frndInfo.getMarriageDay() != null && frndInfo.getMarriageDay().length() > 0) {
                    changeMarriageDay();
                    singleBasicInfo.add(marriageDay, conBasic);
                    conBasic.gridy++;
                }

                if (myFriendProfile.getFriendProfileInfo().getEmail() != null && !myFriendProfile.getFriendProfileInfo().getEmail().trim().equals("") && !myFriendProfile.getFriendProfileInfo().getEmail().equalsIgnoreCase("null") && myFriendProfile.getFriendProfileInfo().getFriendShipStatus() != null) {
                    if (myFriendProfile.getFriendProfileInfo().getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED) {
                        if (myFriendProfile.getFriendProfileInfo().getEmailPrivacy() == AppConstants.PRIVACY_SHORT_ONLY_FRIEND || myFriendProfile.getFriendProfileInfo().getEmailPrivacy() == AppConstants.PRIVACY_SHORT_PUBLIC) {
                            changeEmail();
                            singleBasicInfo.add(email, conBasic);
                            conBasic.gridy++;
                        }
                    } else {
                        if (myFriendProfile.getFriendProfileInfo().getEmailPrivacy() == AppConstants.PRIVACY_SHORT_PUBLIC) {
                            changeEmail();
                            singleBasicInfo.add(email, conBasic);
                            conBasic.gridy++;
                        }
                    }
                }
                if (myFriendProfile.getFriendProfileInfo().getMobilePhone() != null && HelperMethods.check_mobile_number(myFriendProfile.getFriendProfileInfo().getMobilePhone().trim()) && myFriendProfile.getFriendProfileInfo().getFriendShipStatus() != null) {
                    changemobileNumber();
                }
                singleBasicInfo.add(Box.createRigidArea(new Dimension(0, 5)), conBasic);
                conBasic.gridy++;
                /////////////////////////////////////////
                flag = 0;
                if (frndInfo.getCurrentCity() != null && !frndInfo.getCurrentCity().trim().equals("")) {
                    flag = 1;
                    changeCurrentCity();
                    singleLivingInfo.add(currentCity, conLiving);
                    conLiving.gridy++;
                }
                if (frndInfo.getHomeCity() != null && !frndInfo.getHomeCity().trim().equals("")) {
                    flag = 1;
                    changeHomeCity();
                    singleLivingInfo.add(homeCity, conLiving);
                    conLiving.gridy++;
                }

                //////////////////////////////
                if (frndInfo.getAboutMe() != null && !frndInfo.getAboutMe().trim().equals("")) {
                    changeAboutMe();
                    singleAboutMe.add(aboutMe, conAboutMe);
                    conAboutMe.gridy++;
                }
            }
            //////////////////////////////////

            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    freindInfoScorllPanel.getVerticalScrollBar().setValue(0);
                }
            });
        }
    }

    public void changeFriendFullName() {
        if (friendNameLabel != null) {
            if (myFriendProfile.getFriendProfileInfo().getFullName().trim().length() > 0 /*&& myFriendProfile.getFriendProfileInfo().getLastName() != null && myFriendProfile.getFriendProfileInfo().getLastName().trim().length() > 0*/) {
                friendNameLabel.setText(myFriendProfile.getFriendProfileInfo().getFullName()/*+ " " + myFriendProfile.getFriendProfileInfo().getLastName()*/);
            } else {
                friendNameLabel.setText("No name");
            }
            friendNameLabel.revalidate();
        }
    }

    public void changWhatinMind() {
        boolean textarea = true;
        if (whatInMind != null) {
            try {
                whatInMind.removeAll();
                whatInMind.setOpaque(false);

                if (myFriendProfile.getFriendProfileInfo().getWhatisInYourMind() != null
                        && myFriendProfile.getFriendProfileInfo().getWhatisInYourMind().trim().length() > 0
                        && !myFriendProfile.getFriendProfileInfo().getWhatisInYourMind().equalsIgnoreCase("null")) {
                    String replace = "";

                    JPanel titleandEditLabel = buildTitlePanel("Mood");
                    titleandEditLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, DefaultSettings.lightWhiteBorder));
                    titleandEditLabel.setPreferredSize(new Dimension(width - 20, 18));
                    whatInMind.add(titleandEditLabel, BorderLayout.PAGE_START);

                    replace = htmlHelp.replaceStringForEmoticons(myFriendProfile.getFriendProfileInfo().getWhatisInYourMind(), reqularex);
                    if (!replace.equals(myFriendProfile.getFriendProfileInfo().getWhatisInYourMind())) {
                        textarea = false;
                    }

                    if (textarea) {
                        String what_in_mind = "";
                        if (myFriendProfile.getFriendProfileInfo() != null && myFriendProfile.getFriendProfileInfo().getWhatisInYourMind() != null && myFriendProfile.getFriendProfileInfo().getWhatisInYourMind().trim().length() > 0) {
                            what_in_mind = myFriendProfile.getFriendProfileInfo().getWhatisInYourMind();
                        }
                        JTextArea whatArea = DesignClasses.createJTextArea(what_in_mind, 13, MaxLengths.WHATS_ON_MIND + 10);
                        whatArea.setBorder(null);
                        whatArea.setEditable(false);
                        whatInMind.add(whatArea, BorderLayout.CENTER);
                    } else {
                        HTMLDocument chat_doc;
                        HTMLEditorKit editorKit;
                        JTextPane area = new JTextPane();
                        area.setOpaque(false);
                        area.setEditable(false);
                        area.setContentType("text/html");
                        chat_doc = (HTMLDocument) area.getDocument();
                        editorKit = (HTMLEditorKit) area.getEditorKit();
                        String url = "<div  style=\"width: 220px\" >" + replace + "</div>";
                        try {
                            editorKit.insertHTML(chat_doc, chat_doc.getLength(), url, 0, 0, null);
                        } catch (BadLocationException ex) {
                        } catch (IOException ex) {
                        }
                        whatInMind.add(area, BorderLayout.CENTER);
                    }
                }
                whatInMind.revalidate();
                whatInMind.repaint();
            } catch (Exception e) {
                //  e.printStackTrace();
            }
        }
    }

    public void changeFullName() {
        if (fullName != null) {
            try {
                String name = myFriendProfile.getFriendProfileInfo().getFullName() /*+ " " + myFriendProfile.getFriendProfileInfo().getLastName()*/;
                fullName.removeAll();
                fullName.setOpaque(false);
                JPanel titleandEditLabel = createUnitAboutPanel("Name", name, AppConstants.CONS_FULL_NAME);
                fullName.add(titleandEditLabel, BorderLayout.CENTER);
                fullName.revalidate();
                fullName.repaint();
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Error in changeFullName ==>" + e.getMessage());
            }
        }
    }

    public void changemobileNumber() {
        if (phoneNumber != null) {
            try {
                phoneNumber.removeAll();
                int flag = 0;
                if (myFriendProfile.getFriendProfileInfo().getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED) {
                    if (myFriendProfile.getFriendProfileInfo().getMobilePrivacy() == AppConstants.PRIVACY_SHORT_PUBLIC || myFriendProfile.getFriendProfileInfo().getMobilePrivacy() == AppConstants.PRIVACY_SHORT_ONLY_FRIEND) {
                        mobileNmuber = HelperMethods.getPhoneNumber(myFriendProfile.getFriendProfileInfo().getMobilePhoneDialingCode(), myFriendProfile.getFriendProfileInfo().getMobilePhone());
                        flag = 1;
                    }
                } else {
                    mobileNmuber = HelperMethods.getUnknownProfilePhoneNumber(myFriendProfile.getFriendProfileInfo().getMobilePhoneDialingCode(), myFriendProfile.getFriendProfileInfo().getMobilePhone());
                    flag = 1;
                }
                if (flag == 1) {
                    singleBasicInfo.add(phoneNumber, conBasic);
                    conBasic.gridy++;
                    JPanel titleandEditLabel = createUnitAboutPanel(StaticFields.PHONE_NO_TEXT, mobileNmuber, AppConstants.CONS_MOBILE_PHONE);
                    phoneNumber.add(titleandEditLabel, BorderLayout.CENTER);

                    phoneNumber.revalidate();
                    phoneNumber.repaint();
                }
            } catch (Exception e) {
                //  e.printStackTrace();
                log.error("Error in changemobileNumber ==>" + e.getMessage());
            }
        }
    }

    public void changeGender() {
        if (gender != null) {
            try {
                gender.removeAll();
                gender.setOpaque(false);
                JPanel titleandEditLabel = createUnitAboutPanel(StaticFields.GENDER_TEXT, myFriendProfile.getFriendProfileInfo().getGender(), AppConstants.CONS_GENDER);
                gender.add(titleandEditLabel, BorderLayout.CENTER);
                gender.revalidate();
                gender.repaint();
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Error in changeGender ==>" + e.getMessage());
            }
        }
    }

    public void changeBirthDay() {
        if (birthDay != null) {
            try {
                birthDay.removeAll();
                birthDay.setOpaque(false);
                String strDate = "";
                Date d = new Date();
                d = new SimpleDateFormat("yyyy-MM-dd").parse(myFriendProfile.getFriendProfileInfo().getBirthday());
                strDate = new SimpleDateFormat("MMMM d, yyyy").format(d);
                JPanel titleandEditLabel = createUnitAboutPanel(StaticFields.BIRTH_DAY_TEXT, strDate, AppConstants.CONS_BIRTH_DAY);
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
                String city = frndInfo.getCurrentCity();
                currentCity.removeAll();
                currentCity.setOpaque(false);
                JPanel titleandEditLabel = createUnitAboutPanel(StaticFields.CURRENT_CITY_TEXT, city, AppConstants.CONS_CURRENT_CITY);
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
                String city = frndInfo.getHomeCity();
                homeCity.removeAll();
                homeCity.setOpaque(false);
                JPanel titleandEditLabel = createUnitAboutPanel(StaticFields.HOME_CITY_TEXT, city, AppConstants.CONS_HOME_CITY);
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
                JPanel titleandEditLabel = createUnitAboutPanel(StaticFields.MARRIAGE_TEXT, frndInfo.getMarriageDay(), AppConstants.CONS_MARRIAGE_DAY);
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
                String about = frndInfo.getAboutMe();
                aboutMe.removeAll();
                aboutMe.setOpaque(false);
                JPanel titleandEditLabel = createAboutMePanel(about);
                aboutMe.add(titleandEditLabel, BorderLayout.CENTER);
                aboutMe.revalidate();
                aboutMe.repaint();
            } catch (Exception e) {
               // e.printStackTrace();
            log.error("Error in changeAboutMe ==>" + e.getMessage());
            }
        }
    }

    public void changeEmail() {
        if (email != null) {
            try {
                String mail = myFriendProfile.getFriendProfileInfo().getEmail();
                email.removeAll();
                email.setOpaque(false);
                JPanel titleandEditLabel = createUnitAboutPanel(StaticFields.EMAIL_TEXT, mail, AppConstants.CONS_EMAIL);
                email.add(titleandEditLabel, BorderLayout.CENTER);
                email.revalidate();
                email.repaint();
            } catch (Exception e) {
                log.error("Mail change failed -->" + e.getMessage());
            }
        }
    }

    public void changeRingEmail() {
        if (ringEmail != null) {
            try {

                String rEmail = "";
                if (myFriendProfile.getFriendProfileInfo() != null && myFriendProfile.getFriendProfileInfo().getRingEmail() != null && myFriendProfile.getFriendProfileInfo().getRingEmail().trim().length() > 0) {
                    rEmail = myFriendProfile.getFriendProfileInfo().getRingEmail();
                }
                ringEmail.removeAll();

                JPanel titlePanel = buildTitlePanel(StaticFields.RING_EMAIL_TEXT);
                ringEmail.add(titlePanel, BorderLayout.PAGE_START);
                ringEmail.add(addTextDefalt(rEmail), BorderLayout.CENTER);
                ringEmail.revalidate();
                ringEmail.repaint();
            } catch (Exception e) {
                //  e.printStackTrace();
           log.error("Error in changeRingEmail ==>" + e.getMessage());
            }
        }
    }

    private JTextArea addTextDefalt(String text) {
        JTextArea whatArea = DesignClasses.createJTextArea(text, 100);
        whatArea.setOpaque(false);
        whatArea.setEditable(false);
        whatArea.setBorder(new EmptyBorder(2, 0, 2, 2));
        return whatArea;
    }

    private JPanel basicJPanel(int width, int height, int border) {
        JPanel basicJpanel = new JPanel(new BorderLayout());
        basicJpanel.setPreferredSize(new Dimension(width, height));
        basicJpanel.setOpaque(false);
        basicJpanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, DefaultSettings.lightWhiteBorder));
        return basicJpanel;
    }

    private JPanel nullPanel() {
        JPanel basicJpanel = new JPanel(new BorderLayout());
        // basicJpanel.setPreferredSize(new Dimension(600, 35));
        basicJpanel.setOpaque(false);
        basicJpanel.setBackground(RingColorCode.ABOUT_PANEL_BG);
        //  basicJpanel.setBorder(new MatteBorder(0, 0, 1, 0, DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR));
        return basicJpanel;
    }

    private JPanel buildTitlePanel(String title) {
        JPanel contanier = basicJPanel(100, 15, 0);
        contanier.setBorder(new EmptyBorder(1, 0, 1, 2));
        JLabel name = DesignClasses.makeJLabel_normal(title, 14, DefaultSettings.BLACK_FONT);
        name.setBorder(new EmptyBorder(0, 0, 2, 0));
        contanier.add(name, BorderLayout.LINE_START);
        return contanier;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
//new WorkEducationSkillPanel(myFriendProfile.getUserIdentity(), 1);

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == livingInfo || e.getSource() == livingIcon) {
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

            if (clik == 1) {
                livingIcon.setIcon(return_image(GetImages.IMAGE_BOTTOM_ARROW_BIG_H));
                clik = 2;
                if (flag != 0) {
                    livingInfoPanel.setVisible(true);
                }
//                    else{
//                livingInfoPanel.setVisible(false);
//                }
            } else if (clik == 2) {
                livingIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
                livingInfoPanel.setVisible(false);
                clik = 1;
            }
        } else if (e.getSource() == basicInfo || e.getSource() == basicIcon) {
            if (myFriendProfile.getFriendProfileInfo().getFriendShipStatus().intValue() == StatusConstants.FRIENDSHIP_STATUS_ACCEPTED) {
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
                clik = 1;
            }
            if (!basicInfoPanel.isVisible()) {
                basicInfoPanel.setVisible(true);
                basicIcon.setIcon(return_image(GetImages.IMAGE_BOTTOM_ARROW_BIG_H));
            } else {
                basicIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
                basicInfoPanel.setVisible(false);
            }

        } else if (e.getSource() == workInfo || e.getSource() == workIcon) {
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
            clik = 1;

            workInfoPanel.removeAll();
            if (!workInfoPanel.isVisible()) {
                workEducationSkillPanel = new WorkEducationSkillPanel(myFriendProfile.getUserIdentity(), 1);/////////////
                workInfoPanel.add(workEducationSkillPanel, BorderLayout.CENTER);///////////
                workIcon.setIcon(return_image(GetImages.IMAGE_BOTTOM_ARROW_BIG_H));
                workInfoPanel.setVisible(true);
            } else {
                workIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
                workInfoPanel.setVisible(false);
            }
            workInfoPanel.revalidate();
        } else if (e.getSource() == educationInfo || e.getSource() == educationIcon) {
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
            clik = 1;

            educationInfoPanel.removeAll();
            if (!educationInfoPanel.isVisible()) {
                workEducationSkillPanel = new WorkEducationSkillPanel(myFriendProfile.getUserIdentity(), 2);/////////
                educationInfoPanel.add(workEducationSkillPanel, BorderLayout.CENTER);//////////
                educationIcon.setIcon(return_image(GetImages.IMAGE_BOTTOM_ARROW_BIG_H));
                educationInfoPanel.setVisible(true);
            } else {
                educationIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
                educationInfoPanel.setVisible(false);
            }
            educationInfoPanel.revalidate();
        } else if (e.getSource() == skillInfo || e.getSource() == skillIcon) {
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
            clik = 1;

            skillInfoPanel.removeAll();
            if (!skillInfoPanel.isVisible()) {
                workEducationSkillPanel = new WorkEducationSkillPanel(myFriendProfile.getUserIdentity(), 4);////////////
                skillInfoPanel.add(workEducationSkillPanel, BorderLayout.CENTER);///////////
                skillIcon.setIcon(return_image(GetImages.IMAGE_BOTTOM_ARROW_BIG_H));
                skillInfoPanel.setVisible(true);
            } else {
                skillIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
                skillInfoPanel.setVisible(false);
            }
            skillInfoPanel.revalidate();
        } else if (e.getSource() == aboutMeInfo || e.getSource() == aboutMeIcon) {
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
            clik = 1;

            if (!aboutMeInfoPanel.isVisible()) {
                aboutMeInfoPanel.setVisible(true);
                aboutMeIcon.setIcon(return_image(GetImages.IMAGE_BOTTOM_ARROW_BIG_H));
            } else {
                aboutMeIcon.setIcon(return_image(GetImages.IMAGE_RIGHT_ARROW_BIG_H));
                aboutMeInfoPanel.setVisible(false);
            }
        }
    }
}
