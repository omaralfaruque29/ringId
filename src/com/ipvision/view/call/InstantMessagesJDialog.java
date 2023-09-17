/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.call;

import com.desktopCall.settings.ConfigFile;
import com.ipv.chat.ChatConstants;
import com.ipv.chat.service.ChatService;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.MessageDTO;
import com.ipvision.service.utility.chat.PacketResenderHandler;
import com.ipvision.model.dao.InstantMessagesDAO;
import com.ipvision.model.InstantMessageDTO;
import com.ipvision.view.friendprofile.MyfriendSlider;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;

/**
 *
 * @author Sirat Samyoun
 */
public class InstantMessagesJDialog extends JDialog {

    private final Image img;
    private JPanel wrapperPanel;
    private JPanel msgContainer;
    private JPanel mainPanel;
    private JScrollPane containerPanelScroll;
    private JLabel titleLabel;
    private JLabel customLabel;
    final private Color bg_color_all = null;
    final private Color image_hover_border = Color.LIGHT_GRAY;
    public static InstantMessagesJDialog currentInstance = null;
    final private int POPUP_WIDTH = 301;
    final private int POPUP_HEIGHT = 361;
    final private String userID;

    //String atemp[] = {"call u back soon", "in a meeting"};
    public void setVisible(JComponent com) { //y reversely increase in both
        currentInstance = this;
        int location_x, location_y;
        location_x = (int) com.getLocationOnScreen().getX() - 233;
        location_y = (int) com.getLocationOnScreen().getY() + 18;
        setLocation(location_x, location_y);
        setVisible(true);
    }

    public InstantMessagesJDialog(String userID) {
        this.userID = userID;
        ConfigFile.BUSY_TEXT = null;
        img = DesignClasses.return_image(GetImages.EMOTICONS_POPUP_ARROW_UP).getImage();
        setUndecorated(true);
        setResizable(false);
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0, 0));
        setMinimumSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
        setMaximumSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
        setLocation(300, 300);
        initComponent();

    }

    @Override
    protected JRootPane createRootPane() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
            }
        };
        JRootPane rootPane2 = new JRootPane();
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane2.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        return rootPane2;
    }

    private void initComponent() {
        JPanel panel = new ImagePanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(30, 34, 30, 32));
        panel.setOpaque(false);
        setContentPane(panel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                hideThis();
            }
        });
        wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setLayout(new BorderLayout());
        wrapperPanel.setOpaque(false);
        panel.add(wrapperPanel, BorderLayout.CENTER);

        buildHeaderPanel();
        buildCategoryPanel();
        buildBodyPanel();
        loadInstantMessages(0);
    }

    private void buildHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        headerPanel.setPreferredSize(new Dimension(0, 35));
        headerPanel.setOpaque(false);
        wrapperPanel.add(headerPanel, BorderLayout.NORTH);
        titleLabel = new JLabel();
        titleLabel.setText("Can't talk to you right now!");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        headerPanel.add(titleLabel);
    }

    private void buildCategoryPanel() {
        JPanel hoverPanel = new JPanel(new BorderLayout(0, 0));
        hoverPanel.setBorder(new EmptyBorder(8, 5, 5, 5));
        hoverPanel.setPreferredSize(new Dimension(0, 28));
        hoverPanel.setOpaque(false);
        customLabel = new JLabel();
        customLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        customLabel.setText("Custom");
        customLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        customLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainClass.getMainClass().callIgnore.doClick();
                hideThis();
                GuiRingID.getInstance().getMainRight().showFriendProfile(userID, MyfriendSlider.TYPE_MY_FRIEND_CHAT_CALL);
                MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(userID).myFriendChatPanel.reqChatAreaFocus();
            }
        });
        JPanel customPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        customPanel.setOpaque(false);
        customPanel.setPreferredSize(new Dimension(105, 28));
        customPanel.add(customLabel);
        hoverPanel.add(customPanel, BorderLayout.CENTER);
        wrapperPanel.add(hoverPanel, BorderLayout.SOUTH);
    }

    private void buildBodyPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        wrapperPanel.add(mainPanel, BorderLayout.CENTER);
        msgContainer = new JPanel(new GridBagLayout());
        msgContainer.setOpaque(false);
        JPanel wPanel = new JPanel();
        wPanel.setLayout(new BorderLayout(0, 0));
        wPanel.setOpaque(false);
        wPanel.add(msgContainer, BorderLayout.NORTH);
        containerPanelScroll = DesignClasses.getDefaultScrollPaneThin(wPanel);// DesignClasses.getDefaultScorllPane(chatContainerPanel);
        containerPanelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(containerPanelScroll, BorderLayout.CENTER);
    }

    public class ImagePanel extends JPanel {

        public ImagePanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, null);
        }
    }

    public void hideThis() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(false);
                dispose();
                currentInstance = null;
            }
        });
    }

    private void buildFriendChatPacket(String msg) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageType(ChatConstants.TYPE_PLAIN_MSG);
        messageDTO.setPacketType(ChatConstants.CHAT_FRIEND);
        String packetID = SendToServer.getRanDomPacketID();
        messageDTO.setPacketID(packetID);
        messageDTO.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
        messageDTO.setFriendIdentity(userID);
        messageDTO.setMessage(msg);
        messageDTO.setStatus(ChatConstants.CHAT_FRIEND_PENDING);
        messageDTO.setSystemDate(System.currentTimeMillis());
        ChatService.sendFriendChat(messageDTO, new PacketResenderHandler(), InternetUnavailablityCheck.isInternetAvailable);
    }

    public void loadInstantMessages(final int type_emotion) {
        msgContainer.removeAll();
        GridBagConstraints con = new GridBagConstraints();
        con.gridx = 0;
        con.gridy = 0;
        con.anchor = GridBagConstraints.WEST;
        /*        msgContainer.add(Box.createRigidArea(new Dimension(2, 2)), con);
         con.gridy++;*/
//        for (final String str : HashMapsBeforeLogin.getInstance().getInstantMessages()) {
        List<InstantMessageDTO> insMsgs = InstantMessagesDAO.getInstantMessagesFromDB();
        for (InstantMessageDTO insMsg : insMsgs) {
            final String str = insMsg.getInstantMessage();
            try {
                final JLabel label = new JLabel(" " + str);
                label.setToolTipText(str);
                /*label.setBorder(BorderFactory.createCompoundBorder(
                 BorderFactory.createMatteBorder(2, 2, 2, 2, bg_color_all), BorderFactory.createEmptyBorder(0, 2, 0, 0)
                 ));*/
                label.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, bg_color_all));
                label.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ConfigFile.BUSY_TEXT = str;
                        hideThis();
                        MainClass.getMainClass().callIgnore.doClick();

                        /*
                         label.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, image_hover_border));
                         MainClass.getMainClass().callIgnore.doClick();
                         hideThis();
                         buildFriendChatPacket(str);
                         GuiRingID.getInstance().getMainRight().showFriendProfile(userID, MyfriendSlider.TYPE_MY_FRIEND_CHAT_CALL);*/
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        label.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, bg_color_all));
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        label.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, image_hover_border));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        label.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, bg_color_all));
                    }
                });
                JPanel jp = new JPanel(new BorderLayout());
                jp.setPreferredSize(new Dimension(POPUP_WIDTH - 65, 25));
                jp.setOpaque(false);
                jp.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.DEFAULT_BORDER_COLOR));
                jp.add(label, BorderLayout.CENTER);
                jp.add(Box.createRigidArea(new Dimension(3, 3)), BorderLayout.EAST);
                msgContainer.add(jp, con);
                con.gridy++;
            } catch (Exception ex) {
            }
        }
        msgContainer.repaint();
        msgContainer.revalidate();
        mainPanel.revalidate();
        mainPanel.repaint();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                containerPanelScroll.getVerticalScrollBar().setValue(0);
                containerPanelScroll.getHorizontalScrollBar().setValue(0);
                containerPanelScroll.revalidate();
                containerPanelScroll.repaint();
            }
        });
    }
}
