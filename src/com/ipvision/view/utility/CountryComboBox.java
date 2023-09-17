package com.ipvision.view.utility;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

/**
 * A custom combo box with its own renderer and editor.
 *
 * @author wwww.codejava.net
 *
 */
public class CountryComboBox extends JComboBox {

    private DefaultComboBoxModel model;

    public CountryComboBox() {
        model = new DefaultComboBoxModel();
        setModel(model);
        setBorder(DefaultSettings.DEFAULT_BORDER);
        setRenderer(new CountryItemRenderer());
        setEditor(new CountryItemEditor());
        setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton();
                btn.setFocusPainted(false);
                btn.setContentAreaFilled(false);
                btn.setBorder(null);
                btn.setIcon(DesignClasses.return_image(GetImages.IMAGE_BOTTOM_ARROW));
                return btn;
            }

            @Override
            protected ComboPopup createPopup() {
                BasicComboPopup popup = new BasicComboPopup(comboBox) {
                    protected JScrollPane createScroller() {
                        JScrollPane scrollContent = DesignClasses.getDefaultScrollPane(list);
                        scrollContent.setOpaque(false);
                        return scrollContent;
                    }
                };
                return popup;
            }

        });
    }

    public CountryComboBox(KeyListener keyListener) {
        model = new DefaultComboBoxModel();
        setModel(model);
        setBorder(DefaultSettings.DEFAULT_BORDER);
        setRenderer(new CountryItemRenderer());
        setEditor(new CountryItemEditor(keyListener));
        setUI(new BasicComboBoxUI() {
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
    }

    /**
     * Add an array items to this combo box. Each item is an array of two String
     * elements: - first element is country name. - second element is path of an
     * image file for country flag.
     *
     * @param items
     */
    public void addItems(String[][] items) {
        for (String[] anItem : items) {
            model.addElement(anItem);
        }
    }
    private boolean layingOut = false;

    @Override
    public void doLayout() {
        try {
            layingOut = true;
            super.doLayout();
        } finally {
            layingOut = false;
        }
    }

    @Override
    public Dimension getSize() {
        Dimension dim = super.getSize();
        if (!layingOut) {
            dim.width = Math.max(dim.width, getPreferredSize().width);
        }
        return dim;
    }
}
