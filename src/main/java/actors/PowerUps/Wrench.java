package actors.PowerUps;

import actors.Actor;
import game.Stage;

public class Wrench extends Actor {
    private int pointValue = 100;
    private int healingValue = 20;
    private String imgLoc = "powerups/wrench";

    public Wrench(Stage canvas) {
        super(canvas);
        sprites = new String[]{imgLoc + "1.png", imgLoc + "2.png", imgLoc + "3.png",imgLoc + "4.png", imgLoc + "5.png",imgLoc + "6.png",imgLoc + "7.png",
                imgLoc + "8.png",imgLoc + "9.png", imgLoc + "10.png",imgLoc + "11.png", imgLoc + "12.png"};
        width = 45;
        height = 45;
        frameSpeed = 10;
    }

    public void update() {
        super.updateSpriteAnimation();

        if (posY >= stage.getHeight()) {
            setMarkedForRemoval(true);
        }
    }

    public int getPointValue() {
        return pointValue;
    }

    public int getHealingValue() {
        return healingValue;
    }
}
