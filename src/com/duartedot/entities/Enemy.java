package com.duartedot.entities;

import com.duartedot.main.Game;
import com.duartedot.world.World;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {

  private static double speed = 0.4;

  public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
    super(x, y, width, height, sprite);
  }

  public void tick() {
    if (
      x < Game.player.getX() && World.isFree((int) (x + speed), this.getY())
    ) {
      x += speed;
    } else if (
      x > Game.player.getX() && World.isFree((int) (x - speed), this.getY())
    ) {
      x -= speed;
    }

    if (
      y < Game.player.getY() && World.isFree(this.getX(), (int) (y + speed))
    ) {
      y += speed;
    } else if (
      y > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed))
    ) {
      y -= speed;
    }
  }
}
