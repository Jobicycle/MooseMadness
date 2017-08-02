package actors;

import game.Stage;
import game.Utils;

import java.awt.event.KeyEvent;

public class Player extends Actor implements KeyboardControllable {

    private boolean up, down, left, right;
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
        int steerVelocity = 2;
        int maxSteerVelocity = 50;
        int maxSpeed = 200;
        int steerDampener = 1;

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

        //if not steering
        if(!left && !right) {
            vx *= 0.9999;
        }

        //limit max speed
        if (speed > maxSpeed) {
            speed = maxSpeed;
        }

        //limit steering velocity
        if (vx > maxSteerVelocity) {
            vx = maxSteerVelocity;
        } else if (vx < -maxSteerVelocity) {
            vx = -maxSteerVelocity;
        }

        //don't allow scrolling off the edge of the screen
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

    public void triggerKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                up = true;
                break;
            case KeyEvent.VK_DOWN:
                down = true;
                break;
            case KeyEvent.VK_LEFT:
                left = true;
                break;
            case KeyEvent.VK_RIGHT:
                right = true;
                break;
            //TODO powerup projectile logic
//            case KeyEvent.VK_SPACE:
//                fire();
//                break;

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
