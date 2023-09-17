/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.communicator;

import com.ipv.chat.ChatSettings;
import com.ipv.chat.listener.PacketReceivedListener;
import com.ipv.chat.udp.IMDatagramPacket;
import com.ipv.chat.udp.IMDatagramSocket;
import java.io.IOException;


/**
 *
 * @author Shahadat Hossain
 */
public class ChatReceiver extends Thread {

    protected IMDatagramSocket socket;
    protected PacketReceivedListener listener;
    private boolean stop = false;

    public ChatReceiver(IMDatagramSocket socket, PacketReceivedListener listener) {
        this.socket = socket;
        this.listener = listener;
        setName(this.getClass().getSimpleName());
    }

    public IMDatagramSocket getUdpSocket() {
        return socket;
    }

    protected void send(IMDatagramPacket packet) throws IOException {
        if (!stop) {
            socket.send(packet);
        }
    }

    protected void halt() {
        stop = true;
        socket.close();
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                byte[] buf = new byte[ChatSettings.BUFFER_SIZE];
                IMDatagramPacket packet = new IMDatagramPacket(buf, buf.length);
                socket.receive(packet);
                if (packet.getLength() >= 0) {
                    if (listener != null) {
                        listener.onPacketReceived(packet);
                    }
                }
            } catch (Exception e) {
            }

        }
    }
}
