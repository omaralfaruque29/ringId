/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.file;

import com.ipv.chat.ChatConstants;
import com.ipv.chat.ChatSettings;

import com.ipv.chat.dto.ChatFileDTO;
import com.ipv.chat.listener.UploadProgressListener;
import com.ipv.chat.utils.ChatPacketUility;
import com.ipv.codec.G729A;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Shahadat Hossain
 */
public class ChatFileUploader extends Thread {

    private G729A g729 = new G729A();
    private ChatFileDTO chatFileDTO;
    private UploadProgressListener uploadProgressListener;

    public ChatFileUploader(ChatFileDTO chatFileDTO, UploadProgressListener uploadProgressListener) {
        this.chatFileDTO = chatFileDTO;
        this.uploadProgressListener = uploadProgressListener;
    }

    @Override
    public void run() {
        uploadFile();
    }

    public void uploadFile() {
        HttpURLConnection conn = null;
        DataOutputStream dos;
        DataInputStream inStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        String image_name = chatFileDTO.getFile().getName();
        String extension = image_name.substring(image_name.lastIndexOf(".") + 1);
        image_name = chatFileDTO.getMessageType() == ChatConstants.TYPE_AUDIO_FILE ? image_name.replace(extension, "g729") : image_name;

        try {
            byte[] byteArray;
            if (chatFileDTO.getMessageType() == ChatConstants.TYPE_AUDIO_FILE) {
                byteArray = getG729EncodedByteArrayFromFile(chatFileDTO.getFile());
            } else {
                byteArray = getByteArrayFromFile(chatFileDTO.getFile());
            }

            URL url = new URL(chatFileDTO.getUploadUrl());
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"sId\"" + lineEnd + lineEnd + ChatSettings.SESSION_ID + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uId\"" + lineEnd + lineEnd + ChatSettings.USER_IDENTITY + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"authServer\"" + lineEnd + lineEnd + ChatSettings.AUTH_SERVER_IP + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"authPort\"" + lineEnd + lineEnd + ChatSettings.AUTH_SERVER_PORT + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedFile\";filename=\"" + image_name + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.write(byteArray);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            dos.flush();
            dos.close();
        } catch (MalformedURLException ex) {
        } catch (IOException e) {
        }

        try {
            if (conn != null) {
                inStream = new DataInputStream(conn.getInputStream());
                try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
                    byte[] data = new byte[1024];
                    int nRead;
                    while ((nRead = inStream.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    buffer.flush();

                    ChatFileDTO responseDTO = ChatPacketUility.getChatFileUploadResponse(buffer.toByteArray());
                    chatFileDTO.setMessage(responseDTO.getUploadUrl());
                    chatFileDTO.setErrorMsg(responseDTO.getErrorMsg());
                    if (responseDTO.getStatus() == 1) {
                        uploadProgressListener.onSuccess(chatFileDTO);
                    } else {
                        uploadProgressListener.onFailed(chatFileDTO);
                    }
                    inStream.close();
                }
            } else {
                uploadProgressListener.onFailed(chatFileDTO);
            }
        } catch (IOException ex) {
            uploadProgressListener.onFailed(chatFileDTO);
        }
    }

    public byte[] getG729EncodedByteArrayFromFile(File file) throws FileNotFoundException {
        byte[] array = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();

            int SIZE = 160 * 10;
            byte[] buffer = new byte[SIZE];
            byte[] encoded = new byte[10 * 10];
            try {
                for (int readNum; (readNum = fis.read(buffer)) != -1;) {
                    short[] temp = new short[buffer.length / 2];
                    ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(temp);
                    g729.encode(temp, 0, encoded, readNum / 2);
                    //System.err.println("EN SIZE :: " + lng);
                    bos.write(encoded, 0, encoded.length);
                }
            } catch (Exception ex) {
            }
            array = bos.toByteArray();

            fis.close();
            bos.close();
        } catch (Exception ex) {
            try {
                fis.close();
                bos.close();
            } catch (Exception e) {
            }
        }
        return array;
    }

    public byte[] getByteArrayFromFile(File file) throws FileNotFoundException {
        byte[] array = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            try {
                for (int readNum; (readNum = fis.read(buf)) != -1;) {
                    bos.write(buf, 0, readNum);
                }
            } catch (Exception ex) {
            }
            array = bos.toByteArray();

            fis.close();
            bos.close();
        } catch (Exception ex) {
            try {
                fis.close();
                bos.close();
            } catch (Exception e) {
            }
        }
        return array;
    }

}
