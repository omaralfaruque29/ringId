/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.uploaddownload;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.service.utility.HelperMethods;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.ipvision.model.JsonFields;
import com.ipvision.model.constants.NotificationMessages;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import com.ipvision.view.GuiRingID;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.view.utility.ImageUtil;
//import myfnfui.packetsInfo.PackageActions;

/**
 *
 * @author faizahmed
 */
public class ImageUploader {

    FileInputStream fileInputStream;

    public void uploader_method_profile_image(String image_path, BufferedImage originalImage, int cropX, int cropY, int cropW, int cropH, int post, JButton btnSkip) {

        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null && GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null) {
            try {
                GuiRingID.getInstance().getMainRight().getMyProfilePanel().setImageUploadingLoader(true);
            } catch (Exception e) {
            }
        }
        HttpURLConnection conn = null;
        DataOutputStream dos;
        DataInputStream inStream;
        String exsistingFileName = "pcupload";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        DownLoaderHelps dHelps = new DownLoaderHelps();
        String urlString = ServerAndPortSettings.getProfileImageUploadingURL();
        String extension = image_path.substring(image_path.lastIndexOf(".") + 1);
        exsistingFileName = exsistingFileName + "." + extension;

        try {
            /*File fileToUPload = new File(image_path);
             fileInputStream = new FileInputStream(fileToUPload);
             String extension = HelperMethods.getExtension(fileToUPload);
             BufferedImage originalImage = ImageIO.read(fileInputStream);

             try {
             ImageInformation info = ImageInformation.readImageFileInformation(fileToUPload);
             if (info.orientation > 1) {
             AffineTransform transform = ImageInformation.getExifTransformation(info);
             originalImage = ImageInformation.transformImage(originalImage, transform);
             }
             } catch (Exception e) {
             e.printStackTrace();
             }*/

            byte[] imgArr = ImageUtil.imageToByteArray(originalImage, extension);
            //byte[] imgArr = ImageUtil.reduceQualityAndSize(originalImage, DefaultSettings.MAXIMUM_IMAGE_WIDTH, extension);
            //  urlString = "http://192.168.1.141:8084/ringmarket/ImageUploadHandler";
            //  System.out.println("urlString==>"+urlString);
            URL url = new URL(urlString);
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
            dos.writeBytes("Content-Disposition: form-data; name=\"cimX\"" + lineEnd + lineEnd + cropX + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"cimY\"" + lineEnd + lineEnd + cropY + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"iw\"" + lineEnd + lineEnd + cropW + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"ih\"" + lineEnd + lineEnd + cropH + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"pst\"" + lineEnd + lineEnd + post + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"imT\"" + lineEnd + lineEnd + 2 + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedFile\";filename=\"" + exsistingFileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.write(imgArr);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            //fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {
        } catch (IOException ioe) {
            HelperMethods.showPlaneDialogMessage(NotificationMessages.CAN_NOT_UPLOAD_IMAGE);
            // JOptionPane.showMessageDialog(null, NotificationMessages.CAN_NOT_UPLOAD_IMAGE, "Upload image", JOptionPane.ERROR_MESSAGE);
        }
        //------------------ read the SERVER RESPONSE  
        try {
            if (conn != null) {
                inStream = new DataInputStream(conn.getInputStream());
                String str;
                while ((str = inStream.readLine()) != null) {
                    JsonFields js = HelperMethods.getNewsFeedWithMultipleImageDto(str);
                    if (js != null && js.getSuccess() != null && js.getSuccess()) {

                        String previous_image = ImageHelpers.getImageNameFromUrl(MyFnFSettings.userProfile.getProfileImage());
                        String previous_thumb_image = ImageHelpers.getImageNameFromUrl(ImageHelpers.getThumbUrl(MyFnFSettings.userProfile.getProfileImage()));
                        String previous_crop_image = ImageHelpers.getImageNameFromUrl(ImageHelpers.getCropUrl(MyFnFSettings.userProfile.getProfileImage()));

                        File f_pre = new File(dHelps.getProfileDestinationFolder() + File.separator + previous_image);
                        if (f_pre.exists()) {
                            f_pre.delete();
                        }

                        File f_pre_thumb = new File(dHelps.getProfileDestinationFolder() + File.separator + previous_thumb_image);
                        if (f_pre_thumb.exists()) {
                            f_pre_thumb.delete();
                        }

                        File f_pre_crop = new File(dHelps.getProfileDestinationFolder() + File.separator + previous_crop_image);
                        if (f_pre_crop.exists()) {
                            f_pre_crop.delete();
                        }

                        String new_image = ImageHelpers.getImageNameFromUrl(js.getIurl());
                        String new_thumb_image = ImageHelpers.getImageNameFromUrl(ImageHelpers.getThumbUrl(js.getIurl()));
                        String new_crop_image = ImageHelpers.getImageNameFromUrl(ImageHelpers.getCropUrl(js.getIurl()));

                        MyFnFSettings.userProfile.setProfileImage(js.getIurl());
                        MyFnFSettings.userProfile.setProfileImageId(js.getImageId());

                        //   File srcDir = new File(image_path);
                        //    File destDir = new File(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER + File.separator + new_image);
                        //   if (srcDir.exists()) {
                        ImageHelpers.downloadImage(ImageHelpers.getCropUrl(js.getIurl()), new_crop_image, MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER);
                        ImageHelpers.downloadImage(ImageHelpers.getThumbUrl(js.getIurl()), new_thumb_image, MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER);
                        //MoveFileExample.copyFile(srcDir, destDir);
                        ImageHelpers.downloadImage(js.getIurl(), new_image, MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER);
                        //   }
                    } else {
                        HelperMethods.showPlaneDialogMessage(js.getMessage());
                    }
                }
                inStream.close();
            }
        } catch (IOException ioex) {
            HelperMethods.showPlaneDialogMessage(NotificationMessages.CAN_NOT_UPLOAD_IMAGE);
            //  JOptionPane.showMessageDialog(null, NotificationMessages.CAN_NOT_UPLOAD_IMAGE, "Upload image", JOptionPane.ERROR_MESSAGE);
        }
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null && GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null) {
            try {
                GuiRingID.getInstance().getMainRight().getMyProfilePanel().changeMyImage();
            } catch (Exception e) {
            }
        }
        /*
         * ***************************************************************************
         * PROFILE IMAGE UPLOAD FROM LOGIN SCREEN
         * ***************************************************************************
         */
        if (btnSkip != null) {
            btnSkip.setEnabled(true);
            btnSkip.doClick();
        }
    }

//    private 
    public void uploader_method_cover_image(String image_path, BufferedImage originalImage, int cropX, int cropY, int post) {
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null && GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null) {
            try {
                GuiRingID.getInstance().getMainRight().getMyProfilePanel().setImageUploadingLoader(false);
            } catch (Exception e) {
            }
        }

        HttpURLConnection conn = null;
        DataOutputStream dos;
        DataInputStream inStream;
        String exsistingFileName = "pcupload";
        String lineEnd = ImageUtil.lineEnd;
        String twoHyphens = ImageUtil.twoHyphens;
        String boundary = ImageUtil.boundary;
        DownLoaderHelps dHelps = new DownLoaderHelps();
        String urlString = ServerAndPortSettings.getCoverImageUploadingURL();
        String extension = image_path.substring(image_path.lastIndexOf(".") + 1);
        exsistingFileName = exsistingFileName + "." + extension;

        try {
            /*File fileToUPload = new File(image_path);
             fileInputStream = new FileInputStream(fileToUPload);
             String extension = HelperMethods.getExtension(fileToUPload);
             BufferedImage originalImage = ImageIO.read(fileInputStream);
             try {
             ImageInformation info = ImageInformation.readImageFileInformation(fileToUPload);
             if (info.orientation > 1) {
             AffineTransform transform = ImageInformation.getExifTransformation(info);
             originalImage = ImageInformation.transformImage(originalImage, transform);
             }
             } catch (Exception e) {
             e.printStackTrace();
             }*/
            byte[] imgArr = ImageUtil.imageToByteArray(originalImage, extension);
            // byte[] imgArr = ImageUtil.reduceQualityAndSize(originalImage, DefaultSettings.MAXIMUM_IMAGE_WIDTH, extension);

            /**/
            // System.out.println("urlString==>" + urlString);
            URL url = new URL(urlString);
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
            dos.writeBytes("Content-Disposition: form-data; name=\"cimX\"" + lineEnd + lineEnd + cropX + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"cimY\"" + lineEnd + lineEnd + cropY + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"pst\"" + lineEnd + lineEnd + post + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"imT\"" + lineEnd + lineEnd + 3 + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedFile\";filename=\"" + exsistingFileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.write(imgArr);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            //fileInputStream.close();
            dos.flush();
            dos.close();
        } catch (MalformedURLException ex) {
        } catch (IOException ioe) {
            HelperMethods.showPlaneDialogMessage(NotificationMessages.CAN_NOT_UPLOAD_IMAGE);
            //   JOptionPane.showMessageDialog(null, NotificationMessages.CAN_NOT_UPLOAD_IMAGE, "Upload image", JOptionPane.ERROR_MESSAGE);
        }

        try {
            if (conn != null) {
                /*BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                 String str;
                 while ((str = br.readLine()) != null) {
                 if (str != null) {
                 System.out.println("STR--->" + str);
                 }
                 }*/
                inStream = new DataInputStream(conn.getInputStream());
                String str;
                while ((str = inStream.readLine()) != null) {
                    JsonFields js = HelperMethods.getNewsFeedWithMultipleImageDto(str);
                    if (js != null && js.getSuccess() != null && js.getSuccess()) {
                        String previous_image = ImageHelpers.getImageNameFromUrl(js.getIurl());
                        File f_pre = new File(dHelps.getCoverDestinationFolder() + File.separator + previous_image);
                        if (f_pre.exists()) {
                            f_pre.delete();
                        }

                        String new_image = ImageHelpers.getImageNameFromUrl(js.getIurl());
                        MyFnFSettings.userProfile.setCoverImage(js.getIurl());
                        MyFnFSettings.userProfile.setCoverImageId(js.getImageId());
                        MyFnFSettings.userProfile.setCoverImageX(-(js.getCoverImageX()));
                        MyFnFSettings.userProfile.setCoverImageY(-(js.getCoverImageY()));

                        ImageHelpers.downloadImage(js.getIurl(), new_image, MyFnFSettings.TEMP_COVER_IMAGE_FOLDER);
                        /*File trgDir = new File(image_path);
                         File srcDir = new File(MyFnFSettings.TEMP_COVER_IMAGE_FOLDER + File.separator + new_image);
                         if (trgDir.exists()) {
                         MoveFileExample.copyFile(trgDir, srcDir);
                         }*/

                    } else {
                        if (js != null && js.getMessage() != null) {
                            HelperMethods.showPlaneDialogMessage(js.getMessage());
                        }
                        //JOptionPane.showMessageDialog(null, js.getMessage(), "Upload image", JOptionPane.ERROR_MESSAGE);
                    }
                }
                inStream.close();
            }

        } catch (IOException ioex) {
            HelperMethods.showWarningDialogMessage(NotificationMessages.CAN_NOT_UPLOAD_IMAGE);

            //JOptionPane.showMessageDialog(null, NotificationMessages.CAN_NOT_UPLOAD_IMAGE, "Upload image", JOptionPane.ERROR_MESSAGE);
        }
        if (GuiRingID.getInstance() != null && GuiRingID.getInstance().getMainRight() != null && GuiRingID.getInstance().getMainRight().getMyProfilePanel() != null) {
            try {
                GuiRingID.getInstance().getMainRight().getMyProfilePanel().changeMyImage();
            } catch (Exception e) {
            }
        }
    }
}
