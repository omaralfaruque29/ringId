/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

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
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.service.SendFriendRequest;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.SingleBookDetails;
import com.ipvision.model.SingleMemberInCircleDto;
import com.ipvision.model.stores.FriendList;
import com.ipvision.view.utility.JDialogContactType;
import com.ipvision.view.utility.ImageHelpers;

/**
 *
 * @author Faiz Ahmed
 */
public class SharesInPost extends JDialog {

    private JPanel content;
    private JPanel profiles;
    int width = 260;
    int height = 325;
    private JLabel number_of_shares_label;
    private int breakLoop = 0;
    private int posX = 0;
    private int posY = 0;
    private Image img = DesignClasses.return_image(GetImages.LIKE_LIST).getImage();
    private JLabel loading;
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SharesInPost.class);
    private Map<String, SingleMemberInCircleDto> shares = new ConcurrentHashMap<String, SingleMemberInCircleDto>();
    private NewsFeedWithMultipleImage statusDto;
    private String defaultIdentity;

    public SharesInPost(JLabel componet, NewsFeedWithMultipleImage statusDto, String defualtIdentity) {
        this.number_of_shares_label = componet;
        this.statusDto = statusDto;
        this.defaultIdentity = defualtIdentity;
        setSize(width, height);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setLocationRelativeTo(componet);

        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                breakLoop = 1;
                hideThis();
            }
        });

    }

    public void initContainers() {
        try {
            JPanel wrapperPanel = new ImagePanel();
            wrapperPanel.setBorder(new EmptyBorder(17, 12, 17, 10));
            wrapperPanel.setOpaque(false);
            wrapperPanel.setLayout(new BorderLayout());
            wrapperPanel.addMouseListener(poxValueListener);
            wrapperPanel.addMouseMotionListener(frameDragListener);
            setContentPane(wrapperPanel);

            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setPreferredSize(new Dimension(0, 30));
            headerPanel.setOpaque(false);

            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            titlePanel.setOpaque(false);
            headerPanel.add(titlePanel, BorderLayout.WEST);

            JLabel titleLabel = DesignClasses.makeLableBold1("People who shared it", 11);
            titlePanel.add(titleLabel);

            JPanel crossPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            crossPanel.setOpaque(false);
            final JLabel closeButton = DesignClasses.create_imageJlabel(GetImages.BUTTON_CLOSE_MINI);
            crossPanel.add(closeButton);
            closeButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    breakLoop = 1;
                    hideThis();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    closeButton.setIcon(DesignClasses.return_image(GetImages.BUTTON_CLOSE_MINI_H));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    closeButton.setIcon(DesignClasses.return_image(GetImages.BUTTON_CLOSE_MINI));
                }
            });
            closeButton.setToolTipText("Close");
            closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            headerPanel.add(crossPanel, BorderLayout.EAST);
            wrapperPanel.add(headerPanel, BorderLayout.NORTH);

            content = new JPanel();
            content.setOpaque(false);
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

            JPanel nullPanle = new JPanel();
            nullPanle.setOpaque(false);
            nullPanle.add(content, BorderLayout.NORTH);

            JPanel scrollContent = new JPanel(new BorderLayout(0, 0));
            scrollContent.setOpaque(false);
            scrollContent.add(nullPanle, BorderLayout.CENTER);

            JScrollPane downContent = DesignClasses.getDefaultScrollPaneThin(scrollContent);// DesignClasses.getDefaultScorllPane(scrollContent);

            profiles = new JPanel();
            profiles.setOpaque(false);
            profiles.setLayout(new BorderLayout(MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN));
            profiles.setBorder(new EmptyBorder(MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN, MyFnFSettings.DEFAULT_MARGIN));
            profiles.add(downContent, BorderLayout.CENTER);

            wrapperPanel.add(profiles, BorderLayout.CENTER);

        } catch (Exception e) {
        }

    }

    public void addloding() {
        loading = DesignClasses.loadingLable(true);
        content.add(loading);
        content.revalidate();
        new LoadLikes(number_of_shares_label).start();
    }

//    public void addLikes(Map<String, SingleMemberInCircleDto> likes) {
//        content.removeAll();
//        for (String userid : likes.keySet()) {
//            SingleMemberInCircleDto fld = likes.get(userid);
//            JPanel dd = singleLike(fld);
//            content.add(dd);
//            content.add(Box.createRigidArea(new Dimension(0, 5)));
//        }
//        content.revalidate();
//    }
    public void addNewLike(SingleMemberInCircleDto friend) {

        boolean isNew = true;
        for (int idx = 0; idx < content.getComponentCount(); idx++) {
            if (content.getComponent(idx) instanceof JPanel && content.getComponent(idx).getName().equalsIgnoreCase(friend.getUserIdentity())) {
                isNew = false;
                break;
            }
        }

        if (isNew && !defaultIdentity.equalsIgnoreCase(friend.getUserIdentity())) {
            JPanel dd = singleLike(friend);
            content.add(dd);
            content.add(Box.createRigidArea(new Dimension(0, 5)));
            content.revalidate();

        }
    }

    public UserBasicInfo getUserInfo(SingleMemberInCircleDto js) {

        UserBasicInfo basicinfo;
        if (FriendList.getInstance().getFriend_hash_map().get(js.getUserIdentity()) != null) {
            basicinfo = FriendList.getInstance().getFriend_hash_map().get(js.getUserIdentity());
        } else if (js.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
            basicinfo = MyFnFSettings.userProfile;
        } else {
            basicinfo = new UserBasicInfo();
            if (js.getUserIdentity() != null) {
                basicinfo.setUserIdentity(js.getUserIdentity());
                basicinfo.setFullName(js.getUserIdentity());
            }
            if (js.getFullName() != null) {
                basicinfo.setFullName(js.getFullName());
            } else {
                basicinfo.setFullName(js.getUserIdentity());
            }
            /*if (js.getLastName() != null) {
                basicinfo.setLastName(js.getLastName());
            } else {
                basicinfo.setLastName("");
            }*/
        }

        return basicinfo;
    }

    public JPanel singleLike(final SingleMemberInCircleDto userid) {
        final UserBasicInfo basicinfo = getUserInfo(userid);

        final JPanel rowPanel = new JPanel();
        rowPanel.setName(basicinfo.getUserIdentity());
        rowPanel.setOpaque(false);
        rowPanel.setLayout(new BorderLayout());
        rowPanel.setPreferredSize(new Dimension(width - 50, 40));
        //rowPanel.setBorder(new EmptyBorder(0, 5, 3, 5));
        rowPanel.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.COMMENTS_BORDER_COLOR));
        JLabel frienImageLabel = new JLabel();

        String url = "";

        if (basicinfo != null && basicinfo.getProfileImage() != null) {
            url = basicinfo.getProfileImage();
        } else if (userid.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
            url = MyFnFSettings.userProfile.getProfileImage();
        }

        if (url.trim().length() > 0) {
            ImageHelpers.addProfileImageThumb(frienImageLabel, url);
        } else {
            BufferedImage img = ImageHelpers.getUnknownImage(true);//.getBufferImageFromurl(null);
            frienImageLabel.setIcon(new ImageIcon(img));
            img = null;
        }
//        if (url.trim().length() > 0) {
//            BufferedImage img = DesignClasses.getBufferImageFromurlFriendList(url, 34);
//            frienImageLabel.setIcon(new ImageIcon(img));
//        } else {
//            BufferedImage img = DesignClasses.getUnknownImage(true);
//            frienImageLabel.setIcon(new ImageIcon(img));
//        }
        frienImageLabel.revalidate();
        rowPanel.add(frienImageLabel, BorderLayout.WEST);

        JLabel fullname = DesignClasses.makeAncorLabelDefaultColor(userid.getFullName(), 0, 12);
        /*if (userid.getLastName() != null && userid.getLastName().length() > 0) {
            fullname.setText(userid.getFirstName() + " " + userid.getLastName());
        } else {
            fullname.setText(userid.getFirstName());
        }*/
        fullname.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fullname.addMouseListener(new MouseAdapter() {
            Font original;

            @Override
            public void mouseEntered(MouseEvent e) {
                original = e.getComponent().getFont();
                Map attributes = original.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                e.getComponent().setFont(original.deriveFont(attributes));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.getComponent().setFont(original);
            }

            @Override
            public void mouseClicked(MouseEvent arg0) {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (basicinfo != null) {
                            if (basicinfo.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
                                GuiRingID.getInstance().action_of_myProfile_button();
                            } else if (FriendList.getInstance().getFriend_hash_map().get(basicinfo.getUserIdentity()) != null) {
                                GuiRingID.getInstance().showFriendProfile(basicinfo.getUserIdentity());
                            } else {
                                GuiRingID.getInstance().showUnknownProfile(basicinfo);
                            }
                            hideThis();
                        }
                    }
                });
            }
        });
        JPanel fullnamePnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fullnamePnl.setBorder(new EmptyBorder(5, 10, 0, 0));
        fullnamePnl.setOpaque(false);
        fullnamePnl.add(fullname);
        rowPanel.add(fullnamePnl, BorderLayout.CENTER);

        final JLabel addBtn = DesignClasses.create_imageJlabel(GetImages.ADD_FRIEND_POPUP);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JDialogContactType dialog = new JDialogContactType();
                if (JDialogContactType.contactType > 0) {
                    new SendFriendRequest(basicinfo.getUserIdentity(), addBtn, JDialogContactType.contactType).start();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        addBtn.setToolTipText("Send request");
        if (!basicinfo.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID) && FriendList.getInstance().getFriend_hash_map().get(basicinfo.getUserIdentity()) == null) {
            rowPanel.add(addBtn, BorderLayout.EAST);
        }

        return rowPanel;
    }

    class LoadLikes extends Thread {

        private JLabel shareLabel;
        private int shares_num = 0;

        LoadLikes(JLabel likeseLabel) {
            this.shareLabel = likeseLabel;

        }

       @Override
        public void run() {
            try {
               // for (SingleBookDetails bookDetails : statusDto.getWhoShare()) {
                for (SingleBookDetails bookDetails : statusDto.getWhoShare()){
                    SingleMemberInCircleDto addMe = new SingleMemberInCircleDto();
                    addMe.setUserIdentity(bookDetails.getUserIdentity());

                    addMe.setFullName(bookDetails.getFullName());
                    //addMe.setLastName(bookDetails.getLastName());

                    
                    shares.put(bookDetails.getUserIdentity(), addMe);
                    
                }

                for (int i = 0; i <= 10; i++) {

                    if (shares != null && !shares.isEmpty() && shares.size() > shares_num) {
                        shares_num = shares.size();
                        content.remove(loading);
                        for (String userid : shares.keySet()) {
                            SingleMemberInCircleDto frd = shares.get(userid);
                            addNewLike(frd);
                        }
                    }
                    if (breakLoop == 1) {
                        return;
                    }
                    Thread.sleep(150);
                }
                //hideThis();
            } catch (Exception ex) {
                //ex.printStackTrace();
                log.error("SharesInPost-->" + ex.getMessage());
                hideThis();
            }

        }
//         private class AddDetailsOfNotifications extends Thread {
//
//        Long statusID;
//
//        public AddDetailsOfNotifications(Long status_id) {
//            this.statusID = status_id;
//        }

//        @Override
//        public void run() {
//            try {
//                JsonFields js = new JsonFields();
//                js.setAction(AppConstants.TYPE_SINGLE_STATUS_NOTIFICATION);
//                String pakId = SendToServer.getRanDomPacketID();
//                js.setPacketId(pakId);
//                js.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
//                js.setNfId(this.statusID);
//                SendToServer.sendPacketAsString(js, ServerAndPortSettings.REQUEST_PORT);
//
//                for (int i = 1; i <= 5; i++) {
//                    Thread.sleep(500);
//                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
//                        SendToServer.sendPacketAsString(js, ServerAndPortSettings.REQUEST_PORT);
//                    } else {
//                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
//                        statusDto = NewsFeedMaps.getInstance().getAllNewsFeeds().get(statusDto.getNfId());
//                        Thread.sleep(1500);
//                        break;
//                    }
//                }
//
//                content.removeAll();
//                 for (String userid : shares.keySet()) {
//                            SingleMemberInCircleDto frd = shares.get(userid);
//                            addNewLike(frd);
//                        }
//               // addNewLike(frd);
//                content.revalidate();
//            } catch (Exception ed) {
//            }
//        }
   // }
    }
    

    private void hideThis() {
        this.setVisible(false);
        this.dispose();
        shares = null;
        System.gc();
        Runtime.getRuntime().gc();

    }
    MouseAdapter poxValueListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            posX = e.getX();
            posY = e.getY();
        }
    };

    MouseMotionAdapter frameDragListener = new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent evt) {
            setLocation(evt.getXOnScreen() - posX, evt.getYOnScreen() - posY);
        }
    };

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

}
