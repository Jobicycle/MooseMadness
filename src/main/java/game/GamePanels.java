package game;

import actors.Player;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class GamePanels extends Stage {
    private Player player;
    private Graphics g;
    private BufferStrategy strategy;

    public GamePanels(Player player, Graphics g, BufferStrategy strategy) {
        this.player = player;
        this.g = g;
        this.strategy = strategy;
    }

    public void printScore() {
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(Color.green);
        g.drawString("Score: ", 20, 20);
        g.setColor(Color.red);
        g.drawString("" + player.getScore(), 90, 20);
    }

    public void printSpeed() {
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(Color.green);
        g.drawString("Speed: " + player.getSpeed(), 20, 40);
    }

    public void printHealth() {
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(Color.green);
        g.drawString("Health: " + player.getHealth(), 20, 60);
    }

    public void printGameOver() {
        //color entire game panel black
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWIDTH(), getHEIGHT());

        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.setColor(Color.RED);
        int xPos = getHEIGHT() / 2 - 190;
        g.drawString("GAME OVER", (xPos < 0 ? 0 : xPos), getHEIGHT() / 2);

        xPos += 30;
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("ENTER: try again", (xPos < 0 ? 0 : xPos), getHEIGHT() / 2 + 50);

        strategy.show();
    }
}
