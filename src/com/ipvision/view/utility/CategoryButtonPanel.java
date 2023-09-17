/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.constants.RingColorCode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Wasif Islam
 */
public class CategoryButtonPanel extends JPanel {

    public static int TYPE_FRIENDLIST = 1;
    public static int TYPE_CALLHISTORY = 2;
    public static int TYPE_CHAT = 3;
    public static int TYPE_CIRCLE = 4;
    public static int TYPE_CALL_LOG = 5;
    private static CategoryButtonPanel instance;
    private int buttonPnlWidth = 100;
    private int buttonPnlHeight = 30;
    private MouseListener mouseListener = null;
    public int type;
    public CustomPanel btnSelectAll;
    public CustomPanel btnCancel;
    public CustomPanel btnDelete;
    private int ITEM_FIRST = 1;
    private int ITEM_MID = 2;
    private int ITEM_LAST = 3;

    public static CategoryButtonPanel getInstance() {
        return instance;
    }

    // public CategoryButtonPanel() {
    public CategoryButtonPanel(MouseListener mouseListener) {
        instance = this;
        this.setOpaque(false);
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        this.mouseListener = mouseListener;
        // this.type = type;
        init();
    }

    private void init() {

        btnSelectAll = new CustomPanel(ITEM_FIRST);
        this.add(btnSelectAll);
        btnCancel = new CustomPanel(ITEM_MID);
        this.add(btnCancel);
        btnDelete = new CustomPanel(ITEM_LAST);
        this.add(btnDelete);
    }

    public void resetSelected() {
          if (this.type == TYPE_FRIENDLIST) {
         btnSelectAll.isSelected = false;
         btnCancel.isSelected = false;
         btnDelete.isSelected = false;
         } else {
         btnSelectAll.isSelected = false;
         btnDelete.isSelected = false;
         }
         
    }

    public class CustomPanel extends JPanel {

        private Color borderColor = RingColorCode.LEFT_PANEL_SELECTED_COLOR;
        private Color defaultBgColor = Color.WHITE;
        private Color hoverBgColor = RingColorCode.LEFT_PANEL_HOVER_COLOR;
        private Color selectedBgColor = RingColorCode.LEFT_PANEL_SELECTED_COLOR;
        private Color defaultColor = defaultBgColor;
        private Color defaultTxtColor = RingColorCode.LEFT_PANEL_SELECTED_COLOR;
        private Color hoverTxtColor = Color.WHITE;
        JPanel lblTextPanel;
        public JLabel lblText;
        public String text;
        int index;
        public boolean isSelected;

        public CustomPanel(int index) {
            this.index = index;
            this.isSelected = false;
            setPreferredSize(new Dimension(buttonPnlWidth - 25, buttonPnlHeight - 4));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(mouseListener);

            lblTextPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            lblTextPanel.setOpaque(false);
            lblText = new JLabel();
            lblText.setForeground(defaultTxtColor);
            lblText.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            lblTextPanel.add(lblText);
            add(lblTextPanel, BorderLayout.CENTER);
        }

        public void setText(String text) {
            this.text = text;
            lblText.setText(this.text);
        }

        public void setMouseClicked() {
            this.isSelected = true;
            defaultColor = selectedBgColor;
            lblText.setForeground(hoverTxtColor);
            repaint();
        }

        public void setMouseEntered() {
            lblTextPanel.setBackground(RingColorCode.RING_THEME_COLOR);
            lblText.setForeground(Color.WHITE);
            defaultColor = RingColorCode.RING_THEME_COLOR;
            lblText.setForeground(hoverTxtColor);
            repaint();
        }

        public void setMouseExited() {
            lblTextPanel.setBackground(Color.WHITE);
            lblText.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
            defaultColor = defaultBgColor;
            lblText.setForeground(defaultTxtColor);
            repaint();

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = getWidth();
            int h = getHeight();
            if (index == ITEM_FIRST) {
                g2d.setColor(borderColor);
                Area border = new Area(new RoundRectangle2D.Double(0, 0, w / 2, h, 10, 10));
                border.add(new Area(new Rectangle2D.Double(10, 0, w - 10, h)));
                g2d.fill(border);

                g2d.setColor(defaultColor);
                // Area shape = new Area(new RoundRectangle2D.Double(1, 1, w / 2, h - 2, 10, 10));
                Area shape = new Area(new RoundRectangle2D.Double(0.5, 0.5, w / 2, h - 1, 10, 10));
                //shape.add(new Area(new Rectangle2D.Double(10, 1, w - 11, h - 2)));
                shape.add(new Area(new Rectangle2D.Double(10, 0.5, w - 10.5, h - 1)));

                g2d.fill(shape);
            } else if (index == ITEM_MID) {
                g2d.setColor(borderColor);
                Area border = new Area(new Rectangle2D.Double(0, 0, w, h));
                g2d.fill(border);

                g2d.setColor(defaultColor);
                //Area shape = new Area(new Rectangle2D.Double(0, 1, w - 1, h - 2));
                Area shape = new Area(new Rectangle2D.Double(0.5, 0.5, w - 1, h - 1));
                g2d.fill(shape);

            } else if (index == ITEM_LAST) {
                g2d.setColor(borderColor);
                Area border = new Area(new Rectangle2D.Double(0, 0, w - 10, h));
                border.add(new Area(new RoundRectangle2D.Double(w / 2, 0, w / 2, h, 10, 10)));
                g2d.fill(border);

                g2d.setColor(defaultColor);
                //Area shape = new Area(new Rectangle2D.Double(0, 1, w - 11, h - 2));
                Area shape = new Area(new Rectangle2D.Double(0.5, 0.5, w - 10.5, h - 1));
                //shape.add(new Area(new RoundRectangle2D.Double(w / 2, 1, (w / 2) - 1, h - 2, 10, 10)));
                shape.add(new Area(new RoundRectangle2D.Double(w / 2, 0.5, (w / 2) - 0.5, h - 1, 10, 10)));
                g2d.fill(shape);
            }
        }
    }

    /*  public class customPanelMiddle extends JPanel {

     JPanel lblTextPanel;
     JLabel lblText;
     String text;

     public customPanelMiddle() {
     setPreferredSize(new Dimension(buttonPnlWidth, buttonPnlHeight));
     setCursor(new Cursor(Cursor.HAND_CURSOR));
     addMouseListener(mouseListener);

     lblTextPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
     lblTextPanel.setBackground(Color.WHITE);
     lblText = new JLabel();
     lblText.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
     lblText.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
     lblTextPanel.add(lblText);
     add(lblTextPanel, BorderLayout.CENTER);
     }

     public void setText(String text) {
     this.text = text;
     lblText.setText(this.text);
     }

     public void setMouseEntered() {
     lblTextPanel.setBackground(RingColorCode.RING_THEME_COLOR);
     lblText.setForeground(Color.WHITE);
     }

     public void setMouseExited() {
     lblTextPanel.setBackground(Color.WHITE);
     lblText.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
     }

     @Override
     protected void paintComponent(Graphics g) {
     super.paintComponent(g);
     Graphics2D g2d = (Graphics2D) g;
     g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
     g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
     int w = getWidth();
     int h = getHeight();
     g2d.setColor(borderColor);
     Area border = new Area(new Rectangle2D.Double(0, 0, w, h));
     g2d.fill(border);

     g2d.setColor(Color.WHITE);
     Area shape = new Area(new Rectangle2D.Double(0, 1, w - 1, h - 2));
     g2d.fill(shape);
     }
     }

     public class customPanelLast extends JPanel {

     JPanel lblTextPanel;
     JLabel lblText;
     String text;

     public customPanelLast() {
     setPreferredSize(new Dimension(buttonPnlWidth, buttonPnlHeight));
     setCursor(new Cursor(Cursor.HAND_CURSOR));
     addMouseListener(mouseListener);

     lblTextPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
     lblTextPanel.setBackground(Color.WHITE);
     lblText = new JLabel();
     lblText.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
     lblText.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
     lblTextPanel.add(lblText);
     add(lblTextPanel, BorderLayout.CENTER);
     }

     public void setText(String text) {
     this.text = text;
     lblText.setText(this.text);
     }

     public void setMouseEntered() {
     lblTextPanel.setBackground(RingColorCode.RING_THEME_COLOR);
     lblText.setForeground(Color.WHITE);
     }

     public void setMouseExited() {
     lblTextPanel.setBackground(Color.WHITE);
     lblText.setForeground(RingColorCode.DEFAULT_FORGROUND_COLOR);
     }

     @Override
     protected void paintComponent(Graphics g) {
     super.paintComponent(g);
     Graphics2D g2d = (Graphics2D) g;
     g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
     g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
     int w = getWidth();
     int h = getHeight();

     g2d.setColor(borderColor);
     Area border = new Area(new Rectangle2D.Double(0, 0, w - 10, h));
     border.add(new Area(new RoundRectangle2D.Double(w / 2, 0, w / 2, h, 10, 10)));
     g2d.fill(border);

     g2d.setColor(Color.WHITE);
     Area shape = new Area(new Rectangle2D.Double(0, 1, w - 11, h - 2));
     shape.add(new Area(new RoundRectangle2D.Double(w / 2, 1, (w / 2) - 1, h - 2, 10, 10)));
     g2d.fill(shape);
     }
     }

     public static void main(String[] args) {

     CategoryButtonPanel c = new CategoryButtonPanel(null, TYPE_FRIENDLIST);

     JFrame frame = new JFrame("JFrame");
     frame.addWindowListener(new WindowAdapter() {

     public void windowClosing(WindowEvent e) {
     System.exit(0);
     }
     });
     c.setPreferredSize(new Dimension(500, 100));
     frame.getContentPane().add(c, BorderLayout.CENTER);
     frame.pack();
     frame.setVisible(true);

     }*/
}
