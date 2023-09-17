/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopCall.settings;

import javax.sound.sampled.AudioFormat;

/**
 *
 * @author Faiz
 */
public class AudioFormats {

    public static AudioFormat activeAudioFormat() {
        return getAudioFormat8K_16b_1C();
    }

    public static AudioFormat getAudioFormat8K_16b_1C() {
//        float sampleRate = 8000.0F;
//        int sampleSizeInBits = 16;
//        int channels = 1;
//        boolean signed = true;
//        boolean bigEndian = true;
//        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
//                channels, signed, bigEndian);
//        return format;

        float sampleRate = 8000.0F;
        int sampleInbits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleInbits, channels, signed, bigEndian);

    }

    public static AudioFormat getAudioFormat16K_8b_2C() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;

        boolean signed = true;
        boolean bigEndian = false;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        return format;
    }

    public static AudioFormat getAudioFormat44K_16b_2C() {
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 44100.0f;
        int channels = 2;
        int sampleSize = 16;
        boolean bigEndian = false;

        AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8)
                * channels, rate, bigEndian);
        return format;

    }

}
