/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BoundedRangeModel;

/**
 *
 * @author shahadat
 */
public class UpDownAction extends AbstractAction {

    public static final Integer UP = 1;
    public static final Integer DOWN = 2;
    private int action = UP;
    
    private BoundedRangeModel vScrollBarModel;
    private int scrollableIncrement;

    public UpDownAction(int action, BoundedRangeModel model, int scrollableIncrement) {
        this.action = action;
        this.vScrollBarModel = model;
        this.scrollableIncrement = scrollableIncrement;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        int value = vScrollBarModel.getValue();
        if (action == UP) {
            value -= scrollableIncrement;
            vScrollBarModel.setValue(value);
        } else if (action == DOWN) {
            value += scrollableIncrement;
            vScrollBarModel.setValue(value);
        }
    }
}
