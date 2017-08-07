package actors;

import actors.Obstacles.Moose;
import actors.Obstacles.Motorist;
import game.Stage;
import game.Utils;

import java.awt.event.KeyEvent;

public class Player extends Actor implements KeyboardControllable {

    private boolean up, down, left, right, eBrake;
    private int health = 100;
    private float speed = 5f;
    private int score = 0;
    private int[] roadBounds = {110, 590};

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

        sprites = new String[]{"motorist0.png"};
        frame = 0;
        frameSpeed = 35;
        actorSpeed = 10;

        width = 65;
        height = 146;
        posX = Stage.WIDTH / 2 - width / 2;
        posY = (int) (Stage.HEIGHT * 0.50);

        topSpeed = 10f;
        acceleration = 0.04f;
        handling = 0.2f;
        maxHandling = 5f;
        eBrakePower = 0.3f;

        up = down = left = right = eBrake = false;
    }

    /**
     *
     */
    public void update() {
        super.updateSpriteAnimation();
        updateSpeed();

        if (speed > 0) {
            if (posY > (int) (Stage.HEIGHT * 0.50)) posY -= 1;
            else if (posY < (int) (Stage.HEIGHT * 0.50)) posY += 1;
        }
    }


    /**
     *
     */
    private void updateSpeed() {
        if (up && speed < topSpeed && !offRoad) //up arrow to increase speed, which affects the y position of everything on screen relative to player
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
        if (speed < 0) speed = 0;
        if (speed > topSpeed) speed = topSpeed;


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
            if (speed >= 6) speed -= 0.1;
            if (Utils.randInt(0, 1) == 1) posX += 2;
            else posX -= 2;
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
        }
    }

    public void collision(Actor a) {
        //if player hits a moose
        if (a instanceof Moose && !((Moose) a).isHit()) {
            health -= ((Moose) a).getDamageValue();
            speed -= ((Moose) a).getWeight();
        }


//        if player hits a motorist
        if (a instanceof Motorist && !((Motorist) a).isHit()) {
            health -= ((Motorist) a).getDamageValue();

            if (posY > a.getPosY() + a.getHeight() - speed) { //if motorist in front
                posY = a.getPosY() + a.getHeight() + 10;
                speed -= 2;
            } else if (posY + height < a.getPosY() + speed) { //if motorist behind
                posY = a.getPosY() - height - 10;
                speed += 2;
                a.setVy(a.getVy() + 1f);
            } else if (posY < a.getPosY() + a.getHeight() && posY + height > a.getPosY()) { //if sideswipe
                vx = -vx / 1.5f;
                if (posX < a.getPosX()) { //if motorist to the right
                    posX = a.getPosX() - width;
                    a.setVx(a.getVx() + 1f);
                } else if (posX > a.getPosX()) { //if motorist to the left
                    posX = a.getPosX() + a.getWidth();
                    a.setVx(a.getVx() - 1f);
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

    public boolean iseBrake() {
        return eBrake;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
