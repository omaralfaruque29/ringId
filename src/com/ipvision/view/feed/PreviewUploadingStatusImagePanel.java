/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.feed;

import com.ipvision.model.UploadImage;
import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER;
import javax.swing.SwingUtilities;

/**
 *
 * @author Shahadat Hossain
 */
public class PreviewUploadingStatusImagePanel extends JPanel {

    JPanel contentPanel;// = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    public Map<String, UploadImage> imageContainerMap = new ConcurrentHashMap<String, UploadImage>();
    public NewStatus newStatus;
    private int previewPanelWidth = DefaultSettings.BOOK_SINGLE_PANEL_COMMON_WIDTH - 10;
    private int previewPanelHeight = 180;
    private JScrollPane downContent;

    public PreviewUploadingStatusImagePanel(NewStatus newStatus) {
        this.newStatus = newStatus;
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(previewPanelWidth, previewPanelHeight));
        init();
    }

    @Override
    public void doLayout() {
        super.doLayout();
        // if (contentPanel.getHeight() > this.getHeight()) {
        this.setPreferredSize(new Dimension(previewPanelWidth, contentPanel.getHeight()));
        //} 
    }

    private void init() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
        contentPanel.setBackground(Color.WHITE);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.add(contentPanel, BorderLayout.WEST);

        downContent = DesignClasses.getDefaultScrollPaneThin(wrapperPanel);
        downContent.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
        this.add(downContent, BorderLayout.NORTH);
    }

    public void successUploadedImageBlock(String key) {
        UploadImage uploadImage = (UploadImage) imageContainerMap.get(key);
        if (uploadImage != null && uploadImage.getThumbnailPanel() != null) {
            uploadImage.getThumbnailPanel().uploadSuccessPanel.setVisible(true);
            uploadImage.getThumbnailPanel().wrappperPanel.revalidate();
        }

    }

    public void removeSelectedImageBlock(String key) {
        UploadImage uploadImage = (UploadImage) imageContainerMap.get(key);
        if (uploadImage != null && uploadImage.getThumbnailPanel() != null) {
            int index = contentPanel.getComponentZOrder(uploadImage.getThumbnailPanel());
            contentPanel.remove(index);
            contentPanel.revalidate();
            contentPanel.repaint();
            imageContainerMap.remove(key);
            newStatus.refreshPreviewImageAlbum();
            uploadImage = null;
            System.gc();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                downContent.getHorizontalScrollBar().setValue(contentPanel.getWidth());
            }
        });

    }

    public void removeUploadedAllImageBlock() {
        imageContainerMap.clear();
        contentPanel.removeAll();
        contentPanel.revalidate();
        contentPanel.repaint();
        newStatus.refreshPreviewImageAlbum();
        System.gc();
    }

    public void singleUploadedImageBlock(File file, Long imageId, String iurl, String caption) {
        Random rand = new Random();
        String key = (System.currentTimeMillis() + file.getName() + rand.nextInt(99));

        SingleImagePreviewPanel singleImagePreviewPanel = new SingleImagePreviewPanel(this, file, caption, imageId, key);
        UploadImage uploadImage = new UploadImage(singleImagePreviewPanel, imageId, iurl, caption);

        contentPanel.add(singleImagePreviewPanel);
        contentPanel.revalidate();
        imageContainerMap.put(key, uploadImage);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                downContent.getHorizontalScrollBar().setValue(contentPanel.getWidth());
            }
        });
    }

}
