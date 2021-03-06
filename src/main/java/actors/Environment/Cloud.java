package actors.Environment;

import actors.Actor;
import Game.Stage;

/**
 * Cloud class, acts as part of the environment reducing visibility for the player
 */
public class Cloud extends Actor {

    /**
     * Cloud constructor
     * @param canvas
     */
    public Cloud(Stage canvas) {
        super(canvas);
        sprites = new String[]{"environment/cloud0.png"};
        width = 183;
        height = 140;
    }

    public void update() {
        updateSpeed();
    }

    /**
     * Cloud speed updater. Clouds are marked for removal when they reach the right or bottom bounds of the screen.
     */
    private void updateSpeed() {
        posX += vx;
        posY += vy;

        if (posY >= stage.getHeight() || posX > stage.getWidth() || posX + width < 0) {
            setMarkedForRemoval(true);
        }
    }
}
