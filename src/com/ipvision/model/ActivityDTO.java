/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

import java.util.Map;

/**
 *
 * @author Shahadat Hossain
 */
public class ActivityDTO implements Cloneable{

    private long id;
    private String activityBy;
    private String friendIdentity;
    private Long groupId;
    private int activityType;
    private int messageType;
    private long updateTime;
    private boolean isProcessed;
    private Map<String, String> members;
    private int fromCt;
    private int toCt;
    private String prevGroupName;
    private String packetID;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getActivityBy() {
        return activityBy;
    }

    public void setActivityBy(String userIdentity) {
        this.activityBy = userIdentity;
    }

    public String getFriendIdentity() {
        return friendIdentity;
    }

    public void setFriendIdentity(String friendIdentity) {
        this.friendIdentity = friendIdentity;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(boolean isProcessed) {
        this.isProcessed = isProcessed;
    }

    public Map<String, String> getMembers() {
        return members;
    }

    public void setMembers(Map<String, String> members) {
        this.members = members;
    }

    public int getFromContactType() {
        return fromCt;
    }

    public void setFromContactType(int fromCt) {
        this.fromCt = fromCt;
    }

    public int getToContactType() {
        return toCt;
    }

    public void setToContactType(int toCt) {
        this.toCt = toCt;
    }

    public String getPrevGroupName() {
        return prevGroupName;
    }

    public void setPrevGroupName(String prevGroupName) {
        this.prevGroupName = prevGroupName;
    }

    public String getPacketID() {
        return packetID;
    }

    public void setPacketID(String packetID) {
        this.packetID = packetID;
    }

    @Override
    public Object clone() {
        try {
            return (Object) super.clone();
        } catch (Exception e) {
            System.out.println (" Cloning not allowed. " );
            return this;
        }
    }



}
