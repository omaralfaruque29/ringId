/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.AppConstants;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.util.regex.Pattern;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import com.ipvision.constants.XMLConstants;
import com.ipvision.model.JsonFields;
import com.ipvision.model.stores.MyfnfHashMaps;
import com.ipvision.model.stores.NewsFeedMaps;
import com.ipvision.service.utility.SendToServer;

/**
 *
 * @author Faiz
 */
public class TextPanelWithEmoticon extends JPanel {

    Pattern reqular_expresion;
    HTMLDocument chat_doc = null;
    HTMLEditorKit editorKit;
    HtmlHelpers htmlHelp = new HtmlHelpers();
    JTextWrapPane area;
    int width;
    String givenText;
    int lineNumber = 0;
    //String g;
    boolean showContinue = false;
    long status_id;
    boolean seeMoreAdded = false;

    public TextPanelWithEmoticon(int width, String givenText) {
        this.width = width;
        this.givenText = givenText;
        setOpaque(false);
        setLayout(new BorderLayout());
        //setLayout(new FlowLayout());
        if (XMLConstants.EMOTION_REGULAR_EXPRESSION != null) {
            reqular_expresion = Pattern.compile(XMLConstants.EMOTION_REGULAR_EXPRESSION);
        }
        init();
        addSeemoreText();
        //addText();
    }

    public TextPanelWithEmoticon(int width, String givenText, long status_id, boolean showContinue) {
        this.width = width;
        this.givenText = givenText;
        this.showContinue = showContinue;
        this.status_id = status_id;
        setOpaque(false);
        setLayout(new BorderLayout());
        if (XMLConstants.EMOTION_REGULAR_EXPRESSION != null) {
            reqular_expresion = Pattern.compile(XMLConstants.EMOTION_REGULAR_EXPRESSION);
        }
        init();
        addSeemoreText();
    }

    private void init() {
        area = new JTextWrapPane();
        area.setEditable(false);
        area.setOpaque(false);
        area.setContentType("text/html");
        area.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
        area.addMouseListener(new ContextMenuMouseListener());
        area.addHyperlinkListener(hyperlinkListener);

        chat_doc = (HTMLDocument) area.getDocument();
        editorKit = (HTMLEditorKit) area.getEditorKit();
    }

    private void addText() {
        try {
            givenText = givenText != null ? givenText : "";
            String replace = htmlHelp.replaceStringForEmoticons(this.givenText, reqular_expresion);
            editorKit.insertHTML(chat_doc, chat_doc.getLength(), htmlHelp.addCSSforText(width, 10, false), 0, 0, null);
            String style = "singleChat";
            String url = "<div  id=\"" + style + "\">" + replace + "</div>";
            editorKit.insertHTML(chat_doc, chat_doc.getLength(), url, 0, 0, null);
            area.setCaretPosition(0);
            area.revalidate();

            add(area, BorderLayout.CENTER);
        } catch (Exception e) {
        }
    }

    private void addSeemoreText() {
        try {
            givenText = givenText != null ? givenText : "";
            String replace = htmlHelp.replaceStringForEmoticons(this.givenText, reqular_expresion);
            editorKit.insertHTML(chat_doc, chat_doc.getLength(), htmlHelp.addCSSforText(width, 10, false), 0, 0, null);

            String styleSeemore = "seeMore";
            String seemoreLink = "<a href=\"http://www.ringid_see_more_status.com\" id =\"" + styleSeemore + "\"> ... See More</a>";

            lineNumber = replace.split("<br/>").length;
            if (!seeMoreAdded && lineNumber > 5) {
                String str = "";
                String[] line = replace.split("<br/>");
                for (int i = 0; i < 5; i++) {
                    str += line[i] + "<br/>";
                }
                replace = str + seemoreLink;
                seeMoreAdded = true;
            } /*else if (!seeMoreAdded && replace.length() > 1000) {
                if (replace.contains("<a href=>") && replace.contains("</a>")) {
                    replace += seemoreLink;
                } else {
                    replace = replace.substring(0, 1000);
                    replace += seemoreLink;
                }
                seeMoreAdded = true;
            }*/ else if (!seeMoreAdded && showContinue) {
                replace += seemoreLink;
                seeMoreAdded = true;
            }
            String style = "singleChat";
            String url = "<div  id=\"" + style + "\">" + replace + "</div>";
            editorKit.insertHTML(chat_doc, chat_doc.getLength(), url, 0, 0, null);
            area.setCaretPosition(0);
            area.revalidate();

            add(area, BorderLayout.CENTER);
        } catch (Exception e) {
        }
    }

    HyperlinkListener hyperlinkListener = new HyperlinkListener() {
        @Override
        public void hyperlinkUpdate(HyperlinkEvent he) {

            HyperlinkEvent.EventType type = he.getEventType();
            if (type == HyperlinkEvent.EventType.ACTIVATED) {
                if (he.getURL()!=null && he.getURL().toString().equalsIgnoreCase("http://www.ringid_see_more_status.com")) {
                    if (showContinue) {
                        new ShowContinueFeed(status_id).start();
                    } else {
                        addAllText();
                    }
                } else if (!(he instanceof HTMLFrameHyperlinkEvent)) {
                    try {
                        Desktop.getDesktop().browse(he.getURL().toURI());
                    } catch (Exception e) {
                    }
                }
            }

        }
    };

    private void addAllText() {
        this.removeAll();
        init();
        addText();
        this.revalidate();
    }

    private class ShowContinueFeed extends Thread {

        Long statusID;

        public ShowContinueFeed(Long status_id) {
            this.statusID = status_id;
        }

        @Override
        public void run() {
            try {
                JsonFields js = new JsonFields();
                js.setAction(AppConstants.TYPE_SINGLE_STATUS_NOTIFICATION);
                String pakId = SendToServer.getRanDomPacketID();
                js.setPacketId(pakId);
                js.setSessionId(MyFnFSettings.LOGIN_SESSIONID);
                js.setNfId(this.statusID);
                SendToServer.sendPacketAsString(js, ServerAndPortSettings.REQUEST_PORT);

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(500);
                    if (MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().get(pakId) == null) {
                        SendToServer.sendPacketAsString(js, ServerAndPortSettings.REQUEST_PORT);
                    } else {
                        MyfnfHashMaps.getInstance().getPacketGotResponseFromServer().remove(pakId);
                        if (NewsFeedMaps.getInstance().getAllNewsFeeds().get(status_id).getStatus().length() == givenText.length()) {
                            for (int j = 0; j < 5; j++) {
                                if (NewsFeedMaps.getInstance().getAllNewsFeeds().get(status_id).getStatus().length() > givenText.length()) {
                                    givenText = NewsFeedMaps.getInstance().getAllNewsFeeds().get(status_id).getStatus();
                                    break;
                                }
                                Thread.sleep(500);
                            }
                        }

                        addAllText();
                        break;
                    }
                }
            } catch (Exception ed) {
            }
        }
    }
}
