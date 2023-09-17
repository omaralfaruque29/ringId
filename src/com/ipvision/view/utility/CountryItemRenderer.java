package com.ipvision.view.utility;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * Customer renderer for JComboBox
 *
 * @author www.codejava.net
 *
 */
public class CountryItemRenderer extends JPanel implements ListCellRenderer {

    private JLabel labelItem = new JLabel();

    public CountryItemRenderer() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(2, 2, 2, 2);

        labelItem.setOpaque(true);
        labelItem.setHorizontalAlignment(JLabel.LEFT);
        //    labelItem.setPreferredSize(new Dimension(200, 20));
        add(labelItem, constraints);
        setBackground(Color.WHITE);

    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        String[] countryItem = (String[]) value;
        labelItem.setText(countryItem[0]);
        ////        labelItem.setIcon(new ImageIcon(countryItem[1]));
        if (DesignClasses.return_image(GetImages.FLAGS_ROOT_FOLDER + countryItem[0] + ".png") != null) {
            labelItem.setIcon(DesignClasses.return_image(GetImages.FLAGS_ROOT_FOLDER + countryItem[0] + ".png"));
        } else {
            labelItem.setIcon(null);
        }
        if (isSelected) {
            labelItem.setBackground(DefaultSettings.APP_LOGO_COLOR);
            labelItem.setForeground(Color.WHITE);
        } else {
            labelItem.setForeground(Color.BLACK);
            labelItem.setBackground(Color.WHITE);
        }

        return this;
    }
}