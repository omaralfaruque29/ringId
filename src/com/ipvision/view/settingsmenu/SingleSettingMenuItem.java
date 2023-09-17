/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.settingsmenu;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 *
 * @author Shahadat
 */
public class SingleSettingMenuItem extends JPanel{
    
    public String mnuName = "";
    
    public SingleSettingMenuItem(String mnuName) {
        this.mnuName = mnuName;
        setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
        setLayout(new BorderLayout());
        setBorder(new MatteBorder(1, 1, 1, 1, DefaultSettings.DEFAULT_BUTTON_COLOR));
        setPreferredSize(new Dimension(210, 40));
        initContents();
    }

    private void initContents() {
        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        add(contentPanel, BorderLayout.CENTER);
        
        JLabel fullNameLabel = DesignClasses.makeJLabelFullName(mnuName, 14);
        contentPanel.add(fullNameLabel);
    }
    
}
