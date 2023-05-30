package com.example.keuangan.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

public class FileUtils {
    public static void saveResponseBodyToDisk(ResponseBody responseBody, File file) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            byte[] fileReader = new byte[4096];
            long fileSize = responseBody.contentLength();
            long fileSizeDownloaded = 0;

            inputStream = responseBody.byteStream();
            outputStream = new FileOutputStream(file);

            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }

                outputStream.write(fileReader, 0, read);
                fileSizeDownloaded += read;
            }

            outputStream.flush();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}