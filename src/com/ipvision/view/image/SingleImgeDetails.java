/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DesignClasses;
import static com.ipvision.view.utility.DesignClasses.return_image;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.view.utility.FeedUtils;

/**
 *
 *
 * @author Faiz
 */
public class SingleImgeDetails extends BasicImageViewStructure implements ActionListener {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SingleImgeDetails.class);

    public NewsFeedWithMultipleImage statusDto;
    public JsonFields imageDTo;
    int selectedImageIndex;
    long nfID;
    private JButton leftBtn;
    private JButton rightBtn;
    UserBasicInfo basicinfo;
    private static SingleImgeDetails singleImgeDetails;

    public AddImageCaptionCommentsPanel addImageCaptionCommentsPanel;

    public static SingleImgeDetails getSingleImgeDetails() {
        return singleImgeDetails;
    }

    public static void setSingleImgeDetails(SingleImgeDetails singleImgeDetails) {
        SingleImgeDetails.singleImgeDetails = singleImgeDetails;
    }

    public SingleImgeDetails(long nfID, int selectedImageIndex) {

        this.selectedImageIndex = selectedImageIndex;
        this.nfID = nfID;
        setInitInfo();
        addBasicPanels();

//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowDeactivated(WindowEvent e) {
//                if (likesInImageCommentsPopup != null && likesInImageCommentsPopup.isDisplayable()) {
//                    return;
//                } else if (imageLikesPopup != null && imageLikesPopup.isDisplayable()) {
//                    return;
//                } else if (editDeleteImageCommentPopup != null && editDeleteImageCommentPopup.isDisplayable()) {
//                    return;
//                } else if (addEmoticonsJDialog != null && addEmoticonsJDialog.isDisplayable()) {
//                    return;
//                }
//                if (clieckedEditDeletepopup == false) {
//                    hideThis();
//                }
//            }
//        });
    }

    private void setInitInfo() {
        this.statusDto = NewsFeedMaps.getInstance().getAllNewsFeeds().get(this.nfID);
        this.imageDTo = this.statusDto.getImageList().get(selectedImageIndex);
        commentWidth = (windowWidth * 25) / 100;//n% of window width
        basicinfo = FeedUtils.setBasicUserInfoForFeedUser(basicinfo, statusDto);
        setSingleImgeDetails(this);
        try {
            if (this.statusDto.getTotalImage() != null && this.statusDto.getImageList() != null && this.statusDto.getImageList().size() < this.statusDto.getTotalImage()) {
                (new GetSingleStatusDetails(nfID)).start();
            }
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error in initContainers ==>" + e.getMessage());
        }
        getCloseButton().addActionListener(this);
    }

    private void addBasicPanels() {
        leftBtn = DesignClasses.createImageButton(GetImages.PREVIOUS, GetImages.PREVIOUS_H, "Previous");
        leftBtn.addActionListener(this);
        rightBtn = DesignClasses.createImageButton(GetImages.NEXT, GetImages.NEXT_H, "Next");
        rightBtn.addActionListener(this);
        if (this.statusDto.getImageList().size() > 1) {

            getLeftButtonPanel().add(leftBtn);
            getRightButtonPanel().add(rightBtn);
        }
    }

    public void setButtonVisibily(int status) {
        if (status == 1) {
            leftBtn.setVisible(true);
            rightBtn.setVisible(true);
        } else if (status == 2) {
            leftBtn.setVisible(false);
            rightBtn.setVisible(false);
        } else if (status == 3) {
            leftBtn.setVisible(false);
            rightBtn.setVisible(true);
        } else if (status == 4) {
            leftBtn.setVisible(true);
            rightBtn.setVisible(false);
        }
    }

    public synchronized void loadSelectedImage() {

        if (selectedImageIndex < this.statusDto.getImageList().size() && selectedImageIndex >= 0) {
            if (selectedImageIndex == 0) {
                setButtonVisibily(3);
            } else if (selectedImageIndex == statusDto.getImageList().size() - 1) {
                setButtonVisibily(4);
            } else if (this.statusDto.getImageList().size() > 1) {
                setButtonVisibily(1);
            }
            try {
                this.imageDTo = this.statusDto.getImageList().get(selectedImageIndex);
                //getImagePaneMain().addMouseListener(new ImageMousListenerInBook(imageDTo, false));
                getImagePaneMain().addMouseListener(mouseListener);
                new LoadImageInImagePane(imageDTo.getIurl(), getImagePaneMain(), imageDTo.getImT(), selectedImageIndex).start();
                addImageCaptionCommentsPanel = new AddImageCaptionCommentsPanel(this.imageDTo, getTimeLabel(), getLikePanel());
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        getCaptionAndCommentsPanel().removeAll();
                        getCaptionAndCommentsPanel().revalidate();
                        addImageCaptionCommentsPanel.buildStructure();
                        addImageCaptionCommentsPanel.initDefaults();
                        addImageCaptionCommentsPanel.addLikesDetails();
                        //addImageCaptionCommentsPanel.addOPtionsDetails();
                        getCaptionAndCommentsPanel().add(addImageCaptionCommentsPanel, BorderLayout.CENTER);
                        addImageCaptionCommentsPanel.getImageDetails();
                    }
                });

                // (new ImageCommentsAndLikesUpdate(addImageCaptionCommentsPanel, captionAndCommentsPanel)).start();
            } catch (Exception e) {
             //   e.printStackTrace();
             log.error("Error in loadSelectedImage ==>" + e.getMessage());
            }

        }
    }

    public void loadLoadInitialValues() {
        friendDetails();
        loadSelectedImage();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object rsrs = e.getSource();
        if (rsrs == getCloseButton()) {
            dispose();
            //  hideThis();
        } else if (rsrs == leftBtn) {
            if (this.statusDto.getImageList() != null && this.statusDto.getImageList().size() < this.statusDto.getTotalImage()) {
                (new GetSingleStatusDetails(nfID)).start();
            }
            if (selectedImageIndex > 0) {
                selectedImageIndex = selectedImageIndex - 1;
            }
            //System.out.println("statusDto.getImageList().size()==>" + statusDto.getImageList().size() + "==" + selectedImageIndex);
            loadSelectedImage();
        } else if (rsrs == rightBtn) {
            if (this.statusDto.getImageList() != null && this.statusDto.getImageList().size() < this.statusDto.getTotalImage()) {
                (new GetSingleStatusDetails(nfID)).start();
            }
            if (selectedImageIndex < statusDto.getImageList().size()) {
                selectedImageIndex = selectedImageIndex + 1;
            }
            loadSelectedImage();
        }
    }

    MouseAdapter mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                SaveImageInDisk saveImageInDis = new SaveImageInDisk(imageDTo, false);
                saveImageInDis.show(e.getComponent(), e.getPoint().x, e.getPoint().y);
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
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    };
    /*private void showPanels(boolean show) {
     getLikePanel().setVisible(show);
     }*/

    public void friendDetails() {

        try {
            try {
//                BufferedImage img = DesignClasses.getBufferImageFromurlFriendList(this.basicinfo.getProfileImage(), 38);
//                getFriend_image_panel().setIcon(new ImageIcon(img));
//                img.flush();
//                img = null;
                //  new ViewProfileImage(getFriend_image_panel(), this.basicinfo.getProfileImage(), 38).start();
                ImageHelpers.addProfileImageThumb(getFriend_image_panel(), this.basicinfo.getProfileImage(), 38);
            } catch (Exception ex) {
            }
            String fName = this.basicinfo.getFullName();
            /*if (this.basicinfo.getLastName() != null && this.basicinfo.getLastName().length() > 0) {
             fName = this.basicinfo.getFullName() + " " + this.basicinfo.getLastName();
             }*/
            getFirstLabel().setText(fName);
            getFirstLabel().setCursor(new Cursor(Cursor.HAND_CURSOR));
            FeedUtils.onClickFriendName(getFirstLabel(), this.basicinfo);
            if (this.statusDto.getCircleId() != null && this.statusDto.getCircleId() > 0) {
                getSeperatorLabel().setIcon(return_image(GetImages.BOOK_ARROW));
                getSecondLabel().setText(this.statusDto.getCircleName() != null ? this.statusDto.getCircleName() : "Circle");//, DefaultSettings.FULL_NAME_FONT_SIZE_IN_FEED);
                getSecondLabel().setCursor(new Cursor(Cursor.HAND_CURSOR));
                FeedUtils.onClickCircleName(getSecondLabel(), this.statusDto.getCircleId());
            } else if (this.statusDto.getFriendIdentity() != null && this.statusDto.getFriendIdentity().trim().length() > 0) {
                getSeperatorLabel().setIcon(return_image(GetImages.BOOK_ARROW));
                String name = this.statusDto.getFfn();
                /*if (this.statusDto.getFln() != null && this.statusDto.getFln().length() > 0) {
                 name = this.statusDto.getFfn() + " " + this.statusDto.getFln();
                 }*/
                getSecondLabel().setText(name);//, DefaultSettings.FULL_NAME_FONT_SIZE_IN_FEED);
                getSecondLabel().setCursor(new Cursor(Cursor.HAND_CURSOR));
                FeedUtils.onClickFriendName(getSecondLabel(), getUserInfo(this.statusDto));
            } else {
                getSeperatorLabel().setIcon(null);

            }

        } catch (Exception e) {
           // e.printStackTrace();
        log.error("Error in friendDetails ==>" + e.getMessage());
        } finally {
            //  userNameAndImagePanle.revalidate();
            System.gc();
            Runtime.getRuntime().gc();
        }

    }

    public UserBasicInfo getUserInfo(JsonFields js) {
        UserBasicInfo info = new UserBasicInfo();
        if (FriendList.getInstance().getFriend_hash_map().get(js.getFriendIdentity()) != null) {
            info = FriendList.getInstance().getFriend_hash_map().get(js.getFriendIdentity());
        } else if (js.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
            //   System.out.println("jsuser==>" + js.getUserIdentity());
            basicinfo = MyFnFSettings.userProfile;
        } else {
            if (js.getUserIdentity() != null) {
                info.setUserIdentity(js.getFriendIdentity());
                info.setFullName(js.getFriendIdentity());
            }
            if (js.getFullName() != null) {
                info.setFullName(js.getFfn());
            } else {
                info.setFullName(js.getFriendIdentity());
            }
            /*if (js.getLastName() != null) {
             info.setLastName(js.getFln());
             } else {
             info.setLastName("");
             }*/
        }

        return info;
    }

//    public void hideThis() {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                setVisible(false);
//                dispose();
//                if (addImageCaptionCommentsPanel != null) {
//                    addImageCaptionCommentsPanel.getCommentsOfthisImage().clear();
//                    addImageCaptionCommentsPanel.getImageComments().clear();
//                }
//                clieckedEditDeletepopup = false;
//                editDeleteImageCommentPopup = null;
//                imageLikesPopup = null;
//                likesInImageCommentsPopup = null;
//                addEmoticonsJDialog = null;
//                setSingleImgeDetails(null);
//                System.gc();
//                Runtime.getRuntime().gc();
//            }
//        }
//        );
//
//    }
}
