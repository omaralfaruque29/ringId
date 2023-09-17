/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.utility;

/**
 *
 * @author user
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MoveFileExample {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MoveFileExample.class);

    public static void copyFile(File src, File dest) {
        InputStream inStream = null;
        OutputStream outStream = null;

        try {
            inStream = new FileInputStream(src);
            outStream = new FileOutputStream(dest);

            byte[] buffer = new byte[5120];

            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            inStream.close();
            outStream.close();
        } catch (IOException e) {
            //e.printStackTrace();
            log.error("Error in MoveFileExample class ==>" + e.getMessage());
        }
    }
}
