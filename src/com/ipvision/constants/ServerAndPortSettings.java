/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.constants;

import com.ipvision.service.utility.HttpRequest;

/**
 *
 * @author user
 */
public class ServerAndPortSettings {

    public final static String COMMUNICATION_SERVER_HOST = "auth1.ringid.com";
    public final static String IMAGE_SERVER_HOST = "image.ringid.com";
    /**
     * http://38.127.68.54/ringid
     */
///38.127.68.56
    public static String AUTH_SERVER_IP = null;
    public static int CHAT_PORT = 9992;
    public static int SMS_PORT = 9993;
    public static int CONFIRMATION_PORT = 9994;
    public static int UPDATE_PORT = 9995;
    public static int REQUEST_PORT = 9996;
    public static int VOICE_PORT = 99987;
    public static int AUTHENTICATION_PORT = 0;
    public static int KEEP_ALIVE_PORT = 9999;
    public static int DATA_SIZE = 500;
    public static int HEADER_SIZE = 12;

    /*
     "voicePort": 9166,
     "confirmationPort": 9162,
     "requestPort": 9163,
     "updatePort": 9164,
     "chatPort": 9165,
     "authPort": 9160,
     "keepAlivePort": 9161,
     "dataSize": 500,
     "headerSize": 12,
     "authServerIP": "38.108.92.243"
     */
    /*public static String getCommunicationPortsByRingID(String dialingCode, String phoneNumber) {
     String url = "http://" + COMMUNICATION_SERVER_HOST + "/rac/comports";
     return HttpRequest.getJsonByRingID(url, dialingCode, phoneNumber);
     }*/
    public static String getCommunicationPortsByRingID(String ringId) {
        return HttpRequest.getJsonByRingID(getComportURL(), ringId);
    }

    public static String getCommunicationPortsByEmail(String email) {
        return HttpRequest.getJsonByEmail(getComportURL(), email);
    }

    public static String getCommunicationPortsByMobile(String mobile, String mblDc) {
        return HttpRequest.getJsonByMobile(getComportURL(), mobile, mblDc);
    }

    private static String getComportURL() {
        //  return "http://" + COMMUNICATION_SERVER_HOST + "/comports";
        return makeAuthUrl() + "comports";
    }

    public static String getImageServerBase() {
        // public final static String IMAGE_SERVER_BASE = "http://" + IMAGE_SERVER_HOST + "/uploaded/";
        return "http://" + IMAGE_SERVER_HOST + "/uploaded/";
    }

    private static String makeAuthUrl() {
        //  return "http://" + COMMUNICATION_SERVER_HOST + "/comports";
        return "http://" + COMMUNICATION_SERVER_HOST + "/rac/";
    }

    public static String getNewRingID() {
        //  String url = "http://" + COMMUNICATION_SERVER_HOST + "/ringid";
        // String url = "http://" + COMMUNICATION_SERVER_HOST + "/rac/ringid";
        String url = makeAuthUrl() + "ringid";
        return HttpRequest.getRingID(url);
    }

    public static String getPingURL() {
        ////public static String PING_SERVER_HOST = "http://auth1.ringid.com/rac/ping";
        return makeAuthUrl() + "ping";
    }

    public static String getProfileImageUploadingURL() {
        return "http://" + IMAGE_SERVER_HOST + "/ringmarket/ImageUploadHandler";
    }

    public static String getCoverImageUploadingURL() {
        return "http://" + IMAGE_SERVER_HOST + "/ringmarket/CoverImageUploadHandler";
    }

    public static String getAlbumImageUploadingURL() {
        return "http://" + IMAGE_SERVER_HOST + "/ringmarket/AlbumImageUploadHandler";
    }

    public static String getRingStickerMarketServiceUrl() {
        return "http://" + IMAGE_SERVER_HOST + "/ringmarket/StickerHandler";
    }

    public static String getRingStickerMarketIServiceUrl() {
        return "http://" + IMAGE_SERVER_HOST + "/ringmarket/IStickeHandler";
    }

    public static String getRingStickerMarketDownloadUrl() {
        return "http://" + IMAGE_SERVER_HOST + "/stickermarket/d1";
    }

    public static String getChatBackgroundImageServiceUrl() {
        return "http://" + IMAGE_SERVER_HOST + "/ringmarket/ChatBgHandler";
    }

    public static String getChatBackgroundImageDownloadUrl() {
        return "http://" + IMAGE_SERVER_HOST + "/chatbg/d1";
    }

    public static String getChatImageUploadingUrl() {
        return "http://" + IMAGE_SERVER_HOST + "/ringmarket/ChatImageHandler";
    }

    public static String getChatMP3UploadingUrl() {
        return "http://" + IMAGE_SERVER_HOST + "/ringmarket/Mp3Handler";
    }

    public static String getChatMP4UploadingUrl() {
        return "http://" + IMAGE_SERVER_HOST + "/ringmarket/Mp4Handler";
    }

    public static String getChatContentsDownloadUrl() {
        return "http://" + IMAGE_SERVER_HOST + "/chatContens";
    }

}
