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
package org.ipvision.utils.img.com.drew.metadata;

import org.ipvision.utils.img.com.drew.lang.annotations.NotNull;
import org.ipvision.utils.img.com.drew.lang.annotations.Nullable;

/**
 * Models a particular tag within a directory and provides methods for obtaining its value.  Note that a Tag instance is
 * specific to a particular metadata extraction and cannot be reused.
 *
 * @author Drew Noakes http://drewnoakes.com
 */
public class Tag
{
    private final int _tagType;
    @NotNull
    private final Directory _directory;

    public Tag(int tagType, @NotNull Directory directory)
    {
        _tagType = tagType;
        _directory = directory;
    }

    /**
     * Gets the tag type as an int
     *
     * @return the tag type as an int
     */
    public int getTagType()
    {
        return _tagType;
    }

    /**
     * Gets the tag type in hex notation as a String with padded leading
     * zeroes if necessary (i.e. <code>0x100E</code>).
     *
     * @return the tag type as a string in hexadecimal notation
     */
    @NotNull
    public String getTagTypeHex()
    {
        String hex = Integer.toHexString(_tagType);
        while (hex.length() < 4) hex = "0" + hex;
        return "0x" + hex;
    }

    /**
     * Get a description of the tag's value, considering enumerated values
     * and units.
     *
     * @return a description of the tag's value
     */
    @Nullable
    public String getDescription()
    {
        return _directory.getDescription(_tagType);
    }

    /**
     * Get the name of the tag, such as <code>Aperture</code>, or
     * <code>InteropVersion</code>.
     *
     * @return the tag's name
     */
    @NotNull
    public String getTagName()
    {
        return _directory.getTagName(_tagType);
    }

    /**
     * Get the name of the directory in which the tag exists, such as
     * <code>Exif</code>, <code>GPS</code> or <code>Interoperability</code>.
     *
     * @return name of the directory in which this tag exists
     */
    @NotNull
    public String getDirectoryName()
    {
        return _directory.getName();
    }

    /**
     * A basic representation of the tag's type and value.  EG: <code>[FNumber] F2.8</code>.
     *
     * @return the tag's type and value
     */
    @NotNull
    public String toString()
    {
        String description = getDescription();
        if (description==null)
            description = _directory.getString(getTagType()) + " (unable to formulate description)";
        return "[" + _directory.getName() + "] " + getTagName() + " - " + description;
    }
}
