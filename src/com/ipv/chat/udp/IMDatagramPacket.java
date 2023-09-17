
package com.ipv.chat.udp;

import java.net.DatagramPacket;
//import java.net.InetAddress;

public class IMDatagramPacket {

    /**
     * The DatagramPacket
     */
    DatagramPacket packet;

    /**
     * Creates a new UdpPacket
     */
    IMDatagramPacket(DatagramPacket packet) {
        this.packet = packet;
    }

    /**
     * Gets the DatagramPacket
     */
    DatagramPacket getDatagramPacket() {
        return packet;
    }

    /**
     * Sets the DatagramPacket
     */
    void setDatagramPacket(DatagramPacket packet) {
        this.packet = packet;
    }

    /**
     * Creates a new UdpPacket
     */
    public IMDatagramPacket(byte[] data) {
        packet = new DatagramPacket(data, data.length);
    }

    /**
     * Creates a new UdpPacket
     */
    public IMDatagramPacket(byte[] data, IMInetAddress ipaddr, int port) {
        packet = new DatagramPacket(data, data.length, ipaddr.getInetAddress(), port);
    }

    /**
     * Creates a new UdpPacket
     */
    public IMDatagramPacket(byte[] buf, int length) {
        packet = new DatagramPacket(buf, length);
    }

    /**
     * Creates a new UdpPacket
     */
    public IMDatagramPacket(byte[] buf, int length, IMInetAddress ipaddr, int port) {
        packet = new DatagramPacket(buf, length, ipaddr.getInetAddress(), port);
    }

    /**
     * Creates a new UdpPacket
     */
    public IMDatagramPacket(byte[] buf, int offset, int length) {
        packet = new DatagramPacket(buf, offset, length);
    }

    /**
     * Creates a new UdpPacket
     */
    public IMDatagramPacket(byte[] buf, int offset, int length, IMInetAddress ipaddr, int port) {
        packet = new DatagramPacket(buf, offset, length, ipaddr.getInetAddress(), port);
    }

    /**
     * Gets the IP address of the machine to which this datagram is being sent
     * or from which the datagram was received.
     */
    public IMInetAddress getIpAddress() {
        return new IMInetAddress(packet.getAddress());
    }

    /**
     * Gets the data received or the data to be sent.
     */
    public byte[] getData() {
        return packet.getData();
    }

    /**
     * Gets the length of the data to be sent or the length of the data
     * received.
     */
    public int getLength() {
        return packet.getLength();
    }

    /**
     * Gets the offset of the data to be sent or the offset of the data
     * received.
     */
    public int getOffset() {
        return packet.getOffset();
    }

    /**
     * Gets the port number on the remote host to which this datagram is being
     * sent or from which the datagram was received.
     */
    public int getPort() {
        return packet.getPort();
    }

    /**
     * Sets the IP address of the machine to which this datagram is being sent.
     */
    public void setIpAddress(IMInetAddress ipaddr) {
        packet.setAddress(ipaddr.getInetAddress());
    }

    /**
     * Sets the data buffer for this packet.
     */
    public void setData(byte[] buf) {
        packet.setData(buf);
    }

    /**
     * Sets the data buffer for this packet.
     */
    public void setData(byte[] buf, int offset, int length) {
        packet.setData(buf, offset, length);
    }

    /**
     * Sets the length for this packet.
     */
    public void setLength(int length) {
        packet.setLength(length);
    }

    /**
     * Sets the port number on the remote host to which this datagram is being
     * sent.
     */
    public void setPort(int iport) {
        packet.setPort(iport);
    }

    /**
     * Makes a copy of a given UdpPacket.
     */
    public void copy(IMDatagramPacket udp_pkt) {
        setData(udp_pkt.getData(), udp_pkt.getOffset(), udp_pkt.getLength());
        setLength(udp_pkt.getLength());
        setIpAddress(udp_pkt.getIpAddress());
        setPort(udp_pkt.getPort());
    }
}
