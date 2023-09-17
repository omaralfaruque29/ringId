/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.utility.JOptionPanelBasicsShadow;
import com.ipvision.view.utility.RoundedCornerButton;

/**
 *
 * @author Dell
 */
public class TimerEntryOpenDialog extends JOptionPanelBasicsShadow implements ActionListener {

    private JComboBox cmbSeconds;
    private JButton btnSave;
    private JButton btnCancel;
    private int action = 0; // 0 = Cancel, 1 = Save 

    private String[] secondsArray = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
        "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
        "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
        "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80",
        "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100",
        "101", "102", "103", "104", "105", "106", "107", "108", "109", "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120"};
    public static final int ACTION_SAVE = 1;
    public static final int ACTION_CANCEL = 2;

    public Integer getSeconds() {
        return Integer.parseInt(cmbSeconds.getSelectedItem().toString());
    }

    public int getActionType() {
        return action;
    }

    public TimerEntryOpenDialog() {
        //    setMinimumSize(new Dimension(width + 20, height + 40));
        //    componentContent.setBorder(new EmptyBorder(padding + 20, padding, padding, padding));
        setTitle("Enter Timer");
        initComponents();
        //     setLocationRelativeTo(container);
        setVisible(true);
    }

    private void initComponents() {
        GridBagConstraints con = new GridBagConstraints();
        con.gridx = 0;
        con.ipady = 15;
        con.gridy = 0;

        JPanel emptyContainer1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emptyContainer1.setOpaque(false);
        emptyContainer1.setPreferredSize(new Dimension(0, 30));
        content.add(emptyContainer1, con);
        con.gridy++;

        JPanel inputContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        inputContainer.setOpaque(false);

        JLabel lblSeconds = new JLabel("   Seconds:");
        lblSeconds.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        lblSeconds.setOpaque(false);
        inputContainer.add(lblSeconds);

        cmbSeconds = new JComboBox(secondsArray);
        cmbSeconds.setOpaque(false);
        inputContainer.add(cmbSeconds);

        content.add(inputContainer, con);
        con.gridy++;

        JPanel emptyContainer3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emptyContainer3.setOpaque(false);
        emptyContainer3.setPreferredSize(new Dimension(30, 0));
        content.add(emptyContainer3, con);
        con.gridy++;

        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonContainer.setOpaque(false);

        btnSave = new RoundedCornerButton("Ok", "Ok");
        //DesignClasses.createImageButton(GetImages.OK, GetImages.OK_H, "Ok");
        btnSave.addActionListener(this);

        btnCancel = new RoundedCornerButton("Cancel", "Cancel");//DesignClasses.createImageButton(GetImages.BUTTON_CANCEL, GetImages.BUTTON_CANCEL_H, "Cancel");
        btnCancel.addActionListener(this);

        if (InternetUnavailablityCheck.isInternetAvailable) {
            buttonContainer.add(btnSave);
            buttonContainer.add(btnCancel);
        }
        content.add(buttonContainer, con);
        con.gridy++;

    }

//        public static void main(String[] args) {
//        TimerEntryOpenDialog timerEntryOpenDialog = new TimerEntryOpenDialog();
//        timerEntryOpenDialog.setVisible(true);
//    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            action = ACTION_SAVE;
            setVisible(false);
        } else if (e.getSource() == btnCancel) {
            action = ACTION_CANCEL;
            setVisible(false);
        }
    }

}
