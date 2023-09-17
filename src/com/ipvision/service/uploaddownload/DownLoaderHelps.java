/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.uploaddownload;

import com.ipvision.constants.MyFnFSettings;
import java.io.File;

/**
 *
 * @author user
 */
public class DownLoaderHelps {

    private String profileDestinationFolder = MyFnFSettings.TEMP_PROFILE_IMAGE_FOLDER + File.separator;
    private String coverDestinationFolder = MyFnFSettings.TEMP_COVER_IMAGE_FOLDER + File.separator;
    private String bookDestinationFolder = MyFnFSettings.TEMP_BOOK_IMAGE_FOLDER + File.separator;
    private String chatFileDestinationFolder = MyFnFSettings.TEMP_CHAT_FILES_FOLDER + File.separator;
    private String emoticonDestinationFolder = MyFnFSettings.EMOTICON_FOLDER + File.separator;
    private String largeEmoticonDestinationFolder = MyFnFSettings.LARGE_EMOTICON_FOLDER + File.separator;
    private String sticketDestinationFolder = MyFnFSettings.STICKER_FOLDER + File.separator;
    private String chatBackgroundFolder = MyFnFSettings.CHAT_BG_FOLDER + File.separator;
    private String brokenFilesFolder = MyFnFSettings.TEMP_BROKEN_FILES_FOLDER + File.separator;

    public String getBookDestinationFolder() {
        return bookDestinationFolder;
    }

    public void setBookDestinationFolder(String bookDestinationFolder) {
        this.bookDestinationFolder = bookDestinationFolder;
    }

    public String getEmoticonDestinationFolder() {
        return emoticonDestinationFolder;
    }

    public void setEmoticonDestinationFolder(String emoticonDestinationFolder) {
        this.emoticonDestinationFolder = emoticonDestinationFolder;
    }

    public String getSticketDestinationFolder() {
        return sticketDestinationFolder;
    }

    public void setSticketDestinationFolder(String sticketDestinationFolder) {
        this.sticketDestinationFolder = sticketDestinationFolder;
    }

    public String getCoverDestinationFolder() {
        return coverDestinationFolder;
    }

    public void setCoverDestinationFolder(String coverDestinationFolder) {
        this.coverDestinationFolder = coverDestinationFolder;
    }

    public String getProfileDestinationFolder() {
        return profileDestinationFolder;
    }

    public void setProfileDestinationFolder(String profileDestinationFolder) {
        this.profileDestinationFolder = profileDestinationFolder;
    }

    public String getChatBackgroundFolder() {
        return chatBackgroundFolder;
    }

    public void setChatBackgroundFolder(String chatBackgroundFolder) {
        this.chatBackgroundFolder = chatBackgroundFolder;
    }

    public String getChatFileDestinationFolder() {
        return chatFileDestinationFolder;
    }

    public void setChatFileDestinationFolder(String chatFileDestinationFolder) {
        this.chatFileDestinationFolder = chatFileDestinationFolder;
    }

    public String getBrokenFilesFolder() {
        return brokenFilesFolder;
    }

    public void setBrokenFilesFolder(String brokenFilesFolder) {
        this.brokenFilesFolder = brokenFilesFolder;
    }

    public String getLargeEmoticonDestinationFolder() {
        return largeEmoticonDestinationFolder;
    }

    public void setLargeEmoticonDestinationFolder(String largeEmoticonDestinationFolder) {
        this.largeEmoticonDestinationFolder = largeEmoticonDestinationFolder;
    }
    
}
