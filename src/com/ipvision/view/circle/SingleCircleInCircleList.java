/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.circle;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.DeleteCircle;
import com.ipvision.service.LeaveCircle;
import com.ipvision.model.SingleCircleDto;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.utility.JOptionPanelBasics;
import com.ipvision.view.utility.NotificationCounterOrangeLabel;

/**
 *
 * @author Shahadat
 */
public class SingleCircleInCircleList extends JPanel {

    public long circleId;
    private SingleCircleDto circleDto;
    private int height = 40;
    public NotificationCounterOrangeLabel notificationCircle = new NotificationCounterOrangeLabel();
    private JPopupMenu actionPopUp;
    public JMenuItem mnuLeaveCircle;
    public JMenuItem mnuDeleteCircle;
    private JButton actionPopUpButton;

    public SingleCircleInCircleList(SingleCircleDto circleDto) {
        //super(false);
        this.circleDto = circleDto;
        this.circleId = circleDto.getCircleId();
        this.setLayout(new BorderLayout(2, 2));
        this.setBackground(RingColorCode.COMMENTS_PANEL_COLOR);
        this.setPreferredSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH - 40, height - 7));
        this.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
        //setBorder(DefaultSettings.DEFAULT_BOOK_BORDER);
        this.initComponents();

    }

    private void initComponents() {
        JPanel nullPnl = new JPanel(new BorderLayout(5, 0));
        nullPnl.setOpaque(false);
        //  nullPnl.setPreferredSize(new Dimension(10, 0));

        JLabel user_image = DesignClasses.create_imageJlabel(GetImages.GROUP_MINI);
        nullPnl.add(user_image, BorderLayout.WEST);

        JPanel name = new JPanel(new BorderLayout(25, 0));
        name.setOpaque(false);
        name.setBorder(new EmptyBorder(2, 2, 2, 2));

        JLabel circleName = DesignClasses.makeJLabelFullName(circleDto.getCircleName(), 13);
        circleName.setCursor(new Cursor(Cursor.HAND_CURSOR));
        circleName.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
                    GuiRingID.getInstance().getMainRight().showCircleProfile(circleId);
                }
            }
        });
        name.add(circleName, BorderLayout.WEST);

//        JPanel membersNumPnl = new JPanel(new BorderLayout());//(new FlowLayout(0, 10, 0));
//        membersNumPnl.setBorder(new EmptyBorder(10, 0, 0, 0));
//        membersNumPnl.setOpaque(false);
        int number_of_freind = 0;
        if (MyfnfHashMaps.getInstance() != null && !MyfnfHashMaps.getInstance().getCircleMembers().isEmpty() && MyfnfHashMaps.getInstance().getCircleMembers().get(circleId) != null) {
            number_of_freind = MyfnfHashMaps.getInstance().getCircleMembers().get(circleId).size();
        }

        JLabel members = DesignClasses.makeJLabel_normal("# " + number_of_freind, 10, DefaultSettings.disable_font_color);

//        membersNumPnl.add(members,BorderLayout.CENTER);
        name.add(members, BorderLayout.CENTER);

        JPanel notificationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        notificationPanel.setOpaque(false);
        notificationPanel.add(notificationCircle);
        name.add(notificationPanel, BorderLayout.EAST);

        nullPnl.add(name, BorderLayout.CENTER);

        JPanel editPnl = new JPanel(new FlowLayout(0, 10, 0));
        editPnl.setOpaque(false);
        editPnl.setBorder(new EmptyBorder(10, 0, 0, 0));
        actionPopUpButton = DesignClasses.createImageButton(GetImages.IMAGE_BOTTOM_ARROW, GetImages.IMAGE_BOTTOM_ARROW_H, "Settings");
        editPnl.add(actionPopUpButton);
        actionPopUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionPopUp.show(actionPopUpButton, actionPopUp.getX() - 110, actionPopUp.getY() + 10);
            }
        });
        buildActionPopup();

        nullPnl.add(editPnl, BorderLayout.EAST);
        this.add(Box.createRigidArea(new Dimension(20, 1)), BorderLayout.WEST);
        this.add(nullPnl, BorderLayout.CENTER);
    }

    public void buildActionPopup() {
        actionPopUp = new JPopupMenu();
        if (InternetUnavailablityCheck.isInternetAvailable) {
            if (circleDto != null && circleDto.getSuperAdmin().equals(MyFnFSettings.LOGIN_USER_ID)) {
                mnuDeleteCircle = new JMenuItem("Delete Circle", DesignClasses.return_image(GetImages.CLOSE_MINI));
                actionPopUp.add(mnuDeleteCircle);
                mnuDeleteCircle.addActionListener(actionListener);
            } else {
                mnuLeaveCircle = new JMenuItem("Leave Circle", DesignClasses.return_image(GetImages.CLOSE_MINI));
                actionPopUp.add(mnuLeaveCircle);
                mnuLeaveCircle.addActionListener(actionListener);
            }
        }
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (actionPopUpButton != null && e.getSource() == actionPopUpButton) {
                actionPopUp.show(actionPopUpButton, actionPopUp.getX() - 150, actionPopUp.getY() + 10);
            } else if (mnuDeleteCircle != null && e.getSource() == mnuDeleteCircle) {
                HelperMethods.showConfirmationDialogMessage(NotificationMessages.DELETE_NOTIFICAITON);
                if (JOptionPanelBasics.YES_NO) {
                    new DeleteCircle(circleId).start();
                }
            } else if (mnuLeaveCircle != null && e.getSource() == mnuLeaveCircle) {
                HelperMethods.showConfirmationDialogMessage(NotificationMessages.LEAVE_NOTIFICAITON);
                if (JOptionPanelBasics.YES_NO) {
                    new LeaveCircle(circleId).start();
                }
            }
        }
    };
}
