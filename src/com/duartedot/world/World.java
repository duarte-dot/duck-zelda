package com.duartedot.world;

import com.duartedot.entities.*;
import com.duartedot.main.Game;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class World {

  private static Tile[] tiles;
  public static int WIDTH, HEIGHT;
  public static int TILE_SIZE = 16;

  public World(String path) {
    try {
      BufferedImage map = ImageIO.read(getClass().getResource(path));
      int[] pixels = new int[map.getWidth() * map.getHeight()];

      WIDTH = map.getWidth();
      HEIGHT = map.getHeight();

      tiles = new Tile[WIDTH * HEIGHT];

      map.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
      for (int i = 0; i < WIDTH; i++) {
        for (int j = 0; j < HEIGHT; j++) {
          int pixelAtual = pixels[i + (j * WIDTH)];

          tiles[i + j * WIDTH] = new FloorTile(i * 16, j * 16, Tile.TILE_FLOOR);

          if (pixelAtual == 0xFF000000) {
            // Floor
            tiles[i + j * WIDTH] = new FloorTile(
              i * 16,
              j * 16,
              Tile.TILE_FLOOR
            );
          } else if (pixelAtual == 0xFFFFFFFF) {
            // Wall
            tiles[i + j * WIDTH] = new WallTile(i * 16, j * 16, Tile.TILE_WALL);
          } else if (pixelAtual == 0xFF0008FF) {
            Game.player.setX(i * 16);
            Game.player.setY(j * 16);
          } else if (pixelAtual == 0xFFFF0000) {
            // Enemy
            Enemy en = new Enemy(i * 16, j * 16, 16, 16, Entity.ENEMY_EN);
            Game.entities.add(en);
            Game.enemies.add(en);
          } else if (pixelAtual == 0xFFD2701E) {
            // Weapon
            Game.entities.add(
              new Weapon(i * 16, j * 16, 16, 16, Entity.WEAPON_EN)
            );
          } else if (pixelAtual == 0xFFC487C8) {
            // Lifepack
            Game.entities.add(
              new LifePack(i * 16, j * 16, 16, 16, Entity.LIFEPACK_EN)
            );
          } else if (pixelAtual == 0xFFFFC900) {
            // Bullet
            Game.entities.add(
              new Bullet(i * 16, j * 16, 16, 16, Entity.BULLET_EN)
            );
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean isFree(int xnext, int ynext) {
    // Subtrai 4 pixels de cada lado
    int x1 = (xnext + 1) / TILE_SIZE;
    int y1 = (ynext + 3) / TILE_SIZE;

    int x2 = ((xnext + TILE_SIZE - 1) - 1) / TILE_SIZE;
    int y2 = (ynext + 3) / TILE_SIZE;

    int x3 = (xnext + 1) / TILE_SIZE;
    int y3 = ((ynext + TILE_SIZE - 1) - 4) / TILE_SIZE;

    int x4 = ((xnext + TILE_SIZE - 1) - 1) / TILE_SIZE;
    int y4 = ((ynext + TILE_SIZE - 1) - 4) / TILE_SIZE;

    return !(
      (tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile) ||
      (tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile) ||
      (tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile) ||
      (tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile)
    );
  }

  public void render(Graphics g) {
    int xstart = Camera.x >> 4;
    int ystart = Camera.y >> 4;

    int xfinal = xstart + (Game.WIDTH >> 4);
    int yfinal = ystart + (Game.HEIGHT >> 4);

    for (int i = xstart; i <= xfinal; i++) {
      for (int j = ystart; j <= yfinal; j++) {
        if (i < 0 || j < 0 || i >= WIDTH || j >= HEIGHT) {
          continue;
        }

        Tile tile = tiles[i + j * WIDTH];
        tile.render(g);
      }
    }
  }
}
