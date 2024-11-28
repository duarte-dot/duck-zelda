package com.duartedot.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.duartedot.main.Game;
import com.duartedot.world.Camera;

public class BulletShoot extends Entity {

	private int dx;
	private int dy;
	private double speed = 4;
	private int life = 30, curLife = 0;

	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

	public void tick() {
		x += dx * speed;
		y += dy * speed;

		curLife++;
		if (curLife == life) {
			Game.bullets.remove(this);
			return;
		}
	}

	public void render(Graphics g) {
		// g.drawImage(sprite, this.getX(), this.getY(), null); // Uncomment this line
		g.setColor(Color.YELLOW);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, 2, 2);
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

}
