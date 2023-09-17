/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

import com.ipvision.view.utility.ImageHelpers;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.ListOfLikesCommentInImage;
import com.ipvision.view.feed.EditComment;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.SingleMemberInCircleDto;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.view.utility.FeedUtils;
import com.ipvision.view.feed.AnimatedLike;
import com.ipvision.view.utility.TextPanelWithEmoticon;

/**
 *
 * @author Faiz
 */
public class SingeImageCommentDetails extends JPanel {

    public JsonFields commentDto;
    private UserBasicInfo basicinfo = new UserBasicInfo();
    private int imagesize = 35;
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SingeImageCommentDetails.class);
    public JLabel like_this_comment_label;
    public JLabel number_of_likes_on_this_comment, number_of_likes_label;
    // public String imageOwnerId;
    private JLabel timeLabel;
    public JPanel textPanel;
    public AddImageCaptionCommentsPanel addImageCaptionCommentsPanel;
    private Map<String, SingleMemberInCircleDto> imageCommentsLikes = new ConcurrentHashMap<String, SingleMemberInCircleDto>();
    public static SingeImageCommentDetails instance;
    AnimatedLike animatedLike;
    TextPanelWithEmoticon textPanelWithEmoticon;
    public JPanel likeCommentsAndTimePanel;

    public static SingeImageCommentDetails getInatance() {
        return instance;
    }

    public Map<String, SingleMemberInCircleDto> getImageCommentsLikes() {
        return imageCommentsLikes;
    }

    public void setImageCommentsLikes(Map<String, SingleMemberInCircleDto> imageLikes) {
        this.imageCommentsLikes = imageLikes;
    }

    public SingeImageCommentDetails(AddImageCaptionCommentsPanel addImageCaptionCommentsPanel, JsonFields commentDto) {
        this.addImageCaptionCommentsPanel = addImageCaptionCommentsPanel;
        this.instance = this;
//        this.setBackground(Color.WHITE);

        //   this.reqular_expresion = addImageCaptionCommentsPanel.reqular_expresion;
        this.commentDto = commentDto;
        this.setLayout(new BorderLayout(0, 0));
//        this.imageOwnerId = addImageCaptionCommentsPanel.statusDto.getUserIdentity();
        like_this_comment_label = DesignClasses.likesCommentsText(StaticFields.likeText, 0, 12);
        like_this_comment_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        number_of_likes_on_this_comment = DesignClasses.makeAncorLabel("", 0, 12);
        number_of_likes_on_this_comment.setCursor(new Cursor(Cursor.HAND_CURSOR));
        number_of_likes_on_this_comment.setPreferredSize(new Dimension(20, 20));
        number_of_likes_label = DesignClasses.makeAncorLabel("", 0, 12);
        number_of_likes_label.setBorder(new EmptyBorder(2, 0, 0, 30));
        number_of_likes_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false);
        initContainers();
    }

    public void addCommentUIinMap() {
        this.addImageCaptionCommentsPanel.getCommentsOfthisImage().put(this.commentDto.getCmnId(), this);
    }

    private void initContainers() {
        try {
            if (this.commentDto.getUserIdentity() == null || this.commentDto.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
                this.basicinfo = MyFnFSettings.userProfile;
            } else {
                if (this.commentDto.getUserIdentity() != null && FriendList.getInstance() != null && !FriendList.getInstance().getFriend_hash_map().isEmpty()) {

                    if (FriendList.getInstance().getFriend_hash_map().get(this.commentDto.getUserIdentity()) != null) {
                        this.basicinfo = FriendList.getInstance().getFriend_hash_map().get(this.commentDto.getUserIdentity());
                    } else {
                        basicinfo.setUserIdentity(this.commentDto.getUserIdentity());
                        this.basicinfo.setFullName(this.commentDto.getFullName());
                        // this.basicinfo.setLastName(this.commentDto.getLastName());
                    }
                } else {
                    basicinfo.setUserIdentity(this.commentDto.getUserIdentity());
                    this.basicinfo.setFullName(this.commentDto.getFullName());
                    //  this.basicinfo.setLastName(this.commentDto.getLastName());
                }

            }
            JPanel status_name_Panel = new JPanel(new BorderLayout(0, 0));
            status_name_Panel.setOpaque(false);
            status_name_Panel.setBorder(new EmptyBorder(1, 0, 0, 3));
            //status_name_Panel.setBackground(Color.WHITE);
//            status_name_Panel.setBackground(DefaultSettings.COMMENTS_BACK_GROUND_COLOR);

            JPanel profileImageName = new JPanel(new BorderLayout(0, 5));
            profileImageName.setOpaque(false);
            JLabel friend_image_panel = new JLabel();
            friend_image_panel.setPreferredSize(new Dimension(this.imagesize, this.imagesize));
            ImageHelpers.addProfileImageThumb(friend_image_panel, this.basicinfo.getProfileImage());
//            BufferedImage img = DesignClasses.getBufferImageFromurlFriendList(this.basicinfo.getProfileImage(), this.imagesize);
//            try {
            //  img = DesignClasses.scaleImage(this.imagesize, this.imagesize, img);
//                friend_image_panel.setIcon(new ImageIcon(img));
//            } catch (Exception ex) {
//                //System.out.println("img ex-->" + ex.getMessage());
//            }
            JPanel imagePanle = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 7));
            imagePanle.setOpaque(false);
            imagePanle.add(friend_image_panel);
            status_name_Panel.add(imagePanle, BorderLayout.WEST);
            status_name_Panel.add(commentDetails(), BorderLayout.CENTER);
            add(status_name_Panel, BorderLayout.CENTER);

            JPanel nullpane2 = new JPanel(new BorderLayout());
            nullpane2.setBorder(new EmptyBorder(0, 10, 0, 6));
            nullpane2.setOpaque(false);
            nullpane2.setPreferredSize(new Dimension(0, DefaultSettings.HGAP));
            add(nullpane2, BorderLayout.SOUTH);
            JLabel nullLabel = new JLabel();
            nullLabel.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.COMMENTS_BORDER_COLOR));//new Color(0xe1e2e3)));
            nullpane2.add(nullLabel);
        } catch (Exception e) {
            log.error("Single image comment==>" + e.getMessage());
        }

    }

    public void changeComment() {
        textPanel.removeAll();
        JsonFields comment = addImageCaptionCommentsPanel.getImageComments().get(commentDto.getCmnId());
        String text = comment.getComment();
        //FeedUtils.changeComment(text, BasicImageViewStructure.commentWidth - 142, textPanel);
        textPanelWithEmoticon = new TextPanelWithEmoticon(BasicImageViewStructure.commentWidth - 142, text);
        textPanel.add(textPanelWithEmoticon);
        textPanel.revalidate();
        textPanel.repaint();
        likeCommentsAndTimePanel.setVisible(true);
        try {
            editDropDown(comment);
            if (comment.getTm() != null) {
                timeLabel.setText(FeedUtils.getShowableDate(comment.getTm()));
                timeLabel.setToolTipText(FeedUtils.getActualDate(comment.getTm()));
            }
        } catch (Exception e) {
            //  e.printStackTrace();
            log.error("Error in changeComment ==>" + e.getMessage());
        }

    }

    private void editDropDown(final JsonFields commentDto) {
        if ((addImageCaptionCommentsPanel.imageDTo != null && addImageCaptionCommentsPanel.imageDTo.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID))
                || commentDto.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)
                || commentDto.getUserIdentity() == null) {
            editRemovePanel.removeAll();
            final JButton editButton = DesignClasses.createImageButton(GetImages.IMAGE_BOTTOM_ARROW, GetImages.IMAGE_BOTTOM_ARROW_H, "Edit or Delete comment");
            editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    BasicImageViewStructure.clieckedEditDeletepopup = true;
                    BasicImageViewStructure.editDeleteImageCommentPopup = null;
                    BasicImageViewStructure.editDeleteImageCommentPopup = new EditDeleteImageCommentPopup(getSingeImageCommentDetails());
                    // It 
                    if (!BasicImageViewStructure.editDeleteImageCommentPopup.isOnlyDelete) {
                        BasicImageViewStructure.editDeleteImageCommentPopup.optionsPopup_edit_delete.setVisible(editButton, 121, -4);
                    }

                    if (BasicImageViewStructure.editDeleteImageCommentPopup.isOnlyDelete) {
                        BasicImageViewStructure.editDeleteImageCommentPopup.optionsPopup_only_delete.setVisible(editButton, 121, -4);
                    }

                }
            });
            editRemovePanel.add(editButton, BorderLayout.EAST);
            editRemovePanel.revalidate();
        }

    }

    public void editComment() {
        String text = this.commentDto.getComment();

        if (text != null && text.length() > 0) {

            try {
                textPanel.removeAll();
                EditComment editComment = new EditComment(getSingeImageCommentDetails(), text);
                textPanel.add(editComment, BorderLayout.CENTER);
                textPanel.revalidate();

                editRemovePanel.removeAll();
                editRemovePanel.revalidate();
            } catch (Exception e) {
            }

        }
    }

    private JPanel editRemovePanel = new JPanel(new BorderLayout(2, 0));

    private JPanel commentDetails() {
        JPanel rowPanel = new JPanel();
        try {
            rowPanel.setOpaque(false);
            rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));

            JPanel fullName_whatPanel = new JPanel(new BorderLayout(2, 0));
            fullName_whatPanel.setOpaque(false);
//            fullName_whatPanel.setBorder(new EmptyBorder(4, 3, 3, 3));
            fullName_whatPanel.setBorder(new EmptyBorder(4, 3, 0, 3));
            textPanel = new JPanel(new BorderLayout());
            textPanel.setBorder(new EmptyBorder(0, 2, 0, 0));
            textPanel.setBackground(Color.RED);
            textPanel.setOpaque(false);
            timeLabel = DesignClasses.makeJLabel_normal("", 12, DefaultSettings.text_color2);
            String flN = this.basicinfo.getFullName() /*+ " " + this.basicinfo.getLastName()*/;
            JLabel fullName = DesignClasses.makeJLabelFullName2(flN, DefaultSettings.FULL_NAME_FONT_SIZE_IN_FEED);
            fullName.setCursor(new Cursor(Cursor.HAND_CURSOR));
            fullName.addMouseListener(new MouseAdapter() {
                Font original;

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (basicinfo.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
                        e.getComponent().setFont(original);
                        GuiRingID.getInstance().action_of_myProfile_button();
                    } else if (FriendList.getInstance().getFriend_hash_map().get(basicinfo.getUserIdentity()) != null) {
                        e.getComponent().setFont(original);
                    } else {
                        e.getComponent().setFont(original);
                        GuiRingID.getInstance().showUnknownProfile(basicinfo);
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
            });
            JPanel fullNameAndRemoveCommentPanel = new JPanel(new BorderLayout(2, 0));
            fullNameAndRemoveCommentPanel.setBorder(new EmptyBorder(0, 3, 0, 0));
            fullNameAndRemoveCommentPanel.setBackground(Color.GREEN);
            fullNameAndRemoveCommentPanel.setOpaque(false);
            fullNameAndRemoveCommentPanel.add(fullName, BorderLayout.WEST);
            editRemovePanel.setOpaque(false);
            /// if (statusOwnerid.equals(MyFnFSettings.LOGIN_USER_ID) || cmntDto.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
            //   changeButtons();
            fullNameAndRemoveCommentPanel.add(editRemovePanel, BorderLayout.EAST);
            fullName_whatPanel.add(fullNameAndRemoveCommentPanel, BorderLayout.NORTH);
            fullName_whatPanel.add(textPanel, BorderLayout.CENTER);

            likeCommentsAndTimePanel = new JPanel();
            likeCommentsAndTimePanel.setBackground(Color.GREEN);
            likeCommentsAndTimePanel.setOpaque(false);
            likeCommentsAndTimePanel.setLayout(new BorderLayout(0, 0));

            JPanel commentLikePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));//new JPanel(new GridLayout(1, 0, 0, 0));
            likeCommentsAndTimePanel.add(commentLikePanel, BorderLayout.LINE_START);
            commentLikePanel.setOpaque(false);
            number_of_likes_on_this_comment.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI));
            like_this_comment_label.setBorder(new EmptyBorder(0, 0, 0, 5));
            commentLikePanel.add(like_this_comment_label);
            if (this.commentDto.getIl() != null && this.commentDto.getIl() > 0) {
                like_this_comment_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
                number_of_likes_on_this_comment.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI_H));
                number_of_likes_on_this_comment.setForeground(RingColorCode.FEED_LIKED_COLOR);
                number_of_likes_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
            } else {
                like_this_comment_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
                number_of_likes_on_this_comment.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI));
                number_of_likes_on_this_comment.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
                number_of_likes_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
            }
            like_this_comment_label.addMouseListener(likeCommentlistener);
            commentLikePanel.add(number_of_likes_on_this_comment);
            number_of_likes_on_this_comment.addMouseListener(likeCommentlistener);
            commentLikePanel.add(number_of_likes_label);
            number_of_likes_label.addMouseListener(likeCommentlistener);

            likeCommentsAndTimePanel.add(timeLabel, BorderLayout.LINE_END);
            fullName_whatPanel.add(likeCommentsAndTimePanel, BorderLayout.SOUTH);
            rowPanel.add(fullName_whatPanel);
            changeComment();
            change_like_number();

        } catch (Exception ex) {
            // System.err.println("single image comment commentDetails --> " + ex.getMessage());
            log.error("single image comment commentDetails --> " + ex.getMessage());
        }

        return rowPanel;
    }

//    class AnimatedLike implements ActionListener {
//
//        private Color startColor = new Color(0xff8a00);   // where we start
//        private Color endColor = new Color(0xE5E6E9);         // where we end
//        private Color currentColor = startColor;
//        private int animationDuration = 1000;   // each animation will take 2 seconds
//        private long animStartTime;         // start time for each animation
//        //private JPanel panel;
//        private JLabel lbl;
//
//        private Timer timer;
//        public AnimatedLike(JLabel lbl) {
//            // this.panel = panel;
//            this.lbl = lbl;
//            timer = new Timer(10, this);
//            timer.setInitialDelay(0);
//            animStartTime = 0 + System.nanoTime() / 1000000;
//            timer.start();
//        }
//
//        public void actionPerformed(ActionEvent ae) {
//            // calculate elapsed fraction of animation
//            long currentTime = System.nanoTime() / 1000000;
//            long totalTime = currentTime - animStartTime;
//            if (totalTime > animationDuration) {
//                animStartTime = currentTime;
//            }
//            float fraction = (float) totalTime / animationDuration;
//            fraction = Math.min(1.0f, fraction);
//            // interpolate between start and end colors with current fraction
//            // set our new color appropriately
//            lbl.setIcon(DesignClasses.return_image(GetImages.LIKE_ANIM));
//            if (fraction >= 1) {
//                //panel.setBorder(originalBorder);
//                // lbl.setIcon(null);
//                change_like_number();
//                //lbl.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI_H));
//                timer.stop();
//            }
//
//        }
//
//    }
    private MouseListener likeCommentlistener = new MouseAdapter() {
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
            try {
                if (arg0.getComponent() == like_this_comment_label) {
                    new Thread(new Runnable() {  //
                        @Override
                        public void run() {
                            if (like_this_comment_label.getForeground().equals(RingColorCode.FEED_UNLIKE_COLOR)) {
//                                new AnimatedLike(number_of_likes_on_this_comment);
                                BasicImageViewStructure.animatedLike = null;
                                BasicImageViewStructure.animatedLike = new AnimatedLike(number_of_likes_on_this_comment);
                            } //
                            if (MyfnfHashMaps.getInstance().getProcessingLikes().get(commentDto.getCmnId()) == null) {
                                MyfnfHashMaps.getInstance().getProcessingLikes().put(commentDto.getImageId().toString(), System.currentTimeMillis());
                                if (like_this_comment_label.getForeground().equals(RingColorCode.FEED_UNLIKE_COLOR)) {
                                    new LikeOrUnlikeRequestOnImageComment(1).start();
                                } else {
                                    new LikeOrUnlikeRequestOnImageComment(0).start();
                                }
                            } else {
                                long time = MyfnfHashMaps.getInstance().getProcessingLikes().get(commentDto.getCmnId());
                                long diff = System.currentTimeMillis() - time;
                                if (diff > 60000) {
                                    MyfnfHashMaps.getInstance().getProcessingLikes().remove(commentDto.getCmnId());
                                }
                            }
                        } //
                    }).start(); //
                } else if (arg0.getComponent() == number_of_likes_on_this_comment || arg0.getComponent() == number_of_likes_label) {
                    if (getImageCommentsLikes().isEmpty()) {
                        new ListOfLikesCommentInImage(commentDto.getImageId(), commentDto.getCmnId(), AppConstants.TYPE_IMAGE_COMMENT_LIKES, 0).start();
                    } else {
                        Map<String, SingleMemberInCircleDto> likes = getImageCommentsLikes();
                        if (likes == null || commentDto.getTl() == null || likes.size() < commentDto.getTl()) {
                            new ListOfLikesCommentInImage(commentDto.getImageId(), commentDto.getCmnId(), AppConstants.TYPE_IMAGE_COMMENT_LIKES, 0).start();
                        }
                    }
                    if (commentDto.getTl() != null && commentDto.getTl() > 0) {
                        int yPos = (int) number_of_likes_on_this_comment.getLocationOnScreen().getY();
                        int type_int = 1;
                        if (yPos > DefaultSettings.MONITOR_HALF_HEIGHT) {
                            type_int = 0;
                        }
                        BasicImageViewStructure.likesInImageCommentsPopup = null;
                        BasicImageViewStructure.likesInImageCommentsPopup = new LikesInImageCommentsPopup(number_of_likes_on_this_comment, getSingeImageCommentDetails());
                        BasicImageViewStructure.likesInImageCommentsPopup.setVisible(number_of_likes_on_this_comment, type_int);
                    }
                    //BasicImageViewStructure.likesInImageCommentsPopup.addLoading();
//                    LikesInCommentsJdialog likes = new LikesInCommentsJdialog(number_of_likes_on_this_comment, commentDto.getComment(), singleImageCommentid, commentDto.getImageId().toString(), commentDto.getCmnId(), true);
//                    likes.initContainers();
//                    likes.addloding();
//                    likes.setVisible(true);
                    number_of_likes_on_this_comment.setFont(original);
                }
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Error in here ==>" + e.getMessage());

            }

        }
    };

    public void change_like_number() {
        long tl = this.commentDto.getTl() != null ? this.commentDto.getTl() : 0;
        short il = this.commentDto.getIl() != null ? this.commentDto.getIl() : 0;
        changeLikeNumbers(tl);
        changeLikeTextColor(il);
    }

    private void changeLikeNumbers(long value) {
        if (value > 0) {
            number_of_likes_label.setText(String.valueOf(value));
        } else {
            number_of_likes_label.setText("");
//            number_of_likes_on_this_comment.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI));
        }
    }

    public void changeLikeTextColor(short like) {
        if (like > 0) {
            like_this_comment_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
            number_of_likes_on_this_comment.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI_H));
            number_of_likes_on_this_comment.setForeground(RingColorCode.FEED_LIKED_COLOR);
            number_of_likes_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
        } else {
            like_this_comment_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
            number_of_likes_on_this_comment.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI));
            number_of_likes_on_this_comment.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
            number_of_likes_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
        }
    }

    private SingeImageCommentDetails getSingeImageCommentDetails() {
        return this;
    }

    class LikeOrUnlikeRequestOnImageComment extends Thread {

        private int lkd;

        public LikeOrUnlikeRequestOnImageComment(int lkd) {
            this.lkd = lkd;
        }

        @Override
        public void run() {
            try {
                JsonFields pakToSend = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                pakToSend.setPacketId(pakId);
                pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                pakToSend.setAction(AppConstants.TYPE_LIKE_UNLIKE_IMAGE_COMMENT);
                pakToSend.setCmnId(commentDto.getCmnId());
                pakToSend.setImageId(addImageCaptionCommentsPanel.imageDTo.getImageId());
                pakToSend.setLkd(lkd);

                SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(1000);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        FeedBackFields response = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (response.getSuccess()) {
                            try {
                                if (lkd == 1) {
                                    SingleMemberInCircleDto addMe = new SingleMemberInCircleDto();
                                    addMe.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
                                    addMe.setFullName(MyFnFSettings.userProfile.getFullName());
                                    //addMe.setLastName(MyFnFSettings.userProfile.getLastName());
                                    getImageCommentsLikes().put(MyFnFSettings.LOGIN_USER_ID, addMe);
                                    Long tl = (commentDto.getTl() == null) ? 0 : commentDto.getTl();
                                    Long like_num = tl + 1;
                                    commentDto.setTl(like_num);
                                    commentDto.setIl((short) 1);
                                    change_like_number();
//                                       
                                } else {
                                    getImageCommentsLikes().remove(MyFnFSettings.LOGIN_USER_ID);
                                    Long tl = (commentDto.getTl() == null) ? 0 : commentDto.getTl();
                                    Long like_num = tl - 1;

                                    commentDto.setTl(like_num);
                                    commentDto.setIl((short) 0);
                                    change_like_number();
                                }

                            } catch (Exception e) {
                                MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                                MyfnfHashMaps.getInstance().getProcessingLikes().remove(commentDto.getImageId().toString());
                                return;
                            }
                        }

                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        MyfnfHashMaps.getInstance().getProcessingLikes().remove(commentDto.getImageId().toString());
                        return;
                    }

                }
                MyfnfHashMaps.getInstance().getProcessingLikes().remove(commentDto.getImageId().toString());
            } catch (Exception e) {
            }

        }
    }

}
