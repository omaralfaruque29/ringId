package com.ipvision.service.auth;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class AuthSendReceive extends Thread {
    /*
     DatagramPacket finalPacket = new DatagramPacket(finalBytePacket, finalBytePacket.length, InetAddress.getByName(voiceServerIP), port);
     */

    protected DatagramSocket socket;
    private boolean stop = false;
    private final int BUFFER_SIZE = 10240;

    public AuthSendReceive(DatagramSocket param_socket) {
        socket = param_socket;
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
                socket.receive(packet);
                PacketRepository.getInstance().getReceivedQueue().add(packet);
                PacketQueueProcessor2.createInstance();

                //   PacketRepository.getInstance().getReceivedQueue().add(packet);
            } catch (Exception e) {
            }

        }
    }
}
