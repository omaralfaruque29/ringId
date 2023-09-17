/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.ArrayList;
import com.ipvision.model.call.CallLog;

/**
 * @author Shahadat Hossain
 */
public class RecentDTO {
    
    private String contactId;
    private int contactType;
    private int type;
    private Long time;
    private String historyDateName;
    private int callCount;
    private int sequence;
    private CallLog callLog;
    private MessageDTO messageDTO;
    private ActivityDTO activityDTO;
    private ArrayList<String> callIds;

    public ArrayList<String> getCallIds() {
        return callIds;
    }

    public void setCallIds(ArrayList<String> callIds) {
        this.callIds = callIds;
    }

    public int getCallCount() {
        return callCount;
    }

    public void setCallCount(int callCount) {
        this.callCount = callCount;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public CallLog getCallLog() {
        return callLog;
    }

    public void setCallLog(CallLog callLog) {
        this.callLog = callLog;
    }

    public Long getTime() {
        return time;
    }
    
    public void setTime(Long time) {
        this.time = time;
    }

    public String getContactId() {
        return contactId;
    }

    public int getContactType() {
        return contactType;
    }

    public void setContactType(int contactType) {
        this.contactType = contactType;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
    
    public MessageDTO getMessageDTO() {
        return messageDTO;
    }

    public void setMessageDTO(MessageDTO messageDTO) {
        this.messageDTO = messageDTO;
    }

    public ActivityDTO getActivityDTO() {
        return activityDTO;
    }

    public void setActivityDTO(ActivityDTO activityDTO) {
        this.activityDTO = activityDTO;
    }
    
    public String getHistoryDateName() {
        return historyDateName;
    }

    public void setHistoryDateName(String historyDateName) {
        this.historyDateName = historyDateName;
    }
    
}
