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
import com.ipvision.model.AdditionalInfoDTO;
import com.ipvision.service.utility.HelperMethods;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

/**
 *
 * @author Sirat Samyoun
 */
public class MyProfileEmailPanel extends JPanel {

    public GridBagConstraints con;
    private final JButton addButton = DesignClasses.createImageButton(GetImages.ADD_MORE, GetImages.ADD_MORE_H, "Add mail");
    private CreateEmailPanel secondary;
    private CreateEmailPanel last;
    private CreateEmailPanel addedPanel;
    public CreateEmailPanel primary;
    private JPanel topPanel;
    private int secLen = 0;
    public int countSec = 0;

    private int numberSecMails() {
        if (MyFnFSettings.userProfile.getAdditionalInfo() != null && !MyFnFSettings.userProfile.getAdditionalInfo().isEmpty()) {
            int count = 0;
            for (AdditionalInfoDTO ad : MyFnFSettings.userProfile.getAdditionalInfo()) {
                if (HelperMethods.isValidEmail(ad.getNameOfEmail())) {
                    count++;
                }
            }
            return count;
        } else {
            return 0;
        }
    }

    public void checkAddButtonVisibility() {
        if (numberSecMails() <= 2) {
            addButton.setVisible(true);
        } else {
            addButton.setVisible(false);
        }
    }

    public MyProfileEmailPanel() {
        setOpaque(false);
        setVisible(true);
        setLayout(new GridBagLayout());
        con = new GridBagConstraints();
        con.gridx = 0;
        con.gridy = 0;
        con.anchor = GridBagConstraints.WEST;
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        topPanel.setOpaque(false);
        topPanel.setPreferredSize(new Dimension(600, 40));
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(590, 30));
        //titlePanel.setBackground(Color.BLUE);
        titlePanel.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        titlePanel.add(DesignClasses.makeJLabel_normal("Email", 14, DefaultSettings.BLACK_FONT), BorderLayout.CENTER);
        titlePanel.add(addButton, BorderLayout.EAST);
        topPanel.add(Box.createRigidArea(new Dimension(9, 0)));
        topPanel.add(titlePanel);
        add(topPanel, con);
        con.gridy++;
        String pmail = "";
        int isV = -1;
        if (MyFnFSettings.userProfile.getEmail() != null
                && MyFnFSettings.userProfile.getEmail().trim().length() > 0
                && MyFnFSettings.userProfile.getIsEmailVerified() != null) {
            pmail = MyFnFSettings.userProfile.getEmail();
            isV = MyFnFSettings.userProfile.getIsEmailVerified();
        }
        primary = new CreateEmailPanel(0, true, pmail, isV);
        last = primary;
        primary.revalidate();
        primary.repaint();
        add(primary, con);
        con.gridy++;

        if (isV >= 0 && pmail.trim().length() > 0) {
            secLen = numberSecMails();
            countSec = secLen;
            if (secLen <= 2) {
                addButton.setVisible(true);
            } else {
                addButton.setVisible(false);
            }
            if (secLen > 0) {
                int i = 1;
                for (AdditionalInfoDTO ad : MyFnFSettings.userProfile.getAdditionalInfo()) {
                    if (HelperMethods.isValidEmail(ad.getNameOfEmail())) {
                        secondary = new CreateEmailPanel(i++, true, ad.getNameOfEmail(), ad.getIsVerified());
                        last = secondary;
                        add(secondary, con);
                        con.gridy++;
                    }
                }
            }
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    secLen = numberSecMails();//MyFnFSettings.userProfile.getAdditionalInfo().size();
                    if (countSec <= 2) {
                        if (last == primary || last.isOkayed) {
                            if (secLen == 0) {
                                addedPanel = new CreateEmailPanel(1, false, "Add a Mail", 0);
                            } else {
                                addedPanel = new CreateEmailPanel(2, false, "Add a Mail", 0);
                            }
                            countSec++;
                            last = addedPanel;
                            add(addedPanel, con);
                            con.gridy++;
                            addedPanel.revalidate();
                            addedPanel.repaint();
                        }
                    }
                }
            });
        } else {
            addButton.setVisible(false);
        }
    }

}
