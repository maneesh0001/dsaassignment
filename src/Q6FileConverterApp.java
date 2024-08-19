//6

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Q6FileConverterApp extends JFrame {
    private JProgressBar overallProgressBar;
    private JTextArea statusArea;
    private JButton startButton, cancelButton;
    private JFileChooser fileChooser;
    private File[] selectedFiles;
    private SwingWorker<Void, String> worker;

    public Q6FileConverterApp() {
        setTitle("File Converter");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        overallProgressBar = new JProgressBar(0, 100);
        statusArea = new JTextArea();
        statusArea.setEditable(false);
        startButton = new JButton("Start Conversion");
        cancelButton = new JButton("Cancel");

        JPanel panel = new JPanel();
        panel.add(startButton);
        panel.add(cancelButton);

        add(overallProgressBar, BorderLayout.NORTH);
        add(new JScrollPane(statusArea), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        startButton.addActionListener(new StartButtonListener());
        cancelButton.addActionListener(e -> cancelConversion());

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileNameExtensionFilter("All Files", "*.*"));
    }

    private class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int returnValue = fileChooser.showOpenDialog(Q6FileConverterApp.this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                selectedFiles = fileChooser.getSelectedFiles();
                startConversion();
            }
        }
    }

    private void startConversion() {
        overallProgressBar.setValue(0);
        statusArea.setText("");
        worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() {
                int totalFiles = selectedFiles.length;
                for (int i = 0; i < totalFiles; i++) {
                    File file = selectedFiles[i];
                    publish("Processing: " + file.getName());
                    // Simulate file conversion
                    try {
                        Thread.sleep(1000); // Simulate time-consuming task
                    } catch (InterruptedException ex) {
                        if (isCancelled()) {
                            publish("Conversion cancelled.");
                            return null;
                        }
                    }
                    int progress = (i + 1) * 100 / totalFiles;
                    setProgress(progress);
                }
                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String message : chunks) {
                    statusArea.append(message + "\n");
                }
            }

            @Override
            protected void done() {
                try {
                    if (!isCancelled()) {
                        get(); // Needed to catch potential exceptions
                        JOptionPane.showMessageDialog(Q6FileConverterApp.this, "All conversions completed!");
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    JOptionPane.showMessageDialog(Q6FileConverterApp.this, "Error during conversion: " + ex.getMessage());
                }
            }
        };

        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                overallProgressBar.setValue((Integer) evt.getNewValue());
            }
        });

        worker.execute();
    }

    private void cancelConversion() {
        if (worker != null && !worker.isDone()) {
            worker.cancel(true);
            JOptionPane.showMessageDialog(this, "Conversion cancelled.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Q6FileConverterApp app = new Q6FileConverterApp();
            app.setVisible(true);
        });
    }
}
