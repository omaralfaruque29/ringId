package com.ipvision.view.utility;

import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicComboBoxEditor;

/**
 * Editor for JComboBox
 *
 * @author wwww.codejava.net
 *
 */
public class CountryItemEditor extends BasicComboBoxEditor {

    private JPanel panel = new JPanel();
    private JLabel labelItem = new JLabel();
    private String selectedValue;

    public CountryItemEditor() {
        panel.setOpaque(false);
        labelItem.setPreferredSize(new Dimension(45, 25));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(2, 5, 2, 2);

        labelItem.setOpaque(false);
        labelItem.setHorizontalAlignment(JLabel.LEFT);
        labelItem.setForeground(Color.WHITE);

        panel.add(labelItem, constraints);
        panel.setBackground(Color.BLUE);
    }
    
    public CountryItemEditor(KeyListener keyListener) {
        panel.setOpaque(false);
        labelItem.setPreferredSize(new Dimension(45, 25));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(2, 5, 2, 2);

        labelItem.setOpaque(false);
        labelItem.setHorizontalAlignment(JLabel.LEFT);
        labelItem.setForeground(Color.WHITE);
        //labelItem.addKeyListener(keyListener);

        panel.add(labelItem, constraints);
        panel.setBackground(Color.BLUE);
        panel.addKeyListener(keyListener);
    }

    @Override
    public Component getEditorComponent() {
        return this.panel;
    }

    @Override
    public Object getItem() {
        return this.selectedValue;
    }

    @Override
    public void setItem(Object item) {

        if (item == null) {
            return;
        }
        try {
            String[] countryItem = (String[]) item;
            selectedValue = countryItem[0];
            //   labelItem.setText(selectedValue);
            //   labelItem.setIcon(new ImageIcon(countryItem[1]));
            if (DesignClasses.return_image(GetImages.FLAGS_ROOT_FOLDER + countryItem[0] + ".png") != null) {
                labelItem.setIcon(DesignClasses.return_image(GetImages.FLAGS_ROOT_FOLDER + countryItem[0] + ".png"));
            } else {
                labelItem.setIcon(null);
            }
        } catch (Exception e) {
        }
    }
}