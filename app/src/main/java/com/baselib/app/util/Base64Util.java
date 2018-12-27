package com.baselib.app.util;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by snowd on 15/4/7.
 */
public class Base64Util {

    public static String fileToBase64(String path) {
        File file = new File(path);
        return fileToBase64(file);
    }

    public static String fileToBase64(File file) {
        if (file != null && file.exists() && file.isFile()
                && file.length() < Integer.MAX_VALUE) {
            try {
                InputStream is = new FileInputStream(file);
                byte[] b = new byte[(int) file.length()];
                int totalread = 0;
                int read = 0;
                do {
                    read = is.read(b, totalread, (int) (file.length() - totalread));
                    totalread += read;
                } while (totalread != file.length() || read > 0);

                String out = new String(Base64.encode(b, Base64.DEFAULT));
                return out;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String bitmapToBase64(Bitmap b) {
        if (b != null && !b.isRecycled()) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                boolean success = b.compress(Bitmap.CompressFormat.PNG, 75, outputStream);
                if (success) {
                    return new String(Base64.encode(
                            outputStream.toByteArray(), Base64.DEFAULT));
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return null;
    }
}
