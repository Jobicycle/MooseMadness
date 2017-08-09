package actors.Obstacles;

import actors.Actor;
import Game.Stage;
import Game.Utils;

/**
 * Pothole class. Potholes act as stationary obstacles that the player must try to avoid.
 * They also cause motorists to drive erratically when being passed over.
 */
public class Pothole extends Actor {
    private int pointValue = 25; //point value when a pothole is cleared off the screen without being hit
    private int damageValue = 2; //damage done to player when hit
    private boolean hit = false; //hit flag

    /**
     * Pothole constructor
     *
     * @param canvas
     */
    public Pothole(Stage canvas) {
        super(canvas);
        sprites = new String[]{"obstacles/pothole" + Utils.randInt(0, 1) + ".png"};
        width = 30;
        height = 40;
    }

    /**
     * Pothole updater. Potholes are removed when they pass beyond the bottom of the screen.
     */
    public void update() {
        if (posY >= stage.getHeight()) {
            setMarkedForRemoval(true);
        }
    }

    public int getPointValue() {
        return pointValue;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    public int getDamageValue() {
        return damageValue;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
}
