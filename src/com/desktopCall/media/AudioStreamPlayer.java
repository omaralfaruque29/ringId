/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.media;

import com.desktopCall.listeners.OnReceiveListner;
import com.desktopCall.settings.AudioFormats;
import com.ipv.codec.G729A;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author Faiz
 */
public class AudioStreamPlayer implements OnReceiveListner {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AudioStreamPlayer.class);
    ByteArrayOutputStream byteOutputStream;
    AudioFormat adFormat;
    TargetDataLine targetDataLine;
    AudioInputStream InputStream;
    SourceDataLine sourceLine = null;
    G729A g729;
    boolean isRunning = false;

    public AudioStreamPlayer() {
        g729 = new G729A();
        init();
        isRunning = true;
    }

    public void stopPlayer() {
        isRunning = false;
        if (sourceLine != null) {
//            try {
//                sourceLine.drain();
//            } catch (Exception e) {
//            }
            sourceLine.close();
        }
    }

    private void init() {
        try {
            adFormat = AudioFormats.activeAudioFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, adFormat);
            sourceLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceLine.open(adFormat);
            sourceLine.start();
        } catch (LineUnavailableException ex) {
           // System.out.println("Player-->" + ex);
        log.error("Exception in AudioStreamPlayer class" + ex.getMessage());
        }
    }

    @Override
    public void onReceivedMessage(byte[] receivedByte, int length) {
        try {
            int receivedLength = length - 1;
            length = receivedLength - (receivedLength % 10);
            byte audioData[] = new byte[length];
            System.arraycopy(receivedByte, 1, audioData, 0, length);
            short[] decoded = new short[(length / 10) * 80];
            g729.decode(audioData, decoded, length);
            //   byte[] playBytes = shortArrayToByteArray(decoded);
            byte[] bytes2 = new byte[decoded.length * 2];
            ByteBuffer.wrap(bytes2).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(decoded);

            if (sourceLine == null) {
                isRunning = true;
                adFormat = AudioFormats.activeAudioFormat();
                DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, adFormat);
                sourceLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                sourceLine.open(adFormat);
                sourceLine.start();
            } else if (sourceLine != null && !sourceLine.isRunning()) {
                sourceLine.start();
            }
            InputStream byteInputStream = new ByteArrayInputStream(bytes2);
            InputStream = new AudioInputStream(byteInputStream, adFormat, bytes2.length / adFormat.getFrameSize());

            Thread playThread = new Thread(new PlayThread());
            playThread.start();
        } catch (Exception e) {
          log.error("Exception in AudioStreamPlayer class" + e.getMessage());
        }
    }

    class PlayThread extends Thread {

        byte tempBuffer[] = new byte[10000];

        @Override
        public void run() {
            try {
                int cnt;
                while ((cnt = InputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                    if (cnt > 0) {
                        sourceLine.write(tempBuffer, 0, cnt);
                    }
                }
            } catch (Exception e) {
                log.error("Exception in AudioStreamPlayer class" + e.getMessage());
            }
        }
    }

    public static byte[] shortArrayToByteArray(short[] short_array) {
        byte[] byte_array = new byte[short_array.length * 2];
        for (int i = 0; i < short_array.length; ++i) {
            byte_array[2 * i] = getByte1(short_array[i]);
            byte_array[2 * i + 1] = getByte2(short_array[i]);
        }
        return byte_array;
    }

    public static byte getByte1(short s) {
        return (byte) s;
    }

    public static byte getByte2(short s) {
        return (byte) (s >> 8);
    }
}
