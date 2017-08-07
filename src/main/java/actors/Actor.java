package actors;

import game.ResourceLoader;
import game.Stage;

import java.awt.*;


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
    protected int actorSpeed;
    protected int time;
    protected String[] sprites = null;
    protected Stage stage = null;
    private boolean markedForRemoval = false;

    public Actor(Stage canvas) {
        this.stage = canvas;
        frame = 0;
        frameSpeed = 1;
        actorSpeed = 10;
        time = 0;
    }

    public void update() {
        update();
    }

    public void updateSpriteAnimation() {
        time++;

        if (time % frameSpeed == 0) {
            time = 0;
            frame = (frame + 1) % sprites.length;
        }
    }

    public void playSound(final String name) {
        new Thread(new Runnable() {
            public void run() {
                ResourceLoader.getInstance().getSound(name).play();
            }
        }).start();
    }


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
}
