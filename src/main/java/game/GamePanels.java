package game;

import actors.Player;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class GamePanels extends Stage {

    private Player player;
    private Graphics g;
    private BufferStrategy strategy;
    private ScoreManager scoreManager;
    private BufferedImage image;

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
        g.drawString("Speed: " + (int) player.getSpeed() * 10, 20, 40);
    }

    public void printHealth() {
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(Color.green);
        g.drawString("Health: " + player.getHealth(), 20, 60);
    }

    public void printGameOver() {
        URL url = null;
        try {
            url = getClass().getClassLoader().getResource("res/gameover.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //color entire game panel black
        g.setColor(new Color(51, 0, 102));
        g.fillRect(0, 0, getWIDTH(), getHEIGHT());

        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.setColor(Color.YELLOW);
        int xPos = getHEIGHT() / 2 - 190;
        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 9 - 20, this);

        
        try {
            url = getClass().getClassLoader().getResource("res/entertryagain.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 4 , this);

        
        try {
            url = getClass().getClassLoader().getResource("res/yourscore.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.setColor(Color.PINK);
        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 4 +50 ,this);
        xPos += 100;
        g.drawString(""+ player.getScore(),(xPos < 0 ? 0 : xPos), getHEIGHT() / 4 + 150);

        //Displays highscores to user on game over screen
        scoreManager = new ScoreManager(player.getScore());
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.MAGENTA);
        g.drawString("HIGHSCORES: ", (xPos < 0 ? 0 : xPos), getHEIGHT() / 4 + 150);
        if (scoreManager.topThreeScores().size() >= 1) {
            g.drawString(String.valueOf(scoreManager.topThreeScores().get(0)), (xPos < 0 ? 0 : xPos), getHEIGHT() / 4 + 180);
        }
        if (scoreManager.topThreeScores().size() >= 2) {
            g.drawString(String.valueOf(scoreManager.topThreeScores().get(1)), (xPos < 0 ? 0 : xPos), getHEIGHT() / 4 + 210);
        }
        if (scoreManager.topThreeScores().size() >= 3) {
            g.drawString(String.valueOf(scoreManager.topThreeScores().get(2)), (xPos < 0 ? 0 : xPos), getHEIGHT() / 4 + 240);
        }
        strategy.show();
    }

    public void printMainMenu() {
        //color entire game panel black
        g.setColor(new Color(51, 0, 102));
        g.fillRect(0, 0, getWIDTH(), getHEIGHT());
        URL url = null;
        try {
            url = getClass().getClassLoader().getResource("res/mainmenu.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
       
       
        int xPos = getHEIGHT() / 2 - 190;
        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 6, this);
        
        try {
            url = getClass().getClassLoader().getResource("res/startenter.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 2 -50 , this);

        strategy.show();
    }

    public void printPause() {
        //color entire game panel black
        g.setColor(new Color(51, 0, 102));
        g.fillRect(0, 0, getWIDTH(), getHEIGHT());
        URL url = null;
        try {
            url = getClass().getClassLoader().getResource("res/pause.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        g.setFont(new Font("Arial", Font.BOLD, 50));
        
        int xPos = getHEIGHT() / 2 - 190;
        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 6, this);

        
        try {
            url = getClass().getClassLoader().getResource("res/escaperesume.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 2 -50,this);

        strategy.show();
    }
}
