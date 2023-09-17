/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.circle;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.SingleCircleDto;

/**
 *
 * @author Faiz Ahmed
 */
public class CircleList extends JPanel {

    private JPanel circleList;
    public JPanel internalCircleListContainer;
    public List<Long> alreadyDrawCircle = new ArrayList<Long>();
    private boolean my;
    public static Long selectedCircleId = null;
    public JButton createCircleButton;
    private GridBagConstraints con;
    private SingleCirclePanel singleCirclePanel;

    public CircleList(boolean my) {
        setLayout(new BorderLayout(0, 0));
        //setBackground(Color.WHITE);
        setOpaque(false);
        circleList = new JPanel(new BorderLayout(0, 0));
        circleList.setOpaque(false);
        this.my = my;
        con = new GridBagConstraints();
        con.gridx = 0;
        con.gridy = 0;
        internalCircleListContainer = new JPanel(new GridBagLayout());
        circleList.add(internalCircleListContainer, BorderLayout.CENTER);

        internalCircleListContainer.setOpaque(false);
        add(circleList, BorderLayout.CENTER);

        if (this.my) {
            MyfnfHashMaps.getInstance().getSingleCircleInMyCircle().clear();
        } else {
            MyfnfHashMaps.getInstance().getSingleCircleInCircleOfMe().clear();
        }
    }

    public void addCircleList() {
        try {

            alreadyDrawCircle.clear();
            if (internalCircleListContainer != null) {
                internalCircleListContainer.removeAll();
                con.gridx = 0;
                con.gridy = 0;
              //  internalCircleListContainer.add(Box.createRigidArea(new Dimension(5, 5)), con);
                con.gridy++;
                if (MyfnfHashMaps.getInstance() != null && !MyfnfHashMaps.getInstance().getCircleLists().isEmpty()) {
                    for (Long gruopId : MyfnfHashMaps.getInstance().getCircleLists().keySet()) {
                        SingleCircleDto circleDto = MyfnfHashMaps.getInstance().getCircleLists().get(gruopId);
                        if (my && circleDto.getSuperAdmin().equals(MyFnFSettings.LOGIN_USER_ID)) {
                            if (internalCircleListContainer != null) {
                                singleCirclePanel = new SingleCirclePanel(circleDto);
                                internalCircleListContainer.add(singleCirclePanel, con);
                                con.gridy++;
//                                con.gridy++;
                                MyfnfHashMaps.getInstance().getSingleCircleInMyCircle().put(gruopId, singleCirclePanel);
                            }
                        } else if (!my && !circleDto.getSuperAdmin().equals(MyFnFSettings.LOGIN_USER_ID)) {
                            if (internalCircleListContainer != null) {
                                singleCirclePanel = new SingleCirclePanel(circleDto);
                                internalCircleListContainer.add(singleCirclePanel, con);
                                con.gridy++;
//                                internalCircleListContainer.add(Box.createRigidArea(new Dimension(5, 6)), con);
//                                con.gridy++;
                                MyfnfHashMaps.getInstance().getSingleCircleInCircleOfMe().put(gruopId, singleCirclePanel);

                            }
                        }
                    }
                    singleCirclePanel.np.setBorder(null);
                }
                internalCircleListContainer.revalidate();
                internalCircleListContainer.repaint();
            }
        } catch (Exception e) {
        }
    }

    public static void setCircleSelectionColor(Long circleId) {
        selectedCircleId = circleId;

        //for (Entry<Long, SingleCircleInCircleList> entity : MyfnfHashMaps.getInstance().getSingleCircleInCircleOfMe().entrySet()) {
        for (Entry<Long, SingleCirclePanel> entity : MyfnfHashMaps.getInstance().getSingleCircleInCircleOfMe().entrySet()) {

            Long gId = entity.getKey();
            SingleCirclePanel panel = entity.getValue();
            panel.setBackground(Color.WHITE);
            if (selectedCircleId != null && selectedCircleId.intValue() == gId.intValue()) {
                panel.setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
            }
        }

        for (Entry<Long, SingleCirclePanel> entity : MyfnfHashMaps.getInstance().getSingleCircleInMyCircle().entrySet()) {
            Long gId = entity.getKey();
            SingleCirclePanel panel = entity.getValue();
            panel.setBackground(Color.WHITE);
            if (selectedCircleId != null && selectedCircleId.intValue() == gId.intValue()) {
                panel.setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
            }
        }
    }
}
