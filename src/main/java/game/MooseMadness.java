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
    public long usedTime;   //time taken per gameLoop step
    public BufferStrategy strategy;     //double buffering strategy

    private Player player;
    private GamePanels gamePanels;

    private PowerUpManager powerUpManager;
    private ObstacleManager obstacleManager;
    private TrafficManager trafficManager;

//    private InputHandler keyPressedHandler;
//    private InputHandler keyReleasedHandler;

    private BufferedImage background, backgroundTile; //background cache
    private int backgroundY; //background cache position

    /**
     *
     */
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

    /**
     *
     */
    public void initWorld() {
        //add powerup, obstacle, and traffic managers and lists
        powerUpManager = new PowerUpManager(this);
        powerups = new ArrayList<Actor>();
        obstacleManager = new ObstacleManager(this);
        obstacles = new ArrayList<Actor>();
        trafficManager = new TrafficManager(this);
        motorists = new ArrayList<Actor>();

        //add player and input handlers
        player = new Player(this);
//        keyPressedHandler = new InputHandler(this, player);
//        keyPressedHandler.action = InputHandler.Action.PRESS;
//        keyReleasedHandler = new InputHandler(this, player);
//        keyReleasedHandler.action = InputHandler.Action.RELEASE;

        gameOver = false;

        //load background
        backgroundTile = ResourceLoader.getInstance().getSprite("road0.gif");
        background = ResourceLoader.createCompatible(WIDTH, HEIGHT + backgroundTile.getHeight(), Transparency.OPAQUE);
        Graphics2D g = (Graphics2D) background.getGraphics();
        g.setPaint(new TexturePaint(backgroundTile, new Rectangle(0, 0, backgroundTile.getWidth(), backgroundTile.getHeight())));
        g.fillRect(0, 0, background.getWidth(), background.getHeight());
        backgroundY = backgroundTile.getHeight();
    }


    /**
     * main game loop
     */
    public void gameLoop() {
//        loopMusic("music.wav");
        usedTime = 0;

        while (isVisible()) {
            if (super.gameOver) {
                gamePanels.printGameOver();
                break;
            }

            updateWorld();
            paintWorld();
            calculateSleepTime();
        }
    }

//    public void paint(Graphics g) {
//    }


    /**
     *
     */
    public void updateWorld() {
        //iterate through current obstacle list, check for collisions, remove unnecessary obstacles, update obstacle position based on player speed.
        int i = 0;
        while (i < obstacles.size()) {
            Actor obstacle = obstacles.get(i);
            checkCollision(obstacle);

            if (obstacle.isMarkedForRemoval()) {
                player.updateScore(obstacle.getPointValue());
                obstacles.remove(i);
            } else {
                obstacle.setPosY(obstacle.getPosY() + player.getSpeed() / 10);
                obstacle.update();
                i++;
            }
        }

        //if game over
        if (player.getHealth() <= 0) {
            super.gameOver = true;
        }

        //check for player collisions
        checkCollision(player);
        player.update();

        //ask managers if objects should be added to lists
        obstacleManager.randomMoose(obstacles);
    }


    /**
     *
     */
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
        for (Actor obstacle : obstacles) {
            obstacle.paint(g);
        }

        player.paint(g);

//        if (player.iseBrake()) {
//            g.drawImage(ResourceLoader.getInstance().getSprite("skidmarks.png"), player.getPosX(), player.getPosY(), this);
//        }

        backgroundY -= player.getSpeed() / 10;
        if (backgroundY < 0) {
            backgroundY = backgroundTile.getHeight();
        }

        gamePanels.printHealth();
        gamePanels.printScore();
        gamePanels.printSpeed();

        //swap buffer
        strategy.show();
    }


    /**
     *
     * @param actor
     */
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

    /**
     *
     * @param name
     */
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

    /**
     *
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        InputHandler inputHandler = new InputHandler(this, player);
        inputHandler.event = e;
        inputHandler.action = InputHandler.Action.PRESS;
        inputHandler.start();
//        keyPressedHandler.handleInput(e);
    }

    /**
     *
     * @param e
     */
    public void keyReleased(KeyEvent e) {
        InputHandler inputHandler = new InputHandler(this, player);
        inputHandler.event = e;
        inputHandler.action = InputHandler.Action.RELSEASE;
        inputHandler.start();
//        keyReleasedHandler.handleInput(e);
    }

    /**
     *
     * @param e
     */
    public void keyTyped(KeyEvent e) {
    }
}