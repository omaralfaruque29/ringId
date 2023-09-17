/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model.call;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ipvision.service.utility.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import com.ipvision.model.dao.InsertIntoRingCallLogTable;

/**
 *
 * @author FaizAhmed
 */
public class CallLogHelper extends HttpRequest {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CallLogHelper.class);
    public static Gson jsonLib;

    public CallLogHelper() {
        jsonLib = new GsonBuilder().serializeNulls().create();
    }

    public static void callStartTimeToCallLog(String call_id, String friend_id, boolean outgoing) {
        long time = System.currentTimeMillis();
        CallLog callLog = new CallLog();
        if (outgoing) {
            callLog.setCallType(1);
        } else {
            callLog.setCallType(2);
        }
        callLog.setCallID(call_id);
        callLog.setFriendIdentity(friend_id);
        //callLog.setPhoneNo(phone_no);
        callLog.setCallDuration(0L);
        callLog.setCallingTime(time);
        callLog.setUpdateTime(time);

        List<CallLog> callLogList = new ArrayList<CallLog>();
        callLogList.add(callLog);
        log.debug("START CALL LOG-->" + callLog.toString());
        new InsertIntoRingCallLogTable(callLogList, false).start();

    }

    public static void callEndTimeToCallLog(String call_id, String friend_id, Long duration, boolean isMissCall, boolean isOutgoing) {
        CallLog callLog = new CallLog();
        if (isMissCall) {
            callLog.setCallType(0);
        } else if (isOutgoing) {
            callLog.setCallType(1);
        } else {
            callLog.setCallType(2);
        }
        long time = System.currentTimeMillis();
        callLog.setCallID(call_id);
        callLog.setFriendIdentity(friend_id);
        //callLog.setPhoneNo(phone_no);
        callLog.setCallDuration(duration);
        callLog.setCallingTime(time);
        callLog.setUpdateTime(time);
        List<CallLog> callLogList = new ArrayList<CallLog>();
        callLogList.add(callLog);
        log.debug("STOP CALL LOG-->" + callLog.toString());
        new InsertIntoRingCallLogTable(callLogList, true).start();
    }
}
