package actors.PowerUps;

import actors.Actor;
import game.Stage;

public class Wrench extends Actor {
    private int pointValue = 100;
    private int healingValue = 20;

    public Wrench(Stage canvas) {
        super(canvas);
        sprites = new String[]{"wrench1.png" , "wrench2.png" ,"wrench3.png" ,"wrench4.png" ,"wrench5.png" ,"wrench6.png" ,"wrench7.png"
                ,"wrench8.png" ,"wrench9.png" ,"wrench10.png" ,"wrench11.png" ,"wrench12.png"};
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
