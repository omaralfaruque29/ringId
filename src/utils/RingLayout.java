/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class RingLayout implements LayoutManager {

    private int vgap;
    private int hgap;
    private int defaultWidth;

    public RingLayout(int defaultWidth) {
        this(5, 5, defaultWidth);
    }

    public RingLayout(int hgap, int vgap, int defaultWidth) {
        this.vgap = vgap;
        this.hgap = hgap;
        this.defaultWidth = defaultWidth;
    }

    private Dimension layoutSize(Container parent) {
        Container scrollPane = SwingUtilities.getAncestorOfClass(JScrollPane.class, parent);
        System.err.println("A layoutSize ==> " + parent.getWidth());
        System.err.println("B layoutSize ==> " + scrollPane.getWidth());
        System.err.println("C layoutSize ==> " + scrollPane.getParent().getWidth());
//        System.err.println("Start layoutSize ==> " + (scrollPane != null && scrollPane.getParent() != null ? scrollPane.getParent().getWidth() : parent.getWidth()));
//        
        int width = (scrollPane != null ? scrollPane.getWidth() : parent.getWidth());

        width = Math.max(width, defaultWidth + hgap + hgap);
        Dimension dim = new Dimension(width, 0);

        int column = (parent.getWidth() - hgap) / (defaultWidth + hgap);
        int[] columnHeightArray = new int[column > 0 ? column : 1];

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

        int idx = getMaxColumnIndex(columnHeightArray);
        dim.height = columnHeightArray[idx] + vgap;
        //System.err.println("END layoutSize ==> " + (scrollPane != null && scrollPane.getParent() != null ? scrollPane.getParent().getWidth() : parent.getWidth()));
        return dim;
    }

    public JScrollPane getJScrollAncestor(Component c) {
        for (Container p = c.getParent(); p != null; p = p.getParent()) {
            if (p instanceof JScrollPane) {
                return (JScrollPane) p;
            }
        }
        return null;
    }

    @Override
    public void layoutContainer(Container parent) {
        Container scrollPane = SwingUtilities.getAncestorOfClass(JScrollPane.class, parent);
        int width = (scrollPane != null ? scrollPane.getWidth() : parent.getWidth());

        int column = (width - hgap) / (defaultWidth + hgap);
        int[] columnHeightArray = new int[column > 0 ? column : 1];
        int totalHgap = hgap * (columnHeightArray.length + 1);
        int totalComponentWidth = defaultWidth * columnHeightArray.length;
        int totalWidth = totalHgap + totalComponentWidth;
        int emptySpace = (width - totalWidth) / 2;

        synchronized (parent.getTreeLock()) {
            int n = parent.getComponentCount();
            for (int i = 0; i < n; i++) {
                Component c = parent.getComponent(i);
                Dimension d = c.getPreferredSize();
                if (!c.isVisible()) {
                    continue;
                }

                int idx = getMinColumnIndex(columnHeightArray);
                c.setBounds(emptySpace + (idx * (defaultWidth + hgap)) + hgap, columnHeightArray[idx] + vgap, d.width, d.height);
                columnHeightArray[idx] = columnHeightArray[idx] + vgap + d.height;
            }
        }
        //System.err.println("end layoutContainer" + width);
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

    @Override
    public String toString() {
        return getClass().getName() + "[hgap=" + hgap + " vgap=" + vgap + " defultWidth=" + defaultWidth + "]";
    }

}
