package actors;

import actors.Obstacles.Moose;
import actors.Obstacles.Motorist;
import actors.Obstacles.Pothole;
import actors.PowerUps.AirHorn;
import actors.PowerUps.Wrench;
import game.Stage;
import game.Utils;

import java.awt.event.KeyEvent;

/**
 * Player class. Contains all the keybindings and behaviors of hte player car.
 */
public class Player extends Actor implements KeyboardControllable {

    private boolean up, down, left, right, eBrake, activateHorn; //booleans for different actions
    private int health = 100; //starting health
    private float speed = 4f; //max speed
    private int score = 0; //starting score
    private int numberOfHorns = 0; //number of horn powerups
    private int[] roadBounds = {142, 768}; //coordinates of the left and right most grassy boundaries.

    //handling specs
    private float topSpeed;
    private float acceleration;
    private float handling;
    private float maxHandling;
    private float eBrakePower;
    private boolean offRoad;

    /**
     * Player constructor
     *
     * @param stage
     */
    public Player(Stage stage) {
        super(stage);

        sprites = new String[]{"cars/car0.png"};
        frame = 0;
        frameSpeed = 1000;

        width = 54;
        height = 102;

        posX = Stage.WIDTH / 2 - width / 2;
        posY = (int) (Stage.HEIGHT * 0.50);

        topSpeed = 5f;
        acceleration = 0.04f;
        handling = 0.1f;
        maxHandling = 5f;
        eBrakePower = 0.3f;

        up = down = left = right = eBrake = activateHorn = false;
    }

    /**
     * Player updater
     */
    public void update() {
        super.updateSpriteAnimation();
        updateSpeed();

        // reduce score if driving on the sides
        if (offRoad || speed < topSpeed / 2) {
            score -= 1;
        }

        if (health <= 10) sprites[0] = "cars/pcar5.png";
        else if (health <= 30) sprites[0] = "cars/pcar4.png";
        else if (health <= 50) sprites[0] = "cars/pcar3.png";
        else if (health <= 70) sprites[0] = "cars/pcar2.png";
        else if (health <= 90) sprites[0] = "cars/pcar1.png";
    }


    /**
     * Playeer speed updater. Handles speed and steering
     */
    private void updateSpeed() {
        if (up && speed < topSpeed) //up arrow to increase speed, which affects the y position of everything on screen relative to player
            speed += acceleration;

        if (speed >= 0) { //allow for steering when moving
            if (left) vx -= handling;
            if (right) vx += handling;
        }

        if (speed > 0) { //allow for braking/eBraking when moving
            if (down) speed -= acceleration;
            if (eBrake) speed -= eBrakePower;
        }

        //if no longer steering, gradually reduce car drifting
        if (!left && !right && vx != 0) {
            if (vx > 0) vx -= handling * 1.5;
            else if (vx < 0) vx += handling * 1.5;
        }

        //limit handling to maxHandling bounds and set to 0 when between handling and 0
        if (vx > maxHandling) vx = maxHandling;
        else if (vx < -maxHandling) vx = -maxHandling;
        else if (vx > 0 && vx < handling || vx < 0 && vx > -handling) vx = 0;

        //limit car speed and randomly altar posX to simulate rough terrain
        if (posX + width / 1.5 <= roadBounds[0] || posX + width / 1.5 > roadBounds[1]) {
            offRoad = true;
            if (speed >= topSpeed / 2) speed -= 0.1;
            if (speed > 0) {
                if (Utils.randInt(0, 1) == 1) posX += 2;
                else posX -= 2;
            }
        } else {
            offRoad = false;
        }

        // keep player from going out of y bounds
        if (speed > 0) {
            if (posY > (int) (Stage.HEIGHT * 0.50)) posY -= 1;
            else if (posY < (int) (Stage.HEIGHT * 0.50)) posY += 1;
        }

        //prevent player from steering out of x bounds
        if (posX < 0 || posX + width >= stage.getWidth()) vx = -vx;
        if (posX < 0) posX = 0;
        if (posX + width >= stage.getWidth()) posX = stage.getWidth() - width;

        //reduce steering effectiveness if off road, otherwise apply normal steering
        if (offRoad) posX += vx / 2;
        else posX += vx;
    }


    /**
     * key press triggers. Activates booleans according to which key was pressed.
     *
     * @param e
     */
    public void triggerKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                up = true;
                down = false;
                eBrake = false;
                break;

            case KeyEvent.VK_DOWN:
                down = true;
                up = false;
                break;

            case KeyEvent.VK_LEFT:
                left = true;
                right = false;
                break;

            case KeyEvent.VK_RIGHT:
                right = true;
                left = false;
                break;

            case KeyEvent.VK_SPACE:
                if (!eBrake) {
                    eBrake = true;
                }
                break;
            case KeyEvent.VK_CONTROL:
                if (numberOfHorns > 0) {
                    activateHorn = true;
                }
                break;
        }
    }

    /**
     * key release triggers. Similar to the key pressed triggers but sets booleans to false.
     *
     * @param e
     */
    public void triggerKeyRelease(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                up = false;
                break;
            case KeyEvent.VK_DOWN:
                down = false;
                break;
            case KeyEvent.VK_LEFT:
                left = false;
                break;
            case KeyEvent.VK_RIGHT:
                right = false;
                break;
            case KeyEvent.VK_SPACE:
                eBrake = false;
                break;
            case KeyEvent.VK_CONTROL:
                activateHorn = false;
                break;
        }
    }

    /**
     * Player collision logic. Handles picking up powerups, hitting obstacles, and other motorists.
     *
     * @param a
     */
    public void collision(Actor a) {
        //if hitting a wrench, collect it and increase health.
        if (a instanceof Wrench) {
            Wrench wrench = (Wrench) a;
            health += wrench.getHealingValue();
            if (health > 100) health = 100;
            score += wrench.getPointValue();
            wrench.setMarkedForRemoval(true);
            playSound("sounds/wrench.wav");
        }

        //collect airhorn powerups. Max of 1. If player already has 1, collect points.
        if (a instanceof AirHorn) {
            AirHorn airHorn = (AirHorn) a;
            if (numberOfHorns == 0) numberOfHorns = 1;
            else if (numberOfHorns == 1) score += airHorn.getPointValue();
            airHorn.setMarkedForRemoval(true);
            playSound("sounds/hornpickup.wav");
        }

        //if player hits a moose
        if (a instanceof Moose) {
            Moose moose = (Moose) a;

            if (!moose.isHit()) { //if moose is not already hit, flag hit, reduce points to 0, play random crash, lose health/speed and veer.
                moose.setHit(true);
                moose.setPointValue(0);
                playSound("sounds/crash" + Utils.randInt(0, 9) + ".wav");
                health -= moose.getDamageValue();
                speed *= 0.9;
                if (posX + width / 2 > a.getPosX() + a.getWidth() / 2) vx += Utils.randFloat(3, 6);
                else vx -= Utils.randFloat(3, 6);
            }
        }

        //if player hits pothole
        if (a instanceof Pothole) {
            Pothole pothole = (Pothole) a;

            if (!pothole.isHit()) { //if pothole not already hit, mark as hit, set points to 0, make random pothole sound, and lose health
                pothole.setHit(true);
                pothole.setPointValue(0);
                playSound("sounds/pothole" + Utils.randInt(0, 6) + ".wav");
                health -= pothole.getDamageValue();
            }
        }

        //if player hits a motorist
        if (a instanceof Motorist) {
            Motorist motorist = (Motorist) a;

            if (!motorist.isInAccident()) { //if motorist not already in accident, mark them as in accident, set point value to 0, random crash sound, lose health
                motorist.setInAccident(true);
                motorist.setPointValue(0);
                playSound("sounds/crash" + Utils.randInt(0, 9) + ".wav");
                health -= motorist.getDamageValue();
            }

            if (posY > motorist.getPosY() + motorist.getHeight() - speed) { //if motorist in front
                posY = motorist.getPosY() + motorist.getHeight();
                speed -= 2;
                if (posX > motorist.getPosX()) motorist.setVx(Utils.randFloat(-1f, -0.2f));
                else if (posX < motorist.getPosX()) motorist.setVx(Utils.randFloat(0.2f, 1f));

            } else if (posY + height < motorist.getPosY() + speed) { //if motorist behind
                posY = motorist.getPosY() - height;
                motorist.setVy(-speed + 2);
                if (posX > motorist.getPosX()) motorist.setVx(Utils.randFloat(-1f, -0.2f));
                else if (posX < motorist.getPosX()) motorist.setVx(Utils.randFloat(0.2f, 1f));

            } else if (posY < motorist.getPosY() + motorist.getHeight() && posY + height > motorist.getPosY()) { //if sideswipe
                vx = -vx / 1.5f;
                if (posX < motorist.getPosX()) { //if motorist to the right
                    posX = motorist.getPosX() - width;
                    motorist.setVx(motorist.getVx() + 1f);

                } else if (posX > a.getPosX()) { //if motorist to the left
                    posX = motorist.getPosX() + motorist.getWidth();
                    motorist.setVx(motorist.getVx() - 1f);
                }
            }
        }
    }

    public void updateScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public int getHealth() {
        return health;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getTopSpeed() {
        return topSpeed;
    }

    public boolean isActivateHorn() {
        return activateHorn;
    }

    public int getNumberOfHorns() {
        return numberOfHorns;
    }

    public void setNumberOfHorns(int numberOfHorns) {
        this.numberOfHorns = numberOfHorns;
    }
}
