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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import com.ipvision.service.AlbumsImages;
import com.ipvision.service.SendFriendsInfoRequest;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.SendToServer;
import static com.ipvision.view.image.BasicImageViewStructure.commentWidth;

/**
 *
 * @author Faiz
 */
public class SingleImageDetailsProfileCover extends BasicImageViewStructure implements ActionListener {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SingleImageDetailsProfileCover.class);
    public JsonFields imageDTo;
    public long imageID;
    UserBasicInfo basicinfo = new UserBasicInfo();
    private static SingleImageDetailsProfileCover singleImgeDetails;
    private JButton leftBtn;
    private JButton rightBtn;
    public int imageType;
    private String userId;
    public AddImageCaptionCommentsPanel addImageCaptionCommentsPanel;

    int selectedImageIndex = -1;
    ArrayList<JsonFields> list;
    Map<String, JsonFields> map = new HashMap<String, JsonFields>();

    public static SingleImageDetailsProfileCover getSingleImgeDetails() {
        return singleImgeDetails;
    }

    public static void setSingleImgeDetails(SingleImageDetailsProfileCover singleImgeDetails) {
        SingleImageDetailsProfileCover.singleImgeDetails = singleImgeDetails;
    }

    public SingleImageDetailsProfileCover(JsonFields imageDTo, int type, String userId) {
        this.imageDTo = imageDTo;
        this.imageID = imageDTo.getImageId();
        this.imageType = type;
        this.userId = userId;
        setInitInfo();
        addBasicPanels();
    }

    public void addloader() {
        try {
            BufferedImage img = DesignClasses.return_buffer_image(GetImages.PLEASE_WAIT_MINI);
            getImagePaneMain().setImage(img);
            getImagePaneMain().revalidate();
            img.flush();
            img = null;
            (new GetImageDetails()).start();

            if (userId.equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                if (MyfnfHashMaps.getInstance().getAlBumImages() != null
                        && MyfnfHashMaps.getInstance().getAlBumImages().get((imageType == MyFnFSettings.PROFILE_IMAGE) ? MyFnFSettings.PROFILE_IMAGE_ALBUM_ID : MyFnFSettings.COVER_IMAGE_ALBUM_ID) != null
                        && MyfnfHashMaps.getInstance().getAlBumImages().get((imageType == MyFnFSettings.PROFILE_IMAGE) ? MyFnFSettings.PROFILE_IMAGE_ALBUM_ID : MyFnFSettings.COVER_IMAGE_ALBUM_ID).get(String.valueOf(imageID)) != null) {
                    loadMyAllImagesFromServer(MyfnfHashMaps.getInstance().getAlBumImages().get((imageType == MyFnFSettings.PROFILE_IMAGE) ? MyFnFSettings.PROFILE_IMAGE_ALBUM_ID : MyFnFSettings.COVER_IMAGE_ALBUM_ID).size());
                } else {
                    loadMyAllImagesFromServer(0);
                }
            } else {
                if (NewsFeedMaps.getInstance().getFriendsAlbumImages().get(userId) != null
                        && NewsFeedMaps.getInstance().getFriendsAlbumImages().get(userId).get((imageType == MyFnFSettings.PROFILE_IMAGE) ? MyFnFSettings.PROFILE_IMAGE_ALBUM_ID : MyFnFSettings.COVER_IMAGE_ALBUM_ID) != null
                        && NewsFeedMaps.getInstance().getFriendsAlbumImages().get(userId).get((imageType == MyFnFSettings.PROFILE_IMAGE) ? MyFnFSettings.PROFILE_IMAGE_ALBUM_ID : MyFnFSettings.COVER_IMAGE_ALBUM_ID).get(String.valueOf(imageID)) != null) {
                    loadFriendAllImagesFromServer(NewsFeedMaps.getInstance().getFriendsAlbumImages().get(userId).get((imageType == MyFnFSettings.PROFILE_IMAGE) ? MyFnFSettings.PROFILE_IMAGE_ALBUM_ID : MyFnFSettings.COVER_IMAGE_ALBUM_ID).size());
                } else {
                    loadFriendAllImagesFromServer(0);
                }
            }

        } catch (Exception ex) {
        }

    }

    public void loadMyAllImagesFromServer(int size) {
        new AlbumsImages((imageType == MyFnFSettings.PROFILE_IMAGE) ? MyFnFSettings.PROFILE_IMAGE_ALBUM_ID : MyFnFSettings.COVER_IMAGE_ALBUM_ID, size).start();
    }

    public void loadFriendAllImagesFromServer(int size) {
        new SendFriendsInfoRequest(userId, AppConstants.TYPE_FRIEND_ALBUM_IMAGES, size, (imageType == MyFnFSettings.PROFILE_IMAGE) ? MyFnFSettings.PROFILE_IMAGE_ALBUM_ID : MyFnFSettings.COVER_IMAGE_ALBUM_ID).start();

    }

    private void setInitInfo() {
        commentWidth = (windowWidth * 25) / 100;//n% of window width
        setSingleImgeDetails(this);
        getCloseButton().addActionListener(this);
    }

    private void addBasicPanels() {
        leftBtn = DesignClasses.createImageButton(GetImages.PREVIOUS, GetImages.PREVIOUS_H, "Previous");
        leftBtn.addActionListener(this);
        rightBtn = DesignClasses.createImageButton(GetImages.NEXT, GetImages.NEXT_H, "Next");
        rightBtn.addActionListener(this);

        getLeftButtonPanel().add(leftBtn);
        getRightButtonPanel().add(rightBtn);
        setButtonVisibility(2);

    }

    private void setButtonVisibility(int status) {
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

    private void setButtonVisibilityCondition() {
        if (list != null && list.size() > 0 && selectedImageIndex >= 0) {
            this.imageDTo = list.get(selectedImageIndex);
            this.imageID = imageDTo.getImageId();
            if (selectedImageIndex == 0) {
                setButtonVisibility(3);
            } else if (selectedImageIndex == list.size() - 1) {
                setButtonVisibility(4);
            } else if (list.size() > 1) {
                setButtonVisibility(1);
            }
        }
    }

    public void setPreviousNextImages() {
        list = new ArrayList<JsonFields>();
        if (userId.equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
            map = MyfnfHashMaps.getInstance().getAlBumImages().get((imageType == MyFnFSettings.PROFILE_IMAGE) ? MyFnFSettings.PROFILE_IMAGE_ALBUM_ID : MyFnFSettings.COVER_IMAGE_ALBUM_ID);
        } else {
            map = NewsFeedMaps.getInstance().getFriendsAlbumImages().get(userId).get((imageType == MyFnFSettings.PROFILE_IMAGE) ? MyFnFSettings.PROFILE_IMAGE_ALBUM_ID : MyFnFSettings.COVER_IMAGE_ALBUM_ID);
        }
        for (Map.Entry<String, JsonFields> entity : map.entrySet()) {
            JsonFields js = entity.getValue();
            list.add(js);
        }

        Collections.sort(list, new Comparator<JsonFields>() {
            @Override
            public int compare(JsonFields one, JsonFields other) {
                Long onesTime = one.getTm();
                Long othersTime = other.getTm();
                return onesTime.compareTo(othersTime);
            }
        });
        int i = 0;
        for (JsonFields entry : list) {
            if (entry.getImageId() == imageID) {
                selectedImageIndex = i;
                // System.out.println("selectedImageIndex-->" + selectedImageIndex);
                break;
            }
            i++;
        }
        if (selectedImageIndex < list.size() && selectedImageIndex >= 0) {
            if (selectedImageIndex == 0 && list.size() > 1) {
                setButtonVisibility(3); // left false, right on
            }
            if (selectedImageIndex == list.size() - 1 && list.size() > 1) {
                setButtonVisibility(4); //left true, right false
            } else if (list.size() > 1) {
                setButtonVisibility(1); //2 tai true
            } else {
                setButtonVisibility(2); //2 tai false
            }
        }
        //System.out.println("map.size()-->" + map.size());
        if (userId.equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
            loadMyAllImagesFromServer(map.size());
        } else {
            loadFriendAllImagesFromServer(map.size());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object rsrs = e.getSource();
        if (rsrs == getCloseButton()) {
            dispose();
        } else if (rsrs == leftBtn) {
            if (selectedImageIndex > 0) {
                selectedImageIndex = selectedImageIndex - 1;
            }
            setButtonVisibilityCondition();
            loadSelectedImage();
        } else if (rsrs == rightBtn) {

            if (selectedImageIndex < list.size()) {
                selectedImageIndex = selectedImageIndex + 1;
            }
            setButtonVisibilityCondition();
            loadSelectedImage();
        }
    }

    private synchronized void loadSelectedImage() {

        try {
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

    public void friendDetails() {

        try {
            try {
                ImageHelpers.addProfileImageThumb(getFriend_image_panel(), this.basicinfo.getProfileImage(), 38);
            } catch (Exception ex) {
            }

            getFirstLabel().setText(this.imageDTo.getFullName() /*+ " " + this.imageDTo.getLastName()*/);
            getFirstLabel().setCursor(new Cursor(Cursor.HAND_CURSOR));
        } catch (Exception e) {
            //e.printStackTrace();
          log.error("Error in friendDetails ==>" + e.getMessage());
        } finally {
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
                    Thread.sleep(1000);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(pakToSend, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        /*{"imgId":9438,"iurl":"http://38.127.68.50/imageServer/clients_image/ring8801913367007/coverimage/1417297390048.jpg","albId":"coverimage","albn":"CoverImages","ih":267,"iw":300,"imT":3,"tm":1417318847525,"nl":0,"il":0,"ic":1,"nc":1,"pckId":"ring8801728119927151417339235612","sucs":true,"actn":121,"uId":"ring8801913367007","fn":"Anwar001","ln":"Reefat000"}*/
                        FeedBackFields response = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId);
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        if (response != null && response.getSuccess()) {
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
}
