/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.settingsmenu;

import com.ipvision.view.utility.DefaultSettings;
import com.ipvision.constants.RingColorCode;
import com.ipvision.view.utility.DesignClasses;
import com.ipvision.constants.GetImages;
import com.ipvision.service.utility.HelperMethods;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import com.ipvision.model.dao.InstantMessagesDAO;
import com.ipvision.model.InstantMessageDTO;

/**
 *
 * @author Sirat Samyoun
 */
public class CustomizeMessagesSettingsPanel extends JPanel implements ActionListener {

    private JPanel mainContent, buttonsPanel;
    private JTextArea txtAddMsg;
    private List<InstantMessageDTO> insMsgs;
    private JButton plusButton;
    private JButton okAddButton = DesignClasses.createImageButton(GetImages.OK_MINI, GetImages.OK_MINI_H, "Add");
    private JButton cancelAddButton = DesignClasses.createImageButton(GetImages.BUTTON_CLOSE_MINI, GetImages.BUTTON_CLOSE_MINI_H, "Cancel");

    public CustomizeMessagesSettingsPanel() {
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.setBorder(new MatteBorder(0, 1, 0, 0, RingColorCode.DEFAULT_BORDER_COLOR));
        this.initComponents();
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout(5, 5));
        headerPanel.setPreferredSize(new Dimension(0, 30));
        headerPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        headerPanel.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
        JPanel jp = new JPanel(new BorderLayout());
        jp.setOpaque(false);
        jp.add(headerPanel, BorderLayout.NORTH);

        add(jp, BorderLayout.NORTH);

        JLabel myNameLabel = DesignClasses.makeLableBold1("Customize Messages", 15);
        headerPanel.add(myNameLabel, BorderLayout.WEST);
        plusButton = DesignClasses.createImageButton(GetImages.ADD_MORE, GetImages.ADD_MORE_H, "Add Instant Messages!");
        plusButton.addActionListener(this);
        headerPanel.add(plusButton, BorderLayout.EAST);

        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setOpaque(false);
        add(bodyPanel, BorderLayout.CENTER);

        buttonsPanel = new JPanel(new BorderLayout());
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(new EmptyBorder(10, 10, 10, 0));
        /*        txtAddMsg = new JTextArea();
         txtAddMsg.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR));
         txtAddMsg.setColumns(10);
         txtAddMsg.setLineWrap(true);*/
        txtAddMsg = DesignClasses.createJTextArea("", 100);
        txtAddMsg.setToolTipText("Add a Message!Maximum 100 characters");
        okAddButton.addActionListener(this);
        cancelAddButton.addActionListener(this);
        buttonsPanel.add(txtAddMsg, BorderLayout.CENTER);
        JPanel pn = new JPanel();
        pn.setOpaque(false);
        pn.add(okAddButton);
        pn.add(cancelAddButton);
        buttonsPanel.add(pn, BorderLayout.EAST);
        jp.add(buttonsPanel, BorderLayout.CENTER);
        buttonsPanel.setVisible(false);

        mainContent = new JPanel();
        mainContent.setBorder(new EmptyBorder(5, 5, 5, 5));
        mainContent.setOpaque(false);
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));

        JPanel tmp = new JPanel(new BorderLayout());
        tmp.setOpaque(false);
        tmp.add(mainContent, BorderLayout.NORTH);

        JScrollPane containerPanelScroll = DesignClasses.getDefaultScrollPaneThin(tmp);
        containerPanelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        bodyPanel.add(containerPanelScroll, BorderLayout.CENTER);
        showMsgList();
    }

    private void showMsgList() {
        insMsgs = InstantMessagesDAO.getInstantMessagesFromDB();
        for (final InstantMessageDTO insMsg : insMsgs) {
            final String str = insMsg.getInstantMessage();
            final JPanel rowPanel = new JPanel(new BorderLayout());
            rowPanel.setPreferredSize(new Dimension(100, 45));
            rowPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, RingColorCode.FEED_BORDER_COLOR), BorderFactory.createEmptyBorder(0, 3, 0, 3) //10, 3, 10, 3
            ));
            rowPanel.setBackground(Color.WHITE);
            JLabel lb = DesignClasses.makeJLabelFullName(str, 13);
            rowPanel.add(lb, BorderLayout.CENTER); //DesignClasses.makeJLabelFullName("", 13); //final JButton deleteMsgButton = DesignClasses.createImageButton(GetImages.DELETE_CALL_RECORD, GetImages.DELETE_CALL_RECORD_H, GetImages.DELETE_CALL_RECORD_SELECTED, "Delete this Message");
            final JLabel deleteMsgButton = DesignClasses.create_imageJlabel(GetImages.DELETE_CALL_RECORD);
            deleteMsgButton.setToolTipText("Delete this Message");
            deleteMsgButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            rowPanel.add(deleteMsgButton, BorderLayout.EAST);
            deleteMsgButton.setVisible(false);
            deleteMsgButton.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    InstantMessageDTO temp = new InstantMessageDTO();
                    temp.setInstantMessage(str);
                    temp.setMsgType(1);
                    InstantMessagesDAO.deleteSingleInstantMessageFromDB(temp);
                    mainContent.removeAll();
                    showMsgList();
                    mainContent.revalidate();
                    mainContent.repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    //deleteMsgButton.setFocusable(false);
                    rowPanel.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
                    if (insMsg.getMsgType() == 1) {
                        deleteMsgButton.setVisible(true);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // deleteMsgButton.setFocusable(false);
                    rowPanel.setBackground(Color.WHITE);
                    deleteMsgButton.setVisible(false);
                }
            });
            rowPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    rowPanel.setBackground(DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR);
                    if (insMsg.getMsgType() == 1) {
                        deleteMsgButton.setVisible(true);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    rowPanel.setBackground(Color.WHITE);
                    deleteMsgButton.setVisible(false);

                }
            });
            mainContent.add(rowPanel);
        }
    }

    private boolean isMsgExisting(String str) {
        List<InstantMessageDTO> tempList = InstantMessagesDAO.getInstantMessagesFromDB();
        for (InstantMessageDTO im : tempList) {
            if (im.getInstantMessage().equals(str)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == plusButton) {
            buttonsPanel.setVisible(true);
            txtAddMsg.setText("");
        } else if (e.getSource() == okAddButton) {
            String str = txtAddMsg.getText().trim();
            boolean isEmpty = !(str.length() > 0);
            boolean isLarge = str.length() > 100;
            boolean isExisting = isMsgExisting(str);

            if (!isEmpty && !isExisting && !isLarge) {
                InstantMessageDTO temp = new InstantMessageDTO();
                temp.setInstantMessage(str);
                temp.setMsgType(1);
                InstantMessagesDAO.insertSingleInstantMessagetoDB(temp);
                mainContent.removeAll();
                showMsgList();
                mainContent.revalidate();
                mainContent.repaint();
                txtAddMsg.setText("");
            } else {
                if (isEmpty) {
                    HelperMethods.showPlaneDialogMessage("Message Cannot be empty");
                } else if (isExisting) {
                    HelperMethods.showPlaneDialogMessage("Message already Exists");
                } else if (isLarge) {
                    HelperMethods.showPlaneDialogMessage("Message can be maximum 100 characters");
                }
            }
        } else if (e.getSource() == cancelAddButton) {
            buttonsPanel.setVisible(false);
        }
    }
}
/*            if (insMsg.getMsgType() != 0) {
 //deleteMsgButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
 deleteMsgButton.setVisible(false);
 //                deleteMsgButton.addActionListener(new ActionListener() {
 //                    @Override
 //                    public void actionPerformed(ActionEvent e) {
 //                        System.out.println("jsdlkjfklsd");
 InstantMessageDTO temp = new InstantMessageDTO();
 temp.setInstantMessage(str);
 temp.setMsgType(1);
 InstantMessagesDAO.deleteSingleInstantMessageFromDB(temp);
 mainContent.removeAll();
 showMsgList();
 mainContent.revalidate();
 mainContent.repaint();
 //                    }
 //                });
 //;
 }*/
/*
 if (!isEmpty && !isExisting) {
 mainContent.removeAll();
 JPanel rowPanel = new JPanel(new BorderLayout());
 rowPanel.setOpaque(false);
 rowPanel.add(new JLabel(str), BorderLayout.CENTER);
 mainContent.add(rowPanel);
 showMsgList();
 mainContent.revalidate();
 mainContent.repaint();
 txtAddMsg.setText("");
 InstantMessageDTO temp = new InstantMessageDTO();
 temp.setInstantMessage(str);
 temp.setMsgType(1);
 List<InstantMessageDTO> tempList = new ArrayList<>();
 tempList.add(temp);
 new InsertIntoInstantMessagesTable(tempList).start();
 }
 private List<JCheckBox> checkboxes;
 private List<String> customMsgs;  
 private void showAddTextArea() {
 txtAddMsg = new JTextArea();
 txtAddMsg.setBorder(new MatteBorder(1, 1, 1, 1, RingColorCode.FEED_BORDER_COLOR));
 txtAddMsg.setColumns(10);
 buttonsPanel.add(txtAddMsg);
 buttonsPanel.add(btnOkAdd);
 buttonsPanel.add(btnCancelAdd);
 }
    
 private void showButtonsforDelete() {
 buttonsPanel.add(btnOkDelete);
 buttonsPanel.add(btnCancelDelete);
 }
 else if (e.getSource() == btnDeleteMsg) {
 buttonsPanel.removeAll();
 showButtonsforDelete();
 buttonsPanel.revalidate();
 buttonsPanel.repaint();

 mainContent.removeAll();
 showMsgListwithCheckList();
 mainContent.revalidate();
 mainContent.repaint();
 } 
 else if (e.getSource() == btnCancelAdd) {
 buttonsPanel.removeAll();
 showDefaultButtons();
 buttonsPanel.revalidate();
 buttonsPanel.repaint();
 } else if (e.getSource() == btnOkDelete) {
 List<InstantMessageDTO> tempList = new ArrayList<>();
 for (int i = 0; i < checkboxes.size(); i++) {
 if (checkboxes.get(i).isSelected()) {
 InstantMessageDTO temp = new InstantMessageDTO();
 temp.setInstantMessage(customMsgs.get(i));
 temp.setMsgType(1);
 tempList.add(temp);
 }
 }
 InstantMessagesDAO.deleteInstantMessagesFromDB(tempList);
 buttonsPanel.removeAll();
 showDefaultButtons();
 buttonsPanel.revalidate();
 buttonsPanel.repaint();

 mainContent.removeAll();
 showMsgList();
 mainContent.revalidate();
 mainContent.repaint();
 } else if (e.getSource() == btnCancelDelete) {
 buttonsPanel.removeAll();
 showDefaultButtons();
 buttonsPanel.revalidate();
 buttonsPanel.repaint();

 mainContent.removeAll();
 showMsgList();
 mainContent.revalidate();
 mainContent.repaint();
 } 

 private void showMsgListwithCheckList() {
 checkboxes = new ArrayList<>();
 customMsgs = new ArrayList<>();
 insMsgs = InstantMessagesDAO.getInstantMessagesFromDB();
 for (InstantMessageDTO insMsg : insMsgs) {
 final String str = insMsg.getInstantMessage();
 JPanel rowPanel = new JPanel(new BorderLayout());
 rowPanel.setOpaque(false);
 JCheckBox ch = new JCheckBox();
 ch.setOpaque(false);
 if (insMsg.getMsgType() == 0) {
 ch.setEnabled(false);
 }
 checkboxes.add(ch);
 customMsgs.add(str);
 rowPanel.add(ch, BorderLayout.WEST);
 rowPanel.add(new JLabel(str), BorderLayout.CENTER);
 mainContent.add(rowPanel);
 }
 }
 */
