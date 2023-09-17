/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.service.WorkEducationSkillListRequest;
import com.ipvision.model.EducationInfoDTO;
import com.ipvision.model.SkillInfoDTO;
import com.ipvision.model.WorkInfoDTO;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;

/**
 *
 * @author Sirat Samyoun
 */
public class WorkEducationSkillPanel extends JPanel implements ActionListener {

    private JButton addSchoolButton, addClgButton;//, addWorkButton, addSkillButton;
    private GridBagConstraints con, c1, c2, c3, c4;
    private JPanel workDetailsPanel, schoolDetailsPanel, clgDetailsPanel, skillDetailsPanel;//, topPanel4, mainPane4, topPanelWork;
    //  private CreateWorkSinglePanel createWorkSinglePanel;
    private CreateEducationSinglePanel createSchoolSinglePanel, createClgSinglePanel;
//    private CreateSkillSinglePanel createSkillSinglePanel;
    public JPanel topPanel3;
    private int type_int;
    private Long userTableID;
    private String userId;

    public JPanel getWorkDetailsPanel() {
        return workDetailsPanel;
    }

//    public CreateWorkSinglePanel getCreateWorkSinglePanel() {
//        return createWorkSinglePanel;
//    }
    public JPanel getSchoolDetailsPanel() {
        return schoolDetailsPanel;
    }

    public JPanel getClgDetailsPanel() {
        return clgDetailsPanel;
    }

    public CreateEducationSinglePanel getCreateSchoolSinglePanel() {
        return createSchoolSinglePanel;
    }

    public CreateEducationSinglePanel getCreateClgSinglePanel() {
        return createClgSinglePanel;
    }

    public JPanel getSkillDetailsPanel() {
        return skillDetailsPanel;
    }

//    public CreateSkillSinglePanel getCreateSkillSinglePanel() {
//        return createSkillSinglePanel;
//    }
    public WorkEducationSkillPanel(String userID, int type_int) {  //1 for work,2 for sch/clg, 4 for skill
        this.userId = userID;
        this.type_int = type_int;
        if (userId.equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
            this.userTableID = MyFnFSettings.LOGIN_USER_TABLE_ID;
        } else {
            this.userTableID = FriendList.getInstance().getFriend_hash_map().get(userId).getUserTableId();
        }
        this.setLayout(new GridBagLayout());
        //this.setBorder(DefaultSettings.DEFAULT_BOOK_BORDER);
        // this.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
        //this.setPreferredSize(new Dimension(555, 0));
        this.setOpaque(false);
        init();
    }

    public void init() {
        con = new GridBagConstraints();
        con.gridx = 0;
        con.gridy = 0;
        con.anchor = GridBagConstraints.WEST;

        this.add(Box.createRigidArea(new Dimension(605, 1)), con);
        con.gridy++;
////////////////////////////////////// 
        if (type_int == 1) {
//            topPanelWork = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
//            topPanelWork.setOpaque(false);
//            topPanelWork.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
//            JLabel workLabel = DesignClasses.makeJLabelFullName2("WORKS", 14);
//            workLabel.setPreferredSize(new Dimension(550, 16));
//            topPanelWork.add(workLabel);
//            JPanel buttonPanel1 = new JPanel(new BorderLayout());
//            buttonPanel1.setOpaque(false);
//            buttonPanel1.setPreferredSize(new Dimension(21, 21));
//            topPanelWork.add(buttonPanel1);
//            if (userTableID.equals(MyFnFSettings.LOGIN_USER_TABLE_ID)) {
//                addWorkButton = DesignClasses.createImageButton(GetImages.ADD_MORE, GetImages.ADD_MORE_H, "Add a Work");
//                addWorkButton.addActionListener(this);
//                buttonPanel1.add(addWorkButton, BorderLayout.CENTER);
//            }
//            JPanel mainPane1 = new JPanel(new BorderLayout());
//            mainPane1.setBorder(new EmptyBorder(0, 25, 0, 0));
//            mainPane1.setOpaque(false);
//            mainPane1.add(topPanelWork, BorderLayout.CENTER);
//            this.add(mainPane1, con);
//            con.gridy++;
            workDetailsPanel = new JPanel(new GridBagLayout());
            workDetailsPanel.setOpaque(false);
            c1 = new GridBagConstraints();
            c1.gridx = 0;
            c1.gridy = 0;
            this.add(workDetailsPanel, con);
            con.gridy++;
            this.add(Box.createRigidArea(new Dimension(556, 0)), con);
            con.gridy++;
            if (MyfnfHashMaps.getInstance().getWorkInfoMap().get(userTableID) == null || MyfnfHashMaps.getInstance().getWorkInfoMap().get(userTableID).isEmpty()) {
                if (userTableID.equals(MyFnFSettings.LOGIN_USER_TABLE_ID)) {
                    //new WorkEducationSkillListRequest().start();
                } else {
                    new WorkEducationSkillListRequest(userTableID).start();
                }

            } else {
                showPreviouslyAddedWorksPanel(userTableID);
            }
        }
/////////////////////fn start//////////////
        if (type_int == 2) {
            JPanel topPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
            topPanel2.setOpaque(false);
            //topPanel2.setBackground(Color.red); 
            //topPanel2.setPreferredSize(new Dimension(555, 40));
            topPanel2.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
            JLabel schLabel = DesignClasses.makeJLabelFullName2("SCHOOL", 14);
            schLabel.setPreferredSize(new Dimension(550, 16));
            topPanel2.add(schLabel);
            JPanel buttonPanel2 = new JPanel(new BorderLayout());
            buttonPanel2.setOpaque(false);
            buttonPanel2.setPreferredSize(new Dimension(21, 21));
            topPanel2.add(buttonPanel2);
            if (userTableID.equals(MyFnFSettings.LOGIN_USER_TABLE_ID)) {
                addSchoolButton = DesignClasses.createImageButton(GetImages.ADD_MORE, GetImages.ADD_MORE_H, "Add High School");
                addSchoolButton.addActionListener(this);
                buttonPanel2.add(addSchoolButton, BorderLayout.CENTER);
            }
            //this.add(topPanel2, con);
            JPanel mainPane2 = new JPanel(new BorderLayout());
            mainPane2.setBorder(new EmptyBorder(0, 25, 0, 0));
            mainPane2.setOpaque(false);
            mainPane2.add(topPanel2, BorderLayout.CENTER);
            this.add(mainPane2, con);
            con.gridy++;

            schoolDetailsPanel = new JPanel(new GridBagLayout());
            schoolDetailsPanel.setOpaque(false);
            c2 = new GridBagConstraints();
            c2.gridx = 0;
            c2.gridy = 0;
            this.add(schoolDetailsPanel, con);
            con.gridy++;
            ////////////fn end//////////////
            topPanel3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
            topPanel3.setOpaque(false);
            topPanel3.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
            JLabel clgLabel = DesignClasses.makeJLabelFullName2("COLLEGE", 14);
            clgLabel.setPreferredSize(new Dimension(550, 16));
            topPanel3.add(clgLabel);
            JPanel buttonPanel3 = new JPanel(new BorderLayout());
            buttonPanel3.setOpaque(false);
            buttonPanel3.setPreferredSize(new Dimension(21, 21));
            topPanel3.add(buttonPanel3);
            if (userTableID.equals(MyFnFSettings.LOGIN_USER_TABLE_ID)) {
                addClgButton = DesignClasses.createImageButton(GetImages.ADD_MORE, GetImages.ADD_MORE_H, "Add College");
                addClgButton.addActionListener(this);
                buttonPanel3.add(addClgButton, BorderLayout.CENTER);
            }
            JPanel mainPane3 = new JPanel(new BorderLayout());
            mainPane3.setBorder(new EmptyBorder(0, 25, 0, 0));
            mainPane3.setOpaque(false);
            mainPane3.add(topPanel3, BorderLayout.CENTER);
            this.add(mainPane3, con);
            con.gridy++;
            clgDetailsPanel = new JPanel(new GridBagLayout());
            clgDetailsPanel.setOpaque(false);
            c3 = new GridBagConstraints();
            c3.gridx = 0;
            c3.gridy = 0;
            this.add(clgDetailsPanel, con);
            con.gridy++;
            this.add(Box.createRigidArea(new Dimension(556, 0)), con);
            con.gridy++;
            if (MyfnfHashMaps.getInstance().getEducationInfoMap().get(userTableID) == null || MyfnfHashMaps.getInstance().getEducationInfoMap().get(userTableID).isEmpty()) {
                if (userTableID.equals(MyFnFSettings.LOGIN_USER_TABLE_ID)) {
                    // new WorkEducationSkillListRequest().start();
                } else {
                    new WorkEducationSkillListRequest(userTableID).start();
                }
            } else {
                showPreviouslyAddedEducationPanel(userTableID, true, true);
            }
        }
/////////////////////////////////////
        if (type_int == 4) {
//            topPanel4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
//            topPanel4.setOpaque(false);
//            topPanel4.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
//            JLabel sklLabel = DesignClasses.makeJLabelFullName2("SKILLS", 14);
//            sklLabel.setPreferredSize(new Dimension(550, 16));
//            topPanel4.add(sklLabel);
//            JPanel buttonPanel4 = new JPanel(new BorderLayout());
//            buttonPanel4.setOpaque(false);
//            buttonPanel4.setPreferredSize(new Dimension(21, 21));
//            topPanel4.add(buttonPanel4);
//            if (userTableID.equals(MyFnFSettings.LOGIN_USER_TABLE_ID)) {
//                addSkillButton = DesignClasses.createImageButton(GetImages.ADD_MORE, GetImages.ADD_MORE_H, "Add a Skill");
//                addSkillButton.addActionListener(this);
//                buttonPanel4.add(addSkillButton, BorderLayout.CENTER);
//            }
//            mainPane4 = new JPanel(new BorderLayout());
//            mainPane4.setBorder(new EmptyBorder(0, 25, 0, 0));
//            mainPane4.setOpaque(false);
//            mainPane4.add(topPanel4, BorderLayout.CENTER);
//            this.add(mainPane4, con);
//            con.gridy++;
            skillDetailsPanel = new JPanel(new GridBagLayout());
            skillDetailsPanel.setOpaque(false);
            c4 = new GridBagConstraints();
            c4.gridx = 0;
            c4.gridy = 0;
            this.add(skillDetailsPanel, con);
            con.gridy++;
            this.add(Box.createRigidArea(new Dimension(556, 0)), con);
            con.gridy++;
            if (MyfnfHashMaps.getInstance().getSkillInfoMap().get(userTableID) == null || MyfnfHashMaps.getInstance().getSkillInfoMap().get(userTableID).isEmpty()) {
                if (userTableID.equals(MyFnFSettings.LOGIN_USER_TABLE_ID)) {
                    // new WorkEducationSkillListRequest().start();
                } else {
                    new WorkEducationSkillListRequest(userTableID).start();
                }
            } else {
                showPreviouslyAddedSkillPanel(userTableID);
            }
        }
        //////////////////////////   

//        if (MyfnfHashMaps.getInstance().getWorkInfoMap().get(userTableID) == null || MyfnfHashMaps.getInstance().getWorkInfoMap().get(userTableID).isEmpty()
//                || MyfnfHashMaps.getInstance().getEducationInfoMap().get(userTableID) == null || MyfnfHashMaps.getInstance().getEducationInfoMap().get(userTableID).isEmpty()
//                || MyfnfHashMaps.getInstance().getSkillInfoMap().get(userTableID) == null || MyfnfHashMaps.getInstance().getSkillInfoMap().get(userTableID).isEmpty()) {
//            if (userTableID.equals(MyFnFSettings.LOGIN_USER_TABLE_ID)) {
//                new WorkEducationListRequest().start();
//            } else {
//                new WorkEducationListRequest(userTableID).start();
//            }
//
//        } else {
//            showPreviouslyAddedWorksPanel(userTableID);
//            showPreviouslyAddedEducationPanel(userTableID, true, true);
//            showPreviouslyAddedSkillPanel(userTableID);
//        }
    }

    public void setUnsetWorkBorder() {
        if (workDetailsPanel.getComponentCount() > 1) {
//            topPanelWork.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
            JPanel jp = (JPanel) workDetailsPanel.getComponent(workDetailsPanel.getComponentCount() - 2);
            jp.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        }
        if (workDetailsPanel.getComponentCount() > 0) {
            JPanel jp = (JPanel) workDetailsPanel.getComponent(workDetailsPanel.getComponentCount() - 1);
            jp.setBorder(null);
        } else {
            //           topPanelWork.setBorder(null);
        }
    }

    public void showPreviouslyAddedWorksPanel(Long userTableID) {
        if (workDetailsPanel != null) {
            if (MyfnfHashMaps.getInstance().getWorkInfoMap().get(userTableID) != null && !MyfnfHashMaps.getInstance().getWorkInfoMap().get(userTableID).isEmpty()) {
                Map<Long, WorkInfoDTO> tempMap = new HashMap<Long, WorkInfoDTO>();
                long[] times = new long[MyfnfHashMaps.getInstance().getWorkInfoMap().get(userTableID).size()];
                int e = 0;
                // tempMap = MyfnfHashMaps.getInstance().getWorkInfoMap().get(UserID);
                for (long key : MyfnfHashMaps.getInstance().getWorkInfoMap().get(userTableID).keySet()) {
                    WorkInfoDTO w = MyfnfHashMaps.getInstance().getWorkInfoMap().get(userTableID).get(key);
                    tempMap.put(w.getUpdateTime(), w);
                    times[e] = w.getUpdateTime();
                    e++;
                }
                Arrays.sort(times);
                for (int k = times.length - 1; k >= 0; k--) {
                    WorkInfoDTO w = tempMap.get(times[k]);
                    WorkSinglePanel workSinglePanel = new WorkSinglePanel(w.getCompanyName(), w.getDesignation(),
                            w.getCity(), w.getDescription(), w.getFromtime(), w.getTotime(), w.getWorkInfoID(), userTableID);
                    if (k == 0) {
                        workSinglePanel.mainPane.setBorder(null);
                    }
                    workDetailsPanel.add(workSinglePanel, c1);
                    c1.gridy++;
                    revalidate();
                    repaint();
                }
                if (workDetailsPanel.getComponentCount() > 0) {
                    JPanel jp = (JPanel) workDetailsPanel.getComponent(workDetailsPanel.getComponentCount() - 1);
                    jp.setBorder(null);
                }
            }
        }
    }

    public void addNewWorkPanel(WorkInfoDTO w) {
        if (workDetailsPanel != null) {
            WorkSinglePanel workSinglePanel = new WorkSinglePanel(w.getCompanyName(), w.getDesignation(),
                    w.getCity(), w.getDescription(), w.getFromtime(), w.getTotime(), w.getWorkInfoID(), userTableID);
            workSinglePanel.setBorder(null);
            workDetailsPanel.add(workSinglePanel, c1);
            c1.gridy++;
            if (workDetailsPanel.getComponentCount() > 1) {
                JPanel jp = (JPanel) workDetailsPanel.getComponent(workDetailsPanel.getComponentCount() - 2);
                jp.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
            }
            revalidate();
            repaint();
        }
    }

    public void addNewSkillPanel(SkillInfoDTO s) {
        if (skillDetailsPanel != null) {
            SkillSinglePanel skillSinglePanel = new SkillSinglePanel(s.getSkill(), s.getDescription(), s.getSkillInfoID(), userTableID);
            skillSinglePanel.setBorder(null);
            skillDetailsPanel.add(skillSinglePanel, c4);
            c4.gridy++;
            if (skillDetailsPanel.getComponentCount() > 1) {
                JPanel jp = (JPanel) skillDetailsPanel.getComponent(skillDetailsPanel.getComponentCount() - 2);
                jp.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
            }
            revalidate();
            repaint();
        }
    }

    public void showPreviouslyAddedEducationPanel(Long userID, boolean addSchool, boolean addCollege) {
        if (schoolDetailsPanel != null && clgDetailsPanel != null) {
            if (MyfnfHashMaps.getInstance().getEducationInfoMap().get(userID) != null && !MyfnfHashMaps.getInstance().getEducationInfoMap().get(userID).isEmpty()) {
                Map<Long, EducationInfoDTO> tempMap = new HashMap<Long, EducationInfoDTO>();
                long[] times = new long[MyfnfHashMaps.getInstance().getEducationInfoMap().get(userID).size()];
                int cn = 0;
                for (long key : MyfnfHashMaps.getInstance().getEducationInfoMap().get(userID).keySet()) {
                    EducationInfoDTO e = MyfnfHashMaps.getInstance().getEducationInfoMap().get(userID).get(key);
                    tempMap.put(e.getUpdateTime(), e);
                    times[cn] = e.getUpdateTime();
                    cn++;
                }
                Arrays.sort(times);
                for (int k = times.length - 1; k >= 0; k--) {
                    EducationInfoDTO e = tempMap.get(times[k]);
                    EducationSinglePanel educationSinglePanel = new EducationSinglePanel(e.getSchool(), e.getDescription(), e.getFromTime(),
                            e.getToTime(), e.getAttendedFor(), e.getGraduated(), e.getIsSchool(), e.getConcentration(), e.getDegree(), e.getEducationId(), userID);
                    if (k == 0) {
                        educationSinglePanel.mainPane.setBorder(null);
                    }
                    if (e.getIsSchool() && addSchool) {
                        schoolDetailsPanel.add(educationSinglePanel, c2);
                        c2.gridy++;
                    } else if (!e.getIsSchool() && addCollege) {
                        clgDetailsPanel.add(educationSinglePanel, c3);
                        c3.gridy++;
                    }
                }
                if (clgDetailsPanel.getComponentCount() > 0) {
                    JPanel jp = (JPanel) clgDetailsPanel.getComponent(clgDetailsPanel.getComponentCount() - 1);
                    jp.setBorder(null);
                } else if (clgDetailsPanel.getComponentCount() == 0) {
                    topPanel3.setBorder(null);
                }
                revalidate();
                repaint();
            }
        }
    }

    public void showPreviouslyAddedSkillPanel(Long userID) {
        if (skillDetailsPanel != null) {
            if (MyfnfHashMaps.getInstance().getSkillInfoMap().get(userID) != null && !MyfnfHashMaps.getInstance().getSkillInfoMap().get(userID).isEmpty()) {
                Map<Long, SkillInfoDTO> tempMap = new HashMap<Long, SkillInfoDTO>();
                long[] times = new long[MyfnfHashMaps.getInstance().getSkillInfoMap().get(userID).size()];
                int cn = 0;
                for (long key : MyfnfHashMaps.getInstance().getSkillInfoMap().get(userID).keySet()) {
                    SkillInfoDTO s = MyfnfHashMaps.getInstance().getSkillInfoMap().get(userID).get(key);
                    tempMap.put(s.getUpdateTime(), s);
                    times[cn] = s.getUpdateTime();
                    cn++;
                }
                Arrays.sort(times);
//            if (times.length > 0) {
//                topPanel4.setBorder(new MatteBorder(0, 0, 1, 0, DEFAULT_BOOK_BORDER_COLOR));
//                mainPane4.revalidate();
//                mainPane4.repaint();
//            }
                for (int k = times.length - 1; k >= 0; k--) {
                    SkillInfoDTO s = tempMap.get(times[k]);
                    SkillSinglePanel skillSinglePanel = new SkillSinglePanel(s.getSkill(), s.getDescription(), s.getSkillInfoID(), userID);
                    //e.getSchool(), e.getDescription(), e.getFromTime(),
                    //e.getToTime(), e.getAttendedFor(), e.getGraduated(), e.getIsSchool(), e.getConcentration(), e.getEducationId(), UserID);
                    if (k == 0) {
                        skillSinglePanel.mainPane.setBorder(null);
                    }

                    skillDetailsPanel.add(skillSinglePanel, c4);
                    c4.gridy++;
                    revalidate();
                    repaint();
                }
                if (skillDetailsPanel.getComponentCount() > 0) {
                    JPanel jp = (JPanel) skillDetailsPanel.getComponent(skillDetailsPanel.getComponentCount() - 1);
                    jp.setBorder(null);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == addWorkButton) {
//            workDetailsPanel.removeAll();
//            createWorkSinglePanel = new CreateWorkSinglePanel();
//            c1.gridy = 0;
//            workDetailsPanel.add(createWorkSinglePanel, c1);
//            c1.gridy++;
//            showPreviouslyAddedWorksPanel(MyFnFSettings.LOGIN_USER_TABLE_ID);
//            workDetailsPanel.revalidate();
//            workDetailsPanel.repaint();
//        } else 
        if (e.getSource() == addSchoolButton) {
            schoolDetailsPanel.removeAll();
            createSchoolSinglePanel = new CreateEducationSinglePanel(0);  // 0 for school
            c2.gridy = 0;
            schoolDetailsPanel.add(createSchoolSinglePanel, c2);
            c2.gridy++;
            showPreviouslyAddedEducationPanel(MyFnFSettings.LOGIN_USER_TABLE_ID, true, false);
            schoolDetailsPanel.revalidate();
            schoolDetailsPanel.repaint();

            if (createClgSinglePanel != null) {
                clgDetailsPanel.removeAll();
                createClgSinglePanel = null;
                showPreviouslyAddedEducationPanel(MyFnFSettings.LOGIN_USER_TABLE_ID, false, true);
                clgDetailsPanel.revalidate();
                clgDetailsPanel.repaint();
            }
        } else if (e.getSource() == addClgButton) {
            if (topPanel3.getBorder() == null) {
                topPanel3.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
            }
            clgDetailsPanel.removeAll();
            createClgSinglePanel = new CreateEducationSinglePanel(1);  // 1 for clg
            c3.gridy = 0;
            clgDetailsPanel.add(createClgSinglePanel, c3);
            c3.gridy++;
            showPreviouslyAddedEducationPanel(MyFnFSettings.LOGIN_USER_TABLE_ID, false, true);
            clgDetailsPanel.revalidate();
            clgDetailsPanel.repaint();
            if (createSchoolSinglePanel != null) {
                schoolDetailsPanel.removeAll();
                showPreviouslyAddedEducationPanel(MyFnFSettings.LOGIN_USER_TABLE_ID, true, false);
                createSchoolSinglePanel = null;
                schoolDetailsPanel.revalidate();
                schoolDetailsPanel.repaint();
            }
        }
        /*        else if (e.getSource() == addSkillButton) {
         skillDetailsPanel.removeAll();
         createSkillSinglePanel = new CreateSkillSinglePanel();
         c4.gridy = 0;
         skillDetailsPanel.add(createSkillSinglePanel, c4);
         c4.gridy++;
         showPreviouslyAddedSkillPanel(MyFnFSettings.LOGIN_USER_TABLE_ID);
         skillDetailsPanel.revalidate();
         skillDetailsPanel.repaint();
        
         }*/
    }
}
