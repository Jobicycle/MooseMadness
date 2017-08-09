package game;

import actors.Actor;
import actors.Obstacles.Moose;
import actors.Obstacles.Motorist;
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
    public List<Actor> powerUps;
    public List<Actor> clouds;

    private BufferedImage background, backgroundTile; //background cache
    private int backgroundY; //background cache position
    private GamePanels gamePanels;
    private Player player;
    private PowerUpManager powerUpManager;
    private ObstacleManager obstacleManager;
    private TrafficManager trafficManager;
    private CloudManager cloudManager;

    /**
     * Main game class
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
        Utils.loopMusic("musicloop.wav"); //stomp stomp stomp music
        game();
    }

    /**
     * initialize the world. Create actor lists and managers, player, and the background tile.
     */
    public void initWorld() {
        //add powerup, obstacle, and traffic managers and lists
        obstacles = new ArrayList<Actor>();
        motorists = new ArrayList<Actor>();
        powerUps = new ArrayList<Actor>();
        clouds = new ArrayList<Actor>();
        trafficManager = new TrafficManager(this, motorists);
        obstacleManager = new ObstacleManager(this, obstacles);
        powerUpManager = new PowerUpManager(this, powerUps);
        cloudManager = new CloudManager(this, clouds);
        player = new Player(this); //add player

        //load background
        backgroundTile = ResourceLoader.getInstance().getSprite("environment/road0.png");
        background = ResourceLoader.createCompatible(WIDTH, HEIGHT + backgroundTile.getHeight(), Transparency.OPAQUE);
        Graphics2D g = (Graphics2D) background.getGraphics();
        g.setPaint(new TexturePaint(backgroundTile, new Rectangle(0, 0, backgroundTile.getWidth(), backgroundTile.getHeight())));
        g.fillRect(0, 0, background.getWidth(), background.getHeight());
        backgroundY = backgroundTile.getHeight();
    }

    /**
     * main game loop. Allows player to cycle through different screens or is forced to gameover screen when necessary.
     */
    public void game() {
        Graphics g = strategy.getDrawGraphics();
        gamePanels = new GamePanels(player, g, strategy);

        switch (state) {
            case MENU:
                gamePanels.printMainMenu();
                break;

            case HIGHSCORES:
                gamePanels.printHighscores();
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

                //game over loop, updates actors, paints actors, tracks session run time, and calls calculate sleep time to keep frames at normal rate.
                gameLoop:
                while (isVisible()) {
                    gameUpdate();
                    paintWorld(g);
                    sessionRunTime += 0.01;
                    Utils.calculateSleepTime(usedTime, DESIRED_FPS);

                    if (state == GameState.GAMEOVER) { // break out of game loop, change state to game over, then re-enter game() method.
                        game();
                        break gameLoop;
                    } else if (state == GameState.PAUSE) { // break out of game loop, change state to pause, then re-enter game() method.
                        game();
                        break gameLoop;
                    }
                }
                break;
        }
    }

    /**
     * gameUpdate method handles updating all the actors in the game, checking for collisions, and asking managers if actors should be created.
     */
    private void gameUpdate() {
        //iterate through current obstacle list, check for collisions, remove unnecessary obstacles, update obstacle position based on player speed.
        updateActors(obstacles);
        updateActors(motorists);
        updateActors(powerUps);
        updateActors(clouds);

        //check for player collisions between obstacles, motorists, and powerUps
        Utils.checkCollision(player, obstacles);
        Utils.checkCollision(player, motorists);
        Utils.checkCollision(player, powerUps);
        player.update();

        for (Actor motorist : motorists) { //check motorists collisions
            Utils.checkCollision(motorist, obstacles);
            Utils.checkCollision(motorist, motorists);
        }

        //check for air horn activation
        if (player.isActivateHorn() && player.getNumberOfHorns() > 0) {
            Utils.playSound("sounds/horn.wav");
            applyHorn(motorists);
            applyHorn(obstacles);
            player.setNumberOfHorns(0);
        }

        //ask managers if objects should be added to object lists. Only add
        //powerups if player is driving higher than half speed + 1
        if (player.getSpeed() > player.getTopSpeed() / 2 + 1) {
            powerUpManager.randomWrench(sessionRunTime);
            powerUpManager.randomAirHorn(sessionRunTime);
        }
        obstacleManager.randomObstacle(sessionRunTime);
        trafficManager.randomMotorist(sessionRunTime);
        cloudManager.randomCloud(sessionRunTime);

        if (player.getHealth() <= 0) { //if game over
            state = GameState.GAMEOVER;
        }
    }

    /**
     * updateActors function takes in actor lists, checks for collisions, and removes flagged actors.
     * It also adjusts actor Y position according to player speed.
     *
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
     * applyHorn method used to tell Moose and Motorists actors to move left or right according to
     * their position relative to the player.
     *
     * @param actorList
     */
    private void applyHorn(List<Actor> actorList) {
        for (Actor actor : actorList) {
            if (actor instanceof Moose || actor instanceof Motorist) {
                if (player.getPosY() > actor.getPosY()) {
                    if (player.getPosX() + player.getWidth() / 2 > actor.getPosX()) {
                        actor.setVx(Utils.randFloat(-3, -2));
                    } else {
                        actor.setVx(Utils.randFloat(2, 3));
                    }
                }
            }
        }
    }

    /**
     * paintWorld draws the background, all actors, and ui.
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

        //paint the powerups
        for (Actor powerUp : powerUps) {
            powerUp.paint(g);
        }

        //paint the player
        player.paint(g);

        for (Actor cloud : clouds) {
            cloud.paint(g);
        }

        backgroundY -= (int) player.getSpeed();
        if (backgroundY < 0) {
            backgroundY = backgroundTile.getHeight();
        }

        gamePanels.printHealth();
        gamePanels.printScore();
        gamePanels.printSpeed();
        gamePanels.printAirHorns();
        gamePanels.printTimePlayed(sessionRunTime);

        //swap buffer
        strategy.show();
    }

    public void keyPressed(KeyEvent e) {
        InputHandler inputHandler = new InputHandler(this, player);
        inputHandler.event = e;
        inputHandler.action = InputHandler.Action.PRESS;
        inputHandler.start();
    }

    public void keyReleased(KeyEvent e) {
        InputHandler inputHandler = new InputHandler(this, player);
        inputHandler.event = e;
        inputHandler.action = InputHandler.Action.RELEASE;
        inputHandler.start();
    }

    public void keyTyped(KeyEvent e) {
    }
}