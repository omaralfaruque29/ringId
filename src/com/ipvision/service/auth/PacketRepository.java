/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.auth;

import java.net.DatagramPacket;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Faiz
 */
public class PacketRepository {

    private static PacketRepository instance;
    private ConcurrentLinkedQueue<DatagramPacket> receivedQueue;

    public PacketRepository() {
        receivedQueue = new ConcurrentLinkedQueue<DatagramPacket>();
    }

    public static PacketRepository getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }

    private static void createInstance() {
        if (instance == null) {
            instance = new PacketRepository();
        }
    }

    public ConcurrentLinkedQueue<DatagramPacket> getReceivedQueue() {
        return receivedQueue;
    }
}
