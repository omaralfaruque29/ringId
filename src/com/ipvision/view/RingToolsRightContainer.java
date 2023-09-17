/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import com.ipvision.view.ringtools.RingMarketPanel;

/**
 *
 * @author shahadat
 */
public class RingToolsRightContainer extends JPanel {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RingToolsRightContainer.class);
    private RingMarketPanel ringMarketPanel;

    public RingToolsRightContainer() {
        setLayout(new BorderLayout(DefaultSettings.DEFAULT_BORDER_WIDTH, DefaultSettings.DEFAULT_BORDER_WIDTH));
        setBackground(RingColorCode.FRIEND_LIST_BG_COLOR); 
    }

    public RingMarketPanel getRingMarketPanel() {
        return ringMarketPanel;
    }

    public void setRingMarketPanel(RingMarketPanel ringMarketPanel) {
        this.ringMarketPanel = ringMarketPanel;
    }

    public void addRingMarket() {
        try {
            if (ringMarketPanel == null) {
                ringMarketPanel = new RingMarketPanel();
            }
            loadIntoRingToolsRightContainer(ringMarketPanel);
        } catch (Exception e) {
            log.error("Exception in initialization==>" + e.getMessage());
        }
    }

    public void loadIntoRingToolsRightContainer(JPanel comp) {
        if (GuiRingID.getInstance() != null) {
            GuiRingID.getInstance().loadIntoRingToolsRightContainer(comp);
        }
    }

}
