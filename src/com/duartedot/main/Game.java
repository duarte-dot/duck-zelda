package com.duartedot.main;

import com.duartedot.entities.Entity;
import com.duartedot.entities.Player;
import com.duartedot.graphics.Spritesheet;
import com.duartedot.world.World;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Game extends Canvas implements Runnable, KeyListener {

  private static final long serialVersionUID = 1L;

  // Variáveis de Dimensão da Janela
  // private final int WIDTH = 160;
  // private final int HEIGHT = 120;

  // private final int WIDTH = 240;
  // private final int HEIGHT = 160;

  private final int WIDTH = 320;
  private final int HEIGHT = 240;

  private final int SCALE = 3;

  // Componentes Gráficos
  private BufferedImage image;

  public static List<Entity> entities;
  public static Spritesheet spritesheet;

  public static World world;

  public static Player player;

  // Controle de Jogo
  private Thread thread;
  private boolean isRunning = false;

  // Janela do Jogo
  public static JFrame frame;

  // Construtor
  public Game() {
    this.addKeyListener(this);
    this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
    initFrame();

    image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

    entities = new ArrayList<Entity>();

    spritesheet = new Spritesheet("/spritesheet.png");

    player = new Player(0, 0, 16, 16, spritesheet.getSprite(0, 0, 16, 16));
    world = new World("/map.png");

    entities.add(player);
    // setFocusable(false);
  }

  // Método para Inicialização da Janela do Jogo
  public void initFrame() {
    frame = new JFrame("Duck Zelda");
    frame.add(this);
    frame.setResizable(false);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  // Métodos de Controle de Thread do Jogo
  public synchronized void start() {
    thread = new Thread(this);
    isRunning = true;
    thread.start();
  }

  public synchronized void stop() {
    isRunning = false;
    try {
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  // Método Principal do Jogo
  public static void main(String args[]) {
    Game game = new Game();
    game.start();
  }

  // Métodos de Atualização do Jogo
  public void tick() {
    for (int i = 0; i < entities.size(); i++) {
      Entity e = entities.get(i);
      e.tick();
    }
  }

  // Métodos de Renderização do Jogo
  public void render() {
    BufferStrategy bs = this.getBufferStrategy();
    if (bs == null) {
      this.createBufferStrategy(3);
      return;
    }

    Graphics g = image.getGraphics();
    g.setColor(new Color(19, 19, 19));
    g.fillRect(0, 0, WIDTH, HEIGHT);

    // Graphics2D g2 = (Graphics2D) g;

    world.render(g);

    for (int i = 0; i < entities.size(); i++) {
      Entity e = entities.get(i);
      e.render(g);
    }

    g.dispose();
    g = bs.getDrawGraphics();
    g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
    bs.show();
  }

  // Método de execução da Thread
  @Override
  public void run() {
    long lastTime = System.nanoTime();
    double amountOfTicks = 60.0;
    double ns = 1000000000 / amountOfTicks;
    double delta = 0;

    int frames = 0;
    double timer = System.currentTimeMillis();
    while (isRunning) {
      long now = System.nanoTime();
      delta += (now - lastTime) / ns;

      lastTime = now;

      if (delta >= 1) {
        tick();
        render();
        frames++;
        delta--;
      }

      if (System.currentTimeMillis() - timer >= 1000) {
        System.out.println("FPS:" + frames);
        frames = 0;
        timer += 1000;
      }
    }

    stop();
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (
      e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D
    ) {
      player.right = true;
    }

    if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
      player.left = true;
    }

    if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
      player.up = true;
    }

    if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
      player.down = true;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (
      e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D
    ) {
      player.right = false;
    }

    if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
      player.left = false;
    }

    if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
      player.up = false;
    }

    if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
      player.down = false;
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {}
}
