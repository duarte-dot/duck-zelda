package com.duartedot.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.duartedot.main.Game;

public class UI {
    public void render(Graphics g) {
        if (Game.player.life <= 100)
            g.setColor(Color.orange);
        if (Game.player.life <= 40)
            g.setColor(Color.red);
        g.fillRect(8, 4, 50, 8);
        g.setColor(Color.green);
        g.fillRect(8, 4, (int) ((Game.player.life / 100) * 50), 8);
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 8));
        if (Game.player.life >= 10) {
            g.drawString((int) Game.player.life + "/" + (int) Game.player.maxLife, 14, 11);
        } else {
            g.drawString((int) Game.player.life + "/" + (int) Game.player.maxLife, 19, 11);
        }
    }
}
