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

public class CenterLayout implements LayoutManager {

    private int defaultWidth;

    public CenterLayout(int defaultWidth) {
        this.defaultWidth = defaultWidth;
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
        int width = (parent.getParent() == null ? 0 : parent.getParent().getWidth());
        int height = (parent.getParent() == null ? 0 : parent.getParent().getHeight());
        Dimension dim = new Dimension(width, height);
        return dim;
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            int n = parent.getComponentCount();
            for (int i = n - 1; i >= 0; i--) {
                Component c = parent.getComponent(i);
                c.setPreferredSize(new Dimension(defaultWidth, parent.getHeight()));
                if (c.isVisible()) {
                    c.setBounds((parent.getWidth() - defaultWidth)/ 2, 0, defaultWidth, parent.getHeight());
                    break;
                }
            }
        }
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
        return getClass().getName() + "[defultWidth=" + defaultWidth + "]";
    }

}
