import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WhackAZombieGame extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int ZOMBIE_WIDTH = 100;
    private static final int ZOMBIE_HEIGHT = 100;
    private static final int MAX_ZOMBIES = 10;
    private static final int GAME_DURATION = 30; // in seconds

    private List<Zombie> zombies;
    private int score;
    private Timer gameTimer;
    private int timeRemaining;

    public WhackAZombieGame() {
        setTitle("Whack-a-Zombie");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        zombies = new ArrayList<>();
        score = 0;

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                checkZombieClicked(x, y);
            }
        });

        setFocusable(true);
        requestFocusInWindow();

        gameTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                if (timeRemaining <= 0) {
                    endGame();
                }
                repaint();
            }
        });
    }

    public void startGame() {
        score = 0;
        zombies.clear();
        timeRemaining = GAME_DURATION;
        gameTimer.start();
        generateZombies();
        repaint();
    }

    public void endGame() {
        gameTimer.stop();
        String message;
        if (score == MAX_ZOMBIES) {
            message = "Congratulations! You've whacked all the zombies!";
        } else {
            message = "Game Over! Your score: " + score;
        }
        JOptionPane.showMessageDialog(this, message);
    }

    private void generateZombies() {
        Random random = new Random();
        for (int i = 0; i < MAX_ZOMBIES; i++) {
            int x = random.nextInt(WIDTH - ZOMBIE_WIDTH);
            int y = random.nextInt(HEIGHT - ZOMBIE_HEIGHT);
            zombies.add(new Zombie(x, y));
        }
    }

    private void checkZombieClicked(int x, int y) {
        for (int i = 0; i < zombies.size(); i++) {
            Zombie zombie = zombies.get(i);
            if (zombie.isAlive() && zombie.contains(x, y)) {
                zombie.kill();
                score++;
                break;
            }
        }
        repaint();
        if (score == MAX_ZOMBIES) {
            endGame();
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.RED);
        for (Zombie zombie : zombies) {
            if (zombie.isAlive()) {
                g.fillOval(zombie.getX(), zombie.getY(), ZOMBIE_WIDTH, ZOMBIE_HEIGHT);
            }
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 20, 30);
        g.drawString("Time: " + timeRemaining, 20, 60);
    }

    private class Zombie {
        private int x;
        private int y;
        private boolean alive;

        public Zombie(int x, int y) {
            this.x = x;
            this.y = y;
            this.alive = true;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean isAlive() {
            return alive;
        }

        public void kill() {
            alive = false;
        }

        public boolean contains(int x, int y) {
            return x >= this.x && x <= (this.x + ZOMBIE_WIDTH) && y >= this.y && y <= (this.y + ZOMBIE_HEIGHT);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                WhackAZombieGame game = new WhackAZombieGame();
                game.setVisible(true);
                game.startGame();
            }
        });
    }
}
