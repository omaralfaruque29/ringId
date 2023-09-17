/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.group;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.RingColorCode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.utility.CenterLayout;
import com.ipvision.view.utility.RoundedCornerButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author Wasif Islam
 */
public class GroupViewRight extends JPanel implements AncestorListener {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GroupViewRight.class);
    private JPanel groupButtonsPanel;
    public JButton btnBack;
    private JPanel labelOvalPanel;
    public JButton createGroupButton;
    private JPanel myGroupDetailsPanel = null;
    private GridBagConstraints con;
    private JPanel previousComponent;

    public void loadPreviousComponent(JPanel previous) {
        previousComponent = previous;
    }

    public GroupViewRight() {
        addAncestorListener(this);
        setBackground(RingColorCode.DEFAULT_BACKGROUND_COLOR);
        setLayout(new CenterLayout(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH + 20));

        JPanel mainContainer = new JPanel(new BorderLayout(DefaultSettings.HGAP, DefaultSettings.VGAP + 2));
        mainContainer.setBorder(new EmptyBorder(5, 0, 0, 0));
        mainContainer.setOpaque(false);
        //mainContainer.setBackground(Color.WHITE);
        add(mainContainer);

        groupButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        groupButtonsPanel.setPreferredSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH, 50));
        groupButtonsPanel.setBackground(Color.WHITE);
        groupButtonsPanel.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
        btnBack = new RoundedCornerButton("Back", "Back");
        createGroupButton = new RoundedCornerButton("Create new Group", "Create a new group");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
                    GuiRingID.getInstance().loadIntoMainRightContent(previousComponent);
                }
            }
        });
        createGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
                    GuiRingID.getInstance().getMainRight().action_of_fnf_create_group_button();
                }
            }
        });
        groupButtonsPanel.add(Box.createRigidArea(new Dimension(50, 40)));
        groupButtonsPanel.add(btnBack);
        groupButtonsPanel.add(Box.createRigidArea(new Dimension(330, 40)));
        groupButtonsPanel.add(createGroupButton);
        mainContainer.add(groupButtonsPanel, BorderLayout.NORTH);

        myGroupDetailsPanel = new JPanel();
        myGroupDetailsPanel.setLayout(new BoxLayout(myGroupDetailsPanel, BoxLayout.Y_AXIS));
        myGroupDetailsPanel.setBackground(Color.WHITE);

        JPanel holder = new JPanel(new BorderLayout());
        holder.setBackground(Color.WHITE);
        holder.add(myGroupDetailsPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = DesignClasses.getDefaultScrollPaneThin(holder);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR),
                new EmptyBorder(0, 0, 0, 0)));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainContainer.add(scrollPane, BorderLayout.CENTER);
    }

    public void loadData() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                buildGroupList();
            }
        });
    }

    public void buildGroupList() {
        try {
            buildGroupCount();
            buildGroups();
            // buildCircleOfMe();
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Error in buildGroupList ==>" + ex.getMessage());
        }
    }

    public void buildGroupCount() {
        if (MyfnfHashMaps.getInstance() != null && MyfnfHashMaps.getInstance().getGroup_hash_map() != null) {
            //    textMyGroups.setText("My Groups (" + MyfnfHashMaps.getInstance().getGroup_hash_map().size() + ")");
            int count = MyfnfHashMaps.getInstance().getGroup_hash_map().size();
            if (count == 0) {
                labelOvalPanel = DesignClasses.createlabelOvalPanel("No Group");
            } else {
                labelOvalPanel = DesignClasses.createlabelOvalPanel("My Groups (" + MyfnfHashMaps.getInstance().getGroup_hash_map().size() + ")");
            }
        }
    }

    public void buildGroups() {
        myGroupDetailsPanel.removeAll();
        myGroupDetailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        myGroupDetailsPanel.add(labelOvalPanel);
        myGroupDetailsPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        GroupList groupList = new GroupList();
        groupList.addGroupList();
        myGroupDetailsPanel.add(groupList);
//    if (groupList.internalGroupListContainer.getComponentCount() <= 1) {
        //         groupButtonsPanel.setBorder(null);
        //      } 
//        else {
//            groupButtonsPanel.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
//        }
        myGroupDetailsPanel.revalidate();
        myGroupDetailsPanel.repaint();
    }

    @Override
    public void ancestorAdded(AncestorEvent event) {
        if (GuiRingID.getInstance() != null) {
            GuiRingID.getInstance().getMainButtons().setMainGroupButton(true);
        }
    }

    @Override
    public void ancestorRemoved(AncestorEvent event) {
        if (GuiRingID.getInstance() != null) {
            GuiRingID.getInstance().getMainButtons().setMainGroupButton(false);
        }
    }

    @Override
    public void ancestorMoved(AncestorEvent event) {
    }
}
