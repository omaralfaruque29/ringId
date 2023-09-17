/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.audio;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.CompoundControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;

/**
 *
 * @author Wasif Islam
 */
public class AudioController {

    public static Boolean getMasterOutputMute() {
        Line line = getMasterOutputLine();
        if (line == null) {
            return null;
        }
        boolean opened = open(line);
        try {
            BooleanControl control = getMuteControl(line);
            if (control == null) {
                return null;
            }
            return control.getValue();
        } finally {
            if (opened) {
                line.close();
            }
        }
    }

    public static void setMasterOutputMute(boolean value) {
        Line line = getMasterOutputLine();
        if (line == null) {
            throw new RuntimeException("Master output port not found");
        }
        boolean opened = open(line);
        try {
            BooleanControl control = getMuteControl(line);
            if (control == null) {
                throw new RuntimeException("Mute control not found in master port: " + toString(line));
            }
            control.setValue(value);
        } finally {
            if (opened) {
                line.close();
            }
        }
    }

    public static BooleanControl getMuteControl(Line line) {
        if (!line.isOpen()) {
            throw new RuntimeException("Line is closed: " + toString(line));
        }
        return (BooleanControl) findControl(BooleanControl.Type.MUTE, line.getControls());
    }

    private static Control findControl(Control.Type type, Control... controls) {
        if (controls == null || controls.length == 0) {
            return null;
        }
        for (Control control : controls) {
            if (control.getType().equals(type)) {
                return control;
            }
            if (control instanceof CompoundControl) {
                CompoundControl compoundControl = (CompoundControl) control;
                Control member = findControl(type, compoundControl.getMemberControls());
                if (member != null) {
                    return member;
                }
            }
        }
        return null;
    }

    public static String toString(Line line) {
        if (line == null) {
            return null;
        }
        Line.Info info = line.getLineInfo();
        return info.toString();// + " (" + line.getClass().getSimpleName() + ")";
    }

    public static Line getMasterOutputLine() {
        for (Mixer mixer : getMixers()) {
            for (Line line : getAvailableOutputLines(mixer)) {
                if (line.getLineInfo().toString().contains("Master")) {
                    return line;
                }
            }
        }
        return null;
    }

    public static List<Mixer> getMixers() {
        Info[] infos = AudioSystem.getMixerInfo();
        List<Mixer> mixers = new ArrayList<Mixer>(infos.length);
        for (Info info : infos) {
            Mixer mixer = AudioSystem.getMixer(info);
            mixers.add(mixer);
        }
        return mixers;
    }

    public static List<Line> getAvailableOutputLines(Mixer mixer) {
        return getAvailableLines(mixer, mixer.getTargetLineInfo());
    }

    private static List<Line> getAvailableLines(Mixer mixer, Line.Info[] lineInfos) {
        List<Line> lines = new ArrayList<Line>(lineInfos.length);
        for (Line.Info lineInfo : lineInfos) {
            Line line;
            line = getLineIfAvailable(mixer, lineInfo);
            if (line != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    public static Line getLineIfAvailable(Mixer mixer, Line.Info lineInfo) {
        try {
            return mixer.getLine(lineInfo);
        } catch (LineUnavailableException ex) {
            return null;
        }
    }

    public static boolean open(Line line) {
        if (line.isOpen()) {
            return false;
        }
        try {
            line.open();
        } catch (LineUnavailableException ex) {
            return false;
        }
        return true;
    }

    private static boolean defaultVolumeSet = false;

    public static void setDefaultVolume() {
        if (!defaultVolumeSet) {
            javax.sound.sampled.Mixer.Info[] mixers = AudioSystem.getMixerInfo();
            for (int i = 0; i < mixers.length; i++) {
                Mixer.Info mixerInfo = mixers[i];
                Mixer mixer = AudioSystem.getMixer(mixerInfo);
                Line.Info[] lineinfos = mixer.getTargetLineInfo();
                for (Line.Info lineinfo : lineinfos) {
                    try {
                        Line line = mixer.getLine(lineinfo);
                        line.open();
                        if (line.isControlSupported(FloatControl.Type.VOLUME)) {
                            FloatControl control = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
                            // if you want to set the value for the volume 0.5 will be 50%
                            // 0.0 being 0%
                            // 1.0 being 100%
                            control.setValue((float) 1);

                        }
                        line.close();
                    } catch (LineUnavailableException e) {
                        //e.printStackTrace();
                    }
                }
            }
            defaultVolumeSet = true;
        }
    }

    public static int getVolume() {
        int vol = 0;
        javax.sound.sampled.Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (int i = 0; i < mixers.length; i++) {
            Mixer.Info mixerInfo = mixers[i];
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info[] lineinfos = mixer.getTargetLineInfo();
            for (Line.Info lineinfo : lineinfos) {
                try {
                    Line line = mixer.getLine(lineinfo);
                    line.open();
                    if (line.isControlSupported(FloatControl.Type.VOLUME)) {
                        FloatControl control = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
                        // if you want to set the value for the volume 0.5 will be 50%
                        // 0.0 being 0%
                        // 1.0 being 100%
                        vol = (int) (control.getValue() * 100);
                    }
                    line.close();
                } catch (LineUnavailableException e) {
                    //    e.printStackTrace();
                }
            }
        }
        return vol;
    }

    public static void changeVolume(int val) {
        javax.sound.sampled.Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (int i = 0; i < mixers.length; i++) {
            Mixer.Info mixerInfo = mixers[i];
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info[] lineinfos = mixer.getTargetLineInfo();
            for (Line.Info lineinfo : lineinfos) {
                try {
                    Line line = mixer.getLine(lineinfo);
                    line.open();
                    if (line.isControlSupported(FloatControl.Type.VOLUME)) {

                        FloatControl control = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
                        // if you want to set the value for the volume 0.5 will be 50%
                        // 0.0 being 0%
                        // 1.0 being 100%
                        control.setValue((float) val / 100);

                    }
                    line.close();

                } catch (LineUnavailableException e) {
                    //   e.printStackTrace();
                }
            }
        }
    }

}
