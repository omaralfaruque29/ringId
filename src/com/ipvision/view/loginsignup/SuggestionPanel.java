/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.loginsignup;

import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import static com.ipvision.view.loginsignup.RecoverPasswordPanel.createJTextAreaForSignIn;
import com.ipvision.view.utility.DefaultSettings;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.ipvision.view.utility.RoundedCornerButton;
import javax.swing.JTextArea;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;

/**
 *
 * @author Wasif Islam
 */
public class SuggestionPanel extends JPanel implements KeyListener {

    private ArrayList<String> sgtnsList;
    private JPanel containerPanel, mainPanel;
    private JCheckBox[] chkMark;
    private JButton useSelectedInfoBtn;
    private ImageIcon radioIcon = DesignClasses.return_image(GetImages.RADIO_BUTTON);
    private ImageIcon radioIconSelected = DesignClasses.return_image(GetImages.RADIO_BUTTON_SELECTED);
    private String selectedString;

    public SuggestionPanel(ArrayList<String> sgtns) {
        this.sgtnsList = sgtns;
        chkMark = new JCheckBox[sgtnsList.size()];
        setLayout(new BorderLayout());
        setOpaque(false);
        containerPanel = new JPanel(new BorderLayout());
        containerPanel.setPreferredSize(new Dimension(350, 0));
        containerPanel.setOpaque(false);
        add(containerPanel, BorderLayout.EAST);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        containerPanel.add(wrapperPanel, BorderLayout.CENTER);

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        wrapperPanel.add(mainPanel, BorderLayout.NORTH);

        useSelectedInfoBtn = new RoundedButton("Use Selected Info", "Use Selected Info");
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(useSelectedInfoBtn);
        useSelectedInfoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isanyCheckBoxSelected() && selectedString != null && selectedString.length() > 0) {
                    LoginUI.getLoginUI().loadMainContent(LoginUI.VERIFY_RECOVER_PASSWORD_UI, selectedString);
                }
            }
        });
        wrapperPanel.add(buttonsPanel, BorderLayout.CENTER);

        addContents();

    }

    public static JTextArea createJTextAreaForSignIn(String text, int font_size) {
        JTextArea row2 = new JTextArea(text);
        try {
            row2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, font_size));
        } catch (Exception e) {
        }
        row2.setForeground(DefaultSettings.DEFAULT_FONT_COLOR);
        row2.setWrapStyleWord(true);
        row2.setLineWrap(true);
        row2.setEditable(false);
        row2.setColumns(28);
        /*Border border = new MatteBorder(1, 1, 1, 1, DefaultSettings.DEFAULT_TEXT_BOX_BORDER_COLOR);//BorderFactory.createLineBorder(DefaultSettings.DEFAULT_TEXT_BOX_BORDER_COLOR);
         row2.setBorder(BorderFactory.createCompoundBorder(border,
         BorderFactory.createEmptyBorder(5, 5, 5, 5)));*/
        Document doc = row2.getDocument();
        AbstractDocument abdoc;
        if (doc instanceof AbstractDocument) {
            abdoc = (AbstractDocument) doc;
        }
        return row2;
    }

    boolean isanyCheckBoxSelected() {
        for (int i = 0; i < sgtnsList.size(); i++) {
            if (chkMark[i].isSelected()) {
                return true;
            }
        }
        return false;
    }

    private void addContents() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        mainPanel.add(infoPanel, c);

        infoPanel.add(createJTextAreaForSignIn("We have found some suggestions where you can get your verification code:", 12));
        //mainPanel.add(getInstPanel(), c);
        c.insets = new Insets(3, 0, 3, 0);
        c.gridy++;

        for (int index = 0; index < sgtnsList.size(); index++) {
            chkMark[index] = new JCheckBox(radioIcon);
            chkMark[index].addKeyListener(this);
            chkMark[index].setSelectedIcon(radioIconSelected);
            chkMark[index].setName(sgtnsList.get(index));
            chkMark[index].setOpaque(false);
            setActionListener(chkMark[index]);
            JPanel entity = new JPanel(new BorderLayout(5, 0));
            entity.setOpaque(false);
            entity.add(chkMark[index], BorderLayout.WEST);
            JLabel lbl = DesignClasses.makeJLabel_normal(sgtnsList.get(index), 13, Color.BLACK);
            entity.add(lbl, BorderLayout.CENTER);
            c.gridy++;
            mainPanel.add(getWrapperPanel(entity), c);
        }
        c.gridy++;

        /*mainPanel.add(Box.createRigidArea(new Dimension(0, 10)), c);
         c.gridy++;
         useSelectedInfoBtn = new RoundedCornerButton("Use Selected Info", "Use Selected Info");
         JPanel buttonsPanel = new JPanel(new BorderLayout(6, 0));
         buttonsPanel.setOpaque(false);
         buttonsPanel.add(useSelectedInfoBtn, BorderLayout.CENTER);
         useSelectedInfoBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
         if (isanyCheckBoxSelected() && selectedString != null && selectedString.length() > 0) {
         LoginUI.getLoginUI().loadMainContent(LoginUI.getLoginUI().VERIFY_RECOVER_PASSWORD_UI, selectedString);
         }
         }
         });
         */
    }

    private void setActionListener(final JCheckBox chkBox) {
        chkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < chkMark.length; i++) {
                    JCheckBox chkMark1 = chkMark[i];
                    if (chkMark1.getName().equalsIgnoreCase(chkBox.getName())) {
                        selectedString = chkBox.getName();
                    } else {
                        chkMark1.setSelected(false);
                    }

                }
            }
        });
    }

    private JPanel getWrapperPanel(JPanel panel) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setPreferredSize(new Dimension(350, 25));
        wrapper.add(panel);
        wrapper.setOpaque(false);
        return wrapper;
    }

    /*private JPanel getInstPanel() {
     JPanel instPanel = new JPanel();
     instPanel.setOpaque(false);
     instPanel.setPreferredSize(new Dimension(240, 25));
     instPanel.setLayout(new BoxLayout(instPanel, BoxLayout.Y_AXIS));
        
     JPanel text1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
     text1Panel.setOpaque(false);
     JLabel text1Label = new JLabel("We have found some suggestions where you can get ");
     text1Label.setForeground(new Color(0x686868));
     text1Label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
     text1Panel.add(text1Label);
        
     JPanel text2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
     text2Panel.setOpaque(false);
     JLabel text2Label = new JLabel("your verification code:");
     text2Label.setForeground(new Color(0x686868));
     text2Label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
     text2Panel.add(text2Label);
        
     instPanel.add(text1Panel);
     instPanel.add(text2Panel);
     return instPanel;
     }*/
    @Override
    public void keyTyped(KeyEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //if (e.getSource()== txtCode) {
        if ((int) e.getKeyCode() == KeyEvent.VK_ENTER) {
            useSelectedInfoBtn.doClick();
        }
        // }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
