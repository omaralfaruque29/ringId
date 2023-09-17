/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.stores.FriendList;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.SingleMemberInCircleDto;
import com.ipvision.model.stores.NewsFeedMaps;

/**
 *
 * @author Faiz Ahmed
 */
public class PeopleWhoLikesThisPost extends JPopupMenu {

    private String statusid;
    private JPanel singleFriendPanel;
    private long currentlikes;
    int size = 20;

    public PeopleWhoLikesThisPost(String statusid, long currentlikes) {
        super();
        this.statusid = statusid;
        this.currentlikes = currentlikes;
        //  this.setPreferredSize(new Dimension(180, 200));
        this.setLayout(new BorderLayout());
        //    this.setBackground(Color.WHITE);
        this.setBorder(DefaultSettings.DEFAULT_BORDER);
        JPanel friendListPanel = new JPanel();
        friendListPanel.setBackground(Color.WHITE);
        singleFriendPanel = new JPanel();
        friendListPanel.add(singleFriendPanel, BorderLayout.NORTH);
        singleFriendPanel.setLayout(new BoxLayout(singleFriendPanel, BoxLayout.Y_AXIS));
        singleFriendPanel.setOpaque(false);
        JScrollPane freindlistScorllPanel = DesignClasses.getDefaultScrollPane(friendListPanel);//new JScrollPane(friendListPanel);
        add(freindlistScorllPanel, BorderLayout.CENTER);

    }

    public void addLikes() {
        JLabel loading = DesignClasses.create_imageJlabel(GetImages.PLEASE_WAIT_MINI);
        singleFriendPanel.add(loading);
        for (int i = 0; i <= 5; i++) {
            Map<String, SingleMemberInCircleDto> likes = NewsFeedMaps.getInstance().getLikes().get(statusid);
            //         System.out.println("likes.size==>" + likes.size());
            if (likes.size() >= (currentlikes - 1)) {
                singleFriendPanel.removeAll();
                for (String userid : likes.keySet()) {
                    SingleMemberInCircleDto fld = likes.get(userid);
                    JPanel dd = singleLike(fld);
                    singleFriendPanel.add(dd);
                }
                singleFriendPanel.revalidate();
                break;
            }
        }
    }

    private void hidethis() {
        this.setVisible(false);
    }

    public JPanel singleLike(final SingleMemberInCircleDto userid) {
        final JPanel rowPanel = new JPanel();
        rowPanel.setOpaque(false);
        rowPanel.setLayout(new BorderLayout(5, 0));
        rowPanel.setBorder(new EmptyBorder(0, 0, 5, 0));
        JLabel frienImageLabel = new JLabel();
        // frienImageLabel.setBorder(DefaultSettings.DEFAUTL_BORDER);
        rowPanel.setPreferredSize(new Dimension(150, size + 5));
        String url = "";
        final UserBasicInfo single_p = FriendList.getInstance().getFriend_hash_map().get(userid.getUserIdentity());
        if (single_p != null) {
            url = single_p.getProfileImage();
        } else if (userid.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
            url = MyFnFSettings.userProfile.getProfileImage();
        }

//        if (url.trim().length() > 0) {
//            BufferedImage img = DesignClasses.getBufferImageFromurlFriendList(url, size);
//            frienImageLabel.setIcon(new ImageIcon(img));
//        } else {
//            BufferedImage img = DesignClasses.getUnknownImage(true);
//            try {
//                img = DesignClasses.scaleImage(size, size, img);
//                frienImageLabel.setIcon(new ImageIcon(img));
//            } catch (Exception ex) {
//                Logger.getLogger(PeopleWhoLikesThisPost.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
//        if (url.trim().length() > 0) {
//            ImageHelpers.addProfileImageThumb(frienImageLabel, url);
//        } else {
//            BufferedImage img2 = ImageHelpers.getUnknownImage(true);//.getBufferImageFromurl(null);
//            frienImageLabel.setIcon(new ImageIcon(img2));
//            img2 = null;
//        }
        frienImageLabel.revalidate();
        rowPanel.add(frienImageLabel, BorderLayout.WEST);
        JLabel fullname = DesignClasses.makeAncorLabel(userid.getFullName(), 0, 12);
        String fullName = userid.getFullName();
        /*if (userid.getLastName() != null && userid.getLastName().length() > 0 )
        {
            fullName = userid.getFullName() + userid.getLastName();
        }*/
        
        fullname.setText(fullName);
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
                        if (GuiRingID.getInstance() != null) {
                            if (single_p != null) {

                                GuiRingID.getInstance().showFriendProfile(userid.getUserIdentity());
                                hidethis();
                            }
                            //  Gui24FnFNew.getInstance().resetAllDefaultActionButtons();

                        }
                        //    show_window_with_offline(userid);
                    }
                });
            }
        });
        rowPanel.add(fullname, BorderLayout.CENTER);
        //   mouseLiseraction(rowPanel, userid.getUserIdentity());
        return rowPanel;
    }
}
