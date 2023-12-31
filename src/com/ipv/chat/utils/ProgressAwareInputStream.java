/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipv.chat.utils;

import com.ipv.chat.listener.DownloadProgressListener;
import java.io.IOException;
import java.io.InputStream;

/**
 * InputStream that notifies listeners of its progress.
 */
public class ProgressAwareInputStream extends InputStream {

    private InputStream wrappedInputStream;
    private long size;
    private Object tag;
    private long counter;
    private long lastPercent;
    private DownloadProgressListener listener;

    public ProgressAwareInputStream(InputStream in, long size, Object tag) {
        wrappedInputStream = in;
        this.size = size;
        this.tag = tag;
    }

    public void setOnProgressListener(DownloadProgressListener listener) {
        this.listener = listener;
    }

    public Object getTag() {
        return tag;
    }

    @Override
    public int read() throws IOException {
        counter += 1;
        check();
        return wrappedInputStream.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        int retVal = wrappedInputStream.read(b);
        counter += retVal;
        check();
        return retVal;
    }

    @Override
    public int read(byte[] b, int offset, int length) throws IOException {
        int retVal = wrappedInputStream.read(b, offset, length);
        counter += retVal;
        check();
        return retVal;
    }

    private void check() {
        if (counter > size) {
            counter = size;
        }
        int percent = (int) (counter * 100 / size);
        if (percent - lastPercent >= 1) {
            lastPercent = percent;
            if (listener != null) {
                listener.onProgress(percent, tag);
            }
        }
    }

    @Override
    public void close() throws IOException {
        wrappedInputStream.close();
    }

    @Override
    public int available() throws IOException {
        return wrappedInputStream.available();
    }

    @Override
    public void mark(int readlimit) {
        wrappedInputStream.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        wrappedInputStream.reset();
    }

    @Override
    public boolean markSupported() {
        return wrappedInputStream.markSupported();
    }

    @Override
    public long skip(long n) throws IOException {
        return wrappedInputStream.skip(n);
    }

}
