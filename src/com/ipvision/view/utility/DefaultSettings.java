/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.constants.RingColorCode;
import java.awt.Color;
import java.awt.Font;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

/**
 *
 * @author FaizAhmed
 */
public class DefaultSettings {

    public static int FRAME_DEFAULT_WIDTH = 990;
    public static int SETTINGS_DEFAULT_WIDTH = 1000;
    public final static int FRAME_DEFAULT_HEIGHT = 720;
    public static int MAIN_RIGHT_CONTENT_WIDTH = 630;//630;
    public static int BOOK_SINGLE_PANEL_COMMON_WIDTH = 640;
    public static int MAIN_RIGHT_CONTENT_HEIGHT = 454;
    public static int MAXIMUM_IMAGE_WIDTH = 1024;
    public static int MAXIMUM_PROFILE_IMAGE_WIDTH = 512;
    public static int LEFT_SINGLE_PANEL_WIDTH = 170;//250;
    public static int LEFT_FRIEND_LIST_WIDTH = 260;//320;
    public static int RIGHT_RING_TOOLS_EXPAND_WIDTH = 240;
    public static int RIGHT_RING_TOOLS_COLLAPSE_WIDTH = 50;
    public static int RIGHT_RING_TOOLS_EXPAND_HEIGHT = 50;
    public static int RIGHT_RING_TOOLS_COLLAPSE_HEIGHT = 200;

    public static int ADD_FRIEND_TAB_PANEL_WIDTH = 300;
    public static int ADD_FRIEND_TOP_MENUBAR_HEIGHT = 60;
    public static int ADD_FRIEND_CATEGORY_HEIGHT = 45;
    public static int ADD_FRIEND_CONTENT_PANEL_HEIGHT = 520;
    //  public static int MAIN_MENUBAR_WIDTH = 61;

    public static int WEBCAM_CAPTURE_WIDTH = 320;
    public static int WEBCAM_CAPTURE_HEIGHT = 240;
    public static int PROFILE_PIC_UPLOAD_MINIMUM_WIDTH = 210; //ALL PLATFORM COMPATIBALE
    public static int PROFILE_PIC_UPLOAD_MINIMUM_HEIGHT = 210; //ALL PLATFORM COMPATIBALE
    public static int COVER_PIC_UPLOAD_MINIMUM_WIDTH = 875; //ALL PLATFORM COMPATIBALE
    public static int COVER_PIC_UPLOAD_MINIMUM_HEIGHT = 270; //ALL PLATFORM COMPATIBALE
    public static int MONITOR_HALF_HEIGHT = FRAME_DEFAULT_HEIGHT;
    public static int ADD_FRIEND_PANEL_HEIGHT = 50;

    public final static int PROFILE_PIC_DISPLAY_WIDTH = 95;
    public final static int COVER_PIC_DISPLAY_WIDTH = 640;//658, 130
    public final static int COVER_PIC_DISPLAY_HEIGHT = 180;//658, 130

    public static int PROFILE_PIC_UPLOAD_SCREEN_WIDTH = 145;
    public static int PROFILE_PIC_UPLOAD_SCREEN_HEIGHT = 150;
    public static int COVER_PIC_UPLOAD_SCREEN_WIDTH = COVER_PIC_DISPLAY_WIDTH;
    public static int COVER_PIC_UPLOAD_SCREEN_HEIGHT = COVER_PIC_DISPLAY_HEIGHT;

    public static int ABOUT_SINGLE_PANEL_HEIGHT = 22;
    public static int BUTTONS_PANEL_WIDTH = 40;
    public static int ABOUT_TITLE_PANEL_HGAP;
    public static int ABOUT_BASIC_PANEL_HGAP = 600;  //610 total from boundary

    public static int HGAP = 2;
    public static int VGAP = 2;
    public static int VGAP_3 = 3;
    public static int VGAP_5 = 5;
    public static int VGAP_8 = 8;
    public static int VGAP_10 = 10;
    public static int SMALL_PANEL_HEIGHT = 35;
    /**/
    public static int RIGHT_BUTTON_PANEL_HEIGHT = 39;
    public static int MENUBER_HEIGHT = 50;
    public static int MY_NAME_PANEL_HEIGHT = 108;
    public static int SINGLE_FRIEND_PANEL_HEIGHT = 50;
    public static int LEFT_EMPTY_SPACE = 10;
    public static int DEFAULT_BORDER_WIDTH = 1;
    public static int PROFILE_PIC_WIDTH = 35;
    // End Default Height for Mail
    /*default scales  ends*/
    public static final Integer DEFAULT_WIDTH = 304;
    public static final int texBoxHeight = 25;
    public static final int textBoxWidth = 150;
    public static final Color errorLabelColor = Color.RED;
    public static final Color text_color2 = new Color(0x8B8989); //8B8989
    public static final Color text_color1 = new Color(0x3C3939);
    public static final int DEFAULT_FONT = 12;
    public static final Integer DEFAULT_PREFIX_TEXT_BOX_WIDTH = 50;
    public static final Color blue_bar_background = new Color(0x0198E1);// #0198E1
    public static final Color calling_window_background_color = new Color(0xebebeb); //new Color(0x0198e1);// #0198E1//#0f315b//3366FF
    public static final Border calling_window_border_color = new LineBorder(new Color(0xffa660), 1);//new LineBorder(new Color(0x0F31DB), 1);
    public static final Color disable_font_color = new Color(0xC4C4C4);// #0198E1//#0f315b//3366FF
    public static final Color lightWhiteBorder = new Color(0xF2F2F2);
    public static final Color text_color_light_blue = new Color(0x1E90FF);
    public static final Color bar_hover_color = new Color(0x99CCFF);
    public static final Color color_friend = new Color(0x0078CA);
    public static final Color color_me = new Color(0x787878);
    public static final Color chat_text_color = new Color(0x333534);
    public static int chat_write_box_height = 50;
    //#D5E1ED
//    public static final Color DEFAUTL_BORDER_COLOR = new Color(0xD5E1ED);
    //LineBorder(new Color(0xD5E1ED), 1);
    public static final Color DEFAULT_ANCOR_COLOR = new Color(0xF47727);
    public static final Color DEFAULT_FONT_COLOR = new Color(0x141823);
    public static final Border DEFAULT_TEXT_BOX_BORDER = new LineBorder(new Color(0xD3D6DB), 1);
    public static final Color DEFAULT_TEXT_BOX_BORDER_COLOR = new Color(0xD3D6DB);
    public static final Border DEFAUTL_BUTTON_BORDER = new LineBorder(new Color(0xD80d4f5), 1);
    public static Color DEFAULT_BUTTON_COLOR = new Color(0xcff0fb);
    //  public static final Color FULL_NAME_2_FONT_COLOR = new Color(0x3B5998);
    //public static final Color COMMENTS_BACK_GROUND_COLOR = new Color(0xf6f6f6);
    //#3B5998
    public static final Color APP_BORDER_COLOR = new Color(0xff8e15);
    public static final Color APP_HOVER_BORDER_COLOR = new Color(0xff7800);
    //ff7800
    public static final Color APP_LOGO_COLOR = new Color(0xf58020);
    //*new rign id color codes*/
    public static final Color APP_DEFAULT_THEME_COLOR = new Color(0xff8a00);
    public static final Color APP_SCROLL_PANE_COLOR = new Color(0xbf8d46);
    public static final Color APP_DEFAULT_MENUBAR_BG_COLOR = new Color(0xf5f5f5);
    public static final Color APP_DEFAULT_MENUBAR_BG_HOVER_COLOR = new Color(0xa1a1a1);
    public static final Color APP_DEFAULT_TAB_BG_COLOR = new Color(0xc9c9c9);
    public static final Color DEFAUTL_BUTTON_BG_COLOR = new Color(0xcccccc);
    public static Color DEFAULT_BUTTON_HOVER_COLOR = new Color(0xa1a1a1);
    public static Color SELECTION_COLOR = new Color(0xF2EDED); //new Color(0xe6e7e8);
    /*notificatios*/
    public static final Color NOTIFICAION_UNREAD_BG = RingColorCode.FRIEND_LIST_SELECTION_COLOR;//new Color(0xFBF9F9);//new Color(0xF6F7F8);
    public static final Color NOTIFICAION_READ_BG = Color.WHITE;
    public static final Color NOTIFICAION_HOVER = new Color(0xc9c9c9);
    public static final Color NOTIFICAION_BARS_COLOR = new Color(0xa78480);
    //
    public static Color DEFAULT_TITLE_COLOR_IN_FRIEND = new Color(0xff8a00);
    public static Color DEFAULT_PROFILE_NAME_IN_BOOK = new Color(0x3C3C3C);
    public static Color DEFAULT_PEOPLE_YOU_MAY_KNOW_BG = new Color(0xF2EDED); //new Color(0xffe4c4);
    public static Color DEFAULT_PEOPLE_YOU_MAY_KNOW_BORDER_COLOR = new Color(0xffbe72);
    public static Font APP_DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    public static Color APP_DEFAULT_FONT_COLOR = new Color(0xa1a1a1);
    public static Color APP_CALL_PAD_BG = new Color(0xf7f7f7);
    public static Color APP_CALL_PAD_BORDER = new Color(0xfcdfc7);
    public static Color DEFAUTL_DEVICE_NAME_COLOR = new Color(0x8DA924);
    public static Color CHAT_TIMER_FG = new Color(0x999999);
    public static Color CALL_BORDER_COLOR = new Color(0xe7f1f5);

    public static final Border DEFAULT_BORDER_H = new LineBorder(APP_DEFAULT_THEME_COLOR, 1);
    //   public static Color RIGHT_CONTENT_BACKGROUND_COLOR = new Color(0xf7f8f9);
    public static final Color APP_DEFAULT_CONTENT_BG_COLOR = new Color(0xebebeb);
    public static final Color ORANGE_BACKGROUND_COLOR = new Color(0xf47727);
    public static final Color ORANGE_BACKGROUND_COLOR_PRESSED = new Color(0xED7121);
    public static final Color BLACK_FONT = new Color(0x6d6e71);
    //   public static final Color STATUS_PANEL_BACKGROUND = Color.WHITE;
    public static final Color CALLING_PANEL_BACKGROUND = new Color(0xececeb);
    public static final Color NOTIFICATION_NUMBERS_BG = Color.RED;
    public static final Color COMMET_BORDER_COLOR = new Color(0xdcddde);
    public static final Border COMMET_BORDER = new MatteBorder(0, 0, 1, 0, COMMET_BORDER_COLOR);
    public static final Color NOTIFICATION_ONLINE_TEXT = new Color(0x00cb4b);
    public static final Color NOTIFICATION_NAME_COLOR = new Color(0x939598);
    public static final Border DEFAULT_BORDER = new LineBorder(RingColorCode.DEFAULT_BORDER_COLOR, 1);
    public static final Border DEFAULT_LINE_BORDER = new LineBorder(ORANGE_BACKGROUND_COLOR, 1);
    public static final Color SINGLE_FRIEND_BORDER_COLOR = new Color(0xf2f2f2);

    public static final Border DEFAULT_BOOK_BORDER = new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR); //new LineBorder(RingColorCode.DEFAULT_BORDER_COLOR, 1); //

    public static final Color DEFAULT_FONT_COLOR_BUTTONS = new Color(0x939494);
    public static final Border DEFAULT_FEED_BORDER = new MatteBorder(1, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR); //new LineBorder(DEFAULT_FEED_BORDER_COLOR, 1);

    public static final Color CALLING_CONTENT_BG = new Color(0xf8f8f9);
    public static final Color CALL_MINI_BG = new Color(0xefeff0);
    public static final Color CALL_MINI_BG_HOVER = new Color(0xe6e7e8);
    /**/
    public static final int FULL_NAME_FONT_SIZE_IN_FEED = 13;
    //
}
