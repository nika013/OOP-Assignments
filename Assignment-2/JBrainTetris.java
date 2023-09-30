//package JTetris;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;

public class JBrainTetris extends JTetris {
    private DefaultBrain myBrain;
    private int count;
    private Brain.Move move;
    private Brain.Move bestMove;

    private JCheckBox brainMode;
    private JSlider adversary;
    private JCheckBox animate;
    private JLabel status;


    JBrainTetris(int pixel) {
        super(pixel);
        count = 0;
        myBrain = new DefaultBrain();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JBrainTetris tetris = new JBrainTetris(16);
        JFrame frame = JTetris.createFrame(tetris);
        frame.setVisible(true);
    }

    @Override
    public JComponent createControlPanel() {
        JPanel panel = (JPanel) super.createControlPanel();
        panel.add(new JLabel("Brain:"));
        animate = new JCheckBox("Animate fall");
        brainMode = new JCheckBox("Brain active");
        animate.setSelected(true); // by default it should be true

        JPanel little = new JPanel();
        status = new JLabel("ok");

        little.add(new JLabel("Adversary:"));
        adversary = new JSlider(0, 100, 0);
        adversary.setPreferredSize(new Dimension(100, 15));

        little.add(adversary);
        panel.add(animate);
        panel.add(brainMode);
        panel.add(little);
        panel.add(status);
        panel.add(Box.createVerticalStrut(15));

        return panel;
    }

    @Override
    public void tick(int verb) {
        if(brainMode.isSelected() && verb == DOWN) {
            checkCount();
            makeMove(verb);
        } else {
            super.tick(verb);
        }
    }

    private void makeMove(int verb) {
        if(bestMove != null) {
            if (!currentPiece.equals(bestMove.piece)) {
                currentPiece = currentPiece.fastRotation();
            }
            if (currentX > bestMove.x) {
                super.tick(LEFT);
            } else if (bestMove.x > currentX) {
                super.tick(RIGHT);
            } else if (!animate.isSelected() && move.x == currentX && currentY > bestMove.y)
                verb = DROP;
        }
        super.tick(verb);
    }

    private void checkCount() {
        if(count != super.count) {
            count = super.count;
            board.undo();
            bestMove = myBrain.bestMove(board, currentPiece, HEIGHT, bestMove);
        }
    }

    @Override
    public Piece pickNextPiece() {
        int adversVal = adversary.getValue();
        int random = super.random.nextInt(100);

        if (random < adversVal) {
            status.setText("*ok*");
            double badScore = 0;
            Piece badPiece = super.pickNextPiece();

            for (Piece piece: pieces) {
                bestMove = myBrain.bestMove(board, piece, board.getHeight()-TOP_SPACE, bestMove);
                if (bestMove == null) {
                    return super.pickNextPiece();
                }
                if (bestMove.score > badScore) {
                    badPiece = piece;
                    badScore = bestMove.score;
                }
            }
            return badPiece;
        } else {
            status.setText("ok");
            return super.pickNextPiece();
        }
    }
}