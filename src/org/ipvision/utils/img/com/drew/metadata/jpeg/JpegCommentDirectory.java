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
import org.ipvision.utils.img.com.drew.metadata.Directory;

import java.util.HashMap;

/**
 * Describes tags used by a JPEG file comment.
 *
 * @author Drew Noakes http://drewnoakes.com
 */
public class JpegCommentDirectory extends Directory
{
    /**
     * This value does not apply to a particular standard. Rather, this value has been fabricated to maintain
     * consistency with other directory types.
     */
    public static final int TAG_JPEG_COMMENT = 0;

    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<Integer, String>();

    static {
        _tagNameMap.put(TAG_JPEG_COMMENT, "Jpeg Comment");
    }

    public JpegCommentDirectory()
    {
        this.setDescriptor(new JpegCommentDescriptor(this));
    }

    @NotNull
    public String getName()
    {
        return "JpegComment";
    }

    @NotNull
    protected HashMap<Integer, String> getTagNameMap()
    {
        return _tagNameMap;
    }
}
