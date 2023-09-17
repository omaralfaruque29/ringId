/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.file;

import com.ipv.chat.listener.DownloadProgressListener;
import com.ipv.chat.ChatConstants;
import com.ipv.chat.dto.MessageBaseDTO;
import com.ipv.chat.utils.ProgressAwareInputStream;
import com.ipv.codec.G729A;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 *
 * @author Shahadat
 */
public class ChatFileDownloader extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChatFileDownloader.class);
    private G729A g729 = new G729A();
    private MessageBaseDTO entity;
    private String destinationPath;
    private String sourceUrl;
    private DownloadProgressListener downloadProgressListener;

    public ChatFileDownloader(MessageBaseDTO entity, String sourceUrl, String destinationPath, DownloadProgressListener downloadProgressListener) {
        this.entity = entity;
        this.downloadProgressListener = downloadProgressListener;
        this.destinationPath = destinationPath;
        this.sourceUrl = sourceUrl;
    }

    @Override
    public void run() {
        process();
    }

    public void process() {
        try {
            String imageName = getFileName(entity.getMessage());
            String source = sourceUrl + "/" + entity.getMessage();

            if (contains(imageName, ".")) {
                String extension = imageName.substring(imageName.lastIndexOf(".") + 1);
                String destination = destinationPath + (entity.getMessageType() == ChatConstants.TYPE_AUDIO_FILE ? imageName.replace(extension, "mp3") : imageName);
                boolean isFileExist = isFileExistInDirectory(destination);

                if (!isFileExist) {
                    try {
                        URL url = new URL(source);
                        URLConnection yc = url.openConnection();
                        InputStream is = yc.getInputStream();
                        ProgressAwareInputStream pais = new ProgressAwareInputStream(is, yc.getContentLength(), null);
                        pais.setOnProgressListener(downloadProgressListener);

                        if (entity.getMessageType() == ChatConstants.TYPE_AUDIO_FILE) {
                            downloadAudio(destination, pais);
                        } else {
                            downloadFile(destination, pais);
                        }

                        is.close();
                    } catch (Exception e) {
                        downloadProgressListener.onFailed(entity);
                        return;
                    }
                }
            } else {
                downloadProgressListener.onFailed(entity);
                return;
            }

            downloadProgressListener.onSuccess(entity);
        } catch (Exception ex) {
            downloadProgressListener.onFailed(entity);
        }
    }

    public void downloadFile(String destination, ProgressAwareInputStream pais) {
        try {
            FileOutputStream fos = new FileOutputStream(destination);
            int size = 5120;
            byte[] buffer = new byte[size];
            int bytesRead = 0;
            while ((bytesRead = pais.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.close();
        } catch (Exception e) {
        }
    }

    public void downloadAudio(String destination, ProgressAwareInputStream pais) {
        byte[] array = null;
        ByteArrayOutputStream bos = null;

        try {
            bos = new ByteArrayOutputStream();
            int SIZE = 10 * 50;
            byte[] buffer = new byte[SIZE];
            short[] decoded = new short[50 * 80];
            try {
                for (int readNum; (readNum = pais.read(buffer)) != -1;) {
                    g729.decode(buffer, decoded, readNum);
                    //System.err.println("DE SIZE :: " + lng);
                    byte[] temp = new byte[decoded.length * 2];
                    ByteBuffer.wrap(temp).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(decoded);
                    bos.write(temp, 0, temp.length);
                }
            } catch (Exception ex) {
            }

            array = bos.toByteArray();
            saveAudioRecord(array, destination);

            bos.close();
        } catch (Exception e) {
        }
    }

    private boolean isFileExistInDirectory(String directory) {
        File f = new File(directory);
        if (f.exists() && !f.isDirectory()) {
            return true;
        }
        return false;
    }

    public String getFileName(String imageUrl) {
        String fullImageName;
        String userId = "";
        String imageName = "";
        String[] split = imageUrl.split("/");
        if (split.length == 2) {
            userId = split[0];
            imageName = split[1];
        }
        fullImageName = userId + "_" + imageName;
        return fullImageName;
    }

    public boolean contains(String main, String sub) {
        if (main.toLowerCase().contains(sub.toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }

    private void saveAudioRecord(byte[] audioBytes, String destination) {
        try {
            AudioFormat format = format = getAudioFormat();
            ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
            AudioInputStream outputAIS = new AudioInputStream(bais, format, audioBytes.length / format.getFrameSize());
            AudioSystem.write(outputAIS, AudioFileFormat.Type.WAVE, new File(destination));
            bais.close();
            outputAIS.close();
        } catch (Exception ex) {
            //ex.printStackTrace();
            log.error("Error in savingAudioRecord==>" + ex.getMessage());
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
