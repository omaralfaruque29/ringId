/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipv.chat.ChatConstants;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.service.uploaddownload.DownLoaderHelps;
import com.ipvision.service.uploaddownload.StickerMarketDownloader;
import java.awt.Font;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import com.ipvision.model.EmotionDtos;
import com.ipvision.view.chat.ChatTextMessagePanel;
import com.ipvision.model.stores.HashMapsBeforeLogin;
import com.ipvision.model.MessageDTO;

/**
 *
 * @author user
 */
public class HtmlHelpers {

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HtmlHelpers.class);
    private String stickerDownloadUrl = ServerAndPortSettings.getRingStickerMarketDownloadUrl() + "/" + MyFnFSettings.D_FULL;
    DownLoaderHelps dHelp = new DownLoaderHelps();
    Pattern reqular_expresion;

    // private String htmlFont = "\"Siyam Rupali\",Vrinda,Arial,verdana,\"Arial Unicode MS\",Tahoma,SolaimanLipi,FallbackBengaliFont,Helvetica,sans-serif";
    private String htmlFont = "\"SolaimanLipi\",Vrinda,Arial,verdana,\"Arial Unicode MS\",Tahoma,SolaimanLipi,FallbackBengaliFont,Helvetica,sans-serif";
    //  private String htmlFont = "Vrinda,\"SolaimanLipi\",Arial,verdana,\"Arial Unicode MS\",Tahoma,SolaimanLipi,FallbackBengaliFont,Helvetica,sans-serif";
    //private String htmlFont = "verdana,\"Arial Unicode MS\",Vrinda,\"SolaimanLipi\",Arial,Tahoma,SolaimanLipi,FallbackBengaliFont,Helvetica,sans-serif";

    public static Font getArialUnicodeMsFont(int fontWidth, int size) {
        Font fontSize = new Font("Arial Unicode MS", fontWidth, size);
        return fontSize;
    }

    public static Font getArialFont(int fontWidth, int size) {
        Font fontSize = new Font("Arial", fontWidth, size);
        return fontSize;
    }

    public String getStyle_alingRight_with_arrow() {
        return "<style> #demo {position: relative;border: 0px solid #F4F4F4;width: 200px;background-color: #EAF4F7;padding: 0px 0px 0px 5px;margin-bottom:10px;font-size: 9px;color:#7D454C;word-wrap:break-word;font-family: \"Siyam Rupali\",\"Arial Unicode MS\",Tahoma, Helvetica, sans-serif;}\n"
                + "            #demo .after, #demo .before {border: solid transparent;content: ' ';height: 0;right:  100%;position: absolute;width: 0;}\n"
                + "            #demo .after {border-width: 5px;border-right-color: white;top: 5px;}\n"
                + "            #whatfont {font-size: 9px;font-family:" + htmlFont + ";}\n"
                + "            #singleChat {font-size: 9px;font-family:" + htmlFont + ";height:0px;width: 120px;overflow-y: scroll;overflow-x: scroll;text-align: left;}\n"
                + "            #demo .before {border-width: 11px;border-right-color:#C6C6C6;top: 3px;}"
                + "#demo_friend {position: relative;border: 0px solid #C6C6C6;width: 200px;background-color: #FFF4FF;padding: 0px 0px 0px 5px;margin-bottom:10px;font-size: 9px;color:#7D454C;word-wrap:break-word;font-family: \"Siyam Rupali\",\"Arial Unicode MS\",Tahoma, Helvetica,sans-serif;}</style>";
    }

    public String setStyle_inChat(int width) {
        return "<style>\n"
                + "#singleChat {font-size: 10px;font-family:" + htmlFont + ";height:0px;width: " + width + "px;word-wrap: break-word;overflow-y: scroll;overflow-x: scroll;text-align: left;margin-bottom :0px;}\n"
                + " a {text-decoration: none; color: #009eeb;}\n" //#414041;
                + " a.hover {text-decoration: underline;}\n"
                + "</style>";
    }

    public String setStyle_inChat() {
        return "<style>\n"
                + "#singleChat {font-size: 10px;font-family:" + htmlFont + ";height:0px;word-wrap: break-word;overflow-y: scroll;overflow-x: scroll;text-align: left;margin-bottom :0px;}\n"
                + " a {text-decoration: none; color: #414041;}\n"
                + " a.hover {text-decoration: underline;}\n"
                + "</style>";
    }

    public String addCSSforText(int divwidth, int fontsize, boolean bold) {
        if (bold) {
            return "<style>\n"
                    + "#singleChat {font-size:  " + fontsize + "px;font-family:" + htmlFont + ";height:0px;width: " + divwidth + "px;overflow-y: scroll;overflow-x: scroll;text-align: left;margin-bottom :0px;font-weight: bold;}\n"
                    + " a {text-decoration: none; color: #009eeb;}\n"
                    + " a.hover {text-decoration: underline;}\n"
                    + "</style>";
        } else {
            return "<style>\n"
                    + "#singleChat {font-size:  " + fontsize + "px;font-family:" + htmlFont + ";height:0px;width: " + divwidth + "px;overflow-y: scroll;overflow-x: scroll;text-align: left;margin-bottom :0px;}\n"
                    + " a {text-decoration: none; color: #009eeb;}\n"
                    + " a.hover {text-decoration: underline;}\n"
                    + " #seeMore {color: #F47727;}\n"
                    + "</style>";
        }
    }

    public String setStyleHelpAboutUs(int width) {
        return " <style>\n"
                + "            #about_us {font-size: 11px;height:0px;text-align: left;margin-bottom :5px;width: " + width + "px;}\n"
                + "            #header1 {font-weight: bold;font-size: 12px;}\n"
                + "        </style>";
    }

    public String setStyle_inWhatinMind() {
        return "<style>\n"
                + " #whatfont {font-size: 9px;font-family:\"Siyam Rupali\",verdana,arial,\"Arial Unicode MS\",Tahoma, Helvetica, sans-serif;}\n"
                + "</style>";
    }

    public String replaceStringForEmoticons(String givenText, Pattern reqular_expresion) {

        this.reqular_expresion = reqular_expresion;

        String entered_string = givenText;
        if (reqular_expresion != null) {
            entered_string = replaceSpecialChars(entered_string);

            Matcher m = reqular_expresion.matcher(entered_string);
            while (m.find()) {
                if (m.group().length() > 0) {
                    EmotionDtos tememo = HashMapsBeforeLogin.getInstance().getEmotionHashmap().get(m.group());
                    if (tememo != null) {
                        File f = new File(dHelp.getEmoticonDestinationFolder() + File.separator + tememo.getUrl());
                        String src = f.exists() ? f.toURI().toString() : "#";

                        try {
                            String replaceString = "<img src=\"" + src + "\" />";
                            entered_string = entered_string.replace(tememo.getSymbol(), replaceString);
                        } catch (Exception e) {
                        }
                    } else {
                        try {
                            String hyperLink = "<a href=\"" + m.group() + "\">" + m.group() + "</a>";
                            entered_string = entered_string.replace(m.group(), hyperLink);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        return entered_string;
    }

    private String replaceSpecialChars(String givenString) {
        givenString = givenString.replaceAll("<", "&lt;");
        givenString = givenString.replaceAll(">", "&gt;");
        givenString = givenString.replaceAll("\r\n", "<br/>");
        givenString = givenString.replaceAll("\n", "<br/>");
        givenString = givenString.replaceAll("\r", "<br/>");
        return givenString;
    }

    public String replaceStringForEmoticonsForChat(ChatTextMessagePanel chatTextMessagePanel, MessageDTO messageDTO, Pattern reqular_expresion) {
        this.reqular_expresion = reqular_expresion;
        String entered_string = messageDTO.getMessage();
        if (reqular_expresion != null
                && (messageDTO.getMessageType() == ChatConstants.TYPE_PLAIN_MSG
                || messageDTO.getMessageType() == ChatConstants.TYPE_EMOTICON_MSG)) {
            entered_string = replaceSpecialChars(entered_string);
            Matcher m = reqular_expresion.matcher(entered_string.trim());
            while (m.find()) {
                if (m.group().length() > 0) {
                    EmotionDtos tememo = HashMapsBeforeLogin.getInstance().getEmotionHashmap().get(m.group());
                    if (tememo != null) {
                        File f = new File(dHelp.getEmoticonDestinationFolder() + File.separator + tememo.getUrl());
                        String src = f.exists() ? f.toURI().toString() : "#";

                        try {
                            String replaceString = "<img src=\"" + src + "\" title=\"" + tememo.getName() + "\" />";
                            entered_string = entered_string.replace(tememo.getSymbol(), replaceString);
                        } catch (Exception e) {
                        }
                    } else {
                        try {
                            String hyperLink = "<a href=\"" + m.group() + "\">" + m.group() + "</a>";
                            entered_string = entered_string.replace(m.group(), hyperLink);
                        } catch (Exception e) {
                        }
                    }
                }
            }

            if (!entered_string.equals(messageDTO.getMessage()) && messageDTO.getMessageId() <= 0 && messageDTO.getMessageType() == ChatConstants.TYPE_PLAIN_MSG) {
                messageDTO.setMessageType(ChatConstants.TYPE_EMOTICON_MSG);
            }
        } else if (messageDTO.getMessageType() == ChatConstants.TYPE_DOWNLOADED_STICKER_MSG) {
            String src;
            String url = "";
            String downloadUrl = stickerDownloadUrl;

            String[] splits = entered_string.split("/");
            for (int i = 0; i < splits.length; i++) {
                if (splits[i].trim().length() > 0) {
                    url += File.separator + splits[i];
                    downloadUrl += "/" + splits[i];

                    if (i == 0) {
                        try {
                            messageDTO.setStickerCollectionId(Integer.parseInt(splits[i]));
                        } catch (Exception e) {
                        }
                    } else if (i == 1) {
                        try {
                            messageDTO.setStickerCategoryId(Integer.parseInt(splits[i]));
                        } catch (Exception e) {
                        }
                    }
                }
            }

            File f = new File(dHelp.getSticketDestinationFolder() + url);
            if (f.exists()) {
                src = f.toURI().toString();
                try {
                    if (src != null && src.trim().length() > 3) {
                        ImageIcon img = new ImageIcon(ImageIO.read(f));
                        int w = img.getIconWidth();
                        int h = img.getIconHeight();
                        String replaceString = "<img src=\"" + src + "\" height=" + h + " width=" + w + " />";
                        entered_string = entered_string.replace(entered_string, replaceString);
                    } else {
                        entered_string = "";
                    }
                } catch (Exception e) {
                }
            } else {
                entered_string = "";
                new StickerMarketDownloader(downloadUrl, chatTextMessagePanel).start();
            }
        }

        return entered_string;
    }
}
