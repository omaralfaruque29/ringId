/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.media;

import com.desktopCall.settings.AudioFormats;
import com.desktopCall.settings.SignalGenerator;
import com.desktopCall.settings.VoiceConstants;
import com.ipv.codec.G729A;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author Faiz
 */
public class AudioStreamRecorder {

    boolean isRunning = false;
    AudioFormat adFormat;
    TargetDataLine targetDataLine;
    AudioInputStream InputStream;

    public AudioStreamRecorder() {
        captureAudio();
        isRunning = true;
    }

    public void stopRecorder() {
        isRunning = false;
        if (targetDataLine != null) {
//            try {
//                targetDataLine.drain();
//            } catch (Exception e) {
//            }
            targetDataLine.close();
        }
    }

    private void captureAudio() {
        try {
            adFormat = AudioFormats.activeAudioFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, adFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLine.open(adFormat);
            targetDataLine.start();

            Thread captureThread = new Thread(new CaptureThread());
            captureThread.start();
        } catch (Exception e) {
            // log.error("Recorder-->" + e);

        }
    }
//
//    private AudioFormat getAudioFormat() {
//        float sampleRate = 8000.0F;
//        int sampleInbits = 16;
//        int channels = 1;
//        boolean signed = true;
//        boolean bigEndian = false;
//        return new AudioFormat(sampleRate, sampleInbits, channels, signed, bigEndian);
//    }

    class CaptureThread extends Thread {

        @Override
        public void run() {
            int[] packetNumber = {10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
            //   int[] packetNumber = {5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
            Random r = new Random();
            try {
                G729A g729 = new G729A();
                //  InetAddress IPAddress = InetAddress.getByName(VoiceConstants.VOICE_SERVER_IP);

                while (isRunning) {
                    int frame_size_in_byte = packetNumber[r.nextInt(10)] * 160;
                    byte[] packet_buffer = new byte[(frame_size_in_byte / 160) * 10];
                    byte[] read_bytes = new byte[frame_size_in_byte];
                    int cnt = targetDataLine.read(read_bytes, 0, read_bytes.length);
                    if (cnt > 0) {
                        try {
                            //   cnt = g729.encode(byteArrayToShortArray(read_bytes), 0, packet_buffer, frame_size_in_byte / 2);
                            short[] shorts = new short[read_bytes.length / 2];
                            ByteBuffer.wrap(read_bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
                            g729.encode(shorts, 0, packet_buffer, frame_size_in_byte / 2);
                        } catch (Exception ex) {
                        }
                        Random rand = new Random();
                        byte[] sendingBytes = new byte[packet_buffer.length + 1
                                + rand.nextInt(VoiceConstants.NUMBER_OF_MAX_GARBAGE)];
                        sendingBytes[0] = (byte) 0;
                        System.arraycopy(packet_buffer, 0, sendingBytes, 1, packet_buffer.length);
                        SignalGenerator.sendRTP(sendingBytes);
                    }
                }
            } catch (Exception e) {
                //  System.exit(0);
            }
        }
    }

    public static short[] byteArrayToShortArray(byte[] byteArray) {
        short[] short_array = new short[byteArray.length / 2];
        for (int i = 0; i < short_array.length; i++) {
            short_array[i] = (short) (((int) byteArray[2 * i + 1] << 8) + (int) byteArray[2 * i]);
        }
        return short_array;
    }
}
