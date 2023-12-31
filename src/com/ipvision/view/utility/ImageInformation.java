/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import org.ipvision.utils.img.com.drew.imaging.ImageMetadataReader;
import org.ipvision.utils.img.com.drew.metadata.Directory;
import org.ipvision.utils.img.com.drew.metadata.Metadata;
import org.ipvision.utils.img.com.drew.metadata.exif.ExifIFD0Directory;
import org.ipvision.utils.img.com.drew.metadata.jpeg.JpegDirectory;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 *
 * @author Wasif Islam
 */
public class ImageInformation {

    public final int orientation;
    public final int width;
    public final int height;

    public ImageInformation(int orientation, int width, int height) {
        this.orientation = orientation;
        this.width = width;
        this.height = height;
    }

    public String toString() {
        return String.format("%dx%d,%d", this.width, this.height, this.orientation);
    }

    public static ImageInformation readImageFileInformation(File imageFile) {
        Metadata metadata = null;
        int orientation = 1;
        int width = 0;
        int height = 0;
        try {
            metadata = ImageMetadataReader.readMetadata(imageFile);
            Directory directory = metadata.getDirectory(ExifIFD0Directory.class);
            JpegDirectory jpegDirectory = (JpegDirectory) metadata.getDirectory(JpegDirectory.class);
            orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            width = jpegDirectory.getImageWidth();
            height = jpegDirectory.getImageHeight();
        } catch (Exception me) {
        }
        return new ImageInformation(orientation, width, height);
    }

    public static AffineTransform getExifTransformation(ImageInformation info) {

        AffineTransform t = new AffineTransform();

        switch (info.orientation) {
            case 1:
                break;
            case 2: // Flip X
                t.scale(-1.0, 1.0);
                t.translate(-info.width, 0);
                break;
            case 3: // PI rotation 
                t.translate(info.width, info.height);
                t.rotate(Math.PI);
                break;
            case 4: // Flip Y
                t.scale(1.0, -1.0);
                t.translate(0, -info.height);
                break;
            case 5: // - PI/2 and Flip X
                t.rotate(-Math.PI / 2);
                t.scale(-1.0, 1.0);
                break;
            case 6: // -PI/2 and -width
                t.translate(info.height, 0);
                t.rotate(Math.PI / 2);
                break;
            case 7: // PI/2 and Flip
                t.scale(-1.0, 1.0);
                t.translate(-info.height, 0);
                t.translate(0, info.width);
                t.rotate(3 * Math.PI / 2);
                break;
            case 8: // PI / 2
                t.translate(0, info.width);
                t.rotate(3 * Math.PI / 2);
                break;
        }

        return t;
    }

    public static BufferedImage transformImage(BufferedImage image, AffineTransform transform) throws Exception {
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);

        BufferedImage destinationImage = op.createCompatibleDestImage(image, (image.getType() == BufferedImage.TYPE_BYTE_GRAY) ? image.getColorModel() : null);
        Graphics2D g = destinationImage.createGraphics();
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, destinationImage.getWidth(), destinationImage.getHeight());
        destinationImage = op.filter(image, destinationImage);
        return destinationImage;
    }

}
