/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.uploaddownload;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.service.utility.HelperMethods;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import com.ipvision.view.chat.ChatTextMessagePanel;

/**
 *
 * @author Shahadat
 */
public class StickerMarketDownloader extends Thread {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StickerMarketDownloader.class);
    String imageUrl;
    DownLoaderHelps dHelp = new DownLoaderHelps();
    public boolean deleteExistingFile = false;
    private ChatTextMessagePanel chatTextMessagePanel;

    public StickerMarketDownloader(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public StickerMarketDownloader(String imageUrl, ChatTextMessagePanel chatTextMessagePanel) {
        this.imageUrl = imageUrl;
        this.chatTextMessagePanel = chatTextMessagePanel;
    }

    @Override
    public void run() {
        if (imageUrl != null && imageUrl.trim().length() > 0) {
            String imageName = getImageName(imageUrl);

            if (HelperMethods.check_string_contains_substring(imageName, ".")) {
               // String temp = MyfnfHashMaps.getInstance().getBufferedImages().get(imageUrl);

                //   if (temp == null) {
                String subFolder = getSubFolder(imageUrl);
                String destination = dHelp.getSticketDestinationFolder() + subFolder + imageName;
                createStickersDirectory(dHelp.getSticketDestinationFolder() + subFolder);

                //   MyfnfHashMaps.getInstance().getBufferedImages().put(imageUrl, imageName);
                boolean isFileExist = deleteExistingFile ? deleteFile(destination) : isFileExistInDirectory(destination);

                if (!isFileExist) {
                    try {
                        log.warn("Downloading Market Sticker :: " + imageUrl + ", " + imageName);
                        URL url = new URL(imageUrl);
                        BufferedImage bimg = ImageIO.read(url);
                        File outputfile = new File(destination);
                        ImageIO.write(bimg, getExtension(imageName), outputfile);
                    } catch (Exception ex) {
                        try {
                            URL url = new URL(imageUrl);
                            URLConnection yc = url.openConnection();
                            InputStream is = yc.getInputStream();
                            FileOutputStream fos = new FileOutputStream(destination);
                            int size = 5120;
                            byte[] buffer = new byte[size];
                            int bytesRead = 0;
                            while ((bytesRead = is.read(buffer)) != -1) {
                                fos.write(buffer, 0, bytesRead);
                            }
                            fos.close();
                            is.close();
                        } catch (Exception e) {
                            // ex.printStackTrace();
                        }
                    }

                    if (chatTextMessagePanel != null && destination.length() > 0) {
                        showInChatWindow(destination);
                    }

                }
            }
        }
    }

    public static String getImageName(String imageUrl) {
        String imageName = "";
        if (imageUrl.contains("/")) {
            imageName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
        }
        return imageName;
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

    public static boolean deleteFile(String directory) {
        File f = new File(directory);
        if (f.exists() && !f.isDirectory()) {
            f.delete();
        }
        return false;
    }

    private static String getSubFolder(String imageUrl) {
        String subFolder = "";

        String[] splits = imageUrl.split("/");
        for (int i = splits.length - 2; i > 0; i--) {
            if (splits[i].equalsIgnoreCase(MyFnFSettings.D_FULL) || splits[i].equalsIgnoreCase(MyFnFSettings.D_MINI)) {
                break;
            }
            subFolder = splits[i] + File.separator + subFolder;
        }

        return subFolder;
    }

    private static void createStickersDirectory(String subFolder) {
        try {
            File dir = new File(subFolder);
            if (!(dir.exists() && dir.isDirectory())) {
                dir.mkdirs();
            }
        } catch (Exception ex) {
        }
    }

    private void showInChatWindow(String directory) {
        String src;
        File f = new File(directory);
        if (f.exists() && !f.isDirectory()) {
            src = f.toURI().toString();
            try {
                if (src != null && src.trim().length() > 3) {
                    ImageIcon img = new ImageIcon(ImageIO.read(f));
                    int w = img.getIconWidth();
                    int h = img.getIconHeight();
                    String replaceString = "<img src=\"" + src + "\" height=" + h + " width=" + w + " />";
                    chatTextMessagePanel.textArea.setText("");
                    chatTextMessagePanel.setMessageText(replaceString);
                } else {
                }
            } catch (Exception e) {
            }
        }
    }
}
