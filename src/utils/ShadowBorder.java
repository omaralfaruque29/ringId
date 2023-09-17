/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Shahadat
 */
import com.ipvision.view.utility.DefaultSettings;
import java.awt.*;
import javax.swing.border.*;

public class ShadowBorder implements Border {

    protected int m_w = 6;
    protected int m_h = 6;
    protected Color m_topColor = DefaultSettings.APP_DEFAULT_CONTENT_BG_COLOR;
    protected Color m_bottomColor = Color.LIGHT_GRAY;
    protected boolean roundc = false; // Do we want rounded corners on the border?  
    
    public ShadowBorder(boolean round_corners) {
        roundc = round_corners;
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(m_h, m_w, m_h, m_w);
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        w = w - 3;
        h = h - 3;
        x++;
        y++;
        if (roundc) {
            g.setColor(m_topColor);
            g.drawLine(x, y + 3, x, y + h - 3);
            g.drawLine(x + 3, y, x + w - 3, y);
            g.drawLine(x, y + 3, x + 3, y); 
            g.drawLine(x, y + h - 3, x + 3, y + h); 
            g.setColor(m_bottomColor);
            g.drawLine(x + w, y + 3, x + w, y + h - 3);
            g.drawLine(x + 3, y + h, x + w - 3, y + h);
            g.drawLine(x + w - 3, y, x + w, y + 3);
            g.drawLine(x + w, y + h - 3, x + w - 3, y + h); 
        } // Square corners  
        else {
            g.setColor(m_topColor);
            g.drawLine(x, y, x, y + h);
            g.drawLine(x, y, x + w, y);
            g.setColor(m_bottomColor);
            g.drawLine(x + w, y, x + w, y + h);
            g.drawLine(x, y + h, x + w, y + h);
        }
    }
}
