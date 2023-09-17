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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.view.GuiRingID;
import com.ipvision.service.SendFriendRequest;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.SingleMemberInCircleDto;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.view.utility.JDialogContactType;
import com.ipvision.view.utility.LikesJDialogBasics;
import com.ipvision.view.utility.ImageHelpers;
//import org.ipvision.utils.img.SingleImgeDetails;

/**
 *
 * @author Faiz
 */
public class LikesInPost extends LikesJDialogBasics {

    JLabel loading;
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LikesInPost.class);
    private Long likeskey;

    public LikesInPost(JComponent locationRelativeTo, Long likesKey) {
        setLocationRelativeTo(locationRelativeTo);
        this.likeskey = likesKey;
    }

    public void addLoading() {
        loading = DesignClasses.loadingLable(true);
        content.add(loading);
        titleBarLabel.setText("People who loves it");
        content.revalidate();
        new LoadLikes().start();
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
        } else {
            BufferedImage img2 = ImageHelpers.getUnknownImage(true);//.getBufferImageFromurl(null);
            frienImageLabel.setIcon(new ImageIcon(img2));
            img2 = null;
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
//                            if (SingleImgeDetails.getSingleImgeDetails() != null) {
//                                SingleImgeDetails.getSingleImgeDetails().hideThis();

//                            }
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
        addBtn.setToolTipText("Add friend");
        if (!basicinfo.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID) && FriendList.getInstance().getFriend_hash_map().get(basicinfo.getUserIdentity()) == null) {
            rowPanel.add(addBtn, BorderLayout.EAST);
        }

        return rowPanel;
    }

    class LoadLikes extends Thread {

        private Map<String, SingleMemberInCircleDto> likes;
        private int likes_num = 0;

        @Override
        public void run() {
            try {

                for (int i = 0; i <= 10; i++) {
                    likes = NewsFeedMaps.getInstance().getLikes().get(likeskey.toString());
                    if (likes != null && !likes.isEmpty() && likes.size() > likes_num) {
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
                log.error("ImageLikesPopup-->" + ex.getMessage());
                hideThis();
            } finally {
                loading.setIcon(null);
                loading = null;
                content.revalidate();
            }

        }
    }

}
