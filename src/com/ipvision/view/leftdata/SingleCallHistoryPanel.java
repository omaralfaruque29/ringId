/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.leftdata;

/**
 *
 * @author Sirat Samyoun
 */
import com.desktopCall.dtos.CallSettingsDTo;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.call.MainClass;
import com.ipvision.model.RecentDTO;
import com.ipvision.view.utility.chat.ChatHashMap;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.stores.FriendList;
import com.ipvision.view.friendprofile.MyfriendSlider;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.utility.FeedUtils;
import com.ipvision.view.utility.ImageHelpers;

/**
 *
 * @author Sirat Samyoun
 */
public class SingleCallHistoryPanel extends JPanel implements MouseListener {

    private JLabel frienImageLabel = new JLabel();
    private JLabel fullNameLabel;
    //   private JLabel durationLabel;
    private JLabel timeLabel, callIconLabel;
    private JPanel rightHolder;
    private JPanel callStatusPanel;
    private JLabel callStatus;
    private int friendInfowidth = 230;
    public JPanel friendInfoPanel;
    public ImageIcon outgoingCallIcon = DesignClasses.return_image(GetImages.OUTGOING_CALL);
    public ImageIcon incomingCallIcon = DesignClasses.return_image(GetImages.INCOMING_CALL);
    public ImageIcon missedCallIcon = DesignClasses.return_image(GetImages.MISSED_CALL);
    public String contactId = null;
    public RecentDTO recentDTO;
    private SingleCallHistoryPanel currInstance;
    public JPanel checkboxPanel;
    public JCheckBox deleteBox;
    public JPanel checkBoxAndImagePanel;
    public JButton callButton;
    private UserBasicInfo friendProfileInfo;

    public SingleCallHistoryPanel(RecentDTO recentDTO) {
        this.currInstance = this;
        this.recentDTO = recentDTO;
        this.contactId = recentDTO.getContactId();
        friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(contactId);
        this.setLayout(new BorderLayout());
        setBackground(RingColorCode.FRIEND_LIST_BG_COLOR);
        setBorder(new MatteBorder(0, 0, 1, 0, DefaultSettings.SINGLE_FRIEND_BORDER_COLOR));
        friendInfoPanel = new JPanel(new BorderLayout()); //friendInfoPanel.addMouseListener(this);
        friendInfoPanel.setOpaque(false);
        friendInfoPanel.setBorder(new EmptyBorder(4, DefaultSettings.LEFT_EMPTY_SPACE, 4, 0));
        friendInfoPanel.setPreferredSize(new Dimension(DefaultSettings.LEFT_SINGLE_PANEL_WIDTH, DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT + 10));
        friendInfoPanel.addMouseListener(this);

        JPanel friendImagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        friendImagePanel.setPreferredSize(new Dimension(DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT, DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT));
        friendImagePanel.add(frienImageLabel);
        friendImagePanel.setOpaque(false);//Background(Color.cyan);

        checkBoxAndImagePanel = new JPanel(new BorderLayout());
        checkBoxAndImagePanel.setOpaque(false);
        checkboxPanel = new JPanel(new BorderLayout());
        checkboxPanel.setPreferredSize(new Dimension(25, DefaultSettings.SINGLE_FRIEND_PANEL_HEIGHT));
        checkboxPanel.setOpaque(false);
        deleteBox = new JCheckBox(DesignClasses.return_image(GetImages.TICK));
        deleteBox.setSelectedIcon(DesignClasses.return_image(GetImages.TICK_H2));
        deleteBox.setRolloverIcon(DesignClasses.return_image(GetImages.TICK_H));
        deleteBox.setOpaque(false);
        deleteBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GuiRingID.getInstance().getMainLeftContainer().getCallHistoryContainer() != null) {
                    GuiRingID.getInstance().getMainLeftContainer().getCallHistoryContainer().action_all_selected();
                }
            }
        });
        checkboxPanel.setVisible(false);
        checkboxPanel.add(deleteBox, BorderLayout.CENTER);
        checkBoxAndImagePanel.add(checkboxPanel, BorderLayout.WEST);
        checkBoxAndImagePanel.add(friendImagePanel, BorderLayout.CENTER);

        callButton = DesignClasses.createImageButton(GetImages.LOG_CALL_ICON, GetImages.LOG_CALL_ICON_H, GetImages.LOG_CALL_ICON_SELECTED, "Call to " + friendProfileInfo.getFullName());//new RoundedCornerButton("Ok", "Ok");

        JPanel fullNameLabelPanel = new JPanel(new BorderLayout());
        fullNameLabelPanel.setOpaque(false);
        fullNameLabel = DesignClasses.makeJLabelFullName("", 13);
        fullNameLabel.setPreferredSize(new Dimension(125, 15));
        fullNameLabelPanel.add(fullNameLabel, BorderLayout.CENTER);

        callStatusPanel = new JPanel(new GridBagLayout());
        callStatusPanel.setOpaque(false);
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 0;
        callStatus = DesignClasses.makeJLabelUnderFullName("");//makeJLabelFullName("Missed Call", 10); //DesignClasses.makeJLabelUnderFullName("");
        callStatus.setPreferredSize(new Dimension(100, 10));
        callIconLabel = new JLabel();
        callIconLabel.setPreferredSize(new Dimension(16, 16));
        callIconLabel.setIcon(missedCallIcon);
        callStatusPanel.add(callIconLabel, cons);
        cons.gridx++;
        cons.insets = new Insets(0, 5, 0, 0);
        callStatusPanel.add(callStatus, cons);

        JPanel timePanel = new JPanel(new BorderLayout());
        timePanel.setOpaque(false);
        timeLabel = DesignClasses.makeJLabelUnderFullName(FeedUtils.getShowableDateForLeftPane(0));//makeJLabelFullName(FeedUtils.getShowableDateForLeftPane(0), 10);
        // timeLabel.setToolTipText(FeedUtils.getActualDate(0));
        /* Tooltip problm*/
        //http://docs.oracle.com/javase/7/docs/api/javax/swing/ToolTipManager.html
        timeLabel.setPreferredSize(new Dimension(70, 10));
        timePanel.add(timeLabel, BorderLayout.CENTER);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
       // wrapperPanel.setBorder(new EmptyBorder(0, 5, 0, 0));
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(fullNameLabelPanel, BorderLayout.NORTH);
        wrapperPanel.add(callStatusPanel, BorderLayout.CENTER);
        wrapperPanel.add(timePanel, BorderLayout.SOUTH);

        rightHolder = new JPanel(new BorderLayout());
        rightHolder.setPreferredSize(new Dimension(45, 45));
        rightHolder.setBorder(new EmptyBorder(0, 0, 0, 5));
        rightHolder.setOpaque(false);
        rightHolder.add(callButton, BorderLayout.CENTER);

        JPanel leftHolder = new JPanel(new BorderLayout()); //new FlowLayout(FlowLayout.RIGHT, 5, 0)
        leftHolder.setOpaque(false);//Background(Color.red); 
        leftHolder.add(checkBoxAndImagePanel, BorderLayout.WEST);//(new JLabel("asfda"));
        leftHolder.add(wrapperPanel, BorderLayout.CENTER);
        friendInfoPanel.add(leftHolder, BorderLayout.WEST);
        friendInfoPanel.add(rightHolder, BorderLayout.EAST);

        add(friendInfoPanel, BorderLayout.CENTER);

        callButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (MainClass.getMainClass() == null) {
                    CallSettingsDTo callSettingsDTo = new CallSettingsDTo();
                    // callSettingsDTo.setDvc(friendProfileInfo.getDevice());
                    callSettingsDTo.setUserid(MyFnFSettings.LOGIN_USER_ID);
                    callSettingsDTo.setFndId(friendProfileInfo.getUserIdentity());
                    callSettingsDTo.setFn(friendProfileInfo.getFullName());
                    callSettingsDTo.setPrIm(friendProfileInfo.getProfileImage());
                    callSettingsDTo.setIncomming(0);
                    MainClass mainClass2 = new MainClass(callSettingsDTo);
                    mainClass2.showWindow();
                } else {
                    MainClass.getMainClass().showWindow();
                }
            }
        });

        MyfnfHashMaps.getInstance().getSingleCallHistoryPanel().put(recentDTO.getSequence() + "_" + recentDTO.getCallLog().getCallType() + "_" + contactId, this);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                changeFriendAllInformation();
            }
        });
    }

    public String long_to_string(long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millis)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public String getCallTypeasString(int i) {
        String str = "";
        if (i == 1) {
            str = "Outgoing Call";
        } else if (i == 2) {
            str = "Incoming Call";
        } else {
            str = "Missed Call";
        }
        return str;
    }

    public void changeFriendAllInformation() {
        changeFriendProfileImage();
        changeFullName();
        changeNotificationLabel();
        changeInfos();
    }

    public void changeInfos() {
        if (recentDTO != null && recentDTO.getCallLog() != null) {
            if (recentDTO.getCallLog().getCallType() != null) {
                if (recentDTO.getCallLog().getCallType() == 1) {
                    callIconLabel.setIcon(outgoingCallIcon);
                } else if (recentDTO.getCallLog().getCallType() == 2) {
                    callIconLabel.setIcon(incomingCallIcon);
                } else {
                    callIconLabel.setIcon(missedCallIcon);
                }
            }
            if (recentDTO.getCallLog().getCallDuration() != null) {
//                durationLabel.setText(long_to_string(recentDTO.getCallLog().getCallDuration()));
            }
            if (recentDTO.getCallLog().getCallingTime() != null) {
                timeLabel.setText(FeedUtils.getShowableDateForLeftPane(recentDTO.getCallLog().getCallingTime()));
                //  timeLabel.setToolTipText(FeedUtils.getActualDate(recentDTO.getCallLog().getCallingTime()));
            }
            friendInfoPanel.revalidate();
            friendInfoPanel.repaint();
        }
    }

    public void clearNotificationLabel() {
        callStatus.setForeground(DefaultSettings.text_color2);
        callStatus.revalidate();
    }

    public void changeStatus() {
        changeNotificationLabel();
        changeInfos();
    }

    public void changeFriendProfileImage() {
        try {
            UserBasicInfo friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(contactId);
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
            UserBasicInfo friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(contactId);
            fullNameLabel.setText(HelperMethods.getUserFullName(friendProfileInfo));
            // fullNameLabel.setToolTipText(HelperMethods.getUserFullName(friendProfileInfo));
            /* Tooltip problm*/
            //http://docs.oracle.com/javase/7/docs/api/javax/swing/ToolTipManager.html
            fullNameLabel.revalidate();
        } catch (Exception e) {
        }
    }

    public void changeNotificationLabel() {
        String str = getCallTypeasString(recentDTO.getCallLog().getCallType());
        if (recentDTO.getCallCount() > 0) {
            str = str + " (" + recentDTO.getCallCount() + ")";
        }
        callStatus.setText(str);

        RecentDTO unreadRecentDTO = ChatHashMap.getInstance().getCallUnreadHistories().get(contactId);
        if (unreadRecentDTO != null
                && recentDTO.getSequence() == 1
                && Objects.equals(unreadRecentDTO.getCallLog().getCallType(), recentDTO.getCallLog().getCallType())) {
            callStatus.setForeground(DefaultSettings.DEFAULT_TITLE_COLOR_IN_FRIEND);
        } else {
            callStatus.setForeground(DefaultSettings.text_color2);
        }
        callStatusPanel.revalidate();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        changeNotificationLabel();
        GuiRingID.getInstance().getMainRight().showFriendProfile(contactId, MyfriendSlider.TYPE_MY_FRIEND_CHAT_CALL);
        CallHistoryContainer.setCallSelectionColor(currInstance);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
