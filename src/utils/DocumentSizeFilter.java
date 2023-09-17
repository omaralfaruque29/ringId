/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author user
 */
public class DocumentSizeFilter extends DocumentFilter {

    private int max_Characters;
    private boolean DEBUG;

    public DocumentSizeFilter(int max_Chars) {

        max_Characters = max_Chars;
        DEBUG = false;
    }

    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offset, String str, AttributeSet a)
            throws BadLocationException {

        if ((fb.getDocument().getLength() + str.length()) <= max_Characters) {
            super.insertString(fb, offset, str, a);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String str, AttributeSet a)
            throws BadLocationException {

        if ((fb.getDocument().getLength() + str.length() - length) <= max_Characters) {
            super.replace(fb, offset, length, str, a);
        } else {
            int leftSpace = max_Characters - fb.getDocument().getLength() + length;
            if(leftSpace > 0 && str.length() > 0) {
                super.replace(fb, offset, length, str.substring(0, (str.length() < leftSpace ? str.length() : leftSpace)), a);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}
