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
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.view.utility.FeedUtils;
import static com.ipvision.view.image.BasicImageViewStructure.commentWidth;

/**
 *
 * @author Faiz
 */
public class SingleImageDetailsForNotifications extends BasicImageViewStructure implements ActionListener {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SingleImageDetailsForNotifications.class);
    public JsonFields imageDTo;
    public long imageID;
    UserBasicInfo basicinfo = new UserBasicInfo();
    private static SingleImageDetailsForNotifications singleImgeDetails;

    public AddImageCaptionCommentsPanel addImageCaptionCommentsPanel;

    public static SingleImageDetailsForNotifications getSingleImgeDetails() {
        return singleImgeDetails;
    }

    public static void setSingleImgeDetails(SingleImageDetailsForNotifications singleImgeDetails) {
        SingleImageDetailsForNotifications.singleImgeDetails = singleImgeDetails;
    }

    public SingleImageDetailsForNotifications(long imageID) {
        this.imageID = imageID;
        setInitInfo();
    }

    public void addloader() {
        try {
            BufferedImage img = DesignClasses.return_buffer_image(GetImages.PLEASE_WAIT_MINI);
            getImagePaneMain().setImage(img);
            getImagePaneMain().revalidate();
            img.flush();
            img = null;
            (new GetImageDetails()).start();
        } catch (Exception ex) {
        }

    }

    private void setInitInfo() {
        commentWidth = (windowWidth * 25) / 100;//n% of window width
        setSingleImgeDetails(this);
        getCloseButton().addActionListener(this);
    }

    public synchronized void loadSelectedImage() {

        try {
            // this.imageDTo = this.statusDto.getImageList().get(selectedImageIndex);
            getImagePaneMain().addMouseListener(mouseListener);
            new LoadImageInImagePane(imageDTo.getIurl(), getImagePaneMain(), imageDTo.getImT(), -1).start();
            addImageCaptionCommentsPanel = new AddImageCaptionCommentsPanel(this.imageDTo, getTimeLabel(), getLikePanel());
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    getCaptionAndCommentsPanel().removeAll();
                    addImageCaptionCommentsPanel.buildStructure();
                    addImageCaptionCommentsPanel.initDefaults();
                    addImageCaptionCommentsPanel.addLikesDetails();
                    getCaptionAndCommentsPanel().add(addImageCaptionCommentsPanel, BorderLayout.CENTER);
                    addImageCaptionCommentsPanel.getImageDetails();
                    getCaptionAndCommentsPanel().revalidate();
                }
            });
        } catch (Exception e) {
            //  e.printStackTrace();
            log.error("Error in loadSelectedImage ==>" + e.getMessage());
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
        }
    }

    public void friendDetails() {

        try {
            try {
//                BufferedImage img = DesignClasses.getBufferImageFromurlFriendList(this.imageDTo.getProfileImage(), 38);
//                getFriend_image_panel().setIcon(new ImageIcon(img));
//                img.flush();
//                img = null;
                //  new ViewProfileImage(getFriend_image_panel(), this.basicinfo.getProfileImage(), 38).start();
                ImageHelpers.addProfileImageThumb(getFriend_image_panel(), this.basicinfo.getProfileImage(), 38);
            } catch (Exception ex) {
            }

            getFirstLabel().setText(this.imageDTo.getFullName() /*+ " " + this.imageDTo.getLastName()*/);
            getFirstLabel().setCursor(new Cursor(Cursor.HAND_CURSOR));
        } catch (Exception e) {
            //e.printStackTrace();
       log.error("Error in here ==>" + e.getMessage());
        } finally {
            //  userNameAndImagePanle.revalidate();
            System.gc();
            Runtime.getRuntime().gc();
        }

    }

    public UserBasicInfo getUserInfo(JsonFields imageDto) {
        if (imageDto.getUserIdentity() == null) {
            basicinfo = MyFnFSettings.userProfile;
        } else if (imageDto.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
            basicinfo = MyFnFSettings.userProfile;
        } else {
            if (imageDto.getUserIdentity() != null && FriendList.getInstance() != null && !FriendList.getInstance().getFriend_hash_map().isEmpty()) {

                if (FriendList.getInstance().getFriend_hash_map().get(imageDto.getUserIdentity()) != null) {
                    basicinfo = FriendList.getInstance().getFriend_hash_map().get(imageDto.getUserIdentity());
                } else {
                    basicinfo = new UserBasicInfo();
                    if (imageDto.getUserIdentity() != null) {
                        basicinfo.setUserIdentity(imageDto.getUserIdentity());
                        basicinfo.setFullName(imageDto.getUserIdentity());
                    }
                    if (imageDto.getFullName() != null) {
                        basicinfo.setFullName(imageDto.getFullName());
                    } else {
                        basicinfo.setFullName(imageDto.getUserIdentity());
                    }
                    /*if (statusDto.getLastName() != null) {
                     basicinfo.setLastName(statusDto.getLastName());
                     } else {
                     basicinfo.setLastName("");
                     }*/
                }
            } else {
                basicinfo = new UserBasicInfo();
                if (imageDto.getUserIdentity() != null) {
                    basicinfo.setUserIdentity(imageDto.getUserIdentity());
                    basicinfo.setFullName(imageDto.getUserIdentity());
                }
                if (imageDto.getFullName() != null) {
                    basicinfo.setFullName(imageDto.getFullName());
                } else {
                    basicinfo.setFullName(imageDto.getUserIdentity());
                }
                /*if (statusDto.getLastName() != null) {
                 basicinfo.setLastName(statusDto.getLastName());
                 } else {
                 basicinfo.setLastName("");
                 }*/

            }

        }
        return basicinfo;
    }

    class GetImageDetails extends Thread {

        @Override
        public void run() {

            try {

                JsonFields pakToSend = new JsonFields();
                String pakId = SendToServer.getRanDomPacketID();
                pakToSend.setPacketId(pakId);
                pakToSend.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                pakToSend.setImageId(imageID);
                pakToSend.setAction(AppConstants.TYPE_IMAGE_DETAILS);

                SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.REQUEST_PORT);
                Thread.sleep(500);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        /*{"imgId":9438,"iurl":"http://38.127.68.50/imageServer/clients_image/ring8801913367007/coverimage/1417297390048.jpg","albId":"coverimage","albn":"CoverImages","ih":267,"iw":300,"imT":3,"tm":1417318847525,"nl":0,"il":0,"ic":1,"nc":1,"pckId":"ring8801728119927151417339235612","sucs":true,"actn":121,"uId":"ring8801913367007","fn":"Anwar001","ln":"Reefat000"}*/
                        FeedBackFields response = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        if (response.getSuccess()) {
                            imageDTo = HelperMethods.getJsonFields(response.getMessage());
                            basicinfo = getUserInfo(imageDTo);
                            loadLoadInitialValues();
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
//                setSingleImgeDetails(null);
//                System.gc();
//                Runtime.getRuntime().gc();
//            }
//        }
//        );
//
//    }
}
