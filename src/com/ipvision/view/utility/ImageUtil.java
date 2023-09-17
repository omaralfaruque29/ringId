/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/**
 *
 * @author user
 */
public class ImageUtil {

    public static String lineEnd = "\r\n";
    public static String twoHyphens = "--";
    public static String boundary = "*****";

    public static BufferedImage resizeImageWithHint(double size, BufferedImage originalImage) {
        double width;
        double ratio;
        double height;
        if (originalImage.getWidth() >= originalImage.getHeight()) {
            if (size == -1 || (size > originalImage.getWidth())) {
                width = originalImage.getWidth();
                height = originalImage.getHeight();
            } else {
                width = size;
                ratio = originalImage.getWidth() / size;
                height = originalImage.getHeight() / ratio;
            }
        } else {
            if (size == -1 || (size > originalImage.getHeight())) {
                width = originalImage.getWidth();
                height = originalImage.getHeight();
            } else {
                height = size;
                ratio = originalImage.getHeight() / size;
                width = originalImage.getWidth() / ratio;
            }
        }

        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        BufferedImage resizedImage = new BufferedImage((int) width, (int) height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, (int) width, (int) height, null);
        g.dispose();

        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        return resizedImage;
    }

    public static void addDefaultDataWithImage(DataOutputStream dos) {
        try {
            dos.writeBytes("Content-Disposition: form-data; name=\"sId\"" + lineEnd + lineEnd + MyFnFSettings.LOGIN_SESSIONID + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uId\"" + lineEnd + lineEnd + MyFnFSettings.LOGIN_USER_ID + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"authServer\"" + lineEnd + lineEnd + ServerAndPortSettings.AUTH_SERVER_IP + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"authPort\"" + lineEnd + lineEnd + ServerAndPortSettings.AUTHENTICATION_PORT + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
        } catch (IOException ex) {
            Logger.getLogger(ImageUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static byte[] imageToByteArray(BufferedImage originalImage, String extension) {
        byte[] imageByte = null;
        try {
            extension = "jpeg";
            ImageWriter writer;
            ByteArrayOutputStream baos = null;
            ImageOutputStream ios = null;
            try {
                Iterator iter = ImageIO.getImageWritersByFormatName(extension);
                writer = (ImageWriter) iter.next();
                ImageWriteParam iwp = writer.getDefaultWriteParam();
                iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                baos = new ByteArrayOutputStream(37628);
                ios = ImageIO.createImageOutputStream(baos);
                writer.setOutput(ios);
                IIOImage image = new IIOImage(originalImage, null, null);
                writer.write(null, image, iwp);
                writer.dispose();
                imageByte = baos.toByteArray();
            } catch (IOException e) {
            } finally {
                try {
                    if (baos != null) {
                        baos.close();
                    }

                    if (ios != null) {
                        ios.close();
                    }

                } catch (IOException e) {
                }

            }
        } catch (Exception e) {
        }

        return imageByte;
    }

    public static BufferedImage reduceQualityAndSize(BufferedImage originalImage, double width) {
        float default_quality = 0.75f;
        try {
            String extension = "jpeg";
            ImageWriter writer;
            ByteArrayOutputStream baos = null;
            ImageOutputStream ios = null;
            try {
                originalImage = resizeImageWithHint(width, originalImage);
                Iterator iter = ImageIO.getImageWritersByFormatName(extension);
                writer = (ImageWriter) iter.next();
                ImageWriteParam iwp = writer.getDefaultWriteParam();
                iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                if (originalImage.getWidth() > width) {
                    default_quality = 0.8f;
                }
                iwp.setCompressionQuality(default_quality);
                baos = new ByteArrayOutputStream(37628);
                ios = ImageIO.createImageOutputStream(baos);
                writer.setOutput(ios);
                IIOImage image = new IIOImage(originalImage, null, null);
                writer.write(null, image, iwp);
                writer.dispose();

            } catch (IOException e) {
            } finally {
                try {
                    if (baos != null) {
                        baos.close();
                    }

                    if (ios != null) {
                        ios.close();
                    }

                } catch (IOException e) {
                }

            }
        } catch (Exception e) {
        }

        return originalImage;
    }

    public static byte[] reduceQualityAndSize(BufferedImage originalImage, double width, String extension) {
        byte[] imageByte = null;
        float default_quality = 0.75f;
        try {
            extension = "jpeg";
            ImageWriter writer;
            ByteArrayOutputStream baos = null;
            ImageOutputStream ios = null;
            try {
                originalImage = resizeImageWithHint(width, originalImage);
                Iterator iter = ImageIO.getImageWritersByFormatName(extension);
                writer = (ImageWriter) iter.next();
                ImageWriteParam iwp = writer.getDefaultWriteParam();
                iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                if (originalImage.getWidth() > width) {
                    default_quality = 0.8f;
                }
                iwp.setCompressionQuality(default_quality);
                baos = new ByteArrayOutputStream(37628);
                ios = ImageIO.createImageOutputStream(baos);
                writer.setOutput(ios);
                IIOImage image = new IIOImage(originalImage, null, null);
                writer.write(null, image, iwp);
                writer.dispose();
                imageByte = baos.toByteArray();
            } catch (IOException e) {
            } finally {
                try {
                    if (baos != null) {
                        baos.close();
                    }

                    if (ios != null) {
                        ios.close();
                    }

                } catch (IOException e) {
                }

            }
        } catch (Exception e) {
        }

        return imageByte;

    }

    public static byte[] reduceQuality(BufferedImage originalImage, String extension) {
        byte[] imageByte = null;
        float default_quality = 0.75f;
        try {
            extension = "jpeg";
            ImageWriter writer;
            ByteArrayOutputStream baos = null;
            ImageOutputStream ios = null;
            try {
                Iterator iter = ImageIO.getImageWritersByFormatName(extension);
                writer = (ImageWriter) iter.next();
                ImageWriteParam iwp = writer.getDefaultWriteParam();
                iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                iwp.setCompressionQuality(default_quality);
                baos = new ByteArrayOutputStream(37628);
                ios = ImageIO.createImageOutputStream(baos);
                writer.setOutput(ios);
                IIOImage image = new IIOImage(originalImage, null, null);
                writer.write(null, image, iwp);
                writer.dispose();
                imageByte = baos.toByteArray();
            } catch (IOException e) {
            } finally {
                try {
                    if (baos != null) {
                        baos.close();
                    }

                    if (ios != null) {
                        ios.close();
                    }

                } catch (IOException e) {
                }

            }
        } catch (Exception e) {
        }

        return imageByte;

    }
}
