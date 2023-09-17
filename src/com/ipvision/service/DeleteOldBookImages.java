/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service;

import com.ipvision.constants.MyFnFSettings;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Wasif Islam
 */
public class DeleteOldBookImages extends Thread {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteOldBookImages.class);
    private File folder;
    private File[] listOfFiles;
    private Path p;
    private BasicFileAttributes view;
    private long DAY_IN_MS = 1000 * 60 * 60 * 24;
    private long SEVEN_DAYS_AGO_IN_MS;
    private int MAX_IMAGE = 200;
    private int EXTRA_IMAGE = 0;
    private Map<Long, File> fileMap;

    public DeleteOldBookImages() {
        this.folder = new File(MyFnFSettings.TEMP_BOOK_IMAGE_FOLDER);
        this.listOfFiles = folder.listFiles();
        this.SEVEN_DAYS_AGO_IN_MS = getStartOfToday() - (7 * DAY_IN_MS);
        setName(this.getClass().getSimpleName());
    }

    private Long getStartOfToday() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //calendar.getTime();
        return calendar.getTimeInMillis();
    }

    private void findOldExtraImagesAndDelete() {
        fileMap = new HashMap<>();
        long[] times = new long[listOfFiles.length];
        int count = 0;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                p = Paths.get(file.getAbsoluteFile().toURI());
                try {
                    view = Files.getFileAttributeView(p,
                            BasicFileAttributeView.class).readAttributes();
                    fileMap.put(view.creationTime().toMillis(), file);
                    times[count] = view.creationTime().toMillis();
                    count++;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    // e.printStackTrace();
                    log.error("Error in DeletingOldBookImages class ==>" + e.getMessage());
                }
            }
        }

        Arrays.sort(times);

        //for (int k = times.length - 1; k >= MAX_IMAGE; k--) {
        for (int k = 0; k < EXTRA_IMAGE; k++) {
            fileMap.get(times[k]).delete();
        }

        fileMap.clear();

    }

    private void find7DaysOldExtraImagesAndDelete() {
        for (File file : listOfFiles) {

            if (file.isFile()) {
                p = Paths.get(file.getAbsoluteFile().toURI());
                try {
                    view = Files.getFileAttributeView(p,
                            BasicFileAttributeView.class).readAttributes();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    //  e.printStackTrace();
                    log.error("IOException in find7DaysOldExtraImagesAndDelete ==>" + e.getMessage());
                }
                if (view != null && view.creationTime().toMillis() < SEVEN_DAYS_AGO_IN_MS) {
                    file.delete();
                }
            }
        }
    }

    @Override
    public void run() {
        if (listOfFiles != null && listOfFiles.length > 0) {
            if (listOfFiles.length > MAX_IMAGE) {
                EXTRA_IMAGE = listOfFiles.length - MAX_IMAGE;
                findOldExtraImagesAndDelete();
            } else {
                find7DaysOldExtraImagesAndDelete();
            }
        }
    }
}
