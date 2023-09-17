/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.settingsmenu;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.constants.SettingsConstants;
import com.ipvision.service.uploaddownload.ChatBackgroundDownloader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.ipvision.model.ChatBgImageDTO;
import utils.ShadowBorder;

/**
 *
 * @author Shahadat
 */
public class SingleChatBackgroundPanel extends JPanel {

    private JPanel mainPanel;
    public JLabel downlaodLabel;
    public ChatBgImageDTO chatBgImageDTO;
    private SingleChatBackgroundPanel instance;
    int commonWidth = 200;
    int commonHeight = 300;
    private JLabel iconLabel = new JLabel();
    public JLabel nameLabel;
    public JCheckBox chkSetBg;
    private ChatBackgroundSetting chatBackgroundSetting;

    public SingleChatBackgroundPanel(ChatBgImageDTO chatBgImageDTO, ChatBackgroundSetting chatBackgroundSetting) {
        this.instance = this;
        this.chatBackgroundSetting = chatBackgroundSetting;
        this.chatBgImageDTO = chatBgImageDTO;
        this.setLayout(new BorderLayout());
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setBackground(new Color(0xeef3fa));

        this.setPreferredSize(new Dimension(commonWidth, commonHeight));
        this.setBorder(new ShadowBorder(true));

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(3, 3, 7, 3));
        mainPanel.setOpaque(false);
        this.add(mainPanel);

        init();
    }

    private void init() {
        JPanel bannerPanel = new JPanel(new GridLayout());
        bannerPanel.setOpaque(false);
        mainPanel.add(bannerPanel, BorderLayout.CENTER);

        bannerPanel.add(iconLabel);
        boolean isLoaded = loadImage();
        if (isLoaded == false) {
            iconLabel.setIcon(DesignClasses.return_image(GetImages.PLEASE_WAIT));
            new ChatBackgroundDownloader(chatBgImageDTO, this).start();
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        bottomPanel.setOpaque(false);
        bottomPanel.setPreferredSize(new Dimension(0, 30));
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        nameLabel = new JLabel("Set Background");
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        nameLabel.setForeground(DefaultSettings.APP_DEFAULT_FONT_COLOR);

        ImageIcon radioIcon = DesignClasses.return_image(GetImages.CHECK_BOX);
        ImageIcon radioIconSelected = DesignClasses.return_image(GetImages.CHECK_BOX_SELECTED);
        chkSetBg = new JCheckBox(radioIcon);
        chkSetBg.setSelectedIcon(radioIconSelected);
        chkSetBg.setOpaque(false);
        if (chatBgImageDTO.getName().equalsIgnoreCase(SettingsConstants.FNF_CHAT_BG)) {
            chkSetBg.setSelected(true);
        }
        chkSetBg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chatBackgroundSetting != null) {
                    if (chkSetBg.isSelected()) {
                        chatBackgroundSetting.onBackgroundImageChange(chatBgImageDTO.getName());
                    } else {
                        chatBackgroundSetting.onBackgroundImageChange("");
                    }
                }
            }
        });

        bottomPanel.add(nameLabel);
        bottomPanel.add(chkSetBg);
        nameLabel.setVisible(false);
        chkSetBg.setVisible(false);
    }

    private BufferedImage getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    public static int[] getRGB(final String rgb) {
        final int[] ret = new int[3];
        for (int i = 0; i < 3; i++) {
            ret[i] = Integer.parseInt(rgb.substring(i * 2, i * 2 + 2), 16);
        }
        return ret;
    }

    public boolean loadImage() {
        boolean isLoaded = false;
        try {
            BufferedImage buffered = DesignClasses.return_chatbg_buffer_image(chatBgImageDTO.getName());
            if (buffered != null) {
                try {
                    int cHeight = (buffered.getHeight() * (commonWidth - 20)) / buffered.getWidth();
                    buffered = getScaledImage(buffered, (commonWidth - 20), cHeight);
                } catch (Exception ex) {
                }

                if (buffered != null) {
                    iconLabel.setIcon(new ImageIcon((Image) buffered));
                    nameLabel.setVisible(true);
                    chkSetBg.setVisible(true);
                    isLoaded = true;
                }
            }
        } catch (Exception ex) {

        }

        return isLoaded;
    }
}
