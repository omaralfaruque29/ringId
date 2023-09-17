/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import com.ipvision.view.image.SingleImageDetailsForNotifications;
import com.ipvision.view.image.SingleImageDetailsProfileCover;
import com.ipvision.view.image.SingleImgeDetails;

/**
 *
 * @author Faiz
 */
public class JOptionPanelBasics extends JOptionPanelBasicsShadow implements ActionListener {

    String text = "";
    JButton okButton;
    JButton cancelbutton;
    JLabel imageLabel;
    int type;
    public static int PLANE_MASSAGE = 1;
    public static int WARNING_MESSAGE = 2;
    public static int YES_NO_MESSAGE = 3;
    public static boolean YES_NO = false;
    String useIconImage = GetImages.SYS_INFO;

    public JOptionPanelBasics(int type, String text) {
        this.type = type;
        YES_NO = false;
        this.text = text;

        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                hideThis();
            }
        });
        intiTExt();
        initContainers();
        // setVisible(true);
    }

    String okText = "Ok";
    String cancelTExt = "No";

    private void intiTExt() {
        if (this.type == YES_NO_MESSAGE) {
            okText = "Yes";
            useIconImage = GetImages.SYS_WHAT;
        } else if (this.type == WARNING_MESSAGE) {
            useIconImage = GetImages.SYS_WARNING;
        }
    }

    private void initContainers() {
        try {
            GridBagConstraints con = new GridBagConstraints();
            con.gridx = 0;
            con.ipady = 10;
            con.gridy = 0;

            imageLabel = new JLabel(DesignClasses.return_image(useIconImage));
            content.add(imageLabel, con);
            con.gridy++;
            JTextArea textlabel = new JTextArea(text);
            textlabel.setEditable(false);
            if (text.length() > 40) {
                textlabel.setColumns(25);
            } else if (text.length() > 30) {
                textlabel.setColumns(20);
            } else if (text.length() > 15) {
                textlabel.setColumns(15);
            } else {
                textlabel.setColumns(14);
            }
            textlabel.setLineWrap(true);
            textlabel.setWrapStyleWord(true);
            textlabel.setFont(new Font("Arial", Font.PLAIN, 13));
            content.add(textlabel, con);
            con.gridy++;
            con.ipady = 0;
            JPanel buttonPanels = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            buttonPanels.setOpaque(false);
            okButton = new RoundedCornerButton(okText);
            okButton.addActionListener(this);
            buttonPanels.add(okButton);
            if (this.type == YES_NO_MESSAGE) {
                cancelbutton = new RoundedCornerButton(cancelTExt);
                cancelbutton.addActionListener(this);
                buttonPanels.add(cancelbutton);
            }
            content.add(buttonPanels, con);
            con.gridy++;
            getContentPane().add(content, BorderLayout.CENTER);

        } catch (Exception e) {
        }

    }

    public void hideThis() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(false);
                dispose();
                if (SingleImgeDetails.getSingleImgeDetails() != null) {
                    SingleImgeDetails.getSingleImgeDetails().clieckedEditDeletepopup = false;
                }else if (SingleImageDetailsForNotifications.getSingleImgeDetails() != null) {
                    SingleImageDetailsForNotifications.getSingleImgeDetails().clieckedEditDeletepopup = false;
                }
                if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null) {
                    SingleImageDetailsProfileCover.getSingleImgeDetails().clieckedEditDeletepopup = false;
                }
            }
        });

    }

//    public static void main(String[] args) {
//        JOptionPanelBasics likes = new JOptionPanelBasics(3, "Are you sure?");
//
//        System.out.println("YES_NO==>" + YES_NO);
//        //  System.out.println("sd jfkljsadkfj");
//    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            if (type == YES_NO_MESSAGE) {
                YES_NO = true;
            }
            hideThis();
        } else if (e.getSource() == cancelbutton) {
            hideThis();
        }
    }

}
