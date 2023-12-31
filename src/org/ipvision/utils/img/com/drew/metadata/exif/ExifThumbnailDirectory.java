/*
 * Copyright 2002-2012 Drew Noakes
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * More information about this project is available at:
 *
 *    http://drewnoakes.com/code/exif/
 *    http://code.google.com/p/metadata-extractor/
 */

package org.ipvision.utils.img.com.drew.metadata.exif;

import org.ipvision.utils.img.com.drew.lang.annotations.NotNull;
import org.ipvision.utils.img.com.drew.lang.annotations.Nullable;
import org.ipvision.utils.img.com.drew.metadata.Directory;
import org.ipvision.utils.img.com.drew.metadata.MetadataException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * One of several Exif directories.  Otherwise known as IFD1, this directory holds information about an embedded thumbnail image.
 *
 * @author Drew Noakes http://drewnoakes.com
 */
public class ExifThumbnailDirectory extends Directory
{
    public static final int TAG_THUMBNAIL_IMAGE_WIDTH = 0x0100;
    public static final int TAG_THUMBNAIL_IMAGE_HEIGHT = 0x0101;

    /**
     * When image format is no compression, this value shows the number of bits
     * per component for each pixel. Usually this value is '8,8,8'.
     */
    public static final int TAG_BITS_PER_SAMPLE = 0x0102;

    /**
     * Shows compression method for Thumbnail.
     * 1 = Uncompressed
     * 2 = CCITT 1D
     * 3 = T4/Group 3 Fax
     * 4 = T6/Group 4 Fax
     * 5 = LZW
     * 6 = JPEG (old-style)
     * 7 = JPEG
     * 8 = Adobe Deflate
     * 9 = JBIG B&W
     * 10 = JBIG Color
     * 32766 = Next
     * 32771 = CCIRLEW
     * 32773 = PackBits
     * 32809 = Thunderscan
     * 32895 = IT8CTPAD
     * 32896 = IT8LW
     * 32897 = IT8MP
     * 32898 = IT8BL
     * 32908 = PixarFilm
     * 32909 = PixarLog
     * 32946 = Deflate
     * 32947 = DCS
     * 34661 = JBIG
     * 34676 = SGILog
     * 34677 = SGILog24
     * 34712 = JPEG 2000
     * 34713 = Nikon NEF Compressed
     */
    public static final int TAG_THUMBNAIL_COMPRESSION = 0x0103;

    /**
     * Shows the color space of the image data components.
     * 0 = WhiteIsZero
     * 1 = BlackIsZero
     * 2 = RGB
     * 3 = RGB Palette
     * 4 = Transparency Mask
     * 5 = CMYK
     * 6 = YCbCr
     * 8 = CIELab
     * 9 = ICCLab
     * 10 = ITULab
     * 32803 = Color Filter Array
     * 32844 = Pixar LogL
     * 32845 = Pixar LogLuv
     * 34892 = Linear Raw
     */
    public static final int TAG_PHOTOMETRIC_INTERPRETATION = 0x0106;

    /** The position in the file of raster data. */
    public static final int TAG_STRIP_OFFSETS = 0x0111;
    public static final int TAG_ORIENTATION = 0x0112;
    /** Each pixel is composed of this many samples. */
    public static final int TAG_SAMPLES_PER_PIXEL = 0x0115;
    /** The raster is codified by a single block of data holding this many rows. */
    public static final int TAG_ROWS_PER_STRIP = 0x116;
    /** The size of the raster data in bytes. */
    public static final int TAG_STRIP_BYTE_COUNTS = 0x0117;
    /**
     * When image format is no compression YCbCr, this value shows byte aligns of
     * YCbCr data. If value is '1', Y/Cb/Cr value is chunky format, contiguous for
     * each subsampling pixel. If value is '2', Y/Cb/Cr value is separated and
     * stored to Y plane/Cb plane/Cr plane format.
     */
    public static final int TAG_X_RESOLUTION = 0x011A;
    public static final int TAG_Y_RESOLUTION = 0x011B;
    public static final int TAG_PLANAR_CONFIGURATION = 0x011C;
    public static final int TAG_RESOLUTION_UNIT = 0x0128;
    /** The offset to thumbnail image bytes. */
    public static final int TAG_THUMBNAIL_OFFSET = 0x0201;
    /** The size of the thumbnail image data in bytes. */
    public static final int TAG_THUMBNAIL_LENGTH = 0x0202;
    public static final int TAG_YCBCR_COEFFICIENTS = 0x0211;
    public static final int TAG_YCBCR_SUBSAMPLING = 0x0212;
    public static final int TAG_YCBCR_POSITIONING = 0x0213;
    public static final int TAG_REFERENCE_BLACK_WHITE = 0x0214;

    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<Integer, String>();

    static
    {
        _tagNameMap.put(TAG_THUMBNAIL_IMAGE_WIDTH, "Thumbnail Image Width");
        _tagNameMap.put(TAG_THUMBNAIL_IMAGE_HEIGHT, "Thumbnail Image Height");
        _tagNameMap.put(TAG_BITS_PER_SAMPLE, "Bits Per Sample");
        _tagNameMap.put(TAG_THUMBNAIL_COMPRESSION, "Thumbnail Compression");
        _tagNameMap.put(TAG_PHOTOMETRIC_INTERPRETATION, "Photometric Interpretation");
        _tagNameMap.put(TAG_STRIP_OFFSETS, "Strip Offsets");
        _tagNameMap.put(TAG_ORIENTATION, "Orientation");
        _tagNameMap.put(TAG_SAMPLES_PER_PIXEL, "Samples Per Pixel");
        _tagNameMap.put(TAG_ROWS_PER_STRIP, "Rows Per Strip");
        _tagNameMap.put(TAG_STRIP_BYTE_COUNTS, "Strip Byte Counts");
        _tagNameMap.put(TAG_X_RESOLUTION, "X Resolution");
        _tagNameMap.put(TAG_Y_RESOLUTION, "Y Resolution");
        _tagNameMap.put(TAG_PLANAR_CONFIGURATION, "Planar Configuration");
        _tagNameMap.put(TAG_RESOLUTION_UNIT, "Resolution Unit");
        _tagNameMap.put(TAG_THUMBNAIL_OFFSET, "Thumbnail Offset");
        _tagNameMap.put(TAG_THUMBNAIL_LENGTH, "Thumbnail Length");
        _tagNameMap.put(TAG_YCBCR_COEFFICIENTS, "YCbCr Coefficients");
        _tagNameMap.put(TAG_YCBCR_SUBSAMPLING, "YCbCr Sub-Sampling");
        _tagNameMap.put(TAG_YCBCR_POSITIONING, "YCbCr Positioning");
        _tagNameMap.put(TAG_REFERENCE_BLACK_WHITE, "Reference Black/White");
    }

    @Nullable
    private byte[] _thumbnailData;

    public ExifThumbnailDirectory()
    {
        this.setDescriptor(new ExifThumbnailDescriptor(this));
    }

    @NotNull
    public String getName()
    {
        return "Exif Thumbnail";
    }

    @NotNull
    protected HashMap<Integer, String> getTagNameMap()
    {
        return _tagNameMap;
    }

    public boolean hasThumbnailData()
    {
        return _thumbnailData != null;
    }

    @Nullable
    public byte[] getThumbnailData()
    {
        return _thumbnailData;
    }

    public void setThumbnailData(@Nullable byte[] data)
    {
        _thumbnailData = data;
    }

    public void writeThumbnail(@NotNull String filename) throws MetadataException, IOException
    {
        byte[] data = _thumbnailData;

        if (data==null)
            throw new MetadataException("No thumbnail data exists.");

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(filename);
            stream.write(data);
        } finally {
            if (stream!=null)
                stream.close();
        }
    }

/*
    // This thumbnail extraction code is not complete, and is included to assist anyone who feels like looking into
    // it.  Please share any progress with the original author, and hence the community.  Thanks.

    public Image getThumbnailImage() throws MetadataException
    {
        if (!hasThumbnailData())
            return null;

        int compression = 0;
        try {
            compression = this.getInt(ExifSubIFDDirectory.TAG_COMPRESSION);
        } catch (Throwable e) {
            this.addError("Unable to determine thumbnail type " + e.getMessage());
        }

        final byte[] thumbnailBytes = getThumbnailData();

        if (compression == ExifSubIFDDirectory.COMPRESSION_JPEG)
        {
            // JPEG Thumbnail
            // operate directly on thumbnailBytes
            return decodeBytesAsImage(thumbnailBytes);
        }
        else if (compression == ExifSubIFDDirectory.COMPRESSION_NONE)
        {
            // uncompressed thumbnail (raw RGB data)
            if (!this.containsTag(ExifSubIFDDirectory.TAG_PHOTOMETRIC_INTERPRETATION))
                return null;

            try
            {
                // If the image is RGB format, then convert it to a bitmap
                final int photometricInterpretation = this.getInt(ExifSubIFDDirectory.TAG_PHOTOMETRIC_INTERPRETATION);
                if (photometricInterpretation == ExifSubIFDDirectory.PHOTOMETRIC_INTERPRETATION_RGB)
                {
                    // RGB
                    Image image = createImageFromRawRgb(thumbnailBytes);
                    return image;
                }
                else if (photometricInterpretation == ExifSubIFDDirectory.PHOTOMETRIC_INTERPRETATION_YCBCR)
                {
                    // YCbCr
                    Image image = createImageFromRawYCbCr(thumbnailBytes);
                    return image;
                }
                else if (photometricInterpretation == ExifSubIFDDirectory.PHOTOMETRIC_INTERPRETATION_MONOCHROME)
                {
                    // Monochrome
                    return null;
                }
            } catch (Throwable e) {
                this.addError("Unable to extract thumbnail: " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * Handle the YCbCr thumbnail encoding used by Ricoh RDC4200/4300, Fuji DS-7/300 and DX-5/7/9 cameras.
     *
     * At DX-5/7/9, YCbCrSubsampling(0x0212) has values of '2,1', PlanarConfiguration(0x011c) has a value '1'. So the
     * data align of this image is below.
     *
     * Y(0,0),Y(1,0),Cb(0,0),Cr(0,0), Y(2,0),Y(3,0),Cb(2,0),Cr(3.0), Y(4,0),Y(5,0),Cb(4,0),Cr(4,0). . . .
     *
     * The numbers in parenthesis are pixel coordinates. DX series' YCbCrCoefficients(0x0211) has values '0.299/0.587/0.114',
     * ReferenceBlackWhite(0x0214) has values '0,255,128,255,128,255'. Therefore to convert from Y/Cb/Cr to RGB is;
     *
     * B(0,0)=(Cb-128)*(2-0.114*2)+Y(0,0)
     * R(0,0)=(Cr-128)*(2-0.299*2)+Y(0,0)
     * G(0,0)=(Y(0,0)-0.114*B(0,0)-0.299*R(0,0))/0.587
     *
     * Horizontal subsampling is a value '2', so you can calculate B(1,0)/R(1,0)/G(1,0) by using the Y(1,0) and Cr(0,0)/Cb(0,0).
     * Repeat this conversion by value of ImageWidth(0x0100) and ImageLength(0x0101).
     *
     * @param thumbnailBytes
     * @return
     * @throws com.drew.metadata.MetadataException
     * /
    private Image createImageFromRawYCbCr(byte[] thumbnailBytes) throws MetadataException
    {
        /*
            Y  =  0.257R + 0.504G + 0.098B + 16
            Cb = -0.148R - 0.291G + 0.439B + 128
            Cr =  0.439R - 0.368G - 0.071B + 128

            G = 1.164(Y-16) - 0.391(Cb-128) - 0.813(Cr-128)
            R = 1.164(Y-16) + 1.596(Cr-128)
            B = 1.164(Y-16) + 2.018(Cb-128)

            R, G and B range from 0 to 255.
            Y ranges from 16 to 235.
            Cb and Cr range from 16 to 240.

            http://www.faqs.org/faqs/graphics/colorspace-faq/
        * /

        int length = thumbnailBytes.length; // this.getInt(ExifSubIFDDirectory.TAG_STRIP_BYTE_COUNTS);
        final int imageWidth = this.getInt(ExifSubIFDDirectory.TAG_THUMBNAIL_IMAGE_WIDTH);
        final int imageHeight = this.getInt(ExifSubIFDDirectory.TAG_THUMBNAIL_IMAGE_HEIGHT);
//        final int headerLength = 54;
//        byte[] result = new byte[length + headerLength];
//        // Add a windows BMP header described:
//        // http://www.onicos.com/staff/iz/formats/bmp.html
//        result[0] = 'B';
//        result[1] = 'M'; // File Type identifier
//        result[3] = (byte)(result.length / 256);
//        result[2] = (byte)result.length;
//        result[10] = (byte)headerLength;
//        result[14] = 40; // MS Windows BMP header
//        result[18] = (byte)imageWidth;
//        result[22] = (byte)imageHeight;
//        result[26] = 1;  // 1 Plane
//        result[28] = 24; // Colour depth
//        result[34] = (byte)length;
//        result[35] = (byte)(length / 256);

        final BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        // order is YCbCr and image is upside down, bitmaps are BGR
////        for (int i = headerLength, dataOffset = length; i<result.length; i += 3, dataOffset -= 3)
//        {
//            final int y =  thumbnailBytes[dataOffset - 2] & 0xFF;
//            final int cb = thumbnailBytes[dataOffset - 1] & 0xFF;
//            final int cr = thumbnailBytes[dataOffset] & 0xFF;
//            if (y<16 || y>235 || cb<16 || cb>240 || cr<16 || cr>240)
//                "".toString();
//
//            int g = (int)(1.164*(y-16) - 0.391*(cb-128) - 0.813*(cr-128));
//            int r = (int)(1.164*(y-16) + 1.596*(cr-128));
//            int b = (int)(1.164*(y-16) + 2.018*(cb-128));
//
////            result[i] = (byte)b;
////            result[i + 1] = (byte)g;
////            result[i + 2] = (byte)r;
//
//            // TODO compose the image here
//            image.setRGB(1, 2, 3);
//        }

        return image;
    }

    /**
     * Creates a thumbnail image in (Windows) BMP format from raw RGB data.
     * @param thumbnailBytes
     * @return
     * @throws com.drew.metadata.MetadataException
     * /
    private Image createImageFromRawRgb(byte[] thumbnailBytes) throws MetadataException
    {
        final int length = thumbnailBytes.length; // this.getInt(ExifSubIFDDirectory.TAG_STRIP_BYTE_COUNTS);
        final int imageWidth = this.getInt(ExifSubIFDDirectory.TAG_THUMBNAIL_IMAGE_WIDTH);
        final int imageHeight = this.getInt(ExifSubIFDDirectory.TAG_THUMBNAIL_IMAGE_HEIGHT);
//        final int headerLength = 54;
//        final byte[] result = new byte[length + headerLength];
//        // Add a windows BMP header described:
//        // http://www.onicos.com/staff/iz/formats/bmp.html
//        result[0] = 'B';
//        result[1] = 'M'; // File Type identifier
//        result[3] = (byte)(result.length / 256);
//        result[2] = (byte)result.length;
//        result[10] = (byte)headerLength;
//        result[14] = 40; // MS Windows BMP header
//        result[18] = (byte)imageWidth;
//        result[22] = (byte)imageHeight;
//        result[26] = 1;  // 1 Plane
//        result[28] = 24; // Colour depth
//        result[34] = (byte)length;
//        result[35] = (byte)(length / 256);

        final BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        // order is RGB and image is upside down, bitmaps are BGR
//        for (int i = headerLength, dataOffset = length; i<result.length; i += 3, dataOffset -= 3)
//        {
//            byte b = thumbnailBytes[dataOffset - 2];
//            byte g = thumbnailBytes[dataOffset - 1];
//            byte r = thumbnailBytes[dataOffset];
//
//            // TODO compose the image here
//            image.setRGB(1, 2, 3);
//        }

        return image;
    }
*/
}
