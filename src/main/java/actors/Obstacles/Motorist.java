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
        sprites = new String[]{"cars/car" + randomCarNumber + ".png"};
        frameSpeed = 1000;
        width = 54;
        height = 102;
    }

    public void update() {
//        super.updateSpriteAnimation();
        updateSpeed();
    }

    private void updateSpeed() {
        if (vy != 0) posX += vx;
        posY += vy;

        //if inAccident, slow car to a stop
        if (inAccident && vy < 0) vy += Utils.randFloat(0.01f, 0.1f);
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
        if (posX < 0 || posX + width >= stage.getWidth()) {
            this.setInAccident(true);
            vx = -vx / 2;
        }
        if (posX < 0) posX = 0;
        if (posX + width >= stage.getWidth()) posX = stage.getWidth() - width;
    }


    public void collision(Actor a) {
        if (a instanceof Motorist) {
            Motorist them = (Motorist) a;

            if (!them.isInAccident()) {
                them.setInAccident(true);
                this.setInAccident(true);
            }

            if (posY > them.getPosY() + them.getHeight() - 5) { //if they are in front
                vy = them.getVy();
                posY = them.getPosY() + them.getHeight();
                vx += Utils.randFloat(-1, 1);
            } else if (posY + height < them.getPosY() + 5) { //they are behind
                them.setVy(vy);
                them.setPosY(posY + height);
                them.vx += Utils.randFloat(-1, 1);

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

            if (!moose.isHit()) {
                this.setInAccident(true);
                vx = Utils.randFloat(-2, 2);
            }
        }

        if (a instanceof Player) {
            this.setInAccident(true);
        }

        if (a instanceof Pothole) {
            vx += Utils.randFloat(-0.3f, 0.3f);
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
        if (this.isInAccident() == false) {
            playSound("sounds/crash" + Utils.randInt(0, 9) + ".wav");
            sprites[0] = "cars/car" + randomCarNumber + "b.png";
        }
        this.inAccident = inAccident;
    }
}
