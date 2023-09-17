/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.constants.StaticFields;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.SingleBookDetails;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.SendToServer;
import static com.ipvision.view.feed.SingleFeedStructure.instance;
import com.ipvision.view.friendlist.SingleFriendPanel;
import com.ipvision.view.utility.FeedUtils;
import com.ipvision.view.utility.FriendNameMouseListener;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.view.utility.ImagePane;

/**
 *
 * @author Faiz
 */
public class FreindDetailsForFeeds extends JPanel implements MouseListener {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FreindDetailsForFeeds.class);
    SingleFeedStructure singleFeedStructure;
    NewsFeedWithMultipleImage statusDto;

    JLabel userName;
    JLabel circleName;
    JLabel friendName;
    Font original;
    UserBasicInfo basicinfo;
    private boolean isShared;
    private JLabel friend_image_panel;
    private JLabel timeLabel;
    private JPanel fullNameContainerPanel;
    private JPanel fullNameAndRemoveCommentPanel;
    JLabel seperatorLabel;
    JLabel albumNameLabel;
    JLabel infoLabel;
    JButton editRemoveStatusButton;
    boolean forceShared = false;
    public ImagePane imagePaneMain;
    // private boolean isUserLogin = true;

    public JPanel panelSequenc;
    public JPanel panelShareSequenc;
    private BufferedImage image = null;
    public static FreindDetailsForFeeds instance;

    public static FreindDetailsForFeeds getInatance() {
        return instance;
    }

    public FreindDetailsForFeeds(SingleFeedStructure singleFeedStructure, boolean forceShared) {
        this.instance = this;
        this.singleFeedStructure = singleFeedStructure;
        this.statusDto = this.singleFeedStructure.statusDto;
        this.forceShared = forceShared;
        //instance = instance.getShareThisFeedJopUp();

        setBasicUserinfo();
        setOpaque(false);
        setLayout(new BorderLayout(2, 2));
        JPanel basicPanel = new JPanel();
        basicPanel.setOpaque(false);
        basicPanel.setLayout(new BoxLayout(basicPanel, BoxLayout.X_AXIS));
        add(basicPanel, BorderLayout.CENTER);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 2));
        panel.setBackground(Color.GREEN);
        panel.setOpaque(false);
        //panel.add(singleFeedStructure.lblSequenceNumber);
        add(panel, BorderLayout.EAST);
        if (!forceShared) {
            imagePaneMain = new ImagePane();
            try {
                image = ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.SEQUENCE_NUMBER_ICON));

            } catch (Exception e) {
            }

            if (singleFeedStructure.lblSequenceNumber != null) {
                imagePaneMain.setImage(image);
                imagePaneMain.setLayout(new FlowLayout(FlowLayout.CENTER));
                imagePaneMain.add(singleFeedStructure.lblSequenceNumber);
                panel.add(imagePaneMain);
            } else {
                System.out.println(" no sequence number");
            }
        }

        JPanel fullNameUpdateTextTimeLabel = new JPanel(new BorderLayout(2, 2));
        fullNameUpdateTextTimeLabel.setOpaque(false);
        //JPanel fullNameAndRemoveCommentPanel = new JPanel(new BorderLayout());
        fullNameAndRemoveCommentPanel = new JPanel(new BorderLayout());
        fullNameAndRemoveCommentPanel.setOpaque(false);
        fullNameContainerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 2));
        fullNameContainerPanel.setOpaque(false);
        userName = DesignClasses.makeJLabelFullName2("", DefaultSettings.FULL_NAME_FONT_SIZE_IN_FEED);
        //userName = DesignClasses.makeJLabelFullName2(this.basicinfo.getFullName() + " " + this.basicinfo.getLastName(), DefaultSettings.FULL_NAME_FONT_SIZE_IN_FEED);
        userName.setCursor(new Cursor(Cursor.HAND_CURSOR));
        userName.addMouseListener(this);
        fullNameContainerPanel.add(userName);
        infoLabel = DesignClasses.makeFeedUpdatedStringLabel("");
        if (!this.isShared) {
            fullNameUpdateTextTimeLabel.setBorder(new EmptyBorder(0, 5, 8, 2));
            friend_image_panel = new JLabel();
            friend_image_panel.setPreferredSize(new Dimension(38, 38));
            basicPanel.add(friend_image_panel);
        } else {
            fullNameUpdateTextTimeLabel.setBorder(new EmptyBorder(0, 5, 0, 0));
            basicPanel.setPreferredSize(new Dimension(0, 30));

        }
        fullNameAndRemoveCommentPanel.add(fullNameContainerPanel, BorderLayout.WEST);
        if (statusDto != null && (statusDto.getUserIdentity() == null || statusDto.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID) && statusDto.getWhoShare() == null)) {
            if (ShareThisFeedPopUp.getInstance() == null) {
                addEditLabel(fullNameAndRemoveCommentPanel);
            }
        }
        fullNameUpdateTextTimeLabel.add(fullNameAndRemoveCommentPanel, BorderLayout.CENTER);
        timeLabel = DesignClasses.makeFeedUpdatedStringLabel("");//DesignClasses.makeJLabel_normal("", 11, RingColorCode.FEED_UPDATED_STRING_COLOR);//create_what_in_mind_label(what, 75, Gui24FnFNew.getInstance().fnf_left_width - 100);
        fullNameUpdateTextTimeLabel.add(timeLabel, BorderLayout.SOUTH);
        basicPanel.add(fullNameUpdateTextTimeLabel);
    }

    public void sequenceNumberImagePane() {
        imagePaneMain.setVisible(false);
    }

    private void gotoFriendProfile(UserBasicInfo userBasicInfo) {
        if (userBasicInfo.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
            GuiRingID.getInstance().action_of_myProfile_button();
        } else if (FriendList.getInstance().getFriend_hash_map().get(userBasicInfo.getUserIdentity()) != null) {
            GuiRingID.getInstance().showFriendProfile(userBasicInfo.getUserIdentity());
            SingleFriendPanel singleFriendPanel = new SingleFriendPanel(userBasicInfo);
            singleFriendPanel.friendInfoPanel.setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
        } else {
            GuiRingID.getInstance().showUnknownProfile(userBasicInfo);
        }

    }

    String youUserId;

    private void addSharedInfo() {

        final ArrayList<SingleBookDetails> whoShareList = statusDto.getWhoShare();
        if (whoShareList != null && whoShareList.size() > 0) {
            Collections.sort(whoShareList, new Comparator<SingleBookDetails>() {
                @Override
                public int compare(SingleBookDetails one, SingleBookDetails other) {
                    Long onesTime = one.getUt();
                    Long othersTime = other.getUt();
                    return othersTime.compareTo(onesTime);
                }
            });

            JLabel firstLabel = DesignClasses.makeJLabelFullName2(whoShareList.get(0).getFullName(), DefaultSettings.FULL_NAME_FONT_SIZE_IN_FEED);
            if (FeedUtils.whoShareDetails(whoShareList)) {
                firstLabel.setText(StaticFields.STRING_YOU);
                youUserId = MyFnFSettings.LOGIN_USER_ID;

            } else {
                if (FeedUtils.isMe(whoShareList.get(0).getUserIdentity())) {
                    firstLabel.setText(StaticFields.STRING_YOU);
                    youUserId = MyFnFSettings.LOGIN_USER_ID;

                } else {
                    firstLabel.setText(whoShareList.get(0).getFullName());
                    youUserId = whoShareList.get(0).getUserIdentity();
                    firstLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    UserBasicInfo firstPerson = new UserBasicInfo();
                    firstPerson.setUserIdentity(whoShareList.get(0).getUserIdentity());
                    onClickFriendName(firstLabel, firstPerson);
                }

            }

            fullNameContainerPanel.add(firstLabel);

            if (whoShareList.size() == 2) {
                //JLabel andLabel = DesignClasses.makeJLabel_normal(" and ", 12, Color.BLACK);
                JLabel andLabel = DesignClasses.makeFeedUpdatedStringLabel(" and ");// DesignClasses.makeJLabel_normal(" and ", 12, RingColorCode.DEFAULT_FORGROUND_COLOR);
                fullNameContainerPanel.add(andLabel);

                JLabel secondLabel = DesignClasses.makeJLabelFullName2("", DefaultSettings.FULL_NAME_FONT_SIZE_IN_FEED);
                int index = 1;
                if (FeedUtils.isMe(whoShareList.get(1).getUserIdentity())) {
                    secondLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    UserBasicInfo secondPerson = new UserBasicInfo();
                    index = 0;
                    secondPerson.setUserIdentity(whoShareList.get(index).getUserIdentity());
                    secondLabel.setText(whoShareList.get(index).getFullName());
                    onClickFriendName(secondLabel, secondPerson);
                } else {
                    secondLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    UserBasicInfo secondPerson = new UserBasicInfo();
                    secondPerson.setUserIdentity(whoShareList.get(index).getUserIdentity());
                    secondLabel.setText(whoShareList.get(index).getFullName());
                    onClickFriendName(secondLabel, secondPerson);
                }
                fullNameContainerPanel.add(secondLabel);
            } else if (whoShareList.size() > 2) {
                JLabel andLabel = DesignClasses.makeFeedUpdatedStringLabel(" and ");//DesignClasses.makeJLabel_normal(" and ", 12, RingColorCode.FEED_UPDATED_STRING_COLOR);
                fullNameContainerPanel.add(andLabel);
                //final JLabel otherLabel = DesignClasses.makeJLabelFullName2((whoShareList.size() - 1 ) + " Other", 13);
                final JLabel otherLabel = DesignClasses.makeJLabelFullName2((statusDto.getTs() - 1) + " Other", DefaultSettings.FULL_NAME_FONT_SIZE_IN_FEED);

                otherLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                otherLabel.addMouseListener(new MouseAdapter() {
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
                    public void mouseClicked(MouseEvent e) {
                        e.getComponent().setFont(original);
                        //(new SingleStatusHelper.LoadStatusDetail(statusDto.getNfId())).start(); //statusDto.getUserIdentity()
                        ShareOtherList shareOtherList = new ShareOtherList(otherLabel, statusDto, youUserId);
                        shareOtherList.initContainers();
                        shareOtherList.setVisible(true);
//                        SharesInPost shares = new SharesInPost(otherLabel, statusDto, whoShareList.get(0).getUserIdentity());
//                        shares.initContainers();
//                        shares.addloding();
//                        shares.setVisible(true);
                    }
                });
                fullNameContainerPanel.add(otherLabel);
            }
            JLabel sLabel = DesignClasses.makeFeedUpdatedStringLabel(" shared ");//DesignClasses.makeJLabel_normal(" shared ", 12, RingColorCode.FEED_UPDATED_STRING_COLOR);
            fullNameContainerPanel.add(sLabel);

            JLabel statusOwnerLabel = DesignClasses.makeJLabelFullName2(this.basicinfo.getFullName(), DefaultSettings.FULL_NAME_FONT_SIZE_IN_FEED);
//            String whoseFullName = this.basicinfo.getFullName();
//            if (this.basicinfo.getLastName() != null && this.basicinfo.getLastName().length() > 0) {
//                whoseFullName = this.basicinfo.getFullName() + " " + this.basicinfo.getLastName();
//                //System.err.println(whoseFullName);
//            } 
            if (this.statusDto.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
                statusOwnerLabel.setText("Your");
            } else {
                /* if (this.basicinfo.getLastName() != null && this.basicinfo.getLastName().length() > 0) {
                 statusOwnerLabel.setText(this.basicinfo.getFullName() + " " + this.basicinfo.getLastName() + "'s");
                 } else {*/
                statusOwnerLabel.setText(this.basicinfo.getFullName() + "'s");
                // }
                statusOwnerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                onClickFriendName(statusOwnerLabel, this.basicinfo);

            }
//            whoseFullName = whoseFullName + "'s";
//            statusOwnerLabel.setText(whoseFullName);
//            statusOwnerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
//            onClickFriendName(statusOwnerLabel, this.basicinfo);

            fullNameContainerPanel.add(statusOwnerLabel);
            if (this.statusDto.getFriendIdentity() == null && this.statusDto.getType() == 1) {
                if (statusDto.getImageList() != null) {
                    JsonFields imageDetails = statusDto.getImageList().get(0);
                    infoLabel.setText(" photo");
                    if (imageDetails.getImT() != null && imageDetails.getImT() == 2) {
                        infoLabel.setText(" profile photo");
                    } else if (imageDetails.getImT() != null && imageDetails.getImT() == 3) {
                        infoLabel.setText(" cover photo");
                    }
                }
            } else if (this.statusDto.getFriendIdentity() == null && this.statusDto.getImageList() != null && (this.statusDto.getType() == 2 || this.statusDto.getType() == 3 || this.statusDto.getType() == 4)) {
                infoLabel.setText(" photo");
            } else {
                infoLabel.setText(" post");
            }
            fullNameContainerPanel.add(infoLabel);
//            if (youUserId == MyFnFSettings.LOGIN_USER_ID) {
//                if (ShareThisFeedPopUp.getInstance() == null) {
//                    addEditLabel(fullNameAndRemoveCommentPanel);
//                }
//            }
        }
    }

    private void setBasicUserinfo() {
        if (this.statusDto != null) {
            if (this.statusDto.getUserIdentity() == null) {
                basicinfo = MyFnFSettings.userProfile;
            } else if (this.statusDto.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
                basicinfo = MyFnFSettings.userProfile;
            } else {
                if (this.statusDto.getUserIdentity() != null && FriendList.getInstance() != null && !FriendList.getInstance().getFriend_hash_map().isEmpty()) {

                    if (FriendList.getInstance().getFriend_hash_map().get(this.statusDto.getUserIdentity()) != null) {
                        basicinfo = FriendList.getInstance().getFriend_hash_map().get(this.statusDto.getUserIdentity());
                    } else {
                        basicinfo = new UserBasicInfo();
                        basicinfo.setUserIdentity(this.statusDto.getUserIdentity());
                        basicinfo.setFullName(this.statusDto.getFullName());
                        //basicinfo.setLastName(this.statusDto.getLastName());
                    }
                } else {
                    basicinfo = new UserBasicInfo();
                    basicinfo.setUserIdentity(this.statusDto.getUserIdentity());
                    basicinfo.setFullName(this.statusDto.getFullName());
                    //basicinfo.setLastName(this.statusDto.getLastName());
                }

            }
            if (this.forceShared) {
                this.isShared = false;
            } else if (statusDto.getTs() != null && statusDto.getTs() > 0 && statusDto.getWhoShare() != null && statusDto.getWhoShare().size() > 0) {
                this.isShared = true;
            }
        } else {
        }
    }

    public void addDefaultSettings() {
        try {
            //  whoLoveOrComment();
            if (this.isShared) {
                addSharedInfo();
                /*if (statusDto.getWhoShare().size() <= statusDto.getTs()) {
                 new AddDetailsOfNotifications(statusDto.getNfId()).start();
                 }*/

            } else {
                seperatorLabel = DesignClasses.create_imageJlabel(GetImages.BOOK_ARROW);
                //seperatorLabel.setBorder(new EmptyBorder(0, 5, 0, 5));
                circleName = DesignClasses.makeJLabelFullName2("", DefaultSettings.FULL_NAME_FONT_SIZE_IN_FEED);
                circleName.setCursor(new Cursor(Cursor.HAND_CURSOR));
                circleName.addMouseListener(this);
                friendName = DesignClasses.makeJLabelFullName2("", DefaultSettings.FULL_NAME_FONT_SIZE_IN_FEED);
                friendName.setCursor(new Cursor(Cursor.HAND_CURSOR));
                friendName.addMouseListener(this);
                fullNameContainerPanel.add(seperatorLabel);
                fullNameContainerPanel.add(circleName);
                fullNameContainerPanel.add(friendName);
                fullNameContainerPanel.add(infoLabel);
                albumNameLabel = DesignClasses.makeLableBold1("", Color.BLACK);
                fullNameContainerPanel.add(albumNameLabel);
                if (friend_image_panel != null) {
                    if (this.basicinfo != null) {
                        ImageHelpers.addProfileImageThumb(friend_image_panel, this.basicinfo.getProfileImage(), 38);
                    } else {
                        BufferedImage bi = ImageHelpers.getUnknownImage(true);
                        friend_image_panel.setIcon(new ImageIcon(bi));
                        bi.flush();
                        bi = null;
                    }

                }
                addtime();
                updateString();
            }

            fullNameContainerPanel.revalidate();
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Error in addDefaultSettings method ==>" + ex.getMessage());
        }
    }

//    @Override
//    public void hierarchyChanged(HierarchyEvent e) {
//        if (friend_image_panel != null) {
//            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) > 0 && friend_image_panel.is()) {
//                if (statusDto.getNfId() != null) {
//                    System.out.println("statusDto.getNfId()==>"+statusDto.getNfId());
//                }
//            }
//        }
//    }
    private class AddDetailsOfNotifications extends Thread {

        Long statusID;

        public AddDetailsOfNotifications(Long status_id) {
            this.statusID = status_id;
        }

        @Override
        public void run() {
            try {
                JsonFields js = new JsonFields();
                js.setAction(AppConstants.TYPE_SINGLE_STATUS_NOTIFICATION);
                String pakId = SendToServer.getRanDomPacketID();
                js.setPacketId(pakId);
                js.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                js.setNfId(this.statusID);
                SendToServer.sendPacketAsString(js, ServerAndPortSettings.REQUEST_PORT);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(js, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        statusDto = NewsFeedMaps.getInstance().getAllNewsFeeds().get(statusDto.getNfId());
                        Thread.sleep(1500);
                        break;
                    }
                }

                fullNameContainerPanel.removeAll();
                fullNameContainerPanel.add(userName);
                addSharedInfo();

            } catch (Exception ed) {
            }
        }
    }

    private void addtime() {
        String addedTime = "";
        if (this.statusDto == null) {
            addedTime = "time error";
        } else if (this.statusDto.getAddedTime() != null) {
            addedTime = FeedUtils.getShowableDate(this.statusDto.getAddedTime());
            timeLabel.setToolTipText(FeedUtils.getActualDate(this.statusDto.getAddedTime()));
        } else if (this.statusDto.getTm() != null) {
            addedTime = FeedUtils.getShowableDate(this.statusDto.getTm());
            timeLabel.setToolTipText(FeedUtils.getActualDate(this.statusDto.getTm()));
        }
        timeLabel.setText(addedTime);

    }

    private void updateString() {
        if (this.basicinfo != null) {
//            if (this.basicinfo.getLastName() != null && this.basicinfo.getLastName().length() > 0){
//                userName.setText(this.basicinfo.getFullName() + " " + this.basicinfo.getLastName());
//            }else{
//                userName.setText(this.basicinfo.getFullName()); 
//            }

//            } else {
//                userName.setText(this.basicinfo.getFullName() + " " + this.basicinfo.getLastName());
//            }
            String fullName = this.basicinfo.getFullName();
            /*if (this.basicinfo.getLastName() != null && this.basicinfo.getLastName().length() > 0) {
             fullName = fullName + " " + this.basicinfo.getLastName();
             }*/
            userName.setText(fullName);

            if (this.statusDto.getCircleId() != null && this.statusDto.getCircleId() > 0) {
                seperatorLabel.setBorder(new EmptyBorder(0, 5, 0, 5));

                // circleName.setText(this.statusDto.getCircleName() != null ? this.statusDto.getCircleName() : "Group");
                if (this.statusDto.getCircleName() != null && this.statusDto.getCircleName().length() > 0) {
                    circleName.setText(this.statusDto.getCircleName());

                } else {
                    if (MyfnfHashMaps.getInstance().getCircleLists().get(this.statusDto.getCircleId()) != null) {
                        circleName.setText(MyfnfHashMaps.getInstance().getCircleLists().get(this.statusDto.getCircleId()).getCircleName());

                    } else {

                        circleName.setText("Circle");
                    }
                }
            } else if (this.statusDto.getFriendIdentity() != null && this.statusDto.getFriendIdentity().trim().length() > 0) {
                //friendName.setText(this.statusDto.getFfn() + " " + this.statusDto.getFln());
                String fName = this.statusDto.getFfn();
                /*if (this.statusDto.getFln() != null && this.statusDto.getFln().length() > 0) {
                 fName = this.statusDto.getFfn() + " " + this.statusDto.getFln();
                 }*/
                friendName.setText(fName);
                seperatorLabel.setBorder(new EmptyBorder(0, 5, 0, 5));
            } else if (singleFeedStructure.parentNfID != null && singleFeedStructure.parentNfID > 0) {
                seperatorLabel.setIcon(null);
            } else {
                seperatorLabel.setIcon(null);
                if (this.statusDto.getCircleId() == null) {
                    this.statusDto.setCircleId(0L);
                }

                String updatedText = "updated status";

                String albumName = null;
                if (this.statusDto.getFriendIdentity() == null && this.statusDto.getCircleId() != null && this.statusDto.getCircleId() == 0 && this.statusDto.getType() == 1) {

                    if (statusDto.getImageList() != null) {
                        updatedText = "posted a photo";
                        JsonFields imageDetails = statusDto.getImageList().get(0);
                        if (imageDetails.getImT() != null && imageDetails.getImT() == 1) {
                            updatedText = "posted a photo";//);
                        } else if (imageDetails.getImT() != null && imageDetails.getImT() == 2) {
                            updatedText = "changed profile photo";
                        } else if (imageDetails.getImT() != null && imageDetails.getImT() == 3) {
                            updatedText = "changed cover photo";
                        }
                    }
                } else if (this.statusDto.getFriendIdentity() == null && this.statusDto.getCircleId() != null && this.statusDto.getCircleId() == 0 && this.statusDto.getType() == 2) {

                    if (statusDto.getImageList() != null) {
                        updatedText = "posted a photo";
                    }
                } else if (this.statusDto.getFriendIdentity() == null && this.statusDto.getCircleId() != null && this.statusDto.getCircleId() == 0 && this.statusDto.getType() == 3) {
                    int numberofPhotos = 0;
                    if (statusDto != null && statusDto.getTotalImage() != null) {
                        numberofPhotos = statusDto.getTotalImage();
                    }
                    if (numberofPhotos == 0 && statusDto.getImageList() != null && statusDto.getImageList().size() > 0) {
                        numberofPhotos = statusDto.getImageList().size();
                    }

                    updatedText = "posted " + numberofPhotos + " new" + (numberofPhotos > 1 ? " photos" : " photo");
                } else if (this.statusDto.getFriendIdentity() == null && this.statusDto.getCircleId() != null && this.statusDto.getCircleId() == 0 && this.statusDto.getType() == 4) {

                    int numberofPhotos = 0;
                    if (statusDto != null && statusDto.getTotalImage() != null) {
                        numberofPhotos = statusDto.getTotalImage();
                    }
                    if (numberofPhotos == 0 && statusDto.getImageList() != null && statusDto.getImageList().size() > 0) {
                        numberofPhotos = statusDto.getImageList().size();
                    }

                    if (statusDto.getImageList() != null && statusDto.getImageList().get(0).getAlbn() != null) {
                        albumName = statusDto.getImageList().get(0).getAlbn();
                    }

                    if (numberofPhotos > 0) {
                        if (albumName != null && albumName.length() > 0) {
                            updatedText = "added " + numberofPhotos + " new" + ((numberofPhotos > 1) ? " photos" : " photo") + " in ";
                        } else {
                            updatedText = "added " + numberofPhotos + " new" + ((numberofPhotos > 1) ? " photos" : " photo") + " in an album";
                        }
                    } else {
                        updatedText = "created an album";
                    }
                    if (statusDto.getImageList() != null && statusDto.getImageList().get(0).getAlbn() != null) {
                        albumName = statusDto.getImageList().get(0).getAlbn();
                    }

                }
                if (updatedText.length() > 0) {
                    infoLabel.setText(updatedText);
                    infoLabel.setForeground(RingColorCode.FEED_UPDATED_STRING_COLOR);
                }
                if (albumName != null) {
                    albumNameLabel.setText(albumName);
                }
            }
        }
    }

    private void addEditLabel(JPanel fullNameAndRemoveCommentPanel) {
        editRemoveStatusButton = DesignClasses.createImageButton(GetImages.IMAGE_BOTTOM_ARROW, GetImages.IMAGE_BOTTOM_ARROW_H, "Edit/Delete this post");
        editRemoveStatusButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editRemoveStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FeedActionPopup popup = new FeedActionPopup(singleFeedStructure);
                if (!popup.isOnlyDelete) {
                    popup.optionsPopup_edit_delete.setVisible(editRemoveStatusButton, 120, -8);

                } else if (popup.isOnlyDelete) {
                    popup.optionsPopup_only_delete.setVisible(editRemoveStatusButton, 120, -8);
                }
                //editRemoveStatusButton.addMouseListener(singleFeedStructure);

                // popup.add(editRemoveStatusButton, (editRemoveStatusButton.getHorizontalAlignment() - 43), (editRemoveStatusButton.getVerticalAlignment() + 16));
            }
        });
        fullNameAndRemoveCommentPanel.add(editRemoveStatusButton, BorderLayout.EAST);
    }

    private void onClickFriendName(final JLabel label, final UserBasicInfo userBasicInfo) {
        label.addMouseListener(new FriendNameMouseListener(userBasicInfo));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        e.getComponent().setFont(original);
        UserBasicInfo userInfo = null;
        if (e.getComponent() == userName) {
            userInfo = this.basicinfo;
        } else if (e.getComponent() == friendName) {
            if (statusDto.getFriendIdentity() != null && FriendList.getInstance().getFriend_hash_map().get(statusDto.getFriendIdentity()) != null) {
                userInfo = FriendList.getInstance().getFriend_hash_map().get(statusDto.getFriendIdentity());
            } else if (statusDto.getFriendIdentity() != null && statusDto.getFriendIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
                userInfo = this.basicinfo;
            } else {
                userInfo = new UserBasicInfo();
                userInfo.setUserIdentity(statusDto.getFriendIdentity());
                userInfo.setFullName(statusDto.getFfn());
                // userInfo.setLastName(statusDto.getFln());
            }

        } else if (e.getComponent() == circleName) {
            if (statusDto.getCircleId() != null) {
                GuiRingID.getInstance().getMainRight().showCircleProfile(statusDto.getCircleId());
            }
        }
        if (userInfo != null) {
            if (ShareThisFeedPopUp.getInstance() != null && ShareThisFeedPopUp.getInstance().isDisplayable()) {
                ShareThisFeedPopUp.getInstance().dispose_this();
            }
            //ShareThisFeedPopUp.getInstance() 
            gotoFriendProfile(userInfo);
        }
    }

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
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

}
