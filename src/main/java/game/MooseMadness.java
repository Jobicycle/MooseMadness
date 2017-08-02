package game;

import actors.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MooseMadness extends Stage implements KeyListener {

    private static final long serialVersionUID = 1L;

    private Player player;
    private GamePanels gamePanels;
    private ObstacleManager obstacleManager;

    private InputHandler keyPressedHandler;
    private InputHandler keyReleasedHandler;

    public long usedTime;   //time taken per gameLoop step
    public BufferStrategy strategy;     //double buffering strategy

    private BufferedImage background, backgroundTile; //background cache
    private int backgroundY; //background cache position

    public MooseMadness() {
        //init the UI
        setBounds(0, 0, Stage.WIDTH, Stage.HEIGHT);
        setBackground(Color.BLACK);

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(Stage.WIDTH, Stage.HEIGHT));
        panel.setLayout(null);

        panel.add(this);

        JFrame frame = new JFrame("Moose Madness");
        frame.add(panel);

        frame.setBounds(0, 0, Stage.WIDTH, Stage.HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);

        //cleanup resources on exit
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                ResourceLoader.getInstance().cleanup();
                System.exit(0);
            }
        });


        addKeyListener(this);

        //create a double buffer
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        requestFocus();
        initWorld();
    }

    public void initWorld() {
//        actors = new ArrayList<Actor>();
        powerups = new ArrayList<Actor>();
        obstacles = new ArrayList<Actor>();
        obstacleManager = new ObstacleManager(this);
        gameOver = false;

        //add a player
        player = new Player(this);
        keyPressedHandler = new InputHandler(this, player);
        keyPressedHandler.action = InputHandler.Action.PRESS;
        keyReleasedHandler = new InputHandler(this, player);
        keyReleasedHandler.action = InputHandler.Action.RELEASE;
    }


    /**
     * main game loop
     */
    public void gameLoop() {
//        loopMusic("music.wav");
        usedTime = 0;

        while (isVisible()) {
            updateWorld();
            paintWorld();
            calculateSleepTime();
        }
    }

//    public void paint(Graphics g) {
//    }


    public void updateWorld() {
        //TODO update printScore
        //TODO update/remove obstacles

        int i = 0;

        while (i < obstacles.size()) {
            Actor obstacle = obstacles.get(i);
            checkCollision(obstacle);

            if (obstacle.isMarkedForRemoval()) {
                player.updateScore(obstacle.getPointValue());
                obstacles.remove(i);
            } else {
                obstacle.setVy(player.getSpeed() / 10);
                obstacle.update();
                i++;
            }
        }

        //TODO determine if game over
        //if(player.getHealth() <= 0)
        //super.gameOver = true;

        checkCollision(player);
        player.update();
        obstacleManager.spawnMoose(obstacles);
    }


    public void paintWorld() {
        //get the graphics from the buffer
        Graphics g = strategy.getDrawGraphics();
        gamePanels = new GamePanels(player, g, strategy);

        //init image to background
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        //load subimage from the background
        g.drawImage(background, 0, 0, Stage.WIDTH, Stage.HEIGHT, 0, backgroundY, Stage.WIDTH, backgroundY + Stage.HEIGHT, this);

        //paint the obstacles
        for (Actor obstacle: obstacles) {
            obstacle.paint(g);
        }

        player.paint(g);
        gamePanels.printScore();
        gamePanels.printSpeed();

        //swap buffer
        strategy.show();
    }


    private void checkCollision(Actor actor) {
        Rectangle actorBounds = actor.getBounds();

        for (int i = 0; i < obstacles.size(); i++) {
            Actor otherActor = obstacles.get(i);

            if (otherActor == null || actor.equals(otherActor)) {
                continue;
            }

            if (actorBounds.intersects(otherActor.getBounds())) {
                actor.collision(otherActor);
                otherActor.collision(actor);
            }
        }
    }

    public void loopMusic(final String name) {
        new Thread(new Runnable() {
            public void run() {
                ResourceLoader.getInstance().getSound(name).loop();
            }
        }).start();
    }


    /**
     * keep the gameLoop running at a desired fps
     */
    public void calculateSleepTime() {
        long startTime = System.currentTimeMillis();
        usedTime = System.currentTimeMillis() - startTime;

        //calculate sleep time
        if (usedTime == 0) {
            usedTime = 1;
        }

        int timeDiff = (int) ((1000 / usedTime) - DESIRED_FPS);

        if (timeDiff > 0) {
            try {
                Thread.sleep(timeDiff / 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        keyPressedHandler.handleInput(e);
    }

    public void keyReleased(KeyEvent e) {
        keyReleasedHandler.handleInput(e);
    }

    public void keyTyped(KeyEvent e) {
    }
}