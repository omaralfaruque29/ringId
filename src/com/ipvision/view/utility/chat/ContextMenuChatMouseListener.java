/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility.chat;

import com.ipv.chat.ChatConstants;
import java.awt.Color;
import java.awt.Event;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;
import com.ipvision.view.chat.SingleChatPanel;
import com.ipvision.model.MessageDTO;
import com.ipvision.model.dao.RecentContactDAO;

/**
 *
 * @author Faiz Ahmed
 */
public class ContextMenuChatMouseListener extends MouseAdapter {

    private JPopupMenu popup = new JPopupMenu();
    private Action cutAction;
    private Action copyAction;
    private Action pasteAction;
    private Action undoAction;
    private Action editAction;
    private Action deleteAction;
    private Action selectAllAction;
    private JMenuItem cutItem;
    private JMenuItem copyItem;
    private JMenuItem pasteItem;
    private JMenuItem undoItem;
    private JMenuItem editItem;
    private JMenuItem deleteItem;
    private JMenuItem selectAllItem;

    private JTextComponent textComponent;
    private String savedString = "";
    private Actions lastActionSelected;
    private MessageDTO messageDTO;
    private SingleChatPanel chatPanel;

    private enum Actions {

        UNDO, CUT, COPY, PASTE, EDIT, DELETE, SELECT_ALL
    };

    public ContextMenuChatMouseListener(SingleChatPanel chatPanel1) {
        this.chatPanel = chatPanel1;
        this.messageDTO = this.chatPanel.messageDTO;

        undoAction = new AbstractAction("Undo") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                textComponent.setText("");
                textComponent.replaceSelection(savedString);

                lastActionSelected = Actions.UNDO;
            }
        };
        undoAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U, Event.CTRL_MASK));
        undoItem = new JMenuItem(undoAction);
        popup.add(undoItem);
        popup.addSeparator();

        cutAction = new AbstractAction("Cut") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                lastActionSelected = Actions.CUT;
                savedString = textComponent.getText();
                textComponent.cut();
            }
        };
        cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK));
        cutItem = new JMenuItem(cutAction);
        popup.add(cutItem);

        copyAction = new AbstractAction("Copy") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                lastActionSelected = Actions.COPY;
                textComponent.copy();
            }
        };
        copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK));
        copyItem = new JMenuItem(copyAction);
        popup.add(copyItem);

        pasteAction = new AbstractAction("Paste") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                lastActionSelected = Actions.PASTE;
                savedString = textComponent.getText();
                textComponent.paste();
            }
        };
        pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK));
        pasteItem = new JMenuItem(pasteAction);
        popup.add(pasteItem);

        if (chatPanel.msgFrom == SingleChatPanel.TYPE_ME && (messageDTO.getMessageType() == ChatConstants.TYPE_PLAIN_MSG || messageDTO.getMessageType() == ChatConstants.TYPE_EMOTICON_MSG)) {
            editAction = new AbstractAction("Edit") {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    lastActionSelected = Actions.EDIT;
                    chatPanel.setEditPanel();
                    if (messageDTO.getGroupId() != null && messageDTO.getGroupId() > 0) {
                        ChatHelpers.startGroupChat(messageDTO.getGroupId(), true);
                    } else {
                        ChatHelpers.startFriendChat(messageDTO.getFriendIdentity());
                    }
                }
            };
            editAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.CTRL_MASK));
            editItem = new JMenuItem(editAction);
            popup.add(editItem);
        }

        deleteAction = new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                lastActionSelected = Actions.DELETE;
                ChatHelpers.deleteChatMessage(messageDTO);
            }
        };
        deleteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK));
        deleteItem = new JMenuItem(deleteAction);
        popup.add(deleteItem);

        popup.addSeparator();

        selectAllAction = new AbstractAction("Select All") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                lastActionSelected = Actions.SELECT_ALL;
                textComponent.selectAll();
            }
        };
        selectAllAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK));
        selectAllItem = new JMenuItem(selectAllAction);
        popup.add(selectAllItem);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getModifiers() == InputEvent.BUTTON3_MASK) {
            if (!(e.getSource() instanceof JTextComponent)) {
                return;
            }

            textComponent = (JTextComponent) e.getSource();
            textComponent.requestFocus();

            boolean enabled = textComponent.isEnabled();
            boolean editable = textComponent.isEditable();
            boolean nonempty = !(textComponent.getText() == null || textComponent.getText().equals(""));
            boolean marked = textComponent.getSelectedText() != null;
            boolean isNotPending = (messageDTO.getGroupId() != null && messageDTO.getGroupId() > 0
                    ? messageDTO.getStatus() != ChatConstants.CHAT_GROUP_PENDING
                    : messageDTO.getStatus() != ChatConstants.CHAT_FRIEND_PENDING);

            boolean pasteAvailable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).isDataFlavorSupported(DataFlavor.stringFlavor);

            boolean isEditAction = isNotPending && (System.currentTimeMillis() - messageDTO.getMessageDate()) < (RecentContactDAO.MILISECONDS_IN_DAY / 24);
            boolean isUndoAction = enabled && editable && (lastActionSelected == Actions.CUT || lastActionSelected == Actions.PASTE);
            boolean isCutAction = enabled && editable && marked;
            boolean isCopyAction = enabled && marked;
            boolean isPasteAction = enabled && editable && pasteAvailable;
            boolean isSelectAllAction = enabled && nonempty;
            boolean isDeleteAction = chatPanel.msgFrom == SingleChatPanel.TYPE_ME;

            undoAction.setEnabled(isUndoAction);
            undoItem.setForeground(isUndoAction ? Color.BLACK : Color.GRAY);
            cutAction.setEnabled(isCutAction);
            cutItem.setForeground(isCutAction ? Color.BLACK : Color.GRAY);
            copyAction.setEnabled(isCopyAction);
            copyItem.setForeground(isCopyAction ? Color.BLACK : Color.GRAY);
            pasteAction.setEnabled(isPasteAction);
            pasteItem.setForeground(isPasteAction ? Color.BLACK : Color.GRAY);
            selectAllAction.setEnabled(isSelectAllAction);
            selectAllItem.setForeground(isSelectAllAction ? Color.BLACK : Color.GRAY);
            if (editAction != null) {
                editAction.setEnabled(isEditAction);
                editItem.setForeground(isEditAction ? Color.BLACK : Color.GRAY);
            }
            deleteAction.setEnabled(true);
            deleteItem.setForeground(Color.BLACK);

            int nx = e.getX();

            if (nx > 500) {
                nx = nx - popup.getSize().width;
            }

            popup.show(e.getComponent(), nx, e.getY() - popup.getSize().height);
        }
    }

}
