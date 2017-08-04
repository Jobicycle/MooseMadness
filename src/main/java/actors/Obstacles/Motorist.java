package actors.Obstacles;

import actors.Actor;
import actors.Player;
import game.Stage;
import game.Utils;

public class Motorist extends Actor {
    private int pointValue = 0;
    private int damageValue = 5; //how much damage is done
    private int maxSteerVelocity = 1;
    private int steeringResponsiveness = 1;
    private boolean hit = false;
    private int[] laneXs = {115, 270, 430, 585};

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
        int currentLane = getCurrentLane();

        //randomly altar the vx to create steering
        if (Utils.randInt(1, 1000) >= 990) {
            setVx(getVx() + Utils.randInt(-steeringResponsiveness, steeringResponsiveness));
        }

        //randomly stop veering
//        if (vx != 0 && Utils.randInt(1, 1000) <= 5) {
//            setVx(0);
//        }

        //keep the maxSteerVelocity within range
        if (vx > maxSteerVelocity) {
            vx = maxSteerVelocity;
        } else if (vx < -maxSteerVelocity) {
            vx = -maxSteerVelocity;
        }

        //only adjust the X pos at random intervals
        if (Utils.randInt(1, 2) == 1) {
            posX += vx;
        }

        posY += vy;

        if (posX <= laneXs[currentLane]) {
            posX = laneXs[currentLane];
            vx = -vx;
        } else if (posX + this.width >= laneXs[currentLane + 1]) {
            posX = laneXs[currentLane + 1] - width;
            vx = -vx;
        }

        //if car position is 5 screens above or below main screen, remove them from game.
        if (posY > stage.getHeight() * 5 || posY < stage.getHeight() * -5) {
            setMarkedForRemoval(true);
        }
    }

    private int getCurrentLane() {
        if (this.getPosX() >= laneXs[0] && this.getPosX() < laneXs[1]) {
            return 0;
        } else if (this.getPosX() >= laneXs[1] && this.getPosX() < laneXs[2]) {
            return 1;
        } else if (this.getPosX() >= laneXs[2] && this.getPosX() < laneXs[3]) {
            return 2;
        }

        return -1;
    }

    public void collision(Actor a) {
        //if motorist hits player, mark as hit, reduce point value and change sprite
        if (a instanceof Player) {
            if (this.getPosY() < a.getPosY()) { //if player is behind

            }
        }

        if (a instanceof Motorist) {
            if (this.getPosY() > a.getPosY()) {
                this.setVy(a.getVy());
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

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
}
