/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.myprofile;

import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.constants.RingColorCode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.apache.log4j.Logger;
import com.ipvision.model.call.CallLog;
import com.ipvision.view.utility.chat.ChatHelpers;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.stores.FriendList;

/**
 *
 * @author Shahadat Hossain
 */
public class SingleCallPanel extends JPanel {

    org.apache.log4j.Logger log = Logger.getLogger(SingleCallPanel.class);
    public CallLog callLog;
    private SingleCallPanel instance;

    private final int TYPE_MISS_CALL = 1;
    private final int TYPE_CALL_TO = 2;
    private final int TYPE_CALL_FROM = 3;
    private int type;
    private String typeText;
    private Color callMsgColor;

    public SingleCallPanel(CallLog callLog) {
        this.instance = this;
        this.callLog = callLog;

        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        JPanel nPanel = new JPanel();
        nPanel.setOpaque(false);
        nPanel.setPreferredSize(new Dimension(565, 5));
        this.add(nPanel, BorderLayout.NORTH);

        JPanel sPanel = new JPanel();
        sPanel.setOpaque(false);
        sPanel.setPreferredSize(new Dimension(565, 5));
        this.add(sPanel, BorderLayout.SOUTH);

        typeText = "Missed call from";
        type = TYPE_MISS_CALL;
        callMsgColor = RingColorCode.RING_CHAT_MISS_CALL;

        if (callLog.getCallType() == 1) {
            typeText = "Call to";
            type = TYPE_CALL_TO;
            callMsgColor = RingColorCode.RING_CHAT_OUTGOING_CALL;
        } else if (callLog.getCallType() == 2) {
            typeText = "Call from";
            type = TYPE_CALL_FROM;
            callMsgColor = RingColorCode.RING_CHAT_INCOMING_CALL;
        }

        this.initComponents();
    }

    private void initComponents() {
        try {
            UserBasicInfo friendInfo = FriendList.getInstance().getFriend_hash_map().get(callLog.getFriendIdentity());

            String fullName = "No Name";
            if (friendInfo != null && friendInfo.getFullName() != null && friendInfo.getFullName().trim().length() > 0) {
                fullName = friendInfo.getFullName();
            } else if (friendInfo != null) {
                fullName = HelperMethods.getRingID(friendInfo.getUserIdentity());
            } 

            String duration = HelperMethods.convert_seconds_to_h_m_s(callLog.getCallDuration());
            Long callingTime = callLog.getCallingTime();

            String statusImage = GetImages.IMAGE_CALL_FROM_RINGID;
            

            JPanel mainPanel = new JPanel();
            mainPanel.setOpaque(false);
            mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            this.add(mainPanel);
            
            JPanel empty = new JPanel();
            empty.setOpaque(false);
            empty.setPreferredSize(new Dimension(50, 20));
            mainPanel.add(empty, BorderLayout.WEST);

            JPanel textPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    g2d.setColor(callMsgColor);
                    int w = getWidth();
                    int h = getHeight();
                    g2d.fillRoundRect(0, 0, w, h, 40, 40);
                }
            };
            textPanel.setOpaque(false);
            mainPanel.add(textPanel);

            JPanel textWrapper = new JPanel(new BorderLayout());
            textWrapper.setOpaque(false);
            textWrapper.setBorder(new EmptyBorder(5, 5, 5, 5));
            textPanel.add(textWrapper, BorderLayout.CENTER);

            JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 2));
            centerPanel.setOpaque(false);
            textWrapper.add(centerPanel, BorderLayout.CENTER);

            JLabel statusLabel = DesignClasses.create_imageJlabel(statusImage);
            centerPanel.add(statusLabel);

            JLabel typeTextlabel = DesignClasses.makeJLabelFullName(typeText, 11);
            typeTextlabel.setForeground(Color.WHITE);
            centerPanel.add(typeTextlabel);

            JLabel fullNamelabel = DesignClasses.makeLableBold1(fullName, 11, 1);
            fullNamelabel.setForeground(Color.WHITE);
            centerPanel.add(fullNamelabel);

            JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 6));
            eastPanel.setOpaque(false);
            textWrapper.add(eastPanel, BorderLayout.EAST);

            JLabel durationlabel = DesignClasses.makeJLabelFullName(duration, 9);
            durationlabel.setForeground(Color.WHITE);
            eastPanel.add(durationlabel);

            JLabel loadingLabel = DesignClasses.makeJLabelUnderFullName("");
            loadingLabel.setForeground(RingColorCode.RING_CHAT_SHADE_FG);
            loadingLabel.setPreferredSize(new Dimension(50, 20));
            mainPanel.add(loadingLabel, BorderLayout.EAST);

            if (callingTime > 100) {
                loadingLabel.setText(ChatHelpers.getDate(callingTime, ChatHelpers.CHAT_TIME_FORMAT));
            }

        } catch (Exception ex) {

        }
    }
    
}
