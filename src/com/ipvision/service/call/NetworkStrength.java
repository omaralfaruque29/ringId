/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.call;

import com.desktopCall.settings.ConfigFile;
import static com.desktopCall.settings.ConfigFile.numberOfRTPreceiving;
import static com.desktopCall.settings.ConfigFile.numberOfRTPsending;
import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JLabel;

/**
 *
 * @author Faiz
 */
public class NetworkStrength extends Thread {

    private JLabel networkLabel;

    public NetworkStrength(JLabel networkLabel) {
        this.networkLabel = networkLabel;
        setName(this.getClass().getName());
    }

    @Override
    public void run() {
        while (!ConfigFile.noUI) {
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
            }
            if (numberOfRTPsending == 0 && numberOfRTPsending == 0) {
                changeText(0d);
            } else if (numberOfRTPreceiving >= numberOfRTPsending) {
                changeText(100d);
            } else {
                try {
                    if (numberOfRTPsending > 1) {
                        double values = (numberOfRTPreceiving * 100) / numberOfRTPsending;
                        BigDecimal bd = new BigDecimal(values).setScale(2, RoundingMode.HALF_EVEN);
                        values = bd.doubleValue();
                        values = Math.round(values * 100) / 100.0d;
                        changeText(values);
                    }

                } catch (Exception e) {
                }

            }
            numberOfRTPsending = 0;
            numberOfRTPreceiving = 0;
        }

    }

    private void changeText(double value) {
        /*
         poor ff4038
         Good 34dc6d
         average fdce5b
         */
        Color fgColor = new Color(0xff4038);
        String text = "Poor";
        if (value > 90.0) {
            fgColor = Color.GREEN;
            text = "Excellent";
        } else if (value >= 80.0 && value < 90.0) {
            fgColor = new Color(0x34dc6d);
            text = "Good";
        } else if (value >= 70.0 && value < 80.0) {
            fgColor = new Color(0xfdce5b);
            text = "Good";
        } else if (value == 0) {
            text = "Reconnecting...";
        }
        networkLabel.setForeground(fgColor);
        networkLabel.setText(text);
    }
}
