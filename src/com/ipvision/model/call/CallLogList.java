/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.call;

import com.ipvision.model.FeedBackFields;
import java.util.ArrayList;

/**
 *
 * @author FaizAhmed
 */
public class CallLogList extends FeedBackFields {

    private ArrayList<CallLog> callLog = null;

    public ArrayList<CallLog> getCallLog() {
        return callLog;
    }

    public void setCallLog(ArrayList<CallLog> callLog) {
        this.callLog = callLog;
    }
}
