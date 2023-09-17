/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.constants.RingColorCode;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.SettingsConstants;
import com.ipvision.constants.GetImages;
import com.ipvision.service.uploaddownload.DownLoaderHelps;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import com.ipvision.model.GroupInfoDTO;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.service.utility.Scalr;
import com.ipvision.model.stores.FriendList;
import com.ipvision.model.stores.MyfnfHashMaps;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.Area;
import utils.DocumentSizeFilter;

/**
 *
 * @author FaizAhmed
 */
public class DesignClasses {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DesignClasses.class);

    public static JTextArea createJtextareaNormalwithLineWrap(boolean editable, int width, int height) {
        JTextArea notification_message = new JTextArea();
        notification_message.setFont(HtmlHelpers.getArialUnicodeMsFont(0, 12));
        notification_message.setEditable(editable);
        //notification_message.setBorder(BorderFactory.createMatteBorder(3, 5, 2, 8, DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR));
        notification_message.setWrapStyleWord(true);
        notification_message.setLineWrap(true);
        notification_message.setPreferredSize(new Dimension(width, height));
        return notification_message;
    }

    public static JPanel panelButton(String imageSoruce, String text) {
        JPanel panelButton = new JPanel();
        panelButton.setBackground(DefaultSettings.DEFAULT_BUTTON_HOVER_COLOR);
        panelButton.setLayout(new BoxLayout(panelButton, BoxLayout.X_AXIS));
        panelButton.setBorder(new EmptyBorder(0, 5, 0, 5));
        panelButton.setBorder(DefaultSettings.DEFAULT_BORDER);
        if (imageSoruce != null) {
            JLabel iconimage = DesignClasses.create_imageJlabel(imageSoruce);
            iconimage.setBorder(new EmptyBorder(0, 5, 0, 5));
            panelButton.add(iconimage);
        }
        JLabel buttonNameLable = DesignClasses.makeJLabelButtonText(text);
        buttonNameLable.setBorder(new EmptyBorder(2, 0, 3, 3));
        panelButton.add(buttonNameLable);
        return panelButton;
    }

    public static JLabel labelsInbutton(String text) {
        JLabel panelButton = new JLabel(text);
        panelButton.setForeground(DefaultSettings.DEFAULT_FONT_COLOR_BUTTONS);
        panelButton.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        try {
            panelButton.setFont(getDefaultFont(Font.PLAIN, 17f));
        } catch (Exception e) {
        }
        return panelButton;
    }

    public static JLabel circleLabels(String txt) {
        JLabel panelButton = new JLabel(txt);
        panelButton.setForeground(DefaultSettings.DEFAULT_FONT_COLOR_BUTTONS);
        panelButton.setBorder(new EmptyBorder(5, 5, 5, 5));
        try {
            panelButton.setFont(getDefaultFont(Font.PLAIN, 17f));
        } catch (Exception e) {
        }
        return panelButton;
    }

    public static JTextField makeTextFieldLimitedTextSize(String text, int width, int height, int maxWidth) {
        JTextField textField = new JTextField(text);
        //textField.setToolTipText(text);
        try {
            textField.setFont(getDefaultFont(Font.PLAIN, 13));
        } catch (Exception e) {

        }
        //textField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, DefaultSettings.DEFAULT_FONT));
        textField.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
        textField.setPreferredSize(new Dimension(width, height));
        textField.setBorder(BorderFactory.createCompoundBorder(
                textField.getBorder(),
                BorderFactory.createEmptyBorder(2, 3, 2, 3)));
        AbstractDocument abdoc;
        Document doc = textField.getDocument();
        if (doc instanceof AbstractDocument) {
            abdoc = (AbstractDocument) doc;
            abdoc.setDocumentFilter(new DocumentSizeFilter(maxWidth));
        }
        return textField;
    }

    public static JPasswordField makeJpasswordField(String text, int width, int height, int maxWidth, boolean setBorder) {
        JPasswordField textField = new JPasswordField(text);
        textField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, DefaultSettings.DEFAULT_FONT));
        if (setBorder) {
            textField.setBorder(DefaultSettings.DEFAULT_BORDER);
        } else {
            textField.setBorder(null);
        }
        textField.setPreferredSize(new Dimension(width, height));
        textField.setBorder(BorderFactory.createCompoundBorder(
                textField.getBorder(),
                BorderFactory.createEmptyBorder(2, 3, 3, 3)));
        AbstractDocument abdoc;
        Document doc = textField.getDocument();
        if (doc instanceof AbstractDocument) {
            abdoc = (AbstractDocument) doc;
            abdoc.setDocumentFilter(new DocumentSizeFilter(maxWidth));
        }
        return textField;
    }

    public static JLabel makeJLabelCenterAling_with_font_size_width_color(String text, int left, int top, int width, int font_size, int font_width, Color forgroundcolor) {

        JLabel jlble = new JLabel(text, JLabel.CENTER);
        jlble.setBackground(null);
        jlble.setFont(new Font(Font.SANS_SERIF, font_width, font_size)); // Font.PLAIN=0, Font.Bold=1 Font.italic=2
        jlble.setBounds(left, top, width, DefaultSettings.texBoxHeight);
        jlble.setForeground(forgroundcolor);
        return jlble;
    }

    public static JLabel makeJLabel_with_background_font_size_width_color(String text, int left, int top, int width, int height, int font_size, int font_width, Color forgroundcolor, Color bg_color, int alingment) {

        JLabel jlble = new JLabel(text, alingment);
        jlble.setOpaque(true);
        jlble.setBackground(bg_color);

        jlble.setFont(new Font(Font.SANS_SERIF, font_width, font_size)); // Font.PLAIN=0, Font.Bold=1 Font.italic=2
        jlble.setBounds(left, top, width, height);
        jlble.setForeground(forgroundcolor);
        return jlble;
    }

    public static JLabel makeJlabel_no_bonds(String text, int font_size, int font_width, Color forgroundcolor, Color bg_color, int alingment) {
        JLabel jlble = new JLabel(text, alingment);
        jlble.setOpaque(true);
        jlble.setBackground(bg_color);
        jlble.setFont(new Font(Font.SANS_SERIF, font_width, font_size)); // Font.PLAIN=0, Font.Bold=1 Font.italic=2
        jlble.setForeground(forgroundcolor);
        return jlble;
    }

    public static JLabel makeJLabel(String text, int font_size, int font_width, Color forgroundcolor, int alingment) {
        JLabel jlble = new JLabel(text, alingment);
        jlble.setOpaque(false);
        try {
            jlble.setFont(getDefaultFont(font_width, font_size));
        } catch (Exception e) {
        }
        jlble.setForeground(forgroundcolor);
        return jlble;
    }

    public static JLabel titleOfFriendList(String text, int fontWidth, int fontsize) {
        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        //  jlble.setBackground(null);
        jlble.setForeground(DefaultSettings.DEFAULT_TITLE_COLOR_IN_FRIEND);
        jlble.setFont(new Font(Font.SANS_SERIF, fontWidth, fontsize));
        return jlble;
    }

    public static JLabel make_label_for_field_text(String text, int left, int top, int width) {
        Color label_color = Color.WHITE;
        int label_font_width = 1;
        int label_font_size = 11;
        int label_font_aling = 2;
        Color label_text_color = DefaultSettings.text_color1;
        JLabel jlble = makeJLabel_with_background_font_size_width_color(text, left, top, width, DefaultSettings.texBoxHeight, label_font_size, label_font_width, label_text_color, label_color, label_font_aling);
        return jlble;
    }

    public static JLabel makeLableBold1(String text) {

        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setForeground(DefaultSettings.text_color1);
//        jlble.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        try {
            Font font = getDefaultFont(Font.BOLD, 12f);//new Font("Arial Unicode MS", Font.BOLD, fontsize);
            HashMap<TextAttribute, Object> attrs = new HashMap<TextAttribute, Object>();
            attrs.put(TextAttribute.TRACKING, .02);
            font = font.deriveFont(attrs);
            jlble.setFont(font);
        } catch (Exception e) {
        }
        return jlble;
    }

    public static JLabel makeLableBold1(String text, float size) {

        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setForeground(DefaultSettings.text_color1);
//        jlble.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        try {
            Font font = getDefaultFont(Font.PLAIN, size);//new Font("Arial Unicode MS", Font.BOLD, fontsize);
            HashMap<TextAttribute, Object> attrs = new HashMap<TextAttribute, Object>();
            attrs.put(TextAttribute.TRACKING, .02);
            font = font.deriveFont(attrs);
            jlble.setFont(font);
        } catch (Exception e) {
        }
        return jlble;
    }

    public static JLabel makeLableBold1(String text, float size, int fontwidth) {

        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setForeground(DefaultSettings.text_color1);
//        jlble.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        try {
            Font font = getDefaultFont(fontwidth, size);//new Font("Arial Unicode MS", Font.BOLD, fontsize);
            HashMap<TextAttribute, Object> attrs = new HashMap<TextAttribute, Object>();
            attrs.put(TextAttribute.TRACKING, .02);
            font = font.deriveFont(attrs);
            jlble.setFont(font);
        } catch (Exception e) {
        }
        return jlble;
    }

    public static JLabel makeLableBold1(String text, Color foregroud) {

        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);

        if (foregroud != null) {
            jlble.setForeground(foregroud);
        } else {
            jlble.setForeground(DefaultSettings.text_color1);
        }

        jlble.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        return jlble;
    }

    public static JLabel makeLableBold1(String text, Color foregroud, int size) {

        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);

        if (foregroud != null) {
            jlble.setForeground(foregroud);
        } else {
            jlble.setForeground(DefaultSettings.text_color1);
        }

        jlble.setFont(new Font(Font.SANS_SERIF, Font.BOLD, size));
        return jlble;
    }

    public static JLabel makeLableBoldTooLarge(String text) {

        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setForeground(DefaultSettings.text_color1);
        jlble.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        return jlble;
    }

    public static JLabel makeLableBold(String text, int size) {

        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setForeground(DefaultSettings.text_color1);
        jlble.setFont(new Font(Font.SANS_SERIF, Font.BOLD, size));
        return jlble;
    }

    public static JLabel makeJLabelFullName(String text) {
        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setForeground(DefaultSettings.DEFAULT_FONT_COLOR);
        //  jlble.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        try {
            jlble.setFont(getDefaultFont(Font.PLAIN, 12f));
        } catch (Exception e) {
        }
        return jlble;
    }

    public static JLabel makeJLabelFullName(String text, int font_size) {
        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setForeground(DefaultSettings.DEFAULT_FONT_COLOR);
        //  jlble.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        try {
            jlble.setFont(getDefaultFont(Font.PLAIN, font_size));
        } catch (Exception e) {
        }
        return jlble;
    }
    
    public static JLabel makeJLabelFullNameMyNamePanel(String text, int font_size) {
        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setForeground(DefaultSettings.DEFAULT_FONT_COLOR);
        //  jlble.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        try {
            jlble.setFont(getDefaultFont(Font.BOLD, font_size));
        } catch (Exception e) {
        }
        return jlble;
    }

    public static Font getDefaultFont(int style, float fontSize) throws Exception {
        //   File font_file = new File("resources/ARIALUNI.TTF");
        //File font_file = new File("resources/vrinda.ttf");
        //vrinda
        // File font_file = new File("resources/Verdana.ttf");
        //   File font_file = new File("resources/SiyamRupali.ttf");
        String fontString = MyFnFSettings.RESOURCE_FOLDER + MyFnFSettings.DEFAULT_FONT_NAME;
        File font_file = new File(fontString);
//        InputStream is = new Object() {
//        }.getClass().getClassLoader().getResource("/SiyamRupali.ttf").openStream();
        //   InputStream is = DesignClasses.class.getResourceAsStream("SiyamRupali.ttf");
        //  File font_file = new File("resources/HelveticaNeue_Thin.otf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, font_file);
        Font sizedFont = font.deriveFont(style, fontSize);
        return sizedFont;
    }

    public static JLabel makeJLabelFullName2(String text, int fontsize) {
        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
        try {
            Font font = getDefaultFont(Font.PLAIN, fontsize);//new Font("Arial Unicode MS", Font.BOLD, fontsize);
            HashMap<TextAttribute, Object> attrs = new HashMap<TextAttribute, Object>();
            attrs.put(TextAttribute.TRACKING, .02);
            font = font.deriveFont(attrs);
            jlble.setFont(font);
        } catch (Exception e) {
        }

        return jlble;
    }

    public static JTextArea createJTextAreaForSignup(String text, int font_size) {
        JTextArea row2 = new JTextArea(text);
        try {
            row2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, font_size));
        } catch (Exception e) {
        }
        row2.setForeground(DefaultSettings.DEFAULT_FONT_COLOR);
        row2.setWrapStyleWord(true);
        row2.setLineWrap(true);
        row2.setEditable(false);
        row2.setColumns(28);
        /*Border border = new MatteBorder(1, 1, 1, 1, DefaultSettings.DEFAULT_TEXT_BOX_BORDER_COLOR);//BorderFactory.createLineBorder(DefaultSettings.DEFAULT_TEXT_BOX_BORDER_COLOR);
         row2.setBorder(BorderFactory.createCompoundBorder(border,
         BorderFactory.createEmptyBorder(5, 5, 5, 5)));*/
        Document doc = row2.getDocument();
        AbstractDocument abdoc;
        if (doc instanceof AbstractDocument) {
            abdoc = (AbstractDocument) doc;
        }
        return row2;
    }

    public static JTextArea createJTextAreaForAbout(String text, int font_size) {
        JTextArea row2 = new JTextArea(text);
        try {
            row2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, font_size));
        } catch (Exception e) {
        }
        //row2.setBorder(new MatteBorder(1, 1, 1, 1, Color.RED));
        row2.setForeground(DefaultSettings.DEFAULT_FONT_COLOR);
        row2.setWrapStyleWord(true);
        row2.setLineWrap(true);
        row2.setEditable(false);
        row2.setColumns(1);
        /*Border border = new MatteBorder(1, 1, 1, 1, DefaultSettings.DEFAULT_TEXT_BOX_BORDER_COLOR);//BorderFactory.createLineBorder(DefaultSettings.DEFAULT_TEXT_BOX_BORDER_COLOR);
         row2.setBorder(BorderFactory.createCompoundBorder(border,
         BorderFactory.createEmptyBorder(5, 5, 5, 5)));*/
        Document doc = row2.getDocument();
        AbstractDocument abdoc;
        if (doc instanceof AbstractDocument) {
            abdoc = (AbstractDocument) doc;
        }
        return row2;
    }

    public static JLabel makeJLabeltitleAbout(String text) {
        JLabel jlble = new JLabel(text);
        //jlble.setBorder(new EmptyBorder(0, 40, 0, 0));
        //jlble.setPreferredSize(new Dimension(250, 20));
        jlble.setOpaque(false);
        jlble.setForeground(RingColorCode.RING_THEME_COLOR);
        try {
            Font font = getDefaultFont(Font.PLAIN, 14);//new Font("Arial Unicode MS", Font.BOLD, fontsize);
            HashMap<TextAttribute, Object> attrs = new HashMap<TextAttribute, Object>();
            attrs.put(TextAttribute.TRACKING, .02);
            font = font.deriveFont(attrs);
            jlble.setFont(font);
        } catch (Exception e) {
        }

        return jlble;
    }

    public static JLabel makeDisableFontLable(String text, int fontsize) {
        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setForeground(RingColorCode.FEED_UPDATED_STRING_COLOR);
        try {
            Font font = getDefaultFont(Font.PLAIN, fontsize);//new Font("Arial Unicode MS", Font.BOLD, fontsize);
            HashMap<TextAttribute, Object> attrs = new HashMap<TextAttribute, Object>();
            attrs.put(TextAttribute.TRACKING, .02);
            font = font.deriveFont(attrs);
            jlble.setFont(font);
        } catch (Exception e) {
        }

        return jlble;
    }

    public static JLabel makeJLabelFullNamePlain2(String text, int fontsize) {
        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setForeground(DefaultSettings.DEFAULT_PROFILE_NAME_IN_BOOK);
        try {
            Font font = getDefaultFont(Font.PLAIN, fontsize);//new Font("Arial Unicode MS", Font.BOLD, fontsize);
            HashMap<TextAttribute, Object> attrs = new HashMap<TextAttribute, Object>();
            attrs.put(TextAttribute.TRACKING, .02);
            font = font.deriveFont(attrs);
            jlble.setFont(font);
        } catch (Exception e) {
        }

        return jlble;
    }

    public static JLabel makeJLabelButtonText(String text) {
        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setForeground(DefaultSettings.DEFAULT_FONT_COLOR);
        jlble.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        return jlble;
    }

    public static JLabel createPrivacyLabel(String text) {
        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setForeground(new Color(0x444444));
        jlble.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        return jlble;
    }

    public static JLabel makeJLabelUnderFullName(String text) {
        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setForeground(DefaultSettings.text_color2);
        try {
            jlble.setFont(getDefaultFont(Font.PLAIN, 10f));
        } catch (Exception e) {
        }
        //jlble.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        return jlble;
    }

    public static JLabel makeJLabelChatfullNameMe(String text) {
        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setForeground(DefaultSettings.text_color2);
        jlble.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        return jlble;
    }

    public static JLabel makeFeedUpdatedStringLabel(String text) {
        return makeJLabel_normal(text, 12, RingColorCode.FEED_UPDATED_STRING_COLOR);
    }

    public static JLabel makeJLabel_normal(String text, int font_size, Color forground) {

        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setForeground(forground);
        try {
            jlble.setFont(getDefaultFont(Font.PLAIN, font_size));
        } catch (Exception e) {
        }
        //      jlble.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, font_size));
        //  jlble.setBounds(left, top, width, DefaultSettings.texBoxHeight);
        return jlble;
    }

    public static JLabel makeAncorLabel(String text, int fontWidth, int fontsize) {
        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setForeground(DefaultSettings.DEFAULT_TITLE_COLOR_IN_FRIEND);
        try {
            jlble.setFont(getDefaultFont(fontWidth, fontsize));
        } catch (Exception e) {
        }
        return jlble;
    }

    public static JLabel makeAncorLabelSystemFont(String text, int fontWidth, int fontsize) {
        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jlble.setForeground(DefaultSettings.DEFAULT_TITLE_COLOR_IN_FRIEND);
        jlble.setFont(new Font(Font.SANS_SERIF, fontWidth, fontsize));
        return jlble;
    }

    public static JLabel makeAncorLabelDefaultColor(String text, int fontWidth, int fontsize) {
        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jlble.setForeground(DefaultSettings.DEFAULT_FONT_COLOR);
        try {
            jlble.setFont(getDefaultFont(fontWidth, fontsize));
        } catch (Exception e) {
        }
        return jlble;
    }

    public static JLabel makeAncorLabelSystemFontDefaultColor(String text, int fontWidth, int fontsize) {
        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jlble.setForeground(DefaultSettings.DEFAULT_FONT_COLOR);
        jlble.setFont(new Font(Font.SANS_SERIF, fontWidth, fontsize));
        return jlble;
    }

    public static JLabel likesCommentsText(String text, int fontWidth, int fontsize) {
        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
        jlble.setFont(new Font(Font.SANS_SERIF, fontWidth, fontsize));
        return jlble;
    }

    public static JLabel likesCommentsTextForImage(String text, int fontWidth, int fontsize) {
        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        //  jlble.setForeground(DefaultSettings.UNLIKE_COLOR_IMAGE);
        jlble.setForeground(RingColorCode.FEED_UNLIKE_COLOR);
        jlble.setFont(new Font(Font.SANS_SERIF, fontWidth, fontsize));
        return jlble;
    }

    public static JLabel makeHeaderLable(String text) {
        JLabel jlble = new JLabel(text);
        jlble.setOpaque(false);
        jlble.setFont(new Font(Font.SANS_SERIF, 0, 20));
        return jlble;
    }

    public static JPanel createNullPanel_with_preSize(int width, int height) {

        JPanel pnl = new JPanel();
        pnl.setPreferredSize(new Dimension(width, height));
        pnl.setBackground(Color.WHITE);
        return pnl;
    }

    public static JLabel makeJLabel_with_text_color(String text, int left, int top, int width, int font_size, int font_width, Color text_dd) {

        JLabel jlble = new JLabel(text);
        jlble.setBackground(null);
        jlble.setForeground(text_dd);
        jlble.setFont(new Font(Font.SANS_SERIF, font_width, font_size));
        jlble.setBounds(left, top, width, DefaultSettings.texBoxHeight);
        return jlble;
    }

    public static JLabel create_imageJlabel(String image_source) {
        JLabel label = new JLabel();
        label.setIcon(return_image(image_source));
        label.setOpaque(false);
        // foreground text color
        label.setBorder(null);
        return label;
    }

    public static JLabel create_iconJlabel(ImageIcon image_icon, String tooltipTxt) {
        JLabel label = new JLabel();
        label.setIcon(image_icon);
        label.setOpaque(false);
        label.setToolTipText(tooltipTxt);
        // foreground text color
        label.setBorder(null);
        return label;
    }

    public static JLabel loadingLable(boolean small) {
        if (small) {
            return create_imageJlabel(GetImages.PLEASE_WAIT_MINI);
        } else {
            return create_imageJlabel(GetImages.PLEASE_WAIT);
        }
    }

    public static JLabel create_image_label_with_preferredSize(int width, int height, String image_source) {
        JLabel label = new JLabel();
        ImageIcon img = return_image(image_source);
        label.setIcon(img);
        label.setBackground(null);  // light blue
        label.setOpaque(false);                 // foreground text color
        label.setPreferredSize(new Dimension(width, height));
        label.setBorder(null);
        img = null;
        return label;
    }

    public static JPanel createlabelOvalPanel(String txt) {
        JLabel label = new JLabel(txt);//"Outgoing Requests"
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        label.setForeground(Color.WHITE);

        final JPanel tagNamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tagNamePanel.add(label);
        tagNamePanel.setOpaque(false);
        JPanel tagNameContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int cW = tagNamePanel.getWidth() + 34;
                int arrowWidth = (w - cW) / 2;

                Area mainShadowShape = new Area(new RoundRectangle2D.Double(arrowWidth, 0, cW, h, h + 1, h + 1));
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fill(mainShadowShape);

                Polygon polyLeft = new Polygon(
                        new int[]{0, arrowWidth + 1, arrowWidth + 1},
                        new int[]{10, 10, 11},
                        3);
                g2d.fill(polyLeft);

                Polygon polyRight = new Polygon(
                        new int[]{w, arrowWidth + cW - 1, arrowWidth + cW - 1},
                        new int[]{10, 10, 11},
                        3);
                g2d.fill(polyRight);
            }
        };
        tagNameContainer.setName(txt);
        tagNameContainer.setOpaque(false);
        tagNameContainer.add(tagNamePanel);
        return tagNameContainer;
    }

    public static ImageIcon return_image(String image_source) {
        ImageIcon img = null;

        try {
            img = new ImageIcon(new Object() {
            }.getClass().getClassLoader().getResource(image_source));
        } catch (Exception e) {
            log.error("return_image33 Image not found ==>" + image_source);
        }

        return img;
    }

    public static ImageIcon return_ctIcon(String image_source) {
        DownLoaderHelps dHelp = new DownLoaderHelps();
        ImageIcon img = null;
        try {
            File f = new File(dHelp.getSticketDestinationFolder() + File.separator + image_source);
            if (f.exists()) {
                img = new ImageIcon(ImageIO.read(f));
            }
        } catch (Exception e) {
            log.error("return_ctIcon Image not found ==>" + image_source);
        }

        return img;
    }

    public static ImageIcon return_emoticon(String image_source) {
        DownLoaderHelps dHelp = new DownLoaderHelps();
        ImageIcon img = null;
        try {
            File f = new File(dHelp.getEmoticonDestinationFolder() + File.separator + image_source);
            if (f.exists()) {
                img = new ImageIcon(ImageIO.read(f));
            }
        } catch (Exception e) {
            log.error("return_emoticon Image not found ==>" + image_source);
        }

        return img;
    }

    public static ImageIcon return_sticker(String image_source) {
        DownLoaderHelps dHelp = new DownLoaderHelps();
        ImageIcon img = null;
        try {
            File f = new File(dHelp.getSticketDestinationFolder() + File.separator + image_source);
            if (f.exists()) {
                img = new ImageIcon(ImageIO.read(f));
            }
        } catch (Exception e) {
            log.error("return_sticker1 Image not found ==>" + image_source);
        }

        return img;
    }

    public static ImageIcon return_sticker(String image_source, int size) {
        DownLoaderHelps dHelp = new DownLoaderHelps();
        ImageIcon img = null;
        try {
            File f = new File(dHelp.getSticketDestinationFolder() + File.separator + image_source);
            if (f.exists()) {
                BufferedImage bImg = scaleToRoundedImageWithBorder(size, size, ImageIO.read(f), size);
                img = new ImageIcon(bImg);
            }
        } catch (Exception e) {
            log.error("return_sticker Image not found ==>" + image_source);
        }

        return img;
    }

    public static BufferedImage return_chatbg_buffer_image(String image_source) {
        DownLoaderHelps dHelp = new DownLoaderHelps();
        BufferedImage bImg = null;
        try {
            File f = new File(dHelp.getChatBackgroundFolder() + File.separator + image_source);
            if (f.exists()) {
                bImg = ImageIO.read(f);
            }
        } catch (Exception e) {
            System.gc();
            log.error("return_chatbg_buffer_image Image not found ==>" + image_source);
        }

        return bImg;
    }

    public static BufferedImage return_buffer_image(String image_source) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(image_source));
        } catch (IOException ex) {
            log.error("Buffer img error" + ex.getMessage());
        }
        return img;
    }

    public static JButton createImageButtonWithTExt(String mainImage, String hoverImage, String button_text, Color fontColor) {
        JButton buttonContact = new JButton();
        try {
            buttonContact.setFocusPainted(false);
            buttonContact.setBackground(null);
            buttonContact.setPreferredSize(new Dimension(118, 26));
            buttonContact.setIcon(return_image(mainImage));
            buttonContact.setBorder(null);
            buttonContact.setRolloverEnabled(true);
            buttonContact.setRolloverIcon(return_image(hoverImage));
            buttonContact.setLayout(new BorderLayout());
            JLabel label = makeJLabelCenterAling_with_font_size_width_color(button_text, 0, 0, 1, 11, 1, fontColor);
            buttonContact.add(label);
            buttonContact.setOpaque(false);
            buttonContact.setContentAreaFilled(false);
        } catch (Exception e) {
            log.error("createImageButtonWithTExt==>" + e.getMessage());
        }

        return buttonContact;
    }

    public static void change_image(String main_image, JButton button) {
        button.setIcon(return_image(main_image));
    }

    public static JButton createImageButton(String main_image, String hover_image, String tooltipText) {
        JButton buttonContact = new JButton();
        try {
            buttonContact.setFocusPainted(false);
            buttonContact.setBackground(null);
            buttonContact.setIcon(return_image(main_image));
            buttonContact.setBorder(null);
            buttonContact.setRolloverEnabled(true);
            buttonContact.setRolloverIcon(return_image(hover_image));
            buttonContact.setPressedIcon(return_image(hover_image));
            buttonContact.setBorderPainted(false);
            buttonContact.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (tooltipText != null && tooltipText.trim().length() > 0) {
                buttonContact.setToolTipText(tooltipText);
            }
            buttonContact.setOpaque(false);
            buttonContact.setContentAreaFilled(false);
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("createImageButton==>" + e.getMessage());

        }
        return buttonContact;
    }

    public static JButton createImageButtonWithIcons(ImageIcon main_image, ImageIcon hover_image, String tooltipText) {
        JButton buttonContact = new JButton();
        try {
            buttonContact.setFocusPainted(false);
            buttonContact.setBackground(null);
            buttonContact.setIcon(main_image);
            buttonContact.setBorder(null);
            buttonContact.setRolloverEnabled(true);
            buttonContact.setRolloverIcon(hover_image);
            buttonContact.setPressedIcon(hover_image);
            buttonContact.setBorderPainted(false);
            buttonContact.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (tooltipText != null && tooltipText.trim().length() > 0) {
                buttonContact.setToolTipText(tooltipText);
            }
            buttonContact.setOpaque(false);
            buttonContact.setContentAreaFilled(false);
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("createImageButton==>" + e.getMessage());

        }
        return buttonContact;
    }

    public static JButton createImageButtonWithoutCursor(String main_image, String hover_image, String tooltipText) {
        JButton buttonContact = new JButton();
        try {
            buttonContact.setFocusPainted(false);
            buttonContact.setBackground(null);
            buttonContact.setIcon(return_image(main_image));
            buttonContact.setBorder(null);
            buttonContact.setRolloverEnabled(true);
            buttonContact.setRolloverIcon(return_image(hover_image));
            buttonContact.setPressedIcon(return_image(hover_image));
            buttonContact.setBorderPainted(false);
//            buttonContact.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (tooltipText != null && tooltipText.trim().length() > 0) {
                buttonContact.setToolTipText(tooltipText);
            }
            buttonContact.setOpaque(false);
            buttonContact.setContentAreaFilled(false);
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("createImageButtonWithoutCursor ==>" + e.getMessage());
        }
        return buttonContact;
    }

    public static JButton createEmoticonButton() {
        JButton buttonContact = new JButton();
        try {
            buttonContact = DesignClasses.createImageButton(GetImages.EMOTICON, GetImages.EMOTICON_H, "Add Emotiocon");
            buttonContact.setBorder(new EmptyBorder(0, 0, 0, 5));
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("createEmoticonButton ==>" + e.getMessage());
        }
        return buttonContact;
    }

    public static JButton createImageButton(String main_image, String hover_image, String pressed_image, String tooltipText) {
        JButton buttonContact = new JButton();
        try {
            buttonContact.setFocusPainted(false);
            buttonContact.setBackground(null);
            buttonContact.setIcon(return_image(main_image));
            buttonContact.setBorder(null);
            buttonContact.setRolloverEnabled(true);
            buttonContact.setRolloverIcon(return_image(hover_image));
            buttonContact.setPressedIcon(return_image(pressed_image));
            buttonContact.setBorderPainted(false);
            if (tooltipText != null && tooltipText.trim().length() > 0) {
                buttonContact.setToolTipText(tooltipText);
            }
            buttonContact.setOpaque(false);
            buttonContact.setContentAreaFilled(false);
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("createImageButton ==>" + e.getMessage());
        }
        return buttonContact;
    }

    public static JButton createImageButton(String main_image) {
        JButton buttonContact = new JButton();
        try {
            buttonContact.setFocusPainted(false);
            buttonContact.setIcon(return_image(main_image));
            buttonContact.setBorder(null);
            buttonContact.setRolloverEnabled(true);
            buttonContact.setOpaque(false);
            buttonContact.setContentAreaFilled(false);
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("createImageButton ==>" + e.getMessage());
        }
        return buttonContact;
    }

    public static JTextArea createJTextArea(String text, int maxLenght) {
        JTextArea row2 = new JTextArea(text);
        //  row2.setFont(new Font(Font.SANS_SERIF, 0, 12));
        try {
            row2.setFont(getDefaultFont(Font.PLAIN, 12f));
        } catch (Exception e) {
        }
        row2.setForeground(DefaultSettings.DEFAULT_FONT_COLOR);
        row2.setWrapStyleWord(true);
        row2.setLineWrap(true);
        if (text != null && text.length() > 0) {
            row2.setToolTipText(text);
        }
        Border border = new MatteBorder(1, 1, 1, 1, DefaultSettings.DEFAULT_TEXT_BOX_BORDER_COLOR);//BorderFactory.createLineBorder(DefaultSettings.DEFAULT_TEXT_BOX_BORDER_COLOR);
        row2.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        Document doc = row2.getDocument();
        AbstractDocument abdoc;
        if (doc instanceof AbstractDocument) {
            abdoc = (AbstractDocument) doc;
            if (maxLenght > -1) {
                abdoc.setDocumentFilter(new DocumentSizeFilter(maxLenght));
            }
        }
        return row2;
    }

    public static JTextArea createJTextArea(String text, int fontsize, int maxLenght) {
        JTextArea row2 = new JTextArea(text);
        try {
            row2.setFont(getDefaultFont(Font.PLAIN, fontsize));
        } catch (Exception e) {
        }
        row2.setWrapStyleWord(true);
        row2.setLineWrap(true);
        // Border border = BorderFactory.createLineBorder(DefaultSettings.DEFAULT_TEXT_BOX_BORDER_COLOR);
        row2.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        // row2.setBorder(new MatteBorder(1, 1, 1, 1, DEFAUTL_BORDER_COLOR));
        Document doc = row2.getDocument();
        AbstractDocument abdoc;
        if (doc instanceof AbstractDocument) {
            abdoc = (AbstractDocument) doc;
            abdoc.setDocumentFilter(new DocumentSizeFilter(maxLenght));
        }
        return row2;
    }

    public static JTextArea createJTextAreaNoBorderNogap(String text) {
        JTextArea row2 = new JTextArea(text);
        row2.setEditable(false);
        row2.setBorder(new EmptyBorder(2, 2, 2, 2));
        try {
            row2.setFont(getDefaultFont(Font.PLAIN, 12f));
        } catch (Exception e) {
        }
        row2.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
        row2.setForeground(DefaultSettings.DEFAULT_FONT_COLOR);
        row2.setWrapStyleWord(true);
        row2.setLineWrap(true);
        return row2;
    }

    public static JTextArea createJTextAreaWithBanglaFontNoBorder(String text, int size) {
        JTextArea row2 = new JTextArea(text);
        row2.setEditable(false);
        try {
            row2.setFont(getDefaultFont(Font.PLAIN, size));
            //row2.setFont(new Font("Arial Unicode MS", 0, 13));
        } catch (Exception e) {
        }
        row2.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
        row2.setWrapStyleWord(true);
        row2.setLineWrap(true);
        row2.setBorder(null);
        row2.setToolTipText(text);
        row2.setBorder(BorderFactory.createCompoundBorder(row2.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return row2;
    }

    public static JComboBox createJCombobox(String items[]) {
        JComboBox cobmob_box = new JComboBox(items);
        // cobmob_box.setBorder(DefaultSettings.DEFAULT_TEXT_BOX_BORDER);
        cobmob_box.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
        cobmob_box.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton();
                btn.setFocusPainted(false);
                btn.setContentAreaFilled(false);
                btn.setBorder(null);
                btn.setIcon(DesignClasses.return_image(GetImages.IMAGE_BOTTOM_ARROW));
                return btn;
            }
        });
        return cobmob_box;
    }

    public static JComboBox createJCombobox() {
        JComboBox cobmob_box = new JComboBox();
        //cobmob_box.setBorder(DefaultSettings.DEFAULT_TEXT_BOX_BORDER);
        cobmob_box.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.DEFAULT_BORDER_COLOR));
//        Dimension d=cobmob_box.getPreferredSize();
////        int d1= (int)d.getWidth()+20;
////        int d2= (int)d.getHeight()+5;
////        cobmob_box.setPreferredSize(new Dimension(d1, d2));
        cobmob_box.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton();
                btn.setFocusPainted(false);
                btn.setContentAreaFilled(false);
                btn.setBorder(null);
                btn.setIcon(DesignClasses.return_image(GetImages.IMAGE_BOTTOM_ARROW));
                return btn;
            }
        });
        return cobmob_box;
    }

    public static BufferedImage scaleImage(int w, int h, BufferedImage img) {
        BufferedImage bi = null;
        if (img != null) {
            bi = Scalr.resize(img, Scalr.Mode.FIT_EXACT, w, h, Scalr.OP_ANTIALIAS);
        }
//        try {
//            bi = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
//            Graphics2D g2 = bi.createGraphics();
//            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//            g2.drawImage(img, 0, 0, w, h, null);
//            g2.dispose();
//        } catch (Exception e) {
//        }
        return bi;
    }

    public static BufferedImage createRoundedGroupImage(int w, int h, BufferedImage img1, BufferedImage img2, BufferedImage img3) {
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TRANSLUCENT);

        Graphics2D g2d = bi.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setColor(Color.WHITE);

        g2d.drawImage(img1, 0, 0, bi.getWidth() / 2 - 2, bi.getHeight(), null);

        g2d.drawImage(img2, bi.getWidth() / 2 + 3, 0, bi.getWidth() / 2 - 3, bi.getHeight() / 2 - 2, null);

        g2d.drawImage(img3, bi.getWidth() / 2 + 3, bi.getHeight() / 2 + 3, bi.getWidth() / 2 - 3, bi.getHeight() / 2 - 3, null);

        g2d.dispose();

        bi = scaleToRoundedImage(w, h, bi, w);

        return bi;
    }

    public static BufferedImage scaleToRoundedImage(int w, int h, BufferedImage img, int arc) {
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        try {
            img = scaleImage(w, h, img);
            Graphics2D g2 = (Graphics2D) bi.createGraphics();
            g2.setComposite(AlphaComposite.Src);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));
            g2.setComposite(AlphaComposite.SrcAtop);
            g2.drawImage(img, 0, 0, null);

            g2.dispose();
        } catch (Exception e) {
            //  e.printStackTrace();
            log.error("Error in scaleToRoundedImage ==>" + e.getMessage());
        }

        return bi;
    }

    public static BufferedImage scaleToRoundedImageWithBorder(int w, int h, BufferedImage img, int arc) {
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        try {
            img = scaleImage(w, w, img);
            Graphics2D g2 = (Graphics2D) bi.createGraphics();
            g2.setComposite(AlphaComposite.Src);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fill(new RoundRectangle2D.Float(0, 0, w, w, arc, arc));
            g2.setComposite(AlphaComposite.SrcAtop);
            g2.drawImage(img, 0, 0, null);

            g2.setColor(Color.GRAY);
            BasicStroke stroke = new BasicStroke(1); //1 pixels wide (thickness of the border)
            g2.setStroke(stroke);
            g2.drawOval(0, 0, w - 1, h - 1);

            g2.dispose();
        } catch (Exception e) {
            //  e.printStackTrace();
            log.error("Error in scaleToRoundedImageWithBorder ==>" + e.getMessage());
        }

        return bi;
    }

    public static boolean file_exists(String file) {
        // System.out.println("File being checked: " + file);
        return ((file.length()) > 0 && (new File(file).exists()));
    }

    public static JScrollPane getDefaultScrollPane(Component scrollcoponent) {
        JScrollPane scrollPane = new JScrollPane(scrollcoponent);
        return getDefaultScrollPane(scrollPane);
    }

    public static JScrollPane getDefaultScrollPaneThin(Component scrollcoponent) {
        JScrollPane scrollPane = new JScrollPane(scrollcoponent);
        return getDefaultScrollPaneThin(scrollPane);
    }

    public static JScrollPane getDefaultScrollPane(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(11, 0));
        //scrollPane.getVerticalScrollBar().setBackground(new Color(0, 0, 0, 0));
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUI(new MyScrollbarVerticalUI());
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8));
        //scrollPane.getHorizontalScrollBar().setBackground(new Color(0, 0, 0, 0));
        scrollPane.getHorizontalScrollBar().setOpaque(false);
        scrollPane.getHorizontalScrollBar().setUI(new MyScrollbarVerticalUI());
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        return scrollPane;
    }

    public static JScrollPane getDefaultScrollPaneWide(Component scrollcoponent) {
        JScrollPane scrollPane = new JScrollPane(scrollcoponent);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(11, 0));
        scrollPane.getVerticalScrollBar().setUI(new MyScrollbarVerticalUI());
        //scrollPane.getVerticalScrollBar().setBackground(new Color(0, 0, 0, 0));
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8));
        //scrollPane.getHorizontalScrollBar().setBackground(new Color(0, 0, 0, 0));
        scrollPane.getHorizontalScrollBar().setOpaque(false);
        scrollPane.getHorizontalScrollBar().setUI(new MyScrollbarVerticalUI());
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        return scrollPane;
    }

    public static JScrollPane getDefaultScrollPaneThin(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        //scrollPane.getVerticalScrollBar().setBackground(new Color(0, 0, 0, 0));
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUI(new MyScrollbarVerticalUIThin());
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 6));
        //scrollPane.getHorizontalScrollBar().setBackground(new Color(0, 0, 0, 0));
        scrollPane.getHorizontalScrollBar().setOpaque(false);
        scrollPane.getHorizontalScrollBar().setUI(new MyScrollbarVerticalUIThin());
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        return scrollPane;
    }

    public static void setArrowKeyScroll(final JScrollPane scrollPane) {
        InputMap im = (InputMap) UIManager.getDefaults().get("ScrollPane.ancestorInputMap");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "scrollDown");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "scrollUp");

        ActionMap am = scrollPane.getActionMap();
        am.put("scrollDown", new UpDownAction(UpDownAction.DOWN, scrollPane.getVerticalScrollBar().getModel(),
                scrollPane.getVerticalScrollBar().getUnitIncrement()));
        am.put("scrollUp", new UpDownAction(UpDownAction.UP, scrollPane.getVerticalScrollBar().getModel(),
                scrollPane.getVerticalScrollBar().getUnitIncrement()));
        Runnable doRun = new Runnable() {
            @Override
            public void run() {
                if (scrollPane.getComponentCount() > 0) {
                    scrollPane.getComponent(0).requestFocus();//requestFocusInWindow
                }
            }
        };
        SwingUtilities.invokeLater(doRun);
    }

    public static BufferedImage getBufferImageFromImageList(String imageSource) {
        try {
            return ImageIO.read(new Object() {
            }.getClass().getClassLoader().getResource(imageSource));
        } catch (Exception e) {
            log.error("imageSource==>" + imageSource);
        }
        return null;
    }

    public static JPanel makeTopHeaderPanel() {
        JPanel headerPanle = new JPanel();
        headerPanle.setLayout(new BorderLayout(5, 5));
        headerPanle.setPreferredSize(new Dimension(0, DefaultSettings.SMALL_PANEL_HEIGHT));
        headerPanle.setOpaque(false);
//        headerPanle.setBackground(DefaultSettings.RIGHT_CONTENT_BACKGROUND_COLOR);
        return headerPanle;

    }

    public static void setGroupProfileImage(final JLabel label, final long groupId) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                BufferedImage tempImg = null, img1 = null, img2 = null, img3 = null, finalImage = null;
                try {
                    GroupInfoDTO grpDTO = MyfnfHashMaps.getInstance().getGroup_hash_map().get(groupId);
                    if (grpDTO != null) {
                        int count = 0;
                        if (grpDTO.getMemberMap().keySet() != null && grpDTO.getMemberMap().keySet().size() > 0) {
                            for (String member : grpDTO.getMemberMap().keySet()) {
                                count++;
                                UserBasicInfo memberDTO = FriendList.getInstance().getFriend_hash_map().get(member);
                                if (memberDTO != null && memberDTO.getProfileImage() != null && memberDTO.getProfileImage().length() > 0) {
                                    tempImg = ImageHelpers.getBookBuffedImageFromUrl(ImageHelpers.getThumbUrl(memberDTO.getProfileImage()), SettingsConstants.TYPE_PROFILE_IMAGE);
                                } else if (member.equalsIgnoreCase(MyFnFSettings.LOGIN_USER_ID)) {
                                    if (MyFnFSettings.userProfile.getProfileImage() != null && MyFnFSettings.userProfile.getProfileImage().length() > 0) {
                                        tempImg = ImageHelpers.getBookBuffedImageFromUrl(ImageHelpers.getThumbUrl(MyFnFSettings.userProfile.getProfileImage()), SettingsConstants.TYPE_PROFILE_IMAGE);
                                    } else {
                                        tempImg = ImageHelpers.getUnknownImage(true);
                                    }
                                } else {
                                    tempImg = ImageHelpers.getUnknownImage(true);
                                }
                                if (img1 == null) {
                                    img1 = tempImg;
                                } else if (img2 == null) {
                                    img2 = tempImg;
                                } else if (img3 == null) {
                                    img3 = tempImg;
                                }
                                if (count == 3) {
                                    break;
                                }
                            }
                        }

                        if (img1 == null) {
                            img1 = ImageHelpers.getUnknownImage(true);
                        }
                        if (img2 == null) {
                            img2 = ImageHelpers.getUnknownImage(true);
                        }
                        if (img3 == null) {
                            img3 = ImageHelpers.getUnknownImage(true);
                        }

                        finalImage = DesignClasses.createRoundedGroupImage(35, 35, img1, img2, img3);
                        label.setIcon(new ImageIcon(finalImage));

                    } else {
                        finalImage = DesignClasses.scaleToRoundedImage(35, 35, ImageIO.read(new Object() {
                        }.getClass().getClassLoader().getResource(GetImages.GROUP_SMALL)), 35);
                        label.setIcon(new ImageIcon(finalImage));
                    }
                    label.revalidate();
                    label.repaint();
                } catch (Exception e) {
                    //  e.printStackTrace();
                    log.error("Error in here ==>" + e.getMessage());
                } finally {
                    tempImg = null;
                    img1 = null;
                    img2 = null;
                    img3 = null;
                    finalImage = null;
                }

            }
        });
    }
}
