import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuFrame extends JFrame {

	JTextArea sourceArea;
	JTextArea resultsArea;
	Container contentPane;
	JCheckBox autoCheckCheckbox;

	public SudokuFrame() {
		super("Sudoku Solver");

		// Set the layout manager for the content pane
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(4, 4));

		// Create the JTextAreas for the source puzzle and results
		sourceArea = new JTextArea(15, 20);
		sourceArea.setBorder(new TitledBorder("Source Puzzle"));
		contentPane.add(sourceArea, BorderLayout.CENTER);

		resultsArea = new JTextArea(15, 20);
		resultsArea.setBorder(new TitledBorder("Results"));
		contentPane.add(resultsArea, BorderLayout.EAST);

		// Create the horizontal box for the controls
		Box controlsBox = Box.createHorizontalBox();
		contentPane.add(controlsBox, BorderLayout.SOUTH);

		// Create the "Check" button
		JButton checkButton = new JButton("Check");
		controlsBox.add(checkButton);

		// Create the "Auto check" checkbox
		autoCheckCheckbox = new JCheckBox("Auto check");
		controlsBox.add(Box.createHorizontalStrut(10));
		controlsBox.add(autoCheckCheckbox);

		// Add an action listener to the "Check" button to grab the source puzzle text
		checkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String sourceText = sourceArea.getText();
				solvePuzzle(resultsArea, sourceText);
			}
		});

		Document sourceDoc = sourceArea.getDocument();

		sourceDoc.addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				autoCheck();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				autoCheck();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				autoCheck();
			}
		});

		// Pack the components and set other JFrame properties
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationByPlatform(true);
		setVisible(true);
	}

	private void autoCheck() {
		if (autoCheckCheckbox.isSelected()) {
			// If it is, call solvePuzzle to update the results area
			String sourceText = sourceArea.getText();
			solvePuzzle(resultsArea, sourceText);
		}
	}

	private void solvePuzzle(JTextArea resultsArea, String sourceText) {
		try {
			Sudoku s = new Sudoku(sourceText);
			int count = s.solve();
			if (count != 0) {
				String output = s.getSolutionText();
				output += "\n" + "solutions: " + String.valueOf(count);
				output += "\n" + "elapsed: " + String.valueOf(s.getElapsed());
				resultsArea.setText(output);
			}
		} catch(Exception e) {
			System.out.println("Parsing problem");
		}
	}


	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }

		SudokuFrame frame = new SudokuFrame();
	}

}
