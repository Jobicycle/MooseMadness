package actors;

import actors.Obstacles.Moose;
import actors.Obstacles.Motorist;
import game.Stage;

import java.awt.event.KeyEvent;

public class Player extends Actor implements KeyboardControllable {

    private boolean up, down, left, right, eBrake;
    private int health = 100;
    private float speed = 5f;
    private int score = 0;

    //handling specs
    private float topSpeed;
    private float acceleration;
    private float handling;
    private float maxHandling;
    private float eBrakePower;

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
        if (speed < 0) speed = 0;
        if (speed > topSpeed) speed = topSpeed;


        //if no longer steering, gradually reduce car drifting
        if (!left && !right && vx != 0) {
            if (vx > 0) vx -= handling * 2;
            else if (vx < 0) vx += handling * 2;
        }

        //limit handling to maxHandling bounds and set to 0 when between handling and 0
        if (vx > maxHandling) vx = maxHandling;
        else if (vx < -maxHandling) vx = -maxHandling;
        else if (vx > 0 && vx < handling || vx < 0 && vx > -handling) vx = 0;

        posX += vx;
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
        //if player hits a moose, reduce health and speed
        if (a instanceof Moose && !((Moose) a).isHit()) {
            health -= ((Moose) a).getDamageValue();
            speed -= ((Moose) a).getWeight();
        }

        //if player hits a motorist
        if (a instanceof Motorist) {
            if (posY < a.getPosY()) { //if motorist behind me
                a.setVy(a.getVy() + 0.1f);
            } else { //if motorist in front of me
                a.setVy(a.getVy() - 0.1f);
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
