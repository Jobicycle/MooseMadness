package actors.Obstacles;

import actors.Actor;
import actors.Player;
import game.Stage;

public class Moose extends Actor {
    private int pointValue = 50; // point value if moose is not hit by player (clears screen)
    private int damageValue = 5; //how much damage is done
    private int weight = 40; //weight determines how much speed is reduce when the moose is hit
    private boolean hit = false;

    /**
     * @param stage
     */
    public Moose(Stage stage) {
        super(stage);
        sprites = new String[]{"moose0.png"};
        frameSpeed = 1;
        width = 60;
        height = 60;
    }

    /**
     *
     */
    public void update() {
        if (!hit) {
            super.updateSpriteAnimation();
            updateSpeed();
        }
    }

    /**
     *
     */
    private void updateSpeed() {
        posX += getVx();
        posY += getVy();

        if (posX > stage.getWidth() || posY > stage.getHeight()) {
            setMarkedForRemoval(true);
        }
    }

    /**
     * @param a
     */
    public void collision(Actor a) {
        //if moose hits player, mark as hit, reduce point value and change sprite
        if (a instanceof Player) {
            if (!hit) {
                hit = true;
                pointValue = 0;
                sprites = new String[]{"blood.png"};
            }
        }

        if (a instanceof Motorist) {
            if (!hit) {
                hit = true;
                sprites = new String[]{"blood.png"};
            }
        }
    }

    /**
     * @return
     */
    public int getPointValue() {
        return pointValue;
    }

    /**
     * @param pointValue
     */
    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    /**
     * @return
     */
    public int getDamageValue() {
        return damageValue;
    }

    /**
     * @param damageValue
     */
    public void setDamageValue(int damageValue) {
        this.damageValue = damageValue;
    }

    /**
     * @return
     */
    public boolean isHit() {
        return hit;
    }

    /**
     * @param hit
     */
    public void setHit(boolean hit) {
        this.hit = hit;
    }

    /**
     * @return
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @param weight
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }
}
