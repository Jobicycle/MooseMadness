package actors;

import Game.ResourceLoader;
import Game.Stage;

import java.awt.*;


/**
 * Base class for all Game actors
 */
public class Actor {
    private static final int POINT_VALUE = 0;
    protected float vx;
    protected float vy;
    protected int posX;
    protected int posY;
    protected int height;
    protected int width;
    protected int frame;
    protected int frameSpeed;
    protected int time;
    protected String[] sprites = null;
    protected Stage stage = null;
    private boolean markedForRemoval = false;

    public Actor(Stage canvas) {
        this.stage = canvas;
        frame = 0;
        frameSpeed = 1;
        time = 0;
    }

    public void update() {
        update();
    }

    /**
     * cycle through the given images to give the appearance of animation
     */
    public void updateSpriteAnimation() {
        time++;

        if (time % frameSpeed == 0) {
            time = 0;
            frame = (frame + 1) % sprites.length;
        }
    }

    /**
     * play a wav file once
     * @param name
     */
    public void playSound(final String name) {
        new Thread(new Runnable() {
            public void run() {
                ResourceLoader.getInstance().getSound(name).play();
            }
        }).start();
    }


    /**
     * paint the actor on the screen using given sprite, x, y, and stage variables.
     * @param g
     */
    public void paint(Graphics g) {
        g.drawImage(ResourceLoader.getInstance().getSprite(sprites[frame]), posX, posY, stage);
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    protected void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    protected void setHeight(int height) {
        this.height = height;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public Rectangle getBounds() {
        return new Rectangle(posX, posY, width, height);
    }

    public void collision(Actor a) {
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public void setMarkedForRemoval(boolean markedForRemoval) {
        this.markedForRemoval = markedForRemoval;
    }

    public int getPointValue() {
        return Actor.POINT_VALUE;
    }

    public void setFrameSpeed(int frameSpeed) {
        this.frameSpeed = frameSpeed;
    }
}
