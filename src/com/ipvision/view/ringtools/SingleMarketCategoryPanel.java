/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.ringtools;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JPanel;
import com.ipvision.model.StickerCategoryDTO;
import com.ipvision.model.StickerImagesDTO;
import com.ipvision.model.stores.StickerStorer;

/**
 *
 * @author shahadat
 */
public class SingleMarketCategoryPanel extends JPanel {

    private int categoryId;
    int gap = 5;

    public SingleMarketCategoryPanel(int categoryId) {
        this.categoryId = categoryId;

        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.initContents();
    }

    public void initContents() {
        this.removeAll();
        StickerCategoryDTO categoryDTO = StickerStorer.getInstance().getCategoriesMap().get(categoryId);
        if (categoryDTO != null) {
            if (categoryDTO.getDownloaded() != null && categoryDTO.getDownloaded()) {
                showStickerImage(StickerStorer.getInstance().getImagesMap().get(categoryId), categoryDTO);
            } else {
                showStickerImageNotDownloaded(categoryDTO);
            }
        }
    }

    private void showStickerImage(Map<Integer, StickerImagesDTO> map, StickerCategoryDTO categoryDTO) {
        this.removeAll();
        this.setLayout(new GridLayout(0, 2));

        if (map != null && map.size() > 0) {
            for (Entry<Integer, StickerImagesDTO> entry : map.entrySet()) {
                StickerImagesDTO imageDTO = entry.getValue();
                SingleMarketImagePanel imagePanel = new SingleMarketImagePanel(imageDTO, categoryDTO);
                this.add(imagePanel);
            }
        }

        this.revalidate();
        this.repaint();
    }

    private void showStickerImageNotDownloaded(StickerCategoryDTO categoryDTO) {
        this.removeAll();
        this.setLayout(new GridLayout());

        SingleMarketImageDownloadPanel imagePanel = new SingleMarketImageDownloadPanel(categoryDTO);
        this.add(imagePanel);

        this.revalidate();
        this.repaint();
    }

}
