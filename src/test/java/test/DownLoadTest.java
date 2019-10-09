package test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.RandomUtils;

public class DownLoadTest {
    static ExecutorService es = Executors.newFixedThreadPool(10);

    static CountDownLatch cdl = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String url = scanner.nextLine();
        String saveDir = "/Users/shotacon/Downloads/testDownload";
        String fileName = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));
        downloadByApacheCommonIO(url, saveDir, fileName);
    }

    public static void downloadByApacheCommonIO(String url, String saveDir, String fileName)
            throws InterruptedException, IOException {
        URL u = new URL(url);
        URLConnection openConnection = u.openConnection();
        try (InputStream ins = openConnection.getInputStream()) {
            Path target = Paths.get(saveDir, fileName);
            Files.createDirectories(target.getParent());

            // kb
            final long fileSize = openConnection.getContentLengthLong() / 1024;

            System.out.println("begin with " + fileName + ", size: " + fileSize + "kb");
            es.submit(() -> {
                try {
                    Files.copy(ins, target, StandardCopyOption.REPLACE_EXISTING);
                    cdl.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Thread.sleep(2000);
            es.submit(() -> {
                long oldSize = 0;
                while (cdl.getCount() > 0) {
                    try {

                        FileChannel open = FileChannel.open(target, StandardOpenOption.READ);
                        long downloadSize = open.size() / 1024;
                        long a = (fileSize - downloadSize) / (downloadSize - oldSize);
                        System.out.println(downloadSize + "KB, left: " + a + "s");
                        oldSize = downloadSize;
                        Thread.sleep(2000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            cdl.await();
            es.shutdown();

            System.out.println("done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
