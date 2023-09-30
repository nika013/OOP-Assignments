
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class WebFrame extends JFrame {
    private final static int WIDTH = 800;
    private final static int HEIGHT = 700;
    private final ArrayList<String> urls;
    private JButton singleBtn;
    private JButton concurrentBtn;
    private JPanel panel;
    private DefaultTableModel model;
    private JTextField field;
    private JLabel runningLabel;
    private JLabel completedLabel;
    private JLabel elapsedLabel;
    private JProgressBar progressBar;
    private JButton stopBtn;
    private boolean isRunning = false;
    private int runningThreads;
    private int completedThreads;

    private long startTime;
    private Launcher launcher;

    public WebFrame(ArrayList<String> urls) {
        this.urls = urls;
        runningThreads = 1;
        completedThreads = 0;
        setUpPanel();
        addButtons();
        addField();
        addLabels();
        addProgressBar();
        addEventListeners();
    }

    public DefaultTableModel getModel() {
        return model;
    }

    private void setUpPanel() {
        model = new DefaultTableModel(new String[]{"url", "status"}, 0);
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setPreferredSize(new Dimension(600,300));
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(scrollpane);
        add(panel);

        for(String url : urls) {
            model.addRow(new Object[]{url});
        }
    }

    private void addButtons() {
        singleBtn = new JButton("Single Thread Fetch");
        concurrentBtn = new JButton("Concurrent Fetch");
        panel.add(singleBtn);
        panel.add(concurrentBtn);
        stopBtn = new JButton("Stop");
        stopBtn.setEnabled(false);
        panel.add(stopBtn);
    }
    private void addField() {
        field = new JTextField();
        field.setText(Integer.toString(4)); // Chose 4 as default number of threads
        panel.add(field);
        field.setMaximumSize(new Dimension(50, 40));
    }
    private void addLabels() {
        runningLabel = new JLabel("Running : " + 0);
        completedLabel = new JLabel("Completed : " + 0);
        elapsedLabel = new JLabel("Elapsed : " + 0);
        panel.add(runningLabel);
        panel.add(completedLabel);
        panel.add(elapsedLabel);
    }
    private void addProgressBar() {
        progressBar = new JProgressBar(0, model.getRowCount());
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        panel.add(progressBar);
    }
    private void addEventListeners() {
        singleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // disable fetch buttons
                if (!isRunning) {
                    toDeactive();
                    startTime = System.currentTimeMillis();
                    launcher = new Launcher(WebFrame.this, 1);
                    launcher.start();
                }
            }
        });
        concurrentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // disable fetch buttons
                // reset model
                if (!isRunning) {
                    toDeactive();
                    int limitWorkers = Integer.valueOf(field.getText());
                    field.setText("");
                    startTime = System.currentTimeMillis();
                    launcher = new Launcher(WebFrame.this, limitWorkers);
                    launcher.start();
                }
            }
        });
        stopBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // interrupt threads
                if (isRunning) {
                    toActive();
                    // output elapsed time
                    if (launcher != null) launcher.interruptWorkers();
                    setElapsedTime();
                }
            }
        });
    }

    private void setElapsedTime() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        updateField(elapsedLabel, "elapsed: ", elapsedTime);
    }

    private void toDeactive() {
        isRunning = true;
        singleBtn.setEnabled(false);
        concurrentBtn.setEnabled(false);
        stopBtn.setEnabled(true);
        runningThreads = 1;
        completedThreads = 0;
    }

    private void toActive() {
        isRunning = false;
        singleBtn.setEnabled(true);
        concurrentBtn.setEnabled(true);
        stopBtn.setEnabled(false);
    }

    private void setDefaults() {
        SwingUtilities.invokeLater(() -> {
            elapsedLabel.setText("Elapsed: 0");
        });
    }

    public static void main(String[] args) {
        String filePath = args[0];
        ArrayList<String> urls = readFile(filePath);

        WebFrame webFrame = new WebFrame(urls);
        webFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add the label to the frame
        webFrame.setSize(WIDTH, HEIGHT);
        webFrame.setVisible(true);

    }

    private static ArrayList<String> readFile(String filePath) {
        ArrayList<String> content = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public synchronized void increaseRunning() {
        runningThreads++;
        updateField(runningLabel, "Running", runningThreads);
    }

    public synchronized void decreaseRunning() {
        runningThreads--;
        updateField(runningLabel, "Running", runningThreads);
    }

    public synchronized void increaseCompleted() {
        completedThreads++;
        updateField(completedLabel, "Completed", completedThreads);
    }

    private void updateField(JLabel label, String fieldName, long number) {
        SwingUtilities.invokeLater(() -> {
            label.setText(fieldName + ": " + number);
        });
    }

    class Launcher extends Thread {
        private Semaphore limit;
        private WebWorker[] webWorkers;
        private WebFrame frame;
        private CountDownLatch latch;
        public Launcher(WebFrame frame, int limitWorkers) {
            limit = new Semaphore(limitWorkers);
            webWorkers = new WebWorker[urls.size()];
            this.frame = frame;
            latch = new CountDownLatch(urls.size());
        }
        @Override
        public void run() {
            for (int i = 0; i < urls.size(); i++) {
                String url = urls.get(i);
                webWorkers[i] = new WebWorker(frame, limit, url, latch, i);
                try {
                    limit.acquire();
                    webWorkers[i].start();
                    increaseRunning();
                    if (isInterrupted()) throw new InterruptedException();
                } catch (InterruptedException e) {
                    printInterrupt();
                    return;
                }
            }
            end();
        }

        private void printInterrupt() {
            for (int i = completedThreads; i < urls.size(); i++) {
                frame.getModel().setValueAt("interrupted", i, 1);
            }
        }

        private void end() {
            try {
                latch.await();
                decreaseRunning();
                toActive();
                setElapsedTime();
            } catch (InterruptedException e) {
            }
        }

        public void interruptWorkers() {
            for (int i = 0; i < webWorkers.length; i++) {
                if (webWorkers[i] != null) {
                    webWorkers[i].interrupt();
                }
            }
            interrupt();
        }
    }
}
