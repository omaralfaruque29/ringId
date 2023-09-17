/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.image;

import com.ipvision.view.utility.ImageHelpers;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.RingColorCode;
import static com.ipvision.view.utility.ImageHelpers.createDefaultBufferImage;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.BasicStroke;

/**
 *
 * @author Faiz
 */
public class ViewProfileImage extends Thread {

    private String shortName;
    private String imageUrl;
    private String imageName;
    private JLabel imageLabel;
    private int size = 35;
    boolean largeImage = false;
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ViewProfileImage.class);
    private boolean getImage = false;
    private ImagePaneForCoverImage coverImagePanel;
    private int borderWidth = 0;
    private Color borderColor;

    public ViewProfileImage(JLabel imageLabel, String imageUrl) {
        this.imageLabel = imageLabel;
        this.imageUrl = imageUrl;
    }

    public ViewProfileImage(JLabel imageLabel, String imageUrl, int size) {
        this.imageLabel = imageLabel;
        this.imageUrl = imageUrl;
        this.size = size;
    }

    public ViewProfileImage(JLabel imageLabel, String imageUrl, int size, String shortName) {
        this.imageLabel = imageLabel;
        this.imageUrl = imageUrl;
        this.size = size;
        this.shortName = shortName;
    }

    public ViewProfileImage(JLabel imageLabel, String imageUrl, int size, int borderWidth, Color borderColor) {
        this.imageLabel = imageLabel;
        this.imageUrl = imageUrl;
        this.size = size;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
    }

    public ViewProfileImage(JLabel imageLabel, String imageUrl, int size, boolean largeImage) {
        this.imageLabel = imageLabel;
        this.imageUrl = imageUrl;
        this.size = size;
        this.largeImage = largeImage;
    }

    public ViewProfileImage(ImagePaneForCoverImage coverImagePanel, JLabel imageLabel, String imageUrl, int size, boolean largeImage) {
        this.coverImagePanel = coverImagePanel;
        this.imageLabel = imageLabel;
        this.imageUrl = imageUrl;
        this.size = size;
        this.largeImage = largeImage;
    }

    @Override
    public void run() {

        //   try {
        ImageHelpers.setUnknownProfileImageDefault(imageLabel, largeImage, size, shortName);
        if (coverImagePanel != null) {
            doWorkForCropImage();
        } else {
            doWorkForThumbImage();
        }
        //    } catch (Exception e) {
        //       e.printStackTrace();
        //   } finally {
        //      System.gc();
        //  }
    }

    private void doWorkForCropImage() {
        BufferedImage unknownImage = null;
        try {
            if (imageUrl != null && imageUrl.length() > 0) {
                imageName = ImageHelpers.getImageNameFromUrl(imageUrl);

                if (isFileExistInDirectory(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER + File.separator + imageName)) {
                    unknownImage = ImageHelpers.getBufferImageFromFolder(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER, imageName);
                    getImage = true;
                } else if (ImageHelpers.isCropUrl(imageUrl)) {
                    unknownImage = loadThumbImageFromLocal();
                } else {
                    unknownImage = ImageHelpers.downloadImage(imageUrl, imageName, MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER);
                    getImage = true;
                }

                if (unknownImage != null) {
                    unknownImage = scaleToRoundedImage(size, size, unknownImage, size);
                    imageLabel.setIcon(new ImageIcon(unknownImage));
                    imageLabel.revalidate();
                    unknownImage.flush();
                    unknownImage = null;
                }
                if (!getImage) {
                    BufferedImage img = ImageHelpers.downloadImage(imageUrl, null, MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER);
                    if (img != null) {
                        img = scaleToRoundedImage(size, size, img, size);
                        imageLabel.setIcon(new ImageIcon(img));
                        imageLabel.revalidate();
                        img.flush();
                        img = null;

                    }
                }

                coverImagePanel.revalidate();
                coverImagePanel.repaint();

            }
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error in initContainers ==>" + e.getMessage());
            imageLabel.revalidate();
            coverImagePanel.revalidate();
            coverImagePanel.repaint();
        } finally {
            System.gc();
            if (unknownImage != null) {
                unknownImage.flush();
                unknownImage = null;
            }
        }

    }

    private void doWorkForThumbImage() {
        BufferedImage unknownImage = null;
        try {
            if (imageUrl != null && imageUrl.length() > 0) {
                imageName = ImageHelpers.getImageNameFromUrl(imageUrl);

                if (isFileExistInDirectory(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER + File.separator + imageName)) {
                    unknownImage = ImageHelpers.getBufferImageFromFolder(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER, imageName);
                } else {
                    unknownImage = ImageHelpers.downloadImage(imageUrl, imageName, MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER);
                }

                if (unknownImage != null) {
                    if (borderWidth > 0) {
                        unknownImage = scaleToRoundedImageWithBorder(size, size, unknownImage, size, borderWidth, borderColor);
                        borderWidth = 0;
                    } else {
                        unknownImage = scaleToRoundedImage(size, size, unknownImage, size);
                    }
                    imageLabel.setIcon(new ImageIcon(unknownImage));
                    imageLabel.revalidate();
                } else {
                    if (shortName != null && shortName.length() > 0) {
                        unknownImage = createDefaultBufferImage(size, shortName);
                    } else if (largeImage) {
                        unknownImage = DesignClasses.return_buffer_image(GetImages.UNKNOW_IMAGE);
                    } else if (size == 70) {
                        imageLabel.setIcon(ImageObjects.unknown_70);
                        // unknownImage = ImageHelpers.getUnknownImage70();
                    } else if (size == 39) {
                        imageLabel.setIcon(ImageObjects.unknown_39);
                        // unknownImage = DesignClasses.return_buffer_image(GetImages.UNKNOW_IMAGE_39);
                    } else if (size == 25) {
                        imageLabel.setIcon(ImageObjects.unknown_25);
                        // unknownImage = DesignClasses.return_buffer_image(GetImages.UNKNOW_IMAGE_25);
                    } else {
                        imageLabel.setIcon(ImageObjects.unknown_35);
                        //unknownImage = DesignClasses.return_buffer_image(GetImages.UNKNOW_IMAGE_35);
                    }
                }
                imageLabel.revalidate();
            }

        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error in doWorkForThumbImage ==>" + e.getMessage());
            imageLabel.revalidate();
        } finally {
            System.gc();
            if (unknownImage != null) {
                unknownImage.flush();
                unknownImage = null;
            }
        }
    }
    /* BufferedImage unknownImage = null;

     if (imageUrl != null && imageUrl.length() > 0) {
     imageName = ImageHelpers.getImageNameFromUrl(imageUrl);

     if (isFileExistInDirectory(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER + File.separator + imageName)) {
     unknownImage = ImageHelpers.getBufferImageFromFolder(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER, imageName);// ImageIO.read(new File(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER + File.separator + imageName));
     getImage = true;
     }
     if (ImageHelpers.isCropUrl(imageUrl)) {
     unknownImage = loadThumbImageFromLocal();
     }
     if (unknownImage == null) {
     unknownImage = ImageHelpers.downloadImage(imageUrl, imageName, MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER);
     getImage = true;
     }
     if (unknownImage == null && ImageHelpers.isCropUrl(imageUrl)) {
     unknownImage = loadThumbImage();
     }
     }

     if (unknownImage != null) {
     unknownImage = scaleToRoundedImage(size, size, unknownImage, size);
     imageLabel.setIcon(new ImageIcon(unknownImage));
     imageLabel.revalidate();
     unknownImage.flush();
     unknownImage = null;
     if (!getImage && ImageHelpers.isCropUrl(imageUrl)) {
     BufferedImage img = ImageHelpers.downloadImage(imageUrl, null, MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER);
     if (img != null) {
     img = scaleToRoundedImage(size, size, img, size);
     imageLabel.setIcon(new ImageIcon(img));
     imageLabel.revalidate();
     img.flush();
     img = null;
     if (coverImagePanel != null) {
     coverImagePanel.revalidate();
     coverImagePanel.repaint();
     }
     }
     }
     }*/
//}

    public void startDownloadThread(ViewProfileImageLoadListener listener) {
        new DownloadImageThread(listener).start();

    }

    public class DownloadImageThread extends Thread {

        private ViewProfileImageLoadListener listener;

        public DownloadImageThread(ViewProfileImageLoadListener listener) {
            setName("DownloadImageThread");
            this.listener = listener;
        }

        @Override
        public void run() {
            try {
                doWorkForThumbImage();
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Error in here ==>" + e.getMessage());
            } finally {
                System.gc();
                listener.onComplete();
            }

        }

    }

    public static BufferedImage scaleToRoundedImage(int w, int h, BufferedImage img, int arc) {
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        try {
            img = scaleImage(w, h, img);
            Graphics2D g2 = (Graphics2D) bi.createGraphics();
            g2.setComposite(AlphaComposite.Src);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));
            g2.setComposite(AlphaComposite.SrcAtop);
            g2.drawImage(img, 0, 0, null);

            g2.dispose();
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error in here ==>" + e.getMessage());
        }

        return bi;
    }

    public static BufferedImage scaleToRoundedImageWithBorder(int w, int h, BufferedImage img, int arc, int borderWidth, Color borderColor) {
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        try {
            img = scaleImage(w, h, img);
            Graphics2D g2 = (Graphics2D) bi.createGraphics();

            g2.setComposite(AlphaComposite.Src);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));

            g2.setComposite(AlphaComposite.SrcAtop);
            g2.drawImage(img, 0, 0, null);

            g2.setColor(borderColor);
            BasicStroke stroke = new BasicStroke(borderWidth);
            g2.setStroke(stroke);
            g2.draw(new RoundRectangle2D.Float(0.5F, 0, w - 1, h - 1, arc, arc));
            g2.dispose();

        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error in scaleToRoundedImageWithBorder ==>" + e.getMessage());
        }

        return bi;
    }

    public static BufferedImage scaleImage(int w, int h, BufferedImage img) {
        BufferedImage bi = null;
//        if (img != null) {
//            bi = Scalr.resize(img, Scalr.Mode.FIT_EXACT, w, h, Scalr.OP_ANTIALIAS);
//        }
        try {
            bi = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
            Graphics2D g2 = bi.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(img, 0, 0, w, h, null);
            g2.dispose();
        } catch (Exception e) {
        }
        return bi;
    }

    public boolean isFileExistInDirectory(String directory) {
        File f = new File(directory);
        return f.exists() && !f.isDirectory();
    }

    private BufferedImage loadThumbImageFromLocal() {
        BufferedImage unknownImage = null;
        try {
            String thmbImageUrl = ImageHelpers.convertCropToThumbUrl(imageUrl);
            String imageName = ImageHelpers.getImageNameFromUrl(thmbImageUrl);

            if (isFileExistInDirectory(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER + File.separator + imageName)) {
                unknownImage = ImageHelpers.getBufferImageFromFolder(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER, imageName);// ImageIO.read(new File(MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER + File.separator + imageName));
            }
        } catch (Exception e) {
        }
        return unknownImage;
    }

    private BufferedImage loadThumbImage() {
        BufferedImage unknownImage = null;
        try {
            String thmbImageUrl = ImageHelpers.convertCropToThumbUrl(imageUrl);
            String imageName = ImageHelpers.getImageNameFromUrl(thmbImageUrl);
            unknownImage = ImageHelpers.downloadImage(thmbImageUrl, imageName, MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER);
            //  new DownloadImageThread().start();
        } catch (Exception e) {
        }

        return unknownImage;
    }

}
