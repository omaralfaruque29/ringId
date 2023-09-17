/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.ListOfLikesCommentInImage;
import static com.ipvision.view.feed.SingleFeedStructure.instance;
import com.ipvision.service.feed.FeedLikeUnlikeCommentRequest;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.SingleMemberInCircleDto;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.view.utility.FeedUtils;
import com.ipvision.view.utility.TextPanelWithEmoticon;
import com.ipvision.view.utility.ImageHelpers;

/**
 *
 * @author Faiz
 */
public class SingleCommentView extends JPanel implements MouseListener {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SingleCommentView.class);
    //  private SingleCommentView instance;
    public JsonFields commentDto;
    private UserBasicInfo basicinfo = new UserBasicInfo();
    private String dateformate = "hh:mm aaa dd MMM yyyy";
    public String singleCommentid;
    private int imagesize = 35;

    public JLabel like_this_comment_label;
    public JLabel number_of_likes_on_this_comment, number_of_likes_label;
    public String statusOwnerId;
    private JLabel timeLabel;
    public int type;
    public JPanel textPanel;
    public SingleFeedStructure singleFeedStructure;
    private EditComment editComment;
    AnimatedLike animatedLike;
    public JPanel likeCommentsAndTimePanel;
    public static SingleCommentView instance;

    public static SingleCommentView getInatance() {
        return instance;
    }

    public SingleCommentView(JsonFields commentDto, String statusOwnerId, SingleFeedStructure singleFeedStructure) {
        this.instance = this;
        setOpaque(false);
        this.singleFeedStructure = singleFeedStructure;
        this.commentDto = commentDto;
        this.setLayout(new BorderLayout(0, 0));
        this.statusOwnerId = statusOwnerId;
        this.singleCommentid = HelperMethods.makeStatusIDCommentIDKey(this.commentDto.getNfId(), this.commentDto.getCmnId());
        like_this_comment_label = DesignClasses.likesCommentsText(StaticFields.likeText, 0, 12);
        like_this_comment_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        number_of_likes_on_this_comment = DesignClasses.makeAncorLabel("", 0, 12);
        number_of_likes_on_this_comment.setCursor(new Cursor(Cursor.HAND_CURSOR));
        number_of_likes_label = DesignClasses.makeAncorLabel("", 0, 12);
        number_of_likes_on_this_comment.setPreferredSize(new Dimension(20, 20));
        number_of_likes_label.setBorder(new EmptyBorder(2, 0, 0, 30));
        number_of_likes_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                initContainers();
                addSingleCommentView();
            }
        });
    }

    private void addSingleCommentView() {
        singleFeedStructure.getCommentsOfthisPost().put(singleCommentid, this);
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
                        //this.basicinfo.setLastName(this.commentDto.getLastName());
                    }
                } else {
                    basicinfo.setUserIdentity(this.commentDto.getUserIdentity());
                    this.basicinfo.setFullName(this.commentDto.getFullName());
                    //this.basicinfo.setLastName(this.commentDto.getLastName());
                }

            }
            JPanel status_name_Panel = new JPanel(new BorderLayout(0, 0));
            status_name_Panel.setBorder(new EmptyBorder(0, 0, 0, 3));
            //status_name_Panel.setBorder(new EmptyBorder(0, 3, 0, 3));
            ///status_name_Panel.setBackground(DefaultSettings.COMMENTS_BACK_GROUND_COLOR);
            status_name_Panel.setOpaque(false);

            JPanel profileImageName = new JPanel(new BorderLayout(0, 5));
            profileImageName.setOpaque(false);
            JLabel friend_image_panel = new JLabel();
            friend_image_panel.setPreferredSize(new Dimension(this.imagesize, this.imagesize));
            if (this.basicinfo.getProfileImage() != null && this.basicinfo.getProfileImage().trim().length() > 0) {
                ImageHelpers.addProfileImageThumb(friend_image_panel, this.basicinfo.getProfileImage());
            } else {
                BufferedImage bi = ImageHelpers.getUnknownImage(true);
                friend_image_panel.setIcon(new ImageIcon(bi));
                bi.flush();
                bi = null;
            }
//            JPanel imagePanle = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 10));
            JPanel imagePanle = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 6));
            imagePanle.setOpaque(false);
            imagePanle.add(friend_image_panel);
            //imagePanle.add(friend_image_panel, BorderLayout.WEST);
            status_name_Panel.add(imagePanle, BorderLayout.WEST);
            status_name_Panel.add(friendDetails(), BorderLayout.CENTER);
            add(status_name_Panel, BorderLayout.CENTER);

            JPanel nullpane2 = new JPanel(new BorderLayout());
            nullpane2.setBorder(new EmptyBorder(0, 10, 0, 6));
            ///nullpane2.setBackground(DefaultSettings.COMMENTS_BACK_GROUND_COLOR);
            nullpane2.setOpaque(false);
            nullpane2.setPreferredSize(new Dimension(200, DefaultSettings.HGAP));
            add(nullpane2, BorderLayout.SOUTH);
            JLabel nullLabel = new JLabel();
            nullLabel.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.COMMENTS_BORDER_COLOR));//new Color(0xe1e2e3)));
            nullpane2.add(nullLabel);
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Single comment==>" + e.getMessage());
        }

    }

    public void changeComment() {
        textPanel.removeAll();
        //JsonFields comment = NewsFeedMaps.getInstance().getComments().get(commentDto.getNfId()).get(commentDto.getCmnId());
        String text = this.commentDto.getComment();
        if (text != null && text.length() > 0) {
            TextPanelWithEmoticon texte = new TextPanelWithEmoticon(380, text);
            textPanel.add(texte);
        }
        textPanel.revalidate();
        textPanel.repaint();
        likeCommentsAndTimePanel.setVisible(true);
        editRemovePanel.setVisible(true);
        try {
            editDropDown();
            if (commentDto.getTm() != null) {
                timeLabel.setText(FeedUtils.getShowableDate(commentDto.getTm()));
                timeLabel.setToolTipText(FeedUtils.getActualDate(commentDto.getTm()));
            }
        } catch (Exception e) {
            // e.printStackTrace();

            log.error("Error in here ==>" + e.getMessage());
        }

    }

    private void editDropDown() {
        if (statusOwnerId.equals(MyFnFSettings.LOGIN_USER_ID)
                || this.commentDto.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)
                || this.commentDto.getUserIdentity() == null) {
            editRemovePanel.removeAll();
            final JButton editButton = DesignClasses.createImageButton(GetImages.IMAGE_BOTTOM_ARROW, GetImages.IMAGE_BOTTOM_ARROW_H, "Edit or Delete comment");
            editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FeedActionPopup popup = new FeedActionPopup(getSingleCommentView());
                    if (popup.isOnlyDelete == false) {
                        popup.optionsPopup_edit_delete.setVisible(editButton, 120, -8);
                    } else if (popup.isOnlyDelete == true) {
                        popup.optionsPopup_only_delete.setVisible(editButton, 120, -8);
                    }

                    //editButton.addMouseListener(getSingleCommentView());
//                    popup.show(editButton, (editButton.getHorizontalAlignment() - 43), (editButton.getVerticalAlignment() + 12));
                }
            });
            editRemovePanel.add(editButton, BorderLayout.EAST);
            editRemovePanel.revalidate();
        }
    }

    private SingleCommentView getSingleCommentView() {
        return this;
    }

    public void editComment() {
        textPanel.removeAll();
        String text = this.commentDto.getComment();
        if (text != null && text.length() > 0) {
            EditComment editComment = new EditComment(this, text);
            textPanel.add(editComment);
        }
        textPanel.revalidate();
        textPanel.repaint();
    }

//    private final JPanel editRemovePanel = new JPanel(new BorderLayout(2, 0));
    public final JPanel editRemovePanel = new JPanel(new BorderLayout(2, 0));
    JLabel fullName;

    private JPanel friendDetails() {
        JPanel rowPanel = new JPanel();
        try {

            rowPanel.setOpaque(false);
            //   rowPanel.setBackground(Color.WHITE);
            rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
            JPanel fullName_whatPanel = new JPanel(new BorderLayout(2, 0));
            fullName_whatPanel.setOpaque(false);
            //fullName_whatPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
            fullName_whatPanel.setBorder(new EmptyBorder(3, 3, 0, 3));
            textPanel = new JPanel(new BorderLayout(0, 0));
            textPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
            textPanel.setOpaque(false);
            timeLabel = DesignClasses.makeJLabel_normal("", 12, DefaultSettings.text_color2);
            //String flN = this.basicinfo.getFullName() + " " + this.basicinfo.getLastName();
            String flN = this.basicinfo.getFullName();
            /*if (this.basicinfo.getLastName() != null && this.basicinfo.getLastName().length() > 0) {
             flN = this.basicinfo.getFullName() + " " + this.basicinfo.getLastName();
             }*/
            fullName = DesignClasses.makeJLabelFullName2(flN, DefaultSettings.FULL_NAME_FONT_SIZE_IN_FEED);
            fullName.setCursor(new Cursor(Cursor.HAND_CURSOR));
            fullName.addMouseListener(this);
//            fullName.addMouseListener(new MouseAdapter() {
//                Font original;
//
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    if (basicinfo.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
//                        e.getComponent().setFont(original);
//                        GuiRingID.getInstance().action_of_myProfile_button();
//                    } else if (FriendList.getInstance().getFriend_hash_map().get(basicinfo.getUserIdentity()) != null) {
//                        e.getComponent().setFont(original);
//                        GuiRingID.getInstance().showFriendProfile(basicinfo.getUserIdentity());
//                    } else {
//                        e.getComponent().setFont(original);
//                        GuiRingID.getInstance().showUnknownProfile(basicinfo);
//                    }
//                }
//
//                @Override
//                public void mouseEntered(MouseEvent e) {
//                    original = e.getComponent().getFont();
//                    Map attributes = original.getAttributes();
//                    attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
//                    e.getComponent().setFont(original.deriveFont(attributes));
//                }
//
//                @Override
//                public void mouseExited(MouseEvent e) {
//                    e.getComponent().setFont(original);
//                }
//            });
            JPanel fullNameAndRemoveCommentPanel = new JPanel(new BorderLayout(0, 0));
            fullNameAndRemoveCommentPanel.setBorder(new EmptyBorder(0, 3, 0, 0));
            fullNameAndRemoveCommentPanel.setOpaque(false);
            fullNameAndRemoveCommentPanel.add(fullName, BorderLayout.WEST);
            editRemovePanel.setOpaque(false);
            /// if (statusOwnerid.equals(MyFnFSettings.LOGIN_USER_ID) || statusDto.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
            //   changeButtons();

            fullNameAndRemoveCommentPanel.add(editRemovePanel, BorderLayout.EAST);
            fullName_whatPanel.add(fullNameAndRemoveCommentPanel, BorderLayout.NORTH);
            fullName_whatPanel.add(textPanel, BorderLayout.CENTER);

            likeCommentsAndTimePanel = new JPanel();
            likeCommentsAndTimePanel.setBackground(Color.GREEN);
            likeCommentsAndTimePanel.setOpaque(false);
            likeCommentsAndTimePanel.setLayout(new BorderLayout(2, 2));

//            JPanel commentLikePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 5));//new JPanel(new GridLayout(1, 0, 0, 0));
            JPanel commentLikePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
            likeCommentsAndTimePanel.add(commentLikePanel, BorderLayout.LINE_START);
            commentLikePanel.setOpaque(false);
            number_of_likes_on_this_comment.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI));
            like_this_comment_label.setBorder(new EmptyBorder(0, 0, 0, 5));
            commentLikePanel.add(like_this_comment_label);
            if (this.commentDto.getIl() != null && this.commentDto.getIl() > 0) {
                //like_this_comment_label.setText(StaticFields.unLikeText);
                like_this_comment_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
                number_of_likes_on_this_comment.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI_H));
                number_of_likes_on_this_comment.setForeground(RingColorCode.FEED_LIKED_COLOR);
                number_of_likes_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
            } else {
                //like_this_comment_label.setText(StaticFields.likeText);
                like_this_comment_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
                number_of_likes_on_this_comment.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI));
                number_of_likes_on_this_comment.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
                number_of_likes_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
            }
            like_this_comment_label.addMouseListener(this);
            commentLikePanel.add(number_of_likes_on_this_comment);
            number_of_likes_on_this_comment.addMouseListener(this);
            commentLikePanel.add(number_of_likes_label);
            number_of_likes_label.addMouseListener(this);
            //create_what_in_mind_label(what, 75, Gui24FnFNew.getInstance().fnf_left_width - 100);
            likeCommentsAndTimePanel.add(timeLabel, BorderLayout.LINE_END);
            fullName_whatPanel.add(likeCommentsAndTimePanel, BorderLayout.SOUTH);
            rowPanel.add(fullName_whatPanel);
            changeComment();
            change_like_number();
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("*************comment ==> " + ex.getMessage());
            // System.err.println("292 --> " + ex.getMessage());
        }

        return rowPanel;
    }
    Font original;
//    private MouseListener likeCommentlistener = new MouseAdapter() {
//        Font original;
//
//        @Override
//        public void mouseEntered(MouseEvent e) {
//            original = e.getComponent().getFont();
//            Map attributes = original.getAttributes();
//            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
//            e.getComponent().setFont(original.deriveFont(attributes));
//        }
//
//        @Override
//        public void mouseExited(MouseEvent e) {
//            e.getComponent().setFont(original);
//        }
//
//        @Override
//        public void mouseClicked(MouseEvent arg0) {
//            try {
//                if (arg0.getComponent() == like_this_comment_label) {
//                    if (MyfnfHashMaps.getInstance().getProcessingLikes().get(singleCommentid) == null) {
//                        MyfnfHashMaps.getInstance().getProcessingLikes().put(statusDto.getNfId().toString(), System.currentTimeMillis());
//                        if (like_this_comment_label.getForeground().equals(DefaultSettings.UNLIKE_COLOR)) {
//                            new LikeOrUnlikeRequestOnComment(AppConstants.TYPE_LIKE_COMMENT, statusDto.getNfId(), statusDto.getCmnId(), singleCommentid).start();
//                        } else {
//                            new LikeOrUnlikeRequestOnComment(AppConstants.TYPE_UNLIKE_COMMENT, statusDto.getNfId(), statusDto.getCmnId(), singleCommentid).start();
//                        }
//                    } else {
//                        long time = MyfnfHashMaps.getInstance().getProcessingLikes().get(singleCommentid);
//                        long diff = System.currentTimeMillis() - time;
//                        if (diff > 60000) {
//                            MyfnfHashMaps.getInstance().getProcessingLikes().remove(singleCommentid);
//                        }
//                    }
//                } else if (arg0.getComponent() == number_of_likes_on_this_comment) {
//                    if (NewsFeedMaps.getInstance().getLikes() == null) {
//                        new ListOfLikesCommentInImage(statusDto.getNfId(), statusDto.getCmnId(), AppConstants.TYPE_LIST_LIKES_OF_COMMENT, 0).start();
//                    } else {
//                        Map<String, SingleMemberInCircleDto> likes = NewsFeedMaps.getInstance().getLikes().get(singleCommentid);
//                        if (likes == null || statusDto.getTl() == null || likes.size() < statusDto.getTl()) {
//                            new ListOfLikesCommentInImage(statusDto.getNfId(), statusDto.getCmnId(), AppConstants.TYPE_LIST_LIKES_OF_COMMENT, 0).start();
//                        }
//                    }
//                    LikesInCommentsJdialog likes = new LikesInCommentsJdialog(number_of_likes_on_this_comment, statusDto.getComment(), singleCommentid, statusDto.getNfId().toString(), statusDto.getCmnId(), false);
//                    //              likes.initContainers();
//                    //               likes.addloding();
//                    likes.setVisible(true);
//                    likes.addLoading();
//                    number_of_likes_on_this_comment.setFont(original);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//    };

    public void change_like_number() {
        long tl = this.commentDto.getTl() != null ? this.commentDto.getTl() : 0;
        short il = this.commentDto.getIl() != null ? this.commentDto.getIl() : 0;
        changeLikeNumbers(tl);
        changeLikeTextColor(il);
    }

    private void changeLikeNumbers(long value) {
        if (value > 0) {
//            number_of_likes_on_this_comment.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI_H));
            number_of_likes_label.setText(String.valueOf(value));
        } else {
            number_of_likes_label.setText("");
//            number_of_likes_on_this_comment.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI));
        }
    }

    public void changeLikeTextColor(short like) {
        if (like > 0) {
            // like_this_comment_label.setText(StaticFields.unLikeText);
            like_this_comment_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
            number_of_likes_on_this_comment.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI_H));
            number_of_likes_on_this_comment.setForeground(RingColorCode.FEED_LIKED_COLOR);
            number_of_likes_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
        } else {
            //like_this_comment_label.setText(StaticFields.likeText);
            like_this_comment_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
            number_of_likes_on_this_comment.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI));
            number_of_likes_on_this_comment.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
            number_of_likes_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
        }
    }

    /*private String getShowableDate(long milliSeconds) {
     Date date = new Date(milliSeconds);
     DateFormat format = new SimpleDateFormat(this.dateformate);
     String formatted = format.format(date);
     return formatted;
     }*/
    public SingleCommentView getSingleComment() {
        return this;
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
//
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
    @Override
    public void mouseClicked(MouseEvent arg0) {
        arg0.getComponent().setFont(original);
        if (arg0.getComponent() == like_this_comment_label) {
            new Thread(new Runnable() {  //
                @Override
                public void run() {
                    if (like_this_comment_label.getForeground().equals(RingColorCode.FEED_UNLIKE_COLOR)) {
                        //new AnimatedLike(number_of_likes_on_this_comment);
                        animatedLike = new AnimatedLike(number_of_likes_on_this_comment);
                    } //
                    if (MyfnfHashMaps.getInstance().getProcessingLikes().get(singleCommentid) == null) {
                        MyfnfHashMaps.getInstance().getProcessingLikes().put(commentDto.getNfId().toString(), System.currentTimeMillis());
                        if (like_this_comment_label.getForeground().equals(RingColorCode.FEED_UNLIKE_COLOR)) {
                            new FeedLikeUnlikeCommentRequest(AppConstants.TYPE_LIKE_COMMENT, commentDto.getNfId(), commentDto.getCmnId(), singleCommentid).start();
                        } else {
                            new FeedLikeUnlikeCommentRequest(AppConstants.TYPE_UNLIKE_COMMENT, commentDto.getNfId(), commentDto.getCmnId(), singleCommentid).start();
                        }
                    } else {
                        long time = MyfnfHashMaps.getInstance().getProcessingLikes().get(singleCommentid);
                        long diff = System.currentTimeMillis() - time;
                        if (diff > 60000) {
                            MyfnfHashMaps.getInstance().getProcessingLikes().remove(singleCommentid);
                        }
                    }
                } //
            }).start(); //
        } else if (arg0.getComponent() == number_of_likes_on_this_comment || arg0.getComponent() == number_of_likes_label) {
            if (NewsFeedMaps.getInstance().getLikes() == null) {
                new ListOfLikesCommentInImage(commentDto.getNfId(), commentDto.getCmnId(), AppConstants.TYPE_LIST_LIKES_OF_COMMENT, 0).start();
            } else {
                Map<String, SingleMemberInCircleDto> likes = NewsFeedMaps.getInstance().getLikes().get(singleCommentid);
                if (likes == null || commentDto.getTl() == null || likes.size() == 0 /*likes.size() < commentDto.getTl()*/) {
                    new ListOfLikesCommentInImage(commentDto.getNfId(), commentDto.getCmnId(), AppConstants.TYPE_LIST_LIKES_OF_COMMENT, 0).start();
                }
            }
            if (commentDto.getTl() != null && commentDto.getTl() > 0) {
                int yPos = (int) number_of_likes_on_this_comment.getLocationOnScreen().getY();
                int type_int = 1;
                if (yPos > DefaultSettings.MONITOR_HALF_HEIGHT) {
                    type_int = 0;
                }
                LikesInCommentsJdialog likes = new LikesInCommentsJdialog(number_of_likes_on_this_comment, commentDto.getComment(), singleCommentid, commentDto.getNfId(), commentDto.getTl(), commentDto.getCmnId(), false);
//                likes.setVisible(true);
                likes.setVisible(number_of_likes_on_this_comment, type_int);
            }
            //likes.addLoading();
        } else if (arg0.getComponent() == fullName) {
            if (basicinfo.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
                GuiRingID.getInstance().action_of_myProfile_button();
            } else if (FriendList.getInstance().getFriend_hash_map().get(basicinfo.getUserIdentity()) != null) {
                GuiRingID.getInstance().showFriendProfile(basicinfo.getUserIdentity());
            } else {
                GuiRingID.getInstance().showUnknownProfile(basicinfo);
            }

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

}
