import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new UI());
    }
}

class Game {
    private final int actualValue;
    private final int min;
    private final int max;
    private int attempts;
    private int userNumber;
    private int score;

    public Game(int min, int max, int attempts) {
        this.min = min;
        this.max = max;
        this.attempts = attempts;
        this.actualValue = generateRandom();
    }

    public int getActualValue() {
        return actualValue;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public int getScore() {
        return score;
    }

    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }

    public int getUserNumber() {
        return userNumber;
    }

    public int generateRandom() {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public boolean checkCorrect() {
        return actualValue == userNumber;
    }

    public void decrementAttempts() {
        attempts--;
    }

    public void incrementScore() {
        score++;
    }
}

class UI extends JFrame {
    private final int width;
    private final int height;
    private Game game;
    private final JLabel correctLabel;
    private final JLabel attemptsLabel;
    private final JLabel scoreLabel;
    private final JTextField inputField;
    private final JButton enterButton;
    private final JButton refreshButton;

    public UI() {
        super("Number Guessing Game by Arun Kumar");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.width = (screen.width / 2) + 80;
        this.height = (screen.height / 2);

        this.game = new Game(1, 100, 3);
        this.correctLabel = new JLabel("Actual value: " + game.getActualValue());
        this.attemptsLabel = new JLabel("Attempts left: " + game.getAttempts());
        this.scoreLabel = new JLabel("Score: " + game.getScore());
        this.inputField = new JTextField();
        this.enterButton = new JButton("Enter");
        this.refreshButton = new JButton("Refresh");

        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        Font font = new Font("Choco cooky", Font.PLAIN, 20);

        JLabel label = new JLabel("Guess a number between " + game.getMin() + " and " + game.getMax());
        label.setBounds(0, 20, width, 40);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(font);
        add(label);

        inputField.setBounds(120, 80, 320, 40);
        inputField.setFont(font);
        add(inputField);

        enterButton.setBounds(450, 80, 150, 40);
        enterButton.setFont(font);
        enterButton.addActionListener(e -> handleGuess());
        add(enterButton);

        attemptsLabel.setBounds(0, 150, width, 40);
        attemptsLabel.setHorizontalAlignment(JLabel.CENTER);
        attemptsLabel.setFont(font);
        add(attemptsLabel);

        scoreLabel.setBounds(0, 180, width, 40);
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setFont(font);
        add(scoreLabel);

        correctLabel.setBounds(0, 210, width, 40);
        correctLabel.setHorizontalAlignment(JLabel.CENTER);
        correctLabel.setFont(font);
        correctLabel.setVisible(false);
        add(correctLabel);

        refreshButton.setBounds(200, 260, 150, 40);
        refreshButton.setFont(font);
        refreshButton.setEnabled(false);
        refreshButton.addActionListener(e -> refreshGame());
        add(refreshButton);

        JButton quitButton = new JButton("Quit");
        quitButton.setBounds(380, 260, 150, 40);
        quitButton.setFont(font);
        quitButton.addActionListener(e -> System.exit(0));
        add(quitButton);

        setVisible(true);
    }

    private void handleGuess() {
        try {
            int num = Integer.parseInt(inputField.getText());
            if (num < game.getMin() || num > game.getMax()) {
                JOptionPane.showMessageDialog(this, "Please enter a number within the bounds!");
            } else {
                game.setUserNumber(num);
                if (game.checkCorrect()) {
                    JOptionPane.showMessageDialog(this, "YOU WIN!");
                    game.incrementScore();
                    refreshGame();
                } else {
                    game.decrementAttempts();
                    if (game.getAttempts() == 0) {
                        JOptionPane.showMessageDialog(this, "GAME OVER! The correct number was " + game.getActualValue());
                        gameOver();
                    } else {
                        JOptionPane.showMessageDialog(this, "Incorrect! Attempts left: " + game.getAttempts());
                        attemptsLabel.setText("Attempts left: " + game.getAttempts());
                        giveHint();
                    }
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number!");
        }
        inputField.setText("");
    }

    private void gameOver() {
        inputField.setEditable(false);
        enterButton.setEnabled(false);
        correctLabel.setText("Actual value: " + game.getActualValue());
        correctLabel.setVisible(true);
        refreshButton.setEnabled(true);
    }

    private void refreshGame() {
        game = new Game(1, 100, 3);
        inputField.setEditable(true);
        enterButton.setEnabled(true);
        correctLabel.setVisible(false);
        refreshButton.setEnabled(false);
        attemptsLabel.setText("Attempts left: " + game.getAttempts());
        scoreLabel.setText("Score: " + game.getScore());
    }

    private void giveHint() {
        int difference = game.getActualValue() - game.getUserNumber();
        if (difference > 0) {
            if (difference > 30) {
                JOptionPane.showMessageDialog(this, "Way too low");
            } else {
                JOptionPane.showMessageDialog(this, "Too low");
            }
        } else {
            if (difference < -30) {
                JOptionPane.showMessageDialog(this, "Way too high");
            } else {
                JOptionPane.showMessageDialog(this, "Too high");
            }
        }
    }
}
