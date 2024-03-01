package com.duartedot.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Game extends Canvas implements Runnable {

  private static final long serialVersionUID = 1L;

  // Variáveis de Dimensão da Janela
  private final int WIDTH = 160;
  private final int HEIGHT = 120;
  private final int SCALE = 3;

  // Componentes Gráficos
  private BufferedImage image;

  // Controle de Jogo
  private Thread thread;
  private boolean isRunning = false;

  // Janela do Jogo
  public static JFrame frame;

  // Construtor
  public Game() {
    this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
    initFrame();

    image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    // setFocusable(false);
  }

  // Método para Inicialização da Janela do Jogo
  public void initFrame() {
    frame = new JFrame("Zelda Clone");
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
  public void tick() {}

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
}
