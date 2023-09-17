/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.circle;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.RingColorCode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.SingleCircleDto;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.view.utility.CenterLayout;
import com.ipvision.view.utility.NotificationCounterOrangeLabel;
import com.ipvision.view.utility.RoundedCornerButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author Faiz Ahmed
 */
public class CircleViewRight extends JPanel implements AncestorListener {

    private JPanel mycircleButtonsPanel;
    private JPanel mycircleDetailsPanel;
    private JPanel circleofMeButtonsPanel;
    private JPanel circleofMeDetailsPanel;
    GridBagConstraints con, c1, c2;
    private JPanel myCirclePanel;
    private JPanel circleofMePanel;
    //   public JLabel textMyCircles;
    public JLabel textCircleofMe;
    private NotificationCounterOrangeLabel notificationCircleofMe;// = new NotificationCounterOrangeLabel();
    private NotificationCounterOrangeLabel notificationMyCircle;// = new NotificationCounterOrangeLabel();
    private JButton createCircleButton;
    private JButton btnBack;
    private JPanel labelOvalPanel;
    private JPanel labelOvalPanelCircleOfMe;
    private JPanel previousComponent;

    public void loadPreviousComponent(JPanel previous) {
        previousComponent = previous;
    }

    public CircleViewRight() {
        addAncestorListener(this);
        setBackground(RingColorCode.DEFAULT_BACKGROUND_COLOR);
        //setBackground(Color.YELLOW);
        setLayout(new CenterLayout(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH + 20));

        JPanel mainContainer = new JPanel(new BorderLayout(DefaultSettings.HGAP, DefaultSettings.VGAP + 2));
        mainContainer.setBorder(new EmptyBorder(5, 0, 0, 0));
        mainContainer.setOpaque(false);
        // mainContainer.setBackground(Color.RED);
        add(mainContainer);

//        notificationCircleofMe = new NotificationCounterOrangeLabel();
//        notificationMyCircle = new NotificationCounterOrangeLabel();
        JPanel wrapperPanel = new JPanel(new BorderLayout(0, 0));
        wrapperPanel.setOpaque(false);
        JPanel holder = new JPanel(new BorderLayout());
        holder.setBackground(Color.WHITE);
        holder.add(wrapperPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = DesignClasses.getDefaultScrollPaneThin(holder);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR),
                new EmptyBorder(0, 0, 0, 0)));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBackground(Color.WHITE);
        mainContainer.add(scrollPane, BorderLayout.CENTER);

        mycircleButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mycircleButtonsPanel.setPreferredSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH, 50));
        mycircleButtonsPanel.setBackground(Color.WHITE);
        mycircleButtonsPanel.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
        btnBack = new RoundedCornerButton("Back", "Back");
        createCircleButton = new RoundedCornerButton("Create new Circle", "Create a new Circle");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null) {
                    GuiRingID.getInstance().loadIntoMainRightContent(previousComponent);
                }
            }
        });
        createCircleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GuiRingID.getInstance() != null) {
                    GuiRingID.getInstance().getMainRight().action_of_fnf_create_circle_button();
                }
            }
        });
        mycircleButtonsPanel.add(Box.createRigidArea(new Dimension(50, 40)));
        mycircleButtonsPanel.add(btnBack);
        // mycircleButtonsPanel.add(notificationMyCircle);
        mycircleButtonsPanel.add(Box.createRigidArea(new Dimension(330, 20)));
        mycircleButtonsPanel.add(createCircleButton);
        mainContainer.add(mycircleButtonsPanel, BorderLayout.NORTH);

        myCirclePanel = new JPanel(new BorderLayout());
        myCirclePanel.setOpaque(false);
        wrapperPanel.add(myCirclePanel, BorderLayout.NORTH);

        mycircleDetailsPanel = new JPanel();
        mycircleDetailsPanel.setLayout(new BoxLayout(mycircleDetailsPanel, BoxLayout.Y_AXIS));
        mycircleDetailsPanel.setBackground(Color.WHITE);
        myCirclePanel.add(mycircleDetailsPanel, BorderLayout.CENTER);

        circleofMePanel = new JPanel(new BorderLayout(0, 0));
        //circleofMePanel.setOpaque(false);
        circleofMePanel.setBackground(Color.WHITE);
        // circleofMePanel.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        wrapperPanel.add(circleofMePanel, BorderLayout.CENTER);
        circleofMeButtonsPanel = new JPanel(new BorderLayout());
//        circleofMeButtonsPanel.setPreferredSize(new Dimension(DefaultSettings.MAIN_RIGHT_CONTENT_WIDTH, 43));
        circleofMeButtonsPanel.setOpaque(false);
//        circleofMeButtonsPanel.setBackground(Color.WHITE);
//        textCircleofMe = DesignClasses.circleLabels("Circles You're In (0)");
//        circleofMeButtonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
//        circleofMeButtonsPanel.add(textCircleofMe);
        // circleofMeButtonsPanel.add(notificationCircleofMe);
        circleofMePanel.add(circleofMeButtonsPanel, BorderLayout.NORTH);

        circleofMeDetailsPanel = new JPanel(new BorderLayout());
        circleofMeDetailsPanel.setBackground(Color.WHITE);
        circleofMePanel.add(circleofMeDetailsPanel, BorderLayout.CENTER);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setNotificationCount();
            }
        });
    }

    public void loadData() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                buildCircleList();
            }
        });
    }

    public void buildCircleCount() {
        int myCircles = 0;
        int circleOfMe = 0;
        if (MyfnfHashMaps.getInstance() != null && !MyfnfHashMaps.getInstance().getCircleLists().isEmpty()) {
            for (Long gruopId : MyfnfHashMaps.getInstance().getCircleLists().keySet()) {
                SingleCircleDto circleDto = MyfnfHashMaps.getInstance().getCircleLists().get(gruopId);
                if (circleDto.getSuperAdmin().equals(MyFnFSettings.LOGIN_USER_ID)) {
                    myCircles++;
                } else if (!circleDto.getSuperAdmin().equals(MyFnFSettings.LOGIN_USER_ID)) {
                    circleOfMe++;
                }

            }
        }
        //   textMyCircles.setText("My Circles (" + myCircles + ")");
        if (myCircles == 0) {
            //  labelOvalPanel = DesignClasses.createlabelOvalPanel("No Circle");
            labelOvalPanel = null;
        } else {
            labelOvalPanel = DesignClasses.createlabelOvalPanel("My Circles (" + myCircles + ")");
        }
        if (circleOfMe == 0) {
            labelOvalPanelCircleOfMe = null;
            circleofMePanel.setBorder(null);
        } else {
            labelOvalPanelCircleOfMe = DesignClasses.createlabelOvalPanel("Circles You're In (" + circleOfMe + ")");
        }

        // textCircleofMe.setText("Circles You're In (" + circleOfMe + ")");
    }

    public void buildMyCircle() {
        mycircleDetailsPanel.removeAll();
        CircleList circleList = new CircleList(true);
        if (labelOvalPanel != null) {
            mycircleDetailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            mycircleDetailsPanel.add(labelOvalPanel);
        }
        if (MyfnfHashMaps.getInstance() != null && !MyfnfHashMaps.getInstance().getCircleLists().isEmpty()) {
            circleList.addCircleList();
        } else {
            JPanel noCirclePanel = DesignClasses.createlabelOvalPanel("No Circle");
            mycircleDetailsPanel.add(noCirclePanel);
        }
        mycircleDetailsPanel.add(circleList, BorderLayout.CENTER);
//        if (circleList.internalCircleListContainer.getComponentCount() <= 1) {
//            mycircleButtonsPanel.setBorder(null);
//        } else {
//            mycircleButtonsPanel.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
//        }
        mycircleDetailsPanel.revalidate();
        mycircleDetailsPanel.repaint();
    }

    public void buildCircleOfMe() {
        circleofMeDetailsPanel.removeAll();
        circleofMeButtonsPanel.removeAll();
        if (labelOvalPanelCircleOfMe != null) {
            circleofMeButtonsPanel.add(labelOvalPanelCircleOfMe, BorderLayout.CENTER);
            circleofMePanel.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        }
        CircleList circleList = new CircleList(false);
        circleList.addCircleList();
        circleofMeDetailsPanel.add(circleList, BorderLayout.CENTER);
//        if (circleList.internalCircleListContainer.getComponentCount() <= 1) {
//            circleofMeButtonsPanel.setBorder(null);
//        } else {
//            circleofMeButtonsPanel.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
//        }
        circleofMeDetailsPanel.revalidate();
        circleofMeDetailsPanel.repaint();
    }

    public void buildCircleList() {
        try {
            buildCircleCount();
            buildMyCircle();
            buildCircleOfMe();
            setNotificationCount();
        } catch (Exception ex) {

        }
    }

    public void setNotificationCount() {
        int myCircleCount = 0;
        int circleMeCount = 0;

        for (Entry<Long, SingleCircleDto> entry : MyfnfHashMaps.getInstance().getCircleLists().entrySet()) {
            Long circleId = entry.getKey();
            SingleCircleDto circleDto = entry.getValue();
            int size = 0;

            if (NewsFeedMaps.getInstance().getUnreadCircleNewsFeeds().get(circleId) != null) {
                size = NewsFeedMaps.getInstance().getUnreadCircleNewsFeeds().get(circleId).size();
            }

            if (circleDto.getSuperAdmin().equals(MyFnFSettings.LOGIN_USER_ID)) {
                myCircleCount += size;
                SingleCirclePanel singleCircleInCircleList = MyfnfHashMaps.getInstance().getSingleCircleInMyCircle().get(circleId);
                if (singleCircleInCircleList != null) {
                    singleCircleInCircleList.notificationCircle.setText(size > 0 ? size + "" : "");
                    singleCircleInCircleList.revalidate();
                }
            } else {
                circleMeCount += size;
                SingleCirclePanel singleCircleInCircleList = MyfnfHashMaps.getInstance().getSingleCircleInCircleOfMe().get(circleId);
                if (singleCircleInCircleList != null) {
                    singleCircleInCircleList.notificationCircle.setText(size > 0 ? size + "" : "");
                    singleCircleInCircleList.revalidate();
                }
            }
        }
//
//        notificationMyCircle.setText(myCircleCount > 0 ? myCircleCount + "" : "");
//        notificationMyCircle.revalidate();
//        notificationCircleofMe.setText(circleMeCount > 0 ? circleMeCount + "" : "");
//        notificationCircleofMe.revalidate();
//        int numbers = myCircleCount + circleMeCount;
//        if (numbers > 0) {
//            GuiRingID.getInstance().getMainButtonsPanel().getCircleNotification().setText(String.valueOf(numbers));
//            GuiRingID.getInstance().getMainButtonsPanel().getCircleNotification().revalidate();
//            GuiRingID.getInstance().getMainButtonsPanel().getCircleNotification().repaint();
//        } else {
//            GuiRingID.getInstance().getMainButtonsPanel().getCircleNotification().setText("");
//            GuiRingID.getInstance().getMainButtonsPanel().getCircleNotification().revalidate();
//            GuiRingID.getInstance().getMainButtonsPanel().getCircleNotification().repaint();
//        }
    }

    @Override
    public void ancestorAdded(AncestorEvent event) {
        if (GuiRingID.getInstance() != null) {
            GuiRingID.getInstance().getMainButtons().setMainCircleButton(true);
        }
    }

    @Override
    public void ancestorRemoved(AncestorEvent event) {
        if (GuiRingID.getInstance() != null) {
            GuiRingID.getInstance().getMainButtons().setMainCircleButton(false);
        }
    }

    @Override
    public void ancestorMoved(AncestorEvent event) {
    }
}
