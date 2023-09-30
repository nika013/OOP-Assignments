// JCount.java

/*
 Basic GUI/Threading exercise.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JCount extends JPanel {
	private JTextField field;
	private JButton startBtn;
	private JButton stopBtn;
	private JLabel countLabel;
	private WorkerThread worker;
	public JCount() {
		// Set the JCount to use Box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		field = new JTextField(10);
		startBtn = new JButton("Start");
		stopBtn = new JButton("Stop");
		countLabel = new JLabel("0");
		add(field);
		add(countLabel);
		add(startBtn);
		add(stopBtn);
		add(Box.createRigidArea(new Dimension(0, 40))); // Add rigid area for separation
		addListeners();
		worker = new WorkerThread();
	}

	private void addListeners() {
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = field.getText();
				int upTo = text.isEmpty() ? 1000000000 : Integer.valueOf(text);
				if (worker.isAlive()) {
					worker.interrupt();
				}
				worker = new WorkerThread(upTo);
				worker.start();
			}
		});

		stopBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!worker.isInterrupted()) {
					worker.interrupt();
				}
			}
		});
	}

	private class WorkerThread extends Thread {
		private int upTo;
		private int counter;

		public WorkerThread() {
			counter = 0;
		}

		public WorkerThread(int upTo) {
			this.upTo = upTo;
			counter = 0;
		}

		@Override
		public void run() {
			while (counter < upTo && !isInterrupted()) {
				counter++;
				checkPrint();
			}
		}

		private void checkPrint() {
			if (counter % 10000 == 0) {
				SwingUtilities.invokeLater(() -> {
					countLabel.setText(String.valueOf(counter));
				});
				try {
					Thread.sleep(100); // Sleep for 100 milliseconds
				} catch (InterruptedException e) {
					interrupt();
				}
			}
		}
	}


	private static void createAndShowGUI() {
		// create your GUI HERE
		// Creates a frame with 4 JCounts in it.
		// (provided)
		JFrame frame = new JFrame("The Count");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static void main(String[] args)  {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}

