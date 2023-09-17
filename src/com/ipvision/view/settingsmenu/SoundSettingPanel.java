/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.settingsmenu;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.RingColorCode;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.utility.RoundedCornerButton;

/**
 *
 * @author Shahadat
 */
public class SoundSettingPanel extends JPanel{
    
    //public static SoundSettingPanel instance;
    private JButton btnSave;
    private JButton btnReset;
    private JPanel mainContent;
    
  
    public SoundSettingPanel() {
      //  instance = this;
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.initComponents();
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout(5, 5));
        headerPanel.setPreferredSize(new Dimension(0, 30));
        headerPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        headerPanel.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
        add(headerPanel, BorderLayout.NORTH);
        
        JLabel myNameLabel = DesignClasses.makeLableBold1("Sound", 15);
        headerPanel.add(myNameLabel, BorderLayout.WEST);
        
        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBorder(new MatteBorder(0, 1, 0, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        bodyPanel.setOpaque(false);
        add(bodyPanel, BorderLayout.CENTER);
        
        mainContent = new JPanel();
        mainContent.setBorder(new EmptyBorder(7, 7, 7, 7));
        mainContent.setOpaque(false);
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        bodyPanel.add(mainContent, BorderLayout.NORTH);
        
        
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new MatteBorder(1, 0, 0, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        footerPanel.setOpaque(false);
        footerPanel.setPreferredSize(new Dimension(0, 35));
        add(footerPanel, BorderLayout.SOUTH);
        
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonContainer.setBorder(new EmptyBorder(0, 0, 0, 10));
        buttonContainer.setOpaque(false);
        
        btnSave = new RoundedCornerButton("Save Changes","Save");
      //DesignClasses.createImageButton(GetImages.SAVE_CHANGES, GetImages.SAVE_CHANGES_H, "Save");
        btnSave.addActionListener(actionListener);
        
        btnReset = new RoundedCornerButton("Reset Changes","Reset");
//DesignClasses.createImageButton(GetImages.RESET_CHANGES, GetImages.RESET_CHANGES_H, "Reset");
        btnReset.addActionListener(actionListener);
        
        if(InternetUnavailablityCheck.isInternetAvailable){
            buttonContainer.add(btnSave);
            buttonContainer.add(btnReset);
        }
        
        footerPanel.add(buttonContainer, BorderLayout.CENTER);
        
        buildMailSignature();
    }
    
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnSave){

            }else if(e.getSource() == btnReset){
                
            }
        }
    };

    private void buildMailSignature() {
        
    }
}
