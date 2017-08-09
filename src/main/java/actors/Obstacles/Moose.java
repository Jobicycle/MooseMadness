package actors.Obstacles;

import actors.Actor;
import game.Stage;
import game.Utils;

/**
 * Moose actor. Acts as an obstacle that the player must try to avoid.
 */
public class Moose extends Actor {
    private int pointValue = 50; //value of a moose when it clears screen and is not hit by player
    private int damageValue = 7; //damage a moose does to the player
    private boolean hit = false; //hit flag
    private boolean directionRight;

    /**
     * Moose constructor.
     * @param stage
     */
    public Moose(Stage stage) {
        super(stage);
//        sprites = new String[]{"obstacles/moose0.png", "obstacles/moose1.png", "obstacles/moose2.png"};
        frameSpeed = 15;
        width = 50;
        height = 50;
    }

    /**
     * Moose sprite and movement updating
     */
    public void update() {
        if (!hit) {
            super.updateSpriteAnimation();
        }
        updateSpeed();
    }

    /**
     * Moose speed updater. Moose move at a speed assigned by the manager and once hit will quickly
     * reduce their speed to settle on the road.
     */
    private void updateSpeed() {
        posX += vx;
        posY += vy;

        if (posY >= stage.getHeight()) {
            setMarkedForRemoval(true);
        }

        //gradually slow down vx and vy after is has been hit
        if (hit) {
            if (vx > 0) vx -= 0.05f;
            else vx += 0.05f;

            if (vy > 0) vy -= 0.05f;
            else vy += 0.05f;

            if (vx > 0 && vx < 0.1 || vx < 0 && vx > -0.1) vx = 0;
            if (vy > 0 && vy < 0.1 || vy < 0 && vy > -0.1) vy = 0;
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

    public void setDirectionRight(boolean directionRight) {
        if (directionRight) {
            sprites = new String[]{"obstacles/moose0r.png", "obstacles/moose1r.png", "obstacles/moose2r.png"};
        } else {
            sprites = new String[]{"obstacles/moose0.png", "obstacles/moose1.png", "obstacles/moose2.png"};
        }
        this.directionRight = directionRight;
    }

    /**
     * setHit method. When being set to true, a random moose sound is played and the sprite is changed to
     * a death picture.
     * @param hit
     */
    public void setHit(boolean hit) {
        if (hit && !this.isHit()) {
            playSound("sounds/moose" + Utils.randInt(0, 2) + ".wav");
            if (directionRight) {
                sprites[0] = "obstacles/dmoose0r.png";
                sprites[1] = "obstacles/dmoose1r.png";
                sprites[2] = "obstacles/dmoose2r.png";
            } else {
                sprites[0] = "obstacles/dmoose0.png";
                sprites[1] = "obstacles/dmoose1.png";
                sprites[2] = "obstacles/dmoose2.png";
            }
        }

        this.hit = hit;
    }
}
