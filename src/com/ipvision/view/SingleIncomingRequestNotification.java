/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import com.ipvision.view.GuiRingID;
import com.ipvision.view.JDialogNotification;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.stores.FriendList;
import com.ipvision.view.utility.ImageHelpers;

/**
 *
 * @author IPvision
 */
public class SingleIncomingRequestNotification extends JPanel{

    public String userIdentity;
    private JLabel frienImageLabel = new JLabel();
    private JLabel fullNameLabel;
    private JPanel friendShipStatusPanel;
    private JLabel friendShipStatusLabel;
    private JDialogNotification jDialogNotification;
    private Color borderColor = Color.WHITE;
    private SingleIncomingRequestNotification instance;
    private int width = 40;
    private int height = 45;

    public SingleIncomingRequestNotification(String userId, JDialogNotification jDialogNotification) {
        this.instance = this;
        this.userIdentity = userId;
        this.jDialogNotification = jDialogNotification;
        UserBasicInfo friendEntity = FriendList.getInstance().getFriend_hash_map().get(this.userIdentity);

        this.setLayout(new BorderLayout());
        this.setBackground(borderColor);
        this.setToolTipText(HelperMethods.getUserFullName(friendEntity));

        JPanel friendImagePanel = new JPanel(new GridBagLayout());
        friendImagePanel.setPreferredSize(new Dimension(width, height));
        friendImagePanel.add(frienImageLabel);
        friendImagePanel.setOpaque(false);

        fullNameLabel = DesignClasses.makeJLabelFullName("", 13);
        fullNameLabel.setPreferredSize(new Dimension(185, 13));

        friendShipStatusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        friendShipStatusPanel.setBorder(new EmptyBorder(7, 0, 0, 0));
        friendShipStatusPanel.setOpaque(false);
        friendShipStatusLabel = DesignClasses.makeJLabelUnderFullName("Incoming Request");
        friendShipStatusLabel.setIcon(DesignClasses.return_image(GetImages.PENDING));
        friendShipStatusLabel.setPreferredSize(new Dimension(185, 12));
        friendShipStatusPanel.add(friendShipStatusLabel);

        JPanel fullNameWhatInmindWrapper = new JPanel(new GridBagLayout());
        fullNameWhatInmindWrapper.setOpaque(false);

        JPanel fullNameWhatInmindPanel = new JPanel(new BorderLayout());
        fullNameWhatInmindPanel.setOpaque(false);
        fullNameWhatInmindPanel.add(fullNameLabel, BorderLayout.NORTH);
        fullNameWhatInmindPanel.add(friendShipStatusPanel, BorderLayout.SOUTH);
        fullNameWhatInmindWrapper.add(fullNameWhatInmindPanel);

        add(friendImagePanel, BorderLayout.WEST);
        add(fullNameWhatInmindWrapper, BorderLayout.CENTER);
        changeFriendAllInformation();
        mouseLiseraction(this);
    }

    public void changeFriendAllInformation() {
        changeFriendProfileImage();
        changeFullName();
    }

    public void changeFriendProfileImage() {
        try {
            UserBasicInfo friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(this.userIdentity);
            short[] friendPrivacy = new short[5];
            if (friendProfileInfo.getPrivacy() != null) {
                friendPrivacy = friendProfileInfo.getPrivacy();
            }
            short profileImagePrivacy = friendPrivacy[2];

            if (friendProfileInfo.getProfileImage() != null
                    && friendProfileInfo.getProfileImage().trim().length() > 0
                    && profileImagePrivacy > 0
                    && (profileImagePrivacy == AppConstants.PRIVACY_SHORT_PUBLIC || profileImagePrivacy == AppConstants.PRIVACY_SHORT_ONLY_FRIEND)) {
                ImageHelpers.addProfileImageThumb(frienImageLabel, friendProfileInfo.getProfileImage());
            } else {
                BufferedImage img = ImageHelpers.getUnknownImage(true);
                frienImageLabel.setIcon(new ImageIcon(img));
            }
        } catch (Exception ex) {
        }
        frienImageLabel.revalidate();
    }

    public void changeFullName() {
        try {
            UserBasicInfo friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(this.userIdentity);
            fullNameLabel.setText(HelperMethods.getUserFullName(friendProfileInfo));
            fullNameLabel.revalidate();
        } catch (Exception e) {
        }
    }
    
    public void mouseLiseraction(final JComponent dd) {
        dd.addMouseListener(new MouseAdapter() {
            Color bg_color;

            @Override
            public void mouseEntered(MouseEvent e) {
                bg_color = instance.getBackground();
                instance.setBackground(DefaultSettings.SELECTION_COLOR);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
              //  jDialogNotification.hideThis();
                GuiRingID.getInstance().showFriendProfile(userIdentity);
            }

            @Override

            public void mouseExited(MouseEvent e) {
                instance.setBackground(bg_color);
            }
        }
        );
    }

}
