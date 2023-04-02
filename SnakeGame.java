import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SnakeGame extends JPanel implements Runnable, KeyListener {
  
  private static final long serialVersionUID = 1L;
  private static final int WIDTH = 400;
  private static final int HEIGHT = 400;
  private static final int CELL_SIZE = 10;
  private ArrayList<Point> snake;
  private Point food;
  private int score;
  private boolean gameOver;
  private boolean paused;
  private int direction;
  private Random random;
  
  public SnakeGame() {
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
    setBackground(Color.BLACK);
    setFocusable(true);
    addKeyListener(this);
    snake = new ArrayList<Point>();
    food = new Point();
    random = new Random();
    direction = KeyEvent.VK_RIGHT;
    reset();
  }
  
  public void reset() {
    snake.clear();
    snake.add(new Point(0, 0));
    snake.add(new Point(0, 1));
    snake.add(new Point(0, 2));
    food.setLocation(random.nextInt(WIDTH / CELL_SIZE),
                      random.nextInt(HEIGHT / CELL_SIZE));
    score = 0;
    gameOver = false;
    paused = false;
    direction = KeyEvent.VK_RIGHT;
  }
  
  public void move() {
    if (!gameOver && !paused) {
      Point head = snake.get(0);
      Point next = new Point(head);
      switch (direction) {
        case KeyEvent.VK_UP:
          next.y -= 1;
          break;
        case KeyEvent.VK_DOWN:
          next.y += 1;
          break;
        case KeyEvent.VK_LEFT:
          next.x -= 1;
          break;
        case KeyEvent.VK_RIGHT:
          next.x += 1;
          break;
      }
      if (next.equals(food)) {
        snake.add(0, next);
        food.setLocation(random.nextInt(WIDTH / CELL_SIZE),
                          random.nextInt(HEIGHT / CELL_SIZE));
        score += 10;
      } else if (next.x < 0 || next.x >= WIDTH / CELL_SIZE ||
                 next.y < 0 || next.y >= HEIGHT / CELL_SIZE ||
                 snake.contains(next)) {
        gameOver = true;
      } else {
        snake.remove(snake.size() - 1);
        snake.add(0, next);
      }
    }
  }
  
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (!gameOver) {
      g.setColor(Color.RED);
      g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
      for (Point p : snake) {
        g.setColor(Color.GREEN);
        g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
      }
      g.setColor(Color.WHITE);
      g.drawString("Score: " + score, 10, 20);
      if (paused) {
        g.drawString("Paused", 10, HEIGHT - 10);
      }
    } else {
      g.setColor(Color.WHITE);
      g.drawString("Game Over", 10, HEIGHT - 50);
      g.drawString("Score: " + score, 10, HEIGHT - 30);
    }
  }
  
  public void run() {
    while (true) {
      move();
      repaint();
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
  
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == KeyEvent.VK_SPACE) {
      paused = !paused;
    } else if (key == KeyEvent.VK_ENTER && gameOver) {
      reset();
    } else if (!paused) {
      switch (key) {
        case KeyEvent.VK_UP:
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_RIGHT:
          direction = key;
          break;
      }
    }
  }
  
  public void keyTyped(KeyEvent e) {
  }
  
  public void keyReleased(KeyEvent e) {
  }
  
  public static void main(String[] args) {
    JFrame frame = new JFrame("Snake Game");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(new SnakeGame());
    frame.pack();
    frame.setVisible(true);
    new Thread((SnakeGame)frame.getContentPane().getComponent(0)).start();
  }
}