/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.net;

import com.desktopCall.listeners.OnReceiveVoiceInterface;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *
 * @author Faiz
 */
public class VoiceReceiverSocket extends Thread {

    protected DatagramSocket socket;
    protected OnReceiveVoiceInterface listener;
    private boolean stop = false;
    private int BUFFER_SIZE = 10007;

    public VoiceReceiverSocket(DatagramSocket param_socket, OnReceiveVoiceInterface param_listener) {
        socket = param_socket;
        listener = param_listener;
        setName(this.getClass().getSimpleName());
    }

    public DatagramSocket getUdpSocket() {
        return socket;
    }

    public void send(DatagramPacket packet) throws IOException {
        if (!stop) {
            socket.send(packet);
        }
    }

    public void halt() {
        stop = true;
        socket.close();
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                byte[] buf = new byte[BUFFER_SIZE];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                buf = packet.getData();
                socket.receive(packet);
                if (packet.getLength() >= 0) {
                    if (listener != null) {
                        listener.onReceivedPacket(buf, packet.getLength());
                    }
                }
            } catch (Exception e) {
            }

        }
    }
}
