/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.utility.call;

import com.desktopCall.net.CallStates;

import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.ipvision.view.call.MainClass;
import com.ipvision.service.call.CallSignalHandler;

/**
 *
 * @author Faiz
 */
public class CallDurationHelper2 {

    public boolean stopDurationCounting = false;
    public String CALL_ON_HOLD = "Call held";

    public void setStopDurationCounting(boolean stopDurationCounting) {
        this.stopDurationCounting = stopDurationCounting;
    }

    public void reset() {
        sec = 0;
        min = 0;
        hour = 0;
    }
    public int sec = 0;
    public int min = 0;
    public int hour = 0;
    java.text.DecimalFormat nft = new DecimalFormat("#00.###");

    protected String showTime() {
        sec++;
        if (sec == 60) {
            sec = 0;
            min++;
            if (min == 60) {
                hour++;
            }
        }
        return (nft.format(hour) + ":" + nft.format(min) + ":" + nft.format(sec));
    }

    protected void incrementTime() {
        sec++;
        if (sec == 60) {
            sec = 0;
            min++;
            if (min == 60) {
                hour++;
            }
        }

    }

    public void callDurationChecker() {
        final ScheduledExecutorService executor2 = Executors.newSingleThreadScheduledExecutor();
        Runnable periodicTask2 = new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long diff = currentTime - CallSignalHandler.audioTime;
                if (diff > 30000 && CallStates.getStatus() != CallStates.HOLD_CALL) {
                    executor2.shutdown();
                    TopLabelWrapper.cancelButtonAction(false);

                }
                if (MainClass.getMainClass() != null) {
                    if (sec <= 1) {
                        //  MainClass.getMainClass().durationLabel.setIcon(null);
                    }
                    if (CallStates.getStatus() == CallStates.HOLD_CALL) {
                        if (!MainClass.getMainClass().durationLabel.getText().equals(CALL_ON_HOLD)) {
                            MainClass.getMainClass().durationLabel.setText(CALL_ON_HOLD);
                        }
                        incrementTime();
                    } else {
                        MainClass.getMainClass().durationLabel.setText(doPeriodicWork());
                    }
                } else {
                    try {
                        executor2.shutdown();
                        reset();
                    } catch (Exception e) {
                    }
                }
                if (stopDurationCounting) {
                    try {
                        executor2.shutdown();
                        reset();
                    } catch (Exception e) {
                    }
                }
            }

            private String doPeriodicWork() {
                return showTime();
            }
        };

        executor2.scheduleAtFixedRate(periodicTask2,
                2, 1, TimeUnit.SECONDS);

    }
}
