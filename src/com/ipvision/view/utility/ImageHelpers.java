/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.service.image.SaveBufferImageInLoacalFolder;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import static com.ipvision.view.utility.DesignClasses.scaleToRoundedImage;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.RingColorCode;
import com.ipvision.constants.SettingsConstants;
import com.ipvision.service.uploaddownload.DownLoaderHelps;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import com.ipvision.service.utility.Scalr;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.HelperMethods;
import com.ipvision.view.image.ImageObjects;
import com.ipvision.view.image.ImagePaneForCoverImage;
import com.ipvision.view.image.ViewProfileImage;
import java.awt.AlphaComposite;
import java.awt.FontMetrics;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

/**
 *
 * @author Faiz
 */
public class ImageHelpers {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ImageHelpers.class);

    public static ViewProfileImage addProfileImageThumbInQueue(JLabel label, String url, int size, String shortName) {
        setUnknownProfileImageDefault(label, false, size, shortName);
        ViewProfileImage viewProfileImage = new ViewProfileImage(label, ImageHelpers.getThumbUrl(url), size, shortName);
        NewsFeedMaps.getInstance().getViewProfileImageQueue().addItem(viewProfileImage);
        //viewProfileImage.start();
        return viewProfileImage;
    }

    public static ViewProfileImage addProfileImageThumbInQueue(JLabel label, String url, int size) {
        setUnknownProfileImageDefault(label, false, size, null);
        ViewProfileImage viewProfileImage = new ViewProfileImage(label, ImageHelpers.getThumbUrl(url), size);
        NewsFeedMaps.getInstance().getViewProfileImageQueue().addItem(viewProfileImage);
        //viewProfileImage.start();
        return viewProfileImage;
    }

    public static ViewProfileImage addProfileImageThumb(JLabel label, String url) {
        //setUnknownProfileImageDefault(label, false);
        ViewProfileImage viewProfileImage = new ViewProfileImage(label, ImageHelpers.getThumbUrl(url));
        // NewsFeedMaps.getInstance().getViewProfileImageQueue().addItem(viewProfileImage);
        viewProfileImage.start();
        return viewProfileImage;
    }

    public static ViewProfileImage addProfileImageThumb(JLabel label, String url, int size) {
        //setUnknownProfileImageDefault(label, false);
        ViewProfileImage viewProfileImage = new ViewProfileImage(label, ImageHelpers.getThumbUrl(url), size);
        //NewsFeedMaps.getInstance().getViewProfileImageQueue().addItem(viewProfileImage);
        viewProfileImage.start();
        return viewProfileImage;
    }

    public static ViewProfileImage addProfileImageThumb(JLabel label, String url, int size, int borderWidth, Color borderColor) {
        //setUnknownProfileImageDefault(label, false);
        ViewProfileImage viewProfileImage = new ViewProfileImage(label, ImageHelpers.getThumbUrl(url), size, borderWidth, borderColor);
        //NewsFeedMaps.getInstance().getViewProfileImageQueue().addItem(viewProfileImage);
        viewProfileImage.start();
        return viewProfileImage;
    }

    public static ViewProfileImage addProfileImageThumb(JLabel label, String url, int size, boolean large) {
        //setUnknownProfileImageDefault(label, large);
        ViewProfileImage viewProfileImage = new ViewProfileImage(label, ImageHelpers.getThumbUrl(url), size, large);
        viewProfileImage.start();
        //NewsFeedMaps.getInstance().getViewProfileImageQueue().addItem(viewProfileImage);
        return viewProfileImage;
    }

    public static ViewProfileImage addProfileImageCrop(ImagePaneForCoverImage coverImagePanel, JLabel label, String url, int size, boolean large) {
        //setUnknownProfileImageDefault(label, large);
        ViewProfileImage viewProfileImage = new ViewProfileImage(coverImagePanel, label, ImageHelpers.getCropUrl(url), size, large);
        viewProfileImage.start();
        // NewsFeedMaps.getInstance().getViewProfileImageQueue().addItem(viewProfileImage);
        return viewProfileImage;
    }

    public static void setUnknownProfileImageDefault(JLabel imageLabel, boolean largeImage, int size, String shortName) {
        BufferedImage unknownImage = null;
        if (shortName != null && shortName.length() > 0) {
            unknownImage = createDefaultBufferImage(size, shortName);
            imageLabel.setIcon(new ImageIcon(unknownImage));
        } else if (largeImage) {
            unknownImage = DesignClasses.return_buffer_image(GetImages.UNKNOW_IMAGE);
            imageLabel.setIcon(new ImageIcon(unknownImage));
        } else if (size == 70) {
            imageLabel.setIcon(ImageObjects.unknown_70);
            //unknownImage = ImageHelpers.getUnknownImage70();
        } else if (size == 25) {
            imageLabel.setIcon(ImageObjects.unknown_25);
            //unknownImage = ImageHelpers.getUnknownImage25();
        } else if (size == 39) {
            imageLabel.setIcon(ImageObjects.unknown_39);
            //unknownImage = DesignClasses.return_buffer_image(GetImages.UNKNOW_IMAGE_39);
        } else {
            imageLabel.setIcon(ImageObjects.unknown_35);
            //unknownImage = DesignClasses.return_buffer_image(GetImages.UNKNOW_IMAGE_35);
        }
        imageLabel.revalidate();
        //imageLabel.setIcon(new ImageIcon(unknownImage));
    }

    public static BufferedImage createDefaultBufferImage(int size, String shortName) {
        BufferedImage bufferImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferImage.createGraphics();
        g2d.setComposite(AlphaComposite.Src);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fill(new RoundRectangle2D.Float(0, 0, size, size, size, size));

        g2d.setColor(RingColorCode.DEFAULT_BORDER_COLOR);
        BasicStroke stroke = new BasicStroke(1);
        g2d.setStroke(stroke);
        g2d.draw(new RoundRectangle2D.Float(0.5F, 0, size - 1, size - 1, size, size));

        int height = size - 13;
        float fontSize = 15f;

        g2d.setComposite(AlphaComposite.SrcAtop);
        try {
            g2d.setFont(DesignClasses.getDefaultFont(Font.PLAIN, fontSize));//new Font("Arial", Font.PLAIN, fontSize);
        } catch (Exception ex) {
            g2d.setFont(new Font("Arial", Font.PLAIN, (int) fontSize));
        }

        BasicStroke textWidth = new BasicStroke(2);
        g2d.setStroke(textWidth);

        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(shortName);
        int x = (size - width) / 2;

        g2d.setColor(new Color(0xfda445));
        g2d.drawString(shortName, x, height);

        g2d.dispose();
        return bufferImage;
    }

    public static void setAppIcon(JFrame frame, String count) {
        try {
            BufferedImage bi = DesignClasses.return_buffer_image(GetImages.APP_LOGO);
            Image img3 = new ImageIcon(getRingIconImage(bi, count)).getImage();
            List<Image> icons = new ArrayList<>();
            icons.add(img3);
            frame.setIconImages(icons);
        } catch (Exception ex) {
        }
    }

    protected static BufferedImage getRingIconImage(BufferedImage returnImg, String text) {
        //  BufferedImage returnImg = new BufferedImage
        int length = (text == null ? 0 : text.length());

        if (returnImg != null) {
            int cpX = returnImg.getWidth() / 2;
            int cpY = returnImg.getHeight() / 2;
            int tX = 6;
            int tY = 13;
            int oW = 17;
            int oH = 17;

            if (length == 1) {
                tX = 5;
                tY = 14;
                oW = 18;
                oH = 18;
                cpX = (int) (((float) returnImg.getWidth()) / ((float) 1.7));
                cpY = (int) (((float) returnImg.getHeight()) / ((float) 1.7));
            } else if (length == 2) {
                tX = 3;
                tY = 15;
                oW = 22;
                oH = 21;
                cpX = (int) (((float) returnImg.getWidth()) / ((float) 1.9));
                cpY = (int) (((float) returnImg.getHeight()) / ((float) 1.9));
            } else if (length >= 3) {
                tX = 2;
                tY = 19;
                oW = 28;
                oH = 28;
                cpX = (int) (((float) returnImg.getWidth()) / ((float) 2.6));
                cpY = (int) (((float) returnImg.getHeight()) / ((float) 2.6));
            }

            BufferedImage mask = new BufferedImage(oW, oH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = mask.createGraphics();

            if (length > 0) {
                g2d.setColor(new Color(0xfdb612));
                g2d.fillOval(0, 0, oW, oH);
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));
                g2d.drawString(text, tX, tY);
                g2d.setColor(Color.BLACK);
                BasicStroke stroke = new BasicStroke(1); //1 pixels wide (thickness of the border)
                g2d.setStroke(stroke);
                g2d.drawOval(0, 0, oW - 1, oH - 1);
                g2d.dispose();
            }

            BufferedImage comp = new BufferedImage(returnImg.getWidth(), returnImg.getHeight(), BufferedImage.TYPE_INT_ARGB);
            g2d = comp.createGraphics();
            g2d.drawImage(returnImg, 0, 0, null);
            if (length > 0) {
                g2d.drawImage(mask, cpX, cpY, null);
            }
            g2d.dispose();
            returnImg = comp;
        }

        return returnImg;
    }

    public static boolean isFileExistInDirectory(String directory) {
        File f = new File(directory);
        return f.exists() && !f.isDirectory();
    }

    public static boolean isCropUrl(String imageUrl) {
        if (imageUrl != null) {
            return imageUrl.contains("/crp");
        }
        return false;
    }

    public static boolean isThumbUrl(String imageUrl) {
        if (imageUrl != null) {
            return imageUrl.contains("/thumb");
        }
        return false;
    }

    public static String convertCropToThumbUrl(String imageUrl) {
        if (imageUrl != null) {
            imageUrl = imageUrl.replace("/crp", "/thumb");
        }
        return imageUrl;
    }

    public static String getThumbUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().length() <= 0) {
            return null;
        }

        String imageName = "";
        String thumbUrl = "";

        if (imageUrl.contains("/")) {
            imageName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            imageUrl = imageUrl.substring(0, imageUrl.lastIndexOf('/'));
        }

        thumbUrl = imageUrl + "/" + "thumb" + imageName;

        return thumbUrl;
    }

    public static String getCropUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().length() <= 0) {
            return null;
        }
        String imageName = "";
        String thumbUrl = "";
        if (imageUrl.contains("/")) {
            imageName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            imageUrl = imageUrl.substring(0, imageUrl.lastIndexOf('/'));
        }
        thumbUrl = imageUrl + "/" + "crp" + imageName;
        return thumbUrl;
    }

    public static String get300Url(String imageUrl) {

        if (imageUrl == null || imageUrl.trim().length() <= 0) {
            return null;
        }

        String imageName = "";
        String thumbUrl = "";

        if (imageUrl.contains("/")) {
            imageName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            imageUrl = imageUrl.substring(0, imageUrl.lastIndexOf('/'));
        }

        thumbUrl = imageUrl + "/" + "300" + imageName;

        return thumbUrl;
    }

    public static String get600Url(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().length() <= 0) {
            return null;
        }

        String imageName = "";
        String thumbUrl = "";

        if (imageUrl.contains("/")) {
            imageName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            imageUrl = imageUrl.substring(0, imageUrl.lastIndexOf('/'));
        }

        thumbUrl = imageUrl + "/" + "600" + imageName;

        return thumbUrl;
    }

    //  static BufferedImage bufferedImage=null;
    public static BufferedImage getBufferImageFromurl25(String url, int size) {

        DownLoaderHelps dHelp = new DownLoaderHelps();
        try {
            String testString = url != null && url.length() > 0 ? getThumbUrl(url) : "";
            String image_name = testString != null && testString.length() > 0 ? getImageNameFromUrl(testString) : "";
            if (image_name != null && image_name.length() > 2) {
                File f = new File(dHelp.getProfileDestinationFolder() + File.separator + image_name);
                if (f.exists()) {
                    try {
                        return scaleToRoundedImage(size, size, ImageIO.read(f), size);
                    } catch (Exception e) {
                    }
                } else {
                    return getUnknownImage25();//getClass().getResource("/" + GetImages.UNKNOW_IMAGE));
                }
            } else {
                return getUnknownImage25();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static BufferedImage getResizedMenuItemIcon(String url) {
        BufferedImage imgProfile = getBookBuffedImageFromUrl(url, SettingsConstants.TYPE_PROFILE_IMAGE);
        try {
            if (imgProfile != null) {
                return scaleToRoundedImage(25, 25, imgProfile, 25);
            } else {
                return getUnknownImage25();
            }

        } catch (Exception e) {
        }
        return null;
    }

    public static String getChatTransferFileName(String imageUrl) {
        String fullImageName;
        String userId = "";
        String imageName = "";
        String[] split = imageUrl.split("/");

        if (split.length == 2) {
            userId = split[0];
            imageName = split[1];
        }

        fullImageName = userId + "_" + imageName;
        return fullImageName;
    }

    public static BufferedImage getChatTransferImage(String url) {

        DownLoaderHelps dHelp = new DownLoaderHelps();
        try {
            if (url != null && url.trim().length() > 0) {
                String testString = url;
                String image_name = getChatTransferFileName(testString);
                if (image_name != null && image_name.length() > 2) {
                    File f = new File(dHelp.getChatFileDestinationFolder() + File.separator + image_name);
                    if (f.exists()) {
                        try {
                            BufferedImage originalImage = ImageIO.read(f);
                            ImageInformation info = ImageInformation.readImageFileInformation(f);
                            if (info.orientation > 1) {
                                AffineTransform transform = ImageInformation.getExifTransformation(info);
                                originalImage = ImageInformation.transformImage(originalImage, transform);
                            }
                            return originalImage;
                        } catch (Exception e) {
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static BufferedImage getUnknownImage(boolean small) {
        String image = GetImages.UNKNOW_IMAGE;
        if (small) {
            image = GetImages.UNKNOW_IMAGE_35;
        }
        try {
            return ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(image));
        } catch (Exception e) {
            log.error("image not found==>" + image);
        }
        return null;
    }

    public static BufferedImage getUnknownImage25() {
        try {
            return ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.UNKNOW_IMAGE_25));

        } catch (Exception e) {
            log.error("image not found==>" + GetImages.UNKNOW_IMAGE_25);
        }
        return null;
    }

    public static BufferedImage getUnknownImage35() {
        try {
            return ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.UNKNOW_IMAGE_35));

        } catch (Exception e) {
            log.error("image not found==>" + GetImages.UNKNOW_IMAGE_35);
        }
        return null;
    }

    public static BufferedImage getUnknownImage39() {
        try {
            return ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.UNKNOW_IMAGE_39));

        } catch (Exception e) {
            log.error("image not found==>" + GetImages.UNKNOW_IMAGE_39);
        }
        return null;
    }

    public static BufferedImage getUnknownImage70() {
        try {
            return ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(GetImages.UNKNOW_IMAGE_70));

        } catch (Exception e) {
            log.error("image not found==>" + GetImages.UNKNOW_IMAGE_70);
        }
        return null;
    }

    public static BufferedImage getProfileImageChatView(String friendIdentity) {
        BufferedImage image = null;
        try {
            UserBasicInfo friendProfileInfo = FriendList.getInstance().getFriend_hash_map().get(friendIdentity);
            if (friendProfileInfo != null && friendProfileInfo.getProfileImage() != null && friendProfileInfo.getProfileImage().trim().length() > 0) {
                image = ImageHelpers.getBufferImageFromFolder(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER, ImageHelpers.getImageNameThumbFromUrl(friendProfileInfo.getProfileImage().trim()));
                if (image != null) {
                    image = DesignClasses.scaleToRoundedImageWithBorder(35, 35, image, 35);
                } else {
                    image = ImageHelpers.createDefaultBufferImage(35, HelperMethods.getShortName(friendProfileInfo.getFullName()));//ImageHelpers.getUnknownImage(true);
                }
            }
            if (image == null) {
                image = getUnknownImage(true);
            }
        } catch (Exception e) {
        }
        return image;
    }

    public static BufferedImage getBufferImageFromFolder(String folder, String imageName) {
        try {
            File f = new File(folder + File.separator + imageName);
            //  System.out.println("getBufferImageFromFolder file length==>" + f.length());
            if (f.exists()) {
                return ImageIO.read(f);
            }
        } catch (IOException ex) {
            //  ex.printStackTrace();
            log.error("getBufferImageFromFolder ==>" + folder + File.separator + imageName);
        }
        return null;
    }

    public static BufferedImage getBookBuffedImageFromUrl(String image_url, int type) {
        BufferedImage img = null;
        try {
            String imageName = getImageNameFromUrl(image_url);
            if (type == SettingsConstants.TYPE_PROFILE_IMAGE) {
                if (isFileExistInDirectory(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER + File.separator + imageName)) {
                    img = getBufferImageFromFolder(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER, imageName);// ImageIO.read(new File(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER + File.separator + imageName));
                } else {
                    img = downloadImage(image_url, imageName, MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER);
                }

            } else if (type == SettingsConstants.TYPE_COVER_IMAGE) {
                if (isFileExistInDirectory(MyFnFSettings.TEMP_COVER_IMAGE_FOLDER + File.separator + imageName)) {
                    img = getBufferImageFromFolder(MyFnFSettings.TEMP_COVER_IMAGE_FOLDER, imageName);// ImageIO.read(new File(MyFnFSettings.TEMP_COVER_IMAGE_FOLDER + File.separator + imageName));
                } else {
                    img = downloadImage(image_url, imageName, MyFnFSettings.TEMP_COVER_IMAGE_FOLDER);
                }
            } else {

                if (isFileExistInDirectory(MyFnFSettings.TEMP_BOOK_IMAGE_FOLDER + File.separator + imageName)) {
                    img = getBufferImageFromFolder(MyFnFSettings.TEMP_BOOK_IMAGE_FOLDER, imageName);// ImageIO.read(new File(MyFnFSettings.TEMP_BOOK_IMAGE_FOLDER + File.separator + imageName));
                } else {
                    img = downloadImage(image_url, imageName, MyFnFSettings.TEMP_BOOK_IMAGE_FOLDER);
                }

            }
        } catch (Exception e) {
            Runtime.getRuntime().gc();
        }

        return img;
    }

    public static BufferedImage getBufferedImageFromLocal(String imageName, int type) {
        BufferedImage img = null;
        try {
            if (type == SettingsConstants.TYPE_PROFILE_IMAGE) {
                if (isFileExistInDirectory(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER + File.separator + imageName)) {
                    img = getBufferImageFromFolder(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER, imageName);// ImageIO.read(new File(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER + File.separator + imageName));
                }

            } else if (type == SettingsConstants.TYPE_COVER_IMAGE) {
                if (isFileExistInDirectory(MyFnFSettings.TEMP_COVER_IMAGE_FOLDER + File.separator + imageName)) {
                    img = getBufferImageFromFolder(MyFnFSettings.TEMP_COVER_IMAGE_FOLDER, imageName);// ImageIO.read(new File(MyFnFSettings.TEMP_COVER_IMAGE_FOLDER + File.separator + imageName));
                }
            } else {
                if (isFileExistInDirectory(MyFnFSettings.TEMP_BOOK_IMAGE_FOLDER + File.separator + imageName)) {
                    img = getBufferImageFromFolder(MyFnFSettings.TEMP_BOOK_IMAGE_FOLDER, imageName);// ImageIO.read(new File(MyFnFSettings.TEMP_BOOK_IMAGE_FOLDER + File.separator + imageName));
                }
            }
        } catch (Exception e) {
            Runtime.getRuntime().gc();
        }

        return img;
    }

    public static BufferedImage getDiffSizedBufferedImageFromLocal(String imURLString, int type) {
        BufferedImage img = null;
        String imageName;
        try {
            imageName = getImageNameFromUrl(get600Url(imURLString));
            img = getBufferedImageFromLocal(imageName, type); //600 image

            if (img == null) {
                imageName = getImageNameFromUrl(get300Url(imURLString));
                img = getBufferedImageFromLocal(imageName, type); //300 image
            }
            if (img == null) {
                imageName = getImageNameFromUrl(getThumbUrl(imURLString));
                img = getBufferedImageFromLocal(imageName, type); //thumb image
            }

        } catch (Exception e) {
            Runtime.getRuntime().gc();
        }

        return img;
    }

    public static BufferedImage downloadImage(String image_url, String imageName, String folder) {
        BufferedImage img = null;
        String image_url_with_base = ServerAndPortSettings.getImageServerBase() + image_url;
        if (!NewsFeedMaps.getInstance().getAllreadyTriedToDownload().contains(image_url)) {
            try {
                URL url = new URL(image_url_with_base);
                URLConnection yc = url.openConnection();
                InputStream stream = yc.getInputStream();
                img = ImageIO.read(stream);
                if (!NewsFeedMaps.getInstance().getImagesInDownloadingState().contains(image_url_with_base)) {
                    NewsFeedMaps.getInstance().getImagesInDownloadingState().add(image_url_with_base);
                    (new SaveBufferImageInLoacalFolder(folder, img, image_url)).start();
                }
            } catch (Exception ex) {
                NewsFeedMaps.getInstance().getAllreadyTriedToDownload().add(image_url);
                log.error("can not download image==>" + image_url_with_base);
            } finally {
                System.gc();
            }
        } else {
            log.error("Allready tried to download but invalid url==>" + image_url_with_base);
        }
        return img;
    }
//"/" + "thumb"

    public static String getImageNameFromUrl(String imageUrl) {
        String fullImageName = null;
        if (imageUrl != null && imageUrl.contains("/")) {
            String[] splits = imageUrl.split("/");
            fullImageName = splits[0] + "_" + splits[1];
        }
        return fullImageName;
    }

    public static String getImageNameThumbFromUrl(String imageUrl) {
        String fullImageName = null;
        if (imageUrl != null && imageUrl.contains("/")) {
            String[] splits = imageUrl.split("/");
            fullImageName = splits[0] + "_thumb" + splits[1];
        }
        return fullImageName;
    }
    /*feed*/

    public static void setImageInLabelFromBufferedImage(JLabel label, int commonWidth, int maxImageHeight, BufferedImage img) {
        try {

            if (img.getWidth() < commonWidth && img.getHeight() < maxImageHeight) {
                img = Scalr.resize(img, Scalr.Mode.AUTOMATIC, img.getWidth(), img.getHeight(), Scalr.OP_ANTIALIAS);
            } else {
                if (img.getWidth() > img.getHeight()) {
                    if (img.getWidth() > commonWidth) {
                        img = Scalr.resize(img, Scalr.Mode.FIT_TO_WIDTH, commonWidth, maxImageHeight, Scalr.OP_ANTIALIAS);
                    }
                } else if (img.getHeight() > img.getWidth()) {
                    if (img.getHeight() > maxImageHeight) {
                        img = Scalr.resize(img, Scalr.Mode.FIT_TO_HEIGHT, commonWidth, maxImageHeight, Scalr.OP_ANTIALIAS);
                    }
                } else {
                    img = Scalr.resize(img, Scalr.Mode.FIT_EXACT, commonWidth, maxImageHeight, Scalr.OP_ANTIALIAS);
                }
            }

        } catch (Exception e) {
            log.error("setImageInLabelFromBufferedImage==>" + e.getMessage());
        } finally {
            label.setIcon(new ImageIcon(img));
            label.revalidate();
            img.flush();
            img = null;
        }

    }

}
