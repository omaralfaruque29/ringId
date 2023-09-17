/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.auth;

import java.net.DatagramPacket;

/**
 *
 * @author Faiz
 */
public class PacketQueueProcessor2 extends Thread {

    private static PacketQueueProcessor2 packetQueueProcessor2;

    public PacketQueueProcessor2 getPacketQueueProcessor2() {
        return packetQueueProcessor2;
    }

    public void setPacketQueueProcessor2(PacketQueueProcessor2 packetQueueProcessor2) {
        PacketQueueProcessor2.packetQueueProcessor2 = packetQueueProcessor2;
    }

    public PacketQueueProcessor2() {
        setName(this.getClass().getSimpleName());
    }

    public static void createInstance() {
        if (packetQueueProcessor2 == null) {
            packetQueueProcessor2 = new PacketQueueProcessor2();
            packetQueueProcessor2.start();
        }
    }

    @Override
    public void run() {
        while (!PacketRepository.getInstance().getReceivedQueue().isEmpty()) {
            System.out.println("processing...");
            DatagramPacket packet = PacketRepository.getInstance().getReceivedQueue().poll();
            if (packet != null) {
                System.out.println("PacketRepository.getInstance().getReceivedQueue()==>" + PacketRepository.getInstance().getReceivedQueue().size());
                PacketProcessor processor = new PacketProcessor(packet);
                processor.start();
            }
        }
        setPacketQueueProcessor2(null);
        System.out.println("null");

    }

//    public static void addItem(DatagramPacket packet) {
//        PacketRepository.getInstance().getReceivedQueue().add(packet);
//    }
}
