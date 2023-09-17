/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.model.constants.StaticFields;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.ipvision.view.utility.ImageHelpers;

/**
 *
 * @author Faiz Ahmed
 */
public class UnitFrame extends JFrame {

    public JPanel main_contentPane;
    public Object actionSource;
    public Object focusedSource;

    public UnitFrame() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize(new Dimension(DefaultSettings.FRAME_DEFAULT_WIDTH, DefaultSettings.FRAME_DEFAULT_HEIGHT));
        this.setMinimumSize(new Dimension(DefaultSettings.FRAME_DEFAULT_WIDTH, DefaultSettings.FRAME_DEFAULT_HEIGHT));
        this.setLocationRelativeTo(null);
        this.setTitle(StaticFields.APP_NAME);
        ImageHelpers.setAppIcon(this, null);
        main_contentPane = (JPanel) getContentPane();
        main_contentPane.setBackground(Color.WHITE);
        main_contentPane.setLayout(new BorderLayout());
    }
}
