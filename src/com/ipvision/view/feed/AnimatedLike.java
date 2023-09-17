/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import com.ipvision.view.image.AddImageCaptionCommentsPanel;
import com.ipvision.view.image.SingeImageCommentDetails;

/**
 *
 * @author Dell
 */
public class AnimatedLike extends JDialog implements ActionListener {

    private int animationDuration = 1000;   // each animation will take 2 seconds
    private long animStartTime;         // start time for each animation
    //private JPanel panel;
    private JLabel lbl;
    public Timer timer;

    public AnimatedLike(JLabel lbl) {
        this.lbl = lbl;
        init();

    }

    private void init() {
        setUndecorated(true);
        setResizable(false);
        setFocusableWindowState(false);
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0, 0));
        setMinimumSize(new Dimension(78, 68));
        setMaximumSize(new Dimension(78, 68));

        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setOpaque(false);
        JLabel label = new JLabel();
        label.setIcon(DesignClasses.return_image(GetImages.LIKE_ANIM));
        wrapperPanel.add(label);
        setContentPane(wrapperPanel);
        int location_x = (int) lbl.getLocationOnScreen().getX() - 25;
        int location_y = (int) lbl.getLocationOnScreen().getY() - 25;
        setLocation(location_x, location_y);
        setAlwaysOnTop(true);

        timer = new Timer(1, this);
        timer.setInitialDelay(0);
        animStartTime = 0 + System.nanoTime() / 1000000;
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        // calculate elapsed fraction of animation
        long currentTime = System.nanoTime() / 1000000;
        long totalTime = currentTime - animStartTime;
        if (totalTime > animationDuration) {
            animStartTime = currentTime;
        }
        float fraction = (float) totalTime / animationDuration;
        fraction = Math.min(1.0f, fraction);
        // interpolate between start and end colors with current fraction
        // set our new color appropriately
        ///  lbl.setIcon(DesignClasses.return_image(GetImages.LIKE_ANIM));
        setVisible(true);
        if (fraction >= 1) {
            //panel.setBorder(originalBorder);
            // lbl.setIcon(null);
            setVisible(false);
            //change_like_number();
            if (SingleFeedStructure.getInatance() != null) {
                SingleFeedStructure.getInatance().change_like_number();
            }
            if (SingleCommentView.getInatance() != null) {
                SingleCommentView.getInatance().change_like_number();
            }
            if (AddImageCaptionCommentsPanel.getInatance() != null) {
                AddImageCaptionCommentsPanel.getInatance().changeImageLikeAll();
            }
            if (SingeImageCommentDetails.getInatance() != null) {
                SingeImageCommentDetails.getInatance().change_like_number();
            }
            dispose();
            //lbl.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI_H));
            timer.stop();
        }

    }

}
