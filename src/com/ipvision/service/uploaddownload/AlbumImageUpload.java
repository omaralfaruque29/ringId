/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.uploaddownload;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
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
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.view.myprofile.AlbumsHome;
import com.ipvision.view.feed.SingleAlbumNameWithCoverImage;
import com.ipvision.view.feed.SingleImagePreviewPanel;
import com.ipvision.model.FeedBackFields;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.SendToServer;
import com.ipvision.view.utility.ImageUtil;
import com.ipvision.service.image.SaveBufferImageInLoacalFolder;
import com.ipvision.view.utility.ImageInformation;

/**
 *
 * @author Wasif Islam
 */
public class AlbumImageUpload extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AlbumImageUpload.class);
    String albn;  // album name
    String albId;  // album Id
    private String sts; //status
    private HttpURLConnection conn = null;
    private DataOutputStream dos = null;
    String lineEnd = ImageUtil.lineEnd;
    String twoHyphens = ImageUtil.twoHyphens;
    String boundary = ImageUtil.boundary;
    private FileInputStream fileInputStream;
    private URL url;
    JLabel loadingOrtimePanel;
    JButton postButton;
    int pst;
    int type; //newsfeed type --> post=3, album=4
    private CreateAlbumPanel crtAlbm;
    private ArrayList<Long> imgIds;
    private ConcurrentHashMap<String, SingleImagePreviewPanel> imageContainerMap;
    private boolean isNewAlbum;

    AlbumImageUpload(String album_id, String album_name, ConcurrentHashMap file_to_upload, int post_type, int newsfeed_type, JButton post_button, JLabel loading_or_timePanel, CreateAlbumPanel crtAlbm, boolean isNewAlbum) {
        this.albId = album_id;
        this.albn = album_name;
        this.imageContainerMap = file_to_upload;
        this.pst = post_type;
        this.type = newsfeed_type;
        this.postButton = post_button;
        this.loadingOrtimePanel = loading_or_timePanel;
        this.crtAlbm = crtAlbm;
        this.imgIds = new ArrayList<Long>();
        this.isNewAlbum = isNewAlbum;
    }

    public void send_multiple_images_post_request() {
        JsonFields js = new JsonFields();
        /* actn: 117
         pckId:
         albn:
         albId:
         type: 4
         pst: 1
         imgIds: (image id array (comma separate))
         sId*/
        js.setAction(AppConstants.TYPE_MULTIPLE_IMAGE_POST);
        String packId = SendToServer.getRanDomPacketID();
        js.setPacketId(packId);
        js.setAlbn(this.albn);
        js.setAlbId(this.albId);
        js.setType((short) this.type);
        js.setPst(1);
        js.setImageIds(this.imgIds);
        js.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
        js.setStatus(this.sts);

        SendToServer.sendPacketAsString(js, ServerAndPortSettings.UPDATE_PORT);
        for (int i = 1; i <= 5; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                // ex.printStackTrace();
                log.error("Exception in here ==>" + ex.getMessage());
            }
            if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(js.getPacketId()) == null) {
                SendToServer.sendPacketAsString(js, ServerAndPortSettings.UPDATE_PORT);
            } else {
                FeedBackFields fields = MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(js.getPacketId());
                if (fields.getSuccess()) {
                    crtAlbm.dispose_create_album();

                    if (GuiRingID.getInstance() != null
                            && GuiRingID.getInstance().getMainRight() != null
                            && GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null
                            && GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome() != null
                            && isNewAlbum
                            && GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().getAlbums() != null) {
                        GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().getAlbums().addNewMyAlbumsFolder(this.albId);
                    } else {
                       // GuiRingID.getInstance().getMainRight().getMyProfilePanel().getAlbumsHome().refreshMyAlbumsFolder();
                    }

                    JsonFields albumImgs = NewsFeedMaps.getInstance().getMyAllbums().get(this.albId);
                    SingleAlbumNameWithCoverImage album = new SingleAlbumNameWithCoverImage(albumImgs);
                    album.addImagesInDetailsView();

                    MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(js.getPacketId());
                    break;
                }
            }

        }
    }

    @Override
    public void run() {
        try {
            this.postButton.setEnabled(false);
            for (Entry<String, SingleImagePreviewPanel> entry : imageContainerMap.entrySet()) {
                String key = entry.getKey();
                SingleImagePreviewPanel previewPanel = entry.getValue();
                File fileToUpload = previewPanel.file;

                String extension = HelperMethods.getExtension(fileToUpload);
                fileInputStream = new FileInputStream(fileToUpload);
                BufferedImage originalImage = ImageIO.read(fileInputStream);
                try {
                    ImageInformation info = ImageInformation.readImageFileInformation(fileToUpload);
                    if (info.orientation > 1) {
                        AffineTransform transform = ImageInformation.getExifTransformation(info);
                        originalImage = ImageInformation.transformImage(originalImage, transform);
                    }
                } catch (Exception e) {
                    // e.printStackTrace();
                    log.error("Exception in transforming image ==>" + e.getMessage());
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
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                ImageUtil.addDefaultDataWithImage(dos);
                dos.writeBytes("Content-Disposition: form-data; name=\"albn\"" + lineEnd + lineEnd + this.albn + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"albId\"" + lineEnd + lineEnd + this.albId + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"pst\"" + lineEnd + lineEnd + this.pst + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"cptn\"" + lineEnd + lineEnd + HelperMethods.getUrlEncode(previewPanel.getCaption()) + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"imT\"" + lineEnd + lineEnd + 1 + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploadedFile\";filename=\"" + fileToUpload.getName() + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.write(imgArr);
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                if (conn != null) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String str;
                    NewsFeedWithMultipleImage statusChange = null;
                    while ((str = br.readLine()) != null) {
                        if (str != null) {
                            System.out.println("STR--->" + str);
                            statusChange = HelperMethods.getNewsFeedWithMultipleImageDto(str);
                        }
                    }
                    if (statusChange != null) {
                        if (statusChange.getSuccess() != null && statusChange.getSuccess()) {
                            crtAlbm.removeSelectedImageBlock(key);
                            this.imgIds.add(statusChange.getImageId());
                            if (statusChange.getAlbId() != null && statusChange.getAlbId().length() > 0 && NewsFeedMaps.getInstance().getMyAllbums().get(statusChange.getAlbId()) == null && isNewAlbum) {
                                JsonFields newAlbum = new JsonFields();
                                newAlbum.setAlbId(statusChange.getAlbId());
                                newAlbum.setAlbn(albn);
                                newAlbum.setCvImg(statusChange.getIurl());
                                newAlbum.setUt(statusChange.getTm());
                                newAlbum.setTn(1);
                                NewsFeedMaps.getInstance().getMyAllbums().put(statusChange.getAlbId(), newAlbum);
                            } else if (!isNewAlbum) {
                                JsonFields oldAlbum = NewsFeedMaps.getInstance().getMyAllbums().get(statusChange.getAlbId());
                                oldAlbum.setTn(oldAlbum.getTn() + 1);
                                NewsFeedMaps.getInstance().getMyAllbums().put(statusChange.getAlbId(), oldAlbum);
                            } else if (isNewAlbum) {
                                JsonFields newAlbum = NewsFeedMaps.getInstance().getMyAllbums().get(statusChange.getAlbId());
                                newAlbum.setTn(newAlbum.getTn() + 1);
                                NewsFeedMaps.getInstance().getMyAllbums().put(statusChange.getAlbId(), newAlbum);
                            }

                            if (MyfnfHashMaps.getInstance().getAlBumImages().get(statusChange.getAlbId()) == null) {
                                Map<String, JsonFields> imgJson = new ConcurrentHashMap<String, JsonFields>();
                                imgJson.put(statusChange.getIurl(), statusChange);
                                MyfnfHashMaps.getInstance().getAlBumImages().put(statusChange.getAlbId(), imgJson);
                            } else {
                                MyfnfHashMaps.getInstance().getAlBumImages().get(statusChange.getAlbId()).put(statusChange.getIurl(), statusChange);
                            }

                            if (statusChange.getIurl() != null) {
                                (new SaveBufferImageInLoacalFolder(MyFnFSettings.TEMP_BOOK_IMAGE_FOLDER, originalImage, statusChange.getIurl())).start();
                            }

                        } else {
                            this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                        }
                    } else {
                        this.loadingOrtimePanel.setIcon(DesignClasses.return_image(GetImages.FAILED_MINI));
                    }
                    br.close();
                }
            }
            crtAlbm.new_album_name.setEditable(true);
            crtAlbm.importPhotoBtn.setEnabled(true);
            crtAlbm.cameraBtn.setEnabled(true);
            this.postButton.setEnabled(true);
            crtAlbm.repaintImageContentPanel();
            send_multiple_images_post_request();

        } catch (Exception ex) {
           // ex.printStackTrace();
            log.error("Exception in here ==>" + ex.getMessage());
            this.loadingOrtimePanel.setIcon(null);
        } finally {
            try {
                this.postButton.setEnabled(true);
                fileInputStream.close();
                dos.flush();
                dos.close();
                conn.disconnect();
            } catch (Exception e) {
            }

        }
    }
}
