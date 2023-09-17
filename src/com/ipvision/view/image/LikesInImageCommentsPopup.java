/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.ListOfLikesCommentInImage;
import com.ipvision.service.SendFriendRequest;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.SingleMemberInCircleDto;
import com.ipvision.model.stores.FriendList;
import com.ipvision.view.utility.JDialogContactType;
import com.ipvision.view.utility.LikesJDialogBasics;

/**
 *
 * @author Faiz
 */
public class LikesInImageCommentsPopup extends LikesJDialogBasics {

    JLabel loading = DesignClasses.loadingLable(true);
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LikesInImageCommentsPopup.class);
    SingeImageCommentDetails singeImageCommentDetails;
    private JPanel panelSeeMore;
    private JLabel labelSeeMore = DesignClasses.makeLableBold1("See more...", 12);
    private static LikesInImageCommentsPopup likes_popup_imageComment;
    private int count = 0;

    public static LikesInImageCommentsPopup getInstance() {
        return likes_popup_imageComment;
    }

    public void setVisible(JComponent com, int type_int) {
        likes_popup_imageComment = this;
        int location_x, location_y;
        if (type_int == 0) {  //lower half
            location_x = (int) com.getLocationOnScreen().getX() + 10;
            location_y = (int) com.getLocationOnScreen().getY() - 310;
        } else {   //upper half
            location_x = (int) com.getLocationOnScreen().getX() + 10;
            location_y = (int) com.getLocationOnScreen().getY() + 2;
        }
        setLocation(location_x, location_y);
        setVisible(true);

    }

    public LikesInImageCommentsPopup(JComponent locationRelativeTo, SingeImageCommentDetails singeImageCommentDetails) {
        this.likes_popup_imageComment = this;
        titleBarLabel.setText("People who likes it");
        //setLocationRelativeTo(locationRelativeTo);
        setLocation(locationRelativeTo.getX(), locationRelativeTo.getY());
        this.singeImageCommentDetails = singeImageCommentDetails;

        panelSeeMore = new JPanel();
        panelSeeMore.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelSeeMore.setOpaque(false);
        panelSeeMore.add(labelSeeMore);
        mouseLiseraction(panelSeeMore);

        addLoading();
        if (singeImageCommentDetails.getImageCommentsLikes() != null
                && singeImageCommentDetails.getImageCommentsLikes().size() > 0) {
            loadLikes();
        }
    }

    private void addLoading() {
        content.add(loading);
        content.revalidate();
        // new LoadLikes().start();
    }

    public void removeLoading() {
        content.remove(loading);
        content.revalidate();
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

        JPanel rowPanel = new JPanel();
        rowPanel.setName(basicinfo.getUserIdentity());
        rowPanel.setOpaque(false);
        rowPanel.setLayout(new BorderLayout());
        rowPanel.setPreferredSize(new Dimension(width - 50, 40));
        //rowPanel.setBorder(new EmptyBorder(0, 5, 3, 5));
        rowPanel.setBorder(new MatteBorder(0, 0, 1, 0, RingColorCode.COMMENTS_BORDER_COLOR));
        JLabel frienImageLabel = new JLabel();

        String url = "";

        if (basicinfo.getProfileImage() != null) {
            url = basicinfo.getProfileImage();
        } else if (userid.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
            url = MyFnFSettings.userProfile.getProfileImage();
        }

        if (url.trim().length() > 0) {
            ImageHelpers.addProfileImageThumb(frienImageLabel, url);
            //    BufferedImage img = DesignClasses.getBufferImageFromurlFriendList(url, 34);
            //    frienImageLabel.setIcon(new ImageIcon(img));
        } else {
            BufferedImage img = ImageHelpers.getUnknownImage(true);
            frienImageLabel.setIcon(new ImageIcon(img));
            img.flush();
            img = null;
        }
        frienImageLabel.revalidate();
        rowPanel.add(frienImageLabel, BorderLayout.WEST);

        JLabel fullname = DesignClasses.makeAncorLabelDefaultColor(userid.getFullName() /*+ " " + userid.getLastName()*/, 0, 12);
        fullname.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // FeedUtils.onClickFriendName(fullname, basicinfo);
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
                            if (SingleImgeDetails.getSingleImgeDetails() != null) {
                                SingleImgeDetails.getSingleImgeDetails().hideThis();
                            } else if (SingleImageDetailsForNotifications.getSingleImgeDetails() != null) {
                                SingleImageDetailsForNotifications.getSingleImgeDetails().hideThis();
                            } else if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null) {
                                SingleImageDetailsProfileCover.getSingleImgeDetails().hideThis();
                            }

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

        if (basicinfo.getUserIdentity() != null && FriendList.getInstance().getFriend_hash_map().get(basicinfo.getUserIdentity()) == null && !basicinfo.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
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
            rowPanel.add(addBtn, BorderLayout.EAST);
        }

        return rowPanel;
    }

    public void addLikesFromServer(ArrayList<SingleMemberInCircleDto> likesList) {
        removeLoading();
        removeSeeMore();
        for (SingleMemberInCircleDto single : likesList) {
            JPanel dd = singleLike(single);
            content.add(dd);
            content.add(Box.createRigidArea(new Dimension(0, 5)));
            content.revalidate();
            content.repaint();
        }
        if (singeImageCommentDetails.getImageCommentsLikes().size() < singeImageCommentDetails.commentDto.getTl()) {
            addSeeMore(singeImageCommentDetails.getImageCommentsLikes().size());
        }
        content.revalidate();
    }

    private void loadLikes() {
        Map<String, SingleMemberInCircleDto> likes;
        //likes = NewsFeedMaps.getInstance().getImageLikes().get(singeImageCommentDetails.commentDto.g);
        likes = singeImageCommentDetails.getImageCommentsLikes();

        content.removeAll();
        for (String userid : likes.keySet()) {
            SingleMemberInCircleDto frd = likes.get(userid);
            JPanel dd = singleLike(frd);
            content.add(dd);
            content.add(Box.createRigidArea(new Dimension(0, 5)));
            content.revalidate();
            content.repaint();
        }
        if (likes.size() < singeImageCommentDetails.commentDto.getTl()) {
            addSeeMore(likes.size());
        }

        content.revalidate();

    }

    /* class LoadLikes extends Thread {

     private Map<String, SingleMemberInCircleDto> likes;
     private int likes_num = 0;

     @Override
     public void run() {
     try {
     for (int i = 0; i <= 10; i++) {
     likes = singeImageCommentDetails.getImageCommentsLikes();
     if (likes != null
     && singeImageCommentDetails.commentDto.getNl() != null
     && likes.size() >= singeImageCommentDetails.commentDto.getNl()) {
     break;
     } else {
     Thread.sleep(200);
     }
     }
     content.removeAll();
     for (String userid : likes.keySet()) {
     SingleMemberInCircleDto frd = likes.get(userid);
     JPanel dd = singleLike(frd);
     content.add(dd);
     content.add(Box.createRigidArea(new Dimension(0, 5)));
     content.revalidate();
     }
     } catch (Exception ex) {
     ex.printStackTrace();
     log.error("ImageLikesPopup-->" + ex.getMessage());
     hideThis();
     } finally {
     loading.setIcon(null);
     loading = null;
     content.revalidate();
     }

     }
     }*/
    private void addSeeMore(int count) {
        this.count = count;
        content.add(panelSeeMore);
        content.revalidate();
    }

    private void removeSeeMore() {
        content.remove(panelSeeMore);
        content.revalidate();
    }

    public void mouseLiseraction(final JComponent panel) {

        panel.addMouseListener(new MouseAdapter() {
            Font original;

            @Override
            public void mouseEntered(MouseEvent e) {
                if (e.getSource() == panelSeeMore) {
                    original = labelSeeMore.getFont();
                    Map attributes = original.getAttributes();
                    attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                    labelSeeMore.setFont(original.deriveFont(attributes));
                    labelSeeMore.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() == panelSeeMore) {
                    removeSeeMore();
                    addLoading();
                    new ListOfLikesCommentInImage(singeImageCommentDetails.commentDto.getImageId(), singeImageCommentDetails.commentDto.getCmnId(), AppConstants.TYPE_IMAGE_COMMENT_LIKES, count).start();
                    labelSeeMore.setFont(original);
                    content.revalidate();
                    content.repaint();
                }

            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (e.getSource() == panelSeeMore) {
                    labelSeeMore.setFont(original);
                }

            }
        });

    }

}
