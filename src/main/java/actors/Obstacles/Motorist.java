package actors.Obstacles;

import actors.Actor;
import actors.Player;
import game.Stage;
import game.Utils;

/**
 *
 */
public class Motorist extends Actor {
    private int pointValue; //points added to score when motorist clears screen vertically
    private int damageValue; //amount of damage motorist does to player upon initial collision
    private boolean inAccident; //boolean to flag as having been in an accident or not
    private int[] roadBounds = {142, 768}; //left and right most lane boundaries with grass
    private int randomCarNumber; //the car graphic assigned to the motorist

    /**
     * Motorist constructor. Assigns a random sprite to the motorist and sets width, height, pointValue, and damageValue.
     *
     * @param canvas
     */
    public Motorist(Stage canvas) {
        super(canvas);
        randomCarNumber = Utils.randInt(0, 7);
        sprites = new String[]{"cars/car" + randomCarNumber + ".png"};
        width = 50;
        height = 100;
        pointValue = 50;
        damageValue = 5;
    }

    /**
     * Update method. Only needs to use updateSpeed as there are no animations.
     */
    public void update() {
        updateSpeed();
    }

    /**
     * Motorist speed updating. Altars vx, vy,
     */
    private void updateSpeed() {
        //if in accident, slow car to a stop
        if (inAccident && vy < 0) vy += Utils.randFloat(0.008f, 0.01f);
        if (vy < 0 && vy > -0.5f) vy = 0;

        //if not in accident, slowly stop veering
        if (!inAccident && vx != 0) {
            if (vx > 0) vx -= 0.02;
            else vx += 0.02;
            if (vx > -0.2 && vx < 0.2) vx = 0;
        }

        //remove car if it passes top or bottom of screen
        if (posY > stage.getHeight() || posY + height < 0 - stage.getHeight() / 2) {
            setMarkedForRemoval(true);
        }

        //if motorist enters grassy sides and has not been in an accident, altar posX to simulate rough terrain
        if (posX + width / 1.5 <= roadBounds[0] || posX + width / 1.5 > roadBounds[1]) {
            if (!isInAccident()) {
                if (vy < -0.5) posX += Utils.randInt(-1, 1);
            }
        }

        //if motorist hits horizontal bounds, mark in accident and reverse and dampen vx (rebound car)
        if (posX < 0 || posX + width >= stage.getWidth()) {
            this.setInAccident(true);
            vx = -vx / 3;
        }

        //keep motorist position from going beyond 0 and width
        if (posX < 0) posX = 0;
        if (posX + width >= stage.getWidth()) posX = stage.getWidth() - width;

        if (vy < -0.5) posX += vx; // only apply vx to posX if motorist has minimum forward momentum
        posY += vy; //add speed to position
    }

    /**
     * Motorist collison method. How the motorist interacts with other actors is determined here.
     *
     * @param a
     */
    public void collision(Actor a) {
        if (a instanceof Motorist) { //if hitting another motorist
            Motorist them = (Motorist) a;

            if (!them.isInAccident()) { //mark both motorists as have been in an accident
                them.setInAccident(true);
                this.setInAccident(true);
            }

            //determine the location of the other motorist upon collision
            if (posY > them.getPosY() + them.getHeight() - 5) { //if they are in front match their vy, randomize vx, and prevent clipping
                vy = them.getVy();
                posY = them.getPosY() + them.getHeight() + 10;
                vx += Utils.randFloat(-1, 1);
            } else if (posY + height < them.getPosY() + 5) { //if they are behind set their vy to ours, their vx randomly, and prevent clipping
                them.setVy(vy);
                them.setPosY(posY + height + 10);
                them.vx += Utils.randFloat(-1, 1);

            } else if (posY < them.getPosY() + them.getHeight() && posY + height > them.getPosY()) { //if side-swiping, modify their vx with ours
                them.setVx(them.getVx() + vx);
                if (posX < them.getPosX()) { //if they are to the right, prevent clipping
                    posX = them.getPosX() - width - 10;
                } else if (posX > a.getPosX()) { //if they are to the left, prevent clipping
                    posX = them.getPosX() + them.getWidth() + 10;
                }
            }
        }

        if (a instanceof Moose) { //if hitting a moose
            Moose moose = (Moose) a;

            // only worry about hitting "live" moose, set as being in an accident, and apply random vx
            if (!moose.isHit() && !this.inAccident) {
                moose.setHit(true);
                this.setInAccident(true);
                vx = Utils.randFloat(-1f, 1f);
            }
        }

        if (a instanceof Player) { //if hit by a player, mark as being in an accident
            this.setInAccident(true);
        }

        if (a instanceof Pothole) { //if hitting a pothole and not in an accident , randomly altar vx
            if (!inAccident) vx += Utils.randFloat(-0.4f, 0.4f);
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
    public boolean isInAccident() {
        return inAccident;
    }

    /**
     * When motorist is marked as have been in an accident, change graphic to broken car and play a random crash sound.
     * @param inAccident
     */
    public void setInAccident(boolean inAccident) {
        if (this.isInAccident() == false) {
            playSound("sounds/crash" + Utils.randInt(0, 9) + ".wav");
            sprites[0] = "cars/car" + randomCarNumber + "b.png";
        }
        this.inAccident = inAccident;
    }
}
