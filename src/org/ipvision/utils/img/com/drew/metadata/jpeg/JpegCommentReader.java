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
package org.ipvision.utils.img.com.drew.metadata.jpeg;

import org.ipvision.utils.img.com.drew.lang.BufferBoundsException;
import org.ipvision.utils.img.com.drew.lang.BufferReader;
import org.ipvision.utils.img.com.drew.lang.annotations.NotNull;
import org.ipvision.utils.img.com.drew.metadata.Metadata;
import org.ipvision.utils.img.com.drew.metadata.MetadataReader;

/**
 * Decodes the comment stored within Jpeg files, populating a
 * <code>Metadata</code> object with tag values in a
 * <code>JpegCommentDirectory</code>.
 *
 * @author Drew Noakes http://drewnoakes.com
 */
public class JpegCommentReader implements MetadataReader {

    /**
     * Performs the Jpeg data extraction, adding found values to the specified
     * instance of <code>Metadata</code>.
     */

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JpegCommentReader.class);

    public void extract(@NotNull final BufferReader reader, @NotNull Metadata metadata) {
        JpegCommentDirectory directory = metadata.getOrCreateDirectory(JpegCommentDirectory.class);

        try {
            directory.setString(JpegCommentDirectory.TAG_JPEG_COMMENT, reader.getString(0, (int) reader.getLength()));
        } catch (BufferBoundsException e) {
           // directory.addError("Exception reading JPEG comment string");
          log.error("Error in JpegCommentReader class " + e.getMessage());
        }
    }
}
