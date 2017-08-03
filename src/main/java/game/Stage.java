package game;

import actors.Actor;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

public class Stage extends Canvas implements ImageObserver {

    private static final long serialVersionUID = 1L;
    public static final int WIDTH = 550;
    public static final int HEIGHT = 640;
    public static final int DESIRED_FPS = 60;

    protected boolean gameOver = false;

    public List<Actor> obstacles = new ArrayList<Actor>();
    public List<Actor> motorists = new ArrayList<Actor>();
    public List<Actor> powerups = new ArrayList<Actor>();

    public Stage() {
    }

    public void endGame() {
        gameOver = true;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return false;
    }

    public void initWorld() {
    }

    public void gameLoop() {}

    public void game() {
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }


}
