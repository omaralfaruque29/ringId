/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 *
 * @author Faiz
 */
public class BorderLayoutPanel extends JPanel {

    public BorderLayoutPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());
    }

    public BorderLayoutPanel(int hgap, int vgap) {
        setOpaque(false);
        setLayout(new BorderLayout(hgap, vgap));
    }
}
