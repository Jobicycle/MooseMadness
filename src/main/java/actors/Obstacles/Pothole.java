package actors.Obstacles;

import actors.Actor;
import actors.Player;
import game.Stage;
import game.Utils;

public class Pothole extends Actor {
    private int pointValue = 25;
    private int damageValue = 2;
    private boolean hit = false;

    public Pothole(Stage canvas) {
        super(canvas);
        sprites = new String[]{"pothole" + Utils.randInt(0,1) + ".png"};
        width = 40;
        height = 64;
    }

    public void update() {
        super.updateSpriteAnimation();

        if (posY >= stage.getHeight()) {
            setMarkedForRemoval(true);
        }
    }

    public void collision(Actor a) {
        if (a instanceof Player) {
            if (!isHit()) {
                hit = true;
                pointValue = 0;
            }
        }
    }


    public int getPointValue() {
        return pointValue;
    }

    public int getDamageValue() {
        return damageValue;
    }

    public boolean isHit() {
        return hit;
    }
}
