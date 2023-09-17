/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.auth;

import java.net.DatagramPacket;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author Faiz
 */
public class PacketQueueProcessor extends Thread {

    private ExecutorService executorService;
    private boolean running = true;

    public PacketQueueProcessor() {
        setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        //    executorService = Executors.newFixedThreadPool(100);
        while (running) {
            try {

                while (!PacketRepository.getInstance().getReceivedQueue().isEmpty()) {
                    System.out.println("ddd");
                    DatagramPacket packet = PacketRepository.getInstance().getReceivedQueue().poll();
                    if (packet != null) {
                        System.out.println("packet");
                        PacketProcessor processor = new PacketProcessor(packet);
                        processor.start();
                        // executorService.execute(processor);
                    }
                }
            } catch (Exception ex) {

            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException iEx) {

            }
        }
    }

    public boolean stopService() {
        running = false;
        return running;
    }

}
