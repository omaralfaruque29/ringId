/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.audio;

import com.desktopCall.playaudio.AudioClipPlayer;
import com.ipvision.constants.MyFnFSettings;

/**
 *
 * @author Faiz
 */
public class PlayAudioHelper {

    public static String media_path = MyFnFSettings.RESOURCE_FOLDER;
    /**
     * On wav file
     */
    static final String CLIP_ON = "on.wav";
    /**
     * Off wav file
     */
    static final String CLIP_OFF = "off.wav";
    /**
     * Ring wav file
     */
    static final String CLIP_RING = "ringigProgress.wav";
    /**
     * Progress wav file
     */
    static final String CLIP_PROGRESS = "ringigProgress.wav";
    /*landing mp3 file*/
    static final String LANDING_AUDIO = "landing.wav";
    /*landing mp3 file*/
    static final String CHAT_AUDIO = "chat.wav";
    /**/
    static final String HOLD_TUNE = "holdTune.wav";
    // ***************************** attributes ****************************
    /**
     * On sound
     */
    public AudioClipPlayer clip_on;
    /**
     * Off sound
     */
    public AudioClipPlayer clip_off;
    /**
     * Ring sound
     */
    public AudioClipPlayer clip_ring;
    /**
     * Progress sound
     */
    public AudioClipPlayer clip_progress;
    /*landing*/
    public AudioClipPlayer landing_tune;
    /*chat*/
    public AudioClipPlayer chat_tune;
    public AudioClipPlayer hold_tune;
    /**
     * On volume gain
     */
    float clip_on_volume_gain = (float) 0.0; // not changed
    /**
     * Off volume gain
     */
    float clip_off_volume_gain = (float) 0.0; // not changed
    /**
     * Ring volume gain
     */
    float clip_ring_volume_gain = (float) 0.0; // not changed
    /**
     * landing volume gain
     */
    float landing_volume_gain = (float) 0.0; // not changed
    /**
     * chat volume gain
     */
    float chat_volume_gain = (float) 0.0; // not changed
    //
    float hold_tune_gain = (float) 0.0; // not changed

    public void initLanding() {
        try {
            landing_tune = new AudioClipPlayer(media_path + LANDING_AUDIO, null);
            landing_tune.setVolumeGain(landing_volume_gain);
            landing_tune.setLoop();
        } catch (Exception e) {

        }
    }

    public void initChatSounds() {
        try {
            chat_tune = new AudioClipPlayer(media_path + CHAT_AUDIO, null);
            chat_tune.setVolumeGain(chat_volume_gain);
        } catch (Exception e) {

        }
    }

    public void initHoldSound() {
        try {
            hold_tune = new AudioClipPlayer(media_path + HOLD_TUNE, null);
            hold_tune.setVolumeGain(hold_tune_gain);
            hold_tune.setLoop();
        } catch (Exception e) {

        }
    }

    public void init() {
        try {

            clip_on = new AudioClipPlayer(media_path + CLIP_ON, null);
            clip_off = new AudioClipPlayer(media_path + CLIP_OFF, null);
            clip_ring = new AudioClipPlayer(media_path + CLIP_RING, null);
            clip_progress = new AudioClipPlayer(media_path + CLIP_PROGRESS, null);
            clip_ring.setLoop();
            clip_progress.setLoop();

            clip_on.setVolumeGain(clip_on_volume_gain);
            clip_off.setVolumeGain(clip_off_volume_gain);
            clip_ring.setVolumeGain(clip_ring_volume_gain);
            clip_progress.setVolumeGain(clip_progress_volume_gain);

        } catch (Exception e) {

        }
    }
    float clip_progress_volume_gain = (float) 0.0; // not changed
    // for JME
    /**
     * On volume (in the range [0-100])
     */
    int clip_on_volume = 5;
    /**
     * Off volume (in the range [0-100])
     */
    int clip_off_volume = 5;
    /**
     * Ring volume (in the range [0-100])
     */
    int clip_ring_volume = 30;
    /**
     * Progress volume (in the range [0-100])
     */
    int clip_progress_volume = 30;

    public PlayAudioHelper() {
    }

}
