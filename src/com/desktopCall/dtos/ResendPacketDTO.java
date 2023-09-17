/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.dtos;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *
 * @author Faiz
 */
public class ResendPacketDTO {

    private DatagramPacket packet;
    private DatagramSocket udpSocket;
    private int numberOfResend;

    public DatagramPacket getPacket() {
        return packet;
    }

    public void setPacket(DatagramPacket packet) {
        this.packet = packet;
    }

    public DatagramSocket getUdpSocket() {
        return udpSocket;
    }

    public void setUdpSocket(DatagramSocket udpSocket) {
        this.udpSocket = udpSocket;
    }

    public int getNumberOfResend() {
        return numberOfResend;
    }

    public void setNumberOfResend(int numberOfResend) {
        this.numberOfResend = numberOfResend;
    }
}
