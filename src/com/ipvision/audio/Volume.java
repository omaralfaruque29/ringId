/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.audio;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.ipvision.view.call.MainClass;

/**
 *
 * @author Wasif Islam
 */
public class Volume extends JDialog {

    private JSlider slider;
    private int vol;
    private JButton com;
    Volume volume;
      public Volume(JButton com) {
        this.vol = (int) AudioController.getVolume();
        this.com = com;
        
        init();
    }

    private void init() {
        
      
        setSize(50, 200);
        setUndecorated(true);
        setResizable(false);
        setBackground(new Color(0, 0, 0, 0));
        setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 1;
        g.gridy = 0;

        slider = new JSlider(JSlider.VERTICAL, 0, 100, vol);
        slider.setUI(new CustomSliderUI(slider));

        add(slider, g);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                AudioController.changeVolume(slider.getValue());
            }
        });
        int location_x = (int) com.getLocationOnScreen().getX();
        int location_y = (int) com.getLocationOnScreen().getY() - 200;
        setLocation(location_x, location_y);
        setAlwaysOnTop(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                dispose();
//                if (MainClass.getMainClass().vol != null) {
//                    MainClass.getMainClass().vol = null;
//
//                }

//                if (com.getModel().is()) {
//                    System.out.println("lskfkl");
//                   // setVisible(true);
//                } else {
//                    setVisible(false);
//                }
//                if (com.isEnabled()) {
//                    setVisible(true);
//                } else {
//                    setVisible(false);
//                }
//                setVisible(false);
//
//                if (com.isFocusPainted()) {
//                    System.out.println("true");
//                }

                 setVisible(false);
     
            }
        });
    }
    
}
