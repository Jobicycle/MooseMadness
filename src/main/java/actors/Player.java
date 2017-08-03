package actors;

import actors.Obstacles.Moose;
import game.ResourceLoader;
import game.Stage;
import game.Utils;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Actor implements KeyboardControllable {

    private boolean up, down, left, right, eBrake;
    private int health = 100;
    private int speed = 0;
    private int score = 0;

    //handling specs
    private int maxSpeed = 100;
    private int steeringResponsiveness = 1;
    private int maxSteerSpeed = 20;
    private int steerCollisionDampener = 2;
    private int eBrakePower = 3;

    public Player(Stage stage) {
        super(stage);

        sprites = new String[]{"player.png"};
        frame = 0;
        frameSpeed = 35;
        actorSpeed = 10;
        width = 66;
        height = 128;
        posX = Stage.WIDTH / 2 - width / 2;
        posY = Stage.HEIGHT - height * 2;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void update() {
        super.updateSpriteAnimation();
        updateSpeed();
    }

    protected void updateSpeed() {
        //player hit corners of road, rebound and reduce steering velocity
        if (posX <= 0) {
            vx = -vx / steerCollisionDampener;
            posX = (int) (width * Utils.randDouble(0, 0.1));
        } else if (posX + width >= Stage.WIDTH) {
            vx = -vx / steerCollisionDampener;
            posX = (int) (Stage.WIDTH - width * Utils.randDouble(1, 1.1));
        }

        if (up) {
            speed++;
        }
        if (down) {
            speed--;
        }
        if (left) {
            vx -= steeringResponsiveness;
        }
        if (right) {
            vx += steeringResponsiveness;
        }
        if (eBrake) {
            speed -= eBrakePower;
            vx *= 0.90;
        }

        //if not steering, reduce xVelocity
        if (!left && !right) {
            vx *= 0.9999999;
        }

        //limit max speed
        if (speed > maxSpeed) {
            speed = maxSpeed;
        } else if (speed < 0) {
            speed = 0;
        }

        //limit steering velocity
        if (vx > maxSteerSpeed) {
            vx = maxSteerSpeed;
        } else if (vx < -maxSteerSpeed) {
            vx = -maxSteerSpeed;
        }

        //don't allow scrolling off the edge of the screen
        if (speed != 0) {
            if (posX > 0 && vx < 0) {
                posX += vx / 4;
            } else if (posX + width < Stage.WIDTH && vx > 0) {
                posX += vx / 4;
            }

            if (posY - height > 0 && vy < 0) {
                posY += vy;
            } else if (posY + height + (height / 2) < Stage.HEIGHT && vy > 0) {
                posY += vy;
            }
        }
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
//                    up = false;
//                    right = false;
//                    left = false;
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
//        stage.endGame();
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
}
