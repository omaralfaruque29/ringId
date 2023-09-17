/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.uploaddownload;

import com.ipvision.service.utility.HelperMethods;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.imageio.ImageIO;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.friendlist.AllFriendList;
import com.ipvision.view.friendprofile.MyFriendChatCallPanel;
import com.ipvision.view.friendprofile.MyFriendProfile;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.view.utility.ImageHelpers;

/**
 *
 * @author Shahadat Hossain
 */
public class ImageDownloader extends Thread {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ImageDownloader.class);
    private String imageUrl;
    private String userIdentity;
    private int imageType;
    private DownLoaderHelps dHelp = new DownLoaderHelps();
    public static final int FRIEND_PROFILE_IMAGE = 1;
    public static final int FRIEND_COVER_IMAGE = 2;
    public static final int MY_PROFILE_IMAGE = 3;
    public static final int MY_COVER_IMAGE = 4;

    public ImageDownloader(String imageUrl, String userIdentity, int imageType) {
        this.imageUrl = imageUrl;
        this.userIdentity = userIdentity;
        this.imageType = imageType;
    }

    @Override
    public void run() {
        if (imageType == MY_PROFILE_IMAGE || imageType == FRIEND_PROFILE_IMAGE) {
            imageUrl = ImageHelpers.getThumbUrl(imageUrl);
        }

        if (imageUrl != null && imageUrl.trim().length() > 0) {
            String imageName = ImageHelpers.getImageNameFromUrl(imageUrl);
            String destination = ((imageType == FRIEND_PROFILE_IMAGE || imageType == MY_PROFILE_IMAGE) ? dHelp.getProfileDestinationFolder() : dHelp.getCoverDestinationFolder()) + imageName;
            if (HelperMethods.check_string_contains_substring(imageName, ".")) {
               // String temp = MyfnfHashMaps.getInstance().getBufferedImages().get(imageUrl);

                // if (temp == null) {
                //  MyfnfHashMaps.getInstance().getBufferedImages().put(imageUrl, imageName);
                boolean isFileExist = isFileExistInDirectory(destination);

                if (!isFileExist) {
                    try {
                        //log.warn("Downloading :: " + userIdentity + ", " + imageName + ", " + imageUrl + ", " + imageType);
                        URL url = new URL(imageUrl);
                        URLConnection yc = url.openConnection();
                        InputStream is = yc.getInputStream();
                        BufferedImage bimg = ImageIO.read(is);
                        File outputfile = new File(destination);
                        ImageIO.write(bimg, getExtension(imageName), outputfile);
                        is.close();
                    } catch (Exception ex) {
                        try {
                            URL url = new URL(imageUrl);
                            URLConnection yc = url.openConnection();
                            InputStream is = yc.getInputStream();
                            FileOutputStream fos = new FileOutputStream(destination);
                            int size = 1024;
                            byte[] buffer = new byte[size];
                            int bytesRead = 0;
                            while ((bytesRead = is.read(buffer)) != -1) {
                                fos.write(buffer, 0, bytesRead);
                            }
                            fos.close();
                            is.close();
                        } catch (Exception e) {
                        }
                    }
                }

                if (imageType == FRIEND_PROFILE_IMAGE) {
                    AllFriendList.changeFriendProfileImage(userIdentity);
                    MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(userIdentity);
                    if (friendProfile != null) {
                        friendProfile.setFriendProfileInfo(userIdentity);
                        friendProfile.buildFrinedImage();
                    }

                    MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(userIdentity);
                    if (friendChatCallPanel != null) {
                        friendChatCallPanel.setFriendProfileInfo(userIdentity);
                        friendChatCallPanel.refreshMyFriendChatCallPanelImage();
                    }
                } else if (imageType == MY_PROFILE_IMAGE || imageType == MY_COVER_IMAGE) {
                    if (GuiRingID.getInstance() != null
                            && GuiRingID.getInstance().getMainRight() != null
                            && GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null) {
                        GuiRingID.getInstance().getMainRight().getMyProfilePanel().changeMyImage();
                    }
                } else if (imageType == FRIEND_COVER_IMAGE) {
                    MyFriendProfile friendProfile = MyfnfHashMaps.getInstance().getMyFriendProfiles().get(userIdentity);
                    if (friendProfile != null) {
                        friendProfile.setFriendProfileInfo(userIdentity);
                        friendProfile.buildFrinedImage();
                    }

                    MyFriendChatCallPanel friendChatCallPanel = MyfnfHashMaps.getInstance().getMyFriendChatCallPanel().get(userIdentity);
                    if (friendChatCallPanel != null) {
                        friendChatCallPanel.setFriendProfileInfo(userIdentity);
                        friendChatCallPanel.refreshMyFriendChatCallPanelImage();
                    }
                }
            }
        }
    }

    public static String getExtension(String imageName) {
        if (imageName.contains(".")) {
            imageName = imageName.substring(imageName.lastIndexOf('.') + 1);
        }
        return imageName;
    }

    public static boolean isFileExistInDirectory(String directory) {
        File f = new File(directory);
        if (f.exists() && !f.isDirectory()) {
            return true;
        }
        return false;
    }
}
