/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.image;

import static com.ipvision.service.utility.HelperMethods.getExtension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.imageio.ImageIO;
import com.ipvision.view.utility.ImageHelpers;

/**
 *
 * @author Faiz
 */
public class SaveBufferImageInLoacalFolder extends Thread {

    String imageName;
    String folder;
    BufferedImage im;
    String imageUrl;
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveBufferImageInLoacalFolder.class);

    public SaveBufferImageInLoacalFolder(String folder, BufferedImage im, String imageUrl) {
        this.imageUrl = imageUrl;
        this.folder = folder;
        this.im = im;
        this.imageName = ImageHelpers.getImageNameFromUrl(this.imageUrl);
        setName(this.getClass().getSimpleName());
    }
//

    @Override
    public void run() {
        try {
            File outputfile = new File(folder + File.separator + imageName);
            ImageIO.write(im, getExtension(imageName), outputfile);
//            EncryptDecryptFile encryptFile = EncryptDecryptFile.getInstance();
//            encryptFile.encrypt(im, folder + File.separator + imageName);
        } catch (Exception ex) {
            //   ex.printStackTrace();
            log.error("Image format error==>" + imageName + " ==>" + ex.getMessage());
            try {
                URL url = new URL(this.imageUrl);
                URLConnection yc = url.openConnection();
                InputStream is = yc.getInputStream();
                FileOutputStream fos = new FileOutputStream(folder + File.separator + imageName);
                int size = 1024;
                byte[] buffer = new byte[size];
                int bytesRead = 0;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
                fos.close();
                is.close();
            } catch (Exception e) {
                log.error("Image save faild saving image==>" + imageName + " ==>" + ex.getMessage());
            }
        }
    }
}
