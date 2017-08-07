package game;

import actors.Player;

import java.awt.event.KeyEvent;

/**
 * creates a thread to process player input
 *
 * @author ghast
 */

public class InputHandler extends Thread {

    public Action action;
    public KeyEvent event;
    private MooseMadness mooseMadness = null;
    private Player player = null;

    public InputHandler(MooseMadness mooseMadness, Player player) {
        this.mooseMadness = mooseMadness;
        this.player = player;
    }

    public void run() {
        if (action == Action.PRESS) {
            int keyPressed = event.getKeyCode();

            if (mooseMadness.state == Stage.GameState.GAMEOVER) { // if in gameover
                if (KeyEvent.VK_ENTER == keyPressed) { // if enter start new game
                    mooseMadness.state = Stage.GameState.GAME;
                    mooseMadness.initWorld();
                    mooseMadness.game();
                } else if (KeyEvent.VK_ESCAPE == keyPressed) { //if escape key go to menu
                    mooseMadness.state = Stage.GameState.MENU;
                    mooseMadness.game();
                }

            } else if (mooseMadness.state == Stage.GameState.MENU) { // if in menu
                if (KeyEvent.VK_ENTER == keyPressed) { // if enter key pressed start new game
                    mooseMadness.state = Stage.GameState.GAME;
                    mooseMadness.initWorld();
                    mooseMadness.game();
                } else if (KeyEvent.VK_ESCAPE == keyPressed) { //if escape key quit program
                    System.exit(0);
                }


            } else if (mooseMadness.state == Stage.GameState.OPTIONS) { // if in options

            } else if (mooseMadness.state == Stage.GameState.HIGHSCORES) { // if in highscores

            } else if (mooseMadness.state == Stage.GameState.PAUSE) { // if in pause
                if (KeyEvent.VK_ESCAPE == keyPressed) {
                    mooseMadness.state = Stage.GameState.GAME;
                    mooseMadness.game();
                }

            } else {
                if (KeyEvent.VK_ESCAPE == keyPressed) {
                    mooseMadness.state = Stage.GameState.PAUSE;
                } else {
                    player.triggerKeyPress(event);
                }
            }
        } else if (action == Action.RELEASE) {
            player.triggerKeyRelease(event);
        }
    }

    public enum Action {
        PRESS, RELEASE
    }
}