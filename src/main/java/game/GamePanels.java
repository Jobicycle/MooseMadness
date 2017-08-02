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

    public void gameOver() {
        Graphics g = strategy.getDrawGraphics();
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        //about 310 pixels wide
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.setColor(Color.RED);
        int xPos = getWidth() / 2 - 155;
        g.drawString("GAME OVER", (xPos < 0 ? 0 : xPos), getHeight() / 2);

        xPos += 30;
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("ENTER: try again", (xPos < 0 ? 0 : xPos), getHeight() / 2 + 50);

        strategy.show();
    }
}
