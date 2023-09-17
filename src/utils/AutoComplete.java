/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class AutoComplete implements DocumentListener {

    private static enum Mode {INSERT,COMPLETION};
    private JTextField textField;
    private final List<String> keywords;
    private Mode mode = Mode.INSERT;
    private JPopupMenu popupMenu;

    public AutoComplete(JTextField textField, List<String> keywords) {
        this.textField = textField;
        this.keywords = keywords;
        Collections.sort(keywords);
    }

    @Override
    public void changedUpdate(DocumentEvent ev) { 
        if(popupMenu != null){
           // popupMenu.setVisible( false );
        }
    }

    @Override
    public void removeUpdate(DocumentEvent ev) {
        if(popupMenu != null){
            if(popupMenu.isVisible() && (prefix == null || prefix.length() <= 0) ){
                popupMenu.setVisible(false);
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent ev) {
        //if(popupMenu == null){
            eventHandler(ev);
        //}
    }
    
    private String prefix = "";
    private void eventHandler(DocumentEvent ev){
        if (ev.getLength() != 1){
            prefix = "";
            return;
        }

        int pos = ev.getOffset();
        String content = null;

        try {
            content = textField.getText(0, pos + 1);
        } catch (BadLocationException e) {
            //prefix = "";
            //e.printStackTrace();
        }

        // Find where the word starts
        int w;
        for (w = pos; w >= 0; w--) {
            if (!Character.isLetter(content.charAt(w)) 
                    && !Character.isDigit(content.charAt(w)) 
                    && !(content.charAt(w) == '@') 
                    && !(content.charAt(w) == '_')
                    && !(content.charAt(w) == '.')) {
                break;
            }
        }

        // Too few chars
        if (pos - w < 3){
            prefix = "";
            return;
        }

        String match = "";
        prefix = content.substring(w + 1).toLowerCase();
        for (String str : keywords) {
            if(str.toLowerCase().startsWith(prefix.toLowerCase())){
                match = str;
                break;
            }
        }

        if (match.length() > 0) {
            buildSearchPopUp(pos);
            if (match.startsWith(prefix)) {
                String completion = match.substring(pos - w);
                SwingUtilities.invokeLater(new CompletionTask(completion, pos + 1));
            }
        } else {
            mode = Mode.INSERT;
        }
    }

    public class CommitAction extends AbstractAction {
        private static final long serialVersionUID = 5794543109646743416L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            if (mode == Mode.COMPLETION) {
                int pos = textField.getSelectionEnd();
                StringBuffer sb = new StringBuffer(textField.getText());
                sb.insert(pos, " ");
                textField.setText(sb.toString());
                textField.setCaretPosition(pos + 1);
                mode = Mode.INSERT;
            } else {
                textField.replaceSelection("\t");
            }
        }
    }

    private class CompletionTask implements Runnable {
        private String completion;
        private int position;

        CompletionTask(String completion, int position) {
            this.completion = completion;
            this.position = position;
        }

        public void run() {
            try{
                StringBuffer sb = new StringBuffer(textField.getText());
                if(position <= textField.getText().length()){
                    sb.replace(position, textField.getText().length(), "");
                }
                sb.insert(position, completion);
                textField.setText(sb.toString());
                textField.setCaretPosition(position + completion.length());
                textField.moveCaretPosition(position);
                mode = Mode.COMPLETION;
            }catch(Exception ex){
                
            }
        }
    }

    private void buildSearchPopUp(final int pos){
        popupMenu = new JPopupMenu();
        popupMenu.setFocusable(false);
        popupMenu.setBackground(Color.WHITE);
        
        Point p = new Point();
        p.x = 0;
        p.y = (textField.getHeight() / 2) - 5;
        
        int count = 0;
        for (String str : keywords) {
            if(str.toLowerCase().startsWith(prefix.toLowerCase())){
                final String completion = (prefix.length() < str.length()) ? str.substring(prefix.length()) : "";
                
                JMenuItem menuItem = new JMenuItem();
                menuItem.setBackground(Color.WHITE);
                menuItem.setText(str); 
                menuItem.setPreferredSize(new Dimension(400, 30));   
                popupMenu.add(menuItem);
                
                menuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SwingUtilities.invokeLater(new CompletionTask(completion, pos + 1));
                    }
                });
                count++;
            }
        }
        
        if(count > 0){
            popupMenu.setVisible(true);
            popupMenu.pack();
            popupMenu.show(textField, p.x, p.y + 20);
        }

    }

}
