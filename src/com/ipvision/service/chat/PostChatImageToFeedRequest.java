/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.chat;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.model.constants.NotificationMessages;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import org.apache.log4j.Logger;
import com.ipvision.view.feed.PostChatImageInNewsFeed;
import com.ipvision.view.feed.SingleChatImageForFeedPost;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UploadChatImage;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.service.utility.InternetUnavailablityCheck;
import com.ipvision.view.utility.ImageInformation;
import com.ipvision.view.utility.ImageUtil;

/**
 *
 * @author Sirat Samyoun
 */
public class PostChatImageToFeedRequest extends Thread {

    org.apache.log4j.Logger log = Logger.getLogger(PostChatImageToFeedRequest.class);
    short TYPE_SINGLE_IMAGE_STATUS = 1;
    String status;
    String lineEnd = ImageUtil.lineEnd;
    String twoHyphens = ImageUtil.twoHyphens;
    String boundary = ImageUtil.boundary;
    JLabel loadingOrtimePanel;
    private JButton loginbutton;
    private JTextArea textOnly;
    private String default_text;
    private int validity;
    private UploadChatImage uploadChatImage;
    private static PostChatImageInNewsFeed instance;

    public PostChatImageToFeedRequest(
            PostChatImageInNewsFeed instance,
            UploadChatImage uploadChatImage,
            JTextArea textonly,
            String default_text,
            JLabel loadingOrtimePanel,
            JButton postbutton,
            String status,
            int validity) {
        this.instance = instance;
        this.uploadChatImage = uploadChatImage;
        this.loadingOrtimePanel = loadingOrtimePanel;
        this.loginbutton = postbutton;
        this.textOnly = textonly;
        this.default_text = default_text;
        this.status = status;
        this.validity = validity;
        setName(this.getClass().getSimpleName());
    }

    public void send_multiple_images_post_request(ArrayList<Long> imgIds, short type) {
        JsonFields js = new JsonFields();
        String packId = SendToServer.getRanDomPacketID();
        js.setPacketId(packId);
        js.setImageIds(imgIds);
        js.setAction(AppConstants.TYPE_MULTIPLE_IMAGE_POST);
        js.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
        js.setType(type);
        js.setPst(1);
        js.setStatus(status);
        js.setValidity(validity);

        SendToServer.sendPacketAsString(js, ServerAndPortSettings.UPDATE_PORT);
        for (int i = 1; i <= 5; i++) {
            try {
                Thread.sleep(500);
            } catch (Exception ex) {
            }
            if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(js.getPacketId()) == null) {
                SendToServer.sendPacketAsString(js, ServerAndPortSettings.UPDATE_PORT);
            } else {
                FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(js.getPacketId());
                if (fields.getSuccess()) {
                    instance.setVisible(false);
                    MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(js.getPacketId());
                    return;
                }
            }
        }
    }

    @Override
    public void run() {
        if (MyFnFSettings.LOGIN_SESSIONID != null && MyFnFSettings.LOGIN_SESSIONID.length() > 0 && InternetUnavailablityCheck.isInternetAvailable) {
            try {
                this.loginbutton.setEnabled(false);
                ArrayList<Long> imgIds = new ArrayList<Long>();
                boolean newImageUpload = false;
                SingleChatImageForFeedPost singleChatImageForFeedPost = uploadChatImage.getThumbnailPanel();
                File fileToUPload = singleChatImageForFeedPost.file;
                Long imageId = uploadChatImage.getImageId();
                // String caption = "";
                String caption = uploadChatImage.getThumbnailPanel() != null ? uploadChatImage.getThumbnailPanel().getCaption() : "";

                if (uploadChatImage.getThumbnailPanel() != null) {
                    if (uploadChatImage.getThumbnailPanel().getCaption().trim().equals("Add a Caption")) {
                        uploadChatImage.getThumbnailPanel().getCaption();
                    }
                }
                if (imageId != null && imageId.intValue() > 0) {
                    imgIds.add(imageId.longValue());
                } else {
                    FileInputStream fileInputStream = null;
                    HttpURLConnection conn = null;
                    DataOutputStream dos = null;
                    URL url = null;
                    try {
                        fileInputStream = new FileInputStream(fileToUPload);
                        BufferedImage originalImage = ImageIO.read(fileInputStream);
                        String extension = HelperMethods.getExtension(fileToUPload);//"jpeg";
                        try {
                            ImageInformation info = ImageInformation.readImageFileInformation(fileToUPload);
                            if (info.orientation > 1) {
                                AffineTransform transform = ImageInformation.getExifTransformation(info);
                                originalImage = ImageInformation.transformImage(originalImage, transform);
                            }
                        } catch (Exception e) {
                            //  e.printStackTrace();
                            log.error("Error in here ==>" + e.getMessage());
                        }
                        byte[] imgArr = ImageUtil.reduceQualityAndSize(originalImage, DefaultSettings.MAXIMUM_IMAGE_WIDTH, extension);
                        url = new URL(ServerAndPortSettings.getAlbumImageUploadingURL());
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setUseCaches(false);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                        //   conn.setReadTimeout(60000);;
                        dos = new DataOutputStream(conn.getOutputStream());
                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        ImageUtil.addDefaultDataWithImage(dos);
                        dos.writeBytes("Content-Disposition: form-data; name=\"pst\"" + lineEnd + lineEnd + 0 + lineEnd);
                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"cptn\"" + lineEnd + lineEnd + HelperMethods.getUrlEncode(caption) + lineEnd);
                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"vldt\"" + lineEnd + lineEnd + validity + lineEnd);
                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"uploadedFile\";filename=\"" + fileToUPload.getName() + "\"" + lineEnd);
                        dos.writeBytes(lineEnd);
                        dos.write(imgArr);
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                        BufferedReader br;
                        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String str;
                        JsonFields statusChange = null;
                        while ((str = br.readLine()) != null) {
                            if (str != null) {
                                //   System.out.println("sendImagePost==>" + str);
                                statusChange = HelperMethods.getNewsFeedWithMultipleImageDto(str);
                            }
                        }
                        if (statusChange != null) {
                            if (statusChange.getSuccess() != null && statusChange.getSuccess()) {
                                imgIds.add(statusChange.getImageId());
                                newImageUpload = true;
                            }
                        }
                        br.close();
                    } catch (Exception ex) {
                        // ex.printStackTrace();
                        log.error("Error in here ==>" + ex.getMessage());
                    } finally {
                        try {
                            if (fileInputStream != null) {
                                fileInputStream.close();
                            }
                            if (dos != null) {
                                dos.flush();
                                dos.close();
                            }
                            if (conn != null) {
                                conn.disconnect();
                            }
                        } catch (Exception e) {
                        }
                    }
                }
                if (imgIds.size() > 0) {
                    short type = TYPE_SINGLE_IMAGE_STATUS;
                    send_multiple_images_post_request(imgIds, type);
                    this.loadingOrtimePanel.setIcon(null);
                } else {
                    this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                }
                textOnly.setText(default_text);
                textOnly.setForeground(DefaultSettings.disable_font_color);
                this.loginbutton.setEnabled(true);
            } catch (Exception ex) {
                //  ex.printStackTrace();
                log.error("Error in here ==>" + ex.getMessage());
            }
        } else {
            HelperMethods.showWarningDialogMessage(NotificationMessages.INTERNET_UNAVAILABLE);
        }
    }
}
