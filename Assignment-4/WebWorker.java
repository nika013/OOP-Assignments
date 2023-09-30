import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import javax.swing.*;

public class WebWorker extends Thread {
    private String urlString;
    private Semaphore limit;
    private WebFrame frame;
    private int row;
    private CountDownLatch latch;
    public WebWorker(WebFrame frame, Semaphore limit, String urlString, CountDownLatch latch, int row) {
        this.limit = limit;
        this.urlString = urlString;
        this.frame = frame;
        this.row = row;
        this.latch = latch;
    }

    // This is the core web/download i/o code...
    public void download() {
        InputStream input = null;
        StringBuilder contents = null;
        long startTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        int bytes = 0;
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            // Set connect() to throw an IOException
            // if connection does not succeed in this many msecs.
            connection.setConnectTimeout(5000);
            connection.connect();
            input = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            char[] array = new char[1000];
            int len;
            contents = new StringBuilder(1000);
            while ((len = reader.read(array, 0, array.length)) > 0) {
                contents.append(array, 0, len);
                bytes += len;
                Thread.sleep(100);
            }
            String status = formatter.format(new Date());
            status += "     " + Long.toString((System.currentTimeMillis() - startTime)) + " ms     ";
            status += bytes + " bytes";
            frame.getModel().setValueAt(status, row, 1);
        }
        catch (MalformedURLException ignored) {
            frame.getModel().setValueAt("err", row, 1);
        } catch (InterruptedException exception) {
            // YOUR CODE HERE
            frame.getModel().setValueAt("interrupted", row, 1);
        } catch (IOException ignored) {
            frame.getModel().setValueAt("err", row, 1);
        }
        finally {
            try {
                if (input != null) input.close();
            } catch (IOException ignored) {
            }
        }

    }

    @Override
    public void run() {
        download();
        limit.release();
        latch.countDown();
        frame.decreaseRunning();
        frame.increaseCompleted();
    }
}