package com.duartedot.entities;

import com.duartedot.main.Game;
import com.duartedot.world.Camera;
import com.duartedot.world.World;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Player extends Entity {

  public boolean right, left, up, down;
  public int right_dir = 0, left_dir = 1;
  public int dir = right_dir;
  public double speed = 1.4;

  private int frames = 0, maxFrames = 5, index = 0, maxIndex = 5;
  private boolean moved = false;

  private long lastTime; // Variável para controlar o tempo
  private long delay = 500; // Delay de 0.5 segundos em milissegundos

  private BufferedImage[] rightPlayer;
  private BufferedImage[] leftPlayer;

  public Player(int x, int y, int width, int height, BufferedImage sprite) {
    super(x, y, width, height, sprite);
    rightPlayer = new BufferedImage[6];
    leftPlayer = new BufferedImage[6];

    for (int i = 0; i < 6; i++) {
      rightPlayer[i] = Game.spritesheet.getSprite(0 + (i * 16), 0, 16, 16);
    }

    for (int i = 0; i < 6; i++) {
      leftPlayer[i] = Game.spritesheet.getSprite(0 + (i * 16), 16, 16, 16);
    }

    lastTime = System.currentTimeMillis();
  }

  public void tick() {
    moved = false;
    double diagonalSpeed = (speed * Math.sqrt(2)) / 2;

    if (right && World.isFree(this.getX() + (int) speed, this.getY())) {
      dir = right_dir;
      moved = true;
      setX(x + (right && up ? diagonalSpeed : speed));
    } else if (left && World.isFree(this.getX() - (int) speed, this.getY())) {
      dir = left_dir;
      moved = true;
      setX(x - (left && up ? diagonalSpeed : speed));
    }

    if (up && World.isFree(this.getX(), (int) (y - speed))) {
      moved = true;
      setY(y - (up && (right || left) ? diagonalSpeed : speed));
    } else if (down && World.isFree(this.getX(), (int) (y + speed))) {
      moved = true;
      setY(y + (down && (right || left) ? diagonalSpeed : speed));
    }

    long now = System.currentTimeMillis(); // Obtendo o tempo atual
    long elapsedTime = now - lastTime; // Calculando o tempo decorrido desde a última atualização

    if (moved) {
      frames++;

      if (frames == maxFrames) {
        frames = 0;
        index++;

        if (index > maxIndex) {
          index = 2;
        }
      }
    } else {
      // Se o tempo decorrido for maior ou igual ao delay, então atualiza o sprite
      if (elapsedTime >= delay) {
        index = (index == 0) ? 1 : 0;
        frames = 0;
        lastTime = now; // Atualizando o tempo da última atualização
      }
    }

    Camera.x = Camera.clamp(
      this.getX() - (Game.WIDTH / 2),
      0,
      World.WIDTH * 16 - Game.WIDTH
    );

    Camera.y = Camera.clamp(
      this.getY() - (Game.HEIGHT / 2),
      0,
      World.HEIGHT * 16 - Game.HEIGHT
    );
  }

  public void render(Graphics g) {
    if (dir == right_dir) {
      g.drawImage(
        rightPlayer[index],
        this.getX() - Camera.x,
        this.getY() - Camera.y,
        null
      );
    } else if (dir == left_dir) {
      g.drawImage(
        leftPlayer[index],
        this.getX() - Camera.x,
        this.getY() - Camera.y,
        null
      );
    } else {
      g.drawImage(
        rightPlayer[0],
        this.getX() - Camera.x,
        this.getY() - Camera.y,
        null
      );
    }
  }
}
