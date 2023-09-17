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
import javax.swing.JLabel;
import com.ipvision.view.settingsmenu.SingleChatBackgroundPanel;
import com.ipvision.model.ChatBgImageDTO;

/**
 *
 * @author Shahadat
 */
public class ChatBackgroundDownloader extends Thread {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatBackgroundDownloader.class);
    private ChatBgImageDTO entity;
    private JLabel iconLabel;
    private SingleChatBackgroundPanel panel;
    private DownLoaderHelps dHelp = new DownLoaderHelps();

    public ChatBackgroundDownloader(ChatBgImageDTO entity, SingleChatBackgroundPanel panel) {
        this.entity = entity;
        this.panel = panel;
    }

    @Override
    public void run() {
        download(entity);
        
    }

    public void download(ChatBgImageDTO entity) {
        try {
            if (HelperMethods.check_string_contains_substring(entity.getName(), ".")) {
                String destination = dHelp.getChatBackgroundFolder() + entity.getName();
              //  MyfnfHashMaps.getInstance().getBufferedImages().put(entity.getUrl(), entity.getName());
                boolean isFileExist = isFileExistInDirectory(destination);

                if (!isFileExist) {
                    try {
                        log.warn("Downloading Chat Background :: " + entity.getUrl() + ", " + entity.getName());
                        URL url = new URL(entity.getUrl());
                        BufferedImage bimg = ImageIO.read(url);
                        File outputfile = new File(destination);
                        ImageIO.write(bimg, getExtension(entity.getName()), outputfile);
                    } catch (Exception ex) {
                        try {
                            URL url = new URL(entity.getUrl());
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
                            //ex.printStackTrace();
                        }
                    }
                }
            }
            
            if(panel != null) {
                panel.loadImage();
            }
        } catch (Exception ex) {
        }
    }

    private String getExtension(String imageName) {
        if (imageName.contains(".")) {
            imageName = imageName.substring(imageName.lastIndexOf('.') + 1);
        }
        return imageName;
    }

    private boolean isFileExistInDirectory(String directory) {
        File f = new File(directory);
        if (f.exists() && !f.isDirectory()) {
            return true;
        }
        return false;
    }
}
