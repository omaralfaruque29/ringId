/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 *
 * @author Shahdat Hossain
 */
public class ChatUserTyping extends Thread {

    private JTextArea typingArea;
    private JLabel typingIcon;
    private Map<String, Data> map = new ConcurrentHashMap<String, Data>();
    private boolean running = true;
    private Long MAX_VALUE = (long) 1500;
    private String prevText = "";

    private final String IS_TYPING = " is typing...";
    private final String ARE_TYPING = " are typing...";
    private final String CONCATE = ", ";

    public ChatUserTyping(JTextArea typingArea, JLabel typingIcon) {
        this.typingArea = typingArea;
        this.typingIcon = typingIcon;
        setName(this.getClass().getSimpleName());
    }

    public void addUserId(String userId) {
        userId = CONCATE + userId;
        Data data = map.get(userId);

        if (data == null) {
            data = new Data();
            data.setUserId(userId);
            data.setTime(MAX_VALUE);
            map.put(userId, data);
            prevText += userId;
            setText();
        } else {
            data.setTime(MAX_VALUE);
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                if (map.size() <= 0) {
                    stopThread();
                    continue;
                }

                Thread.sleep(500);
                List<String> deletedList = new ArrayList<String>();

                for (Entry<String, Data> entrySet : map.entrySet()) {
                    String userId = entrySet.getKey();
                    Data data = entrySet.getValue();
                    data.setTime(data.getTime() - 500);

                    if (data.getTime() <= 0) {
                        deletedList.add(userId);
                    }
                }

                if (deletedList.size() > 0) {
                    for (String userId : deletedList) {
                        prevText = prevText.replaceAll(userId, "");
                        map.remove(userId);
                    }
                    setText();
                }

                if (map.size() <= 0) {
                    stopThread();
                }
            }
        } catch (Exception e) {
            stopThread();
        }
    }

    public void startThread() {
        running = true;
        this.start();
    }

    public void stopThread() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void setText() {
        if (prevText.length() > 0) {
            String str = prevText.replaceFirst(CONCATE, "");
            int i = str.lastIndexOf(',');
            if (i != -1) {
                StringBuilder sb = new StringBuilder(str);
                sb.replace(i, i + 1, " &");
                sb.append(ARE_TYPING);
                str = sb.toString();
            } else {
                str = str + IS_TYPING;
            }
            typingArea.setText(str);
            typingIcon.setVisible(true);
        } else {
            typingArea.setText("");
            typingIcon.setVisible(false);
        }
        typingArea.revalidate();
        typingArea.repaint();
    }

    class Data {

        private String userId;
        private Long time;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }
    }

}
