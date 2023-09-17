/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view;

import java.awt.Desktop;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Rabiul
 */
public class MenuAboutOpenWebPage {
//    public static final String BLOG_URL = "http://www.ringid.com/mobile_apps_pages/blog.html";
//    public static final String FAQ_URL = "http://www.ringid.com/mobile_apps_pages/faq.html";
//    public static final String THIRD_PARTY_URL = "http://ringid.com/eula-mobile.html";
//    public static final String PRIVACY_URL = "http://www.ringid.com/mobile_apps_pages/privacy.html";
//    public static final String INFO_URL = "http://www.ringid.com/mobile_apps_pages/info.html";

    private static final String BLOG_URL = "http://blog.ringid.com";
    private static final String FAQ_URL = "http://www.ringid.com/faq.xhtml";
    private static final String THIRD_PARTY_URL = "http://ringid.com/eula-mobile.html";
    private static final String PRIVACY_URL = "http://www.ringid.com/privacy.xhtml";
    private static final String FEATURES_URL = "http://www.ringid.com/features.xhtml";
    private static final String MEDIA_KIT_URL = "http://www.ringid.com/mediakit.xhtml";
    //media kit just like Third Party -->http://www.ringid.com/mediakit.xhtml

    public MenuAboutOpenWebPage(String menuItem) {
        if (menuItem == null ? MenuBarTop.BLOG == null : menuItem.equals(MenuBarTop.BLOG)) {
            try {
                URL blog = new URL(BLOG_URL);
                openWebpage(blog);
            } catch (MalformedURLException ex) {

            }
        } else if (menuItem.equals(MenuBarTop.FAQ)) {
            try {
                URL faq = new URL(FAQ_URL);
                openWebpage(faq);
            } catch (MalformedURLException ex) {

            }
        } else if (menuItem.equals(MenuBarTop.THIRD_PARTY_POLICY)) {
            try {
                URL thrdParty = new URL(THIRD_PARTY_URL);
                openWebpage(thrdParty);
            } catch (MalformedURLException ex) {

            }

        } else if (menuItem.equals(MenuBarTop.PRIVACY)) {
            try {
                URL privacy = new URL(PRIVACY_URL);
                openWebpage(privacy);
            } catch (MalformedURLException ex) {

            }
        } else if (menuItem.equals(MenuBarTop.FEATURES)) {
            try {
                URL info = new URL(FEATURES_URL);
                openWebpage(info);
            } catch (MalformedURLException ex) {

            }
        } else if (menuItem.equals(MenuBarTop.MEDIA_KIT)) {
            try {
                URL media = new URL(MEDIA_KIT_URL);
                openWebpage(media);
            } catch (MalformedURLException ex) {

            }
        }

    }

    private static void openWebpage(URL url) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(url.toURI());
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
    }

}
