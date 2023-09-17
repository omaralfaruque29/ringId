package com.ipv.chat.udp;

import com.ipv.chat.ChatSettings;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class IMDatagramSocket {

    DatagramSocket socket;

    public DatagramSocket getSocket() {
        return socket;
    }

    public IMDatagramSocket() {
        try {
            this.socket = new DatagramSocket();
        } catch (SocketException ex) {
            if (ChatSettings.DEBUG) {
                System.err.println("Can not create new socket");
            }
        }
    }

    public IMDatagramSocket(int port) throws java.net.SocketException {
        socket = new DatagramSocket(port);
    }

    public IMDatagramSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public IMDatagramSocket(int port, IMInetAddress ipaddr) throws java.net.SocketException {
        socket = new DatagramSocket(port, ipaddr.getInetAddress());
    }

    public void close() {
        socket.close();
    }

    public void receive(IMDatagramPacket pkt) throws java.io.IOException {
        DatagramPacket dgram = pkt.getDatagramPacket();
        socket.receive(dgram);
        pkt.setDatagramPacket(dgram);
    }

    public void send(IMDatagramPacket pkt) throws java.io.IOException {
        socket.send(pkt.getDatagramPacket());
    }
}
