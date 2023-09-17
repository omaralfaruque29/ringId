/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.ringtools;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import com.ipvision.model.StickerCategoryDTO;
import com.ipvision.model.stores.StickerStorer;
import java.awt.Color;
import javax.swing.border.MatteBorder;

/**
 *
 * @author shahadat
 */
public class RingMarketPanel extends JPanel {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RingMarketPanel.class);
    private JPanel stickerViewPanel;
    private JPanel categoryViewPanel;

    private JPanel categoryWrapper;

    private JButton btnLeftArrow = DesignClasses.createImageButton(GetImages.EMO_LEFT_ARROW, GetImages.EMO_LEFT_ARROW_H, "Previous");
    private JButton btnRightArrow = DesignClasses.createImageButton(GetImages.EMO_RIGHT_ARROW, GetImages.EMO_RIGHT_ARROW_H, "Next");
    private Map<Integer, List<JPanel>> categoryButtonMap = new ConcurrentHashMap<Integer, List<JPanel>>();
    private Map<Integer, SingleMarketCategoryPanel> stickerCategoryPanel = new ConcurrentHashMap<Integer, SingleMarketCategoryPanel>();

    private int totalGroup = 1;
    private int selectedGroup = 0;
    private JPanel selectedPanel = null;
    private JScrollPane scrollerPanel;

    public RingMarketPanel() {
        setLayout(new BorderLayout(DefaultSettings.DEFAULT_BORDER_WIDTH, DefaultSettings.DEFAULT_BORDER_WIDTH));
        setBackground(RingColorCode.RING_MARKET_CENTER_COLOR);

        stickerViewPanel = new JPanel();
        stickerViewPanel.setLayout(new BorderLayout());//e3bdb0
        stickerViewPanel.setOpaque(false);

        scrollerPanel = DesignClasses.getDefaultScrollPaneThin(stickerViewPanel);
        scrollerPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollerPanel.setBorder(new MatteBorder(0, 3, 0, 0, RingColorCode.LEFT_PANEL_BG_COLOR));
        add(scrollerPanel, BorderLayout.CENTER);

        categoryViewPanel = new JPanel();
        categoryViewPanel.setPreferredSize(new Dimension(0, 38));
        categoryViewPanel.setLayout(new BorderLayout());
        categoryViewPanel.setBackground(RingColorCode.RING_MARKET_BOTTOM_COLOR);
        categoryViewPanel.setBorder(new MatteBorder(2, 0, 0, 0, RingColorCode.RING_MARKET_BOTTOM_BORDER_COLOR));
        add(categoryViewPanel, BorderLayout.SOUTH);

        buildButtonCategory();
    }

    private void buildButtonCategory() {
        JPanel stickerIconContainer = new JPanel(new BorderLayout(0, 0));
        stickerIconContainer.setOpaque(false);
        categoryViewPanel.add(stickerIconContainer, BorderLayout.CENTER);

        JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        westPanel.setPreferredSize(new Dimension(20, 0));
        westPanel.setOpaque(false);
        westPanel.add(btnLeftArrow);
        stickerIconContainer.add(westPanel, BorderLayout.WEST);

        categoryWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        categoryWrapper.setOpaque(false);
        stickerIconContainer.add(categoryWrapper, BorderLayout.CENTER);

        JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        eastPanel.setPreferredSize(new Dimension(20, 0));
        eastPanel.setOpaque(false);
        eastPanel.add(btnRightArrow);
        stickerIconContainer.add(eastPanel, BorderLayout.EAST);

        btnLeftArrow.addActionListener(actionListener);
        btnRightArrow.addActionListener(actionListener);

        buildStickerCategoryListPanel();
    }

    public void buildStickerCategoryListPanel() {
        categoryButtonMap.clear();
        totalGroup = 1;
        selectedGroup = 0;
        selectedPanel = null;
        int currentMaxGroup = 0;
        categoryButtonMap.put(currentMaxGroup, new ArrayList<JPanel>());

        for (Entry<Integer, StickerCategoryDTO> entry : StickerStorer.getInstance().getCategoriesMap().entrySet()) {
            Integer categoryId = entry.getKey();
            StickerCategoryDTO categoryDTO = entry.getValue();

            String src = categoryDTO.getStickerCollectionId() + File.separator + categoryDTO.getStickerCategoryId() + File.separator + categoryDTO.getIcon();
            ImageIcon imageIcon = DesignClasses.return_sticker(src, 30);
            JPanel panel = getCategoryPanel(imageIcon);
            panel.setName(categoryId + "");
            setMouseListener(categoryId, panel);

            if (categoryButtonMap.get(currentMaxGroup).size() == 5) {
                currentMaxGroup++;
                categoryButtonMap.put(currentMaxGroup, new ArrayList<JPanel>());
                categoryButtonMap.get(currentMaxGroup).add(panel);
            } else {
                categoryButtonMap.get(currentMaxGroup).add(panel);
            }
        }
        totalGroup = currentMaxGroup;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JPanel initPanel = categoryButtonMap.get(0).size() > 0 ? categoryButtonMap.get(0).get(0) : null;
                int categoryId = initPanel != null ? Integer.parseInt(initPanel.getName()) : 0;
                showSelectedGroup(true);
                addCategoryPanel(categoryId, initPanel);
            }
        });
    }

    private void showSelectedGroup(boolean next) {
        categoryWrapper.removeAll();
        categoryWrapper.revalidate();
        categoryWrapper.repaint();

        if (selectedGroup == 0) {
            btnLeftArrow.setVisible(false);
            if (totalGroup > 0) {
                btnRightArrow.setVisible(true);
            } else {
                btnRightArrow.setVisible(false);
            }
        } else if (selectedGroup == totalGroup) {
            btnRightArrow.setVisible(false);
            if (totalGroup > 0) {
                btnLeftArrow.setVisible(true);
            } else {
                btnLeftArrow.setVisible(false);
            }
        } else {
            btnLeftArrow.setVisible(true);
            btnRightArrow.setVisible(true);
        }

        JPanel container = new JPanel(new GridLayout(1, 5));
        container.setOpaque(false);
        container.setLocation(new Point(next ? 199 : -199, 0));
        container.setVisible(false);
        categoryWrapper.add(container);

        List<JPanel> panelList = categoryButtonMap.get(selectedGroup);
        for (JPanel panel : panelList) {
            container.add(panel);
        }

        btnLeftArrow.setEnabled(false);
        btnRightArrow.setEnabled(false);
        Animation animation = new Animation(container);
        animation.start();

    }

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnLeftArrow) {
                selectedGroup--;
                showSelectedGroup(false);
            } else if (e.getSource() == btnRightArrow) {
                selectedGroup++;
                showSelectedGroup(true);
            }
        }
    };

    private void setMouseListener(final int categoryId, JPanel panel) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addCategoryPanel(categoryId, (JPanel) e.getSource());
            }
        });
    }

    private void addCategoryPanel(int categoryId, JPanel panel) {
        try {
            if (selectedPanel != null) {
                selectedPanel.setBackground(RingColorCode.RING_MARKET_BOTTOM_COLOR);
            }
            stickerViewPanel.removeAll();
            SingleMarketCategoryPanel singleCategoryPanel = stickerCategoryPanel.get(categoryId);
            if (singleCategoryPanel == null) {
                singleCategoryPanel = new SingleMarketCategoryPanel(categoryId);
                stickerCategoryPanel.put(categoryId, singleCategoryPanel);
            }
            stickerViewPanel.add(singleCategoryPanel, BorderLayout.NORTH);
            stickerViewPanel.revalidate();
            stickerViewPanel.repaint();

            selectedPanel = panel;
            if (selectedPanel != null) {
                selectedPanel.setBackground(Color.WHITE);
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    scrollerPanel.getVerticalScrollBar().setValue(0);
                }
            });
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Error in addCategoryPanel ==>" + ex.getMessage());
        }
    }

    private JPanel getCategoryPanel(ImageIcon icon) {
        JPanel categoryPanel = new JPanel(new GridBagLayout());
        categoryPanel.setPreferredSize(new Dimension(38, 36));
        categoryPanel.setBackground(RingColorCode.RING_MARKET_BOTTOM_COLOR);

        JLabel label = new JLabel(icon);
        label.setOpaque(false);
        categoryPanel.add(label);

        return categoryPanel;
    }

    public Map<Integer, SingleMarketCategoryPanel> getStickerCategoryPanel() {
        return stickerCategoryPanel;
    }

    public void showMarketCategoryByID(int categoryId) {
        int group = 0;
        JPanel panel = null;

        for (Entry<Integer, List<JPanel>> entrySet : categoryButtonMap.entrySet()) {
            Integer index = entrySet.getKey();
            List<JPanel> panels = entrySet.getValue();

            for (JPanel value : panels) {
                if (value.getName().equalsIgnoreCase(categoryId + "")) {
                    panel = value;
                    break;
                }
            }

            if (panel != null) {
                group = index;
                break;
            }
        }
        boolean next = group >= selectedGroup;
        selectedGroup = group;
        showSelectedGroup(next);
        addCategoryPanel(categoryId, panel);
    }

    private class Animation extends Thread {

        double x, y, dx, dy, pixel;
        private JPanel container;

        public Animation(JPanel container) {
            this.container = container;
        }

        @Override
        public void run() {
            doInBackground();
        }

        public void doInBackground() {
            this.container.setVisible(true);
            Point target = new Point(4, 0);
            Point prev = container.getLocation();
//            System.err.println("TARGET ==> " + prev.getX() + " || " + prev.getY());
//            System.err.println("DEST   ==> " + target.getX() + " || " + target.getY());
//            System.err.println("DIFF   ==> " + (prev.getX() - target.getX()));
            dx = Math.abs(prev.getX() - target.getX());
            x = prev.getX();
            y = target.getY();

            pixel = dx / 7.5;
            dx = 7.5;

            boolean dxNext = false;
            if (prev.getX() < target.getX()) {
                dxNext = true;
            }

            float i = 1f;
            while (i <= pixel) {
                if (dxNext) {
                    x += dx;
                } else {
                    x -= dx;
                }

                try {
                    Thread.sleep(5);
                } catch (Exception e) {
                }

                container.setLocation(new Point((int) x, (int) y));
                container.invalidate();

                i += 1;
            }
            container.setLocation(new Point((int) target.getX(), (int) target.getY()));
            btnLeftArrow.setEnabled(true);
            btnRightArrow.setEnabled(true);
        }

    }

}
