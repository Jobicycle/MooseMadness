package actors.Obstacles;

import actors.Actor;
import actors.Player;
import game.Stage;

public class Moose extends Actor {
    private int pointValue = 50; // point value if moose is not hit by player (clears screen)
    private int damageValue = 5; //how much damage is done
    private float weight = 2f; //weight determines how much speed is reduce when the moose is hit
    private boolean hit = false;

    /**
     * @param stage
     */
    public Moose(Stage stage) {
        super(stage);
        sprites = new String[]{"moose0.png", "moose1.png", "moose2.png"};
        frameSpeed = 15;
        width = 50;
        height = 50;
    }

    /**
     *
     */
    public void update() {
        if (!hit) {
            super.updateSpriteAnimation();
        }
        updateSpeed();
    }

    /**
     *
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

    /**
     * @param a
     */
    public void collision(Actor a) {
        //if moose hits player, mark as hit, reduce point value and change sprite
        if (a instanceof Player) {
            if (!hit) {
                playSound("moosehit.wav");
                hit = true;
                pointValue = 0;
                sprites[0] = "dmoose0.png";
                sprites[1] = "dmoose1.png";
                sprites[2] = "dmoose2.png";

            }
        }

        if (a instanceof Motorist) {
            if (!hit) {
                playSound("moosehit.wav");
                hit = true;
                sprites[0] = "dmoose0.png";
                sprites[1] = "dmoose1.png";
                sprites[2] = "dmoose2.png";
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
     * @return
     */
    public int getDamageValue() {
        return damageValue;
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
    public float getWeight() {
        return weight;
    }

    /**
     * @param weight
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }
}
