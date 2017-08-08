package actors.Obstacles;

import actors.Actor;
import actors.Player;
import game.Stage;
import game.Utils;

public class Motorist extends Actor {
    private int pointValue = 50;
    private int damageValue = 5; //how much damage is done
    private boolean inAccident;
    private int[] roadBounds = {142, 768};
    private int randomCarNumber;

    public Motorist(Stage canvas) {
        super(canvas);
        randomCarNumber = Utils.randInt(0, 3);
        sprites = new String[]{"car" + randomCarNumber + ".png"};
        frameSpeed = 1;
//        width = 60;
//        height = 114;
        width = 54;
        height = 102;
    }

    public void update() {
        super.updateSpriteAnimation();
        updateSpeed();
    }

    private void updateSpeed() {
        if (vy != 0) posX += vx;
        posY += vy;

        //if inAccident, slow car to a stop
        if (inAccident && vy < 0) vy += 0.02f;
        if (vy > 0 && vy < 0.1f) vy = 0;

        //remove car if it passes top or bottom of screen
        if (posY > stage.getHeight() || posY + height < 0) {
            setMarkedForRemoval(true);
        }

        //randomly altar posX to simulate rough terrain
        if (posX + width / 1.5 <= roadBounds[0] || posX + width / 1.5 > roadBounds[1]) {
            if (vy < 0) posX += Utils.randInt(-1, 1);
        }

        //prevent car from steering out of bounds
        if (posX < 0 || posX + width >= stage.getWidth()) vx = -vx / 2;
        if (posX < 0) posX = 0;
        if (posX + width >= stage.getWidth()) posX = stage.getWidth() - width;
    }

    public void collision(Actor a) {
        if (a instanceof Motorist) {
            Motorist them = (Motorist) a;
            sprites[0] = "car" + randomCarNumber + "b.png";

            if (!them.isInAccident()) {
                them.setInAccident(true);
                this.setInAccident(true);
                playSound("crash" + Utils.randInt(0,2) + ".wav");
            }

            if (posY > them.getPosY() + them.getHeight() + vy) { //if they are in front
                vy = them.getVy();
                posY = them.getPosY() + them.getHeight();
                vx += Utils.randFloat(-1, 1);

            } else if (posY < them.getPosY() + them.getHeight() && posY + height > them.getPosY()) {
                if (posX < them.getPosX()) { //if motorist to the right
                    posX = them.getPosX() - width;
                } else if (posX > a.getPosX()) { //if motorist to the left
                    posX = them.getPosX() + them.getWidth();
                }
            }
        }

        if (a instanceof Moose) {
            Moose moose = (Moose) a;
            sprites[0] = "car" + randomCarNumber + "b.png";

            if (!moose.isHit()) {
                this.setInAccident(true);
                vx = Utils.randFloat(-2, 2);
            }
        }

        if (a instanceof Player) {
            sprites[0] = "car" + randomCarNumber + "b.png";
        }

        if (a instanceof Pothole) {
            vx += Utils.randFloat(-1, 1);
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

    public boolean isInAccident() {
        return inAccident;
    }

    public void setInAccident(boolean inAccident) {
        this.inAccident = inAccident;
    }
}
