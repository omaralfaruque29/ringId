/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.recorder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import com.ipvision.view.chat.AudioRecorderWindow;

/**
 *
 * @author Shahadat
 */
public class AudioRecorder extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AudioRecorder.class);
    private AudioRecorderWindow audioRecorderWindow;
    private RecordingTimer recordingTimer;
    private int NO_OF_FRAME = 4;
    private int frameIndex = 0;

    private TargetDataLine line;
    private AudioFormat format;
    private DataLine.Info info;
    private String fileName = null;
    private boolean success = true;
    public boolean THREAD_RUNNING = false;

    public AudioRecorder(AudioRecorderWindow audioRecorderWindow) {
        this.setName("Voice Chat Recording");
        this.audioRecorderWindow = audioRecorderWindow;
        this.recordingTimer = audioRecorderWindow.recordingTimer;
        this.fileName = audioRecorderWindow.fileName;

        format = getAudioFormat();
        info = new DataLine.Info(TargetDataLine.class, format);

        try {
            if (AudioSystem.isLineSupported(info)) {
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format, line.getBufferSize());
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
            log.error("Error in here ==>" + ex.getMessage());
        }
    }

    public void stopRecording(boolean stopForcefully) {
        this.success = !stopForcefully;
        if (line != null) {
            line.stop();
            line.close();
        }
        recordingTimer.stop();
    }

    @Override
    public void run() {
        THREAD_RUNNING = true;
        byte audioBytes[] = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int frameSizeInBytes = format.getFrameSize();
            int bufferLengthInFrames = line.getBufferSize() / NO_OF_FRAME;
            int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
            byte[] data = new byte[bufferLengthInBytes];
            int numBytesRead;

            recordingTimer.start();
            line.start();

            while (line.isOpen()) {
                if (recordingTimer.isRunning()) {
                    if ((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) {
                        line.stop();
                        line.close();
                        break;
                    }
                    //System.err.println("VOICE RECORDING " + frameIndex);
                    out.write(data, 0, numBytesRead);
                    frameIndex++;
                }
                Thread.sleep(10);
            }

            out.flush();
            out.close();
            audioBytes = out.toByteArray();
            saveAudioRecord(audioBytes, format);
        } catch (Exception e) {
            success = false;
            //e.printStackTrace();
        log.error("Error in here ==>" + e.getMessage());
        }

        if (!success) {
            File file = new File(audioRecorderWindow.helps.getChatFileDestinationFolder() + "/" + fileName);
            if (file.exists()) {
                file.delete();
            }
        }

        THREAD_RUNNING = false;
    }

    private void saveAudioRecord(byte[] audioBytes, AudioFormat format) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
            AudioInputStream outputAIS = new AudioInputStream(bais, format, audioBytes.length / format.getFrameSize());
            AudioSystem.write(outputAIS, Type.WAVE, new File(audioRecorderWindow.helps.getChatFileDestinationFolder() + "/" + fileName));
            bais.close();
            outputAIS.close();
        } catch (Exception ex) {
            //ex.printStackTrace();
        log.error("Error in saveAudioRecord ==>" + ex.getMessage());
        }
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 8000.0F;
        int sampleInbits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleInbits, channels, signed, bigEndian);
    }

}
