package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TestDownload {

    private static final int MAX_BUFFER_SIZE = 1000000;
    private final static String url = "https://cv.phncdn.com/videos/201706/13/120327391/720P_1500K_120327391.mp4?Q7HHslEA7PkNgDLLsCV0ZfZIFciTeBqrUcCy8LGKeCcGZvR8OI8cRM63Jx41eL5OZDClcj3Psydp6lqpA1-5stVQvmX6-0s9d-A5CgonG0bcTCuu8HB-ak9QOdZOc6lIRPYfgdW28ePNPgp5Cp-MdahCtFwEZzL3vh_jbnM89V3puO5sGsUwrcYaiyvJXFC7JiHMoUcbJg";

    public static void main(String[] args) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        RandomAccessFile randomAccessFile = null;
        try {
            // 1.获取连接对象
            URL url = new URL(TestDownload.url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Range", "bytes=0-");
            connection.connect();
            if (connection.getResponseCode() / 100 != 2) {
                System.out.println("连接失败...");
                return;
            }
            // 2.获取连接对象的流
            inputStream = connection.getInputStream();
            // 已下载的大小
            int downloaded = 0;
            // 总文件的大小
            int fileSize = connection.getContentLength();
            String fileName = url.getFile();
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
            // 3.把资源写入文件
            randomAccessFile = new RandomAccessFile(fileName, "rw");
            while (downloaded < fileSize) {
                // 3.1设置缓存流的大小
                byte[] buffer = null;
                if (fileSize - downloaded >= MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[fileSize - downloaded];
                }
                // 3.2把每一次缓存的数据写入文件
                int read = -1;
                int currentDownload = 0;
                long startTime = System.currentTimeMillis();
                while (currentDownload < buffer.length) {
                    read = inputStream.read();
                    buffer[currentDownload++] = (byte) read;
                }
                long endTime = System.currentTimeMillis();
                double speed = 0.0;
                if (endTime - startTime > 0) {
                    speed = currentDownload / 1024.0 / ((double) (endTime - startTime) / 1000);
                }
                randomAccessFile.write(buffer);
                downloaded += currentDownload;
                randomAccessFile.seek(downloaded);
                System.out.printf("下载了进度:%.2f%%,下载速度：%.1fkb/s(%.1fM/s)%n", downloaded * 1.0 / fileSize * 10000 / 100,
                        speed, speed / 1000);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.disconnect();
                inputStream.close();
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
