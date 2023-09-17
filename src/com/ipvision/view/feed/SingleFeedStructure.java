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
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.constants.RingColorCode;
import com.ipvision.model.constants.StaticFields;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.service.CommentsLikes;
import com.ipvision.service.feed.FeedLikeUnlikeStatusRequest;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.SingleMemberInCircleDto;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.SortedArrayList;
import com.ipvision.view.utility.TextPanelWithEmoticon;
import com.ipvision.service.image.LoadImageInPanel;
import com.ipvision.view.friendlist.SingleFriendPanel;

/**
 *
 * @author Faiz
 */
public class SingleFeedStructure extends JPanel implements MouseListener {

    JPanel contentsHere;
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SingleFeedStructure.class);
    public JLabel like_this_post_label; //= DesignClasses.likesCommentsText(StaticFields.likeText, 0, 11);
    private JLabel comment_in_this_post_label;
    private JLabel share_this_post_label;
    public JLabel number_of_likes_icon_label, number_of_likes_label;
    private JLabel number_of_comments_label;
    private JLabel number_of_shares_label;
    //public JLabel number_of_shares_label;
    private JLabel shareLabel;
    public JPanel comments;
    public JPanel commentsContiner;
    public JPanel shareFeedsContinerWrapper;
    public JPanel shareFeedsContiner;
//    private NewComment newComment;
    public NewComment newComment;
    public NewsFeedWithMultipleImage statusDto;
    public Long parentNfID;
    public Long circleId;
    public String friendIdentity;
    private Map<String, SingleCommentView> commentsOfthisPost = new ConcurrentHashMap<String, SingleCommentView>();
    private Map<Long, SingleStatusPanelInShareFeed> singleStatusPanelInShareFeed = new ConcurrentHashMap<Long, SingleStatusPanelInShareFeed>();
    JLabel whoseActivity;
    public int width = DefaultSettings.BOOK_SINGLE_PANEL_COMMON_WIDTH - 12;
    public JPanel sharedPanel;
    public JPanel seperator;
    LikePopupSingleFeed likePopSingleFeed;
    public int newCommentRows = -1;
    public static SingleFeedStructure instance;
    AnimatedLike animatedLike;

    public static SingleFeedStructure getInatance() {
        return instance;
    }
    public JLabel lblSequenceNumber;

    public JPanel getContentsHere() {
        return contentsHere;
    }

    public Map<String, SingleCommentView> getCommentsOfthisPost() {
        return commentsOfthisPost;
    }

    public void setCommentsOfthisPost(Map<String, SingleCommentView> commentsOfthisPost) {
        this.commentsOfthisPost = commentsOfthisPost;
    }

    public Map<Long, SingleStatusPanelInShareFeed> getSingleShareFeedPanel() {
        return singleStatusPanelInShareFeed;
    }

    public void setSingleShareFeedPanel(Map<Long, SingleStatusPanelInShareFeed> singleStatusPanelInShareFeed) {
        this.singleStatusPanelInShareFeed = singleStatusPanelInShareFeed;
    }

    public SingleFeedStructure() {
        this.instance = this;
        setLayout(new BorderLayout());
        //  setOpaque(false);
        setBackground(RingColorCode.FEED_BG_COLOR);
        contentsHere = new JPanel();
        contentsHere.setLayout(new BorderLayout());
        contentsHere.setOpaque(false);
        //contentsHere.setBackground(DefaultSettings.STATUS_PANEL_BACKGROUND);
        //contentsHere.setBorder(DefaultSettings.DEFAULT_BOOK_BORDER);
        contentsHere.setBorder(DefaultSettings.DEFAULT_FEED_BORDER);
        add(contentsHere, BorderLayout.CENTER);
        seperator = new JPanel();
        seperator.setPreferredSize(new Dimension(DefaultSettings.BOOK_SINGLE_PANEL_COMMON_WIDTH, MyFnFSettings.DEFAULT_VERTICAL_GAP));
        seperator.setBackground(RingColorCode.DEFAULT_BACKGROUND_COLOR);
        add(seperator, BorderLayout.SOUTH);

        //        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
        buildDefaultStructure();
//            }
//        });
    }

    int margin = 5;

    private JLabel getLikeCommentJLabel(String text) {
        return DesignClasses.likesCommentsText(text, 0, 12);
    }

    private JLabel getLikeCommentJLabel(String text, boolean anchor) {
        return DesignClasses.makeAncorLabel(text, 0, 12);
    }

    private JPanel borderLayoutPanels() {
        JPanel borderLayoutPanels = new JPanel(new BorderLayout());
        borderLayoutPanels.setOpaque(false);
        return borderLayoutPanels;
    }

    JPanel activityPanel;
    JPanel friendProfilePanel;
    JPanel statusPanel;
    JPanel imagePanel;
    JPanel sharedFeedPanel;

    private void buildDefaultStructure() {

        JPanel topMain = borderLayoutPanels();
        topMain.setBorder(new EmptyBorder(margin, margin, margin - 3, margin));
        contentsHere.add(topMain, BorderLayout.CENTER);

        JPanel tmpcontainer1 = borderLayoutPanels();
        JPanel tmpcontainer2 = borderLayoutPanels();
        JPanel lastStatusAndProfilePanel = borderLayoutPanels();

        lblSequenceNumber = new JLabel("");
        lblSequenceNumber.setOpaque(false);
        lblSequenceNumber.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        lblSequenceNumber.setBorder(new EmptyBorder(2, 4, 0, 0));
        lblSequenceNumber.setForeground(RingColorCode.RING_THEME_COLOR);

        activityPanel = borderLayoutPanels();
        friendProfilePanel = borderLayoutPanels();
        statusPanel = borderLayoutPanels();
        imagePanel = borderLayoutPanels();
        sharedFeedPanel = borderLayoutPanels();
        topMain.add(tmpcontainer1, BorderLayout.NORTH);
        topMain.add(tmpcontainer2, BorderLayout.CENTER);

        lastStatusAndProfilePanel.add(activityPanel, BorderLayout.NORTH);
        lastStatusAndProfilePanel.add(friendProfilePanel, BorderLayout.CENTER);
        tmpcontainer1.add(lastStatusAndProfilePanel, BorderLayout.NORTH);
        tmpcontainer1.add(statusPanel, BorderLayout.CENTER);
        //tmpcontainer1.add(statusPanel, BorderLayout.CENTER);

        tmpcontainer2.add(imagePanel, BorderLayout.NORTH);
        tmpcontainer2.add(sharedFeedPanel, BorderLayout.CENTER);
        /**
         * bottom start
         */
        JPanel bottomMain = new JPanel(new BorderLayout());
        //bottomMain.setBackground(Color.white);
        bottomMain.setBackground(RingColorCode.COMMENTS_PANEL_COLOR);
        commentLikeBar = new JPanel(new BorderLayout());
        //commentLikeBar.setBorder(new MatteBorder(1, 0, 0, 0, DefaultSettings.COMMENTS_LIKE_BAR_BORDER_COLOR));
        contentsHere.add(bottomMain, BorderLayout.SOUTH);
        bottomMain.add(commentLikeBar, BorderLayout.NORTH);

        JPanel commentsAndNewCommentsNullPanel = new JPanel();
        //commentsAndNewCommentsNullPanel.setBorder(new EmptyBorder(0, 10, 0, 5));
//        commentsAndNewCommentsNullPanel.setBorder(new EmptyBorder(0, 5, 0, 5));
        commentsAndNewCommentsNullPanel.setBorder(new EmptyBorder(0, 0, 0, 5));
        commentsAndNewCommentsNullPanel.setOpaque(false);
        commentsAndNewCommentsNullPanel.setLayout(new BorderLayout());
        bottomMain.add(commentsAndNewCommentsNullPanel, BorderLayout.CENTER);
        comments = new JPanel();
        comments.setOpaque(false);
        comments.setLayout(new BoxLayout(comments, BoxLayout.Y_AXIS));

        commentsContiner = new JPanel(new BorderLayout());
        commentsContiner.setOpaque(false);

        shareFeedsContiner = new JPanel();
        shareFeedsContiner.setOpaque(false);
        shareFeedsContiner.setLayout(new BoxLayout(shareFeedsContiner, BoxLayout.Y_AXIS));

        shareFeedsContinerWrapper = new JPanel(new BorderLayout());
        shareFeedsContinerWrapper.setOpaque(false);
        shareFeedsContinerWrapper.add(shareFeedsContiner, BorderLayout.CENTER);

        commentsAndNewCommentsNullPanel.add(comments, BorderLayout.NORTH);
        commentsAndNewCommentsNullPanel.add(commentsContiner, BorderLayout.CENTER);
        commentsAndNewCommentsNullPanel.add(shareFeedsContinerWrapper, BorderLayout.SOUTH);
        // commentsAndNewCommentsNullPanel.setBackground(DefaultSettings.COMMENTS_BACK_GROUND_COLOR);
    }
    JPanel commentLikeBar;

    public void addOrChangeActivityInfo() {
        if (activityPanel != null) {
            activityPanel.removeAll();

            try {
                if (this.statusDto != null
                        && this.statusDto.getActvt() != null
                        && this.statusDto.getActvt() > 0
                        && this.statusDto.getActvt() != AppConstants.NEWSFEED_SHARED
                        && this.statusDto.getWhoShare() == null) {
                    JPanel topTempPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
                    topTempPanel.setOpaque(false);
                    topTempPanel.setPreferredSize(new Dimension(width, 25));
                    whoseActivity = DesignClasses.makeJLabelFullName2("", DefaultSettings.FULL_NAME_FONT_SIZE_IN_FEED);
                    if (statusDto.getAuId() != null && statusDto.getAuId().equals(MyFnFSettings.LOGIN_USER_TABLE_ID + "")) {
                        whoseActivity.setText(StaticFields.STRING_YOU);
                    } else {
                        whoseActivity.setText(this.statusDto.getAfn());

                        whoseActivity.addMouseListener(this);
                        whoseActivity.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }
                    topTempPanel.add(whoseActivity);

                    short activity = this.statusDto.getActvt();
                    String updatedText = "";
                    if (activity == AppConstants.NEWSFEED_LIKED) {
                        updatedText = " liked this.";
                    } else if (activity == AppConstants.NEWSFEED_COMMENTED) {
                        updatedText = " commented on this.";
                    }
                    if (updatedText.length() > 0) {
                        //JLabel infoLabel2 = DesignClasses.makeJLabel_normal(updatedText, 12, Color.BLACK);
                        JLabel infoLabel2 = DesignClasses.makeJLabel_normal(updatedText, 12, RingColorCode.FEED_UPDATED_STRING_COLOR);
                        topTempPanel.add(infoLabel2);
                    }
                    topTempPanel.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.COMMENTS_BORDER_COLOR));
                    activityPanel.add(topTempPanel, BorderLayout.CENTER);
                }
            } catch (Exception e) {
                log.error("addOrChangeActivityInfo == " + e.getMessage());
            }
            activityPanel.revalidate();
        }
    }

    public void addFriendInfo() {
        FreindDetailsForFeeds freindDetailsForFeeds = new FreindDetailsForFeeds(this, false);
        freindDetailsForFeeds.addDefaultSettings();
        friendProfilePanel.add(freindDetailsForFeeds);
        friendProfilePanel.revalidate();
    }
    int lineNumber = 0;

    public void addStatusAndLocationInfor() {
        if (statusPanel != null) {
            statusPanel.removeAll();

            if (statusDto != null && statusDto.getStatus() != null && statusDto.getStatus().trim().length() > 0) {
                if (statusDto.getShowContinue() != null) {
                    statusPanel.add(new TextPanelWithEmoticon(width - 175, statusDto.getStatus(), statusDto.getNfId(), statusDto.getShowContinue()), BorderLayout.CENTER);
                } else {
                    statusPanel.add(new TextPanelWithEmoticon(width - 175, statusDto.getStatus(), statusDto.getNfId(), false), BorderLayout.CENTER);
                }
            }
            if (statusDto != null && statusDto.getLocation() != null && statusDto.getLocation().length() > 0) {
                JLabel location = new JLabel("-At " + statusDto.getLocation());
                location.setForeground(RingColorCode.FEED_LIKED_COLOR);
                statusPanel.add(location, BorderLayout.SOUTH);
            }
            statusPanel.revalidate();
        }
        // statusWrapper.revalidate();
    }

    public void editStatusAndLocationInfor() {
        if (statusPanel != null) {
            statusPanel.removeAll();
            FeedEditStatus feedEditStatus = null;
            if (this.statusDto.getStatus() != null && this.statusDto.getStatus().length() > 0) {
                feedEditStatus = new FeedEditStatus(this, this.statusDto.getStatus(), true);
            } else {
                feedEditStatus = new FeedEditStatus(this, "", true);
            }
            if (this.statusDto.getCptn() != null && this.statusDto.getCptn().length() > 0) {
                feedEditStatus = new FeedEditStatus(this, this.statusDto.getCptn(), false);
            }
            statusPanel.add(feedEditStatus, BorderLayout.CENTER);
            feedEditStatus.setVisible(true);
            feedEditStatus.grabTextBoxFocus();
            if (statusDto.getLocation() != null && statusDto.getLocation().length() > 0) {
                JLabel location = new JLabel("-At " + statusDto.getLocation());
                location.setForeground(RingColorCode.FEED_LIKED_COLOR);
                statusPanel.add(location, BorderLayout.SOUTH);
            }
            statusPanel.revalidate();
        }
        //   statusPanel.repaint();
    }

    public void addImagePanelDetails() {
        if (statusDto.getImageList() != null && statusDto.getImageList().size() > 0) {
            final JPanel imageTempContainer = new JPanel();
            imageTempContainer.setLayout(new BorderLayout(3, 3));
            imageTempContainer.setBorder(new EmptyBorder(0, 5, 0, 0));
            imageTempContainer.setOpaque(false);
            imagePanel.add(imageTempContainer, BorderLayout.NORTH);
            (new LoadImageInPanel(statusDto, imageTempContainer, statusDto.getImageList())).start();

        } else if (statusDto.getIurl() != null) {
        }
    }

    public void addFriendDetailsifShared() {
        FreindDetailsForFeeds freindDetailsForFeeds = new FreindDetailsForFeeds(this, true);
        freindDetailsForFeeds.addDefaultSettings();
        JPanel status_Panel_wrapper = new JPanel(new BorderLayout());
        // status_Panel_wrapper.setBorder(new MatteBorder(0, 2, 0, 0, DefaultSettings.DEFAUTL_BORDER_COLOR));
        status_Panel_wrapper.setBorder(new MatteBorder(0, 2, 0, 0, RingColorCode.FEED_BORDER_COLOR));
        status_Panel_wrapper.setOpaque(false);
        sharedFeedPanel.add(status_Panel_wrapper, BorderLayout.CENTER);

        JPanel status_Panel = new JPanel(new BorderLayout());
        status_Panel.setOpaque(false);
        status_Panel.setBorder(new EmptyBorder(3, 7, 3, 3));
        status_Panel_wrapper.add(status_Panel, BorderLayout.CENTER);
        status_Panel.add(freindDetailsForFeeds, BorderLayout.NORTH);
        if (this.statusDto.getStatus() != null && this.statusDto.getStatus().length() > 0) {
            status_Panel.add(new TextPanelWithEmoticon(width - 195, this.statusDto.getStatus()));
        }

        sharedFeedPanel.revalidate();
    }

    public void addLikeCommentsTexts() {
        if (commentLikeBar != null) {
            commentLikeBar.setBackground(RingColorCode.COMMENTS_LIKE_BAR_COLOR);
            commentLikeBar.setPreferredSize(new Dimension(width, 26));
            /*labels definations*/
            like_this_post_label = getLikeCommentJLabel(StaticFields.likeText);
            comment_in_this_post_label = getLikeCommentJLabel("Comments");
            share_this_post_label = getLikeCommentJLabel("Share");
            number_of_likes_icon_label = getLikeCommentJLabel("", true);
            number_of_likes_label = getLikeCommentJLabel("", true);
            number_of_comments_label = getLikeCommentJLabel("", true);
            number_of_shares_label = getLikeCommentJLabel("", true);
            shareLabel = getLikeCommentJLabel("");
            /**/
            number_of_likes_icon_label.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI));
            number_of_comments_label.setIcon(DesignClasses.return_image(GetImages.COMMENT_MINI));
            //number_of_shares_label.setIcon(DesignClasses.return_image(GetImages.SHARE_MINI));

            JPanel likeComments = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            likeComments.setOpaque(false);
            // likeComments.setBackground(Color.CYAN);
            commentLikeBar.add(likeComments, BorderLayout.WEST);
            likeComments.add(like_this_post_label);
            like_this_post_label.setHorizontalAlignment(SwingConstants.RIGHT);

            like_this_post_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);

            like_this_post_label.addMouseListener(this);
            like_this_post_label.setCursor(new Cursor(Cursor.HAND_CURSOR));

            likeComments.add(number_of_likes_icon_label);
            // number_of_likes_label.setBounds(80, 5, 90, 15);
            number_of_likes_icon_label.addMouseListener(this);
            number_of_likes_icon_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            number_of_likes_icon_label.setPreferredSize(new Dimension(20, 20));
            //    number_of_likes_icon_label.setBorder(new EmptyBorder(0, 0, 0, 15));
            number_of_likes_label.setBorder(new EmptyBorder(2, 0, 0, 30));
            likeComments.add(number_of_likes_label);
            // number_of_likes_label.setBounds(80, 5, 90, 15);
            number_of_likes_label.addMouseListener(this);
            number_of_likes_label.setCursor(new Cursor(Cursor.HAND_CURSOR));

            likeComments.add(comment_in_this_post_label);
            comment_in_this_post_label.addMouseListener(this);
            comment_in_this_post_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            comment_in_this_post_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);

            likeComments.add(number_of_comments_label);
            number_of_comments_label.addMouseListener(this);
            number_of_comments_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            // number_of_comments_label.setBounds(310, 5, 90, 15);

            sharedPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            sharedPanel.setBorder(new EmptyBorder(0, 0, 0, 5));

            if (statusDto != null && statusDto.getCircleId() == null || (statusDto.getCircleId() != null && statusDto.getCircleId() == 0)) {
                commentLikeBar.add(sharedPanel, BorderLayout.EAST);
                sharedPanel.setOpaque(false);
                sharedPanel.add(share_this_post_label);
                // share_this_post_label.setBounds(430, 5, 35, 15);
                share_this_post_label.setToolTipText("Share This Post");
                share_this_post_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
                share_this_post_label.addMouseListener(this);

                sharedPanel.add(shareLabel);
                shareLabel.addMouseListener(this);
                shareLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                shareLabel.setToolTipText("Share This Post ");
                //  shareLabel.setBounds(465, 5, 90, 15);
                shareLabel.setIcon(DesignClasses.return_image(GetImages.SHARE_MINI));

                sharedPanel.add(number_of_shares_label);
                number_of_shares_label.setBounds(width - 140, 5, 60, 15);
                number_of_shares_label.setToolTipText("Show All Shared Post");
                number_of_shares_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
                number_of_shares_label.addMouseListener(this);
            }
            commentLikeBar.revalidate();
            viewPreviousCommentsTxt = DesignClasses.likesCommentsText("Previous Comments", 0, 12);
            viewPreviousCommentsTxt.setIcon(DesignClasses.return_image(GetImages.UP_ARROW));
        }
    }
    /*comment like change*/

    public void change_comments_number() {
        long nc = this.statusDto.getNc() != null ? this.statusDto.getNc() : 0;
        short ic = this.statusDto.getIc() != null ? this.statusDto.getIc() : 0;
        setCommentsNumbers(nc);
        changeCommentTextColor(ic);
    }

    public void change_shares_number() {
        long ns = this.statusDto.getTs() != null ? this.statusDto.getTs() : 0;
        short is = this.statusDto.getIShare() != null ? this.statusDto.getIShare() : 0;
        setSharesNumbers(ns);
        changeShareTextColor(is);
    }

    public void change_like_number() {
        long nl = this.statusDto.getNl() != null ? this.statusDto.getNl() : 0;
        short il = this.statusDto.getIl() != null ? this.statusDto.getIl() : 0;
        changeLikeNumbers(nl);
        changeLikeTextColor(il);
    }

    private void changeLikeNumbers(long value) {
        if (value > 0) {
            //number_of_likes_label.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI_H));
            number_of_likes_label.setText(String.valueOf(value));
        } else {
            number_of_likes_label.setText("");
            //number_of_likes_label.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI));
        }
    }

    public void changeLikeTextColor(int like) {
        if (like > 0) {
            like_this_post_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
            number_of_likes_icon_label.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI_H));
            number_of_likes_icon_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
            number_of_likes_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
        } else {
            like_this_post_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
            number_of_likes_icon_label.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI));
            number_of_likes_icon_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
            number_of_likes_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
        }
    }

    public void changeCommentTextColor(int like) {
        if (like > 0) {
            comment_in_this_post_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
            number_of_comments_label.setIcon(DesignClasses.return_image(GetImages.COMMENT_MINI_H));
            number_of_comments_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
        } else {
            comment_in_this_post_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
            number_of_comments_label.setIcon(DesignClasses.return_image(GetImages.COMMENT_MINI));
            number_of_comments_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
        }
    }

    private void setCommentsNumbers(long value) {
        if (value > 0) {
            //number_of_comments_label.setIcon(DesignClasses.return_image(GetImages.COMMENT_MINI_H));
            number_of_comments_label.setText(String.valueOf(value));
        } else {
            number_of_comments_label.setText("");
            //number_of_comments_label.setIcon(DesignClasses.return_image(GetImages.COMMENT_MINI));
        }
    }

    private void setSharesNumbers(long value) {
        if (value > 0) {
            number_of_shares_label.setText(String.valueOf(value));
        } else {
            number_of_shares_label.setText("");
        }
    }

    public void changeShareTextColor(int like) {
        if (like > 0) {
            share_this_post_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
            shareLabel.setIcon(DesignClasses.return_image(GetImages.SHARE_MINI_H));
            number_of_shares_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
        } else {
            share_this_post_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
            shareLabel.setIcon(DesignClasses.return_image(GetImages.SHARE_MINI));
            number_of_shares_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
        }
    }

    public void increaseDecreseNL(boolean increase) {
        if (increase) {
            if (this.statusDto.getNl() == null || this.statusDto.getNl() <= 0) {
                this.statusDto.setNl(1L);
            } else {
                this.statusDto.setNl(this.statusDto.getNl() + 1);
            }
        } else {
            if (this.statusDto.getNl() == null || this.statusDto.getNl() <= 0) {
                this.statusDto.setNl(0L);
            } else {
                this.statusDto.setNl(this.statusDto.getNl() - 1);
            }
        }

    }

    private void showComments() {
        commentLikeBar.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.COMMENTS_BORDER_COLOR));
        if (comments.getComponentCount() > 0) {
            commentLikeBar.setBorder(null);
            comments.removeAll();
            comments.revalidate();
            comments.repaint();
            commentsContiner.removeAll();
            commentsContiner.revalidate();
            commentsContiner.setBorder(null);
            commentsContiner.repaint();
            getCommentsOfthisPost().clear();
        } else if (commentsContiner.getComponentCount() > 0) {
            commentsContiner.removeAll();
            commentsContiner.revalidate();
            commentsContiner.setBorder(null);
        } else {
            getSingleShareFeedPanel().clear();
            shareFeedsContiner.removeAll();
            shareFeedsContiner.setBorder(null);
            shareFeedsContinerWrapper.setBorder(null);
            shareFeedsContiner.revalidate();
            shareFeedsContiner.repaint();

            if (NewsFeedMaps.getInstance().getComments().get(statusDto.getNfId()) == null || (statusDto.getNc() != null && NewsFeedMaps.getInstance().getComments().get(statusDto.getNfId()).size() > statusDto.getNc())) {
                new CommentsLikes(statusDto.getNfId(), AppConstants.TYPE_COMMENTS_FOR_STATUS).start();
                addSingleNewComment();
            } else {
                repaintAllcomments();
                //  change_comments_number();
            }
        }
    }

    private void addSingleNewComment() {
        commentsContiner.removeAll();
        newComment = new NewComment(this.statusDto, NewComment.FEED_COMMENT, newCommentRows);
        //commentsContiner.setBorder(new EmptyBorder(3, 3, 3, 3));
        commentsContiner.setBorder(new EmptyBorder(6, 5, 6, 3));
        commentsContiner.add(newComment, BorderLayout.CENTER);
        commentsContiner.revalidate();
    }

    JLabel viewPreviousCommentsTxt;
    JPanel viewPrevious;

    public synchronized void repaintAllcomments() {
        try {
            if (!NewsFeedMaps.getInstance().getComments().isEmpty() && NewsFeedMaps.getInstance().getComments().get(statusDto.getNfId()) != null) {
                comments.removeAll();
                getCommentsOfthisPost().clear();

                Map<Long, String> tempMap = new ConcurrentHashMap<Long, String>();
                long[] times = new long[NewsFeedMaps.getInstance().getComments().get(statusDto.getNfId()).size()];

                int e = 0;
                for (String commentId : NewsFeedMaps.getInstance().getComments().get(statusDto.getNfId()).keySet()) {
                    JsonFields fi = NewsFeedMaps.getInstance().getComments().get(statusDto.getNfId()).get(commentId);
                    if (fi != null && fi.getTm() != null) {
                        tempMap.put(fi.getTm(), commentId);
                        times[e] = fi.getTm();
                        e++;
                    }
                }
                Arrays.sort(times);
                for (int k = 0; k < times.length; k++) {
                    String commentid = tempMap.get(times[k]);
                    if (NewsFeedMaps.getInstance().getComments() != null && NewsFeedMaps.getInstance().getComments().get(statusDto.getNfId()) != null) {
                        //{"actn":389,"sucs":true,"stId":5260,"cmnId":571,"uId":"ring8801911722319","pckFs":21763,"cmn":"asd79","fn":"Wasif","ln":"Islam"}
                        if (commentid != null) {
                            JsonFields fields = NewsFeedMaps.getInstance().getComments().get(statusDto.getNfId()).get(commentid);
                            fields.setIsRead(true);
                            fields.setNfId(statusDto.getNfId());
                            SingleCommentView dd = new SingleCommentView(fields, statusDto.getUserIdentity(), this);
                            dd.setOpaque(false);
                            comments.add(dd);
                        }
                    }
                }

                if (number_of_comments_label.getText() != null && number_of_comments_label.getText().length() > 0 && Integer.valueOf(number_of_comments_label.getText()) > NewsFeedMaps.getInstance().getComments().get(statusDto.getNfId()).size()) {
                    viewPrevious = new JPanel(new BorderLayout());
                    viewPrevious.setBackground(RingColorCode.VIEWPREVIOUS_COMMENTS_PANEL_COLOR);
                    viewPrevious.add(viewPreviousCommentsTxt);
                    viewPrevious.setBorder(new EmptyBorder(3, 4, 5, 3));
                    viewPrevious.setPreferredSize(new Dimension(20, 20));
                    comments.add(viewPrevious, 0);
                    viewPrevious.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    viewPrevious.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            new CommentsLikes(statusDto.getNfId(), AppConstants.TYPE_COMMENTS_FOR_STATUS, NewsFeedMaps.getInstance().getComments().get(statusDto.getNfId()).size()).start();
                        }
                    });

                }
                comments.revalidate();
            }
            addSingleNewComment();
        } catch (Exception e) {
            // e.printStackTrace();
            log.error(getClass().getName() + " 519 repaint==>" + e.getMessage());
        }

    }

    public synchronized void addPreviousComments() {
        try {

            if (!NewsFeedMaps.getInstance().getComments().isEmpty() && NewsFeedMaps.getInstance().getComments().get(statusDto.getNfId()) != null) {
                comments.remove(viewPrevious);
                Map<Long, String> tempMap = new ConcurrentHashMap<Long, String>();
                long[] times = new long[10];
                int e = 0;
                for (String commentId : NewsFeedMaps.getInstance().getComments().get(statusDto.getNfId()).keySet()) {
                    if (!NewsFeedMaps.getInstance().getComments().get(statusDto.getNfId()).get(commentId).getIsRead()) {
                        JsonFields fi = NewsFeedMaps.getInstance().getComments().get(statusDto.getNfId()).get(commentId);
                        if (fi != null) {
                            tempMap.put(fi.getTm(), commentId);
                            times[e] = fi.getTm();
                            e++;
                        }
                    }
                }
                Arrays.sort(times);
                for (int k = times.length - 1; k >= 0; k--) {
                    String commentid = tempMap.get(times[k]);
                    if (commentid != null) {
                        JsonFields fields = NewsFeedMaps.getInstance().getComments().get(statusDto.getNfId()).get(commentid);
                        if (fields != null) {
                            fields.setIsRead(true);
                            fields.setNfId(statusDto.getNfId());
                            SingleCommentView dd = new SingleCommentView(fields, statusDto.getUserIdentity(), this);
                            dd.setOpaque(false);
                            comments.add(dd, 0);
                        }
                    }
                }
                if (number_of_comments_label.getText() != null && number_of_comments_label.getText().length() > 0 && Integer.valueOf(number_of_comments_label.getText()) > NewsFeedMaps.getInstance().getComments().get(statusDto.getNfId()).size()) {
                    viewPrevious = new JPanel(new BorderLayout());
                    viewPrevious.add(viewPreviousCommentsTxt);
                    viewPrevious.setBorder(new EmptyBorder(3, 4, 5, 3));
                    viewPrevious.setBackground(RingColorCode.VIEWPREVIOUS_COMMENTS_PANEL_COLOR);
                    viewPrevious.setPreferredSize(new Dimension(20, 20));
                    comments.add(viewPrevious, 0);
                    viewPrevious.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    viewPrevious.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            new CommentsLikes(statusDto.getNfId(), AppConstants.TYPE_COMMENTS_FOR_STATUS, NewsFeedMaps.getInstance().getComments().get(statusDto.getNfId()).size()).start();
                        }
                    });

                }
                comments.revalidate();
            }

            addSingleNewComment();
        } catch (Exception e) {
            log.error(getClass().getName() + " 519 previous==>" + e.getMessage());
        }

    }

    public void addAComment(JsonFields singleComment) {
        singleComment.setIsRead(true);
        singleComment.setNfId(statusDto.getNfId());
        SingleCommentView dd = new SingleCommentView(singleComment, statusDto.getUserIdentity(), this);
        comments.add(dd);
        //repaintAllcomments();
        comments.revalidate();
        comments.repaint();

    }

    public void removeAComment(Long statusId, String commentID) {
        String singleCommentid = HelperMethods.makeStatusIDCommentIDKey(statusId, commentID);
        SingleCommentView singleCommentView = getCommentsOfthisPost().get(singleCommentid);
        if (singleCommentView != null) {
            removeAComment(singleCommentView);
        }
//        if (comments.getComponentZOrder(singleCommentView) > 0) {
//            comments.remove(singleCommentView);
//            comments.revalidate();
//        }
//        getCommentsOfthisPost().remove(commentID);
    }

    public void removeAComment(SingleCommentView singleCommentView) {
        try {
            if (comments.getComponentZOrder(singleCommentView) > -1) {
                comments.remove(singleCommentView);
                comments.revalidate();
            }
            getCommentsOfthisPost().remove(singleCommentView.singleCommentid);
        } catch (Exception e) {
            log.error("Can not delete comment==>" + singleCommentView.singleCommentid);
        }

    }

//    class AnimatedLike extends JDialog implements ActionListener {
//
//        private int animationDuration = 1000;   // each animation will take 2 seconds
//        private long animStartTime;         // start time for each animation
//        //private JPanel panel;
//        private JLabel lbl;
//        private Timer timer;
//
//        public AnimatedLike(JLabel lbl) {
//            this.lbl = lbl;
//            setUndecorated(true);
//            setResizable(false);
//            setLayout(new BorderLayout());
//            setBackground(new Color(0, 0, 0, 0));
//            setMinimumSize(new Dimension(78, 68));
//            setMaximumSize(new Dimension(78, 68));
//
//            JPanel wrapperPanel = new JPanel();
//            wrapperPanel.setOpaque(false);
//            JLabel label = new JLabel();
//            label.setIcon(DesignClasses.return_image(GetImages.LIKE_ANIM));
//            wrapperPanel.add(label);
//            setContentPane(wrapperPanel);
//            int location_x = (int) lbl.getLocationOnScreen().getX() - 25;
//            int location_y = (int) lbl.getLocationOnScreen().getY() - 25;
//            setLocation(location_x, location_y);
//            setAlwaysOnTop(true);
//
//            timer = new Timer(1, this);
//            timer.setInitialDelay(0);
//            animStartTime = 0 + System.nanoTime() / 1000000;
//            timer.start();
//        }
//
//        @Override
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
//            ///  lbl.setIcon(DesignClasses.return_image(GetImages.LIKE_ANIM));
//            setVisible(true);
//            if (fraction >= 1) {
//                //panel.setBorder(originalBorder);
//                // lbl.setIcon(null);
//                setVisible(false);
//                change_like_number();
//                dispose();
//                //lbl.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI_H));
//                timer.stop();
//            }
//
//        }
//
//    }
    Font original;

    @Override
    public void mouseClicked(MouseEvent arg0) {
        try {
            arg0.getComponent().setFont(original);
            if (arg0.getComponent() == like_this_post_label) {

                new Thread(new Runnable() {  //
                    @Override
                    public void run() {
                        if (like_this_post_label.getForeground().equals(RingColorCode.FEED_UNLIKE_COLOR)) {
                            //new AnimatedLike(number_of_likes_icon_label);                            
                            animatedLike = new AnimatedLike(number_of_likes_icon_label);
                        }

                        if (MyfnfHashMaps.getInstance().getProcessingLikes().get(statusDto.getNfId().toString()) == null) {
                            MyfnfHashMaps.getInstance().getProcessingLikes().put(statusDto.getNfId().toString(), System.currentTimeMillis());
                            int type3 = AppConstants.TYPE_UNLIKE_STATUS;
                            if (like_this_post_label.getForeground().equals(RingColorCode.FEED_UNLIKE_COLOR)) {
                                type3 = AppConstants.TYPE_LIKE_STATUS;
                            }
                            if (statusDto.getSfId() != null) {
                                new FeedLikeUnlikeStatusRequest(type3, statusDto.getNfId(), statusDto.getSfId(), instance).start();
                            } else {
                                new FeedLikeUnlikeStatusRequest(type3, statusDto.getNfId(), instance).start();
                            }
                        } else {
                            long time = MyfnfHashMaps.getInstance().getProcessingLikes().get(statusDto.getNfId().toString());
                            long diff = System.currentTimeMillis() - time;
                            if (diff > 60000) {
                                MyfnfHashMaps.getInstance().getProcessingLikes().remove(statusDto.getNfId().toString());
                            }
                        }
                    } //
                }).start(); //
            } else if (arg0.getComponent() == comment_in_this_post_label) {
                showComments();
            } else if (arg0.getComponent() == number_of_likes_icon_label || arg0.getComponent() == number_of_likes_label) {
                if (NewsFeedMaps.getInstance().getLikes() == null) {
                    new CommentsLikes(statusDto.getNfId(), AppConstants.TYPE_LIKES_FOR_STATUS).start();
                } else {
                    Map<String, SingleMemberInCircleDto> likes = NewsFeedMaps.getInstance().getLikes().get(statusDto.getNfId().toString());
                    if (likes == null || statusDto.getNl() == null || likes.size() == 0 /*|| likes.size() < statusDto.getNl()*/) {
                        new CommentsLikes(statusDto.getNfId(), AppConstants.TYPE_LIKES_FOR_STATUS).start();
                    }
                }
                if (statusDto.getNl() != null && statusDto.getNl() > 0) {
                    int yPos = (int) number_of_likes_icon_label.getLocationOnScreen().getY();
                    int type_int = 1;
                    if (yPos > DefaultSettings.MONITOR_HALF_HEIGHT) {
                        type_int = 0;
                    }
                    likePopSingleFeed = new LikePopupSingleFeed(number_of_likes_icon_label, statusDto.getNfId(), statusDto.getNl(), type_int);
                    likePopSingleFeed.setVisible(number_of_likes_icon_label);
                }

            } else if (arg0.getComponent() == number_of_comments_label) {
                showComments();
            } else if (arg0.getComponent() == shareLabel || arg0.getComponent() == share_this_post_label) {
                if (statusDto.getIShare() != null && statusDto.getIShare() == 1) {
                    HelperMethods.showWarningDialogMessage("You have already shared this..");
                } else {
                    ShareThisFeedPopUp shareThisFeedJopUp = new ShareThisFeedPopUp(statusDto.getNfId());
                    shareThisFeedJopUp.addTextAndEmoticon();
                    shareThisFeedJopUp.addPreviousPost();
                    shareThisFeedJopUp.addButtons();
//                shareThisFeedJopUp.addPostImage();
//                shareThisFeedJopUp.addContent();
                    shareThisFeedJopUp.setVisible(true);
                }
            } else if (arg0.getComponent() == number_of_shares_label) {
                showShareFeeds();
                if (statusDto.getTs() != null && statusDto.getTs() > 0 && statusDto.getWhoShare() != null && statusDto.getWhoShare().size() > 0) {

//                    ShareStatusDialog shareStatusDialog = new ShareStatusDialog(statusDto);
//                    shareStatusDialog.setVisible(true);
                }
            } else if (whoseActivity != null && arg0.getComponent() == whoseActivity) {//this.statusDto.getAfn() + " " + this.statusDto.getAln()
                UserBasicInfo userInfo = null;
                if (statusDto.getAuId() != null && FriendList.getInstance().getFriend_hash_map().get(statusDto.getAuId()) != null) {
                    userInfo = FriendList.getInstance().getFriend_hash_map().get(statusDto.getAuId());
                } else if (statusDto.getAuId() != null && statusDto.getAuId().equals(MyFnFSettings.LOGIN_USER_ID)) {
                    userInfo = MyFnFSettings.userProfile;
                } else if (statusDto.getAuId() != null && MyfnfHashMaps.getInstance().getUnknowonProfileMap().get(statusDto.getAuId()) != null) {
                    userInfo = MyfnfHashMaps.getInstance().getUnknowonProfileMap().get(statusDto.getAuId());
                } else {
                    userInfo = new UserBasicInfo();
                    //userInfo.setUserIdentity(statusDto.getAuId());
                    userInfo.setUserTableId(Long.parseLong(statusDto.getAuId()));
                    userInfo.setFullName(statusDto.getAfn());
                }
                if (userInfo != null) {
                    gotoFriendProfile(userInfo);
                }
            }

        } catch (Exception e) {
            //  e.printStackTrace();
            log.error("mouseClicked==>" + e.getMessage());
        }
    }

    private void gotoFriendProfile(UserBasicInfo userBasicInfo) {
        if (userBasicInfo.getUserIdentity() != null) {
            if (userBasicInfo.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
                GuiRingID.getInstance().action_of_myProfile_button();
            } else if (FriendList.getInstance().getFriend_hash_map().get(userBasicInfo.getUserIdentity()) != null) {
                GuiRingID.getInstance().showFriendProfile(userBasicInfo.getUserIdentity());
                SingleFriendPanel singleFriendPanel = new SingleFriendPanel(userBasicInfo);
                singleFriendPanel.friendInfoPanel.setBackground(RingColorCode.FRIEND_LIST_SELECTION_COLOR);
                //   AllFriendList.setFriendSelectionColor(userBasicInfo.getUserIdentity(), true);
            } else {
                GuiRingID.getInstance().showUnknownProfile(userBasicInfo);
            }
        } else {
            GuiRingID.getInstance().showUnknownProfile(userBasicInfo);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        original = e.getComponent().getFont();
        Map attributes = original.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        //attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        e.getComponent().setFont(original.deriveFont(attributes));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        e.getComponent().setFont(original);
    }

    private void showShareFeeds() {
        if (shareFeedsContiner.getComponentCount() > 0) {
            getSingleShareFeedPanel().clear();
            shareFeedsContiner.removeAll();
            shareFeedsContiner.setBorder(null);
            shareFeedsContinerWrapper.setBorder(null);
            shareFeedsContiner.revalidate();
            shareFeedsContiner.repaint();
        } else {
            comments.removeAll();
            comments.revalidate();
            comments.repaint();
            commentsContiner.removeAll();
            commentsContiner.revalidate();
            commentsContiner.setBorder(null);
            commentsContiner.repaint();
            getCommentsOfthisPost().clear();

            if (statusDto.getTs() != null && statusDto.getTs() > 0) {
                repaintAllShareFeeds();
                new AddDetailsOfShareFeed(statusDto.getNfId()).start();
            }

        }
    }

    public void removeAShareFeed(Long statusId) {
        SingleStatusPanelInShareFeed panel = getSingleShareFeedPanel().get(statusId);
        if (panel != null) {
            shareFeedsContiner.remove(panel);
            shareFeedsContiner.revalidate();
            shareFeedsContiner.repaint();
            getSingleShareFeedPanel().remove(statusId);
        }

        if (shareFeedsContiner.getComponentCount() <= 0) {
            shareFeedsContiner.setBorder(null);
            shareFeedsContinerWrapper.setBorder(null);
        }
    }

    public synchronized void repaintAllShareFeeds() {
        try {
            getSingleShareFeedPanel().clear();
            shareFeedsContinerWrapper.setBorder(new EmptyBorder(5, 35, 5, 0));
            shareFeedsContiner.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.FEED_BORDER_COLOR));
            shareFeedsContiner.removeAll();
            SortedArrayList newsFeeds = (SortedArrayList) NewsFeedMaps.getInstance().getShareNewsFeedId().get(statusDto.getNfId()).clone();

            for (SortedArrayList.Data data : newsFeeds) {
                SingleStatusPanelInShareFeed single = getSingleShareFeedPanel().get(data.getNfId());
                if (single == null) {
                    NewsFeedWithMultipleImage fields = NewsFeedMaps.getInstance().getAllNewsFeeds().get(data.getNfId());
                    single = new SingleStatusPanelInShareFeed(fields, statusDto.getNfId());
                    getSingleShareFeedPanel().put(data.getNfId(), single);
                }
                shareFeedsContiner.add(single);
            }
            shareFeedsContiner.revalidate();
            shareFeedsContiner.repaint();

        } catch (Exception e) {
            //  e.printStackTrace();
            log.error(getClass().getName() + " 519 repaint==>" + e.getMessage());

        }

    }

    private class AddDetailsOfShareFeed extends Thread {

        Long statusID;

        public AddDetailsOfShareFeed(Long status_id) {
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
                        statusDto = NewsFeedMaps.getInstance().getAllNewsFeeds().get(statusID);
                        Thread.sleep(3000);
                        break;
                    }
                }

                repaintAllShareFeeds();
            } catch (Exception ed) {
            }
        }
    }

    public void setSequenceNumber(int sequesnceNumber) {
        this.lblSequenceNumber.setText(sequesnceNumber + "");
    }

}
