package game;

import actors.Player;

import java.awt.event.KeyEvent;

/**
 * creates a thread to process player input
 *
 * @author ghast
 */

public class InputHandler extends Thread {

    private MooseMadness mooseMadness = null;
    private Player player  = null;
    public Action action;
    public KeyEvent event;

    public InputHandler(MooseMadness mooseMadness, Player player) {
        this.mooseMadness = mooseMadness;
        this.player = player;
    }

    public void run() {
        if (action == Action.PRESS) {
            if (KeyEvent.VK_ENTER == event.getKeyCode()) {
                if (mooseMadness.gameOver) {
                    mooseMadness.initWorld();
                    mooseMadness.gameLoop();
                }
            }

            else
                player.triggerKeyPress(event);
        }
        else if (action == Action.RELSEASE)
            player.triggerKeyRelease(event);
    }

    public enum Action {
        PRESS,
        RELSEASE
    }
}

//public class InputHandler {
//    public Action action;
//    private Stage stage = null;
//    private KeyboardControllable player = null;
//
//    public InputHandler(Stage stage, KeyboardControllable player) {
//        this.stage = stage;
//        this.player = player;
//    }
//
//    public void handleInput(KeyEvent e) {
//        if (action == Action.PRESS) {
//            if (e.getKeyCode() == KeyEvent.VK_ENTER) { //if enter key pressed
//                if (stage.isGameOver()) { //if in game over screen
//                    stage.initWorld();
//                    stage.gameLoop();
//                }
//
//            } else {
//                player.triggerKeyPress(e);
//            }
//        } else if (action == Action.RELEASE) {
//            player.triggerKeyRelease(e);
//        }
//    }
//
//    public enum Action {
//        PRESS, RELEASE
//    }
//}