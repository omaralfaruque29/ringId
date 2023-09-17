/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.ringtools;

import com.ipvision.model.StickerCategoryDTO;
import com.ipvision.service.image.LoadStickerImageByCatgory;
import com.ipvision.constants.RingColorCode;
import com.ipvision.service.uploaddownload.DownLoaderHelps;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import com.ipvision.service.utility.Scalr;
import com.ipvision.view.utility.RoundedCornerButton;

/**
 *
 * @author Shahadat
 */
public class SingleMarketImageDownloadPanel extends JPanel {

    public JPanel downloadPanel;
    public JButton downloadButton = new RoundedCornerButton("Download Now", "Click to Download");
    public JLabel downloadLabel = new JLabel("Downloading...");
    public StickerCategoryDTO categoryDTO;
    public SingleMarketImageDownloadPanel instance;
    int commonWidth = 220;

    public SingleMarketImageDownloadPanel(StickerCategoryDTO categoryDTO) {
        this.instance = this;
        this.categoryDTO = categoryDTO;
        initContents();
    }

    public void initContents() {
        this.setLayout(new BorderLayout());
        this.setBackground(RingColorCode.RING_MARKET_CENTER_COLOR);

        init();
    }

    private void init() {

        downloadButton.setFont(new Font("Arial", Font.BOLD, 15));
        downloadLabel.setFont(new Font("Arial", Font.BOLD, 13));
        downloadLabel.setForeground(new Color(0xf47727));

        downloadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        downloadPanel.setBorder(new EmptyBorder(7, 0, 10, 0));
        downloadPanel.setOpaque(false);
        downloadPanel.add(downloadButton);
        add(downloadPanel, BorderLayout.NORTH);

        JPanel bannerPanel = new JPanel(new GridBagLayout());
        bannerPanel.setOpaque(false);
        add(bannerPanel, BorderLayout.CENTER);

        String src = categoryDTO.getStickerCollectionId() + File.separator + categoryDTO.getStickerCategoryId() + File.separator + categoryDTO.getDetailImage();
        BufferedImage buffered = return_sticker(src);
        if (buffered != null) {
            try {
                int w = buffered.getWidth();
                int h = buffered.getHeight();
                int nW = commonWidth;
                int nH = (h * commonWidth) / w;

                buffered = Scalr.resize(buffered, Scalr.Mode.FIT_TO_WIDTH, nW, nH, Scalr.OP_ANTIALIAS);
                this.setPreferredSize(new Dimension(nW + 10, nH + 10));
            } catch (Exception ex) {
            }
        }

        JLabel iconLabel = new JLabel();
        if (buffered != null) {
            iconLabel.setIcon(new ImageIcon((Image) buffered));
        }
        bannerPanel.add(iconLabel);
        refreshDownloadLabel();

    }

    public void refreshDownloadLabel() {
        if (categoryDTO.getDownloaded() != null && categoryDTO.getDownloaded()) {
            removeListener();
        } else {
            downloadButton.addActionListener(downloadActionListener);
            downloadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        downloadButton.revalidate();
        downloadButton.repaint();
    }

    public void removeListener() {
        downloadButton.removeActionListener(downloadActionListener);
        downloadButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public ActionListener downloadActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {

            downloadPanel.add(downloadLabel, BorderLayout.NORTH);
            downloadPanel.revalidate();
            downloadPanel.repaint();

            new LoadStickerImageByCatgory(categoryDTO, new MarketImageDownloadListener() {

                @Override
                public void onInit() {
                    downloadPanel.remove(downloadButton);
                    removeListener();
                }

                @Override
                public void onProgress(int remaining) {
                    downloadLabel.setText("# Total: " + remaining);
                }
            }).start();
        }
    };

    public interface MarketImageDownloadListener {

        public void onInit();

        public void onProgress(int remaining);

    }

    public BufferedImage return_sticker(String image_source) {
        DownLoaderHelps dHelp = new DownLoaderHelps();
        BufferedImage img = null;
        try {
            File f = new File(dHelp.getSticketDestinationFolder() + File.separator + image_source);
            if (f.exists()) {
                img = ImageIO.read(f);
            }
        } catch (Exception e) {
        }

        return img;
    }
}
