package actors;

import game.Stage;
import game.Utils;

import java.awt.event.KeyEvent;

public class Player extends Actor implements KeyboardControllable {

    private boolean up, down, left, right, eBreak;
    private int score = 0;
    private int speed = 0;

    public Player(Stage stage) {
        super(stage);

        sprites = new String[]{"player.png"};
        frame = 0;
        frameSpeed = 35;
        actorSpeed = 10;
        width = 66;
        height = 128;
        posX = Stage.WIDTH / 2 - width / 2;
        posY = Stage.HEIGHT / 2 - height / 2;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void update() {
        super.updateSpriteAnimation();
        updateSpeed();
    }

    protected void updateSpeed() {
        int steerVelocity = 1;
        int maxSteerVelocity = 50;
        int maxSpeed = 120;
        int steerDampener = 2;

        if (posX <= 0) {
            vx = -vx / steerDampener;
            posX = (int) (width * Utils.randDouble(0, 0.1));
        } else if (posX + width >= Stage.WIDTH) {
            vx = -vx / steerDampener;
            posX = (int) (Stage.WIDTH - width * Utils.randDouble(1, 1.1));
        }

        if (up) {
            speed++;
        }
        if (down) {
            speed--;
        }
        if (left) {
            vx -= steerVelocity;
        }
        if (right) {
            vx += steerVelocity;
        }
        if (eBreak) {
            speed -= 2;
            vx *= 0.99;
        }

        //if not steering
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
        if (vx > maxSteerVelocity) {
            vx = maxSteerVelocity;
        } else if (vx < -maxSteerVelocity) {
            vx = -maxSteerVelocity;
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
                eBreak = false;
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
            //TODO powerup projectile logic
            case KeyEvent.VK_SPACE:
                eBreak = true;
                up = false;
                right = false;
                left = false;
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
                eBreak = false;
                break;
        }
    }

    public void collision(Actor a) {
        stage.endGame();
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
}
