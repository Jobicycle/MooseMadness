package game;

import actors.Actor;
import actors.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MooseMadness extends Stage implements KeyListener {
    public GameState state = GameState.MENU;
    public long usedTime; //time taken per gameLoop step
    public float sessionRunTime;
    public BufferStrategy strategy; //double buffering strategy
    public List<Actor> obstacles;
    public List<Actor> motorists;
    public List<Actor> powerups;

    private BufferedImage background, backgroundTile; //background cache
    private int backgroundY; //background cache position
    private GamePanels gamePanels;
    private Player player;
    private PowerUpManager powerUpManager;
    private ObstacleManager obstacleManager;
    private TrafficManager trafficManager;

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

        createBufferStrategy(2); //create a double buffer
        strategy = getBufferStrategy();
        requestFocus();
        player = new Player(this); //add player
        game();
    }

    /**
     *
     */
    public void initWorld() {
        //add powerup, obstacle, and traffic managers and lists
        obstacles = new ArrayList<Actor>();
        motorists = new ArrayList<Actor>();
        powerups = new ArrayList<Actor>();
        trafficManager = new TrafficManager(this, motorists);
        obstacleManager = new ObstacleManager(this, obstacles);
        powerUpManager = new PowerUpManager(this);
        player = new Player(this); //add player

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
    public void game() {
        Graphics g = strategy.getDrawGraphics();
        gamePanels = new GamePanels(player, g, strategy);

        switch (state) {
            case MENU:
                gamePanels.printMainMenu();
                break;

            case OPTIONS:
//                gamePanels.printOptions();
                break;

            case HIGHSCORES:
                gamePanels.printGameOver();
                break;

            case PAUSE:
                gamePanels.printPause();
                break;

            case GAMEOVER:
                sessionRunTime = 0;
                gamePanels.printGameOver();
                break;

            case GAME:
                usedTime = 0;

                gameLoop:
                while (isVisible()) {
                    gameUpdate();
                    paintWorld(g);
                    sessionRunTime += 0.01;
                    Utils.calculateSleepTime(usedTime, DESIRED_FPS);

                    if (state == GameState.GAMEOVER) {
                        game();
                        break gameLoop;
                    } else if (state == GameState.PAUSE) {
                        game();
                        break gameLoop;
                    }
                }
                break;
        }
    }

    private void gameUpdate() {
        //iterate through current obstacle list, check for collisions, remove unnecessary obstacles, update obstacle position based on player speed.
        updateActors(obstacles);
        updateActors(motorists);

        //check for player collisions between obstacles, motorists, and powerups
        Utils.checkCollision(player, obstacles);
        Utils.checkCollision(player, motorists);
        player.update();

        for (Actor motorist : motorists) { //check motorists collisions
            Utils.checkCollision(motorist, obstacles);
            Utils.checkCollision(motorist, motorists);
        }

        //ask managers if objects should be added to object lists
//        obstacleManager.randomMoose(sessionRunTime);
        trafficManager.randomMotorist(sessionRunTime);

        if (player.getHealth() <= 0) { //if game over
            state = GameState.GAMEOVER;
        }
    }

    /**
     * @param actorList
     */
    private void updateActors(List<Actor> actorList) {
        int i = 0;
        while (i < actorList.size()) {
            Actor actor = actorList.get(i);
            Utils.checkCollision(actor, actorList);

            if (actor.isMarkedForRemoval()) {
                player.updateScore(actor.getPointValue());
                actorList.remove(i);
            } else {
                actor.setPosY(actor.getPosY() + (int) player.getSpeed());
                actor.update();
                i++;
            }
        }
    }

    /**
     *
     */
    public void paintWorld(Graphics g) {
        //init image to background
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        //load subimage from the background
        g.drawImage(background, 0, 0, Stage.WIDTH, Stage.HEIGHT, 0, backgroundY, Stage.WIDTH, backgroundY + Stage.HEIGHT, this);

        //paint the obstacles
        for (Actor obstacle : obstacles) {
            obstacle.paint(g);
        }

        //paint the motorists
        for (Actor motorist : motorists) {
            motorist.paint(g);
        }

        //paint the player
        player.paint(g);

        backgroundY -= (int) player.getSpeed();
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
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        InputHandler inputHandler = new InputHandler(this, player);
        inputHandler.event = e;
        inputHandler.action = InputHandler.Action.PRESS;
        inputHandler.start();
    }

    /**
     * @param e
     */
    public void keyReleased(KeyEvent e) {
        InputHandler inputHandler = new InputHandler(this, player);
        inputHandler.event = e;
        inputHandler.action = InputHandler.Action.RELEASE;
        inputHandler.start();
    }

    /**
     * @param e
     */
    public void keyTyped(KeyEvent e) {
    }
}