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

import org.ipvision.utils.img.com.drew.lang.annotations.NotNull;
import org.ipvision.utils.img.com.drew.lang.annotations.Nullable;
import org.ipvision.utils.img.com.drew.metadata.TagDescriptor;

/**
 * Provides human-readable string representations of tag values stored in a <code>JpegCommentDirectory</code>.
 *
 * @author Drew Noakes http://drewnoakes.com
 */
public class JpegCommentDescriptor extends TagDescriptor<JpegCommentDirectory>
{
    public JpegCommentDescriptor(@NotNull JpegCommentDirectory directory)
    {
        super(directory);
    }

    @Nullable
    public String getJpegCommentDescription()
    {
        return _directory.getString(JpegCommentDirectory.TAG_JPEG_COMMENT);
    }
}
