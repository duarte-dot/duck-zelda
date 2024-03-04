package com.duartedot.world;

import com.duartedot.entities.*;
import com.duartedot.main.Game;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class World {

  private Tile[] tiles;
  public static int WIDTH, HEIGHT;

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
            Game.entities.add(
              new Enemy(i * 16, j * 16, 16, 16, Entity.ENEMY_EN)
            );
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

  public void render(Graphics g) {
    for (int i = 0; i < WIDTH; i++) {
      for (int j = 0; j < HEIGHT; j++) {
        Tile tile = tiles[i + j * WIDTH];
        tile.render(g);
      }
    }
  }
}
