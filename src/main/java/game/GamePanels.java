package game;

import actors.Player;
import game.ScoreManager;
import game.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Game Panels - Class for different States of the game and user interfaces.
 * Used for Printing score, printing menu, pause, and game over screen
 *
 */
public class GamePanels extends Stage {

    private Player player;
    private Graphics g;
    private BufferStrategy strategy;
    private ScoreManager scoreManager;
    private BufferedImage image;

    /**
     * Game panel Constructor
     *
     * @param player
     * @param g
     * @param strategy
     */
    public GamePanels(Player player, Graphics g, BufferStrategy strategy) {
        this.player = player;
        this.g = g;
        this.strategy = strategy;
    }

    /**
     * Print Score() - used to display score during game
     */
    public void printScore() {
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(Color.green);
        g.drawString("Score: ", 20, 20);
        g.setColor(Color.red);
        g.drawString("" + player.getScore(), 90, 20);
    }

    /**
     * PrintSpeed() - used to print speed during game
     */
    public void printSpeed() {
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(Color.green);
        g.drawString("Speed: " + (int) player.getSpeed() * 20, 20, 40);
    }

    /**
     * printHealth() - used to print health during game
     */
    public void printHealth() {
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(Color.green);
        g.drawString("Health: " + player.getHealth(), 20, 60);
    }

    /**
     * Prints the number of air horns the player has in the ui
     */
    public void printAirHorns() {
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(Color.green);
        g.drawString("Horns: " + player.getNumberOfHorns(), 20, 80);
    }

    /**
     * Prints time played on ui
     * @param sessionRunTime
     */
    public void printTimePlayed(float sessionRunTime) {
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(Color.green);
        g.drawString("Time: " + String.format("%.02f", sessionRunTime), 20, 100);
    }

    /**
     * printGameOver() - Game over menu design
     */
    public void printGameOver() {
        //color entire game panel black
        g.setColor(new Color(51, 0, 102));
        g.fillRect(0, 0, getWIDTH(), getHEIGHT());

        URL url = null;
        try {
            url = getClass().getClassLoader().getResource("res/menus/gameover.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        int xPos = getWIDTH() / 4;
        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 9 - 90, this);

        try {
            url = getClass().getClassLoader().getResource("res/menus/escapemain.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 9 - 10, this);
        try {
            url = getClass().getClassLoader().getResource("res/menus/entertryagain.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 4 - 30, this);

        try {
            url = getClass().getClassLoader().getResource("res/menus/yourscore.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 4 + 50, this);
        xPos = getWIDTH() / 4 + 400;
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.setColor(Color.PINK);
        g.drawString("" + player.getScore(), (xPos < 0 ? 0 : xPos), getHEIGHT() / 4 + 150);
        xPos = getWIDTH()/4;
        //Displays highscores to user on game over screen
        scoreManager = new ScoreManager(player.getScore());

        try {
            url = getClass().getClassLoader().getResource("res/menus/highscores.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 4 + 150, this);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.PINK);
        xPos = getWIDTH() / 2 -50;

        if (scoreManager.topThreeScores().size() >= 1) {
            g.drawString(String.valueOf(scoreManager.topThreeScores().get(0)), (xPos < 0 ? 0 : xPos), getHEIGHT() / 4 + 300);
        }
        if (scoreManager.topThreeScores().size() >= 2) {
            g.drawString(String.valueOf(scoreManager.topThreeScores().get(1)), (xPos < 0 ? 0 : xPos), getHEIGHT() / 4 + 325);
        }
        if (scoreManager.topThreeScores().size() >= 3) {
            g.drawString(String.valueOf(scoreManager.topThreeScores().get(2)), (xPos < 0 ? 0 : xPos), getHEIGHT() / 4 + 350);
        }

        strategy.show();
    }

    /**
     * printMainMenu() - Main Menu Design
     */
    public void printMainMenu() {
        //color entire game panel black
        g.setColor(new Color(51, 0, 102));
        g.fillRect(0, 0, getWIDTH(), getHEIGHT());
        URL url = null;
        try {
            url = getClass().getClassLoader().getResource("res/menus/mainmenu.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        int xPos = getWIDTH() / 4;
        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 6, this);

        try {
            url = getClass().getClassLoader().getResource("res/menus/startenter.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 2 - 50, this);

        try {
            url = getClass().getClassLoader().getResource("res/menus/spacehighscores.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 2 , this);

        strategy.show();
    }

    /**
     * printPause() - Pause Menu design
     */
    public void printPause() {
        //color entire game panel black
        g.setColor(new Color(51, 0, 102));
        g.fillRect(0, 0, getWIDTH(), getHEIGHT());
        URL url = null;
        try {
            url = getClass().getClassLoader().getResource("res/menus/pause.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        g.setFont(new Font("Arial", Font.BOLD, 50));

        int xPos = getWIDTH() / 4;
        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 6, this);

        try {
            url = getClass().getClassLoader().getResource("res/menus/escaperesume.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 2 - 50, this);

        try {
            url = getClass().getClassLoader().getResource("res/menus/spacemain.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 2 , this);
        strategy.show();
    }

    public void printHighscores() {

        g.setColor(new Color(51, 0, 102));
        g.fillRect(0, 0, getWIDTH(), getHEIGHT());
        int xPos = getWIDTH() / 4;
        URL url = null;
        try {
            url = getClass().getClassLoader().getResource("res/menus/highscores.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 20, this);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.PINK);
        scoreManager = new ScoreManager(player.getScore());
        xPos += 200;
        if (scoreManager.topTenScores().size() >= 1) {
            g.drawString(String.valueOf(scoreManager.topTenScores().get(0)), (xPos < 0 ? 0 : xPos), getHEIGHT() / 20 +150);
        }
        if (scoreManager.topTenScores().size() >= 2) {
            g.drawString(String.valueOf(scoreManager.topTenScores().get(1)), (xPos < 0 ? 0 : xPos), getHEIGHT() / 20 + 175);
        }
        if (scoreManager.topTenScores().size() >= 3) {
            g.drawString(String.valueOf(scoreManager.topTenScores().get(2)), (xPos < 0 ? 0 : xPos), getHEIGHT() / 20 + 200);
        }
        if (scoreManager.topTenScores().size() >= 4) {
            g.drawString(String.valueOf(scoreManager.topTenScores().get(3)), (xPos < 0 ? 0 : xPos), getHEIGHT() / 20 + 225);
        }
        if (scoreManager.topTenScores().size() >= 5) {
            g.drawString(String.valueOf(scoreManager.topTenScores().get(4)), (xPos < 0 ? 0 : xPos), getHEIGHT() / 20 + 250);
        }
        if (scoreManager.topTenScores().size() >= 6) {
            g.drawString(String.valueOf(scoreManager.topTenScores().get(5)), (xPos < 0 ? 0 : xPos), getHEIGHT() / 20 + 275);
        }
        if (scoreManager.topTenScores().size() >= 7) {
            g.drawString(String.valueOf(scoreManager.topTenScores().get(6)), (xPos < 0 ? 0 : xPos), getHEIGHT() / 20 + 300);
        }
        if (scoreManager.topTenScores().size() >= 8) {
            g.drawString(String.valueOf(scoreManager.topTenScores().get(7)), (xPos < 0 ? 0 : xPos), getHEIGHT() / 20 + 325);
        }
        if (scoreManager.topTenScores().size() >= 9) {
            g.drawString(String.valueOf(scoreManager.topTenScores().get(8)), (xPos < 0 ? 0 : xPos), getHEIGHT() / 20 + 350);
        }
        if (scoreManager.topTenScores().size() >= 10) {
            g.drawString(String.valueOf(scoreManager.topTenScores().get(9)), (xPos < 0 ? 0 : xPos), getHEIGHT() / 20 + 375);
        }
        xPos = getWIDTH() / 4;
        try {
            url = getClass().getClassLoader().getResource("res/menus/escapemain.png");
            image = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        g.drawImage(image, (xPos < 0 ? 0 : xPos), getHEIGHT() / 20 +350, this);

        strategy.show();
    }
}
