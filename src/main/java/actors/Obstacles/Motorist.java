package actors.Obstacles;

import actors.Actor;
import actors.Player;
import game.Stage;
import game.Utils;

public class Motorist extends Actor {
    private int pointValue = 0;
    private int damageValue = 5; //how much damage is done
    private boolean hit;
    private int minSpeed;
    private int maxSpeed;

    public Motorist(Stage canvas) {
        super(canvas);
        sprites = new String[]{"motorist" + Utils.randInt(0, 0) + ".png"};
        frameSpeed = 1;
        width = 65;
        height = 146;
    }

    public void update() {
        super.updateSpriteAnimation();
        updateSpeed();
    }

    private void updateSpeed() {
        //if car position is 2 screens above or below main screen, remove them from game.
        if (posY > stage.getHeight() * 3 || posY < stage.getHeight() * -3) {
            setMarkedForRemoval(true);
        }

        if (hit && vy < 0) {
            vy += 0.1f;
        }

//        if (Utils.randInt(1, 100) == 1) vy += Utils.randFloat(-1, 1);

//        if (vy < -maxSpeed) vy += 0.01f;
//        else if (vy > -minSpeed) vy -= 0.01f;

//        if (vx < 0) vx += 0.01f;
//        else if (vx > 0) vx -= 0.01f;

        posX += vx;
        posY += vy;
    }

    public void collision(Actor a) {
        if (a instanceof Motorist) {
            Motorist them = (Motorist) a;

            if (!them.isHit()) {
                hit = true;
                them.setHit(true);
                sprites = new String[]{"blood.png"};
            }
        }
    }


    @Override
    public int getPointValue() {
        return pointValue;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    public int getDamageValue() {
        return damageValue;
    }

    public void setDamageValue(int damageValue) {
        this.damageValue = damageValue;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setMinSpeed(int minSpeed) {
        this.minSpeed = minSpeed;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
}
