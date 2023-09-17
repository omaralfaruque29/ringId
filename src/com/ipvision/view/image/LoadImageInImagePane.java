/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

import com.ipvision.view.utility.ImagePane;
import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.SettingsConstants;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import com.ipvision.service.utility.Scalr;

/**
 *
 * @author Faiz
 */
public class LoadImageInImagePane extends Thread {

    ImagePane imgPane;
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LoadImageInImagePane.class);
    String imURLString;
    int imageType;
    BufferedImage loadingIMage;
    BufferedImage imageNotfound;
    BufferedImage mainImage;
    BufferedImage tempImage;
    Dimension imagePaneDimension;
    String imageName;
    int selectedImageIndex;

    public LoadImageInImagePane(String imageUrl, ImagePane imagePane, int imageType, int selectedImageIndex) {
        this.imURLString = imageUrl;
        this.imageType = imageType;
        this.imgPane = imagePane;
        this.selectedImageIndex = selectedImageIndex;
        imagePaneDimension = this.imgPane.getSize();

    }

    @Override
    public void run() {
        try {
            loadingIMage = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.PLEASE_WAIT_MINI));
            this.imgPane.setImage(loadingIMage);
            this.imgPane.revalidate();

            this.imageName = ImageHelpers.getImageNameFromUrl(imURLString);
            getImage();

        } catch (Exception ex) {
            try {
                imageNotfound = ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.NO_IMAGE_FOUND_200x200));
                this.imgPane.setImage(imageNotfound);
                log.error("Error while downloading image from ==>" + imURLString);
            } catch (IOException ex1) {
                log.error("Image not found image exeption ==>" + GetImages.NO_IMAGE_FOUND_200x200);
            }
        } finally {
            this.imgPane.repaint();
            if (loadingIMage != null) {
                loadingIMage.flush();
                loadingIMage = null;
            }
            if (mainImage != null) {
                mainImage.flush();
                mainImage = null;
            }
            if (imageNotfound != null) {
                imageNotfound.flush();
                imageNotfound = null;
            }
            Runtime.getRuntime().gc();
            System.gc();
        }
    }

    public void getImage() {
        mainImage = ImageHelpers.getBufferedImageFromLocal(imageName, this.imageType);
        if (mainImage != null) {
            setImageToImagePane(mainImage);
        } else {
            tempImage = ImageHelpers.getDiffSizedBufferedImageFromLocal(imURLString, imageType);
            if (tempImage != null) {
                setImageToImagePane(tempImage);
                downladAndViewRealImage();
            } else {
                downladAndViewRealImage();
            }
        }
    }

    private void downladAndViewRealImage() {
        if (imageType == SettingsConstants.TYPE_PROFILE_IMAGE) {
            mainImage = ImageHelpers.downloadImage(imURLString, imageName, MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER);
        } else if (imageType == SettingsConstants.TYPE_COVER_IMAGE) {
            mainImage = ImageHelpers.downloadImage(imURLString, imageName, MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER);
        } else {
            mainImage = ImageHelpers.downloadImage(imURLString, imageName, MyFnFSettings.TEMP_BOOK_IMAGE_FOLDER);
        }
        if (mainImage != null) {
            /*if (selectedImageIndex < 0 && SingleImageDetailsProfileCover.getSingleImgeDetails() == null) {
             setImageToImagePane(mainImage);
             }else */
            if (SingleImgeDetails.getSingleImgeDetails() != null && SingleImgeDetails.getSingleImgeDetails().selectedImageIndex == selectedImageIndex) {
                setImageToImagePane(mainImage);
            } else if (SingleImageDetailsProfileCover.getSingleImgeDetails() != null && SingleImageDetailsProfileCover.getSingleImgeDetails().selectedImageIndex == selectedImageIndex) {
                setImageToImagePane(mainImage);
            } else {
                setImageToImagePane(mainImage);
            }
        }
    }

    private void setImageToImagePane(BufferedImage mainImage) {
        try {
            if (mainImage != null) {
                if (mainImage.getHeight() >= imagePaneDimension.getHeight() && mainImage.getWidth() >= imagePaneDimension.getWidth()) {
                    mainImage = Scalr.resize(mainImage, Scalr.Mode.AUTOMATIC, (int) imagePaneDimension.getWidth(), (int) imagePaneDimension.getHeight(), Scalr.OP_ANTIALIAS);
                } else if (mainImage.getHeight() >= imagePaneDimension.getHeight() && mainImage.getWidth() < imagePaneDimension.getWidth()) {
                    mainImage = Scalr.resize(mainImage, Scalr.Mode.FIT_TO_HEIGHT, (int) imagePaneDimension.getWidth(), (int) imagePaneDimension.getHeight(), Scalr.OP_ANTIALIAS);
                } else if (mainImage.getHeight() < imagePaneDimension.getHeight() && mainImage.getWidth() >= imagePaneDimension.getWidth()) {
                    mainImage = Scalr.resize(mainImage, Scalr.Mode.FIT_TO_WIDTH, (int) imagePaneDimension.getWidth(), (int) imagePaneDimension.getHeight(), Scalr.OP_ANTIALIAS);
                }
                this.imgPane.setImage(mainImage);
            } else {
                imageNotfound = ImageIO.read(new Object() {
                }.getClass().getClassLoader().getResource(GetImages.NO_IMAGE_FOUND_200x200));
                this.imgPane.setImage(imageNotfound);
                log.error("Images not found LoadImageInLabel==>" + imURLString);
            }
            this.imgPane.repaint();
        } catch (Exception e) {

        }
    }
}
