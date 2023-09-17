/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.recorder;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryService;
import com.ipvision.service.uploaddownload.DownLoaderHelps;
import com.ipvision.view.chat.VideoRecorderWindow;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author shahadat
 */
public class VideoRecorder extends Thread {

    DownLoaderHelps helps = new DownLoaderHelps();
    private TargetDataLine line;
    private Webcam webcam;
    private RecordingTimer recordingTimer;
    private boolean success = true;
    private String fileName = null;
    private VideoRecorderWindow videoRecorderWindow;
    public boolean THREAD_RUNNING = false;
    private VideoRecorderWindow.InitListener listener;

    private final float SAMPLE_RATE = 44100.0f;
    private final int SAMPLE_SIZE = 16;
    private final int CHANNELS = 1;

    private int camWidth;
    private int camHeight;

    public VideoRecorder(VideoRecorderWindow videoRecorderWindow, VideoRecorderWindow.InitListener listener) {
        this.setName("Video Chat Recording");
        this.videoRecorderWindow = videoRecorderWindow;
        this.webcam = videoRecorderWindow.webcam;
        this.recordingTimer = videoRecorderWindow.recordingTimer;
        this.fileName = videoRecorderWindow.fileName;
        this.camWidth = videoRecorderWindow.camWidth;
        this.camHeight = videoRecorderWindow.camHeight;
        this.listener = listener;

        AudioFormat format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE, CHANNELS, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (AudioSystem.isLineSupported(info)) {
            try {
                this.line = (TargetDataLine) AudioSystem.getLine(info);
                this.line.open();
                this.line.start();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    int videoStreamIndex = 0;
    int videoStreamId = 0;
    int audioStreamIndex = 1;
    int audioStreamId = 1;

    @Override
    public void run() {
        THREAD_RUNNING = true;

        try {
            IMediaWriter writer = ToolFactory.makeWriter(helps.getChatFileDestinationFolder() + "/" + fileName);
            writer.addVideoStream(videoStreamIndex, videoStreamId, camWidth, camHeight);
            writer.addAudioStream(audioStreamIndex, audioStreamId, CHANNELS, (int) SAMPLE_RATE);

            long start = System.currentTimeMillis();
            AudioRecording audio = new AudioRecording(writer, start);
            VideoRecording video = new VideoRecording(writer, start);
            listener.onInit();
            recordingTimer.start();
            audio.start();
            video.start();

            while (audio.isRunning || video.isRunning) {
                Thread.sleep(10);
            }

            writer.close();
        } catch (Exception ex) {
            success = false;
            ex.printStackTrace();
        }

        if (!success) {
            File file = new File(helps.getChatFileDestinationFolder() + "/" + fileName);
            if (file.exists()) {
                file.delete();
            }
        }
        THREAD_RUNNING = false;
    }

    private class AudioRecording extends Thread {

        private long startTime;
        private IMediaWriter writer;
        public boolean isRunning = true;

        public AudioRecording(IMediaWriter writer, long start) {
            this.setName("Video Chat Recording ==> AUDIO");
            this.writer = writer;
            this.startTime = start;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (line.isOpen() == false) {
                        break;
                    }

                    if (recordingTimer.isRunning()) {
                        byte[] buffer = new byte[line.getBufferSize()];
                        int read;
                        if (line.isOpen() && (read = line.read(buffer, 0, buffer.length)) != -1) {
                            short[] samples = new short[read / 2];
                            ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(samples);
                            writer.encodeAudio(audioStreamIndex, samples);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                isRunning = false;
            }
        }

    }

    private class VideoRecording extends Thread {

        private long startTime;
        private IMediaWriter writer;
        public boolean isRunning = true;

        public VideoRecording(IMediaWriter writer, long start) {
            this.setName("Video Chat Recording ==> VIDEO");
            this.writer = writer;
            this.startTime = start;
        }

        @Override
        public void run() {
            try {
                long i = 0;
                long pauseTime = 0;
                long prevTime = 0;
                boolean isPaused = false;

                while (true) {
                    if (webcam.isOpen() == false) {
                        break;
                    }

                    long clock = System.currentTimeMillis();
                    if (recordingTimer.isRunning()) {
                        isPaused = false;
                        if (webcam.isOpen()) {
                            BufferedImage image = ConverterFactory.convertToType(webcam.getImage(), BufferedImage.TYPE_3BYTE_BGR);
                            IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);
                            IVideoPicture frame = converter.toPicture(image, (clock - startTime - pauseTime) * 1000);//(System.currentTimeMillis() - startTime) * 1000
                            frame.setKeyFrame(i == 0);
                            frame.setQuality(0);
                            writer.encodeVideo(videoStreamIndex, frame);
                            //System.err.println("##### ==> " + i + ", " + (clock - startTime - pauseTime));
                            i++;
                        }
                    } else {
                        if (isPaused) {
                            pauseTime += clock - prevTime;
                        }
                        isPaused = true;
                    }
                    prevTime = clock;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                isRunning = false;
            }
        }

    }

    public void stopRecording(boolean stopForcefully) {
        success = !stopForcefully;
        new StopRecording().start();
    }

    private class StopRecording extends Thread {

        @Override
        public void run() {
            try {
                recordingTimer.stop();
                if (webcam != null) {
                    webcam.close();
                }
                if (line != null) {
                    line.stop();
                    line.close();
                }

                WebcamDiscoveryService discovery = Webcam.getDiscoveryService();
                if(discovery.isRunning()) {
                    discovery.stop();
                }
            } catch (Exception ex) {
            }
        }

    }
}
