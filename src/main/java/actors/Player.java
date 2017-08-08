package actors;

import actors.Obstacles.Moose;
import actors.Obstacles.Motorist;
import actors.Obstacles.Pothole;
import actors.PowerUps.Wrench;
import game.Stage;
import game.Utils;

import java.awt.event.KeyEvent;

public class Player extends Actor implements KeyboardControllable {

    private boolean up, down, left, right, eBrake, activateHorn;
    private int health = 100;
    private float speed = 4f;
    private int score = 0;
    private int hornPowerUp = 1;
    private int[] roadBounds = {142, 768};

    //handling specs
    private float topSpeed;
    private float acceleration;
    private float handling;
    private float maxHandling;
    private float eBrakePower;
    private boolean offRoad;

    /**
     * @param stage
     */
    public Player(Stage stage) {
        super(stage);

        sprites = new String[]{"car0.png"};
        frame = 0;
        frameSpeed = 10;
        actorSpeed = 10;

//        width = 60;
//        height = 114;
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
     *
     */
    public void update() {
        super.updateSpriteAnimation();
        updateSpeed();

        // keep player from going out of y bounds
        if (speed > 0) {
            if (posY > (int) (Stage.HEIGHT * 0.50)) posY -= 1;
            else if (posY < (int) (Stage.HEIGHT * 0.50)) posY += 1;
        }

        // reduce score if driving on the sides
        if (offRoad || speed < topSpeed / 2) {
            score -= 1;
        }
    }


    /**
     *
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

        //keep speed within bounds of 0 and topSpeed
//        if (speed < 0) speed = 0;
//        if (speed > topSpeed) speed -= 0.01f;


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

        //prevent car from steering out of bounds
        if (posX < 0 || posX + width >= stage.getWidth()) vx = -vx;
        if (posX < 0) posX = 0;
        if (posX + width >= stage.getWidth()) posX = stage.getWidth() - width;

        //reduce steering effectiveness if off road, otherwise apply normal steering
        if (offRoad) posX += vx / 2;
        else posX += vx;
    }


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
                activateHorn = true;
                break;
        }
    }

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

    public void collision(Actor a) {

        if (a instanceof Wrench) {
            Wrench wrench = (Wrench) a;
            health += wrench.getHealingValue();
            wrench.setMarkedForRemoval(true);
        }

        //if player hits a moose
        if (a instanceof Moose) {
            Moose moose = (Moose) a;

            if (!moose.isHit()) {
                health -= moose.getDamageValue();
                speed -= 1;
                if (posX > a.getPosX()) vx += 2;
                else vx -= 2;
            }
        }

        if (a instanceof Pothole) {
            Pothole pothole = (Pothole) a;

            if (!pothole.isHit()) {
                playSound("pothole.wav");
                health -= pothole.getDamageValue();
                speed -= 0.2f;
            }
        }


//        if player hits a motorist
        if (a instanceof Motorist) {
            playSound("crash" + Utils.randInt(0, 2) + ".wav");
            Motorist motorist = (Motorist) a;

            if (!motorist.isInAccident()) {
                health -= motorist.getDamageValue();
                motorist.setInAccident(true);
                motorist.setPointValue(0);
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

    //TODO tune to projectile
//    private void fire() {
//        Actor shot = new Shot(stage);
//        shot.setX(posX);
//        shot.setY(posY - shot.getHeight());
//        stage.actors.add(shot);
//        playSound("photon.wav");
//    }

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

    public void setActivateHorn(boolean activateHorn) {
        this.activateHorn = activateHorn;
    }

    public int getHornPowerUp() {
        return hornPowerUp;
    }

    public void setHornPowerUp(int hornPowerUp) {
        this.hornPowerUp = hornPowerUp;
    }
}
