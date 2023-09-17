/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.model;

/**
 *
 * @author Ashraful
 */
public class AttributeValues {

    private int packetID;
    private int action;
    private int totalPacket;
    private int packetNumber;
    private int uniqueKey;
    private byte[] data;

    public int getPacketID() {
        return packetID;
    }

    public void setPacketID(int packetID) {
        this.packetID = packetID;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getTotalPacket() {
        return totalPacket;
    }

    public void setTotalPacket(int totalPacket) {
        this.totalPacket = totalPacket;
    }

    public int getPacketNumber() {
        return packetNumber;
    }

    public void setPacketNumber(int packetNumber) {
        this.packetNumber = packetNumber;
    }

    public int getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(int uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
