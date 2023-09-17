/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.recorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

/**
 *
 * @author Shahadat
 */
public class RecordingTimer implements ActionListener {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RecordingTimer.class);
    private static final int ONE_SECOND = 1000;
    private int count = 0;
    private boolean isTimerActive = false;
    private Timer tmr = new Timer(ONE_SECOND, this);
    private JLabel timerLabel;
    private JProgressBar timerProgressBar;
    private TimerListener listener;
    private int limit = 0;

    public RecordingTimer(JLabel timerLabel, TimerListener listener) {
        this.timerLabel = timerLabel;
        this.listener = listener;
        count = 0;
        setTimerText(TimeFormat(count));
    }

    public RecordingTimer(JLabel timerLabel, JProgressBar timerProgressBar, TimerListener listener) {
        this.timerLabel = timerLabel;
        this.timerProgressBar = timerProgressBar;
        this.listener = listener;
        count = 0;
        setProgressValue(ProgressParcent(count));
        setTimerText(TimeFormat(count));
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int seconds) {
        this.limit = seconds;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (isTimerActive) {
                count++;
                setTimerText(TimeFormat(count));
                if (timerProgressBar != null) {
                    setProgressValue(ProgressParcent(count));
                }
                if (limit > 0 && count >= limit) {
                    listener.timerLimitExit();
                }
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
            log.error("Error in here ==>" + ex.getMessage());
        }
    }

    public void start() {
        count = 0;
        isTimerActive = true;
        tmr.start();
    }

    public void resume() {
        isTimerActive = true;
        tmr.restart();
    }

    public void stop() {
        tmr.stop();
    }

    public void pause() {
        isTimerActive = false;
    }

    public void reset() {
        count = 0;
        isTimerActive = true;
        tmr.restart();

    }

    public int getCount() {
        return count;
    }

    public boolean isRunning() {
        return isTimerActive;
    }

    private void setTimerText(String sTime) {
        timerLabel.setText(sTime);
    }

    private void setProgressValue(int value) {
        timerProgressBar.setValue(value);
    }

    private int ProgressParcent(int count) {
        return limit > 0 ? (count * 100) / limit : 0;
    }

    private String TimeFormat(int count) {
        int hours = count / 3600;
        int minutes = (count - hours * 3600) / 60;
        int seconds = count - minutes * 60;
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }

}
