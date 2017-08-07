package game;

import actors.Actor;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

public class Stage extends Canvas implements ImageObserver {
    public static final int WIDTH = 700;
    public static final int HEIGHT = 640;
    public static final int DESIRED_FPS = 60;
    public GameState state;

    public Stage() {
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return false;
    }

    public void initWorld() {
    }

    public void game() {
    }

    public void run() {
        game();
    }

    public enum GameState {
        MENU, OPTIONS, GAME, PAUSE, GAMEOVER, HIGHSCORES
    }


}
