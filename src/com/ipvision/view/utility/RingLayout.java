/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import javax.swing.JScrollPane;
import com.ipvision.view.feed.SingleFeedStructure;

public class RingLayout implements LayoutManager {

    private int vgap;
    private int hgap;
    private int defaultWidth;
    private int maxColumnHeight;
    private int minColumnHeight;
    private Integer maxNumberOfColumn;
    private int numberOfColumn;

    public RingLayout(int defaultWidth) {
        this(5, 5, defaultWidth);
    }

    public RingLayout(int hgap, int vgap, int defaultWidth) {
        this.vgap = vgap;
        this.hgap = hgap;
        this.defaultWidth = defaultWidth;
    }

    public RingLayout(int hgap, int vgap, int defaultWidth, int maxNumberOfColumn) {
        this.vgap = vgap;
        this.hgap = hgap;
        this.defaultWidth = defaultWidth;
        this.maxNumberOfColumn = maxNumberOfColumn;
    }

    public JScrollPane getAncestorJScrollPane(Container comp) {
        if (comp == null) {
            return null;
        }

        Component parent = comp.getParent();
        while (parent != null && !(parent instanceof JScrollPane)) {
            parent = parent.getParent();
        }
        return parent instanceof JScrollPane ? (JScrollPane) parent : null;
    }

    private Dimension layoutSize(Container parent) {
        JScrollPane scrollPane = getAncestorJScrollPane(parent);
        int width = (scrollPane != null ? scrollPane.getViewport().getWidth() : parent.getWidth());

        Insets insets = parent.getInsets();
        width = width - insets.left - insets.right;

        width = Math.max(width, defaultWidth + hgap + hgap);
        Dimension dim = new Dimension(width, 0);

        numberOfColumn = maxNumberOfColumn == null ? (width - hgap) / (defaultWidth + hgap) : 1;
        int[] columnHeightArray = new int[numberOfColumn > 0 ? numberOfColumn : 1];

        synchronized (parent.getTreeLock()) {
            int n = parent.getComponentCount();
            for (int i = 0; i < n; i++) {
                Component c = parent.getComponent(i);
                Dimension d = c.getPreferredSize();
                if (!c.isVisible()) {
                    continue;
                }

                int idx = getMinColumnIndex(columnHeightArray);
                columnHeightArray[idx] = columnHeightArray[idx] + vgap + d.height;
            }
        }

        int maxIdx = getMaxColumnIndex(columnHeightArray);
        int minIdx = getMinColumnIndex(columnHeightArray);

        maxColumnHeight = columnHeightArray[maxIdx] + vgap + insets.top + insets.bottom;
        minColumnHeight = columnHeightArray[minIdx] + vgap + insets.top + insets.bottom;
        //System.err.println("MAX = " + maxColumnHeight + ", MIN = " + minColumnHeight);
        dim.height = maxColumnHeight;
        return dim;
    }

    @Override
    public void layoutContainer(Container parent) {
        JScrollPane scrollPane = getAncestorJScrollPane(parent);
        int width = (scrollPane != null ? scrollPane.getViewport().getWidth() : parent.getWidth());

        Insets insets = parent.getInsets();
        width = width - insets.left - insets.right;

        numberOfColumn = maxNumberOfColumn == null ? (width - hgap) / (defaultWidth + hgap) : 1;
        int[] columnHeightArray = new int[numberOfColumn > 0 ? numberOfColumn : 1];
        int totalHgap = hgap * (columnHeightArray.length + 1);
        int totalComponentWidth = defaultWidth * columnHeightArray.length;
        int totalWidth = totalHgap + totalComponentWidth;
        int emptySpace = (width - totalWidth) / 2;

        for (int i = 0; i < columnHeightArray.length; i++) {
            columnHeightArray[i] += insets.top;
        }

        synchronized (parent.getTreeLock()) {
            int n = parent.getComponentCount();
            int seqNo = 1;
            for (int i = 0; i < n; i++) {
                Component c = parent.getComponent(i);
                Dimension d = c.getPreferredSize();
                if (!c.isVisible()) {
                    continue;
                }

                if (c instanceof SingleFeedStructure) {
                    setSequenceNumber(seqNo, c);
                    seqNo++;
                }

                int idx = getMinColumnIndex(columnHeightArray);
                c.setBounds(insets.left + emptySpace + (idx * (defaultWidth + hgap)) + hgap, columnHeightArray[idx] + vgap, d.width, d.height);
                columnHeightArray[idx] = columnHeightArray[idx] + vgap + d.height;
            }
        }
    }

    private int getMinColumnIndex(int[] columnHeight) {
        int minIndex = 0;
        int minHeight = columnHeight[0];
        int idx = 1;

        while (idx < columnHeight.length) {
            if (columnHeight[idx] < minHeight) {
                minHeight = columnHeight[idx];
                minIndex = idx;
            }
            idx++;
        }

        return minIndex;
    }

    private int getMaxColumnIndex(int[] columnHeight) {
        int maxIndex = 0;
        int maxHeight = 0;
        int idx = 0;

        while (idx < columnHeight.length) {
            if (columnHeight[idx] > maxHeight) {
                maxHeight = columnHeight[idx];
                maxIndex = idx;
            }
            idx++;
        }

        return maxIndex;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return layoutSize(parent);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return layoutSize(parent);
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {

    }

    public int getMaxColumnHeight() {
        return maxColumnHeight;
    }

    public void setMaxColumnHeight(int maxColumnHeight) {
        this.maxColumnHeight = maxColumnHeight;
    }

    public int getMinColumnHeight() {
        return minColumnHeight;
    }

    public void setMinColumnHeight(int minColumnHeight) {
        this.minColumnHeight = minColumnHeight;
    }

    public int getNumberOfColumn() {
        return numberOfColumn;
    }

    private void setSequenceNumber(int seqNo, Component c) {
        SingleFeedStructure structure = (SingleFeedStructure) c;
        structure.setSequenceNumber(seqNo);
    }

    @Override
    public String toString() {
        return getClass().getName() + "[hgap=" + hgap + " vgap=" + vgap + " defultWidth=" + defaultWidth + "]";
    }

}
