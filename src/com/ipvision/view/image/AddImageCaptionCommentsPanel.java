/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

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
import java.awt.Color;
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
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.service.CommentsLikes;
//import org.ipvision.book.NewImageComment;
//import org.ipvision.book.SingleImageComment;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.SingleMemberInCircleDto;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.view.utility.FeedUtils;
import com.ipvision.view.feed.NewComment;
import com.ipvision.view.feed.AnimatedLike;
import com.ipvision.view.utility.TextPanelWithEmoticon;

/**
 *
 * @author Faiz
 */
public class AddImageCaptionCommentsPanel extends JPanel implements MouseListener {

    JPanel statusOnlyPanel;
    public JsonFields imageDTo;
    JPanel captionAndcommentsBar;
    private final JLabel comment_in_this_post_label = DesignClasses.likesCommentsText("Comments", 0, 12);
    private final JLabel number_of_comments_label = DesignClasses.makeAncorLabel("", 0, 12);
    public JLabel like_this_post_label = DesignClasses.likesCommentsText(StaticFields.likeText, 0, 13);
    public JLabel number_of_likes_label = DesignClasses.makeAncorLabel("", 0, 12);
    public JLabel number_of_likes = DesignClasses.makeAncorLabel("", 0, 12);
    public JLabel timeJLabel;
    public JPanel comments;
    public JPanel newCommentJPanel;
    private JScrollPane commentsScroller;
    public NewComment newComment;
    private JLabel optionsLabel;
    private JPanel likeComments;
    private JPanel likePanel;
    AnimatedLike animatedLike;
    JPanel newCommentContainJPanel;
    TextPanelWithEmoticon textPanelWithEmoticon;

    public static AddImageCaptionCommentsPanel instance;

    public static AddImageCaptionCommentsPanel getInatance() {
        return instance;
    }

    //private SingleImgeDetails singleImgeDetails;
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AddImageCaptionCommentsPanel.class);
    private Map<String, JsonFields> imageComments = new ConcurrentHashMap<String, JsonFields>();

    public Map<String, JsonFields> getImageComments() {
        return imageComments;
    }

    public void setImageComments(Map<String, JsonFields> imageComments) {
        this.imageComments = imageComments;
    }
    private Map<String, SingeImageCommentDetails> commentsOfthisImage = new ConcurrentHashMap<String, SingeImageCommentDetails>();

    public Map<String, SingeImageCommentDetails> getCommentsOfthisImage() {
        return commentsOfthisImage;
    }

    public void setCommentsOfthisImage(Map<String, SingeImageCommentDetails> commentsOfthisImage) {
        this.commentsOfthisImage = commentsOfthisImage;
    }
    private Map<String, SingleMemberInCircleDto> imageLikes = new ConcurrentHashMap<String, SingleMemberInCircleDto>();

    public Map<String, SingleMemberInCircleDto> getImageLikes() {
        return imageLikes;
    }

    public void setImageLikes(Map<String, SingleMemberInCircleDto> imageLikes) {
        this.imageLikes = imageLikes;
    }

    public AddImageCaptionCommentsPanel(JsonFields imagedto, JLabel timeJLabel, JPanel likePanel) {
        this.instance = this;
        this.imageDTo = imagedto;
        this.timeJLabel = timeJLabel;
        number_of_likes_label.setPreferredSize(new Dimension(20, 20));
        number_of_likes.setBorder(new EmptyBorder(2, 0, 0, 30));
        number_of_likes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false);
        setLayout(new BorderLayout());
        this.likePanel = likePanel;
        this.likePanel.revalidate();

    }

    public void buildStructure() {
        try {
            captionAndcommentsBar = new JPanel(new BorderLayout(1, 1));
            captionAndcommentsBar.setOpaque(false);
            add(captionAndcommentsBar, BorderLayout.NORTH);

            comments = new JPanel();
            comments.setLayout(new BoxLayout(comments, BoxLayout.Y_AXIS));
            comments.setBackground(RingColorCode.COMMENTS_PANEL_COLOR);
            //comments.setOpaque(false);

            newCommentJPanel = new JPanel(new BorderLayout(0, 0));
            newCommentJPanel.setOpaque(false);
//            newCommentJPanel.setBorder(new EmptyBorder(6, 0, 3, 3));
//            newCommentJPanel.setBorder(new EmptyBorder(6, 0, 3, 0));

            newCommentContainJPanel = new JPanel(new BorderLayout(0, 0));
            newCommentContainJPanel.add(newCommentJPanel);
//            newCommentContainJPanel.setBorder(new MatteBorder(1, 0, 0, 0, RingColorCode.COMMENTS_BORDER_COLOR));
            add(newCommentContainJPanel, BorderLayout.SOUTH);
            newCommentContainJPanel.setBackground(RingColorCode.COMMENTS_PANEL_COLOR);
            //newCommentContainJPanel.setOpaque(false);
            JPanel scrollContent = new JPanel(new BorderLayout(0, 0));
            scrollContent.add(comments, BorderLayout.NORTH);
            scrollContent.setOpaque(false);
            commentsScroller = DesignClasses.getDefaultScrollPaneThin(scrollContent);
            commentsScroller.setPreferredSize(new Dimension(0, (SingleImgeDetails.windowHeight - 140)));
            commentsScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            add(commentsScroller, BorderLayout.CENTER);
        } catch (Exception e) {
            log.error("Error in buildStructure ==>" + e.getMessage());
            // e.printStackTrace();
        }

    }

    public synchronized void initDefaults() {
        try {
            captionAndcommentsBar.add(statusTextPanel(), BorderLayout.CENTER);
            captionAndcommentsBar.revalidate();
            viewPreviousCommentsTxt = DesignClasses.likesCommentsText("Previous Comments", 0, 12);
            viewPreviousCommentsTxt.setIcon(DesignClasses.return_image(GetImages.IMAGE_BOTTOM_ARROW));
            addLikecommentPanel(captionAndcommentsBar);
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error in initDefaults ==>" + e.getMessage());
        }

    }

    public void getImageDetails() {
        new GetImageDetails().start();
    }

    public void showComments() {
        try {
            newCommentContainJPanel.setBorder(new MatteBorder(1, 0, 0, 0, RingColorCode.COMMENTS_BORDER_COLOR));
            if (comments.getComponentCount() > 0) {
                comments.removeAll();
                comments.revalidate();
                newCommentJPanel.removeAll();
                newCommentJPanel.revalidate();
                newCommentJPanel.setBorder(null);
            } else if (newCommentJPanel != null && newCommentJPanel.getComponentCount() > 0) {
                newCommentJPanel.removeAll();
                newCommentJPanel.revalidate();
                newCommentJPanel.setBorder(null);
            } else {

                if (getImageComments().isEmpty() || (this.imageDTo.getNc() != null && getImageComments().size() > this.imageDTo.getNc())) {
                    new CommentsLikes(this.imageDTo.getImageId(), AppConstants.TYPE_COMMENTS_FOR_IMAGE).start();
                    addSingleCommentDetails();
                } else {
                    repaintAllcomments();
                    changeImageCommentAllinBar();
                }
            }
        } catch (Exception e) {
            //  e.printStackTrace();
            log.error("Error in showComments ==>" + e.getMessage());
        }
    }

    private void addSingleCommentDetails() {

        newCommentJPanel.removeAll();
        //newCommentJPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        newCommentJPanel.setBorder(new EmptyBorder(6, 0, 3, 0));
        newComment = new NewComment(this.imageDTo, NewComment.IMAGE_COMMENT);
        newCommentJPanel.add(newComment);
        newCommentJPanel.revalidate();
    }

    public void addLikesDetails() {

        likePanel.removeAll();
        likePanel.setLayout(new BorderLayout());
        //  likePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        if (this.imageDTo.getIl() != null && this.imageDTo.getIl() > 0) {
            like_this_post_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
            number_of_likes_label.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI_H));
            number_of_likes_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
            number_of_likes.setForeground(RingColorCode.FEED_LIKED_COLOR);
        }
        like_this_post_label.setBorder(new EmptyBorder(0, 0, 0, 5));
        like_this_post_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        like_this_post_label.addMouseListener(this);
        //   likePanel.add(like_this_post_label);
        number_of_likes_label.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI));
        number_of_likes_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        number_of_likes_label.addMouseListener(this);
        number_of_likes.addMouseListener(this);
        ///   likePanel.add(number_of_likes_label);

        JPanel likesAndNumbersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 5));
        likesAndNumbersPanel.setOpaque(false);
        likesAndNumbersPanel.add(like_this_post_label);
        likesAndNumbersPanel.add(number_of_likes_label);
        likesAndNumbersPanel.add(number_of_likes);
        likePanel.add(likesAndNumbersPanel, BorderLayout.WEST);

        JPanel optionsContainerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        optionsContainerPanel.setPreferredSize(new Dimension(100, 30));
        likePanel.add(optionsContainerPanel, BorderLayout.EAST);
        optionsContainerPanel.setBackground(new Color(0, 0, 0));

        optionsLabel = new JLabel();
        optionsLabel.setText("Options");
        optionsLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        optionsLabel.setForeground(Color.white);
        //optionsLabel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        //optionsLabel.setBackground(Color.YELLOW);
        optionsLabel.setFont(new Font("Serif", Font.BOLD, 12));
        optionsLabel.addMouseListener(this);
        optionsContainerPanel.add(optionsLabel);
        likePanel.setPreferredSize(new Dimension(0, 40));
        likePanel.revalidate();
    }

    private void addLikecommentPanel(JPanel image_and_comment_like_panel) {
        JPanel commentBar = new JPanel();
        commentBar.setBackground(RingColorCode.COMMENTS_LIKE_BAR_COLOR);
        commentBar.setLayout(new BorderLayout());
        // commentBar.setLayout(null);
        commentBar.setPreferredSize(new Dimension(0, 26));
        commentBar.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.COMMENTS_BORDER_COLOR));//new Color(0xe1e2e3)));
        image_and_comment_like_panel.add(commentBar, BorderLayout.SOUTH);
        number_of_comments_label.setIcon(DesignClasses.return_image(GetImages.COMMENT_MINI));

        likeComments = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        likeComments.setOpaque(false);
        commentBar.add(likeComments, BorderLayout.WEST);
        likeComments.add(comment_in_this_post_label);
        comment_in_this_post_label.addMouseListener(this);
        comment_in_this_post_label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (this.imageDTo.getIc() != null && this.imageDTo.getIc() > 0) {
            comment_in_this_post_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
            number_of_comments_label.setIcon(DesignClasses.return_image(GetImages.COMMENT_MINI_H));
            number_of_comments_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
        } else {
            comment_in_this_post_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
            number_of_comments_label.setIcon(DesignClasses.return_image(GetImages.COMMENT_MINI));
            number_of_comments_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
        }
        likeComments.add(number_of_comments_label);
        number_of_comments_label.addMouseListener(this);
        number_of_comments_label.setCursor(new Cursor(Cursor.HAND_CURSOR));

    }
    /*comment like change*/

    public void increaseDecreseNC(boolean increase) {
        if (increase) {
            if (this.imageDTo.getNc() == null || this.imageDTo.getNc() <= 0) {
                this.imageDTo.setNc(1L);
            } else {
                this.imageDTo.setNc(this.imageDTo.getNc() + 1);
            }
        } else {
            if (this.imageDTo.getNc() == null || this.imageDTo.getNc() <= 0) {
                this.imageDTo.setNc(0L);
            } else {
                this.imageDTo.setNc(this.imageDTo.getNc() - 1);
            }
        }

    }

    public void changeIComment(boolean commented) {
        if (commented) {
            this.imageDTo.setIc((short) 1);
        } else {
            this.imageDTo.setIc((short) 0);
        }

    }

    public void changeCommentTextColor() {
        if (this.imageDTo.getIc() != null && this.imageDTo.getIc() > 0) {
            comment_in_this_post_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
            number_of_comments_label.setIcon(DesignClasses.return_image(GetImages.COMMENT_MINI_H));
            number_of_comments_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
        } else {
            comment_in_this_post_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
            number_of_comments_label.setIcon(DesignClasses.return_image(GetImages.COMMENT_MINI));
            number_of_comments_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
        }
    }

//    private void setCommentsNumbers(long value) {
//        if (value > 0) {
//            //number_of_comments_label.setIcon(DesignClasses.return_image(GetImages.COMMENT_MINI_H));
//            number_of_comments_label.setText(String.valueOf(value));
//        } else {
//            number_of_comments_label.setText("");
//            //number_of_comments_label.setIcon(DesignClasses.return_image(GetImages.COMMENT_MINI));
//        }
//    }
    public void changeImageCommentAllinBar() {
        if (this.imageDTo.getNc() != null && this.imageDTo.getNc() > 0) {
            number_of_comments_label.setText(String.valueOf(this.imageDTo.getNc()));
        } else {
            number_of_comments_label.setText("");
        }
        changeCommentTextColor();
    }

    public void increaseDecreseNL(boolean increase) {
        if (increase) {
            if (this.imageDTo.getNl() == null || this.imageDTo.getNl() <= 0) {
                this.imageDTo.setNl(1L);
            } else {
                this.imageDTo.setNl(this.imageDTo.getNl() + 1);
            }
        } else {
            if (this.imageDTo.getNl() == null || this.imageDTo.getNl() <= 0) {
                this.imageDTo.setNl(0L);
            } else {
                this.imageDTo.setNl(this.imageDTo.getNl() - 1);
            }
        }

    }

    public void changeIlikeUnlike(boolean liked) {
        if (liked) {
            this.imageDTo.setIl((short) 1);
        } else {
            this.imageDTo.setIl((short) 0);
        }

    }

    private void changeLikeTextColor() {
        if (this.imageDTo.getIl() != null && this.imageDTo.getIl() > 0) {
            like_this_post_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
            number_of_likes_label.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI_H));
            number_of_likes_label.setForeground(RingColorCode.FEED_LIKED_COLOR);
            number_of_likes.setForeground(RingColorCode.FEED_LIKED_COLOR);
        } else {
            like_this_post_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
            number_of_likes_label.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI));
            number_of_likes_label.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
            number_of_likes.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
            //like_this_post_label.setForeground(DefaultSettings.UNLIKE_COLOR_IMAGE);
        }
    }

    public void changeImageLikeAll() {
        if (this.imageDTo.getNl() != null && this.imageDTo.getNl() > 0) {
            number_of_likes.setText(String.valueOf(this.imageDTo.getNl()));
        } else {
            number_of_likes.setText("");

        }
        changeLikeTextColor();
    }
    /*comment like change end*/

    public JPanel statusTextPanel() {
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        String text = this.imageDTo.getCptn() != null ? this.imageDTo.getCptn() : "";

        //FeedUtils.changeComment(text, SingleImgeDetails.commentWidth - 100, textPanel);
        textPanelWithEmoticon = new TextPanelWithEmoticon(SingleImgeDetails.commentWidth - 100, text);
        textPanel.add(textPanelWithEmoticon);

        return textPanel;
    }

    private JPanel viewPrevious;
    private JLabel viewPreviousCommentsTxt;

    Map<Long, String> tempMap;

    public synchronized void repaintAllcomments() {
        try {
            if (!getImageComments().isEmpty()) {
                comments.removeAll();

                tempMap = new ConcurrentHashMap<Long, String>();
                long[] times = new long[getImageComments().size()];

                int e = 0;
                for (String commentId : getImageComments().keySet()) {
                    JsonFields fi = getImageComments().get(commentId);
                    if (fi.getTm() != null) {
                        tempMap.put(fi.getTm(), commentId);
                        times[e] = fi.getTm();
                        e++;
                    }
                }
                Arrays.sort(times);
                // for (int k = 0; k < times.length; k++) {
                for (int k = times.length - 1; k >= 0; k--) {
                    String commentid = tempMap.get(times[k]);
                    JsonFields fields = getImageComments().get(commentid);
                    fields.setIsRead(true);
                    fields.setImageId(imageDTo.getImageId());
                    SingeImageCommentDetails dd = new SingeImageCommentDetails(this, fields);
                    dd.addCommentUIinMap();
                    comments.add(dd);
                }

                if (number_of_comments_label.getText() != null && number_of_comments_label.getText().length() > 0 && Integer.valueOf(number_of_comments_label.getText()) > getImageComments().size()) {
                    viewPrevious = new JPanel(new BorderLayout());
//                    viewPrevious.setBackground(DefaultSettings.COMMENTS_BACK_GROUND_COLOR);
                    viewPrevious.setBackground(RingColorCode.VIEWPREVIOUS_COMMENTS_PANEL_COLOR);
                    viewPrevious.setBorder(new EmptyBorder(3, 4, 5, 3));
                    viewPrevious.add(viewPreviousCommentsTxt);
                    viewPrevious.setPreferredSize(new Dimension(20, 20));
                    //comments.add(viewPrevious, 0);
                    comments.add(viewPrevious);
                    viewPrevious.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    viewPrevious.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            new CommentsLikes(imageDTo.getImageId(), AppConstants.TYPE_COMMENTS_FOR_IMAGE, getImageComments().size()).start();
                        }
                    });

                }
                comments.revalidate();
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        if (commentsScroller != null) {
                            commentsScroller.getVerticalScrollBar().setValue(comments.getHeight());
                            commentsScroller.revalidate();
                        }
                    }
                });
            }
            addSingleCommentDetails();
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error in repaintAllcomments ==>" + e.getMessage());
        }

    }

    public synchronized void addPreviousComments() {
        try {

            if (!getImageComments().isEmpty()) {
                if (viewPrevious != null) {
                    comments.remove(viewPrevious);
                }
                tempMap = new ConcurrentHashMap<Long, String>();
                long[] times = new long[10];
                int e = 0;
                for (String commentId : getImageComments().keySet()) {
                    if (!getImageComments().get(commentId).getIsRead()) {
                        JsonFields fi = getImageComments().get(commentId);
                        tempMap.put(fi.getTm(), commentId);
                        times[e] = fi.getTm();
                        e++;
                    }
                }
                Arrays.sort(times);
                for (int k = times.length - 1; k >= 0; k--) {
                    String commentid = tempMap.get(times[k]);
                    if (commentid != null) {
                        JsonFields fields = getImageComments().get(commentid);
                        fields.setIsRead(true);
                        fields.setImageId(imageDTo.getImageId());
                        SingeImageCommentDetails dd = new SingeImageCommentDetails(this, fields);
                        dd.addCommentUIinMap();
                        //comments.add(dd, 0);
                        comments.add(dd);
                    }
                }
                if (number_of_comments_label.getText() != null && number_of_comments_label.getText().length() > 0 && Integer.valueOf(number_of_comments_label.getText()) > getImageComments().size()) {
                    viewPrevious = new JPanel(new BorderLayout());
                    viewPrevious.add(viewPreviousCommentsTxt);
//                    viewPrevious.setBackground(DefaultSettings.COMMENTS_BACK_GROUND_COLOR);
                    viewPrevious.setBackground(RingColorCode.VIEWPREVIOUS_COMMENTS_PANEL_COLOR);
                    viewPrevious.setBorder(new EmptyBorder(3, 4, 5, 3));
                    viewPrevious.setPreferredSize(new Dimension(20, 20));
                    //comments.add(viewPrevious, 0);
                    comments.add(viewPrevious);
                    viewPrevious.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    viewPrevious.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            new CommentsLikes(imageDTo.getImageId(), AppConstants.TYPE_COMMENTS_FOR_IMAGE, getImageComments().size()).start();
                        }
                    });

                }
                comments.revalidate();
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        commentsScroller.getVerticalScrollBar().setValue(0);
                        commentsScroller.revalidate();
                    }
                });
            }

            addSingleCommentDetails();
        } catch (Exception e) {
            log.error(getClass().getName() + " previous comments ex==>" + e.getMessage());
        }

    }

    public void addAComment(JsonFields singleComment) {
        singleComment.setIsRead(true);
        singleComment.setImageId(imageDTo.getImageId());
        // SingleImageComment dd = new SingleImageComment(reqular_expresion, singleComment, statusDto.getUserIdentity());
        SingeImageCommentDetails dd = new SingeImageCommentDetails(this, singleComment);
        dd.addCommentUIinMap();
        comments.add(dd, 0);
        comments.revalidate();

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                commentsScroller.getVerticalScrollBar().setValue(comments.getHeight());
                commentsScroller.revalidate();
            }
        });
        increaseDecreseNC(true);
        changeImageCommentAllinBar();
    }

    public void removeAComment(int index) {
        comments.remove(index);
        comments.revalidate();
        increaseDecreseNC(false);
        changeImageCommentAllinBar();
    }

    public void updateTime(long time) {
        if (time > 0) {
            timeJLabel.setText(FeedUtils.getShowableDate(time));
            timeJLabel.setToolTipText(FeedUtils.getActualDate(time));
            timeJLabel.revalidate();
        }

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
//                changeImageLikeAll();
//                // lbl.setIcon(DesignClasses.return_image(GetImages.LIKE_MINI_H));
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
            if (arg0.getComponent() == like_this_post_label) {
                new Thread(new Runnable() {  //
                    @Override
                    public void run() {
                        if (like_this_post_label.getForeground().equals(RingColorCode.FEED_UNLIKE_COLOR)) {
                            //new AnimatedLike(number_of_likes_label);
                            BasicImageViewStructure.animatedLike = null;
                            BasicImageViewStructure.animatedLike = new AnimatedLike(number_of_likes_label);
                        } //
                        if (MyfnfHashMaps.getInstance().getProcessingLikes().get(imageDTo.getImageId().toString()) == null) {
                            MyfnfHashMaps.getInstance().getProcessingLikes().put(imageDTo.getImageId().toString(), System.currentTimeMillis());
                            if (like_this_post_label.getForeground().equals(RingColorCode.FEED_LIKED_COLOR)) {
                                (new ImageLikeUnlikeRequest(AppConstants.TYPE_LIKE_UNLIKE_IMAGE, 0)).start();
                            } else {
                                (new ImageLikeUnlikeRequest(AppConstants.TYPE_LIKE_UNLIKE_IMAGE, 1)).start();
                            }
                        } else {
                            long time = MyfnfHashMaps.getInstance().getProcessingLikes().get(imageDTo.getImageId().toString());
                            long diff = System.currentTimeMillis() - time;
                            if (diff > 60000) {
                                MyfnfHashMaps.getInstance().getProcessingLikes().remove(imageDTo.getImageId().toString());
                            }
                        }
                    } //
                }).start(); //
            } else if (arg0.getComponent() == comment_in_this_post_label) {
                arg0.getComponent().setFont(original);
                showComments();

            } else if (arg0.getComponent() == number_of_likes_label || arg0.getComponent() == number_of_likes) {
                if (getImageLikes().isEmpty()) {
                    new CommentsLikes(imageDTo.getImageId(), AppConstants.TYPE_LIKES_FOR_IMAGE).start();
                } else {
                    if (imageDTo.getNl() != null && getImageLikes().size() < imageDTo.getNl()) {
                        new CommentsLikes(imageDTo.getImageId(), AppConstants.TYPE_LIKES_FOR_IMAGE).start();
                    }
                }
                if (imageDTo.getNl() != null && imageDTo.getNl() > 0) {
                    int yPos = (int) number_of_likes_label.getLocationOnScreen().getY();
                    int type_int = 1;
                    if (yPos > DefaultSettings.MONITOR_HALF_HEIGHT) {
                        type_int = 0;
                    }
                    BasicImageViewStructure.imageLikesPopup = null;
                    BasicImageViewStructure.imageLikesPopup = new ImageLikesPopup(number_of_likes_label, getAddImageCaptionCommentsPanel());
                    BasicImageViewStructure.imageLikesPopup.setVisible(number_of_likes_label, type_int);
                }
                //BasicImageViewStructure.imageLikesPopup.addLoading();
                number_of_likes_label.setFont(original);
            } else if (arg0.getComponent() == number_of_comments_label) {
                showComments();
            } else if (arg0.getComponent() == optionsLabel) {
                OptionActionPopup pop = new OptionActionPopup(imageDTo);
                pop.show(optionsLabel, optionsLabel.getX() - 148, optionsLabel.getY() - 55);

                //pop.setVisible(true);
            }
            //   likePanel.repaint();
        } catch (Exception e) {
        }
//
    }

    @Override
    public void mousePressed(MouseEvent arg1) {

        if (arg1.getComponent() == optionsLabel) {

            optionsLabel.setBackground(Color.GRAY);
            optionsLabel.setOpaque(true);

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        e.getComponent().setFont(original);

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        original = e.getComponent().getFont();

        Map attributes = original.getAttributes();
        if (e.getComponent() == optionsLabel) {
            optionsLabel.setBackground(Color.GRAY);
            optionsLabel.setOpaque(true);
        } else {
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            e.getComponent().setFont(original.deriveFont(attributes));
        }

//        likePanel.revalidate();
//        likePanel.repaint();
        if (likePanel != null) {
            likePanel.setVisible(true);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        e.getComponent().setFont(original);
        optionsLabel.setOpaque(false);

    }

    class ImageLikeUnlikeRequest extends Thread {

        private int type;
        private int liked;

        public ImageLikeUnlikeRequest(int type, int liked) {
            this.type = type;
            this.liked = liked;

        }

        @Override
        public void run() {
            try {
                JsonFields pakToSend = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                pakToSend.setPacketId(pakId);
                pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                pakToSend.setAction(type);
                pakToSend.setImageId(imageDTo.getImageId());
                pakToSend.setLkd(liked);

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
                                if (!getImageLikes().isEmpty()) {
                                    if (this.liked == 1) {
                                        SingleMemberInCircleDto addMe = new SingleMemberInCircleDto();
                                        addMe.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
                                        addMe.setFullName(MyFnFSettings.userProfile.getFullName());
                                        // addMe.setLastName(MyFnFSettings.userProfile.getLastName());
                                        getImageLikes().put(MyFnFSettings.LOGIN_USER_ID, addMe);
                                    } else {
                                        getImageLikes().remove(MyFnFSettings.LOGIN_USER_ID);

                                    }
                                }
                                if (this.liked == 1) {
                                    increaseDecreseNL(true);
                                    changeIlikeUnlike(true);
                                } else {
                                    increaseDecreseNL(false);
                                    changeIlikeUnlike(false);
                                }
                                changeImageLikeAll();
                            } catch (Exception e) {

                                MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                                MyfnfHashMaps.getInstance().getProcessingLikes().remove(String.valueOf(imageDTo.getImageId()));
                                return;
                            }

                        } else {
                            //   JOptionPane.showConfirmDialog(null, response.getMessage(), "Like/Unlike failed", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        MyfnfHashMaps.getInstance().getProcessingLikes().remove(String.valueOf(imageDTo.getImageId()));
                        return;
                    }

                }
                //   JOptionPane.showConfirmDialog(null, "Can not process like/unlike right now", "Like/Unlike failed", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
            } catch (Exception e) {
            }

        }
    }

    private AddImageCaptionCommentsPanel getAddImageCaptionCommentsPanel() {
        return this;
    }

    class CommentInImageRequest extends Thread {

        private JsonFields newComments;

        public CommentInImageRequest(JsonFields newComments) {
            this.newComments = newComments;
        }

        @Override
        public void run() {
            try {
                /*
                 {"actn":180,"pckId":"1409481139435ring8801717634317","sId":"1409481170524ring8801717634317","cmn":"Random comment 26480.399736449446","imgId":295}
                 */
                JsonFields pakToSend = new JsonFields();

                String pakId = SendToServer.getRanDomPacketID();
                pakToSend.setPacketId(pakId);
                pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                pakToSend.setAction(AppConstants.TYPE_ADD_IMAGE_COMMENT);
                pakToSend.setImageId(newComments.getImageId());
                pakToSend.setComment(newComments.getComment());
                SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
                Thread.sleep(25);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.UPDATE_PORT);
                    } else {
                        FeedBackFields responsefields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        if (responsefields.getSuccess()) {
                            try {
                                newComments.setCmnId(responsefields.getCmnId());
                                newComments.setTm(responsefields.getTm());
                                newComments.setUserIdentity(MyFnFSettings.LOGIN_USER_ID);
                                newComments.setTl(0L);
                                newComments.setIl((short) 0);

                                if (imageDTo.getImageId().longValue() == newComments.getImageId()) {
                                    getImageComments().put(responsefields.getCmnId(), newComments);
                                    changeIComment(true);
                                    addAComment(newComments);
                                    changeImageCommentAllinBar();

                                }
                            } catch (Exception e) {
                                log.error("New Image Comment ex==>" + e.getMessage());
                            }

                        } else {
                            // JOptionPane.showConfirmDialog(null, responsefields.getMessage(), "Comments failed", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
                            HelperMethods.showWarningDialogMessage(responsefields.getMessage());
                        }
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        return;
                    }

                }
                HelperMethods.showWarningDialogMessage("Can not process this comment right now");
                // JOptionPane.showConfirmDialog(null, "Can not process this comment right now", "Comments failed", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
            }
        }
    }

    class GetImageDetails extends Thread {

        @Override
        public void run() {

            try {

                JsonFields pakToSend = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                pakToSend.setPacketId(pakId);
                pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                pakToSend.setImageId(imageDTo.getImageId());
                pakToSend.setAction(AppConstants.TYPE_IMAGE_DETAILS);

                SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.REQUEST_PORT);
                Thread.sleep(500);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        FeedBackFields response = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        if (response.getSuccess()) {
                            imageDTo = HelperMethods.getJsonFields(response.getMessage());
                            updateTime(response.getTm());
                            changeImageCommentAllinBar();
                            changeImageLikeAll();
                            showComments();
                            return;
                        } else {
                            return;
                        }
                    }
                }

            } catch (Exception ex) {
                // ex.printStackTrace();
                log.error("Error in here ==>" + ex.getMessage());
            }
        }

    }

}
