package actors.Obstacles;

import actors.Actor;
import game.Stage;

public class Moose extends Actor {

    // Point value for hitting or avoiding?
    private static final int POINT_VALUE = 50;

    public Moose(Stage stage) {
        super(stage);
        sprites = new String[]{"moose0.png"};
        frameSpeed = 100;
        width = 60;
        height = 60;
    }

    public void update() {
        super.updateSpriteAnimation();
        updateSpeed();
    }

    private void updateSpeed() {
        //moose movement logic here
        posX += getVx();
        posY += getVy();

        if (posX > stage.getWidth() || posY > stage.getHeight()) {
            setMarkedForRemoval(true);
        }
    }

    public void collision(Actor a) {
        //collision logic. If hit by car change sprite and maybe set to slow down other objects that collide
    }

    public int getPointValue() {
        return Moose.POINT_VALUE;
    }

}
