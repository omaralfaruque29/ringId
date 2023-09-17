/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import javax.swing.JLabel;

/**
 *
 * @author Faiz Ahmed
 */
public class SplashScreenBeforLogin extends SplashScreenBG {

    JLabel loading_JLabel;

    public SplashScreenBeforLogin() {
        loading_JLabel = new JLabel();
        loading_JLabel.setOpaque(false);
        profilePicPanel.add(loading_JLabel);
     //   add(profilePicPanel);
        profilePicPanel.revalidate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                loading_JLabel.setIcon(DesignClasses.return_image(GetImages.WELCOME_LOADER));
                loading_JLabel.revalidate();

            }
        }).start();



    }
}
