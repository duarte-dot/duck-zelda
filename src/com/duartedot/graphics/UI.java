package com.duartedot.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.duartedot.entities.Player;

public class UI {
    public void render(Graphics g) {
        if (Player.life <= 100) g.setColor(Color.orange);
        if (Player.life <= 40) g.setColor(Color.red);
        g.fillRect(8, 4, 50, 8);
        g.setColor(Color.green);
        g.fillRect(8, 4, (int) ((Player.life / 100) * 50), 8);
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 8));
        if (Player.life >= 10) {
        	g.drawString((int) Player.life + "/" + (int) Player.maxLife, 17, 11);
        } else {
        	g.drawString((int) Player.life + "/" + (int) Player.maxLife, 19, 11);
        }
    }
}
