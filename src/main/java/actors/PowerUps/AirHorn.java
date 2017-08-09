package actors.PowerUps;

import actors.Actor;
import Game.Stage;

/**
 * Airhorn powerup class. Used by the player to temporarily move non stationary obstacles out of the way.
 */
public class AirHorn extends Actor {
    private int pointValue = 1000;

    /**
     * Airhorn constructor
     */
    public AirHorn(Stage canvas) {
        super(canvas);
        sprites = new String[]{"powerups/airhorn0.png"};
        width = 40;
        height = 60;
    }

    /**
     * Airhorn updater. Is marked for removal when reaching the bottom of the screen.
     */
    public void update() {
        if (posY >= stage.getHeight()) {
            pointValue = 0;
            setMarkedForRemoval(true);
        }
    }

    public int getPointValue() {
        return pointValue;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }
}
