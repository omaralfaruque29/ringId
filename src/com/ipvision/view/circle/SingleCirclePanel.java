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
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.Map;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.DeleteCircle;
import com.ipvision.service.LeaveCircle;
import com.ipvision.model.SingleCircleDto;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.utility.JCustomMenuPopup;
import com.ipvision.view.utility.JOptionPanelBasics;
import com.ipvision.view.utility.NotificationCounterOrangeLabel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 *
 * @author Shahadat
 */
public class SingleCirclePanel extends JPanel {

    public long circleId;
    private SingleCircleDto circleDto;
    private int height = 50;
    public NotificationCounterOrangeLabel notificationCircle = new NotificationCounterOrangeLabel();
//    private JPopupMenu actionPopUp;
//    public JMenuItem mnuLeaveCircle;
//    public JMenuItem mnuDeleteCircle;
    private JButton actionPopUpButton;
    private GridBagConstraints c;
    public JPanel np;
    private JLabel circleName;
    private JPanel holder, actionPopUpPanel;
    JCustomMenuPopup leavePopup = null;
    JCustomMenuPopup deletePopup = null;
    private final String MNU_DELETE = "Delete this Circle";
    private final String MNU_LEAVE = "Leave from Circle ";

    public SingleCirclePanel(SingleCircleDto circleDto) {
        this.circleDto = circleDto;
        this.circleId = circleDto.getCircleId();
        this.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH, height + 10));
        this.initComponents();

    }

    private void initComponents() {
        holder = new JPanel(new FlowLayout(FlowLayout.LEFT));
        holder.setBackground(Color.WHITE);
        holder.addMouseListener(mouseListener);
        holder.add(Box.createRigidArea(new Dimension(5, 20)));
        JLabel user_image = DesignClasses.create_imageJlabel(GetImages.GROUP_MINI);
        holder.add(user_image);

        circleName = DesignClasses.makeJLabelFullName2(circleDto.getCircleName(), DefaultSettings.FULL_NAME_FONT_SIZE_IN_FEED); //DesignClasses.makeJLabelFullName(circleDto.getCircleName(), 13);
        circleName.setCursor(new Cursor(Cursor.HAND_CURSOR));
        circleName.addMouseListener(new MouseAdapter() {

            Font original;

            @Override
            public void mouseEntered(MouseEvent e) {
                original = e.getComponent().getFont();
                Map attributes = original.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                e.getComponent().setFont(original.deriveFont(attributes));
                holder.setBackground(RingColorCode.FRIEND_LIST_RIGHT_BUTTON_PRESSED_COLOR);
                actionPopUpButton.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.getComponent().setFont(original);
                holder.setBackground(Color.WHITE);
                actionPopUpButton.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
                    GuiRingID.getInstance().getMainRight().showCircleProfile(circleId);
                }
            }
        });
        JPanel cirNameAndMembersPanel = new JPanel(new BorderLayout());
        cirNameAndMembersPanel.setPreferredSize(new Dimension(400, 30));
        cirNameAndMembersPanel.setOpaque(false);
        cirNameAndMembersPanel.add(circleName, BorderLayout.NORTH);

        int number_of_freind = 0;
        if (MyfnfHashMaps.getInstance() != null && !MyfnfHashMaps.getInstance().getCircleMembers().isEmpty() && MyfnfHashMaps.getInstance().getCircleMembers().get(circleId) != null) {
            number_of_freind = MyfnfHashMaps.getInstance().getCircleMembers().get(circleId).size();
        }

        JLabel members = DesignClasses.makeJLabel_normal(number_of_freind + " Ringideans ", 10, DefaultSettings.disable_font_color);
        // members.setToolTipText(number_of_freind + " Number of Ringideans");
        cirNameAndMembersPanel.add(members, BorderLayout.CENTER);
        holder.add(cirNameAndMembersPanel);
        //holder.add(Box.createRigidArea(new Dimension(80, 20)));

//        JPanel membersPanel = new JPanel(new BorderLayout());
//        membersPanel.setPreferredSize(new Dimension(80, 20));
//        membersPanel.setOpaque(false);
//        membersPanel.add(members, BorderLayout.CENTER);
        //  holder.add(membersPanel);
        holder.add(Box.createRigidArea(new Dimension(105, 20)));

        leavePopup = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_ONLY_LEAVE);
        leavePopup.addMenu(MNU_LEAVE, GetImages.LEAVE_PHOTO);
        leavePopup.addWindowListener(listener);

        deletePopup = new JCustomMenuPopup(menuListener, JCustomMenuPopup.TYPE_ONLY_LEAVE);
        deletePopup.addMenu(MNU_DELETE, GetImages.DELETE_PHOTO);
        deletePopup.addWindowListener(listener);

        actionPopUpButton = DesignClasses.createImageButton(GetImages.IMAGE_BOTTOM_ARROW, GetImages.IMAGE_BOTTOM_ARROW_H, "Settings");
        actionPopUpButton = DesignClasses.createImageButton(GetImages.IMAGE_BOTTOM_ARROW, GetImages.IMAGE_BOTTOM_ARROW_H, "Settings");
        actionPopUpButton.setVisible(false);
        actionPopUpPanel = new JPanel(new BorderLayout());
        actionPopUpPanel.setOpaque(false);
        actionPopUpPanel.setPreferredSize(new Dimension(15, 20));
        actionPopUpPanel.add(actionPopUpButton, BorderLayout.CENTER);
        holder.add(actionPopUpPanel);

        actionPopUpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                actionPopUpButton.setVisible(true);
                holder.setBackground(RingColorCode.FRIEND_LIST_RIGHT_BUTTON_PRESSED_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!leavePopup.isVisible() && !deletePopup.isVisible()) {
                    actionPopUpButton.setVisible(false);
                    holder.setBackground(Color.WHITE);
                }
            }

        });

        actionPopUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                holder.setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
                if (InternetUnavailablityCheck.isInternetAvailable) {
                    if (circleDto != null && circleDto.getSuperAdmin().equals(MyFnFSettings.LOGIN_USER_ID)) {
                        deletePopup.setVisible(actionPopUpButton, 159, -5);

                    } //                actionPopUp.show(actionPopUpButton, actionPopUp.getX() - 110, actionPopUp.getY() + 10);
                    else {
                        leavePopup.setVisible(actionPopUpButton, 159, -5);

                    }
                }
            }
        });
        //buildActionPopup();
        this.add(holder, c);
        c.gridy++;
        c.anchor = GridBagConstraints.CENTER;
        np = new JPanel();
        np.setBackground(Color.WHITE);
        np.setPreferredSize(new Dimension(560, 1));
        np.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.COMMENTS_BORDER_COLOR));

        this.add(np, c);
    }

//    // public void buildActionPopup() {
////        actionPopUp = new JPopupMenu();
//    if (InternetUnavailablityCheck.isInternetAvailable
//
//    
//        ) {
//            if (circleDto != null && circleDto.getSuperAdmin().equals(MyFnFSettings.LOGIN_USER_ID)) {
//            leaveOrDelete = 1; //delete 
////                mnuDeleteCircle = new JMenuItem("Delete Circle", DesignClasses.return_image(GetImages.CLOSE_MINI));
////                actionPopUp.add(mnuDeleteCircle);
////                mnuDeleteCircle.addActionListener(actionListener);
//        } else {//leave
//            leaveOrDelete = 2;
////                mnuLeaveCircle = new JMenuItem("Leave Circle", DesignClasses.return_image(GetImages.CLOSE_MINI));
////                actionPopUp.add(mnuLeaveCircle);
////                mnuLeaveCircle.addActionListener(actionListener);
//        }
//    }
//}
    MouseListener menuListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            if (menu.text.equalsIgnoreCase(MNU_LEAVE)) {
                leavePopup.hideThis();
                HelperMethods.showConfirmationDialogMessage(NotificationMessages.LEAVE_NOTIFICAITON);
                if (JOptionPanelBasics.YES_NO) {
                    new LeaveCircle(circleId).start();
                }

            } else if (menu.text.equalsIgnoreCase(MNU_DELETE)) {
                deletePopup.hideThis();
                HelperMethods.showConfirmationDialogMessage(NotificationMessages.DELETE_NOTIFICAITON);
                if (JOptionPanelBasics.YES_NO) {
                    new DeleteCircle(circleId).start();
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            menu.setMouseEntered();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JCustomMenuPopup.CustomMenu menu = (JCustomMenuPopup.CustomMenu) e.getSource();
            menu.setMouseExited();
        }
    };

    WindowListener listener = new WindowAdapter() {
        @Override
        public void windowDeactivated(WindowEvent e) {
            holder.setBackground(Color.WHITE);
            actionPopUpButton.setVisible(false);
        }
    };

    MouseListener mouseListener = new MouseListener() {
        Font original;

        @Override
        public void mouseEntered(MouseEvent e) {
            if ((e.getSource() == holder || e.getSource() == actionPopUpButton) && (!leavePopup.isVisible() && !leavePopup.isVisible())) {
                holder.setBackground(RingColorCode.FRIEND_LIST_RIGHT_BUTTON_PRESSED_COLOR);
            }
            actionPopUpButton.setVisible(true);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if ((e.getSource() == holder || e.getSource() == actionPopUpButton) && (!leavePopup.isVisible() && !leavePopup.isVisible())) {
                holder.setBackground(Color.WHITE);
                actionPopUpButton.setVisible(false);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
    };

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
//            if (actionPopUpButton != null && e.getSource() == actionPopUpButton) {
////                actionPopUp.show(actionPopUpButton, actionPopUp.getX() - 150, actionPopUp.getY() + 10);
//            } else if (mnuDeleteCircle != null && e.getSource() == mnuDeleteCircle) {
//                HelperMethods.showConfirmationDialogMessage(NotificationMessages.DELETE_NOTIFICAITON);
//                if (JOptionPanelBasics.YES_NO) {
//                    new DeleteCircle(circleId).start();
//                }
//            } else if (mnuLeaveCircle != null && e.getSource() == mnuLeaveCircle) {
//                HelperMethods.showConfirmationDialogMessage(NotificationMessages.LEAVE_NOTIFICAITON);
//                if (JOptionPanelBasics.YES_NO) {
//                    new LeaveCircle(circleId).start();
//                }
//            }
        }
    };
}
