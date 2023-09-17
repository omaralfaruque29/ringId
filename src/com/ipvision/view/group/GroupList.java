/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.group;

import com.ipvision.view.utility.DefaultSettings;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;

/**
 *
 * @author Wasif Islam
 */
public class GroupList extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GroupList.class);
    private JPanel groupList;
    public JPanel internalGroupListContainer;
    public List<Long> alreadyDrawGroup = new ArrayList<Long>();
    public JButton createGroupButton;
    private GridBagConstraints con;
    private SingleGroupPanel singleGroupPanel;

    public GroupList() {
        setLayout(new BorderLayout(DefaultSettings.HGAP, 5));
        setOpaque(false);
        groupList = new JPanel(new BorderLayout(0, 0));
        groupList.setOpaque(false);
        con = new GridBagConstraints();
        con.gridx = 0;
        con.gridy = 0;
        internalGroupListContainer = new JPanel(new GridBagLayout());
        groupList.add(internalGroupListContainer, BorderLayout.CENTER);
        internalGroupListContainer.setOpaque(false);
        add(groupList, BorderLayout.CENTER);

        //MyfnfHashMaps.getInstance().getSingleGroupInGroupOfMe().clear();
    }

    public void addGroupList() {
        try {

            alreadyDrawGroup.clear();
            if (internalGroupListContainer != null) {
                internalGroupListContainer.removeAll();
                con.gridx = 0;
                con.gridy = 0;
                //internalGroupListContainer.add(Box.createRigidArea(new Dimension(5, 5)), con);
                con.gridy++;
                if (FriendList.getInstance() != null && !MyfnfHashMaps.getInstance().getGroup_hash_map().isEmpty()) {
                    for (Long groupId : MyfnfHashMaps.getInstance().getGroup_hash_map().keySet()) {
                        GroupInfoDTO groupDto = MyfnfHashMaps.getInstance().getGroup_hash_map().get(groupId);
                        if (internalGroupListContainer != null) {
                            singleGroupPanel = new SingleGroupPanel(groupDto);
                            internalGroupListContainer.add(singleGroupPanel, con);
//                            con.gridy++;
//                            internalGroupListContainer.add(Box.createRigidArea(new Dimension(5, 6)), con);
                            con.gridy++;
                        }

                    }
                    singleGroupPanel.np.setBorder(null);
                }
                //internalGroupListContainer.add(Box.createRigidArea(new Dimension(5, 3)), con);
                //con.gridy++;
                internalGroupListContainer.revalidate();
                internalGroupListContainer.repaint();
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("Error in addGroupList ==>" + e.getMessage());
        }
    }

}
