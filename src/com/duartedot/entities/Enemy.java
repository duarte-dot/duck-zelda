package com.duartedot.entities;

import com.duartedot.main.Game;
import com.duartedot.world.Camera;
import com.duartedot.world.World;

// import java.awt.Color;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {

  private static int speed = 1;

  public int maskx = 8, masky = 8, mwidth = 10, mheight = 10;

  private int frames = 0, maxFrames = 20, index = 0, maxIndex = 1;
  private BufferedImage[] sprites;

  public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
    super(x, y, width, height, null);

    sprites = new BufferedImage[2];
    sprites[0] = Game.spritesheet.getSprite(7 * 16, 32, 16, 16);
    sprites[1] = Game.spritesheet.getSprite(7 * 16, 16, 16, 16);
  }

  public void tick() {
    if (Game.rand.nextInt(100) > 30) {
      if (!isColiddingWithPlayer()) {
        if (x < Game.player.getX() && World.isFree((int) (x + speed), this.getY())
            && !isColidding((int) (x + speed), this.getY())) {
          x += speed;
        } else if (x > Game.player.getX() && World.isFree((int) (x - speed), this.getY())
            && !isColidding((int) (x - speed), this.getY())) {
          x -= speed;
        }

        if (y < Game.player.getY() && World.isFree(this.getX(), (int) (y + speed))
            && !isColidding(this.getX(), (int) (y + speed))) {
          y += speed;
        } else if (y > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed))
            && !isColidding(this.getX(), (int) (y - speed))) {
          y -= speed;
        }
      } else {
        if (Game.rand.nextInt(100) < 10) {
          Player.life -= Game.rand.nextInt(3);

          if (Player.life <= 0) {
            Player.life = 0;
          }

          if (Player.life <= 0) {
            // Game Over
            System.exit(1);
          }
        }
      }
    }

    frames++;
    if (frames == maxFrames) {
      frames = 0;
      index++;
      if (index > maxIndex) {
        index = 0;
      }
    }
  }

  public boolean isColiddingWithPlayer() {
    Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, mwidth, mheight);
    Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);

    return enemyCurrent.intersects(player);
  }

  public boolean isColidding(int xnext, int ynext) {
    Rectangle enemyCurrent = new Rectangle(xnext, ynext, World.TILE_SIZE, World.TILE_SIZE);

    for (int i = 0; i < Game.enemies.size(); i++) {
      Enemy e = Game.enemies.get(i);

      if (e == this) {
        continue;
      }

      Rectangle targetEnemy = new Rectangle(e.getX(), e.getY(), World.TILE_SIZE, World.TILE_SIZE);

      if (enemyCurrent.intersects(targetEnemy)) {
        return true;
      }
    }

    return false;
  }

  public void render(Graphics g) {
    // super.render(g);

    // g.setColor(Color.BLUE);
    // g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y,
    // mwidth, mheight);

    g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
  }
}
